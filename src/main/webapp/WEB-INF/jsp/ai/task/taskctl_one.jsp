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
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css?v=11122"/>
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.exedit.min.js"></script>

<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<link type="text/css" rel="stylesheet" href="static/css/ontology.css?v=2016"/>
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12" style="margin-bottom:10px">
							<div id="zhongxin" style="padding-top: 13px;pagging-bottom:5px">
								<input type="hidden" name="TASK_ID" id="TASK_ID" value="${pd.TASK_ID }"/>
								<input type="hidden" name="ontoType" id="ontoType" value="51005"/><!-- 诊断本体 -->
								<input type="hidden" name="AIQuery" id="AIQuery" value="1"/><!-- 诊断本体 -->
								<input type="hidden" id="ctrl_flag" value="${pd.ctrl_flag }"><!-- 标志待处理页面入口 -->
								<input type="hidden" name="STEP" id="STEP" value="${pd.STEP }"/>
								<input type="hidden" name="DIAG_NAME" id="DIAG_NAME" value="${pd.DIAG_NAME }"/>
								<input type="hidden" name="Q_ID" id="Q_ID" value="${pd.Q_ID }"/>
								<span style="display:inlie-block;padding-left:1px;">术语ID: </span>
								<span style="display:inlie-block;padding-left:10px;">${pd.DIAG_ID }</span>
								<span style="display:inlie-block;padding-left:100px;">术语名称: </span>
								<span style="display:inlie-block;padding-left:10px;">${pd.DIAG_NAME }</span>
								<ts:rights code="taskQuery/submitTask">
									<div style="float:right">
										<label>下一条&nbsp;<input type='checkbox' class="ace" id="nextShow" <c:if test="${pd.nextShow==1}">checked="checked"</c:if>/><span class="lbl">&nbsp;&nbsp;&nbsp;</span></label>
										<a id="sub" style="margin-right:10px;" class="btn btn-mini <c:if test="${pd.EDIT_FLAG=='1'}">btn-success</c:if>" 
										<c:if test="${pd.EDIT_FLAG=='1'}"> onclick="submitRs();" </c:if>>提交</a>
									</div>
								</ts:rights>
							</div>
						</div>
						<c:if test="${pd.STEP==2 }"><!-- 二审的一审处理结果列表 -->
							<hr style="padding: 0px;margin:10px;" width="100%"/>
							<iframe name="oneRsFrame" id="oneRsFrame" frameborder="0" src="<%=basePath%>taskQuery/diagOneRs.do?TASK_ID=${pd.TASK_ID}&EDIT_FLAG=${pd.EDIT_FLAG}" style="margin:0 auto;width:100%;"></iframe>
						</c:if>
						<!-- 当前处理结果 -->
						<iframe name="rsFrame" id="rsFrame" frameborder="0" src="<%=basePath%>taskQuery/diagRs.do?TASK_ID=${pd.TASK_ID}&EDIT_FLAG=${pd.EDIT_FLAG}" style="margin:0 auto;width:100%;height:130px"></iframe>
						<!-- 树及列表信息 -->
						<div class="col-xs-12" style="margin-bottom:10px">
							<table style="width:100%;" border="0">
								<tr>
									<td valign="top" bgcolor="#F9F9F9">
										<div style='overflow: scroll;max-width: 350px;max-height: 600px;'>
										<div id="treeName">本体树</div>
										<div id="treeId">
											<ul id="leftTree" class="ztree"></ul>
										</div>
										</div>
									</td>
									<td style="width:75%;"  valign="top" >
										<div style="float:left;margin-left:13px;margin-right:13px;height: 35px;width:76%" class="shadow" id="selectedCont" >
										</div>
										<ts:rights code="taskQuery/toXWC">
											<a style="margin-top:5px" class="btn btn-mini <c:if test="${pd.EDIT_FLAG=='1'}"> btn-info</c:if>" 
												<c:if test="${pd.EDIT_FLAG=='1'}">onclick="xwc();"</c:if>>下位词</a>
										</ts:rights>
										<ts:rights code="taskQuery/toDiagChange">
											<a  style="margin-top:5px" class="btn btn-mini <c:if test="${pd.EDIT_FLAG=='1'}"> btn-warning</c:if>" 
												<c:if test="${pd.EDIT_FLAG=='1'}">onclick="gnzh();"</c:if>>概念转换</a>
										</ts:rights>
										<ts:rights code="taskQuery/toNonTerm">
											<a  style="margin-top:5px" class="btn btn-mini <c:if test="${pd.EDIT_FLAG=='1'}">btn-danger</c:if>" 
												<c:if test="${pd.EDIT_FLAG=='1'}">onclick="nonTerm()" </c:if>>无法干预</a>
										</ts:rights>
										<iframe name="treeFrame" id="treeFrame" frameborder="0" src="<%=basePath%>taskQuery/ontoDiagList.do?DIAG_NAME=${pd.DIAG_NAME}&EDIT_FLAG=${pd.EDIT_FLAG}&TYPE=0&TASK_ID=${pd.TASK_ID}&STEP=${pd.STEP}&Q_ID=${pd.Q_ID}" style="margin:0 auto;width:100%;"></iframe>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- /.main-content -->
	</div>
	<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

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
<script type="text/javascript" src="static/js/ontology/tree.fixer.js?v=2008"></script>
<script type="text/javascript" src="static/js/ontology/tree.js?v=2018008"></script>
<script type="text/javascript" src="static/js/ontology/edit.js?v=2018009"></script>
<script type="text/javascript">
	//刷新任务处理结果信息
	function refreshPage(){
		document.getElementById('rsFrame').contentWindow.location.reload(true);
	}
	//任务提交
	function submitRs(){
		var flag = $("#nextShow").prop("checked");
		//校验已经包含处理的结果才能够提交
		var have_Flag=document.getElementById('rsFrame').contentWindow.haveRs();
		if(have_Flag==0){
			$("#sub").tips({
				side:3,
	            msg:'请处理后再提交',
	            bg:'#AE81FF',
	            time:8
	        });
			return;
		}else{
			bootbox.confirm("确认要提交当前任务的处理结果吗?", function(result) {
				if(result) {
					var TASK_ID=$("#TASK_ID").val();
					var STEP=$("#STEP").val();
					$.ajax({
						type: "POST",
						url: '<%=basePath%>taskQuery/submitTaskAll.do?tm='+new Date().getTime(),
				    	data: {TASK_IDS:TASK_ID,STEP:STEP},
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
									//var searchForm = parent.frames[0].$("iframe[src='taskQuery/listTask.do']")[0].contentWindow.document.taskQueryForm;
									
								}else{
									document.getElementById("zhongxin").style.display = 'none';
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

	var mytimer = 10;
	var runing = false;
	function timerClose(flag){
		if(!flag && runing){
			return;
		}
		runing = true;
		if(mytimer<=0){
			$("#detailInfo").hide("fast");
			runing = false;
		}else{
			mytimer = mytimer-1;
			setTimeout(function(){timerClose(true);},1000);
		}

	}


	//设置可以选择的节点背景颜色
	function onExpand(event, treeId, treeNode) {
		//谷歌360自动给这个ul加上了隐藏属性，导致不显示子节点。火狐没问题
		$("#"+treeNode.tId+"_ul").css("overflow","visible");

		if(treeNode.getParentNode()==null){
			if(treeNode.MAIN_CODE!=null&&treeNode.MAIN_CODE!=''){
				treeNode.main_show=1;
			}
			if(treeNode.ADD_CODE!=null&&treeNode.ADD_CODE!=''){
				treeNode.add_show=1;
			}
		}
		var children = treeNode.children;
		var len = children.length;
		for(var i=0;i<len;i++){
			var c = children[i];
			if(c.IS_DISABLE==1){
				$("#"+c.tId+"_span").css("text-decoration","line-through").css("color","red").attr("title","已经停用");
			}else{
				if(c.MAIN_CODE!=null&&c.MAIN_CODE!=''&&c.MAIN_CODE.indexOf("-")==-1&&(c.MAIN_CODE.length==7||c.MAIN_CODE.length==8)){
					$("#"+c.tId+"_span").css("color","#428bca");
				}else if(c.ADD_CODE!=null&&c.ADD_CODE!=''&&c.ADD_CODE.indexOf("-")==-1&&(c.ADD_CODE.length==7||c.ADD_CODE.length==8)){
					$("#"+c.tId+"_span").css("color","#428bca");
				}
			}
			if(treeNode.main_show==1){
				//可能存在主要编码与附加编码颠倒情况
				if(c.MAIN_CODE!=null&&c.MAIN_CODE!=''){
					c.main_show=1;
					c.name=c.CN +"["+c.MAIN_CODE+"]";
				}else{
					c.add_show=1;
					c.name=c.CN +"["+c.ADD_CODE+"]";
				}
				zTree.updateNode(c);
			}else if(treeNode.add_show==1){
				//可能存在主要编码与附加编码颠倒情况
				if(c.ADD_CODE!=null&&c.ADD_CODE!=''){
					c.add_show=1;
					c.name=c.CN +"["+c.ADD_CODE+"]";
				}else{
					c.main_show=1;
					c.name=c.CN +"["+c.MAIN_CODE+"]";
				}
				zTree.updateNode(c);
			}
		}
	}
	//双击事件,增加一个父节点
	function onDblClick(event, treeId, treeNode, clickFlag){
		if(treeNode==null){return;}
		if(treeNode.IS_DISABLE==1){
			bootbox.dialog({
				message: "<span class='bigger-110'>已经停用的本体不能进行操作！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
		var exit = $("#selectedCont").find("#father_"+treeNode.id);
		//判断是否重复添加
		if(exit.length!=0){
			$("#selectedCont").tips({side:3,msg:'重复添加父节点!',bg:'#AE81FF',time:3});
			return;
		}
		
		setFather(treeNode);
	}

	function setFather(treeNode){
		$("#selectedCont").append($("<span class='tag_grp' id='father_"+treeNode.id+"''><b>"+treeNode.name
			+"</b>&nbsp;<span class='span_remove' onclick='cancelCategory("+treeNode.id+");'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"+
			"<input type='hidden' name='PARENT_IDS' value='"+treeNode.id +"' /><input type='hidden' name='PARENT_NAMES' value='"+treeNode.name+"'/></span>"
		));
		if(treeNode.MAIN_CODE !=null && treeNode.MAIN_CODE !=''){
			$("#father_"+treeNode.id).append("<input type='hidden' name='MAIN_CODES' value='"+treeNode.MAIN_CODE+"'/>");
		}
		if(treeNode.ADD_CODE !=null && treeNode.ADD_CODE !=''){
			$("#father_"+treeNode.id).append("<input type='hidden' name='ADD_CODES' value='"+treeNode.ADD_CODE+"'/>");
		}
	}
	//删除一个父节点
	function cancelCategory(id){
		$("#father_"+id).remove();
	}
	//下位词
	function xwc(){
		var addcode = $("#selectedCont").find("input[name='ADD_CODES']");
		var maincode = $("#selectedCont").find("input[name='MAIN_CODES']");
		var select_len = $("#selectedCont").find("input[name='PARENT_IDS']").length;
		if(select_len == 0){
			$("#selectedCont").tips({side:3,msg:'请双击树节点，选择一个父节点',bg:'#AE81FF',time:3});
			return;
		}else if( select_len > 2){
			$("#selectedCont").tips({side:3,msg:'最多只能选择2个父节点!',bg:'#AE81FF',time:3});
			return;
		}else if(addcode.length==1){
			var add = addcode.eq(0).val();
			//如果父节点附加编码带有*，则必须选择一个主要编码
			if(add.indexOf("*")!=-1){
				//判断是否选择了主要编码
				if(maincode.length==0){
					$("#selectedCont").tips({side:3,msg:'请再选择一个主要编码父节点',bg:'#AE81FF',time:3});
					return;
				}
			}
		}else if(addcode.length>0&&maincode.length==2){
			$("#selectedCont").tips({side:3,msg:'当选择2个父节点,不能包含既有主要编码又有附加编码的节点!',bg:'#AE81FF',time:3});
			return;
		}else if(maincode.length==2&& addcode.length==0){//此时为两个主要编码，提示其中一个作为主要编码
			$("#selectedCont").tips({side:3,msg:'已经选择两个包含主要编码的节点，暂时默认第一个节点作为主要编码!',bg:'#AE81FF',time:3});
		}
		var ps=$("input[name='PARENT_IDS']");
		var IDS="";
		for(var i=0;i<ps.length;i++)
		{
		if(IDS=="")
			IDS=ps[i].value;
		else
			IDS=IDS+","+ps[i].value;
		}
		//校验能否作为上位词进行操作
		$.ajax({
		type: "POST",
		url: '<%=basePath%>taskQuery/getOntMultiPaChk.do',
    	data: {D_IDS:IDS,tm:new Date().getTime()},
    	dataType:'json',
		cache: false,
		success: function(data){
			if(data.result=="success"){
				 top.jzts();
				 var diag = new top.Dialog();
				 diag.Drag=true;
				 diag.Title ="下位词";
				 diag.URL = '<%=path%>/taskQuery/toXWC.do?DIAG_NAME='+$("#DIAG_NAME").val()+'&D_IDS='+IDS+'&TASK_ID='+$("#TASK_ID").val()+'&STEP='+$("#STEP").val()+'&Q_ID='+$("#Q_ID").val();
				 diag.Width = 380;
				 diag.Height = 260;
				 diag.CancelEvent = function(){ //关闭事件
					 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
						 if('${page.currentPage}' == '0'){
							 top.jzts();
							 setTimeout("self.location=self.location",100);
						 }else{
							 //nextPage(${page.currentPage});
							 refreshPage();
						 }
					}
					diag.close();
				 };
				 diag.show();
			 }else{
				 bootbox.dialog({
					 	message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				 return;
			 }
		}
	 	})
	}
	//无法干预弹出窗口
	function nonTerm(){
		 top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="无法干预";
		 diag.URL = '<%=path%>/taskQuery/toNonTerm.do?DIAG_NAME='+$("#DIAG_NAME").val()+'&TASK_ID='+$("#TASK_ID").val()+'&STEP='+$("#STEP").val()+'&Q_ID='+$("#Q_ID").val();
		 diag.Width = 380;
		 diag.Height = 300;
		 diag.CancelEvent = function(){ //关闭事件
			refreshPage();
			diag.close();
		 };
		 diag.show();
	}
</script>
</html>