var tree_id = "leftTree";
var all_pids = "";
var one_pids = "";
var two_pids = "";
var pids = "";
var select_id = "";//当前选择的ID
var pre_id="";//上次选择的ID
var mytimes = 0;
var myInterval = null;
var same_times = 1;
//定位，获取所有父节点
function fixerTree(id,purl,ontoType){
	zTree.setting.view.selectedMulti= true;
	//根据ID 找到节点
	//var c_note = zTree.getNodeByParam ("id", select_id, null);
	//选中节点
	//zTree.selectNode (nodes[i], false, false);
	//定位树与列表的类型不一致，刷新树
	if(ontoType!=null && ontoType != $("#ontoType").val()){
		$("select#ontoType option[value='"+ontoType+"']").attr("selected", "true");
		var s = $("select#ontoType option[value='"+ontoType+"']").text();
		$("#ontoType_chosen").children().eq(0).children().eq(0).text(s);
		changeTree();
	}
	select_id = id;
	if(select_id == pre_id){
		if(same_times==2 || all_pids.indexOf("##")==-1){
			//如果为当前为第3、5、7次等点击，或者结果集中不含有2层父节点，则直接在第一层父节点中定位
			same_times = 1;
			var l_note = zTree.getNodesByParam ("id", select_id, null);
			zTree.selectNode (l_note[0], false, false);
			return ;
		}
		if(same_times==1&&all_pids.indexOf("##")!=-1){
			same_times = 2;
			if(two_pids!=null && two_pids!=""){
				//在第二层父节点中定位
				var l_note = zTree.getNodesByParam ("id", select_id, null);
				zTree.selectNode (l_note[1], false, false);
			}else{
				one_pids = "";
				two_pids = "";
				pids = "";
				setTimeout(function(){fixerTree2(id,purl);},100);
			}
		}
	}else{
		//折叠所有节点
		zTree.expandAll(false);
		same_times = 1;
		pre_id = select_id;
		all_pids = "";
		one_pids = "";
		two_pids = "";
		pids = "";
		setTimeout(function(){fixerTree2(id,purl);},100);
	}
	//启动定时器
	startTimer();
}
//启动定时器
function startTimer(){
	if(myInterval==null){
		myInterval = self.setInterval(function(){
			mytimes=Number(mytimes)+2;
			if(mytimes >= 50 ){
				//清除定时器
				clearInterval(myInterval);
				myInterval = null;
			}
		},200);
	}
}
function fixerTree2(id,purl){
	pids="";
	//去除所有已经被选中的节点样式
	$(".curSelectedNode").removeClass("curSelectedNode");
	$.post(purl, {id:id}, function(data){
		if ( data.result != null && data.result != '' ) {		
			mytimes = 4;
			all_pids = data.result;
			var parentIds = data.result.split("##");
			if(same_times==1){
				one_pids = parentIds[0];
				pids = one_pids.split(";");
			}
			if(same_times==2){
				two_pids = parentIds[1];
				pids = two_pids.split(";");
			}
			//所有根节点
			var nodes = zTree.getNodes();
			myExpandNode(nodes[0]);
		} else {
			mytimes = 0;
			alert("在当前树检索不到该节点,可能该节点不存在或者被停用");
		}
	});
}
function myExpandNode(node){
	if(checkArrayExt(pids,node.id)){
		startTimer();
		//睡眠0.8秒
		if(mytimes >=6){
			mytimes = 0;
		}else{
			setTimeout(function(){myExpandNode(node);},200);
			return false;
		}
		zTree.selectNode (node, false, false);
//		if(node.getParentNode()!=null){
//			$("#"+node.getParentNode().tId+"_a").removeClass("curSelectedNode");
//		}
//		$("#"+node.tId+"_a").addClass("curSelectedNode");
		if(select_id == node.id){
//			zTree.selectNode (node, true, false);
		}else{
	 		//判断当前结点是否被展开
		 	if( node.open ){
	 			//已经展开则继续递归
	 			var child = node.children;
	 			if(child!=null){
	 				myExpandNode(child[0]);
	 			}
		 	}else{
				// 	未展开则展开节点
		 		zTree.expandNode(node, true, false, true, false);
		 		delayonExpand(node,0);
		 	}
	 	}
	}
	var nextNode = node.getNextNode();
	if(nextNode!=null){
		myExpandNode(nextNode);
	}
}


//判断数组中是否含有该值
function checkArrayExt(ids,id){
	for (var i=0, l=ids.length; i<l; i++) {
		if(ids[i] == id){
			return true;
		}
	}
	return false;
}
//zTree展开节点的回掉函数 （多个节点同时异步打开后 只有第一个打开的事件回调函数好使，此处jquery bug，废弃）
function onExpand(event, treeId, treeNode) {
//	if(treeNode==null || treeNode==undefined){
//		alert(treeId);
//	}
//	expandNodeAll(treeNode.children);
	//谷歌360自动给这个ul加上了隐藏属性，导致不显示子节点。火狐没问题
	$("#"+treeNode.tId+"_ul").css("overflow","visible");
	//父节点为空，说明当前为根节点
	if(treeNode.getParentNode()==null){
		if(treeNode.MAIN_CODE!=null&&treeNode.MAIN_CODE!=''){
			treeNode.main_show=1;
		}
		if(treeNode.ADD_CODE!=null&&treeNode.ADD_CODE!=''){
			treeNode.add_show=1;
		}
	}
	var p_main = treeNode.MIAN_CODE;
	var p_add = treeNode.ADD_CODE;
	var children = treeNode.children;
	var len = children.length;
	for(var i=0;i<len;i++){
		var c = children[i];
		if(c.IS_DISABLE==1){
			$("#"+c.tId+"_span").css("text-decoration","line-through").css("color","red").attr("title","已经停用");
		}
		if($("#ontoType").val()=='51005'){
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
}

//延迟判断zTree展开节点
function delayonExpand(treeNode,times) {
	if(times>30){return;}
	times+=1;
	if(treeNode.open){
		//谷歌360自动给这个ul加上了隐藏属性，导致不显示子节点。火狐没问题
		$("#"+treeNode.tId+"_ul").css("overflow","visible");
		//父节点为空，说明当前为根节点
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
			}
			if($("#ontoType").val()=='51005'){
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
		myExpandNode(children[0]);
		return;
	}else{
		setTimeout(function(){delayonExpand(treeNode,times);},300);
	}
}

