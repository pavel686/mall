app.controller("contentController",function($scope,contentService){
	
	$scope.contentList = [];
	$scope.findContentByCategoryId = function(categoryId){
		contentService.findContentByCategoryId(categoryId).success(
		  function(response){
			  $scope.contentList[categoryId] = response;
		  }		
		
		);
	}
	
	//首先搜索方法
	$scope.search = function(){
		//alert($scope.keywords);
		location.href = "http://localhost:8003/search.html#?keywords="+$scope.keywords;
	}
	
	
});