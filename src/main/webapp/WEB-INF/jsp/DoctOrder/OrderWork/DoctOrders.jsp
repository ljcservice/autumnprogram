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
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div >
						<form action="DoctOrder/DoctOrdersDetail.do?patient_id=${page.pd.patient_id}&visit_id=${page.pd.visit_id}" method="post">
							<input type="hidden" name="patient_id" value="${page.pd.patient_id}" id="patient_id"/>
							<input type="hidden" name="visit_id" value="${page.pd.visit_id}" id="visit_id"/>
							<input type="hidden" name="repeat_indicator" value="${page.pd.repeat_indicator}" id="repeat_indicator"/>
							<input type="hidden" name="repeat_indicator_name" value="${page.pd.repeat_indicator_name}" id="repeat_indicator_name"/>
							<input type="hidden" name="order_class" value="${page.pd.order_class}" id="order_class"/>
							<input type="hidden" name="order_class_name" value="${page.pd.order_class_name}" id="order_class_name"/>
							<input type="hidden" name="ngroupnum" value="${page.pd.ngroupnum }" id="ngroupnum"/>
							<input type="hidden" name="show_type" value="${page.pd.show_type }" id="show_type"/>
							<input type="hidden" name="show_type_name" value="${page.pd.show_type_name }" id="show_type_name"/>
							<!-- 快捷审核名字  -->
							<input type="hidden" name="shortcutName" value="${page.pd.shortcutName}" id="shortcutName"/>
							<!-- 点评结果之一 -->
							<input type="hidden" name="checkJsonInfo" value='${page.pd.checkJsonInfo}' id="checkJsonInfo"/>
						<div style="vertical-align:bottom;padding-top: 5px;padding-bottom: 5px;">
							<div style="margin-top: 5px;margin-bottom: 5px;">
								<span><b> <font color="blue" >医嘱信息</font></b></span>
								<div style="float: right;margin-bottom: 5px;">
									<div class="btn-toolbar" style="float: right;">
									
									<div class="btn-group">
										<button id="orderViewType" class="btn btn-sm btn-yellow">
											${page.pd.show_type_name != null && page.pd.show_type_name !=''?page.pd.show_type_name:"医嘱查看切换"}
										</button>
										<button data-toggle="dropdown" class="btn btn-sm btn-yellow dropdown-toggle">
											<i class="ace-icon fa fa-angle-down icon-only"></i>
										</button>
										<ul class="dropdown-menu dropdown-yellow">
											<li>
												<a href="javascript:show_type(0,'常规查看');">常规查看</a>
											</li>
											<li>
												<a href="javascript:show_type(1,'按日分解查看');">按日分解查看</a>
											</li>
											<li>
												<a href="javascript:show_type(2,'按日图分解查看');">按日图分解查看</a>
											</li>
											<li>
												<a href="javascript:show_type(3,'术后医嘱');">术后医嘱</a>
											</li>
											<li class="divider"></li>
											<li>
												<a href="#">Separated link</a>
											</li>
										</ul>
									</div><!-- /.btn-group -->
									
									
									<div class="btn-group">
										<button id="orderViewClass" class="btn btn-sm btn-info">
											${page.pd.order_class_name != null && page.pd.order_class_name !=''?page.pd.order_class_name:"医嘱类别"}
										</button>
										<button data-toggle="dropdown" class="btn btn-sm btn-info dropdown-toggle">
											<i class="ace-icon fa fa-angle-down icon-only"></i>
										</button>
										<ul class="dropdown-menu dropdown-info">
