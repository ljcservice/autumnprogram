package com.ts.service;

import java.sql.ResultSet;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderDrugSensitive;
import com.hitzd.his.Beans.TPatOrderInfoExt;
import com.hitzd.his.Beans.TPatOrderVisitInfo;
import com.hitzd.his.Beans.TPatSigns;
import com.hitzd.his.Beans.TPatient;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.persistent.Persistent4DB;

/**
 * 获得 医嘱 信息 
 * @author Administrator
 *
 */
@Service
@Transactional
public class HisGetPatientOrdersBean extends Persistent4DB implements IHisGetPatientOrders 
{
	@Override
	public TPatientOrder getPatientOrder(String patient_Id, String visit_Id)
	{
		setQueryCode("HIS");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String  prevDate = format.format(new Date()) ;
		String PatientID = patient_Id;
		String   VisitID = visit_Id;
		System.out.println("PatientID: " + PatientID + "; VisitID: " + VisitID);
		TPatient patient = this.getPatientInfo(query, PatientID);
		if (patient != null)
		{
			TPatientOrder                   po = new TPatientOrder();
			/* 病人住院信息 */
			TPatOrderVisitInfo        patVisit = this.getPatientVisitInfo(query, PatientID, VisitID);
			po.setPatVisitInfo(patVisit);
			/* 如果住院号为空  则重新这设置    */
			if(VisitID == null || "".equals(VisitID))
			{
				VisitID = patVisit.getVisitID();
			}
			/* 手术记录  */
			TPatOperation              patOper = this.getPatientOperationInfo(query, PatientID, VisitID, prevDate);
			po.setPatOperation(new TPatOperation[]{patOper});
			/* 诊断记录 */
			TPatOrderDiagnosis[]       patDiag = this.getPatientDiagnosisInfo(query, PatientID, VisitID);
			po.setPatOrderDiagnosiss(patDiag);
			/* 药物医嘱记录 */
			TPatOrderDrug[]            patDrug = this.getPatientOrdersInfo(query, PatientID, VisitID, prevDate);
			po.setPatOrderDrugs(patDrug);
			/* 过敏记录 */
			TPatOrderDrugSensitive[] patSensit = this.getPatientSensitiveInfo(query, PatientID, VisitID);
			po.setPatOrderDrugSensitives(patSensit);
			/* 病人就诊扩展信息  */
			TPatOrderInfoExt        patInfoExt = this.getPatientExtInfo(query, PatientID, VisitID);
			po.setPatInfoExt(patInfoExt);
			/* 体征信息 */
			TPatSigns                 patSigns = this.getPatientVitalSignInfo(query, PatientID, VisitID, prevDate);
			po.setPatSigns(new TPatSigns[]{patSigns});
			/* 病人信息类：Patient 对应数据库表：病人信息  (PAT_MASTER_INFO) */
			po.setPatient(patient);
			po.setDoctorDeptName(patVisit.getInDept());
			po.setDoctorName(patVisit.getMainDoctor());
			this.setPoBaseInfo(po);
			return po;
		}
		return null;
	}
	
