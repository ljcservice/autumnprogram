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
	initTree();
	
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
	$(".curSelectedNode").removeClass("curSelectedNode");
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