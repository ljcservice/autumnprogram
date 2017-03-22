<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<meta charset="utf-8" />
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>  
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
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

</style>
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container" >
		<!-- /section:basics/sidebar -->
		<div class="main-content" >
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div >
							<form name="checkForm" id="checkForm" action="DoctOrder/CheckRsViewUI.do" method="post">
							<input type="hidden" value="${page.pd.patient_id}" name="patient_id" id="patient_id"/>
							<input type="hidden" value="${page.pd.visit_id}"   name="visit_id"   id="visit_id"/>
							<input type="hidden" value="${page.pd.ngroupnum}"  name="ngroupnum"  id="ngroupnum"/>
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
								<thead>
									<tr style="padding-top: 0px;padding-bottom: 0px;">
										<th colspan="4" style="vertical-middle;">
										
											<label>
												<input type="checkbox" name="delAll" id="delAll" class="ace" onclick="setCheckBox(this);">
												<span class="lbl"></span>
											</label>
												&nbsp;&nbsp;&nbsp;
											<a class="btn btn-minier btn-danger"  title="批量删除"  onclick="delCheckRsBatch();">
												<i class="ace-icon fa fa-trash-o bigger-120"></i> 
											</a>
											<a class="btn btn-minier btn-danger"  title="添加点评" onclick="toAddCheckRs('${page.pd.ngroupnum}');">
												<i class="ace-icon fa fa-pencil bigger-120 "></i>
											</a>
											<a  class="btn btn-minier btn-success" title="刷新点评结果" onclick="self.location.href = self.location.href;">
												<i class="ace-icon fa fa-refresh bigger-120"></i>
											</a>
											
											&nbsp;
											|
											&nbsp;
											
											<a class="btn btn-minier btn-info"  title="专家点评" onclick="expertCheckRs('${page.pd.ngroupnum}');">
												<i class="ace-icon fa fa-users bigger-120 "></i>
											</a>
											&nbsp;
											|
											&nbsp;
											<span style="text-align: left;vertical-align: middle;">
											是否合理：
												<label style="padding-top: 0px;padding-bottom: 0px;margin-top: 0px;margin-bottom: 0px;">
													<input name="isCheck" id="isCheck"
														<c:if test="${check_status==0 }"> checked="checked" </c:if>	 
														class="ace ace-switch ace-switch-6" type="checkbox" style="size:5;"  />
													<span class="lbl" ></span>
												</label>
											</span>
											
											<a class="btn btn-minier btn-grey" title="保存" onclick="saveIsCheckTrue()">
												<i class="ace-icon fa fa-floppy-o bigger-120"></i>
											</a>
										</th>
									</tr>
								</thead>
														
								<tbody>
									<!-- 开始循环 -->	
									<c:choose>
										<c:when test="${not empty checkRss}">
											<c:forEach items="${checkRss}" var="rs" varStatus="vs" >
												<tr>
													<td class='center' style="width:30px;vertical-align: middle;" rowspan="2" >
													<label>
														<input type="checkbox" name="rs_ids"  class="ace" value="${rs.rs_id }"  >
														<span class="lbl"></span>
													</label>
													</td>
													<td style="width: 80px;" class='center' > ${rsTypeDict.get(rs.RS_DRUG_TYPE).rs_type_name }</td>
													<td >${rs.drug_id1_name } 
														<c:if test="${rsTypeDict.get(rs.RS_COUNT) == 2}"> 
														<font color="red">与</font> ${rs.drug_id2_name } 
														 </c:if>
													</td>
													<td style="width: 300px;">
														<a class="btn btn-minier btn-danger"  onclick="delCheckRs('${rs.rs_id }');">
															<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
														</a>
														<a class="btn btn-minier btn-info"  onclick="editCheckRs('${rs.rs_id }');">
															<i class="ace-icon fa fa-pencil-square-o bigger-100 "></i> 
														</a>
													</td>
												</tr>
												<tr>
													<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp; ${rs.ALERT_HINT } </td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr class="main_info">
												<td colspan="4" class="center">没有相关数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							</form>
						</div>
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
	<script src="static/ace/js/chosen.jquery.js?v=2008001"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2018082"></script>
