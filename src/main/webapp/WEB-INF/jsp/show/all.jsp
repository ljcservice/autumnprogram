<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta charset="utf-8" />
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
<style>
.ztree li a.curSelectedNode {
/* 	background-color: #ffb951;	 */
 }
 .check-search{
	float: left;
	margin: 4px;
}
.treeName{
	color: #2679b5;
	font-size: 16px;
	font-weight: lighter;
	margin: 0 8px;
	padding: 0;
}
.treeName img{
width: 28px;
}
</style>
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12" id="searchDiv">
							<form name="searchForm" id="searchForm" action="ontology/ontologyList.do">
								<input type="hidden" name="ONTO_TYPE" id="ONTO_TYPE" value="${pd.ONTO_TYPE}"/>
							</form>
						</div>
						
						<div name=""  style="">
							<table style="width:100%;height: 100%;" border="0">
								<tr>
									<td style="width:250px;vertical-align: top;text-align: center;" align="center">
										<div id="treeTitle">
											<div id="treeName" class="treeName">
												<c:if test="${pd.ONTO_TYPE==0 }"><img class="nav-user-photo" src="static/images/show/icon01.png" >药品说明书</c:if>
												<c:if test="${pd.ONTO_TYPE==1 }"><img class="nav-user-photo" src="static/images/show/icon02.png" >个性化给药</c:if>
												<c:if test="${pd.ONTO_TYPE==2 }"><img class="nav-user-photo" src="static/images/show/icon03.png" >抗菌药物临床应用指导原则</c:if>
												<c:if test="${pd.ONTO_TYPE==3 }"><img class="nav-user-photo" src="static/images/show/icon04.png" >医学常用计算公式</c:if>
												<c:if test="${pd.ONTO_TYPE==4 }"><img class="nav-user-photo" src="static/images/show/icon05.png" >医药法规</c:if>
												<c:if test="${pd.ONTO_TYPE==5 }"><img class="nav-user-photo" src="static/images/show/icon06.png" >临床检验正常值及意义</c:if>
												<c:if test="${pd.ONTO_TYPE==6 }"><img class="nav-user-photo" src="static/images/show/icon07.png" >临床路径</c:if>
											</div>
											<div id="codeDiv">
												<select class="chosen-select form-control" name="code" id="code" data-placeholder="药理分类" onchange="changeTree();">
													<option value="1"  selected >药理分类</option>
													<option value="3"   >适应症分类</option>
													<option value="4"   >禁忌症分类</option>
													<option value="5"   >不良反应分类</option>
													<option value="7"   >妊娠分类</option>
												</select>
											</div>
										</div>
										<div id="treeDiv" style='width:250px;overflow: auto;height: 100%;'>
												<ul id="leftTree" class="ztree"></ul>
										</div>
										<div class="sidebar" style="width: 100%;border-style:none;position:relative;" id="mysidebar">
											<div class="sidebar-toggle sidebar-collapse" style="background-color: #ffffff;border-style:none;" id="sidebar-collapse">
												<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
											</div>
										</div>
									</td>
									<td style="vertical-align:top;">
										<iframe name="treeFrame" id="treeFrame" scrolling="no" frameborder="0"  style="margin:0 auto;width:100%;height: 100%;"></iframe>
										&nbsp;
									</td>
								</tr>
							</table>
						</div>
						
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js?v=2008001"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/myframe2.js"></script>
<script type="text/javascript" >
$(function() {
	changeTree();
	
});

// 查询本体
function searchs(){
	top.jzts();
	//清空同义词列表
// 	var emptyHtm = "<tr class='main_info'><td colspan='11' class='center'>没有相关数据</td></tr>";
// 	var mydocument = osynFrame.document;
// 	mydocument.getElementById("osynTbody").innerHTML = emptyHtm;
// 	mydocument.getElementById("osynPageParam").style.display = "none";
	//iframe跳转
	var ontoUrl = '<%=basePath%>'+"show/list.do?tm="+new Date().getTime();
	treeFrame.location.href= ontoUrl;
}
function beforeClick(){
	return true;
}
var zTree;
var zTreeUrl = "";
//切换树
function changeTree(){
	var ontoTypeCurent = $("#ONTO_TYPE").val();
	if(ontoTypeCurent==null || ontoTypeCurent==""){
		return;
	}
	//清空分类选择
	$("#leftTree").empty();
	zTreeUrl = '<%=basePath%>'+"show/tree.do?ONTO_TYPE="+ontoTypeCurent +"&code="+$("#code").val();
	//切换树标题名称
	initTree();
}
function initTree(){
	var setting = {
		view: {
			showIcon: false,
			showLine: true,
			showTitle: false,
			selectedMulti: false
		},
	    check:{
	    	enable: false
		},
		callback:{
		},
		async: {
			enable: true,
			url:zTreeUrl,
			autoParam:["id", "name","IS_LEAF"],
			otherParam:{}
		}
	};
	zTree = $.fn.zTree.init($("#leftTree"), setting);
};
var curentSelected = "";
function onClick(event, treeId, treeNode, clickFlag) {
	return true;
}

</script>

</html>