	/**
	 * 查询 经诊医生 和 科室 
	 * @param po
	 */
	@SuppressWarnings ("unchecked")
    private void setPoBaseInfo(TPatientOrder po)
	{
	    try
	    {
	        String pat = po.getPatVisitInfo().getPatientID();
	        String vst = po.getPatVisitInfo().getVisitID();
	        String sql = " select dept_code ,doctor_in_charge from inpadm.pats_in_hospital where patient_id = ?  and visit_id = ? order by visit_id desc" ;
	        List<String[]> list = (List<String[]>)query.query(sql, new String[]{pat,vst}, new RowMapper()
    	        {
    	            @Override
    	            public Object mapRow(ResultSet rs, int arg1) throws SQLException
    	            {
    	                String[] s = new String[2];
    	                s[0] = rs.getString("dept_code");
    	                s[1] = rs.getString("doctor_in_charge");
    	                return s;
    	            }
    	        });
	        if( list != null && list.size() > 0)
	        {
	            DictCache  d = DictCache.getNewInstance();
	            String[] s = (String[])list.get(0);
	            po.setDoctorDeptID(s[0]);
	            po.setDoctorDeptName(d.getDeptName(s[0]));
	            po.setDoctorID(s[1]);
	            po.setDoctorName(s[1]);
	            po.setDoctorTitleID("");
	            po.setDoctorTitleName("");
	        }
	    }
	    catch(Exception e )
	    {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * 取出病人的体征信息，结果不唯一，取出体征最大值返回一个结果，应该是最后一次的体征信息
	 * @param hisQuery
	 * @param PatientID
	 * @param VisitID
	 * @param prevDate
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private TPatSigns getPatientVitalSignInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID, String prevDate)
    {
    	try
    	{
    		// 体温取prevDate当天的测量值，没有的话，认为体温正常，有的话正常体温范围为36-37.5，超出该范围认为体温不正常
            String sql = "select * from ordadm.vital_signs_rec where Patient_ID = '" + PatientID + "' and " +
                    "Visit_ID = '" + VisitID + "' and " +
                    "to_char(Time_Point, 'yyyy-mm-dd') = '" + prevDate + "'";
            List<TCommonRecord> list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
            TPatSigns ps = new TPatSigns();
            ps.setTWOK("1");
            if (list.size() > 0)
            {
                for (TCommonRecord cr: list)
                {
                    if (cr.get("Vital_Signs").equals(Config.getParamValue("TWCode")))
                    {
                        if ((cr.getDouble("Vital_Signs_Values") < 36.0) || (cr.getDouble("Vital_Signs_Values") > 37.5))
                        {
                            ps.setTWOK("0");
                            ps.setTWValue(cr.get("Vital_Signs_Values"));
                            ps.setTWDate(cr.get("Time_Point"));
                            break;
                        }
                    }
                }
            }
            /*
            sql = "select * from lab.lab_test_master m where m.patient_id = '" + PatientID + "' and m.visit_id = '" + VisitID + "'";
            list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
            String TestNo;
            if (list.size() > 0)
            {
                // 取最后一次检验，但是要判断检验类型是验血类的还是细菌类的，就是上面的sql要增加条件
                TCommonRecord cr = list.get(list.size() - 1);
                TestNo = cr.get("Test_No");
                sql = "select * from lab.lab_test_items where test_no = '" + TestNo + "'";
                list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
                
                sql = "select * from lab.lab_result where test_no = '" + TestNo + "'";
                list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
                for (TCommonRecord crx : list)
                {
                    // 血项值
                    if (crx.get("Report_Item_Code").equals(Config.getParamValue("XXCode")))
                    {
                        ps.setXXDate(crx.get("Result_Date_Time"));
                        ps.setXXValue(crx.get("Result"));
                        if (crx.get("abnormal_indicator").length() == 0 || crx.get("abnormal_indicator").equals("M"))
                            ps.setXXOK("1");
                        else
                            ps.setXXOK("0");
                    }
                    // C反应蛋白
                    if (crx.get("Report_Item_Code").equals(Config.getParamValue("CDBCode")))
                    {
                        ps.setCValue(crx.get("Result"));
                        if (crx.get("abnormal_indicator").length() == 0 || crx.get("abnormal_indicator").equals("M"))
                            ps.setXXOK("1");
                        else
                            ps.setXXOK("0");
                    }
                }
            }
            */
            // 用血项代码和C反应蛋白代码反查
            sql = "select m.*, r.* from lab.lab_test_master m, lab.lab_result r " +
                " where m.patient_id = '" + PatientID + "' and " +
                " m.visit_id = '" + VisitID + "' and " +
                " m.test_no = r.test_no and " +
                " r.report_item_code = '" + Config.getParamValue("XXCode") + "' " +
                " order by r.result_date_time desc";
            list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
            ps.setXXOK("1");
            if (list.size() > 0)
            {
                TCommonRecord crx = list.get(0);
                ps.setXXDate(crx.get("Result_Date_Time"));
                ps.setXXValue(crx.get("Result"));
                if (crx.get("abnormal_indicator").length() == 0 || crx.get("abnormal_indicator").equals("M"))
                    ps.setXXOK("1");
                else
                    ps.setXXOK("0");
            }
            sql = "select m.*, r.* from lab.lab_test_master m, lab.lab_result r " +
                " where m.patient_id = '" + PatientID + "' and " +
                " m.visit_id = '" + VisitID + "' and " +
                " m.test_no = r.test_no and " +
                " r.report_item_code = '" + Config.getParamValue("CDBCode") + "' " +
                " order by r.result_date_time desc";
            list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
            if (list.size() > 0)
            {
                TCommonRecord crx = list.get(0);
                ps.setCValue(crx.get("Result"));
                if (crx.get("abnormal_indicator").length() == 0 || crx.get("abnormal_indicator").equals("M"))
                    ps.setXXOK("1");
                else
                    ps.setXXOK("0");
            }
            System.out.println(ps.getTWDate() + "-" + ps.getTWOK() + "-" + ps.getTWValue() +
                ps.getXXDate() + "-" + ps.getCValue() + "-" + ps.getXXOK() + "-" + ps.getXXValue());
            return ps;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return new TPatSigns();
    	}
        
    }
    
	 /**
     * 取出病人的就诊信息，结果唯一
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    private TPatOrderVisitInfo getPatientVisitInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
    	try
    	{  
    		String sql = "Select Patient_ID,Visit_ID,Dept_Admission_To,ADMISSION_DATE_TIME,DEPT_DISCHARGE_FROM,DEPT_DISCHARGE_FROM,PATIENT_CLASS," +
    				"PAT_ADM_CONDITION,ATTENDING_DOCTOR,DOCTOR_IN_CHARGE,CONSULTING_DOCTOR from medrec.Pat_Visit where patient_id = '" + PatientID + "' ";
            if(!"".equals(VisitID))
            {
            	sql += " and Visit_ID = '" + VisitID + "'";
            }
            sql += " order by to_number(Visit_ID) desc "; 
            TPatOrderVisitInfo pov = (TPatOrderVisitInfo)hisQuery.queryForObject(sql, new RowMapper()
            {
                @Override
                public Object mapRow(ResultSet rs, int arg1) throws SQLException 
                {
                	DictCache dict = DictCache.getNewInstance();
                	JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
                    TPatOrderVisitInfo p = new TPatOrderVisitInfo();
                    p.setPatientID(rs.getString("Patient_ID"));
                    p.setVisitID(rs.getString("Visit_ID"));
                    p.setInDept(dict.getDeptName( rs.getString("Dept_Admission_To")));
                    p.setInDate(rs.getString("ADMISSION_DATE_TIME"));
                    String deptFrom = rs.getString("DEPT_DISCHARGE_FROM");
                    if(deptFrom == null || "".equals(deptFrom))
                    {
                        p.setOutDept("");
                    }
                    else
                    {
                        p.setOutDept(dict.getDeptName(deptFrom));    
                    }
                    p.setInMode(rs.getString("PATIENT_CLASS"));
                    p.setPatAdmCondition(rs.getString("PAT_ADM_CONDITION"));
                    p.setMainDoctor(rs.getString("ATTENDING_DOCTOR"));
                    p.setOtherDoctor(rs.getString("DOCTOR_IN_CHARGE"));
                    p.setConsultingDoctor(rs.getString("CONSULTING_DOCTOR"));
                    System.out.println(p.getPatientID() + "-" + p.getVisitID() + "-" + p.getInDept() +
                        "-" + p.getInMode() + "-" + p.getInDate() + "-" + p.getOutDept() + "-" + p.getPatAdmCondition());
                    query = null;
                    return p;
                }
            });
            return pov;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return new TPatOrderVisitInfo();
    	}
    }
    
    /**
     * 取出病人的扩展信息，结果唯一，暂时娶不到，返回个空值
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    private TPatOrderInfoExt getPatientExtInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
        return new TPatOrderInfoExt();
    }
    
	 /**
     * 取出病人的过敏信息，结果不唯一，不整合
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    @SuppressWarnings("unchecked")
	private TPatOrderDrugSensitive[] getPatientSensitiveInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
    	try
    	{
    		if(true)
    		{
    			return new TPatOrderDrugSensitive[0];
    		}
			// medrec.patient_medcond item_type 1 过敏 2 病生状态 
    	   	String sql = "select DRUG_NAME,DRUG_CODE from surgery.ALERGY_DRUGS where PATIENT_ID = '" + PatientID + "'";
        	List<TPatOrderDrugSensitive> list = (List<TPatOrderDrugSensitive>)hisQuery.query(sql, new RowMapper()
        	{
        		@Override
        		public Object mapRow(ResultSet rs, int arg1) throws SQLException 
        		{
        			TPatOrderDrugSensitive pat = new TPatOrderDrugSensitive();
        			pat.setPatOrderDrugSensitiveID(rs.getString("DRUG_NAME"));
        			pat.setDrugAllergenID(rs.getString("DRUG_CODE"));
        			return pat;
        		}
        	});
            return (TPatOrderDrugSensitive[])list.toArray(new TPatOrderDrugSensitive[0]);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 取出病人的医嘱信息，结果不唯一，不整合
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @param prevDate
     * @return
     */
    @SuppressWarnings ("unchecked")
    private TPatOrderDrug[] getPatientOrdersInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID, String prevDate)
    {
    	try
    	{
    		/* 组织医嘱信息  orders 中 选择长期医嘱  */
        	String sql = "select Order_No,Order_Sub_No,Order_Text,Order_Code,dosage,dosage_Units,administration,start_Date_Time,stop_Date_Time," +
        			"frequency,Ordering_Dept,Doctor from ordadm.orders where patient_id = '" + PatientID + "' and visit_id = '" + VisitID + "' " + 
                " and order_class='" + Config.getParamValue("Drug_In_Order") + "' and " +
                " order_status <> '4' " +                                                    // 未作废的医嘱
                " and " +
                " repeat_indicator = '1' and " +                                             // 长期医嘱
                " ( " +
                " 	(stop_date_time is null or stop_date_time = '') or " +                   // 没有确定结束时间的，就是正在执行的
                "   (to_char(stop_date_time, 'yyyy-mm-dd') >= '" + prevDate + "')" +         // 已经确定时间的，就是当天的
                "  ) ";
            List<TPatOrderDrug> list = (List<TPatOrderDrug>)hisQuery.query(sql, new RowMapper()
            {
                @Override
                public Object mapRow(ResultSet rs, int arg1) throws SQLException
                {
                    DictCache x = DictCache.getNewInstance();
                    TPatOrderDrug pod = new TPatOrderDrug();
                    pod.setRecMainNo(rs.getString("Order_No"));
                    pod.setRecSubNo(rs.getString("Order_Sub_No"));
                    pod.setDrugName(rs.getString("Order_Text"));
                    pod.setDrugID(rs.getString("Order_Code"));
                    pod.setDosage(rs.getString("dosage"));
                    pod.setDoseUnits(rs.getString("dosage_Units"));
                    /* 用药途径  转换  */
                    pod.setAdministrationID(x.getAdminByName(rs.getString("administration")).get("serial_no"));
                    pod.setStartDateTime(rs.getString("start_Date_Time"));
                    pod.setStopDateTime(rs.getString("stop_Date_Time"));
                    /* 频次 转换  */
                    pod.setPerformFreqDictID(x.getPerformMapByName(rs.getString("frequency")).get("serial_no"));
                    pod.setPerformFreqDictText(pod.getPerformFreqDictID());
                    pod.setDoctorDept(rs.getString("Ordering_Dept"));
                    pod.setDoctorName(rs.getString("Doctor"));
                    System.out.println(pod.getDrugID() + "-" + pod.getDrugName() + "-" + pod.getRecMainNo() + pod.getRecSubNo() + "-" + pod.getDosage() + "-" +
                        pod.getDoseUnits() + "-" + pod.getAdministrationID() + "-" + pod.getPerformFreqDictID() + "-" + pod.getStartDateTime() + "-" + pod.getStopDateTime() + "-" + 
                        pod.getDoctorDept() + "-" + pod.getDoctorName()
                        );
                    return pod;
                }
            });
            
            /* 从医生新开的医嘱      */
            sql = "select Order_No,Order_Sub_No,Order_Text,Order_Code,dosage,dosage_Units,administration,start_Date_Time,frequency," +
            		"Ordering_Dept,Doctor from ordadm.doctor_orders where " +
            		"  order_statues = '1' " +                                           //order_statues = 1 下达为执行 2 下达已执行
            		"  start_stop_indicator = '0'  " +                                   //-- 医嘱  0 新开的 1 停止 
            		" and order_class = '" + Config.getParamValue("Drug_In_Order") + "' " +
            		" and patient_id = '" + PatientID + "'" +
            	    " and visit_id = '" + VisitID + "' ";
            List<TPatOrderDrug> list1 = (List<TPatOrderDrug>) hisQuery.query(sql, new RowMapper()
            {
            	@Override
            	public Object mapRow(ResultSet rs, int arg1) throws SQLException 
            	{
            		   DictCache x = DictCache.getNewInstance();
                       TPatOrderDrug pod = new TPatOrderDrug();
                       pod.setRecMainNo(rs.getString("Order_No"));
                       pod.setRecSubNo(rs.getString("Order_Sub_No"));
                       pod.setDrugName(rs.getString("Order_Text"));
                       pod.setDrugID(rs.getString("Order_Code"));
                       pod.setDosage(rs.getString("dosage"));
                       pod.setDoseUnits(rs.getString("dosage_Units"));
                       /* 用药途径  转换  */
                       pod.setAdministrationID(x.getAdminByName(rs.getString("administration")).get("serial_no"));
                       String date_time = rs.getString("start_Date_Time");
                       pod.setStartDateTime(date_time);
                       /* 因为是临时医嘱所以 执行  */
                       pod.setStopDateTime("");
                       /* 频次 转换    临时医嘱 的频次 如果为空则设置为日 */
                       String frequency = rs.getString("frequency");
                       pod.setPerformFreqDictID(x.getPerformMapByName(frequency ==null || "".equals(frequency)?"日":frequency).get("serial_no"));
                       pod.setPerformFreqDictText(pod.getPerformFreqDictID());
                       pod.setDoctorDept(rs.getString("Ordering_Dept"));
                       pod.setDoctorName(rs.getString("Doctor"));
                       
                       System.out.println(pod.getDrugID() + "-" + pod.getDrugName() + "-" + pod.getRecMainNo() + pod.getRecSubNo() + "-" + pod.getDosage() + "-" +
                           pod.getDoseUnits() + "-" + pod.getAdministrationID() + "-" + pod.getPerformFreqDictID() + "-" + pod.getStartDateTime() + "-" + pod.getStopDateTime() + "-" + 
                           pod.getDoctorDept() + "-" + pod.getDoctorName());
            		return pod;
            	}
            });
            list.addAll(list1);
            return list.toArray(new TPatOrderDrug[0]);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    	
    }
    
	/**
     * 取出病人的基本信息，结果唯一
     * @param hisQuery
     * @param PatientID
     * @return
     */
    private TPatient getPatientInfo(JDBCQueryImpl hisQuery, String PatientID)
    {
    	try
    	{
    		String sql = "Select Name,Sex,Date_Of_Birth,Birth_Place,Nation from medrec.Pat_Master_Index where patient_id = '" + PatientID + "'";
            TPatient pat = (TPatient)hisQuery.queryForObject(sql, new RowMapper()
            {
                @Override
                public Object mapRow(ResultSet rs, int arg1) throws SQLException 
                {
                    TPatient p = new TPatient();
                    p.setName(rs.getString("Name"));
                    p.setSex(rs.getString("Sex"));
                    p.setDateOfBirth(rs.getString("Date_Of_Birth"));
                    p.setBirthPlace(rs.getString("Birth_Place"));
                    p.setNation(rs.getString("Nation"));
                    return p;
                }
            });
            if (pat != null)
                System.out.println(pat.getName() + "-" + pat.getSex() + "-" + pat.getDateOfBirth() + "-" + pat.getBirthPlace() + "-" + pat.getNation());
            return pat;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return new TPatient();
    	}
    }
    
    /**
     * 取出病人的手术信息，结果不唯一，需要整合
     * 
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @param prevDate
     * @return
     */
    private TPatOperation getPatientOperationInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID, String prevDate)
    {
        try
        {
        	//String sql = "select * from surgery.OPERATION_MASTER";
            //sql = "select * from surgery.OPERATION_Name";
            String sql = "select n.patient_id, n.visit_id,n.oper_id, n.operation, n.wound_grade, m.start_date_time, m.end_date_time  from surgery.OPERATION_Name n, surgery.operation_master m " +
                    " where n.patient_id = m.patient_id and n.visit_id = m.visit_id and n.oper_id = m.oper_id " +
                    " and m.patient_id = '" + PatientID + "' and m.visit_id = '" + VisitID + "'" +
                    " order by n.patient_id, n.visit_id, n.oper_id, n.operation_no";
            @SuppressWarnings("unchecked")
            List<TCommonRecord> list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
            TPatOperation po = new TPatOperation();
            String OperName = "";
            int OperLevel = 0;
            for (TCommonRecord cr: list)
            {
                po.setOperCode(cr.get("Oper_ID"));
                OperName += cr.get("Operation") + ",";
                String WoundGrade = cr.get("Wound_Grade");
                int ol = 0;
                if (WoundGrade.equals("Ⅰ")) ol = 1; 
                else if (WoundGrade.equals("Ⅱ")) ol = 2;
                else if (WoundGrade.equals("Ⅲ")) ol = 3;
                if (ol > OperLevel) OperLevel = ol;
                po.setOperStartTime(cr.get("Start_Date_Time"));
                po.setOperEndTime(cr.get("End_Date_Time"));
            }
            String lvl = "";
            if (OperLevel == 1) lvl = "Ⅰ"; 
            else if (OperLevel == 2) lvl = "Ⅱ";
            else if (OperLevel == 3) lvl = "Ⅲ";
            /*手术级别 */
            po.setOperLevel(lvl);
            po.setOperName(OperName);
            System.out.println(po.getOperName() + "-" + po.getOperLevel() + "-" + po.getOperCode() + "-" + po.getOperStartTime() + "-" + po.getOperEndTime());
            return po;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return null;
        }
    }
    
    /**
     * 取出病人的诊断信息，结果不唯一
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    private TPatOrderDiagnosis[] getPatientDiagnosisInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
    	try
    	{
			// diag_type   Diagnosis_type_dict  只能取得到门诊诊断  diagnosis_dict 
    		String sql = "select Diagnosis_Desc from medrec.diagnosis where patient_id = '" + PatientID + "' and Visit_ID = '" + VisitID + "'";
            @SuppressWarnings("unchecked")
            List<TPatOrderDiagnosis> list = (List<TPatOrderDiagnosis>)hisQuery.query(sql, new RowMapper()
            {
                @Override
                public Object mapRow(ResultSet rs, int arg1) throws SQLException 
                {
                    TPatOrderDiagnosis pod = new TPatOrderDiagnosis();
                    String diagnosisName = rs.getString("Diagnosis_Desc");
                    pod.setDiagnosisDictID(diagnosisName);
                    pod.setDiagnosisName(diagnosisName);
                    System.out.println(pod.getDiagnosisDictID() + "-" + pod.getDiagnosisName());
                    return pod;
                }
            });
            return list.toArray(new TPatOrderDiagnosis[0]);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
}