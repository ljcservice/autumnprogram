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

<!-- bootstrap & fontawesome -->
<!-- <link rel="stylesheet" href="static/html_UI/assets/css/bootstrap.css" /> -->
<!-- <link rel="stylesheet" href="static/html_UI/assets/css/font-awesome.css" /> -->  

<!-- page specific plugin styles -->
<!-- <link rel="stylesheet" href="static/html_UI/assets/css/bootstrap-duallistbox.css" /> -->
<link rel="stylesheet" href="static/html_UI/assets/css/bootstrap-multiselect.css" />
<!-- <link rel="stylesheet" href="static/html_UI/assets/css/select2.css" /> -->

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

.dropdown-menu{
	height: 350px;
	overflow-y: auto;
	overflow-x: hidden;
	padding-left: 5px;
	padding-right: 5px; 
	width: 160x; 
}

.multiselect-item {
	position: fixed; 
/* 	background-color: red; */
	width: 163px;  
	z-index: 4000;   
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
								<form name="searchForm" id="searchForm" action="drugUserAuth/showDrugUserAuth.do" method="post" > 
									<div class="check-search nav-search" > 
										<span class="input-icon">
											<input class="nav-search-input" style="width: 150px;" autocomplete="off" id="nav-search-input" type="text" name="workcode" value="${pd.workcode}" placeholder="药品名称/药品规格" maxlength="150"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();" title="检索"  id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div class="check-search">
										科室:  
											<!-- #section:plugins/input.multiselect -->
											<select id="deptDicts" name="deptDicts" class="multiselect" multiple="" style="height: 150px;overflow-y:auto;">
												<option></option>
												<option value="*"
													<c:if test="${not empty deptDictsMap['*'] }">selected</c:if> 
													>*</option>
												<c:forEach items="${depts}" var="en" varStatus="vs" >
													<option 
														<c:if test="${not empty deptDictsMap[en.dept_code] }">selected</c:if> 
														value="${en.dept_code}" >${en.dept_name}
													</option>
												</c:forEach>
<%-- 											</c:when> --%>
											</select>
											<!-- /section:plugins/input.multiselect -->
									</div>
									<div class="check-search" >
									控制类型:
											<select name="CONTROL_TYPE" id="CONTROL_TYPE" >
													<option value="">全部</option>
													<c:forEach items="${CONTROLTYPEs}" var="en" varStatus="vs" >
														<option <c:if test="${pd.CONTROL_TYPE == en.code }">selected</c:if> value="${en.code}" >${en.name}</option>
													</c:forEach>
											</select>
									</div>
									<div class="check-search">
										排序:  
											<!-- #section:plugins/input.multiselect -->
											<select id="orderby" name="orderby" class="">
												<option value="DEPT_NAME" 
													<c:if test="${pd.orderby == 'DEPT_NAME'}">selected</c:if>
													>科室名称</option>
												<option value="DOCTOR_NAME"
													<c:if test="${pd.orderby == 'DOCTOR_NAME'}">selected</c:if>
													>医生名称</option>
												<option value="DRUG_NAME"
													<c:if test="${pd.orderby == 'DRUG_NAME'}">selected</c:if>
													>药品名称</option>
											</select>
											<!-- /section:plugins/input.multiselect -->
									</div>
<!-- 									<div class="check-search"> -->
<!-- 										<a title="最大支持导出6万条" class="btn btn-mini btn-success" onclick="listExport();">导出</a> -->
<!-- 										<a title="" class="btn btn-mini btn-success" onclick="myprint();">打印</a> -->
<!-- 									</div> -->
								</form>
							</div>
							<div style="width: 100%;height: auto;">
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap style="width:45px;">序号</th>
									<th class="center" style="width: 50px;" >药品名称</th>
									<th class="center" style="width: 50px;">药品规格</th>
									<th class="center" style="width: 130px;">科室</th>  
									<th class="center" style="width: 80px;" >医生</th>
									<th class="center" style="width: 100px;" >控制类型</th>
									<th class="center" style="width: 60px;" >上线值</th>
									<th class="center" style="width: 80px;" >当前累计值</th>
									<th class="center" style="width: 80px;" >操作 </th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	 
							<c:choose>
								<c:when test="${not empty entitys}">
									<c:forEach items="${entitys}" var="entity" varStatus="vs" >
										<tr ondblclick="">
											<td nowrap class='center' style="width: 30px;">${vs.index+1}</td>
											<td nowrap class="center">${entity.DRUG_NAME} </td>
											<td nowrap class="center">${entity.DRUG_SPEC}</td>
											<td style="width: 130px;" class="center">${entity.DEPT_NAME}</td>
											<td style="width: 80px;" class="center">${entity.DOCTOR_NAME}</td>
											<td style="width: 80px;" class="center">${mapCY[entity.CONTROL_TYPE]}</td>
											<td style="width: 80px;" class="center">${entity.getT_VALUE()}</td>
											<td style="width: 80px;" class="center">${entity.getTOTAL_VALUE()}</td>
											
											<th class="center" style="width: 80px;" >
<%-- 												<ts:rights code="user/toEdit"> --%>
													<a class="btn btn-xs btn-success" title="编辑" onclick="editFunc('${entity.ID}');">
														<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
													</a>
<%-- 												</ts:rights> --%>
<%-- 												<ts:rights code="user/delete"> --%>
													<a class="btn btn-xs btn-danger" onclick="delfunc('${entity.ID}','${entity.DRUG_NAME}|${entity.DEPT_NAME}|${entity.DOCTOR_NAME}');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
<%-- 												</ts:rights> --%>
											</th>
										</tr>
									</c:forEach>  
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
						<div class= "pageStrDiv" id="pageStrDiv" style="padding-top: 5px;padding-bottom: 5px;">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;">
<%-- 										<ts:rights code="user/toAdd"> --%>
											<a class="btn btn-mini btn-success" onclick="add();">新增</a>
<%-- 										</ts:rights> --%>
									</td>
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
	
		 
	<script src="static/html_UI/assets/js/bootstrap.js"></script>
	<!-- page specific plugin scripts -->
<!-- 	<script src="static/html_UI/assets/js/jquery.bootstrap-duallistbox.js"></script> -->
<!-- 	<script src="static/html_UI/assets/js/jquery.raty.js"></script> -->
	<script src="static/html_UI/assets/js/bootstrap-multiselect.js"></script>
<!-- 	<script src="static/html_UI/assets/js/select2.js"></script> -->  
	
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
	

	$('.multiselect').multiselect({
	 enableFiltering: true,
	 buttonClass: 'btn btn-white btn-primary',
	 templates: {
		button: '<button type="button" class="multiselect dropdown-toggle" data-toggle="dropdown"></button>',
		ul: '<ul class="multiselect-container dropdown-menu"></ul>',
		filter: '<li class="multiselect-item filter"><div class="input-group"><span class="input-group-addon"><i class="fa fa-search"></i></span><input class="form-control multiselect-search" type="text"></div></li>',
		filterClearBtn: '<span class="input-group-btn"><button class="btn btn-default btn-white btn-grey multiselect-clear-filter" type="button"><i class="fa fa-times-circle red2"></i></button></span>',
		li: '<li><a href="javascript:void(0);"><label></label></a></li>',
		divider: '<li class="multiselect-item divider"></li>',
		liGroup: '<li class="multiselect-item group"><label class="multiselect-group"></label></li>'
	 }
	});

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

//删除
function delfunc(Code,msg){
	bootbox.confirm("确定要删除["+msg+"]吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>drugUserAuth/delDrugUserAuth.do?ID="+Code+"&tm="+new Date().getTime();
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//修改
function editFunc(_Code){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="修改";
	 diag.URL = '<%=path%>/drugUserAuth/editUIDrugUserAuth.do?ID=' + _Code;
	 diag.Width = 469;
	 diag.Height = 510;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}


//新增
function add(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="新增";
	 diag.URL = '<%=path%>/drugUserAuth/addUIDrugUserAuth.do';
	 diag.Width = 469;
	 diag.Height = 510;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}


function listExport(){
	window.open(path + "/InHospitalRep/DRNO001Export.do?"+$("#searchForm").serialize());
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