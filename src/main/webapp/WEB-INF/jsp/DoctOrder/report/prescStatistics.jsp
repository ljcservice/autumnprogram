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
<style>
.ztree li a.curSelectedNode {
/* 	background-color: #ffb951;	 */
 }
 .check-search{
	float: left;
	margin-top: 2px;
	margin-bottom: 2px;
	margin-left: 3px;
	margin-right: 3px;
}
@media Print { .Noprn { DISPLAY: none }}
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
						<div class="col-xs-12">
							<div class="Noprn">
								<form action="report/prescStatistics.do" method="post" name="searchForm" id="searchForm">
									<div class="check-search"  >
										处方日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.endDate }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
										<font style="color: red;">*</font>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div id="btnDiv" class="check-search">
											<a title="最大支持导出2万条" class="btn btn-mini btn-success" onclick="listExport();">导出</a>
											<a title="" class="btn btn-mini btn-success" onclick="window.print();">打印</a>
									</div>
								</form>
							</div>
						<!-- 检索  -->
						<div style="width: 100%;height: auto;">
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th width="40%" class="center" nowrap>统计指标</th>
									<th width="20%" class="center" nowrap>数量</th>
									<th width="20%" class="center" nowrap>总数</th>
									<th class="center" nowrap>比率</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
								<tr  >
									<td nowrap align="right" >抗菌药处方百分比(抗菌药处方数/处方总数)：</td>
									<td nowrap class="center">${p1.HASKJ_SUM }</td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">${p1.HASKJ_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">注射剂处方百分比(注射剂处方数/处方总数)：</td>
									<td nowrap class="center">${p1.HASZS_SUM }</td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">${p1.HASZS_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">国家基本药物品种百分比(国家基本药物数/使用药物总数)：</td>
									<td nowrap class="center">${p1.BASEDRUG_COUNT_SUM }</td>
									<td nowrap class="center">${p1.DRUG_COUNT_SUM }</td>
									<td nowrap class="center">${p1.BASEDRUG_COUNT_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">合理处方百分比(不合格处方数/处方总数)：</td>
									<td nowrap class="center">${p1.CHECKTRUE }</td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">${p1.CHECKTRUE_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">不合理处方百分比(不合格处方数/处方总数)：</td>
									<td nowrap class="center">${p1.CHECKFALSE }</td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">${p1.CHECKFALSE_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">待定处方百分比(待定处方数/处方总数)：</td>
									<td nowrap class="center">${p1.CHECKPEND }</td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">${p1.CHECKPEND_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">非限制级抗菌药百分比(非限制抗菌药/抗菌药总数)：</td>
									<td nowrap class="center">${p2.ONE_LEVEL }</td>
									<td nowrap class="center">${p2.DRUG_COUNT }</td>
									<td nowrap class="center">${p2.ONE_LEVEL_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">限制级抗菌药百分比（限制抗菌药/抗菌药总数)：</td>
									<td nowrap class="center">${p2.TWO_LEVEL }</td>
									<td nowrap class="center">${p2.DRUG_COUNT }</td>
									<td nowrap class="center">${p2.TWO_LEVEL_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">特殊级抗菌药百分比(特殊级抗菌药/抗菌药总数)：</td>
									<td nowrap class="center">${p2.THREE_LEVEL }</td>
									<td nowrap class="center">${p2.DRUG_COUNT }</td>
									<td nowrap class="center">${p2.THREE_LEVEL_PERSENTS } %</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">抗菌药金额比例百分比(使用抗菌药总金额/使用药品总金额)：</td>
									<td nowrap class="center">￥ <fmt:formatNumber value="${p1.ANTIDRUGCOSTS_SUM}" pattern="###,###,##0.00"></fmt:formatNumber></td>
									<td nowrap class="center">￥ <fmt:formatNumber value="${p1.AMOUNT_SUM }" pattern="###,###,##0.00"></fmt:formatNumber></td>
									<td nowrap class="center">${p1.ANTIDRUGCOSTS_PERSENTS } %</td>
								</tr>
							</tbody>
						</table>
						
						<table id="simple-table2" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th width="40%" class="center" nowrap>统计指标</th>
									<th width="20%" class="center" nowrap>总数</th>
									<th width="20%" class="center" nowrap>总处方数</th>
									<th class="center" nowrap>平均值</th>
								</tr>
							</thead>
							<tbody>
								<tr  >
									<td nowrap align="right" width="40%">平均每张处方用药品种数：</td>
									<td nowrap class="center">${p1.BASEDRUG_COUNT_SUM }</td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">${p1.BASEDRUG_COUNT_AVG } </td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">平均每张处方金额：</td>
									<td nowrap class="center">￥ <fmt:formatNumber value="${p1.AMOUNT_SUM }" pattern="###,###,##0.00"></fmt:formatNumber></td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">￥ ${p1.AMOUNT_AVG }</td>
								</tr>
								<tr  >
									<td nowrap align="right" width="40%">平均处方用药天数：</td>
									<td nowrap class="center">${p1.MAXUSEDAY_SUM }</td>
									<td nowrap class="center">${p1.COUNT }</td>
									<td nowrap class="center">${p1.MAXUSEDAY_AVG }</td>
								</tr>
							</tbody>
							</table>
	
						<table id="simple-table2" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th width="40%" class="center" nowrap>统计指标</th>
									<th width="20%" class="center" nowrap>总数</th>
									<th width="20%" class="center" nowrap>总人次数</th>
									<th class="center" nowrap>平均值</th>
								</tr>
							</thead>
							<tbody>
								<tr  >
									<td nowrap align="right" width="40%">平均每人次处方金额：</td>
									<td nowrap class="center">￥ <fmt:formatNumber value="${p1.AMOUNT_SUM }" pattern="###,###,##0.00"></fmt:formatNumber></td>
									<td nowrap class="center">${p3.PATIENT_ID_COUNT }</td>
									<td nowrap class="center">￥ ${p3.PATIENT_ID_AVG }</td>
								</tr>
							</tbody>
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
	<script type="text/javascript" src="static/js/common/common.js"></script>
	</body>
<script type="text/javascript">
$(top.hangge());

//检索
function searchs(){
	if($("#beginDate").val()==""){
		$("#beginDate").tips({ side:3, msg:'请选择开始日期', bg:'#AE81FF',  time:1   });
		return;
	}
	if($("#endDate").val()==""){
		$("#endDate").tips({ side:3, msg:'请选择结束日期', bg:'#AE81FF',  time:1   });
		return;
	}
	top.jzts();
	$("#searchForm").submit();
}

function resetForm(){
	document.getElementById("searchForm").reset();
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
function listExport(){
	window.open(path + "/report/prescStatisticsExport.do?&"+$("#searchForm").serialize());
}
</script>
</html>