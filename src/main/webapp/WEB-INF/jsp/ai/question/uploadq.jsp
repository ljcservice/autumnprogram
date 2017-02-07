<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>

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
							<form action="importLog/readFile.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
								<div id="zhongxin">
								<table style="width:95%;" >
									<tr>
										<td style="padding-top: 20px;"><input type="file" id="filename" name="filename" style="width:50px;" onchange="fileType(this)" /></td>
									</tr>
									<tr>
										<td style="text-align: center;padding-top: 10px;">
											<ts:rights code="importLog/readFile">
												<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
											</ts:rights>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											<ts:rights code="importLog/downModel">
											<a class="btn btn-mini btn-success" onclick="window.location.href='<%=basePath%>/importLog/downModel.do'">下载模版</a>
											</ts:rights>
										</td>
									</tr>
								</table>
								</div>
								<div id="zhongxin2" class="center" style="display:none"><br/><img src="static/images/jzx.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 上传控件 -->
	<script src="static/ace/js/ace/elements.fileinput.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		$(top.hangge());
		$(function() {
			//上传
			$('#filename').ace_file_input({
				no_file:'请选择文件 ...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'txt|csv',
				blacklist:'gif|png|jpg|jpeg'
				//onchange:''
			});
		});
		
		//保存
		function save(){
			if($("#filename").val()=="" || document.getElementById("filename").files[0] =='请选择txt格式的文件'){
				
				$("#filename").tips({
					side:3,
		            msg:'请选择文件',
		            bg:'#AE81FF',
		            time:3
		        });
				return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.txt'){
		    	$("#filename").tips({
					side:3,
		            msg:'请上传txt格式的文件',
		            bg:'#AE81FF',
		            time:3
		        });
		    	$("#filename").val('');
		    	document.getElementById("filename").files[0] = '请选择txt格式的文件';
		    }
		}
	</script>


</body>
</html>