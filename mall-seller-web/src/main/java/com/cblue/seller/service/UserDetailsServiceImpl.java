package com.cblue.seller.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cblue.pojo.TbSeller;
import com.cblue.sellergoods.service.SellerService;

import java.util.*;
public class UserDetailsServiceImpl implements UserDetailsService {

	private SellerService sellerService;
	
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}


	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("username="+username);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		//return new User(username, "123", authorities);
		TbSeller tbSeller = sellerService.findOne(username);
		if(tbSeller!=null){
			if(tbSeller.getStatus().equals("1")){
				return new User(username,tbSeller.getPassword(),authorities);
				
			}else{
				return null;
			}
			
		}else{
			return null;
		}
		
		
	}

}
