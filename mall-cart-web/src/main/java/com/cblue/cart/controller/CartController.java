package com.cblue.cart.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cblue.cart.service.CartService;
import com.cblue.common.utils.CookieUtil;
import com.cblue.pojogroup.Cart;

import entity.Result;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Reference
	private CartService cartService;
	
	//显示购物车
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(HttpServletRequest request,HttpServletResponse response){
		String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("loginName="+loginName);
		String cartListStr = CookieUtil.getCookieValue(request, "cartList", "utf-8");
		if(cartListStr==null||cartListStr.equals("")){
			cartListStr = "[]";
		}
		List<Cart> cartList_Cookie = JSON.parseArray(cartListStr, Cart.class);
		//用户未登录
		if("anonymousUser".equals(loginName)){
			return cartList_Cookie;
		}else{
			//用户登录
			//从redis中获取数据
			List<Cart> cartList_redis = cartService.findCartListFromRedis(loginName);
			System.out.println("cartList_Cookie.size()="+cartList_Cookie.size());
			//从cookie获取数据
			if(cartList_Cookie.size()>0){
				
				cartList_redis = cartService.megerCartList(cartList_redis, cartList_Cookie);
				//把cookie删除
				CookieUtil.deleteCookie(request, response, "cartList");
				//把合并后的购车信息保存到redis中
				cartService.saveCartListToRedis(loginName, cartList_redis);
			}			
			return cartList_redis;
		}
	}
	
	//添加购物车
	@RequestMapping("/addGoodToCartList")
	public Result addGoodToCartList(Long ItemId,int num,HttpServletRequest request,HttpServletResponse response){
		try {	
			//你允许访问的域名
			response.setHeader("Access-Control-Allow-Origin", "http://localhost");
			//这个是设置允许请求操作cookie的证书
			response.setHeader("Access-Control-Allow-Credentials", "true");
			
			String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
			//获得原有购物车对象
			List<Cart> cartList = this.findCartList(request,response);
			//添加购物车
			cartList = cartService.addGoodToCartList(cartList, ItemId, num);
			//用户未登录
			if("anonymousUser".equals(loginName)){
				//把购物车转化成json字符串，保存到cookie
				CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600*24, "utf-8");
				return new Result(true, "cookie添加成功");
			}else{
				cartService.saveCartListToRedis(loginName, cartList);
				return new Result(true, "redis添加成功");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Result(false, "添加失败");
		}
	}
	
	
	

}
