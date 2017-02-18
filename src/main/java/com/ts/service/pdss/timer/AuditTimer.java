package com.ts.service.pdss.timer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.JdbcTemplateHander;
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
import com.hitzd.springBeanManager.SpringBeanUtil;
import com.ts.service.pdss.IHisAuditor;

/**
 * 事后审核定时程序
 * 定时器，每天0点从HIS系统中抽取前一天的所有医嘱信息，
 * 进行审核。
 * @author Administrator
 *
 */
@Service
@Transactional
public class AuditTimer implements Runnable
{
    private static final Log logger = LogFactory.getLog(JdbcTemplateHander.class);
    public AuditTimer() 
    {
    }
    
    private void LogIt(String s)
    {
        logger.info(s);
    }

    /**
     * 当前日期推前一天
     */
    private String getPrevDate()
    {
        Calendar c = Calendar.getInstance(); 
        c.add(Calendar.DATE, -1); 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        return sdf.format(c.getTime()); 
    }
    
    public boolean canRun()
    {
    	try
    	{
    		JDBCQueryImpl peaasQuery = DBQueryFactory.getQuery("PEAAS");
            @SuppressWarnings("unchecked")
    		List<TCommonRecord> list = peaasQuery.query("select rulevalue from ruleparameter where rulecode = 'AuditTimerEnabled'", new CommonMapper());
            peaasQuery = null;
            if ((list != null) && (list.size() > 0))
            {
        		TCommonRecord cr = list.get(0);
        		Calendar cal = Calendar.getInstance();
	            int hour = cal.get(Calendar.HOUR_OF_DAY);
        		System.out.println("事后审核AuditTimerEnabled启动时间为：" + cr.get("rulevalue"));
        		return hour == Integer.parseInt(cr.get("rulevalue"));
            }
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		return false;
    	}
    	return false;
    }
    
