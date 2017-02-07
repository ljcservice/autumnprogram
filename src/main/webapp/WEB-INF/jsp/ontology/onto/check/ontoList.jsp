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
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<form action="ontology/checkList.do" method="post">
							<input type="hidden" name="ontoType" value="${ontoType}" id="ontoType"/>
						</form>
						<div class="col-xs-12">
						<div>
						<c:if test="${ ontoType=='51005'}">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" nowrap>${ontoName}名称中文</th>
									<th class="center" nowrap>${ontoName}名称英文</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>附加编码</th>
									<th class="center" nowrap>术语类型</th>
									<th class="center" nowrap>停用标记</th>
									<th class="center" nowrap>变更描述</th>
									<th class="center" nowrap>更新人</th>
<!-- 									<th class="center" nowrap>更新时间</th> -->
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<c:if test="${diag.STATUS==0  }">
										<tr ondblclick="detailInfo2(this);">
											<td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' class="ace" value="${diag.H_ID}" D_ID="${diag.D_ID}"/><span class="lbl"></span></label>
										</c:if>
										<c:if test="${diag.STATUS!=0  }">
										<tr ondblclick="detailInfo3(this);">
											<td class='center' style="width: 30px;">
												<input type="hidden" name='hids' class="ace" value="${diag.H_ID}" D_ID="${diag.D_ID}"/>
										</c:if>
											</td>
											<td class="center ">
												<a onclick="detailInfo('${diag.H_ID}');">${diag.STAD_DN_CHN }</a>
												&nbsp;
												<c:if test="${diag.OP_TYPE != 0 }">
													<a style="white-space: nowrap; width: 30px;" class="specila" onclick="fixer_tree(event,'${diag.D_ID}');" >定位</a>
												</c:if>
											</td>
											<td class="center ">${diag.STAD_DN_ENG }</td>
											<td class="center " >${diag.MAIN_CODE }</td>
											<td class="center " >${diag.ADD_CODE }</td>
											<td class="center " ><c:if test="${diag.TERM_TYPE==1}">症状</c:if><c:if test="${diag.TERM_TYPE==2}">疾病</c:if></td>
											<td class="center">
												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if>
												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if>
											</td>
											<td class="center " title="${diag.UPD_DESC }" style="max-width: 150px;"><div style="max-height: 20px;width: 100%;overflow: hidden;">${diag.UPD_DESC }</div></td>
											<td class="center " >${diag.UPDATE_MAN }</td>
<!-- 											<td class="center "><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td> -->
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						<c:if test="${ ontoType=='51003'}">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" nowrap>${ontoName}名称中文</th>
									<th class="center" nowrap>${ontoName}名称英文</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>停用标记</th>
									<th class="center" nowrap>变更描述</th>
									<th class="center" nowrap>更新人</th>
<!-- 									<th class="center" nowrap>更新时间</th> -->
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<c:if test="${diag.STATUS==0  }">
										<tr ondblclick="detailInfo2(this);">
											<td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' class="ace" value="${diag.H_ID}" D_ID="${diag.D_ID}"/><span class="lbl"></span></label>
										</c:if>
										<c:if test="${diag.STATUS!=0  }">
										<tr ondblclick="detailInfo3(this);">
											<td class='center' style="width: 30px;">
												<input type="hidden" name='hids' class="ace" value="${diag.H_ID}" D_ID="${diag.D_ID}"/>
										</c:if>
											</td>
											<td class="center ">
												<a onclick="detailInfo('${diag.H_ID}');">${diag.STAD_DN_CHN }</a>
												&nbsp;
												<c:if test="${diag.OP_TYPE != 0 }">
													<a class="specila" onclick="fixer_tree(event,'${diag.D_ID}');">定位</a>
												</c:if>
											</td>
											<td class="center ">${diag.STAD_DN_ENG }</td>
											<td class="center " >${diag.MAIN_CODE }</td>
											<td class="center">
												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if>
												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if>
											</td>
											<td class="center "  style="max-width: 150px;" title="${diag.UPD_DESC }"><div style="max-height: 20px;width: 100%;overflow: hidden;">${diag.UPD_DESC }</div></td>
											<td class="center " >${diag.UPDATE_MAN }</td>
