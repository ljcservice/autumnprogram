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
		myHeight = myHeight - $("#treeTitle").outerHeight() -10;
	}
	if(myHeight<200){
		myHeight = 200;
	}
	$("#treeDiv").css('max-height', myHeight-20+'px');
	if($("#treeFrame").length >0){
		$("#treeFrame").css('height' ,myHeight-20+'px');
	}
}
var mywidth = 0;
function bindTreeHide(){
	if($("#mysidebar").length >0){
		$("#mysidebar").toggle(
				function(){
					$("#treeDiv").hide();
					var mytd = $(this).closest("td");
					mywidth = mytd.css('width');
					mytd.css('width','20px').prepend("<div id='mytreeName' class='treeName'>" + $("#treeName").text() +"</div>");
					$(this).find("i").eq(0).removeClass("fa-angle-double-left").addClass("fa-angle-double-right");
				},
				function(){
					$(this).closest("td").css('width',mywidth );
					$("#mytreeName").remove();
					$("#treeDiv").show();
					$(this).find("i").eq(0).removeClass("fa-angle-double-right").addClass("fa-angle-double-left");
				}
		);
	}
}