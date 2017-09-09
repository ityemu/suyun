package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Menu;

public interface MenuDao extends JpaRepository<Menu, Integer> {

	public List<Menu> findByParentMenuIsNull();

	@Query("select distinct m from Menu m inner join m.roles r inner join r.users u where u.id = ?")
	public List<Menu> findByUserId(Integer userId);
	
	@Query("select m from Menu m inner join m.roles r where r.id=?")
	public List<Menu> findByRoleId(int parseInt);

}
