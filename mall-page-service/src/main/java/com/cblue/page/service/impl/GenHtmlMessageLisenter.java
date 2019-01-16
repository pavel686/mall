package com.cblue.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.cblue.page.service.ItemPageService;

public class GenHtmlMessageLisenter implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Long[] ids = (Long[])objectMessage.getObject();
			System.out.println("接收到生成静态文件的id");
			for(Long id:ids){
				itemPageService.genItemHtml(id);
			}
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
