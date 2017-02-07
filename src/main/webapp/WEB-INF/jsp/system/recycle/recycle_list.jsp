<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<%@ taglib prefix="clob" uri="/clob" %>
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
						<form action="sys/listRecycle.do" method="post" name="RecycleForm" id="RecycleForm">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}"  type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastLoginEnd" id="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td>
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								<td style="vertical-align:top;padding-left:2px;">
									<ts:rights code="sys/excel">
										<a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a>
									</ts:rights>
								</td>
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
									<th class="center">用户名称</th>
									<th class="center">API类型</th>
									<th class="center">输入</th>
									<th class="center">输出</th>
									<th class="center">登录IP</th>
									<th class="center">设备编号</th>
									<th class="center"><i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>登录时间</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty RecyclList}">
									<c:forEach items="${RecyclList}" var="recycl" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' value="${recycl.LOG_ID }" class="ace"/><span class="lbl"></span></label>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${recycl.USERNAME}</td>
											<td class="center"><h data-toggle="tooltip" data-placement="top" title="${recycl.NAME}">${recycl.NAME}</h></td>
											<td class="center" style="width: 20px;height: 10px;" onclick="editUser('${recycl.LOG_ID}');"><clob:colb addContent="" n="15" clob="${recycl.INPUT}"/></td>
											<td class="center" onclick="editUser('${recycl.LOG_ID}');">
											<clob:colb addContent="" n="15" clob="${recycl.OUTPUT}"/>
											</td>
											<td class="center">${recycl.USER_IP }</td>
											<td class="center"><h data-toggle="tooltip" data-placement="top" title="${recycl.CODE}">${recycl.CODE}</h></td>
											<td class="center"><h data-toggle="tooltip" data-placement="top" title="${recycl.CALL_DATE}">${recycl.CALL_DATE}</h></td>
											<td class="center">
												<div class="btn-group">
													<ts:rights code="sys/goEditU">
														<a class="btn btn-xs btn-success" title="详情" onclick="editUser('${recycl.LOG_ID}');">
															<i class="ace-icon fa fa-pencil-square-o bigger-120" title="详情"></i>
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
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	</body>
 	<script type="text/javascript">
		$(top.hangge());
		
		$(function() {
			//提示框标签
			$("[data-toggle='tooltip']").tooltip();
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});			
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
		
		//检索
		function searchs(){
			top.jzts();
			$("#RecycleForm").submit();
		}
		//查看
		function editUser(log_id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="API日志详情";
			 diag.URL = '<%=path%>/sys/goEditU.do?LOG_ID='+log_id;
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
		//导出excel
		function toExcel(){
			var keywords = $("#nav-search-input").val();
			var lastLoginStart = $("#lastLoginStart").val();
			var lastLoginEnd = $("#lastLoginEnd").val();
			window.location.href='<%=basePath%>sys/excel.do?keywords='+keywords+'&lastLoginStart='+lastLoginStart+'&lastLoginEnd='+lastLoginEnd;
		}
		</script>
</html>
