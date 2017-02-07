<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ts" uri="/rights"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
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
					<div class="row" style="height:30%">
						<div class="col-xs-12" >
							<c:if test="${nlp_pd.SRC_DESC==null }"> <span>原始诊断名称：</span><span style="color: blue">${pd.o_diag_name }</span>
							<span>NLP切词结果：</span><span style="color: blue">${pd.nlp_diag_name }</span>
							<span>MTS匹配结果：</span><span style="color: blue"> ${pd.mts_code }</span> 
							</br> </c:if>
							<c:if test="${nlp_pd.SRC_DESC!=null }">
							<span>原始诊断名称：</span><span style="color: blue">${nlp_pd.SRC_DESC }</span>
							<span>NLP切词结果：</span><span style="color: blue">${nlp_pd.ERR_DESC }</span>
							<span>审核后NLP切词结果：</span><span style="color: blue">${nlp_pd.RIGHT_DESC }</span>
							<span>MTS匹配结果：</span><span style="color: blue"> ${nlp_pd.mts_code }</span> 
							</br></c:if>
							<span>子问题单信息</span> 
							<table id="simple-table"
								class="table table-striped table-bordered table-hover"
								style="margin-top: 5px;">
								<thead>
									<tr>
										<th class="center" style="width: 50px;">序号</th>
										<td style="display: none;">问题单ID</td>
										<th class="center">诊断名称</th>
										<th class="center">来源</th>
										<th class="center">问题单状态</th>
										<th class="center">编号</th>
									</tr>
								</thead>

								<tbody>

									<!-- 开始循环 -->
									<c:choose>
										<c:when test="${not empty child_list}">
											<c:forEach items="${child_list}" var="q" varStatus="vs">
												<tr onclick="q_click(this);" >
													<td class='center' style="width: 30px;">${vs.index+1}</td>
													<td style="display: none;" class="center">${q.CHILD_ID }</td>
													<td class="center">${q.DIAG_NAME }</td>
													<td class="center">${q.ORIGIN }</td>
													<td class="center"><c:if
															test="${q.STATUS == '0' }">
															<span>初始</span>
														</c:if> <c:if test="${q.STATUS == '1' }">
															<span>一审</span>
														</c:if> <c:if test="${q.STATUS == '2' }">
															<span>二审</span>
														</c:if> 
													</td>
													<td class='center' style="width: 50px;">${q.ORDERS }</td>
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
							<span>问题单结果信息</span> 
							<table id="simple-table"
								class="table table-striped table-bordered table-hover"
								style="margin-top: 5px;">
								<thead>
									<tr>
										<th class="center" style="width: 50px;">序号</th>
										<td style="display: none;">问题单ID</td>
										<th class="center">诊断名称</th>
										<th class="center" style="display: none;">诊断结果</th>
										<th class="center" nowrap>标准词名称</th>
										<th class="center" nowrap>标准词编码</th>
										<th class="center" nowrap>上位词名称</th>
										<th class="center" nowrap>上位词编码</th>
										<th class="center" nowrap>同义词类型</th>
										<th class="center" nowrap>术语类型</th>
										<th class="center">干预类型</th>
										<th class="center" nowrap>备注</th>
										<th class="center">编号</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty res_list}">
											<c:forEach items="${res_list}" var="res" varStatus="vs">
												<tr >
													<td class='center' style="width: 30px;">${vs.index+1}</td>
													<td style="display: none;" class="center">${res.CHILD_ID }</td>
													<td class="center">${res.DIAG_WORD }</td>
													<td class="center" style="display: none;">${res.DIAG_ID }</td>
													<c:choose>
												       <c:when test="${res.DIS_TYPE == '1' }">
												            <td>${res.STD_CN}</td>
															<td>${res.STD_CODE}</td>
															<td></td>
															<td></td>
												       </c:when>
												       <c:otherwise>
												       		<td></td>
															<td></td>
												            <td>${res.STD_CN }</td>
															<td>${res.STD_CODE }</td>
												       </c:otherwise>
													</c:choose>
													<td>${res.SYN_TYPE }</td>
													<td>${res.TERM_TYPE }</td>
													<td style="width: 60px;" class="center">
														<c:if test="${res.DIS_TYPE == '0' }"><span class="label label-success arrowed">默认值</span></c:if>
														<c:if test="${res.DIS_TYPE == '1' }"><span class="label label-success arrowed-in">同义词</span></c:if>
														<c:if test="${res.DIS_TYPE == '2' }"><span class="label label-success arrowed-in">下位词</span></c:if>
														<c:if test="${res.DIS_TYPE == '3' }"><span class="label label-success arrowed-in">无法干预</span></c:if>
													</td>
													<td>${res.REMARK }</td>
													<td class='center' style="width: 50px;">${res.ORDERS }</td>
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
					</div>
				</div>
			</div>
		</div>

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
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
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		$(window)
		.off('resize.chosen')
		.on('resize.chosen', function() {
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.parent().width()});
			});
		}).trigger('resize.chosen');
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
//选择上个列表则选择下个列表
function q_click(tr_obj){
	var diag_name=tr_obj.children[2].innerHTML;//子问题单的诊断名称
	//便利问题单的处理结果 找到当前问题单的处理结果进行变色
	var s3 = document.getElementsByTagName("table")[1];
	for ( var i = 1; i < s3.rows.length; i++) {
		var va2=s3.rows[i].cells[2].innerHTML;
		if (diag_name == va2) {
			s3.rows[i].style.color = 'red'; //设置符合要求单元格的样式
		}else{
			s3.rows[i].style.color = 'black';
		}
	}
}

</script>
</html>
