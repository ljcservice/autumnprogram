<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<base href="${basePath}">
<meta charset="utf-8" />
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>  
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
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
						<div class="col-xs-12" >
							<div id="searchDiv"  style="vertical-align:bottom;float: left;padding-top: 4px;padding-bottom: 5px;width: 100%;">
								<form name="searchForm" id="searchForm" action="InHospitalRep/DRANO009.do" method="post" >  
									<div class="check-search nav-search" >
										<span class="input-icon">
											<input class="nav-search-input" style="width: 100px;" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords}" placeholder="科室" maxlength="80"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										<span class="input-icon">
											<input class="nav-search-input" style="width: 100px;" autocomplete="off" id="OPERATION_DESC" type="text" name="OPERATION_DESC" value="${pd.OPERATION_DESC}" placeholder="手术名称" maxlength="80"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();" title="检索"  id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div class="check-search"  >
										起止日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
									</div>
									<div class="check-search"  > 
										选择查询切口：
									 	<select class="chosen-select form-control" name="WOUNDTYPE" id="WOUNDTYPE" data-placeholder="选择查询切口" style="vertical-align:top;width: 80px;">    
											<option value="0" >原切口</option>
											<option <c:if test="${pd.WOUNDTYPE == '1' }">selected</c:if> value="1" >现切口</option>
										</select>
									</div>
									
									<div class="check-search"  > 
										切口类型：
									 	<select class="chosen-select form-control" name="WOUND_GRADE" id="WOUND_GRADE" data-placeholder="切口类型" style="vertical-align:top;width: 80px;">
									 		<option value="">全部</option>
											<option <c:if test="${pd.WOUND_GRADE == 'Ⅰ' }">selected</c:if> value="Ⅰ" >Ⅰ</option>
											<option <c:if test="${pd.WOUND_GRADE == 'Ⅱ' }">selected</c:if> value="Ⅱ" >Ⅱ</option>
											<option <c:if test="${pd.WOUND_GRADE == 'Ⅲ' }">selected</c:if> value="Ⅲ" >Ⅲ</option>
										</select>
									</div>
  
									<div class="check-search">
										查询方式： 
										<select class="chosen-select form-control" name="findType" id="findType" data-placeholder="查询方式" style="vertical-align:top;width: 100px;">    
											<option  value="persion" >按人次查询</option>
											<option <c:if test="${pd.findType == 'count' }">selected</c:if> value="count" >按例数查询</option>
										</select>
									</div>
									<div class="check-search">
										<a title="最大支持导出6万条" class="btn btn-mini btn-success" onclick="listExport();">导出</a>
										<a title="" class="btn btn-mini btn-success" onclick="myprint();">打印</a>
									</div>
									</form>
							</div>
							<div style="width: 100%;height: auto;">
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap style="width:45px;">序号</th>
									<th class="center" nowrap>医生</th>
									<th class="center" nowrap>科室</th>
									<th class="center" nowrap>手术总例数</th>  
									<th class="center" nowrap>抗菌药使用率</th>
									<th class="center" nowrap>时机合理率</th>
									<th class="center" nowrap>疗程合理率</th>
									<th class="center" nowrap>品种合理率</th>
									<th class="center" nowrap>一联使用率</th>
									<th class="center" nowrap>二联使用率</th>
									<th class="center" nowrap>三联使用率</th>
									<th class="center" nowrap>多联使用率</th>
									<th class="center" nowrap>分解</th>
								</tr>
							</thead>
							<tbody>
							
							<!-- 开始循环 -->	 
							<c:choose>
								<c:when test="${not empty opers}">
									<c:forEach items="${opers}" var="oper" varStatus="vs" >
										<tr ondblclick="">
										<td nowrap class='center' style="width: 30px;">${vs.index+1}</td>
											<td nowrap class="center">${oper.operator } </td>
											<td nowrap class="center">${oper.dept_name } </td>
											<td nowrap class="center">${oper.patsum}</td>
											<td nowrap class="center">
												${oper.hasantisum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.ranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>
											<td nowrap class="center">  
												${oper.timingsum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.timingranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>  
											<td nowrap class="center">
												${oper.treatsum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.treatranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>
											<td nowrap class="center">
												${oper.pzsum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.pzranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>
											<td nowrap class="center">
												${oper.lh1sum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.lh1ranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>
											<td nowrap class="center">
												${oper.lh2sum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.lh2ranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>
											<td nowrap class="center">
												${oper.lh3sum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.lh3ranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>
											<td nowrap class="center">
												${oper.lh4sum}/${oper.patsum}=
												<fmt:formatNumber value="${oper.lh4ranking * 100}" type="number" maxFractionDigits="2"></fmt:formatNumber>%
											</td>
											<td nowrap class="center"></td> 
										</tr>
									</c:forEach>  
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="8" class="center">没有相关数据</td>
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
					</div>
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
	<script src="static/ace/js/chosen.jquery.js?v=2008001"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/common/common.js"></script>
<script type="text/javascript" src="static/js/common/lockTable.js?v=20161"></script>
<script type="text/javascript">
$(top.hangge());
$(function() {
	$('[data-rel=tooltip]').tooltip();
	$('[data-rel=popover]').popover({html:true});
	
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
	FixTable("simple-table", 0, rr);
}
// 查询
function searchs(){
	top.jzts();
	$("#searchForm").submit();
	
}

function viewDetail(patId , visitId,ngnum){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="医嘱点评";
	diag.URL = "<%=path%>/DoctOrder/OrderWorkDetailUI.do?patient_id=" + patId + "&visit_Id=" + visitId + "&ngroupnum=" + ngnum;    
	diag.Width = window.screen.width;
	diag.Height = window.screen.height;  
	diag.CancelEvent = function(){ //关闭事件
		
		nextPage(${page.currentPage});
		/**
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}*/
		diag.close();
	 };
	 diag.show();
	
}
function listExport(){
	window.open(path + "/InHospitalRep/DRANO009Export.do?"+$("#searchForm").serialize());
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