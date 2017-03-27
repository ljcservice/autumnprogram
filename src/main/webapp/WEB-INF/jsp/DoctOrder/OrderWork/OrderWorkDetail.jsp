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
/* 	background-color: #ffb951;	 */
 }

</style>
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container" >
		<!-- /section:basics/sidebar -->
		<div class="main-content" >
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12"  >
						<table border="0" style="padding: 0 0 0 0;">
							<tr height="150px" > 
								<td style="margin: 0px 0px 0px 0px;" >
									<div class="row">
								<div class="col-sm-6" >
										<!-- #section:elements.tab -->
										<div class="tabbable" id="myTabbable" >
											<ul class="nav nav-tabs" id="myTab">
												<li class="active">
													<a data-toggle="tab" href="#doctOrder">
														<i class="green ace-icon fa fa-home bigger-120"></i>
														患者信息
													</a>
												</li>

												<li>
													<a data-toggle="tab" href="#opertion">
														手术情况
														<c:if test="${not empty oper}">
															<span class="badge badge-danger">${oper.size() }</span>
														</c:if>
													</a>
												</li>

<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#examination"> -->
<!-- 														检验情况 -->
<!-- 														<span class="badge badge-danger">3</span> -->
<!-- 													</a> -->
<!-- 												</li> -->
<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#nurse"> -->
<!-- 														护理情况 -->
<!-- 														<span class="badge badge-danger">1</span> -->
<!-- 													</a> -->
<!-- 												</li> -->
												
<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#ICUInfo"> -->
<!-- 														重症监护 -->
<!-- 														<span class="badge badge-danger"></span> -->
<!-- 													</a> -->
<!-- 												</li> -->
												
<!-- 												<li> -->
<!-- 													<a data-toggle="tab" href="#inspection"> -->
<!-- 														检查报告 -->
<!-- 														<span class="badge badge-danger">3</span> -->
<!-- 													</a> -->
<!-- 												</li> -->
												<li>
													<a data-toggle="tab" href="#orderCheck" >
														点评结果
														<c:if test="${not empty checkRss}">
															<span class="badge badge-danger">${checkRss.size()}</span>
														</c:if>
													</a>
												</li>
											</ul>

											<div class="tab-content" style="overflow-y:auto;height:150px;padding-top: 2px;padding-left: 2px;padding-right: 2px;padding-bottom: 0px;">
												<div id="doctOrder" class="tab-pane fade in active" >
													<table class="table table-bordered table-striped table-responsive " style="font-size:10px;" >
														<tbody>
															<tr>
																 <td width="10%" class="info">患者ID:</th>
																 <th width="15%"  >${pat.patient_id }(${pat.visit_id})</th>
																 <td width="10%" class="info">患者姓名:</th>
																 <th width="15%" >${pat.name}</th>
																 <td width="10%" class="info">性别:</th>
																 <th width="15%" >${pat.sex}</th>
																 <td width="10%" class="info">年龄:</th>
																 <th width="15%" >
																 	${pat.PAT_AGE }
																 </th>
															</tr>
															  
															<tr>
																  <td width="10%" class="info">出生日期:</th>
																 <th width="15%"  ><fmt:formatDate value="${pat.DATE_OF_BIRTH }" pattern="yyyy-MM-dd"/></th>
																 <td width="10%" class="info">费别:</th>
																 <th width="15%" >${pat.charge_type }</th>
																 <td width="10%" class="info">住院天数:</th>
																 <th width="15%" > ${ pat.dayCount} </th>
																 <td width="10%" class="info">诊断:</th>
																 <th width="15%" >啧啧啧</th>
															</tr>
															
															<tr>
																 <td width="10%" class="info">入院科室:</th>
																 <th width="15%"  >${pat.in_dept_name }</th>
																 <td width="10%" class="info">入院时间:</th>
																 <th width="15%" >
																 	<fmt:formatDate value="${pat.admission_date_time }" pattern="yyyy-MM-dd"/>
																 </th>
																 <td width="10%" class="info">出院科室:</th>
																 <th width="15%" >${pat.out_dept_name }</th>
																 <td width="10%" class="info">出院时间:</th>
																 <th width="15%" >
																 	<fmt:formatDate value="${pat.discharge_date_time}" pattern="yyyy-MM-dd"/> </td>
																 </th>
															</tr>
															
														</tbody>
													</table>
												</div>

												<div id="opertion" class="tab-pane fade" >
													<table id="simple-table" class="table table-striped table-bordered table-hover" >
													<thead>
														<tr>
															<th class="center" nowrap>手术名称</th>
															<th class="center" nowrap>切口等级</th>
															<th class="center" nowrap>切口愈合情况</th>
															<th class="center" nowrap>手术日期</th>
															<th class="center" nowrap>麻醉方法</th>
															<th class="center" nowrap>手术医师</th>
														</tr>
													</thead>
													<tbody>
													<!-- 开始循环 -->	
													<c:choose>
														<c:when test="${not empty oper}">
															<c:forEach items="${oper}" var="oper" varStatus="vs">
																<tr ondblclick="edit(this,'Edit')">
																	<td class='center' >
																		${oper.OPERATION_DESC }
																	</td>
																	<td class="center ">
																		${oper.WOUND_GRADE}
																	</td>
																	<td class="center " >${oper.HEAL }</td>
																	<td class="center " ><fmt:formatDate value="${oper.OPERATING_DATE }" pattern="yyyy-MM-dd"/></td>
																	<td class="center " >${oper.ANAESTHESIA_METHOD }</td>
																	<td class="center " >${oper.OPERATOR } </td>
																</tr>
															
															</c:forEach>
														</c:when>
														<c:otherwise>
															<tr class="main_info">
																<td colspan="8" class="center">没有相关数据</td>
															</tr>
														</c:otherwise>
													</c:choose>
													</tbody>
												</table>
												</div>

												<div id="orderCheck" class="tab-pane fade" >
													<iframe  name="CheckRsFrame" id="CheckRsFrame" scrolling ="auto"" frameborder="0"
														src="DoctOrder/CheckRsViewUI.do?patient_id=${page.pd.patient_id}&visit_id=${page.pd.visit_id}&ngroupnum=${page.pd.ngroupnum}" style="margin:0px 0px 0px 0px;width:100%;height: 135px;">
													</iframe>
												</div>
											</div>
										</div>
										
										<!-- /section:elements.tab -->
									</div><!-- /.col -->
									</div> 
									
								</td>
							</tr>
							<tr id="trLagout" height="*" >
								<td>
									<div style="height:430px;overflow-y:hidden;overflow-x:hidden; ">
										<iframe name="DoctFrame" id="DoctFrame" scrolling="no" frameborder="0" 
											src="DoctOrder/DoctOrdersDetail.do?patient_id=${page.pd.patient_id}&visit_id=${page.pd.visit_id}&ngroupnum=${page.pd.ngroupnum}" style="margin:0 auto;width:100%;height: 100%">
										</iframe>
									</div>
								</td>
							</tr>
						</table>
						</div>
						
