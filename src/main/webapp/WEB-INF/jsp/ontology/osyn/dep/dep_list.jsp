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
							
							<form name="osynForm" id="osynForm" method="post" action="osyn/osynAllList.do">
							<table style="margin-top:5px;">
								<tr>
									<td style="padding-left:5px;padding-right:2px;">
										<div class="nav-search" style="float: left;">
											<span class="input-icon">
												<input class="nav-search-input" autocomplete="off" id="DN_NAME" maxlength="255" type="text" name="DN_NAME" value="${pd.DN_NAME }" placeholder="名称/编码等" style="width: 145px;" />
												<i class="ace-icon fa fa-search nav-search-icon"></i>
											</span>
										</div>
										<div style="float: right;">
											<a class="btn btn-light btn-xs" onclick="searchs(this);" title="检索" target="treeFrame" id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										</div>
									</td>
									<td style="vertical-align:top;padding-left:5px;padding-right:2px;" nowrap> 
										查询类型：
									 	<select class="chosen-select form-control" name="wordType" id="wordType" data-placeholder="" style="vertical-align:top;width: 120px;" onchange="changeWord();">
											<option value=""></option>
											<c:forEach items="${wordMap.entrySet()}" var="ontoTyp" varStatus="vs">
												<option value="${ontoTyp.key}" <c:if test="${ontoTyp.key == wordType }">selected</c:if>>${ontoTyp.value}</option>
											</c:forEach>
											<!-- <option value="1" <c:if test="${wordType == 1 }">selected</c:if>>标准词</option> -->
										</select>
									</td>
									
									<td style="vertical-align:top;padding-left:5px;padding-right:2px;" nowrap>
										术语类别：
									 	<select class="chosen-select form-control" name="termCategory" id="termCategory" data-placeholder="排序方式" style="vertical-align:top;width: 120px;">
									 		<option value=""></option>
									 		<c:forEach items="${typeMap.entrySet()}" var="ontoTyp" varStatus="vs">
												<option value="${ontoTyp.key}" <c:if test="${ontoTyp.key == osynType }">selected</c:if>>${ontoTyp.value}</option>
											</c:forEach>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:5px;padding-right:2px;" nowrap>
										更 新&nbsp;&nbsp;人 ：
										<select class="chosen-select form-control" name="UPDATE_MAN" id="UPDATE_MAN" data-placeholder="更新人" style="vertical-align:top;width: 100px;">
											<option value=""></option>
											<c:forEach items="${userList}" var="user" varStatus="vs">
												<option value="${user.USERNAME}" <c:if test="${pd.UPDATE_MAN==user.USERNAME}">selected</c:if>>${user.USERNAME}&nbsp;&nbsp;</option>
											</c:forEach>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:5px;padding-right:2px;" nowrap>
										更新日期：
										<input class="span10 date-picker" name="UPDATE_TIME" id="UPDATE_TIME"  value="${pd.UPDATE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="更新开始日期"/>
										<input class="span10 date-picker" name="UPDATE_TIME_END" id="UPDATE_TIME_END"  value="${pd.UPDATE_TIME_END}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="更新结束日期"/>
									</td>
								</tr>
								<tr>
									<td style="vertical-align:top;padding-left:5px;padding-right:2px;padding-top: 5px;" nowrap>
										是否停用：
									 	<select class="chosen-select form-control" name="IS_DISABLED" id="IS_DISABLED" data-placeholder="是否停用" style="vertical-align:top;width: 120px;">
											<option value=""></option>
											<option value="" <c:if test="${pd.IS_DISABLED==null || pd.IS_DISABLED==''}">selected</c:if>>全部</option>
											<option value="0" <c:if test="${pd.IS_DISABLED=='0'}">selected</c:if>>否</option>
											<option value="1" <c:if test="${pd.IS_DISABLED=='1'}">selected</c:if>>是</option>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:5px;padding-right:2px;padding-top: 5px;">
										排列方式：
										<select class="chosen-select form-control" name="CONDITION" id="CONDITION" data-placeholder="排序" style="vertical-align:top;width: 120px;">
											<option value=""></option>
											<option value="1" <c:if test="${pd.CONDITION=='1'}">selected</c:if>>按更新时间排序</option>
										</select>
									</td>
									
									<td id="osynTd" style="vertical-align:top;padding-left:5px;padding-right:2px;padding-top: 5px;" nowrap>
										同义词类型：
										<select class="chosen-select form-control" name="OSYN_TYPE" id="OSYN_TYPE" data-placeholder="同义词类型" style="vertical-align:top;width:120px;">
											<option value=""></option>
											<option value="23101" <c:if test="${pd.OSYN_TYPE=='23101'}">selected</c:if>>俗语</option>
											<option value="23102" <c:if test="${pd.OSYN_TYPE=='23102'}">selected</c:if>>缩略语</option>
											<option value="23103" <c:if test="${pd.OSYN_TYPE=='23103'}">selected</c:if>>同音字/错别字</option>
											<option value="23104" <c:if test="${pd.OSYN_TYPE=='23104'}">selected</c:if>>拼音首字母</option>
											<option value="23105" <c:if test="${pd.OSYN_TYPE=='23105'}">selected</c:if>>语用同义词</option>
											<option value="23106" <c:if test="${pd.OSYN_TYPE=='23106'}">selected</c:if>>专指同义词</option>
											<option value="23107" <c:if test="${pd.OSYN_TYPE=='23107'}">selected</c:if>>其它</option>
										</select>
									</td>
								</tr>
							</table>
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
								<thead>
									<tr>
										<th class="center">科室名称</th>
										<th class="center">标准科室名称</th>
										<th class="center">同义词类型</th>
										<th class="center">停用标记</th>
										<th class="center">更新人</th>
										<th class="center">更新时间</th>
									</tr>
								</thead>
								<tbody>
									<!-- 列表循环开始 -->
									<c:choose>
										<c:when test="${not empty osynList}">
											<c:forEach items="${osynList}" var ="osyn" varStatus="vs">
												<tr id="${osyn.ID }" edit_flag="${osyn.OSYN_EDIT_FLAG }">
													<td class='center' <c:if test="${osyn.OSYN_EDIT_FLAG==1}"> style="color: #ffb752" title="审核中" </c:if>>${osyn.DEP_NAME}</td>
													<td class='center'>${osyn.STANDARD_NAME}</td>
													
													<td class='center'>
														<c:if test="${osyn.SYNO_TYPE == 23101}">俗语</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23102}">拼音首字母</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23103}">缩略语</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23104}">同音字/错别字</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23105}">语用同义词</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23106}">专指同义词</c:if>
														<c:if test="${osyn.SYNO_TYPE == 23107}">其它</c:if>
													</td>
													<td class='center'>
														<c:if test="${osyn.IS_DISABLE == 0}">否</c:if>
														<c:if test="${osyn.IS_DISABLE == 1}">是</c:if>
													</td>
													<td class='center'>${osyn.UPDATE_MAN}</td>
													<td class='center'><fmt:formatDate value="${osyn.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr class="main_info">
												<td colspan="6" class="center">没有相关数据</td>
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
												<ts:rights code="OSYN_ADD_${RIGHTS}">
													<a class="btn btn-mini btn-success" onclick="add();">新增术语</a>
												</ts:rights>
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
	changeWord();
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
		$("#osynTd").hide();
	}else{
		$("#osynTd").show();
	}
}
//查询同义词变更历史
function searchHist(dn_id){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="查看历史记录";
	diag.URL = '<%=path%>/alterHist/nameHist.do?DN_ID='+dn_id;
	diag.Width = 900;
	diag.Height = 500;
	diag.CancelEvent = function(){ //关闭事件
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}
//双击选中行
function dbclickF(){  
    var rows=document.getElementById("simple-table").rows;  
    if(rows.length>0){  
        for(var i=1;i<rows.length;i++){  
          (function(i){  
            var dn_id=rows[i].id;  
            var edit_flag=rows[i].getAttribute("edit_flag");  
            var obj=rows[i];  
            obj.ondblclick=function(){
            	toEdit(dn_id,edit_flag);
            };  
            })(i)  
        }  
    }  
}  
//新增
function add(){
	 top.jzts();
	 var wordType=$("#wordType").val();
	 var termCategory = $("#termCategory").val();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="新增";
	 diag.URL = '<%=path%>/osyn/toAdd.do?wordType='+wordType+'&termCategory='+termCategory;
	 diag.Width = 550;
	 diag.Height = 630;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}

function toEdit(on_id,edit_flag){
	if(edit_flag==1){
		alert("该信息正在修改中，审核后才可以继续修改！");
		return;
	}
	top.jzts();
	var wordType=$("#wordType").val();
	 var termCategory = $("#termCategory").val();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="修改同义词";
	 diag.URL = '<%=path%>/osyn/toEdit.do?wordType='+wordType+'&termCategory='+termCategory+'&ON_ID='+on_id;
	 diag.Width = 550;
	 diag.Height = 630;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}
</script>
</html>
