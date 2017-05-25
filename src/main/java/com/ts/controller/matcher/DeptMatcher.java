package com.ts.controller.matcher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.Transaction.TransaCallback;
import com.hitzd.Transaction.TransactionTemp;
import com.hitzd.WebPage.PageView;
import com.hitzd.WebPage.Impl.BasePageBean;
import com.hitzd.his.Web.Utils.CommonUtils;
import com.hitzd.his.Web.base.PubServlet;
import com.ts.util.DeptUtils;
/**
 * 科室关系维护
 * @author dcdt
 *
 */
@Controller("DeptMatcher")
public class DeptMatcher extends PubServlet{
	@Override
	protected void excelPrint(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		super.excelPrint(request, response);
	}
	/**
	 * 查询自维护科室
	 */
	@Override
	protected List<TCommonRecord> query(HttpServletRequest request,
			HttpServletResponse response) {
		synchronized(this){
			this.AjaxFlag = false;
		}
		//查询条件
		String deptCode = CommonUtils.getRequestParameter(request, "dept_code", "");
		request.setAttribute("dept_code", deptCode);
		String deptName = CommonUtils.getRequestParameter(request, "dept_name", "");
		request.setAttribute("dept_name", deptName);
		String clinicAttr = CommonUtils.getRequestParameter(request, "clinic_attr", "");
		request.setAttribute("clinic_attr", clinicAttr);
 		String outpOrInp = CommonUtils.getRequestParameter(request, "outp_or_inp", "");
 		request.setAttribute("outp_or_inp", outpOrInp);
		String internalOrSergery = CommonUtils.getRequestParameter(request, "internal_or_sergery", "");
		request.setAttribute("internal_or_sergery", internalOrSergery);
		String inputCode = CommonUtils.getRequestParameter(request, "input_code", "").toUpperCase();
		request.setAttribute("input_code", inputCode);
		String parentDeptCode = CommonUtils.getRequestParameter(request, "parent_dept_code", "");
		request.setAttribute("parent_dept_code", parentDeptCode);
		String parentDeptName = CommonUtils.getRequestParameter(request, "parent_dept_name", "");
		request.setAttribute("parent_dept_name", parentDeptName);
		String page = CommonUtils.getRequestParameter(request, "page", "1");
		request.setAttribute("page", page);
		
		int iPage = 1;
        try{iPage = Integer.parseInt(page);
        }catch (Exception ex){iPage = 1;}
		
		request.setAttribute("dept_code", deptCode);
		request.setAttribute("dept_name", deptName);
		request.setAttribute("clinic_attr", clinicAttr);
		request.setAttribute("outp_or_inp", outpOrInp);
		request.setAttribute("internal_or_sergery", internalOrSergery);
		request.setAttribute("input_code", inputCode);
		
		//查询器
		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
		String sqlAll = "select * from peaas.dept_dict order by clinic_attr,outp_or_inp,internal_or_sergery,order_no";
		List<TCommonRecord> allDept = query.query(sqlAll, new CommonMapper());
		request.setAttribute("allDept", allDept);
		
		String sql = "";
		if(!"".equals(deptCode)){
			sql += " and dept_code like  '%" + deptCode + "%' ";
		}
		if(!"".equals(deptName)){
			sql += " and dept_name like '%" + deptName + "%' ";
		}
		//处理集合
		if(!"".equals(clinicAttr)){
			sql += " and CLINIC_ATTR in " + convert2Condition(clinicAttr,"reverseClinicAttr") + " " ;
		}
		if(!"".equals(outpOrInp)){
			sql += " and OUTP_OR_INP in " + convert2Condition(outpOrInp,"reversepOutOrInp") + " ";
		}
		if(!"".equals(internalOrSergery)){
			sql += " and INTERNAL_OR_SERGERY in " +convert2Condition(internalOrSergery,"reverseInternalOrSergery")+ " ";
		}
		if(!"".equals(inputCode)){
			sql += " and input_code like  '%" + inputCode + "%' ";
		}
		if(!"".equals(parentDeptName)){
			sql += " and parent_dept_name like  '%" + parentDeptName + "%' ";
		}
		if(!"".equals(parentDeptCode)){
			sql += " and parent_dept_code like  '%" + parentDeptCode + "%' ";
		}
		
		BasePageBean bpb = new BasePageBean();
        LinkedHashMap<String, String> Orders = new LinkedHashMap<String, String>();
        Orders.put("to_number(order_no)", "asc");
        PageView<TCommonRecord> pageView = bpb.getScrollData(12, iPage, "PEAAS", sql, Orders, "DEPT_DICT");
        request.setAttribute("pageView",pageView);
        this.forword = "/WebPage/DeptMatcher/MatcherMain.jsp";
		return null;
	}
	
