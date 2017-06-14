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
											<td style="width:100px;text-align: right;padding-top: 13px;">手术名称:</td>
											<td>
												<input type="hidden" name="O_CODE" id="O_CODE" value="${pd.O_CODE }"/>
												<input type="text" name="O_NAME" id="O_NAME" value="${pd.O_NAME }" style="width:88%;" readonly="readonly"/>
												<a onclick="selectCategory('100');">&nbsp;选择</a>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">手术药品名称:</td>
											<td>
												<input type="hidden" name="O_DRUG_CODE" id="O_DRUG_CODE" value="${pd.O_DRUG_CODE }"/>
												<input type="text" name="O_DRUG_NAME" id="O_DRUG_NAME" value="${pd.O_DRUG_NAME }" style="width:88%;" readonly="readonly"/>
												<a onclick="selectCategory('101');">&nbsp;选择</a>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">科室名称:</td>
											<td>
												<input type="hidden" name="O_DEPT_CODE" id="O_DEPT_CODE" value="${pd.O_DEPT_CODE }"/>
												<input type="text" name="O_DEPT_NAME" id="O_DEPT_NAME" value="${pd.O_DEPT_NAME }" style="width:88%;" readonly="readonly"/>
												<a onclick="selectCategory('102');">&nbsp;选择</a>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">医生名称:</td>
											<td>
												<input type="hidden" name="O_DOCTOR_CODE" id="O_DOCTOR_CODE" value="${pd.O_DOCTOR_CODE }"/>
												<input type="text" name="O_DOCTOR_NAME" id="O_DOCTOR_NAME" value="${pd.O_DOCTOR_NAME }" style="width:88%;" readonly="readonly"/>
												<a onclick="selectCategory('103');">&nbsp;选择</a>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">手术切口等级:</td>
											<td>
												<input type="text" name="O_LEVEL" id="O_LEVEL" value="${pd.O_LEVEL }" style="width:99%;"/>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">规格:</td>
											<td>
												<input type="text" name="O_DRUG_SPCE" id="O_DRUG_SPCE" value="${pd.O_DRUG_SPCE }" style="width:99%;"/>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">剂型:</td>
											<td>
												<input type="text" name="O_DRUG_FORM" id="O_DRUG_FORM" value="${pd.O_DRUG_FORM }" style="width:99%;"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
													<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
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
	//科室分类
	function selectCategory(type){
		top.jzts();
		var diag = new top.Dialog();
		diag.Drag=true;
		diag.URL = path + '/opDrug/selectData.do?ONTO_TYPE='+type;
		diag.Width = $(top.window).width();
		diag.Height = $(top.window).height();
		diag.Title ="选择信息";
		diag.CancelEvent = function(){ //关闭事件
			diag.close();
			//遮罩层控制，第三层弹窗使用
			parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		};
		diag.show();
	}
</script>
</html>