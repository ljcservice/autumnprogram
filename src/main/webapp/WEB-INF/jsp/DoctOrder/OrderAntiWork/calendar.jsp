<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ts" uri="/rights"  %>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="${basePath}">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="static/html_UI/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="static/html_UI/assets/css/font-awesome.css" />

		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="static/html_UI/assets/css/jquery-ui.custom.css" />
		<link rel="stylesheet" href="static/html_UI/assets/css/fullcalendar.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="static/html_UI/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="static/html_UI/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="static/html_UI/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="static/html_UI/assets/css/ace-ie.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
		<script src="static/html_UI/assets/js/ace-extra.js"></script>

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="static/html_UI/assets/js/html5shiv.js"></script>
		<script src="static/html_UI/assets/js/respond.js"></script>
		<![endif]-->
		<!-- #section:basics/navbar.layout -->
</head>
<body class="no-skin">
		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="main-content-inner">
					<!-- #section:basics/content.breadcrumbs -->

					<!-- /section:basics/content.breadcrumbs -->
					<div class="page-content">

						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<div class="row">
									<div class="">
										<div class="space"></div>

										<!-- #section:plugins/data-time.calendar -->
										<div id="calendar"></div>

										<!-- /section:plugins/data-time.calendar -->
									</div>

								</div>

								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->
		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='static/html_UI/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='static/html_UI/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='static/html_UI/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="static/html_UI/assets/js/bootstrap.js"></script>

		<!-- page specific plugin scripts -->
		<script src="static/html_UI/assets/js/jquery-ui.custom.js"></script>
		<script src="static/html_UI/assets/js/jquery.ui.touch-punch.js"></script>
		<script src="static/html_UI/assets/js/date-time/moment.js"></script>
		<script src="static/html_UI/assets/js/fullcalendar.js?v=12"></script>
		<script src="static/html_UI/assets/js/bootbox.js"></script>

		<!-- ace scripts -->
		<script src="static/html_UI/assets/js/ace/elements.scroller.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.colorpicker.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.fileinput.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.typeahead.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.spinner.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.treeview.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.wizard.js"></script>
		<script src="static/html_UI/assets/js/ace/elements.aside.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.ajax-content.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.touch-drag.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.sidebar.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.widget-box.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.settings.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.settings-skin.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.searchbox-autocomplete.js"></script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
		$(top.hangge());
			jQuery(function($) {

/* initialize the external events
	-----------------------------------------------------------------*/

	$('#external-events div.external-event').each(function() {

		// create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
		// it doesn't need to have a start or end
		var eventObject = {
			title: $.trim($(this).text()) // use the element's text as the event title
		};

		// store the Event Object in the DOM element so we can get to it later
		$(this).data('eventObject', eventObject);

		// make the event draggable using jQuery UI
		$(this).draggable({
			zIndex: 999,
			revert: true,      // will cause the event to go back to its
			revertDuration: 0  //  original position after the drag
		});
		
	});


	/* initialize the calendar
	-----------------------------------------------------------------*/
	var zn = '${dateNodes}';
	var dateNodes = eval(zn);
	var dateStart = '${dateStart}';
	var date = new Date();
	if(dateStart!=null && dateStart!=""){
		var dateStr = dateStart.split(" ")[0].split("-");
		date.setFullYear(Number(dateStr[0]));
		date.setMonth(Number(dateStr[1]+1));
		date.setDate(Number(dateStr[2]));
	}
	//var d = date.getDate();
	//var m = date.getMonth();
	//var y = date.getFullYear();
	var calendar = $('#calendar').fullCalendar({
		defaultDate:dateStart,
		//isRTL: true,
		 buttonHtml: {
			prev: '<i class="ace-icon fa fa-chevron-left"></i>',
			next: '<i class="ace-icon fa fa-chevron-right"></i>'
		},
	
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		events: dateNodes,
		editable: true,
		droppable: true, // this allows things to be dropped onto the calendar !!!
		drop: function(date, allDay) { // this function is called when something is dropped
		
			// retrieve the dropped element's stored Event Object
			var originalEventObject = $(this).data('eventObject');
			var $extraEventClass = $(this).attr('data-class');
			
			
			// we need to copy it, so that multiple events don't have a reference to the same object
			var copiedEventObject = $.extend({}, originalEventObject);
			
			// assign it the date that was reported
			copiedEventObject.start = date;
			copiedEventObject.allDay = allDay;
			if($extraEventClass) copiedEventObject['className'] = [$extraEventClass];
			
			// render the event on the calendar
			// the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
			$('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
			
			// is the "remove after drop" checkbox checked?
			if ($('#drop-remove').is(':checked')) {
				// if so, remove the element from the "Draggable Events" list
				$(this).remove();
			}
			
		}
		,
		selectable: true,
		selectHelper: true,
		select: function(start, end, allDay) {
			
// 			bootbox.prompt("New Event Title:", function(title) {
// 				if (title !== null) {
// 					calendar.fullCalendar('renderEvent',
// 						{
// 							title: title,
// 							start: start,
// 							end: end,
// 							allDay: allDay,
// 							className: 'label-info'
// 						},
// 						true // make the event "stick"
// 					);
// 				}
// 			});

// 			calendar.fullCalendar('unselect');
		}
		,
		eventClick: function(calEvent, jsEvent, view) {
			return;
			//display a modal
			var modal = 
			'<div class="modal fade">\
			  <div class="modal-dialog">\
			   <div class="modal-content">\
				 <div class="modal-body">\
				   <button type="button" class="close" data-dismiss="modal" style="margin-top:-10px;">&times;</button>\
				   <form class="no-margin">\
					  <label>Change event name &nbsp;</label>\
					  <input class="middle" autocomplete="off" type="text" value="' + calEvent.title + '" />\
					 <button type="submit" class="btn btn-sm btn-success"><i class="ace-icon fa fa-check"></i> Save</button>\
				   </form>\
				 </div>\
				 <div class="modal-footer">\
					<button type="button" class="btn btn-sm btn-danger" data-action="delete"><i class="ace-icon fa fa-trash-o"></i> Delete Event</button>\
					<button type="button" class="btn btn-sm" data-dismiss="modal"><i class="ace-icon fa fa-times"></i> Cancel</button>\
				 </div>\
			  </div>\
			 </div>\
			</div>';
		
		
			var modal = $(modal).appendTo('body');
			modal.find('form').on('submit', function(ev){
				ev.preventDefault();

				calEvent.title = $(this).find("input[type=text]").val();
				calendar.fullCalendar('updateEvent', calEvent);
				modal.modal("hide");
			});
			modal.find('button[data-action=delete]').on('click', function() {
				calendar.fullCalendar('removeEvents' , function(ev){
					return (ev._id == calEvent._id);
				})
				modal.modal("hide");
			});
			
			modal.modal('show').on('hidden', function(){
				modal.remove();
			});


			//console.log(calEvent.id);
			//console.log(jsEvent);
			//console.log(view);

			// change the border color just for fun
			//$(this).css('border-color', 'red');

		}
		
	});


})
		</script>

		<!-- the following scripts are used in demo only for onpage help and you don't need them -->
		<link rel="stylesheet" href="static/html_UI/assets/css/ace.onpage-help.css" />
		<link rel="stylesheet" href="static/html_UI/docs/assets/js/themes/sunburst.css" />

		<script type="text/javascript"> ace.vars['base'] = '..'; </script>
		<script src="static/html_UI/assets/js/ace/elements.onpage-help.js"></script>
		<script src="static/html_UI/assets/js/ace/ace.onpage-help.js"></script>
		<script src="static/html_UI/docs/assets/js/rainbow.js"></script>
		<script src="static/html_UI/docs/assets/js/language/generic.js"></script>
		<script src="static/html_UI/docs/assets/js/language/html.js"></script>
		<script src="static/html_UI/docs/assets/js/language/css.js"></script>
		<script src="static/html_UI/docs/assets/js/language/javascript.js"></script>
		<style rel="stylesheet">
		.fc-time-grid-container{
			height: auto;
		}
		</style>
	</body>
</html>