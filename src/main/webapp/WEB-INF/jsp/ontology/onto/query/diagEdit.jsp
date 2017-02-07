<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css?v=11122"/>
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.exedit.min.js"></script>
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<link type="text/css" rel="stylesheet" href="static/css/ontology.css?v=2016"/>
</head>
<body class="no-skin" style="background-color: white;">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<table style="width:100%;" border="0">
								<tr>
									<td style="width:15%;" valign="top">
										<div style='overflow: scroll;max-width: 500px;max-height: 570px;'>
											<div id="treeName">本体数</div>
											<div id="treeId">
												<ul id="leftTree" class="ztree"></ul>
											</div>
										</div>
									</td>
									<td style="" valign="top" >
										<form action="ontology/${MSG }.do" name="userForm" id="userForm" method="post">
											<div id="zhongxin" style="padding-top: 10px;">
											<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
											<input type="hidden" name="D_ID" id="D_ID" value="${pd.D_ID }" />
											<input type="hidden" name="DN_ID" id="DN_ID" value="${pd.DN_ID }" />
											<input type="hidden" name="VERSION" id="VERSION" value="${pd.VERSION }" />
											<div style="height:auto;min-height:400px;max-height: 533px;overflow:auto;">
											<div>
											<table id="table_report" class="table table-bordered table-hover">
												<tr>
													<td width="15%;" style="text-align: right;padding-top: 10px;">本体父节点名称:</td>
													<td colspan="3">
														<div class="shadow" id="selectedCont" style="height: 35px;">
															<c:choose>
															<c:when test="${not empty pd.PARENT_LIST}">
																<c:forEach items="${pd.PARENT_LIST}" var="parent" varStatus="vs">
																	<span class='tag_grp' id="father_${parent.ID}"><b>${parent.NAME}
																		</b>&nbsp;<span class="span_remove" onclick="cancelCategory('${parent.ID}')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
																		<input type='hidden' name='PARENT_IDS' value="${parent.ID}" />
																		<input type='hidden' name='PARENT_NAMES' value="${parent.NAME}" />
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
													<td width="15%;" style="text-align: right;padding-top: 10px;">主要编码:</td>
													<td width="35%;">
														<input type="text" name="MAIN_CODE" id="MAIN_CODE" value="${pd.MAIN_CODE }" placeholder="系统自动生成" readonly="readonly" style="width:98%;"/>
													</td>
													<td width="15%;" style="text-align: right;padding-top: 10px;">附加编码:</td>
													<td width="35%;"><input type="text" name="ADD_CODE" id="ADD_CODE" value="${pd.ADD_CODE }" placeholder="系统自动生成" style="width:98%;" readonly="readonly" /></td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">规范名称-中文:</td>
													<td><input type="text" name="STAD_DN_CHN" id="STAD_DN_CHN" value="${pd.STAD_DN_CHN }" placeholder="规范名称-中文"  style="width:98%;" onblur="checkName(this,'DN_CHN');" maxlength="80"/></td>
													<td style="text-align: right;padding-top: 10px;">规范名称-英文:</td>
													<td><input type="text" name="STAD_DN_ENG" id="STAD_DN_ENG" value="${pd.STAD_DN_ENG }" placeholder="规范名称-英文"  style="width:98%;" maxlength="80"/></td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">来源-规范名称-中文:</td>
													<td><input type="text" name="ORG_STAD_DN_CHN" id="ORG_STAD_DN_CHN" value="${pd.ORG_STAD_DN_CHN }" placeholder="来源-规范名称-中文"  style="width:98%;" maxlength="80"/></td>
													<td style="text-align: right;padding-top: 10px;">来源-规范名称-英文:</td>
													<td><input type="text" name="ORG_STAD_DN_ENG" id="ORG_STAD_DN_ENG" value="${pd.ORG_STAD_DN_ENG }" placeholder="来源-规范名称-英文"  style="width:98%;" maxlength="80"/></td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">术语定义:</td>
													<td><input type="email" name="TERM_DEFIN" id="TERM_DEFIN"  value="${pd.TERM_DEFIN }" placeholder="术语定义" style="width:98%;" maxlength="500"/></td>
													<td style="text-align: right;padding-top: 10px;">术语类型:</td>
													<td>
														<select class="chosen-select form-control" style="vertical-align:top;" name="TERM_TYPE" id="TERM_TYPE" title="状态">
															<option value="">请选择</option>
															<option value="1" <c:if test="${pd.TERM_TYPE==1}">selected</c:if>>症状</option>
															<option value="2" <c:if test="${pd.TERM_TYPE==2}">selected</c:if>>疾病</option>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">科室分类:</td>
													<td>
														<input type="hidden" id="DEP_CATEGORY" name="DEP_CATEGORY" value="${pd.DEP_CATEGORY }"/>
														<input type="text" id="DEP_CATEGORY_NAME" name="DEP_CATEGORY_NAME" value="${pd.DEP_CATEGORY_NAME}" placeholder="科室分类" readonly="readonly" style="width:80%;"/>
														<a onclick="depCategory();">&nbsp;选择</a>
													</td>
													<td style="text-align: right;padding-top: 10px;">部位分类:</td>
													<td>
														<select class="chosen-select form-control" style="vertical-align:top;" name="PART_CATEGORY" id="PART_CATEGORY">
															<option value="">请选择</option>
															<c:forEach items="${partMap.entrySet()}" var="partTyp" varStatus="vs">
																<option value="${partTyp.key}" <c:if test="${partTyp.key == pd.PART_CATEGORY }">selected</c:if>>${partTyp.value}</option>
															</c:forEach>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">人群分类:</td>
													<td>
														<select class="chosen-select form-control" style="vertical-align:top;"  name="MAN_CATEGORY" id="MAN_CATEGORY">
															<option value="">请选择</option>
															<c:forEach items="${crowsMap.entrySet()}" var="crowsTyp" varStatus="vs">
																<option value="${crowsTyp.key}" <c:if test="${crowsTyp.key == pd.MAN_CATEGORY }">selected</c:if>>${crowsTyp.value}</option>
															</c:forEach>
														</select>
													</td>
													<td style="text-align: right;padding-top: 10px;">病种类型:</td>
													<td>
														<select class="chosen-select form-control" style="vertical-align:top;" name="DIS_CATEGORY" id="DIS_CATEGORY">
															<option value="">请选择</option>
															<c:forEach items="${disMap.entrySet()}" var="crowsTyp" varStatus="vs">
																<option value="${crowsTyp.key}" <c:if test="${crowsTyp.key == pd.MAN_CATEGORY }">selected</c:if>>${crowsTyp.value}</option>
															</c:forEach>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">停用标记:</td>
													<td>
														<label><input class="ace" id="u3897_input" value="0" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==0 || pd.IS_DISABLE == null}">checked</c:if> >否&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
														<label><input class="ace" id="u3897_input" value="1" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==1 }">checked</c:if> >是&nbsp;<span class="lbl"></span></label>
													</td>
													<td style="text-align: right;padding-top: 10px;">是否慢性病:</td>
													<td>
														<label><input class="ace" id="u3897_input" value="0" name="IS_CHRONIC" type="radio" <c:if test="${pd.IS_CHRONIC==0 || pd.IS_CHRONIC == null}">checked</c:if> >否&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
														<label><input class="ace" id="u3897_input" value="1" name="IS_CHRONIC" type="radio" <c:if test="${pd.IS_CHRONIC==1 }">checked</c:if> >是&nbsp;<span class="lbl"></span></label>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">停用描述:</td>
													<td colspan="3"><input type="text" name="DESCRIPTION" id="DESCRIPTION" value="${pd.DESCRIPTION }" placeholder="停用描述" maxlength="80" style="width:99%;"/></td>
												</tr>		
											</table>
											</div>
											<div id="osynDiv">