<script type="text/javascript" src="static/js/ontology/tree.js?v=20180861"></script>
<script type="text/javascript">
$(top.hangge());
$(function() {
	//changeTree();
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	$('[data-rel=tooltip]').tooltip();
	$('[data-rel=popover]').popover({html:true});
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		//resize the chosen on window resize
	
		$(window)
		.off('resize.chosen')
		.on('resize.chosen', function() {
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.width()});
			})
		}).trigger('resize.chosen');
		//resize chosen on sidebar collapse/expand
		$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
			if(event_name != 'sidebar_collapsed') return;
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': $this.width()});
			})
		});
	
	
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}

});
// 查询
function searchs(){
	top.jzts();
	$("#searchForm").submit();
	
}
//单个删除
function delCheckRs(id){
	bootbox.confirm("确定删除该点评结果吗?", function(result) {
		if(result) {
			var url = path+"/DoctOrder/delCheckRs.do?RS_ID="+id;
			$.get(url,function(data){
				if(data.result=="success"){
					self.location.href = self.location.href ;
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}
			});
		};
	});	
}
//批量通过   批量拒绝
function delCheckRsBatch(){
	var str = '';
	for(var i=0;i < document.getElementsByName('rs_ids').length;i++)
	{
		  if(document.getElementsByName('rs_ids')[i].checked){
		  	if(str=='') str += document.getElementsByName('rs_ids')[i].value;
		  	else str += ';' + document.getElementsByName('rs_ids')[i].value;
		  }
	}
	if(str==''){
		bootbox.dialog({
			message: "<span class='bigger-110'>您没有选择任何内容!</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		$("#zcheckbox").tips({
			side:3,
            msg:'点这里全选',
            bg:'#AE81FF',
            time:3
        });
		
		return;
	}
	var url = path+"/DoctOrder/delCheckRsAll.do";
	var msg = "确定删除全部选中的数据吗?";
	bootbox.confirm(msg,function(result) {
		if(result) {
			top.jzts();
			$.ajax({
				type: "POST",
				url: url,
			   	data: {rs_ids:str},
				dataType:'json',
				//beforeSend: validateData,
				cache: false,
				success: function(data){
					if(data.result=="success"){
						self.location.href = self.location.href ;
					}else{
						$(top.hangge());
						var msg = data.result;
						if(msg=="failed"){msg="操作失败！";}
						bootbox.dialog({
							message: "<span class='bigger-110'>"+msg+"</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
					}
				}
			});
		}
	});
}
function toAddCheckRs(ngroupnum){
	var url = path + "/DoctOrder/toAddCheckRs.do?ngroupnum="+ngroupnum+"&patient_id="+$("#patient_id").val()+"&visit_id="+$("#visit_id").val();
	//top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="编辑点评";
	diag.URL = url;
	diag.Width = 650;
	diag.Height =500;
	diag.CancelEvent = function(){ //关闭事件
		var ngroupnum = diag.innerFrame.contentWindow.document.getElementById('ngroupnum').value;
		$("#ngroupnum").val(ngroupnum);
		diag.close();
		//遮罩层控制，第三层弹窗使用
		top.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		$("#checkForm").submit();
	};
	diag.show();
}
function editCheckRs(rs_id){
	var url = path + "/DoctOrder/toEditCheckRs.do?rs_id="+rs_id;
	//top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="编辑点评";
	diag.URL = url;
	diag.Width = 650;
	diag.Height =500;
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		top.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		$("#checkForm").submit();
	};
	diag.show();
	
}
function saveIsCheckTrue(){
	var checkstats = '${check_status}';
	var msg = "";
	if($("#isCheck").attr("checked")){
		msg= "选择合理将清空所有点评结果，请确认？";
		//合理
		if(checkstats==0){
			$("#isCheck").tips({ side:3, msg:'已经为合理，无需再次保存', bg:'#AE81FF',  time:3   });
			return;
		}else{
			
		}
	}else{
		msg= "确定设置为不合理吗，请确认？";
		//不合理
		if(checkstats==0){
			
		}else{
			$("#isCheck").tips({ side:3, msg:'已经为不合理，无需再次保存', bg:'#AE81FF',  time:3   });
			return;
		}
	}
	
	bootbox.confirm(msg, function(result) {
		if(result) {
			var CHECK_STATUS = 1;//不合理
			if($("#isCheck").attr("checked")){
				CHECK_STATUS = 0;//合理
			}
			var url = path+"/DoctOrder/setCheckRsStatus.do?CHECK_STATUS="+CHECK_STATUS+"&"+$("#checkForm").serialize();
			$.get(url,function(data){
				if(data.result=="success"){
					self.location.href = self.location.href ;
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}
			});
		};
	});	
	
}
</script>
</html>