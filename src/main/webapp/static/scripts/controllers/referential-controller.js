var refController = function ($scope, resolvedDtos, service,$modal,metamodel,FileUpload) {
    $scope.dtos = resolvedDtos;
    $scope.metamodel=metamodel;
    $scope.myFile={};
    $scope.dto=metamodel.init();
    
    $scope.refresh = function(){
    	$scope.dtos = service.query();
    	$scope.clear();
    	
    };
    
    $scope.create = function () {
    	service.save($scope.dto,
            function () {
                $scope.refresh();
            });
    };

    $scope.update = function (id) {
        $scope.dto = service.get({id: id});
        $scope.form();
    };

    $scope.delete = function (id) {
    	service.delete({id: id},
            function () {
    			$scope.refresh();
            });
    };

    $scope.clear = function () {
    	$scope.dto = metamodel.init();
    };
    
    $scope.form = function () {

        var modalInstance = $modal.open({
          templateUrl: 'views/refform.html',
          controller: function ($scope, $modalInstance,dto,metamodel) {
        	  
        	  $scope.dto=dto;
        	  $scope.metamodel=metamodel
        	  
        	  $scope.ok = function () {
        	    $modalInstance.close($scope.dto);
        	  };

        	  $scope.cancel = function () {
        	    $modalInstance.dismiss('cancel');
        	  };
        	},
          resolve: {
            dto: function () {
              return $scope.dto;
            },
            metamodel: function(){
            	return $scope.metamodel;
            }
          }
        });

        modalInstance.result.then(function (dto) {
          $scope.create();
        }, function () {
        	$scope.clear();
        });
      };
      
	  $scope.upload = function(){
		  var modalInstance = $modal.open({
	          templateUrl: 'views/refupload.html',
		  controller: function ($scope, $modalInstance,myFile,metamodel) {
			  
			  $scope.metamodel=metamodel;
			  $scope.myFile={};
			  $scope.ok = function () {
			    $modalInstance.close($scope.myFile);
			  };
		
			  setFile = function(element) {
			        $scope.myFile = element.files[0];
			  };
			    
			  $scope.cancel = function () {
			    $modalInstance.dismiss('cancel');
		    	  };
		    	},
		      resolve: {
		    	myFile: function () {
		    		return $scope.myFile;
		        },
		        metamodel: function(){
		        	return $scope.metamodel;
		        }
		      }
		    });
	
		  modalInstance.result.then(function (myFile) {
			  FileUpload.uploadFileToUrl(myFile, '/rest/'+$scope.metamodel.resource+'/upload');
	        }, function () {
	        	$scope.refresh();
	        });
			  
		  };
  };

  superBetApp.controller('BookmakersController', ['$scope', 'resolvedDtos', 'Bookmakers','$modal','metamodel','FileUpload',
    refController]);
  superBetApp.controller('SportsController', ['$scope', 'resolvedDtos', 'Sports','$modal','metamodel','FileUpload',
    refController]);
  superBetApp.controller('BetTypesController', ['$scope', 'resolvedDtos', 'BetTypes','$modal','metamodel','FileUpload',
    refController]);
  superBetApp.controller('DataMappingsController', ['$scope', 'resolvedDtos', 'DataMappings','$modal','metamodel','FileUpload',
    refController]);
  superBetApp.controller('MatchesController', ['$scope', 'resolvedDtos', 'Matches','$modal','metamodel','FileUpload',
    refController]);
