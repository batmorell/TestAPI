/**
 * Created by Baptiste on 06/08/2016.
 */
mainApp.controller('dashboardCtrl', function ($scope, RqstSrv,$state) {

    $scope.getListOfProspects = function(){
      RqstSrv.get("/prospect",function(data){
          $scope.prospects = data;
      },function(){
          console.log("Error");
      })
    };

    $scope.goToDetail = function(id){
        $state.go('detail',{id: id});
    };

    $scope.change = function(){
        $state.go('login');
    };

//Load Page
$scope.getListOfProspects();
});