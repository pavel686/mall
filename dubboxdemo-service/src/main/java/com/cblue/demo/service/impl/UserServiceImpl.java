package com.cblue.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cblue.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	

	public String getName() {
		// TODO Auto-generated method stub
		return "zhangsan";
	}

}
