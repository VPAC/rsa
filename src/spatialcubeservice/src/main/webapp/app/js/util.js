
if (vpac === undefined)
	var vpac = {};

vpac.subclass = function(base, sub) {
	var dummy = function() {};
	dummy.prototype = base.prototype;
	sub.prototype = new dummy();
	sub.prototype.constructor = sub;
	return sub;
};

vpac.subclassError = function(base, name) {
	return vpac.subclass(base, function(message) {
		base.call(this);
		this.name = name;
		this.message = message || '';
	});
};

vpac.indexOf = function(arr, elem) {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i] == elem) {
			return i;
		}
	}
	return -1;
};

vpac.pushFront = function(arr, elem, limit) {
	var newArr = [elem].concat(arr);
	if (limit !== undefined)
		return newArr.splice(0, limit);
	else
		return newArr;
};

vpac.contains = function(arr, elem) {
	return vpac.indexOf(arr, elem) >= 0;
};

vpac.removeAngularHash = function(obj) {
	if (obj instanceof Array) {
		for (var i = 0; i < obj.length; i++)
			vpac.removeAngularHash(obj[i]);
	} else if (obj instanceof Object) {
		if (obj['$$hashKey'] !== undefined)
			delete obj['$$hashKey'];
		for (var attr in obj)
			vpac.removeAngularHash(obj[attr]);
	}
};

vpac.datasetNameFromQualname = function(qualname) {
	// qualname is in the format rsa:small_landsat/100m
	// this will return small_landsat
	return qualname.substring(qualname.indexOf(":")+1,qualname.indexOf("/"))
	
}

//WebFont.load({
//	google: {
//		families: [ 'Inder' ]
//	},
//	active: function() {
//		console.log('Fonts loaded');
//		jsPlumb.recalculateOffsets();
//		jsPlumb.repaintEverything();
//	}
//});
