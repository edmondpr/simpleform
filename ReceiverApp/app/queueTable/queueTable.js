'use strict';

angular.module('simpleFormApp.queueTable', [])

.controller('queueTableCtrl', function($scope, $http, $timeout) {

    $scope.isLoading = false;
    $scope.rowCollection = [];
    $scope.displayedCollection = [];
    
    $scope.columns = ['label', 'value', 'position'];
    
	$http.get('https://api.parse.com/1/classes/ClientsFields', {
	    headers: {
	        'X-Parse-Application-Id': "HxlZ3d7O3BuGM6oION0qPLrtrh5TcqnGR1eRecmA",
	        'X-Parse-REST-API-Key': "CaQYDxjD1iuYBOh8A0l3HNsDoNqbTSnZVbUzYHrK"
	    }
	}).then(function(response) {
		for (var i in response.data.results) {
        	$scope.rowCollection.push(response.data.results[i]);
    	}
    	$scope.displayedCollection  = [].concat($scope.rowCollection);
    });  

});