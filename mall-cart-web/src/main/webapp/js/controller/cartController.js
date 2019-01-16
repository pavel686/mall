app.controller('cartController',function($scope,$controller,$http,cartService){	
			
	        $controller('baseController',{$scope:$scope});//继承	
	        
	        
	        //添加订单
	        $scope.addOrder = function(){
	        	//alert(111);
	        	$scope.order.receiverAreaName = $scope.address.address;
	        	$scope.order.receiverMobile = $scope.address.mobile;
	        	$scope.order.receiver = $scope.address.contact;
	             
	        	cartService.addOrder($scope.order).success(
	        		 function(response){
	        			 if(response.success){
	        				 //如果你是微信支付，就跳转到微信支付页面
	        				 if($scope.order.paymentType==1){
	        					 location.href="pay.html";
	        				 }else{
	        					 //如果你是货到付款，就直接跳转到完成页面
	        					 location.href="paysuccess.html"
	        				 }
	        			 }
	        			 
	        		 }
	        		
	        	)
	        	
	        }
	        
	        //定义一个支付方式
	        $scope.order={paymentType:1,sourceType:2};
	        
	        
	        //设置支付方式
	        $scope.selectPaymentType=function(type){
	        	//alert(111);
	        	$scope.order.paymentType = type;
	        }
	        

	        
	        //选择收货地址
	        $scope.selectAddress = function(address){
	        	//alert(111);
	        	$scope.address = address;
	        }
	        
	        //判断当前地址是否为默认地址
	        $scope.isSelectAddress = function(address){
	        	if(address==$scope.address){
	        		return true;
	        	}
	        	return false;
	        }
	        
	        //获得收货地址列表
	        $scope.findAddressByUserId = function(){
	        	cartService.findAddressByUserId().success(
	        	   function(response){
	        		   $scope.addressList = response;
	        		   for(var i=0;i<$scope.addressList.length;i++){
	        			   if($scope.addressList[i].isDefault==1){
	        				   $scope.address = $scope.addressList[i];
	        				   break;
	        			   }
	        		   }
	        	   }		
	        	)
	        }
			 
	        //获取购物车列表
	        $scope.findCartList = function(){
	        	cartService.findCartList().success(
	        	   function(response){
	        		   $scope.cartList = response;
	        		   $scope.totalValue = cartService.sum($scope.cartList);
	        		   
	        	   } 		
	        	)
	        }
	        
	        //添加购物车
	        $scope.addGoodToCartList = function(itemId,num){
	        	cartService.addGoodToCartList(itemId,num).success(
	        	   function(response){
	        		   if(response.success){
	        			   //刷新列表
	        			   $scope.findCartList()
	        		   }else{
	        			   alert(response.message);
	        		   }
	        	   }		
	        	)
	        }
			
			
})
			