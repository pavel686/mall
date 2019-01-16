package com.cblue.springjms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicProduct {

	public static void main(String[] args) throws Exception{
		
		//创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.137:61616");
		//通过连接工厂获得连接对象
		Connection conn = connectionFactory.createConnection();
		//启动连接
		conn.start();
		//根据连接获得session 参数1：代表是否启动事务  参数2：消息确认的模式
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//通过session产生队列对象
		Topic topic = session.createTopic("test-topic");
		//产生一个消息生产者
		MessageProducer messageProducer =session.createProducer(topic);
		//创建一个消息对象
		TextMessage textMessage = session.createTextMessage("topic message");
		//消息生产者发送消息
		messageProducer.send(textMessage);
		//关闭资源
		messageProducer.close();
		session.close();
		conn.close();
	}
}
