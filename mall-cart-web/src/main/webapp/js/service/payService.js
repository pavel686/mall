 app.service('payService',function($http){
	    	
	 
	   //访问微信扫码支付
       this.createNative = function(){
       	 return $http.get("../pay/createNative.do");
       }
       
       //查询支付状态
       this.queryPayState = function(out_trade_no){
    	   return  $http.get("../pay/queryPayState.do?out_trade_no="+out_trade_no);
       }
 })