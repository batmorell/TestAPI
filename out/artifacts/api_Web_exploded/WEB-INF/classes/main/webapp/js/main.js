/**
 * Created by Baptiste on 06/08/2016.
 */

var mainApp = angular.module('mainApp', [
    'ui.router',
    'ngCookies',
    'ngRoute',
    'ui.materialize'
]);

mainApp.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('dashboard', {
            url: "/dashboard",
            grantAccessNeeded: "1",
            views: {
                'main': {
                    templateUrl: 'templates/dashboard.html',
                    controller: 'dashboardCtrl'
                },
                'slider':{
                    templateUrl: 'templates/slider.html'
                },
                'header':{
                    templateUrl: 'templates/header.html'
                }
            }
        }).state('login', {
        url: "/",
        views: {
            'main': {
                templateUrl: 'templates/login.html'
            }
        }
        }).state('detail', {
        url: "/detail/:id",
        grantAccessNeeded: "1",
        views: {
            'main': {
                templateUrl: 'templates/detail.html',
                controller: 'detailCtrl'
            },
            'slider':{
                templateUrl: 'templates/slider.html'
            },
            'header':{
                templateUrl: 'templates/header.html'
            }
        }
        }).state('newProspect', {
        url: "/new",
        grantAccessNeeded: "1",
        views: {
            'main': {
                templateUrl: 'templates/detail.html',
                controller: 'newProspectCtrl'
            }
        }
        }).state('test', {
        url: "/test",
        grantAccessNeeded: "2",
        views: {
            'main': {
                templateUrl: 'templates/test.html',
                controller: 'newProspectCtrl'
            }
        }
    });


    $urlRouterProvider.otherwise('/');

}).run(function($rootScope, $state, RqstSrv, $cookies){
/*    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams, options){

        $rootScope.authToken = $cookies.get('authToken');
        if(!($rootScope.connectedUser) && $rootScope.authToken){
            RqstSrv.get("/users/fromAuthToken/" + $rootScope.authToken, function(data){
                $rootScope.connectedUser = data;
            }, function(){
                $rootScope.connectedUser = null;
            });
        }

        if(toState.grantAccessNeeded){
            if(!$rootScope.connectedUser){
                console.log('Vous devez etre connecter');
                event.preventDefault();

            }else{
                if(toState.grantAccessNeeded > $rootScope.connectedUser.accessLevel.ordinal){
                    console.log("Vous ne possédez pas les droits suffisants pour accéder à cette partie du site");
                    event.preventDefault();
                }
            }
        }
    });*/
});