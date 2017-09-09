<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>运单快速录入</title>
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
			var editIndex ;
			
			function doAdd(){
				if(editIndex != undefined){
					$("#grid").datagrid('endEdit',editIndex);
				}
				if(editIndex==undefined){
					//alert("快速添加电子单...");
					$("#grid").datagrid('insertRow',{
						index : 0,
						row : {}
					});
					$("#grid").datagrid('beginEdit',0);
					editIndex = 0;
				}
			}
			
			function doSave(){
				$("#grid").datagrid('endEdit',editIndex );
			}
			
			function doCancel(){
				if(editIndex!=undefined){
					$("#grid").datagrid('cancelEdit',editIndex);
					if($('#grid').datagrid('getRows')[editIndex].id == undefined){
						$("#grid").datagrid('deleteRow',editIndex);
					}
					editIndex = undefined;
				}
			}
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',	
				text : '新增一行',
				iconCls : 'icon-edit',
				handler : doAdd
			}, {
				id : 'button-cancel',
				text : '取消编辑',
				iconCls : 'icon-cancel',
				handler : doCancel
			}, {
				id : 'button-save',
				text : '保存',
				iconCls : 'icon-save',
				handler : doSave
			}];
			// 定义列
			var columns = [ [ {
				field : 'wayBillNum',
				title : '工作单号',
				width : 120,
				align : 'center',
				editor :{
					type : 'validatebox',
					options : {
						required: true
					}
				}
			}, {
				field : 'arrivecity',
				title : '到达地',
				width : 120,
				align : 'center',
				editor :{
					type : 'validatebox',
					options : {
						required: true
					}
				}
			},{
				field : 'product',
				title : '产品',
				width : 120,
				align : 'center',
				editor :{
					type : 'validatebox',
					options : {
						required: true
					}
				}
			}, {
				field : 'num',
				title : '件数',
				width : 120,
				align : 'center',
				editor :{
					type : 'numberbox',
					options : {
						required: true
					}
				}
			}, {
				field : 'weight',
				title : '重量',
				width : 120,
				align : 'center',
				editor :{
					type : 'validatebox',
					options : {
						required: true
					}
				}
			}, {
				field : 'floadreqr',
				title : '配载要求',
				width : 220,
				align : 'center',
				editor :{
					type : 'validatebox',
					options : {
						required: true
					}
				}
			}] ];
			
			$(function(){
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 运单数据表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : true,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url :  "",
					idField : 'id',
					columns : columns,
					onDblClickRow : doDblClickRow,
					onAfterEdit : function(rowIndex, rowData, changes){
						console.info(rowData);
						editIndex = undefined;
						//发送ajax请求，将修改后的数据提交到服务器，更新数据库
						$.post("../../waybillAction_save.action",rowData,function(data){
							if(data=='0'){
								//录入失败，弹出提示
								$.messager.alert("提示信息","运单数据录入失败！！","error");
							}
						});
					}
				});
			});
		
			function doDblClickRow(rowIndex, rowData){
				alert("双击表格数据...");
				console.info(rowIndex);
				$('#grid').datagrid('beginEdit',rowIndex);
				editIndex = rowIndex;
			}
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
	</body>

</html> 