angular.module('fieldApp.controllers', []).controller('FieldListController', function($scope, $state, $http, $modal, popupService, $window, Fields) {
  $scope.fields = Fields.query(); 

  // generate QRCode with fields
  /*var qrCode = new QRCode("qrcode");
  qrCode.makeCode("mongolab.simpleform.com/clients_fields/edmondpr");
  $scope.fields.$promise.then(function(data) {
    var qrCodeArray = new Array();
    for (var i=0; i<data.length; i++) {
      var qrCodePair = new Object();
      qrCodePair[data[i].label] = data[i].value;
      qrCodeArray[i] = qrCodePair;
    }
    
  });*/

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
});

