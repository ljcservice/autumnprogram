<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.DBUtils.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/Others.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/WebPage/public/Common.js"></script>
	<title>用户信息</title>
	<script type="text/javascript">
		function ThisBack()
		{
			with (document.formPost)
			{
				o.value = "query";	
				submit();
			}
		}
		function ThisUpdate()
		{
			with (document.formPost)
			{
				if(checkValue(elements))
				{
					return ;
				}
				o.value = "updUnit";
				submit();
			}
		}
	</script>
</head>
<body leftmargin="0" topmargin="0" style="background-color:white">
	<form name="formPost" action="<%=request.getContextPath()%>/control/UserInfo" method="post">
        <input type="hidden" name="o" value="query">
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>"/>
		<input type="hidden" name="Q_USERID" value="<%=CommonUtils.getRequestParameter(request, "Q_USERID", "") %>">
		<input type="hidden" name="Q_USERNAME" value="<%=CommonUtils.getRequestParameter(request, "Q_USERNAME", "") %>">
		<input type="hidden" name="Q_USERUNIT" value="<%=CommonUtils.getRequestParameter(request, "Q_USERUNIT", "") %>">
		<%
			TCommonRecord entity = (TCommonRecord) request.getAttribute("entity");
			if (entity == null)
				entity = new TCommonRecord();
		%>
		<input type="hidden" name="PWD" value="<%=entity.get("USERPWD")%>">
		<table border="0" cellpadding="2" cellspacing="1" style="background-color: #D9D9D9" align="center" width="95%">
			<tr height="30" bgcolor="#FAFAF1">
				<th colspan="2">选择用户所属医院</th>
			</tr>
			<tr>
				<td bgcolor="#FAFAF1" style="font-weight: bold;" align="right">用户ID:</td>
				<td bgcolor="#FFFFFF" align="left">
					<input type="text" maxlength="25" name="USERID" value="<%=entity.get("USERID")%>" readonly="readonly" class="noline">
				</td>
			</tr>
			<tr>
		    	<td bgcolor="#FAFAF1" style="font-weight: bold;" align="right">用户姓名:</td>
				<td bgcolor="#FFFFFF" align="left">
					<input type="text" maxlength="20" name="USERNAME" value="<%=entity.get("USERNAME")%>" readonly="readonly" class="noline">
				</td>
			</tr>
			<tr>
				<td bgcolor="#FAFAF1" style="font-weight: bold;" align="right">所属医院:</td>
				<td bgcolor="#FFFFFF" align="left">
					<select name="USERUNIT" Required="true" RequiredValue="所属医院必选！">
						<option value=""></option>
						<%
							List<TCommonRecord> hosList = (List<TCommonRecord>) request.getAttribute("List");
							if (hosList != null)
							{
								for (TCommonRecord hos : hosList)
								{
						%>
						<option value="<%=hos.get("HOSPITAL_CODE") %>" <%=hos.get("HOSPITAL_CODE").equals(entity.get("USERUNIT")) ? "selected='selected'" : "" %>><%=hos.get("HOSPITAL_NAME") %></option>
						<%
								}
							}
						%>
					</select>
				</td>
			</tr>
			<tr height="22">
				<td align="center" bgcolor="#EEF4EA" colspan="2">
					<img alt="保存" src="<%=request.getContextPath() %>/images/frame/save.jpg" onclick="javascript:ThisUpdate();" style="cursor: pointer;" align="absmiddle">
					<img alt="取消" src="<%=request.getContextPath() %>/images/frame/cal.jpg" onclick="javascript:ThisBack();" style="cursor: pointer;" align="absmiddle">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>