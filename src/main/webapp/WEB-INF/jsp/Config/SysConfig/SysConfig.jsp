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
.nav-search-input:hover{
	width: 105px;
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
						<form name="searchForm" id="searchForm" action="SysConfig/SysConfigViewUI.do" method="post" > 
<!-- 						<div class="col-xs-12" id="searchDiv"> -->
<!-- 							<input type="hidden" name="category_id" id="category_id" value=""/>    -->
<!-- 									<div class="check-search"  > -->
<!-- 										抽取时间： -->
<%-- 										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" /> --%>
<%-- 										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" /> --%>
<!-- 										抽取范围： -->
<!-- 									 	<select  name="fatchType" id="fatchType" data-placeholder="抽取范围" style="vertical-align:top;width: 80px;"> -->
<!-- 									 		<option value="-1">全部</option> -->
<%-- 											<option <c:if test="${pd.fatchType == '0' }">selected</c:if> value="0" >门诊</option> --%>
<%-- 											<option <c:if test="${pd.fatchType == '1' }">selected</c:if> value="1" >住院</option> --%>
<!-- 										</select> -->
<!-- 										执行类型 -->
<!-- 										<select  name="execType" id="execType" data-placeholder="执行类型" style="vertical-align:top;width: 90px;"> -->
<%-- 											<option <c:if test="${pd.execType == '0' }">selected</c:if> value="0" >执行抽取</option> --%>
<%-- 											<option <c:if test="${pd.execType == '1' }">selected</c:if> value="1" >执行审核</option> --%>
<%-- 											<option <c:if test="${pd.execType == '2' }">selected</c:if> value="2" >执行报表</option> --%>
<!-- 										</select> -->
<!-- 										<select name="reportCode" id="reportCode" data-placeholder="报表名称"  > -->
<!-- 											<option value="-1">全部报表</option> -->
<%-- 											<option <c:if test="${pd.reportCode == '0' }">selected</c:if> value="0" >报表名称</option> --%>
<!-- 										</select> -->
										
<!-- 									</div> -->
									
									<div class="check-search" >
									总审核客户端数为: ${counter} 访问机器， &nbsp;&nbsp;&nbsp;
										<a class="btn btn-sm btn-success" onclick="execute();">刷新</a>
										
										<a class="btn btn-sm btn-success" onclick="clean();">清空</a>
									</div>
<!-- 							</div> -->
							<div class="col-xs-12">
								<div style="width:100%;padding-bottom: 5px">
										
								</div>
							</div>
							
							<div style="width: 100%;height: auto;">
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty iCBMs}">
										<tr>
									<c:forEach items="${iCBMs}" var="entity" varStatus="vs" >
<%-- 											<td nowrap class='center' style="width: 30px;">${vs.index+1}</td> --%>
											<td nowrap> 
												<div style="height: 125px;width: 130px;">
													<div align="left">
														访问 IP:${entity.ip}</br>
														科室:${entity.dept}</br>
														医生:${entity.doctor }</br>
														医嘱数:${entity.orderCount }</br>
														审核耗时:${entity.checkTime }&nbsp;毫秒  </br>
														审核时间：<fmt:formatDate value="${entity.time }" pattern="yyyy-MM-dd HH:mm:ss"/></br>
														审查次数:${entity.useCount }</br>
													</div>	
												</div>
											</td>
										<c:if test="${ (vs.index+1)%4 ==0 }">
											</tr>
											<tr>
										</c:if>
									</c:forEach>
										</tr>
								</c:when>
								
								<c:otherwise>
									<tr class="main_info">
										<td colspan="11" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</div>
						<div class= "pageStrDiv" id="pageStrDiv" style="padding-top: 10px;padding-bottom: 10px;">
							<table style="width:100%;">
								<tr>
									<td>
										<div class="pagination" style="float: right;padding: 0px;margin: 0px;">${page.pageStr}</div>
									</td>
								</tr>
							</table>
						</div>
					</form>
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
	//initWidthHeight();
	//$(window).off('resize').on('resize', function() {
	//	initWidthHeight();
	//}).trigger('resize');
});
//重置当前页面高度，自适应浏览器
function initWidthHeight(){
	var height = $(window).height();
	var myheight =height-$("#searchDiv").outerHeight()-$("#pageStrDiv").outerHeight();
	//if($("#simple-table").innerHeight() >myheight || $("#simple-table").innerWidth() >$(window).width()){
		FixTable("simple-table", 0, $(window).width(), myheight-20);
	//}
}
// 执行ETL
function execute(){
	top.jzts();
	$("#searchForm").submit();
	
}

function clean()
{
	top.jzts();
	$("#searchForm").attr("action","SysConfig/SysConfigClean.do");
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

</script>
</html>