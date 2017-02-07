<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>诊断新增页面-增加同义词</title>
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
								<form action="osyn/saveDiagOsyn.do" name="osynForm" id="osynForm" method="post">
									<input type="hidden" id="tableId" value=""/>
									<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="text-align: center;padding: 10px;" colspan="2">
													<c:if test="${ontoType==51005}">诊断通用名</c:if>
													<c:if test="${ontoType==51003}">手术通用名</c:if>
													<c:if test="${ontoType==51006}">科室通用名</c:if>
											</td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">术语名称-中文:</td>
											<td><input type="text" name="DN_CHN" id="DN_CHN" maxlength="80" placeholder="输入术语名称中文" value="${pd.DN_CHN }" style="width:98%;" onblur="checkName(this,'DN_CHN');"/></td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">术语名称-英文:</td>
											<td><input type="text" name="DN_ENG" id="DN_ENG"  maxlength="80" placeholder="输入术语名称英文" value="${pd.DN_ENG }" style="width:98%;" /></td>
										</tr>
<!-- 										<tr> -->
<!-- 											<td style="text-align: right;padding: 10px;">术语类型:</td> -->
<!-- 											<td> -->
<!-- 												<select class=" form-control" name="TERM_TYPE" id="TERM_TYPE" data-placeholder="术语类型" style="vertical-align:top;"> -->
<!-- 													<option value=""></option> -->
<!-- 													<option value="1" <c:if test="${pd.TERM_TYPE==1}">selected</c:if>>症状</option> -->
<!-- 													<option value="2" <c:if test="${pd.TERM_TYPE==2}">selected</c:if>>疾病</option> -->
<!-- 												</select> -->
<!-- 											</td> -->
<!-- 										</tr> -->
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
											<td style="text-align: right;padding: 10px;">术语来源名称-中文:</td>
											<td><input type="text" name="ORG_DN_CHN" id="ORG_DN_CHN" value="${pd.ORG_DN_CHN }" placeholder="输入术语来源名称-中文" maxlength="80" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">术语来源名称-英文:</td>
											<td>
												<input type="email" name="ORG_DN_ENG" id="ORG_DN_ENG"  value="${pd.ORG_DN_ENG }" placeholder="输入术语来源名称-英文" maxlength="80" style="width:98%;"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: right;padding: 10px;">停用标记:</td>
											<td style="vertical-align: middle;">
												<label><input class="ace" id="u3897_input" value="0" name="IS_DISABLE" type="radio" text="否" <c:if test="${pd.IS_DISABLE==0 || pd.IS_DISABLE == null}">checked</c:if> >否&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
												<label><input class="ace" id="u3897_input" value="1" name="IS_DISABLE" type="radio" text="是" <c:if test="${pd.IS_DISABLE==1 }">checked</c:if> >是&nbsp;<span class="lbl"></span></label>
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
												<a class="btn btn-mini btn-warning" onclick="saveAndAdd();">保存并新增</a>
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
	top.Dialog.close();
}
function saveAndAdd(){
	if(!check()){return;}
	save();
	document.getElementById("osynForm").reset();
	$('select#TERM_TYPE option:first').attr('selected','true')
	$('select#SYNO_TYPE option:first').attr('selected','true')
}
//保存
function save(){
	var mydocument = parent.$("#_DialogFrame_0")[0].contentWindow.document;
	var osyndiv = $(mydocument.getElementById("osynDiv"));
	var tr_all = 
			"<tr>"+
				"<input type='hidden' name='DN_CHN' value='"+$("#DN_CHN").val()+"' />"+
				"<input type='hidden' name='DN_ENG' value='"+$("#DN_ENG").val()+"' />"+
// 				"<input type='hidden' name='TERM_TYPE' value='"+$('select#TERM_TYPE option:selected').val()+"' />"+
				"<input type='hidden' name='SYNO_TYPE' value='"+$('select#SYNO_TYPE option:selected').val()+"' />"+
				"<input type='hidden' name='ORG_DN_CHN' value='"+$("#ORG_DN_CHN").val()+"' />"+
				"<input type='hidden' name='ORG_DN_ENG' value='"+$("#ORG_DN_ENG").val()+"' />"+
				"<input type='hidden' name='IS_DISABLE' value='"+$("input[name='IS_DISABLE']:checked").val()+"' />"+
				"<input type='hidden' name='DESCRIPTION' value='"+$("#DESCRIPTION").val()+"' />"+
				"<td width='100px;'>术语名称中文:</td><td style='max-width:250px;'>"+$("#DN_CHN").val()+"</td><td width='130px;'>术语来源名称中文:</td><td style='max-width:250px;'>"+$("#ORG_DN_CHN").val()+"</td><td width='100px;'>同义词类型:</td><td>"+$('select#SYNO_TYPE option:selected').text()+"</td><td width='80px;'>停用标记:</td><td>"+$("input[name='IS_DISABLE']:checked").attr("text")+"</td>"+
				"<td rowspan='2' width='100px;' style='vertical-align: middle;' align='center'>"+
				"<div class='hidden-sm hidden-xs btn-group'>"+
					"<a class='btn btn-xs btn-success' title='编辑' onclick='modifyOsyn(this);'><i class='ace-icon fa fa-pencil-square-o bigger-120'></i></a>"+
					"<a class='btn btn-xs btn-danger' onclick='closeTable(this);'><i class='ace-icon fa fa-trash-o bigger-120' title='删除'></i></a>"+ 
				"</div>"+
				"</td>"+
			"</tr>"+
			"<tr><td>术语名称英文:</td><td>"+$("#DN_ENG").val()+"</td><td>术语来源名称英文:</td><td>"+$("#ORG_DN_ENG").val()+"</td><td >停用描述:</td><td colspan='3' style='max-width:250px;'>"+$("#DESCRIPTION").val()+"</td></tr>";
	if($("#tableId").val()==""){
		//新增
		var tableID = parent.$("#_DialogFrame_0")[0].contentWindow.getTable_index();
		var html =
		"<table  class='table table-bordered table-hover' id='table_" +tableID+ "'>"+
			tr_all +
		"</table>";
		osyndiv.append($(html));
	}else{
		//修改
		//找到上一个窗口的table
		$(mydocument.getElementById($("#tableId").val())).empty().html(tr_all);
	}
}
function check(){
	var flag = true;
	if($("#DN_CHN").val()==null ||$.trim($("#DN_CHN").val())=="" ){
		$("#DN_CHN").tips({ side:3, msg:'输入名称中文', bg:'#AE81FF',  time:3   });
		flag = false;
	}
// 	if($("#DN_ENG").val()==null ||$.trim($("#DN_ENG").val())=="" ){
// 		$("#DN_ENG").tips({ side:3, msg:'输入名称英文', bg:'#AE81FF',  time:3   });
// 		flag = false;
// 	}
	if($("#ORG_DN_CHN").val()==null ||$.trim($("#ORG_DN_CHN").val())=="" ){
		$("#ORG_DN_CHN").tips({ side:3, msg:'请输入来源名称中文', bg:'#AE81FF',  time:3   });
		flag = false;
	}
// 	if($("#ORG_DN_ENG").val()==null ||$.trim($("#ORG_DN_ENG").val())=="" ){
// 		$("#ORG_DN_ENG").tips({ side:3, msg:'请输入来源名称英文', bg:'#AE81FF',  time:3   });
// 		flag = false;
// 	}
// 	if($("#TERM_TYPE").val()==""){
// 		$("#TERM_TYPE").tips({ side:3, msg:'请选择术语类型', bg:'#AE81FF',  time:3   });
// 		flag = false;
// 	}
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
// 	if(!checkName($("#DN_ENG"),'DN_ENG')){
// 		flag = false;
// 	}
	return flag;
}
//检查名称是否存在
function checkName(obj,name){
	if($.trim($(obj).val())==""){
		return false;
	}
	var flag = true;
	var mydata = {DN_CHN:$.trim($(obj).val()),ontoType:$("#ontoType").val(),standFlag:0};
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