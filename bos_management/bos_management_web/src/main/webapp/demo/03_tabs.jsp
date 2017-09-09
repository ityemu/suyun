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
</head>
<body class="easyui-layout">
	<!-- 使用div表示其中的一个区域 -->
	<div title="速运快递项目" style="height: 100px" data-options="region:'north'">北部区域</div>
	<div title="系统菜单" style="width: 200px" data-options="region:'west'">
		<!-- 展示accordion折叠面板效果 -->
		<div class="easyui-accordion" data-options="fit:true">
			<!-- 每个子div表示其中的一个面板 -->
			<div data-options="iconCls:'icon-mini-add'" title="基本功能">
				<a id="mybutton" class="easyui-linkbutton">点我</a>
				<script type="text/javascript">
					$(function(){
						//页面加载完成后，为上面的按钮绑定事件
						$("#mybutton").click(function(){
							//判断是否存在
							var e = $("#mytabs").tabs("exists","这是标题");
							if(e){
								//已经存在了，选中
								$("#mytabs").tabs("select","这是标题");
							}else{
								//不存在
								//调用tabs对象的add方法动态在页面中添加一个选项卡面板
								$("#mytabs").tabs("add",{
									title:'这是标题',
									content:'<iframe frameborder="0" width="100%" height="100%" src="../index.jsp"></iframe>',
									closable:true,
									iconCls:'icon-mini-add'
								});
							}
						});
					});
				</script>
			</div>
			<div title="系统管理">
				可以
			</div>
		</div>
	</div>
	<div data-options="region:'center'">
		<!-- 展示tabs选项卡面板效果 -->
		<div id="mytabs" class="easyui-tabs" data-options="fit:true">
			<!-- 每个子div表示其中的一个面板 -->
			<div data-options="iconCls:'icon-mini-add',closable:true" title="基本功能">
				
			</div>
			<div title="系统管理">
				可以
			</div>
		</div>
	</div>
	<div style="width: 200px" data-options="region:'east'">东部区域</div>
	<div style="height: 50px" data-options="region:'south'">南部区域</div>
</body>
</html>