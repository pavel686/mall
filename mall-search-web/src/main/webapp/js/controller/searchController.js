app.controller("searchController",function($scope,$location,searchService,itemCatService){
	
	
	$scope.itemCatList=[];//商品分类列表
	//加载商品分类列表
	$scope.findItemCatList=function(){		
		itemCatService.findAll().success(
				function(response){							
					for(var i=0;i<response.length;i++){
						$scope.itemCatList[response[i].id]=response[i].name;
					}
				}
		);
	};
	
	$scope.loadSearch = function(){
		alert($location.search()['keywords']);
		$scope.searchMap.keywords = $location.search()['keywords'];
		$scope.search();
	}
	
	
	//$scope.searchMap ={'keywords':'华为','category':'手机','brand':'华为','spec':{'网络':'3G','内存':'128'}};
	$scope.searchMap ={'keywords':'','category':'','categoryId':'','brand':'','price':'','spec':{},'pageNo':1,'pageSize':15,'sortFiled':'','sort':''};
	
	//实现排序查询
	$scope.sortSearch= function(sortFile,sort){
		
		$scope.searchMap.sortFiled = sortFile;
		$scope.searchMap.sort = sort;
		$scope.search();
	}
	
	
	
	//根据当前页提交查询
	$scope.queryByPage = function(pageNo){
		
		pageNo = parseInt(pageNo);
		
		if(pageNo<1||pageNo>$scope.resultMap.totalPage){
			return ;
		}
		
		$scope.searchMap.pageNo = pageNo;
		
		$scope.search();
	}
	
	//判断当前页是否是第一页
	$scope.isFirstPage = function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}
	}
	//判断当前页是否是最后一页
	$scope.isEndPage = function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPage){
			return true;
		}else{
			return false;
		}
	}
	
	//判断关键字中是否包含品牌  keywords=诺基亚
	$scope.keywordIsBrand = function(){
	//alert(111);
	  for(var i=0;i<$scope.resultMap.brand.length;i++){
		  if($scope.searchMap.keywords.indexOf($scope.resultMap.brand[i].text)>=0){
			  //alert($scope.searchMap.keywords);
			 // alert($scope.resultMap.brand[i].text);
			 // alert($scope.searchMap.keywords.indexOf($scope.resultMap.brand[i].text));
			  return true;
		  }
	  }
	  return false;
	}
	
	
	
	
	//构建分页标签
	$scope.buildPageLable = function(){
		//alert(111);
		//定义一个PageLable
		$scope.pageLable = [];
		//获得总页数
		var maxPageNo  = $scope.resultMap.totalPage;
		//分码的开始位置
		var firstPage = 1;
		//页面的结束位置
		var lastPage = maxPageNo;
		//定义前面是否有点点
		$scope.firstdot = true;
		//定义后面是否有点点
		$scope.lastdot = true;
		
		
		//首先判断总页数是否大于5页
		if($scope.resultMap.totalPage>5){
			//并且当前页小于等于3页
			if($scope.searchMap.pageNo<=3){
				lastPage = 5;
				$scope.firstdot = false;
				
			}else if($scope.searchMap.pageNo>=lastPage-2){
				firstPage = maxPageNo-4;
				$scope.lastdot = false;
			}else{
				firstPage = $scope.searchMap.pageNo - 2;
				lastPage = $scope.searchMap.pageNo + 2;
			}
			
		}else{
			$scope.firstdot = false;
			$scope.lastdot = false;
		}
		//alert(fistPage);
		//alert(lastPage);
		for(var i = firstPage;i<=lastPage;i++){
			$scope.pageLable.push(i);
		}
		

	}
	
	
	//添加搜索项
	$scope.addSearchItem = function(key,value){
		
		if(key=='category'||key=='brand'||key=='price'){
			$scope.searchMap[key] = value;
		}else if(key=='categoryId'){
			$scope.searchMap[key] = value;
		} else{
			$scope.searchMap.spec[key] = value;
		}
		//实现搜索
		$scope.search();
	}
	//移除项
	$scope.removeSearchItem = function(key){
		if(key=='category'||key=='brand'||key=='categoryId'||key=='price'){
			$scope.searchMap[key] = '';
		}else{
			delete $scope.searchMap.spec[key];
		}
		//实现搜索
		$scope.search();
	}
	
	$scope.search = function(){
		//alert(111);
		searchService.search($scope.searchMap).success(
			function(response){
				//alert(response);
				$scope.resultMap = response;
				$scope.buildPageLable();
			}	
		);
	}
	
})