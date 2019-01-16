package com.cblue.sms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

@Component
public class SmsListener {
	
	
	@Autowired
	private SmsUtils smsUtils;
	

	
	@JmsListener(destination="sms")
	public void getMessage(Map<String,String> map){
		try {
			SendSmsResponse response = smsUtils.sendSms(map.get("phoneNumber"), map.get("signName"), map.get("templateCode"), map.get("templateParam"));
	        System.out.println("短信接口返回的数据----------------");
	        System.out.println("Code=" + response.getCode());
	        System.out.println("Message=" + response.getMessage());
	        System.out.println("RequestId=" + response.getRequestId());
	        System.out.println("BizId=" + response.getBizId());
		   
		
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
