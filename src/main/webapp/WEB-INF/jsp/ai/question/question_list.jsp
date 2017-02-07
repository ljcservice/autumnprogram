<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ts" uri="/rights"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
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
					<div class="row" style="height:30%">
						<div class="col-xs-12" >

							<!-- 检索  -->
							<form action="queryQuestion/listQuestion.do" method="post" 
								name="questionForm" id="questionForm">
								<table style="margin-top: 5px;">
									<tr>
										<td>
											<div class="nav-search">
												<span class="input-icon"> <input
													class="nav-search-input" autocomplete="off"
													id="nav-search-input" type="text" name="keywords"
													value="${pd.keywords }" placeholder="这里输入关键词" /> <i
													class="ace-icon fa fa-search nav-search-icon"></i>
												</span>
											</div>
										</td>
										<td>
											<div class="nav-search" style="vertical-align:top;padding-left:2px;">
												<span class="input-icon"> <input
													class="nav-search-input" autocomplete="off"
													id="nav-search-input" type="text" name="FILE_NAME"
													value="${pd.FILE_NAME }" placeholder="导入文件名称" /> 
												</span>
											</div>
										</td>
										<td>
											<div class="nav-search" style="vertical-align:top;padding-left:2px;">
												<span class="input-icon"> <input
													class="nav-search-input" autocomplete="off"
													id="nav-search-input" type="text" name="BATCH_NUMBER"
													value="${pd.BATCH_NUMBER }" placeholder="批次号" /> 
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
										<td style="padding-left:2px;"><input class="span10 date-picker" name="createStart" id="createStart"  value="${pd.createStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:98px;" placeholder="导入开始日期" title="问题单导入开始"/></td>
										<td style="padding-left:2px;"><input class="span10 date-picker" name="createEnd" name="createEnd"  value="${pd.createEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:98px;" placeholder="导入结束日期" title="问题单导入结束"/></td>
										<td style="vertical-align:top;padding-left:2px;"> 
											<select class="chosen-select form-control" name="orders" id="orders" data-placeholder="排序方式"  style="vertical-align:top;width: 140px;">
												<option value=""></option>
												<option value="1" <c:if test="${pd.orders == 1 }">selected</c:if>>导入时间正序</option>
												<option value="2" <c:if test="${pd.orders == 2 }">selected</c:if>>导入时间倒序</option>
											</select>
										</td>
										<td style="vertical-align: top; padding-left: 2px;">
											<a  class="btn btn-light btn-xs" onclick="searchs();" title="检索"><i
												id="nav-search-icon"
												class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i>
											</a>
										</td>
									</tr>
								</table>
								<!-- 检索  -->

								<table id="simple-table"
									class="table table-striped table-bordered table-hover"
									style="margin-top: 5px;height:200px;">
									<thead>
										<tr>
											<th class="center" style="width: 50px;">序号</th>
											<td style="display: none;">原始ID</td>
											<td style="display: none;">问题单ID</td>
											<th class="center">原始诊断名称</th>
											<th class="center">原始诊断编码</th>
											<th class="center">NLP切词结果</th>
											<th class="center">MTS匹配结果</th>
											<th class="center">导入文件</th>
											<th class="center">批次号</th>
											<th class="center">状态</th>
											<th class="center">子状态</th>
											
										</tr>
									</thead>

									<tbody>

										<!-- 开始循环 -->
										<c:choose>
											<c:when test="${not empty qList}">
												<c:forEach items="${qList}" var="q" varStatus="vs">
													<tr ondblclick="q_info(this);">
														<td class='center' style="width: 30px;">${vs.index+1}</td>
														<td style="display: none;" class="center">${q.O_ID }</td>
														<td style="display: none;" class="center">${q.Q_ID }</td>
														<td class="center">${q.O_DIAG_NAME }</td>
														<td class="center">${q.O_DIAG_CODE }</td>
														<td class="center">${q.NLP_DIAG_NAME }</td>
														<td class="center">${q.MTS_DIAG_CODE }</td>
														<td class="center">${q.FILE_NAME }</td>
														<td class="center">${q.BATCH_NUMBER }</td>
														<td style="width: 60px;" class="center"><c:if
																test="${q.STATUS == '0' }">
																<span>初始</span>
															</c:if> <c:if test="${q.STATUS == '1' }">
																<span>一审</span>
															</c:if> <c:if test="${q.STATUS == '2' }">
																<span>NLP审核</span>
															</c:if> <c:if test="${q.STATUS == '3' }">
																<span>已分解</span>
															</c:if>
														</td>
														<!-- 设置子问题单的状态，还未分解则为"未开始"，已经分解则为"处理中"，子问题单的数量与处理结果的数量相同则"已完成" -->
														<td class="center"  style="width: 80px;">
															<c:if test="${q.STATUS == '2' }">
																<span>未开始</span>
															</c:if>
															<c:if test="${q.STATUS == '3' and q.CHILD_COUNT != q.RS_COUNT}">
																<span>处理中</span>
															</c:if>
															<c:if test="${q.STATUS == '3' and q.CHILD_COUNT == q.RS_COUNT}">
																<span>已完成</span>
															</c:if>
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
									<table style="width: 100%;">
										<tr>
											<!-- <td>
												<ts:rights code="taskParam/deleteAll">
													<a title="MTS验证" class="btn btn-mini btn-danger" onclick="MTSCheck('确定要调用MTS做验证么?');" >MTS验证</a>
												</ts:rights>
											</td> -->
											<td style="vertical-align: top;"><div class="pagination"
													style="float: right; padding-top: 0px; margin-top: 0px;">${page.pageStr}</div></td>
										</tr>
									</table>
								</div>
							</form>
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
//列表的双击事件
function q_info(tr_obj){
	 top.jzts();
	 var q_id=tr_obj.children[2].innerHTML;//问题单的ID
	 var o_diag_name=tr_obj.children[3].innerHTML;//原始诊断名称
	 var nlp_diag_name=tr_obj.children[5].innerHTML;//NLP切词结果
	 var mts_code=tr_obj.children[6].innerHTML;//MTS匹配编码
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="问题单信息";
	 diag.URL = '<%=path%>/queryQuestion/qInfo.do?q_id='+q_id+"&o_diag_name="+o_diag_name+"&nlp_diag_name="+nlp_diag_name+"&mts_code="+mts_code+"&tm="+new Date().getTime();
	 diag.Width = 1100;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}	
</script>
</html>
