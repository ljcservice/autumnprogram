﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
						<form action="standard/osynStand.do" id="standForm" method="post">
						<div id="zhongxin" style="padding-top: 13px;">
							<input type="hidden" name="ontoType" value="${ontoType}" id="osynType"/>
							<table style="margin-top:5px;">
								<tr>
									<td>
										<div class="nav-search">
										术语名称：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="DN_NAME" value="${pd.DN_NAME }" placeholder="这里输入关键词" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										</div>
									</td>
									<td style="padding-left:2px;padding-right:8px;" nowrap>
										更新日期：
										<input class="span10 date-picker" name="UPDATE_TIME" id="UPDATE_TIME"  value="${pd.UPDATE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="更新开始日期"/>
										<input class="span10 date-picker" name="UPDATE_TIME_END" id="UPDATE_TIME_END"  value="${pd.UPDATE_TIME_END}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="更新结束日期" />
									</td>
									<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs(this);"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
									<td>
										<a class="btn btn-mini btn-primary" onclick="selectCont();">确定</a>
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
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
									<th class="center" nowrap>药品规格</th>
									<th class="center" nowrap>来源药品规格</th>
									<th class="center" nowrap>更新人</th>
									<th class="center" nowrap>停用标记</th>
									<th class="center" nowrap>更新时间</th>
									<th class="center" nowrap>备注</th>
								</tr>
							</thead>
													
							<tbody id="standTbody">
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="drug" varStatus="vs">
												
										<tr>
											<td class='center' style="width: 30px;">
												<input type="radio" name='standIs' value="${drug.SP_ID}" CN="${drug.DRUG_SP}" O_CN="${drug.ORG_DRUG_SP}"/>
											</td>
											<td class="center">${drug.DRUG_SP}</td>
											<td class="center">${drug.ORG_DRUG_SP}</td>
											<td class="center">${drug.UPDATE_MAN}</td>
											<td class="center">
												<c:if test="${drug.IS_DISABLE == 0}"><span class="label label-success arrowed">正常</span></c:if>
												<c:if test="${drug.IS_DISABLE == 1}"><span class="label label-important arrowed-in">停用</span></c:if>
											</td>
											<td class="center"><fmt:formatDate value="${drug.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center">${drug.REMARK}</td>
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
						</form>
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
	mydocument.getElementById("STAD_CHN").value = osyn.attr("CN");
	mydocument.getElementById("ORG_STAD_CHN").value = osyn.attr("O_CN");
	ownerDialog.close();
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
			if(event_name != 'sidebar_collapsed') return;
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.parent().width()});
			});
		});
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}
});

//查询标准词
function searchs(){
	top.jzts();
	$("#standForm").submit();
}
</script>
</html>
