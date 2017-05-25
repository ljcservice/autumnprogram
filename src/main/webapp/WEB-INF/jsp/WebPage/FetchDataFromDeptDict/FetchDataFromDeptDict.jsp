<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" %>
<%@page import="com.hitzd.WebPage.PageView,com.hitzd.his.Utils.Browser" %>
<%@page import="com.hitzd.his.Web.Utils.CommonUtils" %>
<%@page import="com.hitzd.DBUtils.*" %>
<%@page import="java.util.*" %>
<!DOCTYPE html>
<%!
   String getStringRequestAttribute(HttpServletRequest req, String name, String replace){
       String result = (String)req.getAttribute(name);
       if(result == null  || "".equals(result))result = replace;
       return result;
   }
%>

<%
    String path = request.getContextPath();
    String pageNoStr = CommonUtils.getRequestParameter(request, "page", "");
    int pageNo = 1;
    if (null != pageNoStr && "".equals(pageNoStr)) {
        pageNo = Integer.valueOf(pageNo);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>科室导入</title>
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
		
		 function ThisTransfer() {
		        if (confirm("系统提示：确认迁移所有数据？")) {
		            with (formPost) {
		                o.value = "transferAll";
		                submit();
		            }
		        }
		    }
		    function transferSingle(deptCode) {
		        with (formPost) {
		            o.value = "transferSingle";
		            p_dept_code.value = deptCode;
		            submit();
		        }
		
		    }
		</script>
	<script type="text/javascript"  src="<%=request.getContextPath() %>/WebPage/public/Newjs.js" ></script>
</head>

<body  background="<%=path%>/skin/images/allbg.gif" style="background-color: white;margin: 0 0 0 0;overflow: hidden;">
<div class="search-bar-container">
<form name="formPost" style="margin: 0 0 0 0" action="<%=path%>/control/FetchDataFromDeptDict" method="post">
        <input name="o" type="hidden" value="query"/>
        <input type="hidden" name="page" value="<%=pageNo%>"/>
        <input type="hidden" name="p_dept_code" value=""/>

        <div class="form-inline search-bar-top search-bar-container">
	            <label>科室代码:<input name="dept_code" value="<%=getStringRequestAttribute(request,"dept_code","")%>"
                        style="width:70px"></label>
            <label>科室名称:<input name="dept_name" value="<%=getStringRequestAttribute(request,"dept_name","")%>"
                        style="width:80px"></label>
	 		<label> 科室属性标识:<input name="clinic_attr" value="<%=getStringRequestAttribute(request,"clinic_attr","")%>"
                        style="width:80px"></label>
	  		<label>门诊住院标识:<input name="outp_or_inp" value="<%=getStringRequestAttribute(request,"outp_or_inp","")%>"
                        style="width:80px"></label>
           	<label>  内外科标识:<input name="internal_or_sergery" value="<%=getStringRequestAttribute(request,"internal_or_sergery","")%>"
                        style="width:80px"></label>
          	<label>  输入码:<input name="input_code" value="<%=getStringRequestAttribute(request,"input_code","")%>"
                        style="width:80px"></label>
	     </div>
		   <table width="100%">
			    <tr >
			        <td  align="right">
						<div class="form-inline search-bar-bottom" style="height: 20px">
							<img alt="统计检索"  src="<%=request.getContextPath() %>/images/query.jpg" onclick="javascript:thisSubmit()" style="cursor: pointer;">
							<input type="button" value="全部迁移" onclick="ThisTransfer();" style="width:60px;" />
						</div>
					</td>
			    </tr>
			   </table>
    </div>
<center>
   <div style="font-family:黑体;font-size:20pt;font-color:black;line-height:40px;text-align:center;">
		HIS科室导入
	</div>
    <table class="table table-bordered header-fixed table-striped table-hover">  
    	<thead> 
	        <tr>
	             <th>No</th>
	             <th>科室代码</th>
	             <th>科室名称</th>
	             <th>科室属性标识</th>
	             <th>门诊住院标识</th>
	             <th>内外科标识</th>
	             <th>输入码</th>
	             <th id="last_th">操作</th>
	         </tr>
         </thead>
       <tbody>
             <%
             PageView<TCommonRecord> pageView = (PageView<TCommonRecord>) request.getAttribute("pageView");
             if (null != pageView) {

                 List<TCommonRecord> list = pageView.getRecords();
                 if(list != null)
                 for (int i = 0; i < list.size(); i++) {
                     String bgcolor = "#FFFFFF";
                     TCommonRecord tcr = list.get(i);
         %>
         <tr>
             <td><%=tcr.get("serial_no")%>
             </td>
             <td><%=tcr.get("dept_code")%>
             </td>
             <td><%=tcr.get("dept_name")%>
             </td>
             <td><%=tcr.get("clinic_attr")%>
             </td>
             <td><%=tcr.get("outp_or_inp")%>
             </td>
             <td><%=tcr.get("internal_or_sergery")%>
             </td>
             <td><%=tcr.get("input_code")%>
             </td>
             <td>
                 <a href="javascript:transferSingle('<%=tcr.get("dept_code")%>')">迁移</a>
             </td>
         </tr>
         <%
                 }
             }
         %>
       </tbody>
       <tfoot>
           <tr height="20">
               <td colspan="12">
                   <%@ include file="../share/fenye.jsp" %>
               </td>
           </tr>
     	</tfoot>
    </table>
</center>
</form>
</body>
</html>