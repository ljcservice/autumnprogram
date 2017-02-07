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
						<div id="zhongxin" style="padding-top: 13px;">
						<form action="taskAllot/toAllot.do" method="post" name="taskAllotUserForm" id="taskAllotUserForm">
						<input type="hidden" name="allot_type" id="allot_type" value="${pd.allot_type }"/><!-- 任务分配的类型  1选中2所有3范围 -->
						<input type="hidden" name="q_ids" id="q_ids" value="${pd.q_ids }"/><!-- 分配类型为选中的IDs 用逗号分隔 -->
						<!-- 其他任务分配的参数信息 -->
						<input type="hidden" name="SEARCH_ORIGIN_ID" id="SEARCH_ORIGIN_ID" value="${pd.SEARCH_ORIGIN_ID}"/>
						<input type="hidden" name="SEARCH_TASK_TYPE_ID" id="SEARCH_TASK_TYPE_ID" value="${pd.SEARCH_TASK_TYPE_ID}"/>
						<input type="hidden" name="SEARCH_TASK_TYPE_CHILD_ID" id="SEARCH_TASK_TYPE_CHILD_ID" value="${pd.SEARCH_TASK_TYPE_CHILD_ID}"/>
						<input type="hidden" name="SEARCH_EXP_ID" id="SEARCH_EXP_ID" value="${pd.SEARCH_EXP_ID}"/>
						<input type="hidden" name="SEARCH_createStart" id="SEARCH_createStart" value="${pd.SEARCH_createStart}"/>
						<input type="hidden" name="SEARCH_createEnd" id="SEARCH_createEnd" value="${pd.SEARCH_createEnd}"/>
						<input type="hidden" name="SEARCH_keywords" id="SEARCH_keywords" value="${pd.SEARCH_keywords}"/>
						<input type="hidden" name="start_count" id="start_count" value="${pd.start_count}"/>
						<input type="hidden" name="end_count" id="end_count" value="${pd.end_count}"/>
						
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;" >
						
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">用户名称</th>
									<th class="center">姓名</th>
									<th class="center">编号</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty userList}">
									<c:forEach items="${userList}" var="user" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
												<input type='checkbox' name='ids' value="${user.USER_ID }" id="${user.USER_ID }" class="ace"/><span class="lbl"></span>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${user.USERNAME }</td>
											<td class="center">${user.NAME }</td>
											<td class="center">${user.USER_NO }</td>
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
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
							</tr>
						</table>
						<table style="width:100%;">
							<tr>
								<td style="text-align: center;" colspan="10">
									<ts:rights code="taskAllot/allot">
										<a class="btn btn-mini btn-primary"  id="allot_btn" onclick="allot();">分配</a>
									</ts:rights>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</form>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
						</div>
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
//分配
function allot(){
	var q_ids=$("#q_ids").val();
	var allot_type=$("#allot_type").val();
	var SEARCH_TASK_TYPE_ID=$("#SEARCH_TASK_TYPE_ID").val();
	var SEARCH_TASK_TYPE_CHILD_ID=$("#SEARCH_TASK_TYPE_CHILD_ID").val();
	var SEARCH_ORIGIN_ID=$("#SEARCH_ORIGIN_ID").val();
	var SEARCH_createStart=$("#SEARCH_createStart").val();
	var SEARCH_createEnd=$("#SEARCH_createEnd").val();
	var SEARCH_keywords=$("#SEARCH_keywords").val();
	var SEARCH_EXP_ID=$("#SEARCH_EXP_ID").val();
	
	var start_count=$("#start_count").val();
	var end_count=$("#end_count").val();

	//获取选中的人员
	var tableObj = document.getElementsByName('ids');
	
	var users = '';
	var user_count=0;
	for(var i=0;i < tableObj.length;i++)
	{
		  if(tableObj[i].checked){
			user_count++;
		  	if(users=='') users += tableObj[i].value;
		  	else users += ',' + tableObj[i].value;
		  	
		  }
	}
	//校验若为二审则只能够 选择一人进行任务分配
	if((SEARCH_EXP_ID==2||SEARCH_EXP_ID=='2')&&user_count>1){
		bootbox.dialog({
			message: "<span class='bigger-110'>NLP审核或者二审只能选择一个用户进行任务分配!</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return;
	}
	if(users==''){
		bootbox.dialog({
			message: "<span class='bigger-110'>您没有选择分配的人员!</span>",
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
		//执行任务分配的方法
		$.ajax({
			type: "POST",
			url: '<%=basePath%>taskAllot/allot.do',
	    	data: {users:users,q_ids:q_ids,SEARCH_TASK_TYPE_ID:SEARCH_TASK_TYPE_ID,
	    		SEARCH_TASK_TYPE_CHILD_ID:SEARCH_TASK_TYPE_CHILD_ID,
	    		SEARCH_ORIGIN_ID:SEARCH_ORIGIN_ID,
	    		SEARCH_keywords:SEARCH_keywords,
	    		SEARCH_createStart:SEARCH_createStart,SEARCH_createEnd:SEARCH_createEnd,
	    		start_count:start_count,end_count:end_count,SEARCH_EXP_ID:SEARCH_EXP_ID,
	    		allot_type:allot_type,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					 $("#zhongxin").hide();
					 //$("#zhongxin2").show();
					 top.Dialog.close();
				 }else{
					$("#allot_btn").css("background-color","#D16E6C");
					//setTimeout("$('#TASK_TYPE_CHILD_ID').val('此类型的配置已存在!')",500);
					$("#allot_btn").tips({
						side:3,
			            msg:'任务分配失败',
			            bg:'#AE81FF',
			            time:3
			        });
				 }
			}
		});
	}
}

</script>
</html>
