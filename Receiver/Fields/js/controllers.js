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
}).controller('ClientListController', function($scope, $state, $http, $modal, popupService, $window, ClientsFields) {
  var clientsFields = ClientsFields.query(); 
  $scope.clients = new Array();
  var users = new Array();

  clientsFields.$promise.then(function(data) {
    // Sort list based on user
    data.sort(function(a, b){
        if(a.user < b.user) return -1;
        if(a.user > b.user) return 1;
        return 0;
    });
    
    // Get list of users
    var user = data[0].user;
    var client = {};
    $scope.clients = new Array();
    for (var i=0; i<data.length; i++) {      
      if (data[i].label == "Name") {
        client.name = data[i].value;
      } else if (data[i].label == "Surname") {
        client.surname = data[i].value;
      } 
      if (data[i+1] == undefined || data[i+1].user != user) {
        $scope.clients.push(client);
        client = {};
        user = data[i+1].user;
      }           
    }
   
  });

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
}).controller('ClientCreateController', function($scope, $state, $modalInstance, ClientsFields) {
  $scope.client = new ClientsFields();  //create new client instance. Properties will be set via ng-model on UI

  $scope.addClient = function() { 
    $scope.client.user = utils.getUser();
    $scope.client.$save(function() {
      location.href = ''; //redirect to home
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };    
}).controller('ClientEditController', function($scope, $state, $http, $modalInstance, clientId, ClientsFields) { 
  $scope.loadClient = function() { 
    $scope.client = ClientsFields.get({ id: clientId });
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
}).controller('SendInviteController', function($scope, $state, $http, ClientsRegdata, ClientsFields, Fields, ClientsTemplates) { 
  var fields = Fields.query(); 
  var clientTemplate = new ClientsTemplates();
  var clientRegdata = new ClientsRegdata();
  $scope.templateFields = new Array();

  fields.$promise.then(function(data) {
    data.sort(function(a, b){
        if(a.position < b.position) return -1;
        if(a.position > b.position) return 1;
        return 0;
    });    
    for (var i=0; i<data.length; i++) {
      $scope.templateFields[i] = {};
      $scope.templateFields[i].label = data[i].label; 
      $scope.templateFields[i].position = data[i].position; 
      $scope.templateFields[i].connect = data[i].connect;
    }
  });

  $scope.createTemplate = function() { 
    var templateFields = $scope.templateFields;
    for (var i=0; i<templateFields.length; i++) {
      if (templateFields[i].label == "Name") {
        clientTemplate.user = templateFields[i].value;
      } else if (templateFields[i].label == "Surname") {
        clientTemplate.user += "." + templateFields[i].value;
      }
    }
    for (var i=0; i<templateFields.length; i++) {    
      if (templateFields[i].connect !== undefined) {  
        var clientFields = new ClientsFields(); 
        clientFields.user = clientTemplate.user;
        clientFields.label = templateFields[i].label;
        clientFields.value = templateFields[i].value;
        clientFields.$save();    
        delete templateFields[i].value;    
      }  
    }  
    clientRegdata.user = clientTemplate.user;     
    clientRegdata.fullName = clientTemplate.user.replace(".", " ");
    clientRegdata.password = "123456";
    clientRegdata.$save(function() {
      console.log('success saving registration data')
    });    
    clientTemplate.owner = "MoneyGram";
    clientTemplate.type = "Send Form";
    clientTemplate.template = $scope.templateFields;
    clientTemplate.$save(function() {
      console.log('success saving template')
    });
  };   
});