	private String convert2Condition(String condition,String reverseMethod){
		if(condition == null || "".equals(condition.trim())){
			return "('')";
		}
		StringBuffer result = new StringBuffer();
		String[] cond = condition.split(",");
		result.append("(");
		for(String element: cond){
			if("reverseClinicAttr".equals(reverseMethod)){
				result.append("'" + DeptUtils.reverseClinicAttr(element) + "',");
			}else if("reversepOutOrInp".equals(reverseMethod)){
				result.append("'" + DeptUtils.reversepOutOrInp(element) + "',");
			}else if("reverseInternalOrSergery".equals(reverseMethod)){
				result.append("'" + DeptUtils.reverseInternalOrSergery(element) + "',");
			}else{
				result.append("'" + element + "',");
			}
		}
		result.setLength(result.length()-1);
		result.append(")");
		return result.toString();
	}

	@Override
	protected List<TCommonRecord> option(String o, HttpServletRequest request,
			HttpServletResponse response) {
		this.AjaxFlag = false;
		if("deleteIt".equals(o)){
			String deptCode = CommonUtils.getRequestParameter(request, "p_dept_code", "");
			if("".equals(deptCode)){return query(request,response);}
			String sql = "delete from peaas.dept_dict where dept_code='" + deptCode + "'";
			JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
			query.execute(sql);
			return query(request,response);
		}else if("matcherit".equals(o)){
			/*科室关系维护*/
			String deptCode = CommonUtils.getRequestParameter(request, "p_dept_code", "");
			if("".equals(deptCode.trim())){
				return query(request,response);
			}
			/*查询科室相关信息*/
			JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
			String sql = "select * From peaas.dept_dict where dept_code='" + deptCode + "'";
			TCommonRecord dept = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			
			sql = "select * from peaas.dept_dict order by clinic_attr,outp_or_inp,internal_or_sergery,order_no";
			List<TCommonRecord> allDept = query.query(sql, new CommonMapper());
			
			sql = "select * from peaas.dept_dict where parent_dept_code='"+deptCode+"' and dept_code!='"+ deptCode +"'";
			List<TCommonRecord> allChildDept = query.query(sql, new CommonMapper());
			
			request.setAttribute("matchingDept", dept);
			request.setAttribute("allDept", allDept);
			request.setAttribute("allChildDept", allChildDept);
			
			this.forword = "/WebPage/DeptMatcher/deptRelation.jsp";
			return null;
		}else if("addDeptPre".equals(o)){
			this.forword = "/WebPage/DeptMatcher/addDept.jsp";
			return null;
		}else if("modifyItPre".equals(o)){
			//查询
			String deptCode = CommonUtils.getRequestParameter(request, "p_dept_code", "");
			if("".equals(deptCode)){
				return query(request,response);
			}
			String sql = "select * from peaas.dept_dict where dept_code='" + deptCode + "'";
			JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
			TCommonRecord dept = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
			
			request.setAttribute("modifyDept", dept);
			this.forword = "/WebPage/DeptMatcher/modifyDept.jsp";
			return null;
		}else if("updateDept".equals(o)){
			String deptCode = CommonUtils.getRequestParameter(request, "a_dept_code", "");
			String deptName = CommonUtils.getRequestParameter(request, "a_dept_name", "");
			String deptAlias = CommonUtils.getRequestParameter(request, "a_dept_alias", "");
			String clinicAttr = CommonUtils.getRequestParameter(request, "a_clinic_attr", "");
			String outpOrInp = CommonUtils.getRequestParameter(request, "a_outp_or_inp", "");
			String internalOrSergery = CommonUtils.getRequestParameter(request, "a_internal_or_sergery", "");
			String inputCode = CommonUtils.getRequestParameter(request, "a_input_code", "");
			String orderNo = CommonUtils.getRequestParameter(request, "a_order_no", "");
			TCommonRecord params = new TCommonRecord();
			params.set("dept_code", deptCode);
			params.set("dept_name", deptName);
			params.set("dept_alias", deptAlias);
			params.set("clinic_attr", clinicAttr);
			params.set("outp_or_inp", outpOrInp);
			params.set("internal_or_sergery", internalOrSergery);
			params.set("input_code", inputCode);
			params.set("order_no", orderNo);
			updateDept(params);
			
		}else if("addDept".equals(o)){
			//增加科室
			String deptName = CommonUtils.getRequestParameter(request, "a_dept_name", "");
			String deptAlias = CommonUtils.getRequestParameter(request, "a_dept_alias", "");
			String clinicAttr = CommonUtils.getRequestParameter(request, "a_clinic_attr", "");
			String outpOrInp = CommonUtils.getRequestParameter(request, "a_outp_or_inp", "");
			String internalOrSergery = CommonUtils.getRequestParameter(request, "a_internal_or_sergery", "");
			String inputCode = CommonUtils.getRequestParameter(request, "a_input_code", "");
			String orderNo = CommonUtils.getRequestParameter(request, "a_order_no", "");
			//处理orderNo，默认情况下为0
			if("".equals(orderNo.trim()))orderNo = "0";
			String deptCode = "VIR_" + UUID.randomUUID();
			TCommonRecord params = new TCommonRecord();
			params.set("order_no", orderNo);
			params.set("dept_code", deptCode);
			params.set("dept_name", deptName);
			params.set("dept_alias", deptAlias);
			params.set("clinic_attr", clinicAttr);
			params.set("outp_or_inp", outpOrInp);
			params.set("internal_or_sergery", internalOrSergery);
			params.set("input_code", inputCode);
			addDept(params);
		}else if("saveMatch".equals(o)){
			//
			synchronized(this){
				String deptRelation = CommonUtils.getRequestParameter(request, "a_dept_relation", "").trim();
				String deptCode = CommonUtils.getRequestParameter(request, "a_dept_code", "").trim();
				String deptName = CommonUtils.getRequestParameter(request, "a_dept_name", "").trim();
				String parentDeptName = CommonUtils.getRequestParameter(request, "a_parent_dept_name", "").trim();
				String parentDeptCode = CommonUtils.getRequestParameter(request, "a_parent_dept_code", "").trim();
				String childDeptCodes = CommonUtils.getRequestParameter(request, "a_child_dept_codes", "").trim();
				String childDeptNames = CommonUtils.getRequestParameter(request, "a_child_dept_names", "").trim();
				if("".equals(deptRelation) || "".equals(deptCode) || "".equals(deptName)){
					return query(request,response);
				}
				TCommonRecord params = new TCommonRecord();
				params.set("dept_code", deptCode);
				params.set("dept_name", deptName);
				params.set("parent_dept_name", parentDeptName);
				params.set("child_dept_names", childDeptNames);
				params.set("parent_dept_code", parentDeptCode);
				params.set("child_dept_codes", childDeptCodes);
				
				if("1".equals(deptRelation)){
					saveParentMatcher(params);
				}else if("2".equals(deptRelation)){
					saveChildMatcher(params);
					this.AjaxFlag = true;
				}
			}
			return null;
		}
		return query(request, response);
	}
	public void saveParentMatcher(TCommonRecord params){
		if(params == null){
			throw new IllegalArgumentException("argument error");
		}
		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
		String sql = "";
		if(!"".equals(params.get("parent_dept_code").trim())){
			TCommonRecord parent = queryByDeptCode(params.get("parent_dept_code"));
			if("".equals(parent.get("dept_name")) || "".equals(parent.get("dept_code"))){
				return ;
			}
			sql = "update peaas.dept_dict set parent_dept_name='"+parent.get("dept_name")+"',parent_dept_code='"+parent.get("dept_code")+"' where dept_code='"+params.get("dept_code")+"'";	
		}else{
			sql = "update peaas.dept_dict set parent_dept_name=dept_name,parent_dept_code=dept_code where dept_code='"+params.get("dept_code")+"'";
		}
		query.execute(sql);
	}
	public void saveChildMatcher(TCommonRecord params){
		if(params == null){
			throw new IllegalArgumentException("argument error");
		}
		if("".equals(params.get("dept_code"))){
			return;
		}
		
		//delete
		TransactionTemp transaction = new TransactionTemp("PEAAS");
		Object object = transaction.execute(new TransaCallback(params){
			@Override
			public void ExceuteSqlRecord() {
				TCommonRecord params = getTranParm();
				String[] deptCodes = params.get("child_dept_codes").split(",");
				String[] sqls = new String[deptCodes.length];
				JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
				String deleteSql = "update peaas.dept_dict set parent_dept_name=dept_name, parent_dept_code=dept_code where parent_dept_code='"+params.get("dept_code")+"'";
				query.execute(deleteSql);
				for(int i=0; i<deptCodes.length; i++){
					String deptCode = deptCodes[i].trim();
					String sql = "update peaas.dept_dict set parent_dept_name='"+params.get("dept_name")+"',parent_dept_code='"+params.get("dept_code")+"' where dept_code='"+deptCode+"' and dept_code = parent_dept_code";
					sqls[i] = sql;
				}
				query.batchUpdate(sqls);
			}
		});
	}
	
