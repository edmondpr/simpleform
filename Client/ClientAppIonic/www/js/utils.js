window.utils = {

    getDbUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/client_fields";
        /*if (window.location.href.indexOf("localhost") >= 0) {
            url = "/mongodb/client_fields";
        }*/
        return url;
    },

    getUser: function() {
        var user = "edmondpr@gmail.com";
        return user;
    }


};