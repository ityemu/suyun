package com.itheima.bos.realm;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.itheima.bos.dao.system.PermissionDao;
import com.itheima.bos.dao.system.UserDao;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.User;

public class CustomRealm extends AuthorizingRealm{
	@Autowired
	private UserDao userDao ;
	
	@Autowired
	private PermissionDao permissionDao;
	
	//认证方法
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken passwordToken = (UsernamePasswordToken)token;
		String username = passwordToken.getUsername();
		if(StringUtils.isNotBlank(username)){
			//用户名不为空，根据username查询数据库
			User user = userDao.findByUsername(username);
			if(user == null){
				return null;
			}
			//查询到数据了
			String dbPassword = user.getPassword();
			AuthenticationInfo info = new SimpleAuthenticationInfo(user, dbPassword, this.getName());
			return info;
		}else{
			return null;
		}
	}

	//授权方法
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		List<Permission> list = null;
		//根据当前登录用户查询其对于的实际权限
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//User user = (User) SecurityUtils.getSubject().getPrincipal();
		User user = (User) principals.getPrimaryPrincipal();//获取当前登录用户
		if(user.getUsername().equals("admin")){
			//内置管理员账号，用友所有权限
			list = permissionDao.findAll();
		}else{
			//普通用户，查询对应的权限
			list = permissionDao.findByUserId(user.getId());
		}
		for (Permission permission : list) {
			info.addStringPermission(permission.getKeyword());
		}
		return info;
	}
}
