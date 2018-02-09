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
	margin: 2px;
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
						<div class="col-xs-12">
						<!-- 检索  -->
								<div id="searchDiv"  style="vertical-align:bottom;float: left;padding-top: 2px;padding-bottom: 2px;width: 100%;">
								<form action="presc/prescWorkListPage.do" method="post" name="searchForm" id="searchForm">
									<div class="check-search"  >
										处方日期：
										<input class="span10 date-picker" name="beginDate" id="beginDate"  value="${pd.beginDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
										<input class="span10 date-picker" name="endDate" id="endDate"  value="${pd.end_Date }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
										<font color="red">*</font>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div class="check-search nav-search"  >
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="ORG_NAME" type="text" name="ORG_NAME" value="${pd.ORG_NAME}" placeholder="科室名称" maxlength="80" style="width: 100px;" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords}" placeholder="诊断" maxlength="80" style="width: 120px;" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="DRUG_NAME" type="text" name="DRUG_NAME" value="${pd.DRUG_NAME}" placeholder="药品名称" maxlength="80" style="width: 100px;" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search"   >
										是否点评：
									 	<select class="chosen-select form-control" name="ISORDERCHECK" id="ISORDERCHECK" data-placeholder="处方是否点评" style="vertical-align:top;width: 80px;" >
											<option value="">全部</option>
											<option <c:if test="${pd.ISORDERCHECK == '0'}">selected</c:if> value="0" >未点评</option>
											<option <c:if test="${pd.ISORDERCHECK == '1'}">selected</c:if> value="1" >已点评</option>
										</select>
									</div>
									<div class="check-search"  >
										是否合理：
									 	<select class="chosen-select form-control" name="ISCHECKTRUE" id="ISCHECKTRUE" data-placeholder="处方是否合理" style="vertical-align:top;width: 80px;">
									 		<option value="">全部</option>
											<option <c:if test="${pd.ISCHECKTRUE == '0' }">selected</c:if> value="0" >合理</option>
											<option <c:if test="${pd.ISCHECKTRUE == '1' }">selected</c:if> value="1" >不合理</option>
											<option <c:if test="${pd.ISCHECKTRUE == '2' }">selected</c:if> value="2" >待定</option>
										</select>
									</div>
									<div class="check-search"  >
										药品种类：
									 	<select class="chosen-select form-control" name="DRUG_TYPE" id="DRUG_TYPE" data-placeholder="是否抗菌" style="vertical-align:top;width: 105px;">
									 		<option value="">全部</option>
											<option <c:if test="${pd.DRUG_TYPE == 'HASKJ' }">selected</c:if> value="HASKJ" >抗菌药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'HASZS' }">selected</c:if> value="HASZS" >注射药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'ELJSY' }">selected</c:if> value="ELJSY" >二类抗拒药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'DDRUG' }">selected</c:if> value="DDRUG" >毒性药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'FSDRUG' }">selected</c:if> value="FSDRUG" >放射性药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'YLJSY' }">selected</c:if> value="YLJSY" >一类精神药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'GZDRUG' }">selected</c:if> value="GZDRUG" >贵重药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'MDRUG' }">selected</c:if> value="MDRUG" >麻药</option>
											<option <c:if test="${pd.DRUG_TYPE == 'DMDRUG' }">selected</c:if> value="DMDRUG" >毒麻药</option>
										</select>
									</div>
									<div class="check-search"  > 
										专家点评：
									 	<select class="chosen-select form-control" name="IS_EXPERT" id="IS_EXPERT" data-placeholder="是否专家点评" style="vertical-align:top;width: 80px;"  >
											<option value="">全部</option>
											<option <c:if test="${pd.IS_EXPERT == '0'}">selected</c:if> value="0" >否</option>
											<option <c:if test="${pd.IS_EXPERT == '1'}">selected</c:if> value="1" >是</option>
										</select> 
									</div>
									<div class="check-search"  >
										<a title="随机抽取" class="btn btn-mini btn-info" onclick="randomQuery();">随机抽取</a>
										<input type="number" placeholder="抽取数量" class="ace" name="RANDOM_NUM" value="${pd.RANDOM_NUM}" width="20px;" onchange="changeNum(this)">
									</div>
									<div class="check-search" id="btnDiv" >
										<a title="最大支持导出6万条" class="btn btn-mini btn-success" onclick="prescListExport();">导出</a>
										<a title="" class="btn btn-mini btn-success" onclick="myprint();">打印</a>
									</div>
								</form>
								</div>
						<!-- 检索  -->
						<div>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:2px;">
							<thead>
								<tr>
									<th class="center" nowrap>处方号</th>
									<th class="center" nowrap>处方日期</th>
									<th class="center" nowrap>患者</th>
									<th class="center" nowrap>性别</th>
									<th class="center" nowrap>科室</th>
									<th class="center" nowrap>医生</th>
									<th class="center" nowrap>诊断</th>
									<th class="center" nowrap>点评</th>
									<th class="center" nowrap>是否合理</th>
									<th class="center" nowrap>品种</th>
									<th class="center" nowrap>抗菌</th>
									<th class="center" nowrap>注射</th>
									<th class="center" nowrap>基药数</th>
									<th class="center" nowrap>金额</th>
									<th class="center" nowrap>存在问题</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty prescList}">
									<c:forEach items="${prescList}" var="presc" varStatus="vs">
										<tr ondblclick="detailPresc('${presc.id}','${presc.NGROUPNUM}')">
											<td class="center" nowrap><a href="javascript:detailPresc('${presc.id}','${presc.NGROUPNUM}');">${presc.PRESC_NO}</a></td>
											<td class="center" nowrap>${presc.ORDER_DATE}</td>
											<td class="center" nowrap>${presc.PATIENT_NAME}</td>
											<td class="center">${presc.PATIENT_SEX}</td>
											<td class="center" nowrap>${presc.ORG_NAME}</td>
											<td class="center" nowrap>${presc.DOCTOR_NAME}</td>
											<td class="center" style="max-width: 150px;">${presc.DIAGNOSIS_NAMES}</td>
											<td class="center" nowrap><c:if test="${presc.ISORDERCHECK!=1}">未点评</c:if><c:if test="${presc.ISORDERCHECK==1}">已点评</c:if></td>
											<td class="center" nowrap>
												<c:choose>
													<c:when test="${presc.ISCHECKTRUE == 0 }">合理 </c:when>
													<c:when test="${presc.ISCHECKTRUE == 1 }">不合理</c:when>
													<c:otherwise>待定</c:otherwise>
												</c:choose>
											</td>
											<td class="center" nowrap>${presc.DRUG_COUNT}</td>
											<td class="center" nowrap><c:if test="${presc.HASKJ==0}">否</c:if><c:if test="${presc.HASKJ==1}">是</c:if></td>
											<td class="center" nowrap>${presc.HASZS}</td>
											<td class="center" nowrap>${presc.BASEDRUG_COUNT}</td>
											<td class="center" nowrap><fmt:formatNumber value="${presc.AMOUNT }" pattern="###,###,##0.00"></fmt:formatNumber></td>
											<td class="center" nowrap>
												<div class="hidden-sm hidden-xs btn-group">
													<c:forEach items="${presc.RS_DRUG_TYPES}" var="rs_type" varStatus="vs" >
														<a class="btn btn-xs ${rstypeColorMap.get(rs_type)}"  title="${checktypeMap.get(rs_type).RS_TYPE_NAME}" >
															${rstypeMap.get(rs_type)}
														</a>
													</c:forEach>
												</div>
											</td>
										</tr>
									</c:forEach>
									<tr>
										<td align="right" nowrap>总 计：</td><td align="center" nowrap>${report.ALL_COUNT}</td><td></td><td></td><td></td>
										<td></td><td></td><td></td><td></td><td align="center" nowrap>${report.DRUG_COUNT_SUM }</td>
										<td align="center" nowrap>${report.HASZS_SUM }</td><td align="center" nowrap>${report.HASKJ_SUM }</td>
										<td align="center" nowrap>${report.BASEDRUG_COUNT_SUM }</td>
										<td align="center" nowrap><fmt:formatNumber value="${report.AMOUNT_SUM}" pattern="###,###,##0.00"></fmt:formatNumber></td><td></td>
									</tr>
									<tr>
										<td align="right" nowrap>平 均：</td><td></td><td></td><td></td><td></td>
										<td></td><td></td><td></td><td></td><td align="center" nowrap>${report.DRUG_COUNT_AVG }</td>
										<td></td><td></td>
										<td></td>
										<td align="center" nowrap>${report.AMOUNT_AVG }</td><td></td>
									</tr>
									<tr>
										<td align="center"> % </td><td></td><td></td><td></td><td></td>
										<td></td><td></td><td></td><td></td><td></td>
										<td align="center" nowrap>${report.HASZS_PERSENTS }</td><td align="center" nowrap>${report.HASKJ_PERSENTS}</td>
										<td align="center" nowrap>${report.BASEDRUG_COUNT_PERSENTS }</td>
										<td></td><td></td>
									</tr>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
									
							</tbody>
						</table>
						</div>
						<c:if test="${pd.randomflag!=1}">
						<div class= "pageStrDiv" id="pageStrDiv" style="padding-top: 5px;padding-bottom: 5px;">
							<table style="width:100%;">
								<tr>
									<td>
										<div class="pagination" style="float: right;padding: 0px;margin: 0px;">${page.pageStr}</div>
									</td>
								</tr>
							</table>
						</div>
						</c:if>
	
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
<script type="text/javascript" src="static/js/common/lockTable.js?v=201612"></script>
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

	//重置当前页面高度，自适应浏览器
	initWidthHeight();
	$(window).off('resize').on('resize', function() {
		initWidthHeight();
	}).trigger('resize');
});
//重置当前页面高度，自适应浏览器
function initWidthHeight(){
	var rr = new Array;
	rr[0]="searchDiv";
	rr[1]="pageStrDiv";
	FixTable("simple-table", 1, rr);
}
//
function detailPresc(id,NGROUPNUM){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="处方详情及点评";
	diag.URL = path + "/presc/prescDetail.do?id=" +  id +"&NGROUPNUM="+NGROUPNUM;
	diag.Width =  window.screen.width;
	diag.Height =  window.screen.height;  
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		nextPage(${page.currentPage});
	 };
	 diag.show();
}
function prescListExport(){
	window.open(path + "/presc/prescListExport.do?work=1&"+$("#searchDiv").serialize()+"&randomFlag="+'${pd.randomFlag}');
}
function myprint(){
	$("#main-container").hide();
	var tableFixClone = $("#simple-table").clone(true);
	$("<div id='myprint' style='width=100%;height=100%;'></div>").appendTo($("body"));
	tableFixClone.appendTo($("#myprint"));
	$("#myprint").css("z-index",9999) .css("position","absolute").css("left",0).css("top",0).css("background-color","white");
	window.print();
	$("#myprint").remove();
	$("#main-container").show();
}
function randomQuery(){
	window.location.href=path + "/presc/prescWorkListPage.do?randomFlag=1&"+$("#searchForm").serialize();
}
function changeNum(obj){
	var mynum = $(obj).val();
	if(isNaN(obj)){
		if(mynum.indexOf("\.")!=-1){
			$(obj).val(50);
		}
		if(mynum>200){
			$(obj).val(200);
		}
	}else{
		$(obj).val(50);
	}
}
</script>
</html>
