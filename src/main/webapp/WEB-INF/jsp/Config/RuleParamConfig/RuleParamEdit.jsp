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
<%@ include file="../../system/index/top.jsp" %>   
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
								<form action="RuleParam/${MSG}RuleParam.do" name="ruleForm" id="ruleForm" method="post">
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">参数编码:</td>
											<td><input type="text" name="RULECODE" id="RULECODE" value="${pd.RULECODE }" maxlength="32" placeholder="这里输入参数编码" 
												 <c:if test="${MSG == 'add' }">
												 	onblur="hasRuleCode('${pd.RULECODE }')"
												 </c:if>
												 <c:if test="${MSG=='update' }">
												 	readonly="readonly"
												 </c:if>
												 title="参数编码" style="width:98%;"  /></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">参数名称:</td>
											<td><input type="text" name="RULENAME" id="RULENAME" value="${pd.RULENAME }" maxlength="32" placeholder="这里输入参数名称" title="参数名称"  style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">参数值:</td>
											<td><input type="text" name="RULEVALUE" id="RULEVALUE" value="${pd.RULEVALUE }"  placeholder="输入参数值" title="参数值" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">参数描述:</td>
											<td>
												<textarea  name="RULEDESC" id="RULEDESC" rows="4" cols="40"   placeholder="这里输入参数描述" title="参数描述"  style="width:98%;" >${pd.RULEDESC }</textarea>
											</td>
										</tr>
										
										<tr>
											<td style="text-align: center;" colspan="10">
												<c:if test="${MSG=='add'}">
<%-- 													<ts:rights code="user/add"> --%>
														<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
<%-- 													</ts:rights> --%>
												</c:if>
												<c:if test="${MSG=='update'}">
<%-- 													<ts:rights code="user/edit"> --%>
														<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
<%-- 													</ts:rights> --%>
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
	<%@ include file="../../system/index/foot.jsp"%>
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
	
		//重置页面高度
		treeFrameT("treeFrame");
		function treeFrameT(obj){
			var hmainT = parent.document.getElementById(obj);
			hmainT.style.width = '100%';
			hmainT.style.height = ($("#main-container").height()) + 'px';
		}
		/*if($("#user_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}*/
	});
	//保存
	function save(){
		var ret = true;
		
		if($("#RULEVALUE").val()==""){
			$("#RULEVALUE").tips({
				side:3,
	            msg:'请填写参数值',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#RULENAME").focus();
		}
		
		if($("#RULENAME").val()==""){
			$("#RULENAME").tips({
				side:3,
	            msg:'请填写参数名称',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#RULENAME").focus();
		}
		if($("#RULECODE").val()==""){
			$("#RULECODE").tips({
				side:3,
	            msg:'请填写参数编码',
	            bg:'#AE81FF',
	            time:2  
	        });
			ret = false;
			$("#RULECODE").focus();
		}
		if(!ret) return false;
		
		$("#ruleForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
		
	}

	//判断用户名是否存在
	function hasRuleCode(){
		var RULECODE = $.trim($("#RULECODE").val());
		$.ajax({
			type: "POST",
			url: '<%=basePath%>RuleParam/isValidRuleCode.do',
	    	data: {RULECODE:RULECODE,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					//$("#userForm").submit();
					//$("#zhongxin").hide();
					//$("#zhongxin2").show();
				 }else{
					 $("#RULECODE").tips({
							side:3,
				            msg:'此参数编码已存在!',
				            bg:'#AE81FF',
				            time:2  
				        });
						// ret = false;
						$("#RULECODE").focus();
					//$("#RULECODE").css("background-color","#D16E6C");
					//setTimeout("$('#RULECODE').val('此用户名已存在!')",500);
				 }
			}
		});
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