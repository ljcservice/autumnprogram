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
<style>
.check-search{
	float: left;
	margin: 2px;
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
						<!-- 检索  -->
						<form name="searchForm" id="searchForm" action="opDrug/list.do">
						<div class="col-xs-12" id="btnDiv">
							<input type="hidden" name="category_id" id="category_id" value=""/>
							<div class="check-search nav-search"  >
								手术名称
								<span class="input-icon" style="">
									<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="O_NAME" value="${pd.O_NAME }" placeholder="名称/编码等" maxlength="80"/>
									<i class="ace-icon fa fa-search nav-search-icon"></i>
								</span>
							</div>
							<div class="check-search"  >
								<a class="btn btn-light btn-xs" onclick="searchs(this);" title="检索" target="treeFrame" id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
								<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
							</div>
							<div class="check-search"   >
								更新日期：
								<input class="span10 date-picker" name="UPDATE_TIME_START" id="UPDATE_TIME_START"  value="${pd.UPDATE_TIME_START }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="开始日期" />
								<input class="span10 date-picker" name="UPDATE_TIME_END" id="UPDATE_TIME_END"  value="${pd.UPDATE_TIME_END }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:85px;" placeholder="结束日期" />
							</div>
						</div>
						
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" nowrap>手术名称</th>
									<th class="center" nowrap>手术切口等级</th>
									<th class="center" nowrap>手术药品名称</th>
									<th class="center" nowrap>规格</th>
									<th class="center" nowrap>剂型</th>
									<th class="center" nowrap>科室名称</th>
									<th class="center" nowrap>医生名称</th>
									<th class="center" nowrap>是否可用</th>
									<th class="center" nowrap>操作人</th>
									<th class="center" nowrap>操作时间</th>
									<th class="center" nowrap>操作</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="obj" varStatus="vs">
										<tr>
											<td class="center" style="width: 30px;">
												<label><input type="checkbox" class="ace" name='ids' value="${obj.O_ID}" /><span class="lbl"></span></label>
											</td>
											<td class="center">${obj.O_NAME}</td>
											<td class="center">${obj.O_LEVEL}</td>
											<td class="center">${obj.O_DRUG_NAME}</td>
											<td class="center">${obj.O_DRUG_SPCE}</td>
											<td class="center">${obj.O_DRUG_FORM}</td>
											<td class="center">${obj.O_DEPT_NAME}</td>
											<td class="center">${obj.O_DOCTOR_NAME}</td>
											<td class="center"><c:if test="${obj.IS_USE==0}">不可用</c:if><c:if test="${obj.IS_USE==1}">可用</c:if></td>
											<td class="center">${obj.OPERATORUSER_NAME}</td>
											<td class="center">${obj.OPERATORDATE}</td>
											<td class="center" width="80px;">
												<div class="btn-group" style="height: 30px;width: auto;overflow: visible;">
														<a class="btn btn-xs btn-success" title="编辑" onclick="editOp('${obj.O_ID}');">
															<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
														</a>
														<a class="btn btn-xs btn-danger" onclick="delOp('${obj.O_ID }','${obj.opNAME }');">
															<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
														</a>
												</div>
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
							</tbody>
						</table>
						
						<div class= "pageStrDiv" id="pageStrDiv" style="padding-top: 10px;padding-bottom: 10px;">
							<table style="width:100%;">
								<tr>
									<td>
										<div class="pagination" style="float: right;padding: 0px;margin: 0px;">${page.pageStr}</div>
									</td>
								</tr>
							</table>
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
<script type="text/javascript" src="static/js/ontology/lockTable.js?v=20161"></script>
<script type="text/javascript">
$(top.hangge());

$(function() {
	//重置当前页面高度，自适应浏览器
	initWidthHeight();
});
//重置当前页面高度，自适应浏览器
function initWidthHeight(){
	var mycars = new Array();
	mycars[0]="btnDiv";mycars[1]="pageStrDiv";
	FixTable("simple-table", 2 ,mycars);
}
//检索
function searchs(){
	top.jzts();
	$("#searchForm").submit();
}

//删除
function delOp(id,msg){
	bootbox.confirm("确定要删除吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>opDrug/delete.do?O_ID="+id+"&tm="+new Date().getTime();
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//新增
function add(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="新增";
	 diag.URL = '<%=path%>/opDrug/toAdd.do';
	 diag.Width = 600;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
		nextPage(${page.currentPage});
		diag.close();
	 };
	 diag.show();
}

//修改
function editOp(id){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="资料";
	 diag.URL = '<%=path%>/opDrug/toEdit.do?o_id='+id;
	 diag.Width = 600;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
			nextPage(${page.currentPage});
		diag.close();
	 };
	 diag.show();
}

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


//批量操作
function makeAll(){
	bootbox.confirm('确定要删除选中的数据吗?', function(result) {
		if(result) {
			var str = '';
			var emstr = '';
			var phones = '';
			var username = '';
			for(var i=0;i < document.getElementsByName('ids').length;i++)
			{
				  if(document.getElementsByName('ids')[i].checked){
				  	if(str=='') str += document.getElementsByName('ids')[i].value;
				  	else str += ',' + document.getElementsByName('ids')[i].value;
				  	
				  	if(emstr=='') emstr += document.getElementsByName('ids')[i].id;
				  	else emstr += ';' + document.getElementsByName('ids')[i].id;
				  	
				  	if(phones=='') phones += document.getElementsByName('ids')[i].alt;
				  	else phones += ';' + document.getElementsByName('ids')[i].alt;
				  	
				  	if(username=='') username += document.getElementsByName('ids')[i].title;
				  	else username += ';' + document.getElementsByName('ids')[i].title;
				  }
			}
			if(str==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				$("#zcheckbox").tips({
					side:3,
		            msg:'点这里全选',
		            bg:'#AE81FF',
		            time:8
		        });
				
				return;
			}else{
					$.ajax({
						type: "POST",
						url: '<%=basePath%>opDrug/deleteAll.do?tm='+new Date().getTime(),
				    	data: {USER_IDS:str},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							if(data.result=="success"){
								top.jzts();
								 $.each(data.list, function(i, list){
									nextPage(${page.currentPage});
								 });
							}else{
								bootbox.dialog({
									message: "<span class='bigger-110'>"+data.result+"</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
								});
							}
						}
					});
			}
		}
	});
}


</script>
</html>