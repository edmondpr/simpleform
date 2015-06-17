angular.module('clientApp.services',[]).factory('Fields',function($resource){
    return $resource(utils.getDbClientsFieldsUrl() + '/:id',
    {
      apiKey:'bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      id:'@_id.$oid'
    });
}).factory('RegData',function($resource){
    return $resource(utils.getDbClientsRegDataUrl() + '/:id',
    {
      apiKey:'bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      id:'@_id.$oid'
    });
}).factory('ClientsTemplates',function($resource){
    return $resource(utils.getDbClientsTemplatesUrl() + '/:id',
    {
      apiKey:'bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      id:'@_id.$oid'
    });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
});