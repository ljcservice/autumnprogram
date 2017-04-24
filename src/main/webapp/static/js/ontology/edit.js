$(top.hangge());
$(function() {
	changeTree();
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
		$(window)
		.off('resize.chosen')
		.on('resize.chosen', function() {
			$('.chosen-select').each(function() {
				 var $this = $(this);
				 $this.next().css({'width': '98%'});
			});
		}).trigger('resize.chosen');
		$('#chosen-multiple-style .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
			 else $('#form-field-select-4').removeClass('tag-input-style');
		});
	}
	
	var _move = false; //移动标记
	var _x, _y; //鼠标离控件左上角的相对位置
	$(".drag").mousedown(function(e) {
	     _move = true;
	     _x = e.pageX - parseInt($(".drag").css("left"));
	     _y = e.pageY - parseInt($(".drag").css("top"));
	     $(".drag").fadeTo(20, 0.6); //点击后开始拖动并透明显示
	});
	$(".drag").mouseup(function(e) {
		_move = false;
	     $(".drag").fadeTo("fast", 1); //松开鼠标后停止移动并恢复成不透明
	});
	$(".drag").mousemove(function(e) {
	     if (_move) {
	        var x = e.pageX - _x; //移动时根据鼠标位置计算控件左上角的绝对位置
	        var y = e.pageY - _y;
	        $(".drag").css({ top: y, left: x }); //控件新位置
	    }
	}).mouseup(function() {
	     _move = false;
	     $(".drag").fadeTo("fast", 1); //松开鼠标后停止移动并恢复成不透明
	});
});
var zTree;
var zTreeUrl = basePath+"ontoTree/tree.do?ONTO_TYPE=" + $("#ONTO_TYPE").val();
function initTree(){
	var setting = {
		view: {
			showIcon:false,
			showLine: true,
			dblClickExpand: false,
			selectedMulti: false,
			addDiyDom: addDiyDom
		},
		callback:{
			onExpand : onExpand,
			onDblClick : onDblClick
		},
		async: {
			enable: true,
			url:zTreeUrl,
			autoParam:["id", "name=n", "CATEGORY"],
			otherParam:{"type":"1"}
		}
	};
	zTree = $.fn.zTree.init($("#leftTree"), setting);
	zTree.setting.edit.showRenameBtn = true;
};

