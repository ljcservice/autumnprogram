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
    		if($(this).attr("name")=="status"||$(this).attr("name")=="STATUS"){
    			$(this).find("option:[value=0]").attr("selected",true);
    		}else{
    			$(this).find("option:first").attr("selected",true);
    		}
    		$(this).change();
			$(this).chosen('destroy');
			$(this).chosen({allow_single_deselect:true});
    	}		
 	});
}