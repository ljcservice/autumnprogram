<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<meta charset="utf-8" />
<title>主页</title>
<style>
ul,li{
 list-style:none;
 margin:0px;
 padding:0px;
}
img{
 border:0px; 
}
#main{
 width:200px;
 height:180px;
 overflow:hidden; 
}
#mainDiv img{
	width: 35px;
}
</style>
<link type="text/css" rel="stylesheet" href="static/css/show/show.css"/>
<script type="text/javascript" src="static/js/show/jq.js"></script>
<script>
	
	$(function() {
		//设置位置居中
		var w = ($(top.window).width()-$("#item_content").width())/2;
		var y = ($(top.window).height()-$("#item_content").height())/2;
		$("#item_content").css("left",w).css("top",y);
		$("#myframe").width($(top.window).width()-60);
		$("#myframe").height($(top.window).height()-50);
		$("#mainDiv").height($(top.window).height()-50);
		function Pointer(x, y) {
			this.x = x ;
			this.y = y ;
		}
		function Position(left, top) {
			this.left = left ;
			this.top = top ;
		}
		$(".item_content .item").each(function(i) {
			this.init = function() { // 初始化
				this.box = $(this).parent() ;
				$(this).attr("index", i).css({
					position : "absolute",
					left : this.box.offset().left-w,
					top : this.box.offset().top-y
				}).appendTo(".item_content") ;
				this.drag() ;
			},
			this.move = function(callback) {  // 移动
				$(this).stop(true).animate({
					left : this.box.offset().left-w,
					top : this.box.offset().top-y
				}, 500, function() {
					if(callback) {
						callback.call(this) ;
					}
				}) ;
			},
			this.collisionCheck = function() {
				var currentItem = this ;
				var direction = null ;
				$(this).siblings(".item").each(function() {
					if(
						currentItem.pointer.x > this.box.offset().left &&
						currentItem.pointer.y > this.box.offset().top &&
						(currentItem.pointer.x < this.box.offset().left + this.box.width()) &&
						(currentItem.pointer.y < this.box.offset().top + this.box.height())
					) {
						// 返回对象和方向
						if(currentItem.box.offset().top < this.box.offset().top) {
							direction = "down" ;
						} else if(currentItem.box.offset().top > this.box.offset().top) {
							direction = "up" ;
						} else {
							direction = "normal" ;
						}
						this.swap(currentItem, direction) ;
					}
				}) ;
			},
			this.swap = function(currentItem, direction) { // 交换位置
				if(this.moveing) return false ;
				var directions = {
					normal : function() {
						var saveBox = this.box ;
						this.box = currentItem.box ;
						currentItem.box = saveBox ;
						this.move() ;
						$(this).attr("index", this.box.index()) ;
						$(currentItem).attr("index", currentItem.box.index()) ;
					},
					down : function() {
						// 移到上方
						var box = this.box ;
						var node = this ;
						var startIndex = currentItem.box.index() ;
						var endIndex = node.box.index(); ;
						for(var i = endIndex; i > startIndex ; i--) {
							var prevNode = $(".item_content .item[index="+ (i - 1) +"]")[0] ;
							node.box = prevNode.box ;
							$(node).attr("index", node.box.index()) ;
							node.move() ;
							node = prevNode ;
						}
						currentItem.box = box ;
						$(currentItem).attr("index", box.index()) ;
					},
					up : function() {
						// 移到上方
						var box = this.box ;
						var node = this ;
						var startIndex = node.box.index() ;
						var endIndex = currentItem.box.index(); ;
						for(var i = startIndex; i < endIndex; i++) {
							var nextNode = $(".item_content .item[index="+ (i + 1) +"]")[0] ;
							node.box = nextNode.box ;
							$(node).attr("index", node.box.index()) ;
							node.move() ;
							node = nextNode ;
						}
						currentItem.box = box ;
						$(currentItem).attr("index", box.index()) ;
					}
				}
				directions[direction].call(this) ;
			},
			this.drag = function() { // 拖拽
				var oldPosition = new Position() ;
				var oldPointer = new Pointer() ;
				var isDrag = false ;
				var currentItem = null ;
				$(this).mousedown(function(e) {
					e.preventDefault() ;
					oldPosition.left = $(this).position().left ;
					oldPosition.top =  $(this).position().top;
					oldPointer.x = e.clientX;
					oldPointer.y = e.clientY;
					isDrag = true ;
					
					currentItem = this ;
					
				}) ;
				$(document).mousemove(function(e) {
					var currentPointer = new Pointer(e.clientX, e.clientY) ;
					if(!isDrag) return false ;
					$(currentItem).css({
						"opacity" : "0.8",
						"z-index" : 999
					}) ;
					var left = currentPointer.x - oldPointer.x + oldPosition.left ;
					var top = currentPointer.y - oldPointer.y + oldPosition.top ;
					$(currentItem).css({
						left : left,
						top : top
					}) ;
					currentItem.pointer = currentPointer ;
					// 开始交换位置
					
					currentItem.collisionCheck() ;
					
					
				}) ;
				$(document).mouseup(function() {
					if(!isDrag) return false ;
					isDrag = false ;
					currentItem.move(function() {
						$(this).css({
							"opacity" : "1",
							"z-index" : 0
						}) ;
					}) ;
				}) ;
			}
			this.init() ;
		}) ;
	}) ;
	
	
	function mainDiv(ONTO_TYPE){
		$("#item_content").hide();
		$("#mainDiv").show();
		searchFram(ONTO_TYPE);
	}
	function searchFram(ONTO_TYPE){
		var ontoUrl = '<%=basePath%>' + "show/all.do?ONTO_TYPE="+ONTO_TYPE +"&tm="+new Date().getTime();
		myframe.location.href= ontoUrl;
	}
	function showCtanner(){
		$("#item_content").show();
		$("#mainDiv").hide();
	}
	function tologin(){
		window.open('<%=basePath%>login_toLogin');
	}
	function  toMain(){
		window.open('<%=basePath%>main/index');
	}
