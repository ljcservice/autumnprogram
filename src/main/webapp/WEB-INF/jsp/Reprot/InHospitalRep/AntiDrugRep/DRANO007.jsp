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
<!-- 提示 -->
<link rel="stylesheet" href="static/ace/css/jquery.gritter.css" />
<link rel="stylesheet" href="static/ace/css/jquery-ui.custom.css" />

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
								<form name="searchForm" id="searchForm" action="InHospitalRep/DRANO007.do" method="post" >  
									<div class="check-search"  >
										起止日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
										<font style="color: red;">*</font>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();" title="检索"  id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
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
  
<!-- 									<div class="check-search"> -->
<!-- 										查询方式：  -->
<!-- 										<select class="chosen-select form-control" name="findType" id="findType" data-placeholder="查询方式" style="vertical-align:top;width: 100px;">     -->
<!-- 											<option  value="persion" >按人次查询</option> -->
<%-- 											<option <c:if test="${pd.findType == 'count' }">selected</c:if> value="count" >按例数查询</option> --%>
<!-- 										</select> -->
<!-- 									</div> -->
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
									<th class="center" nowrap >患者姓名</th>
									<th class="center" nowrap >手术式</th>  
									<th class="center" nowrap>医生姓名</th>
									<th class="center" nowrap>住院科室</th>
									<th class="center" nowrap>手术日期</th>
									<th class="center" nowrap>原切口类型</th>
									<th class="center" nowrap>现切口类型</th>
									<th class="center" nowrap>抗菌药</th>
									<th class="center" nowrap>联合用药</th>
									<th class="center" nowrap>品种 </th>
									<th class="center" nowrap>疗程</th>
									<th class="center" nowrap>时机</th>
									<th class="center" nowrap>操作</th>
								</tr>
							</thead>
							<tbody>
							<c:set var="count" value="0">
									
							</c:set>
							<c:set var="antiCount" value="0" >
								
							</c:set>
							<!-- 开始循环 -->	 
							<c:choose>
								<c:when test="${not empty opers}">
									<c:forEach items="${opers}" var="oper" varStatus="vs" >
										
										<tr ondblclick="">
											<td nowrap class="center">${oper.name } </td>
											<td nowrap class="center">${oper.OPERATION_DESC }</td>
											<td nowrap class="center">${oper.OPERATOR }</td>
											<td nowrap class="center">${oper.dept_name }</td>
											<td nowrap class="center">
											     <fmt:formatDate value="${oper.OPERATING_DATE }" pattern="yyyy-MM-dd"/>	
											</td>
											<td nowrap class="center">${oper.WOUND_GRADE }</td>
											<td nowrap class="center" >
												<select name="WOUND_GRADE_UPDATE_${oper.patient_id}_${oper.visit_id}" id="WOUND_GRADE_UPDATE_${oper.patient_id}_${oper.visit_id}" >
													<option value=""></option>
													<option <c:if test="${oper.WOUND_GRADE_UPDATE == 'Ⅰ' }">selected</c:if> value="Ⅰ" >Ⅰ</option>
													<option <c:if test="${oper.WOUND_GRADE_UPDATE == 'Ⅱ' }">selected</c:if> value="Ⅱ" >Ⅱ</option>
													<option <c:if test="${oper.WOUND_GRADE_UPDATE == 'Ⅲ' }">selected</c:if> value="Ⅲ" >Ⅲ</option>
												</select>
											</td>
											<td nowrap class="center">
												<select name="HAS_ANTI_${oper.patient_id}_${oper.visit_id}" id="HAS_ANTI_${oper.patient_id}_${oper.visit_id}" >
													<option value="0"></option>  
													<option <c:if test="${oper.HAS_ANTI == '1' }">selected</c:if> value="1" >是</option>
												</select>
											</td>
											<td nowrap class="center">
												<select name="LH_${oper.patient_id}_${oper.visit_id}" id="LH_${oper.patient_id}_${oper.visit_id}">
													<option value="0"></option>  
													<option <c:if test="${oper.LH == '1' }">selected</c:if> value="1" >一联</option>
													<option <c:if test="${oper.LH == '2' }">selected</c:if> value="2">二联</option>
													<option <c:if test="${oper.LH == '3' }">selected</c:if> value="3">三联</option>
													<option <c:if test="${oper.LH == '4' }">selected</c:if> value="4">多联</option>
												</select>
											</td>
											<td nowrap class="center">
												<select name="PZ_${oper.patient_id}_${oper.visit_id}" id="PZ_${oper.patient_id}_${oper.visit_id}">
													<option value="0"></option>  
													<option <c:if test="${oper.PZ == '1' }">selected</c:if> value="1" >是</option>
												</select>
											</td>
											<td nowrap class="center">
												<select name="IS_TREATMENT_${oper.patient_id}_${oper.visit_id}" id="IS_TREATMENT_${oper.patient_id}_${oper.visit_id}">
													<option value="0"></option>  
													<option <c:if test="${oper.IS_TREATMENT == '1' }">selected</c:if> value="1" >是</option>
												</select> 
											</td>
											<td nowrap class="center">
												<select name="IS_TIMING_${oper.patient_id}_${oper.visit_id}" id="IS_TIMING_${oper.patient_id}_${oper.visit_id}">
													<option value="0"></option>  
													<option <c:if test="${oper.IS_TIMING == '1' }">selected</c:if> value="1" >是</option>
												</select>
											</td>
											<td nowrap class="center">
												<button class="btn btn-success" onclick="funSave('${oper.patient_id}','${oper.visit_id}','${oper.id}','${oper.name}')">保存</button>
											</td> 
										</tr>
									</c:forEach>  
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="14" class="center">没有相关数据</td>
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
	<script src="static/ace/js/jquery.gritter.js"></script>
	
 	 
	
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
	FixTable("simple-table", 4, rr); 
}