<!-- 												<table  class='table table-bordered table-hover'> -->
<!-- 													<tr> -->
<!-- 														<input type='hidden' name='DN_CHN' id='DN_CHN' value='' /> -->
<!-- 														<input type='hidden' name='DN_ENG' id='DN_ENG'  value='' /> -->
<!-- 														<input type='hidden' name='TERM_TYPE' id='TERM_TYPE'  value='' /> -->
<!-- 														<input type='hidden' name='SYNO_TYPE' id='SYNO_TYPE'  value='' /> -->
<!-- 														<input type='hidden' name='ORG_DN_CHN' id='ORG_DN_CHN'  value='' /> -->
<!-- 														<input type='hidden' name='ORG_DN_ENG' id='ORG_DN_ENG'  value='' /> -->
<!-- 														<input type='hidden' name='IS_DISABLE' id='IS_DISABLE'  value='' /> -->
<!-- 														<input type='hidden' name='DESCRIPTION' id='DESCRIPTION'  value='' /> -->
<!-- 														<td>术语名称中文:</td><td></td><td>术语来源名称中文:</td><td></td><td>同义词类型:</td><td></td><td>停用标记:</td><td></td> -->
<!-- 														<td rowspan='2' width='80px;' style='vertical-align: middle;' align='center'> -->
<!-- 														<div class='hidden-sm hidden-xs btn-group'> -->
<!-- 															<a class='btn btn-xs btn-success' title='编辑' onclick='editUser();'><i class='ace-icon fa fa-pencil-square-o bigger-120'></i></a> -->
<!-- 															<a class='btn btn-xs btn-danger' onclick='delUser('813fddd1a0334ab5b90ff944c70b1e97','qwq');'><i class='ace-icon fa fa-trash-o bigger-120' title='删除'></i></a> -->
<!-- 														</div> -->
<!-- 														</td> -->
<!-- 													</tr> -->
<!-- 													<tr><td>术语名称英文:</td><td></td><td>术语来源名称英文:</td><td></td><td>术语类型:</td><td></td><td>停用描述:</td><td></td></tr> -->
<!-- 												</table> -->
											</div>
											</div>
											<div class="position-relative" id="osynPageParam" style="padding-bottom:4px;">
												<table style="width:100%;">
													<tr>
														<td style="text-align: center;" colspan="10">
															<c:if test="${EDIT_FLAG==0}">
																<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
																<a class="btn btn-mini btn-warning" onclick="addOsyn();">增加同义词</a>
																<c:if test="${MSG=='edit'}">
																	<a class="btn btn-mini btn-info" onclick="hisDetail();">本体历史</a>
																</c:if>
															</c:if>
															<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
														</td>
													</tr>
												</table>
											</div>
											</div>
											<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
										</form>
									</td>
								</tr>
							</table>
								
								
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
	
	<div id="detailInfo" class="detailInfo drag" style="display: none;position: absolute;">
		<div>详情: <span class="span_remove" onclick="closeDetail(event);" style="float: right;"/></div>
		<table>
		<tr>
			<td align="right">名称中文：</td><td id="det_cn" style="min-width: 60px;max-width: 180px;"></td>
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
			<td align="right">术语定义：</td><td id="det_defin" style="max-width: 135px;"></td>
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
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2008"></script>
<script type="text/javascript" src="static/js/ontology/tree.js?v=2018008"></script>
<script type="text/javascript" src="static/js/ontology/edit.js?v=2018009"></script>
<script type="text/javascript">

