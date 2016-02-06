window.utils = {

    getDbOwnersFieldsUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/owners_fields";
        /*if (window.location.href.indexOf("localhost") >= 0) {
            url = "/mongodb/receiver_fields";
        }*/
        return url;
    },

    getDbClientsRegdataUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/clients_regdata";
        return url;
    },     

    getDbClientsFieldsUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/clients_fields";
        return url;
    },   

    getDbClientsTemplatesUrl: function() {
        var url = "https://api.mongolab.com/api/1/databases/simpleformdb/collections/clients_templates";
        return url;
    },     

    getUser: function() {
        var user = "edmondpr@gmail.com";
        return user;
    }


};