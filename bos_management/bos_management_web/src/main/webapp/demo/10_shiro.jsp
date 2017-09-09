<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<shiro:hasPermission name="abc">
		<input type="button" value="删除">
	</shiro:hasPermission>
	<shiro:authenticated>
		<input type="button" value="认证后可见">
	</shiro:authenticated>
	<shiro:hasRole name="admin">
		<input type="button" value="具有admin角色可见">
	</shiro:hasRole>
	<shiro:user>
		<input type="button" value="用户可见">
	</shiro:user>
	<shiro:notAuthenticated>
		<input type="button" value="未认证可见">
	</shiro:notAuthenticated>
</body>
</html>