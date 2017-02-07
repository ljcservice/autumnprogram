<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights" %>
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
<body style="background-color:white;">
	<div class="panel-group" id="accordion">
		<!-- 检索  -->
		<form action="api/roleList.do" method="post" name="ypForm" id="ypForm">
		<input type="hidden" value="${ROLE_ID}" name="ROLE_ID" id="ROLE_ID" />
		<input type="hidden" value="${result}" name="result" id="result" />
			<table style="margin-top:5px;">
				<tr>
					<td style="vertical-align:top;padding-left:2px;"><select
						class="chosen-select form-control" name="businessName"
						id="businessName" data-placeholder="业务名称"
						style="vertical-align:top;width: 200px;"
						onchange="businessNameChange()">
							<option value=""></option>
							<option value="">全部</option>
							<c:choose>
								<c:when test="${not empty interfaceList}">
									<c:forEach items="${interfaceList}" var="interfaceM" varStatus="vs">
										<option value="${interfaceM.Type }"
											<c:if test="${pd.businessName == interfaceM.Type  }">selected</c:if>>${interfaceM.NAME }</option>
									</c:forEach>
								</c:when>
							</c:choose>
					</select></td>
					<td style="vertical-align:top;padding-left:2px; display:none;" id="select1">
						<select class="chosen-select form-control" name="fieldDisc" id="fieldDisc" data-placeholder="列说明" style="vertical-align:top;width: 200px;">
					</select>
					</td>
					<td style="vertical-align:top;padding-left:2px;">
						<a class="btn btn-light btn-xs" onclick="searchsYP();" title="检索">
							<i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i>
						</a>
					</td>

				</tr>
			</table>
			<!-- 开始循环 -->
			<c:choose>
				<c:when test="${not empty zTree}">
					<c:forEach items="${zTree}" var="zTree" varStatus="vs">

						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" onclick="collapseOne(this,${vs.index+1});" chek="0">
										${zTree.NAME}
									</a>
								</h4>
							</div>
							<div id="collapseOne_${vs.index+1}"
								class="panel-collapse collapse">
								<div class="panel-body">
									<table id="simple-table"
										class="table table-striped table-bordered table-hover ischek"
										style="margin-top:5px;">
										<thead>
											<tr>
												<th class="center" style="width:35px;">
													<ts:rights code="api/addDeleteAllR">
														<label class="pos-rel"> 
															<input type="checkbox" class="ace chekid" id="tips" /> <span class="lbl"></span>
														</label>
														<ts:norights>
															<label class="pos-rel"> 
																<input type="checkbox" class="ace chekid" id="tips" /> <span class="lbl"></span>
															</label>
														</ts:norights>
													</ts:rights>
												</th>
												<th class="center" style="width:50px;">序号</th>
<!-- 												<th class="center">业务名称</th> -->
												<th class="center">操作表名称</th>
												<th class="center">表说明</th>
												<th class="center">字段名称</th>
												<th class="center">列说明</th>
												<th class="center">数据权限</th>
												<th class="center">是否启用</th>
											</tr>
										</thead>
										<tbody>
											<!-- 开始循环 -->
											<c:choose>
												<c:when test="${not empty resultMap}">
													<c:forEach items="${resultMap.get(zTree.IN_ID)}"
														var="access" varStatus="vs">
														<tr>
															<td class='center' style="width: 30px;">
																<ts:rights code="api/saveDelete">
																	<label>
																		<input type='checkbox' name='ids' value="${access.DR_ID }" id="DR_ID" class="ace" onclick="checkboxOnclick(this,'${access.DR_ID }')"
																		<c:if test="${access.flag==1}">checked</c:if> /> <span class="lbl"></span>
																	</label>
																	<ts:norights>
																		<label>
																			<input type='checkbox' name='ids' value="${access.DR_ID }" id="DR_ID" class="ace" onclick="checkboxOnclick(this,'${access.DR_ID }')"
																			<c:if test="${access.flag==1}">checked</c:if> /> <span class="lbl"></span>
																		</label>
																	</ts:norights>
																</ts:rights>
															</td>
															<td class='center' style="width: 30px;">${vs.index+1}</td>
