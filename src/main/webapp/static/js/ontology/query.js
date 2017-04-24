//清除遮罩层
$(top.hangge());
$(function() {

	//复选框全选控制
	var active_class = 'active';
	$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
		var th_checked = this.checked;//checkbox inside "TH" table header
		$(this).closest('table').find('tbody > tr').each(function(){
			var row = this;
			if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
			else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
		});
	});
	
	//重置当前页面高度，自适应浏览器
	initWidthHeight();
});
//重置当前页面高度，自适应浏览器
function initWidthHeight(){
	var mycars = new Array();
	mycars[0]="btnDiv";mycars[1]="pageStrDiv";
	FixTable("simple-table", 2 ,mycars);
}
//新增
function add(name,id){
	 top.jzts();
	 var diag = new top.Dialog();
	 var ontoTypeCurent = $("#ONTO_TYPE").val();
	 diag.Drag=true;
	 diag.URL = path+'/ontology/to'+name+'.do?ONTO_TYPE='+ontoTypeCurent+'&ONTO_ID='+id;
	 if(ontoTypeCurent=="5"){
	 	diag.Title ="药品";
	 }else if(ontoTypeCurent=="2"){
	 	diag.Title ="手术";
	 }else if(ontoTypeCurent=="1"){
	 	diag.Title ="诊断";
	 }else if(ontoTypeCurent=="3"){
	 	diag.Title ="科室";
	 }
	 diag.Width = $(top.window).width();
	 diag.Height = $(top.window).height();
	 if(name=="Add"){
		diag.Title = diag.Title+"新增";
	 }else{
		diag.Title = diag.Title+"修改";
	 }
	 diag.CancelEvent = function(){ //关闭事件
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			if(name=="Edit"){
				nextPage($("#currentPage").val());
			}
		}
		diag.close();
	 };
	 diag.show();
}
function edit(obj,name){
	var onto = $(obj).find("input[name=ids]").eq(0);
	add(name,onto.val());
}

