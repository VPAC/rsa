
// The workspace is the area that houses filters.
angular.module('graphEditor').directive('workspace', function($timeout) {
	return {
		restrict: 'E',
		transclude: true,

		// 1: container
		// 2: draggable for scrolling
		// 3: offset div to store scroll
		template: '<div class="workspace"><div class="fill"><div ng-transclude class="fill"></div></div></div>',
		replace: true,

		compile: function() {
			return {
				post: function(scope, iElement, iAttrs) {
					console.log('Creating workspace');

					var dragElem = iElement.children().first();
					var offsetElem = dragElem.children().first();

					// Set workspace as drop target for tools.
					iElement.droppable({
						greedy: true,
						activeClass: 'rsa-drop-active',
						hoverClass: 'rsa-drop-hover',
						accept: '.node-proto',
						drop: function(event, ui) {
							// Create a new node.
							var offset = offsetElem.offset();
							var protonode = ui.draggable.scope().$parent.tool;
							// Clone the node prototype.
							var node = $.extend(true, {
								position: {
									x: event.pageX - offset.left,
									y: event.pageY - offset.top
								}
							}, protonode);

							scope.nodes.push(node);
							scope.$apply();
						}
					});

					// Allow dragging of workspace for scrolling. After
					// dragging, transfer offset to another element and reset
					// position. Otherwise, the grabbable region will change.
					dragElem.draggable({
						stop: function(event, ui) {
							var pos = offsetElem.position();
							offsetElem
								.css('top', ui.position.top + pos.top)
								.css('left', ui.position.left + pos.left);
							dragElem
								.css('top', 0)
								.css('left', 0);
						},
						cancel: '.nodrag,.btn'
					});
					dragElem.disableSelection();

					// TODO: Not sure if this really belongs here... maybe it
					// can be done in Socket on a case-by-case basis?
					jsPlumb.importDefaults({
						Container: offsetElem
					});

					// Walk through the connections, as jsPlumb knows them, and
					// store them in the Angular model.
					var syncSocketModel = function(tSocketElem) {
						var tScope = tSocketElem.scope();
						var tSocket = tScope.socket;
						var vConnections = jsPlumb.getConnections({
							target: tSocketElem
						});
						var mConnections = [];
						for (var i = 0; i < vConnections.length; i++) {
							var conn = vConnections[i];
							var ref = vpac.socket.elemToRef(conn.source);
							mConnections.push(ref);
						}
						tSocket.connections = mConnections;
					};
					var suppressDetachCb = false;
					jsPlumb.bind("jsPlumbConnection", function(conn, event) {
						var tScope = conn.target.scope();
						var ref = vpac.socket.elemToRef(conn.source);

						var similarConnections = jsPlumb.getConnections({
							source: conn.source, target: conn.target
						});
						for (var i = 1; i < similarConnections.length; i++) {
							console.log('Removing duplicate connection', ref);
							suppressDetachCb = true;
							try {
								jsPlumb.detach(similarConnections[i]);
							} finally {
								suppressDetachCb = false;
							}
						}
						syncSocketModel(conn.target);
						if (!tScope.$$phase)
							tScope.$apply();
					});
					jsPlumb.bind("jsPlumbConnectionDetached", function(conn, event) {
						if (suppressDetachCb)
							return;
						var tScope = conn.target.scope();
						if (tScope === undefined) {
							console.log('Ignoring detach event for deleted socket');
							return;
						}
						syncSocketModel(conn.target);
						if (!tScope.$$phase)
							tScope.$apply();
					});

					scope.$watch('fullRepaintRequired', function(newDirty, oldDirty) {
						if (!newDirty)
							return;
						$timeout(function() {
							console.log('Redrawing all connections');
							if (scope.$$destroyed) {
								console.log('Not redrawing: element is not in document tree.');
								return;
							}
							jsPlumb.repaintEverything();
							scope.fullRepaintRequired = false;
						}, 0);
					});
				}
			};
		}
	};
});

