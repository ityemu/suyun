package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Permission;

public interface PermissionDao extends JpaRepository<Permission, Integer> {

	@Query("select p from Permission p inner join p.roles r inner join r.users u where u.id = ?")
	public List<Permission> findByUserId(Integer userId);

	@Query("select p from Permission p inner join p.roles r where r.id=?")
	public List<Permission> findByRoleId(int parseInt);

}