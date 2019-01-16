package com.cblue.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-jms-producer.xml")
public class TestQueueProducer {
	
	@Autowired
	private QueueProducer queueProducer;
	
	@Test
	public void testSend(){
		System.out.println((long)(Math.random()*1000000));
	/*	try {
			queueProducer.send("springjsm-queue new");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
