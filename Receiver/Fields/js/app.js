angular.module('fieldApp', ['ui.router', 'ngResource', 'ui.bootstrap',  
                           'fieldApp.controllers', 'fieldApp.services']);
 
angular.module('fieldApp').config(function($stateProvider) {
  $stateProvider.state('fields', { // state for showing all fields
    url: '/fields',
    templateUrl: 'partials/fields.html',
    controller: 'FieldListController'
  });
}).run(function($state) {
  $state.go('fields'); //make a transition to fields state when app starts
});
