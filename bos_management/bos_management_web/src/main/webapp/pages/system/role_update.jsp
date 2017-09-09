<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>角色添加</title>
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
				
				// 授权树初始化
				var setting = {
					data : {
						key : {
							title : "t"
						},
						simpleData : {
							enable : true
						}
					},
					check : {
						enable : true
					}
					
				};
				
				var roleID =${param.roleId};
				
				//查询指定角色的菜单
					var MenusId = (function() {
					    var result;
					    $.ajax({
					        type:'POST',
					        url:'../../menuAction_findByRoleId.action',
					        dataType:'json',
					        data:{roleID:roleID},
					        async:false,
					        success:function(data){
					            result = data;
					        }
					    });
					    return result;
					})();
				
				
				
				$.ajax({
					url : '../../menuAction_listajax.action',
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						$.fn.zTree.init($("#menuTree"), setting, data);
						
						var zTreeObj = $.fn.zTree.getZTreeObj("menuTree") 
						
						 for (var i = 0; i < MenusId.length; i++) {
							var menuid = MenusId[i].id;
							var nodes = zTreeObj.getNodesByParam("id", menuid)[0]
							zTreeObj.checkNode(nodes,true);
							
						} 
					},
					error : function(msg) {
						alert('树加载异常!');
					}
				});
				
				
				
				
				//回显表单
				//$("#roleForm").form('load',data)
				$("#roleForm").form('load',"../../roleAction_findOne.action?id="+roleID)
				
				
				//查询所有权限
				//页面加载完成后，发送ajax请求获取所有的权限数据，使用复选框形式展示到页面中
					$.post("../../permissionAction_listajax.action",function(data){
						$("#permissionID").empty();
						for(var i=0;i<data.length;i++){
							var id = data[i].id;
							var name = data[i].name;
							//var input = '<input type="checkbox" name="permissionIds" value="'+id+'" /> ' + name;
							var input =  '<input  type="checkbox" name="permissionIds" value="'+id+'"  id="check'+id+'" > '+name+'</input>';
							$("#permissionID").append(input);
							
							$.post("../../permissionAction_findByRoleId.action",{roleID:roleID},function(data){
								for (var i = 0; i < data.length; i++) {
									var id = data[i].id;
									// alert(data[i].id) 
		/* 							 console.log(data[i].id)
									 console.log($("#check"+id))
									 console.log(document.getElementById("check"+id).checked) */
									 document.getElementById("check"+id).checked=true
									//$("#check"+id).prop("checked",true);
								}
							},"json");
							
							
						}
					},"json");
					
					
				
						
				/* //查询所有权限
				 $.post("../../permissionAction_listajax.action",null,function(data){
					$("#checkboxId").empty();
					var array = new Array();
					for (var i = 0; i < data.length; i++) {
						var id = data[i].id;
						var name = data[i].name;
						var temp ='<input type="checkbox" name="permissionIds" value="'+id+'" id="'+id+'"/><label for="'+id+'">'+name+'</label>';
						$("#checkboxId").append(temp);
					}
				});  */
				
				//查询指定角色的权限
				/*  $(function(){
					$.post("../../permissionAction_findByRoleId.action",{roleID:roleID},function(data){
						for (var i = 0; i < data.length; i++) {
							var id = data[i].id;
							// alert(data[i].id) 
							// console.log(data[i].id)
							// console.log($("#check"+id))
							// console.log(document.getElementById("check"+id).checked) 
							 document.getElementById("check"+id).checked=true
							//$("#check"+id).prop("checked",true);
						}
					},"json");
				})  */
				
				
			 $("#save").click(function(){
				var treeObj = $.fn.zTree.getZTreeObj("menuTree");
				var nodes = treeObj.getCheckedNodes(true);
				
				var list = new Array();
				for (var i = 0; i < nodes.length; i++) {
					
					list.push(nodes[i].id)
				}
				var menuId= list.join(",");
				$("#menuId").val(menuId)
				$("#roelId").val(roleID)
				$("#roleForm").submit();
			})	 
			
			});
		</script>
	</head>

	<body class="easyui-layout">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save"  class="easyui-linkbutton" plain="true">保存</a>
			</div>
		</div>
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="roleForm" method="post" action="../../roleAction_save.action">
				<!-- <input type="hidden" name="menuIds">
				<input type="hidden" name="id" id="roelId" > -->
				
				<!-- <input type="hidden" name="menuIds" id="menuId" > -->
				
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">角色信息
							 <input type="hidden" name="id" id="roelId" >
							<input type="hidden" name="menuIds" id="menuId" > 
						</td>
					</tr>
					<tr>
						<td>名称</td>
						<td>
							<input type="text" name="name" class="easyui-validatebox" data-options="required:true" />
						</td>
					</tr>
					<tr>
						<td>关键字</td>
						<td>
							<input type="text" name="keyword" class="easyui-validatebox" data-options="required:true" />
						</td>
					</tr>
					<tr>
						<td>描述</td>
						<td>
							<textarea name="description" rows="4" cols="60"></textarea>
						</td>
					</tr>
					<tr>
						<td>权限选择</td>
						<td id="permissionID">
							
						</td>
					</tr>
					<tr>
						<td>菜单授权</td>
						<td>
							<ul id="menuTree" class="ztree">
							</ul>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>

</html>