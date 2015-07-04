Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

window.utils = {

    getDbClientsFieldsUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/clients_fields";
        /*if (window.location.href.indexOf("localhost") >= 0) {
            url = "/mongodb/clients_fields";
        }*/
        return url;
    },

    getDbClientsRegDataUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/clients_regdata";
        return url;
    },   

    getDbClientsTemplatesUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/clients_templates";
        return url;
    },   

    getDbOwnersTemplatesUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/owners_fields";
        return url;
    },     

    getUser: function() {
        var user = "edmondpr@gmail.com";
        return user;
    },

    getUserId: function() {
        var userId = "555527af6bcd36ac2680bb84";
        return userId;
    },

    updateUser: function(http, userId, user, userFields) {
        http.put(utils.getDbClientsFieldsUrl() + '/' + 
                 userId + '?apiKey=bQIONBYLTcZ-IpiEIN7GbjZfhkw1FfLD',
          { 'user': user,
            'fields': userFields
        }).success(function (data, status, headers, config) {

        })
    }    


};