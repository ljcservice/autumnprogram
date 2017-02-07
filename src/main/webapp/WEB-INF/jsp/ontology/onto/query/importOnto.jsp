<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<link type="text/css" rel="stylesheet" href="static/css/jquery-ui-1.8.16.custom.css"/>
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<style type="text/css">
body{background:#eee;font-family:Verdana, Helvetica, Arial, sans-serif;margin:0;padding:0}
.example{background:#FFF;width:650px;font-size:80%;border:1px #000 solid;margin:20px auto;padding:15px;position:relative;-moz-border-radius: 3px;-webkit-border-radius: 3px}
h3 {text-align:center}

.pbar .ui-progressbar-value {display:block !important}
.pbar {overflow: hidden}
.percent {position:relative;text-align: right;}
.elapsed {position:relative;text-align: right;}
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
								<div id="zhongxin">
								<form action="ontology/uploadOntology.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
								<table  style="width:95%;" >
									<tr >
										<td colspan="2" style="padding-top: 25px;">
										<input type="file" id="filename" name="filename" style="width:50px;" onchange="fileType(this)" />
										<span id="mytip"></span>
										</td>
									</tr>
									<tr >
										<td style="padding-top: 10px;" align="center" nowrap="nowrap">
											类型：
											<select class="" name="ontoType" id="ontoType">
												<c:forEach items="${typeMap.entrySet()}" var="ontoTyp" varStatus="vs">
													<option value="${ontoTyp.key}" <c:if test="${ontoTyp.key == ontoType }">selected</c:if>>${ontoTyp.value}</option>
												</c:forEach>
											</select>
										</td>
										<td style="padding-top: 10px;">
											需要审核： &nbsp;&nbsp;
												<label><input class="ace" id="u3897_input" value="0" name="IS_CHECK" type="radio" text="否"  checked="checked">否&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
												<label><input class="ace" id="u3897_input" value="1" name="IS_CHECK" type="radio" text="是"  >是&nbsp;<span class="lbl"></span></label>
										</td>
									</tr>
									<tr >
										<td colspan="2" style="padding-top: 10px;text-align: center;" align="center">
											<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											<a class="btn btn-mini btn-success" onclick="downTemplate();">下载模版</a>
										</td>
									</tr>
								</table>
								</form>
								</div>
								<div id="zhongxin2" class="center" style="display:none">
								
									<div id="progress2" style="padding-top: 25px;">
										<div class="percent"></div>
										<div class="pbar"></div>
										<div class="elapsed"></div>
									</div>
									
								</div>
								<div id="zhongxin3" class="center" style="display:none">
								
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
	<script type="text/javascript" src="static/js/progress/progress.js"></script>
	<script type="text/javascript" src="static/js/progress/jquery-ui-1.8.16.custom.min.js"></script>
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
				whitelist:'xls|xlsx',
				blacklist:'gif|png|jpg|jpeg|text'
				//onchange:''
			});
		});
		
		//保存
		function save(){
			if($("#filename").val()=="" || document.getElementById("filename").files[0] =='请选择xls或xlsx格式的文件'){
				$("#filename").eq(0).tips({
					side:2,
		            msg:'请选择文件',
		            bg:'#AE81FF',
		            time:3
		        });
				return false;
			}
			var iNow = new Date().setTime(new Date().getTime() + 1 * 1000); // now plus 2 secs
			var iEnd = new Date().setTime(new Date().getTime() + 8 * 1000); // now plus 15 secs
			$('#progress2').anim_progressbar({start: iNow, finish: iEnd, interval: 100});
			$("#zhongxin").hide();
			$("#zhongxin2").show();
			$("#Form").submit();
		}
		
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.xls' && fileType != '.xlsx'){
		    	$("#filename").tips({
					side:2,
		            msg:'请选择xls或xlsx格式的文件',
		            bg:'#AE81FF',
		            time:3
		        });
		    	$("#filename").val('');
		    	document.getElementById("filename").files[0] = '请选择xls或xlsx格式的文件';
		    }
		}
		function downTemplate(){
			window.location.href='${basePath}/ontology/downTemplate.do?ontoType='+$("#ontoType").val();
		}
	</script>

</body>
</html>