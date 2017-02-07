<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>诊断维护页-快捷键修改父节点,科室分类选择</title>
<base href="${basePath}">
<meta charset="utf-8" />
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
<!-- <script type="text/javascript" src="static/mts_utils.js"></script> -->
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
<link type="text/css" rel="stylesheet" href="static/css/ontology.css"/>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div id="zhongxin" class="col-xs-12" style="padding-top: 15px;">
							<div>
							<form id="parentForm">
							<table style="width:100%;" border="0">
								<tr>
									<td width="150px;" align="right" style="vertical-align: top;">已选节点： 
										<input type="hidden" name="ONTO_ID" id="ONTO_ID" value="${pd.ONTO_ID }" />
										<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
										<input type="hidden" name="businessType" value="${businessType}" id="businessType"/>
									</td>
									<td style="padding-left:12px;">
										<div class="shadow" id="selectedCont" style="height: 35px;">
											<c:choose>
											<c:when test="${not empty PARENT_LIST}">
												<c:forEach items="${PARENT_LIST}" var="parent" varStatus="vs">
													<span class='tag_grp' id="father_${parent.ID}"><b>${parent.NAME}
														</b>&nbsp;<span class="span_remove"  onclick="cancelCategory('${parent.ID}')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
														<input type='hidden' name='PARENT_IDS' value="${parent.ID}" />
														<c:if test="${parent.MAIN_CODE !=null and parent.MAIN_CODE !=''}">
															<input type='hidden' name='MAIN_CODES' value="${parent.MAIN_CODE}" />
														</c:if>
														<c:if test="${parent.ADD_CODE !=null and parent.ADD_CODE !=''}">
															<input type='hidden' name='ADD_CODES' value="${parent.ADD_CODE}" />
														</c:if>
													</span>
												</c:forEach>
											</c:when>
											</c:choose>
										
										</div>
									</td>
								</tr>
								<tr>
									<td style="width:15%;" valign="top" bgcolor="#F9F9F9">
										<div style='overflow: scroll;max-width: 360px;max-height: 500px;'>
											<div id="treeName">选择区域(双击选择)</div>
											<div style="width:15%;" id="treeId">
												<ul id="leftTree" class="ztree"></ul>
											</div>
										</div>
									</td>
									<td style="width:85%;" valign="top" >
										<iframe name="oysnWidgetFrame" id="oysnWidgetFrame" frameborder="0" src="${basePath}common/ontoWidget.do?ontoType=${ontoType}&businessType=${businessType}" style="margin:0 auto;width:100%;height: 410px;"></iframe>
									</td>
								</tr>
							</table>
							</form>
							</div>
							<div class="col-xs-12" style="text-align:center">
									<a class="btn btn-mini btn-primary" onclick="selectCont();">确定</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
							</div>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	<div id="detailInfo" class="detailInfo" style="display: none;position: absolute;">
		<div>详情: <span onclick="closeDetail();" style="float: right;margin-right: 2px;"><a>X</a></span></div>
		<table>
		<tr>
			<td align="right">名称中文：</td><td id="det_cn" style="min-width: 50px;"></td>
		</tr>
		<tr>
			<td align="right">名称英文：</td><td id="det_en"></td>
		</tr>
		<tr>
			<td align="right">来源名称中文：</td><td id="det_o_cn"></td>
		</tr>
		<tr>
			<td align="right">来源名称英文：</td><td id="det_o_en"></td>
		</tr>
		<tr>
			<td align="right">术语类型：</td><td id="det_type"></td>
		</tr>
		<tr>
			<td align="right">术语定义：</td><td id="det_defin"></td>
		</tr>
		<tr>
			<td align="right">科室分类：</td><td id="det_dep"></td>
		</tr>
		<tr>
			<td align="right">部位分类：</td><td id="det_part"></td>
		</tr>
		<tr>
			<td align="right">人群分类：</td><td id="det_man"></td>
		</tr>
		<tr>
			<td align="right">病种类型：</td><td id="det_dis"></td>
		</tr>
		<tr>
			<td align="right">是否慢性病：</td><td id="det_chronic"></td>
		</tr>	
		</table>
	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2008"></script>
