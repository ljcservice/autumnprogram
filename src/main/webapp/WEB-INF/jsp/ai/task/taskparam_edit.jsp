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
								<form action="taskParam/${MSG }.do" name="taskParamForm" id="taskParamForm" method="post">
									<input type="hidden" name="P_ID" id="P_ID" value="${pd.P_ID }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:99px;text-align: right;padding-top: 13px;">任务类型:</td>
											<td>
												<select name="TASK_TYPE_ID" id="TASK_TYPE_ID" data-placeholder="请选择" style="vertical-align:top;width:78%;background-color: #F5F5F5;" onchange="">
													<c:forEach items="${taskTypeList}" var="var">
													<option value="${var.D_KEY }" <c:if test="${pd.TASK_TYPE_ID == var.D_KEY }">selected</c:if>>${var.D_VALUE}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:99px;text-align: right;padding-top: 13px;">任务子类型:</td>
											<td>
												<select onchange="changeTaskTypeChild(this)" name="TASK_TYPE_CHILD_ID" id="TASK_TYPE_CHILD_ID" data-placeholder="请选择" style="vertical-align:top;width:78%;background-color: #F5F5F5;" onchange="">
													<c:forEach items="${taskTypeChildList}" var="var">
													<option value="${var.D_KEY }" <c:if test="${pd.TASK_TYPE_CHILD_ID == var.D_KEY }">selected</c:if>> ${var.D_VALUE}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<c:if test="${pd.TASK_TYPE_CHILD_ID != 85002 }">
										<tr id="btn_div">
											<td style="width:79px;text-align: right;padding-top: 13px;">一审角色:</td>
											<td>
											<input type="hidden" name="EXP_ONE_ROLE" id="EXP_ONE_ROLE" value="${pd.EXP_ONE_ROLE }"  >
											<input type="text" name="EXP_ONE_ROLE_NAME" id="EXP_ONE_ROLE_NAME"  value="${pd.EXP_ONE_ROLE_NAME }"  maxlength="32" title="一审角色" style="width:78%;"/>
											<a class="btn btn-mini btn-success" onclick="selectOneRole();">选择角色</a>
											</td>
										</tr>
										</c:if>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">二审角色:</td>
											<td>
											<input type="hidden" name="EXP_TWO_ROLE" id="EXP_TWO_ROLE"  value="${pd.EXP_TWO_ROLE }">
											<input type="text" name="EXP_TWO_ROLE_NAME" id="EXP_TWO_ROLE_NAME"  value="${pd.EXP_TWO_ROLE_NAME }"  maxlength="32" title="二审角色" style="width:78%;"/>
											<a class="btn btn-mini btn-success" onclick="selectTwoRole();">选择角色</a>
											</td>
										</tr>
										<tr>
											<td style="width:99px;text-align: right;padding-top: 13px;">状态:</td>
											<td>
												<select name="STATUS" title="状态">
												<option value="0" <c:if test="${pd.STATUS == '0' }">selected</c:if> >有效</option>
												<option value="1" <c:if test="${pd.STATUS == '1' }">selected</c:if> >失效</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
												<c:if test="${MSG=='add'}">
													<ts:rights code="taskParam/add"><a class="btn btn-mini btn-primary" onclick="save();">保存</a></ts:rights>
												</c:if>
												<c:if test="${MSG=='edit'}">
													<ts:rights code="taskParam/edit"><a class="btn btn-mini btn-primary" onclick="save();">保存</a></ts:rights>
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
		if($("#TASK_TYPE_ID").val()==""){
			$("#TASK_TYPE_ID").tips({
				side:3,
	            msg:'输入任务类型',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TASK_TYPE_ID").focus();
			return false;
		}else{
			$("#TASK_TYPE_ID").val($.trim($("#TASK_TYPE_ID").val()));
		}
		if($("#TASK_TYPE_CHILD_ID").val()==""){
			$("#TASK_TYPE_CHILD_ID").tips({
				side:3,
	            msg:'输入任务子类型',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TASK_TYPE_CHILD_ID").focus();
			return false;
		}else{
			$("#TASK_TYPE_CHILD_ID").val($.trim($("#TASK_TYPE_CHILD_ID").val()));
		}
		if($("#EXP_ONE_ROLE_NAME").val()==""){
			if($("#TASK_TYPE_CHILD_ID").val()!=85002){
				$("#EXP_ONE_ROLE_NAME").tips({
					side:3,
		            msg:'输入一审角色',
		            bg:'#AE81FF',
		            time:3
		        });
				$("#EXP_ONE_ROLE_NAME").focus();
				return false;	 
			}
		}else{
			$("#EXP_ONE_ROLE_NAME").val($.trim($("#EXP_ONE_ROLE_NAME").val()));
		}
		if($("#EXP_TWO_ROLE_NAME").val()==""){
			$("#EXP_TWO_ROLE_NAME").tips({
				side:3,
	            msg:'输入二审角色',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#EXP_TWO_ROLE_NAME").focus();
			return false;
		}else{
			$("#EXP_TWO_ROLE_NAME").val($.trim($("#EXP_TWO_ROLE_NAME").val()));
		}
		var P_ID=$("#P_ID").val();
		if(P_ID==""){//新增需要校验类型是否已经配置
			has();
		}
		else{
			$("#TASK_TYPE_ID").removeAttr("disabled"); 
			$("#TASK_TYPE_CHILD_ID").removeAttr("disabled"); 
			$("#taskParamForm").submit();
		 	$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
	}
	//判断是否已经配置
	function has(){
		var TASK_TYPE_ID = $.trim($("#TASK_TYPE_ID").val());
		var TASK_TYPE_CHILD_ID = $.trim($("#TASK_TYPE_CHILD_ID").val());
		$.ajax({
			type: "POST",
			url: '<%=basePath%>taskParam/has.do',
	    	data: {TASK_TYPE_ID:TASK_TYPE_ID,TASK_TYPE_CHILD_ID:TASK_TYPE_CHILD_ID,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					 $("#taskParamForm").submit();
					 $("#zhongxin").hide();
					 $("#zhongxin2").show();
				 }else{
					$("#TASK_TYPE_CHILD_ID").css("background-color","#D16E6C");
					//setTimeout("$('#TASK_TYPE_CHILD_ID').val('此类型的配置已存在!')",500);
					$("#TASK_TYPE_CHILD_ID").tips({
						side:3,
			            msg:'此类型的配置已存在',
			            bg:'#AE81FF',
			            time:3
			        });
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
		if($("#P_ID").val()!=""){
			$("#TASK_TYPE_ID").attr("disabled","disabled");
			$("#TASK_TYPE_ID").css("color","gray");
			
			$("#TASK_TYPE_CHILD_ID").attr("disabled","disabled");
			$("#TASK_TYPE_CHILD_ID").css("color","gray");
			$("#TASK_TYPE_ID").tips({
				side:3,
	            msg:'任务类型不可更改',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TASK_TYPE_CHILD_ID").tips({
				side:3,
	            msg:'任务子类型不可更改',
	            bg:'#AE81FF',
	            time:3
	        });
			
		}
	});
	//选择一审角色
	function selectOneRole(){
		 top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="一审角色";
		 diag.URL = '<%=path%>/taskParam/selectRole.do';
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
		 	var roleinfo=diag.innerFrame.contentWindow.document.getElementById('ROLE_INFO').value;
			//document.getElementById('EXP_ONE_ROLE').value=;
			diag.close();
			if(roleinfo==null||roleinfo==""){
			}else{
				$("#EXP_ONE_ROLE").val(roleinfo.split("###")[0]);
				$("#EXP_ONE_ROLE_NAME").val(roleinfo.split("###")[1]);
			}
		 };
		 diag.show();
	}
	//选择二审角色
	function selectTwoRole(){
		top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="二审角色";
		 diag.URL = '<%=path%>/taskParam/selectRole.do';
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
		 	var roleinfo=diag.innerFrame.contentWindow.document.getElementById('ROLE_INFO').value;
			//document.getElementById('EXP_ONE_ROLE').value=;
			diag.close();
			if(roleinfo==null||roleinfo==""){
			}else{
				$("#EXP_TWO_ROLE").val(roleinfo.split("###")[0]);
				$("#EXP_TWO_ROLE_NAME").val(roleinfo.split("###")[1]);
			}
		 };
		 diag.show();
	}
	//变更任务的子类型
	function changeTaskTypeChild(obj){
		if(obj.value==85002)
		{
			$("#btn_div").hide();
		}
		else
		{
			$("#btn_div").show();
		}
	}
</script>
</html>