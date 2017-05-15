<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="ts" uri="/rights"  %>
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
	<div class="main-container" id="main-container" >
		<!-- /section:basics/sidebar -->
		<div class="main-content" >
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12"  >
										<!-- #section:elements.tab -->
										<div class="tabbable" id="myTabbable" style="padding-top: 2px;" >
											<ul class="nav nav-tabs" id="myTab">
												<li class="active">
													<a data-toggle="tab" href="#doctOrder">
														<i class="green ace-icon fa fa-home bigger-120"></i>
														患者信息
													</a>
												</li>

												<li>
													<a data-toggle="tab" href="#opertion">
														手术情况
														<c:if test="${not empty oper}">
															<span class="badge badge-danger">${oper.size() }</span>
														</c:if>
													</a>
												</li>

<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#examination"> -->
<!-- 														检验情况 -->
<!-- 														<span class="badge badge-danger">3</span> -->
<!-- 													</a> -->
<!-- 												</li> -->
<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#nurse"> -->
<!-- 														护理情况 -->
<!-- 														<span class="badge badge-danger">1</span> -->
<!-- 													</a> -->
<!-- 												</li> -->
												
<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#ICUInfo"> -->
<!-- 														重症监护 -->
<!-- 														<span class="badge badge-danger"></span> -->
<!-- 													</a> -->
<!-- 												</li> -->
												
<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#inspection"> -->
<!-- 														检查报告 -->
<!-- 														<span class="badge badge-danger">3</span> -->
<!-- 													</a> -->
<!-- 												</li> -->
												<li>
													<a data-toggle="tab" href="#orderCheck" >
														点评结果
														<c:if test="${not empty checkRss}">
															<span class="badge badge-danger" id="checkSize">${checkRss.size()}</span>
														</c:if>
														<c:if test="${empty checkRss}">
															<span class="badge badge-danger" id="checkSize">0</span>
														</c:if>
													</a>
												</li>
											</ul>

											<div class="tab-content" style="padding:2px;max-height: 220px;overflow: auto;">
												<div id="doctOrder" class="tab-pane fade in active" >
													<table class="table table-bordered table-striped table-responsive " style="font-size:10px;margin-bottom: 2px;" >
														<tbody>
															<tr>
																 <td width="10%" class="info">患者ID:</th>
																 <th width="15%"  >${pat.patient_id }(${pat.visit_id})</th>
																 <td width="10%" class="info">患者姓名:</th>
																 <th width="15%" >${pat.name}</th>
																 <td width="10%" class="info">性别:</th>
																 <th width="15%" >${pat.sex}</th>
																 <td width="10%" class="info">年龄:</th>
																 <th width="15%" >
																 	${pat.PAT_AGE }
																 </th>
															</tr>
															  
															<tr>
																  <td width="10%" class="info">出生日期:</th>
																 <th width="15%"  ><fmt:formatDate value="${pat.DATE_OF_BIRTH }" pattern="yyyy-MM-dd"/></th>
																 <td width="10%" class="info">费别:</th>
																 <th width="15%" >${pat.charge_type }</th>
																 <td width="10%" class="info">住院天数:</th>
																 <th width="15%" > ${ pat.dayCount} </th>
																 <td width="10%" class="info">诊断:</th>
																 <th width="15%" >${ pat.DIAGNOSIS_DESC}</th>
															</tr>
															
															<tr>
																 <td width="10%" class="info">入院科室:</th>
																 <th width="15%"  >${pat.in_dept_name }</th>
																 <td width="10%" class="info">入院时间:</th>
																 <th width="15%" >
																 	<fmt:formatDate value="${pat.admission_date_time }" pattern="yyyy-MM-dd"/>
																 </th>
																 <td width="10%" class="info">出院科室:</th>
																 <th width="15%" >${pat.out_dept_name }</th>
																 <td width="10%" class="info">出院时间:</th>
																 <th width="15%" >
																 	<fmt:formatDate value="${pat.discharge_date_time}" pattern="yyyy-MM-dd"/> </td>
																 </th>
															</tr>
															
														</tbody>
													</table>
												</div>

												<div id="opertion" class="tab-pane fade" >
													<table id="simple-table" class="table table-striped table-bordered table-hover" >
													<thead>
														<tr>
															<th class="center" nowrap>手术名称</th>
															<th class="center" nowrap>切口等级</th>
															<th class="center" nowrap>切口愈合情况</th>
															<th class="center" nowrap>手术日期</th>
															<th class="center" nowrap>麻醉方法</th>
															<th class="center" nowrap>手术医师</th>
														</tr>
													</thead>
													<tbody>
													<!-- 开始循环 -->	
													<c:choose>
														<c:when test="${not empty oper}">
															<c:forEach items="${oper}" var="oper" varStatus="vs">
																<tr ondblclick="edit(this,'Edit')">
																	<td class='center' >
																		${oper.OPERATION_DESC }
																	</td>
																	<td class="center ">
																		${oper.WOUND_GRADE}
																	</td>
																	<td class="center " >${oper.HEAL }</td>
																	<td class="center " ><fmt:formatDate value="${oper.OPERATING_DATE }" pattern="yyyy-MM-dd"/></td>
																	<td class="center " >${oper.ANAESTHESIA_METHOD }</td>
																	<td class="center " >${oper.OPERATOR } </td>
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

												<div id="orderCheck" class="tab-pane fade" style="width: 100%;min-height: 100px;">
													<iframe  name="CheckRsFrame" id="CheckRsFrame" scrolling ="auto"" frameborder="0"
														src="DoctOrder/CheckRsViewUI.do?patient_id=${page.pd.patient_id}&visit_id=${page.pd.visit_id}&ngroupnum=${page.pd.ngroupnum}" style="margin:0px 0px 0px 0px;width:100%;height: 100%;">
													</iframe>
												</div>
											</div><!-- tab-content -->
											
										</div><!-- tabbable -->
										
										<div style="">
											<iframe name="DoctFrame" id="DoctFrame" scrolling="no" frameborder="0" 
												src="DoctOrder/DoctOrdersDetail.do?patient_id=${page.pd.patient_id}&visit_id=${page.pd.visit_id}&ngroupnum=${page.pd.ngroupnum}" style="margin:0 auto;width:100%;height: 100%;">
											</iframe>
										</div>
									
						</div><!-- /.col-xs-12 -->
					</div><!-- /.row -->
				</div><!-- /.page-content -->
			</div><!-- /.main-content-inner -->
		</div> <!-- /.main-content -->

		<!-- 返回顶部 -->
