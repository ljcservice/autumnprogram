<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" %>
<%@page import="com.hitzd.WebPage.PageView" %>
<%@page import="com.hitzd.his.Web.Utils.CommonUtils" %>
<%@page import="com.hitzd.DBUtils.*" %>
<%@page import="java.util.*" %>
<%!
    String getRequestAttribute(HttpServletRequest request, String name, String replace) {
        String result = (String) request.getAttribute(name);
        if (null == result || "".equals(result)) {
            result = replace;
        }
        return result;
    }
%>
<%

    String path = request.getContextPath();
    PageView<TCommonRecord> pageView = (PageView<TCommonRecord>) request.getAttribute("pageView");
    //为空处理
    if (null == pageView) {
        pageView = new PageView<TCommonRecord>();
    }
    //参数回归
    String iPage = getRequestAttribute(request, "page", "");
    String drugCode = getRequestAttribute(request, "drugCode", "");
    String drugName = getRequestAttribute(request, "drugName", "");
    String whereValue = getRequestAttribute(request, "whereValue", "");
    String whereField = getRequestAttribute(request, "whereField", "");
    String q_is_anti = getRequestAttribute(request, "q_is_anti", "");
    String q_is_psychotic = getRequestAttribute(request, "q_is_psychotic", "");
    String q_is_danger = getRequestAttribute(request, "q_is_danger", "");
    String matched = getRequestAttribute(request, "matched", "");

    //自动配码的条数
    String updCnt = (String) request.getAttribute("updCnt");
    String noUpdCnt = (String) request.getAttribute("noUpdCnt");

%>
<!DOCTYPE html>
<html>
<head>
<base href="${basePath}">
    <title>药品配码</title>
    <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>  
<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
	<script type="text/javascript">
	$(top.hangge());
</script>
<%@include file="../reportView/Common.jsp" %>
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
	        with (formPost) {
	            o.value = "transferAll";
	            submit();
	        }
	    }
	    function transferSingle(drugCode, drugSpec) {
	        with (formPost) {
	            o.value = "transferSingle";
	            drug_code.value = drugCode;
	            drug_spec.value = drugSpec;
	            submit();
	        }
	    }
	    function showBG() {
	        document.body.style.margin = "0";
	        bgObj = document.createElement("div");
	        bgObj.setAttribute('id', 'bgDiv');
	        bgObj.style.position = "absolute";
	        bgObj.style.top = "0";
	        bgObj.style.background = "#777";
	        bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=50)";
	        bgObj.style.opacity = "0.4";
	        bgObj.style.left = "0";
	        bgObj.style.width = "100%";
	        bgObj.style.height = "100%";
	        bgObj.style.zIndex = "1000";
	        document.body.appendChild(bgObj);
	    }
	    function MatchIt(drug_map_id) {
	        var url = "<%=path%>/control/DrugMatcher?drug_map_id=" + drug_map_id + "&o=matcherit";
	        var obj = new Object();
	        var returnVal = window.showModalDialog(url, obj, "dialogHeight=750px;dialogWidth=950px;resizable=0;scroll=no");
	        document.formPost.submit();
	//        alert(returnVal);
	//        if (returnVal != "" && returnVal != "undefined" && returnVal != undefined)
	//        {
	//            with (document.formPost)
	//            {
	//                submit();
	//            }
	//        }
	    }
	    function MatchItAuto() {
	        with (document.formPost) {
	            if (params.value == "") {
	                if (confirm("过滤参数为空，默认将过滤参数设置为“★,☆,●,＃,！,◇,◎,▲,■,#,□,○,※,△,Ξ,注射用,注射剂,注射液”，确定以此参数开始自动配码吗？")) {
	                    showBG();
	                    params.value = "★,☆,●,＃,！,◇,◎,▲,■,#,□,○,※,△,Ξ,注射用,注射剂,注射液";
	                    submit();
	                }
	            }
	            else {
	                showBG();
	                submit();
	            }
	        }
	    }
	    function DeleteIt(drug_map_id) {
	        if (confirm("系统提示：确认删除？")) {
	            with(document.formPost){
	                dmi.value = drug_map_id;
	                o.value="deleteIt";
	                submit();
	            }
	        }
	    }
	    var prevColor;
	    function MouseOver(obj) {
	        prevColor = obj.style.backgroundColor;
	        obj.style.backgroundColor = "yellow";
	    }
	    function MouseOut(obj) {
	        obj.style.backgroundColor = prevColor;
	    }
	    function Print() {
	        with (document.formPost) {
	            o.value = "excelPrint";
	            submit();
	        }
	    }
	</script>
	<script type="text/javascript"  src="js/public/Newjs.js" ></script>
</head>

