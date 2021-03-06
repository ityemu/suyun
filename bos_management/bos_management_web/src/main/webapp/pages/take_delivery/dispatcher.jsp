<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>人工调度</title>
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
			$(function() {
				$("#grid").datagrid({
					singleSelect : true,
					pageList: [20,50,100],
					pagination : true,
					toolbar : [ {
						id : 'dispatcher',
						text : '人工调度',						
						iconCls : 'icon-edit',
						handler : function() {
							// 弹出窗口
							$("#dispatcherWindow").window('open');
						}
					} ],
					columns : [ [ {
						field : 'id',
						title : '编号',
						width : 100
					},{
						field : 'orderNum',
						title : '订单号',
						width : 100
					}, {
						field : 'delegater',
						title : '联系人',
						width : 100
					}, {
						field : 'telephone',
						title : '电话',
						width : 100
					}, {
						field : 'sendAddress',
						title : '取件地址',
						width : 100
					}, {
						field : 'goodsType',
						title : '货物',
						width : 100
					}, {
						field : 'orderTime',
						title : '下单时间',
						width : 100,
						formatter : function(data, row, index) {
							return data.replace("T", " ");
						}
					} ] ],
					url : ''
				});
		
				// 点击保存按钮，为通知单 进行分单 --- 生成工单
				$("#save").click(function() {
		
				});
			});
		</script>
	</head>

	<body class="easyui-layout">
		<div data-options="region:'center',border:false">
			<table id="grid"></table>
		</div>
		<div class="easyui-window" title="人工调度" id="dispatcherWindow" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:100px;left:200px;width: 500px; height: 300px">
			<div region="north" style="height:30px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="dispatcherForm" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">人工调度</td>
						</tr>
						<tr>
							<td>订单编号</td>
							<td>
								<input type="hidden" name="id" id="orderId" /> <span id="orderIdView"></span>
						</tr>
						<tr>
							<td>选择快递员</td>
							<td>
								<input class="easyui-combobox" required="true" name="courier.id" data-options="valueField:'id',textField:'name',url:'../../courier_ajaxlist.action'" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>
</html>