<script type="text/javascript">
$(top.hangge());
$(function() {
	initTree();
});
var zTree;
var zTreeUrl = basePath+"ontoTree/tree.do?ontoType=${ontoType}";
function initTree(){
	var setting = {
		view: {
			showIcon:false,
			showLine: true,
			dblClickExpand: false,
			selectedMulti: false,
			addDiyDom: addDiyDom
		},
	    check:{
	    	enable: false
		},
		callback:{
			onExpand : onExpand,
			onDblClick : onDblClick
		},
		async: {
			enable: true,
			url:zTreeUrl,
			autoParam:["id", "name=n", "CATEGORY"],
			otherParam:{"type":"1"}
		}
	};
	zTree = $.fn.zTree.init($("#leftTree"), setting);
	setTimeout(initTreeDis,1000);
};
//初始化改变停用节点的样式
function initTreeDis(){
	var children = zTree.getNodes();
	if(children!=null){
		var len = children.length;
		for(var i=0;i<len;i++){
			var c = children[i];
			if(c.IS_DISABLE==1){
				$("#"+c.tId+"_span").css("text-decoration","line-through").css("color","red").attr("title","已经停用");
			}
		}
	}else{
		setTimeout(initTreeDis,1000);
	}
}
//增加图标
function addDiyDom(treeId, treeNode) {
	if($("#ontoType").val()=='51005' && $("#businessType").val()!=2){
		var aObj = $("#" + treeNode.tId + "_a");
		var editStr = "<span class='demoIcon' id='diyBtn_" +treeNode.id+ "' title='"+treeNode.name+"' onclick='queryDetail(this,"+treeNode.id+");'><span class='detail'></span></span>";
		aObj.after(editStr);
	}
}
//查询单个本体,允许页面缓存
function queryDetail(obj,id){
	var ontoType = $("#ontoType").val();
	$.ajax({
		type: "POST",
		url: basePath+'ontology/ontoDetail.do',
    	data: {ID:id,ontoType:ontoType},
		dataType:'json',
		asyn:true,
		cache: true,
		success: function(data){
			if(data.result=="success"){
			 	var offset = $(obj).offset();  
				$("#det_cn").text(data.STAD_DN_CHN);
				$("#det_en").text(data.STAD_DN_ENG);
				$("#det_o_cn").text(data.ORG_STAD_DN_CHN);
				$("#det_o_en").text(data.ORG_STAD_DN_ENG);
				if(data.TERM_TYPE==0){
					$("#det_type").text('症状');
				}else if(data.TERM_TYPE==1){
					$("#det_type").text('疾病');
				}
				$("#det_defin").text(data.TERM_DEFIN);
				$("#det_dep").text(data.DEP_CATEGORY_NAME);
				$("#det_part").text(data.PART_CATEGORY);
				$("#det_man").text(data.MAN_CATEGORY);
				$("#det_dis").text(data.DIS_CATEGORY);
				$("#det_chronic").text(data.IS_CHRONIC==0?'否':'是');
				//去除所有已经被选中的节点样式
				$(".curSelectedNode").removeClass("curSelectedNode");
				//选择节点
				var c_note = zTree.getNodeByParam ("id", id, null);
				zTree.selectNode (c_note, false, false);
				//设置显示位置
				var windowTop = $(top.window).height();
				var detailTop = $("#detailInfo").height();
			 	var offset = $(obj).offset();  
			 	if((offset.top + detailTop+80)>windowTop){
			 		$("#detailInfo").show().css("top",windowTop-detailTop-80).css("left",offset.left+30);
			 	}else{
			 		$("#detailInfo").show().css("top",offset.top).css("left",offset.left+30);
			 	}
			}else{
				//查询失败
			}
		}
	});
}
//关闭详细div弹出框
function closeDetail(){
	$("#detailInfo").hide();
}
//双击事件,增加一个父节点
function onDblClick(event, treeId, treeNode, clickFlag){
	if(treeNode==null){return;}
	if(treeNode.IS_DISABLE==1){
		bootbox.dialog({
			message: "<span class='bigger-110'>该节点已经停用不能选择！</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return ;
	}
	if($("#ontoType").val()=='51005'){
		//诊断单独处理
		onDblClick2(treeId,treeNode);
		return;
	}
	if($("#ontoType").val()=='51003'){
		onDblClick3(treeId,treeNode);
		return;
	}
	//诊断编辑页面选择科室
	if($("#businessType").val()==2){
		//科室分类只能选择一个节点
	}
	$("#selectedCont").empty().append($("<span class='tag_grp' id='father_"+treeNode.id+"''><b>"+treeNode.name
		+"</b>&nbsp;<span class='span_remove' onclick='cancelCategory("+treeNode.id+");'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"
		+"<input type='hidden' name='PARENT_IDS' value='"+treeNode.id +"' /><input type='hidden' name='PARENT_NAMES' value='"+treeNode.name+"'/>"
		+"<input type='hidden' name='MAIN_CODES' value='"+treeNode.name +"' />"
		+"</span>"
	));
}
//手术双击事件,增加一个父节点
function onDblClick3( treeId, treeNode){
	if(treeNode==null){return;}
	if(treeNode.MAIN_CODE !=null && treeNode.MAIN_CODE !=''){
		if(treeNode.MAIN_CODE.indexOf("-")!=-1){
			bootbox.dialog({
				message: "<span class='bigger-110'>主要编码为编码范围不能进行扩码！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
		if(treeNode.MAIN_CODE.length>=8){
			bootbox.dialog({
				message: "<span class='bigger-110'>主要编码为7位不能进行扩码！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
		if(treeNode.MAIN_CODE.length<=2){
			bootbox.dialog({
				message: "<span class='bigger-110'>主要编码为2位不能进行扩码！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
	
	}
	//3、4位编码，则验证是否可扩展
	if(treeNode.MAIN_CODE !=null && treeNode.MAIN_CODE.length!=7 ){
		var flag = false;
		$.ajax({
			type: "POST",
			url: basePath+'ontoTree/enableExtend.do',
	    	data: {id:treeNode.id,ontoType:$("#ontoType").val()},
			dataType:'json',
			async:false,
			cache: false,
			success: function(data){
				if(data.result=="success"){
					flag = true;
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}
			}
		});
		if(!flag){
			return;
		}
	}
	$("#selectedCont").empty().append($("<span class='tag_grp' id='father_"+treeNode.id+"''><b>"+treeNode.name
		+"</b>&nbsp;<span class='span_remove' onclick='cancelCategory("+treeNode.id+");'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"
		+"<input type='hidden' name='PARENT_IDS' value='"+treeNode.id +"' /><input type='hidden' name='PARENT_NAMES' value='"+treeNode.name+"'/>"
		+"<input type='hidden' name='MAIN_CODES' value='"+treeNode.name +"' />"
		+"</span>"
	));
}
//诊断双击事件,增加一个父节点
function onDblClick2( treeId, treeNode){
	if(treeNode==null){return;}
	if(treeNode.IS_DISABLE==1){
		bootbox.dialog({
			message: "<span class='bigger-110'>已经停用的本体不能进行扩码！</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return;
	}
	var exit = $("#selectedCont").find("#father_"+treeNode.id);
	//判断是否重复添加
	if(exit.length!=0){
		$("#selectedCont").tips({side:3,msg:'重复添加父节点!',bg:'#AE81FF',time:3});
		return;
	}
	//数量限制
	var select_len = $("#selectedCont").find("input[name='PARENT_IDS']").length;
	if( select_len >= 2){
		$("#selectedCont").tips({side:3,msg:'最多只能选择2个父节点!',bg:'#AE81FF',time:3});
		return;
	}
	//逻辑判断
	//形态学编码
	var MAIN_CODE = null;
	var ADD_CODE = null;
	if(treeNode.MAIN_CODE!=null){
		MAIN_CODE = treeNode.MAIN_CODE.replace("+","");
		MAIN_CODE = MAIN_CODE.replace("*","");
	}
	if(treeNode.ADD_CODE!=null){
		ADD_CODE = treeNode.ADD_CODE.replace("+","");
		ADD_CODE = ADD_CODE.replace("*","");
	}
	var maincode  =  $("#selectedCont").find("input[name='MAIN_CODES']");
	var addcode  =  $("#selectedCont").find("input[name='ADD_CODES']");
	//主要编码为编码范围不能进行扩码
	if(MAIN_CODE!=null){
		if(MAIN_CODE.indexOf("-")!=-1){
			bootbox.dialog({
				message: "<span class='bigger-110'>主要编码为编码范围不能进行扩码！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
		if(MAIN_CODE.length>=9){
			bootbox.dialog({
				message: "<span class='bigger-110'>当前为扩充词, 不能增加下位词！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
	}
	if(ADD_CODE !=null && ADD_CODE !=''){
		if(ADD_CODE.indexOf("-")!=-1){
			bootbox.dialog({
				message: "<span class='bigger-110'>附加编码为编码范围不能进行扩码！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
		if(ADD_CODE.length>=9){
			bootbox.dialog({
				message: "<span class='bigger-110'>当前为扩充词, 不能增加下位词！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
	}
	//非6位编码，则验证是否可扩展
	if((MAIN_CODE !=null && MAIN_CODE.length!=7) ||(ADD_CODE !=null && ADD_CODE.length!=7 && ADD_CODE.indexOf("/")==-1) ){
		var flag = false;
		$.ajax({
			type: "POST",
			url: basePath+'ontoTree/enableExtend.do',
	    	data: {id:treeNode.id,ontoType:$("#ontoType").val()},
			dataType:'json',
			async:false,
			cache: false,
			success: function(data){
				if(data.result=="success"){
					flag = true;
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}
			}
		});
		if(!flag){
			return;
		}
	}
	
	//如果已经选择的父节点为空则不判断
	if(select_len==1){
		var maincode_len = maincode.length;
		var addcode_len = addcode.length;
		//规则1如果已经存在一个主要编码，则只能另外选择一个附加编码的诊断
		//规则2如果已经存在一个附加编码，则只能另外选择一个主要编码的诊断
		if(maincode_len==1){
			//判断当前双击选择的是否是主要编码，是则提示错误
			if(MAIN_CODE !=null && MAIN_CODE !=''){
				$("#selectedCont").tips({side:3,msg:'父节点最多只能选择1个主要编码',bg:'#AE81FF',time:3});
				return;
			}
			//当前已经选择了一个主要编码，新选择的附加编码为空，提示错误
			if(ADD_CODE ==null || ADD_CODE ==''){
				$("#selectedCont").tips({side:3,msg:'请选择1个仅含有附件编码的父节点',bg:'#AE81FF',time:3});
				return;
			}
		}
		if(addcode_len==1){
			//判断当前双击选择的是否是附加编码，是则提示错误
			if(ADD_CODE !=null && ADD_CODE !=''){
				$("#selectedCont").tips({side:3,msg:'父节点最多只能选择1个附件编码',bg:'#AE81FF',time:3});
				return;
			}
			//当前已经选择了一个附加编码，新选择的主要编码为空，提示错误
			if(MAIN_CODE ==null || MAIN_CODE ==''){
				$("#selectedCont").tips({side:3,msg:'请选择1个仅含有主要编码的父节点',bg:'#AE81FF',time:3});
				return;
			}
			//特殊规则99：如果附加编码含有星号，则不能在该章里在选择主要编码，切不能选择B95-B97、V-Y 中的节点
			if(addcode.eq(0).val().indexOf("*")!=-1){
				//找到已经选择的附加编码的节点
				var add_note = zTree.getNodeByParam ("ADD_CODE", addcode.eq(0).val(), null);
				//找到add_note的章节点
				while(add_note.getParentNode()!=null){
					add_note = add_note.getParentNode();
				}
				//此时add_note即为章节点
				var rootMainCode = add_note.MAIN_CODE.split("-");
				var rootMainCodeStart = rootMainCode[0];
				var rootMainCodeEnd = rootMainCode[1];
				var z = MAIN_CODE.substring(0,1);
				var c = MAIN_CODE.substring(1,3);
				//判断当前选中的节点主要编码是否在 已经选择过的章内
				if(rootMainCodeStart.indexOf(z)!=-1 && rootMainCodeEnd.indexOf(z)!=-1){
					if(Number(rootMainCodeStart.substring(1,3)) <= Number(c) && Number(rootMainCodeEnd.substring(1,3)) >= Number(c) ){
						$("#selectedCont").tips({side:3,msg:'含有*号的附加编码，不能与主要编码在同一章下。',bg:'#AE81FF',time:3});
						return;
					}
				}else if(rootMainCodeStart.indexOf(z)!=-1){
					if(Number(rootMainCodeStart.substring(1,3)) <= Number(c) ){
						$("#selectedCont").tips({side:3,msg:'含有*号的附加编码，不能与主要编码在同一章下。',bg:'#AE81FF',time:3});
						return;
					}
				}else if(rootMainCodeEnd.indexOf(z)!=-1){
					if(Number(rootMainCodeEnd.substring(1,3)) >= Number(c) ){
						$("#selectedCont").tips({side:3,msg:'含有*号的附加编码，不能与主要编码在同一章下。',bg:'#AE81FF',time:3});
						return;
					}
				}
				if(MAIN_CODE.indexOf("B95")!=-1 || MAIN_CODE.indexOf("B96")!=-1 || MAIN_CODE.indexOf("B97")!=-1 || MAIN_CODE.indexOf("V")!=-1 || MAIN_CODE.indexOf("Y")!=-1 ){
					$("#selectedCont").tips({side:3,msg:'附加编码带有*的节点，主要编码不能为B95-B97、不能含有V、Y。',bg:'#AE81FF',time:3});
					return;
				}
			}
		}
	}
	setFather(treeNode);
	//判断双击选择的父节点如果为8位编码则提示选择6位的上级编码
// 	if(MAIN_CODE!=null && MAIN_CODE!=''){
// 		if(MAIN_CODE.length == 9){
// 			bootbox.confirm("当前为扩充词, 不能增加下位词, 点击确认则选择当前扩充词的父节点?", function(result) {
// 				if(result) {
// 					var parent_node = treeNode.getParentNode();
// 					treeNode = parent_node;
// 					setFather(treeNode);
// 				}
// 			});
// 		}else{
// 			setFather(treeNode);	
// 		}
// 	}else if(ADD_CODE!=null && ADD_CODE!=''){
// 		if(ADD_CODE.length == 9){
// 			bootbox.confirm("当前为扩充词, 不能增加下位词, 点击确认则选择当前扩充词的父节点?", function(result) {
// 				if(result) {
// 					var parent_node = treeNode.getParentNode();
// 					treeNode = parent_node;
// 					setFather(treeNode);
// 				}
// 			});
// 		}else{
// 			setFather(treeNode);	
// 		}
// 	}
}
function setFather(treeNode){
	$("#selectedCont").append($("<span class='tag_grp' id='father_"+treeNode.id+"''><b>"+treeNode.name
		+"</b>&nbsp;<span class='span_remove' onclick='cancelCategory("+treeNode.id+");'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"+
		"<input type='hidden' name='PARENT_IDS' value='"+treeNode.id +"' /></span>"
	));
	if(treeNode.MAIN_CODE !=null && treeNode.MAIN_CODE !=''){
		$("#father_"+treeNode.id).append("<input type='hidden' name='MAIN_CODES' value='"+treeNode.MAIN_CODE+"'/>");
	}
	if(treeNode.ADD_CODE !=null && treeNode.ADD_CODE !=''){
		$("#father_"+treeNode.id).append("<input type='hidden' name='ADD_CODES' value='"+treeNode.ADD_CODE+"'/>");
	}
}
//设置可以选择的节点背景颜色
function onExpand(event, treeId, treeNode) {
	//谷歌360自动给这个ul加上了隐藏属性，导致不显示子节点。火狐没问题
	$("#"+treeNode.tId+"_ul").css("overflow","visible");
	var children = treeNode.children;
	var len = children.length;
	for(var i=0;i<len;i++){
		var c = children[i];
		if(c.IS_DISABLE==1){
			$("#"+c.tId+"_span").css("text-decoration","line-through").css("color","red").attr("title","已经停用");
		}
		if($("#ontoType").val()=='51005'){
			var MAIN_CODE = null;
			var ADD_CODE = null;
			if(c.MAIN_CODE!=null){
				MAIN_CODE = c.MAIN_CODE.replace("+","");
				MAIN_CODE = MAIN_CODE.replace("*","");
			}
			if(c.ADD_CODE!=null){
				ADD_CODE = c.ADD_CODE.replace("+","");
				ADD_CODE = ADD_CODE.replace("*","");
			}
			if(MAIN_CODE!=null&&MAIN_CODE!=''&&ADD_CODE!=null&&ADD_CODE!=''){
				if(MAIN_CODE.indexOf("-")==-1&&(MAIN_CODE.length==7||MAIN_CODE.length==8)){
					if(ADD_CODE.indexOf("-")==-1&&(ADD_CODE.length==7||ADD_CODE.length==8)){
						$("#"+c.tId+"_span").css("color","#428bca");
					}
				}
			}else if(MAIN_CODE!=null&&MAIN_CODE!=''){
				if(MAIN_CODE.indexOf("-")==-1&&(MAIN_CODE.length==7||MAIN_CODE.length==8)){
					$("#"+c.tId+"_span").css("color","#428bca");
				}
			}else if(ADD_CODE!=null&&ADD_CODE!=''){
				if(ADD_CODE.indexOf("-")==-1&&(ADD_CODE.length==7||ADD_CODE.length==8)){
					$("#"+c.tId+"_span").css("color","#428bca");
				}
			}
// 			if(treeNode.getParentNode()==null){
// 				if(treeNode.MAIN_CODE!=null&&treeNode.MAIN_CODE!=''){
// 					treeNode.main_show=1;
// 				}
// 				if(treeNode.ADD_CODE!=null&&treeNode.ADD_CODE!=''){
// 					treeNode.add_show=1;
// 				}
// 			}
			//在修改父节点页面，要显示所有编码让用户方便选择
// 			if(treeNode.main_show==1){
//				//可能存在主要编码与附加编码颠倒情况
// 				if(c.MAIN_CODE!=null&&c.MAIN_CODE!=''){
// 					c.main_show=1;
// 					c.name=c.CN +"["+c.MAIN_CODE+"]";
// 				}else{
// 					c.add_show=1;
// 					c.name=c.CN +"["+c.ADD_CODE+"]";
// 				}
// 				zTree.updateNode(c);
// 			}else if(treeNode.add_show==1){
//				//可能存在主要编码与附加编码颠倒情况
// 				if(c.ADD_CODE!=null&&c.ADD_CODE!=''){
// 					c.add_show=1;
// 					c.name=c.CN +"["+c.ADD_CODE+"]";
// 				}else{
// 					c.main_show=1;
// 					c.name=c.CN +"["+c.MAIN_CODE+"]";
// 				}
// 				zTree.updateNode(c);
// 			}
		}else if($("#ontoType").val()=='51003'){
			if(c.MAIN_CODE!=null&&c.MAIN_CODE!=''&&c.MAIN_CODE.indexOf("-")==-1&&(c.MAIN_CODE.length==5)){
				$("#"+c.tId+"_span").css("color","#428bca");
			}
		}else if($("#ontoType").val()=='51006'){
			//KS001.001.001.001 第四层允许选中
// 			if(c.MAIN_CODE!=null&&c.MAIN_CODE!=''&&(c.MAIN_CODE.length==17)){
// 				$("#"+c.tId+"_span").css("color","#428bca");
// 			}
		}
	}
}
//删除一个父节点
function cancelCategory(id){
	$("#father_"+id).remove();
}
//确定业务类型（快捷键或者本体审核页面）
function selectCont(){
	var val = $("#businessType").val();
	if(val==0){
		//本体维护页面-修改父节点
		selectCont0();
	}else if(val==1){
		//本体审核页面，修改父节点
		selectCont1();
	}else if(val==2){
		//诊断维护页面，科室分类选择
		selectCont2();
	}else if(val==3){
		//诊断审核页面，科室分类选择
		selectCont3();
	}
}
//快捷键修改父节点
function selectCont0(){
	if($("#ontoType").val()=='51006'){
		selectCont0Dep();
		return;
	}
	var obj = $("#selectedCont").find("input[name='PARENT_IDS']");
	var addcode = $("#selectedCont").find("input[name='ADD_CODES']");
	if(obj.length == 0){
		$("#selectedCont").tips({side:3,msg:'请选择一个父节点',bg:'#AE81FF',time:3});
		return;
	}else if(addcode.length==1){
		var add = addcode.eq(0).val();
		//如果父节点附加编码带有*，则必须选择一个主要编码
		if(add.indexOf("*")!=-1){
			//判断是否选择了主要编码
			var maincode = $("#selectedCont").find("input[name='MAIN_CODES']");
			if(maincode.length==0){
				$("#selectedCont").tips({side:3,msg:'请再选择一个主要编码父节点',bg:'#AE81FF',time:3});
				return;
			}
		}
	}
	//后台修改父节点
	requestSaveParent();
}
function requestSaveParent(){
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	$.ajax({
		type: "POST",
		url: basePath+'common/editParent.do',
    	data: $("#parentForm").serialize(),
		dataType:'json',
		success: function(data){
			if(data.result=="success"){
				document.getElementById("zhongxin").style.display = 'none';
				top.Dialog.close();
			}else{
			 	bootbox.dialog({
					message: "<span class='bigger-110'>"+data.result+"</span>",
					buttons:
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				//查询失败
				$("#zhongxin").show();
				$("#zhongxin2").hide();
			}
		}
	});
}
//快捷键修改父节点-科室单独处理
function selectCont0Dep(){
	var obj = $("#selectedCont").find("input[name='PARENT_IDS']");
	if(obj.length == 0){
		bootbox.confirm("不选择父节点，当前本体将会作为顶级节点，请确认？", function(result) {
			if(result) {
				requestSaveParent();
			}
		});
		return;
	}
	//验证是否是自己
	if(obj.eq(0).val()==$("#ONTO_ID").val()){
		$("#selectedCont").tips({side:3,msg:'父节点不能选择自己',bg:'#AE81FF',time:3});
		return;
	}
	requestSaveParent();
}
//本体审核页面修改父节点
function selectCont1(){
	//设置父节点值
	if($("input[name='PARENT_IDS']").length==0){
		$("#selectedCont").tips({
			side:3,
	           msg:'请选择一个父节点',
	           bg:'#AE81FF',
	           time:3
	       });
		$("#selectedCont").focus();
		return false;
	}
	var text = "";
	$(".tag_grp b").each(function(){
		if(text == ""){
			text = $.trim( $(this).text()) ;
		}else{
			text += ";" + $.trim( $(this).text()) ;
		}
	});
	//父节点保存到审核页面
	var p_window = parent.$("#_DialogFrame_0")[0].contentWindow;
	p_window.$("#newParent").empty().append($("input[name='PARENT_IDS']").clone()).append(text);
	top.Dialog.close();
}
//审核页面-选择父节点
function selectCont2(){
	//设置父节点值
	if($("input[name='PARENT_IDS']").length!=1){
		$("#selectedCont").tips({
			side:3,
	           msg:'请选择一个父节点',
	           bg:'#AE81FF',
	           time:3
	       });
		$("#selectedCont").focus();
		return false;
	}
	var text =  $(".tag_grp b").eq(0).text() ;
	//父节点保存到审核页面
	var p_window = parent.$("#_DialogFrame_0")[0].contentWindow;
	p_window.$("#DEP_CATEGORY_NAME").val(text).text(text);
	p_window.$("#DEP_CATEGORY").val($("input[name='PARENT_IDS']").eq(0).val());
	top.Dialog.close();
}
//诊断修改-科室选择
function selectCont3(){
	//设置父节点值
	if($("input[name='PARENT_IDS']").length!=1){
		$("#selectedCont").tips({
			side:3,
	           msg:'请选择一个父节点',
	           bg:'#AE81FF',
	           time:3
	       });
		$("#selectedCont").focus();
		return false;
	}
	var text =  $(".tag_grp b").eq(0).text() ;
	//父节点保存到审核页面
	var p_window = parent.$("#_DialogFrame_0")[0].contentWindow;
	p_window.$("#DEP_CATEGORY_NAME").val(text).text(text);
	p_window.$("#DEP_CATEGORY").val($("input[name='PARENT_IDS']").eq(0).val());
	top.Dialog.close();
}
</script>
</html>