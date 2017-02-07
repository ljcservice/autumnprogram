<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ts" uri="/rights"%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
<!-- <link href="static/ace/css/bootstrap.min.css" rel="stylesheet" media="screen"> -->
<link href="static/css/progress.css" rel="stylesheet" media="screen">
<style>
</style>
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row" style="height:30%">
						<div class="col-xs-12" >
							<!-- 检索  -->
							<div>
								<form action="checkQuestion/listQuestion.do" method="post" 	name="questionForm" id="questionForm">
								<table style="margin-top: 5px;">
									<tr>
										<td>
											<div class="nav-search" style="vertical-align:top;padding-left:2px;">
												<span class="input-icon"> 批次号：<input
													class="nav-search-input" autocomplete="off"
													id="nav-search-input" type="text" name="BATCH_NUMBER"
													value="${pd.BATCH_NUMBER }" placeholder="批次号" /> 
												</span>
											</div>
										</td>
										<td style="vertical-align:top;padding-left:10px;"> 
											数据来源：
											<select class="chosen-select form-control" name="ORIGIN_ID" id="ORIGIN_ID" data-placeholder="数据来源"  style="vertical-align:top;width: 120px;">
												<c:forEach items="${originList}" var="var">
													<option value="${var.D_KEY }" <c:if test="${pd.ORIGIN_ID == var.D_KEY }">selected</c:if>>${var.D_VALUE}</option>
												</c:forEach>
											</select>
										</td>
										<td style="vertical-align: top; padding-left: 10px;"><a
											class="btn btn-light btn-xs" onclick="searchs();" title="检索"><i
												id="nav-search-icon"
												class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
									</tr>
								</table>
								</form>
							</div>
							<!-- 按钮和提示  -->
							<div  style="margin: 10px 0px 0px 0px;height: auto;">
								<div id="progress" class="progress progress-striped progress-success active" style="width: 400px;display: none;">
									<div style="width: 1%;" class="bar"></div>
								</div>
								<div>
									<c:if test="${ RELOADING == 0 and  CHECKING == 0 }">
									<div id="mybtn" style="width: 180px;height: 30px;float: left;">
											<ts:rights code="taskParam/deleteAll">
												<a id="refreshBtn" title="刷新MTS数据" class="btn btn-mini btn-primary" onclick="refreshMTS();" >刷新MTS数据</a>
											</ts:rights>
										    <ts:rights code="taskParam/deleteAll">
												<a id="checkBtn" title="MTS验证" class="btn btn-mini btn-success" onclick="MTSCheck('确定要调用MTS做验证么?');" >MTS验证</a>
											</ts:rights>
									</div>
									</c:if>
									<div style="height: 30px;float: left;padding-top: 5px;">
										<img id="mtsRefresh" <c:if test="${ RELOADING == 0}"> style="display:none"</c:if> width="30px" height="30px;" src="static/images/jzx.gif" />
										<c:if test="${ RELOADING == 1}">
											<span style="color: #ffb752">MTS正在刷新中，可能会需要等待2-5分钟左右,请稍后刷新页面重试。</span>
										</c:if>
										<span style="color: #ffb752">温馨提示：MTS最新数据时间：<fmt:formatDate value="${update_time}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></span>
									</div>
								</div>
							</div>
							<div style="margin-top: 20px;">
								<table id="simple-table"
									class="table table-striped table-bordered table-hover" 
									style="margin-top: 5px;">
									<thead>
										<tr>
											<th class="center" style="width:35px;">
											</th>
											<th class="center" style="width: 50px;">序号</th>
											<th class="center">批次号码</th>
											<th class="center">问题单数量</th>
										</tr>
									</thead>

									<tbody>

										<!-- 开始循环 -->
										<c:choose>
											<c:when test="${not empty qList}">
												<c:forEach items="${qList}" var="q" varStatus="vs">
													<tr ondblclick="q_info(this);">
														<td class='center' style="width: 30px;">
															<input <c:if test="${q.checking==1}"> disabled="disabled"</c:if> type="radio" name='ids' value="${q.batch_number }" class="ace"/><span class="lbl"></span>
														</td>
														<td class='center' style="width: 30px;">${vs.index+1}</td>
														<td class="center">${q.batch_number }</td>
														<td class="center">${q.q_count }</td>
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
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
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
	$("#questionForm").submit();
}

