$(function() {
	//窗口变化，重新初始化 
	$(window).off('resize').on('resize', function() {
		initWidthHeight();
	}).trigger('resize');
	
	bindTreeHide();
});
function initWidthHeight(){
	var myHeight = $(window).outerHeight() ;
	if($("#treeTitle").length >0){
		myHeight = myHeight - $("#treeTitle").outerHeight();
	}
	if(myHeight<200){
		myHeight = 200;
	}
	//alert(myHeight-30);
	$("#treeDiv").css('max-height', myHeight-25+'px');
	if($("#treeFrame").length >0){
		$("#treeFrame").css('height' ,$(window).height()-10+'px');
	}
}
var mywidth = 0;
function bindTreeHide(){
	if($("#mysidebar").length >0){
		$("#mysidebar").toggle(
				function(){
					$("#searchDiv").hide();
					$("#codeDiv").hide();
					$("#treeDiv").hide();
					var mytd = $(this).closest("td");
					mywidth = mytd.css('width');
					mytd.css('width','20px');//.prepend("<div id='mytreeName' class='treeName'>" + $("#treeName").text() +"</div>");
					$(this).find("i").eq(0).removeClass("fa-angle-double-left").addClass("fa-angle-double-right");
				},
				function(){
					$("#searchDiv").show();
					$("#codeDiv").show();
					$(this).closest("td").css('width',mywidth );
					$("#mytreeName").remove();
					$("#treeDiv").show();
					$(this).find("i").eq(0).removeClass("fa-angle-double-right").addClass("fa-angle-double-left");
				}
		);
	}
}