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
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12" id="zhongxin">
						<div id="showView" style="overflow: auto;">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;margin-bottom: 2px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									</th>
									<th class="center" nowrap>手术名称中文</th>
									<th class="center" nowrap>手术名称英文</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>上级名称</th>
									<th class="center" nowrap>来源名称中文</th>
									<th class="center" nowrap>来源名称英文</th>
									<th class="center" nowrap>停用标记</th>
									<th class="center" nowrap>停用描述</th>
									<th class="center" nowrap>更新人</th>
									<th class="center" nowrap>更新时间</th>
									<th class="center" nowrap>审核人</th>
									<th class="center" nowrap>审核时间</th>
									<th class="center" nowrap>审核备注</th>
									<th class="center" nowrap>变更描述</th>
									<th class="center" nowrap>操作类型</th>
									<th class="center" nowrap>状态</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
												${vs.index +1 }
											</td>
											<td class="center" nowrap>${diag.STAD_DN_CHN }</td>
											<td class="center" nowrap>${diag.STAD_DN_ENG }</td>
											<td class="center" nowrap >${diag.MAIN_CODE }</td>
											<td class="center" nowrap>${diag.PARENT_NAME }</td>
											<td class="center" nowrap >${diag.ORG_STAD_DN_CHN }</td>
											<td class="center" nowrap >${diag.ORG_STAD_DN_ENG }</td>
											<td class="center">
												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if>
												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if>
											</td>
											<td class="center" nowrap title="${diag.DESCRIPTION }"><div style="max-height: 20px;max-width: 100px;overflow: hidden;">${diag.DESCRIPTION }</div></td>
											<td class="center" nowrap >${diag.UPDATE_MAN }</td>
											<td class="center" nowrap><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center" nowrap >${diag.CHECK_USER }</td>
											<td class="center" nowrap><fmt:formatDate value="${diag.CHECK_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center" nowrap >${diag.CHECK_MEMO }</td>
											<td class="center" nowrap title="${diag.UPD_DESC }"><div style="max-height: 20px;max-width: 100px;overflow: hidden;">${diag.UPD_DESC }</div></td>
											<td class="center" nowrap>
												<c:if test="${diag.OP_TYPE == '0' }">新增</c:if>
												<c:if test="${diag.OP_TYPE == '1' }">修改</c:if>
												<c:if test="${diag.OP_TYPE == '2' }">修改父节点</c:if>
												<c:if test="${diag.OP_TYPE == '4' }">停用术语</c:if>	
											</td>
											<td class="center" nowrap>
												<c:if test="${diag.STATUS == '0' }">审批中</c:if>
												<c:if test="${diag.STATUS == '1' }">审批通过</c:if>
												<c:if test="${diag.STATUS == '2' }">审批拒绝</c:if>
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
						<div class="" style="margin-top: 12px;">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<div style="text-align:center ;float: inherit;padding-top: 0px;margin-top: 0px;">
										<a class="btn btn-mini btn-success" onclick="top.Dialog.close();"> 关闭</a>
									</div>
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

	</body>

<script type="text/javascript">
$(top.hangge());


</script>
</html>
