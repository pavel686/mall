package com.cblue.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cblue.demo.service.UserService;

@Controller
public class UserController {

	@Reference
	private UserService userService;
	
	@RequestMapping("/getUser")
	@ResponseBody
	public String getUser(){
		return userService.getName();
	}
	
	
	
}
