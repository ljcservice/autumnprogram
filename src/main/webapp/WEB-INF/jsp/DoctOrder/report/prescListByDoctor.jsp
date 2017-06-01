<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
<style>
.ztree li a.curSelectedNode {
/* 	background-color: #ffb951;	 */
 }
 .check-search{
	float: left;
	margin-top: 2px;
	margin-bottom: 5px;
}
</style>
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
						<!-- 检索  -->
					<div style="margin-bottom: 5px;display: none;">	
								<form action="report/prescListByDoctor.do" method="post" name="searchForm" id="searchForm">
									<input type="hidden" name="RS_DRUG_TYPE" value="${pd.RS_DRUG_TYPE}">
									<div class="check-search"   >
										起止日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
									</div>
									<div class="check-search nav-search" >
										科室：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="ORG_NAME" type="text" name="ORG_NAME" value="${pd.ORG_NAME}" placeholder="科室名称" maxlength="32" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										医生：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="DOCTOR_NAME" value="${pd.DOCTOR_NAME}" placeholder="诊断名称" maxlength="32"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										药品：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="DRUG_NAME" value="${pd.DRUG_NAME}" placeholder="药品名称" maxlength="32"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search" style="width: 190px;" >
										问题类别：
									 	<select class="chosen-select form-control" name="RS_DRUG_TYPE" id="RS_DRUG_TYPE" data-placeholder="问题类别" style="vertical-align:top;width: 110px;" >
											<option value="" >全部</option>
											<c:forEach items="${checktypeMap.entrySet()}" var="map" varStatus="vs">
												<option <c:if test="${map.key == pd.RS_DRUG_TYPE}">selected</c:if> value="${map.key}" >${map.value.RS_TYPE_NAME}</option>
											</c:forEach>
										</select>
									</div>
								</form>
							</div>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap>医生</th>
									<th class="center" nowrap>问题类型</th>
									<th class="center" nowrap>问题数</th>
									<th class="center" nowrap>问题占比</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="order" varStatus="vs">
										<tr  >
											<td class="center">${order.DOCTOR_NAME}</td>
											<td class="center">${checktypeMap.get(pd.RS_DRUG_TYPE).RS_TYPE_NAME}</td>
											<td class="center">${order.count}</td>
											<td class="center">${order.percent}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="5" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						
							<div class="position-relative" id="osynPageParam" style="padding-bottom:4px;">
								<table style="width:100%;">
									<tr>
										<td style="text-align: center;" colspan="10">
											<a title="最大支持导出6万条" class="btn btn-mini btn-success" onclick="listExport();">导出</a>
											<a title="" class="btn btn-mini btn-success" onclick="myprint();">打印</a>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">关闭</a>
										</td>
									</tr>
								</table>
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
	<script type="text/javascript" src="static/js/common/common.js"></script>
	</body>

<script type="text/javascript">
$(top.hangge());

function listExport(){
	window.open(path + "/report/prescListByDoctorExport.do?&"+$("#searchForm").serialize());
}
function myprint(){
	$("#main-container").hide();
	var tableFixClone = $("#simple-table").clone(true);
	$("<div id='myprint' style='width=100%;height=100%;'></div>").appendTo($("body"));
	tableFixClone.appendTo($("#myprint"));
	$("#myprint").css("z-index",9999) .css("position","absolute").css("left",0).css("top",0).css("background-color","white");
	window.print();
	$("#myprint").remove();
	$("#main-container").show();
}
</script>
</html>