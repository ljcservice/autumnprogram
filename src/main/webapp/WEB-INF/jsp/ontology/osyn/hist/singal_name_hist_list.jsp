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
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									
									<th class="center" nowrap>术语名称中文</th>
									<th class="center" nowrap>术语名称英文</th>
									<th class="center" nowrap>术语标准名称</th>
									<th class="center" nowrap>术语类型</th>
									<th class="center" nowrap>同义词类型</th>
									<th class="center" nowrap>更新人</th>
									<th class="center" nowrap>更新时间</th>
									<th class="center" nowrap>版本</th>
									<th class="center" nowrap>变更描述</th>
									<th class="center" nowrap>审核人</th>
									
								</tr>
							</thead>
													
							<tbody id="standTbody">
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty histList}">
									<c:forEach items="${histList}" var="hist" varStatus="vs">
												
										<tr>
											
											<td class="center">${hist.DN_CHN}</td>
											<td class="center">${hist.DN_ENG}</td>
											<td class="center">${hist.STAND_NAME}</td>
											<td class='center'>
												<c:if test="${hist.TERM_TYPE==1}">症状</c:if>
												<c:if test="${hist.TERM_TYPE==2}">疾病</c:if>
												<c:if test="${hist.TERM_TYPE==3}">手术</c:if>
												<c:if test="${hist.TERM_TYPE==4}">检查</c:if>
											</td>
											<td class='center'>
												<c:if test="${hist.SYNO_TYPE == 23101}">俗语</c:if>
												<c:if test="${hist.SYNO_TYPE == 23102}">拼音首字母</c:if>
												<c:if test="${hist.SYNO_TYPE == 23103}">缩略语</c:if>
												<c:if test="${hist.SYNO_TYPE == 23104}">同音字/错别字</c:if>
												<c:if test="${hist.SYNO_TYPE == 23105}">语用同义词</c:if>
												<c:if test="${hist.SYNO_TYPE == 23106}">专指同义词</c:if>
												<c:if test="${hist.SYNO_TYPE == 23107}">其它</c:if>
											</td>
											<td class="center">${hist.UPDATE_MAN}</td>
											<td class="center"><fmt:formatDate value="${hist.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center">${hist.VERSION}</td>
											<td class="center">${hist.UPD_DESC}</td>
											<td class="center">${hist.CHECK_USER}</td>
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
	
	mydocument.getElementById("STAD_DN_ID").value = osyn.val();
	mydocument.getElementById("SHOW_NAME").value = osyn.attr("CN");
	ownerDialog.close();
}
</script>
</html>
