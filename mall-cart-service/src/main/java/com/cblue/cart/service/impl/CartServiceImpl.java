package com.cblue.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.cblue.cart.service.CartService;
import com.cblue.mapper.TbItemMapper;
import com.cblue.pojo.TbItem;
import com.cblue.pojo.TbOrderItem;
import com.cblue.pojogroup.Cart;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<Cart> addGoodToCartList(List<Cart> cartList, Long ItemId,
			Integer num) {
		//根据SKU找到对应的商品信息
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(ItemId);
		//判断商品是否存在
		if(tbItem==null){
		  throw new RuntimeException("商品不存在");	
		}
		//判断商品是否有效
		if(!tbItem.getStatus().equals("1")){
			throw new RuntimeException("商品已失效");	
		}
		//获得商家id
		String sellerId = tbItem.getSellerId();
		
		Cart cart = null;
		
		//判断当前商家是否在购物车中
		if(cartList !=null){
			cart = searchCarBySellerId(cartList,sellerId);
		}
		//判断这个商家是否存在，如果不存在，创建一个购物车对象，如果存在，把商品添加到购物车
		if(cart==null){
			System.out.println("111");
			//新建一个购物车对象
			cart = new Cart();
			cart.setSellerId(sellerId);
			cart.setSellerName(tbItem.getSeller());
			TbOrderItem tbOrderItem = createOrderItem(tbItem, num);
			System.out.println("tbOrderItem="+tbOrderItem);
			List<TbOrderItem> tbOrderItems = new ArrayList<TbOrderItem>();
			tbOrderItems.add(tbOrderItem);
			cart.setOrderItemList(tbOrderItems);
			//把这个购物车对象，加到购物车集合
			cartList.add(cart);
		}else{
			//如果不存在这个商品，我就在tbOrderItem添加这个商品。如果存在，累加数量
			TbOrderItem tbOrderItem = searchTbOrderItemByItemId(cart.getOrderItemList(),ItemId);
			if(tbOrderItem==null){
				System.out.println("222");
				TbOrderItem tbOrderItem1 = createOrderItem(tbItem, num);
				cart.getOrderItemList().add(tbOrderItem1);
			}else{
				System.out.println("333");
				//设置商品数量
				tbOrderItem.setNum(tbOrderItem.getNum()+num);
				//设置商品总价
				tbOrderItem.setTotalFee(new BigDecimal(tbOrderItem.getPrice().doubleValue()*tbOrderItem.getNum()));
				//如果商品的数量为0，就是移除商品
				if(tbOrderItem.getNum()<1){
					cart.getOrderItemList().remove(tbOrderItem);
				}
				//如果购物上中没有商品，移除这个购物车
				if(cart.getOrderItemList().size()<1){
					cartList.remove(cart);
				}
			}
			
		}
		return cartList;
	}
	
	/**
	 * 查找购物车，查看里面是否有该商家，如果有，返回当前商家的购物车对象，否则返回null
	 * @param cart 购物车信息
	 * @param sellerId 商家id
	 * @return
	 */
	private Cart searchCarBySellerId(List<Cart> cartList,String sellerId){
		for(Cart cart :cartList){
			if(cart.getSellerId().equals(sellerId)){
				return cart;
			}
		}
		return null;
	}
	
	/**
	 * 创建订单详情
	 */
	private TbOrderItem createOrderItem(TbItem tbItem,Integer num){
		TbOrderItem tbOrderItem = new TbOrderItem();
		tbOrderItem.setGoodsId(tbItem.getGoodsId());
		tbOrderItem.setItemId(tbItem.getId());
		tbOrderItem.setNum(num);
		tbOrderItem.setPrice(tbItem.getPrice());
		tbOrderItem.setPicPath(tbItem.getImage());
		tbOrderItem.setSellerId(tbItem.getSellerId());
		tbOrderItem.setTitle(tbItem.getTitle());
		tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue()*num));
		return tbOrderItem;
	}
	//判断购物车中是否存在该商品
	private TbOrderItem searchTbOrderItemByItemId(List<TbOrderItem> tbOrderItems,Long ItemId){
	  for(TbOrderItem tbOrderItem:tbOrderItems){
		  System.out.println(tbOrderItem.getItemId().longValue()==ItemId.longValue());
		  System.out.println(tbOrderItem.getItemId());
		  System.out.println(ItemId);
		  if(tbOrderItem.getItemId().longValue()==ItemId.longValue()){
			  return tbOrderItem;
		  }
	  }	
	  return null;
	}

	@Override
	public List<Cart> findCartListFromRedis(String loginUser) {
		// TODO Auto-generated method stub
		System.out.println("从redis取"+loginUser);
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartlist").get(loginUser);
		if(cartList==null){
			cartList = new ArrayList<Cart>();
		}
		return cartList;
	}

	@Override
	public void saveCartListToRedis(String loginUser, List<Cart> cartList) {
		System.out.println("向redis中存"+loginUser);
		redisTemplate.boundHashOps("cartlist").put(loginUser, cartList);
		
	}

	@Override
	public List<Cart> megerCartList(List<Cart> cartList1, List<Cart> cartList2) {
		// TODO Auto-generated method stub
		System.out.println("合并购物车");
		for(Cart cart:cartList2){
			for(TbOrderItem tbOrderItem :cart.getOrderItemList()){	
				this.addGoodToCartList(cartList1, tbOrderItem.getItemId(), tbOrderItem.getNum());
			}
		}
		return cartList1;
	}

}
