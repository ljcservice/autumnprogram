<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@ page import="com.hitzd.Factory.DBQueryFactory" %>
<%@ page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.his.Utils.*" %>
<%@ page import="com.hitzd.DBUtils.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
	<title>给药频次配码</title>
	<%@include file="/WebPage/reportView/Common.jsp" %>
	<script type="text/javascript">
		//table高度 根据不同要求手动设置
		var SCROLL_TABLE_HIEGHT = 220;
		function ThisSel(pfdi, perform_freq_dict_name, freq_counter, freq_interval, freq_interval_units, frequency)
		{
			window.parent.document.formPost.perform_freq_dict_id.value = pfdi;
			window.parent.document.formPost.perform_freq_dict_name.value = perform_freq_dict_name.replace("&", "'");
			window.parent.document.formPost.freq_counter_pdss.value = freq_counter;
			window.parent.document.formPost.freq_interval_pdss.value = freq_interval;
			window.parent.document.formPost.freq_interval_units_pdss.value = freq_interval_units;
			window.parent.document.formPost.frequency_pdss.value = frequency;
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
<body leftmargin="0" topmargin="0" background='<%=request.getContextPath()%>/images/allbg.gif' style="background-color: white; overflow-y: hidden">
	<form name="formPost" action="<%=request.getContextPath() %>/control/PerformFreqMatcher" method="post">
	<div class="search-bar-container">
		<input type="hidden" name="o" value="queryPDSSList" />
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
			<div class="form-inline search-bar-top">
				<label>给药频次描述:<input type="text" name="q_perform_freq_dict_name" value="<%=CommonUtils.getRequestParameter(request, "q_perform_freq_dict_name", "")%>"></label>
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
			<tr>
				<th>操作</th>
				<th>给药频次描述</th>
				<th>频率次数</th>
				<th>频率间隔</th>
				<th id="last_th">频率间隔单位</th>
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
				<td><input type="button" style="width: 50px;" value="选择" onclick="ThisSel('<%=entity.get("perform_freq_dict_id") %>','<%=entity.get("perform_freq_dict_name").replace("'", "&") %>','<%=entity.get("freq_counter") %>','<%=entity.get("freq_interval") %>','<%=entity.get("freq_interval_units") %>', '<%=entity.get("frequency")%>')"></td>
				<td title="<%=entity.get("perform_freq_dict_name")%>"><%=entity.get("perform_freq_dict_name")%></td>
				<td title="<%=entity.get("freq_counter")%>"><%=entity.get("freq_counter")%></td>
				<td title="<%=entity.get("freq_interval")%>"><%=entity.get("freq_interval")%></td>
				<td title="<%=entity.get("freq_interval_units")%>"><%=entity.get("freq_interval_units")%></td>
			</tr>
			<%
				}
			%>
		</tbody>
		<tfoot>
			<tr>
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