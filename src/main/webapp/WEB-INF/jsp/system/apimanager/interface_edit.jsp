<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
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
						<form action="sys/${msg }.do" name="interfaceForm" id="interfaceForm" method="post">
							<input type="hidden" name="IN_ID" id="in_id" value="${pd.IN_ID }"/>
							<div id="zhongxin" style="padding-top: 13px;">
							<table id="table_report" class="table table-striped table-bordered table-hover">
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">业务类别:</td>
									<td><input type="text" name="TYPE" id="type" value="${pd.TYPE }" maxlength="3" onblur="len()" placeholder="输入类别" title="用户类别" style="width:98%;" /></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">业务名称:</td>
									<td><input type="text" name="NAME" id="name"  value="${pd.NAME }" placeholder="输入业务名称" title="业务名称" style="width:98%;" /></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">业务CODE:</td>
									<td><input type="text" name="CODE" id="code" value="${pd.CODE }" maxlength="4" placeholder="输入编号" title="编号"style="width:98%;" /></td>
								</tr>
								<tr>
									<td class="center" colspan="6">
										<ts:rights code="sys/saveI">
											<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
										</ts:rights>
									
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
									</td>
								</tr>
							</table>
							</div>
							<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
						</form>
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>						
<script type="text/javascript">
	$(top.hangge());
	
	$(document).ready(function(){
		if($("#in_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}
	});
	function len(){
		var pattern = /(1,2,3,4,5)/,
		str = '';
		console.log(pattern.test(str));
	}
	//保存
	function save(){
					$("#interfaceForm").submit();
					$("#zhongxin").hide();
					$("#zhongxin2").show();
// 		var TYPE = $("#type").val();
// 		var NAME = $("#name").val();
// 		var CODE = $("#code").val();
// 		$.ajax({
// 			type: "POST",
// 			url: '<%=basePath%>sys/saveI.do',
// 	    	data: {TYPE:TYPE,NAME:NAME,CODE:CODE},
// 			dataType:'json',
// 			cache: false,
// 			success: function(data){
// 				 if("success" == data.result){
// 					$("#zhongxin").hide();
// 					$("#zhongxin2").show();
// 				 }else{
// 					$("#loginname").css("background-color","#D16E6C");
// 					setTimeout("$('#loginname').val('此用户名已存在!')",500);
// 				 }
// 			}
// 		});
	}
</script>
</html>