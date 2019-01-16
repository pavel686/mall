package com.cblue.pay.service.impl;

import java.util.HashMap;
import java.util.Map;






import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.cblue.common.utils.HttpClient;
import com.cblue.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPayUtil;

@Service
public class WXPayServiceImpl implements WXPayService {
	
	
	@Value("${appid}")
	private String appid;
	
	@Value("${partner}")
	private String partner;
	
	@Value("${partnerkey}")
	private String partnerkey;

	@Override
	public Map createNativePay(String out_trade_no, String total_fee) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("appid",appid);
		//map.put("appid","wx5e9360a3f46f64cd");
		map.put("mch_id", partner);
		//map.put("mch_id", "1322117501");
		
		map.put("nonce_str",WXPayUtil.generateNonceStr());
		map.put("body", "买个小东西");
		map.put("out_trade_no", out_trade_no);
		map.put("total_fee", total_fee);
		
		map.put("spbill_create_ip","192.168.0.1");
		map.put("notify_url","http://www.cblue.com");
		map.put("trade_type","NATIVE");
		
		//签名
		try {
			/*String xmlParam = WXPayUtil.generateSignedXml(map,partnerkey);
			//String xmlParam = WXPayUtil.generateSignedXml(map,"916703CD13C3615B9B629C4A9E4C3337");
			System.out.println("xmlParam="+xmlParam);
			//发送给微信的支付系统
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			httpClient.setHttps(true);
			httpClient.setXmlParam(xmlParam);
			httpClient.post();
			String result = httpClient.getContent();
			//把xml类型转化成map类型
			Map resultMap = WXPayUtil.xmlToMap(result);*/
			Map resultMap = null;
			return resultMap;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int count = 0;
	
	@Override
	public Map queryPayState(String out_trade_no) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("appid",appid);
		map.put("mch_id", partner);
		map.put("out_trade_no", out_trade_no);
		map.put("nonce_str",WXPayUtil.generateNonceStr());
		
		try {
		/*	String xmlParam = WXPayUtil.generateSignedXml(map,partnerkey);
			System.out.println("xmlParam="+xmlParam);
			//发送给微信的支付系统
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			httpClient.setHttps(true);
			httpClient.setXmlParam(xmlParam);
			httpClient.post();
			String result = httpClient.getContent();
			//把xml类型转化成map类型
			Map resultMap = WXPayUtil.xmlToMap(result);*/
			
			//模拟数据
			count++;
			System.out.println("count="+count);
			Map resultmap = null;
			if(count<10){
				resultmap = new HashMap();
				resultmap.put("return_code","SUCCESS");
				resultmap.put("return_msg", "OK");
				resultmap.put("out_trade_no", out_trade_no);
				resultmap.put("nonce_str",WXPayUtil.generateNonceStr());
				resultmap.put("trade_state","NOTPAY");
			}else{
				resultmap = new HashMap();
				resultmap.put("return_code","SUCCESS");
				resultmap.put("return_msg", "OK");
				resultmap.put("out_trade_no", out_trade_no);
				resultmap.put("nonce_str",WXPayUtil.generateNonceStr());
				resultmap.put("trade_state","SUCCESS");
				resultmap.put("transaction_id", "1009660380201506130728806387");
			}
			return resultmap;
			//return resultMap;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

}
