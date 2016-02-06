angular.module('clientApp.fields', ['ngResource', 'ngAnimate', 'clientApp.push'])

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

    .config( function($mdThemingProvider){

    // Configure a dark theme with primary foreground yellow

    $mdThemingProvider.theme('docs-dark', 'default')
        .primaryPalette('blue');

    })

    .controller('FieldListCtrl', function ($scope, $rootScope, $http, $window, $timeout, $q, $log, $ionicModal, Fields, ClientsTemplates, OwnersTemplates, popupService) {

        var self = $scope;
        self.simulateQuery = false;
        self.isDisabled    = false;
        self.expanded      = false;

        self.clientTemplates = ClientsTemplates.query(); 
        self.ownersTemplates = OwnersTemplates.query();  
        self.selectedTemplates = self.clientTemplates;
        self.templates = new Array();
        self.querySearch   = querySearch;
        self.selectedItemChange = selectedItemChange;
        self.searchTextChange   = searchTextChange;

        createTemplates();


        $scope.switchTemplates = function(type) {
            if (type == "client") {
                self.selectedTemplates = self.clientTemplates;
            } else if (type == "owners") {
                self.selectedTemplates = self.ownersTemplates;
            }
            self.templates = [];
            createTemplates();      
        };        


        function querySearch (query) {
          var results = query ? self.templates.filter( createFilterFor(query) ) : self.templates,
              deferred;
          return results;
        }
        function searchTextChange(text) {
          $log.info('Text changed to ' + text);
        }
        function selectedItemChange(item) {
            if (item != undefined) {
                self.selectedTemplates.$promise.then(function(data) { 
                    for (var i=0; i<data.length; i++) {
                        if (i == item.index) {
                            var newFields = data[i].fields;
                            break;
                        }
                    }
                    for (var i=0; i<newFields.length; i++) {
                        if (newFields[i].connect != undefined) {
                            for (var j=0; j<self.fields.length; j++) {
                                var connect = newFields[i].connect;
                                connect = connect.substring(1, connect.length - 1)
                                if (connect == self.fields[j].label) {
                                    newFields[i].value = self.fields[j].value;
                                }
                            }
                        }
                    }                
                    self.fields = newFields;
                });            
                $log.info('Item changed to ' + JSON.stringify(item));
            }
        }

        // Create list of selectable templates
        function createTemplates() {
            self.fields = new Array();
            self.selectedTemplates.$promise.then(function(data) { 
                for (var i=0; i<data.length; i++) {
                    if (data[i].user == utils.getUser() || self.selectedTemplates == self.ownersTemplates) {
                        // Populate base client form on initial screen
                        if (data[i].owner == undefined && data[i].type == undefined) {
                            for (var j=0; j<data[i].fields.length; j++) {
                                field = new Object({"label": data[i].fields[j].label, "value": data[i].fields[j].value});
                                self.fields.push(field);
                            } 
                        } 

                        // Populate all templates                         
                        var fullName = "";
                        // If base client form put on the first position
                        if (data[i].owner == undefined) {
                            for (var j in data[i].fields) {
                                if (data[i].fields[j].label == 'Name') {
                                    fullName += data[i].fields[j].value;
                                }
                                if (data[i].fields[j].label == 'Surname') {
                                    fullName += " " + data[i].fields[j].value;
                                }
                            }
                            self.templates.unshift({
                              value: fullName.toLowerCase(),
                              display: fullName,
                              index: i
                            });                        
                        } else {
                            for (var j in data[i].fields) {
                                if (data[i].fields[j].label == 'Receiver Name') {
                                    fullName += data[i].fields[j].value;
                                }
                                if (data[i].fields[j].label == 'Receiver Surname') {
                                    fullName += " " + data[i].fields[j].value;
                                }
                            }
                            self.templates.push({
                              value: fullName.toLowerCase(),
                              display: fullName,
                              index: i
                            });
                        }
                    }
                }
            });
        }

        function createFilterFor(query) {
          var lowercaseQuery = angular.lowercase(query);
          return function filterFn(template) {
            return (template.value.indexOf(lowercaseQuery) === 0);
          };
        }

        // Create the add field modal
        $ionicModal.fromTemplateUrl('templates/field-add.html', {
            scope: self
        }).then(function (modal) {
            self.modalAddField = modal;
        });

        // Open the add field modal
        self.newField = function () {
            self.modalAddField.show();
        }; 

        // Close the add field modal
        self.closeModalAddField = function () {
            self.modalAddField.hide();
        }   

        // Create the barcode modal
        $ionicModal.fromTemplateUrl('templates/barcode.html', {
            scope: self
        }).then(function (modal) {           
            self.modalBarcode = modal;
        });   

        // Open the barcode modal
        self.generateBarcode = function () {
            self.modalBarcode.show();
            $("#barcode").JsBarcode(utils.getUser(), {width:1,height:25});             
        };  

        // Close the barcode modal
        self.closeModalBarcode = function () {
            self.modalBarcode.hide();
        }                         

        self.saveField = function() { 
            var label = self.field.label;
            var value = self.field.value; 
            var user = utils.getUser();
            var userId = utils.getUserId();
            Fields.get({ id: userId }).$promise.then(function(data) {
                var userFields = data.fields;
                field = new Object({"label": label, "value": value, "position": userFields.length+1});
                userFields.push(field);
                utils.updateUser($http, userId, user, userFields);                
            });
        };

        self.updateField = function() {
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
 
        self.deleteField = function(field) { 
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

    .animation('.slideDown', function() {
    return {
        addClass: function(element, className, done) {
            $(element).slideDown({duration: 200, done});
        },
        removeClass: function(element, className, done) {
            $(element).slideUp({duration: 200, done});
        }
    }
});