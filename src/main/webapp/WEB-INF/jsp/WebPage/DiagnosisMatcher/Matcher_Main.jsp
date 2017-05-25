<%--
  modified by IntelliJ IDEA.
  User: apachexiong
  Date: 10/30/13p
  Time: 6:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" %>
<%@page import="com.hitzd.DBUtils.*" %>
<%@page import="java.util.*" %>
<%@ page import="com.hitzd.his.Web.Utils.CommonUtils" %>
<%

    String path = request.getContextPath();

%>
<!DOCTYPE html>
<html>
<head>
<title>诊断配码</title>
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
	    function ThisModify(idx) {
	        with (document.formPost) {
	            o.value = "modify";
	            ca_id.value = idx;
	            submit();
	        }
	    }
	
	    function ThisMatch(idx1, idx2) {
	        with (document.formPost) {
	            var url = "<%=request.getContextPath() %>/control/DiagnosisMatcher?o=modify&diagnosis_map_id=" + idx1 + "&diagnosis_name=" + idx2 + "&page=" + page.value + "&t=" + new Date();
	            var obj = new Object();
	            var returnVal = window.showModalDialog(url, obj, "dialogHeight=600px;dialogWidth=950px;resizable=0");
	            if (returnVal != "" && returnVal != "undefined" && returnVal != undefined) {
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
<form name="formPost" style="margin: 0 0 0 0" action="<%=path%>/control/DiagnosisMatcher" method="post">
    <div class="search-bar-container">
        <input type="hidden" name="page" value="<%=CommonUtils.getRequestParameter(request, "page", "0")%>">
        <input type="hidden" name="o" value="query"/>
        <div class="form-inline search-bar-top search-bar-container">
           <label>诊断代码:<input name="q_diagnosis_code" value="<%=CommonUtils.getRequestParameter(request, "q_diagnosis_code", "")%>"></label>
           <label> 诊断名称:<input name="q_diagnosis_name" value="<%=CommonUtils.getRequestParameter(request, "q_diagnosis_name", "")%>"></label>
        </div>
	    <table width="100%">
	        <tr height="20">
	            <td align="right">
						<img alt="统计检索"  src="<%=request.getContextPath() %>/images/query.jpg" onclick="javascript:thisSubmit()" style="cursor: pointer;">
	            </td>
	        </tr>
	     </table>
     </div>
<center>
	 <div style="font-family:黑体;font-size:20pt;font-color:black;line-height:40px;text-align:center;">
		诊断配码
	</div>
	<table class="table table-bordered header-fixed table-striped table-hover"> 
          <thead>
          <tr>
              <th colspan="2" >本院诊断信息</th>
              <th colspan="4">配码信息</th>
              <th rowspan="2" id="last_th">操作</th>
          </tr>
          <tr>
              <th>诊断码</th>
              <th>诊断名称</th>
              <th>诊断码</th>
              <th>诊断名称</th>
              <th>诊断分类</th>
              <th>诊断分类代码</th>
          </tr>
          </thead>
          <tbody>
	          <%
	              PageView<TCommonRecord> pageView = (PageView) request.getAttribute("pageView");
	              if (pageView != null) {
	                  List<TCommonRecord> list = (List<TCommonRecord>) pageView.getRecords();
	                  Map<String, TCommonRecord> map = (Map<String, TCommonRecord>) request.getAttribute("diagnosisMap");
	                  if (map == null)
	                      map = new HashMap<String, TCommonRecord>();
	                  if(list != null)
	                  for (TCommonRecord entity : list) {
	          %>
	          <tr>
	              <td title="<%=entity.get("diagnosis_code")%>"><%=entity.get("diagnosis_code")%>
	              </td>
	              <td title="<%=entity.get("diagnosis_name")%>"><%=entity.get("diagnosis_name")%>
	              </td>
	              <%
	                  TCommonRecord mapEntity = map.get(entity.get("diagnosis_name"));
	                  if (mapEntity == null)
	                      mapEntity = new TCommonRecord();
	              %>
	              <td title="<%=mapEntity.get("icd9")%>"><%=mapEntity.get("icd9")%>
	              </td>
	              <td title="<%=mapEntity.get("diagnosis_name")%>"><%=mapEntity.get("diagnosis_name")%>
	              </td>
	              <td title="<%=mapEntity.get("diagnosis_class")%>"><%=mapEntity.get("diagnosis_class")%>
	              </td>
	              <td title="<%=mapEntity.get("diagnosis_class_code")%>"><%=mapEntity.get("diagnosis_class_code")%>
	              </td>
	              <td>
	                  <a href="javascript:ThisMatch('<%=mapEntity.get("diagnosis_map_id")%>', '<%=entity.get("diagnosis_name")%>')">配对</a>
	              </td>
	          </tr>
	          <%
	                  }
	              }
	          %>
          </tbody>
          <tfoot>
	          <tr height="20">
	              <td colspan="7">
	                  <%@ include file="../share/fenye.jsp" %>
	              </td>
	          </tr>
          </tfoot>
      </table>
</center>
</form>
</body>
</html>