<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
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
  	  background-color: #ffb951;	
  	}
	</style>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							
							<form name="osynForm" id="osynForm" method="post" action="osynHis/checkList.do">
							<table style="margin-top:5px;">
								<tr>
									<td style="padding-left:2px;padding-right:8px;padding-top:5px;">
										<div class="nav-search" style="float: left;">
											<span class="input-icon">
												<input class="nav-search-input" autocomplete="off" id="DN_NAME" type="text" name="DN_NAME" value="${pd.DN_NAME }" placeholder="名称/编码等" style="width: 130px;" />
												<i class="ace-icon fa fa-search nav-search-icon"></i>
											</span>
										</div>
										<div style="float: right;">
											<a class="btn btn-light btn-xs" onclick="searchs(this);" title="检索" target="treeFrame" id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										</div>
									</td>
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap> 
										查询类型：
									 	<select class="chosen-select form-control" name="wordType" id="wordType" data-placeholder="" style="vertical-align:top;width: 100px;">
											<c:forEach items="${wordMap.entrySet()}" var="ontoTyp" varStatus="vs">
												<option value="${ontoTyp.key}" <c:if test="${ontoTyp.key == wordType }">selected</c:if>>${ontoTyp.value}</option>
											</c:forEach>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;" nowrap>
										术语类别：
									 	<select class="chosen-select form-control" name="onto_type" id="onto_type" data-placeholder="排序方式" style="vertical-align:top;width: 150px;">
									 		<option value=""></option>
									 		<c:forEach items="${typeMap.entrySet()}" var="ontoTyp" varStatus="vs">
												<option value="${ontoTyp.key}" <c:if test="${ontoTyp.key == osynType }">selected</c:if>>${ontoTyp.value}</option>
											</c:forEach>
										</select>
									</td>
									
									
									<td style="padding-left:2px;padding-right:8px;" nowrap>
										更 新人：
										<select class="chosen-select form-control" name="UPDATE_MAN" id="UPDATE_MAN" data-placeholder="更新人" style="vertical-align:top;width: 100px;">
											<option value=""></option>
											<c:forEach items="${userList}" var="user" varStatus="vs">
												<option value="${user.USERNAME}" <c:if test="${pd.UPDATE_MAN==user.USERNAME}">selected</c:if>>${user.USERNAME}&nbsp;&nbsp;</option>
											</c:forEach>
										</select>
									</td>
									<td style="padding-left:2px;padding-right:8px;" nowrap>
										更新日期：
										<input class="span10 date-picker" name="UPDATE_TIME" id="UPDATE_TIME" value="${pd.UPDATE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="更新开始日期" title="最近登录开始"/>
										<input class="span10 date-picker" name="UPDATE_TIME_END" id="UPDATE_TIME_END" value="${pd.UPDATE_TIME_END}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="更新结束日期" title="最近登录结束"/>
									</td>
								</tr>
								<tr>
									
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;padding-top:5px;" nowrap>
										操作类型：
									 	<select class="chosen-select form-control" name="OP_TYPE" id="OP_TYPE" data-placeholder="操作类型" style="vertical-align:top;width: 100px;">
											<option value=""></option>
											<option value="" <c:if test="${pd.OP_TYPE==null || pd.OP_TYPE==''}">selected</c:if>>全部</option>
											<option value="0" <c:if test="${pd.OP_TYPE=='0'}">selected</c:if>>新增</option>
											<option value="1" <c:if test="${pd.OP_TYPE=='1'}">selected</c:if>>修改</option>
											<option value="4" <c:if test="${pd.OP_TYPE=='4'}">selected</c:if>>停用术语</option>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;padding-top:5px;" nowrap>
										审核状态：
									 	<select class="chosen-select form-control" name="STATUS" id="STATUS" data-placeholder="审核状态" style="vertical-align:top;width: 100px;">
											<option value=""></option>
											<option value="" <c:if test="${pd.STATUS==null || pd.STATUS==''}">selected</c:if>>全部</option>
											<option value="0" <c:if test="${pd.STATUS=='0'}">selected</c:if>>审核中</option>
											<option value="1" <c:if test="${pd.STATUS=='1'}">selected</c:if>>审核通过</option>
											<option value="2" <c:if test="${pd.STATUS=='2'}">selected</c:if>>审核拒绝</option>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:2px;padding-right:8px;padding-top:5px;" nowrap>
										是否停用：
									 	<select class="chosen-select form-control" name="IS_DISABLED" id="IS_DISABLED" data-placeholder="是否停用" style="vertical-align:top;width: 100px;">
											<option value=""></option>
											<option value="" <c:if test="${pd.IS_DISABLED==null || pd.IS_DISABLED==''}">selected</c:if>>全部</option>
											<option value="0" <c:if test="${pd.IS_DISABLED=='0'}">selected</c:if>>否&nbsp;&nbsp;</option>
											<option value="1" <c:if test="${pd.IS_DISABLED=='1'}">selected</c:if>>是&nbsp;&nbsp;</option>
										</select>
									</td>
									
								</tr>
								
							</table>
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
								<thead>
									<tr>
										<th class="center" style="width:35px;">
										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
										</th>
										<th class="center">科室名称</th>
										<th class="center">标准科室名称</th>
										<th class="center">同义词类型</th>
										<th class="center">审核状态</th>
										<th class="center">停用标记</th>
										<th class="center">更新人</th>
										<th class="center">更新时间</th>
										<th class="center">操作类型</th>
									</tr>
								</thead>
								<tbody>
									<!-- 列表循环开始 -->
									<c:choose>
										<c:when test="${not empty list}">
											<c:forEach items="${list}" var ="osyn" varStatus="vs">
												<tr>
													<td class='center' style="width: 30px;"><label>
														<c:if test="${osyn.STATUS=='0'}">
															<input type='checkbox' name='ids' value="${osyn.H_ID }" ontoType="${osyn.ONTO_TYPE }" dn_id="${osyn.DN_ID }" stad_dn_id="${osyn.STAD_DN_ID }" operation="${osyn.OP_TYPE }" id="${osyn.H_ID }" alt="${osyn.dn_chn}" title="${osyn.dn_chn }" class="ace"/><span class="lbl"></span>
														</c:if>
														<c:if test="${osyn.STATUS=='1' || osyn.STATUS=='2'}">
															<input type='hidden' name='ids' value="${osyn.H_ID }" ontoType="${osyn.ONTO_TYPE }" dn_id="${osyn.DN_ID }" stad_dn_id="${osyn.STAD_DN_ID }" operation="${osyn.OP_TYPE }" id="${osyn.H_ID }" alt="${osyn.dn_chn}" title="${osyn.dn_chn }" class="ace"/><span class="lbl"></span>
														</c:if>
													</label></td>
													<td class='center'>${osyn.DN_CHN }</td>
													<td class='center'>${osyn.DEP_NAME}</td>
													<td class='center'>
														<c:if test="${osyn.SYNO_TYPE == 23101}">俗语</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23104}">拼音首字母</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23102}">缩略语</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23103}">同音字/错别字</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23105}">语用同义词</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23106}">专指同义词</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23107}">其它</c:if>
													</td>
													<td class='center'>
														<c:if test="${osyn.STATUS==0}">未审核</c:if>
														<c:if test="${osyn.STATUS==1}">审核通过</c:if>
														<c:if test="${osyn.STATUS==2}">审核拒绝</c:if>
													</td>
													<td class='center'>
														<c:if test="${osyn.IS_DISABLE == 0}">否</c:if>
														<c:if test="${osyn.IS_DISABLE == 1}">是</c:if>
													</td>
													<td class='center'>${osyn.UPDATE_MAN}</td>
													<td class='center'><fmt:formatDate value="${osyn.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
													<td class='center'>
														<c:if test="${osyn.OP_TYPE == 0}">新增</c:if>
														<c:if test="${osyn.OP_TYPE == 1}">修改</c:if>
														<c:if test="${osyn.OP_TYPE == 2}">修改父节点</c:if>
														<c:if test="${osyn.OP_TYPE == 3}">术语知识编辑</c:if>
														<c:if test="${osyn.OP_TYPE == 4}">停用术语</c:if>
														<c:if test="${osyn.OP_TYPE == 7}">级联新增</c:if>
														<c:if test="${osyn.OP_TYPE == 8}">被动新增</c:if>
														<c:if test="${osyn.OP_TYPE == 9}">删除</c:if>
													</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr class="main_info">
												<td colspan="10" class="center">没有相关数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
									<tr></tr>
								</tbody>
							</table>
							<div class="page-header position-relative">
								<table style="width:100%;">
									<tr>
										<td style="vertical-align:top;">
											<div style="vertical-align:bottom;float: left;padding-top: 0px;margin-top: 0px;">
												<ts:rights code="OSYN_CHECK_PASS_ALL_${RIGHTS}">
													<a class="btn btn-mini btn-success" onclick="check('确认通过吗？');">通&nbsp;&nbsp;过</a>&nbsp;&nbsp;
												</ts:rights>
												<ts:rights code="OSYN_CHECK_REFUSE_ALL_${RIGHTS}">
													<a class="btn btn-mini btn-danger" onclick="check('确定要拒绝吗？');">拒&nbsp;&nbsp;绝</a>&nbsp;&nbsp;
												</ts:rights>
												<!-- <a class="btn btn-mini btn-success" onclick="toDetail();">详&nbsp;&nbsp;情</a>&nbsp;&nbsp; -->
											</div>
											<div class="pagination" style="float: right;padding-top: 0px;margin: 0px;">${page.pageStr}</div>
										</td>
									</tr>
								</table>
							</div>
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
	dbclickF();

	
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

