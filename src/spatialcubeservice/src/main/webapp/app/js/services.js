angular.module('graphEditor', ['ui', 'ui.bootstrap.tabs', 'ui.bootstrap.dialog', 'ngResource', 'ngSanitize']);

angular.module('graphEditor').factory('datasets', function ($resource) {
	return $resource('../QueryDataset.json', {}, {});
});

angular.module('graphEditor').factory('filters', function ($resource) {
	return $resource('../QueryFilter.json', {}, {});
});
