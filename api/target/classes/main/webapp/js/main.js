/**
 * Created by Baptiste on 06/08/2016.
 */

var mainApp = angular.module('mainApp', [
    'ui.router'
]);

mainApp.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('dashboard', {
            url: "/dashboard",
            views: {
                'main': {
                    templateUrl: 'templates/dashboard.html',
                    controller: 'dashboardCtrl'
                }
            }
        }).state('login', {
        url: "/",
        views: {
            'main': {
                templateUrl: 'templates/login.html',
                controller: 'loginCtrl'
            }
        }
        }).state('detail', {
        url: "/detail/:id",
        views: {
            'main': {
                templateUrl: 'templates/detail.html',
                controller: 'detailCtrl'
            }
        }
        }).state('newProspect', {
        url: "/new",
        views: {
            'main': {
                templateUrl: 'templates/detail.html',
                controller: 'newProspectCtrl'
            }
        }
        }).state('test', {
        url: "/test",
        views: {
            'main': {
                templateUrl: 'templates/test.html',
                controller: 'newProspectCtrl'
            }
        }
    });

    $urlRouterProvider.otherwise('/');
});