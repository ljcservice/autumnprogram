<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@ page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.DBUtils.*"%>
<%@ page import="java.util.*"%>
<html>
<head>
	<title>用户信息</title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/WebPage/js/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/Others.css"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap.min.css" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/css/common.css" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/WebPage/public/Common.js"></script>
	<script type="text/javascript">
		function ThisModify(idx)
		{
			with (document.formPost)
			{
				o.value = "modUnit";
				USERID.value = idx;
				submit();
			}
		}
	</script>
</head>
<body leftmargin="0" topmargin="0" style="background-color:white">
	<form name="formPost" action="<%=request.getContextPath() %>/control/UserInfo" method="post">
		<input type="hidden" name="o" value="query" />
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
		<input type="hidden" name="USERID">
		<table width="100%" height="100%">
			<tr height="25">
				<td>
					<div class="search-bar-container">
						<div>
							<div class="form-inline search-bar-top" style="text-align: left;">
								<label>用户ID:</label>
								<input type="text" name="Q_USERID" value="<%=CommonUtils.getRequestParameter(request, "Q_USERID", "") %>">
								<label>用户姓名:</label>
								<input type="text" name="Q_USERNAME" value="<%=CommonUtils.getRequestParameter(request, "Q_USERNAME", "") %>">
								<%
									List<TCommonRecord> hosList = (List<TCommonRecord>) request.getAttribute("List");
									if (hosList != null)
									{
								%>
								<label>所属医院:</label>
								<select name="Q_USERUNIT">
									<option value=""></option>
									<%
										for (TCommonRecord entity : hosList)
										{
									%>
									<option value="<%=entity.get("HOSPITAL_CODE") %>" <%=entity.get("HOSPITAL_CODE").equals(CommonUtils.getRequestParameter(request, "Q_USERUNIT", "")) ? "selected='selected'" : "" %>><%=entity.get("HOSPITAL_NAME") %></option>
									<%
										}
									%>
								</select>
								<%
									}
								%>
							</div>
							<div class="form-inline search-bar-bottom" style="height: 20px">
								<span style="margin-left: 3px;float: right;">   
									<img alt="查询" src="<%=request.getContextPath() %>/images/frame/find.jpg" onclick="javascript:ThisSubmit();" style="cursor: pointer;" align="absmiddle">
								</span>
							</div>
						</div>
					</div>
				</td>
			</tr>
			<tr height="*">
				<td valign="top">
					<table width="100%" height="100%">
						<tr height="40">
							<td align="center">
								<div style="font-family:黑体;font-size:20pt;font-color:black;line-height:40px;text-align:center;">用户信息</div>
							</td>
						</tr>
						<tr>
							<td valign="top">
								<table border="0" cellpadding="2" cellspacing="1" style="background-color: #D9D9D9" align="center" width="95%">
									<tr align="center" bgcolor="#FAFAF1" height="26">
										<th width="150">用户ID</th>
										<th width="150">用户姓名</th>
										<th width="*">所属医院</th>
										<th width="100">操作</th>
									</tr>
									<%
										PageView<TCommonRecord> pageView = (PageView) request.getAttribute("pageView");
										if (pageView != null)
										{
											List<TCommonRecord> list = (List<TCommonRecord>)pageView.getRecords();
											for (TCommonRecord entity : list)
											{
									%>
									<tr align="center" bgcolor="#FFFFFF" height="22" onmousemove="javascript:this.bgColor='#E7F3A5';" onmouseout="javascript:this.bgColor='#FFFFFF';">
										<td title="<%=entity.get("USERID")%>"><%=entity.get("USERID")%></td>
										<td title="<%=entity.get("USERNAME")%>"><%=entity.get("USERNAME")%></td>
										<td title="<%=entity.get("HOSPITAL_NAME")%>"><%=entity.get("HOSPITAL_NAME")%></td>
										<td>
											<a href="javascript:ThisModify('<%=entity.get("USERID")%>');">选择所属医院</a>
										</td>
									</tr>
									<%
											}
									%>
									<tr align="right" bgcolor="#EEF4EA">
										<td height="22" colspan="4" align="center">
											<%@ include file="/WebPage/share/fenye.jsp" %>
										</td>
									</tr>
									<%
										}
									%>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>