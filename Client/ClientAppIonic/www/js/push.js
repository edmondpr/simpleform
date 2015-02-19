angular.module('clientApp.push', [])

    .factory('Notification', function ($http, PUSH_SERVER_URL) {
        return {
            push: function(message) {
                return $http.post(PUSH_SERVER_URL + '/messages/', message);
            }
        };
    });
