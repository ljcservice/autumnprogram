<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
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
						<form action="osyn/osynStand.do" method="post">
						<div id="zhongxin" style="padding-top: 13px;">
							<input type="hidden" name="osynType" value="${osynType}" id="osynType"/>
							<table style="margin-top:5px;">
								<tr>
									<td>
										<div class="nav-search">
										术语名称：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="术语名称 " />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										</div>
									</td>
									<td style="padding-left:10px;" nowrap>
										更新日期：
										<input class="span10 date-picker" name="UPDATE_TIME" id="UPDATE_TIME"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="最近登录开始"/>
									</td>
									<td style="vertical-align:top;padding-left:10px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
									<td style="vertical-align:top;padding-left:15px;">
										<a class="btn btn-mini btn-primary" onclick="selectCont();">确定</a>
										<a class="btn btn-mini btn-danger" style="margin-left:10px;" onclick="top.Dialog.close();">取消</a>
									</td>
								</tr>
							</table>
						</div>
						<div >
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"></label>
									</th>
									<th class="center" nowrap>术语名称中文</th>
									<th class="center" nowrap>术语名称英文</th>
									<th class="center" nowrap>来源名称中文</th>
									<th class="center" nowrap>来源名称英文</th>
									<th class="center" nowrap>更新时间</th>
								</tr>
							</thead>
													
							<tbody id="standTbody">
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
												
										<tr onclick="clickTr(this);">
											<td class='center' style="width: 30px;">
												<label><input type="radio" class="ace" name='standIs' value="${diag.ID}" CN="${diag.CN}" EN="${diag.EN}" O_CN="${diag.ORG_CN}" O_EN="${diag.ORG_EN}"/><span class="lbl"></span></label>
											</td>
											<td class="center">${diag.CN}</td>
											<td class="center">${diag.EN}</td>
											<td class="center">${diag.ORG_CN}</td>
											<td class="center">${diag.ORG_EN}</td>
											<td class="center"><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="11" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</div>
						<div class="page-header position-relative" id="osynPageParam">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
		
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
						</form>
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
function clickTr(obj){
	$("input[name='standIs']").prop("checked",false);
	$(obj).find("input[name='standIs']").eq(0).prop("checked",true);
}
function selectCont(){
	var osyn = $("input[name='standIs']:checked");
	if(osyn.val()==undefined || osyn.val()==""){
		return;
	}
	var mydocument = parent.$("#_DialogFrame_0")[0].contentWindow.document;
	
	mydocument.getElementById("DN_ID").value = osyn.val();
	mydocument.getElementById("SHOW_NAME").value = osyn.attr("CN");
	mydocument.getElementById("STAD_DN_CHN").value = osyn.attr("CN");
	mydocument.getElementById("STAD_DN_ENG").value = osyn.attr("EN");
	mydocument.getElementById("ORG_STAD_DN_CHN").value = osyn.attr("O_CN");
	mydocument.getElementById("ORG_STAD_DN_ENG").value = osyn.attr("O_EN");
	top.Dialog.close();
}
</script>
</html>
