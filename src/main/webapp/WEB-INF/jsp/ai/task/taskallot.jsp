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
						<input type="hidden" name="SEARCH_ORIGIN_ID" id="SEARCH_ORIGIN_ID" value="${pd.SEARCH_ORIGIN_ID}"/>
						<input type="hidden" name="SEARCH_TASK_TYPE_ID" id="SEARCH_TASK_TYPE_ID" value="${pd.SEARCH_TASK_TYPE_ID}"/>
						<input type="hidden" name="SEARCH_TASK_TYPE_CHILD_ID" id="SEARCH_TASK_TYPE_CHILD_ID" value="${pd.SEARCH_TASK_TYPE_CHILD_ID}"/>
						<input type="hidden" name="SEARCH_EXP_ID" id="SEARCH_EXP_ID" value="${pd.SEARCH_EXP_ID}"/>
						<input type="hidden" name="SEARCH_createStart" id="SEARCH_createStart" value="${pd.SEARCH_createStart}"/>
						<input type="hidden" name="SEARCH_createEnd" id="SEARCH_createEnd" value="${pd.SEARCH_createEnd}"/>
						<input type="hidden" name="SEARCH_keywords" id="SEARCH_keywords" value="${pd.SEARCH_keywords}"/>
						<!-- 检索  -->
						<form action="taskAllot/listQuestion.do" method="post" name="taskAllotForm" id="taskAllotForm">
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
									<select class="chosen-select form-control" name="ORIGIN_ID" id="ORIGIN_ID" data-placeholder="数据来源"  style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option value="">全部</option>
										<c:forEach items="${originList}" var="var">
										<option value="${var.D_KEY }" <c:if test="${pd.ORIGIN_ID == var.D_KEY }">selected</c:if>>${var.D_VALUE}</option>
										</c:forEach>
									</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;"> 
									<select class="chosen-select form-control" name="TASK_TYPE_ID" id="TASK_TYPE_ID" data-placeholder="任务类型"  style="vertical-align:top;width: 120px;">
										<c:forEach items="${taskTypeList}" var="var">
										<option value="${var.D_KEY }" <c:if test="${pd.TASK_TYPE_ID == var.D_KEY }">selected</c:if>>${var.D_VALUE}</option>
										</c:forEach>
									</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;"> 
									<select onchange="changeTaskTypeChild(this)" class="chosen-select form-control" name="TASK_TYPE_CHILD_ID" id="TASK_TYPE_CHILD_ID" data-placeholder="任务子类型"  style="vertical-align:top;width: 120px;">
										<c:forEach items="${taskTypeChildList}" var="var">
										<option value="${var.D_KEY }" <c:if test="${pd.TASK_TYPE_CHILD_ID == var.D_KEY }">selected</c:if>>${var.D_VALUE}</option>
										</c:forEach>
									</select>
								</td>
								<td id="btn_step" style="vertical-align:top;padding-left:2px;"> 
									<select class="chosen-select form-control" name="EXP_ID" id="EXP_ID" data-placeholder="审核步骤"  style="vertical-align:top;width: 120px;">
										<option value="1" <c:if test="${pd.EXP_ID == '1' }">selected</c:if> >一审</option>
										<option value="2" <c:if test="${pd.EXP_ID == '2' }">selected</c:if> >二审</option>
									</select>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="createStart" id="createStart"  value="${pd.createStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px;" placeholder="问题单开始日期" title="问题单开始"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="createEnd" name="createEnd"  value="${pd.createEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px;" placeholder="问题单结束日期" title="问题单结束"/></td>
							</tr>
						</table>
						<!-- 检索  -->
						<!-- 分配 -->
						<table style="width:100%;">
							<tr>
								
								<td style="vertical-align:top;padding-top:2px;">
								<div style="float:left" >
								<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
								
								<ts:rights code="taskAllot/toAllot">
									<a class="btn btn-light btn-xs" onclick="taskallot()"  title="分配">
									<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>分配</a>
									<input type="radio" name="allot_type" value="1" checked="checked">分配选中       
									<input type="radio" name="allot_type" value="2">分配所有     
									<!--  
									<input type="radio" name="allot_type" value="3">从
									<input type="number" id ="start_count" style="width:80px;"> 
									到 <input type="number" id ="end_count" style="width:80px;"> -->
								</ts:rights> 
								</div> 
								<ts:rights code="taskAllot/skipNLP">
									<div style="float:left" id="btn_div">
										<a  class="btn btn-light btn-xs" onclick="skipNLP()"  title="跳过NLP审核">
										<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>跳过NLP审核</a>
									</div>
								</ts:rights>
								</td>
								
								
								
							</tr>
						</table>
						<!-- 分配 -->
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">问题单号</th>
									<th class="center">数据来源</th>
									<th class="center">任务类型</th>
									<th class="center">任务子类型</th>
									<th class="center">术语名称</th>
									<th class="center">NLP切词结果</th>
									<th class="center">状态</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty questionList}">
									<c:forEach items="${questionList}" var="q" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
												<input type='checkbox' name='ids' value="${q.Q_NO }" id="${q.Q_NO }" class="ace"/><span class="lbl"></span>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${q.Q_NO }</td>
											<td class="center">${q.ORIGIN }</td>
											<td class="center">${q.TASK_TYPE }</td>
											<td class="center">${q.TASK_TYPE_CHILD }</td>
											<td class="center">${q.DIAG_NAME }</td>
											<td class="center">${q.NLP_DIAG_NAME }</td>
											<td style="width: 60px;" class="center">
												<c:if test="${q.STATUS == '0' }"><span class="label label-success arrowed">初始</span></c:if>
												<c:if test="${q.STATUS == '1' }"><span class="label label-success arrowed-in">一审</span></c:if>
												<c:if test="${q.STATUS == '2' }"><span class="label label-success arrowed-in">二审</span></c:if>
												<c:if test="${q.STATUS == '3' }"><span class="label label-success arrowed-in">已分解</span></c:if>
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
	$("#taskAllotForm").submit();
}
function resetForm(){
	document.getElementById("taskAllotForm").reset();
}
//跳过NLP审核
function skipNLP(){
	//判断列表是否为空
	var tableObj = document.getElementsByName('ids');
    if (tableObj.length == 0) {
    	$("#nav-search-icon").tips({
			side:3,
            msg:'无数据需要操作，请重新查询!',
            bg:'#AE81FF',
            time:3
        });
        return false;
    }
    var str = '';
	for(var i=0;i < tableObj.length;i++)
	{
		  if(tableObj[i].checked){
			//校验选择的列表是否都包含NLP切词结果
			var table0 = document.getElementsByTagName("table")[2];
			var var7=table0.rows[i+1].cells[7].innerHTML;//NLP切词结果
			if(var7=='' ||var7==null){
				bootbox.dialog({
					message: "<span class='bigger-110'>您选择的内容包含未有NLP切词的问题单，不能跳过NLP审核!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				return;
			}
		  	if(str=='') str += tableObj[i].value;
		  	else str += ',' + tableObj[i].value;
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
		//选中的问题单的ID用','分隔
		bootbox.confirm("确定要跳过NLP审核吗?", function(result) {
			if(result) {
				$.ajax({
					type: "POST",
					url: '<%=basePath%>taskAllot/skipNLP.do?tm='+new Date().getTime(),
			    	data: {Q_IDS:str},
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
			};
		});
	}
};
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
	if($("#TASK_TYPE_CHILD_ID").val()==85002)
	{
		$("#btn_div").show();
		$("#btn_step").hide();
	}
		
	else
	{
	$("#btn_div").hide();
	$("#btn_step").show();
	}
});
//任务分配

function taskallot(){
	//判断列表是否为空
	var tableObj = document.getElementsByName('ids');
    if (tableObj.length == 0) {
    	$("#nav-search-icon").tips({
			side:3,
            msg:'无数据需要分配，请重新查询!',
            bg:'#AE81FF',
            time:3
        });
        return false;
    }
    //判断分配的方式
    var val=$('input:radio[name="allot_type"]:checked').val();
    if(val==1){//分配选中
    	var str = '';
		for(var i=0;i < tableObj.length;i++)
		{
			  if(tableObj[i].checked){
			  	if(str=='') str += tableObj[i].value;
			  	else str += ',' + tableObj[i].value;
			  	
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
			//选中的问题单的ID用','分隔
			toAllot(str,val);
		}
    }else if(val==2){//分配所有
    	toAllot('',val);
    }else{//分配范围
    	//校验范围的值是否合法
    	var start_count=$("#start_count").val();
    	var end_count=$("#end_count").val();
    	alert($("#start_count").val());
    	if(start_count==null||start_count==''){
    		$("#start_count").tips({
				side:3,
	            msg:'请填入数值',
	            bg:'#AE81FF',
	            time:8
	        });
    		return ;
    	}
    	if(end_count==null||end_count==''){
    		$("#end_count").tips({
				side:3,
	            msg:'请填入数值',
	            bg:'#AE81FF',
	            time:8
	        });
    		return ;
    	}
    	if(start_count>end_count){
    		$("#start_count").tips({
				side:3,
	            msg:'起始值不能大于结束值',
	            bg:'#AE81FF',
	            time:8
	        });
    		return ;
    	}
    	toAllot('',val);
    }
};
//打开任务分配窗口
function toAllot(q_ids,allot_type){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="任务分配";
	 diag.URL = '<%=path%>/taskAllot/toAllot.do?SEARCH_TASK_TYPE_ID='+$("#SEARCH_TASK_TYPE_ID").val()+
			 '&SEARCH_TASK_TYPE_CHILD_ID='+$("#SEARCH_TASK_TYPE_CHILD_ID").val()+
			 '&SEARCH_ORIGIN_ID='+$("#SEARCH_ORIGIN_ID").val()+
			 '&SEARCH_createStart='+$("#SEARCH_createStart").val()+
			 '&SEARCH_createEnd='+$("#SEARCH_createEnd").val()+
			 '&start_count='+$("#start_count").val()+
			 '&end_count='+$("#end_count").val()+
			 '&q_ids='+q_ids+'&allot_type='+allot_type+'&SEARCH_EXP_ID='+$("#SEARCH_EXP_ID").val();
	 diag.Width = 620;
	 diag.Height = 450;
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

//选择上个列表则选择下个列表
function q_click(tr_obj){
	var diag_name=tr_obj.children[2].innerHTML;//子问题单的诊断名称
	//便利问题单的处理结果 找到当前问题单的处理结果进行变色
	var s3 = document.getElementsByTagName("table")[1];
	for ( var i = 1; i < s3.rows.length; i++) {
		var va2=s3.rows[i].cells[2].innerHTML;
		if (diag_name == va2) {
			s3.rows[i].style.color = 'red'; //设置符合要求单元格的样式
		}else{
			s3.rows[i].style.color = 'black';
		}
	}
}
//变更任务的子类型
function changeTaskTypeChild(obj){
	if(obj.value==85002)
	{
		$("#btn_step").hide();
	}
	else
	{
		$("#btn_step").show();
	}
}
</script>
</html>
