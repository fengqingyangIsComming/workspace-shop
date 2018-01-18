package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.taotao.content.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	/**
	 * 
	 */
	@Test
	public void jedisSpringTest(){
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-redis.xml");
		JedisClient jedisClient = context.getBean(JedisClient.class);
		jedisClient.set("springjedis", "this is springjedis1");
		System.out.println(jedisClient.get("springjedis"));
	}

	/**
	 * 使用单机版jedis
	 * <p>Title: testRedisSingle</p>
	 * <p>Description: </p>
	 */
	@Test
	public void testRedisSingle(){
		//每次创建连接，耗费资源
		Jedis jedis = new Jedis("192.168.25.153",6379);
		String value1 = jedis.get("key1");
		System.out.println(value1);
		jedis.set("key1", "123");
		value1 = jedis.get("key1");
		System.out.println(value1);
	}
	/**
	 * 使用连接池jedisPool
	 * <p>Title: testRedisPoolSingle</p>
	 * <p>Description: </p>
	 */
	@Test
	public void testRedisPoolSingle(){
		//创建连接池
		JedisPool jedisPool = new JedisPool("192.168.25.153",6379);
		//从连接池获取jedis连接
		Jedis jedis = jedisPool.getResource();
		String value1 = jedis.get("key1");
		System.out.println(value1);
		//使用完回收
		jedis.close();
		jedisPool.close();
	}
	/**
	 * jedis集群连接使用
	 * <p>Title: testRedisClusterSingle</p>
	 * <p>Description: </p>
	 */
	@Test
	public void testRedisClusterSingle(){
		Set<HostAndPort> nodes=new HashSet<>();
		for (int i = 0;i<=5;i++) {
			HostAndPort hostAndPort = new HostAndPort("192.168.25.153", 7001+i);
			nodes.add(hostAndPort);
		}
		//连接集群使用jediscluster对象,包含连接池，可在系统中单例使用
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("jedisCluster", "123456");
		String res = jedisCluster.get("jedisCluster");
		System.out.println(res);
		//在系统结束时关闭
		jedisCluster.close();
	}
}