<!-- 						<div name="" style="padding-top: 30px;"> -->
<!-- 							<table style="width:100%;" border="0"> -->
<!-- 								<tr> -->
<!-- 									<td style="width:15%;" valign="top"> -->
<!-- 										<div style='overflow: scroll;max-width: 350px;max-height: auto;'> -->
<!-- 											<div id="treeName">本体数</div> -->
<!-- 											<div id="treeId"> -->
<!-- 												<ul id="leftTree" class="ztree"></ul> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</td> -->
<!-- 									<td style="width:85%;" valign="top" > -->
<%-- 										<iframe name="treeFrame" id="treeFrame" scrolling="no" frameborder="0" src="${basePath}ontology/ontologyList.do?ontoType=${ontoType}" style="margin:0 auto;width:100%;"></iframe> --%>
<!-- 										<iframe name="osynFrame" id="osynFrame" frameborder="0" src="${basePath}osyn/osynList.do?initFlag=0&ontoType=${ontoType}" style="margin:0 auto;width:100%;height: 300px;"></iframe> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 							</table> -->
<!-- 						</div> -->
						
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
	<script src="static/ace/js/chosen.jquery.js?v=2008001"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2018082"></script>
<script type="text/javascript" src="static/js/ontology/tree.js?v=20180861"></script>
<script type="text/javascript">
$(top.hangge());
$(function() {
	//changeTree();
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	var myObj = document.getElementById("myTabbable");
	myObj.style.width = "970px"; 
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		//resize the chosen on window resize
	
		$(window)
		.off('resize.chosen')
		.on('resize.chosen', function() {
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.width()});
			})
		}).trigger('resize.chosen');
		//resize chosen on sidebar collapse/expand
		$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
			if(event_name != 'sidebar_collapsed') return;
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.width()});
			})
		});
	
	
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}

});
// 查询
function searchs(){
	top.jzts();
	$("#searchForm").submit();
	
}

</script>
</html>