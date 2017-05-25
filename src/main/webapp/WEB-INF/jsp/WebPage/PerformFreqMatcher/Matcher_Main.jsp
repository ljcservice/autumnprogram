<%--
  Created by IntelliJ IDEA.
  User: apachexiong
  Date: 10/30/13p
  Time: 6:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" %>
<%@page import="com.hitzd.WebPage.PageView" %>
<%@page import="com.hitzd.his.Web.Utils.CommonUtils" %>
<%@page import="com.hitzd.DBUtils.*" %>
<%@page import="java.util.*" %>
<%

    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>给药频次配码</title>
	<%@include file="/WebPage/reportView/Common.jsp" %>
	<script type="text/javascript">
		//table高度 根据不同要求手动设置
		var SCROLL_TABLE_HIEGHT = 340;
		function thisSubmit(){
			with(formPost){
				o.value="query";
				o.target="_self";
				submit();
				
			}
		}
		
	    function ThisModify(idx)
	    {
	        with (document.formPost)
	        {
	            o.value = "modify";
	            ca_id.value = idx;
	            submit();
	        }
	    }
	
	    function ThisMatch(idx1, idx2)
	    {
	        with (document.formPost)
	        {
	            var url = "<%=request.getContextPath() %>/control/PerformFreqMatcher?o=modify&perform_freq_dict_map_id=" + idx1 + "&perform_freq_dict_name=" + idx2 + "&page=" + page.value + "&t=" + new Date();
	            var obj = new Object();
	            var returnVal = window.showModalDialog(url, obj, "dialogHeight=600px;dialogWidth=950px;resizable=0");
	            if (returnVal != "" && returnVal != "undefined" && returnVal != undefined)
	            {
	                o.value = "query";
	                page.value = returnVal;
	                submit();
	            }
	        }
	    }
	</script>
	<script type="text/javascript"  src="<%=request.getContextPath() %>/WebPage/public/Newjs.js" ></script>
</head>
<body  background="<%=path%>/skin/images/allbg.gif" style="background-color: white;margin: 0 0 0 0;overflow: hidden;">
<form name="formPost" style="margin: 0 0 0 0" action="<%=path%>/control/PerformFreqMatcher" method="post">
    <div class="search-bar-container">
        <input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
        <input type="hidden" name="o" value="query" />
        <div class="form-inline search-bar-top">
           	<label> 给药频次描述:<input name="q_freq_desc" value="<%=CommonUtils.getRequestParameter(request, "q_freq_desc", "")%>"></label>
        </div>
	    <table width="100%">
	        <tr height="20">
	            <td  align="right">
	                <img alt="统计检索"  src="<%=request.getContextPath() %>/images/query.jpg" onclick="javascript:thisSubmit()" style="cursor: pointer;">
	            </td>
	        </tr>
	     </table>
	  </div>
<center>
    <div style="font-family:黑体;font-size:20pt;font-color:black;line-height:40px;text-align:center;">
	 给药频次配码
    </div>
       <table class="table table-bordered header-fixed table-striped table-hover">     
           <thead>
           <tr>
               <th  colspan="4">本院给药频次信息</th>
               <th  colspan="4">配对信息</th>
               <th  rowspan="2" id="last_th">操作</th>
           </tr>
           <tr>
               <th>给药频次描述</th>
               <th>频率次数</th>
               <th>频率间隔</th>
               <th>频率间隔单位</th>
               <th>给药频次描述</th>
               <th>频率次数</th>
               <th>频率间隔</th>
               <th>频率间隔单位</th>
           </tr>
           </thead>
           <tbody>
            <%
                PageView<TCommonRecord> pageView = (PageView) request.getAttribute("pageView");
                if (pageView != null){
                    List<TCommonRecord> list = (List<TCommonRecord>) pageView.getRecords();
                    Map<String, TCommonRecord> map = (Map<String, TCommonRecord>) request.getAttribute("performMap");
                    if (map == null)
                        map = new HashMap<String, TCommonRecord>();
                    if(list != null && list.size()>0)
                    {
                    	for (TCommonRecord entity : list)
                    	{
            %>
            <tr >
                <td title="<%=entity.get("freq_desc")%>"><%=entity.get("freq_desc")%></td>
                <td title="<%=entity.get("freq_counter")%>"><%=entity.get("freq_counter")%></td>
                <td title="<%=entity.get("freq_interval")%>"><%=entity.get("freq_interval")%></td>
                <td title="<%=entity.get("freq_interval_units")%>"><%=entity.get("freq_interval_units")%></td>
                <%
                    TCommonRecord mapEntity = map.get(entity.get("freq_desc"));
                    if (mapEntity == null)
                        mapEntity = new TCommonRecord();
                %>
                <td title="<%=mapEntity.get("perform_freq_dict_name")%>"><%=mapEntity.get("perform_freq_dict_name")%></td>
                <td title="<%=mapEntity.get("freq_counter")%>"><%=mapEntity.get("freq_counter")%></td>
                <td title="<%=mapEntity.get("freq_interval")%>"><%=mapEntity.get("freq_interval")%></td>
                <td title="<%=mapEntity.get("freq_interval_units")%>"><%=mapEntity.get("freq_interval_units")%></td>
                <td>
                    <a href="javascript:ThisMatch('<%=mapEntity.get("perform_freq_dict_map_id")%>', '<%=entity.get("freq_desc")%>')">配对</a>
                </td>
            </tr>
            <%
                    }
            %>
           </tbody>
           <tfoot>
            <tr height="20">
                <td colspan="9">
                    <%@ include file="../share/fenye.jsp" %>
                </td>
            </tr>
           </tfoot>
           <%
                    }
                }
           %>
       </table>
</center>
</form>
</body>
</html>