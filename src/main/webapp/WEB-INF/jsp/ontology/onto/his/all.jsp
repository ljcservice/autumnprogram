<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
  	  background-color: #ffb951;	
  	}
	</style>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<form name="searchForm" id="searchForm" action="ontology/ontologyList.do">
								<input type="hidden" name="category_id" id="category_id" value=""/>
								<table style="margin-top:5px;">
									<tr >
										<td style="padding-left:2px;padding-right:8px;">
											<div class="nav-search">
												<span class="input-icon">
													<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="名称/编码等" />
													<i class="ace-icon fa fa-search nav-search-icon"></i>
												</span>
											</div>
										</td>
										<td style="vertical-align:top;padding-left:2px;padding-right:8px;"><a class="btn btn-light btn-xs" onclick="searchs(this);" title="检索" target="treeFrame" id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
										<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap> 
											类型：
										 	<select class="chosen-select form-control" name="ontoType" id="ontoType" data-placeholder="" style="vertical-align:top;width: 80px;" onchange="changeTree();">
												<option value="51001" <c:if test="${ontoType == 51001 }">selected</c:if>>药品</option>
												<option value="51003" <c:if test="${ontoType == 51003 }">selected</c:if>>手术</option>
												<option value="51005" <c:if test="${ontoType == 51005 }">selected</c:if>>诊断</option>
											</select>
										</td>
										
										<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap>
											排序方式：
										 	<select class="chosen-select form-control" name="sort_type" id="sort_type" data-placeholder="排序方式" style="vertical-align:top;width: 120px;">
										 		<option value="4"></option>
												<option value="1" >主要编码正序</option>
												<option value="2" >主要编码倒序</option>
												<option value="3" >更新时间正序</option>
												<option value="4" >更新时间倒序</option>
											</select>
										</td>
										<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap>
											更新人：
										 	<select class="chosen-select form-control" name="UPDATE_MAN" id="UPDATE_MAN" data-placeholder="更新人" style="vertical-align:top;width: 100px;">
												<option value=""></option>
												<c:forEach items="${userList}" var="user" varStatus="vs">
													<option value="${user.USERNAME}" >${user.USERNAME}&nbsp;&nbsp;</option>
												</c:forEach>
											</select>
										</td>
										<td style="padding-left:2px;padding-right:8px;" nowrap>
											更新日期：
											<input class="span10 date-picker" name="UPDATE_TIME" id="UPDATE_TIME"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="更新日期" />
										</td>
										<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap>
											审核人：
										 	<select class="chosen-select form-control" name="CHECK_USER" id="CHECK_USER" data-placeholder="审核人" style="vertical-align:top;width: 100px;">
												<option value=""></option>
												<c:forEach items="${userList}" var="user" varStatus="vs">
													<option value="${user.USERNAME}" >${user.USERNAME}&nbsp;&nbsp;</option>
												</c:forEach>
											</select>
										</td>
<!-- 										<td style="padding-left:2px;padding-right:8px;" nowrap> -->
<!-- 											审核日期： -->
<!-- 											<input class="span10 date-picker" name="CHECK_TIME" id="CHECK_TIME"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="更新日期"  /> -->
<!-- 										</td> -->
									</tr>
								</table>
							</form>
						</div>
						
						<div name="" style="padding-top: 30px;">
							<table style="width:100%;" border="0">
								<tr>
									<td style="width:15%;" valign="top" bgcolor="#F9F9F9">
										<div id="treeName">本体数</div>
										<div style="width:15%;" id="treeId">
											<ul id="leftTree" class="ztree"></ul>
										</div>
									</td>
									<td style="width:85%;" valign="top" >
										<iframe name="treeFrame" id="treeFrame" frameborder="0" src="${basePath}ontology/hisList.do?ontoType=${ontoType}" style="margin:0 auto;width:100%;"></iframe>
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
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>

<script type="text/javascript">
$(top.hangge());

$(function() {
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
});
var zTree;
var zTreeUrl = basePath+"ontoTree/tree.do?ontoType=" + $("#ontoType").val();
function initTree(){
	var setting = {
		view: {
			showIcon:false,
			showLine: true,
			dblClickExpand: false,
			selectedMulti: false
		},
	    check:{
	    	enable: false
		},
		callback:{
			onClick: onClick
		},
		async: {
			enable: true,
			url:zTreeUrl,
			autoParam:["id", "name=n", "CATEGORY"],
			otherParam:{"type":"1"}
		}
	};
	
	var curentSelected = "";
	function onClick(event, treeId, treeNode, clickFlag) {
		var obj  = $("#"+treeNode.tId+"_a");
		if(treeNode.tId == curentSelected){
			//已经被选择则取消选择状态，
			obj.removeClass("curSelectedNode");
			//清除树查询条件
			$("#category_id").val("");
			//清楚已选中标志
			curentSelected = "";
		}else{
			//设置查询条件分类
			$("#category_id").val(treeNode.id);
			curentSelected = treeNode.tId;
		}
		return true;
	}
	zTree = $.fn.zTree.init($("#leftTree"), setting);
};

// 查询历史信息
function searchs(){
	top.jzts();
	//iframe跳转
	var valid = $("#searchForm").serialize();
	var ontoUrl = "ontology/hisList.do?"+valid+"&tm="+new Date().getTime();
	treeFrame.location.href= ontoUrl;
}

//切换树
function changeTree(){
	var ontoTypeCurent = $("#ontoType").val();
	if(ontoTypeCurent==null || ontoTypeCurent==""){
		return;
	}
	$("#leftTree").empty();
	zTreeUrl = basePath+"ontoTree/tree.do?ontoType="+ontoTypeCurent;
	//切换树标题名称
	if(ontoTypeCurent=="51001"){
		$("#treeName").html("药品ATC分类");
	}else if(ontoTypeCurent=="51003"){
		$("#treeName").html("手术本体树");
	}else if(ontoTypeCurent=="51005"){
		$("#treeName").html("诊断本体树");
	}
	initTree();
}

</script>
</html>
