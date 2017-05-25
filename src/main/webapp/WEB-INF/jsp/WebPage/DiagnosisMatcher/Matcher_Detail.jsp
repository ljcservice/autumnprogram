<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.his.Utils.*" %>
<%@ page import="com.hitzd.DBUtils.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	TCommonRecord hisEntity = (TCommonRecord) request.getAttribute("hisEntity");
	if (hisEntity == null)
		hisEntity = new TCommonRecord();
	TCommonRecord mapEntity = (TCommonRecord) request.getAttribute("mapEntity");
	if (mapEntity == null)
		mapEntity = new TCommonRecord();
	String diagnosis_map_id = "".equals(mapEntity.get("diagnosis_map_id")) ? CommonUtils.getRequestParameter(request, "diagnosis_map_id", "") : mapEntity.get("diagnosis_map_id");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>
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
	text-align: right;
}
.td21
{
	background-color: #E5FFF2;
}
</style>
<title>药品诊断</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/WebPage/public/Common.js"></script>
<script>
	window.onload = function(){
		window.frames[0].document.formPost.q_diagnosis_code.value = "<%=hisEntity.get("diagnosis_code") %>";
		window.frames[0].document.formPost.q_diagnosis_name.value = "<%=hisEntity.get("diagnosis_name") %>";
		window.status = "药品配码";
	};
	function ThisSave()
	{
		with (document.formPost)
		{
			if (diagnosis_id.value == "")
			{
				alert("无对照信息，无法保存！");
				return;
			}
			o.value = "update";
			submit();
		}
	}
	
	function ThisClose()
	{
		if(confirm("是否确认关闭窗口？"))
		{
			window.returnValue = document.formPost.page.value;
			close();
		}
	}
	window.name="aa";
</script>
</head>

<body style="margin: 0 0 0 0;" >
<table width="99%" height="99%" border="1" cellpadding="0" cellspacing="1" ">
	<tr height="22">
		<td class="td10" colspan="6" align="center"><b>本院信息</b></td>  
	</tr>
