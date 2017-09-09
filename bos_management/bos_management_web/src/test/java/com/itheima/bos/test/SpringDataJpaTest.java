package com.itheima.bos.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardDao;
import com.itheima.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class SpringDataJpaTest {
	//注入接口对应的代理对象
	@Autowired
	private StandardDao proxy;
	
	@Test
	public void test1(){
		List<Standard> list = proxy.findAll();
		System.out.println(list);
	}
	
	//根据收派标准的名称模糊查询
	@Test
	@Transactional
	public void test2(){
		//List<Standard> list = proxy.findByNamexx("xx");
		//System.out.println(list);
		proxy.deleteByName("dd");
	}
}
