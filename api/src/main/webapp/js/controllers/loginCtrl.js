/**
 * Created by Baptiste on 07/08/2016.
 */
mainApp.controller('loginCtrl', function ($scope, RqstSrv, LoginSrv, $stateParams) {

    $scope.user;
    $scope.login = function(email,password){
        LoginSrv.login(email, password);
    }
});