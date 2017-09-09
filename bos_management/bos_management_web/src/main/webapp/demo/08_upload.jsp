<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 引入easyUI相关的资源文件 -->
<link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css">
<script type="text/javascript" src="../js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../js/ocupload/jquery.ocupload-1.1.2.js"></script>
<script type="text/javascript">
	$(function(){
		//页面加载完成后调用upload，这个方法其实是使用jQuery的语法动态修改了页面HTML代码，
		//动态创建了<iframe><form><input type="file">
		$("#mybutton").upload({
			action:'my.action',
			name:'myFile'
		});
	});
</script>
</head>
<body>
	<input id="mybutton" type="button" value="上传">
</body>
</html>