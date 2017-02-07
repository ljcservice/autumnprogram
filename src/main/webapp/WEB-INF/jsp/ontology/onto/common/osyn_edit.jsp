<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<style>
.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
	padding: 4px;
}
</style>
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
								<form action="common/${msg}.do" name="osynForm" id="osynForm" method="post">
									<input type="hidden" name="DN_ID" id="DN_ID" value="${pd.DN_ID }"/>
									<input type="hidden" name="ontoType" id="ontoType" value="${ontoType}" />
									<input type="hidden" name="stad_dn_id" id="stad_dn_id" value="${pd.stad_dn_id}" />
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="text-align: center;padding: 10px;" colspan="2">
													<c:if test="${ontoType==10301}">诊断通用名</c:if>
													<c:if test="${ontoType==10501}">手术通用名</c:if>
													<c:if test="${ontoType==10502}">科室通用名</c:if>
											</td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">术语名称-中文:</td>
											<td><input type="text" name="DN_CHN" id="DN_CHN" maxlength="80" placeholder="输入术语名称中文" value="${pd.DN_CHN }" style="width:98%;" onblur="checkName(this,'DN_CHN');"/></td>
										</tr>
										<c:if test="${ontoType==10301 || ontoType==10501}">
										<tr>
											<td style="text-align: right;padding: 10px;">术语名称-英文:</td>
											<td><input type="text" name="DN_ENG" id="DN_ENG"  maxlength="80" placeholder="输入术语名称英文" value="${pd.DN_ENG }" style="width:98%;" /></td>
										</tr>
										</c:if>
										<c:if test="ontoType = '51005'">
										<tr>
											<td style="text-align: right;padding: 10px;">术语类型:</td>
											<td>
												<select class=" form-control" name="TERM_TYPE" id="TERM_TYPE" data-placeholder="术语类型" style="vertical-align:top;">
													<option value=""></option>
													<option value="1" <c:if test="${pd.TERM_TYPE==1}">selected</c:if>>症状</option>
													<option value="2" <c:if test="${pd.TERM_TYPE==2}">selected</c:if>>疾病</option>
												</select>
											</td>
										</tr>
										</c:if>
										<tr>
											<td style="text-align: right;padding: 10px;">同义词类型:</td>
											<td>
												<select class=" form-control" name="SYNO_TYPE" id="SYNO_TYPE" data-placeholder="同义词类型" style="vertical-align:top;">
													<option value=""></option>
													<option value="23101" <c:if test="${pd.SYNO_TYPE==23101}">selected</c:if>>俗语</option>
													<option value="23102" <c:if test="${pd.SYNO_TYPE==23102}">selected</c:if>>缩略语</option>
													<option value="23103" <c:if test="${pd.SYNO_TYPE==23103}">selected</c:if>>同音字/错别字</option>
													<option value="23104" <c:if test="${pd.SYNO_TYPE==23104}">selected</c:if>>拼音首字母</option>
													<option value="23105" <c:if test="${pd.SYNO_TYPE==23105}">selected</c:if>>语用同义词</option>
													<option value="23106" <c:if test="${pd.SYNO_TYPE==23106}">selected</c:if>>专指同义词</option>
													<option value="23107" <c:if test="${pd.SYNO_TYPE==23107}">selected</c:if>>其它</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">标准术语名称:</td>
											<td><input type="text" name="standard_name"  value="${pd.standard_name }" readonly="readonly" style="width:98%;" maxlength="80" /></td>
										</tr>
										<c:if test="${ontoType==10301 || ontoType==10501}">
										<tr>
											<td style="text-align: right;padding: 10px;">术语来源名称-中文:</td>
											<td><input type="text" name="ORG_DN_CHN" id="ORG_DN_CHN" value="${pd.ORG_DN_CHN }" placeholder="输入术语来源名称-中文" maxlength="80" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">术语来源名称-英文:</td>
											<td>
												<input type="email" name="ORG_DN_ENG" id="ORG_DN_ENG"  value="${pd.ORG_DN_ENG }" maxlength="80" placeholder="输入术语来源名称-英文" style="width:98%;"/>
											</td>
										</tr>
										</c:if>
										<tr>
											<td style="text-align: right;padding: 10px;">停用标记:</td>
											<td style="vertical-align: middle;">
												<label><input class="ace" id="u3897_input" value="0" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==0 || pd.IS_DISABLE == null}">checked</c:if> >否&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
												<label><input class="ace" id="u3897_input" value="1" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==1 }">checked</c:if> >是&nbsp;<span class="lbl"></span></label>
											</td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">停用描述:</td>
											<td>
												<input type="text" name="DESCRIPTION" id="DESCRIPTION" value="${pd.DESCRIPTION }" placeholder="若为停用状态请在此添加说明" maxlength="80" title="停用描述" style="width:98%;"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;padding: 7px;" colspan="2">
												<a class="btn btn-mini btn-primary" onclick="add();">保存</a>
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
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
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

		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}
});
function add(){
	if(!check()){return;}
	save();
}
//保存
function save(){
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	$.ajax({
		type: "POST",
		url: basePath+$("#osynForm").attr("action")+'?tm='+new Date().getTime(),
    	data: $("#osynForm").serialize(),
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			if(data.result=="success"){
				document.getElementById("zhongxin").style.display = 'none';
				top.Dialog.close();
			}else{
				bootbox.dialog({
					message: "<span class='bigger-110'>"+data.result+"</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				$("#zhongxin").show();
				$("#zhongxin2").hide();
			}
		}
	});
}
function check(){
	var ontoType=$("#ontoType").val();
	var flag = true;
	if($("#DN_CHN").val()==null ||$.trim($("#DN_CHN").val())=="" ){
		$("#DN_CHN").tips({ side:3, msg:'输入名称中文', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if(ontoType=='10301'){
		if($("#TERM_TYPE").val()==""){
			$("#TERM_TYPE").tips({ side:3, msg:'请输入来术语类型', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	if(ontoType=='10301' || ontoType=='10501' ){
		if($("#ORG_DN_CHN").val()==null ||$.trim($("#ORG_DN_CHN").val())=="" ){
			$("#ORG_DN_CHN").tips({ side:3, msg:'请输入来源名称中文', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}

	if($("#SYNO_TYPE").val()==""){
		$("#SYNO_TYPE").tips({ side:3, msg:'请选择同义词类型', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if($("input[name='IS_DISABLE']:checked").val()==1){
		if($("#DESCRIPTION").val()==""){
			$("#DESCRIPTION").tips({ side:3, msg:'请输入停用描述', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	if(!checkName($("#DN_CHN"),'DN_CHN')){
		flag = false;
	}
	return flag;
}
//检查名称是否存在
function checkName(obj,name){
	if($.trim($(obj).val())==""){
		return false;
	}
	var flag = true;
	var mydata = {DN_CHN:$.trim($(obj).val()),ontoType:$("#ontoType").val(),standFlag:0,DN_ID:$("#DN_ID").val()};
	$.ajax({
		type: "POST",
		url: basePath+'/osyn/checkExistName.do',
    	data: mydata,
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			if(data.result=="success"){
			
			}else{
				$(obj).tips({ side:3, msg:data.result, bg:'#AE81FF',  time:3   });
				flag = false;
			}
		}
	});
	return flag;
}
</script>
</html>