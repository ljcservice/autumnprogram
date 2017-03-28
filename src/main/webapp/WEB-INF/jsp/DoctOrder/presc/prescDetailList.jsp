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
						<form action="presc/prescDetailList.do?id=${page.pd.id}" method="post" id="myForm" name="myForm">
							<input type="hidden" name="id" value="${page.pd.id}" id="id"/>
							<input type="hidden" name="ngroupnum" value="${page.pd.ngroupnum }" id="ngroupnum"/>
							<!-- 快捷审核名字  -->
							<input type="hidden" name="shortcutName" value="${page.pd.shortcutName}" id="shortcutName"/>
							<!-- 点评结果之一 -->
							<input type="hidden" name="checkJsonInfo" value='${page.pd.checkJsonInfo}' id="checkJsonInfo"/>
						<div style="vertical-align:bottom;padding-top: 5px;padding-bottom: 5px;">
							<div style="margin-top: 5px;margin-bottom: 5px;">
								<span><b> <font color="blue" >医嘱信息</font></b></span>
								<div style="float: right;margin-bottom: 5px;">
									<div class="btn-toolbar" style="float: right;">
									<c:if test="${modifyFlag==1 }">
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
									</c:if>
										
									</div>
								</div>
							</div>
						</div>
						<div >
<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" nowrap></th>
									<th class="center" nowrap>药品名称规格</th>
									<th class="center" nowrap>单次计量</th>
									<th class="center" nowrap>用法</th>
									<th class="center" nowrap>频次</th>
									<th class="center" nowrap>用药天数</th>
									<th class="center" nowrap>单价</th>
									<th class="center" nowrap>数量</th>
									<th class="center" nowrap>单位</th>
									<th class="center" nowrap>药费</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty prescDetailList}">
									<c:forEach items="${prescDetailList}" var="order" varStatus="vs">
										<tr ondblclick="orderCheck(this)"  id="tr${order.order_no}${order.order_sub_no}" 
											order_no="${order.order_no}" order_sub_no="${order.order_sub_no}" order_name="${order.drug_name }" >
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
												${order.DRUG_NAME } ${order.DRUG_SPEC }
											</td>
											<td class="center ">${order.DOSAGE }</td>
											<td class="center ">不知道</td>
											<td class="center " >${order.FREQUENCY}</td>
											<td class="center " >${order.DRUG_USE_DAYS }</td>
											<td class="center " >${order.COSTS } </td>
											<td class="center " >${order.AMOUNT } </td>
											<td class="center " >${order.PACKAGE_SPEC}</td>
											<td class="center " >${order.COSTS * order.AMOUNT}</td>
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
														<textarea rows="4" cols="40" id="checkText"></textarea>
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
		return checkJson;
	}
	
	function setCheckJsonInfo(){
		$("#checkJsonInfo").val(buildJson());
	}
	
	// 快捷审核页面 保存
	function dragSave(){
		
		var ngroupnum = $("#ngroupnum").val();
		var checkText = $("#checkText").val();
		var id     = $("#id").val();
		$.ajax({
			type: "POST",
			url: basePath + 'presc/saveShortcut.do', 
	    	data: {checkType:checkType,order_no:order_no,order_sub_no:order_sub_no
	    		,order_name:order_name,tmpOrder_Name:tmpOrder_Name,tmpOrder_sub_no:tmpOrder_sub_no
	    		,tmpOrder_no:tmpOrder_no,count:count,checkText:checkText,ngroupnum:ngroupnum,id:id},
			dataType:'json',
			async:false,
			cache: false,
			success: function(data){
				
				if(data.result=="ok"){
					$("#dragCheck").hide(500); 
					reSetCheck();
					$("#myForm").submit();
// 					nextPage(${page.currentPage});
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
	function reSetViewType(){
		$("#resetViewTypeId").text("未选择医嘱类型");
		$("#resetViewTypeSpan").text("医嘱类型");
		var myform = window.document.forms[0];
		myform.repeat_indicator.value = "";
		myform.repeat_indicator_name.value = "";
	}
	function reSetOrderView(){
		$("#resetOrderViewId").text("未选择医嘱类别");
		$("#resetOrderViewSpan").text("医嘱类别");
		var myform = window.document.forms[0];
		myform.order_class.value = "";
		myform.order_class_name.value = "";
	}
	function reSetShowType(){
		$("#reSetShowTypeId").text("未选择");
		$("#reSetShowTypeSpan").text("医嘱查看切换");
		var myform = window.document.forms[0];
		myform.show_type.value = "";
		myform.show_type_name.value = "";
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