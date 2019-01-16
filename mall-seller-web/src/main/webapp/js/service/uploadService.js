//服务层
app.service('uploadService',function($http){
	
	//实现上传
	this.upload = function(){
		var formData = new FormData(); //angularJS给我们提供的上传对象
		//把上传的文件对象放到FormData中
		formData.append("file",file.files[0]);
		//上传
		return $http({
		 method:"POST",
		 url:"../upload.do",
		 data:formData,
		 headers: {'Content-Type':undefined},
		 transformRequest: angular.identity
		});
		
	}
	
	
});