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
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
						
							
<table width="100%" height="100%" class="table table-striped table-bordered table-hover" style="margin-bottom: 2px;" >
	<tr height="22">
		<td class="td10" colspan="8" align="center"><b>本院药品信息</b></td>  
	</tr>
	<input type="hidden" name="dose_per_unit" id="dose_per_unit" value="${cr.get('dose_per_unit')}">
	<tr height="22">
		<td style="text-align: right;">药品名称：</td>
		<td style="text-align: left;">${cr.get('Drug_Name_Local')}</td>
		<td style="text-align: right;">规格：</td>
		<td style="text-align: left;">${cr.get('Drug_Spec')}</td>
		<td style="text-align: right;">剂型：</td>
		<td style="text-align: left;">${cr.get('Drug_Form')}</td>
		<td style="text-align: right;">单位：</td>
		<td style="text-align: left;">${cr.get('Units')}</td>
	</tr>
<form name="formMatcher" id="formMatcher" method="post" action="${path}/DrugMatcher/update" target="submitSelf">
	<input type="Hidden" name="Oper"/>
	<input type="Hidden" name="drug_map_id" value="${drug_map_id}"/>
<%--	<input type="Hidden" name="page" value="${CommonUtils.getRequestParameter(request, "page", "1')}"/> --%>
	<tr height="22">
		<td colspan="8" align="center">
			<b>药品库配对信息</b>
			(<input type="text" name="drug_id" id="drug_id"  maxlength="10" class="noline" readonly value="${cr.get('Drug_ID')}"/>)
			<input type="button" onclick="clearInfo()" value="清除"/>
		</td>
	</tr>
	<tr height="22">
		<td  style="text-align: right;">药品名称:</td>
		<td  style="text-align: left;">
			<input type="hidden" name="dose_per_unit_pdss" value="${cr.get('dose_per_unit_pdss')}">
			<input type="hidden" name="dose_units_pdss" value="${cr.get('dose_units_pdss')}">
			<input type="hidden" name="drug_indicator_pdss" value="${cr.get('drug_indicator_pdss')}">
			<input type="hidden" name="toxi_property_pdss" value="${cr.get('toxi_property_pdss')}">
			<input  type="text" name="drug_name" value="${cr.get('drug_name')}"   readonly="readonly" maxlength="255" />
		</td>
		<td  style="text-align: right;">规格:</td>
		<td  style="text-align: left;">
			<input  type="text" id="drugspec" name="drug_spec_pdss" value="${cr.get('drug_spec_pdss')}"  maxlength="255" />
		</td>
		<td  style="text-align: right;">剂型:</td>
		<td  style="text-align: left;">
			<input  type="text" id="drugform" name="drug_form_pdss" value="${cr.get('drug_form_pdss')}"   maxlength="24" />
		</td>
		<td  style="text-align: right;">单位:</td>
		<td  style="text-align: left;">
			<input  type="text" id="unit" name="units_pdss" value="${cr.get('units_pdss')}" maxlength="255" />
		</td>
	</tr>
	<tr height="22">
		<td colspan="8" align="center"><b>附加信息</b></td>
	</tr>
	<tr height="22">
		<td  style="text-align: right;" nowrap="nowrap">国家基本药物:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_basedrug" value="1" <c:if test="${'1'==cr.get('is_basedrug')}" >checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_basedrug" value="0" <c:if test="${'0'==cr.get('is_basedrug')}" >checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">兴奋剂:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_exhilarant" value="1" <c:if test="${'1'==cr.get('is_exhilarant')}"  >checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_exhilarant" value="0" <c:if test="${'0'==cr.get('is_exhilarant')}"  >checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">注射剂:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_injection" value="1" <c:if test="${'1'==cr.get('is_injection')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_injection" value="0" <c:if test="${'0'==cr.get('is_injection')}" > checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;" nowrap="nowrap">口服制剂:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_oral" value="1" <c:if test="${'1'==cr.get('is_oral')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_oral" value="0" <c:if test="${'0'==cr.get('is_oral')}" > checked='checked' </c:if>>否
		</td>
	</tr>
	<tr height="22">
		<td  style="text-align: right;">溶剂:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_impregnant" value="1" <c:if test="${'1'==cr.get('is_impregnant')}"  >checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_impregnant" value="0" <c:if test="${'0'==cr.get('is_impregnant')}"  >checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">OTC:</td>
		<td style="text-align: left;" colspan="1">
			<input type="radio" class="noline" name="is_otc" value="1" <c:if test="${'1'==cr.get('is_otc')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_otc" value="0" <c:if test="${'0'==cr.get('is_otc')}" > checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">辅助用药:</td>
		<td style="text-align: left;" colspan="1">
			<input type="radio" class="noline" name="is_assist" value="1" <c:if test="${'1'==cr.get('is_assist')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_assist" value="0" <c:if test="${'0'==cr.get('is_assist')}" > checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">外用:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_external" value="1" <c:if test="${'1'==cr.get('is_external')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_external" value="0" <c:if test="${'0'==cr.get('is_external')}" > checked='checked' </c:if>>否
		</td>
	</tr>
	<tr height="22">
		<td  style="text-align: right;">中药:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_chinesedrug" value="1" <c:if test="${'1'==cr.get('is_chinesedrug')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_chinesedrug" value="0" <c:if test="${'0'==cr.get('is_chinesedrug')}" > checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">抗过敏:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_allergy" value="1" <c:if test="${'1'==cr.get('is_allergy')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_allergy" value="0" <c:if test="${'0'==cr.get('is_allergy')}"  >checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">中成药:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_patentdrug" value="1" <c:if test="${'1'==cr.get('is_patentdrug')}"  >checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_patentdrug" value="0" <c:if test="${'0'==cr.get('is_patentdrug')}"  >checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">抗肿瘤:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_tumor" value="1" <c:if test="${'1'==cr.get('is_tumor')}"  >checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_tumor" value="0" <c:if test="${'0'==cr.get('is_tumor')}" > checked='checked' </c:if>>否
		</td>
	</tr>
	<tr height="22">
		<td  style="text-align: right;">毒性药:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_poison" value="1" <c:if test="${'1'==cr.get('is_poison')}"  >checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_poison" value="0" <c:if test="${'0'==cr.get('is_poison')}"  >checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">精神类药:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_psychotic" value="0" <c:if test="${'0'==cr.get('is_psychotic')}"  >checked='checked' </c:if>>否
			<input type="radio" class="noline" name="is_psychotic" value="1" <c:if test="${'1'==cr.get('is_psychotic')}" > checked='checked' </c:if>>I类
			<input type="radio" class="noline" name="is_psychotic" value="2" <c:if test="${'2'==cr.get('is_psychotic')}" > checked='checked' </c:if>>II类
		</td>
		<td  style="text-align: right;">麻药:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_habitforming" value="1" <c:if test="${'1'==cr.get('is_habitforming')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_habitforming" value="0" <c:if test="${'0'==cr.get('is_habitforming')}">  checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">放射:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_radiation" value="1" <c:if test="${'1'==cr.get('is_radiation')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_radiation" value="0" <c:if test="${'0'==cr.get('is_radiation')}" > checked='checked' </c:if>>否
		</td>
	</tr>
	<tr height="22">
		<td  style="text-align: right;">贵重药:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_precious" value="1" <c:if test="${'1'==cr.get('is_precious')}" > checked='checked' </c:if>>是
			<input type="radio" class="noline" name="is_precious" value="0" <c:if test="${'0'==cr.get('is_precious')}">  checked='checked' </c:if>>否
		</td>
		<td  style="text-align: right;">高危药:</td>
		<td style="text-align: left;">
            <select name="is_danger">
                <option value="0">非高危药</option>
                <option value="1" <c:if test="${'1'==cr.get('is_danger')}" > selected='selected' </c:if> >A级</option>
                <option value="2" <c:if test="${'2'==cr.get('is_danger')}" > selected='selected' </c:if> >B级</option>
                <option value="3" <c:if test="${'3'==cr.get('is_danger')}" > selected='selected' </c:if> >C级</option>
            </select>
		</td>
		<td  style="text-align: right;">药理分类:</td>
		<td style="text-align: left;">
			<select name="pharm_catalog">
				<option value=""></option>
				<option value="1" <c:if test="${'1'==cr.get('pharm_catalog')}"> selected='selected' </c:if> >药品</option>
				<option value="2" <c:if test="${'2'==cr.get('pharm_catalog')}"> selected='selected' </c:if> >中草药</option>
				<option value="3" <c:if test="${'3'==cr.get('pharm_catalog')}"> selected='selected' </c:if> >中成药</option>
				<option value="4" <c:if test="${'4'==cr.get('pharm_catalog')}"> selected='selected' </c:if> >原料</option>
				<option value="5" <c:if test="${'5'==cr.get('pharm_catalog')}"> selected='selected' </c:if> >化学试剂</option>
				<option value="6" <c:if test="${'6'==cr.get('pharm_catalog')}"> selected='selected' </c:if> >敷料</option>
				<option value="9" <c:if test="${'9'==cr.get('pharm_catalog')}"> selected='selected' </c:if> >其它</option>
			</select>
		</td>
	</tr>
	<tr>
		<td  style="text-align: right;">分类标识:</td>
		<td style="text-align: left;" colspan="7">
			<input type="text" name="drug_catalog" width="100%;"  value="${cr.get('drug_catalog')}" maxlength="50"  />
		</td>
	</tr>
    <tr height="22">
        <th colspan="8">药品附加信息<input type="hidden" name="ybnum" value="21" /></th>
    </tr>
    <tr height="22">
        <td  style="text-align: right;">激素类:</td>
        <td style="text-align: left;" colspan="1">
            <input type="radio" class="noline" name="is_hormone" value="1" <c:if test="${'1'==cr.get('IS_HORMONE')}"> checked='checked' </c:if>>是
            <input type="radio" class="noline" name="is_hormone" value="0" <c:if test="${'0'==cr.get('IS_HORMONE')}"> checked='checked' </c:if>>否
        </td>
        <td style="text-align: right;">心脑血管类:</td>
        <td style="text-align: left;" colspan="1">
            <input type="radio" class="noline" name="is_cardiovascular" value="1" <c:if test="${'1'==cr.get('IS_CARDIOVASCULAR')}"> checked='checked' </c:if>>是
            <input type="radio" class="noline" name="is_cardiovascular" value="0" <c:if test="${'0'==cr.get('IS_CARDIOVASCULAR')}"> checked='checked' </c:if>>否
        </td>
        <td  style="text-align: right;">消化系统:</td>
        <td style="text-align: left;" colspan="1">
            <input type="radio" class="noline" name="is_digestive" value="1" <c:if test="${'1'==cr.get('IS_DIGESTIVE')}"> checked='checked' </c:if>>是
            <input type="radio" class="noline" name="is_digestive" value="0" <c:if test="${'0'==cr.get('IS_DIGESTIVE')}"> checked='checked' </c:if>>否
        </td>
        <td  style="text-align: right;">生物制剂:</td>
        <td style="text-align: left;" colspan="1">
            <input type="radio" class="noline" name="is_biological" value="1" <c:if test="${'1'==cr.get('IS_BIOLOGICAL')}"> checked='checked' </c:if>>是
            <input type="radio" class="noline" name="is_biological" value="0" <c:if test="${'0'==cr.get('IS_BIOLOGICAL')}"> checked='checked' </c:if>>否
        </td>
    </tr>
	<tr height="22">
		<th colspan="8"   >医保信息<input type="hidden" name="ybnum" value="21" /></th>
	</tr>
	<tr height="22">
		<td style="text-align: left;" colspan="8">
			<input type="checkbox" class="noline" name="yb1" value="1" <c:if test="${is_medcare1=='1'}">checked='checked'</c:if>>国家医保
			<input type="checkbox" class="noline" name="yb2" value="1" <c:if test="${is_medcare2=='1'}">checked='checked'</c:if>>地方医保
			<input type="checkbox" class="noline" name="yb3" value="1" <c:if test="${is_medcare3=='1'}">checked='checked'</c:if>>保健一级
			<input type="checkbox" class="noline" name="yb4" value="1" <c:if test="${is_medcare4=='1'}">checked='checked'</c:if>>保健二级
			<input type="checkbox" class="noline" name="yb5" value="1" <c:if test="${is_medcare5=='1'}">checked='checked'</c:if>>居民医保
			<input type="checkbox" class="noline" name="yb6" value="1" <c:if test="${is_medcare6=='1'}">checked='checked'</c:if>>重点保健
			<input type="checkbox" class="noline" name="yb7" value="1" <c:if test="${is_medcare7=='1'}">checked='checked'</c:if>>军队医改
			<input type="checkbox" class="noline" name="yb8" value="1" <c:if test="${is_medcare8=='1'}">checked='checked'</c:if>>军区不孕
			<input type="checkbox" class="noline" name="yb9" value="1" <c:if test="${is_medcare9=='1'}">checked='checked'</c:if>>免费
			<input type="checkbox" class="noline" name="yb10" value="1" <c:if test="${is_medcare10=='1'}">checked='checked'</c:if>>不孕不育
			<input type="checkbox" class="noline" name="yb11" value="1" <c:if test="${is_medcare11=='1'}">checked='checked'</c:if>>512医保
			<input type="checkbox" class="noline" name="yb12" value="1" <c:if test="${is_medcare12=='1'}">checked='checked'</c:if>>离休医保
			<input type="checkbox" class="noline" name="yb13" value="1" <c:if test="${is_medcare13=='1'}">checked='checked'</c:if>>农合医保
			<input type="checkbox" class="noline" name="yb14" value="1" <c:if test="${is_medcare14=='1'}">checked='checked'</c:if>>医保
			<input type="checkbox" class="noline" name="yb15" value="1" <c:if test="${is_medcare15=='1'}">checked='checked'</c:if>>省异地
			<input type="checkbox" class="noline" name="yb16" value="1" <c:if test="${is_medcare16=='1'}">checked='checked'</c:if>>市医保
			<input type="checkbox" class="noline" name="yb17" value="1" <c:if test="${is_medcare17=='1'}">checked='checked'</c:if>>铁路医保
			<input type="checkbox" class="noline" name="yb18" value="1" <c:if test="${is_medcare18=='1'}">checked='checked'</c:if>>铁路离休
			<input type="checkbox" class="noline" name="yb19" value="1" <c:if test="${is_medcare19=='1'}">checked='checked'</c:if>>铁路保健
			<input type="checkbox" class="noline" name="yb20" value="1" <c:if test="${is_medcare20=='1'}">checked='checked'</c:if>>铁路512
			<input type="checkbox" class="noline" name="yb21" value="1" <c:if test="${is_medcare21=='1'}">checked='checked'</c:if>>异地医保
		</td>
	</tr>
	<tr height="22">
		<th colspan="8">抗菌药属性</th>
	</tr>
	<tr height="22">
		<td  style="text-align: right;">抗菌药:</td>
		<td style="text-align: left;">
			<input type="radio" class="noline" name="is_anti" value="1" <c:if  test="${'1'==cr.get('is_anti')}"> checked='checked'</c:if>  onclick="ThisCheckAnti(this.value)">是
			<input type="radio" class="noline" name="is_anti" value="0" <c:if  test="${'0'==cr.get('is_anti')}">checked='checked'</c:if> onclick="ThisCheckAnti(this.value)">否
		</td>
        <td  style="text-align: right;">作用范围:</td>
        <td style="text-align: left;">
            <%
            /*处理逻辑
            抗菌药分为外用和内用，全身就是内用，外用就是局部，当是抗菌药时is_part起作用，当不是抗菌药时is_external 起作用。
            */
            %>
            <input type="radio" class="noline" name="is_part" value="0" <c:if  test="${'0'==cr.get('is_part')}">checked='checked'</c:if> >全身
            <input type="radio" class="noline" name="is_part" value="1" <c:if  test="${'1'==cr.get('is_part')}">checked='checked'</c:if>  >局部
        </td>
		<td  style="text-align: right;" >抗菌药级别：</td>
		<td style="text-align: left;" colspan="3">
			<input type="radio" name="anti_level" <c:if test="${'1'==cr.get('is_anti')}"> disabled='disabled'</c:if> <c:if  test="${cr.get('anti_level')=='1'}" > checked='checked'</c:if>  class="noline" value="1" onclick="isAnti('1')">非限制
			<input type="radio" name="anti_level" <c:if test="${'1'==cr.get('is_anti')}"> disabled='disabled'</c:if> <c:if  test="${cr.get('anti_level')=='2'}" > checked='checked'</c:if>  class="noline" value="2" onclick="isAnti('1')">限制
			<input type="radio" name="anti_level" <c:if test="${'1'==cr.get('is_anti')}"> disabled='disabled'</c:if> <c:if  test="${cr.get('anti_level')=='3'}" > checked='checked'</c:if>  class="noline" value="3" onclick="isAnti('1')">特殊
		</td>
	</tr>
	<tr>
		<td  style="text-align: right;">DDD值:</td>
		<td style="text-align: left;">
			<input  type="text"  name="ddd_value" id="ddd_value" <c:if test="${''== cr.get('is_anti') || '1'==cr.get('is_anti')}"> disabled='disabled'</c:if> value="${cr.get('ddd_value')}" size="15" ondragenter="return false" style="ime-mode:disabled" />
		</td>
		<td  style="text-align: right;" nowrap="nowrap">DDD剂量单位</td>
		<td style="text-align: left;">
			<input  type="text"  name="ddd_per_unit" id="ddd_per_unit" <c:if test="${''== cr.get('is_anti') || '1'==cr.get('is_anti')}"> disabled='disabled'</c:if> value="${cr.get('ddd_per_unit')}" size="15" maxlength="10" />
			<input  class="noline" name="ddd_unit" id="ddd_unit_g"  <c:if test="${''== cr.get('is_anti') || '1'==cr.get('is_anti')}"> disabled='disabled'</c:if> value="g" checked="checked" type="radio">g </input>
			<input  class="noline" name="ddd_unit" id="ddd_unit_mg" <c:if test="${''== cr.get('is_anti') || '1'==cr.get('is_anti')}"> disabled='disabled'</c:if> value="mg" <c:if test="${'mg'==cr.get('ddd_unit')}"> checked='checked' </c:if> type="radio">mg</input>
		</td>
		<td  style="text-align: right;"  nowrap="nowrap">单日最大剂量:</td>
		<td style="text-align: left;" colspan="3">
			<input  type="text"  name="ddd_value_x" id="ddd_value_x" <c:if test="${''== cr.get('is_anti') || '1'==cr.get('is_anti')}"> disabled='disabled' </c:if> value="${cr.get('ddd_value_x')}" size="15" style="ime-mode:disabled" />
		</td>
	</tr>
	<tr height="22">
		<td colspan="8" align="center">
			<input type="button" onclick="SaveIt()" value="  保存  "/>
			<input type="button"  onclick="top.Dialog.close();" value="  关闭  "/>
		</td>
	</tr>
</form>
</table>
</table>
<table width="100%" height="100%" class="table table-striped table-bordered table-hover"  >
	<tr height="22">
		<th colspan="8" align="center" style="text-align: center;vertical-align: middle;">药品匹配</th>
	</tr>
	<tr height="22">
		<td colspan="8">
			<form target="childs" action="DrugMatcher/queryPDSSList.do" method="post">
				药品名称：<input   type="text"   name="drugName" value="${cr.get('Drug_Name_Local')}" size="35"/>
				<input type="submit" value="查询 "/>
			</form>
		</td>
	</tr>
	<tr height="*" style="background-color: white;">
		<td colspan="8">
			<iframe width="100%" height="100%" frameborder="0" name="childs" src="${path}/DrugMatcher/queryPDSSList.do"></iframe>
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

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

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
	<script type="text/javascript" src="static/js/common/common.js"></script>
	</body>
<script type="text/javascript" src="static/js/common/lockTable.js?v=201612"></script>
<script type="text/javascript">
$(top.hangge());
//检索
function searchs(){
	top.jzts();
	$("#formMatcher").submit();
}
$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
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
	}
});

//保存药品信息
function SaveIt()
{
	if($("radio[name='is_anti']").val()==0)
	{
		if($("#ddd_value").value() == "")
		{
			if(!confirm("抗菌药物信息填写不全，是否继续保存!"))return							
		}
	}
	$.ajax({
		type: "POST",
		url: '${path}/DrugMatcher/update.do',
    	data: $("#formMatcher").serialize(),
		dataType:'json',
		cache: false,
		success: function(data){
			top.Dialog.close();
		}
	});
}

window.name="submitSelf";

function saveAndCloseMe(){
    with(document.formMaster){
        submit();
    }
    window.close();
}
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
	var is_part  = document.getElementsByName("is_part");
	if (is_anti_val == '1')
	{	for (var i = 0; i < is_part.length; i++)
		is_part[i].disabled = "";
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
	{for (var i = 0; i < is_part.length; i++)
		is_part[i].disabled = "disabled";
		for (var i = 0; i < antiLevelObj.length; i++)
			antiLevelObj[i].disabled = "disabled";
		for (var i = 0; i < dddUnitObj.length; i++)
			dddUnitObj[i].disabled = "disabled";
		dddValObj.disabled     = "disabled";
		dddPerUnitObj.disabled = "disabled";
		dddValXObj.disabled    = "disabled";
	}
}
</script>
</html>