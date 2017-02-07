<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
								<form action="taskQuery/saveDiagRs.do" name="tycForm" id="tycForm" method="post">
									<input type="hidden" name="TASK_ID" id="TASK_ID" value="${pd.TASK_ID }"/>
									<input type="hidden" name="STEP" id="STEP" value="${pd.STEP }"/>
									<input type="hidden" name="Q_ID" id="Q_ID" value="${pd.Q_ID }"/>
									<input type="hidden" name="DIS_TYPE" id="DIS_TYPE" value="1"/><!-- 处理类型 同义词 -->
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:99px;text-align: right;padding-top: 13px;">诊断术语:</td>
											<td>
												<input type="text" name="DIAG_NAME" id="DIAG_NAME"  value="${pd.DIAG_NAME }" readonly="readonly"  maxlength="32" title="诊断术语" style="width:78%;"/>
											</td>
										</tr>
										<tr>
											<td style="width:99px;text-align: right;padding-top: 13px;">术语类型:</td>
											<td>
												<select name="TERM_TYPE" id="TERM_TYPE" data-placeholder="请选择" style="vertical-align:top;width:78%;background-color: #F5F5F5;" onchange="">
													<c:forEach items="${syTypeList}" var="var">
													<option value="${var.D_KEY }" <c:if test="${pd.term_type == var.D_KEY }">selected</c:if>> ${var.D_VALUE}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:99px;text-align: right;padding-top: 13px;">标准术语:</td>
											<td>
												<input type="text" name="STAD_CN" id="STAD_CN"  value="${pd.STAD_CN }" readonly="readonly" maxlength="32" title="标准术语" style="width:78%;"/>
												<input type="hidden" name="STD_ID" id="STD_ID" value="${pd.STD_ID }"/>
											</td>
										</tr>
										<tr>
											<td style="width:99px;text-align: right;padding-top: 13px;">同义词类型:</td>
											<td>
												<select name="SYN_TYPE" id="SYN_TYPE" data-placeholder="请选择" style="vertical-align:top;width:78%;background-color: #F5F5F5;" onchange="">
													<c:forEach items="${tycTypeList}" var="var">
													<option value="${var.D_KEY }" <c:if test="${pd.SYN_TYPE == var.D_KEY }">selected</c:if>> ${var.D_VALUE}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
												<a class="btn btn-mini btn-primary" onclick="saveNonTerm();">确认</a>
												<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											</td>
										</tr>
									</table>
									</div>
									<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript">
	$(top.hangge());
	$(document).ready(function(){
		
	});
	$(function() {
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
	});
	//保存无法干预
	function saveNonTerm(){
		//校验信息
		$("#tycForm").submit();
	 	$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
</script>
</html>