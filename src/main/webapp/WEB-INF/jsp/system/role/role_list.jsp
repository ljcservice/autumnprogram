<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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

<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
	<script type="text/javascript">
		//刷新ztree
		function parentReload(){
			if(null != '${MSG}' && 'change' == '${MSG}'){
				parent.location.href="<%=basePath%>role/listAllRole.do?ROLE_ID="+"${ROLE_ID}";
			}else{
				//什么也不干
			}
		}
		parentReload();
	</script>
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

							<table id="dynamic-table" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
<!-- 										<th class="center" style="width: 50px;">序号</th> -->
										<th class='center'>角色名称</th>
										<th class='center'>上级角色</th>
										<th class='center' >角色描述</th>
										<th class='center' style="width: 120px;">角色身份</th>
										<th class='center'>操作</th>
									</tr>
								</thead>

								<tbody>
								<c:choose>
									<c:when test="${not empty roleList}">
									<c:forEach items="${roleList}" var="role" varStatus="vs">
									<tr>
<!-- 										<td class='center'>${vs.index+1}</td> -->
										<td class='center'>
											<a href="javascript:goSonrole('${role.ROLE_ID }')">${role.ROLE_NAME }</a>
										</td>
										<td>${pd.ROLE_NAME}</td>
										<td>${role.INTRODUCTION}</td>
										<td class='center'>
											<c:if test="${role.ROLE_TYPE==0}">管理角色</c:if>
											<c:if test="${role.ROLE_TYPE==1}">授权角色</c:if>
											<c:if test="${role.ROLE_TYPE==2}">业务角色</c:if>
										</td>
										<td class='center'>
											<div class="action-buttons">
												<ts:rights code="role/menuFun">
													<a class="btn btn-mini btn-purple" onclick="editRights('${role.ROLE_ID }');"><i class="icon-pencil"></i>功能权限</a>
												</ts:rights>
												<ts:rights code="role/toEdit">
													<a class="green" href="javascript:editrole('${role.ROLE_ID }');">
													<i class="ace-icon fa fa-pencil-square-o bigger-130" title="修改"></i>
													</a>
												</ts:rights>
												<ts:rights code="role/delete">
													<a class="red" href="javascript:delrole('${role.ROLE_ID }');">
														<i class="ace-icon fa fa-trash-o bigger-130" title="删除"></i>
													</a>
												</ts:rights>
											</div>
											
										</td>
									</tr>
									</c:forEach>
									</c:when>
										<c:otherwise>
											<tr>
											<td colspan="100" class='center'>没有相关数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							
							<div>
								&nbsp;&nbsp;
								<ts:rights code="role/toAdd"><c:if test="${HASROLE=='1' || ROLE_TYPE==0}"> <a class="btn btn-sm btn-success" onclick="addrole('${ROLE_ID}');">新增</a></c:if></ts:rights>
								<c:if test="${null != pd.ROLE_ID && pd.ROLE_ID != '0' && pd.PARENT_ID!='0'}">
									<a class="btn btn-sm btn-success" onclick="goSonrole('${pd.PARENT_ID}');">返回</a>
								</c:if>
								<c:if test="${pd.PARENT_ID=='0' && ROLE_TYPE==0}">
									<a class="btn btn-sm btn-success" onclick="goSonrole('${pd.PARENT_ID}');">返回</a>
								</c:if>
							</div>
							
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
		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
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

	<!-- inline scripts related to this page -->
	<script type="text/javascript">
		$(document).ready(function(){
			top.hangge();
		});	
		
		//去此ID下子菜单列表
		function goSonrole(ROLE_ID){
			top.jzts();
			window.location.href="<%=basePath%>role/list.do?ROLE_ID="+ROLE_ID;
		};
		
		//新增菜单
		function addrole(ROLE_ID){
			top.jzts();
			window.location.href="<%=basePath%>role/toAdd.do?ROLE_ID="+ROLE_ID;
		};
		
		//编辑菜单
		function editrole(ROLE_ID){
			top.jzts();
			window.location.href="<%=basePath%>role/toEdit.do?ROLE_ID="+ROLE_ID;
		};
		
		//删除
		function delrole(ROLE_ID){
			var url = "<%=basePath%>role/checkRoleUsed.do?ROLE_ID="+ROLE_ID+"&guid="+new Date().getTime();
			$.get(url,function(data){
				var tip = "";
				if("success" == data.result){
					tip ="确定要删除此菜单吗?";
				}else{
					tip = data.result;
				}
				bootbox.confirm(tip, function(result) {
					if(result) {
						top.jzts();
						url = "<%=basePath%>role/delete.do?ROLE_ID="+ROLE_ID+"&guid="+new Date().getTime();
						$.get(url,function(data){
							if("success" == data.result){
								parent.location.href="<%=basePath%>role/listAllRole.do?ROLE_ID="+"${ROLE_ID}";
							}else if("false" == data.result){
								top.hangge();
								bootbox.dialog({
									message: "<span class='bigger-110'>删除失败,存在用户使用该角色!</span>",
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
		
			//菜单权限
		function editRights(ROLE_ID){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag = true;
			 diag.Title = "功能权限设置";
			 diag.URL = '<%=path%>/role/menuFun.do?ROLE_ID='+ROLE_ID;
			 diag.Width = 650;
			 diag.Height = 350;
			 diag.CancelEvent = function(){ //关闭事件
				diag.close();
			 };
			 diag.show();
		}
		
	</script>


</body>
</html>