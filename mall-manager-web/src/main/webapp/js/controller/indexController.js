app.controller('indexController',function($scope,loginService){
	
	$scope.getLoginName= function(){
		loginService.loginName().success(
			function(data){
				//document.write(data.username);
				$scope.LogiName = data.username;	
			}
		);
		
	}
	
});