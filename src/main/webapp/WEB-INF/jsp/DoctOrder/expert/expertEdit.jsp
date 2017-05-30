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
								<form action="expert/${MSG }.do" name="userForm" id="userForm" method="post">
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">用户名 / 姓名:</td>
											<c:if test="${MSG=='add' }">
												<td>
												<select name="USER_ID"  style="width: 100%;">
													<c:forEach items="${userList}" var="user" varStatus="vs" >
														<option value="${user.user_id }" > ${user.username} / ${user.name}</option>
													</c:forEach>
												</select>
												</td>
											</c:if>
											<c:if test="${MSG=='edit' }">
												<td>
													<input type="hidden" name="USER_ID" id="user_id" value="${pd.USER_ID }"/>
													${pd.username} / ${pd.name}
												</td>
											</c:if>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">医嘱专家:</td>
											<td>
												<select name="IS_ORDERS" title="医嘱专家"  style="width: 100%;">
												<option value="0" <c:if test="${pd.IS_ORDERS == '0' }">selected</c:if> >否</option>
												<option value="1" <c:if test="${pd.IS_ORDERS == '1' }">selected</c:if> >是</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">处方专家:</td>
											<td>
												<select name="IS_RECIPE" title="处方专家"  style="width: 100%;">
												<option value="0" <c:if test="${pd.IS_RECIPE == '0' }">selected</c:if> >否</option>
												<option value="1" <c:if test="${pd.IS_RECIPE == '1' }">selected</c:if> >是</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">备注:</td>
											<td><input type="text" name="REMARK" id="BZ" value="${pd.REMARK }" placeholder="这里输入备注" maxlength="80" title="备注" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
												<c:if test="${MSG=='add'}">
													<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
												</c:if>
												<c:if test="${MSG=='edit'}">
													<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
												</c:if>
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
	//保存
	function save(){
		if($("#user_id").val()==""){
			$("#juese").tips({
				side:3,
	            msg:'选择一个用户',
	            bg:'#AE81FF',
	            time:2
	        });
			return false;
		}
		if($("#user_id").val()==""){
			hasU();
		}else{
			$("#userForm").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
	}
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
</script>
</html>