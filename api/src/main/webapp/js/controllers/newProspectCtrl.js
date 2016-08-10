/**
 * Created by Baptiste on 07/08/2016.
 */
mainApp.controller('newProspectCtrl', function ($scope, RqstSrv) {

    $scope.editMode = true;
    $scope.showTabs = true;
    

    $scope.edit = function(){
        $('.validate').each(function(){
            this.removeAttribute('disabled');
            $scope.editMode = true;
        })
    };

    $scope.cancel = function(){
        $('.validate').each(function(){
            this.setAttribute('disabled','');
            $scope.editMode = false;
        })
    };

    $scope.save = function(){
        RqstSrv.post("/prospect", $scope.prospect, function(){
            console.log("prospect modifié");
            $scope.cancel();
        }, function(){
            console.log("Error");
        })
    };
    
    //Load Page
    $scope.edit();
});