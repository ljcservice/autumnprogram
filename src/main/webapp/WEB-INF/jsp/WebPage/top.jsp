<%@page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.his.Utils.Browser"%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<title>top</title>
	<link type="text/css" rel="stylesheet" href="/COMMON/bootstrap/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="/COMMON/hitzd/css/common.css" />
	<script type="text/javascript" src="/COMMON/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="/COMMON/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/COMMON/hitzd/js/common.js"></script>
</head>
<body class="<%=Browser.getBrowserName(request)%>" >
	<div class="navbar navbar-static-top">
      <div class="navbar-inner" >
        <div class="pull-right links">
	        你好：<%=CommonUtils.getSessionUser(request).getUserName()%>，
	        <!-- <div class="btn-group"> -->
	        	<a href="javascript:ChangeMenu(0);" class="toggle-side-nav">隐藏左菜单</a>&nbsp;|&nbsp;
				<a href="javascript:showDesktop()">显示桌面</a>&nbsp;|&nbsp;
				<a id="show-sys-switcher" href="###">切换平台</a>&nbsp;|&nbsp;
	          	<!--  <a href="<%=request.getContextPath()%>/WebPage/user/userCusUpdate.jsp" target="main">修改密码</a>&nbsp;|&nbsp;-->
			<!-- </div>
			<div class="btn-group"> -->
				<a href="javascript:Logout()">注销</a>
	        <!-- </div> -->
	        
		</div>
      </div>
    </div>
</body>
</html>