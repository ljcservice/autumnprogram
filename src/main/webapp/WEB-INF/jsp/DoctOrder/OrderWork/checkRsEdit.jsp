<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css?v=11122"/>
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.exedit.min.js"></script>
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<link type="text/css" rel="stylesheet" href="static/css/ontology.css"/>
</head>
<body class="no-skin" style="background-color: white;">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<table style="width:100%;" border="0">
								<tr>
									<td style="" valign="top" >
										<form action="" name="userForm" id="userForm" method="post">
											<div id="zhongxin" style="padding-top: 10px;">
											<input type="hidden" name="patient_id" value="${pd.patient_id}" id="patient_id"/>
											<input type="hidden" name="visit_id" value="${pd.visit_id}" id="visit_id"/>
											<input type="hidden" name="ngroupnum" value="${pd.ngroupnum}" id="ngroupnum"/>
											<input type="hidden" name="RS_ID" id="RS_ID" value="${pd.RS_ID}" />
											<div style="height:auto;min-height:200px;max-height: 533px;overflow:auto;">
											<div>
											<table id="table_report" class="table table-bordered table-hover">
												<tr>
													<td style="text-align: right;padding-top: 10px;">点评类型:</td>
													<td rowspan="1">${checkType.get(pd.RS_DRUG_TYPE.toString()).RS_TYPE_NAME} </td>
												</tr>
												<tr>
													<td width="15%;" style="text-align: right;padding-top: 10px;">医嘱名称:</td>
													<td width="35%;">
														<div>
															${pd.DRUG_ID1_NAME}
														</div>
														<div id="divorderMap2">
															${pd.DRUG_ID2_NAME}
														</div>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">问题说明:</td>
													<td rowspan="1">
														<textarea name="ALERT_HINT" id="ALERT_HINT" rows="5" cols="48">${pd.ALERT_HINT}</textarea>
													</td>
												</tr>
											</table>
											</div>
											<div class="position-relative" id="osynPageParam" style="padding-bottom:4px;">
												<table style="width:100%;">
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
									</td>
								</tr>
							</table>
								
								
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
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/tree.fixer.js"></script>
<script type="text/javascript" src="static/js/ontology/edit.js?v=2018"></script>
<script type="text/javascript">
//保存本体
function save(){
	var flag = true;
	if($("#ALERT_HINT").val()==null ||$.trim($("#ALERT_HINT").val())=="" ){
		$("#ALERT_HINT").tips({ side:3, msg:'请输入问题描述', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if(!flag){return;}
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	var url = path+"/DoctOrder/editCheckRs.do?"+$("#userForm").serialize();
	$.get(url,function(data){
		if(data.result=="success"){
			top.Dialog.close();
		}else{
			$("#zhongxin").show();
			$("#zhongxin2").hide();
			bootbox.dialog({
				message: "<span class='bigger-110'>"+data.result+"</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
		}
	});
}
//改变类型
function changeType(obj){
	var val = $(obj).val();
	if(val=="ingredien" || val=="interaction" || val=="iv_effect"){
		$("#divorderMap2").show();
		$("#orderMap2").attr("disabled","");
	}else{
		$("#divorderMap2").hide();
		$("#orderMap2").attr("disabled","disabled");
	}
}
</script>
</html>