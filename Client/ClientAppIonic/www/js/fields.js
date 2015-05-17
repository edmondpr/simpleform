angular.module('clientApp.fields', ['ngResource', 'clientApp.push'])

    // Routes
    .config(function ($stateProvider) {

        $stateProvider

            .state('app.fields', {
                url: "/fields",
                views: {
                    'menuContent' :{
                        templateUrl: "templates/fields.html",
                        controller: "FieldListCtrl"
                    }
                }
            })

    })

    .controller('FieldListCtrl', function ($scope, $rootScope, $http, $window, $timeout, $q, $log, $ionicModal, Fields, popupService) {

        var allFields = Fields.query(); 
        $scope.fields = new Array();
        var userFields = new Array();

        $scope.field = new Fields();  //create new field instance. Properties will be set via ng-model on UI   
        allFields.$promise.then(function(data) {
            for (var i=0; i<data.length; i++) {
                if (data[i].user == utils.getUser()) {   
                    userFields = data[i].fields;
                    break;
                }
            }
            for (var i=0; i<userFields.length; i++) {
                field = new Object({"label": userFields[i].label, "value": userFields[i].value});
                $scope.fields.push(field);
            }   
        });

        // Create the add field modal
        $ionicModal.fromTemplateUrl('templates/field-add.html', {
            scope: $scope
        }).then(function (modal) {
            $scope.modalAddField = modal;
        });

        // Open the add field modal
        $scope.newField = function () {
            $scope.modalAddField.show();
        }; 

        // Close the add field modal
        $scope.closeModalAddField = function () {
            $scope.modalAddField.hide();
        }   

        // Create the barcode modal
        $ionicModal.fromTemplateUrl('templates/barcode.html', {
            scope: $scope
        }).then(function (modal) {           
            $scope.modalBarcode = modal;
        });   

        // Open the barcode modal
        $scope.generateBarcode = function () {
            $scope.modalBarcode.show();
            $("#barcode").JsBarcode(utils.getUser(), {width:1,height:25});             
        };  

        // Close the barcode modal
        $scope.closeModalBarcode = function () {
            $scope.modalBarcode.hide();
        }                         

        $scope.saveField = function() { 
            var label = $scope.field.label;
            var value = $scope.field.value; 
            var user = utils.getUser();
            var userId = utils.getUserId();
            Fields.get({ id: userId }).$promise.then(function(data) {
                var userFields = data.fields;
                field = new Object({"label": label, "value": value, "position": userFields.length+1});
                userFields.push(field);
                utils.updateUser($http, userId, user, userFields);                
            });
        };

        $scope.updateField = function() {
            var label = this.field.label;
            var value = this.field.value;
            var user = utils.getUser();
            var userId = utils.getUserId();
            Fields.get({ id: userId }).$promise.then(function(data) {
                var userFields = data.fields;
                for (var i=0; i<userFields.length; i++) {
                    if (userFields[i].label == label) {
                        userFields[i].value = value;
                        break;
                    }                   
                }
                utils.updateUser($http, userId, user, userFields);                
            });             
            //var userId = this.field._id.$oid;
        };
 
        $scope.deleteField = function(field) { 
            if (popupService.showPopup('Really delete this?')) {
                var label = field.label;   
                var user = utils.getUser();
                var userId = utils.getUserId();
                Fields.get({ id: userId }).$promise.then(function(data) {
                    var userFields = data.fields;
                    for (var i=0; i<userFields.length; i++) {
                        if (userFields[i].label == label) {
                            userFields.remove(i);
                            break;
                        }                   
                    }
                    utils.updateUser($http, userId, user, userFields);
                });
            }
        };                   


    })