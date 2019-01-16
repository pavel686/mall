package com.cblue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cblue.mapper.UsersMapper;
import com.cblue.pojo.Users;
import com.cblue.pojo.UsersExample;
import com.cblue.pojo.UsersExample.Criteria;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UsersMapper usersMapper;

	@Override
	public void addUser(Users users) {
		// TODO Auto-generated method stub
		usersMapper.insert(users);
	}

	
	@Override
	public List<Users> queryUser(Users users) {
		// TODO Auto-generated method stub
		UsersExample usersExample = new UsersExample();
		Criteria criteria = usersExample.createCriteria();
		criteria.andUsernameEqualTo(users.getUsername());
		List<Users> usersList = usersMapper.selectByExample(usersExample);
		if(usersList.size()>0){
			if(usersList.get(0).getUserpass().equals(users.getUserpass())){
				return usersList;
			}
		}
		return null;
	}


	@Override
	public boolean isExist(String username) {
		// TODO Auto-generated method stub
		UsersExample usersExample = new UsersExample();
		Criteria criteria = usersExample.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<Users> users = usersMapper.selectByExample(usersExample);
		return users.size()>0;
	}

}
