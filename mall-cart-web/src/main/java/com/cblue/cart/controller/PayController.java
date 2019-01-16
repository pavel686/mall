package com.cblue.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cblue.common.utils.IdWorker;
import com.cblue.order.service.OrderService;
import com.cblue.pay.service.WXPayService;
import com.cblue.pojo.TbPayLog;

import entity.Result;

@RestController
@RequestMapping("/pay")

public class PayController {
	
	@Reference(timeout=360000)
	private WXPayService wxPayService;
	
	@Reference
	private OrderService orderService;
	
	@RequestMapping("/createNative")
	public Map createNave(){
		//用雪花算法模拟
		//IdWorker idWorker = new IdWorker();
		//wxPayService.createNativePay(idWorker.nextId()+"","1");
		String UserId = SecurityContextHolder.getContext().getAuthentication().getName();
		TbPayLog tbPayLog = orderService.queryTbPayLogByUserId(UserId);
		if(tbPayLog!=null){
			wxPayService.createNativePay(tbPayLog.getOrderList(), tbPayLog.getTotalFee()+"");
		}
		Map map = new HashMap();
		map.put("return_code", "SUCCESS");
		map.put("return_msg", "OK");
		map.put("appid", "wx2421b1c4370ec43b");
		map.put("code_url","weixin://wxpay/s/An4baqw");
		map.put("out_trade_no",tbPayLog.getOrderList());
		map.put("total_fee", tbPayLog.getTotalFee());
		return map;
	}

	@RequestMapping("/queryPayState")
	public Result queryPayState(String out_trade_no){
		Result result = null;
		
		//默认5分钟未支付就关闭查询
		int lastTime = 0;
		while(true){
			Map map = wxPayService.queryPayState(out_trade_no);
			//支付出错
			if(map==null){
				 result = new Result(false, "支付出错");
				 break;
			}
			//支付成功
			if(map.get("trade_state").equals("SUCCESS")){
				 result = new Result(true, "支付成功");
				 //更新支付状态
				 orderService.updateOrderStatus(out_trade_no, map.get("transaction_id").toString());
				 break;
			}
			
			lastTime++;
			if(lastTime>=60){
				result = new Result(false, "二维码过期");
				break;
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return result;
	}
	
	
	
	
}
