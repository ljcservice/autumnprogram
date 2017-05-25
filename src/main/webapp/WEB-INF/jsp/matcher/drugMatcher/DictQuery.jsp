<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
<style>
.ztree li a.curSelectedNode {
/* 	background-color: #ffb951;	 */
 }
 .check-search{
	float: left;
	margin: 2px;
}
</style>
</head>
<body style="background-color:white;">
						<div class=" ">
						<!-- 检索  -->
						<div id="tbl-container" style="width:100%;height:100%;overflow-y:auto; overflow-x:hidden; ">
						<table width="100%" height="100%" class="table table-striped table-bordered table-hover" height="22"  >
							<thead>
							<tr style="background-color: white;">
								<th width="35" nowrap="nowrap">操作</th>
								<th width="*" nowrap="nowrap">药品名称</th>
								<th width="80" nowrap="nowrap">规格</th>
								<th width="80" nowrap="nowrap">单位</th>
								<th width="80" nowrap="nowrap">剂型</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${drugList}" var="cr" varStatus="vs">
							<tr >
								<td align="center"><input type="button" onclick="ThisSel('${cr.get("drug_id") }',
																'${cr.get("drug_name") }',
																'${cr.get("drug_spec") }',
																'${cr.get("units") }',
																'${cr.get("drug_form") }',
																'${cr.get("dose_per_unit") }',
																'${cr.get("dose_units") }',
																'${cr.get("drug_indicator") }',
																'${cr.get("is_anti") }',
																'${cr.get("is_basedrug") }',
																'${cr.get("ddd_value") }',
																'${cr.get("ddd_unit") }',
																'${cr.get("is_exhilarant") }',
																'${cr.get("is_injection") }',
																'${cr.get("is_oral") }',
																'${cr.get("is_impregnant") }',
																'${cr.get("pharm_catalog") }',
																'${cr.get("drug_catalog") }',
																'${cr.get("is_external") }',
																'${cr.get("is_chinesedrug") }',
																'${cr.get("is_allergy") }',
																'${cr.get("ddd_value_x") }',
																'${cr.get("is_patentdrug") }',
																'${cr.get("is_tumor") }',
																'${cr.get("is_poison") }',
																'${cr.get("is_psychotic") }',
																'${cr.get("is_habitforming") }',
																'${cr.get("is_radiation") }',
																'${cr.get("is_precious") }',
																'${cr.get("is_danger") }',
																'${cr.get("is_otc") }',
																'${cr.get("is_medcare") }',
																'${cr.get("toxi_property") }',
                                                                '${is_part}',
                                                                '${cr.get("is_hormone") }',
                                                                '${cr.get("is_cardiovascular") }',
                                                                '${cr.get("is_digestive") }',
                                                                '${cr.get("is_biological") }',
                                                                '${cr.get("is_danger") }',
                                                                '${cr.get("is_psychotic")}'
                                                                )" value="选择"/></td>
								<td>${cr.get("Drug_Name")}&nbsp;</td>
								<td>${cr.get("Drug_Spec")}&nbsp;</td>
								<td>${cr.get("Units")}&nbsp;</td>
								<td>${cr.get("Drug_Form")}&nbsp;</td>
							</tr>
							</c:forEach>
							</tbody>
						</table>
						</div>
						
						<div class= "pageStrDiv" id="pageStrDiv" style="padding-top: 5px;padding-bottom: 5px;">
							<table style="width:100%;">
								<tr>
									<td>
										<div class="pagination" style="float: right;padding: 0px;margin: 0px;">${page.pageStr}</div>
									</td>
								</tr>
							</table>
						</div>
	
						</div>
						<!-- /.col -->
		<!-- /.main-content -->
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
	<script type="text/javascript" src="static/js/common/common.js"></script>
	</body>
<script type="text/javascript" src="static/js/common/lockTable.js?v=201612"></script>
<script type="text/javascript">
$(top.hangge());

