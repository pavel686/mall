package com.cblue.search.service.impl;

import java.util.Arrays;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.cblue.search.SearchItemService;

public class ItemSearchDeleteMessageListener implements MessageListener {

	
	@Autowired
	private SearchItemService searchItemService;
	
	@Override
	public void onMessage(Message message) {
		System.out.println("接收到删除消息");
		try {
			ObjectMessage objectMessage = (ObjectMessage)message;
			Long[] ids = (Long[])objectMessage.getObject();
			System.out.println("消费者接收到id="+ids);
			searchItemService.deleteByIds(Arrays.asList(ids));
			System.out.println("删除索引库成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
