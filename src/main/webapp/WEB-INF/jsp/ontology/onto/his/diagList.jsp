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
<style>
.table-hover > tbody > tr:hover {
/*    background-color: #ffe599; */
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
						<form action="ontology/hisList.do" method="post">
						<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
						<div class="col-xs-12">
						<div>
						</div>
						<div id="showView" style="overflow: auto;">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;margin-bottom: 2px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									</th>
									<th class="center" nowrap>诊断名称中文</th>
									<th class="center" nowrap>诊断名称英文</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>附加编码</th>
									<th class="center" nowrap>来源名称中文</th>
									<th class="center" nowrap>来源名称英文</th>
									<th class="center" nowrap>术语类型</th>
									<th class="center" nowrap>术语定义</th>
									<th class="center" nowrap>停用标记</th>
									<th class="center" nowrap>停用描述</th>
									<th class="center" nowrap>科室分类</th>
									<th class="center" nowrap>部位分类</th>
									<th class="center" nowrap>人群分类</th>
									<th class="center" nowrap>病种类型</th>
									<th class="center" nowrap>是否慢性病</th>
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
											<td class="center" nowrap><a onclick="hisDetail('${diag.D_ID}');">${diag.STAD_DN_CHN }</a></td>
											<td class="center" nowrap>${diag.STAD_DN_ENG }</td>
											<td class="center" nowrap >${diag.MAIN_CODE }</td>
											<td class="center" nowrap >${diag.ADD_CODE }</td>
											<td class="center" nowrap >${diag.ORG_STAD_DN_CHN }</td>
											<td class="center" nowrap >${diag.ORG_STAD_DN_ENG }</td>
											<td class="center" nowrap >${diag.TERM_TYPE }</td>
											<td class="center" nowrap title="${diag.TERM_DEFIN }"><div style="max-height: 20px;max-width: 100px;overflow: hidden;">${diag.TERM_DEFIN }</div></td>
											<td class="center">
												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if>
												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if>
											</td>
											<td class="center" nowrap title="${diag.DESCRIPTION }"><div style="max-height: 20px;max-width: 100px;overflow: hidden;">${diag.DESCRIPTION }</div></td>
											<td class="center" nowrap >${diag.DEP_CATEGORY }</td>
											<td class="center" nowrap >${diag.PART_CATEGORY }</td>
											<td class="center" nowrap >${diag.MAN_CATEGORY }</td>
											
											<td class="center" nowrap >${diag.DIS_CATEGORY }</td>
											<td class="center" nowrap >
												<c:if test="${diag.IS_CHRONIC == '0' }">否</c:if>
												<c:if test="${diag.IS_CHRONIC == '1' }">是</c:if>
											</td>
											<td class="center" nowrap >${diag.UPDATE_MAN }</td>
											<td class="center" nowrap><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center" nowrap >${diag.CHECK_USER }</td>
											<td class="center" nowrap><fmt:formatDate value="${diag.CHECK_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center" nowrap >${diag.CHECK_MEMO }</td>
											<td class="center" nowrap title="${diag.UPD_DESC }"><div style="max-height: 20px;max-width: 100px;overflow: hidden;">${diag.UPD_DESC }</div></td>
											<td class="center" nowrap>
												<c:if test="${diag.OP_TYPE == '0' }">新增</c:if>
												<c:if test="${diag.OP_TYPE == '1' }">修改</c:if>
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
									<div style="vertical-align:bottom;float: left;padding-top: 0px;margin-top: 0px;">
									</div>
									<div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
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
			hmainT.style.height = '300px';
		}else{
			hmainT.style.height = ($("#main-container").height()+10) + 'px';
		}
	}
// 	var t = $("#main-container").height()-50;
// 	if(t<=120){
// 		document.getElementById("showView").style.height= '120px';
// 	}else if( t<440 ){
// 		document.getElementById("showView").style.height= ($("#main-container").height()-40 )+ 'px';
// 	}else{
// 		document.getElementById("showView").style.height= '440px';
// 	}
});
//单个历史详情
function hisDetail(onto_id){
	 top.jzts();
	 var diag = new top.Dialog();
	 var ontoTypeCurent = $("#ontoType").val();
	 diag.Drag=true;
	 diag.URL = path+'/ontology/hisDetail.do?ontoType='+ontoTypeCurent+'&D_ID='+onto_id;
	 diag.Width = 900;
 	 diag.Height = 500;
 	 diag.Title ="变更历史详情";
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}

</script>
</html>
