app.controller('baseController',function($scope){
	
	 $scope.reloadList = function(){
   	  //$scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
   	   $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
     }
	
	 //分页控件配置 
    $scope.paginationConf = {
    		 currentPage: 1,  //默认当前页
    		 totalItems: 10, //总记录数
    		 itemsPerPage: 10, //每页显示的条数
    		 perPageOptions: [10, 20, 30, 40, 50],  //显示页的选项
    		 onChange: function(){
    			 $scope.reloadList();
    		 }
    }; 
    
    //根据id删除
    //定义一个数组，这个数据保存的是要删除的数据的id值
    $scope.ids = [];
    $scope.updateSelected = function($event,id){
    	alert(id);
  	  //判断是否被选中，如果选中，添加数组。否则，从数组中删除
  	  if($event.target.checked){
  		  $scope.ids.push(id);
  	  }else{
  		  //首先获得取消id所在的位置
  		  var did = $scope.ids.indexOf(id);
  		  //删除did位置的id
  		  $scope.ids.splice(did,1);
  	  }
    }
    
	//从一个json字符串中提取某个属性值，并使用逗号分隔
    $scope.jsonToString = function(jsonString,key){
    	//把json字符串转化成js对象
    	 var jsonObj = JSON.parse(jsonString);
    	 var value ="";
    	 //循环数据,获得每一个对象key对应的值  
    	 for(var i=0;i<jsonObj.length;i++){
    		 //{"id":1,"text":"联想"}
    		 if(i>0){
    			 value+=",";
    		 }
    		 value+=jsonObj[i][key];
    	 }
    	 return value;
    }

    
    
	
});