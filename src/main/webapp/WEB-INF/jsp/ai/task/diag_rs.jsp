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
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
				<input type="hidden" id="task_id" name="task_id" value="${pd.task_id}"/>
				<span>当前处理结果</span>
				<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
					<thead>
						<tr>
							<th class="center" nowrap>操作</th>
							<th class="center" nowrap>待处理术语</th>
							<th class="center" nowrap>标准词名称</th>
							<th class="center" nowrap>标准词编码</th>
							<th class="center" nowrap>上位词名称</th>
							<th class="center" nowrap>上位词编码</th>
							<th class="center" nowrap>同义词类型</th>
							<th class="center" nowrap>术语类型</th>
							<th class="center" nowrap>操作类型</th>
							<th class="center" nowrap>备注</th>
							
						</tr>
					</thead>
											
					<tbody>
						
					<!-- 开始循环 -->	
					<c:choose>
						<c:when test="${not empty resList}">
							<c:forEach items="${resList}" var="diag" varStatus="vs">
								<tr>
									<td>
										<ts:rights code="taskQuery/deleteRs">
											<a class="btn btn-mini <c:if test="${pd.EDIT_FLAG=='1'}">btn-danger</c:if>" <c:if test="${pd.EDIT_FLAG=='1'}">onclick="deleteRs(${diag.HIS_RS_ID});"</c:if>>删除</a>
										</ts:rights>
										<input type="hidden" name="ids"/>
									</td>
									<td>${diag.DIAG_WORD }</td>
									<c:choose>
								       <c:when test="${diag.DIS_TYPE == '1' }">
								            <td>${diag.STD_CN }</td>
											<td>${diag.STD_CODE }</td>
											<td></td>
											<td></td>
								       </c:when>
								       <c:otherwise>
								       		<td></td>
											<td></td>
								            <td>${diag.STD_CN }</td>
											<td>${diag.STD_CODE }</td>
								       </c:otherwise>
									</c:choose>
									
									<td>${diag.SYN_TYPE }</td>
									<td>${diag.TERM_TYPE }</td>
									<td style="width: 60px;" class="center">
										<c:if test="${diag.DIS_TYPE == '0' }"><span class="label label-success arrowed">默认值</span></c:if>
										<c:if test="${diag.DIS_TYPE == '1' }"><span class="label label-success arrowed-in">同义词</span></c:if>
										<c:if test="${diag.DIS_TYPE == '2' }"><span class="label label-success arrowed-in">下位词</span></c:if>
										<c:if test="${diag.DIS_TYPE == '3' }"><span class="label label-important arrowed-in">无法干预</span></c:if>
									</td>
									<td>${diag.MEMO }</td>
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
			</div>
		</div>
	</div>
	<!-- /.main-content -->
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
$(document).ready(function(){
});
	
//删除本人的处理结果
function deleteRs(HIS_RS_ID){
	bootbox.confirm("确定要删除当前的处理结果吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>taskQuery/deleteRs.do?HIS_RS_ID="+HIS_RS_ID+"&task_id="+$('#task_id').val()+"&tm="+new Date().getTime();
			$.get(url,function(data){
				if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location=self.location",100);
				 }else{
					 parent.refreshPage();
				 }
			});
		};
	});
}
//判断是否包含处理结果 是则1 否则0
function haveRs(){
	//判断列表是否为空
	var tableObj = document.getElementsByName('ids');
    if (tableObj.length == 0)
    	return 0;
    else
    	return 1;
}
</script>
</html>
