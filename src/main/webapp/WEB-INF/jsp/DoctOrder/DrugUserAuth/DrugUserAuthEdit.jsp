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
<body class="no-skin" >
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
								<form action="drugUserAuth/${MSG}DrugUserAuth.do" name="DrugUserAuthForm" id="DrugUserAuthForm" method="post">
									<div id="zhongxin" style="padding-top: 13px;">  
									<input type="hidden" id="ID" name="ID" value="${entity.ID}">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">药品名称:</td>
											<td>
												<input type="hidden" name="DRUG_CODE" id="DRUG_CODE" value="${entity.DRUG_CODE }" />
												<input type="hidden" name="DRUG_FORM" id="DRUG_FORM" value="${entity.DRUG_FORM }" />
												<input type="text" name="DRUG_NAME" id="DRUG_NAME" value="${entity.DRUG_NAME }" maxlength="32" placeholder="这里输入参数编码" readonly="readonly"	 title="药品名称" style="width:80%;"  />
												<a class="btn btn-light btn-xs" onclick="searchs_Drug();" title="检索"  id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>	
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">药品规格:</td>
											<td><input type="text" name="DRUG_SPEC" id="DRUG_SPEC" value="${entity.DRUG_SPEC }" maxlength="32" placeholder="这里输入参数名称"  readonly="readonly" title="药品规格"  style="width:80%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">科室名称:</td>
											<td>
											
											<c:if test="${MSG=='edit'}">
												<input type="hidden" name="DEPT_CODE" id="DEPT_CODE" value="${entity.DEPT_CODE }" />
												<input type="text" name="DEPT_NAME" id="DEPT_NAME" value="${entity.DEPT_NAME }" maxlength="32"   readonly="readonly" title="科室名称"  style="width:80%;"/>
											</c:if>
											
											<c:if test="${MSG=='add'}">
												<select class="chosen-select form-control"  name="DEPT_CODE" id="DEPT_CODE"  onchange="javascript:findDoctorName()" data-placeholder="选择科室" style="vertical-align:top;" >    
										 			<option value=""></option>
<!-- 										 			<option value="*" -->
<%-- 										 				<c:if test="${entity.DEPT_CODE == '*' }">selected</c:if> >*</option> --%>
													<c:forEach items="${depts}" var="en" varStatus="vs" >
														<option <c:if test="${entity.DEPT_CODE == en.dept_code }">selected</c:if> value="${en.dept_code}" >${en.dept_name}</option>
													</c:forEach>
												</select>
												<input type="hidden" value="" name="DEPT_NAME" id="DEPT_NAME">
											</c:if>
<%-- 												<input type="text" name="RULEVALUE" id="RULEVALUE" value="${entity.RULEVALUE }"  maxlength="32" readonly="readonly" placeholder="输入参数值" title="科室名称" style="width:80%;"/> --%>
<!-- 												<a class="btn btn-light btn-xs" onclick="searchs();" title="检索"  id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a> -->
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">医生名称:</td>
											<td>
											<c:if test="${MSG=='edit'}">
												<input type="text" name="DOCTOR_NAME" id="DOCTOR_NAME" value="${entity.DOCTOR_NAME }" maxlength="32"   readonly="readonly" title="医生名称"  style="width:80%;"/>
											</c:if>
											
											<c:if test="${MSG=='add'}">
												<select class="chosen-select form-control" name="DOCTOR_NAME" id="DOCTOR_NAME" data-placeholder="选择医生" style="vertical-align:top;"	> 
										 			<option value=""></option>
<!-- 										 			<option value="*" >*</option> -->
<%-- 													<c:forEach items="${doctors}" var="entity" varStatus="vs" > --%>
<%-- 														<option <c:if test="${pd.DOCTOR_NAME == entity.name }">selected</c:if> value="${entity.name}" >${entity.name}</option> --%>
<%-- 													</c:forEach> --%>
												</select>
											</c:if>
