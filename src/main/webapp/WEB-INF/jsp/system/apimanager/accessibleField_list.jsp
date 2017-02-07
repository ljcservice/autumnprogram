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
<%@ include file="../index/top.jsp"%>
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
						<div class="col-xs-12">
						<!-- 检索  -->
						<form action="api/access.do" method="post" name="accessForm" id="accessForm">
						<table style="margin-top:5px;">
							<tr>
								<td style="vertical-align:top;padding-left:2px;"> 
								 	<select class="chosen-select form-control" name="businessName" id="businessName" data-placeholder="业务名称" style="vertical-align:top;width: 200px;" onchange="businessNameChange()">
										<option value=""></option>
										<option value="">全部</option>
										<c:choose>
											<c:when test="${not empty interfaceList}">
												<c:forEach items="${interfaceList}" var="interfaceM" varStatus="vs">
													<option value="${interfaceM.Type }" <c:if test="${pd.businessName == interfaceM.Type  }">selected</c:if> >${interfaceM.NAME }</option>
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;display:none;" id="select1"> 
								 	<select class="chosen-select form-control" name="fieldDisc" id="fieldDisc" data-placeholder="列说明" style="vertical-align:top;width: 200px;">
									</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								
							</tr>
						</table>
						<!-- 检索  -->
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">业务名称</th>
									<th class="center">操作表名称</th>
									<th class="center">表说明</th>
									<th class="center">字段名称</th>
									<th class="center">列说明</th>
									<th class="center">数据权限</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty accessList}">
									<c:forEach items="${accessList}" var="access" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' value="${access.DR_ID }"  class="ace"/><span class="lbl"></span></label>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${access.NAME }</td>
											<td class="center">${access.TABLE_NAME }</td>
											<td class="center">${access.TABLE_DISC }</td>
											<td class="center">${access.COLUMN_NAME}</td>
											<td class="center">${access.COLUMN_DISC}</td>
											<td class="center">
												<c:if test="${access.isVal == '1' }"><span class="label label-success arrowed">有</span></c:if>
												<c:if test="${access.isVal == '0' }"><span class="label label-important arrowed-in">无</span></c:if>															
											</td>
											<td class="center">
												<div class="btn-group">
													<ts:rights code="col/goRule">
														<a class="btn btn-xs btn-purple" title="数据权限管理" onclick="dataJurisdiction('${access.DR_ID }');">数据权限管理</a>
													</ts:rights>
													<ts:rights code="api/goEditA">
														<a class="btn btn-xs btn-success" title="编辑" onclick="editAccess('${access.DR_ID}');">
															<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
														</a>
													</ts:rights>
													<ts:rights code="api/deleteA">
													<a class="btn btn-xs btn-danger" title="删除" onclick="delAccess('${access.DR_ID }');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													</ts:rights>
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="100" class="center" >没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<ts:rights code="api/goAddA">
										<a class="btn btn-mini btn-success" onclick="add();">新增</a>
									</ts:rights>
									<ts:rights code="api/deleteAllA">
										<a title="批量删除" class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
									</ts:rights>
								</td>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
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
	<%@ include file="../index/foot.jsp"%>
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
			
			//下拉框
			if(!ace.vars['touch']) {
				$('.chosen-select').chosen({allow_single_deselect:true}); 
				$(window)
				.off('resize.chosen')
				.on('resize.chosen', function() {
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				}).trigger('resize.chosen');
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
		});
		
		//检索
		function searchs(){
			top.jzts();
			$("#accessForm").submit();
		}

		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增字段";
			 diag.URL = '<%=path%>/api/goAddA.do';
			 diag.Width = 600;
			 diag.Height = 415;
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
		
		//修改
		function editAccess(dr_id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="字段信息";
			 diag.URL = '<%=path%>/api/goEditA.do?DR_ID='+dr_id;
			 diag.Width = 600;
			 diag.Height = 415;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function delAccess111(drId,msg){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>api/deleteA.do?DR_ID="+drId+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
				//删除
		function delAccess(drId){
			var url = "<%=basePath%>api/checkDRID.do?DR_ID="+drId;
			$.get(url,function(data){
				var tip = "";
				if("success" == data.result){
					tip ="确定要删除此数据吗?";
				}else{
					tip = data.result;
				}
				bootbox.confirm(tip, function(result) {
					if(result) {
						top.jzts();
						var url = "<%=basePath%>api/deleteA.do?DR_ID="+drId+"&tm="+new Date().getTime();
						$.get(url,function(data){
							if("success" == data.result){

								nextPage(${page.currentPage});
							}else if("failed" == data.result){
								top.hangge();
								bootbox.dialog({
									message: "<span class='bigger-110'>删除失败!</span>",
									buttons: 			
									{
										"button" :
										{
											"label" : "确定",
											"className" : "btn-sm btn-success"
										}
									}
								});
							}
						});
					}
				});
			});
			

		}
		//设置数据权限
		function dataJurisdiction(dr_id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="数据权限";
			 diag.URL = '<%=path%>/col/goRule.do?DR_ID='+dr_id;
			 diag.Width = 800;
			 diag.Height = 500;
			 diag.CancelEvent = function(){ //关闭事件
				diag.close();
			 };
			 diag.show();
		}
		
		//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					var emstr = '';
					var phones = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	
						  	if(emstr=='') emstr += document.getElementsByName('ids')[i].id;
						  	else emstr += ';' + document.getElementsByName('ids')[i].id;
						  	
						  	if(phones=='') phones += document.getElementsByName('ids')[i].alt;
						  	else phones += ';' + document.getElementsByName('ids')[i].alt;
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
						if(msg == '确定要删除选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>api/deleteAllA.do?tm='+new Date().getTime(),
						    	data: {DR_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
						
					}
				}
			});
		}
		//级联业务名称查询列说明
		function businessNameChange(){
			var selected = $("#businessName").val();
			
			if(null==selected||""==selected){
				$("#fieldDisc").empty();
				$("#select1").hide();
			}else{
				$("#fieldDisc").empty();
				
				$.ajax({
					type: "POST",
					url: '<%=basePath%>api/findBusinessName.do',
					data: {businessName:selected},
					dataType:'json',
					success: function(data){
						if("success" == data.result){
							$("#fieldDisc").append("<option value=\"\"></option>");
							$("#fieldDisc").append("<option value=\"\">全部</option>");
							$.each(data.interfaceList, function(i, str){
								$("#fieldDisc").append("<option value=\""+str+"\">"+str+"</option>");
							});
							$("#select1").show();
							$("#fieldDisc").chosen('destroy');
							$('#fieldDisc').chosen({allow_single_deselect:true});
						}else{
							$("#select1").hide();
						}
					}
				});
				
				//$("#fieldDisc").append("<option value=\"answer\">全---fffff--部</option>");
				//$("#fieldDisc").chosen('destroy');
				//$('#fieldDisc').chosen({allow_single_deselect:true});
			}
		}
		</script>
</html>
