<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>角色列表页面</title>
		<!-- 导入jquery核心类库 -->
		<script type="text/javascript" src="../../js/jquery-1.8.3.js"></script>
		<!-- 导入easyui类库 -->
		<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="../../js/easyui/ext/portal.css">
		<link rel="stylesheet" type="text/css" href="../../css/default.css">
		<script type="text/javascript" src="../../js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../../js/easyui/ext/jquery.portal.js"></script>
		<script type="text/javascript" src="../../js/easyui/ext/jquery.cookie.js"></script>
		<script src="../../js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
		<!-- 导入ztree类库 -->
		<link rel="stylesheet" href="../../js/ztree/zTreeStyle.css" type="text/css" />
		<script src="../../js/ztree/jquery.ztree.all-3.5.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(function(){
				// 数据表格属性
				$("#grid").datagrid({
					toolbar : [
						{
							id : 'add',
							text : '添加角色',
							iconCls : 'icon-add',
							handler : function(){
								location.href='role_add.jsp';
							}
						},
						{
							id : 'edit',
							text : '修改角色',
							iconCls : 'icon-edit',
							handler : function(){
								var roleId = $("#grid").datagrid('getSelections');
								if(roleId.length==1){
									
								    location.href='${pageContext.request.contextPath}/pages/system/role_update.jsp?roleId='+roleId[0].id;
								
								}else{
									
									$.messager.alert('警告','请选择一个修改','warning'); 
								}
								
							}
						}
					],
					url : '../../roleAction_pageQuery.action',
					pagination:true,
					fit:true,
					columns : [[
						{
							field : 'id',
							title : '编号',
							width : 200,
							checkbox : true
						},
						{
							field : 'name',
							title : '名称',
							width : 200
						}, 
						{
							field : 'keyword',
							title : '关键字',
							width : 200
						}, 
						{
							field : 'description',
							title : '描述',
							width : 200
						} 
					]]
				});
			});
		</script>
	</head>

	<body class="easyui-layout">
		<div data-options="region:'center'">
			<table id="grid"></table>
		</div>
	</body>

</html>