<!-- 											<td class="center "><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td> -->
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						<c:if test="${ ontoType=='51006'}">
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:10px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" nowrap>${ontoName}名称中文</th>
									<th class="center" nowrap>主要编码</th>
									<th class="center" nowrap>科室定义</th>
									<th class="center" nowrap>停用标记</th>
									<th class="center" nowrap>变更描述</th>
									<th class="center" nowrap>更新人</th>
									<th class="center" nowrap>更新时间</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty resultList}">
									<c:forEach items="${resultList}" var="diag" varStatus="vs">
										<c:if test="${diag.STATUS==0  }">
										<tr ondblclick="detailInfo2(this);">
											<td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' class="ace" value="${diag.H_ID}" D_ID="${diag.D_ID}"/><span class="lbl"></span></label>
										</c:if>
										<c:if test="${diag.STATUS!=0  }">
										<tr ondblclick="detailInfo3(this);">
											<td class='center' style="width: 30px;">
												<input type="hidden" name='hids' class="ace" value="${diag.H_ID}" D_ID="${diag.D_ID}"/>
										</c:if>
											</td>
											<td class="center ">
												<a onclick="detailInfo('${diag.H_ID}');">${diag.STAD_DN_CHN }</a>
												&nbsp;
												<c:if test="${diag.OP_TYPE != 0 }">
													<a class="specila" onclick="fixer_tree(event,'${diag.D_ID}');">定位</a>
												</c:if>
											</td>
											<td class="center " >${diag.MAIN_CODE }</td>
											<td class="center " >${diag.TERM_DEFIN }</td>
											<td class="center">
												<c:if test="${diag.IS_DISABLE == '0' }">否</c:if>
												<c:if test="${diag.IS_DISABLE == '1' }">是</c:if>
											</td>
											<td class="center "  style="max-width: 150px;" title="${diag.UPD_DESC }"><div style="max-height: 20px;width: 100%;overflow: hidden;">${diag.UPD_DESC }</div></td>
											<td class="center " >${diag.UPDATE_MAN }</td>
											<td class="center "><fmt:formatDate value="${diag.UPDATE_TIME}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										</tr>
									
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						</c:if>
						</div>
						<div class="">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<div style="vertical-align:bottom;float: left;padding-top: 0px;margin-top: 0px;">
										<ts:rights code="ONTO_CHECK_PASS_ALL_${RIGHTS}">
											<a class="btn btn-mini btn-success" onclick="passRefuseAll(0);">批量通过</a>
										</ts:rights>
										<ts:rights code="ONTO_CHECK_REFUSE_ALL_${RIGHTS}">
											<a class="btn btn-mini btn-danger" onclick="passRefuseAll(1);">批量拒绝</a>
										</ts:rights>
<!-- 										<a class="btn btn-mini btn-primary" onclick="btnDetailInfo();">详情</a> -->
<!-- 										<a class="btn btn-mini btn-warning" onclick="hisDetail();">历史</a> -->
									</div>
									<div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
								</td>
							</tr>
						</table>
						</div>
		
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	</body>

<script type="text/javascript">
$(top.hangge());

$(function() {
	//重置页面高度
	treeFrameT("treeFrame");
	function treeFrameT(obj){
		var hmainT = parent.document.getElementById(obj);
		hmainT.style.width = '100%';
		hmainT.style.height = ($("#main-container").height()+10) + 'px';
	}
	//复选框全选控制
	var active_class = 'active';
	$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
		var th_checked = this.checked;//checkbox inside "TH" table header
		$(this).closest('table').find('tbody > tr').each(function(){
			var row = this;
			if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
			else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
		});
	});
});

