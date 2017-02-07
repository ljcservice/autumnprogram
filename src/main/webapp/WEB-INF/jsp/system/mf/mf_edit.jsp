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
									编辑功能权限
								</small>
							</h1>
					</div><!-- /.page-header -->

					<div class="row">
						<div class="col-xs-12">

						<form  action="mf/${MSG }.do" name="menuForm" id="menuForm" method="post" class="form-horizontal">
							<input type="hidden" name="MENU_ID" id="MENU_ID" value="${MENU_ID }"/>
							<input type="hidden" name="MF_ID" id="MF_ID" value="${pd.MF_ID }"/>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 菜单 :</label>
								<div class="col-sm-9">
									<div style="padding-top:5px;">
										<div class="col-xs-4 label label-lg label-light arrowed-in arrowed-right">
											<b>${pd.MENU_NAME}</b>
										</div>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 功能code :</label>
								<div class="col-sm-9">
									<input type="text" name="FUN_CODE" id="funcode" value="${pd.FUN_CODE }" placeholder="这里输入功能code" class="col-xs-10 col-sm-5" maxlength="200"/>
									<lable style="color: red;">&nbsp; * </lable>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 说明  :</label>
								<div class="col-sm-9">
									<input type="text" name="INTRODUCTION" id="introduction" value="${pd.INTRODUCTION }" placeholder="这里输入功能说明" class="col-xs-10 col-sm-5" maxlength="80"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 备注 : </label>
								<div class="col-sm-9">
									<input type="text" name="REMARK" id="remark" value="${pd.REMARK}" placeholder="这里输入功能备注" title="" class="col-xs-10 col-sm-5" maxlength="300"/>
								</div>
							</div>
												
						
							<div class="clearfix form-actions">
								<div class="col-md-offset-3 col-md-9">
									<c:if test="${MSG =='add'}">
										<ts:rights code="mf/add"><a class="btn btn-mini btn-primary" onclick="save();">保存</a></ts:rights>
									</c:if>
									<c:if test="${MSG =='edit'}">
										<ts:rights code="mf/edit"><a class="btn btn-mini btn-primary" onclick="save();">保存</a></ts:rights>
									</c:if>
									<a class="btn btn-mini btn-danger" onclick="goback('${MENU_ID}');">取消</a>
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
		function goback(MENU_ID){
			top.jzts();
			window.location.href="<%=basePath%>mf/list.do?MENU_ID="+MENU_ID;
		}
		
		//保存
		function save(){
			if($("#funcode").val()==""){
				$("#funcode").tips({
					side:3,
		            msg:'请输入功能code！',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#funcode").focus();
				return false;
			}
			if(checkCode()){
				$("#menuForm").submit();
			}
		}
		function checkCode(){
			var flag = true;
			$.ajax({
				type: "POST",
				url: basePath+'/mf/checkCode.do',
		    	data: {MF_ID:$("#MF_ID").val(),FUN_CODE:$("#funcode").val()},
				dataType:'json',
				async:false,
				cache: false,
				success: function(data){
					if(data.result=="success"){
					
					}else{
						flag = false;
						$("#funcode").tips({
							side:3,
				            msg:'功能code已经存在！',
				            bg:'#AE81FF',
				            time:2
				        });
					}
				}
			});
			return flag;
		}
	</script>
</body>
</html>