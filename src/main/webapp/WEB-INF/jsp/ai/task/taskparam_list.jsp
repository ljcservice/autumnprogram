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
						<form action="taskParam/listTaskParams.do" method="post" name="taskParamForm" id="taskParamForm">
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
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<!--  <th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>-->
									<th class="center" style="width:50px;">序号</th>
									<th class="center">任务类型</th>
									<th class="center">任务子类型</th>
									<th class="center">一审角色</th>
									<th class="center">二审角色</th>
									<th class="center">状态</th>
									<th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty taskParamList}">
									<c:forEach items="${taskParamList}" var="tp" varStatus="vs">
										<tr>
										<!-- 
											<td class='center' style="width: 30px;">
												<input type='checkbox' name='ids' value="${tp.P_ID }" id="${tp.P_ID }" class="ace"/><span class="lbl"></span>
											</td> -->
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${tp.TASK_TYPE }</td>
											<td class="center">${tp.TASK_TYPE_CHILD }</td>
											<td class="center">${tp.EXP_ONE_ROLE }</td>
											<td class="center">${tp.EXP_TWO_ROLE }</td>
											<td style="width: 60px;" class="center">
												<c:if test="${tp.STATUS == '0' }"><span class="label label-success arrowed">有效</span></c:if>
												<c:if test="${tp.STATUS == '1' }"><span class="label label-important arrowed-in">失效</span></c:if>
											</td>
											<td class="center">
												<div class="hidden-sm hidden-xs btn-group">
													<ts:rights code="taskParam/toEdit">
														<a class="btn btn-xs btn-success" title="编辑" onclick="edit('${tp.P_ID}');">
															<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
														</a>
													</ts:rights>
													<!--<ts:rights code="taskParam/delete">
														<a class="btn btn-xs btn-danger" onclick="del('${tp.P_ID }');">
															<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
														</a>
													</ts:rights>-->
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
							<td style="vertical-align:top;">
								<ts:rights code="taskParam/toAdd">
									<a class="btn btn-mini btn-success" onclick="add();">新增</a>
								</ts:rights>
								<!--  
								<ts:rights code="taskParam/deleteAll">
									<a title="批量删除" class="btn btn-mini btn-danger" onclick="deleteAll('确定要删除选中的数据吗?');" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
								</ts:rights>-->
							</td>
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
	$("#taskParamForm").submit();
}

function resetForm(){
	document.getElementById("taskParamForm").reset();
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

//新增
function add(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="新增";
	 diag.URL = '<%=path%>/taskParam/toAdd.do';
	 diag.Width = 469;
	 diag.Height = 320;
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

//修改
function edit(p_id){
 //判断能否更改，
 //1.若当前的状态为失效的状态 包含了有效的任务类型的配置则不能进行修改
 //2.若当前的配置包含任务信息记录，则不能修改
 $.ajax({
		type: "POST",
		url: '<%=basePath%>taskParam/editCan.do',
    	data: {P_ID:p_id,CTL_TYPE:"edit",tm:new Date().getTime()},
		dataType:'json',
		cache: false,
		success: function(data){
			 if("success" == data.result){
				 top.jzts();
				 var diag = new top.Dialog();
				 diag.Drag=true;
				 diag.Title ="修改";
				 diag.URL = '<%=path%>/taskParam/toEdit.do?P_ID='+p_id;
				 diag.Width = 469;
				 diag.Height = 320;
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
//删除
function del(p_id){
	bootbox.confirm("确定要删除吗?", function(result) {
		if(result) {
			//判断能否删除
			$.ajax({
				type: "POST",
				url: '<%=basePath%>taskParam/editCan.do',
		    	data: {P_ID:p_id,CTL_TYPE:"delete",tm:new Date().getTime()},
				dataType:'json',
				cache: false,
				success: function(data){
					 if("success" == data.result){
						top.jzts();
						var url = "<%=basePath%>taskParam/delete.do?P_ID="+p_id+"&tm="+new Date().getTime();
						$.get(url,function(data){
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
//批量操作
function deleteAll(msg){
	bootbox.confirm(msg, function(result) {
		if(result) {
			var str = '';
			var emstr = '';
			var phones = '';
			var username = '';
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
				if(msg == '确定要删除选中的数据吗?'){
					$.ajax({
						type: "POST",
						url: '<%=basePath%>taskParam/deleteAll.do?tm='+new Date().getTime(),
				    	data: {P_IDS:str},
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
		}
	});
}


</script>
</html>