<%-- 															<td class="center" onclick="dataRule(this,'${access.DR_ID}','${access.isVal }');" >${access.NAME }</td> --%>
															<ts:rights code="roleCol/goRoleCol">
																<td class="center" id="TABLE_NAME" onclick="dataRule(this,'${access.DR_ID}','${access.isVal }');">${access.TABLE_NAME }</td>
																<td class="center" onclick="dataRule(this,'${access.DR_ID}','${access.isVal }');" >${access.TABLE_DISC }</td>
																<td class="center" onclick="dataRule(this,'${access.DR_ID}','${access.isVal }');" >${access.COLUMN_NAME}</td>
																<td class="center" onclick="dataRule(this,'${access.DR_ID}','${access.isVal }');" >${access.COLUMN_DISC}</td>
																<td class="center" onclick="dataRule(this,'${access.DR_ID}','${access.isVal }');" >
																	<c:if test="${access.isVal == '1' }"><span class="label label-success arrowed">有</span></c:if>
																	<c:if test="${access.isVal == '0' }"><span class="label label-important arrowed-in">无</span></c:if>															
																</td>
																<td class="center" onclick="dataRule(this,'${access.DR_ID}','${access.isVal }');" >
																	<c:if test="${access.isCol == '1' }"><span class="label label-success arrowed">已启用</span></c:if>
																	<c:if test="${access.isCol == '0' }"><span class="label label-important arrowed-in">未启用</span></c:if>															
																</td>
																<ts:norights>
																	<td class="center" id="TABLE_NAME">${access.TABLE_NAME }</td>
																	<td class="center"  >${access.TABLE_DISC }</td>
																	<td class="center"  >${access.COLUMN_NAME}</td>
																	<td class="center"  >${access.COLUMN_DISC}</td>
																	<td class="center"  >
																		<c:if test="${access.isVal == '1' }"><span class="label label-success arrowed">有</span></c:if>
																		<c:if test="${access.isVal == '0' }"><span class="label label-important arrowed-in">无</span></c:if>															
																	</td>
																	<td class="center"  >
																		<c:if test="${access.isCol == '1' }"><span class="label label-success arrowed">已启用</span></c:if>
																		<c:if test="${access.isCol == '0' }"><span class="label label-important arrowed-in">未启用</span></c:if>															
																	</td>
																
																</ts:norights>
															</ts:rights>
														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr class="main_info">
														<td colspan="100" class="center">没有相关数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:when>
			</c:choose>
			</tbody>
			</table>
		</form>
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
 			$('.chekid').on('click', function() {
 				var th_checked = this.checked; //checkbox inside "TH" table header
 				$(this).closest('table').find('tbody > tr').each(function() {
 					var row = this;
 					if (th_checked) {
 						$(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
 	
 					} else {
 						$(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
 					}
 				});
 	
 	
 				var inputs = $(this).closest('table').find('input[name=ids]');
 				var ids = ""
 				for (var i = 0; i < inputs.length; i++) {
 					if (ids != "") {
 						ids += "," + inputs.eq(i).val();
 					} else {
 						ids = inputs.eq(i).val();
 					}
 				}
 				var ROLE_ID = document.getElementById("ROLE_ID").value;
 				var temp = this;
 				if (th_checked) {
 					$.ajax({
 						type : "POST",
 						url : '<%=basePath%>api/addAllR.do',
 						data : {
 							ROLE_ID : ROLE_ID,
 							DR_ID : ids
 						},
 						success : function(data) {
//  							alert(data.result);
 							$(temp).tips({
 								side : 2,
 								msg : data.result,
 								bg : '#307FC1',
 								time : 1
 							});
 						},
 						error:function(data){
 						 	$(temp).tips({
 								side : 2,
 								msg : '批量添加失败！',
 								bg : '#307FC1',
 								time : 1
 							});
 						}
 					});
 				} else {
 					$.ajax({
 						type : "POST",
 						url : '<%=basePath%>api/deleteAllR.do',
 						data : {
 							ROLE_ID : ROLE_ID,
 							DR_ID : ids
 						},
 						success : function(data) {
//  							alert(data.result);
 							$(temp).tips({
 								side : 2,
 								msg : data.result,
 								bg : '#307FC1',
 								time : 1
 							});
 						},
 						error:function(data){
 						 	$(temp).tips({
 								side : 2,
 								msg : '批量删除失败！',
 								bg : '#307FC1',
 								time : 1
 							});
 						}
 					});
 				}
 	
 			});
 	
 			$('.ischek').each(function() {
 				var chekid = true;
 	
 				$(this).find('tbody > tr').each(function() {
 					if (!$(this).find('input[type=checkbox]').eq(0).prop('checked')) {
 						chekid = false;
 					}
 				});
 	
 				if (chekid) {
 					$(this).find('thead > tr input[type=checkbox]').eq(0).prop('checked', true);
 				}
 			});
 	
 			//下拉框
 			if (!ace.vars['touch']) {
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
		//数据规则管理点击事件
		function dataRule(ifs,dr_id,isVal){
			 var ischek  = $(ifs).closest('tr').find('input[type=checkbox]').eq(0).prop('checked');
			 if(!ischek){
				$(ifs).tips({
					side:1,
		            msg:'该字段无访问权限！',
		            bg:'#307FC1',
		            time:1
	        	});
				return ;
			 }
			 if(isVal == "0"){
			 	$(ifs).tips({
					side:1,
		            msg:'该字段为最大权限！',
		            bg:'#307FC1',
		            time:1,
		            y:1
	        	});
				return ;
			 }
			 var ROLE_ID = document.getElementById("ROLE_ID").value;	
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="数据权限";
			 diag.URL = '<%=path%>/roleCol/goRoleCol.do?DR_ID='+dr_id+'&ROLE_ID='+ROLE_ID; 
			 diag.Width = 800;
			 diag.Height = 500;
			 diag.CancelEvent = function(){ //关闭事件
				diag.close();
			 };
			 diag.show();
		
		}

		
		//树点击事件
		function collapseOne(obj,index){
			if($(obj).attr("chek")==0){
				$("#collapseOne_"+index).addClass("in");
				$(obj).attr("chek",1);
			}else{
				$(obj).attr("chek",0);
				$("#collapseOne_"+index).removeClass("in");
			}
		}
		//chekbox点击事件
		function checkboxOnclick(obj,DR_ID){
			var ROLE_ID = document.getElementById("ROLE_ID").value;
			if ( $(obj).prop("checked") == true){
				$.ajax({
				type:"POST",
				url:'<%=basePath%>api/save.do',
				data:{
					ROLE_ID:ROLE_ID,
					DR_ID:DR_ID
				},
				success: function(data){
					$(obj).tips({
						side:2,
			            msg:data.result,
			            bg:'#307FC1',
			            time:1
		        	});
				},
 				error:function(data){
 					$(obj).tips({
 						side : 2,
 						msg : '添加失败！',
 						bg : '#307FC1',
 						time : 1
 						});
 					}				
				});
				
			}else{
				$.ajax({
				type:"POST",
				url:'<%=basePath%>api/delete.do',
				data:{
					ROLE_ID:ROLE_ID,
					DR_ID:DR_ID
				},
				success: function(data){
					$(obj).tips({
						side:2,
			            msg:data.result,
			            bg:'#307FC1',
			            time:1
		        	});
				},
 				error:function(data){
 					$(obj).tips({
 						side : 2,
 						msg : '刪除失败！',
 						bg : '#307FC1',
 						time : 1
 					});
 					}
				});
			}
		}
		
		//检索
		function searchsYP(){
			top.jzts();
			$("#ypForm").submit();
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
