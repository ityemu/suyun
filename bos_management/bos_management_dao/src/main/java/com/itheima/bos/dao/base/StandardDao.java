package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Standard;

public interface StandardDao extends JpaRepository<Standard, Integer>{
	public List<Standard> findByNameLike(String name);
	public List<Standard> findByName(String name);
	public List<Standard> findByNameAndMinWeight(String name,Integer min);
	public List<Standard> findByNameOrMinWeight(String name,Integer min);
	public List<Standard> findByNameIsNull();
	public List<Standard> findByNameIsNotNull();
	
	//nativeQuery指定是否为sql
	@Query(value="select * from T_STANDARD where C_NAME = ?",nativeQuery=true)
	public List<Standard> findByNamexxx(String name);
	
	@Query(value="from Standard where name like ?")
	public List<Standard> findByNamexx(String name);
	
	@Modifying
	@Query(value="delete from Standard where name like ?")
	public void deleteByName(String name);
}