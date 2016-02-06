'use strict';

// Declare app level module which depends on views, and components
angular.module('simpleFormApp', [
  'ngRoute',
  'ngAnimate', 
  'mgcrea.ngStrap',
  'smart-table',
  'simpleFormApp.queueTable',
  'simpleFormApp.version'
])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/queueTable', {
		templateUrl: 'queueTable/queueTable.html',
		controller: 'queueTableCtrl'
	});
  	$routeProvider.otherwise({redirectTo: '/queueTable'});
}]);