</script>
<style>
.item_content ul  {
	list-style:none;
}
.item_content ul li {
	width:200px;
	height:120px;
	float:left;
	margin:10px
}
.item_content {
	width:720px;
	height:460px;
	border:1px solid #ccc;
	position: absolute;
<%--		float:left;--%>
}

.item_content .item {
	width:200px;
	height:120px;
<%--		line-height:120px;--%>
	text-align:center;
	cursor:pointer;
<%--		background:#ccc;--%>
}
.item_content .item img {
	width:200px;
	height:120px;
	border-radius:6px;
	
}
.item div{
	margin-left:50px;
}
.tabDiv{
<%--	-webkit-box-shadow: #666 0px 0px 6px;  --%>
<%--	-moz-box-shadow: #666 0px 0px 6px;  --%>
<%--	box-shadow: #666 0px 0px 6px;  --%>
<%--	background: rgb(153, 204, 255);  --%>
	border:1px solid rgb(153, 204, 255);
}
</style>
</head>
<body>
	<div style="position:relative;width: 100% ;z-index: 1;">
		<div style=" float: right;margin-right: 20px;">
		<c:if test="${pd.SYSNAME !=null}"><a href="javascript:toMain();">主页</a></c:if> 
		<c:if test="${pd.SYSNAME ==null}">
		
		<a href="javascript:tologin();">登录</a>
		
		</c:if>
		</div>
	</div>
	<div class="item_container" style="height: 100%;z-index: 999;">
		
		<div class="item_content" id="item_content">
			<ul>
				<li>
					<div class="item" style="float:right; " title="药品说明书" ondblclick="mainDiv(0);">
						<div class="drugNormal" style="position:relative"></div>
						<font style="">药品说明书</font>
					</div>
				</li>
				
				<li>
					<div class="item" title="个性化给药" ondblclick="mainDiv(1);">
						<div class="gexingNormal" style="position:relative"></div>
						<font style="">个性化给药</font>
					</div>
				</li>
				
				<li>
					<div class="item " title="抗菌药物临床应用指导原则" ondblclick="mainDiv(2);">
						<div class="kangjunNormal" style="position:relative"></div>
						<font style="">抗菌药物临床应用指导原则</font>
					</div>
				</li>
				
				<li>
					<div class="item " title="医学常用计算公式 " ondblclick="mainDiv(3);">
						<div class="gongshiNormal" style="position:relative"></div>
						<font style="">医学常用计算公式</font>
					</div>
				</li>
				
				
				<li>
					<div class="item " title="医药法规 " ondblclick="mainDiv(4);">
						<div class="faguiNormal" style="position:relative"></div>
						<font style="">医药法规</font>
					</div>
				</li>
				
				
				<li>
					<div class="item " title="临床检验正常值及意义" ondblclick="mainDiv(5);">
					<div class="linchuangNormal" style="position:relative"></div>
						<font style="">临床检验正常值及意义</font>
					</div>
				</li>
				<li>
					<div class="item " title="临床路径"  ondblclick="mainDiv(6);">
					<div class="lujingNormal" style="position:relative"></div>
						<font style="">临床路径</font>
					</div>
				</li>
				
				
				<li>
<%--					<div class="item">--%>
<%--					</div>--%>
				</li>
				
				<li>
<%--					<div class="item">--%>
<%--					</div>--%>
				</li>
				
			</ul>
		</div>
		<div style="display: none;height: 100%;" id="mainDiv">
			<table style="width: 100%;height: 100%;z-index: 999;" >
				<tr>
					<td style="width:50px;height: 100%;vertical-align:top">
						<div class="tabDiv">
							<ul style="list-style-type: none;">
								<li><a href="javascript:searchFram(0);" title="药品说明书"><img class="nav-user-photo" src="static/images/show/icon01.png" ></a></li>
								<li><a href="javascript:searchFram(1);" title="个性化给药"><img class="nav-user-photo" src="static/images/show/icon02.png" ></a></li>
								<li><a href="javascript:searchFram(2);" title="抗菌药物临床应用指导原则"><img class="nav-user-photo" src="static/images/show/icon03.png" ></a></li>
								<li><a href="javascript:searchFram(3);" title="医学常用计算公式"><img class="nav-user-photo" src="static/images/show/icon04.png" ></a></li>
								<li><a href="javascript:searchFram(4);" title="医药法规"><img class="nav-user-photo" src="static/images/show/icon05.png" ></a></li>
								<li><a href="javascript:searchFram(5);" title="临床检验正常值及意义"><img class="nav-user-photo" src="static/images/show/icon06.png" ></a></li>
								<li><a href="javascript:searchFram(6);" title="临床路径"><img class="nav-user-photo" src="static/images/show/icon07.png" ></a></li>
								<li ><a href="javascript:showCtanner(7);" title="显示主页"><img class="nav-user-photo" src="static/images/show/jiantou.jpg" ></a></li>
							</ul>
						</div>
					</td>
					<td style="height: 100%;vertical-align:top;">
						<div class="" style="">
							<iframe name="myframe" id="myframe" scrolling="auto" style="border: none;" width="100%" height="100%"></iframe>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>

</body>

</html>
