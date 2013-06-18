'use strict';

angular.module('graphEditor').controller('GraphEditor', function GraphEditor($scope, datasets, filters, $http) {
	console.log('init graph editor');

	// Allow debugging on-demand: press F7 to prevent the page from updating.
	$(window).keydown(function(e) {
		if (e.keyCode == 118)
			debugger;
	});

	$scope.node_map = {};
	$scope.getUniqueNodeId = function(prefix) {
		var re = /\W+/g;
		prefix = prefix.replace(re, '_');
		var i = 0;
		while (true) {
			var id = prefix + '_' + i++;
			if ($scope.node_map[id] === undefined)
				return id;
		}
	};
	$scope.$watch('nodes', function(newNodes, oldNodes) {
		console.log('Nodes changed');
		// Remove old nodes from the map.
		for (var i = 0; i < oldNodes.length; i++) {
			var node = oldNodes[i];
			if (vpac.indexOf(newNodes, node) < 0)
				delete $scope.node_map[node.id];
		}
		// Ensure each node has an ID, and is in the map.
		for (var i = 0; i < newNodes.length; i++) {
			var node = newNodes[i];
			if (node.id === undefined) {
				node.id = $scope.getUniqueNodeId(node.name);
			}
			if ($scope.node_map[node.id] === undefined)
				$scope.node_map[node.id] = node;
			for (var j = 0; j < node.inputs.length; j++) {
				var socket = node.inputs[j];
				if (!vpac.socket.isLiteral(socket)) {
					socket.value = undefined;
					if (socket.connections === undefined)
						socket.connections = [];
				} else {
					socket.connections = undefined;
					if (socket.value === undefined)
						socket.value = '';
				}
			}
		}
	}, true);

	$scope.toolbox = [ {
		name: 'Inputs',
		values: []
	}, {
		name: 'Filters',
		values: []
	} ];

	$scope.fetchTools = function() {
		console.log('Refreshing');
		$scope.toolbox[0] = datasets.get();
		$scope.toolbox[1] = filters.get();
	};
	$scope.fetchTools();

	$scope.currentPreview = null;
	$scope.previewImages = [];
	$scope.getPreviewImages = function() {
		if ($scope.currentPreview == null)
			return $scope.previewImages;
		else
			return [$scope.currentPreview].concat($scope.previewImages);
	};

	var previewNum = 0;
	$scope.MAX_PREVIEW_ITEMS = 6;
	$scope.fetchPreview = function() {
		var query = vpac.serialiseQuery($scope.nodes, {preview: true});
		console.log('Fetching preview', query);
		var requestData = $.param({query: query});
		var state = angular.copy($scope.nodes);
		vpac.removeAngularHash(state);
		console.log(state);
		$http({
			method: 'POST',
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			url: '../Data/MultiPreviewQuery',
			data: requestData

		}).success(function(data, status, headers, config) {
			console.log('fetched', status, data);
			var preview = {
				seq: ++previewNum,
				data: data,
				nodeGraph: state,
				success: true
			};
			$scope.previewImages = vpac.pushFront($scope.previewImages, preview,
					$scope.MAX_PREVIEW_ITEMS);
			$scope.currentPreview = preview;
			console.log($scope.previewImages);

		}).error(function(data, status, headers, config) {
			console.log('Error fetching preview:', status, data);
			var errstring;
			// First try to find an exception with an error message; just display the message.
			var errmatch = /root cause.*?QueryConfigurationException: (.*)$/m.exec(data);
			if (errmatch == null) {
				var errmatch = /root cause.*?([a-zA-Z0-9_]+: .*)$/m.exec(data);
			}
			if (errmatch == null) {
				// If no message, display the class name.
				errmatch = /root cause.*?([a-zA-Z0-9_.]+)$/m.exec(data);
			}
			if (errmatch == null) {
				// Neither matched; just show the whole error report.
				errstring = data;
			} else {
				errstring = errmatch[1];
			}
			var preview = {
				seq: ++previewNum,
				error: errstring,
				nodeGraph: state,
				success: false
			};
			$scope.previewImages = vpac.pushFront($scope.previewImages, preview,
					$scope.MAX_PREVIEW_ITEMS);
			$scope.currentPreview = preview;
		});
	};
	$scope.selectPreview = function(preview) {
		$scope.currentPreview = preview;
		$scope.restoreState(preview.nodeGraph)
	};
	$scope.restoreState = function(nodeGraph) {
		$scope.nodes = angular.copy(nodeGraph);
		$scope.fullRepaintRequired = true;
	};

	$scope.nodes = [ {
	    "name": "Output",
	    "id": "output",
	    "type": "output",
	    "position": {
	      "y": 102.7890625,
	      "x": 776.5,
	      "left": 708,
	      "top": 23
	    },
	    "inputs": [
	      {
	        "name": "grid",
	        "connections": [],
	        "type": "meta",
	        "dimensions": ""
	      },
	      {
	        "name": "band",
	        "connections": [
	          "#Blur_0/output"
	        ],
	        "type": "scalar",
	        "dimensions": ""
	      }
	    ],
	    "outputs": []
	  },
	  {
	    "position": {
	      "x": 101,
	      "y": 59,
	      "left": 32.5,
	      "top": 39.2109375
	    },
	    "description": "",
	    "outputs": [
	      {
	        "name": "grid",
	        "type": "meta"
	      },
	      {
	        "name": "time",
	        "type": "scalar,axis"
	      },
	      {
	        "name": "y",
	        "type": "scalar,axis"
	      },
	      {
	        "name": "x",
	        "type": "scalar,axis"
	      },
	      {
	        "name": "B30",
	        "type": "scalar"
	      },
	      {
	        "name": "B40",
	        "type": "scalar"
	      },
	      {
	        "name": "B50",
	        "type": "scalar"
	      }
	    ],
	    "qualname": "rsa:small_landsat/100m",
	    "inputs": [],
	    "name": "small_landsat",
	    "type": "input",
	    "id": "small_landsat_0"
	  },
	  {
	    "position": {
	      "x": 419.5,
	      "y": 208.7890625,
	      "left": 351,
	      "top": 189
	    },
	    "description": "Blur pixel using 2D Gaussian kernel",
	    "outputs": [
	      {
	        "name": "output",
	        "type": "Cell"
	      }
	    ],
	    "qualname": "org.vpac.ndg.query.Blur",
	    "inputs": [
	      {
	        "name": "input",
	        "type": "PixelSource",
	        "connections": [
	          "#small_landsat_0/B30"
	        ]
	      }
	    ],
	    "name": "Blur",
	    "type": "filter",
	    "id": "Blur_0"
	  } ];
});


