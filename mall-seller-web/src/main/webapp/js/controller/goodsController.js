 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	$scope.status=['未审核','已审核','审核未通过','关闭'];//商品状态
	
	
	//根据规格名称和选项名称返回是否被勾选
	//网络，移动4G
	$scope.checkAttributeValue=function(specName,optionName){
		//获取选中的内容[{"attributeName":"网络","attributeValue":["移动4G"]}]
		var items= $scope.entity.tbGoodsDesc.specificationItems;
		//查询当前的内容是否保存在被选中的内容中
		var object= $scope.searchObjetByKey(items,'attributeName',specName);
		if(object==null){
			return false;
		}else{
			if(object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else{
				return false;
			}
		}			
	}
	
	
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
	
	//定义显示图片列表的结构
	$scope.entity = {tbGoods:{},tbGoodsDesc:{itemImages:[],specificationItems:[]},itemList:[]};
	
	//初始化SKU列表
	$scope.createItemList = function(){
		//首先初始化itemList
		$scope.entity.itemList = [{spec:{},"price":0,"num":0,"status":"0","isDefault":"0"}];
		//获得规格选项[{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
		var items = $scope.entity.tbGoodsDesc.specificationItems;  
		for(var j = 0;j<items.length;j++){
			$scope.entity.itemList = addColumn($scope.entity.itemList,items[j].attributeName,items[j].attributeValue);
		}
		
	}
	//对规格属性进行spec属性添加
	//参数1 要添加的集合  [{spec:{},"price":0,"num":0,"status":"0","isDefault":"0"}]
	//参数2 规格名称  网络
	//参数3 规格选项 "移动3G","移动4G"
	addColumn = function(list,columnName,columnValue){
		var newList = [];  //定义一个空的集合
		for(var i=0;i<list.length;i++){
			//{spec:{},"price":0,"num":0,"status":"0","isDefault":"0"}
			var oldRows = list[i];
			//{"attributeName":"网络","attributeValue":["移动3G","移动4G"]}
			for(var k=0;k<columnValue.length;k++){
				//{spec:{},"price":0,"num":0,"status":"0","isDefault":"0"}
				var newRows = JSON.parse(JSON.stringify(oldRows));
				//{spec{"网络":"移动3G"}}
				newRows.spec[columnName] = columnValue[k];
				//[{spec{"网络":"移动3G"},"price":0,"num":0,"status":"0","isDefault":"0"}]
				newList.push(newRows);
			}
		}
		return newList;
	}
	
	
	//判断当前集合中是否有这个属性
	//list 就是要判断的集合  key 就是要判断是否有这个键值  keyvalue 键值对应里面值
	//list = specificationItems  key = attributeName  keyValue=网络
	//[{"attributeName":"网络制式","attributeValue":["移动4G"]},
	//{"attributeName":"屏幕尺寸","attributeValue":["5.5寸","4.5寸"]}]
	
	// [{"attributeName":"网络","attributeValue":["移动4G"]}]   attributeName   网络
	$scope.searchObjetByKey = function(list,key,keyValue){
		for(var i=0;i<list.length;i++){
			//集合中已经存在了改keyValue值
			if(list[i][key]==keyValue){
				return list[i];
			}
		}
		return null;
	}
	
	//修改规格选项
	//name，value就我们选中的多选框的name 规格名，value 规格值
	$scope.updateSpecItem = function($event,name,value){
		
		//通过名字查询是否属性存在
		//$scope.entity.tbGoodsDesc.specificationItems =[{"attributeName":"网络","attributeValue":["移动3G"]}]
		//name = 网络
		var obj = $scope.searchObjetByKey($scope.entity.tbGoodsDesc.specificationItems,"attributeName",name);
		
		if(obj!=null){
			//obj = {"attributeName":"网络","attributeValue":["移动3G"]}
			// value 移动4G
			//obj = {"attributeName":"网络","attributeValue":["移动3G","移动4G"]}
			
			//判断是否取消了选项
			//如果为true，代表选中
			if($event.target.checked){
				obj.attributeValue.push(value);
			}else{
				//如果没有选中，就从集合中删除
				obj.attributeValue.splice(obj.attributeValue.indexOf(value),1);
				//obj = {"attributeName":"网络","attributeValue":["移动3G"]}
				//如果你把里面所有的属性都删除了，该删除这个对象
				 if(obj.attributeValue.length==0){
					 $scope.entity.tbGoodsDesc.specificationItems.splice($scope.entity.tbGoodsDesc.specificationItems.indexOf(obj),1);
				 }
			}
			
		}else{
			//当原本的规格名不存在的时候，添加一个attributeName和atrributeValue
			//[{"attributeName":"网络","attributeValue":["移动3G"]}]
			$scope.entity.tbGoodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
			
		}
		
		
	}
	
	
	//定义一个方法来访问顶级菜单
	$scope.selectItemCat1List = function(){
		//alert("111")
		itemCatService.findByParentId(0).success(
				function(response){
					//alert("response="+response);
					$scope.itemCat1List = response;
					
				});
	}
	//选择二级菜单
	$scope.$watch("entity.tbGoods.category1Id",function(newValue,oldValue){
		//alert(newValue+'--'+oldValue);
		itemCatService.findByParentId(newValue).success(function(response){
			 $scope.itemCat2List = response;
		});
	});
	//查询三级菜单
	$scope.$watch("entity.tbGoods.category2Id",function(newValue,oldValue){
		//alert(newValue+'--'+oldValue);
		itemCatService.findByParentId(newValue).success(function(response){
			 $scope.itemCat3List = response;
		});
	});

	//查询模板id
	$scope.$watch("entity.tbGoods.category3Id",function(newValue,oldValue){
		itemCatService.findOne(newValue).success(function(response){
			$scope.entity.tbGoods.typeTemplateId = response.typeId
		});
		
	});
	
	//根据模板id，查询对应品牌信息
	$scope.$watch("entity.tbGoods.typeTemplateId",function(newValue,oldValue){
		
		typeTemplateService.findOne(newValue).success(function(response){
			$scope.typeTemplate= response;
			
			//[{"id":1,"text":"联想"},{"id":3,"text":"三星"}]
			$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
			//同时获得自定义属性 [{"text":"内存大小"},{"text":"颜色"}]   [{"text":"内存大小","value":'128M'},{"text":"颜色"}]
			//如果没有ID，则加载模板中的扩展数据
			if($location.search()['id']==null){
			  $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
			}
		});
		
		typeTemplateService.findSpecList(newValue).success(function(response){
			 $scope.specList = response;
		});
		
		
		
	});
	
	
	
	//定义一个方法，把图片信息放到上面的结构
	$scope.add_image_entity = function(){
		$scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
	}
	//从集合删除某个对象
	$scope.remove_image_entity = function(index){
		$scope.entity.tbGoodsDesc.itemImages.splice(index,1);
	}
	
	//图片上传
	$scope.upload = function(){
		//alert('111');
		uploadService.upload().success(
				function(response){
					if(response.success){
						//获得图片名字
						$scope.image_entity.url = response.message;
						$('#file').val("");
					}else{
						alert(response.message);
					}
				}
		      ).error(
		    	function(){
		    		alert("上传出现错误");
		    });
		
	}
	
	//添加商品
	$scope.add = function(){
		//获得文本编辑器的内容
		//alert(editor.html());
		$scope.entity.tbGoodsDesc.introduction = editor.html();
		//alert($scope.entity.tbGoodsDesc.itemImages[0].url);
		goodsService.add($scope.entity).success(
			function(response){
				if(response.success){
					alert("商品添加成功");
					$scope.entity = null;
					editor.html('');
				}else{
					alert("商品添加失败");
				}
			}
			
		);
	};
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
		//?id=xxx
		var id= $location.search()['id'];//获取参数值
		//alert("id="+id);
		if(id==null){
			return ;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;	
				//向富文本编辑器添加商品介绍
				editor.html($scope.entity.tbGoodsDesc.introduction);
				 //显示图片列表
				$scope.entity.tbGoodsDesc.itemImages=  
				JSON.parse($scope.entity.tbGoodsDesc.itemImages);
				//显示扩展属性
				alert("$scope.entity.tbGoodsDesc.customAttributeItems="+$scope.entity.tbGoodsDesc.customAttributeItems);
				$scope.entity.tbGoodsDesc.customAttributeItems=  JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
				//规格				
				$scope.entity.tbGoodsDesc.specificationItems=JSON.parse($scope.entity.tbGoodsDesc.specificationItems);	
				if($scope.entity.tbGoodsDesc.specificationItems!=null){
					$scope.entity.tbGoods.isEnableSpec="1";
				}
				//SKU列表规格列转换				
				for( var i=0;i<$scope.entity.itemList.length;i++ ){
	               $scope.entity.itemList[i].spec = 
	                          JSON.parse( $scope.entity.itemList[i].spec);		
				}		
			
			}
		);				
	}
	
	//保存 
	$scope.save=function(){		
		//提取文本编辑器的值
		$scope.entity.tbGoodsDesc.introduction=editor.html();
		
		var serviceObject;//服务层对象  				
		if($scope.entity.tbGoods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert('保存成功');					
					$scope.entity={};
					editor.html("");
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.ids ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
});	
