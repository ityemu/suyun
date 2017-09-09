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
<script type="text/javascript">
	$(function(){
		//$.messager.alert("这是标题啊","这是提示的内容啊！！","question");
		/* $.messager.confirm("这是标题啊","这是提示的内容啊！！",function(r){
			alert(r);
		}); */
		
		$.messager.show({
			  title:'今日头条',  	
			  msg:'今天立秋！！',  	
			  timeout:5000,  	
			  showType:'slide'  
		});
	});
</script>
</head>
<body>
</body>
</html>