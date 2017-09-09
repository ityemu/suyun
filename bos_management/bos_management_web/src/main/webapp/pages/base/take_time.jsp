<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>收派时间管理</title>
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
		<script type="text/javascript">
			$(function(){
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 收派时间管理信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url : "../../taketimeAction_pageQuery.action",
					idField : 'id',
					columns : columns
				});
			});	
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : function(){
					alert('增加');
				}
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function(){
					alert('修改');
				}
			},{
				id : 'button-delete',
				text : '删除',
				iconCls : 'icon-cancel',
				handler : function(){
					alert('删除');
				}
			} ];
			
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true,
			},{
				field : 'name',
				title : '时间名称',
				width : 120,
				align : 'center'
			}, {
				field : 'normalWorkTime',
				title : '平时上班时间',
				width : 120,
				align : 'center'
			}, {
				field : 'normalDutyTime',
				title : '平时休息时间',
				width : 120,
				align : 'center'
			}, {
				field : 'satWorkTime',
				title : '周六上班时间',
				width : 120,
				align : 'center'
			}, {
				field : 'satDutyTime',
				title : '周六休息时间',
				width : 120,
				align : 'center'
			}, {
				field : 'sunWorkTime',
				title : '周日上班时间',
				width : 120,
				align : 'center'
			}, {
				field : 'sunDutyTime',
				title : '周日休息时间',
				width : 120,
				align : 'center'
			}, {
				field : 'status',
				title : '状态',
				width : 120,
				align : 'center'
			}, {
				field : 'company',
				title : '所属单位',
				width : 120,
				align : 'center'
			} , {
				field : 'operator',
				title : '操作人',
				width : 120,
				align : 'center'
			}, {
				field : 'operatingTime',
				title : '操作时间',
				width : 120,
				align : 'center'
			}, {
				field : 'operatingCompany',
				title : '操作单位',
				width : 120,
				align : 'center'
			} ] ];
		</script>
	</head>
	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
	</body>
</html>
