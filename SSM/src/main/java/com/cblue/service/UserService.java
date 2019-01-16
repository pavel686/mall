package com.cblue.service;

import java.util.List;

import com.cblue.pojo.Users;

public interface UserService {
	
	public void addUser(Users users);
	
	public List<Users> queryUser(Users users);

	public boolean isExist(String username);

	
}