function resetForm(){
	document.getElementById("questionForm").reset();
}

$(function() {
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
	//正在验证中，执行定时刷新进度条
	if(1==${CHECKING}){
		queryMTSCheck(true);
	}
	if(1==${RELOADING}){
		//mts刷新中
		setTimeout(function(){
			window.location.href = window.location.href ;
		},60*1000);
	}
});
//列表的双击事件
function q_info(tr_obj){
	 top.jzts();
	 var batch_no=tr_obj.children[2].innerHTML;//批次号
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="问题单信息";
	 diag.URL = '${path}/queryQuestion/listQuestion.do?queryType=1&BATCH_NUMBER='+batch_no+"&tm="+new Date().getTime();
	 diag.Width = 1100;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}
//刷新MTS
function refreshMTS(){
	var msg ="确定刷新MTS数据吗？可能会需要等待2-5分钟左右。";
	bootbox.confirm(msg, function(result) {
		if(result) {
			$("#mybtn").hide();
			$("#mtsRefresh").show();
			$.ajax({
				type: "POST",
				url: '${basePath}checkQuestion/refreshMTS.do?tm='+new Date().getTime(),
				dataType:'json',
				async: true,
				cache: false,
				time:600000,
				success: function(data){
					if(data.result == 'success'){
						window.location.href = window.location.href ;
					}
				}
			});
		}
	});
}

//调用MTS校验,问题单 的原始数据的ID
function MTSCheck(msg){
	var c = $("input[name=ids]:checked");
	if(c.length!=1){
		$("#simple-table tbody").children().first().children().first().children().first().tips({
			side:2,
            msg:'请勾选一条数据',
            bg:'#AE81FF',
            time:2
        });
        return false;
	}
	bootbox.confirm(msg, function(result) {
		if(result) {
			$("#mybtn").hide();
			stopFlag = false;
			$.ajax({
				type: "POST",
				url: '${basePath}checkQuestion/MTSCheck.do?tm='+new Date().getTime(),
		    	data: {batch_number:c.eq(0).val(),ORIGIN_ID:$("#ORIGIN_ID").val()},
				dataType:'json',
				async: true,
				cache: false,
				success: function(data){
					stopFlag = true;
					$("#mybtn").show();
					if(data.result == 'success'){
						$(".bar").width("100%");
						var ss = bootbox.dialog({
							message: "<span class='bigger-110'>"+data.msg+"</span>",
							buttons: 			
							{ "button":
								{ 
									"label":"确定", "className":"btn-sm btn-success",callback: function(){window.location.href = window.location.href ;}
								}
							},
							onEscape: function() {window.location.href = window.location.href ;}
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
			queryMTSCheck(false);
		}
	});
}
var stopFlag = false;
//查询MTS校验进度
function queryMTSCheck(refresh){
	if(stopFlag){
		//停止进度条,重置进度条
		$(".progress").hide();
		$(".bar").width("1%");
		return;
	}
	$("#progress").show();
	$.ajax({
		type: "POST",
		url: '${basePath}checkQuestion/queryMTSCheck.do?tm='+new Date().getTime(),
		dataType:'json',
		async: true,
		cache: false,
		success: function(data){
			if(data.result == 'success'){
				//更新进度条
				if(data.progress == 0){
					//继续验证
					setTimeout(function(){queryMTSCheck(refresh);},2000);
				}else if(data.progress < 100){
					//验证完成，关闭进度条，显示验证按钮
					$(".bar").width(data.progress+"%");
					//继续验证
					setTimeout(function(){queryMTSCheck(refresh);},2000);
				}else if(data.progress >= 100){
					//验证完成，刷新页面
					if(refresh){
						window.location.href = window.location.href ;
					}
				}
			}
		},
		error:function (XMLHttpRequest, textStatus, errorThrown) {
		 	alert('网络异常，请稍后重试');
		}
	});
	
}
</script>
</html>