function ThisCheckAnti(is_anti_val)
{
	
	var antiLevelObj = window.parent.document.getElementsByName("anti_level");
	var isPart = window.parent.document.getElementsByName("is_part");
	var dddValObj = window.parent.document.getElementById("ddd_value");
	var dddUnitObj = window.parent.document.getElementById("ddd_per_unit");
	var dddValXObj = window.parent.document.getElementById("ddd_value_x");
	if (is_anti_val == '0' || '' == is_anti_val){
		for (var i = 0; i < antiLevelObj.length; i++)
			antiLevelObj[i].disabled = "disabled";
		dddValObj.disabled = "disabled";
		dddUnitObj.disabled = "disabled";
		dddValXObj.disabled = "disabled";
		for(var i = 0; i < isPart.length; i++){
			isPart[i].disabled="disabled";
		}
	}else{
		for (var i = 0; i < antiLevelObj.length; i++)
			antiLevelObj[i].disabled = "";
		for(var i = 0; i < isPart.length; i++){
			isPart[i].disabled="";
		}
		dddValObj.disabled = "";
		dddUnitObj.disabled = "";
		/*
		2014-07-18 修改 liujc 该字段已经更改意义 
		if (dddUnitObj.value == "")
			dddUnitObj.value = "g";*/
		dddValXObj.disabled = "";
	}
}

