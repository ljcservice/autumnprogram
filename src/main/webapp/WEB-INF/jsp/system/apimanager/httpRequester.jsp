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
					<div class="row">
						<div class="col-xs-12">
					 	<div class="span12">
							<div class="widget-box">
								<div class="widget-header widget-header-blue widget-header-flat wi1dget-header-large">
									<h4 class="lighter">API接口测试</h4>
								</div>
					 		<form id="httpForm" name="httpForm" action="http/https.do" method="post">
								<div class="widget-body">
								 <div class="widget-main">
					 				 <div style="margin-bottom: 20px;" class="form-group">
					 				 	
										<div style="padding-left: 0px; padding-right: 0px;"  class="col-sm-6">
											<input type="text" autocomplete="off" placeholder="请求地址，以   http:// 或 https:// 开头" value="${pd.url}"  id="url" name="url" class="showBox form-control" />
										</div>
										<br><br>
							   			<div style="padding-left: 0px; padding-right: 0px;" class="col-sm-1">
								
										    <select id="type" name="type" class="form-control">
												<option value="Post">Post</option>
												<option value="Get">Get</option>
										    </select>
								      	</div>
					   					<div style="padding-left: 0px; padding-right: 0px;" class="col-sm-1">
										    <select id="charset" name="charset" class="form-control">
												<option value="UTF-8">UTF-8</option>
												<option value="GBK">GBK</option>
												<option value="GB2312">GB2312</option>
												<option value="GB18030">GB18030</option>
										    </select>
									    </div>
										<div style="padding-left: 0px; padding-right: 15px;"  class="col-sm-1">
										    <select id="Ctype" name="Ctype" class="form-control" >
												<option value="application/json">Json</option>
												<option value="application/xml">XML</option>
										    </select>
										</div>
										<div  style="padding-left: 0px; padding-right: 0px;" class="col-sm-4">
										<ts:rights code="http/https">
											<a class="btn btn-sm btn-success" onclick="searchs();">请求</a>&nbsp;
										</ts:rights>
											<a class="btn btn-sm btn-info" onclick="gReload();">重置</a>
										</div>
					  				</div>
									
	
					  				<div>
					  				<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">
										<thead>
											<tr>
												<th >请求参数</th>
												<th >返回结果</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="center" style="height: 200px;">
													<textarea id="json-cans" name="keys" value="${pd.keys}" title="请输入参数,Json格式" class="autosize-transition span12"  style="width: 530px;height: 195px; max-width: 530px; max-height: 195px; resize: none;">${pd.keys}</textarea>
												</td>
												<td class="center" style="height: 200px;">
													<textarea id="json-field" name="values" value="${pd.values}"  title="返回结果" class="autosize-transition span12" style="width: 530px;height: 195px; max-width: 530px; max-height: 195px; resize: none;">${pd.values}</textarea>
												</td>
											</tr>
										</tbody>
										<tfoot>
											<tr>
												<th colspan="2">状态信息</th>
											</tr>
											<tr>
												<td colspan="2" style="height: 80px;">
													<textarea id="json-type" title="状态信息" class="autosize-transition span12" style="width: 100%;height: 100%; resize: none;">${pd.response }</textarea>
												</td>
											</tr>
										</tfoot>
									</table>
									</div>
										<div class="step-content row-fluid position-relative">
											<ul class="unstyled spaced2">
												<li class="text-warning orange"><i class="ace-icon fa fa-exclamation-triangle"></i>
													相关参数协议：Status: 1 请求成功; -1  非法身份; -2 请求超时; 100 API类型错误; -111 无权访问; -222	参数错误 ; 
												</li>
												<li class="text-warning orange"><i class="ace-icon fa fa-exclamation-triangle"></i>
													Status: 999 非法使用; 400  页面丢失; 500 服务器内部错误; -1000 错误码; 200 访问成功;  
												</li>
											</ul>
										</div>
									 <input type="hidden" name="S_TYPE" id="S_TYPE" value="POST"/>
								 </div><!--/widget-main-->
								</div><!--/widget-body-->
							</form>
							</div>
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
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!--MD5-->
	<script type="text/javascript" src="static/js/jQuery.md5.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!--引入属于此页面的js-->
	<script type="text/javascript" src="static/js/myjs/httpRequester.js"></script>
	<script type="text/javascript">
		$(top.hangge());
		function searchs(){
			top.jzts();
			$("#httpForm").submit(); 
		}
	</script>
</body>
</html>