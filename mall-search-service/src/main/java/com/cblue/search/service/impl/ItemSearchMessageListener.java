package com.cblue.search.service.impl;

import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.cblue.pojo.TbItem;
import com.cblue.search.SearchItemService;

public class ItemSearchMessageListener implements MessageListener {

	@Autowired
	private SearchItemService searchItemService;
	
	@Override
	public void onMessage(Message message) {
		
		
		try {
			TextMessage textMessage = (TextMessage)message;
			String tbItemListJSONStr = textMessage.getText();
			List<TbItem> tbItemList = JSON.parseArray(tbItemListJSONStr, TbItem.class);
			for(TbItem tbItem:tbItemList){
				System.out.println(tbItem.getSpec());
				Map map = JSON.parseObject(tbItem.getSpec());
				tbItem.setSpecMap(map);
			}
			searchItemService.importList(tbItemList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