	private TCommonRecord queryByDeptCode(String deptCode){
		if(deptCode == null || "".equals(deptCode)){
			throw new IllegalArgumentException("argument error");
		}
		/*查询科室相关信息*/
		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
		String sql = "select * From peaas.dept_dict where dept_code='" + deptCode + "'";
		TCommonRecord dept = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
		return dept;
	}
	private TCommonRecord queryByDeptName(String deptName){
		if(deptName == null || "".equals(deptName)){
			throw new IllegalArgumentException("argument error");
		}
		/*查询科室相关信息*/
		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
		String sql = "select * From peaas.dept_dict where dept_name='" + deptName.trim() + "'";
		TCommonRecord dept = (TCommonRecord)query.queryForObject(sql, new CommonMapper());
		return dept;
	}
	public void updateDept(TCommonRecord params){
		//delete
		TransactionTemp transaction = new TransactionTemp("PEAAS");
		/*Object object =*/ transaction.execute(new TransaCallback(params){
			@Override
			public void ExceuteSqlRecord() {
				TCommonRecord params = getTranParm();
				JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
				String sql = "UPDATE PEAAS.DEPT_DICT " +
						" SET ORDER_NO = '"+params.get("order_no")+"', " +
						" DEPT_NAME = '"+params.get("dept_name")+"', " +
						" DEPT_ALIAS = '"+params.get("dept_alias")+"'," +
						" CLINIC_ATTR = '"+params.get("clinic_attr")+"', " +
						" OUTP_OR_INP = '"+params.get("outp_or_inp")+"'," +
						" INTERNAL_OR_SERGERY = '"+params.get("internal_or_sergery")+"'," +
						" INPUT_CODE = '"+params.get("input_code")+"' " +
						" WHERE " +
						"DEPT_CODE='"+params.get("dept_code")+"'";
				
				String updateOther = "UPDATE PEAAS.DEPT_DICT SET  " +
						" PARENT_DEPT_NAME='"+ params.get("dept_name") +"' " +
						" WHERE " +
						" PARENT_DEPT_CODE='"+params.get("dept_code")+"'";
				
				query.execute(updateOther);
				query.execute(sql);
			}
		});
	}
	public List<TCommonRecord> addDept(TCommonRecord params){
		
		JDBCQueryImpl query = DBQueryFactory.getQuery("PEAAS");
        String sql = "INSERT INTO " +
        		"  PEAAS.DEPT_DICT ( " +
        		" ORDER_NO,DEPT_CODE,DEPT_NAME,DEPT_ALIAS, " +
        		" CLINIC_ATTR,OUTP_OR_INP,INTERNAL_OR_SERGERY,INPUT_CODE, " +
        		" PARENT_DEPT_CODE,PARENT_DEPT_NAME   " +
        		")VALUES( " +
        		" '"+params.get("order_no")+"','"+params.get("dept_code")+"','"+params.get("dept_name")+"','"+params.get("dept_alias")+"'," +
        		" '"+params.get("CLINIC_ATTR")+"','"+params.get("OUTP_OR_INP")+"','"+params.get("INTERNAL_OR_SERGERY")+"','"+params.get("INPUT_CODE")+"'," +
        		" '"+params.get("DEPT_CODE")+"','"+params.get("DEPT_NAME")+"'" +
        		")";
        query.execute(sql);
		return null;
	}
}
