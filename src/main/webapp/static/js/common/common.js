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