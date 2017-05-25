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
	String administration_map_id = "".equals(mapEntity.get("administration_map_id")) ? CommonUtils.getRequestParameter(request, "administration_map_id", "") : mapEntity.get("administration_map_id");
%>
<html>
<head>
	<title>给药途径配码信息</title>
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
			window.frames[0].document.formPost.q_item_name.value = "<%=hisEntity.get("administration_name")%>";
		};
		function ThisSave()
		{
			with (document.formPost)
			{
				if (administration_id.value == "")
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
	<table width="99%" height="99%" border="1" cellpadding="0" cellspacing="1" ">
	<tr height="22">
		<td class="td10" colspan="6" align="center"><b>本院信息</b></td>  
	</tr>
	<form name="formPost" action="<%=request.getContextPath() %>/control/AdministrationMatcher" method="post" target="aa">
		<input type="hidden" name="o" value="update">
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
		<input type="hidden" name="administration_map_id" value="<%=administration_map_id%>">
		<tr height="22">
			<td class="td20">给药途径码：</td>
			<td class="td11" >
				<input type="hidden" name="serial_no" value="<%=hisEntity.get("serial_no")%>">
				<input type="hidden" name="input_code" value="<%=hisEntity.get("input_code")%>">
				<input name="administration_no_local" size="30" value="<%=hisEntity.get("administration_code") %>" readonly="readonly" class="noline" />
			</td>
			<td class="td20" >给药途径名称：</td>
			<td class="td11">
				<input type="hidden" name="administration_name_his" value="<%=hisEntity.get("administration_name")%>" />
				<input name="administration_name_local" value="<%=hisEntity.get("administration_name")%>" readonly="readonly" class="noline" />
			</td>
			<td class="td20" >是否注射：</td>
			<td class="td11">
				<input name="is_injection" value="<%=hisEntity.get("is_injection")%>" readonly="readonly" class="noline" />
			</td>
		</tr>
		<tr height="22">
			<td class="td10" colspan="6" align="center"><b>对照信息</b></td>  
		</tr>
		<tr height="22">
			<td class="td20">给药途径码：</td>
			<td class="td11" colspan="2">
				<input name="administration_id" size="30" value="<%=mapEntity.get("administration_id") %>" readonly="readonly" maxlength="3" />
			</td>
			<td class="td20" >给药途径名称：</td>
			<td class="td11" colspan="2">
				<input name="administration_name" value="<%=mapEntity.get("administration_name")%>" readonly="readonly" maxlength="16" />
			</td>
		</tr>
		<tr height="22">
			<td class="td10" colspan="6" align="center">
				<img alt="保存" src="<%=request.getContextPath() %>/images/frame/save.jpg" onclick="ThisSave();" style="cursor: pointer;" align="absmiddle">
				<img alt="关闭" src="<%=request.getContextPath() %>/images/frame/close.jpg" onclick="ThisClose();" style="cursor: pointer;" align="absmiddle">
			</td>
		</tr>
	</table>
	</form>
	<iframe name="childs" src="<%=request.getContextPath() %>/control/AdministrationMatcher?o=queryPDSSList" height="380" width="99%"></iframe>
</body>
</html>