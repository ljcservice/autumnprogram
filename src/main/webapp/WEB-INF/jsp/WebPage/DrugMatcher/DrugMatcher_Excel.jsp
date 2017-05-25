<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://tag.autumnTag.org/xls-taglib" prefix="xls"%>
<%@ include file="/WebPage/reportView/CommonExcel.jsp" %>
<%@ page import="com.hitzd.DBUtils.*" %>
<%@ page import="com.hitzd.Factory.DBQueryFactory"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hitzd.his.Web.Utils.CommonUtils"%>
<%@ page import="com.hitzd.his.Utils.*"%>
<%
    List<Object> objs = (List<Object>)request.getAttribute("sessionObject");
    List<TCommonRecord> list = (List<TCommonRecord>)objs.get(0);

%>
<xls:book name="DrugMatcher.xls" >
	<xls:sheet name="药品信息">
		<%
			if(list != null && list.size() > 0)
			{
				int serial = 0;
                String is_part = ""; //部分
                String is_hormone = ""; //激素类
                String is_external = "";
                String is_cardiovascular = "";//心脑血管类
                String is_digestive = "";//消化系统类
                String is_biological = "";//生物制药类
                String is_danger = "";//高危类
                String is_anti = "";//抗菌类
                String is_psychotic = ""; //精神类
                String anti_level = "";
				for (TCommonRecord entity : list)
				{
					serial++;
                    is_part = "";
                    is_anti = entity.get("is_anti");
                    is_external = entity.get("is_external");
                    is_hormone = entity.get("is_hormone");
                    is_cardiovascular = entity.get("is_cardiovascular");
                    is_digestive = entity.get("is_digestive");
                    is_biological = entity.get("is_biological");
                    is_danger = entity.get("is_danger");
                    is_psychotic = entity.get("is_psychotic");
                    anti_level = entity.get("anti_level");

                    //抗菌药作用范围
                    if("1".equals(is_anti)){
                        if("1".equals(is_external)){
                            is_part = "局部";
                        }else if("0".equals(is_external)){
                            is_part = "全身";
                        }else{
                            is_part = "未设置";
                        }
                    }else{
                        is_part = "非抗菌药";
                    }

                    //抗菌药等级
                    if("1".equals(is_anti)){
                        if("3".equals(anti_level)){
                            anti_level = "特殊级";
                        }else if("2".equals(anti_level)){
                            anti_level = "限制级";
                        }else if("1".equals(anti_level)){
                            anti_level = "非限制级";
                        }else{
                            anti_level = "未设置";
                        }
                    }else{
                        anti_level = "非抗菌药";
                    }
                    //抗菌药
                    if("1".equals(is_anti)){
                        is_anti = "是";
                    }else if("0".equals(is_anti)){
                        is_anti = "否";
                    }else{
                        is_anti = "未设置";
                    }
                    //激素类
                    if("1".equals(is_hormone)){
                        is_hormone = "是";
                    }else if("0".equals(is_hormone)){
                        is_hormone = "否";
                    }else{
                        is_hormone = "未设置";
                    }

                    //心脑血管类
                    if("1".equals(is_cardiovascular)){
                        is_cardiovascular = "是";
                    }else if("0".equals(is_cardiovascular)){
                        is_cardiovascular = "否";
                    }else{
                        is_cardiovascular = "未设置";
                    }

                    //生物制药类
                    if("1".equals(is_biological)){
                        is_biological = "是";
                    }else if("0".equals(is_biological)){
                        is_biological = "否";
                    }else{
                        is_biological = "未设置";
                    }

                    //消化系统类
                    if("1".equals(is_digestive)){
                        is_digestive = "是";
                    }else if("0".equals(is_digestive)){
                        is_digestive = "否";
                    }else{
                        is_digestive = "未设置";
                    }

                    //精神类
                    if("2".equals(is_psychotic)){
                        is_psychotic = "II类";
                    }else if("1".equals(is_psychotic)){
                        is_psychotic = "I类";
                    }else if("0".equals(is_psychotic)){
                        is_psychotic = "否";
                    }else{
                        is_psychotic = "未设置";
                    }

                    //高危类
                    if("3".equals(is_danger)){
                        is_danger = "C级";
                    }else if("2".equals(is_danger)){
                        is_danger = "B级";
                    }else if("1".equals(is_danger)){
                        is_danger = "A级";
                    }else if("0".equals(is_danger)){
                        is_danger = "否";
                    }else{
                        is_danger = "未设置";
                    }

                    //抗菌药等级

		%>
		<xls:row start="2">
			<xls:cell cell="A" value='<%=serial %>'></xls:cell>	
			<xls:cell cell="B" value='<%=entity.get("DRUG_NO_LOCAL")%>'></xls:cell>	
			<xls:cell cell="C" value='<%=entity.get("DRUG_NAME_LOCAL")%>'></xls:cell>
			<xls:cell cell="D" value='<%=entity.get("DRUG_SPEC")%>'></xls:cell>	
			<xls:cell cell="E" value='<%=entity.get("DRUG_FORM")%>'></xls:cell>
            <xls:cell cell="F" value='<%=is_anti%>'></xls:cell>
            <xls:cell cell="G" value='<%=is_part%>'></xls:cell>
            <xls:cell cell="H" value='<%=anti_level%>'></xls:cell>
            <xls:cell cell="I" value='<%=is_hormone%>'></xls:cell>
            <xls:cell cell="J" value='<%=is_cardiovascular%>'></xls:cell>
            <xls:cell cell="K" value='<%=is_biological%>'></xls:cell>
            <xls:cell cell="L" value='<%=is_digestive%>'></xls:cell>
            <xls:cell cell="M" value='<%=is_psychotic%>'></xls:cell>
            <xls:cell cell="N" value='<%=is_danger%>'></xls:cell>
		</xls:row>
		<%	
				}
			}
		%>
	</xls:sheet>
</xls:book>