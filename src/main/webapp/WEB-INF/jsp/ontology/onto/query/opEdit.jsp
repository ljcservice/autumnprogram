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
<link type="text/css" rel="stylesheet" href="static/css/ontology.css"/>
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
										<div id="treeName">本体数</div>
										<div style="width:100%;max-height: 533px;overflow:auto;"" id="treeId">
											<ul id="leftTree" class="ztree"></ul>
										</div>
									</td>
									<td style="" valign="top" >
										<form action="ontology/${MSG }.do" name="userForm" id="userForm" method="post">
											<div id="zhongxin" style="padding-top: 10px;">
											<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
											<input type="hidden" name="D_ID" id="D_ID" value="${pd.OP_ID }" />
											<input type="hidden" name="DN_ID" id="DN_ID" value="${pd.ON_ID }" />
											<input type="hidden" name="VERSION" id="VERSION" value="${pd.VERSION }" />
											<div style="height:auto;min-height:200px;max-height: 533px;overflow:auto;">
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
																		<input type='hidden' name='MAIN_CODES' value="${parent.MAIN_CODE}" />
																	</span>
																</c:forEach>
															</c:when>
															</c:choose>
														</div>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">规范名称-中文:</td>
													<td><input type="text" name="STAD_DN_CHN" id="STAD_DN_CHN" value="${pd.STAD_OP_CHN }" placeholder="规范名称-中文"  style="width:98%;" onblur="checkName(this);" maxlength="80"/></td>
													<td style="text-align: right;padding-top: 10px;">规范名称-英文:</td>
													<td><input type="text" name="STAD_DN_ENG" id="STAD_DN_ENG" value="${pd.STAD_OP_ENG }" placeholder="规范名称-英文"  style="width:98%;" maxlength="80"/></td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">来源-规范名称-中文:</td>
													<td><input type="text" name="ORG_STAD_DN_CHN" id="ORG_STAD_DN_CHN" value="${pd.ORG_STAD_OP_CHN }" placeholder="来源-规范名称-中文"  style="width:98%;" maxlength="80"/></td>
													<td style="text-align: right;padding-top: 10px;">来源-规范名称-英文:</td>
													<td><input type="text" name="ORG_STAD_DN_ENG" id="ORG_STAD_DN_ENG" value="${pd.ORG_STAD_OP_ENG }" placeholder="来源-规范名称-英文"  style="width:98%;" maxlength="80"/></td>
												</tr>
												<tr>
													<td width="15%;" style="text-align: right;padding-top: 10px;">主要编码:</td>
													<td width="35%;">
														<input type="text" name="MAIN_CODE" id="MAIN_CODE" value="${pd.OP_CODE }" placeholder="系统自动生成" readonly="readonly" style="width:98%;" />
													</td>
													<td style="text-align: right;padding-top: 10px;">停用标记:</td>
													<td>
														<label><input class="ace" id="u3897_input" value="0" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==0 || pd.IS_DISABLE == null}">checked</c:if> >否&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
														<label><input class="ace" id="u3897_input" value="1" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==1 }">checked</c:if> >是&nbsp;<span class="lbl"></span></label>
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
<!-- 														<input type='hidden' name='ON_CHN' id='ON_CHN' value='' /> -->
<!-- 														<input type='hidden' name='ON_ENG' id='ON_ENG'  value='' /> -->
<!-- 														<input type='hidden' name='TERM_TYPE' id='TERM_TYPE'  value='' /> -->
<!-- 														<input type='hidden' name='SYNO_TYPE' id='SYNO_TYPE'  value='' /> -->
<!-- 														<input type='hidden' name='ORG_ON_CHN' id='ORG_ON_CHN'  value='' /> -->
<!-- 														<input type='hidden' name='ORG_ON_ENG' id='ORG_ON_ENG'  value='' /> -->
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
															</c:if>
															<c:if test="${MSG=='edit'}">
																<a class="btn btn-mini btn-info" onclick="hisDetail();">本体历史</a>
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
<script type="text/javascript" src="static/js/ontology/tree.fixer.js"></script>
<script type="text/javascript" src="static/js/ontology/edit.js?v=2018"></script>
<script type="text/javascript">

//双击事件,增加一个父节点
function onDblClick(event, treeId, treeNode, clickFlag){
// 	var exit = $("#selectedCont").find("#father_"+treeNode.id);
	//判断是否重复添加
// 	if(exit.length!=0){
// 		$("#selectedCont").tips({side:3,msg:'重复添加父节点!',bg:'#AE81FF',time:3});
// 		return;
// 	}
	if(treeNode==null){return;}
	if(treeNode.IS_DISABLE==1){
		bootbox.dialog({
			message: "<span class='bigger-110'>已经停用的本体不能进行扩码！</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return;
	}
	//主要编码为编码范围不能进行扩码
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
	//非3、4位编码，则验证是否可扩展
	if(treeNode.MAIN_CODE !=null && treeNode.MAIN_CODE.length!=5){
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
		inner_document.getElementById("SYNO_TYPE").value = table.find("input[name='SYNO_TYPE']").eq(0).val();
		inner_document.getElementById("DESCRIPTION").value = table.find("input[name='DESCRIPTION']").eq(0).val();
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
	if($("input[name='IS_DISABLE']:checked").val()==1){
		if($("#DESCRIPTION").val()==""){
			$("#DESCRIPTION").tips({ side:3, msg:'请输入停用描述', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	if(!checkName($("#STAD_DN_CHN"))){
		flag = false;
	}
	if(flag){
		if(!checkOsynName()){
			flag = false;
		}
	}
	if(!flag){return;}
	if($("#selectedCont").find("input[name='PARENT_IDS']").length != 1){
		$("#selectedCont").tips({
			side:3,
            msg:'请选择一个父节点',
            bg:'#AE81FF',
            time:3
       });
	}else{
		saveOnto();
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
		}else{
			if(c.MAIN_CODE!=null&&c.MAIN_CODE!=''&&c.MAIN_CODE.indexOf("-")==-1&&(c.MAIN_CODE.length==5)){
				$("#"+c.tId+"_span").css("color","#428bca");
			}
		}
	}
}
</script>
</html>