<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
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
<style type="text/css">
.changeTd{
	background-color:#ffb752;
}
.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
	padding: 4px;
}
.form-control{
	height: 30px;
	padding: 1px;
}
select.form-control {
	padding: 1px;
}
input[type="text"]{
	padding: 2px 2px 2px;
}
span.edit {
  float:right;
  background-attachment: scroll;
  background-color: transparent;
  background-image: url("static/images/zTreeStandard.png");
  background-position: -112px -49px;
  background-repeat: no-repeat;
  border: 0 none;
  cursor: pointer;
  display: inline-block;
  height: 16px;
  line-height: 0;
  margin: 0;
  outline: medium none;
  vertical-align: middle;
  width: 16px;
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
						<form action="osyn/pass.do" name="checkForm" id="checkForm" method="post">
						<input type="hidden" name="H_ID" value="${pd_new.H_ID }"/>
						<input type="hidden" name="DN_ID" id="DN_ID" value="${pd_new.DN_ID }"/>
						<input type="hidden" name="ONTO_TYPE" value="${pd_new.ONTO_TYPE }"/>
						<input type="hidden" name="OP_TYPE" value="${pd_new.OP_TYPE }"/>
						<input type="hidden" name="STAD_DN_ID" value="${pd_new.STAD_DN_ID }"/>
						<input type="hidden" name="STATUS" id="STATUS"/>
						<input type="hidden" name="refreshFlag" value="${refreshFlag}" id="refreshFlag"/>
						<div id="zhongxin" style="padding-top: 13px;overflow: auto;height:450px;">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									
									<td align="left" width="24%">
										<c:if test="${pd_new.OP_TYPE ==0}">新增同义词</c:if>
										<c:if test="${pd_new.OP_TYPE ==1}">修改同义词</c:if>
										<c:if test="${pd_new.OP_TYPE ==4}">停用术语</c:if>
										：</b>
									</td>
									<td align="center" width="38%">原数据</td>
									<td align="center" width="38%">新数据</td>
								</tr>
							</thead>
													
							<tbody id="standTbody">
								<tr>
									<td align="right">药品剂型_中文：</td>
									<td>${pd.F_CHN_NAME }</td>
									<td>${pd_new.DN_CHN}<c:if test="${pd_new.OP_TYPE<=1 }"><span class="edit" onclick="setInput(this,'DN_CHN','${pd_new.DN_CHN}');"></span></c:if></td>
								</tr>
								<tr>
									<td align="right">药品剂型_英文：</td>
									<td>${pd.F_ENG_NAME }</td>
									<td>${pd_new.DN_ENG}<c:if test="${pd_new.OP_TYPE<=1 }"><span class="edit" onclick="setInput(this,'DN_ENG','${pd_new.DN_ENG}');"></span></c:if></td>
								</tr>
								<tr>
									<td align="right">药品标准剂型_中文：</td>
									<td>${pd.STAD_DOS_CHN }</td>
									<td>${pd_new.STAD_CHN}</td>
								</tr>
								<tr>
									<td align="right">药品标准剂型_英文：</td>
									<td>${pd.STAD_DOS_ENG }</td>
									<td>${pd_new.STAD_ENG}</td>
								</tr>
								<tr>
									<td align="right">来源-药品剂型_中文：</td>
									<td>${pd.ORG_DOS_CHN }</td>
									<td>${pd_new.ORG_DN_CHN}<c:if test="${pd_new.OP_TYPE<=1 }"><span class="edit" onclick="setInput(this,'ORG_DN_CHN','${pd_new.ORG_DN_CHN}');"></span></c:if></td>
								</tr>
								<tr>
									<td align="right">来源-药品剂型_英文：</td>
									<td>${pd.ORG_DOS_ENG }</td>
									<td>${pd_new.ORG_DN_ENG}<c:if test="${pd_new.OP_TYPE<=1 }"><span class="edit" onclick="setInput(this,'ORG_DN_ENG','${pd_new.ORG_DN_ENG}');"></span></c:if></td>
								</tr>
								<tr>
									<td align="right">来源-药品标准剂型_中文：</td>
									<td>${pd.ORG_STAD_DOS_CHN }</td>
									<td>${pd_new.ORG_STAD_CHN}</td>
								</tr>
								<tr>
									<td align="right">来源-药品标准剂型_英文：</td>
									<td>${pd.ORG_STAD_DOS_ENG }</td>
									<td>${pd_new.ORG_STAD_ENG}</td>
								</tr>
								<tr>
									<td align="right">备注：</td>
									<td>${pd.REMARK }</td>
									<td>${pd_new.DRUG_MEMO}<c:if test="${pd_new.OP_TYPE<=1 }"><span class="edit" onclick="setInput(this,'DRUG_MEMO','${pd_new.DRUG_MEMO}');"></span></c:if></td>
								</tr>
								<tr>
									<td align="right">停用标记：</td>
									<td><c:if test="${pd.IS_DISABLE ==0 }"><span class="label label-success arrowed">正常</span></c:if><c:if test="${pd.IS_DISABLE ==1 }"><span class="label label-important arrowed-in">停用</span></c:if></td>
									<td><c:if test="${pd_new.IS_DISABLE ==0 }"><span class="label label-success arrowed">正常</span></c:if><c:if test="${pd_new.IS_DISABLE ==1 }"><span class="label label-important arrowed-in">停用</span></c:if><c:if test="${pd_new.OP_TYPE<=1 ||pd_new.OP_TYPE==4}"><span class="edit" onclick="setSelect(this,'IS_DISABLE');"></span></c:if></td>
								</tr>
								<tr>
									<td align="right">停用描述：</td>
									<td>${pd.DESCRIPTION }</td>
									<td>${pd_new.DESCRIPTION}<c:if test="${pd_new.OP_TYPE<=1 ||pd_new.OP_TYPE==4}"><span class="edit" onclick="setInput(this,'DESCRIPTION','${pd_new.DESCRIPTION}');"></span></c:if></td>
								</tr>
								<tr>
									<td align="right">版本：</td>
									<td>${pd.VERSION }</td>
									<td>${pd_new.VERSION}</td>
								</tr>
								<tr>
									<td align="right">更新人：</td>
									<td>${pd.UPDATE_MAN }</td>
									<td>${pd_new.UPDATE_MAN}</td>
								</tr>
								<tr>
									<td align="right">更新时间：</td>
									<td>
										<fmt:formatDate value="${pd.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
									<td> 
										<fmt:formatDate value="${pd_new.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
								<c:if test="${pd_new.OP_TYPE==1 }">
								<tr>
									<td align="right">变更描述：</td>
									<td colspan="2">${pd_new.UPD_DESC }</td>
								</tr>
								</c:if>
								<tr>
									<td align="right">审批意见：</td>
									<td colspan="2"><input type="text" name="CHECK_MEMO" style="width: 95%;"/></td>
								</tr>
								<tr id="showConsol">
									<td colspan="3" align="center"><a>显示全部</a></td>
								</tr>
							</tbody>
						</table>
						</div>
						<div style="padding: 0px;margin: 4px;vertical-align: middle;text-align: center;">
							<c:if test="${pd_new.STATUS==0 }">
								<label>下一条&nbsp;<input type='checkbox' class="ace" id="nextShow" <c:if test="${nextShow==1}">checked="checked"</c:if>/><span class="lbl">&nbsp;&nbsp;&nbsp;</span></label>
								<ts:rights code="OSYN_CHECK_PASS_${RIGHTS}">
									<a class="btn btn-mini btn-success" onclick="pass('${pd_new.H_ID}',0);">通&nbsp;&nbsp;过</a>
								</ts:rights>
								<ts:rights code="OSYN_CHECK_REFUSE_${RIGHTS}">
									<a class="btn btn-mini btn-danger" onclick="pass('${pd_new.H_ID}',1);">拒&nbsp;&nbsp;绝</a>
								</ts:rights>
							</c:if>
							<a class="btn btn-mini btn-success" onclick="top.Dialog.close();"> 关闭</a>
						</div>
						</form>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	<div style="display: none;">
		<select class="chosen-select form-control" style="vertical-align:top;display: none" name="IS_DISABLE" id="IS_DISABLE">
			<option value="0" <c:if test="${newOnto.IS_DISABLE==0}">selected</c:if>>否</option>
			<option value="1" <c:if test="${newOnto.IS_DISABLE==1}">selected</c:if>>是</option>
		</select>
	</div>
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
$(function(){
	var i = 0;
	$("#showConsol").bind("click",function (){
		if(i==0){
			//全部显示
			$("#standTbody").children().show();
			i=1;
			$("#showConsol").children().eq(0).html("<a>收  缩</a>");
		}else if(i==1){
			showConsol();
			i=0;
			$("#showConsol").children().eq(0).html("<a>显示全部</a>");
		}
	});
	showConsol();
	
})

//审核通过
function pass(id,type){
	//数据校验
	if(!checkData()){
		return;
	}
	var flag = $("#nextShow").prop("checked");
	var url = "";
	if(type==0){
		$("#STATUS").attr("value",'1');
		url = basePath+'osyn/pass.do';
	}else{
		$("#STATUS").attr("value",'2');
		url = basePath+'osyn/refuse.do';
	}
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	$.ajax({
		type: "POST",
		url: url+'?tm='+new Date().getTime(),
    	data: $("#checkForm").serialize(),
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			if(data.result=="success"){
				if(flag){
					//下一条ID
					var nextid = 0;
					//获取上一页查询条件，如果页面整体框架变动则此处需要更改
					var searchForm = parent.frames[0].$("iframe[src='osynHis/checkList.do']")[0].contentWindow.document.osynForm;
					//显示下一条
					self.location = path+'/osynHis/checkDetail.do?nextShow=1&refreshFlag='+$("#refreshFlag").val()+"&"+ $(searchForm).serialize();
				}else{
					document.getElementById("zhongxin").style.display = 'none';
					top.Dialog.close();
				}
			}else{
				$("#zhongxin").show();
				$("#zhongxin2").hide();
				bootbox.dialog({
					message: "<span class='bigger-110'>"+data.result+"</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
			}
		}
	});
	
}

//审核拒绝
function refuse(){
	$("#STATUS").attr("value",'2');
	$("#checkForm").attr("action","osyn/refuse.do");
	$("#checkForm").submit();
	$("#zhongxin").hide();
	$("#zhongxin2").show();
}

//控制对比的颜色
function showConsol(){
	var start = 3;
	var end = $("#standTbody").children().length;
	$("#standTbody").children().each(function(index,element){
		//默认隐藏列
		if(index>=start && index<=(end-6)){
			$(this).hide();
		}
		//变更数据显示，设置背景
		if(index>=0 && index<=(end-6)){
			var childrens = $(this).children();
			var s1 =childrens.eq(1).text();
			var s2 =childrens.eq(2).text();
			//如果对比不相等则显示
			if($.trim(s1) != $.trim(s2)){
				if(childrens.eq(2).children().length==0){
					childrens.eq(2).addClass("changeTd");
				}else if(childrens.eq(2).children().eq(0).attr("class")=='edit'){
					childrens.eq(2).addClass("changeTd");
				}
				$(this).show();
			}
		}
	});
}

//双击显示输入框
function setInput(obj,name,val){
	var htm = "<input type='text' name='"+name+"' id='"+name+"' value='"+val+"' style='width:98%;'/>"
	$(obj).parent().html(htm).attr("onclick","return false;").removeClass("changeTd");
	$("#"+name).val("").focus().val(val);
}
//双击显示下拉框
function setSelect(obj,name){
	$(obj).parent().html($("#"+name).show()).attr("onclick","return false;").removeClass("changeTd");
}
//检查名称是否存在
function checkName(obj,name){
	if($.trim($(obj).val())==""){
		return false;
	}
	var flag = true;
	var mydata = {DN_CHN:$.trim($(obj).val()),ontoType:$("#ontoType").val(),standFlag:1,DN_ID:$("#DN_ID").val()};
	$.ajax({
		type: "POST",
		url: basePath+'/osyn/checkExistName.do',
    	data: mydata,
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			if(data.result=="success"){
			
			}else{
				$(obj).tips({ side:3, msg:data.result, bg:'#AE81FF',  time:3   });
				flag = false;
			}
		}
	});
	return flag;
}
//检验数据
function checkData(){
	var flag = true;
	var DN_CHN = document.getElementById("DN_CHN");
	if(DN_CHN!=null){
		if(DN_CHN.value ==null || DN_CHN.value=="" ){
			$(DN_CHN).tips({ side:3, msg:'请输入中文名', bg:'#AE81FF',  time:3   });
			flag = false;
		}
		if(!checkName($("#DN_CHN"),'DN_CHN')){
			flag = false;
		}
	}
	var DN_ENG = document.getElementById("DN_ENG");
	if(DN_ENG!=null){
		if(DN_ENG.value ==null || DN_ENG.value=="" ){
			$(DN_ENG).tips({ side:3, msg:'请输入英文名', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var ORG_DN_CHN = document.getElementById("ORG_DN_CHN");
	if(ORG_DN_CHN!=null){
		if(ORG_DN_CHN.value ==null || ORG_DN_CHN.value=="" ){
			$(ORG_DN_CHN).tips({ side:3, msg:'请输入来源中文名', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var ORG_DN_ENG = document.getElementById("ORG_DN_ENG");
	if(ORG_DN_ENG!=null){
		if(ORG_DN_ENG.value ==null || ORG_DN_ENG.value=="" ){
			$(ORG_DN_ENG).tips({ side:3, msg:'请输入来源英文名', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var IS_DISABLE = document.getElementById("IS_DISABLE");
	if(IS_DISABLE!=null && IS_DISABLE.value==1){
		var DESCRIPTION = document.getElementById("DESCRIPTION");
		if(DESCRIPTION!=null){
			if(DESCRIPTION.value ==null || DESCRIPTION.value=="" ){
				$(DESCRIPTION).tips({ side:3, msg:'输入停用描述', bg:'#AE81FF',  time:3   });
				flag = false;
			}
		}
	}
	
	return flag;
}
</script>
</html>
