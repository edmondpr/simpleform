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

    .controller('FieldListCtrl', function ($scope, $http, $window, $ionicModal, Fields, popupService) {
        $scope.fields = Fields.query(); 
        $scope.field = new Fields();  //create new field instance. Properties will be set via ng-model on UI    

        $scope.fields.$promise.then(function(data) {
          console.log(data[0].label)  
        });
         
        /*for (var i=0; i<=fields.length-1; i++) {
           
            if (field.label == "E-mail") {
                $("#barcode").JsBarcode(field.value,{width:1,height:25});
            }
        }*/
        

        // Create the add field modal
        $ionicModal.fromTemplateUrl('templates/field-add.html', {
            scope: $scope
        }).then(function (modal) {
            $scope.modal = modal;
        });  

        // Open the add field modal
        $scope.newField = function () {
            $scope.modal.show();
        };   

        $scope.saveField = function() { 
            $scope.field.user = utils.getUser();
            $scope.field.$save(function() {
              location.href = ''; //redirect to home
            });
        };

        $scope.updateField = function() {
            var fieldId = this.field._id.$oid;
            $http.put(utils.getDbUrl() + '/' + 
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

        // Triggered in the modal window to close it
        $scope.closeModal = function () {
            $scope.modal.hide();
        }          

    })