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
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
						<!-- 检索  -->
						<form action="qualityStat/sList.do" method="post" name="qualityStatForm" id="qualityStatForm">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td style="vertical-align:top;padding-left:2px;"> 
									<select class="chosen-select form-control" name="TASK_TYPE_ID" id="TASK_TYPE_ID" data-placeholder="任务类型"  style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<c:forEach items="${taskTypeList}" var="var">
										<option value="${var.D_KEY }" <c:if test="${pd.TASK_TYPE_ID == var.D_KEY }">selected</c:if>>${var.D_VALUE}</option>
										</c:forEach>
									</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;"> 
									<select class="chosen-select form-control" name="TASK_TYPE_CHILD_ID" id="TASK_TYPE_CHILD_ID" data-placeholder="任务子类型"  style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<c:forEach items="${taskTypeChildList}" var="var">
										<option value="${var.D_KEY }" <c:if test="${pd.TASK_TYPE_CHILD_ID == var.D_KEY }">selected</c:if>>${var.D_VALUE}</option>
										</c:forEach>
									</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;"> 
									<select class="chosen-select form-control" name="STEP" id="STEP" data-placeholder="审核步骤"  style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<option value="1" <c:if test="${pd.STEP == '1' }">selected</c:if> >一审</option>
										<option value="2" <c:if test="${pd.STEP == '2' }">selected</c:if> >二审</option>
									</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;"> 
									<input class="nav-search-input" autocomplete="off" id="DUTY_USER" type="text" name="DUTY_USER" value="${pd.DUTY_USER }" placeholder="这里输入责任人" />
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="ALLOT_START" id="ALLOT_START"  value="${pd.ALLOT_START}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:98px;" placeholder="分配开始日期" title="任务分配开始"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="ALLOT_END" id="ALLOT_END"  value="${pd.ALLOT_END}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:98px;" placeholder="分配结束日期" title="任务分配结束"/></td>
								<td style="padding-left:2px;">
									<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
								</td>
								<td style="vertical-align:top;padding-left:2px;">
									<ts:rights code="qualityStat/excel">
										<a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a>
									</ts:rights>
								</td>
							</tr>
						</table>
						<!-- 检索  -->
						<table style="width:100%;">
							<tr>
								
							</tr>
						</table>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">责任人</th>
									<th class="center">任务类型</th>
									<th class="center">任务子类型</th>
									<th class="center">任务步骤</th>
									<th class="center">分配总数</th>
									<th class="center">未处理</th>
									<th class="center">已处理</th>
									<th class="center">已提交</th>
									<th class="center">采纳率</th>
									<!-- <th class="center">已完成</th> -->
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty sList}">
									<c:forEach items="${sList}" var="t" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${t.DUTY_USER }</td>
											<td class="center">${t.TASK_TYPE }</td>
											<td class="center">${t.TASK_TYPE_CHILD }</td>
											<td style="width: 60px;" class="center">
												<c:if test="${t.STEP == '1' }"><span class="label label-important arrowed-in">一审</span></c:if>
												<c:if test="${t.STEP == '2' }"><span class="label label-important arrowed-in">二审</span></c:if>
											</td>
											<td class="center">${t.all_count }</td>
											<td class="center">${t.x0_count }</td>
											<td class="center">${t.x1_count }</td>
											<td class="center">${t.x2_count }</td>
											<td class="center">
												<c:if test="${t.STEP == '2' }">100%</c:if>
												<c:if test="${t.STEP == '1' and t.adopt_count ==0}">0</c:if>
												<c:if test="${t.STEP == '1' and t.adopt_count !=0}">${t.adopt_count*100.00/ t.x2_count}%</c:if>
											</td>
											<!-- <td class="center">${t.x3_count }</td> -->
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
							<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
						</tr>
					</table>
					</div>
					</form>
	
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
	</body>
<script type="text/javascript">
$(top.hangge());
//检索
function searchs(){
	top.jzts();
	$("#qualityStatForm").submit();
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		$(window)
		.off('resize.chosen')
		.on('resize.chosen', function() {
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.parent().width()});
			});
		}).trigger('resize.chosen');
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

	
	//复选框全选控制
	var active_class = 'active';
	$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
		var th_checked = this.checked;//checkbox inside "TH" table header
		$(this).closest('table').find('tbody > tr').each(function(){
			var row = this;
			if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
			else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
		});
	});
});
//导出excel
function toExcel(){
	var keywords = $("#nav-search-input").val();
	var TASK_TYPE_ID = $("#TASK_TYPE_ID").val();
	var TASK_TYPE_CHILD_ID = $("#TASK_TYPE_CHILD_ID").val();
	var STEP = $("#STEP").val();
	var DUTY_USER = $("#DUTY_USER").val();
	var ALLOT_START = $("#ALLOT_START").val();
	var ALLOT_END = $("#ALLOT_END").val();
	window.location.href='<%=basePath%>qualityStat/excel.do?keywords='
			+keywords+'&TASK_TYPE_ID='+TASK_TYPE_ID+'&TASK_TYPE_CHILD_ID='
			+TASK_TYPE_CHILD_ID+'&STEP='+STEP+'&DUTY_USER='+DUTY_USER+
		'&ALLOT_START='+ALLOT_START+'&ALLOT_END='+ALLOT_END;
}
</script>
</html>
