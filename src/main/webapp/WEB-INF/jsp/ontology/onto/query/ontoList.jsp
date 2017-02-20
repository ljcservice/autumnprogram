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
						<form action="ontology/ontologyList.do" method="post">
							<input type="hidden" name="standardId" value="${standardId}" id="standardId"/>
							<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
						</form>
						<div style="vertical-align:bottom;float: left;padding-top: 5px;padding-bottom: 5px;">
							<ts:rights code="ONTO_ADD_${RIGHTS}">
								<a class="btn btn-mini btn-success" onclick="add('Add');">新增${ontoName}</a>
							</ts:rights>
							<ts:rights code="ONTO_EDIT_${RIGHTS}">
								<a class="btn btn-mini btn-primary" onclick="selectCategory();">修改父节点</a>
								<c:if test="${ontoType==51005 }">
									<a class="btn btn-mini btn-primary" onclick="depCategory();">术语知识编辑</a>
								</c:if>
							</ts:rights>
							<ts:rights code="ONTO_STOP_${RIGHTS}">
								<a class="btn btn-mini btn-warning" onclick="stopOnto();">停用术语</a>
							</ts:rights>
							<ts:rights code="OSYN_ADD_${RIGHTS}">
								<a class="btn btn-mini btn-warning" onclick="editOsyn(0);">添加同义词</a>
							</ts:rights>
							<ts:rights code="OSYN_EDIT_${RIGHTS}">
								<a class="btn btn-mini btn-success" onclick="editOsyn(1);">修改同义词</a>
							</ts:rights>
							<ts:rights code="OSYN_IMPORT_${RIGHTS}">
								<a class="btn btn-mini btn-success" onclick="importOnto();">本体导入</a>
							</ts:rights>
						</div>
						<div>
						<!-- 诊断列表显示 -->
						<c:if test="${ontoType==51005 }">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
<!-- 										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label> -->
									</th>
									<th class="center" nowrap>${ontoName}标准名称</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>附加编码</th>
									<th class="center" nowrap>同义词</th>
									<th class="center" nowrap>父节点名称</th>
									<th class="center" nowrap>父节点主要编码</th>
									<th class="center" nowrap>父节点附加编码</th>
									<th class="center" nowrap>术语类型</th>
									<th class="center" nowrap>科室分类</th>
									<th class="center" nowrap>部位分类</th>
									<th class="center" nowrap>人群分类</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<tr ondblclick="edit(this,'Edit')">
											<td class='center' style="width: 35px;">
													<label><input type="radio" name='ids' class="ace" value="${diag.ID}" OSYN_ID="${diag.OSYN_ID}" STAND_ID="${diag.DN_ID}" OSYN_FLAG="${diag.OSYN_EDIT_FLAG}" ONTO_FLAG="${diag.ONTO_EDIT_FLAG}" IS_DISABLE="${diag.IS_DISABLE }" OSYN_IS_DISABLE="${diag.OSYN_IS_DISABLE }" MAIN_CODE="${diag.MAIN_CODE}" ADD_CODE="${diag.ADD_CODE}" /><span class="lbl"></span></label>
											</td>
											<td class="center " <c:if test="${diag.ONTO_EDIT_FLAG==1}"> style="color: #ffb752" title="审核中" </c:if>>
												<label <c:if test="${diag.IS_DISABLE==1}">style="text-decoration:line-through;"</c:if>>${diag.STAD_CN }</label>&nbsp;
												<a style="white-space: nowrap; width: 30px;" class="specila" onclick="fixer_tree(event,'${diag.ID}');" >定位</a>
											</td>
											<td class="center " >${diag.MAIN_CODE }</td>
											<td class="center " >${diag.ADD_CODE }</td>
											<td class="center " <c:if test="${diag.OSYN_EDIT_FLAG==1}"> style="color: #ffb752" title="审核中" </c:if>>
												<label <c:if test="${diag.OSYN_IS_DISABLE==1}">style="text-decoration:line-through;"</c:if>>${diag.CN }</label>
											</td>
											<td class="center " >${diag.PARENT_NAME } </td>
											<td class="center " >${diag.P_MAIN_CODE }</td>
											<td class="center " >${diag.P_ADD_CODE }</td>
											<td class="center " >
												<c:if test="${diag.TERM_TYPE==1}">症状</c:if>
												<c:if test="${diag.TERM_TYPE==2}">疾病</c:if>
											</td>
											<td class="center " >${diag.DEP_CATEGORY_NAME }</td>
											<td class="center ">
												<c:forEach items="${partMap.entrySet()}" var="partTyp" varStatus="vs">
													<c:if test="${partTyp.key == diag.PART_CATEGORY }">${partTyp.value}</c:if>
												</c:forEach>
												<!--  <c:if test="${diag.PART_CATEGORY == '0' }">否</c:if>
												<c:if test="${diag.PART_CATEGORY == '1' }">是</c:if> -->
											</td>
											<td class="center " >
												<c:forEach items="${crowsMap.entrySet()}" var="crowsTyp" varStatus="vs">
													<c:if test="${crowsTyp.key == diag.MAN_CATEGORY }">${crowsTyp.value}</c:if>
												</c:forEach>
											</td>
