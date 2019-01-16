package com.cblue.springboot;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	
	@JmsListener(destination="msg")
	public void getMessage(String msg){
		System.out.println("收到消息："+msg);
	}
	
	
	
	

}
