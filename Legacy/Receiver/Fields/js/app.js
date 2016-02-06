angular.module('fieldApp', ['ui.router', 'ngResource', 'ui.bootstrap',  
                           'fieldApp.controllers', 'fieldApp.services']);
 
angular.module('fieldApp').config(function($stateProvider) {
  $stateProvider.state('fields', { // state for showing all fields
    url: '/fields',
    templateUrl: 'partials/fields.html',
    controller: 'FieldListController'
  });
}).config(function($stateProvider) {
  $stateProvider.state('scan', { // state for scan
    url: '/scan',
    templateUrl: 'partials/scan.html',
    controller: 'ScanController'
  });
}).config(function($stateProvider) {
  $stateProvider.state('clients', { // state for scan
    url: '/clients',
    templateUrl: 'partials/clients.html',
    controller: 'ClientListController'
  });
}).config(function($stateProvider) {
  $stateProvider.state('send-invite', { // state for scan
    url: '/send-invite',
    templateUrl: 'partials/send-invite.html',
    controller: 'SendInviteController'
  });
}).run(function($state) {
  $state.go('fields'); //make a transition to fields state when app starts
});
