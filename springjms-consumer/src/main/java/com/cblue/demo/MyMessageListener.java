package com.cblue.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message msg) {
		TextMessage textMessage = (TextMessage)msg;
        try {
			System.out.println("springjsm收到消息:"+textMessage.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

}
