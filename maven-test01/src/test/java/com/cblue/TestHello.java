package com.cblue;

import org.junit.Test;

public class TestHello {
	
	@Test
	public void testHello(){
		Hello hello = new Hello();
		String result = hello.sayHello();
		assert result =="hello";
	}

}
