$(top.hangge());

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
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
	dbclickF();
	
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
	mycars[0]="searchDiv";mycars[1]="pageStrDiv";
	FixTable("simple-table", 2 ,mycars);
}

//查询本体
function searchs(){
	top.jzts();
	$("#osynForm").submit();
}
//查询同义词变更历史
function searchHist(dn_id){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="查看历史记录";
	diag.URL= '<%=path%>/osynHis/nameHist.do?DN_ID='+dn_id;
	diag.Width = 900;
	diag.Height = 500;
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
	 };
	 diag.show();
}
//审批处理结果
function check(msg){
	bootbox.confirm(msg, function(result) {
		if(result){
			var idsObj = document.getElementsByName("ids");
			var h_ids ='';//选择审核的副本ID
			for (var i = 0; i < idsObj.length; i++) {
				if(idsObj[i].checked){
					if(h_ids=='')h_ids=idsObj[i].value;
					else h_ids+=','+idsObj[i].value;
				}
			}
			if(h_ids==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				return;
			}else{
				if(msg=='确认通过吗？'){
					$.ajax({
						type: "POST",
						url: '<%=basePath%>osyn/passAll.do',
				    	data: {H_IDS:h_ids},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							if(data.result=="success"){
								nextPage($("#currentPage").val());
							}else{
								var msg = data.result;
								if(msg=="failed"){msg="操作失败！";}
								bootbox.dialog({
									message: "<span class='bigger-110'>"+msg+"</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success",callback:function(){nextPage($("#currentPage").val());}  }},
									callback: function(result) { nextPage($("#currentPage").val()); }
								});
							}
						}
					});
				}else if(msg=='确定要拒绝吗？'){
					$.ajax({
						type: "POST",
						url: '<%=basePath%>osyn/refuseAll.do',
				    	data: {H_IDS:h_ids},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							if(data.result=="success"){
								nextPage($("#currentPage").val());
							}else{
								var msg = data.result;
								if(msg=="failed"){msg="操作失败！";}
								bootbox.dialog({
									message: "<span class='bigger-110'>"+msg+"</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success",callback:function(){nextPage($("#currentPage").val());}  }},
									callback: function(result) { nextPage($("#currentPage").val()); }
								});
							}
						}
					});
				}
			}
		}
	})
	
}

//双击事件
function dbclickF(){  
    var rows=document.getElementById("simple-table").rows; 
    if(rows.length>0){  
        for(var i=1;i<rows.length;i++){  
          (function(i){
        	var dn_id=rows[i].id;
            var obj=rows[i]; 
	            if(dn_id!=''){
	           	 	obj.ondblclick=function(){
	                	toDetail(obj);
	                };  
	            }
            })(i)  
        }  
    }  
}
//查看审批详情
function toDetail(obj){
	top.jzts();
	var osyn = $(obj).children().eq(0).find("input:first");
	var h_id = osyn.val();
	var ONTO_TYPE = osyn.attr("ONTO_TYPE");
	var dn_id = osyn.attr("dn_id");
	var operation = osyn.attr("operation");
	var stad_dn_id = osyn.attr("stad_dn_id");
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.Title ="查看审批详情";
	diag.URL = path+'/osynHis/checkDetail.do?h_id='+h_id+'&onto_type='+ONTO_TYPE+'&dn_id='+dn_id+'&op_type='+operation+'&stad_dn_id='+stad_dn_id;
	diag.Width = 900;
	diag.Height = 500;
	diag.CancelEvent = function(){ //关闭事件
		searchs();
		diag.close();
	};
	diag.show();
}