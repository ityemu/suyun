package com.itheima.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.UserDao;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;
import com.itheima.bos.utils.MD5Utils;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao dao;
	/**
	 * 保存一个用户，同时需要关联角色
	 */
	public void save(User user, Integer[] roleIds) {
		//将密码进行md5加密
		String md5 = MD5Utils.md5(user.getPassword());
		user.setPassword(md5);
		dao.save(user);
		if(roleIds != null && roleIds.length > 0){
			for (Integer roleId : roleIds) {
				Role role = new Role(roleId);//脱管对象
				//建立用户和角色的关联关系
				user.getRoles().add(role);
			}
		}
	}

	public Page<User> pageQuery(Pageable pageable) {
		return dao.findAll(pageable);
	}
}
