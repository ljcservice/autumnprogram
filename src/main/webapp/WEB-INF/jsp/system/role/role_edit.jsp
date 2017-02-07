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

</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
				
					<div class="page-header">
							<h1>
								<small>
									<i class="ace-icon fa fa-angle-double-right"></i>
									编辑角色
								</small>
							</h1>
					</div><!-- /.page-header -->

					<div class="row">
						<div class="col-xs-12">

						<form  action="role/${MSG }.do" name="roleForm" id="roleForm" method="post" class="form-horizontal">
							<input type="hidden" name="ROLE_ID" id="ROLE_ID" value="${pd.ROLE_ID }"/>
							<input type="hidden" name="PARENT_ID" id="PARENT_ID" value="${null == pd.PARENT_ID ? ROLE_ID:pd.PARENT_ID}"/>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 上级角色 :</label>
								<div class="col-sm-9">
									<div style="padding-top:5px;">
										<div class="col-xs-4 label label-lg label-light arrowed-in arrowed-right">
											<b>${null == pds.ROLE_NAME ?'(无) 此项为顶级角色':pds.ROLE_NAME}</b>
										</div>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 角色名称 :</label>
								<div class="col-sm-9">
									<input type="text" name="ROLE_NAME" id="roleName" value="${pd.ROLE_NAME }" placeholder="这里输入角色名称" class="col-xs-10 col-sm-5" maxlength="80"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 角色描述  :</label>
								<div class="col-sm-9">
									<input type="text" name="INTRODUCTION" id="introduction" value="${pd.INTRODUCTION }" placeholder="这里输入角色描述" class="col-xs-10 col-sm-5" maxlength="300"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 角色身份 : </label>
								<div class="col-sm-9">
								 	<select class="chosen-select form-control" name="ROLE_TYPE" id="roleType" style="vertical-align:top;width: 41.66%;">
										<c:if test="${ROLE_TYPE ==0}" ><option value="0" <c:if test="${pd.ROLE_TYPE==0}">selected</c:if>>管理角色</option></c:if>
										<c:if test="${ROLE_TYPE <=1}" ><option value="1" <c:if test="${pd.ROLE_TYPE==1}">selected</c:if>>授权角色</option></c:if>
										<option value="2" <c:if test="${pd.ROLE_TYPE!=0 && pd.ROLE_TYPE!=1}">selected</c:if>>业务角色</option>
								  	</select>
								</div>
							</div>
							

							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<c:if test="${MSG =='add'}">
										<ts:rights code="role/add"><a class="btn btn-mini btn-primary" onclick="save();">保存</a></ts:rights>
									</c:if>
									<c:if test="${MSG =='edit'}">
										<ts:rights code="role/edit"><a class="btn btn-mini btn-primary" onclick="save();">保存</a></ts:rights>
									</c:if>
									<a class="btn btn-mini btn-danger" onclick="goback('${ROLE_ID}');">取消</a>
								</div>
							</div>
							<div class="hr hr-18 dotted hr-double"></div>
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
		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!-- inline scripts related to this page -->
	<script type="text/javascript">
		$(top.hangge());
		
		//返回
		function goback(ROLE_ID){
			top.jzts();
			window.location.href="<%=basePath%>role/list.do?ROLE_ID="+ROLE_ID;
		}
		
		//保存
		function save(){
			if($("#roleName").val()==""){
				$("#roleName").tips({
					side:3,
		            msg:'请输入角色名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#roleName").focus();
				return false;
			}

			$("#roleForm").submit();
		}

	</script>


</body>
</html>