//定时关闭提示框
// 				mytimer = 20;
// 				timerClose(false);
var mytimer = 10;
var runing = false;
function timerClose(flag){
	if(!flag && runing){
		return;
	}
	runing = true;
	if(mytimer<=0){
		$("#detailInfo").hide("fast");
		runing = false;
	}else{
		mytimer = mytimer-1;
		setTimeout(function(){timerClose(true);},1000);
	}

}

//双击事件,增加一个父节点
function onDblClick(event, treeId, treeNode, clickFlag){
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
		"<input type='hidden' name='PARENT_IDS' value='"+treeNode.id +"' /><input type='hidden' name='PARENT_NAMES' value='"+treeNode.name+"'/></span>"
	));
	if(treeNode.MAIN_CODE !=null && treeNode.MAIN_CODE !=''){
		$("#father_"+treeNode.id).append("<input type='hidden' name='MAIN_CODES' value='"+treeNode.MAIN_CODE+"'/>");
	}
	if(treeNode.ADD_CODE !=null && treeNode.ADD_CODE !=''){
		$("#father_"+treeNode.id).append("<input type='hidden' name='ADD_CODES' value='"+treeNode.ADD_CODE+"'/>");
	}
}

//修改同义词窗口
function modifyOsyn(obj){
	var table = $(obj).closest("table");
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="修改同义词";
	diag.URL = path+'/common/toSubOsynAdd.do?ontoType='+$("#ontoType").val();
	diag.Width = 450;
 	diag.Height = 440;
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	};
	diag.OnLoad = function(){ 
		//设置值
		var inner_document = diag.innerFrame.contentWindow.document;
		var tableId = table.attr("id");
		inner_document.getElementById("tableId").value = tableId;
		inner_document.getElementById("DN_CHN").value = table.find("input[name='DN_CHN']").eq(0).val();
		inner_document.getElementById("DN_ENG").value = table.find("input[name='DN_ENG']").eq(0).val();
		inner_document.getElementById("ORG_DN_CHN").value = table.find("input[name='ORG_DN_CHN']").eq(0).val();
		inner_document.getElementById("ORG_DN_ENG").value = table.find("input[name='ORG_DN_ENG']").eq(0).val();
		inner_document.getElementById("DESCRIPTION").value = table.find("input[name='DESCRIPTION']").eq(0).val();
		//inner_document.getElementById("TERM_TYPE").value = table.find("input[name='TERM_TYPE']").eq(0).val();
		inner_document.getElementById("SYNO_TYPE").value = table.find("input[name='SYNO_TYPE']").eq(0).val();
		diag.innerFrame.contentWindow.$("input:radio[value="+table.find("input[name='IS_DISABLE']").eq(0).val()+"]").attr('checked','true');
	};
	diag.show();

}
//保存本体
function save(){
	var flag = true;
	if($("#STAD_DN_CHN").val()==null ||$.trim($("#STAD_DN_CHN").val())=="" ){
		$("#STAD_DN_CHN").tips({ side:3, msg:'输入规范名称中文', bg:'#AE81FF',  time:3   });
		flag = false;
	}
// 	if($("#STAD_DN_ENG").val()==null ||$.trim($("#STAD_DN_ENG").val())=="" ){
// 		$("#STAD_DN_ENG").tips({ side:3, msg:'输入规范名称英文', bg:'#AE81FF',  time:3   });
// 		flag = false;
// 	}
	if($("#ORG_STAD_DN_CHN").val()==null ||$.trim($("#ORG_STAD_DN_CHN").val())=="" ){
		$("#ORG_STAD_DN_CHN").tips({ side:3, msg:'请输入来源名称中文', bg:'#AE81FF',  time:3   });
		flag = false;
	}
// 	if($("#ORG_STAD_DN_ENG").val()==null ||$.trim($("#ORG_STAD_DN_ENG").val())=="" ){
// 		$("#ORG_STAD_DN_ENG").tips({ side:3, msg:'请输入来源名称英文', bg:'#AE81FF',  time:3   });
// 		flag = false;
// 	}
	if($("#TERM_TYPE").val()==""){
		$("#TERM_TYPE_chosen").tips({ side:3, msg:'请选择术语类型', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if($("#DEP_CATEGORY").val()==""){
		$("#DEP_CATEGORY_NAME").tips({ side:3, msg:'请选择科室分类', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if($("#PART_CATEGORY").val()==""){
		$("#PART_CATEGORY_chosen").tips({ side:3, msg:'请选择部位分类', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if($("#MAN_CATEGORY").val()==""){
		$("#MAN_CATEGORY_chosen").tips({ side:3, msg:'请选择人群分类', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if($("#DIS_CATEGORY").val()==""){
		$("#DIS_CATEGORY_chosen").tips({ side:3, msg:'请选择病种类型', bg:'#AE81FF',  time:3   });
		flag = false;
	}
	if($("input[name='IS_DISABLE']:checked").val()==1){
		if($("#DESCRIPTION").val()==""){
			$("#DESCRIPTION").tips({ side:3, msg:'请输入停用描述', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	if(!checkName($("#STAD_DN_CHN"),'DN_CHN')){
		flag = false;
	}
	if(flag){
		if(!checkOsynName()){
			flag = false;
		}
	}
	if(!flag){return;}
	var addcode = $("#selectedCont").find("input[name='ADD_CODES']");
	if($("#selectedCont").find("input[name='PARENT_IDS']").length == 0){
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
	saveOnto();
}
//设置可以选择的节点背景颜色
function onExpand(event, treeId, treeNode) {
	//谷歌360自动给这个ul加上了隐藏属性，导致不显示子节点。火狐没问题
	$("#"+treeNode.tId+"_ul").css("overflow","visible");

// 	if(treeNode.getParentNode()==null){
// 		if(treeNode.MAIN_CODE!=null&&treeNode.MAIN_CODE!=''){
// 			treeNode.main_show=1;
// 		}
// 		if(treeNode.ADD_CODE!=null&&treeNode.ADD_CODE!=''){
// 			treeNode.add_show=1;
// 		}
// 	}
	var children = treeNode.children;
	var len = children.length;
	for(var i=0;i<len;i++){
		var c = children[i];
		if(c.IS_DISABLE==1){
			$("#"+c.tId+"_span").css("text-decoration","line-through").css("color","red").attr("title","已经停用");
		}else{
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
		}
		//在修改父节点页面，要显示所有编码让用户方便选择
// 		if(treeNode.main_show==1){
//			//可能存在主要编码与附加编码颠倒情况
// 			if(c.MAIN_CODE!=null&&c.MAIN_CODE!=''){
// 				c.main_show=1;
// 				c.name=c.CN +"["+c.MAIN_CODE+"]";
// 			}else{
// 				c.add_show=1;
// 				c.name=c.CN +"["+c.ADD_CODE+"]";
// 			}
// 			zTree.updateNode(c);
// 		}else if(treeNode.add_show==1){
//			//可能存在主要编码与附加编码颠倒情况
// 			if(c.ADD_CODE!=null&&c.ADD_CODE!=''){
// 				c.add_show=1;
// 				c.name=c.CN +"["+c.ADD_CODE+"]";
// 			}else{
// 				c.main_show=1;
// 				c.name=c.CN +"["+c.MAIN_CODE+"]";
// 			}
// 			zTree.updateNode(c);
// 		}
	}
}
</script>
</html>