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
<link type="text/css" rel="stylesheet" href="static/css/ontology.css?v=2016"/>
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
										<form action="ontology/${MSG }.do" name="userForm" id="userForm" method="post">
											<div id="zhongxin" style="padding-top: 10px;height:auto;max-height: 533px;min-height: 500px;overflow:auto;">
											<input type="hidden" name="patient_id" value="${pd.patient_id}" id="patient_id"/>
											<input type="hidden" name="visit_id" value="${pd.visit_id}" id="visit_id"/>
											<input type="hidden" name="id" value="${pd.id}" id="id"/>
											<input type="hidden" name="business_type" value="${pd.business_type}" id="business_type"/>
											<input type="hidden" name="ngroupnum" value="${pd.ngroupnum}" id="ngroupnum"/>
											<div >
											<div>
											<table id="table_report" style="width: 100%;" class="table table-bordered table-hover">
												<tr>
													<td width="20%;" style="text-align: right;padding-top: 10px;">点评类型:</td>
													<td width="80%;">
														<select class="chosen-select form-control" style="vertical-align:top;width: 80px;" name="RS_DRUG_TYPE" id="RS_DRUG_TYPE" onchange="changeType(this);">
															<c:forEach items="${checkType.entrySet()}" var="partTyp" varStatus="vs">
																<option value="${partTyp.key}" <c:if test="${partTyp.key == 'iv_effect' }">selected</c:if>>${partTyp.value.rs_type_name}</option>
															</c:forEach>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;" rowspan="2"><c:if test="${pd.business_type==0}">医嘱名称</c:if><c:if test="${pd.business_type==1}">药品名称</c:if>:</td>
													<td >
														<div>
															<select class="chosen-select form-control" name="orderDrug1" id="orderDrug1" >
																<option value="">请选择</option>
																<c:forEach items="${orderMap1.entrySet()}" var="partTyp" varStatus="vs">
																	<option value="${partTyp.key}" <c:if test="${vs.index ==0 }">selected</c:if>>${partTyp.value}</option>
																</c:forEach>
															</select>
														</div>
													</td>
												</tr>
												<tr>
													<td>
														<div id="divorderMap2" style="padding-top: 5px;">
															<select class="chosen-select form-control" name="orderDrug2" id="orderDrug2" >
																<option value="">请选择</option>
																<c:forEach items="${orderMap2.entrySet()}" var="partTyp" varStatus="vs">
																	<option value="${partTyp.key}" <c:if test="${vs.index ==0}">selected</c:if>>${partTyp.value}</option>
																</c:forEach>
															</select>
															<c:if test="${pd.business_type==1}">
																<span style="color: #ffb752">（此处包括同日其他处方的药品）</span>
															</c:if>
														</div>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">问题说明:</td>
													<td >
														<textarea name="ALERT_HINT" id="ALERT_HINT" rows="5" cols="77">${pd.ALERT_HINT}</textarea>
													</td>
												</tr>
											</table>
											</div>
											
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
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/edit.js?v=2018009"></script>
<script type="text/javascript">
//保存本体
function save(){
	var flag = true;
	if($("#ALERT_HINT").val()==null ||$.trim($("#ALERT_HINT").val())=="" ){
		$("#ALERT_HINT").tips({ side:3, msg:'请输入问题说明', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	var val = $("#RS_DRUG_TYPE").val();
	var orderDrug1 = $("#orderDrug1").val();
	if(orderDrug1==null||orderDrug1==""||$.trim(orderDrug1)==""){
		$("#orderDrug1_chosen").tips({ side:3, msg:'请选择第一个药品', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if(val=="ingredien" || val=="interaction" || val=="iv_effect"){
		var orderDrug2 = $("#orderDrug2").val();
		if(orderDrug2==null||orderDrug2==""||$.trim(orderDrug2)==""){
			$("#orderDrug2_chosen").tips({ side:3, msg:'请选择第二个药品', bg:'#AE81FF',  time:3   });
			flag = false;
		}
		if(orderDrug1!=null &&orderDrug1!="" && orderDrug1==orderDrug2){
			$("#orderDrug2_chosen").tips({ side:3, msg:'第二个药品不能与第一个药品相同', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	if(!flag){ return; }
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	var url = path+"/DoctOrder/addCheckRs.do";
	$.ajax({
		type: "POST",
		url: url, 
    	data: $("#userForm").serialize(),
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			if(data.result=="success"){
				var ngroupnum =data.ngroupnum;
				if(ngroupnum!=null&&ngroupnum!=""){
					$("#ngroupnum").val(ngroupnum);
				}
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
		}
    });
}
//改变类型
function changeType(obj){
	var val = $(obj).val();
	if(val=="ingredien" || val=="interaction" || val=="iv_effect"||val=="manager4Two"){
		$("#divorderMap2").show();
		$("#orderDrug2").removeAttr("disabled");
		$("#orderDrug2").chosen('destroy');
		$("#orderDrug2").chosen({allow_single_deselect:true});
	}else{
		$("#divorderMap2").hide();
		$("#orderDrug2").attr("disabled","disabled");
		$("#orderDrug2").chosen('destroy');
		$("#orderDrug2").chosen({allow_single_deselect:true});
	}
}
</script>
</html>