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
    }


};