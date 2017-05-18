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
						<div class="col-xs-12"  >
							<form action="drugAmount/drugAmountByDep.do" method="post" name="searchForm" id="searchForm">
								<div id="searchDiv"  style="vertical-align:bottom;float: left;padding-top: 4px;padding-bottom: 5px;width: 100%;">
									<div class="check-search" style="width: 250px;" >
										起止日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
									</div>
									<div class="check-search" style="width: 77px;">
										<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div class="check-search nav-search">
										药品名称：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="DRUG_NAME" value="${pd.DRUG_NAME}" placeholder="药品名称" maxlength="80"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										科室：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="DEPT_NAME" type="text" name="DEPT_NAME" value="${pd.DEPT_NAME}" placeholder="科室名称" maxlength="80" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search"  >
										发药药房：
									 	<select class="chosen-select form-control" name="DISPENSARY_NAME" id="DISPENSARY_NAME" data-placeholder="发药药房" style="vertical-align:top;width: 110px;" >
											<option value="" >全部</option>
											<option <c:if test="${'西药局' == pd.DISPENSARY_NAME}">selected</c:if> value="西药局" >西药局</option>
											<option <c:if test="${'中药局' == pd.DISPENSARY_NAME}">selected</c:if> value="中药局" >中药局</option>
										</select>
									</div>
									<div class="check-search"  >
										类型：
									 	<select class="chosen-select form-control" name="TOXI_PROPERTY" id="TOXI_PROPERTY" data-placeholder="类型" style="vertical-align:top;width: 110px;" >
											<option value="" >全部</option>
											<option <c:if test="${'普通药品' == pd.TOXI_PROPERTY}">selected</c:if> value="普通药品" >普通药品</option>
											<option <c:if test="${'精神一类' == pd.TOXI_PROPERTY}">selected</c:if> value="精神一类" >精神一类</option>
											<option <c:if test="${'精神二类' == pd.TOXI_PROPERTY}">selected</c:if> value="精神二类" >精神二类</option>
										</select>
									</div>
									<div class="check-search"  >
										排序：
									 	<select class="chosen-select form-control" name="sort_type" id="sort_type" data-placeholder="排序方式" style="vertical-align:top;width: 100px;">
									 		<option value=""></option>
											<option <c:if test="${'1' == pd.sort_type}">selected</c:if> value="1" >药名</option>
											<option <c:if test="${'2' == pd.sort_type}">selected</c:if> value="2" >厂家</option>
											<option <c:if test="${'3' == pd.sort_type}">selected</c:if> value="3" >金额 ↑</option>
											<option <c:if test="${'4' == pd.sort_type}">selected</c:if> value="4" >金额 ↓</option>
											<option <c:if test="${'5' == pd.sort_type}">selected</c:if> value="5" >科室</option>
										</select>
									</div>
								</div>
							</form>
						<!-- 检索  -->
						<div>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap>科室</th>
									<th class="center" nowrap>药名</th>
									<th class="center" nowrap>规格</th>
									<th class="center" nowrap>厂家</th>
									<th class="center" nowrap>金额</th>
									<th class="center" nowrap>使用量</th>
									<th class="center" nowrap>药品属性</th>
									<th class="center" nowrap>按类别分解</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty reportList}">
									<c:forEach items="${reportList}" var="report" varStatus="vs">
										<tr >
											<td class="center">${report.dept_name}</td>
											<td class="center">${report.drug_name}</td>
											<td class="center">${report.package_spec}</td>
											<td class="center">${report.firm_id}</td>
											<td class="center">￥ <fmt:formatNumber value="${report.costs}" pattern="###,###,##0.00"></fmt:formatNumber></td>
											<td class="center">${report.amounts} ${report.units}</td>
											<td class="center">${report.TOXI_PROPERTY}</td>
											<td class="center"> </td>
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
