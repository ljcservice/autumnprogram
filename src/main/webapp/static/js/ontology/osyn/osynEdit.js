$(top.hangge());

$(function() {
	//下拉框
	if(!ace.vars['touch']) {
		$('.chosen-select').chosen({allow_single_deselect:true}); 
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
});	
//标准诊断名称弹出框
function selectDiag(){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	var ONTO_TYPE=$("#ONTO_TYPE").val();
	diag.Title ="标准诊断名称";
	diag.URL = path+'/standard/osynStand.do?ONTO_TYPE='+ONTO_TYPE;
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
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
	var standFlag = $("input[name='WORD_TYPE']:checked").val();
	var	mydata = {DN_CHN:$.trim($(obj).val()),ONTO_TYPE:$("#ONTO_TYPE").val(),standFlag:standFlag,DN_ID:$("#DN_ID").val()};
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
		
//保存
function save(){
	if(!checkData()){
		return;
	}
	$("#zhongxin").hide();
	$("#zhongxin2").show();
	$.ajax({
		type: "POST",
		url: basePath+$("#osynForm").attr("action"),
    	data: $("#osynForm").serialize(),
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
//查询同义词变更历史
function searchHist(dn_id){
	top.jzts();
	var ONTO_TYPE=$("#ONTO_TYPE").val();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="查看历史记录";
	diag.URL = path+'/osynHis/nameHist.do?DN_ID='+dn_id+'&ONTO_TYPE='+ONTO_TYPE;
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
	diag.CancelEvent = function(){ //关闭事件
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if($("#currentPage").val() == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage($("#currentPage").val());
			 }
		}
		diag.close();
		//遮罩层控制，第三层弹窗使用
		parent.$("#_DialogBGDiv").css("z-index",900).css("display","block");
	 };
	 diag.show();
}