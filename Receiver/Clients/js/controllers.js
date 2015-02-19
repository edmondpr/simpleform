angular.module('clientApp.controllers', []).controller('ClientListController', function($scope, $state, $http, $modal, popupService, $window, Clients) {
  $scope.clients = Clients.query(); 

  $scope.updateClient = function() {
    var clientId = this.client._id.$oid;
    $http.put(utils.getDbUrl() + '/' + 
             clientId + '?apiKey=bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      { 'label': this.client.label,
        'value': this.client.value,
        'user': utils.getUser()
    }).success(function (data, status, headers, config) {

    })
  }

  $scope.newClient = function () {
    var modalInstance = $modal.open({      
      templateUrl: 'partials/client-add.html',
      controller: 'ClientCreateController'
    });
  }; 

  $scope.editClient = function (clientId) {
    var modalInstance = $modal.open({      
      templateUrl: 'partials/client-edit.html',
      controller: 'ClientEditController',
      resolve: {
        clientId: function () {
          return clientId;
        }
      }
    });
  }; 

  $scope.deleteClient = function(client) { 
    if (popupService.showPopup('Really delete this?')) {
      client.$delete(function() {
        $window.location.href = ''; //redirect to home
      });
    }
  };  
}).controller('ClientCreateController', function($scope, $state, $modalInstance, Clients) {
  $scope.client = new Clients();  //create new client instance. Properties will be set via ng-model on UI

  $scope.addClient = function() { 
    $scope.client.user = utils.getUser();
    $scope.client.$save(function() {
      location.href = ''; //redirect to home
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };    
}).controller('ClientEditController', function($scope, $state, $http, $modalInstance, clientId, Clients) { 
  $scope.loadClient = function() { 
    $scope.client = Clients.get({ id: clientId });
  };
 
  $scope.loadClient(); 

  $scope.updateClient = function() { 
    $http.put(utils.getDbUrl() + '/' + 
             clientId + '?apiKey=bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      { 'label': this.client.label,
        'value': this.client.value,
        'user': utils.getUser()
    }).success(function (data, status, headers, config) {
      location.href = '';
    })
  };

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };   
});

