<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>"
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
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
						<form action="show/list.do" method="post" id="myform" name="myform">
							<input type="hidden" name="id" value="${pd.id}" id="id"/>
							<input type="hidden" name="name" value="${pd.name}" id="name"/>
							<input type="hidden" name="ONTO_TYPE" value="${pd.ONTO_TYPE}" id="ONTO_TYPE"/>
						</form>
						<div id="resultDiv" style="overflow: auto;">
							<div>
								${pd.name}
							</div>
							<div style="padding: 5px;">
								<h4 style="color: blue;">检验项目</h4>
								<c:forEach items="${list1}" var="item" varStatus="vs">
									<div>
										<div>[检查名称]：${item.CHECK_ITEM }
										</div>
										<div>[检查时间]：${item.CHECK_TIME }
										</div>
									</div>
								</c:forEach>
								<div style="width:100%;height:2px;border-bottom: 1px dotted #e2e2e2;"></div>
							</div>
							
							<div style="padding: 5px;">
								<h4 style="color: blue;">血液浓度采集时期</h4>
								<c:forEach items="${list2}" var="item" varStatus="vs">
									<div>
										<div>[采集时间]：${item.COLLECT_TIME }
										</div>
										<div>[周期性检测]：${item.COLLECT_DESC }
										</div>
									</div>
								</c:forEach>
								<div style="width:100%;height:2px;border-bottom: 1px dotted #e2e2e2;"></div>
							</div>
							
							<div style="padding: 5px;">
								<h4 style="color: blue;">血液浓度范围</h4>
								<div>
								<table style="width: 100%;">
									<c:forEach items="${list3}" var="item" varStatus="vs">
										<tr>
											<td align="left">${item.RANGE }</td>
											<td width="200px;" align="center">${item.CROWD }</td>
										</tr>
									</c:forEach>
								</table>
								</div>
								<div style="width:100%;height:2px;border-bottom: 1px dotted #e2e2e2;"></div>
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

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!--本体查询列表js-->
	</body>
<script>
$(function() {
	//窗口变化，重新初始化 
	$(window).off('resize').on('resize', function() {
		initWidthHeight();
	}).trigger('resize');
	
});
function initWidthHeight(){
	var myHeight = $(window).outerHeight() ;
	$("#resultDiv").css('max-height', myHeight-2+'px');
}
</script>
</html>