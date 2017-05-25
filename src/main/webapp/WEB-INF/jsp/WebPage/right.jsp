<%@page import="java.util.List"%>
<%@ page import="com.hitzd.his.Utils.Browser"%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>main</title>
<base target="_self">
<link rel="stylesheet" type="text/css" href="../skin/css/base.css" />
<link rel="stylesheet" type="text/css" href="../skin/css/main.css" />
<script language="javascript" type="text/javascript" src="../skin/js/servlet.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap.min.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/css/common.css" />
</head>
<body leftmargin="8" topmargin='8' class="<%=Browser.getBrowserName(request)%>">
<form name="formPost"	action="<%=request.getContextPath() %>/verifylogin" method="post">
<input name="o" value="get" type="hidden" />
<table width="98%" align="center" border="0" cellpadding="4"
	cellspacing="1" bgcolor="#CBD8AC" style="margin-bottom: 8px">
	<tr bgcolor="#FFFFFF">
		<td height="30" colspan="2" align="center" >
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr align="center" >
				<td >	
					<img src="/COMMON/hitzd/img/welcome.jpg">
				</td>
			</tr>
		
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>