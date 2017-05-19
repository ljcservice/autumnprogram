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
	margin-bottom: 2px;
	margin-left: 3px;
	margin-right: 3px;
}
</style>
</head>
<body class="no-skin" style="background-color:white;">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<div id="searchDiv" style="min-height: 35px;">
							<form action="report/exceedCommonOrderDoctor.do" method="post" name="searchForm" id="searchForm">
								<div class="check-search"  >
									医嘱日期：
									<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
									<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
									<font style="color: red;">*</font>
								</div>
								<div class="check-search nav-search"  >
									医生：
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="ATTENDING_DOCTOR" type="text" name="ATTENDING_DOCTOR" value="${pd.ATTENDING_DOCTOR}" placeholder="医生" maxlength="80"/>
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
								</div>
								<div class="check-search"  >
									<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
									<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
								</div>
								<div id="btnDiv" class="check-search">
										<a title="最大支持导出2万条" class="btn btn-mini btn-success" onclick="listExport();">导出</a>
										<a title="" class="btn btn-mini btn-success" onclick="myprint();">打印</a>
								</div>
							</form>
							</div>
						<!-- 检索  -->
						<div  >
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap>医生名称</th>
									<th class="center" nowrap>用法用量</th>
									<th class="center" nowrap>禁忌症</th>
									<th class="center" nowrap>重复给药</th>
									<th class="center" nowrap>相互作用</th>
									<th class="center" nowrap>配伍禁忌</th>
									<th class="center" nowrap>不良反应</th>
									<th class="center" nowrap>给药途径</th>
									<th class="center" nowrap>特殊人群</th>
									<th class="center" nowrap>管理</th>
									<th class="center" nowrap>不合格医嘱数</th>
									<th class="center" nowrap>医嘱总医嘱数</th>
									<th class="center" width="150px;">不合格医嘱数占总不合格医嘱百分比</th>
									<th class="center" width="150px;">不合格医嘱数占（查询日期内）医生总医嘱数</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="p1" varStatus="vs" >
								<tr  >
									<td nowrap class="center">${p1.ATTENDING_DOCTOR }</td>
									<td nowrap class="center">${p1.DOSAGE_SUM }</td>
									<td nowrap class="center">${p1.DIAGINFO_SUM }</td>
									<td nowrap class="center">${p1.INGREDIEN_SUM }</td>
									<td nowrap class="center">${p1.INTERACTION_SUM }</td>
									<td nowrap class="center">${p1.IV_EFFECT_SUM }</td>
									<td nowrap class="center">${p1.SIDE_SUM }</td>
									<td nowrap class="center">${p1.ADMINISTRATOR_SUM }</td>
									<td nowrap class="center">${p1.SPECPEOPLE_SUM }</td>
									<td nowrap class="center">${p1.MANAGER_SUM }</td>
									<td nowrap class="center">${p1.CHECKFALSE_SUM }</td>
									<td nowrap class="center">${p1.ORDER_COUNT }</td>
									<td nowrap class="center">${p1.CHECKFALSE_PERSENTS1 } %</td>
									<td nowrap class="center">${p1.CHECKFALSE_PERSENTS2 } %</td>
								</tr>
								</c:forEach>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
								<tr  >
									<td nowrap class="center">总计：</td>
									<td nowrap class="center">${all.DOSAGE_SUM }</td>
									<td nowrap class="center">${all.DIAGINFO_SUM }</td>
									<td nowrap class="center">${all.INGREDIEN_SUM }</td>
									<td nowrap class="center">${all.INTERACTION_SUM }</td>
									<td nowrap class="center">${all.IV_EFFECT_SUM }</td>
									<td nowrap class="center">${all.SIDE_SUM }</td>
									<td nowrap class="center">${all.ADMINISTRATOR_SUM }</td>
									<td nowrap class="center">${all.SPECPEOPLE_SUM }</td>
									<td nowrap class="center">${all.MANAGER_SUM }</td>
									<td nowrap class="center">${all.CHECKFALSE_SUM_ALL }</td>
									<td nowrap class="center">${all.ORDER_COUNT_ALL }</td>
									<td nowrap class="center">&nbsp;</td>
									<td nowrap class="center">&nbsp;</td>
								</tr>
							</tbody>
						</table>
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
<script type="text/javascript" src="static/js/common/lockTableBottom.js?v=212"></script>
<script type="text/javascript">
$(top.hangge());

//检索
function searchs(){
	if($("#beginDate").val()==""){
		$("#beginDate").tips({ side:3, msg:'请选择开始日期', bg:'#AE81FF',  time:1   });
		return;
	}
	if($("#endDate").val()==""){
		$("#endDate").tips({ side:3, msg:'请选择结束日期', bg:'#AE81FF',  time:1   });
		return;
	}
	top.jzts();
	$("#searchForm").submit();
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	//重置当前页面高度，自适应浏览器
	initWidthHeight();
	$(window).off('resize').on('resize', function() {
		initWidthHeight();
	}).trigger('resize');
});
//重置当前页面高度，自适应浏览器
function initWidthHeight(){
	var rr = new Array;
	rr[0]="searchDiv";
	FixTable("simple-table", 1, rr);
}
function listExport(){
	window.open(path + "/report/exceedCommonOrderDoctorExport.do?&"+$("#searchForm").serialize());
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