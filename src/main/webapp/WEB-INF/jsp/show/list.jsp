<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>"
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
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
						<form action="show/list.do" method="post" id="myform" name="myform">
							<input type="hidden" name="id" value="${pd.id}" id="id"/>
							<input type="hidden" name="name" value="${pd.name}" id="name"/>
							<input type="hidden" name="ONTO_TYPE" value="${pd.ONTO_TYPE}" id="ONTO_TYPE"/>
						</form>
						<div id="myTitle">
							<table style="width: 100%;">
								<tr>
									<td align="left"><b >搜索结果</b></td>
									<td align="right"><b >搜索到含“<font style="color: blue;">${pd.name}</font>”的结果共有<font style="color: blue;">${resultList.size()}</font>条 </b>&nbsp;&nbsp;</td>
								</tr>
							</table>
						</div>
						<div id="resultDiv" style="overflow: auto;">
							<c:forEach items="${resultList}" var="item" varStatus="vs">
								<div style="padding: 5px;">
									<h4 style="color: blue;"><a href="javascript:detailPage('${item.DIRECTION_NO }');">${item.drug_name } - ${item.firm_name }</a></h4>
									<div>
										<div>[成分]：${item.INGREDIENT }
										</div>
										<div>[适应症]：${item.INDICATION }
										</div>
									</div>
									<div style="width:100%;height:2px;border-bottom: 1px dotted #e2e2e2;"></div>
								</div>
							</c:forEach>
						</div>
						</div></div></div>
						<div class= "pageStrDiv" id="pageStrDiv" style="padding : 3px;">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;" nowrap="nowrap">
										<div class="pagination" style="float: right;padding: 0px;margin: 0px;">${page.pageStr}</div>
									</td>
								</tr>
							</table>
						</div>
						
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
			
		</div>
		<!-- /.main-content -->

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
	<!--本体查询列表js-->
	</body>
<script>
$(function() {
	//窗口变化，重新初始化 
	$(window).off('resize').on('resize', function() {
		initWidthHeight();
	}).trigger('resize');
	
});
function initWidthHeight(){
	var ss = $("#pageStrDiv").outerHeight();
	if(ss>50){//后台传输到前台内容有误，导致标签宽高不准确
		ss=50;
	}
	var myHeight = $(window).outerHeight() - ss  -$("#myTitle").height();
	$("#resultDiv").css('max-height', myHeight-2+'px');
}
function detailPage(DIRECTION_NO){
	window.location.href =  '<%=basePath%>'+"show/detail.do?DIRECTION_NO="+ DIRECTION_NO +"&"+$("#myform").serialize()+"&tm="+new Date().getTime();
}

//单页遮罩层
var bgObj = null;
function closeBG()
{
	if(bgObj != null)
	{
		document.body.removeChild(bgObj);
	}
	bgObj = null;
}
function showBG()
{
	document.body.style.margin = "0";
	bgObj   = document.createElement("div");
	bgObj.setAttribute('id', 'bgDiv');
	bgObj.style.position   = "absolute";
	bgObj.style.top        = "0";
	bgObj.style.background = "#777";
	bgObj.style.filter     = "progid:DXImageTransform.Microsoft.Alpha(opacity=50)";
	bgObj.style.opacity    = "0.4";
	bgObj.style.left       = "0";
	bgObj.style.width      = "100%";
	bgObj.style.height     = "100%";
	bgObj.style.zIndex     = "1000";
	document.body.appendChild(bgObj);
}
</script>
</html>