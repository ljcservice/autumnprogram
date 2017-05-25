<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@page import="com.hitzd.DBUtils.TCommonRecord"%>
<%@ include file="MedcareUtils.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/skin/css/Others.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/skin/js/Common.js"></script>
<style>
*{
	font-size:12px;
}
.thHead
{
	background-color: white;
}
.td10
{
	background-color: #FFFFFF;
}
.td11
{
	background-color: #FFFFE5;
}
.td20
{
	background-color: #E5FFE5;
	width: 80px;
	text-align: right;
}
.td21
{
	background-color: #E5FFF2;
}
</style>
<title>药品匹配</title>
</head>
<script>
function closeMe()
{
    window.returnValue = 'OK';
    window.close();
}
// 保存药品信息
function SaveIt()
{
	with (document.formMatcher)
	{
		if((is_anti[0].checked))
		{
			if(ddd_value.value == "")
			{
				if(!confirm("抗菌药物信息填写不全，是否继续保存!"))return							
			}
		}
		submit();
	}
}

function saveAndCloseMe(){
    with(document.formMaster){
        submit();
    }
    window.close();
}
window.name="aa";
function clearInfo()
{  
	document.getElementById("drugspec").value="";
	document.getElementById("drugform").value="";
	document.getElementById("unit").value="";
}

function LoadOver()
{
	window.status = "药品配码";
}

function isAnti(is_anti_val)
{
	var antiObj = document.getElementsByName("is_anti");
	for (var i = 0; i < antiObj.length; i++)
	{
		if (antiObj[i].value == is_anti_val)
			antiObj[i].checked = true;
	}
}

function ThisCheckAnti(is_anti_val)
{
	var antiLevelObj  = document.getElementsByName("anti_level");
	var dddValObj     = document.getElementById("ddd_value");
	var dddPerUnitObj = document.getElementById("ddd_per_unit");
	var dddUnitObj   = document.getElementsByName("ddd_unit");
	var dddValXObj    = document.getElementById("ddd_value_x");
	if (is_anti_val == '1')
	{
		for (var i = 0; i < antiLevelObj.length; i++)
			antiLevelObj[i].disabled = "";
		for (var i = 0; i < dddUnitObj.length; i++)
			dddUnitObj[i].disabled = "";
		dddValObj.disabled     = "";
		dddPerUnitObj.disabled = "";
		if(dddPerUnitObj.value == "")
		{
			dddPerUnitObj.value = document.getElementById("dose_per_unit").value;
		}
		dddValXObj.disabled    = "";
	}
	else
	{
		for (var i = 0; i < antiLevelObj.length; i++)
			antiLevelObj[i].disabled = "disabled";
		for (var i = 0; i < dddUnitObj.length; i++)
			dddUnitObj[i].disabled = "disabled";
		dddValObj.disabled     = "disabled";
		dddPerUnitObj.disabled = "disabled";
		dddValXObj.disabled    = "disabled";
	}
}
    window.name="submitSelf";
</script>
<%!
    String getRequestAttribute(HttpServletRequest request, String name,String replace){
        String result = (String)request.getAttribute(name);
        if(null == result || "".equals(result)){
            result = replace;
        }
        return result;
    }
%>
<%
    String path = request.getContextPath();
    String drug_map_id = getRequestAttribute(request,"drug_map_id","");
    TCommonRecord cr = (TCommonRecord)request.getAttribute("drug");
%>
<body style="margin: 0 0 0 0;" onload="LoadOver()">
<table width="100%" height="100%" border="1" cellpadding="0" cellspacing="1" ">
	<tr height="22">
		<td class="td10" colspan="8" align="center"><b>本院药品信息</b></td>  
	</tr>
	<input type="hidden" name="dose_per_unit" id="dose_per_unit" value="<%=cr.get("dose_per_unit")%>">
	<tr height="22">
		<td class="td20">药品名称：</td>
		<td class="td11"><%=cr.get("Drug_Name_Local")%></td>
		<td class="td20">规格：</td>
		<td class="td11"><%=cr.get("Drug_Spec")%></td>
		<td class="td20">剂型：</td>
		<td class="td11"><%=cr.get("Drug_Form")%></td>
		<td class="td20">单位：</td>
		<td class="td11"><%=cr.get("Units")%></td>
	</tr>
