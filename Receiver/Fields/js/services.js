angular.module('fieldApp.services',[]).factory('Fields',function($resource){
    return $resource(utils.getDbOwnersFieldsUrl() + '/:id',
    {
      apiKey:'bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      id:'@_id.$oid'
    });
}).factory('ClientsRegdata',function($resource){
    return $resource(utils.getDbClientsRegdataUrl() + '/:id',
    {
      apiKey:'bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
      id:'@_id.$oid'
    });
}).factory('ClientsFields',function($resource){
    return $resource(utils.getDbClientsFieldsUrl() + '/:id',
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