<%-- 												<input type="text" name="RULEVALUE" id="RULEVALUE" value="${entity.RULEVALUE }"  maxlength="32" readonly="readonly"  placeholder="输入参数值" title="医生名称" style="width:80%;"/> --%>
<!-- 												<a class="btn btn-light btn-xs" onclick="searchs();" title="检索"  id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a> -->
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">控制类型:</td>
											<td>
												<select name="CONTROL_TYPE" id="CONTROL_TYPE" >
												
													<c:forEach items="${CONTROLTYPEs}" var="en" varStatus="vs" >
														<option <c:if test="${entity.CONTROL_TYPE == en.code }">selected</c:if> value="${en.code}" >${en.name}</option>
													</c:forEach>
													
												</select>	
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">上线值(包装):</td>
											<td>
												<input 
													onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
												type="text" name="T_VALUE" id="T_VALUE" value="${entity.getT_VALUE() }"  maxlength="32" placeholder="输入参数值" title="上线值" style="width:80%;"/>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">当前累计值(包装):</td>
											<td>
												<input 
													onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
												type="text" name="TOTAL_VALUE" id="TOTAL_VALUE" 
												<c:if test="${MSG=='add'}">
													readonly="readonly" 
												</c:if>
												value="${entity.getTOTAL_VALUE() }"  maxlength="32" placeholder="" title="当前累计值" style="width:80%;"/>
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
	
		$('[data-rel=tooltip]').tooltip();
		$('[data-rel=popover]').popover({html:true}); 
		
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
		};  
		// findDoctorName();
	});
	
	window.onload = function(){
		
	}
	
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
		$("#DRUG_FORM").val(drug_form);
	}
	
	var msg = '${MSG}';
	
	
	//保存
	function save(){
		var ret = true;
		
		if($("#DRUG_CODE").val()==""){
			$("#DRUG_NAME").tips({
				side:3,
	            msg:'请选择药品',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#DRUG_NAME").focus();
		}
		
		if($("#DRUG_NAME").val()==""){
			$("#DRUG_NAME").tips({
				side:3,
	            msg:'请选择药品',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#DRUG_NAME").focus();
		}
		
		if($("#DRUG_SPEC").val()==""){
			$("#DRUG_SPEC").tips({
				side:3,
	            msg:'请选择药品',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#DRUG_NAME").focus();
		}
		
		if($("#DEPT_CODE").val()==""){
			$("#DEPT_CODE_chosen").tips({
				side:3,
	            msg:'请选择科室',
	            bg:'#AE81FF',
	            time:2
	        });
			ret = false;
			$("#DEPT_CODE").focus();
		}
		
		if($("#DOCTOR_NAME").val()==""){
			$("#DOCTOR_NAME_chosen").tips({
				side:3,
	            msg:'请选择医生',
	            bg:'#AE81FF',
	            time:2  
	        });
			ret = false;
			$("#DOCTOR_NAME").focus();
		}
		
		if($("#CONTROL_TYPE").val()==""){
			$("#CONTROL_TYPE").tips({
				side:3,
	            msg:'请选择类型',
	            bg:'#AE81FF',
	            time:2  
	        });
			ret = false;
			$("#CONTROL_TYPE").focus();
		}
		
		if($("#T_VALUE").val()==""){
			$("#T_VALUE").tips({
				side:3,
	            msg:'请上线值(包装)',
	            bg:'#AE81FF',
	            time:2  
	        });
			ret = false;
			$("#T_VALUE").focus();
		}
		
		
		if(!hasDrugUserAuth())
		{
			 $("#DRUG_NAME").tips({
					side:3,
		            msg:'添加[药品,科室,医生,控制类型]已经存在,请重新填写!',
		            bg:'#AE81FF',
		            time:2   
		        });
				$("#DRUG_NAME").focus();
			ret = false;
		}
		
		if(!ret) return false;
		
		$("#DrugUserAuthForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
		
	}

	function findDoctorName()
	{
		var DEPTCODE = $.trim($("#DEPT_CODE").val());
		var deptname = $.trim($("#DEPT_CODE").find("option:selected").text());
		$("#DEPT_NAME").val(deptname);
		var doctorName = '${entity.DOCTOR_NAME}';
		if(DEPTCODE == '') return ;
		$("#DOCTOR_NAME").empty(); 
//			 alert(JSON.stringify(data.entitys));   JSON.parse()  
		$("#DOCTOR_NAME").append("<option value=\"\"></option>");
// 		$("#DOCTOR_NAME").append("<option " +  (doctorName == '*' ?"selected":"") + " value=\"*\">*</option>");
		if(DEPTCODE == '*')
		{
			$("#DOCTOR_NAME").get(0).options[1].selected = true;
		}
		else 
		{
			$.ajax({
				type: "POST",
				url: '<%=basePath%>drugUserAuth/findDoctorName.do',
		    	data: {DEPT_CODE:DEPTCODE,tm:new Date().getTime()},
				dataType:'json',
				cache: false,
				success: function(data){
					 if("success" == data.result){
							$.each(data.entitys, function(i, str){
								$("#DOCTOR_NAME").append("<option " +  (doctorName == str['NAME'] ?"selected":"") + "  value=\"" + str['NAME'] + "\">"+ str['NAME'] +"|" + str['JOB'] +"|" + str['TITLE'] + "</option>");  
							});
							
							$("#DOCTOR_NAME").chosen('destroy');
							$('#DOCTOR_NAME').chosen({allow_single_deselect:true}); 
					 }else{
// 						 $("#RULECODE").tips({
// 								side:3,
// 					            msg:'此参数编码已存在!',
// 					            bg:'#AE81FF',
// 					            time:2  
// 					        });
// 							// ret = false;
// 							$("#RULECODE").focus();
					 }
				}
			});
		}
		$("#DOCTOR_NAME").chosen('destroy');
		$('#DOCTOR_NAME').chosen({allow_single_deselect:true});
	}
	
	//判断用户名是否存在
	function hasDrugUserAuth(){
		var DRUG_CODE = $.trim($("#DRUG_CODE").val());
		var DRUG_NAME = $.trim($("#DRUG_NAME").val());
		var DRUG_FORM = $.trim($("#DRUG_FORM").val());
		var DRUG_SPEC = $.trim($("#DRUG_SPEC").val());
		var DEPT_NAME = $.trim($("#DEPT_NAME").val());
		var DOCTOR_NAME = $.trim($("#DOCTOR_NAME").val());
		var CONTROL_TYPE = $.trim($("#CONTROL_TYPE").val());
		var ID = $.trim($("#ID").val());
		var rs = true;
		$.ajax({  
			type:"POST",
			async:false,
			url:'<%=basePath%>drugUserAuth/hasDrugUserAuth.do',
	    	data:{ID:ID,DRUG_CODE:DRUG_CODE,DRUG_NAME:DRUG_NAME,DRUG_FORM:DRUG_FORM,DRUG_SPEC:DRUG_SPEC,DEPT_NAME:DEPT_NAME,DOCTOR_NAME:DOCTOR_NAME,CONTROL_TYPE:CONTROL_TYPE,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					 rs = true;
				 }else{
					 rs = false ;
				 }
			}
		});
		return rs;
	}
	
</script>
</html>