angular.module('clientApp.login', ['ngResource', 'clientApp.push'])

    // Routes
    .config(function ($stateProvider) {

        $stateProvider

            .state('app.login', {
                url: "/login",
                views: {
                    'menuContent' :{
                        templateUrl: "templates/login.html",
                        controller: "LoginCtrl"
                    }
                }
            })

    })

    .controller('LoginCtrl', function ($scope, $rootScope, $state, $http, $window, RegData, popupService) {
        $scope.loginData = {};
        //console.log($rootScope.loginId);
        var regData = RegData.query(); 
        $scope.doLogin = function () {
            regData.$promise.then(function(data) {
                for (var i=0; i<data.length; i++) {
                    if ($scope.loginData.user === data[i].user && $scope.loginData.password === data[i].password) {
                        $rootScope.loginEmail = data[i].user;  
                        $state.go('app.fields'); 
                    }
                }
            });
        }

        /*$scope.fbLogin = function () {
            openFB.login(
                function (response) {
                    if (response.status === 'connected') {
                        console.log('Facebook login succeeded');
                        $scope.closeLogin();
                    } else {
                        alert('Facebook login failed');
                    }
                },
                {scope: 'email,publish_actions'});
        }   */    

    })