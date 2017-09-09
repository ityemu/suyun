package com.itheima.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;

public interface RoleService {

	public void save(Role model, String menuIds, Integer[] permissionIds);

	public Page<Role> pageQuery(Pageable pageable);

	public List<Role> findAll();

	public Role findOne(Integer id);

}
