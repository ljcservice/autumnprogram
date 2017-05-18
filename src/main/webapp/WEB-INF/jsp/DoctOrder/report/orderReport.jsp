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
@media Print { .Noprn { DISPLAY: none }}
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
								<div class="Noprn" style="margin-bottom: 5px;">	
								<form action="report/ordersReport.do" method="post" name="searchForm" id="searchForm">
									<div class="check-search"  >
										起止日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
										<font style="color: red;">*</font>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div class="check-search nav-search" style="width: 400px;">
										科室：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="OUT_DEPT_NAME" type="text" name="OUT_DEPT_NAME" value="${pd.OUT_DEPT_NAME}" placeholder="科室名称" maxlength="32" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										医生：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="DOCTOR_NAME" value="${pd.DOCTOR_NAME}" placeholder="医生 名称" maxlength="32"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
<!-- 										药品： -->
<!-- 										<span class="input-icon"> -->
<!-- 											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="DRUG_NAME" value="${pd.DRUG_NAME}" placeholder="诊断" maxlength="32"/> -->
<!-- 											<i class="ace-icon fa fa-search nav-search-icon"></i> -->
<!-- 										</span> -->
									</div>
									<div id="btnDiv" class="check-search">
											<a title="最大支持导出2万条" class="btn btn-mini btn-success" onclick="listExport();">导出</a>
											<a title="" class="btn btn-mini btn-success" onclick="window.print();">打印</a>
									</div>
								</form>
								</div>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap>问题</th>
									<th class="center" nowrap>问题数</th>
									<th class="center" nowrap>问题占比</th>
									<th class="center" nowrap>细分</th>
									<th class="center" nowrap>处方列表</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty reportList}">
									<c:forEach items="${reportList}" var="presc" varStatus="vs">
										<tr  >
											<td class="center">${presc.RS_TYPE_NAME}</td>
											<td class="center">${presc.count}</td>
											<td class="center">${presc.percent}</td>
											<td class="center">
												<a onclick="detailListByDep('${presc.RS_TYPE_CODE}');" href="javascript:void(0);">科室</a>
												<a onclick="detailListByDoctor('${presc.RS_TYPE_CODE}');" href="javascript:void(0);">医生</a>
											</td>
											<td class="center"><a onclick="detailList('${presc.RS_TYPE_CODE}');" href="javascript:void(0);">处方列表</a></td>
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

	

});
//
function detailList(RS_TYPE_CODE){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="问题医嘱列表";
	diag.URL = path + "/report/orderList.do?type=0&RS_DRUG_TYPE="+RS_TYPE_CODE+"&"+$("#searchForm").serialize();
	diag.Width =  window.screen.width;
	diag.Height =  window.screen.height;  
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}
function detailListByDoctor(RS_TYPE_CODE){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="医嘱问题统计列表(医生)";
	diag.URL = path + "/report/orderListByDoctor.do?type=0&RS_DRUG_TYPE="+RS_TYPE_CODE+"&"+$("#searchForm").serialize();
	diag.Width =  window.screen.width;
	diag.Height =  window.screen.height;  
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}
function detailListByDep(RS_TYPE_CODE){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="医嘱问题统计列表(科室)";
	diag.URL = path + "/report/orderListByDep.do?type=0&RS_DRUG_TYPE="+RS_TYPE_CODE+"&"+$("#searchForm").serialize();
	diag.Width =  window.screen.width;
	diag.Height =  window.screen.height;  
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}
function listExport(){
	window.open(path + "/report/ordersReportExport.do?&"+$("#searchForm").serialize());
}
</script>
</html>