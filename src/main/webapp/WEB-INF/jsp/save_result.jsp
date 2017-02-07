<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>保存结果</title>
<base href="<%=basePath%>">
<!-- <meta name="description" content="overview & stats" /> -->
<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0" /> -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="static/ace/js/bootbox.js"></script>
</head>
<body style="background-color: white;">
	<input type="hidden" id="refreshFlag" value="${refreshFlag}"/>
	<div class="main-container" id="zhongxin">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class=" hr-18 dotted "></div>
					<div class="row">
						<div class="col-xs-12">
							<div class="error-container">
								<div class="well">
									<h1 class="grey lighter smaller" style="font-size: 22px;">
										<span class="blue bigger-125" id="message"   <i class="icon-sitemap"></i> 当前用户无此操作权限！</span>
									</h1>
									<div class="space"></div>
									<div style="text-align: center;">
										<a class="btn btn-mini btn-primary" style="font-size: 14px;" onclick="closeDiag();">确定 </a>
									</div>
								</div>
							</div>
		
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->


		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<script type="text/javascript">
	//$(top.hangge());
	$(function() {
		var msg = "${msg}";
		if(msg=="success" || msg=="" || msg=="ok"){
			document.getElementById('zhongxin').style.display = 'none';
			top.Dialog.close();
		}else if(msg=="failed" || msg=="no"){
			$("#message").html("操作失败！");
		}else{
			//弹出后台提示语
			$("#message").html(msg);
		}
	});
	function closeDiag(){
		top.Dialog.close();
	}
	</script>
</body>
</html>