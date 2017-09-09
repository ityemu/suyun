<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>管理定区/调度排班</title>
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
			function doAdd(){
				$('#addWindow').window("open");
			}
			
			function doEdit(){
				
				var rows=$("#grid").datagrid("getSelections");
				
				if(rows.length!=1){
					$.messager.alert("提示信息","请选择一条信息进行修改","warning");
				}else{
					$("#addWindow").window("open");
					$("#fixedAreaForm").form("load",rows[0]);
				}
			}
			
			function doDelete(){
				alert("删除...");
			}
			
			function doSearch(){
				$('#searchWindow').window("open");
			}
			
			
			//以下是关联客户的导出    xls 和 pdf 格式
			function doExportXls(){
				window.location.href = "../../fixedAreaAction_exportXls.action";
			}
			
			function doExportPdf(){
				window.location.href = "../../fixedAreaAction_exportPDF.action";
			}
			
			function doExportXls2(){
				var rows = $("#grid").datagrid("getSelections");
				if(rows.length != 1){
					$.messager.alert("提示信息","选择一条且只能选择一条数据","warning")
				}else{
					window.location.href = "../../fixedAreaAction_exportXls2.action?id="+rows[0].id;
				}
			}
			
			function doExportPdf2(){
				var rows = $("#grid").datagrid("getSelections");
				if(rows.length != 1){
					$.messager.alert("提示信息","选择一条且只能选择一条数据","warning")
				}else{
					window.location.href = "../../fixedAreaAction_exportPDF2.action?id="+rows[0].id;
				}
			}
			
			
			
			
			function doAssociations(){
				//获得数据表格所有选中的行
				var rows = $("#grid").datagrid("getSelections");
				if(rows.length != 1){
					$.messager.alert("提示信息","请选择一个定区操作！","warning");
				}else{
					//弹出窗口
					$('#customerWindow').window('open');
					//清空下拉框中客户数据
					$("#noassociationSelect").empty();
					$("#associationSelect").empty();
					
					//发送ajax请求，请求后台系统的Action，在Action中调用CRM客户端代理对象获得客户数据，将客户数据转为json响应到页面
					$.post("../../fixedAreaAction_findCustomersNotAssociation.action",function(data){
						if(data.length > 0){
							for(var i=0;i<data.length;i++){
								var id = data[i].id;
								var telephone = data[i].telephone;
								var address = data[i].address;
								var username = data[i].username + "["+telephone+"]";
								var option = "<option title='"+address+"' value='"+id+"'>"+username+"</option>";
								//将客户信息追加到下拉框中
								$("#noassociationSelect").append(option);
							}
						}
					} );
					
					//发送ajax请求，请求后台系统的Action，在Action中调用CRM客户端代理对象获得客户数据，将客户数据转为json响应到页面
					$.post("../../fixedAreaAction_findCustomersHasAssociation.action",{id:rows[0].id},function(data){
						if(data.length > 0){
							for(var i=0;i<data.length;i++){
								var id = data[i].id;
								var telephone = data[i].telephone;
								var address = data[i].address;
								var username = data[i].username + "["+telephone+"]";
								var option = "<option title='"+address+"' value='"+id+"'>"+username+"</option>";
								//将客户信息追加到下拉框中
								$("#associationSelect").append(option);
							}
						}
					});
				}
			}
			
			
		
			
			function doAssociations(){

				//获得数据表格所有选中的行
				var rows = $("#grid").datagrid("getSelections");
				if(rows.length != 1){
					$.messager.alert("提示信息","请选择一个定区操作！","warning");
				}else{
					//弹出窗口
					$('#customerWindow').window('open');
					//清空下拉框中客户数据
					$("#noassociationSelect").empty();
					$("#associationSelect").empty();
					
					//发送ajax请求，请求后台系统的Action，在Action中调用CRM客户端代理对象获得客户数据，将客户数据转为json响应到页面
					$.post("../../fixedAreaAction_findCustomersNotAssociation.action",function(data){
						if(data.length > 0){
							for(var i=0;i<data.length;i++){
								var id = data[i].id;
								var telephone = data[i].telephone;
								var address = data[i].address;
								var username = data[i].username + "["+telephone+"]";
								var option = "<option title='"+address+"' value='"+id+"'>"+username+"</option>";
								//将客户信息追加到下拉框中
								$("#noassociationSelect").append(option);
							}
						}
					} );
					
					//发送ajax请求，请求后台系统的Action，在Action中调用CRM客户端代理对象获得客户数据，将客户数据转为json响应到页面
					$.post("../../fixedAreaAction_findCustomersHasAssociation.action",{id:rows[0].id},function(data){
						if(data.length > 0){
							for(var i=0;i<data.length;i++){
								var id = data[i].id;
								var telephone = data[i].telephone;
								var address = data[i].address;
								var username = data[i].username + "["+telephone+"]";
								var option = "<option title='"+address+"' value='"+id+"'>"+username+"</option>";
								//将客户信息追加到下拉框中
								$("#associationSelect").append(option);
							}
						}
					});
				}
			}
			
			
			
			//做分区关联
			function doSubAreaAssociation(){
				//获得数据表格所有选中的行
				var rows=$("#grid").datagrid("getSelections");
				
				if(rows.length!=1){
					$.messager.alert("提示信息","请选择一个定区操作！","warning");
				}else{
					//弹出窗口
				   $('#subAreaWindow').window('open');
					
					//清空下拉框中客户数据
					$("#noassociationSubAreaSelect").empty();
					$("#associationSubAreaSelect").empty();
					
					//发送ajax请求，请求后台系统的Action
					//在Action中调用客户端代理对象，获得客户数据
					//将客户数据转为json响应到页面
					
					var url1="${pageContext.request.contextPath}/fixedAreaAction_findSubAreaNotAssociation.action";
					$.post(url1,function(data){
						
						if(data.length>0){
							
							for(var i=0;i<data.length;i++){
								var id=data[i].id;
								var keyWords=data[i].keyWords;
								var assistKeyWords=data[i].assistKeyWords;
								var address=data[i].address;
								var subArea=keyWords+"["+assistKeyWords+"]";
							    var option="<option title='"+address+"' value='"+id+"'>"+subArea+"</option>";
							
							    //将客户信息追加到下拉框中
							    $("#noassociationSubAreaSelect").append(option);
							}
						}
						
					});
					
					
					//发送ajax请求，请求后台系统的Action
					//在Action中调用客户端代理对象，获得客户数据
					//将客户数据转为json响应到页面
					var url2="${pageContext.request.contextPath}/fixedAreaAction_findSubAreaHasAssociation.action";
					$.post(url2,{id:rows[0].id},function(data){
						
						if(data.length>0){
							
							for(var i=0; i<data.length;i++){
								
								var id=data[i].id;
								var keyWords=data[i].keyWords;
								var assistKeyWords=data[i].assistKeyWords;
								var address=data[i].address;
								var subArea=keyWords+"["+assistKeyWords+"]";
							    var option="<option title='"+address+"' value='"+id+"'>"+subArea+"</option>";
							
							    //将客户信息追加到下拉框中
							    $("#associationSubAreaSelect").append(option);
							}
						}
						
					});
					
					
				}
				
			}
			
			
			//工具栏
			var toolbar = [ {
				id : 'button-search',	
				text : '查询',
				iconCls : 'icon-search',
				handler : doSearch
			}, {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : doAdd
			}, {
				id : 'button-edit',	
				text : '修改',
				iconCls : 'icon-edit',
				handler : doEdit
			},{
				id : 'button-delete',
				text : '删除',
				iconCls : 'icon-cancel',
				handler : doDelete
			},{
				id : 'button-association',
				text : '关联客户',
				iconCls : 'icon-sum',
				handler : doAssociations
			},{
				id : 'button-association-courier',
				text : '关联快递员',
				iconCls : 'icon-sum',
				handler : function(){
					// 判断是否已经选中了一个定区，弹出关联快递员窗口 
					var rows = $("#grid").datagrid('getSelections');
					if(rows.length==1){
						// 只选择了一个定区
						// 弹出定区关联快递员 窗口 
						$("#courierWindow").window('open');
						//动态设置combobox的URL地址，控件会自动发送ajax请求获取快递员信息
						$("#courierId").combobox({
							url:'../../courierAction_listajax.action'
						});
						//动态设置combobox的URL地址，控件会自动发送ajax请求获取收派时间信息
						$("#takeTimeId").combobox({
							url:'../../taketimeAction_listajax.action'
						});
					}else{
						// 没有选中定区，或者选择 了多个定区
						$.messager.alert("警告","关联快递员,只能（必须）选择一个定区","warning");
					}
				}
			},{
				id : 'button-association2',
				text : '关联分区',
				iconCls : 'icon-sum',
				handler : doSubAreaAssociation
			},{		//手动添加导出客户按钮
				id : 'button-exportxls2',
				text : ' 关联客户导出xls',
				iconCls : 'icon-undo',
				handler : doExportXls2
			},{
				id : 'button-exportpdf2',
				text : '关联客户导出pdf',
				iconCls : 'icon-undo',
				handler : doExportPdf2
			},{
				id : 'button-exportxls',
				text : ' 全部客户导出xls',
				iconCls : 'icon-undo',
				handler : doExportXls
			},{
				id : 'button-exportpdf',
				text : '全部客户导出pdf',
				iconCls : 'icon-undo',
				handler : doExportPdf
			}];
			// 定义列
			var columns = [ [ {
				field : 'id',
				title : '编号',
				width : 80,
				align : 'center',
				checkbox:true
			},{
				field : 'fixedAreaName',
				title : '定区名称',
				width : 120,
				align : 'center'
			}, {
				field : 'fixedAreaLeader',
				title : '负责人',
				width : 120,
				align : 'center'
			}, {
				field : 'telephone',
				title : '联系电话',
				width : 120,
				align : 'center'
			}, {
				field : 'company',
				title : '所属公司',
				width : 120,
				align : 'center'
			} ] ];
			
			$(function(){
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 定区数据表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : true,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url : "../../fixedAreaAction_pageQuery.action",
					idField : 'id',
					columns : columns,
					onDblClickRow : doDblClickRow
				});
				
				// 添加、修改定区
				$('#addWindow').window({
			        title: '添加修改定区',
			        width: 600,
			        modal: true,
			        shadow: true,
			        closed: true,
			        height: 400,
			        resizable:false
			    });
				
				// 查询定区
				$('#searchWindow').window({
			        title: '查询定区',
			        width: 400,
			        modal: true,
			        shadow: true,
			        closed: true,
			        height: 400,
			        resizable:false
			    });
				
				//工具方法，作用是将指定表单中所有
		    	// 输入项转为json数据用于数据提交
				$.fn.serializeJson=function(){  
		            var serializeObj={};  
		            var array=this.serializeArray();
		            var str=this.serialize();  
		            $(array).each(function(){  
		                if(serializeObj[this.name]){  
		                    if($.isArray(serializeObj[this.name])){  
		                        serializeObj[this.name].push(this.value);  
		                    }else{  
		                        serializeObj[this.name]=[serializeObj[this.name],this.value];  
		                    }  
		                }else{  
		                    serializeObj[this.name]=this.value;   
		                }  
		            });  
		            return serializeObj;  
		        };
		        
				
				$("#btn").click(function(){
					
					var p=$("#searchForm").serializeJson();
		        	//调用datagird的load方法，发送一次ajax请求
		        	$("#grid").datagrid("load",p);
		        	//关闭查询窗口
		        	$("#searchWindow").window("close");
				});
				
			});
		
			function doDblClickRow(rowIndex,rowData){
				
				 /* 关联分区 */
				$('#association_subarea').datagrid( {
					fit : true,
					border : true,
					rownumbers : true,
					striped : true,
					url : "../../fixedAreaAction_findSubAreaHasAssociation.action?id="+rowData.id,
					columns : [ [{
						field : 'id',
						title : '分拣编号',
						width : 120,
						align : 'center'
					},{
						field : 'province',
						title : '省',
						width : 120,
						align : 'center',
						formatter : function(data,row ,index){
							if(row.area!=null){
								return row.area.province;
							}
							return "";
						}
					}, {
						field : 'city',
						title : '市',
						width : 120,
						align : 'center',
						formatter : function(data,row ,index){
							if(row.area!=null){
								return row.area.city;
							}
							return "";
						}
					}, {
						field : 'district',
						title : '区',
						width : 120,
						align : 'center',
						formatter : function(data,row ,index){
							if(row.area!=null){
								return row.area.district;
							}
							return "";
						}
					}, {
						field : 'keyWords',
						title : '关键字',
						width : 120,
						align : 'center'
					}, {
						field : 'startNum',
						title : '起始号',
						width : 100,
						align : 'center'
					}, {
						field : 'endNum',
						title : '终止号',
						width : 100,
						align : 'center'
					} , {
						field : 'single',
						title : '单双号',
						width : 100,
						align : 'center'
					} , {
						field : 'address',
						title : '位置',
						width : 200,
						align : 'center'
					} ] ]
				});
				 
				/*  关联客户*/
				$('#association_customer').datagrid( {
					fit : true,
					border : true,
					rownumbers : true,
					striped : true,
					url : "../../fixedAreaAction_findCustomersHasAssociation.action?id="+rowData.id,
					columns : [[{
						field : 'id',
						title : '客户编号',
						width : 120,
						align : 'center'
					},{
						field : 'username',
						title : '客户名称',
						width : 120,
						align : 'center'
					}, {
						field : 'company',
						title : '所属单位',
						width : 120,
						align : 'center'
					}]]
				});
				
				/*  关联快递员*/
				$('#association_courier').datagrid( {
					fit : true,
					border : true,
					rownumbers : true,
					striped : true,
					url : "../../fixedAreaAction_findCourierHasAssociation.action?id="+rowData.id,
					columns : [[{
						field : 'courierNum',
						title : '快递员工号',
						width : 120,
						align : 'center'
					},{
						field : 'name',
						title : '快递员',
						width : 120,
						align : 'center'
					},{
						field : 'telephone',
						title : '快递员电话',
						width : 120,
						align : 'center'
					}, {
						field : 'takeTime.name',
						title : '取件时间',
						width : 120,
						align : 'center',
						formatter : function(data,row,index){
							if(row.takeTime!=null){
								return row.takeTime.name;
							}
							return "";
						}
					},
					{
						field : 'company',
						title : '所属单位',
						width : 120,
						align : 'center'
					}]]
				});
				
				
				
			}
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
		<div region="south" border="false" style="height:150px">
			<div id="tabs" fit="true" class="easyui-tabs">
				<div title="关联分区" id="subArea" style="width:100%;height:100%;overflow:hidden">
					<table id="association_subarea"></table>
				</div>
				<div title="关联客户" id="customers" style="width:100%;height:100%;overflow:hidden">
					<table id="association_customer"></table>
				</div>
				<div title="关联快递员" id="courier" style="width:100%;height:100%;overflow:hidden">
					<table id="association_courier"></table>
				</div>
			</div>
		</div>

		<!-- 添加 修改定区 -->
		<div class="easyui-window" title="定区添加修改" id="addWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
			<div style="height:31px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
					<script type="text/javascript">
						$(function(){
							$("#save").click(function(){
								if($("#fixedAreaForm").form("validate")){
									$("#fixedAreaForm").submit();
								}
							});
						});
					</script>
					</script>
				</div>
			</div>

			<div style="overflow:auto;padding:5px;" border="false">
				<form id="fixedAreaForm" method="post" action="../../fixedAreaAction_save.action">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">定区信息</td>
						</tr>
						<tr>
							<td>定区编码</td>
							<td><input type="text" name="id" class="easyui-validatebox"
								required="true" /></td>
						</tr>
						<tr>
							<td>定区名称</td>
							<td><input type="text" name="fixedAreaName"
								class="easyui-validatebox" required="true" /></td>
						</tr>
						<tr>
							<td>负责人</td>
							<td><input type="text" name="fixedAreaLeader"
								class="easyui-validatebox" required="true" /></td>
						</tr>
						<tr>
							<td>联系电话</td>
							<td><input type="text" name="telephone"
								class="easyui-validatebox" required="true" /></td>
						</tr>
						<tr>
							<td>所属公司</td>
							<td><input type="text" name="company"
								class="easyui-validatebox" required="true" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		
		
		
		<!-- 查询定区 -->
		<div class="easyui-window" title="查询定区窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
			<div style="overflow:auto;padding:5px;" border="false">
				<form id="searchForm">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">查询条件</td>
						</tr>
						<tr>
							<td>定区编码</td>
							<td>
								<input type="text" name="id" class="easyui-validatebox"  />
							</td>
						</tr>
						<tr>
							<td>所属单位</td>
							<td>
								<input type="text" name="company" class="easyui-validatebox" />
							</td>
						</tr>
						<tr>
							<td>定区</td>
							<td>
								<input type="text" name="fixedAreaName" class="easyui-validatebox"  />
							</td>
						</tr>
						<tr>
							<td colspan="2"><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> </td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<!-- 关联客户窗口 -->
		<div modal="true" class="easyui-window" title="关联客户窗口" id="customerWindow" collapsible="false" closed="true" minimizable="false" maximizable="false" style="top:20px;left:200px;width: 400px;height: 300px;">
			<div style="overflow:auto;padding:5px;" border="false">
				<form id="customerForm" action="../../fixedAreaAction_assignCustomers2FixedArea.action" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="3">关联客户</td>
						</tr>
						<tr>
							<td>
								<input type="hidden" name="id" id="customerFixedAreaId" />
								<select id="noassociationSelect" multiple="multiple" size="10">
								</select>
							</td>
							<td>
								<input type="button" value="》》" id="toRight">
								<br/>
								<input type="button" value="《《" id="toLeft">
								<script type="text/javascript">
									$(function(){
										//为左右移动按钮绑定事件，实现客户数据左右移动效果
										$("#toRight").click(function(){
											//获得左侧下拉框中所有选中的option
											$("#associationSelect").append($("#noassociationSelect option:selected"));
										});
										
										$("#toLeft").click(function(){
											//获得左侧下拉框中所有选中的option
											$("#noassociationSelect").append($("#associationSelect option:selected"));
										});
										
										//为关联客户按钮绑定事件
										$("#associationBtn").click(function(){
											//提交表单之前需求为隐藏域赋值（定区选中的定区id）
											var rows = $("#grid").datagrid("getSelections");
											$("#customerFixedAreaId").val(rows[0].id);
											//提交表单之前，需要选中所有的option,就是为所有的option添加一个selected属性
											$("#associationSelect option").attr("selected","selected");
											$("#customerForm").submit();
										});
									});
								</script>
							</td>
							<td>
								<select id="associationSelect" name="customerIds" multiple="multiple" size="10"></select>
							</td>
						</tr>
						<tr>
							<td colspan="3"><a id="associationBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">关联客户</a> </td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		
		<!-- 关联快递员窗口 -->
		<div class="easyui-window" title="关联快递员窗口" id="courierWindow" modal="true"
			collapsible="false" closed="true" minimizable="false" maximizable="false" style="top:20px;left:200px;width: 700px;height: 300px;">
			<div style="overflow:auto;padding:5px;" border="false">
				<form id="courierForm" 
					action="../../fixedAreaAction_associationCourier2FixedArea.action" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">关联快递员</td>
						</tr>
						<tr>
							<td>选择快递员</td>
							<td>
								<!-- 存放定区编号 -->
								<input type="hidden" name="id" id="courierFixedAreaId" />
								<input id="courierId" data-options="editable:false,valueField:'id',textField:'courierInfo'"
									 type="text" name="courierId" class="easyui-combobox" required="true" />
							</td>
						</tr>
						<tr>
							<td>选择收派时间</td>
							<td>
								<input id="takeTimeId" data-options="editable:false,valueField:'id',textField:'name'" type="text" name="takeTimeId" class="easyui-combobox" required="true" />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<a id="associationCourierBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">关联快递员</a>
								<script type="text/javascript">
									$(function(){
										$("#associationCourierBtn").click(function(){
											if($("#courierForm").form("validate")){
												//提交表单之前需要为隐藏域赋值（当前选中的定区id）
												var rows = $("#grid").datagrid("getSelections");
												$("#courierFixedAreaId").val(rows[0].id);
												$("#courierForm").submit();
											}
										});
									});
								</script>
							 </td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		
		
		
		
			<!-- 关联分区窗口 -->
		<div modal="true" class="easyui-window" title="关联分区窗口" id="subAreaWindow" collapsible="false" closed="true" minimizable="false" maximizable="false" style="top:20px;left:200px;width: 400px;height: 300px;">
			<div style="overflow:auto;padding:5px;" border="false">
				<form id="subAreaForm" action="../../fixedAreaAction_associationSubArea2FixedArea.action" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="3">关联分区</td>
						</tr>
						<tr>
							<td>
								<input type="hidden" name="id" id="subAreaFixedAreaId" />
								<select id="noassociationSubAreaSelect" multiple="multiple" size="10"></select>
							</td>
							<td>
								<input type="button" value="》》" id="toRight2">
								<br/>
								<input type="button" value="《《" id="toLeft2">
							     <script type="text/javascript">
							       $(function(){
							    	   
							    	   //为左右移动按钮绑定事件，实现客户数据左右一定效果
							    	   $("#toRight2").click(function(){
							    		   //获得左侧下拉框中所选中的option
							    		   $("#associationSubAreaSelect").append($("#noassociationSubAreaSelect option:selected"));
							    		   
							    	   });
							    	   
							    	   $("#toLeft2").click(function(){
							    		   
							    		   //获得左侧下拉框中所有选中的option
							    		   $("#noassociationSubAreaSelect").append($("#associationSubAreaSelect option:selected"));
							    	   });
							    	   
							    	   
							    	   $("#associationSubAreaBtn").click(function(){
											  //提交表单之前需求为隐藏域赋值（定区选中定区id）
											  var rows=$("#grid").datagrid("getSelections");
											  
											  $("#subAreaFixedAreaId").val(rows[0].id);
											  //提交表单之前，需要选中所有的option，就是为所有option添加一个select属性
											  $("#associationSubAreaSelect option").attr("selected","selected");
											  $("#subAreaForm").submit();
										  });
							    	   
							       });
							      
							     </script>
							</td>
							<td>
								<select id="associationSubAreaSelect" name="subAreaIds" multiple="multiple" size="10"></select>
							</td>
						</tr>
						<tr>
							<td colspan="3"><a id="associationSubAreaBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">关联分区</a> 
							
							</td>
						     
						</tr>
					</table>
				</form>
			</div>
		</div>
		
		
	</body>

</html>