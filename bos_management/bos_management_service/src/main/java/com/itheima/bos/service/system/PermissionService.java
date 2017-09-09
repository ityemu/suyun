package com.itheima.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Permission;

public interface PermissionService {

	public void save(Permission model);

	public Page<Permission> pageQuery(Pageable pageable);

	public List<Permission> findAll();

	public List<Permission> findByRoleId(int parseInt);

}
