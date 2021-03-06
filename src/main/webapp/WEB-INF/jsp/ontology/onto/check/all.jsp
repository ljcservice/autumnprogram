﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<meta charset="utf-8" />
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
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
	/* background-color: #ffb951; */
}
.check-search{
	float: left;
	margin-top: 2px;
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
						<div class="col-xs-12" style=" margin-top:3px;word-wrap:break-word;word-break:break-all; ">
							<form name="searchForm" id="searchForm" action="ontology/ontologyList.do">
							<input type="hidden" name="category_id" id="category_id" value=""/>
									<div class="check-search nav-search" style="width: 160px;">
										<span class="input-icon" style="">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="名称/编码等" maxlength="32"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search" style="width: 50px;">
										<a class="btn btn-light btn-xs" onclick="searchs(this);" title="检索" target="treeFrame" id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
									</div>
									<div class="check-search" style="width: 130px;" > 
										类型：
									 	<select class="chosen-select form-control" name="ontoType" id="ontoType" data-placeholder="" style="vertical-align:top;width: 80px;" onchange="changeTree();">
											
											<c:forEach items="${typeMap.entrySet()}" var="ontoTyp" varStatus="vs">
												<option value="${ontoTyp.key}" <c:if test="${ontoTyp.key == ontoType }">selected</c:if>>${ontoTyp.value}</option>
											</c:forEach>
										</select>
									</div>
									<div class="check-search" style="width: 180px;">
										审核状态：
									 	<select class="chosen-select form-control" name="STATUS" id="STATUS" data-placeholder="审核状态" style="vertical-align:top;width: 100px;">
									 		<option value=""></option>
											<option value="0" selected="selected">审核中</option>
											<option value="1" >审核通过</option>
											<option value="2" >审核拒绝</option>
										</select>
									</div>
									<div class="check-search" style="width: 180px;">
										操作类型：
									 	<select class="chosen-select form-control" name="OP_TYPE" id="OP_TYPE" data-placeholder="操作类型" style="vertical-align:top;width: 100px;">
									 		<option value=""></option>
											<option value="0" >新增</option>
											<option value="1" >修改</option>
											<option value="2" >修改父节点</option>
											<option value="3" >术语知识编辑</option>
											<option value="4" >停用术语</option>
<!-- 											<option value="7" >级联新增</option> -->
<!-- 											<option value="8" >被动新增</option> -->
										</select>
									</div>
									<div class="check-search" style="width: 180px;" >
										排序方式：
									 	<select class="chosen-select form-control" name="sort_type" id="sort_type" data-placeholder="排序方式" style="vertical-align:top;width: 100px;">
									 		<option value="4"></option>
											<option value="1" >主要编码正序</option>
											<option value="2" >主要编码倒序</option>
											<option value="3" >更新时间正序</option>
											<option value="4" >更新时间倒序</option>
										</select>
									</div>
									<div class="check-search" style="width: 170px;" >
										更新人：
									 	<select class="chosen-select form-control" name="UPDATE_MAN" id="UPDATE_MAN" data-placeholder="更新人" style="vertical-align:top;width: 100px;">
											<option value=""></option>
											<c:forEach items="${userList}" var="user" varStatus="vs">
												<option value="${user.USERNAME}" >${user.USERNAME}&nbsp;&nbsp;</option>
											</c:forEach>
										</select>
									</div>
									<div class="check-search" style="width: 250px;" >
										更新日期：
										<input class="span10 date-picker" name="UPDATE_TIME_START" id="UPDATE_TIME_START"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="UPDATE_TIME_END" id="UPDATE_TIME_END"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
									</div>
							</form>
						</div>
						
						<div name="树和列表" style="padding-top: 30px;">
							<table style="width:100%;" border="0">
								<tr>
									<td style="width:15%;" valign="top" >
										<div style='overflow: scroll;max-width: 270px;max-height: 480px;'>
											<div id="treeName">本体数</div>
											<div id="treeId">
												<ul id="leftTree" class="ztree"></ul>
											</div>
										</div>
									</td>
									<td style="width:85%;" valign="top" >
										<iframe name="treeFrame" id="treeFrame" frameborder="0" src="${basePath}ontology/checkList.do?ontoType=${ontoType}" style="margin:0 auto;width:100%;"></iframe>
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
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js?v=2008"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2018080"></script>
<script type="text/javascript" src="static/js/ontology/tree.js?v=2008"></script>
<script type="text/javascript">
$(top.hangge());
$(function() {
	$("#treeFrame").attr("src","${basePath}ontology/checkList.do?"+$("#searchForm").serialize());
	changeTree();
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

// 查询待审核信息
function searchs(){
	top.jzts();
	//iframe跳转
	var valid = $("#searchForm").serialize();
	var ontoUrl = basePath + "ontology/checkList.do?"+valid+"&tm="+new Date().getTime();
	treeFrame.location.href= ontoUrl;
}


</script>
</html>
