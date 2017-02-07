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
<style>
.detailInfo{
	-webkit-box-shadow: #666 0px 0px 6px;  
	-moz-box-shadow: #666 0px 0px 6px;  
	box-shadow: #666 0px 0px 6px;  
	background: rgb(153, 204, 255);  
	border:1px solid #d5d5d5;
	padding: 2px;
}
.shadow{  
/* 	-webkit-box-shadow: #666 0px 0px 10px;   */
/* 	-moz-box-shadow: #666 0px 0px 10px;   */
/* 	box-shadow: #666 0px 0px 10px;   */
/* 	background: #EEFF99;   */
	border:1px solid #d5d5d5
}
.tag_grp {
    border: 1px solid #d3d3d3;
    border-radius: 3px;
    color: black;
    cursor: pointer;
    display: inline-block;
    float: none;
    font-size: 12px;
    line-height: 22px;
    margin: 2px 2px 2px 4px;
    padding: 2px 5px;
    background-color: rgb(153, 204, 255);
}
.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
	padding: 4px;
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
							<table style="width:100%;" border="0">
								<tr>
									<td style="width:25%;" valign="top">
										<div id="treeName">科室分类树</div>
										<div style="width:15%;" id="treeId">
											<ul id="leftTree" class="ztree"></ul>
										</div>
									</td>
									<td style="" valign="top" >
										<form action="" name="userForm" id="userForm" method="post">
											<div id="zhongxin" style="padding-top: 10px;">
											<input type="hidden" name="ontoType" value="51006" id="ontoType"/>
											<input type="hidden" name="D_ID" id="D_ID" value="${pd.D_ID }" />
											<input type="hidden" name="DN_ID" id="DN_ID" value="${pd.DN_ID }" />
											<div style="height:auto;max-height: 533px;">
											<table id="table_report" class="table table-bordered table-hover">
												<tr>
													<td width="22%;" style="text-align: right;padding-top: 10px;">科室分类:</td>
													<td colspan="3">
														<div class="shadow" id="selectedCont" style="height: 35px;">
																<c:if test="${pd.DEP_CATEGORY !=null && pd.DEP_CATEGORY !=''}">
																	<span class='tag_grp' id="father_${pd.DEP_CATEGORY}"><b>${pd.DEP_CATEGORY_NAME} [ ${pd.DEP_CATEGORY_CODE}  ]
																	</b>&nbsp;&nbsp;&nbsp;<a onclick="cancelCategory('${pd.DEP_CATEGORY}')">X</a>
																	<input type='hidden' name='DEP_CATEGORY' value="${pd.DEP_CATEGORY}" /></span>
																</c:if>
														</div>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">部位分类:</td>
													<td>
														<select class="chosen-select form-control" style="vertical-align:top;" name="PART_CATEGORY" id="PART_CATEGORY">
														<option value="">请选择</option>
														<option value="0" <c:if test="${pd.PART_CATEGORY == '0' }">selected</c:if> >正常</option>
														<option value="1" <c:if test="${pd.PART_CATEGORY == '1' }">selected</c:if> >冻结</option>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">人群分类:</td>
													<td>
														<select class="chosen-select form-control" style="vertical-align:top;"  name="MAN_CATEGORY" id="MAN_CATEGORY">
														<option value="">请选择</option>
														<option value="0" <c:if test="${pd.MAN_CATEGORY == '0' }">selected</c:if> >正常</option>
														<option value="1" <c:if test="${pd.MAN_CATEGORY == '1' }">selected</c:if> >冻结</option>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">病种类型:</td>
													<td>
														<select class="chosen-select form-control" style="vertical-align:top;" name="DIS_CATEGORY" id="DIS_CATEGORY">
														<option value="">请选择</option>
														<option value="0" <c:if test="${pd.DIS_CATEGORY == '0' }">selected</c:if> >正常</option>
														<option value="1" <c:if test="${pd.DIS_CATEGORY == '1' }">selected</c:if> >冻结</option>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;padding-top: 10px;">是否慢性病:</td>
													<td>
														<label><input class="ace" id="u3897_input" value="0" name="IS_CHRONIC" type="radio" <c:if test="${pd.IS_CHRONIC==0 || pd.IS_CHRONIC == null}">checked</c:if> >否&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
														<label><input class="ace" id="u3897_input" value="1" name="IS_CHRONIC" type="radio" <c:if test="${pd.IS_CHRONIC==1 }">checked</c:if> >是&nbsp;<span class="lbl"></span></label>
													</td>
												</tr>
											</table>
											</div>
											<div class="position-relative" id="osynPageParam" style="padding-bottom:4px;">
												<table style="width:100%;">
													<tr>
														<td style="text-align: center;" colspan="10">
														<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
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
	initTree();
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		$(window)
		.off('resize.chosen')
		.on('resize.chosen', function() {
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': '98%'});
			});
		}).trigger('resize.chosen');
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}
});

var zTree;
var zTreeUrl = basePath+"ontoTree/tree.do?ontoType=" + $("#ontoType").val();
function initTree(){
	var setting = {
		view: {
			showIcon:false,
			showLine: true,
			dblClickExpand: false,
			selectedMulti: false
		},
		callback:{
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
	zTree.setting.edit.showRenameBtn = true;
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
	//判断是否重复添加
// 	var exit = $("#selectedCont").find("#father_"+treeNode.id);
// 	if(exit.length!=0){
// 		bootbox.dialog({
// 			message: "<span class='bigger-110'>重复添加！</span>",
// 			buttons: 			
// 			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
// 		});
// 		return;
// 	}
	$("#selectedCont").empty();
	$("#selectedCont").append($("<span class='tag_grp' id='father_"+treeNode.id+"''><b>"+treeNode.name
		+"</b>&nbsp;&nbsp;&nbsp;<a onclick='cancelCategory("+treeNode.id+");'>X</a>"+
		"<input type='hidden' name='DEP_CATEGORY' value='"+treeNode.id +"' /></span>"
	));
}
//删除一个父节点
function cancelCategory(id){
	$("#father_"+id).remove();
}
//移除一个同义词table
function closeTable(obj){
	$(obj).closest("table").remove();
}

//保存本体
function save(){
	var flag = true;
	if($("#selectedCont").children().length==0){
		$("#selectedCont").tips({ side:3, msg:'请选择科室分类', bg:'#AE81FF',  time:3   });
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
	if(flag){
		save2();
	}
}
function save2(){
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	$.ajax({
		type: "POST",
		url: basePath+'common/knowledgeEdit.do',
    	data: $("#userForm").serialize(),
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			if(data.result=="success"){
				document.getElementById("zhongxin").style.display = 'none';
				top.Dialog.close();
			 	bootbox.dialog({
					message: "<span class='bigger-110'>保存成功，请到本体审核页面查看。</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
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

</script>
</html>