<form name="formPost" action="<%=request.getContextPath() %>/control/DiagnosisMatcher" method="post" target="aa">
		<input type="hidden" name="o" value="update">
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
		<input type="hidden" name="diagnosis_map_id" value="<%=diagnosis_map_id%>">
	<tr height="22">
		<td class="td20">诊断码：</td>
		<td class="td11" colspan="2">
			<input type="hidden" name="std_indicator" value="<%=hisEntity.get("std_indicator")%>">
			<input type="hidden" name="approved_indicator" value="<%=hisEntity.get("approved_indicator")%>">
			<input type="hidden" name="input_code" value="<%=hisEntity.get("input_code")%>">
			<input type="hidden" name="infect_indicator" value="<%=hisEntity.get("infect_indicator")%>">
			<input type="hidden" name="health_level" value="<%=hisEntity.get("health_level")%>">
			<input type="hidden" name="outp_drug_day" value="<%=hisEntity.get("outp_drug_day")%>">
			<input name="diagnosis_no_local" value="<%=hisEntity.get("diagnosis_code") %>" readonly="readonly" class="noline" />
		</td>
		<td class="td20" >诊断名称:</td>
		<td class="td11" colspan="2">
			<input type="hidden" name="diagnosis_name_his" value="<%=hisEntity.get("diagnosis_name")%>" />
			<input name="diagnosis_name_local" value="<%=hisEntity.get("diagnosis_name")%>" readonly="readonly" class="noline" />
		</td>
	</tr>
	<tr height="22">
		<td class="td10" colspan="6" align="center"><b>对照信息</b></td>  
	</tr>
	<tr height="22">
		<td class="td20">诊断码：</td>
		<td class="td11" colspan="2">
			<input type="hidden" name="diagnosis_id" value="<%=mapEntity.get("diagnosis_id") %>" />
			<input type="hidden" name="diagnosis_code" value="<%=mapEntity.get("diagnosis_code") %>" maxlength="24" />
			<input type="hidden" name="diagnosis_code2" value="<%=mapEntity.get("diagnosis_code2") %>" maxlength="16" />
			<input type="text" name="icd9"  value="<%=mapEntity.get("icd9") %>" readonly="readonly" maxlength="20" />
		</td>
		<td class="td20" >诊断名称：</td>
		<td class="td11" colspan="2">
			<input name="diagnosis_name" value="<%=mapEntity.get("diagnosis_name")%>" readonly="readonly"  maxlength="255" />
		</td>
	</tr>
	<tr height="22">
		<td class="td20">诊断分类：</td>
		<td class="td11">
			<input type="text" name="diagnosis_class" value="<%=mapEntity.get("diagnosis_class") %>" readonly="readonly" maxlength="255" />
		</td>
		<td class="td20" >诊断分类代码：</td>
		<td class="td11">
			<input type="text" name="diagnosis_class_code" value="<%=mapEntity.get("diagnosis_class_code") %>" readonly="readonly" maxlength="24" />
		</td>
		<td class="td20" >肾病标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="renal_indi" value="1" <%="1".equals(mapEntity.get("renal_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="renal_indi" value="0" <%="0".equals(mapEntity.get("renal_indi")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	<tr height="22">
		<td class="td20">肝病标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="hepatic_indi" value="1" <%="1".equals(mapEntity.get("hepatic_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="hepatic_indi" value="0" <%="0".equals(mapEntity.get("hepatic_indi")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20" >心脏病标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="cardio_idi" value="1" <%="1".equals(mapEntity.get("cardio_idi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="cardio_idi" value="0" <%="0".equals(mapEntity.get("cardio_idi")) ? "checked='checked'" : ""%>>否		
		</td>
		<td class="td20" >肺病标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="pulm_indi" value="1" <%="1".equals(mapEntity.get("pulm_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="pulm_indi" value="0" <%="0".equals(mapEntity.get("pulm_indi")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	<tr height="22">
		<td class="td20">神经疾病标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="neur_indi" value="1" <%="1".equals(mapEntity.get("neur_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="neur_indi" value="0" <%="0".equals(mapEntity.get("neur_indi")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20" >内分泌病标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="endo_indi" value="1" <%="1".equals(mapEntity.get("endo_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="endo_indi" value="0" <%="0".equals(mapEntity.get("endo_indi")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20" >急慢性标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="acute_indi" value="1" <%="1".equals(mapEntity.get("acute_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="acute_indi" value="0" <%="0".equals(mapEntity.get("acute_indi")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	
	<tr height="22">
		<td class="td20">副作用标志：</td>
		<td class="td11" colspan="2">
			<input type="radio" class="noline" name="side_indi" value="1" <%="1".equals(mapEntity.get("side_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="side_indi" value="0" <%="0".equals(mapEntity.get("side_indi")) ? "checked='checked'" : ""%>>否
		</td>
		<td class="td20" colspan="2">疾病名称标志：</td>
		<td class="td11">
			<input type="radio" class="noline" name="diag_indi" value="1" <%="1".equals(mapEntity.get("diag_indi")) ? "checked='checked'" : ""%>>是
			<input type="radio" class="noline" name="diag_indi" value="0" <%="0".equals(mapEntity.get("diag_indi")) ? "checked='checked'" : ""%>>否
		</td>
	</tr>
	<tr height="22">
		<td class="td10" colspan="6" align="center">
			<img alt="保存" src="<%=request.getContextPath() %>/images/frame/save.jpg" onclick="ThisSave();" style="cursor: pointer;" align="absmiddle">
			<img alt="关闭" src="<%=request.getContextPath() %>/images/frame/close.jpg" onclick="ThisClose();" style="cursor: pointer;" align="absmiddle">
		</td>  
	</tr>
</form>
</table>
<iframe name="childs" src="<%=request.getContextPath() %>/WebPage/DiagnosisMatcher/Dict_Query.jsp" height="360" width="99%"></iframe>
</body>
</html>