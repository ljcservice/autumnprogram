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
						<div >
							<form name="checkForm" id="checkForm" action="DoctOrder/CheckRsViewUI.do?patient_id=${page.pd.patient_id}&visit_id=${page.pd.visit_id}&ngroupnum=${page.pd.ngroupnum}" method="post">
							<input type="hidden" value="${page.pd.patient_id}" name="patient_id" id="patient_id"/>
							<input type="hidden" value="${page.pd.visit_id}"   name="visit_id"   id="visit_id"/>
							<input type="hidden" value="${page.pd.ngroupnum}"  name="ngroupnum"  id="ngroupnum"/>
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
								<thead>
									<tr style="padding-top: 0px;padding-bottom: 0px;">
										<th colspan="4" style="vertical-middle;">
										
											<label>
												<input type="checkbox" name="delAll" id="delAll" class="ace" onclick="setCheckBox(this);">
												<span class="lbl"></span>
											</label>
												&nbsp;&nbsp;&nbsp;
											<button class="btn btn-minier btn-yellow"  title="批量删除" onclick="delCheckRs('delBatch');">
												<i class="ace-icon fa fa-trash-o bigger-120 "></i>
											</button>
											<button class="btn btn-minier btn-danger"  title="添加点评" onclick="addCheckRs('${page.pd.ngroupnum}');">
												<i class="ace-icon fa fa-pencil bigger-120 "></i>
											</button>
											<button  class="btn btn-minier btn-success" title="刷新点评结果" onclick="document.form[0].submit();">
												<i class="ace-icon fa fa-refresh bigger-120"></i>
											</button>
											
											&nbsp;
											|
											&nbsp;
											
											<button class="btn btn-minier btn-info"  title="专家点评" onclick="expertCheckRs('${page.pd.ngroupnum}');">
												<i class="ace-icon fa fa-users bigger-120 "></i>
											</button>
											&nbsp;
											|
											&nbsp;
											<span style="text-align: left;vertical-align: middle;">
											是否合理：
												<label style="padding-top: 0px;padding-bottom: 0px;margin-top: 0px;margin-bottom: 0px;">
													<input name="isCheckTrueInput" 
														<c:if test="${empty checkRss || checkRss.size() ==0 }"> checked="checked" </c:if>	 
														class="ace ace-switch ace-switch-6" type="checkbox" style="size:5;"  />
													<span class="lbl" ></span>
												</label>
											</span>
											
											<button class="btn btn-minier btn-grey" title="保存" onclick="saveIsCheckTrue()">
												<i class="ace-icon fa fa-floppy-o bigger-120"></i>
											</button>
										</th>
									</tr>
								</thead>
														
								<tbody>
									<!-- 开始循环 -->	
									<c:choose>
										<c:when test="${not empty checkRss}">
											<c:forEach items="${checkRss}" var="rs" varStatus="vs" >
												<tr>
													<td class='center' style="width:30px;vertical-align: middle;" rowspan="2" >
													<label>
														<input type="checkbox" name="del"  class="ace" value="${rs.rs_id }"  >
														<span class="lbl"></span>
													</label>
													</td>
													<td style="width: 80px;" class='center' > ${rsTypeDict.get(rs.IN_RS_TYPE).rs_type_name }</td>
													<td >${rs.drug_id1_name } 
														<c:if test="${rsTypeDict.get(rs.RS_COUNT) == 2}"> 
														<font color="red">与</font> ${rs.drug_id2_name } 
														 </c:if>
													</td>
													<td style="width: 300px;">
														<button class="btn btn-minier btn-yellow"  title="删除" onclick="delCheckRs('del');">
															<i class="ace-icon fa fa-trash-o bigger-120 "></i>
														</button>
														<button class="btn btn-minier btn-info"  title="修改" onclick="editCheckRs('${rs.rs_id}');">
															<i class="ace-icon fa fa-pencil-square-o bigger-100 "></i> 
														</button>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp; ${rs.ALERT_HINT } </td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr class="main_info">
												<td colspan="4" class="center">没有相关数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							</form>
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
	
	
	$('[data-rel=tooltip]').tooltip();
	$('[data-rel=popover]').popover({html:true});
	
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