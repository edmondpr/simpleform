angular.module('clientApp.myForms', ['ngResource', 'clientApp.push'])

    // Routes
    .config(function ($stateProvider) {

        $stateProvider

            .state('app.myForms', {
                url: "/my-forms",
                views: {
                    'menuContent' :{
                        templateUrl: "templates/my-forms.html",
                        controller: "FieldListCtrl"
                    }
                }
            })

    })

    .controller('FieldListCtrl', function ($scope, $rootScope, $http, $window, $ionicModal, ClientsTemplates, popupService) {
        var allFields = Fields.query(); 
        $scope.fields = new Array();

        $scope.field = new Fields();  //create new field instance. Properties will be set via ng-model on UI   
        allFields.$promise.then(function(data) {
            for (var i=0; i<data.length; i++) {
                if (data[i].user == utils.getUser()) {   
                    var field = new Object({"label": data[i].label, "value": data[i].value});
                    $scope.fields.push(field);
                }
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
            $scope.field.user = utils.getUser();
            $scope.field.$save(function() {
              location.href = ''; //redirect to home
            });
        };

        $scope.updateField = function() {
            var fieldId = this.field._id.$oid;
            $http.put(utils.getDbClientsFieldsUrl() + '/' + 
                     fieldId + '?apiKey=bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
              { 'label': this.field.label,
                'value': this.field.value,
                'user': utils.getUser()
            }).success(function (data, status, headers, config) {

            })
        };
 
        $scope.deleteField = function(field) { 
            if (popupService.showPopup('Really delete this?')) {
              field.$delete(function() {
                $window.location.href = ''; //redirect to home
              });
            }
        };                   


    })