<form name="formMatcher" method="post" action="<%=path%>/control/DrugMatcher" target="submitSelf">
	<input type="Hidden" name="Oper"/>
	<input type="Hidden" name="o" value="update"/>
	<input type="Hidden" name="drug_map_id" value="<%=drug_map_id%>"/>
	<input type="Hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "1")%>"/> 
	<tr height="22">
		<td colspan="8" align="center">
			<b>药品库配对信息</b>
			(<input name="drug_id" id="drug_id" size="10" maxlength="10" class="noline" readonly value="<%=cr.get("Drug_ID")%>"/>)
			<input type="button" onclick="clearInfo()" value="清除"/>
		</td>
	</tr>
	<tr height="22">
		<td class="td20">药品名称:</td>
		<td class="td11">
			<input type="hidden" name="dose_per_unit_pdss" value="<%=cr.get("dose_per_unit_pdss")%>">
			<input type="hidden" name="dose_units_pdss" value="<%=cr.get("dose_units_pdss")%>">
			<input type="hidden" name="drug_indicator_pdss" value="<%=cr.get("drug_indicator_pdss")%>">
			<input type="hidden" name="toxi_property_pdss" value="<%=cr.get("toxi_property_pdss")%>">
			<input name="drug_name" value="<%=cr.get("drug_name")%>" size="15" readonly="readonly" maxlength="255" />
		</td>
		<td class="td20">规格:</td>
		<td class="td11">
			<input id="drugspec" name="drug_spec_pdss" value="<%=cr.get("drug_spec_pdss")%>" size="15" maxlength="255" />
		</td>
		<td class="td20">剂型:</td>
		<td class="td11">
			<input id="drugform" name="drug_form_pdss" value="<%=cr.get("drug_form_pdss")%>" size="10" maxlength="24" />
		</td>
		<td class="td20">单位:</td>
		<td  class="td11">
			<input id="unit" name="units_pdss" value="<%=cr.get("units_pdss")%>" size="10" maxlength="255" />
		</td>
	</tr>
	<tr height="22">
		<td colspan="8" align="center"><b>附加信息</b></td>
	</tr>
	<tr height="22">
		<td class="td20">国家基本药物:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_basedrug" value="1" <%="1".equals(cr.get("is_basedrug")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_basedrug" value="0" <%="0".equals(cr.get("is_basedrug")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">兴奋剂:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_exhilarant" value="1" <%="1".equals(cr.get("is_exhilarant")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_exhilarant" value="0" <%="0".equals(cr.get("is_exhilarant")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">注射剂:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_injection" value="1" <%="1".equals(cr.get("is_injection")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_injection" value="0" <%="0".equals(cr.get("is_injection")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">口服制剂:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_oral" value="1" <%="1".equals(cr.get("is_oral")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_oral" value="0" <%="0".equals(cr.get("is_oral")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	<tr height="22">
		<td class="td20">溶剂:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_impregnant" value="1" <%="1".equals(cr.get("is_impregnant")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_impregnant" value="0" <%="0".equals(cr.get("is_impregnant")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">药理分类:</td>
		<td class="td11">
			<select name="pharm_catalog">
				<option value=""></option>
				<option value="1" <%="1".equals(cr.get("pharm_catalog")) ? "selected='selected'" : ""%>>药品</option>
				<option value="2" <%="2".equals(cr.get("pharm_catalog")) ? "selected='selected'" : ""%>>中草药</option>
				<option value="3" <%="3".equals(cr.get("pharm_catalog")) ? "selected='selected'" : ""%>>中成药</option>
				<option value="4" <%="4".equals(cr.get("pharm_catalog")) ? "selected='selected'" : ""%>>原料</option>
				<option value="5" <%="5".equals(cr.get("pharm_catalog")) ? "selected='selected'" : ""%>>化学试剂</option>
				<option value="6" <%="6".equals(cr.get("pharm_catalog")) ? "selected='selected'" : ""%>>敷料</option>
				<option value="9" <%="9".equals(cr.get("pharm_catalog")) ? "selected='selected'" : ""%>>其它</option>
			</select>
		</td>
		<td class="td20">分类标识:</td>
		<td class="td11">
			<input type="text" name="drug_catalog" size="10" value="<%=cr.get("drug_catalog")%>" maxlength="50"  />
		</td>
		<td class="td20">外用:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_external" value="1" <%="1".equals(cr.get("is_external")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_external" value="0" <%="0".equals(cr.get("is_external")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	<tr height="22">
		<td class="td20">中药:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_chinesedrug" value="1" <%="1".equals(cr.get("is_chinesedrug")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_chinesedrug" value="0" <%="0".equals(cr.get("is_chinesedrug")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">抗过敏:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_allergy" value="1" <%="1".equals(cr.get("is_allergy")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_allergy" value="0" <%="0".equals(cr.get("is_allergy")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">中成药:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_patentdrug" value="1" <%="1".equals(cr.get("is_patentdrug")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_patentdrug" value="0" <%="0".equals(cr.get("is_patentdrug")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">抗肿瘤:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_tumor" value="1" <%="1".equals(cr.get("is_tumor")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_tumor" value="0" <%="0".equals(cr.get("is_tumor")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	<tr height="22">
		<td class="td20">毒性药:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_poison" value="1" <%="1".equals(cr.get("is_poison")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_poison" value="0" <%="0".equals(cr.get("is_poison")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">精神类药:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_psychotic" value="0" <%="0".equals(cr.get("is_psychotic")) ? "checked='checked'" : ""%>>否
			<input type="radio" class="noline" name="is_psychotic" value="1" <%="1".equals(cr.get("is_psychotic")) ? "checked='checked'" : ""%>>I类
			<input type="radio" class="noline" name="is_psychotic" value="2" <%="2".equals(cr.get("is_psychotic")) ? "checked='checked'" : ""%>>II类
		</td>
		<td class="td20">麻药:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_habitforming" value="1" <%="1".equals(cr.get("is_habitforming")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_habitforming" value="0" <%="0".equals(cr.get("is_habitforming")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">放射:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_radiation" value="1" <%="1".equals(cr.get("is_radiation")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_radiation" value="0" <%="0".equals(cr.get("is_radiation")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	<tr height="22">
		<td class="td20">贵重药:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_precious" value="1" <%="1".equals(cr.get("is_precious")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_precious" value="0" <%="0".equals(cr.get("is_precious")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20">高危药:</td>
		<td class="td11">
            <select name="is_danger">
                <option value="0">非高危药</option>
                <option value="1" <%="1".equals(cr.get("is_danger")) ? "selected='selected'" : ""%>>A级</option>
                <option value="2" <%="2".equals(cr.get("is_danger")) ? "selected='selected'" : ""%>>B级</option>
                <option value="3" <%="3".equals(cr.get("is_danger")) ? "selected='selected'" : ""%>>C级</option>
            </select>
		</td>
		<td class="td20">OTC:</td>
		<td class="td11" colspan="3">
			<input type="radio" class="noline" name="is_otc" value="1" <%="1".equals(cr.get("is_otc")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="is_otc" value="0" <%="0".equals(cr.get("is_otc")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
    <tr height="22">
        <th colspan="8">药品附加信息<input type="hidden" name="ybnum" value="21" /></th>
    </tr>
    <tr height="22">
        <td class="td20">激素类:</td>
        <td class="td11" colspan="1">
            <input type="radio" class="noline" name="is_hormone" value="1" <%="1".equals(cr.get("IS_HORMONE")) ? "checked='checked'" : ""%>>是
            <input type="radio" class="noline" name="is_hormone" value="0" <%="0".equals(cr.get("IS_HORMONE")) ? "checked='checked'" : ""%>>否
        </td>
        <td class="td20">心脑血管类:</td>
        <td class="td11" colspan="1">
            <input type="radio" class="noline" name="is_cardiovascular" value="1" <%="1".equals(cr.get("IS_CARDIOVASCULAR")) ? "checked='checked'" : ""%>>是
            <input type="radio" class="noline" name="is_cardiovascular" value="0" <%="0".equals(cr.get("IS_CARDIOVASCULAR")) ? "checked='checked'" : ""%>>否
        </td>
        <td class="td20">消化系统:</td>
        <td class="td11" colspan="1">
            <input type="radio" class="noline" name="is_digestive" value="1" <%="1".equals(cr.get("IS_DIGESTIVE")) ? "checked='checked'" : ""%>>是
            <input type="radio" class="noline" name="is_digestive" value="0" <%="0".equals(cr.get("IS_DIGESTIVE")) ? "checked='checked'" : ""%>>否
        </td>
        <td class="td20">生物制剂:</td>
        <td class="td11" colspan="1">
            <input type="radio" class="noline" name="is_biological" value="1" <%="1".equals(cr.get("IS_BIOLOGICAL")) ? "checked='checked'" : ""%>>是
            <input type="radio" class="noline" name="is_biological" value="0" <%="0".equals(cr.get("IS_BIOLOGICAL")) ? "checked='checked'" : ""%>>否
        </td>
    </tr>
	<tr height="22">
		<th colspan="8">医保信息<input type="hidden" name="ybnum" value="21" /></th>
	</tr>
	<tr height="22">
		<td class="td11" colspan="8">
			<input type="checkbox" class="noline" name="yb1" value="1" <%="1".equals(getMedcareValue(1, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>国家医保
			<input type="checkbox" class="noline" name="yb2" value="1" <%="1".equals(getMedcareValue(2, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>地方医保
			<input type="checkbox" class="noline" name="yb3" value="1" <%="1".equals(getMedcareValue(3, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>保健一级
			<input type="checkbox" class="noline" name="yb4" value="1" <%="1".equals(getMedcareValue(4, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>保健二级
			<input type="checkbox" class="noline" name="yb5" value="1" <%="1".equals(getMedcareValue(5, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>居民医保
			<input type="checkbox" class="noline" name="yb6" value="1" <%="1".equals(getMedcareValue(6, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>重点保健
			<input type="checkbox" class="noline" name="yb7" value="1" <%="1".equals(getMedcareValue(7, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>军队医改
			<input type="checkbox" class="noline" name="yb8" value="1" <%="1".equals(getMedcareValue(8, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>军区不孕
			<input type="checkbox" class="noline" name="yb9" value="1" <%="1".equals(getMedcareValue(9, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>免费
			<input type="checkbox" class="noline" name="yb10" value="1" <%="1".equals(getMedcareValue(10, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>不孕不育
			<input type="checkbox" class="noline" name="yb11" value="1" <%="1".equals(getMedcareValue(11, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>512医保
			<input type="checkbox" class="noline" name="yb12" value="1" <%="1".equals(getMedcareValue(12, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>离休医保
			<input type="checkbox" class="noline" name="yb13" value="1" <%="1".equals(getMedcareValue(13, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>农合医保<br/><br/>
			<input type="checkbox" class="noline" name="yb14" value="1" <%="1".equals(getMedcareValue(14, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>医保
			<input type="checkbox" class="noline" name="yb15" value="1" <%="1".equals(getMedcareValue(15, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>省异地
			<input type="checkbox" class="noline" name="yb16" value="1" <%="1".equals(getMedcareValue(16, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>市医保
			<input type="checkbox" class="noline" name="yb17" value="1" <%="1".equals(getMedcareValue(17, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>铁路医保
			<input type="checkbox" class="noline" name="yb18" value="1" <%="1".equals(getMedcareValue(18, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>铁路离休
			<input type="checkbox" class="noline" name="yb19" value="1" <%="1".equals(getMedcareValue(19, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>铁路保健
			<input type="checkbox" class="noline" name="yb20" value="1" <%="1".equals(getMedcareValue(20, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>铁路512
			<input type="checkbox" class="noline" name="yb21" value="1" <%="1".equals(getMedcareValue(21, cr.get("is_medcare"))) ? "checked='checked'" : ""%>>异地医保
		</td>
	</tr>
	<tr height="22">
		<th colspan="8">抗菌药属性</th>
	</tr>
	<tr height="22">
		<td class="td20">抗菌药:</td>
		<td class="td11">
			<input type="radio" class="noline" name="is_anti" value="1" <%="1".equals(cr.get("is_anti")) ? "checked='checked'" : ""%> onclick="ThisCheckAnti(this.value)">是
			<input type="radio" class="noline" name="is_anti" value="0" <%="0".equals(cr.get("is_anti")) ? "checked='checked'" : ""%> onclick="ThisCheckAnti(this.value)">否
		</td>
        <td class="td20">作用范围:</td>
        <td class="td11">
            <%
            /*处理逻辑
            抗菌药分为外用和内用，全身就是内用，外用就是局部，当是抗菌药时is_part起作用，当不是抗菌药时is_external 起作用。
            */
            String is_part = "";
            if("1".equals(cr.get("is_anti"))){
                is_part=cr.get("is_external");
            }
            %>
            <input type="radio" class="noline" name="is_part" value="0" <%="0".equals(is_part)?"checked='checked'":""%> >全身
            <input type="radio" class="noline" name="is_part" value="1" <%="1".equals(is_part)?"checked='checked'":""%> >局部
        </td>
		<td class="td20" >抗菌药级别：</td>
		<td class="td11" colspan="3">
			<input type="radio" name="anti_level" <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> <%=cr.get("anti_level").equals("1") ? "checked='checked'" : ""%> class="noline" value="1" onclick="isAnti('1')">非限制
			<input type="radio" name="anti_level" <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> <%=cr.get("anti_level").equals("2") ? "checked='checked'" : ""%> class="noline" value="2" onclick="isAnti('1')">限制
			<input type="radio" name="anti_level" <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> <%=cr.get("anti_level").equals("3") ? "checked='checked'" : ""%> class="noline" value="3" onclick="isAnti('1')">特殊
		</td>
	</tr>
	<tr>
		<td class="td20">DDD值:</td>
		<td class="td11">
			<input name="ddd_value" id="ddd_value" <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> value="<%=cr.get("ddd_value")%>" size="15" ondragenter="return false" style="ime-mode:disabled" />
		</td>
		<td class="td20">DDD剂量单位</td>
		<td class="td11">
			<input name="ddd_per_unit" id="ddd_per_unit" <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> value="<%=cr.get("ddd_per_unit")%>" size="15" maxlength="10" />
			<input name="ddd_unit" id="ddd_unit_g"  <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> value="g" checked="checked" type="radio">g </input>
			<input name="ddd_unit" id="ddd_unit_mg" <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> value="mg" <%="mg".equals(cr.get("ddd_unit")) ? "checked='checked'" : ""%> type="radio">mg</input>
		</td>
		<td class="td20">单日最大剂量:</td>
		<td class="td11" colspan="3">
			<input name="ddd_value_x" id="ddd_value_x" <%="1".equals(cr.get("is_anti")) ? "" : "disabled='disabled'"%> value="<%=cr.get("ddd_value_x")%>" size="15" style="ime-mode:disabled" />
		</td>
	</tr>
	<tr height="22">
		<td colspan="8" align="center">
			<input type="button" onclick="SaveIt()" value="  保存  "/>
			<input type="button" onclick="closeMe()" value="  关闭  "/>
		</td>
	</tr>
</form>
	<tr height="22">
		<td colspan="8" align="center">药品匹配</td>
	</tr>
	<tr height="22">
		<td colspan="8">
			<form target="childs" action="<%=path%>/control/DrugMatcher" method="post">
                <input type="hidden" name="o" value="queryPDSSList"/>
				药品名称：<input name="drugName" value="<%=cr.get("Drug_Name_Local")%>" size="35"/>
				<input type="submit" value=" 查询 "/>
			</form>
		</td>
	</tr>
	<tr height="*">
		<td colspan="8">
			<iframe width="100%" height="100%" frameborder="0" name="childs" src="<%=path%>/control/DrugMatcher?o=queryPDSSList"></iframe>
		</td>
	</tr>
</table>
</body>
</html>