var zTree;
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
		}
	};
	
	var curentSelected = "";
	var zn = '${zTreeNodes}';
	var zTreeNodes = eval(zn);
	zTree = $.fn.zTree.init($("#leftTree"), setting,zTreeNodes);
};
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