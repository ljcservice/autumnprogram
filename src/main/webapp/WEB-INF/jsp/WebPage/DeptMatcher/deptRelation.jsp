<%@page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@ page import="com.hitzd.DBUtils.*" %>
<%@ page import="com.hitzd.his.Utils.DictCache" %>
<%@ page import="java.util.*" %>
<%@page import="com.hitzd.mi.Utils.DeptUtils" %>
<html>
<head>
<title>科室关系维护</title>

<style>
.text {
	layout-flow: vertical-ideographic;
	height: 110px;
	line-height: 100%;
}
td{
	border-left:solid 1px #eee;
	border-top:solid 1px #eee;
}
label{
	float:left;
}
<%
	TCommonRecord dept = (TCommonRecord)request.getAttribute("matchingDept");
	if(dept == null)dept = new TCommonRecord();
	List<TCommonRecord> deptList = (List<TCommonRecord>)request.getAttribute("allDept");
	List<TCommonRecord> childDeptList = (List<TCommonRecord>)request.getAttribute("allChildDept");
	if(deptList == null){deptList = new ArrayList<TCommonRecord>();}
	if(childDeptList == null){childDeptList = new ArrayList<TCommonRecord>();}
	String childDeptString = "";
	String childDeptCode = ",";
	for(TCommonRecord child:childDeptList){
		childDeptString += child.get("dept_name")+",";
		childDeptCode += child.get("dept_code") + ",";
	}
	if(childDeptList.size()>0){
		childDeptString = childDeptString.substring(0,childDeptString.length()-1);
	}
	String allParent = ",";
	for(TCommonRecord parent:deptList){
		if(!parent.get("parent_dept_code").equals(parent.get("dept_code"))){
			allParent += parent.get("parent_dept_code") + ",";
		}
	}
%>
</style>
	<script type="text/javascript" src="/COMMON/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="/COMMON/jquery/jquery-migrate.min.js"></script>
	<script type="text/javascript" src="/COMMON/bootstrap/js/bootstrap.min.js"></script>
	<!--[if lte IE 6]>
	<script type="text/javascript" src="/COMMON/bootstrap/js/bootstrap-ie.js"></script>
	<![endif]-->
	<script type="text/javascript" src="/COMMON/hitzd/js/common.js"></script>
	<link type="text/css" rel="stylesheet" href="/COMMON/bootstrap/css/bootstrap.min.css" />
	<!--[if lte IE 6]>
	<link rel="stylesheet" type="text/css" href="/COMMON/bootstrap/css/bootstrap-ie6.css">
	<![endif]-->
	<!--[if lte IE 7]>
	<link rel="stylesheet" type="text/css" href="/COMMON/bootstrap/css/ie.css">
	<![endif]-->
	<link type="text/css" rel="stylesheet" href="/COMMON/hitzd/css/common.css" />
	<script type="text/javascript" src="/COMMON/hitzd/js/nav2.1.js"></script>
	<script type="text/javascript"  src="/COMMON/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/COMMON/hitzd/js/tableSortTool.js"></script>
	
	<script type="text/javascript">
	function returnBack(){
		if(confirm("还未保存，确认上一页？")){
			window.close();
		}
	}
	/**
	监听关系改变事件
	*/
	function changeDeptInput(obj){
		var value = obj.value;
		if(value == '1'){
			//父科室
			$("#childDept").hide();
			$("#parentDept").show();
		}else if(value == '2'){
			//子科室
			$("#childDept").show();
			$("#parentDept").hide();
		}else{
			//无
			$("#childDept").hide();
			$("#parentDept").hide();
		}
	}
	
	
	function insert(){
		var pattern = $("select[name='a_dept_relation']").val();
		if('0' == pattern){
			alert("请填写完整必要条件");
			return;
		}else if('1' == pattern){
			if('<%=childDeptString.trim()%>' != ''){
				alert("系统提示：该科室含有子科室，不能增加父科室");
				return;
			}
			if($("select[name='a_parent_dept_name']").val() == ''){
				if(confirm("系统提示：您确认将该科室父科室置空？") == false){
					return;
				};
			}
		}else if('2' == pattern){
			if('<%=dept.get("parent_dept_name").equals(dept.get("dept_name"))?"":dept.get("parent_dept_name")%>' != ''){
				alert("系统提示：该科室含有父科室，不能增加子科室");
				return;
			}
			if($("input[name='a_child_dept_names']").val() == ''){
				if(confirm("系统提示：您确认将该科室子科室置空？") == false){
					return;
				};
			}
		}
		with(document.formPost){	
			submit();
		}
		window.returnValue = 'OK';
		window.close();
	}
	$(document).ready(function(){
		$("#childDept").hide();
		$("#parentDept").hide();
	});
	window.name="submitSelf";
	</script>
