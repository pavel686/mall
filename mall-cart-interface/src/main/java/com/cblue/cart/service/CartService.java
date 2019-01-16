package com.cblue.cart.service;

import java.util.List;

import com.cblue.pojogroup.Cart;

public interface CartService {
	
	/**
	 * 
	 * @param cartList 原有的购物车
	 * @param ItemId 添加的商品的SKUId
	 * @param num 添加商品数量
	 * @return
	 */
	//添加购物车
	public List<Cart> addGoodToCartList(List<Cart> cartList,Long ItemId,Integer num);
	
	
	//从Redis中查询购物车
	public List<Cart> findCartListFromRedis(String loginUser);
	
	
	//把购物车保存到Redis
	public void saveCartListToRedis(String loginUser,List<Cart> cartList);
	
	//合并购物车
	public List<Cart> megerCartList(List<Cart> cartList1,List<Cart> cartList2);
	

}
