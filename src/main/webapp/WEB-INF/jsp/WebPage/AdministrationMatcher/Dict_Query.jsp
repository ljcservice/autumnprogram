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
	<title>给药途径信息</title>
	<%@include file="/WebPage/reportView/Common.jsp" %>
	<script type="text/javascript">
		//table高度 根据不同要求手动设置
		var SCROLL_TABLE_HIEGHT = 220;
		function ThisSel(std_route_id, item_name)
		{
			window.parent.document.formPost.administration_id.value = std_route_id;
			window.parent.document.formPost.administration_name.value = item_name;
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
	<form name="formPost" action="<%=request.getContextPath() %>/control/AdministrationMatcher" method="post">
	<div class="search-bar-container">
		<input type="hidden" name="o" value="queryPDSSList" />
		<input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
		<div class="form-inline search-bar-top">
			<label>给药途径名称:<input type="text" name="q_item_name" value="<%=CommonUtils.getRequestParameter(request, "q_item_name", "")%>"></label>
			<label>给药途径类别:
				<select name="q_std_route_id">
					<option></option>
					<%
						List<TCommonRecord> mList = (List<TCommonRecord>) request.getAttribute("List");
						for (TCommonRecord m : mList)
						{
					%>
					<option value="<%=m.get("route_id")%>" <%=m.get("route_id").equals(CommonUtils.getRequestParameter(request, "q_std_route_id", "")) ? "selected='selected'" : "" %>><%=m.get("item_name")%></option>
					<%
						}
					%>
				</select>
			</label>
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
				<th id="last_th">给药途径名称</th>
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
				<td><input type="button"  value="选择" onclick="ThisSel('<%=entity.get("std_route_id") %>','<%=entity.get("item_name") %>')"></td>
				<td title="<%=entity.get("item_name")%>"><%=entity.get("item_name")%></td>
			</tr>
			<%
				}
			%>
		</tbody>
		<tfoot>
			<tr align="center" bgcolor="#EEF4EA">
				<td height="22" colspan="2">
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