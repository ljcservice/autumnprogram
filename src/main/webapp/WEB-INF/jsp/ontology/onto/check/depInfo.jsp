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
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<style type="text/css">
.changeTd{
	background-color:#ffb752;
}
.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
	padding: 4px;
}
.form-control{
	height: 30px;
	padding: 1px;
}
select.form-control {
	padding: 1px;
}
input[type="text"]{
	padding: 2px 2px 2px;
}
span.edit {
  float:right;
  background-attachment: scroll;
  background-color: transparent;
  background-image: url("static/images/zTreeStandard.png");
  background-position: -112px -49px;
  background-repeat: no-repeat;
  border: 0 none;
  cursor: pointer;
  display: inline-block;
  height: 16px;
  line-height: 0;
  margin: 0;
  outline: medium none;
  vertical-align: middle;
  width: 16px;
}
.memo{
	max-width: 200px;
	word-wrap: break-word; word-break: normal;
}
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
						<form action="" id="checkForm">
						<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
						<input type="hidden" name="H_ID" value="${newOnto.H_ID}" id="H_ID"/>
						<input type="hidden" name="D_ID" value="${newOnto.D_ID}" id="D_ID"/>
						<input type="hidden" name="DN_ID" value="${newOnto.DN_ID}" id="DN_ID"/>
						<input type="hidden" name="refreshFlag" value="${refreshFlag}" id="refreshFlag"/>
						<div id="zhongxin" class="col-xs-12">
						<div style="overflow: auto;height: auto;">
						<div >
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
									<td align="left"><b>
										<c:if test="${newOnto.OP_TYPE ==0}">新增本体</c:if>
										<c:if test="${newOnto.OP_TYPE ==1}">修改本体</c:if>
										<c:if test="${newOnto.OP_TYPE ==2}">修改父节点</c:if>
										<c:if test="${newOnto.OP_TYPE ==3}">术语知识编辑</c:if>
										<c:if test="${newOnto.OP_TYPE ==4}">停用术语</c:if>
										：</b>
									</td><td align="center">原数据</td><td align="center">新数据</td>
									</tr>
								<tbody align="center" id="myTbody">
								<tr>
									<td align="right" width="24%">科室名称中文:</td><td width="38%">${oldOnto.STAD_DN_CHN}</td><td width="38%" > ${newOnto.STAD_DN_CHN} <c:if test="${newOnto.OP_TYPE <=1}"><span class="edit" onclick="setInput(this,'STAD_DN_CHN');"/></c:if></td>
								</tr>
								<tr>
									<td align="right">科室上级名称:</td><td>${oldOnto.PARENT_NAME}</td><td><span id="newParent">${newOnto.PARENT_NAME}</span><c:if test="${newOnto.OP_TYPE <=2}"> <span class="edit" onclick="selectCategory();"/></c:if></td>
								</tr>
								<tr>
									<td align="right">主要编码:</td><td>${oldOnto.MAIN_CODE}</td><td >${newOnto.MAIN_CODE} </td>
								</tr>
								<tr>
									<td align="right">科室定义:</td><td>${oldOnto.TERM_DEFIN}</td><td >${newOnto.TERM_DEFIN} <c:if test="${newOnto.OP_TYPE <=1}"><span class="edit" onclick="setInput(this,'TERM_DEFIN');"/></c:if></td>
								</tr>
								<tr>
									<td align="right">停用标记:</td><td><c:if test="${oldOnto.IS_DISABLE==0}">否</c:if><c:if test="${oldOnto.IS_DISABLE==1}">是</c:if></td><td ><c:if test="${newOnto.IS_DISABLE==0}">否</c:if><c:if test="${newOnto.IS_DISABLE==1}">是</c:if><c:if test="${newOnto.OP_TYPE <=1  || newOnto.OP_TYPE==4}"><span class="edit" onclick="setSelect(this,'IS_DISABLE');"/></c:if></td>
								</tr>
								<tr>
									<td align="right">停用描述:</td><td>${oldOnto.DESCRIPTION}</td><td >${newOnto.DESCRIPTION} <c:if test="${newOnto.OP_TYPE <=1 || newOnto.OP_TYPE==4}"><span class="edit" onclick="setInput(this,'DESCRIPTION');"/></c:if></td>
								</tr>
								<tr>
									<td align="right">更新人:</td><td>${oldOnto.UPDATE_MAN}</td><td>${newOnto.UPDATE_MAN}</td>
								</tr>
								<tr>
									<td align="right">更新时间:</td><td><fmt:formatDate value="${oldOnto.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td><td><fmt:formatDate value="${newOnto.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								</tr>
								<tr>
									<td align="right">变更描述:</td><td colspan="2" align="left">${newOnto.UPD_DESC}</td>
								</tr>
								<tr>
									<td align="right">审批备注:</td><td colspan="2" align="left">
										<c:if test="${newOnto.status==0 }">
											<input type="text" name="CHECK_MEMO" id="CHECK_MEMO" style="width:100%;" maxlength="80" placeholder="可输入审批备注">
										</c:if>
										<c:if test="${newOnto.status!=0 }">
											${newOnto.CHECK_MEMO}
										</c:if>
									</td>
								</tr>
								<tr id="showConsol">
									<td colspan="3"><a>显示全部</a></td>
								</tr>
								</tbody>
							</table>
							</div>
							<div>
								<c:choose>
								<c:when test="${not empty osynList}">
									<div style="height: 37px;font-size: 16px;"><span style="background-color: #6faed9;">&nbsp;新增科室同义词：</span></div>
									<div>
										<c:forEach items="${osynList}" var="osyn" varStatus="vs">
										<table  class='table table-bordered table-hover'>
											<tr>
												<td align="right">术语名称:</td><td class="memo">${osyn.DN_CHN}</td>
												<td align="right">同义词类型:</td>
												<td>
														<c:if test="${osyn.SYNO_TYPE==23101}">俗语</c:if>
														<c:if test="${osyn.SYNO_TYPE==23102}">缩略语</c:if>
														<c:if test="${osyn.SYNO_TYPE==23103}">同音字/错别字</c:if>
														<c:if test="${osyn.SYNO_TYPE==23104}">拼音首字母</c:if>
														<c:if test="${osyn.SYNO_TYPE==23105}">语用同义词</c:if>
														<c:if test="${osyn.SYNO_TYPE==23106}">专指同义词</c:if>
														<c:if test="${osyn.SYNO_TYPE==23107}">其它</c:if>
												</td>
												<td align="right">停用标记:</td><td><c:if test="${osyn.IS_DISABLE == '0' }">否</c:if><c:if test="${osyn.IS_DISABLE == '1' }">是</c:if></td>
												<td align="right">停用描述:</td><td class="memo">${osyn.DESCRIPTION}</td>
											</tr>
										</table>
										</c:forEach>
									</div>
								</c:when>
								</c:choose>
							</div>
						</div>
						<div style="padding: 0px;margin: 4px;vertical-align: middle;text-align: center;">
							<c:if test="${newOnto.STATUS==0  }">
								<label>下一条&nbsp;<input type='checkbox' class="ace" id="nextShow" <c:if test="${nextShow==1}">checked="checked"</c:if>/><span class="lbl">&nbsp;&nbsp;&nbsp;</span></label>
								<ts:rights code="ONTO_CHECK_PASS_${RIGHTS}">
									<a class="btn btn-mini btn-primary" onclick="passRefuse('${newOnto.H_ID}',0);">通过 </a>
								</ts:rights>
								<ts:rights code="ONTO_CHECK_REFUSE_${RIGHTS}">
									<a class="btn btn-mini btn-danger" onclick="passRefuse('${newOnto.H_ID}',1);">拒绝 </a>
								</ts:rights>
							</c:if>
							<a class="btn btn-mini btn-success" onclick="top.Dialog.close();"> 关闭</a>
						</div>
						</div>
						<!-- /.col -->
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
						</form>
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->
	</div>
	<!-- /.main-container -->
	<div>
		<select class="chosen-select form-control" style="vertical-align:top;display: none" name="IS_DISABLE" id="IS_DISABLE">
			<option value="0" <c:if test="${newOnto.IS_DISABLE==0}">selected</c:if>>否</option>
			<option value="1" <c:if test="${newOnto.IS_DISABLE==1}">selected</c:if>>是</option>
		</select>
	</div>
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript" src="static/js/ontology/check.js?v=001"></script>
	</body>
</html>