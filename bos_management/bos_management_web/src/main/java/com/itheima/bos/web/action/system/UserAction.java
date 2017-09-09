package com.itheima.bos.web.action.system;

import java.io.IOException;

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
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;
import com.itheima.bos.utils.MD5Utils;
import com.itheima.bos.web.action.common.CommonAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class UserAction extends CommonAction<User>{
	private String validateCode;//接收验证码
	
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	
	/**
	 * 基于shiro框架实现用户认证
	 */
	@Action(value="userAction_login",results={
			@Result(name="login",type="redirect",location="/login.jsp"),
			@Result(name="success",type="redirect",location="/index.jsp")})
	public String login(){
		//获取生成的验证码
		String key = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
		if(StringUtils.isNotBlank(validateCode) && StringUtils.isNotBlank(key) && key.equals(validateCode)){
			//页面输入的验证码正确
			Subject subject = SecurityUtils.getSubject();//Subject代表当前用户对象
			//用户名密码令牌
			AuthenticationToken token = new UsernamePasswordToken(getModel().getUsername(), MD5Utils.md5(getModel().getPassword()));
			try{
				subject.login(token);//认证
				//如果shiro框架没有抛出异常，说明认证通过了，跳转到首页
				User user = (User) subject.getPrincipal();
				ServletActionContext.getRequest().getSession().setAttribute("loginUser", user);
				return SUCCESS;
			}catch(Exception e){
				return LOGIN;
			}
		}else{
			//页面输入的验证码错误
			return LOGIN;
		}
	}
	
	/**
	 * 用户注销
	 * @return
	 */
	@Action(value="userAction_logout",results={
			@Result(name="login",type="redirect",location="/login.jsp")})
	public String logout(){
		Subject subject = SecurityUtils.getSubject();//Subject代表当前用户对象
		subject.logout();//基于shiro框架实现退出系统
		ServletActionContext.getRequest().getSession().invalidate();
		return LOGIN;
	}
	
	//属性驱动，接收多个角色id
	private Integer[] roleIds;

	public void setRoleIds(Integer[] roleIds) {
		this.roleIds = roleIds;
	}

	@Autowired
	private UserService service;
	
	/**
	 * 用户保存
	 * @return
	 */
	@Action(value="userAction_save",results={
			@Result(name="success",type="redirect",location="/pages/system/user.jsp")})
	public String save(){
		service.save(getModel(),roleIds);
		return SUCCESS;
	}
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="userAction_pageQuery")
	public String pageQuery() throws IOException{
		//spring data jpa提供的方式，Pageable用于封装查询条件
		Pageable pageable = new PageRequest(page - 1, rows);
		
		Page<User> page = service.pageQuery(pageable);
		
		page2json(page, new String[]{"roles"});
		
		return NONE;
	}
}
