<%@page import="com.hitzd.his.sso.SSOController"%>
<%@page import="com.hitzd.his.tree.TreeNode"%>
<%@page import="java.util.List"%>
<%@page import="com.hitzd.his.Beans.frame.User"%>
<%@page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.his.Utils.Browser"%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<!DOCTYPE html>
<html>
<head>
<title>menu</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
	<link type="text/css" rel="stylesheet" href="/COMMON/bootstrap/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="/COMMON/hitzd/css/common.css" />
	<script type="text/javascript" src="/COMMON/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="/COMMON/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/COMMON/hitzd/js/common.js"></script>
	<script type="text/javascript">var curopenItem = '1';</script>
<script type="text/javascript" >
//菜单显示主内容 （跳转 ）
function pubFun(url,type) 
{
	var path = url
	if(type!="")
	{
		path += "?o=" + type;
	}				
	top.main.location.href = path ;
}

function right_shdow() {
	var obj  = $("body",window.parent.frames["main"].document);
	$(obj).ajaxMask();
}
</script>
</head>
<body target="main" class="sidebar-page container <%=Browser.getBrowserName(request)%>">
	<div class="sidebar-container span1">
        <div class="sidebar">
        	
    		<%
		    TreeNode childs = (TreeNode)request.getAttribute("nodes");
		    for(int i = 0 ; i < childs.getChildCount() ; i ++)
		   	{
		    	TreeNode nodes = childs.getChild(i);
		    	if (nodes.getChildCount() > 0)
		    	{
			%>
					<div class="mc">
						<div class="nav-header"><%=nodes.getMenuCaption() %></div>
						<ul>
			<%
				   		for(int j = 0; j < nodes.getChildCount() ; j ++)
				   		{
				   			TreeNode node = nodes.getChild(j);
				   			String action = "control";
				   			if (node.getMenuIsRpt().equals("1"))
				   				action = "report";
				   			String Program = request.getContextPath();
				   			String url = node.getMenuID();
				   			String param = node.getMenuParam() + "&" + TreeNode.URLMenuID + "=" + node.getMenuID();
				   			if (node.getRefMenu() != null)
				   			{
				   				Program = "/" + node.getRefMenu().getProgramID();
				   				url = node.getRefMenu().getMenuID();
				   				if (node.getRefMenu().getMenuParam().length() > 0)
				   					param += "&" + node.getRefMenu().getMenuParam();
				   				param += "&token=" + (String)session.getAttribute(SSOController.Token);
				   			}
						%>
							<li class="nav-item"><a onclick="javascript:pubFun('<%=Program%>/<%=action%>/<%=url%>','<%=param%>');right_shdow(); " target="main"><%=node.getMenuCaption() %></a></li>	
						<% } %>
				   		</ul>
					</div>
			<%	
			    	}
			   	}
			%>
          
        </div>
  	</div>	
</body>
</html>