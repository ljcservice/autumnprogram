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
										<th colspan="6" style="vertical-middle;">
										
											<label>
												<input type="checkbox" name="delAll" id="delAll" class="ace" onclick="setCheckBox(this);">
												<span class="lbl"></span>
											</label>
												&nbsp;&nbsp;&nbsp;
											<c:if test="${modifyFlag==1 }">
											<a class="btn btn-minier btn-danger"  title="批量删除"  onclick="delCheckRsBatch();">
												<i class="ace-icon fa fa-trash-o bigger-120"></i> 
											</a>
											<a class="btn btn-minier btn-danger"  title="添加点评" onclick="toAddCheckRs('${page.pd.ngroupnum}');">
												<i class="ace-icon fa fa-pencil bigger-120 "></i>
											</a>
											</c:if>
											<a  class="btn btn-minier btn-success" title="刷新点评结果" onclick="refreshAll();">
												<i class="ace-icon fa fa-refresh bigger-120"></i>
											</a>
											<c:if test="${empty expert_id && modifyFlag==1 }">
											&nbsp;
											|
											&nbsp;
											<a class="btn btn-minier btn-info"  title="专家点评" href="javascript:expertOrders();">
												<i class="ace-icon fa fa-users bigger-120 "></i>
											</a>
											</c:if>
											&nbsp;
											|
											&nbsp;
											<span style="text-align: left;vertical-align: middle;">
												是否合理：
												<label><input class="ace" id="u3897_input" value="0" name="ISCHECKTRUE" type="radio" <c:if test="${ISCHECKTRUE==0 }">checked</c:if> >合理&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
												<label><input class="ace" id="u3897_input" value="1" name="ISCHECKTRUE" type="radio" <c:if test="${ISCHECKTRUE==1 }">checked</c:if> >不合理&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
												<label><input class="ace" id="u3897_input" value="2" name="ISCHECKTRUE" type="radio" <c:if test="${ISCHECKTRUE==2 }">checked</c:if> >待定&nbsp;<span class="lbl"></span></label>&nbsp;&nbsp;
											</span>
											<c:if test="${modifyFlag==1 }">
												<a class="btn btn-minier btn-info" title="保存" onclick="saveIsCheckTrue()">
													<i class="ace-icon fa fa-floppy-o bigger-120"></i>
												</a>
											</c:if>
											<span style="text-align: left;vertical-align: middle;">
											<c:if test="${expert_name !=null and expert_name!='' }">
												&nbsp;&nbsp;&nbsp;&nbsp;<font color="#87b87f" >当前指定专家：${expert_name}</font>
											</c:if>
											</span>
											&nbsp;
											|
											&nbsp;
											<select  name="is_checkPeople" id="is_checkPeople"  style="vertical-align:top;width: 80px;">
									 		<option value="">全部审核</option> 
											<option <c:if test="${page.pd.is_checkPeople == '1' }">selected</c:if> value="1" >药师审核</option>
											<option <c:if test="${page.pd.is_checkPeople == '0' }">selected</c:if> value="0" >系统审核</option>
										    </select>
											
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
													<td style="width: 100px;" class='center' > ${rsTypeDict.get(rs.RS_DRUG_TYPE).rs_type_name }</td>
													<td >${rs.drug_id1_name } 
														<c:if test="${rsTypeDict.get(rs.RS_DRUG_TYPE).RS_COUNT == 2}"> 
														<font color="red">与</font> ${rs.drug_id2_name } 
														 </c:if>
													</td>
													<td style="width: 100px;text-align: right;" nowrap>提交日期：</td>
													<td style="width: 100px;text-align: left;vertical-align: middle;">${rs.CHECKDATE } </td>
													<td rowspan="2" style="width: 100px;text-align: center;vertical-align: middle;">
														<c:if test="${modifyFlag==1 }">
														<a class="btn btn-minier btn-danger"  onclick="delCheckRs('${rs.rs_id }');">
															<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
														</a>
														<a class="btn btn-minier btn-info"  onclick="editCheckRs('${rs.rs_id }');">
															<i class="ace-icon fa fa-pencil-square-o bigger-100 " title="修改"></i> 
														</a>
														</c:if>
													</td>
												</tr>
												<tr>
													<td style="text-align: right;" nowrap>问题描述：</td>
													<td  class="left"  colspan="1">&nbsp;&nbsp;&nbsp;&nbsp; ${rs.ALERT_HINT } </td>
													<td style="text-align: right;" nowrap>提交人：</td>
													<td class="left" >${rs.CHECKPEOPLE_NAME }</td>
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
	//var height = $(document).height();
	var w = '${checkRss==null?null:checkRss.size()}';
	if(w!=null){
		parent.$("#checkSize").text(w);
	}
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
	$('[data-rel=tooltip]').tooltip();
	$('[data-rel=popover]').popover({html:true});
	
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 

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
//单个删除
function delCheckRs(id){
	bootbox.confirm("确定删除该点评结果吗?", function(result) {
		if(result) {
			var url = path+"/DoctOrder/delCheckRs.do?RS_ID="+id;
			$.get(url,function(data){
				if(data.result=="success"){
					refreshDoctFrame();
					$("#checkForm").submit();
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
						refreshDoctFrame();
						$("#checkForm").submit();
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
	var url = path + "/DoctOrder/toAddCheckRs.do?business_type=0&ngroupnum="+ngroupnum+"&patient_id="+$("#patient_id").val()+"&visit_id="+$("#visit_id").val();
	//top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="编辑点评";
	diag.URL = url;
	diag.Width = 900;
	diag.Height =650;
	diag.CancelEvent = function(){ //关闭事件
		var ngroupnum = diag.innerFrame.contentWindow.document.getElementById('ngroupnum').value;
		$("#ngroupnum").val(ngroupnum);
		diag.close();
		//遮罩层控制，第三层弹窗使用
		top.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		refreshDoctFrame();
		//parent.DoctFrame.location.href = parent.$("#DoctFrame").attr("src")+ngroupnum;
		$("#checkForm").submit();
	};
	diag.show();
}
function editCheckRs(rs_id){
	var url = path + "/DoctOrder/toEditCheckRs.do?business_type=0&rs_id="+rs_id;
	//top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="编辑点评";
	diag.URL = url;
	diag.Width = 900;
	diag.Height =650;
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		refreshDoctFrame();
		top.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		$("#checkForm").submit();
	};
	diag.show();
	
	
}
function saveIsCheckTrue(){
	var checkstats = '${ISCHECKTRUE}';
	var msg = "";
	var ISCHECKTRUE = $("input[name='ISCHECKTRUE']:checked").val();
	if(ISCHECKTRUE==0){
		msg= "选择合理将清空所有点评结果，请确认？";
		/*/合理
		if(checkstats==0){
			$("#isCheck").tips({ side:3, msg:'已经为合理，无需再次保存', bg:'#AE81FF',  time:3   });
			return;
		}else{
			
		}*/
	}else if(ISCHECKTRUE==1){
		msg= "确定设置为不合理吗，请确认？";
		/*/不合理
		if(checkstats==1){
			$("#isCheck").tips({ side:3, msg:'已经为不合理，无需再次保存', bg:'#AE81FF',  time:3   });
			return;
		}*/
	}else if(ISCHECKTRUE==2){
		msg= "确定设置为待定吗，请确认？";
		/*/不合理
		if(checkstats==2){
			$("#isCheck").tips({ side:3, msg:'已经为待定，无需再次保存', bg:'#AE81FF',  time:3   });
			return;
		}*/
	}else{
		alert("请刷新页面重试！");
	}
	
	bootbox.confirm(msg, function(result) {
		if(result) {
			var url = path+"/DoctOrder/setCheckRsStatus.do?"+$("#checkForm").serialize();
			$.get(url,function(data){
				if(data.result=="success"){
					if(ISCHECKTRUE==0){
						refreshDoctFrame();
					}
					$("#checkForm").submit();
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
// 	专家点评
function expertOrders(){
	//top.jzts();
	//医嘱专家列表
	var url = path + "/expert/selectExpertList.do?business_type=0&IS_ORDERS=1"+"&patient_id="+$("#patient_id").val()+"&visit_id="+$("#visit_id").val();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="编辑点评";
	diag.URL = url;
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		top.$("#_DialogBGDiv").css("z-index",900).css("display","block");
		//刷新下方iframe列表
		refreshDoctFrame();
		$("#checkForm").submit();
	};
	diag.show();
}
function refreshAll(){
	//刷新下方iframe列表
	refreshDoctFrame();
	$("#checkForm").submit();
}

//function refreshDoctFramByNum()

function refreshDoctFrame(){
	var myngroupnum =  $("#ngroupnum").val(); 
	var orderUrl = "DoctOrder/DoctOrdersDetail.do?patient_id=${page.pd.patient_id}&visit_id=${page.pd.visit_id}&ngroupnum=" + myngroupnum;
	//刷新下方iframe列表
	//alert(orderUrl);
	//parent.$("#DoctFrame").attr("src",orderUrl);// = orderUrl ;  
	parent.frames["DoctFrame"].refreshPage(myngroupnum);
	//parent.DoctFrame.location.href = orderUrl;//parent.$("#DoctFrame").attr("src");
}
</script>
</html>