<!-- 										此处可以使用参数的方式进行设置 -->
											<li>
												<a href="javascript:orderViewClass('A','药疗');"> 药疗</a>
											</li>
											<li>
												<a href="">处置</a>
											</li>
											<li>
												<a href="">检查</a>
											</li>
											<li>
												<a href="#">化验</a>
											</li>
											<li>
												<a href="#">手术</a>
											</li>
											<li>
												<a href="#">护理</a>
											</li>
											<li>
												<a href="#">膳食</a>
											</li>
											<li>
												<a href="#">其他</a>
											</li>
											<li class="divider"></li>
											<li>
												<a href="#"> 未选择</a>
											</li>
										</ul>
									</div><!-- /.btn-group -->
									
									<div class="btn-group">
										<button id="orderViewType" class="btn btn-sm btn-yellow">
											${page.pd.repeat_indicator_name != null && page.pd.repeat_indicator_name !=''?page.pd.repeat_indicator_name:"医嘱类型"}
										</button>
										<button data-toggle="dropdown" class="btn btn-sm btn-yellow dropdown-toggle">
											<i class="ace-icon fa fa-angle-down icon-only"></i>
										</button>
										<ul class="dropdown-menu dropdown-yellow">
											<li>
												<a href="javascript:orderViewType('','全部医嘱');">全部医嘱</a>
											</li>
											<li>
												<a href="javascript:orderViewType('1','长期医嘱');">长期医嘱</a>
											</li>
											<li>
												<a href="javascript:orderViewType('0','临时医嘱');">临时医嘱</a>
											</li>
											<li>
												<a href="javascript:orderViewType('operation','手后医嘱');">手后医嘱</a>
											</li>
											<li class="divider"></li>
											<li>
												<a href="#">Separated link</a>
											</li>
										</ul>
									</div><!-- /.btn-group -->
									
										<div class="btn-group">
											<button data-toggle="dropdown" class="btn btn-info btn-sm dropdown-toggle">
												<span id="shortcutNameSpan" >
													${page.pd.shortcutName != null && page.pd.shortcutName !=''?page.pd.shortcutName:"快捷点评"}
												</span>
												<span class="ace-icon fa fa-caret-down icon-on-right"></span>
											</button>

											<ul class="dropdown-menu dropdown-info dropdown-menu-right"> 
												<c:forEach items="${rsTypeDict.keySet()}" var="dict" varStatus="vs">
												<li>
													<a href="javascript:shortcutCheck(${rsTypeDict.get(dict).rs_count },'${rsTypeDict.get(dict).rs_type_code }','${rsTypeDict.get(dict).rs_type_name }');">${rsTypeDict.get(dict).rs_type_name }</a>
												</li>
												</c:forEach>
												<li class="divider"></li>
												<li>
													<a href="javascript:reSetCheck()" id="resetCheckId">
														<c:if test="${page.pd.shortcutName != null && page.pd.shortcutName !=''}">
															已经选中：${page.pd.shortcutName }
														</c:if>
														<c:if test="${page.pd.shortcutName == null || page.pd.shortcutName ==''}">
															未选择点评项
														</c:if>
													</a>
												</li>
											</ul>
										</div><!-- /.btn-group -->
									</div>
								</div>
							</div>
						</div>
						<div >
						<!-- 	常规查看 -->
						<c:if test="${page.pd.show_type== null || page.pd.show_type=='' || page.pd.show_type==0}">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" nowrap>
									</th>
									<th class="center" nowrap>医嘱名称</th>
									<th class="center" nowrap>医嘱类型</th>
									<th class="center" nowrap>医嘱科室</th>
									<th class="center" nowrap>用法</th>
									<th class="center" nowrap>用量</th>
									<th class="center" nowrap>途径</th>
									<th class="center" nowrap>开始时间</th>
									<th class="center" nowrap>结束时间</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty DoctOrders}">
									<c:forEach items="${DoctOrders}" var="order" varStatus="vs">
										<tr ondblclick="orderCheck(this)"  id="tr${order.order_no}${order.order_sub_no}" 
											order_no="${order.order_no}" order_sub_no="${order.order_sub_no}" order_name="${order.order_Text }" >
											<c:set var="key1" >
											${order.order_no.toString()}_${order.order_sub_no.toString()}
											</c:set>
											<td class='center' style="padding-bottom: 0px;">
												<c:if test="${CheckRss.containsKey(key1)}">
												  	
														<a class="fa fa-flag red bigger-130"
															data-rel="popover" 
															data-placement="right" 
															title="<i class='ace-icon fa fa-check red'></i>   ${order.order_Text}" 
															data-content="<font size='0'>
																<c:forEach items="${CheckRss.get(key1)}" var="rs">
																	<b>${rsTypeDict.get(rs.IN_RS_TYPE).rs_type_name }:  
																	<c:if test="${rs.drug_id1_name != order.order_Text }"> 
																		${rs.drug_id1_name }</b>
																	</c:if>
																	<c:if test="${rs.drug_id2_name != order.order_Text }"> 
																		${rs.drug_id2_name }</b>
																	</c:if>
																	 <br>
																	${rs.ALERT_HINT }<br>
																</c:forEach>	
															</font>"
														></a>
												</c:if>
											</td>
											
											<td class='center' >
												${order.order_Text }
											</td>
											<td class="center ">
												${order.order_class}
											</td>
											<td class="center " >${order.dept_name}</td>
											<td class="center " >${order.frequency }</td>
											<td class="center ">
												<fmt:formatNumber value="${order.dosage }" pattern="#0.00"></fmt:formatNumber>
												${order.dosage_units }
											</td>
											<td class="center " >${order.administration } </td>
											<td class="center " >${order.start_date_time }</td>
											<td class="center " >${order.stop_date_time }</td>
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
						</c:if>
						<!-- 	按日分解查看						 -->
						<c:if test="${page.pd.show_type!= null && page.pd.show_type==1}">		
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" nowrap>日期</th>
									<th class="center" nowrap> </th>
									<th class="center" nowrap>医嘱名称</th>
									<th class="center" nowrap>医嘱类型</th>
									<th class="center" nowrap>医嘱科室</th>
									<th class="center" nowrap>用法</th>
									<th class="center" nowrap>用量</th>
									<th class="center" nowrap>途径</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty datestrMap and not empty DoctOrders}">
									<c:forEach items="${datestrMap}" var="dateMap" varStatus="vs">
											<c:set var="myindex">0</c:set>
											<c:forEach items="${DoctOrders}" var="order" varStatus="vs">
											<c:if test="${order.datestr == dateMap.key}">
											<tr ondblclick="orderCheck(this)"  id="tr${order.order_no}${order.order_sub_no}" 
												order_no="${order.order_no}" order_sub_no="${order.order_sub_no}" order_name="${order.order_Text }" >
											<c:if test="${myindex==0}">
												<td class="center mydateclass" rowspan="${dateMap.value}">${dateMap.key }</td>
											</c:if>
											<c:set var="myindex">${myindex+1}</c:set>
											<td class='center' style="padding-bottom: 0px;">
												<c:set var="key1" >
													${order.order_no.toString()}_${order.order_sub_no.toString()}
												</c:set>
												<c:if test="${CheckRss.containsKey(key1)}">
														<a class="fa fa-flag red bigger-130"
															data-rel="popover" 
															data-placement="right" 
															title="<i class='ace-icon fa fa-check red'></i>   ${order.order_Text}" 
															data-content="<font size='0'>
																<c:forEach items="${CheckRss.get(key1)}" var="rs">
																	<b>${rsTypeDict.get(rs.IN_RS_TYPE).rs_type_name }:  
																	<c:if test="${rs.drug_id1_name != order.order_Text }"> 
																		${rs.drug_id1_name }</b>
																	</c:if>
																	<c:if test="${rs.drug_id2_name != order.order_Text }"> 
																		${rs.drug_id2_name }</b>
																	</c:if>
																	 <br>
																	${rs.ALERT_HINT }<br>
																</c:forEach>	
															</font>"
														></a>
												</c:if>
											</td>
											
											<td class='center' >
												${order.order_Text }
											</td>
											<td class="center ">
												${order.order_class}
											</td>
											<td class="center " >${order.dept_name}</td>
											<td class="center " >${order.frequency }</td>
											<td class="center ">
												<fmt:formatNumber value="${order.dosage }" pattern="#0.00"></fmt:formatNumber>
												${order.dosage_units }
											</td>
											<td class="center " >${order.administration } </td>
										</tr>
										</c:if>
									</c:forEach>
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
						
						</c:if>	
						</div>
						<div class="page-header position-relative">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;">
										<div class="pagination" style="float: right;padding-top: 0px;margin: 0px;">${page.pageStr}</div>
									</td>
								</tr>
							</table>
						</div>
						
								<div  id="dragCheck" style="width: 400px;position: absolute;top: 100px;left:260px;display:none;" class="drag"   >
										<!-- #section:custom/widget-box -->
										<div class="widget-box" >
											<div class="widget-header" >
												<h5 class="widget-title" >审核结果</h5>
												<!-- #section:custom/widget-box.toolbar -->
												<div class="widget-toolbar">
