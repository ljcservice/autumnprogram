<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
<style>
.ztree li a.curSelectedNode {
/* 	background-color: #ffb951;	 */
 }
 .check-search{
	float: left;
	margin: 2px;
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
						<div class="col-xs-12">
						<!-- 检索  -->
								<div id="searchDiv"  style="vertical-align:bottom;float: left;padding-top: 4px;padding-bottom: 5px;width: 100%;">
								<form action="DrugMatcher/query.do" method="post" name="searchForm" id="searchForm">
									<div class="check-search nav-search"  >
										药品代码：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="drugCode" type="text" name="drugCode" value="${pd.drugCode}" placeholder="药品代码" maxlength="80"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
										药品名称：
										<span class="input-icon">
											<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="drugName" value="${pd.drugName}" placeholder="药品名称" maxlength="80"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<div class="check-search"  >
										<a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
										<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
									</div>
									<div class="check-search"   >
										附加条件:
									 	<select class="chosen-select form-control" name="whereField" id="whereField" data-placeholder=" " style="vertical-align:top;width: 120px;" >
												<option value=""></option>
								                <option value="is_basedrug" <c:if test="${'is_basedrug'==whereField}"> selected='selected'</c:if>>国家基本药物
								                </option>
								                <option value="is_exhilarant" <c:if test="${'is_exhilarant'==whereField}"> selected='selected'</c:if>>兴奋剂
								                </option>
								                <option value="is_injection" <c:if test="${'is_injection'==whereField}"> selected='selected'</c:if>>注射剂
								                </option>
								                <option value="is_oral" <c:if test="${'is_oral'==whereField}"> selected='selected'</c:if>>口服制剂</option>
								                <option value="is_impregnant" <c:if test="${'is_impregnant'==whereField}"> selected='selected'</c:if>>溶剂
								                </option>
								                <option value="is_external" <c:if test="${'is_external'==whereField}"> selected='selected'</c:if>>外用
								                </option>
								                <option value="is_chinesedrug" <c:if test="${'is_chinesedrug'==whereField}"> selected='selected'</c:if>>中药
								                </option>
								                <option value="is_allergy" <c:if test="${'is_allergy'==whereField}"> selected='selected'</c:if>>抗过敏
								                </option>
								                <option value="is_patentdrug" <c:if test="${'is_patentdrug'==whereField}"> selected='selected'</c:if>>中成药
								                </option>
								                <option value="is_tumor" <c:if test="${'is_tumor'==whereField}"> selected='selected'</c:if>>抗肿瘤</option>
								                <option value="is_poison" <c:if test="${'is_poison'==whereField}"> selected='selected'</c:if>>毒药</option>
								                <option value="is_psychotic" <c:if test="${'is_psychotic'==whereField}"> selected='selected'</c:if>>精神药
								                </option>
								                <option value="is_habitforming" <c:if test="${'is_habitforming'==whereField}"> selected='selected'</c:if>>麻药
								                </option>
								                <option value="is_radiation" <c:if test="${'is_radiation'==whereField}"> selected='selected'</c:if>>放射
								                </option>
								                <option value="is_precious" <c:if test="${'is_precious'==whereField}"> selected='selected'</c:if>>贵重药
								                </option>
								                <option value="is_danger" <c:if test="${'is_danger'==whereField}"> selected='selected'</c:if>>高危药</option>
								                <option value="is_otc" <c:if test="${'is_otc'==whereField}"> selected='selected'</c:if>>OTC</option>
								                <option value="is_hormone" <c:if test="${'is_hormone'==whereField}"> selected='selected'</c:if>>激素类
								                </option>
								                <option value="is_cardiovascular" <c:if test="${'is_cardiovascular'==whereField}"> selected='selected'</c:if>>心脑血管类
								                </option>
								                <option value="is_digestive" <c:if test="${'is_digestive'==whereField}"> selected='selected'</c:if>>消化系统类
								                </option>
								                <option value="is_biological" <c:if test="${'is_biological'==whereField}"> selected='selected'</c:if>>生物制品类
								                </option>			
										</select>=
							            <select  class="chosen-select form-control" name="whereValue" data-placeholder=" " style="vertical-align:top;width: 60px;">
							                <option></option>
							                <option value="1" <c:if test="${'1'==whereValue}"> selected='selected'</c:if>>是</option>
							                <option value="0" <c:if test="${'0'==whereValue}"> selected='selected'</c:if>>否</option>
							            </select>
									</div>
									<div class="check-search"  >
										抗菌药:
									 	<select class="chosen-select form-control" name="q_is_anti" id="q_is_anti" data-placeholder=" " style="vertical-align:top;width: 120px;">
									 		<option></option>
							                <option value="0" <c:if test="${'0'==q_is_anti}"> selected='selected'</c:if>>所有抗菌药</option>
							                <option value="1" <c:if test="${'1'==q_is_anti}"> selected='selected'</c:if>>非限制级抗菌药</option>
							                <option value="2" <c:if test="${'2'==q_is_anti}"> selected='selected'</c:if>>限制级抗菌药</option>
							                <option value="3" <c:if test="${'3'==q_is_anti}"> selected='selected'</c:if>>特殊级抗菌药</option>
							                <option value="4" <c:if test="${'4'==q_is_anti}"> selected='selected'</c:if>>局部抗菌药</option>
							                <option value="5" <c:if test="${'5'==q_is_anti}"> selected='selected'</c:if>>全身抗菌药</option>
							            </select>
									</div>
									<div class="check-search"  >
										精神类药:
									 	<select class="chosen-select form-control" name="q_is_psychotic" id="q_is_psychotic" data-placeholder=" " style="vertical-align:top;width: 120px;">
									 		<option></option>
							                <option value="0" <c:if test="${'0'==q_is_psychotic}"> selected='selected'</c:if>>所有精神类药物</option>
							                <option value="1" <c:if test="${'1'==q_is_psychotic}"> selected='selected'</c:if>>I类精神类药</option>
							                <option value="2" <c:if test="${'2'==q_is_psychotic}"> selected='selected'</c:if>>II类精神类药</option>
							            </select>
									</div>
									<div class="check-search"  >
										高危药:
									 	<select class="chosen-select form-control" name="q_is_danger" id="q_is_danger" data-placeholder=" " style="vertical-align:top;width: 110px;">
									 		<option></option>
							                <option value="0" <c:if test="${'0'==q_is_danger}"> selected='selected'</c:if>>所有高危药</option>
							                <option value="1" <c:if test="${'1'==q_is_danger}"> selected='selected'</c:if>>A级</option>
							                <option value="2" <c:if test="${'2'==q_is_danger}"> selected='selected'</c:if>>B级</option>
							                <option value="3" <c:if test="${'3'==q_is_danger}"> selected='selected'</c:if>>C级</option>
							            </select>
									</div>
									<div class="check-search"  >
										配对状态:
									 	<select class="chosen-select form-control" name="matched" id="matched" data-placeholder=" " style="vertical-align:top;width: 80px;">
									 		<option value="">全部</option>
							               <option value="is not null" <c:if test="${'is not null'==matched}">selected='selected'</c:if> >已配对</option>
							               <option value="is null" <c:if test="${'is null'==matched}">selected='selected'</c:if>>未配对</option>
							           </select>
									</div>
									
									<div class="check-search"  id="autoMatcherBtn" >
										<ts:rights code="DrugMatcher/autoMatcher" >
											<c:choose>
											<c:when test="${autoMatcher!=null and autoMatcher!='' }">
												自动匹配中......
											</c:when>
											<c:otherwise>
												<a class="btn btn-mini btn-primary" onclick="autoMatcher();">自动匹配</a>
											</c:otherwise>
											</c:choose>
										</ts:rights>
									</div>
								</form>
								</div>
						<!-- 检索  -->
						<div>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
						             <th class="center" colspan="2">本院药品信息</th>
						             <th class="center" colspan="6">配码信息</th>
						             <th class="center" rowspan="2" id="last_th" style="border-right: 1px;background-color: #f1f1f1;">操作</th>
						         </tr>
						         <tr>
						             <th class="center">药品码</th>
						             <th class="center">药品名称</th>
						             <th class="center">药品名称</th>
						             <th class="center">规格</th>
						             <th class="center">单位</th>
						             <th class="center">剂型</th>
						             <th class="center">最小单位剂量</th>
						             <th class="center" style="background-color: #f1f1f1;border-right: 1px;">剂量单位</th>
						         </tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="obj" varStatus="vs">
										<tr  >
											 <td class="center">${obj.get("Drug_No_Local")}
								              </td>
								              <td class="center">${obj.get("Drug_Name_Local")}
								              </td>
								              <td class="center">${obj.get("drug_name")}
								              </td class="center">
								              <td class="center">${obj.get("drug_spec_pdss")}
								              </td>
								              <td class="center">${obj.get("units_pdss")}
								              </td>
								              <td class="center">${obj.get("drug_form_pdss")}
								              </td>
								              <td class="center">${obj.get("dose_per_unit_pdss")}
								              </td>
								              <td class="center">${obj.get("dose_units_pdss")}
								              </td>
								              <td  class="center">
								                  <a href="javascript:DeleteIt('${obj.get("drug_map_id")}')">删除</a>
								                  <a href="javascript:MatchIt('${obj.get("drug_map_id")}')">配对</a>
								              </td>
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="13" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</div>
						<div class= "pageStrDiv" id="pageStrDiv" style="padding-top: 5px;padding-bottom: 5px;">
							<table style="width:100%;">
								<tr>
									<td>
										<b>药品总数：<font color="#428bca">${matcherCount.drug_all}</font> &nbsp;&nbsp;  已匹配总数： <font color="#428bca">${matcherCount.success}</font></b>
									</td>
									<td>
										<div class="pagination" style="float: right;padding: 0px;margin: 0px;">${page.pageStr}</div>
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

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

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
	<script type="text/javascript" src="static/js/common/common.js"></script>
	</body>
<script type="text/javascript" src="static/js/common/lockTable.js?v=201612"></script>
<script type="text/javascript">
$(top.hangge());

//检索
function searchs(){
	top.jzts();
	$("#searchForm").submit();
}

function resetForm(){
	document.getElementById("searchForm").reset();
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
			if(event_name != 'sidebar_collapsed') return;
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.parent().width()});
			});
		});
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}

	//重置当前页面高度，自适应浏览器
	initWidthHeight();
	$(window).off('resize').on('resize', function() {
		initWidthHeight();
	}).trigger('resize');
});
//重置当前页面高度，自适应浏览器
function initWidthHeight(){
	var rr = new Array;
	rr[0]="searchDiv";
	rr[1]="pageStrDiv";
	FixTable("simple-table", 1, rr);
}
//删除
function DeleteIt(id){
	bootbox.confirm("确定要删除吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "DrugMatcher/deleteIt.do?dmi="+id+"&tm="+new Date().getTime();
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//配对
function MatchIt(drug_map_id) {
    var url = path + "/DrugMatcher/matcherIt.do?drug_map_id=" + drug_map_id ;
  	 top.jzts();
  	 var diag = new top.Dialog();
  	 diag.Drag=true;
  	 diag.Title ="资料";
  	 diag.URL = url;
	 diag.Width = $(top.window).width();
	 diag.Height = $(top.window).height();
  	 diag.CancelEvent = function(){ //关闭事件
  		nextPage(${page.currentPage});
  		diag.close();
  	 };
  	 diag.show();
}
function autoMatcher(){
	$("#autoMatcherBtn").html("自动匹配中......");
	$.ajax({
		type: "POST",
		url: '${path}/DrugMatcher/autoMatcher.do',
		data:$("#searchForm").serialize(),
		dataType:'json',
		async:true,
		cache: false,
		success: function(data){
			alert(data.msg);
			nextPage(${page.currentPage});
		}
    });
}
</script>
</html>
