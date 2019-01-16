package com.cblue.springjms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueConsumer {

	public static void main(String[] args) throws Exception {
		// 创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://192.168.0.137:61616");
		// 通过连接工厂获得连接对象
		Connection conn = connectionFactory.createConnection();
		// 启动连接
		conn.start();
		// 根据连接获得session 参数1：代表是否启动事务 参数2：消息确认的模式
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 通过session产生队列对象
		Queue queue = session.createQueue("test-queue");
		
		//创建消费者
		MessageConsumer messageConsumer = session.createConsumer(queue);
		
		//监听消息
		messageConsumer.setMessageListener(new MessageListener() {
			public void onMessage(Message msg) {
				TextMessage textMessage = (TextMessage)msg;
				try {
					System.out.println("接收到消息:"+textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//等待键盘输入
		System.in.read();
		
		//关闭资源
		messageConsumer.close();
		session.close();
		conn.close();
		

	}

}
