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
</head>
<body>
	<table class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'id'">编号</th>
				<th data-options="field:'name'">姓名</th>
				<th data-options="field:'address'">地址</th>
				<th data-options="field:'telephone'">电话</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>001</td>
				<td>小明</td>
				<td>北京</td>
				<td>123</td>
			</tr>
		</tbody>
	</table>
	<hr>
	<table class="easyui-datagrid" data-options="url:'../data/datagrid.json'">
		<thead>
			<tr>
				<th data-options="field:'id'">编号</th>
				<th data-options="field:'name'">姓名</th>
				<th data-options="field:'address'">地址</th>
				<th data-options="field:'telephone'">电话</th>
			</tr>
		</thead>
	</table>
	<hr>
	<table id="mytable">
	</table>
	<script type="text/javascript">
		$(function(){
			//页面加载完成后，调用easyUI提供的API，动态在页面中创建一个datagrid
			$("#mytable").datagrid({
				//定义表头（标题行）
				columns:[[
				          {title:'编号',field:'id',checkbox:true},//每个json对象表示其中的一列
				          {title:'姓名',field:'name'},
				          {title:'地址',field:'address'},
				          {title:'电话',field:'telephone'},
				          {title:'年龄',field:'age'}
				          ]],
				//指定ajax请求的地址
				url:'../data/datagrid.json',
				rownumbers:true,//显示行号
				singleSelect:true,//是否只能单选
				//定义datagrid的工具栏
				toolbar:'#tb',
				//分页条
				pagination:true
			});
		});
	</script>
	
	<div id="tb">
	  <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true"></a>
	  <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-help',plain:true"></a>
	  <select class="easyui-combobox"></select>
	  <input type="text">
	</div> 
</body>
</html>