angular.module('fieldApp.controllers', []).controller('FieldListController', function($scope, $state, $http, $modal, popupService, $window, Fields) {
  $scope.fields = Fields.query(); 

  $scope.updateField = function() {
    var fieldId = this.field._id.$oid;
    $http.put(utils.getDbUrl() + '/' + 
             fieldId + '?apiKey=bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      { 'label': this.field.label,
        'value': this.field.value,
        'user': utils.getUser()
    }).success(function (data, status, headers, config) {

    })
  }

  $scope.newField = function () {
    var modalInstance = $modal.open({      
      templateUrl: 'partials/field-add.html',
      controller: 'FieldCreateController'
    });
  }; 

  $scope.editField = function (fieldId) {
    var modalInstance = $modal.open({      
      templateUrl: 'partials/field-edit.html',
      controller: 'FieldEditController',
      resolve: {
        fieldId: function () {
          return fieldId;
        }
      }
    });
  }; 

  $scope.deleteField = function(field) { 
    if (popupService.showPopup('Really delete this?')) {
      field.$delete(function() {
        $window.location.href = ''; //redirect to home
      });
    }
  };  
}).controller('FieldCreateController', function($scope, $state, $modalInstance, Fields) {
  $scope.field = new Fields();  //create new field instance. Properties will be set via ng-model on UI

  $scope.addField = function() { 
    $scope.field.user = utils.getUser();
    $scope.field.$save(function() {
      location.href = ''; //redirect to home
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };    
}).controller('FieldEditController', function($scope, $state, $http, $modalInstance, fieldId, Fields) { 
  $scope.loadField = function() { 
    $scope.field = Fields.get({ id: fieldId });
  };
 
  $scope.loadField(); 

  $scope.updateField = function() { 
    $http.put(utils.getDbUrl() + '/' + 
             fieldId + '?apiKey=bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      { 'label': this.field.label,
        'value': this.field.value,
        'user': utils.getUser()
    }).success(function (data, status, headers, config) {
      location.href = '';
    })
  };

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };   
}).controller('ScanController', function($scope, $state, $http, $modal, Fields) { 
  $scope.code = '';  
  $scope.openReceiveForm = function () {
    var modalInstance = $modal.open({      
      templateUrl: 'partials/receive-form.html',
      controller: 'ReceiveFormController',
      resolve: {
        code: function () {
          return $scope.code;
        }
      }
    });
  };  

  $scope.openSendForm = function () {
    var modalInstance = $modal.open({      
      templateUrl: 'partials/send-form.html',
      controller: 'SendFormController',
      resolve: {
        code: function () {
          return $scope.code;
        }
      }
    });
  };    
}).controller('ReceiveFormController', function($scope, $state, $http, $modalInstance, code, ClientsFields) { 
  $scope.code = code;
  var clientsFields = ClientsFields.query();
  $scope.fields = new Array();

  clientsFields.$promise.then(function(data) {
    for (var i=0; i<data.length; i++) {
        if (data[i].user == code) {   
            var field = new Object({"label": data[i].label, "value": data[i].value});
            $scope.fields.push(field);
        }
    }
  });  

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };   
}).controller('SendFormController', function($scope, $state, $http, $modalInstance, code, ClientsFields) { 
  $scope.code = code;
  var clientsFields = ClientsFields.query();
  $scope.fields = new Array();

  clientsFields.$promise.then(function(data) {
    for (var i=0; i<data.length; i++) {
        if (data[i].user == code) {   
            var field = new Object({"label": data[i].label, "value": data[i].value});
            $scope.fields.push(field);
        }
    }
  });

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };   
});