</head>

<body leftmargin="0" topmargin="0">
<form name="formPost" style="margin: 0 0 0 0" action="<%=request.getContextPath()%>/control/DeptMatcher" method="post" target="submitSelf">
	<input name="o" type="hidden" value="saveMatch" />
	<input name="a_dept_code" type="hidden" value="<%=dept.get("dept_code") %>" />
	<input name="a_dept_name" type="hidden" value="<%=dept.get("dept_name") %>" />
	
	<!-- !分类-->
		<div id="popover-charge-type-box" class="popover-box" data-field="field-dept-name" data-field-code="a_child_dept_codes">
			<div class="close-container">
				<a href="#">【关闭】</a>
			</div>
			<input type="hidden" class="JUST_FOR_FIREFOX_DONT_REMOVE"/>
			<div class="popover-body" style="width:250px;height:220px;">
				<label class="chk-all" style="width:200px;">
					<input type="checkbox" class="chk-all"/>
					全部
				</label>
					
					<%
						String aclinicAttrFlag = "";
						String aoutpOrInpFlag = "";
						String ainternalOrSergeryFlag = "";
						for(int i=0; i<deptList.size(); i++){
							TCommonRecord department = deptList.get(i);
							int width = 16*(department.get("dept_name").length());
							if(width < 110)width=110;
							if(width > 250)width=250;
							if(!department.get("clinic_attr").equals(aclinicAttrFlag)){
								aclinicAttrFlag = department.get("clinic_attr"); 
								out.append("<label style='width:200px;clear:both'><strong>"+DeptUtils.deptClinicAttr(department.get("clinic_attr"))+"</strong></label>");
							}
							if(!department.get("outp_or_inp").equals(aoutpOrInpFlag)){
								aoutpOrInpFlag = department.get("outp_or_inp"); 
								out.append("<label style='width:200px;clear:both'>|--><strong>"+DeptUtils.deptOutpOrInp(department.get("outp_or_inp"))+"</strong></label>");
							}
							if(!department.get("internal_or_sergery").equals(ainternalOrSergeryFlag)){
								ainternalOrSergeryFlag = department.get("internal_or_sergery"); 
								out.append("<label style='width:200px;clear:both'>|--|--><strong>"+DeptUtils.deptInternalOrSergery(department.get("internal_or_sergery"))+"</strong></label>");
							}
					%>
					
					<label style="width:<%=width%>px;<%=width>140?"clear:both":""%>">
						<%
							boolean isSelected = (","+childDeptCode+",").contains(","+department.get("dept_code")+",");
							boolean isSelectedByOther = 
								(department.get("dept_code").equals(department.get("parent_dept_code")) || (department.get("parent_dept_code")).equals(dept.get("dept_code")) )
									&& !allParent.contains(","+department.get("dept_code")+",");
							String deptColor = "black";
							if(isSelected){
								deptColor="red";
							}else if(!isSelectedByOther){
								deptColor="green";
							}
							
							if(isSelectedByOther && !dept.get("dept_code").equals(department.get("dept_code"))){
						%>
						<input type="checkbox" value='<%=department.get("dept_name")%>' name="ORG_NAME" <%=isSelected?"checked":""%> text="<%=department.get("dept_code")%>"/>
						<%
							}
						%>
							<font color='<%=deptColor %>'>
								<%=department.get("dept_name")%>
							</font>
					</label>
					<%
						}
					%>
			</div>
		</div>
	<div class="search-bar-top">
	<table  style="border-collapse:collapse;border:1px solid #ccc;" align="center"  cellpadding="4" width="80%"  cellspacing="1" bgcolor="#CBD8AC">
		<thead>
			<tr  bgcolor="#EEEEEE">
				<th colspan="2">科室关系维护</th>
			</tr>
		</thead>
		<tbody style="font-size:12px">
			<tr   height="22" >
				<td  align="right"  width="40%">科室名称：</td>
				<td  align="left">
					<%=dept.get("dept_name") %>
				</td>
			</tr>
			<tr>
				<td  align="right" >科室关系：</td>
				<td  align="left" >
					<select name="a_dept_relation" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;" onchange="changeDeptInput(this)">
						<option value="0" >--请选择--</option>
						<option value="1" >设置父科室</option>
						<option value="2" >设置子科室</option>
					</select>
				</td>
			</tr>
			<tr id="parentDept">
				<td  align="right" >父科室：</td>
				<td  align="left" >
					<select name="a_parent_dept_code" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;">
						<option value="" >---无---</option>
						<%
						String clinicAttrFlag = "";
						String outpOrInpFlag = "";
						String internalOrSergeryFlag = "";
						for(int i=0; i<deptList.size(); i++){
							TCommonRecord department = deptList.get(i);
							if(!department.get("clinic_attr").equals(clinicAttrFlag)){
								clinicAttrFlag = department.get("clinic_attr"); 
								out.append( "<option >|-"+DeptUtils.deptClinicAttr(department.get("clinic_attr"))+"</option>");
							}
							if(!department.get("outp_or_inp").equals(outpOrInpFlag)){
								outpOrInpFlag = department.get("outp_or_inp"); 
								out.append("<option >|-----|-"+DeptUtils.deptOutpOrInp(department.get("outp_or_inp"))+"</option>");
							}
							if(!department.get("internal_or_sergery").equals(internalOrSergeryFlag)){
								internalOrSergeryFlag = department.get("internal_or_sergery"); 
								out.append("<option >|-----|-----|"+DeptUtils.deptInternalOrSergery(department.get("internal_or_sergery"))+"</option>");
							}
							boolean deptFlag = dept.get("parent_dept_code").equals(department.get("dept_code")) && !dept.get("dept_code").equals(dept.get("parent_dept_code"));
						%>
						<option value="<%=department.get("dept_code")%>" <%=deptFlag?"selected":""%>>|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|--<%=department.get("dept_name")%><%=deptFlag?"[选]":""%></option>
						<%	
							}
						%>
					</select>
				</td>
			</tr>
			<tr id="childDept">
				<td  align="right" >子科室：</td>
				<td  align="left" >
					<input type="text" id="deptNameValue" data-field="field-dept-name"  style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;float:left;" name="a_child_dept_names" readonly value="<%=childDeptString%>"/>
					<label class="select" style="float:left" >
						<a id="popover-dept-name" data-field="field-dept-name" data-field-code='a_child_dept_codes' text="field-dept-code" class="popover-toggle" href="#">选择</a></label>
            		</label>
            		<input name="a_child_dept_codes" data-field="a_child_dept_codes" type="hidden" value="<%=childDeptCode%>" />
				</td>
			</tr>
			<tr>
				<td align="right">
				当前父科室：
				</td>
				<td align="left">
				<% if(dept.get("parent_dept_code").equals(dept.get("dept_code"))){%>
                         	无
				<% }else{ %>
                          <%=dept.get("parent_dept_name")%>
				<% } %>
				</td>
			</tr>
			<tr>
				<td align="right"  style="height: 130px;">说明：</td>
				<td>
					1.一个科室只能具备一个父科室。即，当一个科室已经有父科室的情况下，则不能设置为该科室的子科室。<br/>
					2.通过“科室关系”下拉列表中选择你需要维护的科室关系。<br/>
					3.子科室颜色说明：红色->该科室已经选择的子科室;绿色->绿色标识科室已经被选择为其他科室子科室;黑色->待选科室</br>
					4.科室分类：先按照科室属性分类，再按照门诊住院标识分类，最后按照内外科分类；</br>
					 &nbsp;&nbsp;科室属性：0-临床 1-辅诊 2-护理单元 3-机关 9-其他</br>
   					 &nbsp;&nbsp;门诊住院标识：0-门诊 1-住院 2-门诊住院 9其他</br>
   					 &nbsp;&nbsp;内外科标识：0-内科 1-外科 9-其他</br>
				</td>
			</tr>
		</tbody>
		<tfoot style="font-size:14px">
			<tr bgcolor="#EEEEEE">
				<td colspan="4" align="center">
					<a href="javascript:insert()">保存</a>&nbsp;|&nbsp;<a href="javascript:returnBack()">返回</a>
				</td>
			</tr>
		
		</tfoot>
 	</table>
 	</div>
</form>
</body>
</html>