// Nodes represent input datasets, filters, and output datasets.
angular.module('graphEditor').directive('node', function($timeout) {
	return {
		restrict: 'E',
		scope: false,

		controller: 'node',

		// templateUrl is currently broken when combined with ng-repeat; see
		// http://stackoverflow.com/questions/16160549/transcluded-element-not-being-deleted-on-removal-of-list-item-when-using-ng-repe
		//templateUrl: 'partials/node.html',
		template: '<div class="node {{node.type + \'-node\'}}" ng-style="{top: node.position.top + \'px\', left: node.position.left + \'px\'}">'
				+   '<h2>'
				+     '<span ng-show="canDestroyNode()" class="btn nodrag" ng-click="destroyNode(nodes, node)">x</span>{{node.name}}'
				+   '</h2>'
				+   '<ul class="input">'
				+     '<socket ng-repeat="socket in node.inputs" is-input="true"></socket>'
				+     '<li ng-show="canAddSocket(\'input\')"><span ng-click="addSocket(\'input\')" class="btn">+</span></li>'
				+   '</ul>'
				+   '<ul class="output">'
				+     '<socket ng-repeat="socket in node.outputs" is-input="false"></socket>'
				+     '<li ng-show="canAddSocket(\'output\')"><span ng-click="addSocket(\'output\')" class="btn">+</span></li>'
				+   '</ul>'
				+   '<div style="clear: both"></div>'
				+ '</div>',

		replace: true,
		compile: function(tElement, tAttrs, transclude) {
			console.log('compiling node');
			return {
				pre: function(scope, iElement, iAttrs) {
					// Set up two-way binding between centre of node and
					// top-left corner.
					scope.$watch('node.position.x', function(newX, oldX) {
						if (newX === undefined)
							return;
						var w = iElement.width();
						scope.node.position.left = newX - (w / 2);
					});
					scope.$watch('node.position.y', function(newY, oldY) {
						if (newY === undefined)
							return;
						var h = iElement.height();
						scope.node.position.top = newY - (h / 2);
					});
					scope.$watch('node.position.left', function(newLeft, oldLeft) {
						if (newLeft === undefined)
							return;
						var w = iElement.width();
						scope.node.position.x = newLeft + (w / 2);
					}, true);
					scope.$watch('node.position.top', function(newTop, oldTop) {
						if (newTop === undefined)
							return;
						var h = iElement.height();
						scope.node.position.y = newTop + (h / 2);
					});

					scope.$watch('dirty', function(newDirty, oldDirty) {
						if (!newDirty)
							return;
						$timeout(function() {
							console.log('Redrawing', scope.node.name);
							if (scope.$$destroyed) {
								console.log('Not redrawing: element is not in document tree.');
								return;
							}
							// Redraw connections.
							jsPlumb.doWhileSuspended(function() {
								jsPlumb.recalculateOffsets(iElement);
							});
							jsPlumb.repaint(iElement);
							scope.dirty = false;
						}, 0);
					});
				},

				post: function(scope, iElement, iAttrs) {
					console.log('Creating node', scope.node.name);
					jsPlumb.draggable(iElement, {
						// When dragging stops, record the new location in the
						// model.
						stop: function(event, ui) {
							scope.node.position.top = ui.position.top;
							scope.node.position.left = ui.position.left;
							scope.$apply();
						},
						cancel: '.nodrag,.btn'
					});
					iElement.resizable({
						minHeight: 50,
						minWidth: 80,
						autoHide: true,
						handles: 'se',
						resize: function(event, ui) {
							// Disable height resizing: this allows the element
							// to assume its natural height after reflowing
							// based on the new width.
							iElement.css('height', '');
							// Redraw connections.
							jsPlumb.doWhileSuspended(function() {
								jsPlumb.recalculateOffsets(iElement);
							});
							jsPlumb.repaint(iElement);
						}
					});
					scope.$on('$destroy', function(event) {
						console.log('destroying node');
						jsPlumb.doWhileSuspended(function() {
							jsPlumb.remove(iElement);
						}, true);
					});

				}
			};
		}
	};
});