angular.module('graphEditor').controller('node', function($scope, $element, $dialog) {
	console.log('Node controller', $scope.node.name);

	$scope.destroyNode = function(nodes, node) {
		console.log($scope);
		var i = vpac.indexOf(nodes, node);
		console.log('Destroying node', node.name, i);
		nodes.splice(i, 1);
	};
	$scope.canDestroyNode = function() {
		return $scope.node.type != 'output';
	}

	// SOCKETS

	// When a socket is created, it calls this function. This queues up
	// redraw of the connections and endpoints, because jsPlumb can't
	// calculate the offsets until Angular has finished doing its thang.
	$scope.socketsChanged = function() {
		console.log('Sockets changed:', $scope.node.name);
		$scope.dirty = true;
	};

	$scope.getSocketName = function(socket) {
		var lowerCaseFirstChar = function(str) {
			return str.charAt(0).toLowerCase() + str.substr(1);
		};
		var match = /^(input|output)(.+)$/.exec(socket.name);
		if (match != null)
			return lowerCaseFirstChar(match[2]);
		var match = /^(in|out)(.+)$/.exec(socket.name);
		if (match != null && match[2] != 'put')
			return lowerCaseFirstChar(match[2]);

		// Just return original name. No camel case here, so don't change case.
		return socket.name;
	};

	$scope.showValue = function(socket) {
		return vpac.socket.isLiteral(socket);
	};

	$scope.canAddSocket = function(direction) {
		return $scope.node.type == 'output' && direction == 'input';
	};

	$scope.canRemoveSocket = function(socket) {
		return $scope.node.type == 'output';
	};

	$scope.addSocket = function(direction) {
		var socket = {
			name: 'Band',
			type: 'scalar'
		};
		if (direction == 'input') {
			socket.connections = [];
			$scope.node.inputs.push(socket);
		} else {
			$scope.node.outputs.push(socket);
		}
	};

	var editOutputInputSocket = function(socket) {
		var d = $dialog.dialog({
			controller: 'editSocketDialog',
			resolve: {
				socket: function() {
					return angular.copy(socket);
				},
				node: function() {
					return angular.copy($scope.node);
				}
			},
			templateUrl: 'template/outputSocketDialog.html'
		});
		d.open().then(function(result) {
			console.log('Dialog result:', result, $scope);
			if (!result)
				return;
			// For now, we don't need to update references because only
			// sockets of the output node can be edited, and it only has
			// input sockets.
			socket.name = result.name;
			socket.dimensions = result.dimensions;
			// The width may have changed, so queue a redraw.
			$scope.dirty = true;
		});
	};
	var editFilterLiteralInputSocket = function(socket) {
		var d = $dialog.dialog({
			controller: 'editSocketDialog',
			resolve: {
				socket: function() {
					return angular.copy(socket);
				},
				node: function() {
					return angular.copy($scope.node);
				}
			},
			templateUrl: 'template/literalSocketDialog.html'
		});
		d.open().then(function(result) {
			console.log('Dialog result:', result, $scope);
			if (!result)
				return;
			socket.value = result.value;
			// The width may have changed, so queue a redraw.
			$scope.dirty = true;
		});
	};
	$scope.editSocket = function(socket) {
		console.log('editSocket', socket);
		if ($scope.node.type == 'output')
			editOutputInputSocket(socket);
		else if ($scope.node.type == 'filter' && vpac.socket.isLiteral(socket))
			editFilterLiteralInputSocket(socket);
		else
			console.log("Can't edit " + socket.type + " socket of " + $scope.node.type + " node.");
	};

	$scope.destroySocket = function(socket) {
		console.log('destroySocket', socket);
		var i;
		i = vpac.indexOf($scope.node.inputs, socket);
		if (i >= 0) {
			console.log('Destroying input socket', socket.name, i);
			$scope.node.inputs.splice(i, 1);
		} else {
			i = vpac.indexOf($scope.node.outputs, socket);
			console.log('Destroying output socket', socket.name, i);
			$scope.node.outputs.splice(i, 1);
		}
	};
});

angular.module('graphEditor').controller('editSocketDialog', function($scope, dialog, socket, node) {
	console.log('editSocket scope', $scope);
	$scope.socket = socket;
	$scope.node = node;

	$scope.close = function(socket) {
		dialog.close(socket);
	};
});