    /**
     * 取出病人的基本信息，结果唯一
     * @param hisQuery
     * @param PatientID
     * @return
     */
    public TPatient getPatientInfo(JDBCQueryImpl hisQuery, String PatientID)
    {
        String sql = "Select * from medrec.Pat_Master_Index where patient_id = '" + PatientID + "'";
        TPatient pat = (TPatient)hisQuery.queryForObject(sql, new RowMapper(){
            @Override
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
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

    /**
     * 取出病人的手术信息，结果不唯一，需要整合
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @param prevDate
     * @return
     */
    public TPatOperation getPatientOperationInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID, String prevDate)
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
    
    /**
     * 取出病人的诊断信息，结果不唯一
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    public TPatOrderDiagnosis[] getPatientDiagnosisInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
        String sql = "select * from medrec.diagnosis where patient_id = '" + PatientID + "' and Visit_ID = '" + VisitID + "'";
        @SuppressWarnings("unchecked")
        List<TPatOrderDiagnosis> list = (List<TPatOrderDiagnosis>)hisQuery.query(sql, new RowMapper(){
            @Override
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                TPatOrderDiagnosis pod = new TPatOrderDiagnosis();
                String Diagnosis_Desc = rs.getString("Diagnosis_Desc");
                if(!"".equals(Diagnosis_Desc) && null!=Diagnosis_Desc)
                {
                	pod.setDiagnosisDictID(Diagnosis_Desc);
                	pod.setDiagnosisName(Diagnosis_Desc);
                }
                System.out.println(pod.getDiagnosisDictID() + "-" + pod.getDiagnosisName());
                return pod;
            }
        });
        return list.toArray(new TPatOrderDiagnosis[0]);
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
    public TPatOrderDrug[] getPatientOrdersInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID, String prevDate)
    {
        String sql = "select * from ordadm.orders where patient_id = '" + PatientID + "' and visit_id = '" + VisitID + "' " + 
            " and order_class='" + Config.getParamValue("Drug_In_Order") + "' and " +
            " order_status <> '4' and " +                                                // 未作废的医嘱
            " (start_date_time >= to_date('2000-01-01', 'yyyy-mm-dd')) " +               // 过滤掉很久以前的医嘱
            " and " +
            " ( " + 
            "     (repeat_indicator = '0' and to_char(start_date_time, 'yyyy-mm-dd') = '" + prevDate + "') or " + // 临时医嘱，并且是当天的
            "     ( " +
            "       repeat_indicator = '1' and " +                                       // 长期医嘱
            "       ( " +
            "         (stop_date_time is null or stop_date_time = '') or " +             // 没有确定结束时间的，就是正在执行的
            "         (to_char(stop_date_time, 'yyyy-mm-dd') = '" + prevDate + "')" +    // 已经确定时间的，就是当天的
            "       ) " +
            "     ) " +
            "   )  ";
        List<TPatOrderDrug> list = (List<TPatOrderDrug>)hisQuery.query(sql, new RowMapper()
        {    
        	@Override
            public Object mapRow(ResultSet rs, int arg1) throws SQLException
            {
        		TPatOrderDrug pod = new TPatOrderDrug();
        		try
        		{
        			 DictCache x = DictCache.getNewInstance();
                     pod.setRecMainNo(rs.getString("Order_No"));
                     pod.setRecSubNo(rs.getString("Order_Sub_No"));
                     pod.setDrugName(rs.getString("Order_Text"));
                     pod.setDrugID(rs.getString("Order_Code"));
                     pod.setDosage(rs.getString("dosage"));
                     pod.setDoseUnits(rs.getString("dosage_Units"));
                     /* 用药途径  转换  */
                     String administration = rs.getString("administration");
                     if(!"".equals(administration)&&null != administration)
                     {
                     	pod.setAdministrationID(x.getAdminByName(administration).get("serial_no"));
                     }
                     pod.setStartDateTime(rs.getString("start_Date_Time"));
                     pod.setStopDateTime(rs.getString("stop_Date_Time"));
                     /* 频次 转换   如果频次为空 则设置为"日" */
                     
                     String frequency = rs.getString("frequency");
                     if(!"".equals(frequency)&&frequency!=null)
                     {
                    	 pod.setPerformFreqDictID(x.getPerformMapByName("".equals(frequency)?"日":frequency).get("serial_no"));
                    	 pod.setPerformFreqDictText(pod.getPerformFreqDictID());
                     }
                     
                     pod.setDoctorDept(rs.getString("Ordering_Dept"));
                     pod.setDoctorName(rs.getString("Doctor"));
                     System.out.println(pod.getDrugID() + "-" + pod.getDrugName() + "-" + pod.getRecMainNo() + pod.getRecSubNo() + "-" + pod.getDosage() + "-" +
                         pod.getDoseUnits() + "-" + pod.getAdministrationID() + "-" + pod.getPerformFreqDictID() + "-" + pod.getStartDateTime() + "-" + pod.getStopDateTime() + "-" + 
                         pod.getDoctorDept() + "-" + pod.getDoctorName()
                         );
        		}catch(Exception e)
        		{
        			e.printStackTrace();
        		}
               
                return pod;
        	}
        });
        return list.toArray(new TPatOrderDrug[0]);
    }
    
    /**
     * 取出病人的过敏信息，结果不唯一，不整合
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    public TPatOrderDrugSensitive[] getPatientSensitiveInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
        return new TPatOrderDrugSensitive[0];
    }
    
    /**
     * 取出病人的扩展信息，结果唯一，暂时娶不到，返回个空值
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    public TPatOrderInfoExt getPatientExtInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
        return new TPatOrderInfoExt();
    }
    
    /**
     * 取出病人的就诊信息，结果唯一
     * @param hisQuery
     * @param PatientID
     * @param VisitID
     * @return
     */
    public TPatOrderVisitInfo getPatientVisitInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID)
    {
        String sql = "Select * from medrec.Pat_Visit where patient_id = '" + PatientID + "' and Visit_ID = '" + VisitID + "'";
        TPatOrderVisitInfo pov = (TPatOrderVisitInfo)hisQuery.queryForObject(sql, new RowMapper(){

            @Override
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                TPatOrderVisitInfo p = new TPatOrderVisitInfo();
                String pid = rs.getString("Patient_ID");
                String vid = rs.getString("Visit_ID");
                p.setPatientID(pid);
                p.setVisitID(vid);
                p.setInDept(rs.getString("Dept_Admission_To"));
                p.setInDate(rs.getString("ADMISSION_DATE_TIME"));
                p.setOutDept(rs.getString("DEPT_DISCHARGE_FROM"));
                p.setInMode(rs.getString("PATIENT_CLASS"));
                p.setPatAdmCondition(rs.getString("PAT_ADM_CONDITION"));
                String doctor = rs.getString("ATTENDING_DOCTOR");
                if(doctor==null || "".equals(doctor))
                {
                	JDBCQueryImpl HIS = DBQueryFactory.getQuery("HIS");
                	List<TCommonRecord> list = HIS.query("select distinct doctor from ordadm.orders where patient_id='"+pid+"' and visit_id='"+vid+"'", new CommonMapper());
                	p.setMainDoctor((list.get(0)).get("doctor"));
                }
                else
                {
                	p.setMainDoctor(doctor);	
                }
                System.out.println(p.getPatientID() + "-" + p.getVisitID() + "-" + p.getInDept() +
                    "-" + p.getInMode() + "-" + p.getInDate() + "-" + p.getOutDept() + "-" + p.getPatAdmCondition()
                    );
                
                return p;
            }
        });
        return pov;
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
    public TPatSigns getPatientVitalSignInfo(JDBCQueryImpl hisQuery, String PatientID, String VisitID, String prevDate)
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
            ps.getXXDate() + "-" + ps.getCValue() + "-" + ps.getXXOK() + "-" + ps.getXXValue() 
            );
        return ps;
    }
    
    /**
     * 
     * @param hisQuery
     * @param prevDate
     * @return
     */
    public List<TCommonRecord> getPatIDs(JDBCQueryImpl hisQuery, String prevDate)
    {
        List<TCommonRecord> list = new ArrayList<TCommonRecord>();
        
        String sql = "select o.patient_id, o.visit_id from inpadm.pats_in_hospital o ";
        list = hisQuery.query(sql, new CommonMapper());
        
        /* 
        String sql = "select o.patient_id, o.visit_id from ordadm.orders o ,medrec.pat_visit t " + 
            " where o.patient_id=t.patient_id and o.visit_id=t.visit_id and t.discharge_date_time is null and t.catalog_date is null  and " +
            "   order_class='" + Config.getParamValue("Drug_In_Order") + "' and " +      // 药品医嘱
            "   order_status <> '4' and " +                                              // 未作废的医嘱
            "   (start_date_time >= to_date('2000-01-01', 'yyyy-mm-dd')) " +             // 过滤掉很久以前的医嘱
            "   and " +
            "   ( " +
            "     (repeat_indicator = '0' and to_char(start_date_time, 'yyyy-mm-dd') = '" + prevDate + "') or " + // 临时医嘱，并且是当天的
            "     ( " +
            "       repeat_indicator = '1' and " +                                       // 长期医嘱
            "       ( " +
            "         (stop_date_time is null or stop_date_time = '') or " +             // 没有确定结束时间的，就是正在执行的
            "         (to_char(stop_date_time, 'yyyy-mm-dd') = '" + prevDate + "')" +    // 已经确定时间的，就是当天的
            "       ) " +
            "     ) " + 
            "   )" +
            " group by o.patient_id, o.visit_id ";
        @SuppressWarnings("unchecked")
        List<TCommonRecord> list = (List<TCommonRecord>)hisQuery.query(sql, new CommonMapper());
        System.out.println("取到医嘱数量" + list.size());
        */
        return list;
    }
    /**
     * 按日期审核
     * @param hisQuery
     * @param prevDate
     */
    public List<TPatientOrder> AuditOrdersByDate(JDBCQueryImpl hisQuery, String prevDate)
    {
        List<TCommonRecord> list   = getPatIDs(hisQuery, prevDate);
        List<TPatientOrder> listpo = new ArrayList<TPatientOrder>();
        int i = 1 ;
        for (TCommonRecord cr : list)
        {
            String PatientID = cr.get("Patient_ID");
            String VisitID = cr.get("Visit_ID");
            System.out.println("PatientID: " + PatientID + "; VisitID: " + VisitID +"===============第" + i + "共"+list.size());
            TPatient patient = this.getPatientInfo(hisQuery, PatientID);
            if (patient != null)
            {
                TPatientOrder                   po = new TPatientOrder();
                /* 手术记录  */
                TPatOperation              patOper = this.getPatientOperationInfo(hisQuery, PatientID, VisitID, prevDate);
                po.setPatOperation(new TPatOperation[]{patOper});
                /* 诊断记录 */ 
                TPatOrderDiagnosis[]       patDiag = this.getPatientDiagnosisInfo(hisQuery, PatientID, VisitID);
                po.setPatOrderDiagnosiss(patDiag);
                /* 药物医嘱记录 */
                TPatOrderDrug[]            patDrug = this.getPatientOrdersInfo(hisQuery, PatientID, VisitID, prevDate);
                po.setPatOrderDrugs(patDrug);
                /* 过敏记录 */
                TPatOrderDrugSensitive[] patSensit = this.getPatientSensitiveInfo(hisQuery, PatientID, VisitID);
                po.setPatOrderDrugSensitives(patSensit);
                /* 病人就诊扩展信息  */
                TPatOrderInfoExt        patInfoExt = this.getPatientExtInfo(hisQuery, PatientID, VisitID);
                po.setPatInfoExt(patInfoExt);
                /* 病人住院信息 */
                TPatOrderVisitInfo        patVisit = this.getPatientVisitInfo(hisQuery, PatientID, VisitID);
                po.setPatVisitInfo(patVisit);
                /* 体征信息 */
                TPatSigns                 patSigns = this.getPatientVitalSignInfo(hisQuery, PatientID, VisitID, prevDate);
                po.setPatSigns(new TPatSigns[]{patSigns});
                /* 病人信息类：Patient 对应数据库表：病人信息  (PAT_MASTER_INFO) */
                po.setPatient(patient);
                po.setPatientID(PatientID);
                /* 科室 代码 */
                po.setDoctorDeptID(patVisit.getInDept());
                /* 科室名称 */
                po.setDoctorDeptName(DictCache.getNewInstance().getDeptName(hisQuery, patVisit.getInDept()));
                /* 医生代码 */
                po.setDoctorID(DictCache.getNewInstance().getDoctorCode(hisQuery, patVisit.getMainDoctor()));
                /* 医生名称 */
                po.setDoctorName(patVisit.getMainDoctor());
                listpo.add(po);
            }
            System.out.println("====================================================================================");
        }
        return listpo;
    }
    
    @Override
    public void run()
    {
        if (!canRun()) return;
        LogIt("准备审核医嘱...");
        JDBCQueryImpl hisQuery = DBQueryFactory.getQuery("HIS");
        LogIt("数据库连接准备完毕...");
        String prevDate = getPrevDate();
        LogIt("提取日期：" + prevDate);
        /* 提取住院处方 */ 
        LogIt("开始审核" + prevDate + "的医嘱记录...");
        try 
        {
            /* 返回当日所有医嘱信息 */
            List<TPatientOrder> pos = AuditOrdersByDate(hisQuery, prevDate);
            for(TPatientOrder po : pos)
            {
                /* 处理业务类 */
                IHisAuditor hisAuditor = (IHisAuditor) SpringBeanUtil.getBean("hisAuditor");
                /* 根据医嘱 做事后审查 */
                hisAuditor.DrugSecutityCheckAllPO(po);
            }
            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        hisQuery = null;
        /* 保存操作日志 */
        LogIt("审核结束...");
    }
}
