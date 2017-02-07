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
						<form action="importLog/listImportLogs.do" method="post" name="importLogForm" id="importLogForm">
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
									<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
									<td style="vertical-align:top;">
										<ts:rights code="importLog/toImport">
											<a style=" margin:3px" class="btn btn-mini btn-success" onclick="importQ();">导入</a>
										</ts:rights>
									</td>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">文件名称</th>
									<th class="center">批次号</th>
									<th class="center">状态</th>
									<th class="center">导入成功数量</th>
									<th class="center">导入失败数量</th>
									<th class="center">导入时间</th>
									<th class="center">导入用户</th>
									<th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty logList}">
									<c:forEach items="${logList}" var="log" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${log.FILE_NAME }</td>
											<td class="center">${log.BATCH_NUMBER }</td>
											<td style="width: 60px;" class="center">
												<c:if test="${log.STATUS == '0' }"><span class="label label-success arrowed">初始</span></c:if>
												<c:if test="${log.STATUS == '1' }"><span class="label label-success arrowed-in">导入成功</span></c:if>
												<c:if test="${log.STATUS == '2' }"><span class="label label-important arrowed-in">导入失败</span></c:if>
												<c:if test="${log.STATUS == '3' }"><span class="label label-important arrowed-in">已撤销</span></c:if>
											</td>
											<td class="center">${log.SUC_COUNT }</td>
											<td class="center">${log.FAIL_COUNT }</td>
											<td class="center">${log.IMPORT_TIME }</td>
											<td class="center">${log.IMPORT_USER }</td>
											<td class="center">
												<ts:rights code="importLog/toDelImport">
												<a class="btn btn-mini <c:if test="${log.STATUS == '1' }">btn-danger</c:if>" <c:if test="${log.STATUS == '1' }">onclick="deleteImportQ(${log.IMPORT_ID});"</c:if>>撤销导入</a>
												</ts:rights>
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
	$("#importLogForm").submit();
}

function resetForm(){
	document.getElementById("importLogForm").reset();
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
//打开上传问题单页面
function importQ(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="导入问题单";
	 diag.URL = '<%=path%>/importLog/goUploadQ.do';
	 diag.Width = 800;
	 diag.Height = 200;
	 diag.CancelEvent = function(){ //关闭事件
		 //if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location.reload()",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		//}
		diag.close();
	 };
	 diag.show();
}
//撤销导入操作
function deleteImportQ(import_id){
	bootbox.confirm("确定要撤销当前的文件导入吗?", function(result) {
		if(result) {
			//判断能否删除
			$.ajax({
				type: "POST",
				url: '<%=basePath%>importLog/deleteImport.do',
		    	data: {IMPORT_ID:import_id,tm:new Date().getTime()},
				dataType:'json',
				cache: false,
				success: function(data){
					 if("success" == data.result){//撤销导入成功
						top.jzts();
						if('${page.currentPage}' == '0'){
							 top.jzts();
							 setTimeout("self.location=self.location",100);
						 }else{
							 nextPage(${page.currentPage});
						 }
					 }else{
						 bootbox.dialog({
							 	message: "<span class='bigger-110'>"+data.result+"</span>",
								buttons: 			
								{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
							});
					 }
				}
			});

		};
	});
}
</script>
</html>
