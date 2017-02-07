<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- jsp文件头和头部 -->
	<%@ include file="../index/top.jsp"%>
	<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
	<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
	<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
	<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
	<style type="text/css">
	footer{height:50px;position:fixed;bottom:0px;left:0px;width:100%;text-align: center;}
	</style>
<body>

</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<div id="zhongxin">
								<div style="height:100%;width: 100%;">
								<ul id="funtree" class="ztree" style="overflow:auto;"></ul>
								</div>
							</div>
							<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">正在保存...</h4></div>
							</div>
						<!-- /.col -->
						</div>
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->
	
		<div style="width: 100%;padding-top: 5px;" class="center">
			<ts:rights code="role/saveMenuFun"><a class="btn btn-mini btn-primary" onclick="save();">保存</a></ts:rights>
			<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
		</div>
	
	<script type="text/javascript">
		$(top.hangge());
		var myzTree = null;
		$(document).ready(function(){
			var setting = {
			    check:{
			    	enable: true,
			    	chkboxType: {Y: "",N: ""}
			    }
			};
			var zn = '${zTreeNodes}';
			var zTreeNodes = eval(zn);
     		myzTree = $.fn.zTree.init($("#funtree"), setting, zTreeNodes);
		});
	
		//保存
		 function save(){
			var nodes = myzTree.getCheckedNodes();
			var tmpNode;
			var ids = "";
			for(var i=0; i<nodes.length; i++){
				tmpNode = nodes[i];
				if(tmpNode.target=='${functionFlag}'){//菜单为功能时侯，累计id
					if(i!=nodes.length-1){
						ids += tmpNode.id+",";
					}else{
						ids += tmpNode.id;
					}
				}
			}
			var ROLE_ID = "${ROLE_ID}";
			var url = "<%=basePath%>role/saveMenuFun.do";
			var postData;
			postData = {"ROLE_ID":ROLE_ID,"menuFunIds":ids};
			$("#zhongxin").hide();
			$("#zhongxin2").show();
			$.post(url,postData,function(data){
				top.Dialog.close();
			});
		 }
	
	</script>
</body>
</html>