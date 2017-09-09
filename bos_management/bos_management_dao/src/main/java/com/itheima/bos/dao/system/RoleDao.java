package com.itheima.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.Role;

public interface RoleDao extends JpaRepository<Role, Integer> {

}
