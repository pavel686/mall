package com.cblue.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	@Autowired
	private Environment environment;
	
	@RequestMapping("hello")
	public String hello(){
		System.out.println("333"+environment.getProperty("url"));
		
		return "hello world";
	}

}
