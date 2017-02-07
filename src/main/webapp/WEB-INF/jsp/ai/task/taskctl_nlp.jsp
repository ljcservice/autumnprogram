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
									<input type="hidden" name="TASK_ID" id="TASK_ID" value="${pd.TASK_ID }"/>
									<input type="hidden" name="Q_ID" id="Q_ID" value="${pd.Q_ID }"/>
									<input type="hidden" id="ctrl_flag" value="${pd.ctrl_flag }"><!-- 标志待处理页面入口 -->
									<input type="hidden" name="haveRs" id="haveRs" value="${haveRs }"/><!-- 是否包含处理结果的状态信息，1是0否 -->
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:110px;text-align: right;padding-top: 13px;">术语名称:</td>
											<td>
												<input type="text" name="DIAG_NAME" id="DIAG_NAME"  value="${pd.DIAG_NAME }"  maxlength="32" title="" style="width:79%;"/>
												<ts:rights code="taskQuery/submitTask">
													<div style="float:right">
														<label>下一条&nbsp;<input type='checkbox' class="ace" id="nextShow" <c:if test="${pd.nextShow==1 or pd.checkFlag==1}">checked="checked"</c:if>/><span class="lbl">&nbsp;&nbsp;&nbsp;</span></label>
														<a id="sub" style="margin-right:10px;" class="btn btn-mini <c:if test="${pd.EDIT_FLAG=='1'}">btn-success</c:if>" 
														<c:if test="${pd.EDIT_FLAG=='1'}"> onclick="submitRs();" </c:if>>提交</a>
													</div>
												</ts:rights>
											</td>
										</tr>
										<tr style="height:100px">
											<td style="width:110px;text-align: right;padding-top: 13px;">原始切分结果:</td>
											<td>
												<table id="t_old" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
													<thead>
														<tr>
															<th class="center">诊断名称</th>
															<th class="center">MTS匹配结果</th>
														</tr>
													</thead>
													<tbody>
													<!-- 开始循环 -->	
													<c:choose>
														<c:when test="${not empty diagNameList}">
															<c:forEach items="${diagNameList}" var="s" varStatus="vs">
																<tr>
																<!-- 	<td class='center' style="width: 30px;">${vs.index+1}</td>
																	<td class='center' style="width: 30px;">
																		<input type='checkbox' name='old_names' value=" " id="" class="ace"/><span class="lbl"></span>
																	</td>-->
																	<td class="center">${s.name }</td>
																	<td style="display:none">${s.mts_code}</td>
																	<td class="center">
																	<c:if test="${s.mts_code=='UNM' }">无</c:if>
																	<c:if test="${s.mts_code!='UNM' }">${s.mts_code }</c:if>
																	</td>
																</tr>
															</c:forEach>
														</c:when>
													</c:choose>
													<tr style="text-align: center;" >
														<td style="vertical-align:top;padding-top:4px;" colspan="2" >
															<c:if test="${not empty diagNameList}">
															<ts:rights code="NLP/splitTrue">
																<a class="btn btn-light btn-xs" <c:if test="${pd.EDIT_FLAG=='1'}">onclick="split_sure(1)"</c:if>  title="切分正确">
																<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>切分正确</a>
															</ts:rights>
															<ts:rights code="NLP/splitHand">
																<a  class="btn btn-light btn-xs" <c:if test="${pd.EDIT_FLAG=='1'}">onclick="reback()"</c:if>  title="人工切分">
																<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>人工切分</a>
															</ts:rights>
															</c:if>
															<ts:rights code="NLP/splitNlp">
																<a  class="btn btn-light btn-xs" <c:if test="${pd.EDIT_FLAG=='1'}">onclick="nlpCtrl()"</c:if>  title="调用NLP切分">
																<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>调用NLP切分</a>
															</ts:rights>
														</td>
													</tr>
													</tbody>
												</table>
											</td>
										</tr>
										<tr>
											<td style="width:110px;text-align: right;padding-top: 13px;">审核后NLP结果:</td>
											<td>
													<div class="input_div" id="aaaaaa" style="float:left;margin-right:3px">
													<!-- 开始循环 -->	
													<c:choose>
														<c:when test="${not empty NLPNameList}">
															<c:forEach items="${NLPNameList}" var="d" varStatus="vd">
																<!-- 	<td class='center' style="width: 30px;">${vs.index+1}</td>
																	<td class='center' style="width: 30px;">
																		<input type='checkbox' name='sure_names' value="" id="" class="ace"/><span class="lbl"></span>
																	</td> 
																	<td>
																	<div class="input_div" id="${vs.index+1}" style="float:left;margin-right:3px">
																	<input type="text" id="nlpname_id_${vs.index+1}"  name="nlpname_${vs.index+1}" value="${s.name }" 
																	<c:if test="${s.mts_code != ''&&s.mts_code!=null &&s.mts_code!='UNM'}">  readonly="readonly" </c:if> maxlength="32" title="" />
																	<input type="hidden"  id="nlpcode_id_${vs.index+1}" name="nlpcode_${vs.index+1}" value="${s.mts_code}"/>
																	</div>
																	<c:if test="${s.mts_code == ''||s.mts_code==null ||s.mts_code=='UNM'}">
																		<a  class="btn btn-xs btn-success" onclick="addName(${vs.index+1});">+</a>
																	</c:if>
																	</td>-->
																<input style='margin-bottom:5px;margin-right:3px;' class="canremove"  type="text" id="nlpname_${vd.index}"  name="nlpname" value="${d.name }"  maxlength="32" title="" />
																<a class="canremove" class='btn btn-xs btn-danger' id='a_${vd.index}' onclick='cancelAdd(${vd.index});'>X</a>
															</c:forEach>
														</c:when>
													</c:choose>
													</div>
													<a class="btn btn-xs btn-success" onclick="addName('');">+</a>
													<ts:rights code="NLP/saveNLP">
														<a  style="margin-left:5px" class="btn btn-light btn-xs" <c:if test="${pd.EDIT_FLAG=='1'}">onclick="split_sure(2)"</c:if>  title="保存">
														<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>保存</a>
													</ts:rights>
											</td>
										</tr>
									</table>
									</div>
									<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	</body>
