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
<%@ include file="../../index/top.jsp"%>
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
						<form action="roleCol/goRoleCol.do" method="post" name="collForm" id="collForm">
							<input type="hidden" value="${pd.DR_ID}" name="dr_id" id="dr_id"/>
							<input type="hidden" value="${pd.ROLE_ID}" name="role_id" id="role_id"/>
							
						<!-- 检索  -->
						<table id="simple-table" class="table table-striped table-bordered table-hover ischek" style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<ts:rights code="roleCol/editAllR">
									<label class="pos-rel"><input type="checkbox" class="ace chekid" /><span class="lbl"></span></label>
									</ts:rights>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">字段名称</th>
									<th class="center">字段说明</th>
									<th class="center">规则名称</th>
									<th class="center">备注</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty colList}">
									<c:forEach items="${colList}" var="col" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
											<ts:rights code="roleCol/updata">
												<label><input type='checkbox' name='ids' value="${col.ID}" id="RULE_NAME" class="ace" onclick="checkboxOnclick(this,'${col.ID}')" <c:if test="${col.flag==1}">checked</c:if>/><span class="lbl"></span></label>
											</ts:rights>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${col.COLUMN_NAME }</td>
											<td class="center">${col.COLUMN_DISC}</td>
											<td class="center">${col.RULE_NAME}</td>
											<td class="center">${col.REMARK}</td>
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
	<%@ include file="../../index/foot.jsp"%>
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
			$('.chekid').on('click', function(){
				var th_checked = this.checked;//checkbox inside "TH" table header
				$(this).closest('table').find('tbody > tr').each(function(){
					var row = this;
					if(th_checked){
						 $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
					 
					 }else{
					 	 $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
					  }
				});
 
				
				var inputs = $(this).closest('table').find('input[name=ids]');
				var ids = "";
				for(var i=0;i < inputs.length;i++)
				{
					if(ids != ""){
						ids += ";" + inputs.eq(i).val();
					}else{
					ids = inputs.eq(i).val();
					}
				}
				var ROLE_ID = $("#role_id").val();
				var DR_ID = $("#dr_id").val();
					if(th_checked){
							$(this).tips({
								side:2,
					            msg:'批量新增成功！',
					            bg:'#307FC1',
					            time:2
	        					});
							$.ajax({
								type:"POST",
								url:'<%=basePath%>roleCol/editAllR.do',
								data:{
									ROLE_ID:ROLE_ID,
									DR_ID:DR_ID,
									COL_RULE:ids
								},
								success: function(data){
										}
								});
						 }else{
							$(this).tips({
								side:2,
					            msg:'批量删除成功！',
					            bg:'#307FC1',
					            time:2
	        					});
							 $.ajax({
								type:"POST",
								url:'<%=basePath%>roleCol/editAllR.do',
								data:{
									ROLE_ID:ROLE_ID,
									DR_ID:DR_ID,
									COL_RULE:""
								},
								success: function(data){
										}
								});
						  }

			});
			
			$('.ischek').each(function(){
				var chekid = true;	
				
				$(this).find('tbody > tr').each(function(){
				    if(! $(this).find('input[type=checkbox]').eq(0).prop('checked')){
				        chekid = false;
				    }
				});
				
				if(chekid){
					$(this).find('thead > tr input[type=checkbox]').eq(0).prop('checked',true);
				}
			});
			});
			//chekbox点击事件
		function checkboxOnclick(obj,ID){
			var DR_ID = $("#dr_id").val();
			var ROLE_ID = $("#role_id").val();
			if ( $(obj).prop("checked") == true){
				$.ajax({
					type:"POST",
					url:'<%=basePath%>roleCol/updata.do',
					data:{
						DR_ID:DR_ID,
						ROLE_ID:ROLE_ID,
						COL_RULE:ID
					},
					success: function(data){
// 					alert(data.result);
					}
				});
						$(obj).tips({
							side:2,
				            msg:'添加成功！',
				            bg:'#307FC1',
				            time:1
			        	});
			}else{
				$.ajax({
					type:"POST",
					url:'<%=basePath%>roleCol/updata.do',
					data:{
						DR_ID:DR_ID,
						ROLE_ID:ROLE_ID,
						COL_RULE:ID
					},
					success: function(data){
					}					
				});	
						$(obj).tips({
							side:2,
				            msg:'删除成功',
				            bg:'#307FC1',
				            time:1
			        	});			
			}
		}
				
		</script>
</html>
