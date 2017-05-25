<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@ page import="com.hitzd.Factory.DBQueryFactory" %>
<%@ page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.his.Utils.*" %>
<%@ page import="com.hitzd.DBUtils.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<%

    String path = request.getContextPath();

%>
<head>
	<title>诊断信息</title>
	<%@include file="/WebPage/reportView/Common.jsp" %>
	<script type="text/javascript">
		//table高度 根据不同要求手动设置
		var SCROLL_TABLE_HIEGHT = 200;
		function ThisSel(diagnosis_dict_id,diagnosis_code,diagnosis_code2,diagnosis_name,acute_indi,side_indi,diag_indi,diagnosis_class,diagnosis_class_code,renal_indi,hepatic_indi,cardio_idi,pulm_indi,neur_indi,endo_indi)
		{
			window.parent.document.formPost.diagnosis_id.value = diagnosis_dict_id;
			window.parent.document.formPost.diagnosis_code.value = diagnosis_code;
			window.parent.document.formPost.diagnosis_code2.value = diagnosis_code2;
			window.parent.document.formPost.diagnosis_name.value = diagnosis_name;
			for (var i = 0; i < window.parent.document.formPost.acute_indi.length; i++)
			{
				if (window.parent.document.formPost.acute_indi[i].value == acute_indi)
					window.parent.document.formPost.acute_indi[i].checked = true;
				else
					window.parent.document.formPost.acute_indi[i].checked = false;
			}
			for (var i = 0; i < window.parent.document.formPost.side_indi.length; i++)
			{
				if (window.parent.document.formPost.side_indi[i].value == side_indi)
					window.parent.document.formPost.side_indi[i].checked = true;
				else
					window.parent.document.formPost.side_indi[i].checked = false;
			}
			for (var i = 0; i < window.parent.document.formPost.diag_indi.length; i++)
			{
				if (window.parent.document.formPost.diag_indi[i].value == diag_indi)
					window.parent.document.formPost.diag_indi[i].checked = true;
				else
					window.parent.document.formPost.diag_indi[i].checked = false;
			}
			window.parent.document.formPost.diagnosis_class.value = diagnosis_class;
			window.parent.document.formPost.diagnosis_class_code.value = diagnosis_class_code;
			for (var i = 0; i < window.parent.document.formPost.renal_indi.length; i++)
			{
				if (window.parent.document.formPost.renal_indi[i].value == renal_indi)
					window.parent.document.formPost.renal_indi[i].checked = true;
				else
					window.parent.document.formPost.renal_indi[i].checked = false;
			}
			for (var i = 0; i < window.parent.document.formPost.hepatic_indi.length; i++)
			{
				if (window.parent.document.formPost.hepatic_indi[i].value == hepatic_indi)
					window.parent.document.formPost.hepatic_indi[i].checked = true;
				else
					window.parent.document.formPost.hepatic_indi[i].checked = false;
			}
			for (var i = 0; i < window.parent.document.formPost.cardio_idi.length; i++)
			{
				if (window.parent.document.formPost.cardio_idi[i].value == cardio_idi)
					window.parent.document.formPost.cardio_idi[i].checked = true;
				else
					window.parent.document.formPost.cardio_idi[i].checked = false;
			}
			for (var i = 0; i < window.parent.document.formPost.pulm_indi.length; i++)
			{
				if (window.parent.document.formPost.pulm_indi[i].value == pulm_indi)
					window.parent.document.formPost.pulm_indi[i].checked = true;
				else
					window.parent.document.formPost.pulm_indi[i].checked = false;
			}
			for (var i = 0; i < window.parent.document.formPost.neur_indi.length; i++)
			{
				if (window.parent.document.formPost.neur_indi[i].value == neur_indi)
					window.parent.document.formPost.neur_indi[i].checked = true;
				else
					window.parent.document.formPost.neur_indi[i].checked = false;
			}
			for (var i = 0; i < window.parent.document.formPost.endo_indi.length; i++)
			{
				if (window.parent.document.formPost.endo_indi[i].value == endo_indi)
					window.parent.document.formPost.endo_indi[i].checked = true;
				else
					window.parent.document.formPost.endo_indi[i].checked = false;
			}
			window.parent.document.formPost.icd9.value = diagnosis_code;
		}
		
		function ThisQuery()
		{
			with (document.formPost)
			{
				o.value = "queryPDSSList";
				submit();
			}
		}
	</script>
	<script type="text/javascript"  src="<%=request.getContextPath() %>/WebPage/public/Newjs.js" ></script>
