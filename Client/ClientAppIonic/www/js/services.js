angular.module('clientApp.services',[]).factory('Fields',function($resource){
    return $resource(utils.getDbUrl() + '/:id',
    {
      apiKey:'bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      id:'@_id.$oid'
    });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
});