// Finds a node and socket (model, not element) by URI.
// Returns:
//  - [node, socket], or
//  - [node, null] if the node is found but the socket is not, or
//  - null if the node can't be found.
vpac.socket = {};
vpac.socket.fromRef = function(ref, nodeMap, direction) {
	var re = /^#([^\/]+)(\/(.+))?$/;
	var match = re.exec(ref);
	if (match == null)
		return null;
	var nodeId = match[1];
	var socketName = match[3];
	var node = nodeMap[nodeId];
	if (node === undefined)
		return null;
	var sockets;
	if (direction == 'input')
		sockets = node.inputs;
	else if (direction == 'output')
		sockets = node.outputs;
	else
		sockets = node.inputs.concat(node.outputs);
	for (var i = 0; i < sockets.length; i++) {
		var socket = sockets[i];
		if (socket.name == socketName)
			return [node, socket];
	}
	return [node, null];
};
vpac.socket.toRef = function(node, socket) {
	return '#' + node.id + '/' + socket.name;
};
// 
vpac.socket.elemToRef = function(socket) {
	var scope = socket.scope();
	var node = scope.$parent.node;
	return vpac.socket.toRef(node, scope.socket);
};
// Tests whether an object matches some set of criteria (by name).
vpac.matches = function(ob, criteria) {
	if (criteria == null)
		return true;
	for (var field in criteria) {
		if (ob[field] != criteria[field])
			return false;
	}
	return true;
};
// Finds a socket that is connected to
vpac.socket.findConnection = function(socket, nodeMap, nodeCriteria, socketCriteria) {
	for (var i = 0; i < socket.connections.length; i++) {
		var ref = socket.connections[i];
		var sockPair = vpac.socket.fromRef(ref, nodeMap);
		if (sockPair == null)
			continue;
		var otherNode = sockPair[0];
		var otherSocket = sockPair[1];
		if (!vpac.matches(otherNode, nodeCriteria))
			continue;
		if (!vpac.matches(otherSocket, socketCriteria))
			continue;
		return sockPair;
	}
	return null;
};
vpac.socket.isLiteral = function(socket) {
	var re = /byte|short|int|long|float|double|String/;
	return re.exec(socket.type) != null;
};

angular.module('graphEditor').directive('socket', function($timeout) {
	var scalarParams = {
		connectorStyle: {
			strokeStyle: '#ddd',
			lineWidth: 4,
			outlineWidth: 1,
			outlineColor: '#777'
		},
		paintStyle: {
			fillStyle: '#aaa',
			outlineColor: '#444',
			outlineWidth: 1
		},
		endpoint: [ 'Dot', {
			radius: 6
		} ],
	};
	var metaParams = {
		connectorStyle: {
			strokeStyle: '#6b6',
			'stroke-dasharray': '3 3',
			lineWidth: 2,
			outlineWidth: 0,
			//outlineColor: '#696'
		},
		paintStyle: {
			fillStyle: '#8d8',
			outlineColor: '#474',
			outlineWidth: 1
		},
		endpoint: [ 'Dot', {
			radius: 6
		} ],
	};
	var axisParams = {
			connectorStyle: {
				strokeStyle: '#e88',
				'stroke-dasharray': '3 3',
				lineWidth: 2,
				outlineWidth: 0,
				//outlineColor: '#966'
			},
			paintStyle: {
				fillStyle: '#d88',
				outlineColor: '#744',
				outlineWidth: 1
			},
			endpoint: [ 'Dot', {
				radius: 6
			} ],
		};
	var inParams = {
		maxConnections: -1,
		uniqueEndpoint: true,
		isSource: false,
		isTarget: true,
		// Left side, just above the middle (to account for text baseline).
		anchor: [ 0, 0.4, -1, 0 ]
	};
	var outParams = {
		maxConnections: -1,
		uniqueEndpoint: true,
		isSource: true,
		isTarget: false,
		// Right side, just above the middle (to account for text baseline).
		anchor: [ 1, 0.4, 1, 0 ]
	};

	return {
		restrict: 'E',
		template: '<li ng-dblclick="editSocket(socket)">'
			+ '{{getSocketName(socket)}}'
			+ '<span ng-show="showValue(socket)">: {{socket.value}}</span>'
			+ '<span ng-show="canRemoveSocket(socket)" ng-click="destroySocket(socket)" class="btn actions">-</span>'
			+ '</li>',

		replace: true,
		compile: function(tElement, tAttrs, transclude) {
			console.log('compiling socket');
			return {
				post: function(scope, iElement, iAttrs) {
					console.log('Creating socket', scope.node.name + '/' + scope.socket.name);

					if (vpac.socket.isLiteral(scope.socket))
						return;

					// Add a jsPlumb endpoint to this socket - either at the
					// left or right, depending on whether this socket is an
					// input or output.
					var baseParams;
					var types = scope.socket.type.split(',');
					if (vpac.contains(types, 'meta'))
						baseParams = metaParams;
					else if (vpac.contains(types, 'axis'))
						baseParams = axisParams;
					else
						baseParams = scalarParams;

					var params;
					if (iAttrs.isInput == 'true')
						params = $.extend(true, {}, baseParams, inParams);
					else
						params = $.extend(true, {}, baseParams, outParams);
					jsPlumb.addEndpoint(iElement, {}, params);

					// Notify parent node that the sockets have changed.
					scope.socketsChanged();

					// Bind from the connection field back to jsPlumb.
					scope.$watch('socket.connections', function(newValue, oldValue) {
						console.log('Connection changed', vpac.socket.elemToRef(iElement), oldValue, newValue);
						// If the socket is an output, it will not list its
						// connections.
						if (scope.socket.connections === undefined)
							return;

						// Remove existing connections that are no longer listed
						// in the model.
						var existingConnections = jsPlumb.getConnections({target: iElement});
						for (var i = 0; i < existingConnections.length; i++) {
							var conn = existingConnections[i];
							var ref = vpac.socket.elemToRef(conn.source);
							var index = vpac.indexOf(scope.socket.connections, ref);
							if (index < 0) {
								jsPlumb.detach(conn);
							}
						}
						// Create connections that are listed but don't exist
						// yet. This needs to happen after the pending scope
						// digests (i.e. inside $timeout), because this watch
						// function may be called before all the nodes that are
						// currently in the model have been created.
						$timeout(function() {
							var sockets = angular.element('li._jsPlumb_endpoint_anchor_');
							for (var i = 0; i < sockets.length; i++) {
								var ref = vpac.socket.elemToRef(sockets.eq(i));
								if (ref == null) {
									console.log('Warning: reference is broken:', ref);
									continue;
								}
								var index = vpac.indexOf(newValue, ref);
								if (index < 0)
									continue;
								var existingConnections = jsPlumb.getConnections({
									source: sockets.eq(i),
									target: iElement
								});
								if (existingConnections.length > 0)
									continue;
								var source = sockets.eq(i);
								var target = iElement;
								jsPlumb.connect({
									source: jsPlumb.getEndpoints(source)[0],
									target: jsPlumb.getEndpoints(target)[0]
								});
							}
						}, 0);

					}, true);

					// On destruction, the endpoints must be manually removed.
					// jsPlumb drawing must be suspended, because otherwise
					// endpoint deletion triggers a redraw which fails because
					// the socket element has already been removed.
					scope.$on('$destroy', function(event) {
						console.log('destroying socket', scope.socket.name);
						jsPlumb.doWhileSuspended(function () {
							// Need to use remove instead of deleting each
							// endpoint: it does extra stuff!
							// https://github.com/sporritt/jsPlumb/issues/30
							jsPlumb.remove(iElement);
						}, true);
						scope.socketsChanged();
					});
				}
			};
		}
	};
});

