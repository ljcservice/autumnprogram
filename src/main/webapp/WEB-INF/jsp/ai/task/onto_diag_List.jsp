<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<link type="text/css" rel="stylesheet" href="static/css/ontology.css?v=2016"/>
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<form action="taskQuery/ontoDiagList.do" method="post" name="queryDiagNameForm" id="queryDiagNameForm">
							<input type="hidden" id="TASK_ID" name="TASK_ID" value="${pd.TASK_ID}"/>
							<input type="hidden" name="STEP" id="STEP" value="${pd.STEP }"/>
							<input type="hidden" name="Q_ID" id="Q_ID" value="${pd.Q_ID }"/>
							<input type="hidden" id="hid_diag_name" name="hid_diag_name" value="${pd.hid_diag_name}"/>
							<input type="hidden" name="EDIT_FLAG" id="EDIT_FLAG" value="${pd.EDIT_FLAG }"/>
							<input type="hidden" name="query_type" id="query_type" value="${pd.query_type }"/>
							<input type="hidden" name="tree_id" id="tree_id" value="${pd.tree_id }"/>
							
							<input type="text" style="width:93%;margin-top:10px;margin-left:13px" name="DIAG_NAME" id="DIAG_NAME" value="${pd.DIAG_NAME}"/>
							<!-- <a class="btn btn-light btn-xs" onclick="query(1);"  title="查询">
							<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>查询</a> -->
							<a class="btn btn-light btn-xs" onclick="query(1);"  title="检索">
							<i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
						<div class="col-xs-12">
						<div>
						<!-- <span>查询结果</span> -->
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" nowrap>包含搜索词的名称</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>附加编码</th>
									<th class="center" nowrap>标准词名称</th>
									<th class="center" nowrap>操作</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<tr>
											<td>${diag.CN }<a class="specila" onclick="fixer_tree(event,'${diag.ID}');">定位</a></td>
											<td>${diag.MAIN_CODE }</td>
											<td>${diag.ADD_CODE }</td>
											<td>${diag.STAD_CN }</td>
											<td style="width: 140px;" class="center">
												<ts:rights code="taskQuery/toTYC">
													<a class="btn btn-mini <c:if test="${pd.EDIT_FLAG=='1'}">btn-success</c:if>" <c:if test="${pd.EDIT_FLAG=='1'}">onclick="tyc('${diag.STAD_ID}','${diag.STAD_CN }');"</c:if>>同义词</a>
												</ts:rights>
											</td>
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</div>
						<div class="position-relative">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<div class="pagination" style="float: right;padding-top: 0px;margin: 0px;">${page.pageStr}</div>
								</td>
							</tr>
						</table>
						</div>
		
						</div>
						</form>
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
	</body>

<script type="text/javascript">
$(top.hangge());

$(function() {
	
	//重置页面高度
	treeFrameT("treeFrame");
	function treeFrameT(obj){
		var hmainT = parent.document.getElementById(obj);
		hmainT.style.width = '100%';
		if($("#main-container").height()<300){
			hmainT.style.height = '400px';
		}else{
			hmainT.style.height = ($("#main-container").height()+10) + 'px';
		}
	}
	
	//复选框全选控制
	var active_class = 'active';
	$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
		var th_checked = this.checked;//checkbox inside "TH" table header
		$(this).closest('table').find('tbody > tr').each(function(){
			var row = this;
			if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
			else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
		});
	});
	
});

//定位
var purl = '${basePath}ontoTree/treePidsById.action?ontotype=51005';
function fixer_tree(e,id){
	window.event? window.event.cancelBubble = true : e.stopPropagation();
	parent.fixerTree(id,purl);
}
//查询
function query(type,on_id){
	if(type!=null && type!=''){
		$("#tree_id").val(null);
		$("#query_type").val(type);
	}else{
		$("#tree_id").val(on_id);
		$("#query_type").val(null);
	}
	top.jzts();
	$("#queryDiagNameForm").submit();
}
function nlpquery(){
	
}


//同义词
function tyc(STD_ID,STAD_CN){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="同义词";
	 diag.URL = '<%=path%>/taskQuery/toTYC.do?DIAG_NAME='+$("#hid_diag_name").val()+'&STD_ID='+STD_ID+'&STAD_CN='+STAD_CN+'&TASK_ID='+$("#TASK_ID").val()+'&STEP='+$("#STEP").val()+'&Q_ID='+$("#Q_ID").val();
	 diag.Width = 380;
	 diag.Height = 300;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 //nextPage(${page.currentPage});
				 parent.refreshPage();
			 }
		}
		diag.close();
	 };
	 diag.show();
}

//下位词
function xwc0(STD_ID,STAD_CN,D_ID){
	 //首先校验能否增加下位词
	 $.ajax({
		type: "POST",
		url: '<%=basePath%>taskQuery/getOntMultiPaChk.do',
    	data: {D_ID:D_ID,tm:new Date().getTime()},
    	dataType:'json',
		cache: false,
		success: function(data){
			if(data.result=="success"){
				 top.jzts();
				 var diag = new top.Dialog();
				 diag.Drag=true;
				 diag.Title ="下位词";
				 diag.URL = '<%=path%>/taskQuery/toXWC.do?DIAG_NAME='+$("#hid_diag_name").val()+'&STD_ID='+STD_ID+'&STAD_CN='+STAD_CN+'&TASK_ID='+$("#TASK_ID").val()+'&STEP='+$("#STEP").val()+'&Q_ID='+$("#Q_ID").val();
				 diag.Width = 380;
				 diag.Height = 260;
				 diag.CancelEvent = function(){ //关闭事件
					 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
						 if('${page.currentPage}' == '0'){
							 top.jzts();
							 setTimeout("self.location=self.location",100);
						 }else{
							 //nextPage(${page.currentPage});
							 parent.refreshPage();
						 }
					}
					diag.close();
				 };
				 diag.show();
			 }else{
				 bootbox.dialog({
					 	message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				 return;
			 }
		}
	 })
}
</script>
</html>
