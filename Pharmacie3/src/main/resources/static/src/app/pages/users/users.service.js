(function () {

        'use strict';
        angular
            .module('app')
            .factory('UsersService', usersService);

        usersService.$inject = ['$http', '$q', 'API_URL'];

        function usersService($http, $q, API_URL) {

            return {
                getUsers: getUsers,
                getUserById : getUserById,
                createUser : createUser,
                updateUser : updateUser,
                deleteUser : deleteUser,

            };

            function getUsers() {
                var url = API_URL+'/users';
                req = {
                    method: 'GET',
                    url: url,
                };
                return $http(req)
                    .then(function successCallback(response) {
                        return response.data;
                    }, function errorCallback(response) {
                        deferred.reject(response);
                    });
            }


            function getUserById(userId) {
                var req = {
                    method: 'GET',
                    url: API_URL+'/users/'+userId+'/users/'+userId,
                };
                return $http(req)
                    .then(function successCallback(response) {
                        return response.data;
                    }, function errorCallback(error) {
                        return error;
                    });
            }

            function createUser(user, userTenants) {
                var customUrl = API_URL+'/users';

                var req = {
                    method: 'POST',
                    url: customUrl,
                    data: user
                };
                return $http(req)
                    .then(function successCallback(response) {
                        return response.data;
                    }, function errorCallback(error) {
                        return error;
                    });
            }
            function updateUser(user, userTenants) {
                var customUrl = API_URL+'/users/'+user.id;

                var req = {
                    method: 'PUT',
                    url: customUrl,
                    data: user

                };
                return $http(req)
                    .then(function successCallback(response) {
                        return response.data;
                    }, function errorCallback(error) {
                        return error;
                    });
            }
            function deleteUser(userId) {
                var customUrl = API_URL+'/users/'+userId;

                var req = {
                    method: 'DELETE',
                    url: customUrl,
                };
                return $http(req)
                    .then(function successCallback(response) {
                        return response.data;
                    }, function errorCallback(error) {
                        return error;
                    });
            }


        }
    })();