//查询本体
function searchs(){
	top.jzts();
	$("#osynForm").submit();
}
//修改查询的词类型：同义词、标准词
function changeWord(){
	var wordType = $("#wordType").val();
	if(wordType==1){
		$("#OSYN_TYPE").hide();
	}else{
		$("#OSYN_TYPE").show();
	}
	
}
//查询同义词变更历史
function searchHist(dn_id){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="查看历史记录";
	diag.URL = '<%=path%>/osynHis/nameHist.do?DN_ID='+dn_id;
	diag.Width = 900;
	diag.Height = 500;
	diag.CancelEvent = function(){ //关闭事件
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
		}
		diag.close();
	 };
	 diag.show();
}
//审批处理结果
function check(msg){
	bootbox.confirm(msg, function(result) {
		if(result){
			var idsObj = document.getElementsByName("ids");
			var h_ids ='';//选择审核的副本ID
			for (var i = 0; i < idsObj.length; i++) {
				if(idsObj[i].checked){
					if(h_ids=='')h_ids=idsObj[i].value;
					else h_ids+=','+idsObj[i].value;
				}
			}
			if(h_ids==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				return;
			}else{
				if(msg=='确认通过吗？'){
					$.ajax({
						type: "POST",
						url: '<%=basePath%>osyn/passAll.do',
				    	data: {H_IDS:h_ids},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							if(data.result=="success"){
								nextPage(${page.currentPage});
							}else{
								$(top.hangge());
								var msg = data.result;
								if(msg=="failed"){msg="操作失败！";}
								bootbox.dialog({
									message: "<span class='bigger-110'>"+msg+"</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
								});
							}
						}
					});
				}else if(msg=='确定要拒绝吗？'){
					$.ajax({
						type: "POST",
						url: '<%=basePath%>osyn/refuseAll.do',
				    	data: {H_IDS:h_ids},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							if(data.result=="success"){
								nextPage(${page.currentPage});
							}else{
								$(top.hangge());
								var msg = data.result;
								if(msg=="failed"){msg="操作失败！";}
								bootbox.dialog({
									message: "<span class='bigger-110'>"+msg+"</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
								});
							}
						}
					});
				}
			}
		}
	})
}
//双击事件
function dbclickF(){  
    var rows=document.getElementById("simple-table").rows;  
    if(rows.length>0){  
        for(var i=1;i<rows.length;i++){  
          (function(i){  
            var dn_id=rows[i].id;  
            var obj=rows[i];  
            obj.ondblclick=function(){
            	toDetail(obj);
            };  
            })(i)  
        }  
    }  
}

function toDetail(obj){
	top.jzts();
	var osyn = $(obj).children().eq(0).find("input:first");
	if(osyn.val()==undefined || osyn.val()==""){
		alert("请选择修改的本体信息！");
		return;
	}
	if(osyn.length>1){
		alert("请选择单个信息！");
		return;
	}
	var h_id = osyn.val();
	var ontoType = osyn.attr("ontoType");
	var dn_id = osyn.attr("dn_id");
	var operation = osyn.attr("operation");
	var stad_dn_id = osyn.attr("stad_dn_id");
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="查看审批详情";
	diag.URL = '<%=path%>/osynHis/checkDetail.do?h_id='+h_id+'&onto_type='+ontoType+'&dn_id='+dn_id+'&OP_TYPE='+operation+'&stad_dn_id='+stad_dn_id;
	diag.Width = 900;
	diag.Height = 500;
	diag.CancelEvent = function(){ //关闭事件
		searchs();
		diag.close();
	};
	diag.show();
}
</script>
</html>
