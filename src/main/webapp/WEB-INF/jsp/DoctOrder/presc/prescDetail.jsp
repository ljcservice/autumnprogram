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
										<div class="tabbable" id="myTabbable" >
											<ul class="nav nav-tabs" id="myTab">
												<li class="active">
													<a data-toggle="tab" href="#doctOrder">
														<i class="green ace-icon fa fa-home bigger-120"></i>
														患者信息
													</a>
												</li>
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

											<div class="tab-content" style="overflow-y:auto;height:150px;padding-top: 2px;padding-left: 2px;padding-right: 2px;padding-bottom: 0px;">
												<div id="doctOrder" class="tab-pane fade in active" >
													<table class="table table-bordered table-striped table-responsive " style="font-size:10px;margin-bottom:0px;" >
														<tbody>
															<tr>
																 <td width="10%" class="info">患者编号:</th>
																 <th width="15%"  >${pat.patient_id }</th>
																 <td width="10%" class="info">患者姓名:</th>
																 <th width="15%" >${pat.PATIENT_NAME}</th>
																 <td width="10%" class="info">性别:</th>
																 <th width="15%" >${pat.PATIENT_SEX}</th>
																 <td width="10%" class="info">出生日期:</th>
																 <th width="15%"  >${pat.PATIENT_BIRTH }</th>
															</tr>
															<tr>
																 <td width="10%" class="info">年龄:</th>
																 <th width="15%" > ${pat.PATIENT_AGE } </th>
																 <td width="10%" class="info">患者类别:</th>
																 <th width="15%" colspan="3">${pat.IDENTITY }</th>
																 <td width="10%" class="info">处方日期:</th>
																 <th width="15%" > ${ pat.ORDER_DATE} </th>
															</tr>
															<tr>
																 <td width="10%" class="info">处方编号:</th>
																 <th width="15%" > ${ pat.dayCount} </th>
																 <td width="10%" class="info">科室:</th>
																 <th width="15%" > ${ pat.ORG_NAME} </th>
																 <td width="10%" class="info">医生姓名:</th>
																 <th width="15%" > ${ pat.DOCTOR_NAME} </th>
																 <td width="10%" class="info">药房:</th>
																 <th width="15%" > ${ pat.DISPENSARYNAME} </th>
															</tr>
															<tr>
																 <td width="10%" class="info">诊断:</th>
																 <th width="15%" colspan="7"> ${ pat.DIAGNOSIS_NAMES}</th>
															</tr>
															
														</tbody>
													</table>
												</div>

												<div id="orderCheck" class="tab-pane fade" >
													<iframe  name="CheckRsFrame" id="CheckRsFrame" scrolling ="auto"" frameborder="0"
														src="presc/checkRsView.do?id=${pat.id}&NGROUPNUM=${pat.NGROUPNUM}" style="margin:0px 0px 0px 0px;width:100%;height: 135px;">
													</iframe>
												</div>
											</div>
										</div>
										
									<div style="overflow-y:hidden;overflow-x:hidden; ">
										<iframe name="DoctFrame" id="DoctFrame" scrolling="no" frameborder="0" 
											src="presc/prescDetailList.do?id=${pat.id}&NGROUPNUM=${pat.NGROUPNUM}" style="margin:0 auto;width:100%;height: 100%">
										</iframe>
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
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2018082"></script>
<script type="text/javascript" src="static/js/ontology/tree.js?v=20180861"></script>
<script type="text/javascript">
$(top.hangge());
$(function() {
	//changeTree();
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	var myObj = document.getElementById("myTabbable");
// 	myObj.style.width = "970px"; 
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		//resize the chosen on window resize
	
		$(window)
		.off('resize.chosen')
		.on('resize.chosen', function() {
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.width()});
			})
		}).trigger('resize.chosen');
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

	//重置当前页面高度，自适应浏览器
	treeFrameT("DoctFrame");
});
function treeFrameT(obj){
	var hmainT = document.getElementById(obj);
	hmainT.style.width = '100%';
	hmainT.style.height = ( $(window).height() - $("#myTabbable").height() -10) + 'px';
}
// 查询
function searchs(){
	top.jzts();
	$("#searchForm").submit();
	
}

</script>
</html>