//var map = new Hash(object)

var HAS_ANTI  = "";
var IS_TIMING = "";
var IS_TREATMENT = "";
var LH = "";
var PZ = "";
var WOUND_GRADE_UPDATE = ""; 

function funSave(pat,vist,idx,patName){
	
	var HAS_ANTI = $("#simple-table #HAS_ANTI_" + pat + "_" + vist).val();
	var IS_TIMING = $("#simple-table #IS_TIMING_" + pat + "_" + vist).val();
	var IS_TREATMENT = $("#simple-table #IS_TREATMENT_" + pat + "_" + vist).val();
	var LH = $("#simple-table #LH_" + pat + "_" + vist).val();
	var PZ = $("#simple-table #PZ_" + pat + "_" + vist).val();
	var WOUND_GRADE_UPDATE = $("#simple-table #WOUND_GRADE_UPDATE_" + pat + "_" + vist).val();
	var id = idx;
	var titlename = "患者:" + patName;
	
	
	$.ajax({
		type: "POST",
		url: basePath + 'InHospitalRep/DRANO07_Ajax01.do', 
    	data: {HAS_ANTI:HAS_ANTI,IS_TIMING:IS_TIMING,IS_TREATMENT:IS_TREATMENT,LH:LH,PZ:PZ,WOUND_GRADE_UPDATE:WOUND_GRADE_UPDATE,id:id},
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			var strRs = "保存成功！";
			if(data.result!="ok")strRs = "保存失败！";
			
			$.gritter.add({
				// (string | mandatory) the heading of the notification
				title: titlename ,
				// (string | mandatory) the text inside the notification
				text: strRs,
				class_name: 'gritter-success gritter-light'
			});
		},
		error:function (XMLHttpRequest, textStatus, errorThrown) {
			mycun =0;    			
		 	alert('网络异常，请稍后重试');
		}
	});

	
}

function viewDetail(patId , visitId,ngnum){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="医嘱点评";
	diag.URL = "<%=path%>/InHospitalRep/DRAN007DetailView.do?patient_id=" + patId + "&visit_Id=" + visitId + "&ngroupnum=" + ngnum;    
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
	window.open(path + "/InHospitalRep/DRANO007Export.do?"+$("#searchForm").serialize());
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