 app.service('cartService',function($http){
	    	
	   this.findCartList = function(){
	    		return $http.get('../cart/findCartList.do');
	    }
	   
	   this.addGoodToCartList = function(ItemId,num){
		   return $http.get('../cart/addGoodToCartList.do?ItemId='+ItemId+'&num='+num);
	   }
	   
	   this.findAddressByUserId = function(){
		   return $http.get('../address/findAddressByUserId.do');
	   }
	   
	   this.addOrder = function(order){
		   return $http.post('../order/add.do',order);
	   }
	   
	   //计算总价
	   this.sum = function(cartList){
		   var totalValue = {totalnum:0,totalprice:0};
		  // alert(cartList.length);
		   for(var i=0;i<cartList.length;i++){
			   var cart = cartList[i];//0 1
			   for(j=0;j<cart.orderItemList.length;j++){
				   var orderItem = cart.orderItemList[j];
				   totalValue.totalnum += orderItem.num;
				   totalValue.totalprice += orderItem.totalFee;
			   }
		   }
		   return totalValue;
	   }
 })