// Tools are prototypes for nodes. The user can drag them from the toolbox onto
// the workspace.
angular.module('graphEditor').directive('tool', function() {
	return {
		restrict: 'E',
		scope: true,

		controller: function($scope) {
		},

		// templateUrl is currently broken when combined with ng-repeat; see
		// http://stackoverflow.com/questions/16160549/transcluded-element-not-being-deleted-on-removal-of-list-item-when-using-ng-repe
		//templateUrl: 'partials/tool.html',
		template: '<li ui-jq="tooltip" '
				+ 'title="{{tool.description}}\n\n'
				+ '{{tool.qualname}}\n'
				+ 'Inputs: {{tool.inputs.length}}, outputs: {{tool.outputs.length}}" '
				+ 'ui-options="{position: {my: \'left top\', at: \'right top\'}}" '
				+ 'class="node-proto" '
				+ '><span>{{tool.name}}</span></li>',

		replace: true,
		compile: function(tElement, tAttrs, transclude) {
			console.log('compiling tool');
			return {
				post: function(scope, iElement, iAttrs) {
					console.log('Creating tool', scope.tool.name);
					iElement.draggable({
						// When dragging starts, create a clone so that the tool
						// can be dragged into different divs.
						helper: function(event) {
							var helperOb = $(this).clone();
							helperOb.width($(this).width()).appendTo('body');
							return helperOb;
						}
					});
					scope.$on('$destroy', function(event) {
						console.log('destroying tool');
					});
				}
			};
		}
	};
});

