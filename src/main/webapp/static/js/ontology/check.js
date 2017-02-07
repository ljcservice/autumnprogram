$(top.hangge());

$(function() {
	var i = 0;
	$("#showConsol").bind("click",function (){
		if(i==0){
			//全部显示
			$("#myTbody").children().show();
			i=1;
			$("#showConsol").children().eq(0).html("<a>收  缩</a>");
		}else if(i==1){
			showConsol();
			i=0;
			$("#showConsol").children().eq(0).html("<a>显示全部</a>");
		}
	});
	showConsol();
	
});
//控制显示行数
//控制对比的颜色
function showConsol(){
	var start = 3;
	var end = $("#myTbody").children().length;
	$("#myTbody").children().each(function(index,element){
		//默认隐藏列
		if(index>=start && index<=(end-6)){
			$(this).hide();
		}
		//变更数据显示，设置背景
		if(index>=0 && index<=(end-6)){
			var childrens = $(this).children();
			var s1 =childrens.eq(1).text();
			var s2 =childrens.eq(2).text();
			//如果对比不相等则显示
			if($.trim(s1) != $.trim(s2)){
				if(childrens.eq(2).children().length==0){
					childrens.eq(2).addClass("changeTd");
				}else if(childrens.eq(2).children().eq(0).attr("class")=='edit'||childrens.eq(2).children().eq(1).attr("class")=='edit'){
					childrens.eq(2).addClass("changeTd");
				}
				$(this).show();
			}
		}
	});
}
//审批通过
function passRefuse(id,type){
	//数据校验
	if(!checkData()){
		return;
	}
	var flag = $("#nextShow").prop("checked");
	var url = "";
	if(type==0){
		url = basePath+'ontology/checkPass.do';
	}else{
		url = basePath+'ontology/checkRefuse.do';
	}
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	$.ajax({
		type: "POST",
		url: url+'?tm='+new Date().getTime(),
    	data: $("#checkForm").serialize(),
		dataType:'json',
		async:false,
		cache: false,
		success: function(data){
			if(data.result=="success"){
				if(flag){
					//下一条ID
					var nextid = 0;
					//获取上一页查询条件，如果页面整体框架变动则此处需要更改
					var searchForm = parent.frames[0].$("iframe[src='ontology/check.do']")[0].contentWindow.document.searchForm;
					//显示下一条
					self.location = path+'/ontology/detailInfo.do?nextShow=1&refreshFlag='+$("#refreshFlag").val()+"&"+ $(searchForm).serialize();
				}else{
					document.getElementById("zhongxin").style.display = 'none';
					top.Dialog.close();
				}
			}else{
				$("#zhongxin").show();
				$("#zhongxin2").hide();
				bootbox.dialog({
					message: "<span class='bigger-110'>"+data.result+"</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
			}
		}
	});
}
//双击显示输入框
function setInput(obj,name){
	var val =  $(obj).parent().text();
	var htm = "<input type='text' name='"+name+"' id='"+name+"' value='"+val+"' title='"+val+"' style='width:98%;' maxlength='80'/>"
	$(obj).parent().html(htm).attr("onclick","return false;").removeClass("changeTd");
	$("#"+name).focus();
	if('TERM_DEFIN'==name){
		$("#"+name).attr("maxlength",500);
	}
}
//双击显示下拉框
function setSelect(obj,name){
	$(obj).parent().html($("#"+name).show()).attr("onclick","return false;").removeClass("changeTd");
}
//修改父节点
function selectCategory(){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="选择父节点";
	diag.URL = path + '/common/treeWidget.do?ontoType='+$("#ontoType").val()+"&H_ID="+$("#H_ID").val()+"&D_ID="+$("#D_ID").val()+"&businessType=1";
	diag.Width = 900;
 	diag.Height = 500;
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
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
	mydata = {DN_CHN:$.trim($(obj).val()),ontoType:$("#ontoType").val(),standFlag:1,DN_ID:$("#DN_ID").val()};
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
	var STAD_DN_CHN = document.getElementById("STAD_DN_CHN");
	if(STAD_DN_CHN!=null){
		if(STAD_DN_CHN.value ==null || STAD_DN_CHN.value=="" ){
			$(STAD_DN_CHN).tips({ side:3, msg:'输入名称中文', bg:'#AE81FF',  time:3   });
			flag = false;
		}
		if(!checkName($("#STAD_DN_CHN"),'DN_CHN')){
			flag = false;
		}
	}
	var STAD_DN_ENG = document.getElementById("STAD_DN_ENG");
	if(STAD_DN_ENG!=null){
		if(STAD_DN_ENG.value ==null || STAD_DN_ENG.value=="" ){
			$(STAD_DN_ENG).tips({ side:3, msg:'输入名称中文', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var ORG_STAD_DN_CHN = document.getElementById("ORG_STAD_DN_CHN");
	if(ORG_STAD_DN_CHN!=null){
		if(ORG_STAD_DN_CHN.value ==null || ORG_STAD_DN_CHN.value=="" ){
			$(ORG_STAD_DN_CHN).tips({ side:3, msg:'输入来源名称中文', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var ORG_STAD_DN_ENG = document.getElementById("ORG_STAD_DN_ENG");
	if(ORG_STAD_DN_ENG!=null){
		if(ORG_STAD_DN_ENG.value ==null || ORG_STAD_DN_ENG.value=="" ){
			$(ORG_STAD_DN_ENG).tips({ side:3, msg:'输入来源名称英文', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var MAIN_CODE = document.getElementById("MAIN_CODE");
	if(MAIN_CODE!=null){
		if(MAIN_CODE.value ==null || MAIN_CODE.value=="" ){
			$(MAIN_CODE).tips({ side:3, msg:'输入主要编码', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var ADD_CODE = document.getElementById("ADD_CODE");
	if(ADD_CODE!=null){
		if(ADD_CODE.value ==null || ADD_CODE.value=="" ){
			$(ADD_CODE).tips({ side:3, msg:'输入附加编码', bg:'#AE81FF',  time:3   });
			flag = false;
		}
	}
	var IS_DISABLE = document.getElementById("IS_DISABLE");
	if(IS_DISABLE!=null && IS_DISABLE.value==1){
		var DESCRIPTION = document.getElementById("DESCRIPTION");
		if(DESCRIPTION!=null){
			if(DESCRIPTION.value ==null || DESCRIPTION.value=="" ){
				$(DESCRIPTION).tips({ side:3, msg:'输入停用描述', bg:'#AE81FF',  time:3   });
				flag = false;
			}
		}
	}
	
	return flag;
}
//诊断审核详情页中，科室分类选择
function depCategory(){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.URL = path + '/common/treeWidget.do?ontoType=51006'+"&ONTO_ID="+$("#D_ID").val()+"&H_ID="+$("#H_ID").val()+"&businessType=3";
	diag.Width = 900;
	diag.Height = 500;
	diag.Title ="科室分类信息";
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	};
	diag.show();
}