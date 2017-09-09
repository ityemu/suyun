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
	<table id="mytable">
	</table>
	<script type="text/javascript">
		$(function(){
			//页面加载完成后，调用easyUI提供的API，动态在页面中创建一个datagrid
			$("#mytable").datagrid({
				//定义表头（标题行）
				columns:[[
				          {title:'编号',field:'id',checkbox:true},//每个json对象表示其中的一列
				          
				          {width:200, title:'姓名',field:'name',editor:{//指定当前列（name）可以进行编辑
				        	  type:'validatebox',
				        	  options:{}
				          }},
				          {title:'地址',field:'address',editor:{//指定当前列（name）可以进行编辑
				        	  type:'validatebox',
				        	  options:{}
				          }},
				          {title:'电话',field:'telephone'},
				          {title:'年龄',field:'age'}
				          ]],
				//指定ajax请求的地址
				url:'../data/datagrid.json',
				rownumbers:true,//显示行号
				singleSelect:true,//是否只能单选
				//定义datagrid的工具栏
				toolbar:'#tb',
				onAfterEdit:function(rowIndex,rowData,changes){
					$.post("xxx.action",rowData,function(){
						
					});
				}
			});
			
			$("#edit").click(function(){
				$("#mytable").datagrid("beginEdit",0);
			});
			
			$("#save").click(function(){
				$("#mytable").datagrid("endEdit",0);
			});
			
			$("#add").click(function(){
				$("#mytable").datagrid("insertRow",{
					index:0,//在第一行插入
					row:{}//空行
				});
				$("#mytable").datagrid("beginEdit",0);//开启编辑
			});
			
			$("#remove").click(function(){
				$("#mytable").datagrid("deleteRow",0);//开启编辑
			});
		});
	</script>
	
	<div id="tb">
	  <a id="edit" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true"></a>
	  <a id="save" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"></a>
	  <a id="add" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"></a>
	  <a id="remove" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"></a>
	</div> 
</body>
</html>