<script type="text/javascript">
	$(top.hangge());
	//保存切分结果 1为切分正确2 为保存
	function split_sure(type){
		bootbox.confirm("确认要保存当前的切分结果么?", function(result) {
			if(result) {
				var TASK_ID=$("#TASK_ID").val();//任务的ID信息
				var Q_ID=$("#Q_ID").val();//问题单ID
				var SRC_DESC=$("#DIAG_NAME").val();//原始诊断名称
				var RIGHT_DESC=''//正确的描述
				var ERR_DESC='';//错误的描述
				var MTS_CODE='';//MTS状态
				
				var name_val = '';//原切分结果
				var code_val = '';//原MTS匹配编码
				var new_name_val='';//新的切分结果
				var new_code_val='';//新的MTS匹配编码
				var t_old=$("#t_old").find("tr");
				for(var i=1;i<t_old.length-1;i++){
					var td_name_temp=t_old.eq(i).find("td").eq(0).text().trim();
					var td_code_temp=t_old.eq(i).find("td").eq(1).text().trim();
					if (td_name_temp=='')
						continue;
					if(name_val=='')
						name_val=td_name_temp;
					else
						name_val=name_val+";"+td_name_temp;
					if(code_val=='')
						code_val=td_code_temp;
					else
						code_val=code_val+";"+td_code_temp;
				}
				RIGHT_DESC=name_val;
				MTS_CODE=code_val;
				if(type==2){//新的人工切分结果
					var temp_name=document.getElementsByName('nlpname');
					for(var j=0;j<temp_name.length;j++){
						var new_td_name_temp=temp_name[j].value.trim();
						if(new_td_name_temp=='')
							continue;
						else//校验词的正确性
						{
							var check_include_flag=isInclude(SRC_DESC,new_td_name_temp);
							if(!check_include_flag) {
								bootbox.dialog({
									message: "<span class='bigger-110'>新的切分词，不是源术语的部分,请重新处理!</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
								});
								return ;
							}
						}
						if(new_name_val=='')
							new_name_val=new_td_name_temp;
						else
							new_name_val=new_name_val+";"+new_td_name_temp;
					}
					//设置新值
					RIGHT_DESC=new_name_val;
					//后台做处理
					MTS_CODE="";
					ERR_DESC=name_val;
					//校验值的合法性，判断原始诊断字符与新切分结果除了符号外是否相同
					var check_equal_flag=isEqual(SRC_DESC,RIGHT_DESC);
					if(!check_equal_flag) {
						bootbox.dialog({
							message: "<span class='bigger-110'>新的切分结果长度不等于源术语词的总长度,请重新处理!</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
						return ;
					}
				}
				$.ajax({
					type: "POST",
					url: '<%=basePath%>taskQuery/saveNLP.do?tm='+new Date().getTime(),
			    	data: {TYPE:type,RIGHT_DESC:RIGHT_DESC,MTS_CODE:MTS_CODE,ERR_DESC:ERR_DESC,TASK_ID:TASK_ID,Q_ID:Q_ID,SRC_DESC:SRC_DESC},
					dataType:'json',
					cache: false,
					success: function(data){
						if(data.result=="success"){
							var checkFlag=0;
							if($("#nextShow").prop("checked")){
								checkFlag=1;
							}
							//刷新当前的页面
							self.location = path+'/taskQuery/toCtl.do?task_id='+$("#TASK_ID").val()+'&checkFlag='+checkFlag+'&ctrl_flag='+$("#ctrl_flag").val();
							//top.Dialog.close();
						}else{
							bootbox.dialog({
								message: "<span class='bigger-110'>"+data.result+"</span>",
								buttons: 			
								{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
							});
						}
					}
				});
			}
		});	
	}
	//判断两个字符串长度是否一致 
	function isEqual(str1,str2){
		var tefu = "#(（）)+,，.。;；？?!！、“'‘\"/| \t\n，，．。；；？？！！、＂”＇’＼＼＂“／、｜｜　　";
		var  tes  = new Array();
		tes=tefu.split("");
		for(i=0;i<tes.length;i++){
			str1=str1.replaceAll("["+tes[i]+"]", "");
			str2=str2.replaceAll("["+tes[i]+"]", "");
		}
		//由于根据空格拆分的  所以空格没剔除掉 再replace下 
		str1=str1.replaceAll("[ ]", "");
		str2=str2.replaceAll("[ ]", "");
		var lenstr1 = $.trim(str1).length;
		var lenstr2 = $.trim(str2).length;
		if(lenstr1==lenstr2){
			return true;
		}
		return false;
	}
	//判断字符串是否在另一个字符串中
	function isInclude(str1,str2){
		var tefu = "#(（）)+,，.。;；？?!！、“'‘\"/| \t\n，，．。；；？？！！、＂”＇’＼＼＂“／、｜｜　　";
		var  tes  = new Array();
		tes=tefu.split("");
		for(i=0;i<tes.length;i++){
			str1=str1.replaceAll("["+tes[i]+"]", "");
			str2=str2.replaceAll("["+tes[i]+"]", "");
		}
		//由于根据空格拆分的  所以空格没剔除掉 再replace下 
		str1=str1.replaceAll("[ ]", "");
		str2=str2.replaceAll("[ ]", "");
		var ishave = str1.indexOf(str2);
		if(ishave != -1){
			return true;
		}
		return false;
	}
	String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
	    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
	        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
	    } else {  
	        return this.replace(reallyDo, replaceWith);  
	    }  
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
		if($("#P_ID").val()!=""){
			$("#TASK_TYPE_ID").attr("disabled","disabled");
			$("#TASK_TYPE_ID").css("color","gray");
			
			$("#TASK_TYPE_CHILD_ID").attr("disabled","disabled");
			$("#TASK_TYPE_CHILD_ID").css("color","gray");
			$("#TASK_TYPE_ID").tips({
				side:3,
	            msg:'任务类型不可更改',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TASK_TYPE_CHILD_ID").tips({
				side:3,
	            msg:'任务子类型不可更改',
	            bg:'#AE81FF',
	            time:3
	        });
			
		}
	});
	
	var num=document.getElementsByName('nlpname').length;
	//增加文本框
	function addName(val){
		$("#aaaaaa").append($("<input style='margin-bottom:5px;margin-right:3px;' class='canremove' type='text' id='nlpname_"+num+"' value='"+val+"'  name='nlpname' maxlength='32' style='margin-right:3px' /><input type='hidden' class='canremove' id='nlpcode_"+num+"' name='nlpcode' value='UNM'/><a class='canremove' class='btn btn-xs btn-danger' id='a_"+num+"' onclick='cancelAdd("+num+");'>X</a>"
		));
		num=num+1;
		
	}
	//删除增加的框框
	function cancelAdd(num){
		$("#nlpname_"+num+"").remove();
		$("#a_"+num+"").remove();
	}

	//还原或人工切分执行的数据恢复为原来的结果
	function reback(){
		var t_old=$("#t_old").find("tr");
		//将原来存在的先删除
		$(".canremove").remove();
		num=0;
		for(var i=1;i<t_old.length-1;i++){
			var line_val=t_old.eq(i).find("td").eq(0).text();
			addName(line_val);
		}
	}
	
	//调用NLP的切词
	function nlpCtrl(){
		var DIAG_NAME=$("#DIAG_NAME").val();
		$.ajax({
			type: "POST",
			url: '<%=basePath%>taskQuery/nlpCtrl.do?tm='+new Date().getTime(),
	    	data: {DIAG_NAME:DIAG_NAME},
			dataType:'json',
			cache: false,
			success: function(data){
				if(data.result=="success"){
					//获取NLP切词成功
					var nlp_str=data.nlp_str;
					if(nlp_str!=null){
						//将原来存在的先删除
						$(".canremove").remove();
						num=0;
						nlps=nlp_str.split(";");
						for (var int = 0; int < nlps.length; int++) {
							var nlp_element = nlps[int];
							addName(nlp_element);
						}
					}
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}
			}
		});
	}
	//任务提交
	function submitRs(){
		var flag = $("#nextShow").prop("checked");//是否选择下一条
		var haveRs=$("#haveRs").val();
		if(haveRs==0){
			bootbox.dialog({
				message: "<span class='bigger-110'>还未包含处理信息，请处理后再提交!</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}else{
			//提交当前的任务信息，且判断下一条是否选择
			bootbox.confirm("确认要提交当前任务的处理结果吗?", function(result) {
				if(result) {
					var TASK_ID=$("#TASK_ID").val();
					$.ajax({
						type: "POST",
						url: '<%=basePath%>taskQuery/submitTaskAll.do?tm='+new Date().getTime(),
				    	data: {TASK_IDS:TASK_ID,STEP:2},//NLP审核只有二审
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							if(data.result=="success"){
								//刷新当前的页面	self.location = path+'/taskQuery/toCtl.do?task_id='+$("#TASK_ID").val()+"&task_stat=2";
								if(flag){
									var searchForm ;
									if($("#ctrl_flag").val()==1){//待处理页入口
										searchForm=parent.frames[0].$("iframe[src='taskQuery/listTask4Ctrl.do']")[0].contentWindow.document.taskQueryForm;
										//显示下一条
										self.location = path+'/taskQuery/toCtl.do?nextShow=1&ctrl_flag=1&'+ $(searchForm).serialize();
									}else{//任务查询页面入口
										searchForm=parent.frames[0].$("iframe[src='taskQuery/listTask.do']")[0].contentWindow.document.taskQueryForm
										//显示下一条
										self.location = path+'/taskQuery/toCtl.do?nextShow=1&'+ $(searchForm).serialize();
									}
								}else{
									//document.getElementById("zhongxin").style.display = 'none';
									top.Dialog.close();
								}
							}else{
								bootbox.dialog({
									message: "<span class='bigger-110'>"+data.result+"</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
								});
							}
						}
					});
				}
			});
		}
	}
</script>
</html>