function ThisSel(drug_id,drug_name,drug_spec,units,drug_form,dose_per_unit,dose_units,drug_indicator,is_anti,is_basedrug,ddd_value,ddd_unit,is_exhilarant,is_injection,is_oral,is_impregnant,pharm_catalog,drug_catalog,is_external,is_chinesedrug,is_allergy,ddd_value_x,is_patentdrug,
                 is_tumor,is_poison,is_psychotic,is_habitforming,is_radiation,
                 is_precious,is_danger,is_otc,is_medcare,toxi_property,is_part, is_hormone, is_cardiovascular, is_digestive, is_biological,is_danger, is_psychotic)
{
	window.parent.document.formMatcher.drug_id.value = drug_id;
	window.parent.document.formMatcher.drug_name.value = drug_name;
	window.parent.document.formMatcher.drug_spec_pdss.value = drug_spec;
	window.parent.document.formMatcher.units_pdss.value = units;
	window.parent.document.formMatcher.drug_form_pdss.value = drug_form;
	window.parent.document.formMatcher.dose_per_unit_pdss.value = dose_per_unit;
	window.parent.document.formMatcher.dose_units_pdss.value = dose_units;
	window.parent.document.formMatcher.drug_indicator_pdss.value = drug_indicator;
	window.parent.document.formMatcher.toxi_property_pdss.value = toxi_property;
	for (var i = 0; i < window.parent.document.formMatcher.is_anti.length; i++)
	{
		if (window.parent.document.formMatcher.is_anti[i].value == is_anti){
			window.parent.document.formMatcher.is_anti[i].checked = true;
		}else{
			window.parent.document.formMatcher.is_anti[i].checked = false;
		}
	}
	ThisCheckAnti(is_anti);
    //抗菌药作用范围
    for (var i = 0; i < window.parent.document.formMatcher.is_part.length; i++)
    {
        if (window.parent.document.formMatcher.is_part[i].value == is_part)
            window.parent.document.formMatcher.is_part[i].checked = true;
        else
            window.parent.document.formMatcher.is_part[i].checked = false;
    }
    //激素类
    for (var i = 0; i < window.parent.document.formMatcher.is_hormone.length; i++)
    {
        if (window.parent.document.formMatcher.is_hormone[i].value == is_hormone)
            window.parent.document.formMatcher.is_hormone[i].checked = true;
        else
            window.parent.document.formMatcher.is_hormone[i].checked = false;
    }
    //心脑血管类
    for (var i = 0; i < window.parent.document.formMatcher.is_cardiovascular.length; i++)
    {
        if (window.parent.document.formMatcher.is_cardiovascular[i].value == is_cardiovascular)
            window.parent.document.formMatcher.is_cardiovascular[i].checked = true;
        else
            window.parent.document.formMatcher.is_cardiovascular[i].checked = false;
    }
    //消化系统类
    for (var i = 0; i < window.parent.document.formMatcher.is_digestive.length; i++)
    {
        if (window.parent.document.formMatcher.is_digestive[i].value == is_digestive)
            window.parent.document.formMatcher.is_digestive[i].checked = true;
        else
            window.parent.document.formMatcher.is_digestive[i].checked = false;
    }

    //生物制剂类
    for (var i = 0; i < window.parent.document.formMatcher.is_biological.length; i++)
    {
        if (window.parent.document.formMatcher.is_biological[i].value == is_biological)
            window.parent.document.formMatcher.is_biological[i].checked = true;
        else
            window.parent.document.formMatcher.is_biological[i].checked = false;
    }
    //高危类
    for (var i = 0; i < window.parent.document.formMatcher.is_danger.length; i++)
    {
        if (window.parent.document.formMatcher.is_danger[i].value == is_danger)
            window.parent.document.formMatcher.is_danger[i].selected = true;
        else
            window.parent.document.formMatcher.is_danger[i].selected = false;
    }
    //精神类
    for (var i = 0; i < window.parent.document.formMatcher.is_psychotic.length; i++)
    {
        if (window.parent.document.formMatcher.is_psychotic[i].value == is_psychotic)
            window.parent.document.formMatcher.is_psychotic[i].checked = true;
        else
            window.parent.document.formMatcher.is_psychotic[i].checked = false;
    }



	for (var i = 0; i < window.parent.document.formMatcher.is_basedrug.length; i++)
	{
		if (window.parent.document.formMatcher.is_basedrug[i].value == is_basedrug)
			window.parent.document.formMatcher.is_basedrug[i].checked = true;
		else
			window.parent.document.formMatcher.is_basedrug[i].checked = false;
	}
	window.parent.document.formMatcher.ddd_value.value = ddd_value;
	window.parent.document.formMatcher.ddd_unit.value = ddd_unit;
	for (var i = 0; i < window.parent.document.formMatcher.is_exhilarant.length; i++)
	{
		if (window.parent.document.formMatcher.is_exhilarant[i].value == is_exhilarant)
			window.parent.document.formMatcher.is_exhilarant[i].checked = true;
		else
			window.parent.document.formMatcher.is_exhilarant[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_injection.length; i++)
	{
		if (window.parent.document.formMatcher.is_injection[i].value == is_injection)
			window.parent.document.formMatcher.is_injection[i].checked = true;
		else
			window.parent.document.formMatcher.is_injection[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_oral.length; i++)
	{
		if (window.parent.document.formMatcher.is_oral[i].value == is_oral)
			window.parent.document.formMatcher.is_oral[i].checked = true;
		else
			window.parent.document.formMatcher.is_oral[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_impregnant.length; i++)
	{
		if (window.parent.document.formMatcher.is_impregnant[i].value == is_impregnant)
			window.parent.document.formMatcher.is_impregnant[i].checked = true;
		else
			window.parent.document.formMatcher.is_impregnant[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_impregnant.length; i++)
	{
		if (window.parent.document.formMatcher.pharm_catalog[i].value == pharm_catalog)
			window.parent.document.formMatcher.pharm_catalog[i].selected = true;
		else
			window.parent.document.formMatcher.pharm_catalog[i].checked = false;
	}
	window.parent.document.formMatcher.drug_catalog.value = drug_catalog;
	for (var i = 0; i < window.parent.document.formMatcher.is_external.length; i++)
	{
		if (window.parent.document.formMatcher.is_external[i].value == is_external)
			window.parent.document.formMatcher.is_external[i].checked = true;
		else
			window.parent.document.formMatcher.is_external[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_chinesedrug.length; i++)
	{
		if (window.parent.document.formMatcher.is_chinesedrug[i].value == is_chinesedrug)
			window.parent.document.formMatcher.is_chinesedrug[i].checked = true;
		else
			window.parent.document.formMatcher.is_chinesedrug[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_allergy.length; i++)
	{
		if (window.parent.document.formMatcher.is_allergy[i].value == is_allergy)
			window.parent.document.formMatcher.is_allergy[i].checked = true;
		else
			window.parent.document.formMatcher.is_allergy[i].checked = false;
	}
	window.parent.document.formMatcher.ddd_value_x.value = ddd_value_x;
	for (var i = 0; i < window.parent.document.formMatcher.is_patentdrug.length; i++)
	{
		if (window.parent.document.formMatcher.is_patentdrug[i].value == is_patentdrug)
			window.parent.document.formMatcher.is_patentdrug[i].checked = true;
		else
			window.parent.document.formMatcher.is_patentdrug[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_tumor.length; i++)
	{
		if (window.parent.document.formMatcher.is_tumor[i].value == is_tumor)
			window.parent.document.formMatcher.is_tumor[i].checked = true;
		else
			window.parent.document.formMatcher.is_tumor[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_poison.length; i++)
	{
		if (window.parent.document.formMatcher.is_poison[i].value == is_poison)
			window.parent.document.formMatcher.is_poison[i].checked = true;
		else
			window.parent.document.formMatcher.is_poison[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_psychotic.length; i++)
	{
		if (window.parent.document.formMatcher.is_psychotic[i].value == is_psychotic)
			window.parent.document.formMatcher.is_psychotic[i].checked = true;
		else
			window.parent.document.formMatcher.is_psychotic[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_habitforming.length; i++)
	{
		if (window.parent.document.formMatcher.is_habitforming[i].value == is_habitforming)
			window.parent.document.formMatcher.is_habitforming[i].checked = true;
		else
			window.parent.document.formMatcher.is_habitforming[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_radiation.length; i++)
	{
		if (window.parent.document.formMatcher.is_radiation[i].value == is_radiation)
			window.parent.document.formMatcher.is_radiation[i].checked = true;
		else
			window.parent.document.formMatcher.is_radiation[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_precious.length; i++)
	{
		if (window.parent.document.formMatcher.is_precious[i].value == is_precious)
			window.parent.document.formMatcher.is_precious[i].checked = true;
		else
			window.parent.document.formMatcher.is_precious[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_danger.length; i++)
	{
		if (window.parent.document.formMatcher.is_danger[i].value == is_danger)
			window.parent.document.formMatcher.is_danger[i].checked = true;
		else
			window.parent.document.formMatcher.is_danger[i].checked = false;
	}
	for (var i = 0; i < window.parent.document.formMatcher.is_otc.length; i++)
	{
		if (window.parent.document.formMatcher.is_otc[i].value == is_otc)
			window.parent.document.formMatcher.is_otc[i].checked = true;
		else
			window.parent.document.formMatcher.is_otc[i].checked = false;
	}
	for (var i = 1; i <= window.parent.document.formMatcher.ybnum.value; i++)
	{
		for (var j = 0; j < window.parent.document.getElementsByName("yb" + i).length; j++)
		{
			if (window.parent.document.getElementsByName("yb" + i)[j].value == getMedcareValue(i, is_medcare))
				window.parent.document.getElementsByName("yb" + i)[j].checked = true;
			else
				window.parent.document.getElementsByName("yb" + i)[j].checked = false;
		}
	}
}

function loadOver()
{
	
}
function getMedcareValue(pos, str)
{
	if (str != null && str.length >= pos)
	{
		return str.substring(pos - 1, pos);
	}
	return "";
}
</script>
</html>