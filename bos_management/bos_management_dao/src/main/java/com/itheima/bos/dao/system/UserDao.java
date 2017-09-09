package com.itheima.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.User;

public interface UserDao extends JpaRepository<User, Integer> {

	public User findByUsername(String username);

}