//切换树
function changeTree(){
	var ontoTypeCurent = $("#ONTO_TYPE").val();
	if(ontoTypeCurent==null || ontoTypeCurent==""){
		return;
	}
	$("#leftTree").empty();
	zTreeUrl = basePath + "ontoTree/tree.do?ONTO_TYPE="+ontoTypeCurent;
	//切换树标题名称
	if(ontoTypeCurent=="5"){
		$("#treeName").html("药品ATC分类");
	}else if(ontoTypeCurent=="2"){
		$("#treeName").html("手术本体树");
	}else if(ontoTypeCurent=="1"){
		$("#treeName").html("诊断本体树");
	}else if(ontoTypeCurent=="3"){
		$("#treeName").html("科室本体树");
	}
	initTree();
}
//增加图标
function addDiyDom(treeId, treeNode) {
	//诊断显示详情图标
	if($("#ONTO_TYPE").val() == '1'){
		var aObj = $("#" + treeNode.tId + "_a");
		var editStr = "<span class='demoIcon' id='diyBtn_" +treeNode.id+ "' title='"+treeNode.name+"' onclick='queryDetail(this,"+treeNode.id+");'><span class='detail'></span></span>";
		aObj.after(editStr);
	}
}
//获取一个序列值
var index_value = 0;
function getTable_index(){
	index_value = index_value+1;
	return index_value;
}
//检查名称是否存在
function checkName(obj,name){
	if($.trim($(obj).val())==""){
		return false;
	}
	var flag = true;
	var mydata = {DN_CHN:$.trim($(obj).val()),ONTO_TYPE:$("#ONTO_TYPE").val(),standFlag:1,DN_ID:$("#DN_ID").val()};
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
//单个历史详情
function hisDetail(){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.URL = path+'/ontology/hisDetail.do?ONTO_TYPE='+$("#ONTO_TYPE").val()+'&D_ID='+$("#D_ID").val();
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
	diag.Title ="变更历史详情";
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	};
	diag.show();
}
//科室分类
function depCategory(){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.URL = path + '/common/treeWidget.do?ONTO_TYPE=3'+"&ONTO_ID="+$("#D_ID").val()+"&businessType=2";
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
	diag.Title ="科室分类信息";
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	};
	diag.show();
}
//保存本体
function saveOnto(){
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	$.ajax({
		type: "POST",
		url: basePath+$("#userForm").attr("action"),
    	data: $("#userForm").serialize(),
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
//删除一个父节点
function cancelCategory(id){
	$("#father_"+id).remove();
	$("#span_exchange").remove();
	if($("#ADD_P_ID").val()!=null&&$("#ADD_P_ID").val()!=""){
		var b = $("#selectedCont").find("b").eq(0);
		b.text(b.text().replace("(附)","(主)"));
	}
	$("#ADD_P_ID").val("");
}
//移除一个同义词table
function closeTable(obj){
	$(obj).closest("table").remove();
}
//增加同义词窗口
function addOsyn(){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="增加同义词";
	diag.URL = path+'/common/toSubOsynAdd.do?ONTO_TYPE='+$("#ONTO_TYPE").val();
	diag.Width = 600;
	diag.Height = $(top.window).height();
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	};
	diag.show();
}
//查询单个本体,允许页面缓存
function queryDetail(obj,id){
	var ONTO_TYPE = $("#ONTO_TYPE").val();
	$.ajax({
		type: "POST",
		url: basePath+'ontology/ontoDetail.do',
    	data: {ID:id,ONTO_TYPE:ONTO_TYPE},
		dataType:'json',
		asyn:true,
		cache: true,
		success: function(data){
			if(data.result=="success"){
				//设置页面值
				$("#det_cn").text(data.STAD_DN_CHN);
				$("#det_en").text(data.STAD_DN_ENG);
				$("#det_o_cn").text(data.ORG_STAD_DN_CHN);
				$("#det_type").text(data.TERM_TYPE);
				$("#det_defin").text(data.TERM_DEFIN);
				$("#det_dep").text(data.DEP_CATEGORY_NAME);
				$("#det_part").text(data.PART_CATEGORY);
				$("#det_man").text(data.MAN_CATEGORY);
				$("#det_dis").text(data.DIS_CATEGORY);
				$("#det_chronic").text(data.IS_CHRONIC==0?'否':'是');
				var c_note = zTree.getNodeByParam ("id", id, null);
				zTree.selectNode (c_note, false, false);
				//设置显示位置
				var windowTop = $(top.window).height();
				var detailTop = $("#detailInfo").height();
			 	var offset = $(obj).offset();  
			 	if((offset.top + detailTop+80)>windowTop){
			 		$("#detailInfo").show().css("top",windowTop-detailTop-80).css("left",offset.left+30);
			 	}else{
			 		$("#detailInfo").show().css("top",offset.top).css("left",offset.left+30);
			 	}
			}else{
				//查询失败
			}
		}
	});
}
//关闭详细div弹出框
function closeDetail(e){
	window.event? window.event.cancelBubble = true : e.stopPropagation();
	$("#detailInfo").css("top",-1000).hide();
}
//本体修改页面，添加多个同义词，验证同义词名称是否重复
function checkOsynName(){
	var flag = true;
	var array = new Array();
	var DN_CHN = $("#osynDiv").find("input[name='DN_CHN']");
	if(DN_CHN.length>0){
		DN_CHN.each(function (){
			//多个同义词之间检测
			for(var i=0;i<array.length;i++){
				if(array[i] == $.trim($(this).val())){
					flag = false;
				 	bootbox.dialog({
						message: "<span class='bigger-110'>重复的同义词名称："+$(this).val()+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				 	break;
				}
			}
			if(!flag){ return false; }
			//同义词与标准词名称对比
			if($.trim($(this).val()) == $.trim($("#STAD_DN_CHN").val())){
			 	bootbox.dialog({
					message: "<span class='bigger-110'>同义词与标准词名称重复，名称为："+$(this).val()+"</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
			 	flag = false;
			 	return false;
			}
			array[array.length] = $.trim($(this).val());
		});
	}
	return flag;
}
