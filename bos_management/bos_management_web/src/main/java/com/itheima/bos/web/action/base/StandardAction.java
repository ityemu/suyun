package com.itheima.bos.web.action.base;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
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
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;
import com.itheima.bos.web.action.common.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 收派标准管理
 * @author zhaoqx
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class StandardAction extends CommonAction<Standard>{
	//注入Service
	@Autowired
	private StandardService service;
	
	/**
	 * 保存收派标准
	 */
	@Action(value="standardAction_save",results={
			@Result(name="success",type="redirect",location="/pages/base/standard.jsp")})
	public String save(){
		Subject subject = SecurityUtils.getSubject();
		subject.checkPermission("abc");
		
		service.save(getModel());
		return SUCCESS;
	}

	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="standardAction_pageQuery")
	public String pageQuery() throws IOException{
		//spring data jpa提供的方式，Pageable用于封装查询条件
		Pageable pageable = new PageRequest(page - 1, rows);
		
		Page<Standard> page = service.pageQuery(pageable);
		
		page2json(page, null);
		
		return NONE;
	}
	
	/**
	 * 查询所有收派标准数据，返回json
	 */
	@Action(value="standardAction_findAll")
	public String findAll() throws IOException{
		List<Standard> list = service.findAll();
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"maxLength","maxWeight","minLength"});
		String json = JSONArray.fromObject(list,jsonConfig).toString();
		
		//使用输出流将json数据写回到客户端
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(json);
		
		return NONE;
	}
}
