package com.itheima.bos.service.system.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.RoleDao;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;
@Service
@Transactional
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleDao dao;
	/**
	 * 保存一个角色，同时需要关联菜单和权限
	 */
	public void save(Role role, String menuIds, Integer[] permissionIds) {
		
		if(StringUtils.isNotBlank(menuIds)){
			String[] mIds = menuIds.split(",");
			for (String menuId : mIds) {
				Menu menu = new Menu(Integer.parseInt(menuId));//脱管
				//建立角色和菜单的关系
				role.getMenus().add(menu);
				//menu.getRoles().add(role);
			}
		}
		
		if(permissionIds != null && permissionIds.length > 0){
			for (Integer permissionId : permissionIds) {
				Permission permission = new Permission(permissionId);
				//建立角色和权限的关系
				role.getPermissions().add(permission);
			}
		}
		dao.save(role);
	}
	
	public Page<Role> pageQuery(Pageable pageable) {
		return dao.findAll(pageable);
	}

	public List<Role> findAll() {
		return dao.findAll();
	}

	@Override
	public Role findOne(Integer id) {
		// TODO Auto-generated method stub
		return dao.findOne(id);
	}
}
