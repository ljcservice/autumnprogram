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
														<span class="badge badge-danger">4</span>
													</a>
												</li>

												<li>
													<a data-toggle="tab" href="#examination">
														检验情况
														<span class="badge badge-danger">4</span>
													</a>
												</li>
												<li>
													<a data-toggle="tab" href="#nurse">
														护理情况
														<span class="badge badge-danger">4</span>
													</a>
												</li>
												
												<li>
													<a data-toggle="tab" href="#ICUInfo">
														重症监护
														<span class="badge badge-danger">4</span>
													</a>
												</li>
												
												<li>
													<a data-toggle="tab" href="#inspection">
														检查报告
														<span class="badge badge-danger">4</span>
													</a>
												</li>
											</ul>

											<div class="tab-content" style="overflow-y:auto;height:150px;padding-top: 5px;padding-left: 2px;padding-right: 2px">
												<div id="doctOrder" class="tab-pane fade in active" >
													<table class="table table-bordered table-striped table-responsive " style="font-size:10px;" >
														<tbody>
															<tr>
																 <td width="10%" class="info">患者ID：</th>
																 <th width="15%"  >${pat.patient_id }(${pat.visit_id})</th>
																 <td width="10%" class="info">患者姓名:</th>
																 <th width="15%" >封神榜</th>
																 <td width="10%" class="info">性别:</th>
																 <th width="15%" >男</th>
																 <td width="10%" class="info">年龄:</th>
																 <th width="15%" >2</th>
															</tr>  
															<tr>
																 <td width="10%" class="info">入院科室</th>
																 <th width="15%"  >ddserwer</th>
																 <td width="10%" class="info">出入科室</th>
																 <th width="15%" >封神榜</th>
																 <td width="10%" class="info"></th>
																 <th width="15%" >男</th>
																 <td width="10%" class="info">年龄:</th>
																 <th width="15%" >233333333333</th>
															</tr>
															<tr>
																  <td width="10%" class="info">患者ID：</th>
																 <th width="15%"  >ddserwer</th>
																 <td width="10%" class="info">患者姓名:</th>
																 <th width="15%" >封神榜</th>
																 <td width="10%" class="info">性别:</th>
																 <th width="15%" >男</th>
																 <td width="10%" class="info">年龄:</th>
																 <th width="15%" >2</th>
															</tr>
														</tbody>
													</table>
												</div>

												<div id="opertion" class="tab-pane fade" >
													<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid.</p>
													<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid.</p>
													<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid.</p>
													<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid.</p>
													<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid.</p>
													<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid.</p>
												
												</div>

												<div id="examination" class="tab-pane fade">
													<p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's organic lomo retro fanny pack lo-fi farm-to-table readymade.</p>
												</div>

												<div id="nurse" class="tab-pane fade">
													<p>Trust fund seitan letterpress, keytar raw denim keffiyeh etsy art party before they sold out master cleanse gluten-free squid scenester freegan cosby sweater. Fanny pack portland seitan DIY, art party locavore wolf cliche high life echo park Austin.</p>
												</div>
												
												<div id="ICUInfo" class="tab-pane fade">
													<p>Trust fund seitan letterpress, keytar raw denim keffiyeh etsy art party before they sold out master cleanse gluten-free squid scenester freegan cosby sweater. Fanny pack portland seitan DIY, art party locavore wolf cliche high life echo park Austin.</p>
												</div>
												
												<div id="inspection" class="tab-pane fade">
													<p>Trust fund seitan letterpress, keytar raw denim keffiyeh etsy art party before they sold out master cleanse gluten-free squid scenester freegan cosby sweater. Fanny pack portland seitan DIY, art party locavore wolf cliche high life echo park Austin.</p>
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
									<div style="height:430px;overflow-yautod;overflow-x:hidden; ">
										<div style="margin-top: 5px;margin-bottom: 5px;">
											<span><b> <font color="blue" >医嘱信息</font></b></span>
											<div style="float: right;margin-bottom: 5px;">
												<div class="btn-toolbar">
												<div class="btn-group">
													<button class="btn btn-sm btn-yellow">医嘱类型</button>
													<button data-toggle="dropdown" class="btn btn-sm btn-yellow dropdown-toggle">
														<i class="ace-icon fa fa-angle-down icon-only"></i>
													</button>
													<ul class="dropdown-menu dropdown-yellow">
														<li>
															<a href="#">全部医嘱</a>
														</li>
														<li>
															<a href="#">长期医嘱</a>
														</li>
														<li>
															<a href="#">临时医嘱</a>
														</li>
														<li>
															<a href="#">手后医嘱</a>
														</li>
	
														<li class="divider"></li>
	
														<li>
															<a href="#">Separated link</a>
														</li>
													</ul>
												</div><!-- /.btn-group -->
												
												
													<div class="btn-group">
														<button data-toggle="dropdown" class="btn btn-info btn-sm dropdown-toggle">
															快捷点评
															<span class="ace-icon fa fa-caret-down icon-on-right"></span>
														</button>
		
														<ul class="dropdown-menu dropdown-info dropdown-menu-right">
															<li>
																<a href="#">相互作用</a>
															</li>
															<li>
																<a href="#">重复用药</a>
															</li>
															<li>
																<a href="#">禁忌症</a>
															</li>
															<li>
																<a href="#">配伍禁忌</a>
															</li>
															<li>
																<a href="#">用法用量</a>
															</li>
															<li>
																<a href="#">特殊人群</a>
															</li>
															<li class="divider"></li>
															<li>
																<a href="#"> 未选择</a>
															</li>
														</ul>
													</div><!-- /.btn-group -->
												</div>
											</div>
										</div>
										<table class="table table-striped table-bordered table-hover" >
											<thead >
												<tr >
													<th width="35%"> 医嘱名称</th>
													<th width="9%">  医嘱类型</th>
													<th width="9%" > 医嘱科室</th>
													<th width="8%" > 用法</th>
													<th width="8%" > 用量</th>
													<th width="14%"> 开始时间</th>
													<th width="*" > 结束时间</th>
												</tr>
											</thead>
											<tbody align="center" >
											<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
											</tr>
												<tr>
												<td >ccc</td>
												<td  >fff</td>  
												<td > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd	</td>
												<td  > fsdfsfd水电费水	</td>
												<td > fsdfsfd水电费水	</td>
											</tr>
											</tbody>
										</table>
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
	changeTree();
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	var myObj = document.getElementById("myTabbable");
	myObj.style.width = "970px"; 
	
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
// 查询
function searchs(){
	top.jzts();
	$("#searchForm").submit();
	
}

</script>
</html>