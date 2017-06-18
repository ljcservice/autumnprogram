<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
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
						<form action="opDrug/selectData.do" method="post" id="searchForm">
						<input type="hidden" name="ONTO_TYPE" value="${pd.ONTO_TYPE}" id="ONTO_TYPE"/>
						<div id="zhongxin" style="padding-top: 13px;">
							<table style="margin-top:5px;">
								<tr>
									<td>
										<div class="nav-search">
										名称：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="专家名称 " />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										</div>
									</td>
									<td style="vertical-align:top;padding-left:10px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
									<td style="vertical-align:top;padding-left:15px;">
										<a class="btn btn-mini btn-primary" onclick="selectCont();">确定</a>
										<a class="btn btn-mini btn-danger" style="margin-left:10px;" onclick="top.Dialog.close();">取消</a>
									</td>
								</tr>
							</table>
						</div>
						<div >
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"></label>
									</th>
									<th class="center" nowrap>编码</th>
									<th class="center" nowrap>名称</th>
								</tr>
							</thead>
													
							<tbody id="standTbody">
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="onto" varStatus="vs">
												
										<tr onclick="clickTr(this);">
											<td class='center' style="width: 30px;">
												<label><input type="radio" class="ace" name="userIs" ontoName='${onto.name}' value="${onto.code}" code="${onto.code}" /><span class="lbl"></span></label>
											</td>
											<td class="center">${onto.code}</td>
											<td class="center">${onto.name}</td>
<%--											<td class="center"><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>--%>
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
						</div>
						<div class="page-header position-relative" id="osynPageParam">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
		
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
function clickTr(obj){
	$("input[name='userIs']").prop("checked",false);
	$(obj).find("input[name='userIs']").eq(0).prop("checked",true);
}
function selectCont(){
	var osyn = $("input[name='userIs']:checked");
	if(osyn.val()==undefined || osyn.val()==""){
		return;
	}
	var mydocument = parent.$("#_DialogFrame_0")[0].contentWindow.document;
	//设置为专家点评
	var onto_type = $("#ONTO_TYPE").val();
	if(onto_type==100){
		mydocument.getElementById("O_CODE").value = osyn.attr("code");
		mydocument.getElementById("O_NAME").value = osyn.attr("ontoName");
	}
	if(onto_type==101){
		mydocument.getElementById("O_DRUG_CODE").vvalue = osyn.attr("code");
		mydocument.getElementById("O_DRUG_NAME").value = osyn.attr("ontoName");
	}
	if(onto_type==102){
		mydocument.getElementById("O_DEPT_CODE").value = osyn.attr("code");
		mydocument.getElementById("O_DEPT_NAME").value = osyn.attr("ontoName");
	}
	if(onto_type==103){
		mydocument.getElementById("O_DOCTOR_CODE").value = osyn.attr("code");
		mydocument.getElementById("O_DOCTOR_NAME").value = osyn.attr("ontoName");
	}
	top.Dialog.close();
}
//检索
function searchs(){
	top.jzts();
	$("#searchForm").submit();
}
</script>
</html>