'use strict';

/* Controllers */
angular.module('app')
  // Flot Chart controller
  .controller('UsersCtrl', ['$scope', 'UsersService', function($scope, UsersService) {
    $scope.user={};
    $scope.users = [];

    $scope.addUser = addUser;
    $scope.editUser = editUser;
    $scope.deleteUser = deleteUser;
    populateUsers();

    function populateUsers() {
      for (var i = 0; i < 10; i++) {
        var element = {
          name: "user " + i,
          email: "emailuser"+i+"@mail.com"
        }
        $scope.users.push(element);
      }

      // From API
      // UsersService.getUsers().then(function (data) {
      //   $scope.users = data;
      // }, function (error) {});
    }

    function addUser(user) {
      $scope.users.push(user);
    }
    function editUser(user) {

    }
    function deleteUser(user) {
      $scope.users.splice($scope.users.indexOf(user),1);
    }
  }]);
