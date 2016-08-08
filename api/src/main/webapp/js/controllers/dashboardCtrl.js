/**
 * Created by Baptiste on 06/08/2016.
 */
mainApp.controller('dashboardCtrl', function ($scope, RqstSrv) {

$scope.getListOfProspects = function(){
  RqstSrv.get("/prospect",function(data){
      $scope.prospects = data;
  },function(){
      console.log("Error");
  })
};

//Load Page
$scope.getListOfProspects();
});