angular.module('clientApp', ['ui.router', 'ngResource', 'ui.bootstrap',  
                           'clientApp.controllers', 'clientApp.services']);
 
angular.module('clientApp').config(function($stateProvider) {
  $stateProvider.state('clients', { // state for showing all clients
    url: '/clients',
    templateUrl: 'partials/clients.html',
    controller: 'ClientListController'
  });
}).run(function($state) {
  $state.go('clients'); //make a transition to clients state when app starts
});
