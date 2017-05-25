<%@page import="com.hitzd.his.sso.SSOController"%>
<%@page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@page import="com.hitzd.his.Beans.frame.User"%>
<%@page import="com.hitzd.Utils.SystemConsts"%>
<%@ page import="com.hitzd.his.Utils.Browser"%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title><%=SystemConsts.getSystemName(request.getContextPath())%></title>
	<script type="text/javascript" src="/COMMON/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="/COMMON/jquery/jquery-migrate.min.js"></script>
	<script type="text/javascript" src="/COMMON/bootstrap/js/bootstrap.min.js"></script>
	<!--[if lte IE 6]>
	<script type="text/javascript" src="/COMMON/bootstrap/js/bootstrap-ie.js"></script>
	<![endif]-->
	<script type="text/javascript" src="/COMMON/hitzd/js/common.js"></script>
	<link type="text/css" rel="stylesheet" href="/COMMON/bootstrap/css/bootstrap.min.css" />
	<!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/COMMON/bootstrap/css/bootstrap-ie6.css">
	<link rel="stylesheet" type="text/css" href="/COMMON/bootstrap/css/ie.css">
	<![endif]-->
	<link type="text/css" rel="stylesheet" href="/COMMON/hitzd/css/common.css" />
<script language="JavaScript">  
 self.resizeTo(screen.availWidth,screen.availHeight);
 self.focus();
 function ThisLogout()
 {
	 window.location.href = "<%=request.getContextPath()%>/Verifylogin?o=logout" ;
 }
 function ThisQuit()
 {
	 ThisLogout();
	 window.close();
 }
</script>   
<script type="text/javascript">
	$().ready(function(){
		$("#sys-switcher-modal").modal({
			keyboard:false,
			show:false
		});
	});
</script>
<%
	User user    = CommonUtils.getSessionUser(request);
	String[] ups = user.getUserProgram().split(",");
%>
</head>
<body id="main-frame" class="<%=Browser.getBrowserName(request)%>">
	<div id="main-frame-top">
		<iframe src="top.jsp" frameborder="0" scrolling="no"></iframe>
	</div>
	<div id="main-frame-left">
		<iframe src="../TreeServlet" frameborder="0"></iframe>	
	</div>
	<div id="main-frame-right">
		<iframe src="right.jsp" name="main" frameborder="0"></iframe>		
	</div>
	<input id="btn-show-sys-switcher" type="button" class="hide"/>
	<div id="sys-switcher-modal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>切换系统</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered" style="background:#fff;text-align: left;">
				<%	
						//整体功能为 4个位一个行，不够补位 , 默认可以查看“医药知识在线查询平台”
						int p = 0 ;
						for(int i = 1 ; i <= ups.length ; i++)
						{
						    String up    = "/" + ups[i-1];
						    String pName = SystemConsts.getSystemName(up);
						    String pURL  = SystemConsts.getPlatFormUrl(up);  
				            if(i%4==1)
							{
	                %>
	                	<tr>
	                <%
							}
					%>  
							<td align="left" valign="top" style="text-align: left;">
								<%=i%>. <a target="_blank" href="<%=pURL + "?" + SSOController.getToken(session) %>" style="font-color:green" >
									<%=pName%>
								</a>
							</td>
					<%
							if(i%4==0)
							{
				    %>
				    	</tr>
				    <% 
							}
							p = i ; 
						}
						if(p%4!=0)
						{
				    %>
				    		<td align="left" valign="top" style="text-align: left;" colspan="<%=p>4?(4-p%4):"1" %>" >
							<%=++p%>. <a target="_blank" href="/index.jsp" style="font-color:green"><%=SystemConsts.getSystemName("/")%></a><br/>
							</td>
							</tr>
					<%
						}
						else
						{
				    %>
					    <tr>
							<td align="left" valign="top" style="text-align: left;" colspan="4">
								<%=++p%>. <a target="_blank" href="/index.jsp" style="font-color:green"><%=SystemConsts.getSystemName("/")%></a><br/>
							</td>
						</tr>
				    <% 
						}
					%>
			</table>
		</div>
	</div>
</body>
</html>