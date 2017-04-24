//根据二期需求，改变点击和双击事件，无特殊要求可以继续使用tree.js
//切换树
function changeTree(){
	var ontoTypeCurent = $("#ONTO_TYPE").val();
	if(ontoTypeCurent==null || ontoTypeCurent==""){
		return;
	}
	//清空分类选择
	$("#category_id").val("");
	$("#leftTree").empty();
	zTreeUrl = basePath+"ontoTree/tree.do?ONTO_TYPE="+ontoTypeCurent;
	//切换树标题名称
	if(ontoTypeCurent=="5"){
		$("#treeName").html("药品ATC分类");
		$("#diagSearch").hide();
	}else if(ontoTypeCurent=="2"){
		$("#treeName").html("手术本体树");
		$("#diagSearch").hide();
	}else if(ontoTypeCurent=="1"){
		$("#treeName").html("诊断本体树");
		$("#diagSearch").show();
	}else if(ontoTypeCurent=="3"){
		$("#treeName").html("科室本体树");
		$("#diagSearch").hide();
	}
	if(ontoTypeCurent!=null && ontoTypeCurent!=''){
		if($("#mytreeName").length>0){
			
		}else{
			$(".sidebar").show();
		}
	}else{
		
	}
	$("#mytreeName").html($("#treeName").html());
	
	initTree();
	
	//给Tree增加点击双击事件
	var enable = 0;//双保险，控制单击执行
	var timeLock = null;
	$(".node_name").live("click",function(){
		clearTimeout(timeLock);
		var obj = $(this);
		timeLock = setTimeout(function(){
			if(enable==0){
				//获取点击的节点
				var c_note = zTree.getNodeByParam ("tId", obj.attr("id").replace("_span",""), null);
				if( c_note.open ){ 
					//闭合节点
					zTree.expandNode(c_note, false, false, false, false);
				}else{
					//展开节点
					//去除所有已经被选中的节点样式
					$(".curSelectedNode").removeClass("curSelectedNode");
					$("#"+c_note.tId+"_a").addClass("curSelectedNode");
					zTree.expandNode(c_note, true, false, false, false);
				}
			}
		},300);
	});
	$(".node_name").live("dblclick",function(){
		enable++;
		clearTimeout(timeLock);
		var obj = $(this);
		var treeNode_tId = obj.attr("id").replace("_span","");
		var c_note = zTree.getNodeByParam ("tId", treeNode_tId, null);
		//去除所有已经被选中的节点样式
		$(".curSelectedNode").removeClass("curSelectedNode");
		$("#"+c_note.tId+"_a").addClass("curSelectedNode");
		//查询右侧列表
		var ontoUrl = basePath+$("#searchForm").attr("action")+"?ONTO_TYPE="+$("#ONTO_TYPE").val()+"&ONTO_ID="+c_note.id;
//		alert(ontoUrl);
		treeFrame.location.href= ontoUrl;
		enable =0;
	});
}
var zTree;
var zTreeUrl = basePath+"ontoTree/tree.do?ONTO_TYPE=" + $("#ONTO_TYPE").val();
function initTree(){
	var setting = {
		view: {
			showIcon: false,
			showLine: true,
			showTitle: false,
			dblClickExpand: false,
			selectedMulti: false
		},
	    check:{
	    	enable: false
		},
		callback:{
			onExpand : onExpand,
			onClick: onClick,
			beforeClick :beforeClick
		},
		async: {
			enable: true,
			url:zTreeUrl,
			autoParam:["id", "name=n", "CATEGORY"],
			otherParam:{"type":"1"}
		}
	};
	zTree = $.fn.zTree.init($("#leftTree"), setting);
	if($("#ONTO_TYPE").val()=='3'){
		setTimeout(initTreeDis,1000);
	}
};
var curentSelected = "";
//无用，不会调用
function onClick(event, treeId, treeNode, clickFlag) {
	var obj  = $("#"+treeNode.tId+"_a");
	if(treeNode.tId == curentSelected){
		//已经被选择则取消选择状态，
		obj.removeClass("curSelectedNode");
		//清除树查询条件
		$("#category_id").val("");
		//清楚已选中标志
		curentSelected = "";
	}else{
		//AI设置点击事件为查询本体列表
		var AIQuery = $("#AIQuery").val();
		if(AIQuery==null || AIQuery==""){
		}else{
			document.getElementById('treeFrame').contentWindow.query(null,treeNode.id);
		}
		//设置查询条件分类
		$("#category_id").val(treeNode.id);
		curentSelected = treeNode.tId;
	}
	return true;
}
function beforeClick(){
	//设置为false 不调用onclick事件，
	return false;
}
 
//初始化改变停用节点的样式
function initTreeDis(){
	var children = zTree.getNodes();
	if(children!=null){
		var len = children.length;
		for(var i=0;i<len;i++){
			var c = children[i];
			if(c.IS_DISABLE==1){
				$("#"+c.tId+"_span").css("text-decoration","line-through").css("color","red").attr("title","已经停用");
			}
		}
	}else{
		setTimeout(initTreeDis,1000);
	}
}