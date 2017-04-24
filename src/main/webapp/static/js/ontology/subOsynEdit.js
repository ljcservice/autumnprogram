//获取一个序列值
var index_value = 0;
function getTable_index(){
	index_value = index_value+1;
	return index_value;
}
//增加同义词窗口
function addOsyn(){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="增加同义词";
	diag.URL = path+'/common/toSubOsynAdd.do?ONTO_TYPE='+$("#ONTO_TYPE").val();
	diag.Width = 800;
	diag.Height = 600;
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	};
	diag.show();
}
//修改同义词窗口
function modifyOsyn(obj){
	var table = $(obj).closest("table");
	top.jzts();
	var diag = new top.Dialog();
	var ONTO_TYPE = $("#ONTO_TYPE").val();
	diag.Drag=true;
	diag.Title ="修改同义词";
	diag.URL = path+'/common/toSubOsynAdd.do?ONTO_TYPE='+ONTO_TYPE;
	diag.Width = 800;
	diag.Height = 600;
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	};
	diag.OnLoad = function(){ 
		var inner_document = diag.innerFrame.contentWindow.document;
		var tableId = table.attr("id");
		//设置值
		inner_document.getElementById("tableId").value = tableId;
		inner_document.getElementById("DN_CHN").value = table.find("input[name='DN_CHN']").eq(0).val();
		var ORG_DN_CHN = inner_document.getElementById("ORG_DN_CHN");
		var w = table.find("input[name='ORG_DN_CHN']").eq(0).val();
		$(ORG_DN_CHN).find("option[value="+w+"]").attr("selected",true);
		diag.innerFrame.contentWindow.resetSelect("ORG_DN_CHN");
		inner_document.getElementById("DESCRIPTION").value = table.find("input[name='SYNO_DESCRIPTION']").eq(0).val();
		diag.innerFrame.contentWindow.$("input:radio[value="+table.find("input[name='SYNO_IS_DISABLE']").eq(0).val()+"]").attr('checked','true');
		
		if(ONTO_TYPE=='1'){
			//诊断多出来的几个值设置值
			inner_document.getElementById("DN_ENG").value = table.find("input[name='DN_ENG']").eq(0).val();
			var SYNO_TYPE = inner_document.getElementById("SYNO_TYPE");
			var m = table.find("input[name='SYNO_TYPE']").eq(0).val();
			$(SYNO_TYPE).find("option[value="+m+"]").attr("selected",true);
			diag.innerFrame.contentWindow.resetSelect("SYNO_TYPE");
			diag.innerFrame.contentWindow.$("input:radio[value="+table.find("input[name='SYNO_IS_DISABLE']").eq(0).val()+"]").attr('checked','true');
		}
		if(ONTO_TYPE=='2'){
			//手术多出来的几个值设置值
			inner_document.getElementById("DN_ENG").value = table.find("input[name='DN_ENG']").eq(0).val();
		}
		if(ONTO_TYPE=='3'){
			//科室上面已经满足
		}
	};
	diag.show();
}