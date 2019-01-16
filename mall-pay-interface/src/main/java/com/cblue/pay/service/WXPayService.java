package com.cblue.pay.service;

import java.util.Map;

public interface WXPayService {
	
	//得到微信支付系统返回的支付二维码字符串
	/**
	 * 
	 * @param out_trade_no 订单号
	 * @param total_fee 支付金额
	 * @return
	 */
	public Map createNativePay(String out_trade_no,String total_fee);
	
	
	/**
	 * 查询订单状态
	 */
	public Map queryPayState(String out_trade_no);
	
	

}
