<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>诊断维护页-快捷键修改父节点,科室分类选择</title>
<base href="${basePath}">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<style type="text/css">
.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
	padding: 4px;
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
						<div id="zhongxin" class="col-xs-12">
						<form name="searchsForm" id="searchsForm" action="common/ontoWidget.do" method="post">
						<div  style="padding-top: 0px;">
							<input type="hidden" name="osynType" value="${osynType}" id="osynType"/>
							<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
							<input type="hidden" name="businessType" value="${businessType}" id="businessType"/>
							<table style="margin-top:10px;margin-bottom: 12px;">
								<tr>
									<td style="padding-left:2px;padding-right:8px;" nowrap>
										标准词：
										<input class="nav-search-input" autocomplete="off" name="CN" id="CN" type="text" value="${pd.CN}"   />
									</td>
									<td style="padding-left:2px;padding-right:8px;" nowrap>
										编码：
										<input class="nav-search-input" autocomplete="off" name="MAIN_CODE" id="MAIN_CODE" type="text" value="${pd.MAIN_CODE}"  />
									</td>
									<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</tr>
							</table>
						</div>
						<div >
						<!--  诊断父节点 -->
						<c:if test="${businessType<2}">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"></label>
									</th>
									<th class="center" nowrap>本体ID</th>
									<th class="center" nowrap>同义词</th>
									<th class="center" nowrap>标准词</th>
								</tr>
							</thead>
													
							<tbody id="standTbody">
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
												
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center'>${diag.ID}</td>
											<td class="center">${diag.CN}</td>
											<td class="center">${diag.STAD_CN} &nbsp;[ ${diag.MAIN_CODE} ]&nbsp;&nbsp;<a onclick="fixer('${diag.ID}');">定位</a></td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="11" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						<!-- 科室名称 -->
						<c:if test="${businessType>=2}">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"></label>
									</th>
									<th class="center" nowrap>标准科室名称</th>
									<th class="center" nowrap>编码</th>
									<th class="center" nowrap>同义词</th>
									<th class="center" nowrap>父节点名称</th>
									<th class="center" nowrap>父节点编码</th>
									<th class="center" nowrap>更新人</th>
									<th class="center" nowrap>更新时间</th>
								</tr>
							</thead>
													
							<tbody id="standTbody">
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="onto" varStatus="vs">
												
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center'>${onto.STAD_CN} <a onclick="fixer('${onto.ID}');"> 定位</a> </td>
											<td class="center">${onto.Main_CODE}</td>
											<td class="center">${onto.CN}</td>
											<td class="center">${onto.PARENT_NAME}</td>
											<td class="center">${onto.PARENT_CODE}</td>
											<td class="center">${onto.UPDATE_MAN}</td>
											<td class="center">${onto.UPDATE_TIME}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="11" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						</div>
						<div class="position-relative" id="osynPageParam" style="padding-bottom:4px;">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
						</form>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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

function selectCont(){
	var osyn = $("input[name='standIs']:checked");
	if(osyn.val()==undefined || osyn.val()==""){
		return;
	}
	var mydocument = parent.$("#_DialogFrame_0")[0].contentWindow.document;
	mydocument.getElementById("DN_ID").value = osyn.val();
	mydocument.getElementById("SHOW_NAME").value = osyn.attr("CN");
	mydocument.getElementById("STAD_DN_CHN").value = osyn.attr("CN");
	mydocument.getElementById("STAD_DN_ENG").value = osyn.attr("EN");
	mydocument.getElementById("ORG_STAD_DN_CHN").value = osyn.attr("O_CN");
	mydocument.getElementById("ORG_STAD_DN_ENG").value = osyn.attr("O_EN");
	top.Dialog.close();
}
var purl = basePath+'ontoTree/treePidsById.action?ontotype=${ontoType}';
function fixer(id){
	parent.fixerTree(id,purl);
}

function searchs(){
	$("#searchsForm").submit();
}
</script>
</html>
