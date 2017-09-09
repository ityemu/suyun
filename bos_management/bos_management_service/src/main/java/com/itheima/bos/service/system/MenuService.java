package com.itheima.bos.service.system;

import java.util.List;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;

public interface MenuService {

	public List<Menu> findAll();

	public List<Menu> findTopMenu();

	public void save(Menu model);

	public List<Menu> findByUser(User user);

	public List<Menu> findByRoleId(int parseInt);

}
