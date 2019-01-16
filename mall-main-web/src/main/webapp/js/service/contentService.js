app.service("contentService",function($http){
	
	//根据广告分类的id查询广告信息
	this.findContentByCategoryId = function(categoryId){
		return $http.get("findCotentByCategoryId.do?categoryId="+categoryId);
	}
	
});