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
						<form action="taskQuery/listTask4Ctrl.do" method="post" name="taskQueryForm" id="taskQueryForm">
						<input type="hidden" id="login_user_id" value="${pd.login_user_id }"><!-- 当前登录的用户ID -->
						<input type="hidden" id="DUTY_USER_ID" value="${pd.DUTY_USER_ID }"><!-- 操作人员 为空则为管理员 -->
						<input type="hidden" id="ctrl_flag" value="1"><!-- 标志此页面 -->
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
									<select class="chosen-select form-control" name="TASK_STAT" id="TASK_STAT" data-placeholder="任务状态"  style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<option value="0" <c:if test="${pd.TASK_STAT == '0' }">selected</c:if> >未处理</option>
										<option value="1" <c:if test="${pd.TASK_STAT == '1' }">selected</c:if> >已处理</option>
									</select>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="ALLOT_START" id="ALLOT_START"  value="${pd.ALLOT_START}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:98px;" placeholder="分配开始日期" title="任务分配开始"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="ALLOT_END" name="ALLOT_END"  value="${pd.ALLOT_END}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:98px;" placeholder="分配结束日期" title="任务分配结束"/></td>
								<td style="padding-left:2px;">
									<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索">
									<i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
								</td>
								<td>
									<ts:rights code="taskQuery/submitTaskAll">
										<div style="margin-left:3px;float:left" id="btn_div">
										<a  class="btn btn-light btn-xs" onclick="task_submit()"  title="提交">
										<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>提交</a>
										</div>
									</ts:rights>
								</td>
							</tr>
						</table>
						<table style="width:100%;">
							<tr>
								
							</tr>
						</table>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<td style="display:none;">任务ID</td>
									<td style="display:none;">责任人ID</td>
									<td style="display:none;">任务状态ID</td>
									<th class="center">问题单号</th>
									<th class="center">任务类型</th>
									<th class="center">任务子类型</th>
									<th class="center">审核步骤</th>
									<th class="center">任务状态</th>
									<th class="center">术语名称</th>
									<th class="center">负责人</th>
									<th class="center">分配人</th>
									<th class="center">分配时间</th>
									
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty taskList}">
									<c:forEach items="${taskList}" var="t" varStatus="vs">
										<tr ondblclick="task_ctl(this);">
											<td class='center' style="width: 30px;">
												<input type='checkbox' name='ids' value="${t.TASK_ID }" id="${t.TASK_ID }" class="ace"/><span class="lbl"></span>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td style="display:none;">${t.TASK_ID }</td>
											<td style="display:none;">${t.USER_ID }</td>
											<td style="display:none;">${t.TASK_STAT }</td>
											<td class="center">${t.Q_ID }</td>
											<td class="center">${t.TASK_TYPE }</td>
											<td class="center">${t.TASK_TYPE_CHILD }</td>
											
											<td style="width: 60px;" class="center">
												<c:if test="${t.STEP == '1' }"><span class="label label-success arrowed-in">一审</span></c:if>
												<c:if test="${t.STEP == '2' }"><span class="label label-success arrowed-in">二审</span></c:if>
											</td>
											<td style="width: 60px;" class="center">
												<c:if test="${t.TASK_STAT == '0' }"><span class="label label-success arrowed-in">未处理</span></c:if>
												<c:if test="${t.TASK_STAT == '1' }"><span class="label label-success arrowed-in">已处理</span></c:if>
											</td>
											<td class="center">${t.DIAG_NAME }</td>
											<td class="center">${t.DUTY_USER }</td>
											<td class="center">${t.ALLOT_USER }</td>
											<td class="center">${t.ALLOT_TIME }</td>
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
	$("#taskQueryForm").submit();
}
function resetForm(){
	document.getElementById("taskQueryForm").reset();
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
	if($("#TASK_STAT").val()==1)
	{
		$("#btn_div").show();
	}
		
	else
	{
	$("#btn_div").hide();
	}
});
//列表的双击事件
function task_ctl(tr_obj){
	var task_id=tr_obj.children[2].innerHTML;//任务ID
	var duty_user_id=tr_obj.children[3].innerHTML;//责任人ID
	var task_stat=tr_obj.children[4].innerHTML;//任务状态
	var login_user_id=$("#login_user_id").val();//登录用户的ID
	//校验当前的用户能否处理当前的任务1、当前的用户与处理人相同
	if(login_user_id==duty_user_id){
		//弹出任务处理的对话框
		task_ctr_diag(task_id,task_stat);
	}
}
//弹出的任务处理对话框
function task_ctr_diag(task_id,task_stat){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="人工干预";
	 diag.URL = '<%=path%>/taskQuery/toCtl.do?ctrl_flag=1&task_id='+task_id+'&task_stat='+task_stat;
	 diag.Width = $(top.window).width();
	 diag.Height = $(top.window).height();
	 diag.CancelEvent = function(){ //关闭事件
		 searchs();
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
//提交
function task_submit(){
	var login_user_id=$("#login_user_id").val();//登录用户的ID
	var duty_user_id=$("#DUTY_USER_ID").val();//责任人ID
	if(duty_user_id==''||login_user_id!=duty_user_id){
		bootbox.dialog({
			message: "<span class='bigger-110'>当前用户不具有提交的权限</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return;
	}
	bootbox.confirm("确认要提交所选择的任务吗?", function(result) {
		if(result) {
			var str = '';
			for(var i=0;i < document.getElementsByName('ids').length;i++)
			{
				  if(document.getElementsByName('ids')[i].checked){
				  	if(str=='') str += document.getElementsByName('ids')[i].value;
				  	else str += ',' + document.getElementsByName('ids')[i].value;
				  	
				  }
			}
			if(str==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				$("#zcheckbox").tips({
					side:3,
		            msg:'点这里全选',
		            bg:'#AE81FF',
		            time:8
		        });
				return;
			}else{
				$.ajax({
					type: "POST",
					url: '<%=basePath%>taskQuery/submitTaskAll.do?tm='+new Date().getTime(),
			    	data: {TASK_IDS:str},
					dataType:'json',
					//beforeSend: validateData,
					cache: false,
					success: function(data){
						if(data.result=="success"){
							top.jzts();
							 $.each(data.list, function(i, list){
								 if('${page.currentPage}' == '0'){
									 top.jzts();
									 setTimeout("self.location=self.location",100);
								 }else{
									 nextPage(${page.currentPage});
								 }
							 });
						}else{
							bootbox.dialog({
								message: "<span class='bigger-110'>"+data.result+"</span>",
								buttons: 			
								{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
							});
						}
					}
				});
			}
		}
	});
}
</script>
</html>
