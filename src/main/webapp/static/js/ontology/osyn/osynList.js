//双击选中行
function dbclickF(){  
    var rows=document.getElementById("simple-table").rows;  
    if(rows.length>0){  
        for(var i=1;i<rows.length;i++){  
          (function(i){  
            var dn_id=rows[i].id;
            var edit_flag=rows[i].getAttribute("edit_flag");
            var obj=rows[i];  
            if(dn_id!=''){
            	obj.ondblclick=function(){
                	toEdit(dn_id,edit_flag);
                };  
            }
            })(i)  
        }  
    }  
}

//新增同义词
function add(){
	 top.jzts();
	 var OSYN_FLAG=$("#OSYN_FLAG").val();
	 var ONTO_TYPE = $("#ONTO_TYPE").val();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="新增同义词";
	 diag.URL = path+'/osyn/toAdd.do?OSYN_FLAG='+OSYN_FLAG+'&ONTO_TYPE='+ONTO_TYPE;
	 diag.Width = 800;
	 diag.Height = 600;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}
//修改同义词
function toEdit(dn_id,edit_flag){
	if(edit_flag==1){
		alert("该信息正在修改中，审核后才可以继续修改！");
		return;
	}
	top.jzts();
	 var ONTO_TYPE = $("#REAL_ONTO_TYPE").val();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="修改同义词";
	 diag.URL = path+'/osyn/toEdit.do?&ONTO_TYPE='+ONTO_TYPE+'&OSYN_ID='+dn_id;
	 diag.Width = 800;
	 diag.Height = 600;
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