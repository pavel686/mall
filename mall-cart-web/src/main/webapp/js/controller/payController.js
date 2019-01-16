app.controller('payController',function($scope,$controller,$http,payService,$location){	
			
	        $controller('baseController',{$scope:$scope});//继承	
	        
	      $scope.createNative = function(){
	    	  payService.createNative().success(
	    		  function(response){
	    			  $scope.total_fee = (response.total_fee/100);  //支付金额
	    			  $scope.out_trade_no = response.out_trade_no;//订单号
	    			  //二维码对象
	    			  var qr = window.qr = new QRious({
	    			      element: document.getElementById('qrious'),
	    			      size: 250,
	    			      level:'H',
	    			      value: response.code_url
	    			    })
	    			  //查询订单状态
	    			  $scope.queryPayState($scope.out_trade_no);
	    			  
	    		  }	  
	    	  )
	      }
	      
	      $scope.queryPayState = function(out_trade_no){
	    	  payService.queryPayState(out_trade_no).success(
	    			  function(response){
	    				  if(response.success){
	    					  //支付成功，跳转到成功页面
	    					  location.href="paysuccess.html#?money="+$scope.total_fee;
	    				  }else{
	    					  if(response.message="二维码过期"){
	    						  $scope.createNative();
	    					  }else{
	    						  location.href="payfail.html";
	    					  }
	    				  }
	    				  
	    			  }
	    	  )
	      }
	      
	      //获取参数中的支付金额
	      $scope.getMoney = function(){
	    	 return $location.search()['money'];
	      }
	      
	       
			
})
			