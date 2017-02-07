<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="clob" uri="/clob" %>
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
						<form action="sys/${msg }.do" name="RecycleForm" id="RecycleForm" method="post">
							<input type="hidden" name="LOG_ID" id="log_id" value="${pd.LOG_ID }"/>
							<div id="zhongxin" style="padding-top: 13px;">
							<table id="table_report" class="table table-striped table-bordered table-hover">
								<tr>
									<td class="center" style="width:79px;text-align: right;padding-top: 13px;">用户ID:</td>
									<td style="width:50px;text-align: left;padding-top: 13px;">${pd.USER_ID }</td>
									<td class="center" style="width:79px;text-align: right;padding-top: 13px;">用户名称:</td>
									<td style="text-align: left;padding-top: 13px;">${pd.USERNAME}</td>
								</tr>
								<tr>
									<td class="center" style="width:79px;text-align: right;padding-top: 13px;">设备编号:</td>
									<td style="text-align: left;padding-top: 13px;">${pd.CODE}</td>
									<td class="center" style="width:79px;text-align: right;padding-top: 13px;">API类型:</td>
									<td style="text-align: left;padding-top: 13px;">${pd.NAME }</td>
								</tr>
								<tr>
									<td  class="center" style="width:79px;text-align: right;padding-top: 13px;">登录时间:</td>
									<td style="text-align: left;padding-top: 13px;">${pd.CALL_DATE }</td>
									<td class="center" style="width:79px;text-align: right;padding-top: 13px;">登录IP:</td>
									<td style="text-align: left;padding-top: 13px;">${pd.USER_IP}</td>
								</tr>
								<tr>
									<td class="center" style="width:79px;text-align: right;padding-top: 13px;">输入:</td>
									<td colspan="3">
										<textarea  style="overflow-y:auto; resize:none; width: 478px; height: 72px; max-width: 478px; max-height: 72px;"><clob:colb addContent="" n="" clob="${pd.INPUT}"/></textarea>
									</td>
								</tr>
								<tr>
									<td class="center" style="width:79px;text-align: right;padding-top: 13px;">输出:</td>
									<td colspan="3">
									<textarea  style="overflow-y:auto; resize:none; width: 478px; height: 72px; max-width: 478px; max-height: 72px;" ><clob:colb addContent="" n="" clob="${pd.OUTPUT}"/></textarea>
									</td>
								</tr>
								<tr>
									<td class="center" colspan="6">
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">返回</a>
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
		if($("#user_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}
	});
	
</script>
</html>