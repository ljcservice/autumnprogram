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
.shadow{  
/* 	-webkit-box-shadow: #666 0px 0px 10px;   */
/* 	-moz-box-shadow: #666 0px 0px 10px;   */
/* 	box-shadow: #666 0px 0px 10px;   */
/* 	background: #EEFF99;   */
	border:1px solid #d5d5d5;
	overflow: hidden;
}
.tag_grp {
    border: 1px solid #d3d3d3;
    border-radius: 3px;
    color: black;
    cursor: pointer;
    display: inline-block;
    float: none;
    font-size: 12px;
    line-height: 22px;
    margin: 2px 2px 2px 4px;
    padding: 2px 5px;
    background-color: rgb(153, 204, 255);
    width: auto;
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
						<div class="col-xs-12" id="searchDiv">
							
    <form name="searchForm" id="searchForm"  action="${path}/FetchDataFromDrugDict/query.do" method="post">
        <input name="o" type="hidden" value="query"/>
        <input type="hidden" name="drug_code" id="drug_code"  value=""/>
        <input type="hidden" name="drug_spec" id="drug_spec" value=""/>
        <input type="hidden" name="drug_name" id="drug_name" value=""/>
        <div class="check-search nav-search"  >
        	药品代码:
       		 <span class="input-icon">
				<input class="nav-search-input" autocomplete="off" id="q_drug_code" type="text" name="q_drug_code" value="${pd.q_drug_code}" placeholder="药品代码" maxlength="80"/>
				<i class="ace-icon fa fa-search nav-search-icon"></i>
			</span>药品名称:
			 <span class="input-icon">
				<input class="nav-search-input" autocomplete="off" id="q_drug_name" type="text" name="q_drug_name" value="${pd.q_drug_name}" placeholder="药品名称" maxlength="80"/>
				<i class="ace-icon fa fa-search nav-search-icon"></i>
			</span>
        </div>
         <div class="check-search nav-search"  >
            <label class="select" >
            	<a id="popover-dept-name"  class="popover-toggle" href="javascript:void(0);" onclick="showCont();">
            	分类：
            	</a>
            	<input type="text" id="deptNameValue" data-field="field-dept-name" style="width: 100px;"  name="toxi_property" readonly value="${toxi_property }"/>
            </label>
        </div>
        <div class="check-search"  >
			<a class="btn btn-light btn-xs" onclick="searchs(this);" title="检索" target="treeFrame" id="searchBtn"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
			<a class="btn btn-light btn-xs" onclick="reset('searchForm');" title="重置"  id="resetBtn"><i id="nav-search-icon" class="ace-icon fa fa-undo bigger-110"></i></a>
			<a class="btn btn-mini btn-primary" onclick="ThisTransfer();">全部迁移</a>
		</div>
    </form>
</div>
<div>
	<div style="width:100%;font-family:黑体;font-size:20pt;font-color:black;line-height:40px;text-align:center;">
		HIS药品导入
	</div>
	<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
		 <thead>
		      <tr>
		          <th>No</th>
		          <th>药品代码</th>
		          <th>药品名称</th>
		          <th>规格</th>
		          <th>单位</th>
		          <th>剂型</th>
		          <th>毒理分类</th>
		          <th>最小单位剂量</th>
		          <th>剂量单位</th>
		          <th>药品类别标志</th>
		          <th>输入码</th>
		          <th id="last_th">操作</th>
		      </tr>
	      </thead>
	      <tbody>
	      <c:choose>
			<c:when test="${not empty resultList}">
	      	  <c:forEach items="${resultList}" var="tcr" varStatus="vs">
		      <tr>
		          <td>
		          	${vs.index + 1 + pageIndex}
		          </td>
		          <td>${tcr.get("drug_code")}
		          </td>
		          <td>${tcr.get("drug_name")}
		          </td>
		          <td>${tcr.get("drug_spec")}
		          </td>
		          <td>${tcr.get("units")}
		          </td>
		          <td>${tcr.get("drug_form")}
		          </td>
		          <td>${tcr.get("toxi_property")}
		          </td>
		          <td>${tcr.get("dose_per_unit")}
		          </td>
		          <td>${tcr.get("dose_units")}
		          </td>
		          <td>${tcr.get("drug_indicator")}
		          </td>
		          <td>${tcr.get("input_code")}
		          </td>
		          <td>
		          	<a href="javascript:transferSingle('${tcr.get("drug_code")}','${tcr.get("drug_spec")}','${tcr.get("drug_name")}')">
		          		迁移
		          	</a>
		          </td>
		      </tr>
		      </c:forEach>
		      </c:when>
				<c:otherwise>
					<tr class="main_info">
						<td colspan="12" class="center">没有相关数据</td>
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


		<!-- !分类-->
		<div id="popover-charge-type-box" style="display: none;background-color: white;border:1px solid #d5d5d5;width: 220px;height: auto;z-index: 9999" class="popover-box shadow" data-field="field-dept-name">
			<div class="close-container">
				<a href="javascript:void(0);" onclick="$(this).parent().parent().hide();mm=0;">【关闭】</a>
			</div>
			<input type="hidden" class="JUST_FOR_FIREFOX_DONT_REMOVE"/>
			<div class="popover-body tag_grp" style="width:210px;height:auto;">
				<label class="chk-all" style="width:200px;">
					<input type="checkbox" class="chk-all" onclick="selectAll(this);"/>
					全部
				</label>
					<c:forEach items="${toxiPropertyList}" var="obj" varStatus="vs">
						<label style="width:90px;"> 
							
							<input type="checkbox" value='${obj }' name="ORG_CODE" class="ORG_CODE" onclick="setValue();" nameValue="${obj }" <c:if test="${pd.toxi_property.contains(obj)==true }">checked="checked" </c:if>/>
							${obj }
						</label>
					</c:forEach>
			</div>
		</div>
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

function ThisTransfer() {
    if (confirm("系统提示：确认迁移所有数据？")) {
            $.ajax({
				type: "POST",
				url: '${path}/FetchDataFromDrugDict/transferAll.do',
		    	data: $("#searchForm").serialize(),
				dataType:'json',
				cache: false,
				success: function(data){
					if(data.result=="success"){
						nextPage($("#currentPage").val());
					}else{
						bootbox.dialog({
							message: "<span class='bigger-110'>操作失败</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
					}
				}
            });
    }
}
function transferSingle(_drugCode, _drugSpec,_drug_name) {
        drug_code.value = _drugCode;
        drug_spec.value = _drugSpec;
        drug_name.value = _drug_name;
       $.ajax({
			type: "POST",
			url: '${path}/FetchDataFromDrugDict/transferSingle.do',
	    	data: $("#searchForm").serialize(),
			dataType:'json',
			cache: false,
			success: function(data){
				if(data.result=="success"){
					nextPage($("#currentPage").val());
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>操作失败</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}
			}
        });
}
var mm = 0;
function showCont(){
	if(mm==0){
		$("#popover-charge-type-box").show().offset($("#deptNameValue").offset());
		mm=1;
	}else{
		$("#popover-charge-type-box").hide();
		mm=0;
	}
}
function selectAll(obj){
	if($(obj).prop("checked")){ 
		$(".ORG_CODE").each(function(i) {
			$(this).prop("checked",true);
		 });
	}else{
		$(".ORG_CODE").each(function(i) {
			$(this).prop("checked",false);
		 });
	}
	setValue();
}
function setValue(){
	var text = "";
	$(".ORG_CODE").each(function(i) {
		if($(this).prop("checked")){ 
			if(text==""){
				text = $(this).attr("nameValue");
			}else{
				text = text +","+ $(this).attr("nameValue");
			}
		}
	 });
	$("#deptNameValue").val(text);
}
</script>
</html>