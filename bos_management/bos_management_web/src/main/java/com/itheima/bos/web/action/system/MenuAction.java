package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;
import com.itheima.bos.utils.MD5Utils;
import com.itheima.bos.web.action.common.CommonAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class MenuAction extends CommonAction<Menu>{
	@Autowired
	private MenuService service;
	
	/**
	 * 加载所有的菜单数据，用于combotree页面展示
	 */
	@Action(value="menuAction_listajax")
	public String listajax(){
		List<Menu> list = service.findTopMenu();
		this.list2json(list, new String[]{"roles","childrenMenus","parentMenu"});
		return NONE;
	}
	
	/**
	 * 保存菜单
	 */
	@Action(value="menuAction_save",results={
			@Result(name="success",type="redirect",location="/pages/system/menu.jsp")})
	public String save(){
		service.save(getModel());
		return SUCCESS;
	}
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="menuAction_findAll")
	public String findAll() throws IOException{
		List<Menu> list = service.findAll();
		this.list2json(list, new String[]{"roles","childrenMenus","parentMenu"});
		return NONE;
	}
	
	/**
	 * 根据登录人查询对应的菜单
	 * @throws IOException 
	 */
	@Action(value="menuAction_showMenu")
	public String showMenu() throws IOException{
		//获得当前登录用户对象
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<Menu> list = service.findByUser(user);
		this.list2json(list, new String[]{"roles","childrenMenus","parentMenu","children"});
		return NONE;
	}
	
	/*=============================================================*/
	/**
	 * 查询角色的菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	private String roleID;

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
 
	@Action("menuAction_findByRoleId")
	public String findByRoleId() throws Exception {
		
		List<Menu> list = new ArrayList<Menu>();

		if (StringUtils.isNotBlank(roleID)) {

			list = service.findByRoleId(Integer.parseInt(roleID));

			this.list2json(list, new String[] { "roles", "childrenMenus", "parentMenu","children" });
		}

		return NONE;
	}
}
