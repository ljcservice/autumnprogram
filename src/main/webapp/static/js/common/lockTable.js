var mynums = 0;
//锁定table表头和列，TableID表单的ID，FixColumnNumber锁定的列数。arraySubtraction为Array数组，里面放入需要减去的div高度的id
function FixTable(TableID, FixColumnNumber,arraySubtraction) {
	var width = $(window).outerWidth();
	var height = $(window).outerHeight();
	if(mynums==0){
		mynums++;
		$(window).off('resize').on('resize', function() {
			 FixTable(TableID, FixColumnNumber,arraySubtraction);
		}).trigger('resize');
	}
	if(arraySubtraction!=null){
		for (var i=0;i<arraySubtraction.length;i++)
		{
			height = height - $("#"+arraySubtraction[i]).outerHeight();
		}
	}
	$("#"+TableID).css("width",width-17);
	//去除右侧下来条宽度
	if(width<300){
		width =300;
	}else{
		width = width-20;
	}
	if(height<200){
		height = 200;
	}
	if($("#"+TableID).outerHeight()+18 <height){
		height = $("#"+TableID).outerHeight()+18;
	}
	//alert(height);
	if ($("#" + TableID + "_tableLayout").length != 0) {
		$("#" + TableID + "_tableLayout").before($("#" + TableID));
		$("#" + TableID + "_tableLayout").empty();
		$("#" + TableID + "_tableLayout").css("width",width+'px');
		$("#" + TableID + "_tableLayout").css("height",height+'px');
	}
	else {
		$("#" + TableID).after("<div id='" + TableID + "_tableLayout' style='overflow:hidden;height:" + height + "px; width:"+width+"px;'></div>");
	}

	$('<div id="' + TableID + '_tableFix"></div>'
	+ '<div id="' + TableID + '_tableHead"></div>'
	+ '<div id="' + TableID + '_tableColumn"></div>'
	+ '<div id="' + TableID + '_tableData"></div>').appendTo("#" + TableID + "_tableLayout");


	var oldtable = $("#" + TableID);

	var tableFixClone = oldtable.clone(true);
	tableFixClone.attr("id", TableID + "_tableFixClone");
	$("#" + TableID + "_tableFix").append(tableFixClone);
	var tableHeadClone = oldtable.clone(true);
	tableHeadClone.attr("id", TableID + "_tableHeadClone");
	$("#" + TableID + "_tableHead").append(tableHeadClone);
	var tableColumnClone = oldtable.clone(true);
	tableColumnClone.attr("id", TableID + "_tableColumnClone");
	$("#" + TableID + "_tableColumn").append(tableColumnClone);
	$("#" + TableID + "_tableData").append(oldtable);

	$("#" + TableID + "_tableLayout table").each(function () {
		$(this).css("margin", "0");
	});


	var HeadHeight = $("#" + TableID + "_tableHead thead").height();
	HeadHeight += 2;
	$("#" + TableID + "_tableHead").css("height", HeadHeight);
	$("#" + TableID + "_tableFix").css("height", HeadHeight);


	var ColumnsWidth = 0;
	var ColumnsNumber = 0;
	$("#" + TableID + " tr:first th:lt(" + FixColumnNumber + ")").each(function () {
//		alert($(this).outerWidth())
		ColumnsWidth += $(this).outerWidth();
		ColumnsNumber++;
	});
//	ColumnsWidth -= 20;

	if (!$.support.leadingWhitespace) {
		var br=navigator.userAgent.toLowerCase();  
		 var browserVer=(br.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [0, '0'])[1];
		switch (browserVer) {
			case "7.0":
				if (ColumnsNumber >= 3) ColumnsWidth--;
				break;
			case "8.0":
				if (ColumnsNumber >= 2) ColumnsWidth--;
				break;
		}
	}
	// +1 是为了 锁定列时候显示边框显示边框
	if(ColumnsWidth!=0){ColumnsWidth+=1;}
	$("#" + TableID + "_tableColumn").css("width", ColumnsWidth);
	$("#" + TableID + "_tableFix").css("width", ColumnsWidth);


	$("#" + TableID + "_tableData").scroll(function () {
		$("#" + TableID + "_tableHead").scrollLeft($("#" + TableID + "_tableData").scrollLeft());
		$("#" + TableID + "_tableColumn").scrollTop($("#" + TableID + "_tableData").scrollTop());
	});

	$("#" + TableID + "_tableFix").css({ "overflow": "hidden", "position": "relative", "z-index": "50" });
	$("#" + TableID + "_tableHead").css({ "overflow": "hidden", "width": width - 17 , "position": "relative", "z-index": "45"});
	$("#" + TableID + "_tableColumn").css({ "overflow": "hidden", "height": height - 17, "position": "relative", "z-index": "40"});
	$("#" + TableID + "_tableData").css({ "overflow": "scroll", "width": width, "height": height, "position": "relative", "z-index": "35" });

	$("#" + TableID + "_tableFix").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableHead").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableColumn").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableData").offset($("#" + TableID + "_tableLayout").offset());

//	if ($("#" + TableID + "_tableHead").width() > $("#" + TableID + "_tableFix table").width()) {
//		$("#" + TableID + "_tableHead").css("width", $("#" + TableID + "_tableFix table").width());
//		$("#" + TableID + "_tableData").css("width", $("#" + TableID + "_tableFix table").width() + 17);
//	}
//	if ($("#" + TableID + "_tableColumn").height() > $("#" + TableID + "_tableColumn table").height()) {
//		$("#" + TableID + "_tableColumn").css("height", $("#" + TableID + "_tableColumn table").height());
//		$("#" + TableID + "_tableData").css("height", $("#" + TableID + "_tableFix table").height() + 17);
//	}
}