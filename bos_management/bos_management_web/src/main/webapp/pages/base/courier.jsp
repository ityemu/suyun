<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>管理取派员</title>
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
				$('#addWindow').window("open");
				var rows = $("#grid").datagrid("getSelections");
				$("#standards").combobox("select",rows[0].standard.id);
				$("#courierForm").form("load",rows[0]);
			}
			
			function doSearch(){
				//弹出查询窗口
				$("#searchWindow").window("open");
			}
			
			//快递员批量删除
			function doDelete(){
				//获得所有选中的行
				var rows = $("#grid").datagrid("getSelections");//[{id:001},{id:002,name:'xxx'}]
				if(rows.length == 0){
					//没有选择，弹出提示
					$.messager.alert("提示信息","请选择需要删除的快递员！","warning");
				}else{
					//选中了，弹出确认框
					$.messager.confirm("确认信息","你确定要删除当前选中的快递员吗？",function(r){
						if(r){
							var array = new Array();
							//确定，需要删除，首先需要将选中快递员id获得，拼接为字符串1,2,3,4
							for(var i=0;i<rows.length;i++){
								var id = rows[i].id;
								array.push(id);
							}
							var ids = array.join(",");//将数组对象中所有的元素进行字符串拼接1,2,3,4
							//发送请求，提交IDS参数
							window.location.href = "../../courierAction_deleteBatch.action?ids=" + ids;
						}
					});
				}
			}
			
			function doRestore(){
				//获得所有选中的行
				var rows = $("#grid").datagrid("getSelections");//[{id:001},{id:002,name:'xxx'}]
				if(rows.length == 0){
					//没有选择，弹出提示
					$.messager.alert("提示信息","请选择需要还原快递员！","warning");
				}else{
					//选中了，弹出确认框
					$.messager.confirm("确认信息","你确定要还原当前选中的快递员吗？",function(r){
						if(r){
							var array = new Array();
							//确定，需要还原，首先需要将选中快递员id获得，拼接为字符串1,2,3,4
							for(var i=0;i<rows.length;i++){
								var id = rows[i].id;
								array.push(id);
							}
							var ids = array.join(",");//将数组对象中所有的元素进行字符串拼接1,2,3,4
							//发送请求，提交IDS参数
							window.location.href = "../../courierAction_reStoreBatch.action?ids=" + ids;
						}
					});
				}
			}
			//工具栏
			var toolbar = [ {
				id : 'button-add',	
				text : '增加',
				iconCls : 'icon-add',
				handler : doAdd
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : doEdit
			},
			
			<shiro:hasPermission name="courier-delete">
			{
				id : 'button-delete',
				text : '作废',
				iconCls : 'icon-cancel',
				handler : doDelete
			},
			</shiro:hasPermission>
			<shiro:hasPermission name="courier-restore">
			{
				id : 'button-restore',
				text : '还原',
				iconCls : 'icon-save',
				handler : doRestore
			},
			</shiro:hasPermission>
			{
				id : 'button-search',
				text : '查询',
				iconCls : 'icon-search',
				handler : doSearch
			}];
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true,
			},{
				field : 'courierNum',
				title : '工号',
				width : 80,
				align : 'center'
			},{
				field : 'name',
				title : '姓名',
				width : 80,
				align : 'center'
			}, {
				field : 'telephone',
				title : '手机号',
				width : 120,
				align : 'center'
			}, {
				field : 'checkPwd',
				title : '查台密码',
				width : 120,
				align : 'center'
			}, {
				field : 'pda',
				title : 'PDA号',
				width : 120,
				align : 'center'
			}, {
				field : 'standard.name',
				title : '取派标准',
				width : 120,
				align : 'center',
				formatter : function(data,row, index){
					if(row.standard != null){
						return row.standard.name;
					}
					return "";
				}
			}, {
				field : 'type',
				title : '取派员类型',
				width : 120,
				align : 'center'
			}, {
				field : 'company',
				title : '所属单位',
				width : 200,
				align : 'center'
			}, {
				field : 'deltag',
				title : '是否作废',
				width : 80,
				align : 'center',
				formatter : function(data,row, index){
					if(data=="1"){
						return "已作废"
					}else{
						return "正常使用";
					}
				}
			}, {
				field : 'vehicleType',
				title : '车型',
				width : 100,
				align : 'center'
			}, {
				field : 'vehicleNum',
				title : '车牌号',
				width : 120,
				align : 'center'
			} ] ];
			
			$(function(){
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 取派员信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [3,6,9],
					pagination : true,
					toolbar : toolbar,
					url : "../../courierAction_pageQuery.action",
					idField : 'id',
					columns : columns,
					onDblClickRow : doDblClickRow
				});
				
				// 添加取派员窗口
				$('#addWindow').window({
			        title: '添加取派员',
			        width: 800,
			        modal: true,
			        shadow: true,
			        closed: true,
			        height: 400,
			        resizable:false
			    });
				
			});
		
			function doDblClickRow(){
				alert("双击表格数据...");
			}
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
		<div class="easyui-window" title="对收派员进行添加或者修改" id="addWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
			<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
					<script type="text/javascript">
						$(function(){
							$("#save").click(function(){
								//进行表单校验
								if($("#courierForm").form("validate")){
									$("#courierForm").submit();
								}
							});
						});
					</script>
				</div>
			</div>

			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="courierForm" action="../../courierAction_save.action" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="4">收派员信息</td>
						</tr>
						<tr>
							<td>快递员工号</td>
							<td>
								<input type="text" name="courierNum" class="easyui-validatebox" required="true" />
							</td>
							<td>姓名</td>
							<td>
								<input type="text" name="name" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>手机</td>
							<td>
								<input type="text" name="telephone" class="easyui-validatebox" required="true" />
							</td>
							<td>所属单位</td>
							<td>
								<input type="text" name="company" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>查台密码</td>
							<td>
								<input type="text" name="checkPwd" class="easyui-validatebox" required="true" />
							</td>
							<td>PDA号码</td>
							<td>
								<input type="text" name="pda" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>快递员类型</td>
							<td>
								<input type="text" name="type" class="easyui-validatebox" required="true" />
							</td>
							<td>取派标准</td>
							<td>
								<input id="standards" type="text" name="standard.id" 
										class="easyui-combobox" 
										data-options="required:true,valueField:'id',textField:'name',
											url:'../../standardAction_findAll.action'"/>
							</td>
						</tr>
						<tr>
							<td>车型</td>
							<td>
								<input type="text" name="vehicleType" class="easyui-validatebox" required="true" />
							</td>
							<td>车牌号</td>
							<td>
								<input type="text" name="vehicleNum" class="easyui-validatebox" required="true" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		
		<!-- 查询快递员-->
		<div class="easyui-window" title="查询快递员窗口" modal="true" closed="true" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="width: 400px; top:40px;left:200px">
			<div style="overflow:auto;padding:5px;" border="false">
				<form id="searchForm">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">查询条件</td>
						</tr>
						<tr>
							<td>工号</td>
							<td>
								<input type="text" id="courierNum" name="courierNum" />
							</td>
						</tr>
						<tr>
							<td>收派标准</td>
							<td>
								<input type="text" name="standard.name" />
							</td>
						</tr>
						<tr>
							<td>所属单位</td>
							<td>
								<input type="text" name="company" />
							</td>
						</tr>
						<tr>
							<td>类型</td>
							<td>
								<input type="text" id="type" name="type" />
							</td>
						</tr>
						<tr>
							<td colspan="2"><a id="searchBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> 
							<script type="text/javascript">
								$(function(){
									//工具方法，作用是将指定表单中所有的输入项转为json数据，用于参数提交
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
									
									$("#searchBtn").click(function(){
										var p = $("#searchForm").serializeJson();//{key:value,key:value}
										//调用datagrid的load方法，重新发送一次ajax请求，而且可以提交参数
										$("#grid").datagrid("load",p);
										//关闭查询窗口
										$("#searchWindow").window("close");
									});
								});
							</script>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>

</html>