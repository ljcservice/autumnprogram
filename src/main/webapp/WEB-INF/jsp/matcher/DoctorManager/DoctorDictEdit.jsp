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
								<form action="BaseInfoManager/${MSG}DoctorDict.do" name="submintForm" id="submintForm" method="post">
									<div id="zhongxin" style="padding-top: 13px;">
									<input type="hidden" id="ID" name="ID" value="${entity.ID}">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:150px;text-align: right;padding-top: 13px;">医生代码:</td>
											<td><input type="text" name="emp_no" id="emp_no" value="${entity.emp_no }" maxlength="32" placeholder="输入医生代码" 
											 <c:if test="${MSG=='edit'}">  
											 	readonly="readonly" 
											 </c:if>
											 <c:if test="${MSG=='add'}">
											 	 onblur="hasDoctorCode()"
											 </c:if>
											 title="科室代码"  style="width:80%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">医生名称:</td>
											<td>
												<input type="text" name="name" id="name" value="${entity.name }"  maxlength="32" 
												 placeholder="输入医生名称" title="医生名称" style="width:80%;"/>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">科室:</td>
											<td>
												<select class="chosen-select form-control" name="dept_code" id="dept_code" data-placeholder="科室选择" style="vertical-align:top;width: 110px;">    
													<c:forEach items="${depts}" var="en" varStatus="vs" >
														<option <c:if test="${entity.dept_code == en.dept_code }">selected</c:if> value="${en.dept_code}">${en.dept_name}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">工作类别:</td>
											<td>
												<select  name="JOB" id="JOB" data-placeholder="工作类别" style="vertical-align:top;width: 100px;">    
														<c:forEach items="${JOBs}" var="en" varStatus="vs" >
															<option 
															<c:if test="${entity.JOB == en.name }">selected</c:if>  
															value="${en.name}" >${en.name}</option>  
														</c:forEach>
												</select>	
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">职称：</td>
											<td>
											 	<select  name="TITLE" id="TITLE" data-placeholder="职称" style="vertical-align:top;width: 90px;">    
														<c:forEach items="${TITLEs}" var="en" varStatus="vs" >
															<option 
																<c:if test="${entity.TITLE == en.name }">selected</c:if>
															value="${en.name}" >${en.name}</option>
														</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">输入码:</td>
											<td>
												<input type="text" name="INPUT_CODE" id="INPUT_CODE" value="${entity.INPUT_CODE }"  maxlength="32"   placeholder="输入大写输入码" title="输入码" style="width:80%;"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
												<c:if test="${MSG=='add'}">
<%-- 													<ts:rights code="user/add"> --%>
														<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
<%-- 													</ts:rights> --%>
												</c:if>
												<c:if test="${MSG=='edit'}">
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
	$(function() {
	
		//重置页面高度
// 		treeFrameT("treeFrame");
// 		function treeFrameT(obj){
// 			var hmainT = parent.document.getElementById(obj);
// 			hmainT.style.width = '100%';
// 			hmainT.style.height = ($("#main-container").height()) + 'px';
// 		}
		/*if($("#user_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}*/
		
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
	
	function searchs_Drug()
	{
		//top.jzts();
		var diag = new top.Dialog();
		diag.Drag = true;  
		diag.URL = '<%=path%>/drugUserAuth/queryByDrug.do?';
		diag.Width = $(top.window).width();
		diag.Height = $(top.window).height();
		diag.Title ="药品选择";
		diag.CancelEvent = function(){ //关闭事件
			diag.close();
			//alert("xxx");
			//遮罩层控制，第三层弹窗使用
			parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		};
		diag.show();
	}
	
	function searchs_Dept()
	{
		//top.jzts();
		var diag = new top.Dialog();
		diag.Drag = true;  
		diag.URL = '<%=path%>/drugUserAuth/queryByDrug.do?';
		diag.Width = $(top.window).width();
		diag.Height = $(top.window).height();
		diag.Title ="科室选择";
		diag.CancelEvent = function(){ //关闭事件
			diag.close();
			//alert("xxx");
			//遮罩层控制，第三层弹窗使用
			parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		};
		diag.show();
	}
	
	function searchs_Doctor()
	{
		//top.jzts();
		var diag = new top.Dialog();
		diag.Drag = true;  
		diag.URL = '<%=path%>/drugUserAuth/queryByDrug.do?';
		diag.Width = $(top.window).width();
		diag.Height = $(top.window).height();
		diag.Title ="医生选择";
		diag.CancelEvent = function(){ //关闭事件
			diag.close();
			//alert("xxx");
			//遮罩层控制，第三层弹窗使用
			parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		};
		diag.show();
	}
	
	function setDrugInfo(drug_no_local,drug_Name_Local,drug_spec,units,drug_form)
	{
		$("#DRUG_CODE").val(drug_no_local);
		$("#DRUG_NAME").val(drug_Name_Local);
		$("#DRUG_SPEC").val(drug_spec);
	}
	
	//保存
	function save(){
		var ret = true;
		
		if($("#emp_no").val()==""){
			$("#emp_no").tips({
				side:3,
	            msg:'请填写医生代码',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#emp_no").focus();
		}
		
		if($("#name").val()==""){
			$("#name").tips({
				side:3,
	            msg:'请填写医生名称',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#name").focus();
		}
		if($("#INPUT_CODE").val()==""){
			$("#INPUT_CODE").tips({
				side:3,
	            msg:'请填写输入码',
	            bg:'#AE81FF',
	            time:2  
	        });
			ret = false;
			$("#INPUT_CODE").focus();
		}
		if(!ret) return false;
		
		$("#submintForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
		
	}

	//判断科室代码是否存在
	function hasDoctorCode(){
		var DoctorCode = $.trim($("#emp_no").val());
		$.ajax({
			type: "POST",
			url: '<%=basePath%>BaseInfoManager/isValidDoctorCode.do',
	    	data: {DoctorCode:DoctorCode,tm:new Date().getTime()}, 
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					//$("#userForm").submit();
					//$("#zhongxin").hide();
					//$("#zhongxin2").show();
				 }else{
					 $("#emp_no").tips({
							side:3,
				            msg:'此参数编码已存在!',
				            bg:'#AE81FF',
				            time:2  
				        });
						// ret = false;
						$("#emp_no").focus();
					//$("#RULECODE").css("background-color","#D16E6C");
					//setTimeout("$('#RULECODE').val('此用户名已存在!')",500);
				 }
			}
		});
	}
	
</script>
</html>