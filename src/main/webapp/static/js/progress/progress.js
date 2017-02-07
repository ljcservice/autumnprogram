$(document).ready(function(){
	var vInterval ;
	var stop = 0;
jQuery.fn.complate_progressbar = function(aOptions){
	this.children('.percent').html('<b>100%</b>');
	this.children('.pbar').children().eq(0).css("width","100%");
	this.children('.elapsed').html('已完成');
	clearInterval(vInterval);
	stop=1;
};
jQuery.fn.anim_progressbar = function (aOptions) {
stop = 0;
// def values
var iCms = 1000;
var iMms = 60 * iCms;
var iHms = 3600 * iCms;
var iDms = 24 * 3600 * iCms;
// def options
var aDefOpts = {
start: new Date(), // now
finish: new Date().setTime(new Date().getTime() + 60 * iCms), // now + 60 sec
interval: 100
}
var aOpts = jQuery.extend(aDefOpts, aOptions);
var vPb = this;
// each progress bar
return this.each(
function() {
var iDuration = aOpts.finish - aOpts.start;
// calling original progressbar
$(vPb).children('.pbar').progressbar();
// looping process
 vInterval = setInterval(
function(){
if(stop>=1){clearInterval(vInterval);}
var iLeftMs = aOpts.finish - new Date(); // left time in MS
var iElapsedMs = new Date() - aOpts.start, // elapsed time in MS
iDays = parseInt(iLeftMs / iDms), // elapsed days
iHours = parseInt((iLeftMs - (iDays * iDms)) / iHms), // elapsed hours
iMin = parseInt((iLeftMs - (iDays * iDms) - (iHours * iHms)) / iMms), // elapsed minutes
iSec = parseInt((iLeftMs - (iDays * iDms) - (iMin * iMms) - (iHours * iHms)) / iCms), // elapsed seconds
iPerc = (iElapsedMs > 0) ? iElapsedMs / iDuration * 100 : 0; // percentages
// display current positions and progress
$(vPb).children('.percent').html('<b>'+iPerc.toFixed(1)+'%</b>');
$(vPb).children('.elapsed').html(iDays+' days '+iHours+'h:'+iMin+'m:'+iSec+'s</b>');
$(vPb).children('.pbar').children('.ui-progressbar-value').css('width', iPerc+'%');
// in case of Finish
if (iPerc >= 100) {
clearInterval(vInterval);
$(vPb).children('.percent').html('<b>100%</b>');
$(vPb).children('.elapsed').html('已完成');
}
} ,aOpts.interval
);
}
);
}
// default mode
//$('#progress1').anim_progressbar();
// from second #5 till 15
//var iNow = new Date().setTime(new Date().getTime() + 5 * 1000); // now plus 5 secs
//var iEnd = new Date().setTime(new Date().getTime() + 10 * 1000); // now plus 15 secs
//$('#progress2').anim_progressbar({start: iNow, finish: iEnd, interval: 100});
// we will just set interval of updating to 1 sec
//$('#progress3').anim_progressbar({interval: 1000});
});