angular.module('graphEditor').directive('preview', function() {
	return {
		restrict: 'E',
		scope: true,

		controller: function($scope) {
		},

		template: '<li class="preview" ng-click="selectPreview(result)" '
			+     'ng-class="{ultimate: $index == MAX_PREVIEW_ITEMS, primary: $index == 0, error: !result.success}">'
			+   '<a class="thumbnail">'
			+     '<h3>{{result.seq}}</h3>'
			+     '<p ng-show="!result.success"><span ng-bind-html="result.error"></span></p>'
			+     '<img ng-show="result.success" ng-src="data:image/png;base64,{{result.data}}">'
			+   '</a>'
			+ '</li>',

		replace: true,
		compile: function(tElement, tAttrs, transclude) {
			console.log('compiling preview');
			return {
				post: function(scope, iElement, iAttrs) {
					console.log('Creating preview');
					scope.$on('$destroy', function(event) {
						console.log('destroying preview');
					});
				}
			};
		}
	};
});

(function () {
	var refRe = /^rsa:(.*)$/;
	var serialiseInput = function(node, preview) {
		// TODO: This URI mutation is probably the responsibility of the preview
		// web service. Should send a normal query to it instead of modifying it
		// here.
		var ref;
		if (preview) {
			var match = refRe.exec(node.qualname);
			ref = 'preview:' + match[1];
		} else {
			ref = node.qualname;
		}
		return '\t<input id="' + node.id + '" href="' + ref + '"/>\n';
	};

	var serialiseOutput = function(node, nodeMap) {
		var out = '\t<output id="' + node.id + '">\n';
		for (var i = 0; i < node.inputs.length; i++) {
			var socket = node.inputs[i];
			var types = socket.type.split(',');

			// Handle grid socket specially.
			if (vpac.contains(types, 'meta') && socket.name == 'grid') {
				out += '\t\t<grid';
				if (socket.connections.length > 0) {
					var ref = socket.connections[0];
					var sourceSockPair = vpac.socket.fromRef(ref, nodeMap);
					if (sourceSockPair != null)
						out += ' ref="#' + sourceSockPair[0].id + '"';
				}
				out += '/>\n';

			} else {
				out += '\t\t<variable name="' + socket.name + '"';
				for (var j = 0; j < socket.connections.length; j++) {
					var ref = socket.connections[j];
					out += ' ref="' + ref + '"';
				}
				out += '/>\n';
			}
		}
		out += '\t</output>\n';
		return out;
	};

	var createNodeMap = function(nodes) {
		var nodeMap = {};
		for (var i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			nodeMap[node.id] = node;
		}
		return nodeMap;
	};

	var serialiseFilter = function(node) {
		var out = '\t<filter id="' + node.id + '" cls="' + node.qualname + '">\n';
		for (var i = 0; i < node.inputs.length; i++) {
			var socket = node.inputs[i];
			if (socket.connections !== undefined) {
				out += '\t\t<sampler name="' + socket.name + '"'
				if (socket.connections.length == 0)
					out += '/>\n';
				else if (socket.connections.length == 1)
					out += ' ref="' + socket.connections[0] + '"/>\n';
				else {
					out += '>\n';
					for (var j = 0; j < socket.connections.length; j++) {
						var ref = socket.connections[j];
						out += '\t\t\t<sampler ref="' + ref + '"/>\n';
					}
					out += '\t\t</sampler>\n';
				}
			} else {
				if (socket.value != '')
					out += '\t\t<literal name="' + socket.name + '" value="' + socket.value + '"/>\n';
			}
		}
		out += '\t</filter>\n';
		return out;
	};

	vpac.serialiseQuery = function(nodes, params) {
		var options = $.extend({
			preview: false
		}, params);
		console.log('Serialising to rsaquery', options);

		var out = '<?xml version="1.0" encoding="UTF-8"?>\n<query xmlns="http://www.vpac.org/namespaces/rsaquery-0.2">\n';

		// Accumulate output bindings. These are special connections that go in
		// reverse!
		var nodeMap = createNodeMap(nodes);

		// List inputs
		for (var i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			if (node.type != 'input')
				continue;
			out += serialiseInput(node, options.preview);
		}
		out += '\n';

		// List outputs
		for (var i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			if (node.type != 'output')
				continue;
			out += serialiseOutput(node, nodeMap);
		}
		out += '\n';

		// List filters
		for (var i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			if (node.type != 'filter')
				continue;
			out += serialiseFilter(node);
		}

		out += '</query>';
		return out;
	};
})();

angular.module('graphEditor').filter('rsaquery', function() {
	return vpac.serialiseQuery;
});