<body  background="<%=path%>/skin/images/allbg.gif" style="background-color: white;margin: 0 0 0 0;overflow: hidden;">
<div class="search-bar-container">
<form name="formPost" style="margin: 0 0 0 0" action="<%=path%>/control/DrugMatcher" method="post" target="_self">
        <input name="o" type="hidden" value="query"/>
        <input type="hidden" name="page" value="<%=pageView.getCurrentpage()%>"/>
        <input type="Hidden" name="dmi">

        <div class="form-inline search-bar-top search-bar-container">
	            <label>药品代码: <input name="drugCode" value="<%=drugCode%>" size="10"></label>
	            <label>药品名称：<input name="drugName" value="<%=drugName %>" size="10"/></label>
	           	<label> 附加条件:
	            <select name="whereField">
	                <option value=""></option>
	                <option value="is_basedrug" <%="is_basedrug".equals(whereField) ? "selected='selected'" : ""%>>国家基本药物
	                </option>
	                <option value="is_exhilarant" <%="is_exhilarant".equals(whereField) ? "selected='selected'" : ""%>>兴奋剂
	                </option>
	                <option value="is_injection" <%="is_injection".equals(whereField) ? "selected='selected'" : ""%>>注射剂
	                </option>
	                <option value="is_oral" <%="is_oral".equals(whereField) ? "selected='selected'" : ""%>>口服制剂</option>
	                <option value="is_impregnant" <%="is_impregnant".equals(whereField) ? "selected='selected'" : ""%>>溶剂
	                </option>
	                <option value="is_external" <%="is_external".equals(whereField) ? "selected='selected'" : ""%>>外用
	                </option>
	                <option value="is_chinesedrug" <%="is_chinesedrug".equals(whereField) ? "selected='selected'" : ""%>>
	                    		中药
	                </option>
	                <option value="is_allergy" <%="is_allergy".equals(whereField) ? "selected='selected'" : ""%>>抗过敏
	                </option>
	                <option value="is_patentdrug" <%="is_patentdrug".equals(whereField) ? "selected='selected'" : ""%>>中成药
	                </option>
	                <option value="is_tumor" <%="is_tumor".equals(whereField) ? "selected='selected'" : ""%>>抗肿瘤</option>
	                <option value="is_poison" <%="is_poison".equals(whereField) ? "selected='selected'" : ""%>>毒药</option>
	                <option value="is_psychotic" <%="is_psychotic".equals(whereField) ? "selected='selected'" : ""%>>精神药
	                </option>
	                <option value="is_habitforming" <%="is_habitforming".equals(whereField) ? "selected='selected'" : ""%>>
	                    麻药
	                </option>
	                <option value="is_radiation" <%="is_radiation".equals(whereField) ? "selected='selected'" : ""%>>放射
	                </option>
	                <option value="is_precious" <%="is_precious".equals(whereField) ? "selected='selected'" : ""%>>贵重药
	                </option>
	                <option value="is_danger" <%="is_danger".equals(whereField) ? "selected='selected'" : ""%>>高危药</option>
	                <option value="is_otc" <%="is_otc".equals(whereField) ? "selected='selected'" : ""%>>OTC</option>
	                <option value="is_hormone" <%="is_hormone".equals(whereField) ? "selected='selected'" : ""%>>激素类
	                </option>
	                <option value="is_cardiovascular" <%="is_cardiovascular".equals(whereField) ? "selected='selected'" : ""%>>
	                    心脑血管类
	                </option>
	                <option value="is_digestive" <%="is_digestive".equals(whereField) ? "selected='selected'" : ""%>>消化系统类
	                </option>
	                <option value="is_biological" <%="is_biological".equals(whereField) ? "selected='selected'" : ""%>>
	                    生物制品类
	                </option>
	            </select>=
	            <select name="whereValue">
	                <option></option>
	                <option value="1" <%="1".equals(whereValue) ? "selected='selected'" : ""%>>是</option>
	                <option value="0" <%="0".equals(whereValue) ? "selected='selected'" : ""%>>否</option>
	            </select>
	            </label>
	          	<label>抗菌药:
	            <select name="q_is_anti">
	                <option></option>
	                <option value="0" <%="0".equals(q_is_anti) ? "selected='selected'" : ""%>>所有抗菌药</option>
	                <option value="1" <%="1".equals(q_is_anti) ? "selected='selected'" : ""%>>非限制级抗菌药</option>
	                <option value="2" <%="2".equals(q_is_anti) ? "selected='selected'" : ""%>>限制级抗菌药</option>
	                <option value="3" <%="3".equals(q_is_anti) ? "selected='selected'" : ""%>>特殊级抗菌药</option>
	                <option value="4" <%="4".equals(q_is_anti) ? "selected='selected'" : ""%>>局部抗菌药</option>
	                <option value="5" <%="5".equals(q_is_anti) ? "selected='selected'" : ""%>>全身抗菌药</option>
	            </select>
	            </label>
	            <label>精神类药:
	            <select name="q_is_psychotic">
	                <option></option>
	                <option value="0" <%="0".equals(q_is_psychotic) ? "selected='selected'" : ""%>>所有精神类药物</option>
	                <option value="1" <%="1".equals(q_is_psychotic) ? "selected='selected'" : ""%>>I类精神类药</option>
	                <option value="2" <%="2".equals(q_is_psychotic) ? "selected='selected'" : ""%>>II类精神类药</option>
	            </select>
	            </label>
	            <label>高危药:
	            <select name="q_is_danger">
	                <option></option>
	                <option value="0" <%="0".equals(q_is_danger) ? "selected='selected'" : ""%>>所有高危药</option>
	                <option value="1" <%="1".equals(q_is_danger) ? "selected='selected'" : ""%>>A级</option>
	                <option value="2" <%="2".equals(q_is_danger) ? "selected='selected'" : ""%>>B级</option>
	                <option value="3" <%="3".equals(q_is_danger) ? "selected='selected'" : ""%>>C级</option>
	            </select>
	            </label>
	
	         	<label>配对状态:
	            <select name="matched">
	                <option value="">全部</option>
	                <option value="is not null" <%="is not null".equals(matched) ? "selected='selected'" : ""%>>已配对</option>
	                <option value="is null" <%="is null".equals(matched) ? "selected='selected'" : ""%>>未配对</option>
	            </select>
	            </label>
	            <label><a href="<%=request.getContextPath() %>/js/MatchHelp.doc" target="_blank">使用说明</a></label>
	            <!--<br/>
			            自动配码特殊符号(英文逗号分割)：<input type="input" name="params" value="" style="width:150px"/>
			            配码范围: <input type="radio" name="scale" value="1" >全部药品&nbsp;&nbsp;<input type="radio" name="scale" value="2" checked>未配对药品
			            &nbsp;&nbsp;
	            <input type="button" value="  自动配码  " onclick="MatchItAuto()" />
	            -->
	     </div>
	    <table width="100%">
	        <tr height="20">
	            <td background="<%=request.getContextPath() %>/images/tbg.gif" align="right">
	                <img alt="统计检索"  src="<%=request.getContextPath() %>/images/query.jpg" onclick="javascript:thisSubmit()" style="cursor: pointer;">
	                <img alt="导出excel" src="<%=request.getContextPath() %>/images/excel.gif" onclick="javascript:Print()" style="cursor: pointer;">
	            </td>
	        </tr>
	    </table>
	 </form>
 </div>

 <div style="font-family:黑体;font-size:20pt;font-color:black;line-height:40px;text-align:center;">药品配码</div>
   <table class="table table-bordered header-fixed table-striped table-hover">   
      <thead>
         <tr>
             <th colspan="2">本院药品信息</th>
             <th colspan="6">配码信息</th>
             <th rowspan="2" id="last_th">操作</th>
         </tr>
         <tr>
             <th>药品码</th>
             <th>药品名称</th>
             <th>药品名称</th>
             <th>规格</th>
             <th>单位</th>
             <th>剂型</th>
             <th>最小单位剂量</th>
             <th>剂量单位</th>
         </tr>
      </thead>
      <tbody>
           <%
              if (null != pageView) {
                  List<TCommonRecord> list = pageView.getRecords();
                  if(list != null)
                  for (int i = 0; i < list.size(); i++) {
                      TCommonRecord cr = list.get(i);
          %>
          <tr>
              <td><%=cr.get("Drug_No_Local")%>
              </td>
              <td><%=cr.get("Drug_Name_Local")%>
              </td>
              <td><%=cr.get("drug_name")%>
              </td>
              <td><%=cr.get("drug_spec_pdss")%>
              </td>
              <td><%=cr.get("units_pdss")%>
              </td>
              <td><%=cr.get("drug_form_pdss")%>
              </td>
              <td><%=cr.get("dose_per_unit_pdss")%>
              </td>
              <td><%=cr.get("dose_units_pdss")%>
              </td>
              <td>
                  <a href="javascript:DeleteIt('<%=cr.get("drug_map_id")%>')">删除</a>
                  <a href="javascript:MatchIt('<%=cr.get("drug_map_id")%>')">配对</a>
              </td>
          </tr>
          <%
             }
             if(pageView.getTotalpage() > 1) {
          %>
     </tbody>
     <tfoot>
         <%
             if (null != updCnt && !"".equals(updCnt)) {
         %>
         <tr height="20">
             <td colspan="9">
             	<font color="red">自动配码完成！本次完成配码<%=updCnt %>条数据，剩余未配对数据<%=noUpdCnt %> 条！</font>
             </td>
         </tr>
         <%
             }
         %>
         <tr height="20">
             <td colspan="9">
                 <%@ include file="../share/fenye.jsp" %>
             </td>
         </tr>
   	</tfoot>
   	<%}
             }%>
  </table>
</body>
</html>