<!-- 											<td class="center queryOsyn"><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td> -->
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						<!-- 手术列表显示 -->
						<c:if test="${ontoType==51003 }">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
<!-- 										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label> -->
									</th>
									<th class="center" nowrap>${ontoName}标准名称</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>同义词</th>
									<th class="center" nowrap>父节点名称</th>
									<th class="center" nowrap>父节点主要编码</th>
									<th class="center" nowrap>来源名称中文</th>
<!-- 									<th class="center" nowrap>停用标记</th> -->
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<tr ondblclick="edit(this,'Edit')">
											<td class='center' style="width: 30px;">
												<label><input type="radio" name='ids' class="ace" value="${diag.ID}" OSYN_ID="${diag.OSYN_ID}" STAND_ID="${diag.ON_ID}" OSYN_FLAG="${diag.OSYN_EDIT_FLAG}" ONTO_FLAG="${diag.ONTO_EDIT_FLAG}" IS_DISABLE="${diag.IS_DISABLE }" OSYN_IS_DISABLE="${diag.OSYN_IS_DISABLE }" MAIN_CODE="${diag.OP_CODE}"/><span class="lbl"></span></label>
											</td>
											<td class="center " <c:if test="${diag.ONTO_EDIT_FLAG==1}"> style="color: #ffb752" title="审核中" </c:if>>
												<label <c:if test="${diag.IS_DISABLE==1}">style="text-decoration:line-through;"</c:if>>${diag.STAD_CN }</label>&nbsp;
												<a style="white-space: nowrap; width: 30px;" class="specila" onclick="fixer_tree(event,'${diag.ID}');" >定位</a>
											</td>
											<td class="center ">${diag.OP_CODE }</td>
											<td class="center " <c:if test="${diag.OSYN_EDIT_FLAG==1}"> style="color: #ffb752" title="审核中" </c:if>>
												<label <c:if test="${diag.OSYN_IS_DISABLE==1}">style="text-decoration:line-through;"</c:if>>${diag.CN }</label>
											</td>
											<td class="center ">${diag.PARENT_NAME } </td>
											<td class="center ">${diag.P_OP_CODE }</td>
											<td class="center ">${diag.ORG_STAD_OP_CHN }</td>
<!-- 											<td class="center "> -->
<!-- 												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if> -->
<!-- 												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if> -->
<!-- 											</td> -->
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						<!-- 科室列表显示 -->
						<c:if test="${ontoType==51006 }">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
<!-- 										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label> -->
									</th>
									<th class="center" nowrap>${ontoName}标准名称</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>同义词</th>
									<th class="center" nowrap>父节点名称</th>
									<th class="center" nowrap>父节点主要编码</th>
<!-- 									<th class="center" nowrap>停用标记</th> -->
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<tr ondblclick="edit(this,'Edit')">
											<td class='center' style="width: 30px;">
												<label><input type="radio" name='ids' class="ace" value="${diag.ID}" OSYN_ID="${diag.OSYN_ID}" STAND_ID="${diag.DN_ID}" OSYN_FLAG="${diag.OSYN_EDIT_FLAG}" ONTO_FLAG="${diag.ONTO_EDIT_FLAG}" IS_DISABLE="${diag.IS_DISABLE }" OSYN_IS_DISABLE="${diag.OSYN_IS_DISABLE }" MAIN_CODE="${diag.DEP_NAME_CODE}"/><span class="lbl"></span></label>
											</td>
											<td class="center " <c:if test="${diag.ONTO_EDIT_FLAG==1}"> style="color: #ffb752" title="审核中" </c:if>>
												<label <c:if test="${diag.IS_DISABLE==1}">style="text-decoration:line-through;"</c:if>>${diag.STAD_CN }</label>&nbsp;
												<a style="white-space: nowrap; width: 30px;" class="specila" onclick="fixer_tree(event,'${diag.ID}');" >定位</a>
											</td>
											<td class="center ">${diag.DEP_NAME_CODE }</td>
											<td class="center " <c:if test="${diag.OSYN_EDIT_FLAG==1}"> style="color: #ffb752" title="审核中" </c:if>>
												<label <c:if test="${diag.OSYN_IS_DISABLE==1}">style="text-decoration:line-through;"</c:if>>${diag.CN }</label>
											</td>
											<td class="center ">${diag.PARENT_NAME } </td>
											<td class="center ">${diag.P_CODE }</td>
<!-- 											<td class="center "> -->
<!-- 												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if> -->
<!-- 												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if> -->
<!-- 											</td> -->
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						</div>
						<div class="page-header position-relative">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;">
										<div class="pagination" style="float: right;padding-top: 0px;margin: 0px;">${page.pageStr}</div>
									</td>
								</tr>
							</table>
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
	<!--本体查询列表js-->
<!-- 	<script type="text/javascript" src="static/js/ontology/query.js?v=2016086"></script> -->
	</body>
</html>