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
								<form name="searchForm" id="searchForm" action="InHospitalRep/DRNO006.do" method="post" > 
									<div class="check-search nav-search" >  
									
										<span class="input-icon">
											<input class="nav-search-input" style="width: 100px;" autocomplete="off" id="nav-search-input" type="text" name="drug_name" value="${pd.drug_name}" placeholder="药品名称" maxlength="80"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										<span class="input-icon">
											<input class="nav-search-input" style="width: 100px;" autocomplete="off" id="nav-search-input" type="text" name="dept_name" value="${pd.dept_name}" placeholder="科室名称" maxlength="80"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										<span class="input-icon">
											<input class="nav-search-input" style="width: 100px;" autocomplete="off" id="nav-search-input" type="text" name="doctor_name" value="${pd.doctor_name}" placeholder="医生名称" maxlength="80"/>
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
<!-- 									<div class="check-search"  >  -->
<!-- 										排名： -->
<!-- 									 	<select class="chosen-select form-control" name="ranking" id="ranking" data-placeholder="排名" style="vertical-align:top;width: 80px;"> -->
<!-- 									 		<option value="10">10</option> -->
<%-- 											<option <c:if test="${pd.ranking == '20' }">selected</c:if> value="20" >20</option> --%>
<%-- 											<option <c:if test="${pd.ranking == '999999' }">selected</c:if> value="999999" >全部</option> --%>
<!-- 										</select> -->
<!-- 									</div> -->
									
									<div class="check-search"  > 
										类型：
									 	<select class="chosen-select form-control" name="drugType" id="drugType" data-placeholder="类型" style="vertical-align:top;width: 80px;">    
									 		<option value="-1">全部</option>
											<option <c:if test="${pd.drugType == '0' }">selected</c:if> value="0" >抗菌药</option>
											<option <c:if test="${pd.drugType == '1' }">selected</c:if> value="1" >基本药物</option>
											<option <c:if test="${pd.drugType == '2' }">selected</c:if> value="2" >非抗菌药</option>
<%-- 											<option <c:if test="${pd.drugType == '3' }">selected</c:if> value="juvenile" >青年(大于10,小于18)</option> --%>
										</select>
									</div>  
									
									<div class="check-search">
										排序：
										<select class="chosen-select form-control" name="orderBy" id="orderBy" data-placeholder="抗菌药类型" style="vertical-align:top;width: 100px;">    
											<option  value="costs" >金额</option>
											<option <c:if test="${pd.orderBy == 'amount' }">selected</c:if> value="amount" >用量</option>
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
									<th class="center" nowrap>药品名称</th>
									<th class="center" nowrap>规格</th>
									<th class="center" nowrap>科室</th>
									<th class="center" nowrap>医生</th>
									<th class="center" nowrap>金额</th>
									<th class="center" nowrap>数量</th>
								</tr>
							</thead>
							<tbody>
								
							<!-- 开始循环 -->	 
							<c:choose>
								<c:when test="${not empty druginfos}">
									<c:forEach items="${druginfos}" var="druginfo" varStatus="vs" >
										<tr ondblclick="">
											<td nowrap class='center' style="width: 30px;">${vs.index+1}</td>
											<td nowrap class="center">${druginfo.drug_name }</td>
											<td nowrap class="center">${druginfo.drug_spec }</td>
											<td nowrap class="center">${druginfo.dept_name }</td>
											<td nowrap class="center">${druginfo.doctor_name }</td>
											<td nowrap class="center">
												￥<fmt:formatNumber value="${druginfo.costs }" type="number" maxFractionDigits="2"></fmt:formatNumber>
											</td>  
											<td nowrap class="center">
												${druginfo.amount }${druginfo.drug_units }
											</td>
										</tr>
									</c:forEach>  
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="7" class="center">没有相关数据</td>
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
	window.open(path + "/InHospitalRep/DRNO006Export.do?"+$("#searchForm").serialize());
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