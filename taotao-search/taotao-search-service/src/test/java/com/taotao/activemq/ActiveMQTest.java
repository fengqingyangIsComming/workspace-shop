package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ActiveMQTest {
	
	@Test
	public void testQueueConsumerSpring() throws Exception {
		// 初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-activemq.xml");
		// 等待
		System.in.read();
	}

}
