package com.itheima.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.crm.dao.CustomerDao;
import com.itheima.crm.domain.Customer;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{
	@Autowired
	private CustomerDao dao;
	
	//根据客户地址查询定区id
	public String findFixedAreaIdByAddress(String address) {
		return dao.findFixedAreaIdByAddress(address);
	}

	
	//客户登录
	public Customer login(String telephone, String password) {
		return dao.findByTelephoneAndPassword(telephone,password);
	}
	
	public List<Customer> findAll() {
		return dao.findAll();
	}
	
	//查询未关联到定区的客户
	public List<Customer> findCustomersNotAssociation() {
		return  dao.findByFixedAreaIdIsNull();
	}
	
	//查询已经关联到指定定区的客户
	public List<Customer> findCustomersHasAssociation(String fixedAreaId) {
		return dao.findByFixedAreaId(fixedAreaId);
	}

	//实现定区关联客户
	public void assignCustomers2FixedArea(String fixedAreaId, Integer[] customerIds) {
		//1、清理关联关系，当前定区不再关联任何一个客户
		dao.cleanFixedArea4Customer(fixedAreaId);
		if(customerIds != null && customerIds.length > 0){
			for (Integer customerId : customerIds) {
				//2、重新建立定区和客户的关系
				dao.assignCustomers2FixedArea(fixedAreaId, customerId);
			}
		}
	}

	//根据手机号查询客户
	public Customer findCustomerByTelephone(String telephone) {
		return dao.findByTelephone(telephone);
	}

	public void regist(Customer customer) {
		dao.save(customer);
	}

	//客户邮件激活
	public void activeMail(String telephone) {
		dao.activeMail(telephone);
	}

}
