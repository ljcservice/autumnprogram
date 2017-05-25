<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
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
	String perform_freq_dict_map_id = "".equals(mapEntity.get("perform_freq_dict_map_id")) ? CommonUtils.getRequestParameter(request, "perform_freq_dict_map_id", "") : mapEntity.get("perform_freq_dict_map_id");
%>
<html>
<head>
	<title>给药频次配码</title>
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
	<script type="text/javascript" src="<%=request.getContextPath() %>/WebPage/public/Common.js"></script>
	<script type="text/javascript">
		window.onload = function(){
			window.frames[0].document.formPost.q_perform_freq_dict_name.value = "<%=hisEntity.get("freq_desc")%>";
		};
		function ThisSave()
		{
			with (document.formPost)
			{
				if (perform_freq_dict_id.value == "")
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
<body>
	<form name="formPost" action="<%=request.getContextPath() %>/control/PerformFreqMatcher" method="post" target="aa">
		<input type="hidden" name="o" value="update">
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
		<input type="hidden" name="perform_freq_dict_map_id" value="<%=perform_freq_dict_map_id%>">
		<table width="99%" height="99%" border="1" cellpadding="0" cellspacing="1" ">
			<tr height="22">
				<td class="td10" colspan="4" align="center"><b>本院信息</b></td>  
			</tr>
			<tr height="22">
				<td class="td20">给药频次描述：</td>
				<td class="td11" >
					<input type="hidden" name="serial_no" value="<%=hisEntity.get("serial_no") %>" />
					<input type="hidden" name="perform_freq_dict_no_local" value="<%=hisEntity.get("serial_no") %>" />
					<input type="hidden" name="perform_freq_dict_name_local" value="<%=hisEntity.get("freq_desc") %>" />
					<input name="freq_desc" value="<%=hisEntity.get("freq_desc") %>" readonly="readonly" class="noline" />
				</td>
				<td class="td20" >频率次数：</td>
				<td class="td11" >
					<input name="freq_counter" value="<%=hisEntity.get("freq_counter")%>" readonly="readonly" class="noline" />
				</td>
			</tr>
			<tr height="22">
				<td class="td20">频率间隔：</td>
				<td class="td11" >
					<input name="freq_interval" value="<%=hisEntity.get("freq_interval") %>" readonly="readonly" class="noline" />
				</td>
				<td class="td20" >频率间隔单位：</td>
				<td class="td11" >
					<input name="freq_interval_units" value="<%=hisEntity.get("freq_interval_units")%>" readonly="readonly" class="noline" />
				</td>
			</tr>
			
			
			<tr height="22">
				<td class="td10" colspan="4" align="center"><b>对照信息</b></td>  
			</tr>
			<tr height="22">
				<td class="td20">给药频次描述：</td>
				<td class="td11" >
					<input type="hidden" name="perform_freq_dict_id" value="<%=mapEntity.get("perform_freq_dict_id") %>" />
					<input name="perform_freq_dict_name" value="<%=mapEntity.get("perform_freq_dict_name") %>" maxlength="16" />
				</td>
				<td class="td20" >频率次数：</td>
				<td class="td11" >
					<input name="freq_counter_pdss" value="<%=mapEntity.get("freq_counter_pdss")%>" maxlength="2" onkeypress="return vaildIntegerNumber(event, this)" onpaste="return !clipboardData.getData('text').match(/\D/)" ondragenter="return false" style="ime-mode:disabled" />
				</td>
			</tr>
			<tr height="22">
				<td class="td20">频率间隔：</td>
				<td class="td11" >
					<input name="freq_interval_pdss" value="<%=mapEntity.get("freq_interval_pdss") %>" maxlength="2" onkeypress="return vaildIntegerNumber(event, this)" onpaste="return !clipboardData.getData('text').match(/\D/)" ondragenter="return false" style="ime-mode:disabled" />
				</td>
				<td class="td20" >频率间隔单位：</td>
				<td class="td11" >
					<input name="freq_interval_units_pdss" value="<%=mapEntity.get("freq_interval_units_pdss")%>" maxlength="4" />
					<input type="hidden" name="frequency_pdss" value="<%=mapEntity.get("frequency_pdss")%>" maxlength="5" />
				</td>
			</tr>
			<tr height="22">
				<td class="td10" colspan="4" align="center">
					<img alt="保存" src="<%=request.getContextPath() %>/images/frame/save.jpg" onclick="ThisSave();" style="cursor: pointer;" align="absmiddle">
					<img alt="关闭" src="<%=request.getContextPath() %>/images/frame/close.jpg" onclick="ThisClose();" style="cursor: pointer;" align="absmiddle"> 
				</td>
			</tr>
		</table>
		<iframe name="childs" src="<%=request.getContextPath() %>/WebPage/PerformFreqMatcher/Dict_Query.jsp" height="360" width="99%"></iframe>
	</form>
</body>
</html>