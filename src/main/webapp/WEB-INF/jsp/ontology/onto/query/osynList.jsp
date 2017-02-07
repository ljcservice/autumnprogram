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
						<form action="osyn/osynList.do" method="post">
							<input type="hidden" name="standardId" value="${standardId}" id="standardId"/>
							<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
						</form>
						<div class="col-xs-12">
						<div >
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" nowrap>术语名称中文</th>
									<th class="center" nowrap>术语名称英文</th>
									<th class="center" nowrap>术语类型</th>
									<th class="center" nowrap>同义词类型</th>
									<th class="center" nowrap>来源名称中文</th>
									<th class="center" nowrap>来源名称英文</th>
									<th class="center" nowrap>停用标记</th>
									<th class="center" nowrap>停用描述</th>
									<th class="center" nowrap>更新人</th>
									<th class="center" nowrap>更新时间</th>
								</tr>
							</thead>
													
							<tbody id="osynTbody">
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
												
										<tr>
											<td class='center' style="width: 30px;"><label><input type='checkbox' name='ids' class="ace" value="${diag.DN_ID}" /><span class="lbl"></span></label></td>
											<td class="center">${diag.CN }</td>
											<td class="center">${diag.EN }</td>
											<td class="center">${diag.TERM_TYPE }</td>
											<td class="center">${diag.SYNO_TYPE }</td>
											<td class="center">${diag.ORG_CH }</td>
											<td class="center">${diag.ORG_EN }</td>
											<td class="center">
												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if>
												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if>
											</td>
											<td class="center" title="${diag.DESCRIPTION }" ><div style="max-height: 20px;max-width: 120px;overflow: hidden;">${diag.DESCRIPTION }</div></td>
											<td class="center">${diag.UPDATE_MAN }</td>
											<td class="center"><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
						<div class="position-relative" >
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;">
									<div style="vertical-align:bottom;float: left;padding-top: 0px;margin-top: 0px;">
										<a class="btn btn-mini btn-success" onclick="add('Add');">新增术语</a>
										<a class="btn btn-mini btn-info" onclick="edit('Edit');">修改术语</a>
									</div>
									<div class="pagination" id="osynPageParam" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
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
	</body>

<script type="text/javascript">
$(top.hangge());

$(function() {
	//重置页面高度
	treeFrameT("osynFrame");
	function treeFrameT(obj){
		var hmainT = parent.document.getElementById(obj);
		hmainT.style.width = '100%';
		hmainT.style.height = ($("#main-container").height()) + 'px';
// 		document.getElementById("osynShow").style.width = ($("#main-container").width()-20) + 'px';
// 		document.getElementById("osynShow").style.height = ($("#main-container").height()-89) + 'px';
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
</script>
</html>
