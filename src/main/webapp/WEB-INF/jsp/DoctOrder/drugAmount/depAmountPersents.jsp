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
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12"  >
							<form action="drugAmount/depAmountPersents.do" method="post" name="searchForm" id="searchForm">
								<div id="searchDiv"  style="vertical-align:bottom;float: left;padding-top: 4px;padding-bottom: 5px;width: 100%;">
									<div class="check-search"   >
										起止日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
										<font style="color: red;">*</font>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div class="check-search nav-search">
										科室：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="DEPT_NAME" type="text" name="DEPT_NAME" value="${pd.DEPT_NAME}" placeholder="科室名称" maxlength="80" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search"  >
										排序：
									 	<select class="chosen-select form-control" name="sort_type" id="sort_type" data-placeholder=" " style="vertical-align:top;width: 110px;" >
											<option value=""></option>
											<option <c:if test="${'1' == pd.sort_type}">selected</c:if> value="1" >总费用 ↑</option>
											<option <c:if test="${'2' == pd.sort_type}">selected</c:if> value="2" >总费用 ↓</option>
											<option <c:if test="${'3' == pd.sort_type}">selected</c:if> value="3" >药费 ↑</option>
											<option <c:if test="${'4' == pd.sort_type}">selected</c:if> value="4" >药费 ↓</option>
											<option <c:if test="${'5' == pd.sort_type}">selected</c:if> value="5" >抗菌药费 ↑</option>
											<option <c:if test="${'6' == pd.sort_type}">selected</c:if> value="6" >抗菌药费 ↓</option>
										</select>
									</div>
									<div id="btnDiv" class="check-search">
											<a title="最大支持导出2万条" class="btn btn-mini btn-success" onclick="listExport();">导出</a>
											<a title="" class="btn btn-mini btn-success" onclick="myprint();">打印</a>
										</div>
								</div>
							</form>
						<!-- 检索  -->
						<div>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr style="border-bottom-width:1px;">
									<th class="center" nowrap rowspan="2"style="background-color: #f1f1f1;">科室</th>
									<th class="center" nowrap rowspan="2"style="background-color: #f1f1f1;">总费用</th>
									<th class="center" nowrap colspan="2">药费</th>
									<th class="center" nowrap colspan="2">抗菌药费</th>
								</tr>
								<tr>
									<th class="center" nowrap >费用</th>
									<th class="center" nowrap >比例</th>
									<th class="center" nowrap>费用</th>
									<th class="center" nowrap>比例</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty reportList}">
									<c:forEach items="${reportList}" var="report" varStatus="vs">
										<tr >
											<td class="center">${report.dept_name}</td>
											<td class="center">￥ ${report.med}</td>
											<td class="center">￥ ${report.drug} </td>
											<td class="center">${report.drug_persents} %</td>
											<td class="center">￥ ${report.anti}</td>
											<td class="center">${report.anti_persents} %</td>
										</tr>
									</c:forEach>
									<tr >
										<td class="center">合计：</td>
										<td class="center">￥ ${count.med_all}</td>
										<td class="center">￥ ${count.drug_all} </td>
										<td class="center">${count.drug_all_persents} %</td>
										<td class="center">￥ ${count.anti_all}</td>
										<td class="center">${count.anti_all_persents} %</td>
									</tr>
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
<script type="text/javascript" src="static/js/common/lockTableBottom.js?v=201612"></script>
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

function listExport(){
	window.open(path + "/drugAmount/depAmountPersentsExport.do?&"+$("#searchForm").serialize());
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
