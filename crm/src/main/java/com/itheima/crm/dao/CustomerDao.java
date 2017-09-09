package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
	//根据客户地址查询定区id
	@Query(value="select fixedAreaId from Customer where address = ?")
	public String findFixedAreaIdByAddress(String address);
	
	//根据手机号和密码进行查询
	public Customer findByTelephoneAndPassword(String telephone, String password);
	
	public List<Customer> findByFixedAreaIdIsNull();
	
	public List<Customer> findByFixedAreaId(String fixedAreaId);
	
	//清理定区和客户的关联关系
	@Query(value="update Customer set fixedAreaId = null where fixedAreaId  = ?")
	@Modifying
	public void cleanFixedArea4Customer(String fixedAreaId);
	
	//建立客户和定区的关联关系
	@Query(value="update Customer set fixedAreaId = ? where id  = ?")
	@Modifying
	public void assignCustomers2FixedArea(String fixedAreaId,Integer id);

	public Customer findByTelephone(String telephone);

	//客户邮件激活
	@Query(value="update Customer set type = 1 where telephone = ?")
	@Modifying
	public void activeMail(String telephone);

}
