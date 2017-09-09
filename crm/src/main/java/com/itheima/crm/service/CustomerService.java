package com.itheima.crm.service;

import java.util.List;
import javax.jws.WebService;
import com.itheima.crm.domain.Customer;

@WebService
public interface CustomerService {
	//根据客户地址查询定区id
	public String findFixedAreaIdByAddress(String address);
	
	//登录方法
	public Customer login(String telephone,String password);
	
	public List<Customer> findAll();
	
	//查询未关联到定区的客户
	public List<Customer> findCustomersNotAssociation();
	
	//查询已经关联到指定定区的客户
	public List<Customer> findCustomersHasAssociation(String fixedAreaId);
	
	//实现定区关联客户
	public void assignCustomers2FixedArea(String fixedAreaId,Integer[] customerIds);
	
	//根据手机号查询客户
	public Customer findCustomerByTelephone(String telephone);
	
	//客户注册
	public void regist(Customer customer);
	
	//激活邮件
	public void activeMail(String telephone);
	
}
