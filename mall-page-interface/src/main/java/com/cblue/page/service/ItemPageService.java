package com.cblue.page.service;

public interface ItemPageService {
	
	//根据商品id生成静态页
	public boolean genItemHtml(Long goodId);
	
	//删除静态页
	public boolean deleteItemHtml(Long[] goodIds);

}
