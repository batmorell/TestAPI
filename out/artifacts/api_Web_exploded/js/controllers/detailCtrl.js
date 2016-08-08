/**
 * Created by Baptiste on 07/08/2016.
 */
mainApp.controller('detailCtrl', function ($scope, RqstSrv, $stateParams) {
    
    $scope.editMode = false;
    $scope.input = {};
    $scope.input.statuts = {};

    $scope.getAProspect = function(){
        RqstSrv.get("/prospect/" + $stateParams.id ,function(data){
            $scope.prospect = data;
        },function(){
            console.log("Error");
        })
    };

    $scope.getListOfStatuts = function(){
        RqstSrv.get("/liste/statuts" ,function(data){
            $scope.input.statuts.option = data;
        },function(){
            console.log("Error");
        })
    };
    
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
        RqstSrv.put("/prospect", $scope.prospect, function(){
            console.log("prospect ajouté");
            $scope.cancel();
        }, function(){
            console.log("Error");
        })
    };

//Load Page
    $scope.getAProspect();
    $scope.getListOfStatuts();
});