//定位
var purl = path+'/ontoTree/treePidsById.action?ONTO_TYPE='+$("#ONTO_TYPE").val();
function fixer_tree(e,id){
	//window.event? window.event.cancelBubble = true : e.stopPropagation();
	parent.fixerTree(id,purl,$("#ONTO_TYPE").val());
}
//修改父节点
function selectCategory(){
	if(!checkOnto()){
		return;
	}
	var obj = $("input[name=ids]:checked");
	var m = obj.eq(0);
	//诊断本体编码大于8位数才允许修改
	if($("#ONTO_TYPE").val()=='1'){
		if((m.attr("MAIN_CODE")!=""&&m.attr("MAIN_CODE").length<=8)||(m.attr("ADD_CODE")!=""&&m.attr("ADD_CODE").length<=8)){
			bootbox.dialog({
				message: "<span class='bigger-110'>诊断只有8位编码才能修改父节点！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
	}
	//科室本体编码大于11位数才允许修改
	if($("#ONTO_TYPE").val()=='3'){
//		if(m.attr("MAIN_CODE")!=""&&m.attr("MAIN_CODE").length<10){
//			bootbox.dialog({
//				message: "<span class='bigger-110'>科室只有11位编码才能修改父节点！</span>",
//				buttons: 			
//				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
//			});
//			return;
//		}
	}
	//手术本体编码大于8位数才允许修改
	if($("#ONTO_TYPE").val()=='2'){
		if(m.attr("MAIN_CODE")!=""&&m.attr("MAIN_CODE").length<6){
			bootbox.dialog({
				message: "<span class='bigger-110'>手术只有7位编码才能修改父节点！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
	}
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="选择父节点";
	diag.URL = path + '/common/treeWidget.do?ONTO_TYPE='+$("#ONTO_TYPE").val()+"&ONTO_ID="+obj.eq(0).val()+"&businessType=0";
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
	diag.CancelEvent = function(){ //关闭事件
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 top.jzts();
			 nextPage($("#currentPage").val());
		}
		diag.close();
	};
	diag.show();
}


//验证是否正在修改中
function checkOnto(){
	var obj = $("input[name=ids]:checked");
	if(obj.length != 1){
		$("#simple-table tbody").children().first().children().first().children().first().tips({
			side:2,
          msg:'请勾选一条数据',
          bg:'#AE81FF',
          time:2
      });
      return false;
	}
	var onto = obj.eq(0);
	if(onto.attr("ONTO_FLAG")==1){
		//正在修改中的本体，提示
		bootbox.dialog({
			message: "<span class='bigger-110'>该本体正在修改中，审核后才可以继续修改！</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return false;
	}else{
		return true;
	}
}
//停用本体术语
function stopOnto(){
	var obj = $("input[name=ids]:checked");
	if(obj.length != 1){
		$("#simple-table tbody tr td").children().first().tips({ side:2, msg:'请勾选一条数据',  bg:'#AE81FF', time:2});
      return false;
	}
	var m = obj.eq(0);
	//本体编码大于8位数才允许修改
	if($("#ONTO_TYPE").val()=='1'){
		if((m.attr("MAIN_CODE")!=""&&m.attr("MAIN_CODE").length<=8)||(m.attr("ADD_CODE")!=""&&m.attr("ADD_CODE").length<=8)){
			bootbox.dialog({
				message: "<span class='bigger-110'>诊断只有8位编码才能停用！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
	}
	//科室本体编码大于11位数才允许修改
	if($("#ONTO_TYPE").val()=='3'){
//		if(m.attr("MAIN_CODE")!=""&&m.attr("MAIN_CODE").length<10){
//			bootbox.dialog({
//				message: "<span class='bigger-110'>科室只有11位编码才能停用！</span>",
//				buttons: 			
//				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
//			});
//			return;
//		}
	}
	//手术本体编码大于8位数才允许修改
	if($("#ONTO_TYPE").val()=='2'){
		if(m.attr("MAIN_CODE")!=""&&m.attr("MAIN_CODE").length<6){
			bootbox.dialog({
				message: "<span class='bigger-110'>手术只有7位编码才能停用！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}
	}
	//先判断是本体还是同义词
	var ontology = obj.eq(0);
	var msg = "";
	var url = "";
	var IS_DISABLE ="";
	var ONTO_FLAG = "";
	if(ontology.attr("OSYN_ID")==ontology.attr("STAND_ID")){
		//本体
		msg = "本体";
		ONTO_FLAG = ontology.attr("ONTO_FLAG");
		IS_DISABLE = obj.eq(0).attr("IS_DISABLE");
		url = basePath +"common/stopOntology.do?ONTO_TYPE="+$("#ONTO_TYPE").val()+"&ONTO_ID=" + obj.eq(0).val();
	}else{
		//同义词
		msg ="同义词";
		ONTO_FLAG = ontology.attr("OSYN_FLAG");
		IS_DISABLE = obj.eq(0).attr("OSYN_IS_DISABLE");
		url = basePath +"common/stopOsyn.do?ONTO_TYPE="+$("#ONTO_TYPE").val()+"&OSYN_ID=" + obj.eq(0).attr("OSYN_ID");
	}
	if(IS_DISABLE==1){
		bootbox.dialog({
			message: "<span class='bigger-110'>该"+msg+"已经被停用！</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return ;
	}
	if(ONTO_FLAG == 1){
		//正在修改中的本体，提示
		bootbox.dialog({
			message: "<span class='bigger-110'>该"+msg+"正在修改中，审核后才可以继续修改！</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return false;
	}
	bootbox.confirm("确定要停用该"+msg+"吗?", function(result) {
		if(result) {
			top.jzts();
			$.get(url,function(data){
				if(data.result=="success"){
					nextPage($("#currentPage").val());
				}else{
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}
			});
		};
	});
}
//添加同义词
function editOsyn(type){
	var obj = $("input[name=ids]:checked");
	if(obj.length != 1){
		$("#simple-table tbody tr td").children().first().tips({
		  side:2,msg:'请勾选一条数据',bg:'#AE81FF',time:2
      });
      return false;
	}
	var diag = new top.Dialog();
	diag.Drag=true;
	if(type==0){
		//验证标准词是否为停用
		if(obj.eq(0).attr("IS_DISABLE")==1){
			obj.eq(0).tips({ side:2,msg:'标准词已经被停用，不能添加同义词。',bg:'#AE81FF',time:2 });
			return;
		}
		top.jzts();
		diag.Title ="添加同义词";
		diag.URL = path + '/common/toOsynAdd.do?ONTO_TYPE='+$("#ONTO_TYPE").val()+"&STAND_ID="+obj.eq(0).attr("STAND_ID");
	}else{
		top.jzts();
		if(obj.eq(0).attr("OSYN_ID")==obj.eq(0).attr("STAND_ID")){
			top.hangge();
		 	bootbox.dialog({
				message: "<span class='bigger-110'>该数据为标准词，请选择一条同义词！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return ;
		}
		if(obj.eq(0).attr("OSYN_FLAG")==1){
			top.hangge();
		 	bootbox.dialog({
				message: "<span class='bigger-110'>该同义词正在修改中，审核后才可以继续修改！</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return ;
		}
		diag.Title ="修改同义词";
		diag.URL = path + '/common/toOsynEdit.do?ONTO_TYPE='+$("#ONTO_TYPE").val()+"&OSYN_ID="+obj.eq(0).attr("OSYN_ID");
	}
	diag.Width = 600;
	diag.Height = $(top.window).height();
	diag.CancelEvent = function(){ //关闭事件
		if(type==1){
			if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 top.jzts();
				 nextPage($("#currentPage").val());
			}
		}
		diag.close();
	};
	diag.show();
}
//术语知识编辑
function depCategory(){
	if(!checkOnto()){
		return;
	}
	var obj = $("input[name=ids]:checked");
	//本体编码大于8位数才允许修改
//	if($("#ONTO_TYPE").val()=='1'){
//		var m = obj.eq(0);
//		if((m.attr("MAIN_CODE")!=""&&m.attr("MAIN_CODE").length<=8)||(m.attr("ADD_CODE")!=""&&m.attr("ADD_CODE").length<=8)){
//			bootbox.dialog({
//				message: "<span class='bigger-110'>诊断只有8位编码才能编辑！</span>",
//				buttons: 			
//				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
//			});
//			return;
//		}
//	}
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.URL = path + '/common/toKnowledgeEdit.do?ONTO_TYPE='+$("#ONTO_TYPE").val()+'&ONTO_ID='+obj.eq(0).val();
	diag.Width = 900;
	diag.Height = 500;
	diag.Title ="术语知识编辑";
	diag.CancelEvent = function(){ //关闭事件
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 top.jzts();
			 nextPage($("#currentPage").val());
		}
		diag.close();
	};
	diag.show();
}

function cancelSubmit(id){
	bootbox.confirm("确定要撤销已提交的数据吗?", function(result) {
		if(result) {
			top.jzts();
			var url = path+'/ontology/cancelSubmit?ONTO_ID='+id+"&ONTO_TYPE="+$("#ONTO_TYPE").val();
			$.get(url,function(data){
				if(data.result=="success"){
					nextPage($("#currentPage").val());
				}else{
					if(data.refresh==1){
						nextPage($("#currentPage").val());
					}
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
					$(top.hangge());
				}
			});
		};
	});
}
function cancelOsyn(id){
	bootbox.confirm("确定要撤销已提交的数据吗?", function(result) {
		if(result) {
			top.jzts();
			var url = path+'/osyn/cancelSubmit?DN_ID='+id+"&ONTO_TYPE="+$("#ONTO_TYPE").val();
			$.get(url,function(data){
				if(data.result=="success"){
					nextPage($("#currentPage").val());
				}else{
					if(data.refresh==1){
						nextPage($("#currentPage").val());
					}
					bootbox.dialog({
						message: "<span class='bigger-110'>"+data.result+"</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
					$(top.hangge());
				}
			});
		};
	});
}
