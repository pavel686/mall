package com.cblue.manager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	
	//要返回当前对象
	@RequestMapping("loginName")
	public Map getName(){
		//从框架中获得登录的用户名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		map.put("username", username);
		return map;
	}

}
