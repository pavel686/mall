app.controller('brandController',function($scope,$controller,$http,brandService){	
			
	        $controller('baseController',{$scope:$scope});//继承	
			 
	        //读取列表数据绑定到表单中  
			$scope.findAll=function(){
				brandService.findAll().success(
					function(result){
						$scope.brands=result;
					}			
				);
			}
	      $scope.findPage=function(page,rows){
	    	  brandService.findPage(page,rows).success(
	    		function(response){
	    			$scope.list = response.rows;
	    			$scope.paginationConf.totalItems=response.total;
	    		}	  
	    	  );
	    	  
	      };
	      
	      //添加代码
	      $scope.add = function(){
	    	  var obj = null;;
	    	  if($scope.entity.id!=null){
	    		  //修改
	    		obj = brandService.update($scope.entity);
	    	  }else{
	    		obj = brandService.add($scope.entity);
	    	  }
	    	  obj.success(
	    		function(result){
	    			//如果添加成功
	    			if(result.success){
	    				//刷新页面
	    				$scope.reloadList();
	    			}else{
	    				alert(result.message);
	    			}
	    		}	  
	    	  );
	    	  
	      }
	      //根据id获得对象
	      $scope.findOne=function(id){
	    	  //alert(id);
	    	  brandService.findOne(id).success(
	    		  function(response){
	    			  $scope.entity=response;
	    		  }	  
	    	  );
	    	  
	      }
	      
	      $scope.dele = function(){
	    	  brandService.dele($scope.ids).success(
	    		 function(result){
	    			 if(result.success){
	    				 //清空删除的数组
	    				 $scope.ids = [];
	    				 //刷新页面
	    				 $scope.reloadList();
	    			 }
	    			 
	    		 } 	  
	    	  );
	    	  
	      }
	      $scope.searchEntity={};
	      //搜索
	      $scope.search = function(page,rows){
	    	  //alert('../brand/search.do?page='+page+'&rows='+rows);
	    	  brandService.search(page,rows,$scope.searchEntity).success(
	    			function(response){
	  	    			$scope.list = response.rows;
	  	    			$scope.paginationConf.totalItems=response.total;
	  	    		}	  	  
	    	  );
	    	  
	      }
	      
	     
	      
	        
		});	