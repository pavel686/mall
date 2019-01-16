package com.cblue.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.cblue.pojo.TbOrderItem;

public class Cart implements Serializable {
	
	//商家id
	private String sellerId;
	//商家名称
	private String sellerName;
	//商品列表
	private List<TbOrderItem>  OrderItemList;
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<TbOrderItem> getOrderItemList() {
		return OrderItemList;
	}
	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		OrderItemList = orderItemList;
	}

}
