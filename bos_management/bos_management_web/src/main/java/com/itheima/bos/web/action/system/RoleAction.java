package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

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
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;
import com.itheima.bos.web.action.common.CommonAction;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class RoleAction extends CommonAction<Role>{
	//属性驱动，接收拼接的菜单id--->>1,2,3,4
	private String menuIds;
	
	//属性驱动，接收多个权限id
	private Integer[] permissionIds;
	
	public void setPermissionIds(Integer[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	
	@Autowired
	private RoleService service;
	
	/**
	 * 保存菜单
	 */
	@Action(value="roleAction_save",results={
			@Result(name="success",type="redirect",location="/pages/system/role.jsp")})
	public String save(){
		service.save(getModel(),menuIds,permissionIds);
		return SUCCESS;
	}
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="roleAction_pageQuery")
	public String pageQuery() throws IOException{
		//spring data jpa提供的方式，Pageable用于封装查询条件
		Pageable pageable = new PageRequest(page - 1, rows);
		
		Page<Role> page = service.pageQuery(pageable);
		
		page2json(page, new String[]{"permissions","menus","users"});
		
		return NONE;
	}
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="roleAction_listajax")
	public String listajax() throws IOException{
		//spring data jpa提供的方式，Pageable用于封装查询条件
		List<Role> list = service.findAll();
		list2json(list, new String[]{"permissions","menus","users"});
		return NONE;
	}
	/**
	 * 查询所有角色
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("roleAction_findOne")
	public String findOne() throws Exception {
	//  List<Role> list = new ArrayList<Role>();
		Role role  = service.findOne(getModel().getId());
	//	list.add(role);
	//	this.list2json(list, new String[] { "users", "permissions", "menus" });
		
		
	//使用json-lib将page对象转为json数据，提供给datagrid进行展示
	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.setExcludes(new String[] { "users", "permissions", "menus" });
	String json = JSONObject.fromObject(role,jsonConfig).toString();
	
	//使用输出流将json数据写回到客户端
	ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
	try {
		ServletActionContext.getResponse().getWriter().print(json);
	} catch (IOException e) {
		e.printStackTrace();
	}
		return NONE;
	}
	
}
