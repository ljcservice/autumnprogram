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
						<div class="col-xs-12" id="zhongxin">
							<form name="searchForm" id="searchForm" action="DoctOrder/OrderWork.do" method="post" > 
							<input type="hidden" name="category_id" id="category_id" value=""/>   
							<table style="margin-top:5px;">
								<tr >
									<td style="padding-left:2px;padding-right:8px;">
										<div class="nav-search">
											<span class="input-icon">
												<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${page.pd.keywords}" placeholder="科室/诊断" maxlength="32"/>
												<i class="ace-icon fa fa-search nav-search-icon"></i>
											</span>
										</div>
									</td>
									
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;">
										<a class="btn btn-light btn-xs" onclick="searchs();" title="检索"  id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
									</td>
									
									<td style="padding-left:2px;padding-right:8px;" nowrap>
										出院日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${page.pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${page.pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
									</td>
									
								
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap> 
										是否点评：
									 	<select class="chosen-select form-control" name="orderRemark" id="orderRemark" data-placeholder="医嘱是否点评" style="vertical-align:top;width: 80px;" onchange="changeTree();">
											<option value="">全部</option>
											<option <c:if test="${page.pd.orderRemark == '1' }">selected</c:if> value="1" >已点评</option>
											<option <c:if test="${page.pd.orderRemark == '0' }">selected</c:if> value="0" >未点评</option>
										</select>
									</td>
									
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap>
										是否合理：
									 	<select class="chosen-select form-control" name="OrderIsTrue" id="OrderIsTrue" data-placeholder="医嘱是否合理" style="vertical-align:top;width: 120px;">
									 		<option value="">全部</option>
											<option <c:if test="${page.pd.OrderIsTrue == '1' }">selected</c:if> value="1" >合理</option>
											<option <c:if test="${page.pd.OrderIsTrue == '0' }">selected</c:if> value="0" >不合理</option>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap>
										科室：
										<input type="text" name="orderDept" id="orderDept"  maxlength="50" placeholder="科室名称" title="科室名称" value="${page.pd.orderDept }"/>
									</td>
									
								</tr>
							</table>
							
							
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">患者ID</th>
									<th class="center">患者名称</th>
									<th class="center">入院科室</th>
									<th class="center"><i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>入院时间</th>
									<th class="center">出院科室</th>
									<th class="center"><i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>出院时间</th>
									<th class="center">点评</th>
									<th class="center">合理</th>
									<th class="center" style="width:260px;">结果</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty patVisits}">
									<c:forEach items="${patVisits}" var="patVisit" varStatus="vs" >
												
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center"> <a onclick="javascript:viewDetail('${patVisit.PATIENT_ID}','${patVisit.VISIT_ID}','${patVisit.ngroupnum}')" style="cursor:pointer;">${patVisit.PATIENT_ID}(${patVisit.VISIT_ID})</a> </td>
											<td class="center">${patVisit.NAME }</td>
											<td class="center">${patVisit.in_dept_name }</td>
											<td class="center"><fmt:formatDate value="${patVisit.admission_date_time }" pattern="yyyy-MM-dd"/> </td>
											<td class="center"> ${patVisit.out_dept_name }</td>
											<td class="center"><fmt:formatDate value="${patVisit.discharge_date_time}" pattern="yyyy-MM-dd"/> </td> 
											<td class="center">
												<c:choose>
													<c:when test="${patVisit.ISORDERCHECK == 1 }">
													是
													</c:when>
													<c:otherwise>
													否
													</c:otherwise>
												</c:choose>
											</td>
											<td style="width: 60px;" class="center">
												
												<c:choose>
													<c:when test="${patVisit.ISCHECKTRUE == 0 }">
													否
													</c:when>
													<c:otherwise>
													是
													</c:otherwise>
												</c:choose>
												
<%-- 												<c:if test="${user.STATUS == '0' }"><span class="label label-success arrowed">正常</span></c:if> --%>
<%-- 												<c:if test="${user.STATUS == '1' }"><span class="label label-important arrowed-in">冻结</span></c:if> --%>
											</td>
											<td class="center">
												<div class="hidden-sm hidden-xs btn-group">
													<c:forEach items="${patVisit.RS_DRUG_TYPES}" var="rs_type" varStatus="vs" >
														<a class="btn btn-xs btn-purple"  title="${checktypeMap.get(rs_type).RS_TYPE_NAME}" >
															${rstypeMap.get(rs_type)}
														</a>
													</c:forEach>
													<a class="btn btn-xs"  title="医院管理" onclick="editRoles('${user.USER_ID }');">
														管
													</a>
												
<!-- 														<span class="btn btn-sm" data-rel="tooltip" title="Default">Default</span> -->
<!-- 														<span class="btn btn-warning btn-sm tooltip-warning" data-rel="tooltip" data-placement="left" title="Left Warning">Left</span> -->
<!-- 														<span class="btn btn-success btn-sm tooltip-success" data-rel="tooltip" data-placement="right" title="Right Success">Right</span> -->
<!-- 														<span class="btn btn-danger btn-sm tooltip-error" data-rel="tooltip" data-placement="top" title="Top Danger">Top</span> -->
<!-- 														<span class="btn btn-info btn-sm tooltip-info" data-rel="tooltip" data-placement="bottom" title="Bottm Info">Bottom</span> -->
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						
						<div class="page-header position-relative">
					<table style="width:100%;"> 
						<tr>
<!-- 							<td style="vertical-align:top;"> -->
<%-- 								<ts:rights code="user/toAdd"> --%>
<!-- 									<a class="btn btn-mini btn-success" onclick="add();">新增</a> -->
<%-- 								</ts:rights> --%>
<%-- 								<ts:rights code="fhsms/goAdd"> --%>
<!-- 									<a title="批量发送站内信" class="btn btn-mini btn-info" onclick="makeAll('确定要给选中的用户发送站内信吗?');"><i class="ace-icon fa fa-envelope-o bigger-120"></i></a> -->
<%-- 								</ts:rights> --%>
<%-- 								<ts:rights code="head/goSendEmail"> --%>
<!-- 									<a title="批量发送电子邮件" class="btn btn-mini btn-primary" onclick="makeAll('确定要给选中的用户发送邮件吗?');"><i class="ace-icon fa fa-envelope bigger-120"></i></a> -->
<%-- 								</ts:rights> --%>
<%-- 								<ts:rights code="head/goSendSms"> --%>
<!-- 									<a title="批量发送短信" class="btn btn-mini btn-warning" onclick="makeAll('确定要给选中的用户发送短信吗?');"><i class="ace-icon fa fa-envelope-o bigger-120"></i></a> -->
<%-- 								</ts:rights> --%>
<%-- 								<ts:rights code="user/deleteAll"> --%>
<!-- 									<a title="批量删除" class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a> -->
<%-- 								</ts:rights> --%>
<!-- 							</td> -->
							<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
						</tr>
					</table>
					</div>
					</form>
						</div>
						
<!-- 						<div name="" style="padding-top: 30px;"> -->
<!-- 							<table style="width:100%;" border="0"> -->
<!-- 								<tr> -->
<!-- 									<td style="width:15%;" valign="top"> -->
<!-- 										<div style='overflow: scroll;max-width: 350px;max-height: auto;'> -->
<!-- 											<div id="treeName">本体数</div> -->
<!-- 											<div id="treeId"> -->
<!-- 												<ul id="leftTree" class="ztree"></ul> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</td> -->
<!-- 									<td style="width:85%;" valign="top" > -->
<%-- 										<iframe name="treeFrame" id="treeFrame" scrolling="no" frameborder="0" src="${basePath}ontology/ontologyList.do?ontoType=${ontoType}" style="margin:0 auto;width:100%;"></iframe> --%>
<!-- 										<iframe name="osynFrame" id="osynFrame" frameborder="0" src="${basePath}osyn/osynList.do?initFlag=0&ontoType=${ontoType}" style="margin:0 auto;width:100%;height: 300px;"></iframe> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</table> -->
<!-- 						</div> -->
						
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
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2018082"></script>
<script type="text/javascript" src="static/js/ontology/tree.js?v=20180861"></script>
<script type="text/javascript">
$(top.hangge());
$(function() {
	changeTree();
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
	diag.Width = 1000;//window.screen.width;
	diag.Height = 900;//window.screen.height;  
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