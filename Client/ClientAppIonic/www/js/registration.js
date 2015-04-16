angular.module('clientApp.registration', ['ngResource', 'clientApp.push'])

    // Routes
    .config(function ($stateProvider) {

        $stateProvider

            .state('app.registration', {
                url: "/registration",
                views: {
                    'menuContent' :{
                        templateUrl: "templates/registration.html",
                        controller: "RegistrationCtrl"
                    }
                }
            })

    })

    .controller('RegistrationCtrl', function ($scope, $state, $http, $window, RegData, Fields, popupService) {           
        $scope.regData = new RegData();
        var regDataAll = RegData.query();       

        $scope.saveData = function() { 
            // Check if registering user exists
            regDataAll.$promise.then(function(data) {
                var userExists = false;                 
                for (var i=0; i<data.length; i++) {
                    if (data[i].email === $scope.regData.email) {
                        userExists = true;
                    }
                }
                if (!userExists) {
                    var field = new Fields(); 
                    field.label = "Name";
                    field.value = $scope.regData.name;
                    field.user = $scope.regData.email;                    
                    field.$save(); 
                    field = new Fields(); 
                    field.label = "E-mail";
                    field.value = $scope.regData.email;
                    field.user = $scope.regData.email;                     
                    field.$save(); 
                    $scope.regData.$save(function() {
                        $state.go('app.login'); 
                    }); 
                }                 
            });  
        }


    })