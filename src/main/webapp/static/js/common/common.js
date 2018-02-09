function reset(obj){
	if(typeof(obj)=='string'){
		obj = $("#"+obj);
	}
	$(obj).children().each(function(){
    	var nodeName = $(this)[0].nodeName;
    	if(nodeName == 'DIV' || nodeName== 'SPAN'){
    		reset($(this));
    	}else if(nodeName == 'INPUT'){
    		if($(this).attr('type')!='hidden'){
    			$(this).val("");
    		}
    	}else if(nodeName == 'SELECT'){
    		$(this).find("option").attr("selected",false);
    		if($(this).attr("name")=="status"||$(this).attr("name")=="STATUS"){
    			$(this).change();
    			$(this).find("option:[value=0]").attr("selected",true);
    		}else{
    			$(this).find("option:first").attr("selected",true);
    		}
			$(this).chosen('destroy');
			$(this).chosen({allow_single_deselect:true});
    	}		
 	});
}
//查询
function searchs(){
	if($("#beginDate").val()==""){
		$("#beginDate").tips({ side:3, msg:'请选择开始日期', bg:'#AE81FF',  time:1   });
		return;
	}
	if($("#endDate").val()==""){
		$("#endDate").tips({ side:3, msg:'请选择结束日期', bg:'#AE81FF',  time:1   });
		return;
	}
	top.jzts();
	$("#searchForm").submit();
}
//单页遮罩层
var bgObj = null;

function closeBG()  
{
	if(bgObj != null)
	{
		document.body.removeChild(bgObj);
	}
	bgObj = null;
}
function showBG()
{  
	document.body.style.margin = "0";
	bgObj   = document.createElement("div");
	bgObj.setAttribute('id', 'bgDiv');
	bgObj.style.position   = "absolute";
	bgObj.style.top        = "0";
	bgObj.style.background = "#777";
	bgObj.style.filter     = "progid:DXImageTransform.Microsoft.Alpha(opacity=50)";
	bgObj.style.opacity    = "0.4";
	bgObj.style.left       = "0";
	bgObj.style.width      = "100%";
	bgObj.style.height     = "100%";
	bgObj.style.zIndex     = "1000";
	document.body.appendChild(bgObj);
}