function btnDetailInfo(){
	var c = $("input[name=ids]:checked").length;
	if(c!=1){
		$("#simple-table tbody").children().first().children().first().children().first().tips({
			side:2,
            msg:'请勾选一条数据',
            bg:'#AE81FF',
            time:2
        });
        return false;
	}
	var id = $("input[name=ids]:checked").eq(0).val();
	detailInfo(id);
}
function detailInfo2(obj){
	var id = $(obj).find("input[name=ids]").eq(0).val();
	detailInfo(id);
}
function detailInfo3(obj){
	var id = $(obj).find("input[name=hids]").eq(0).val();
	detailInfo(id);
}
//审核详情
function detailInfo(id){
	top.jzts();
	var diag = new top.Dialog();
	var ontoTypeCurent = $("#ontoType").val();
	diag.Drag=true;
	diag.URL = path+'/ontology/detailInfo.do?ontoType='+ontoTypeCurent+'&H_ID='+id;
	diag.Width = 900;
	diag.Height = 500;
	if(ontoTypeCurent=="51001"){
		diag.Title ="药品变更详情";
	}else if(ontoTypeCurent=="51003"){
		diag.Title ="手术变更详情";
	}else if(ontoTypeCurent=="51005"){
		diag.Title ="诊断变更详情";
	}else if(ontoTypeCurent=="51006"){
		diag.Title ="科室变更详情";
	}
	diag.CancelEvent = function(){ //关闭事件
	var diagdocument = diag.innerFrame.contentWindow.document;
		if(diagdocument.getElementById('zhongxin').style.display == 'none' || diagdocument.getElementById('refreshFlag').value=='1'){
			top.jzts();
			setTimeout("self.location=self.location",100);
		}
		diag.close();
	};
	diag.show();
}
//批量通过   批量拒绝
function passRefuseAll(type){
	var str = '';
	for(var i=0;i < document.getElementsByName('ids').length;i++)
	{
		  if(document.getElementsByName('ids')[i].checked){
		  	if(str=='') str += document.getElementsByName('ids')[i].value;
		  	else str += ';' + document.getElementsByName('ids')[i].value;
		  }
	}
	if(str==''){
		bootbox.dialog({
			message: "<span class='bigger-110'>您没有选择任何内容!</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		$("#zcheckbox").tips({
			side:3,
            msg:'点这里全选',
            bg:'#AE81FF',
            time:3
        });
		
		return;
	}
	var url = "";
	var msg = "确定通过全部选中的数据吗?";
	if(type==0){
		url = basePath+'ontology/checkPassAll.do';
	}else{
		url = basePath+'ontology/checkRefuseAll.do';
		msg = "确定拒绝全部选中的数据吗?";
	}
	bootbox.confirm(msg,function(result) {
		if(result) {
			var ontoType = $("#ontoType").val();
			top.jzts();
			$.ajax({
				type: "POST",
				url: url,
			   	data: {ontoType:ontoType,H_IDS:str},
				dataType:'json',
				//beforeSend: validateData,
				cache: false,
				success: function(data){
					if(data.result=="success"){
						nextPage(${page.currentPage});
					}else{
						$(top.hangge());
						var msg = data.result;
						if(msg=="failed"){msg="操作失败！";}
						bootbox.dialog({
							message: "<span class='bigger-110'>"+msg+"</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
					}
				}
			});
		}
	});
}
//单个历史详情
function hisDetail(){
	var obj = $("input[name=ids]:checked");
	if(obj.length != 1){
		$("#simple-table tbody").children().first().children().first().children().first().tips({
			side:2,
            msg:'请勾选一条数据',
            bg:'#AE81FF',
            time:2
        });
        return false;
	}
	top.jzts();
	var diag = new top.Dialog();
	var ontoTypeCurent = $("#ontoType").val();
	diag.Drag=true;
	diag.URL = path+'/ontology/hisDetail.do?ontoType='+ontoTypeCurent+'&D_ID='+obj.eq(0).attr("D_ID");
	diag.Width = 900;
	diag.Height = 500;
	diag.Title ="变更历史详情";
	diag.CancelEvent = function(){ //关闭事件
		diag.close();
	};
	diag.show();
}
//定位
var purl = basePath+'ontoTree/treePidsById.action?ontotype='+$("#ontoType").val();
function fixer_tree(e,id){
	//window.event? window.event.cancelBubble = true : e.stopPropagation();
	parent.fixerTree(id,purl,$("#ontoType").val());
}
</script>
</html>
