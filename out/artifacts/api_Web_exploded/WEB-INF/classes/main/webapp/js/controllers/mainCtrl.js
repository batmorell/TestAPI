/**
 * Created by Baptiste on 06/08/2016.
 */
mainApp.controller('mainCtrl', function ($scope, LoginSrv) {

    $scope.deconnexion = function() {
        LoginSrv.logout();
    };

    $scope.user;

    $scope.login = function(email,password){
        LoginSrv.login(email, password);
    }
});