<%--		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">--%>
<%--			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>--%>
<%--		</a>--%>

	</div> <!-- /.main-container -->
	
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
	<script type="text/javascript" src="static/js/common/lockTable.js?v=20161"></script>
</body>
<script type="text/javascript">
$(top.hangge());
$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	//var myObj = document.getElementById("myTabbable");
	//myObj.style.width = "970px"; 
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		//resize the chosen on window resize
		//resize chosen on sidebar collapse/expand
		$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
			if(event_name != 'sidebar_collapsed') return;
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.width()});
			})
		});
	
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}
	//var title = $('#myTabbable');//the element I want to monitor
	//title.bind('DOMNodeInserted',
	// function(e) {
	//    alert('element  now contains: ' );
	//});
	//重新划分页面高度及大小
	//重置当前页面高度，自适应浏览器
	initWidthHeight();
	$("#myTabbable").bind('click', function() {
		setTimeout(initWidthHeight,300);
	});
});
var height = $(window).height();
//重置当前页面高度，自适应浏览器
function initWidthHeight(){
	//alert($("#myTabbable").outerHeight());
	//alert((height-$("#myTabbable").outerHeight()-10 ));
	$("#DoctFrame").css('height' ,(height-$("#myTabbable").outerHeight()-10 )+'px');
	//var myheight =height-$("#btnDiv").outerHeight()-$("#pageStrDiv").outerHeight();
	//FixTable("simple-table", 2, $(window).outerWidth(), myheight);
}
// 查询
function searchs(){
	top.jzts();
	$("#searchForm").submit();
	
}
</script>
</html>