</head>
<body  background="<%=path%>/skin/images/allbg.gif" style="background-color: white;margin: 0 0 0 0;overflow: hidden;">
	<form name="formPost" action="<%=request.getContextPath() %>/control/DiagnosisMatcher" method="post">
	 <div class="search-bar-container">
		<input type="hidden" name="o" value="queryPDSSList" />
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
		<div class="form-inline search-bar-top search-bar-container">
			<label>诊断码:<input type="text" name="q_diagnosis_code" value="<%=CommonUtils.getRequestParameter(request, "q_diagnosis_code", "")%>"></label>
			<label>诊断名称:<input type="text" name="q_diagnosis_name" value="<%=CommonUtils.getRequestParameter(request, "q_diagnosis_name", "")%>"></label>
		</div>
		<table width="100%">
	        <tr height="20">
	            <td align="right">
						<img alt="统计检索"  src="<%=request.getContextPath() %>/images/query.jpg" onclick="javascript:ThisQuery()" style="cursor: pointer;">
	            </td>
	        </tr>
	     </table>
	 </div>
	<table class="table table-bordered header-fixed table-striped table-hover"> 
			<thead>
				<tr >
					<th>操作</th>
					<th>诊断码</th>
					<th>诊断名称</th>
					<th>诊断分类</th>
					<th id="last_th">诊断分类代码</th>
				</tr>
			</thead>
			<%
				PageView<TCommonRecord> pageView = (PageView) request.getAttribute("pageView");
				if (pageView != null)
				{
			%>
			<tbody>
				<%
					List<TCommonRecord> list = (List<TCommonRecord>) pageView.getRecords();
					for (TCommonRecord entity : list) 
					{
				%>
				<tr>
					<td><input type="button" value="选择" onclick="ThisSel('<%=entity.get("diagnosis_dict_id") %>','<%=entity.get("diagnosis_code") %>','<%=entity.get("diagnosis_code2") %>','<%=entity.get("diagnosis_name") %>','<%=entity.get("acute_indi") %>','<%=entity.get("side_indi") %>','<%=entity.get("diag_indi") %>','<%=entity.get("diagnosis_class") %>','<%=entity.get("diagnosis_class_code") %>','<%=entity.get("renal_indi") %>','<%=entity.get("hepatic_indi") %>','<%=entity.get("cardio_idi") %>','<%=entity.get("pulm_indi") %>','<%=entity.get("neur_indi") %>','<%=entity.get("endo_indi") %>')"></td>
					<td title="<%=entity.get("diagnosis_code")%>"><%=entity.get("diagnosis_code")%></td>
					<td title="<%=entity.get("diagnosis_name")%>"><%=entity.get("diagnosis_name")%></td>
					<td title="<%=entity.get("diagnosis_class")%>"><%=entity.get("diagnosis_class")%></td>
					<td title="<%=entity.get("diagnosis_class_code")%>"><%=entity.get("diagnosis_class_code")%></td>
				</tr>
				<%
					}
				%>
			</tbody>
			<tfoot>
				<tr align="center" bgcolor="#EEF4EA">
					<td height="22" colspan="5">
						<%@ include file="/WebPage/share/fenye.jsp" %>
					</td>
				</tr>
			</tfoot>
			<%
				}
			%>
		</table>
	</form>
</body>
</html>