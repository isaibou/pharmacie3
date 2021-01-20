'use strict';

/**
 * Config for the router
 */
angular.module('app')
  .run(
    [          '$rootScope', '$state', '$stateParams',
      function ($rootScope,   $state,   $stateParams) {
          $rootScope.$state = $state;
          $rootScope.$stateParams = $stateParams;
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider', 'JQ_CONFIG', 'MODULE_CONFIG',
      function ($stateProvider,   $urlRouterProvider, JQ_CONFIG, MODULE_CONFIG) {
          var layout = "src/app/app.html";

            $urlRouterProvider
              .otherwise('/app/dashboard');

          $stateProvider
              .state('app', {
                  abstract: true,
                  url: '/app',
                  templateUrl: layout
              })
              .state('app.dashboard', {
                  url: '/dashboard',
                  controller: 'DashboardCtrl',
                  templateUrl: 'src/app/pages/dashboard/dashboard.html',
                  // resolve: load(['src/app/pages/dashboard/dashboard.controller.js'])
                  // resolve : {
                  //   lazyLoad: function ($ocLazyLoad) {
                  //                     return $ocLazyLoad.load([
                  //                         'src/app/pages/dashboard/dashboard.controller.js',
                  //                     ]);
                  //                 }
                  //
                  // }
              })
              .state('app.users', {
                  url: '/users',
                  controller: 'UsersCtrl',
                  templateUrl: 'src/app/pages/users/users.html',
                  // // resolve: load(['src/app/pages/dashboard/dashboard.controller.js'])
                  // lazyLoad: function ($ocLazyLoad) {
                  //                   return $ocLazyLoad.load([
                  //                       'src/app/pages/dashboard/dashboard.controller.js',
                  //                   ]);
                  //               }
              })

              .state('access', {
                  url: '/access',
                  template: '<div ui-view class="fade-in-right-big smooth"></div>'
              })
              .state('access.signin', {
                  url: '/signin',
                  controller: "SigninFormController",
                  templateUrl: 'src/app/pages/signin/signin.html',
              //     // resolve: load( ['src/app/pages/signin/signin.controller.js'] )
              //     lazyLoad: function ($ocLazyLoad) {
              //                       return $ocLazyLoad.load([
              //                           'src/app/pages/signin/signin.controller.js',
              //                       ]);
              //                   }
               })
              // .state('access.signup', {
              //     url: '/signup',
              //     templateUrl: 'tpl/page_signup.html',
              //     resolve: load( ['js/controllers/signup.js'] )
              // })
              ;

          function load(srcs, callback) {
            return {

                deps: ['$ocLazyLoad', '$q',
                  function( $ocLazyLoad, $q ){
                    var deferred = $q.defer();
                    var promise  = false;
                    srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                    if(!promise){
                      promise = deferred.promise;
                    }
                    angular.forEach(srcs, function(src) {
                      promise = promise.then( function(){
                        if(JQ_CONFIG[src]){
                          return $ocLazyLoad.load(JQ_CONFIG[src]);
                        }
                        angular.forEach(MODULE_CONFIG, function(module) {
                          if( module.name == src){
                            name = module.name;
                          }else{
                            name = src;
                          }
                        });
                        return $ocLazyLoad.load(name);
                      } );
                    });
                    deferred.resolve();
                    return callback ? promise.then(function(){ return callback(); }) : promise;
                }]
            }
          }


      }
    ]
  );
