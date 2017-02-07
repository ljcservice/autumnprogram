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
<%@ include file="../../../system/index/top.jsp"%>
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
									<input type="hidden" name="DN_ID" id="DN_ID" value="${pd.ID }"/>
									<input type="hidden" name="ONTO_TYPE" id="ONTO_TYPE" value="${pd.termCategory }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<!-- <c:if test="${MSG=='saveDiagOsyn'}">
										
											<tr>
												<td style="width:130px;text-align: right;padding-top: 13px;">是否为标准:</td>
												<td>
													<input id="u3897_input" value="1" name="WORD_TYPE" type="radio" <c:if test="${pd.wordType==1 }">checked</c:if> >是&nbsp;
													<input id="u3897_input" value="0" name="WORD_TYPE" type="radio" <c:if test="${pd.wordType==0 }">checked</c:if>>否 
												</td>
											</tr>
										</c:if> -->
										<!-- 
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">词语类型:</td>
											<td>
												<select class="chosen-select form-control" name="ONTO_TYPE" id="ONTO_TYPE" style="vertical-align:top;width: 98%;">
											 		<option value=""></option>
											 		<option value="10301" <c:if test="${pd.termCategory==10301}">selected</c:if>>诊断名称同义词</option>
													<option value="10501" <c:if test="${pd.termCategory==10501}">selected</c:if>>手术名称同义词</option>
													
													<option value="10402" <c:if test="${pd.termCategory==10402}">selected</c:if>>药品通用名 </option>
													<option value="10403" <c:if test="${pd.termCategory==10403}">selected</c:if>>药品产品名 </option>
													<option value="10404" <c:if test="${pd.termCategory==10404}">selected</c:if>>药品商品名 </option>
													<option value="10405" <c:if test="${pd.termCategory==10405}">selected</c:if>>药品化学名 </option>
													
													<option value="10804" <c:if test="${pd.termCategory==10804}">selected</c:if>>药品分包装企业 </option>
													<option value="10801" <c:if test="${pd.termCategory==10801}">selected</c:if>>药品生产企业 </option>
													
													<option value="10101" <c:if test="${pd.termCategory==10101}">selected</c:if>>剂型名称 </option>
													<option value="10202" <c:if test="${pd.termCategory==10202}">selected</c:if>>药品包装规格 </option>
													<option value="10204" <c:if test="${pd.termCategory==10204}">selected</c:if>>药品规格表 </option>
													<option value="10205" <c:if test="${pd.termCategory==10205}">selected</c:if>>药品包装材质表 </option>
													<option value="10206" <c:if test="${pd.termCategory==10206}">selected</c:if>>药品规格单位表 </option>
													<option value="10201" <c:if test="${pd.termCategory==10201}">selected</c:if>>药品装量规格 </option>
													<option value="10203" <c:if test="${pd.termCategory==10203}">selected</c:if>>制剂最小单位</option>
												</select>
											</td>
										</tr>
										 -->
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">术语名称-中文:</td>
											<td><input type="text" name="DN_CHN" id="DN_CHN" maxlength="255" placeholder="输入术语名称中文" value="${pd.DEP_NAME }" onblur="checkName(this,'DN_CHN');" style="width:98%;"/></td>
										</tr>
										
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">术语规范名称:</td>
											<td><input type="hidden" name="STAD_DN_ID" id="STAD_DN_ID"  value="${pd.STAD_ID }"  maxlength="50" style="width:70%;"/>
												<input type="text" name="SHOW_NAME" id="SHOW_NAME"  value="${pd.STANDARD_NAME }"  maxlength="255" placeholder="选择术语规范名称" style="width:90%;"/>
												<a id="selectStad" onclick="selectDiag();">选择</a>
											</td>
										</tr>
										
										<!-- 如果添加或修改的是同义词，则显示同义词类型选择框 -->
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">同义词类型:</td>
											<td>
												<select name="SYNO_TYPE" id="SYNO_TYPE" data-placeholder="同义词类型" style="width:98%;">
													<option value=""></option>
													<option value="23101" <c:if test="${pd.SYNO_TYPE==23101}">selected</c:if>>俗语</option>
													<option value="23102" <c:if test="${pd.SYNO_TYPE==23102}">selected</c:if>>缩略语</option>
													<option value="23103" <c:if test="${pd.SYNO_TYPE==23103}">selected</c:if>>同音字/错别字</option>
													<option value="23104" <c:if test="${pd.SYNO_TYPE==23104}">selected</c:if>>拼音首字母</option>
													<option value="23105" <c:if test="${pd.SYNO_TYPE==23105}">selected</c:if>>语用同义词</option>
													<option value="23106" <c:if test="${pd.SYNO_TYPE==23106}">selected</c:if>>专指同义词</option>
													<option value="23107" <c:if test="${pd.SYNO_TYPE==23107||pd.SYNO_TYPE==null}">selected</c:if>>其它</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">停用标记:</td>
											<td>
												<input id="u3897_input" value="0" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE=='0' || IS_DISABLE==null }">checked</c:if>>否 &nbsp;
												<input id="u3897_input" value="1" name="IS_DISABLE" type="radio" <c:if test="${pd.IS_DISABLE=='1' }">checked</c:if>>是
											</td>
										</tr>
										<tr id="stopDesc">
											<td style="width:79px;text-align: right;padding-top: 13px;">停用描述:</td>
											<td>
												<input type="text" name="DESCRIPTION" id="DESCRIPTION" value="${pd.DESCRIPTION }" placeholder="若为停用状态请在此添加说明" maxlength="300" title="停用描述" style="width:98%;"/>
												<input type="hidden" value="${upuser.NAME }" name="UPDATE_MAN"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
												<c:if test="${MSG=='saveDiagOsyn'}">
													<input type="hidden" name="OP_TYPE" value="0"/><!-- 新增 -->
													<c:if test="${pd.wordType==1 }">
														<input type="hidden" name="UPD_DESC" value="新增标准词"/>
													</c:if>
													<c:if test="${pd.wordType==0 }">
														<input type="hidden" name="UPD_DESC" value="新增同义词"/>
													</c:if>
													<ts:rights code="OSYN_ADD_${RIGHTS}"><a class="btn btn-mini btn-success" onclick="save();">保存</a></ts:rights>
												</c:if>
												<c:if test="${MSG=='edit'}">
													<input type="hidden" name="OP_TYPE" value="1"/><!-- 修改 -->
													<c:if test="${pd.wordType==1 }">
														<input type="hidden" name="UPD_DESC" value="修改标准词"/>
													</c:if>
													<c:if test="${pd.wordType==0 }">
														<input type="hidden" name="UPD_DESC" value="修改同义词"/>
													</c:if>
													<ts:rights code="OSYN_EDIT_${RIGHTS}"><a class="btn btn-mini btn-success" onclick="save();">保存</a></ts:rights>
													<a class="btn btn-mini btn-primary" onclick="searchHist('${pd.DN_ID}');">查看历史记录</a>
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
	<%@ include file="../../../system/index/foot.jsp"%>
		<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
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
		if('${pd.IS_DISABLE}'=='0'||'${pd.IS_DISABLE==null}'){
			$("#stopDesc").hide();
		}else{
			$("#stopDesc").show();
		}
	});
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
	
		$("input:radio[name='IS_DISABLE']").change(function(){
			var type = $(this).val();
			if(type==0){
				$("#stopDesc").hide();
				checkDisable();
			}else{
				$("#stopDesc").show();
			}
		})
	});
	
	
	//标准诊断名称弹出框
	function selectDiag(){
		top.jzts();
		var diag = new top.Dialog();
		diag.Drag=true;
		var ontoType=$("#ONTO_TYPE").val();
		diag.Title ="标准科室名称";
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
	
	function checkData(){
		var flag = true;
		if($("#DN_CHN").val()==null ||$.trim($("#DN_CHN").val())=="" ){
			$("#DN_CHN").tips({ side:3, msg:'输入术语名称中文', bg:'#AE81FF',  time:3   });
			flag = false;
		}
		if($("#DN_CHN").val()!=null ||$.trim($("#DN_CHN").val())!="" ){
			if(!checkName($("#DN_CHN"),'DN_CHN')){
				$("#DN_CHN").tips({ side:3, msg:'名称已存在', bg:'#AE81FF',  time:3   });
				flag = false;
			}
		}
		if($("input[name='IS_DISABLE']:checked").val()==null ||$.trim($("input[name='IS_DISABLE']:checked").val())=="" ){
			$("input[name='IS_DISABLE']").tips({ side:3, msg:'选择是否停用', bg:'#AE81FF',  time:3   });
			flag = false;
		}
		if($("input[name='IS_DISABLE']:checked").val()==1){
			if($("#DESCRIPTION").val()==""){
				$("#DESCRIPTION").tips({ side:3, msg:'请输入停用描述', bg:'#AE81FF',  time:3   });
				flag = false;
			}
		}
		if($("input[name='IS_DISABLE']:checked").val()==1){
			if(!checkDisable){
				$("input:radio[name='IS_DISABLE'][value='0']").tips({ side:3, msg:data.result, bg:'#AE81FF',  time:3});
				flag=false;
			}
		}
		return flag;
	}
	//检验本体是否停用
	function checkDisable(){
		var flag = true;
		var mydata = {osynType:$("#ONTO_TYPE").val(),DN_ID:$("#STAD_DN_ID").val()};
		$.ajax({
			type: "POST",
			url: basePath+'/osyn/isOntoDisable.do',
	    	data: mydata,
			dataType:'json',
			async:false,
			cache: false,
			success: function(data){
				if(data.result=="success"){
				
				}else{
					$("input:radio[name='IS_DISABLE'][value='0']").attr("checked",false);
					$("input:radio[name='IS_DISABLE'][value='1']").attr("checked",true);
					$("input:radio[name='IS_DISABLE'][value='0']").tips({ side:3, msg:data.result, bg:'#AE81FF',  time:3   });
					flag = false;
				}
			}
		});
		return flag;
	}
	
	//查询同义词变更历史
	function searchHist(dn_id){
		top.jzts();
		var ontoType=$("#ONTO_TYPE").val();
		var diag = new top.Dialog();
		diag.Drag=true;
		diag.Title ="查看历史记录";
		diag.URL = '<%=path%>/alterHist/nameHist.do?DN_ID='+dn_id+'&ONTO_TYPE='+ontoType;
		 diag.Width = $(top.window).width()-200;
		 diag.Height = $(top.window).height()-100;
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