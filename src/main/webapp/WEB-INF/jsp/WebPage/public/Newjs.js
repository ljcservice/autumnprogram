/*时间控件的功能*/
function CurDate(){
	var myDate = new Date();
	var str;
	var mon=myDate.getMonth()+1,date=myDate.getDate();
	
	if(mon<10){
		mon="0"+mon;
	}
	if(date<10){
		date="0"+date;
	}
	str=myDate.getFullYear()+"-"+mon+"-"+date;
	
	return str;

}

function endAutoFill(){
	var str="";
	var beginDate=$("[name='beginDate']").val();
	switch(beginDate.substr(5,2)){
		case "01":
			str+=beginDate.substr(0,8)+"31"
			break;
		case "02":
			if(0==beginDate.substr(0,4)%4){
				str+=beginDate.substr(0,8)+"28";
			}else{
				str+=beginDate.substr(0,8)+"28";
			}
			break;
		case "03":
			str+=beginDate.substr(0,8)+"31";
			break;
		case "04":
			str+=beginDate.substr(0,8)+"30";
			break;
		case "05":
			str+=beginDate.substr(0,8)+"31";
			break;
		case "06":
			str+=beginDate.substr(0,8)+"30";
			break;
		case "07":
			str+=beginDate.substr(0,8)+"31";
			break;
		case "08":
			str+=beginDate.substr(0,8)+"31";
			break;
		case "09":
			str+=beginDate.substr(0,8)+"30";
			break;
		case "10":
			str+=beginDate.substr(0,8)+"31";
			break;
		case "11":
			str+=beginDate.substr(0,8)+"30";
			break;
		case "12":
			str+=beginDate.substr(0,8)+"31";
			break;
		default:	
		 	break;
	}
	if(str>CurDate()){
		str=CurDate();
	}
	return str;
}

function DateRight(){
	var Cur="";
	Cur=CurDate();
	
	var beginDate=$("[name='beginDate']").val();
	var endDate=$("[name='endDate']").val();
	
	if(endDate>Cur){
		alert("结束日期不能大于"+Cur+",请重新选择日期！");
		$("[name='endDate']").val("");
	}else{
		if(beginDate>endDate)
		{
			alert("结束日期不能小于开始日期,请重新选择日期！");
			$("[name='endDate']").val("");
		}
	}
	/*if(beginDate==""){
		$("[name='beginDate']").val(beginAutoFill());
	}*/
}
function DateLeft(){
	
	var Cur="";
	Cur=CurDate();
	var beginDate=$("[name='beginDate']").val();
	var endDate=$("[name='endDate']").val();
	if(beginDate>Cur){
		alert("开始日期不能大于"+Cur+",请重新选择日期！");
		$("[name='beginDate']").val("");
	}else{
		/*if(beginDate>endDate && endDate !="")
		{
			//alert("开始日期不能大于结束日期,请重新选择日期！");
			$("[name='beginDate']").val("");
		}else{*/
		if(beginDate>endDate)
			$("[name='endDate']").val(endAutoFill());
		//}
	}
}

$().ready(function(){
	 //重新调整表格的高度
	function resizeTableHeight(){
		//最大的表格滚动高度=窗口的高度-表格上方搜索区域的高度（例子中为90）
		var scrollBodyHeight = SCROLL_TABLE_HIEGHT;
		//表格实际高度
		var scrollBodyTableHeight = $(".dataTables_scrollBody table").height();
		//设置表格的滚动高度
		$(".dataTables_scrollBody").css("height",scrollBodyTableHeight>scrollBodyHeight?scrollBodyHeight:scrollBodyTableHeight);
	}

	//当窗体的大小发生变化的时候，需要重新调整表格的高度
	$(window).resize(function(){
		resizeTableHeight();
	});
	
	$(".header-fixed").ready(function(){
		var scrollBodyHeight = SCROLL_TABLE_HIEGHT;
		//表格实际高度
		var scrollBodyTableHeight=$(".dataTables_scrollBody table").height();
		if(scrollBodyTableHeight>scrollBodyHeight) {
			//增加表头最后一列的宽度以适应右边滚动条多出来的宽度
			$(".dataTables_scrollHeadInner").addClass("all_width");
			$(".dataTables_scrollHeadInner .header-fixed").addClass("all_width");
			$("#last_th").addClass("add_border");
		}else{
			$(".dataTables_scrollHeadInner").addClass("all_width");
			$(".dataTables_scrollHeadInner .header-fixed").addClass("all_width");
		}
		//当表格加载完毕时，向表格正文中的第一行增加first的class
		$(".dataTables_scrollBody .header-fixed tbody tr:first").addClass("first");
		//当表格加载完毕时，需要重新调整表格的高度
		resizeTableHeight();
	}).dataTable( {
		"sScrollY": document.documentElement.clientHeight, //如果表格正文超过设置的高度，则会出现竖向滚动条。
		"bPaginate": false, //是否显示表格的分页组件。
		"bFilter": false, //是否显示表格的查询组件。
		"bScrollCollapse": false, //
		"bInfo":false, //是否显示表格记录的信息，例如共多少条记录等。
		"bAutoWidth": true, //是否自动设置表格宽度。
		"bSort": false, //是否开启自动排序
		oLanguage: {
			sEmptyTable: "暂无数据"
		}
	});
});