package com.cblue.pojogroup;

import java.util.List;

import com.cblue.pojo.TbGoods;
import com.cblue.pojo.TbGoodsDesc;
import com.cblue.pojo.TbItem;

public class Goods implements java.io.Serializable {
	
	private TbGoods tbGoods;
	private TbGoodsDesc tbGoodsDesc;
	private List<TbItem> itemList;
	
	
	public TbGoods getTbGoods() {
		return tbGoods;
	}
	public void setTbGoods(TbGoods tbGoods) {
		this.tbGoods = tbGoods;
	}
	public TbGoodsDesc getTbGoodsDesc() {
		return tbGoodsDesc;
	}
	public void setTbGoodsDesc(TbGoodsDesc tbGoodsDesc) {
		this.tbGoodsDesc = tbGoodsDesc;
	}
	public List<TbItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}
	
	
	

}
