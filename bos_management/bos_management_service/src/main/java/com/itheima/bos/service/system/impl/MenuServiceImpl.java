package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuDao;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao dao;
	public List<Menu> findAll() {
		return dao.findAll();
	}
	
	/**
	 * 查询顶级菜单
	 */
	public List<Menu> findTopMenu() {
		return dao.findByParentMenuIsNull();
	}

	public void save(Menu model) {
		if(model.getParentMenu() != null && model.getParentMenu().getId() == null){
			model.setParentMenu(null);
		}
		dao.save(model);
	}

	/**
	 * 根据用户查询对应的菜单
	 */
	public List<Menu> findByUser(User user) {
		if(user.getUsername().equals("admin")){
			return dao.findAll();
		}
		return dao.findByUserId(user.getId());
	}
	/**
	 * 根据角色查询对应的菜单
	 */
	@Override
	public List<Menu> findByRoleId(int parseInt) {
		
		return dao.findByRoleId(parseInt);
	}
}
