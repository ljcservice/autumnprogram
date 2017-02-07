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
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
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
								<form action="osyn/saveDiagOsyn.do" name="diagOsynForm" id="diagOsynForm" method="post">
									<input type="hidden" name="H_ID" id="H_ID" value=""/>
									<input type="hidden" name="STATUS" id="STATUS" value="0"/>
									<input type="hidden" name="DN_ID" id="DN_ID" value="${pd.SP_ID }"/>
									<input type="hidden" name="ONTO_TYPE" id="ONTO_TYPE" value="${pd.termCategory }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="text-align: right;padding-top: 13px;">是否为标准:</td>
											<td colspan="3">
												<input id="u3897_input" value="1" name="WORD_TYPE" type="radio" <c:if test="${pd.wordType==1 }">checked</c:if> >是&nbsp;
												<input id="u3897_input" value="0" name="WORD_TYPE" type="radio" <c:if test="${pd.wordType==0 }">checked</c:if>>否 
											</td>
										</tr>
										<tr>
											<td width="19%" style="text-align: right;padding-top: 13px;">药品规格:</td>
											<td width="31%" ><input type="text" name="DN_CHN" id="DN_CHN" maxlength="255" placeholder="输入药品规格" onblur="checkName(this,'DN_CHN');" value="${pd.DRUG_SP}" style="width:98%;"/></td>
											<td width="19%" style="text-align: right;padding-top: 13px;">来源药品规格:</td>
											<td width="31%" >
												<input type="text" name="ORG_DN_CHN" id="ORG_DN_CHN"  value="${pd.ORG_DRUG_SP }"  maxlength="255" placeholder="输入来源药品规格" style="width:98%;"/>
												<input type="hidden" name="STAD_DN_ID" id="STAD_DN_ID"  value="${pd.DRUG_STAD_ID }"  maxlength="255" style="width:70%;"/><!-- 标准名称ID -->
											</td>
										</tr>
										
										<!-- 若新增的词语为同义词，则需要获取以下信息 -->
										<tr id="stad1">
											<td style="width:130px;text-align: right;padding-top: 13px;">药品标准规格:</td>
											<td>
												<input type="text" name="STAD_CHN" id="STAD_CHN"  value="${pd.DRUG_STAD }"  maxlength="255" placeholder="选择药品标准规格" style="width:85%;" readonly/>
												<a onclick="selectDiag();">选择</a>	
											</td>
											<td style="width:130px;text-align: right;padding-top: 13px;">来源标准药品规格:</td>
											<td><input type="text" name="ORG_STAD_CHN" id="ORG_STAD_CHN" value="${pd.ORG_DRUG_STAD_SP }" placeholder="来源标准药品规格-选择" maxlength="300" title="备注" style="width:98%;" readonly/></td>
										</tr>
											
										<tr>
											<td style="width:130px;text-align: right;padding-top: 13px;">停用标记:</td>
											<td>
												<input id="u3897_input" value="0" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==0 || pd.IS_DISABLE==null }">checked</c:if>>否 &nbsp;
												<input id="u3897_input" value="1" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE==1 }">checked</c:if>>是
											</td>
											<td></td>
											<td></td>
										</tr>
										<tr>
											<td style="width:130px;text-align: right;padding-top: 13px;">停用描述:</td>
											<td colspan="3">
												<input type="text" name="DESCRIPTION" id="DESCRIPTION" value="${pd.DESCRIPTION }" placeholder="若为停用状态请在此添加说明" maxlength="300" title="停用描述" style="width:98%;"/>
												<input type="hidden" value="${upuser.NAME }" name="UPDATE_MAN"/>
											</td>
										</tr>
										<tr>
											<td style="width:130px;text-align: right;padding-top: 13px;">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
											<td colspan="3">
												<input type="text" name="DRUG_MEMO" id="DRUG_MEMO" value="${pd.REMARK }" placeholder="输入备注信息" maxlength="300" title="备注" style="width:98%;"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
												<c:if test="${MSG=='saveDiagOsyn'}">
													<input type="hidden" name="OP_TYPE" value="0"/><!-- 新增 -->
													<input type="hidden" name="UPD_DESC" value="新增药品规格"/>
													<ts:rights code="OSYN_ADD_${RIGHTS}"><a class="btn btn-mini btn-success" onclick="save();">保存</a></ts:rights>
												</c:if>
												<c:if test="${MSG=='edit'}">
													<input type="hidden" name="OP_TYPE" value="1"/><!-- 修改 -->
													<ts:rights code="OSYN_EDIT_${RIGHTS}"><a class="btn btn-mini btn-success" onclick="save();">保存</a></ts:rights>
													<a class="btn btn-mini btn-primary" onclick="searchHist('${pd.SP_ID}','${pd.termCategory}');">查看历史记录</a>
												</c:if>
												<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											</td>
										</tr>
									</table>
									</div>
									<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/bootbox.js"></script>
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript">
	$(top.hangge());
	$(document).ready(function(){
		if($("#user_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}
		if('${pd.wordType}'=='0'){
			$("#stad1").show();
		}else{
			$("#stad1").hide();
		}
	});
	//判断是否为空
	function checkData(){
		var flag = true;
		if($("#DN_CHN").val()==null ||$.trim($("#DN_CHN").val())=="" ){
			$("#DN_CHN").tips({ side:3, msg:'请输入药品规格', bg:'#AE81FF',  time:3   });
			flag = false;
		}
		if($("#DN_CHN").val()!=null ||$.trim($("#DN_CHN").val())!="" ){
			if(!checkName($("#DN_CHN"),'DN_CHN')){
				flag = false;
			}
		}
		
		if($("input[name='IS_DISABLE']:checked").val()==null ||$.trim($("input[name='IS_DISABLE']:checked").val())=="" ){
			$("input[name='IS_DISABLE']:first").tips({ side:3, msg:'请选择是否停用', bg:'#AE81FF',  time:3   });
			flag = false;
		}
		if($("input[name='IS_DISABLE']:checked").val()==1){
			if($("#DESCRIPTION").val()==""){
				$("#DESCRIPTION").tips({ side:3, msg:'请输入停用描述', bg:'#AE81FF',  time:3   });
				flag = false;
			}
		}
		if($("input[name='WORD_TYPE']:checked").val()==0){
			if($("#STAD_CHN").val()==null || $.trim($("input[name='IS_DISABLE']:checked").val())==""){
				$("#STAD_CHN").tips({ side:3, msg:'请选择药品标准规格', bg:'#AE81FF',  time:3   });
			}
		}
		return flag;
	}
	//保存
	function save(){
		if(!checkData()){
			return;
		}
		$("#zhongxin").hide();
		$("#zhongxin2").show();
		$.ajax({
			type: "POST",
			url: basePath+$("#diagOsynForm").attr("action"),
	    	data: $("#diagOsynForm").serialize(),
			dataType:'json',
			async:false,
			cache: false,
			success: function(data){
				if(data.result=="success"){
					top.Dialog.close();
				 	bootbox.dialog({
						message: "<span class='bigger-110'>保存成功，请到本体审核页面查看。</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
					//查询失败
					$("#zhongxin").show();
					$("#zhongxin2").hide();
				}
			}
		});
	}
	
	$(function() {
		//下拉框
		if(!ace.vars['touch']) {
			$('.chosen-select').chosen({allow_single_deselect:true}); 
			$(window)
			.off('resize.chosen')
			.on('resize.chosen', function() {
				$('.chosen-select').each(function() {
					 var $this = $(this);
					 $this.next().css({'width': $this.parent().width()});
				});
			}).trigger('resize.chosen');
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
		
		//radio change事件
		$("input:radio[name='WORD_TYPE']").change(function(){
			var type = $(this).val();
			if(type==0){
				$("#stad1").show()
			}else{
				$("#stad1").hide()
			}
		})
	});
	
	
	//标准药品规格弹出框
	function selectDiag(){
		top.jzts();
		var diag = new top.Dialog();
		diag.Drag=true;
		var ontoType=$("#ONTO_TYPE").val();
		diag.Title ="标准药品规格";
		diag.URL = '<%=path%>/standard/osynStand.do?ontoType='+ontoType;
		diag.Width = 1000;
	 	diag.Height = 600;
		diag.CancelEvent = function(){ //关闭事件
			if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location=self.location",100);
				 }else{
					 nextPage(${page.currentPage});
				 }
			}
		
		diag.close();
		//遮罩层显示
//	 	$("#_DialogBGDiv").css("z-index","900").attr("display","");
		
		};
		diag.show();
	}
	
	//检查名称是否存在
	function checkName(obj,name){
		if($.trim($(obj).val())==""){
			return false;
		}
		var flag = true;
		var mydata = null;
		var standFlag = $("input[name='WORD_TYPE']:checked").val();
		if(name=='DN_CHN') {
			mydata = {DN_CHN:$.trim($(obj).val()),ontoType:$("#ONTO_TYPE").val(),standFlag:standFlag,DN_ID:$("#DN_ID").val()};
		}else{
			mydata = {DN_ENG:$.trim($(obj).val()),ontoType:$("#ONTO_TYPE").val(),standFlag:standFlag,DN_ID:$("#DN_ID").val()};
		}
		$.ajax({
			type: "POST",
			url: basePath+'/osyn/checkExistName.do',
	    	data: mydata,
			dataType:'json',
			async:false,
			cache: false,
			success: function(data){
				if(data.result=="success"){
				
				}else{
					$(obj).tips({ side:3, msg:data.result, bg:'#AE81FF',  time:3   });
					flag = false;
				}
			}
		});
		return flag;
	}
	
	//查询同义词变更历史
	function searchHist(dn_id,ontoType){
		top.jzts();
		var diag = new top.Dialog();
		diag.Drag=true;
		diag.Title ="查看历史记录";
		diag.URL = '<%=path%>/osynHis/nameHist.do?DN_ID='+dn_id+'&ONTO_TYPE='+ontoType;
		diag.Width = 1200;
		diag.Height = 500;
		diag.CancelEvent = function(){ //关闭事件
			if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location=self.location",100);
				 }else{
					 nextPage(${page.currentPage});
				 }
			}
			diag.close();
		 };
		 diag.show();
	}
</script>
</html>