<!-- 													<div class="widget-menu"> -->
<!-- 														<a href="#" data-action="settings" data-toggle="dropdown"> -->
<!-- 															<i class="ace-icon fa fa-bars"></i> -->
<!-- 														</a> -->
<!-- 													</div> -->

<!-- 													<a href="#" data-action="fullscreen" class="orange2"> -->
<!-- 														<i class="ace-icon fa fa-expand"></i> -->
<!-- 													</a> -->

<!-- 													<a href="#" data-action="reload"> -->
<!-- 														<i class="ace-icon fa fa-refresh"></i> -->
<!-- 													</a> -->
													<a href="javascript:dragSave()" data-action="collapse" title="保存">
														<i class="ace-icon fa blue fa-check-square-o"></i>
													</a>

													<a href="javascript:dragClose()" data-action="close" title="关闭">
														<i class="ace-icon fa fa-times"></i>
													</a>
												</div>
												<!-- /section:custom/widget-box.toolbar -->
											</div>
											<div class="widget-body">
												<div class="widget-main">
													<div>
													<span id="checkDrug1"></span>
													<span id="checkDrug2"></span>
													</div>
													<div style="vertical-align: text-top;">
														<div> <b>问题说明:</b></div>
														<textarea rows="4" cols="50" id="checkText"></textarea>
													</div>
												</div>
											</div>
										</div>
									</div>
						
						</form>
						
						</div>
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

	<script type="text/javascript">
	
	$(top.hangge());
	$(function() {
		
		$('[data-rel=tooltip]').tooltip();
		$('[data-rel=popover]').popover({html:true});
		
		var _move = false; //移动标记
		var _x, _y; //鼠标离控件左上角的相对位置
		$(".drag").mousedown(function(e) {
		     _move = true;
		     _x = e.pageX - parseInt($(".drag").css("left"));
		     _y = e.pageY - parseInt($(".drag").css("top"));
		     $(".drag").fadeTo(20, 0.6); //点击后开始拖动并透明显示
		});
		$(".drag").mouseup(function(e) {
			_move = false;
		     $(".drag").fadeTo("fast", 1); //松开鼠标后停止移动并恢复成不透明
		});
		$(".drag").mousemove(function(e) {
		     if (_move) {
		        var x = e.pageX - _x; //移动时根据鼠标位置计算控件左上角的绝对位置
		        var y = e.pageY - _y;
		        $(".drag").css({ top: y, left: x }); //控件新位置
		    }
		}).mouseup(function() {
		     _move = false;
		     $(".drag").fadeTo("fast", 1); //松开鼠标后停止移动并恢复成不透明
		});
	});
	
	window.onload = function(){
		var cji = $("#checkJsonInfo").val();
		if(cji == '' || cji == 'undefind') return ;
		var jsonObj = JSON.parse(cji);
		var id = "#tr" + jsonObj.order_no + "" + jsonObj.order_sub_no ;
		$(id).css("background-Color","red");
		setCount = jsonObj.setCount;
		checkFlag = jsonObj.checkFlag ;
		checkName = jsonObj.checkName;
		checkType = jsonObj.checkType;
		count     = jsonObj.count;
		order_no     = jsonObj.order_no;
		order_sub_no = jsonObj.order_sub_no;
		order_name   = jsonObj.order_name;
		checkJson    = $("#checkJsonInfo").val();
	} 
	
	var count        = 0;
	var setCount     = 0;
	var checkName    = "";
	var checkFlag    = false;
	var checkType    = "";
	var oldColor     = "";
	var order_no     = "";
	var order_sub_no = "";
	var order_name   = "";
	var checkJson    = "";
	
	function buildJson(){
		checkJson = "{\"setCount\":" + setCount + ",\"checkName\":\"" + checkName + "\",\"checkFlag\":" + checkFlag + ",\"checkType\":\"" 
			+ checkType + "\",\"oldColor\":\"" + oldColor + "\",\"order_no\":\"" + order_no + "\",\"order_sub_no\":\"" + order_sub_no + "\",\"order_name\":\"" + order_name + "\",\"count\":\"" + count + "\"}";
		return checkJson;
	}
	
	function setCheckJsonInfo(){
		$("#checkJsonInfo").val(buildJson());
	}
	
	// 快捷审核页面 保存
	function dragSave(){
		
		var ngroupnum = $("#ngroupnum").val();
		var checkText = $("#checkText").val();
		var patId     = $("#patient_id").val();
		var visitId   = $("#visit_id").val();
		alert("");
		$.ajax({
			type: "POST",
			url: basePath + 'DoctOrder/SaveShortcut.do', 
	    	data: {checkType:checkType,order_no:order_no,order_sub_no:order_sub_no
	    		,order_name:order_name,tmpOrder_Name:tmpOrder_Name,tmpOrder_sub_no:tmpOrder_sub_no
	    		,tmpOrder_no:tmpOrder_no,count:count,checkText:checkText,ngroupnum:ngroupnum,patient_id:patId,visit_id:visitId},
			dataType:'json',
			async:false,
			cache: false,
			success: function(data){
				
				if(data.result=="ok"){
					$("#dragCheck").hide(500); 
					reSetCheck();
					
					nextPage(${page.currentPage});
					// 如果成功设置旗子
					
					//var trFirst = $("#tr" + order_no + order_sub_no);
					//var trSecond = $("#tr" + tmpOrder_no + tmpOrder_sub_no);
					//if(trFirst){ 
					//	trFirst.css("background-Color",oldColor);
						//alert(trFirst);///.eq(1).html("<div class='fa fa-flag red bigger-130'></div>");
					//}
					//trSecond.css("background-Color",tmpColor);
					//trsecond.children.eq(1).html("<div class='fa fa-flag red bigger-130'></div>");
					
				}
			}
		});
		
	}
	
	// 快捷审核页面  关闭
	function dragClose(){
		bootbox.confirm({
			message: "确认离开快捷审核",
			buttons: {
			  confirm: {
				 label: "确认",
				 className: "btn-primary btn-sm",
			  },
			  cancel: {
				 label: "取消",
				 className: "btn-sm",
			  }
			},
			callback: function(result) {
				if(!result) return ;
				$("#dragCheck").hide(500);
				var trFirst = $("#tr" + order_no + order_sub_no);
				if(trFirst) trFirst.css("background-Color",oldColor);
				$("#tr" + tmpOrder_no + tmpOrder_sub_no).css("background-Color",tmpColor);
				reSetCheck();
			}
		  }
		);
	}
	
	// 审核预设置
	function shortcutCheck(_SetCount,_CheckType,_CheckName){
		setCount  = _SetCount;
		count     = _SetCount;
		checkType = _CheckType;
		checkName = _CheckName;
		checkFlag = true;;
		$("#resetCheckId").text("已经选中：" + _CheckName);
		$("#shortcutNameSpan").text(_CheckName);
		$("#shortcutName").val(_CheckName);
		setCheckJsonInfo();
	}
	
	// 重置审核项
	function reSetCheck(){
		if(!checkFlag) return ;
		count        = 0;
		setCount     = 0;
		checkName    = "";
		checkFlag    = false;
		checkType    = "";
		oldColor     = "";
		order_no     = "";
		order_sub_no = "";
		order_name   = "";
		checkJson    = "";
		$("#resetCheckId").text("未选择点评项");
		$("#shortcutNameSpan").text("快捷点评");
		$("#shortcutName").val("");
		$("#checkJsonInfo").val("");
		$("#checkText").val("");
		cleanDragDrug();
		setCheckJsonInfo();
	}
	//清空快捷审核展现药品位置
	function cleanDragDrug(){
		$("#checkDrug1").text("");
		$("#checkDrug2").text("");
	}
	
	var tmpOrder_Name   = "";
	var tmpOrder_no     = "";
	var tmpOrder_sub_no = "";
	var tmpColor        = "";
	
	//快捷点评 点选某行
	function orderCheck(_trObj){
		if(!checkFlag) return ;
		if(_trObj.style.backgroundColor=="red"){
			_trObj.style.backgroundColor = oldColor;
			order_no     = "";
			order_sub_no = "";
			order_name   = "";
			setCount++;
			setCheckJsonInfo();
			return ;
		}
		var myColor = _trObj.style.backgroundColor;
		_trObj.style.backgroundColor = "red";
		if($("#show_type").val()==1){
			$(".mydateclass").css("background-Color","white");
		}
		if(setCount == 1)
		{
			var drug1 = $("#checkDrug1");
			var drug2 = $("#checkDrug2");
			tmpOrder_Name   = _trObj.getAttributeNode("order_name").value;
			tmpOrder_no     = _trObj.getAttributeNode("order_no").value;
			tmpOrder_sub_no = _trObj.getAttributeNode("order_sub_no").value;
			tmpColor        = myColor;
			if(order_no == ""){
				
				drug1.text(_trObj.getAttributeNode("order_name").value);
				alert("一个药品可以做添加点评项目");
				
			}else{
				drug1.text(order_name);
				var  text = "<b>与</b> " + _trObj.getAttributeNode("order_name").value
				drug2.html(text);
				alert("两个药品可以做添加点评项目");
			}
			$(".widget-title").text(checkName);
			$("#dragCheck").show(500);
		}else if (setCount == 2 )
		{
			oldColor     = myColor;
			order_no     = _trObj.getAttributeNode("order_no").value;
			order_sub_no = _trObj.getAttributeNode("order_sub_no").value;
			order_name   = _trObj.getAttributeNode("order_name").value;
			setCount--;
			setCheckJsonInfo();
		}
	}
	//处理医嘱类别
	function orderViewClass(_type,_name)
	{
		showBG();
		var myform = window.document.forms[0];
		myform.order_class.value = _type;
		myform.order_class_name.value = _name;
		myform.submit();
	}
	//医嘱查看切换
	function show_type(_type,_name){
		showBG();
		var myform = window.document.forms[0];
		myform.show_type.value = _type;
		myform.show_type_name.value = _name;
		myform.submit();
	}
	
	// 处理医嘱类型
	function orderViewType(_type,_name)
	{
		showBG();
		var myform = window.document.forms[0];
		myform.repeat_indicator.value = _type;
		myform.repeat_indicator_name.value = _name;
		myform.submit();
	}
	
	//查新当前页
	function reloadPage(){
		showBG();
		window.document.forms[0].submit();
	}
		
	//单页遮罩层
	var bgObj = null;
	function closeBG()
	{
		if(bgObj != null)
		{
			document.body.removeChild(bgObj);
		}
		bgObj = null;
	}
	function showBG()
	{
		document.body.style.margin = "0";
		bgObj   = document.createElement("div");
		bgObj.setAttribute('id', 'bgDiv');
		bgObj.style.position   = "absolute";
		bgObj.style.top        = "0";
		bgObj.style.background = "#777";
		bgObj.style.filter     = "progid:DXImageTransform.Microsoft.Alpha(opacity=50)";
		bgObj.style.opacity    = "0.4";
		bgObj.style.left       = "0";
		bgObj.style.width      = "100%";
		bgObj.style.height     = "100%";
		bgObj.style.zIndex     = "1000";
		document.body.appendChild(bgObj);
	}
	</script>
	</body>
</html>