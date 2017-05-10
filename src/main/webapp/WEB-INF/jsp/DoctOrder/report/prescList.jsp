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
	margin: 4px;
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
						<div>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap>处方号</th>
									<th class="center" nowrap>处方日期</th>
									<th class="center" nowrap>患者</th>
									<th class="center" nowrap>性别</th>
									<th class="center" nowrap>科室</th>
									<th class="center" nowrap>医生</th>
									<th class="center" nowrap>抗菌</th>
									<th class="center" nowrap>诊断</th>
									<th class="center" nowrap>药费</th>
									<th class="center" nowrap>点评</th>
									<th class="center" nowrap>是否合理</th>
									<th class="center" nowrap>结果</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty prescList}">
									<c:forEach items="${prescList}" var="presc" varStatus="vs">
										<tr >
											<td class="center">${presc.PRESC_NO}</td>
											<td class="center">${presc.ORDER_DATE}</td>
											<td class="center">${presc.PATIENT_NAME}</td>
											<td class="center">${presc.PATIENT_SEX}</td>
											<td class="center">${presc.ORG_NAME}</td>
											<td class="center">${presc.DOCTOR_NAME}</td>
											<td class="center"><c:if test="${presc.HASKJ==0}">否</c:if><c:if test="${presc.HASKJ==1}">是</c:if></td>
											<td class="center">${presc.DIAGNOSIS_NAMES}</td>
											<td class="center">${presc.AMOUNT}</td>
											<td class="center"><c:if test="${presc.ISORDERCHECK==0}">否</c:if><c:if test="${presc.ISORDERCHECK==1}">是</c:if></td>
											<td class="center">
												<c:choose>
													<c:when test="${presc.ISCHECKTRUE == 0 }"> 合理 </c:when>
													<c:when test="${presc.ISCHECKTRUE == 1 }"> 不合理</c:when>
													<c:otherwise>待定</c:otherwise>
												</c:choose>
											</td>
											<td class="center">
												<div class="hidden-sm hidden-xs btn-group">
													<c:forEach items="${presc.RS_DRUG_TYPES}" var="rs_type" varStatus="vs" >
														<a class="btn btn-xs ${rstypeColorMap.get(rs_type)}"  title="${checktypeMap.get(rs_type).RS_TYPE_NAME}" >
															${rstypeMap.get(rs_type)}
														</a>
													</c:forEach>
												</div>
											</td>
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="12" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</div>
						<div class= "pageStrDiv" id="pageStrDiv" style="padding-top: 5px;padding-bottom: 5px;">
							<table style="width:100%;">
								<tr>
									<td align="left"><a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">关闭</a></td>
									<td>
										<div class="pagination" style="float: right;padding: 0px;margin: 0px;">${page.pageStr}</div>
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
<script type="text/javascript" src="static/js/common/lockTable.js?v=201612"></script>
<script type="text/javascript">
$(top.hangge());

//检索
function searchs(){
	top.jzts();
	$("#searchForm").submit();
}

function resetForm(){
	document.getElementById("searchForm").reset();
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
			if(event_name != 'sidebar_collapsed') return;
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.parent().width()});
			});
		});
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}

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
	rr[1]="pageStrDiv";
	FixTable("simple-table", 1, rr);
}


</script>
</html>
