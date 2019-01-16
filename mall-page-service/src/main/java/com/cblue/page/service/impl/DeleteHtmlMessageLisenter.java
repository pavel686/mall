package com.cblue.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.cblue.page.service.ItemPageService;

public class DeleteHtmlMessageLisenter implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Long[] ids = (Long[])objectMessage.getObject();
			itemPageService.deleteItemHtml(ids);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
