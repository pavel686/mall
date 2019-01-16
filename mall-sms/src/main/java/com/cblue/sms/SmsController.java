package com.cblue.sms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@RequestMapping("sendSMS")
	public void send(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("phoneNumber","13977307504");
		map.put("signName", "优才");
		map.put("templateCode", "SMS_144456050");
		map.put("templateParam", "{\"code\":\"123\"}");
		jmsMessagingTemplate.convertAndSend("sms",map);
	}
	
	
	

}
