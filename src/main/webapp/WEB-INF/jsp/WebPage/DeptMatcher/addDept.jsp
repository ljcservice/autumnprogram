<%@page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@ page import="com.hitzd.DBUtils.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
<title>科室添加</title>

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
			history.go(-1);
		}
	}
	
	function insert(){
		var flag="true";
		$("[Required]").each(function(i){
			if($("[Required]").eq(i).val()==""){
				alert("请填写完整必要条件");
				flag="false";
				return false;
			}
		});
		
		if(flag=="true"){
			with(document.formPost){
				o.value="addDept";
				submit();
			}
		}
	}
	
	</script>
</head>

<body leftmargin="0" topmargin="0">
<form name="formPost" style="margin: 0 0 0 0" action="<%=request.getContextPath()%>/control/DeptMatcher" method="post">
	<input name="o" type="hidden" value="query" />
	<table  style="border-collapse:collapse;border:1px solid #ccc;" align="center"  cellpadding="4" width="80%"  cellspacing="1" bgcolor="#CBD8AC">
		<thead>
			<tr  bgcolor="#EEEEEE">
				<th colspan="2">添加科室</th>
			</tr>
		</thead>
		<tbody style="font-size:12px">
			<tr   height="22" >
				<td  align="right"  width="50%">科室名称：</td>
				<td  align="left"><input type="text" name="a_dept_name" Required value="" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;"/></td>
			</tr>
			<tr height="22" >
				<td  align="right"  >科室别名：</td>
				<td  align="left" ><input type="text" name="a_dept_alias" Required value="" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;"/></td>
			</tr>
			<tr>
				<td  align="right" >科室属性标识：</td>
				<td  align="left" >
					<select name="a_clinic_attr" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;">
						<option value="0" >临床</option>
						<option value="1" >辅诊</option>
						<option value="2" >护理单元</option>
						<option value="3" >机关</option>
						<option value="9" >其他</option>
					</select>
				</td>
			</tr>
			<tr>
				<td  align="right" >门诊住院标识：</td>
				<td  align="left" >
					<select name="a_outp_or_inp" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;">
						<option value="0" >门诊</option>
						<option value="1" >住院</option>
						<option value="2" >门诊住院</option>
						<option value="9" >其他</option>
					</select>
				</td>
			</tr>
			<tr>
				<td  align="right" >内外科标识：</td>
				<td  align="left" >
					<select name="a_internal_or_sergery" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;">
						<option value="0" >内科</option>
						<option value="1" >外科</option>
						<option value="9" >其他</option>
					</select>
				</td>
			</tr>
			<tr height="22" >
				<td  align="right"  >科室序号[数字]：</td>
				<td  align="left" ><input type="text" name="a_order_no" value="" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;"/></td>
			</tr>
			<tr height="22" >
				<td  align="right"  >科室输入码：</td>
				<td  align="left" ><input type="text" name="a_input_code" value="" style="margin-bottom:0px;padding-bottom:0px;width:200px;height:25px;"/></td>
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
</form>
</body>
</html>