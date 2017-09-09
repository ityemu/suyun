package com.itheima.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class JedisTest {
	@Test
	public void test1(){
		Jedis jedis = new Jedis("localhost");
		jedis.set("k2", "v2");
		String value = jedis.get("k2");
		System.out.println(value);
		jedis.del("k2");
	}
	
	//注入RedisTemplate对象
	@Autowired
	private RedisTemplate<String, Object> template;
	
	@Test
	public void test2(){
		template.opsForValue().set("key3", "value3");
		Object value = template.opsForValue().get("key3");
		System.out.println(value);
		template.delete("key3");
		template.opsForValue().set("k4", "v4", 24, TimeUnit.HOURS);
		
	}
}
