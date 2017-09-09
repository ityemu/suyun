package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;
import com.itheima.bos.web.action.common.CommonAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class PermissionAction extends CommonAction<Permission> {
	@Autowired
	private PermissionService service;
	/**
	 * 保存权限
	 */
	@Action(value="permissionAction_save",results={
			@Result(name="success",type="redirect",location="/pages/system/permission.jsp")})
	public String save(){
		service.save(getModel());
		return SUCCESS;
	}
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="permissionAction_pageQuery")
	public String pageQuery() throws IOException{
		//spring data jpa提供的方式，Pageable用于封装查询条件
		Pageable pageable = new PageRequest(page - 1, rows);
		
		Page<Permission> page = service.pageQuery(pageable);
		
		page2json(page, new String[]{"roles"});
		
		return NONE;
	}
	
	/**
	 * 查询所有权限数据，返回json
	 * @throws IOException 
	 */
	@Action(value="permissionAction_listajax")
	public String listajax() throws IOException{
		List<Permission> list = service.findAll();
		list2json(list, new String[]{"roles"});
		return NONE;
	}
	/**
	 * 查询指定角色的权限
	 * 
	 * @return
	 * @throws Exception
	 */
	private String roleID;

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	@Action("permissionAction_findByRoleId")
	public String findByRoleId() throws Exception {
		List<Permission> list = service.findByRoleId(Integer.parseInt(roleID));

		this.list2json(list, new String[] { "roles" });

		return NONE;

	}
}
