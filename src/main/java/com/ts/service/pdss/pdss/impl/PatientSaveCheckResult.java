package com.ts.service.pdss.pdss.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderDrugSensitive;
import com.hitzd.his.Beans.TPatSigns;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Beans.TPreveUseDrug;
import com.hitzd.his.Beans.TTreatUseDrug;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.persistent.Persistent4DB;
import com.ts.dao.DaoSupportPdss;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityCheckResult;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;
import com.ts.entity.pdss.pdss.Beans.TAllergIngrDrug;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugIvEffect;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.entity.pdss.pdss.RSBeans.TAdministrationRslt;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResult;
import com.ts.entity.pdss.pdss.RSBeans.TDrugAllergenRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDiagRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugDosageRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugHarmfulRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugIngredientRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugInteractionRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugIvEffectRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSpecPeopleRslt;
import com.ts.service.pdss.pdss.manager.IPatientSaveCheckResult;
import com.ts.util.DateUtil;
import com.ts.util.PageData;

@Service
@Transactional
/**
 * 保存审查信息
 */
public class PatientSaveCheckResult extends Persistent4DB implements IPatientSaveCheckResult
{
	@Resource(name = "daoSupportPdss")
	private DaoSupportPdss dao;
	
    /* 每次批次号 */
    private String ngroupnum = "";
    String CheckTime  = "";
    
    public PatientSaveCheckResult()
    {
        
    }
    
    private void setNgroupnum()
    {
        this.ngroupnum = UUID.randomUUID().toString();
        CheckTime  = DateUtils.formatDate(DateUtils.FORMAT_DATETIME);
    }
    
    public void setNgroupnum(String value)
    {
        if ((value == null) || (value.length() == 0))
            this.ngroupnum = UUID.randomUUID().toString();
        else
            ngroupnum = value;
        CheckTime  = DateUtils.formatDate(DateUtils.FORMAT_DATETIME);
    }
    
    private final static Logger log = Logger.getLogger(PatientSaveCheckResult.class);
    
    @SuppressWarnings("unchecked")
    @Override
    public void savePatientCheckInfo(TPatientOrder po,TDrugSecurityRslt dsr)
    {
        try
        {
//            setQueryCode("HisSysManager");
            StringBuffer patient = new StringBuffer();
            /* 病人信息 
             *   姓名 性别 出生日期 出生地 民族 病人ID 检查组序号
             * */
            List<TCommonRecord> cr = null;//query.query("select count(*)c  from check_patient where patient_id='" + po.getPatientID() + "'", new CommonMapper());
            PageData pd =  (PageData) dao.findForObject("InfoMapper.getCheckPatient", po.getPatientID());
            
            String patient_id =  po.getPatientID();
            if(pd == null || pd.getInt("c") == 0)
            {
            	PageData param = new PageData();
            	param.put("NAME", po.getPatient().getName());
            	param.put("SEX", po.getPatient().getSex());
            	param.put("DATE_OF_BIRTH", DateUtil.fomatDate(po.getPatient().getDateOfBirth().length() > 10 ? po.getPatient().getDateOfBirth().substring(0,10):po.getPatient().getDateOfBirth()));
            	param.put("BIRTH_PLACE", po.getPatient().getBirthPlace().length()> 10 ? po.getPatient().getBirthPlace().substring(0,9):po.getPatient().getBirthPlace() );
            	param.put("NATION", po.getPatient().getNation());
            	param.put("PATIENT_ID", patient_id);
            	param.put("NGROUPNUM", this.ngroupnum);
            	param.put("checkDate", CheckTime);
            	dao.save("InfoMapper.insertCheckPatient", param);
                /* 病人信息 CHECK_PATIENT */
                //query.update(patient.toString());
            }else {
            	PageData pp = new PageData();
            	pp.put("CheckTime", CheckTime);
            	pp.put("ngroupnum", this.ngroupnum);
            	pp.put("patient_id", patient_id);
            	dao.save("InfoMapper.updateCheckPatient", pp);
                //query.execute(" update check_patient set checkDate='" + CheckTime + "',NGROUPNUM='" + this.ngroupnum + "' where patient_id='" + patient_id + "'");
            }
            patient = new StringBuffer(); 
            String visit_id =  po.getPatVisitInfo().getVisitID();
            //cr = query.query("select count(*) c  from CHECK_PAT_ORDER_VISITINFO where patient_id='" + po.getPatientID() + "' and visit_id='" + visit_id + "'", new CommonMapper());
            PageData pm = new PageData();
            pm.put("patient_id", patient_id);
            pm.put("visit_id", visit_id);
            pm.put("patient_id", patient_id);
            PageData pw =  (PageData) dao.findForObject("InfoMapper.getCheckPtaOrderVisi", pm);
            
            /* 审查病人住院信息表  */
            if(pw == null || pw.getInt("c") == 0)
            {
            	pw.put("VISIT_ID", po.getPatVisitInfo().getVisitID());
            	pw.put("IN_DEPT",po.getPatVisitInfo().getInDept());
            	pw.put("IN_MODE",po.getPatVisitInfo().getInMode());
            	pw.put("PAT_ADM_CONDITION",po.getPatVisitInfo().getPatAdmCondition());
            	pw.put("OUT_DEPT",po.getPatVisitInfo().getOutDept());
            	pw.put("NGROUPNUM",this.ngroupnum);
            	pw.put("checkDate",CheckTime);
            	pw.put("patient_id",patient_id);
                if(!"".equals(po.getPatVisitInfo().getInDate())&&po.getPatVisitInfo().getInDate()!= null)
                {
                	pw.put("IN_DATE",DateUtil.fomatDate2( po.getPatVisitInfo().getInDate().length()>19?po.getPatVisitInfo().getInDate().substring(0, 19):po.getPatVisitInfo().getInDate()));
                }
                dao.save("InfoMapper.insertCheckPtaOrderVisi", pw);
                /* 审查病人住院信息表 CHECK_PAT_ORDER_VISITINFO */
                //query.update(patient.toString());
                patient = new StringBuffer();
                /* 审查病人扩展信息表 */
                patient.append("insert into check_pat_order_info_ext(is_lact,is_pregnant,insureance_type,insurance_no,is_liverwhole,is_kidneywhole,height,weight,ngroupnum,checkdate,patient_id,visit_id)")
                        .append(" values(")
                        .append("'").append(po.getPatInfoExt().getIsLact()).append("'")
                        .append(",'").append(po.getPatInfoExt().getIsPregnant()).append("'")
                        .append(",'").append(po.getPatInfoExt().getInsureanceType()).append("'")
                        .append(",'").append(po.getPatInfoExt().getInsuranceNo()).append("'")
                        .append(",'").append(po.getPatInfoExt().getIsLiverWhole()).append("'")
                        .append(",'").append(po.getPatInfoExt().getIsKidneyWhole()).append("'")
                        .append(",'").append(po.getPatInfoExt().getHeight()).append("'")
                        .append(",'").append(po.getPatInfoExt().getWeight()).append("'")
                        .append(",'").append(this.ngroupnum).append("'")
                        .append(",'").append(CheckTime).append("'")
                        .append(",'").append(patient_id).append("'")
                        .append(",").append(visit_id)
                        .append(")");
                /* 审查病人扩展信息表  CHECK_PAT_ORDER_INFO_EXT */
                query.update(patient.toString());
            }
            /*保存医生信息*/
            patient = new StringBuffer(); 
            patient.append("insert into CHECK_DOCTOR_INFO(NGROUPNUM,DOCTOR_NAME,DOCTOR_CODE,DEPT_NAME,DEPT_CODE,DOCTOR_TITLE, Doctor_Title_id,patient_id,visit_id)")
                    .append(" values(")
                    .append("'").append(this.ngroupnum).append("'")
                    .append(",'").append(po.getDoctorName()).append("'")
                    .append(",'").append(po.getDoctorID()).append("'")
                    .append(",'").append(po.getDoctorDeptName()).append("'")
                    .append(",'").append(po.getDoctorDeptID()).append("'")
                    .append(",'").append(po.getDoctorTitleName()).append("'")
                    .append(",'").append(po.getDoctorTitleID()).append("'")
                    .append(",'").append(patient_id).append("'")
                    .append(",").append(visit_id)
                    .append(")");
            query.update(patient.toString());
            
            query.update(" insert into CHECK_SERIAL_LIST (patient_id, visit_id, check_date, ngroupnum) values ("+
                    "'"+patient_id+"',"+
                    visit_id+","+
                    "to_date('" + CheckTime + "','yyyy-mm-dd hh24:mi:ss'),"+
                    "'" + this.ngroupnum + "'" + 
                    ")");
            
            //String[] sql = new String[po.getPatOrderDrugs().length];
            List<String> sqlList = new ArrayList<String>();
            for(int i = 0 ; i<po.getPatOrderDrugs().length ; i++)
            {
                TPatOrderDrug pod = po.getPatOrderDrugs()[i];
                patient = new StringBuffer();
                TDrug drug = new TDrug();
                drug.setDRUG_NO_LOCAL(pod.getDrugID());
                drug.setRecMainNo(pod.getRecMainNo());
                drug.setRecSubNo(pod.getRecSubNo());
                TCheckResult cres = dsr.getCheckResult(drug);
                if (cres != null)
                {
                    String checkResult = cres.getAlertLevel();
                    /* 病人用药记录 */
                    patient.append("insert into check_pat_order_drug(DOCTOR_DEPT,IS_GROUP,ADMINISTRATION_ID,PERFORM_FREQ_DICT_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO")
                            .append(",PERFORM_FREQ_DICT_NAME,DOSAGE,DOSE_UNITS,START_DATE_TIME,STOP_DATE_TIME,DOCTOR,NGROUPNUM,checkDate,USETYPE,USECAUSE,DRUG_NAME,check_Result )")
                            .append(" values (")
                            .append("'").append(pod.getDoctorDept()).append("'")
                            .append(",'").append("0").append("'")
                            .append(",'").append(pod.getAdministrationID()).append("'")
                            .append(",'").append(pod.getPerformFreqDictID()).append("'")
                            .append(",'").append(pod.getDrugID()).append("'")
                            .append(",'").append(pod.getRecMainNo()).append("'")
                            .append(",'").append(pod.getRecSubNo()).append("'")
                            .append(",'").append(pod.getPerformFreqDictText()).append("'")
                            .append(",'").append(pod.getDosage()).append("'")
                            .append(",'").append(pod.getDoseUnits()).append("'");
                    		String start = pod.getStartDateTime();
                    		String stop  = pod.getStartDateTime();
                            if(start != null && !"".equals(start))
                            {
                            	if(start.length()>19)
                            		start = start.substring(0, 19);
                            	patient.append(",to_date('").append(start).append("','yyyy-mm-dd hh24:mi:ss')");
                            }
                            else
                            {
                            	patient.append(",''");
                            }
                            if(stop != null && !"".equals(stop))
                            {
                            	if(stop.length()>19)
                            		stop = stop.substring(0, 19);
                            	patient.append(",to_date('").append(stop).append("','yyyy-mm-dd hh24:mi:ss')");
                            }
                            else
                            {
                            	patient.append(",''");
                            }
                            
                            patient.append(",'").append(pod.getDoctorName()).append("'")
                            .append(",'").append(this.ngroupnum).append("'")
                            .append(",'").append(CheckTime).append("'")
//                            .append(",'").append(pod.getUseType()).append("'")
//                            .append(",'").append(pod.getUseCause()).append("'")
                            .append(",''")
                            .append(",''")
                            .append(",'").append(pod.getDrugName()).append("'")
                            .append(",'").append(checkResult).append("'")
                            .append(" )");
                    query.update(patient.toString());
                    patient.setLength(0);
                    //sqlList.add(patient.toString());
                }
            }
            /* 病人用药记录  CHECK_PAT_ORDER_DRUG */
//            if(sqlList.size() > 0)
//            {
//              String[] sqls = sqlList.toArray(new String[0]);
//            }
//            String[] sql = new String[po.getPatOrderDiagnosiss().length];
            for(int i = 0 ; i < po.getPatOrderDiagnosiss().length ; i++)
            {
                TPatOrderDiagnosis pod = po.getPatOrderDiagnosiss()[i];
                patient = new StringBuffer();
                /*  审查医嘱诊断表 */
                patient.append("insert into check_pat_order_diagnosis(DIAGNOSIS_DICT_ID,NGROUPNUM,DIAGNOSIS_NAME,checkDate)")
                        .append(" values (")
                        .append("'").append(pod.getDiagnosisDictID()).append("'")
                        .append(",'").append(this.ngroupnum).append("'")
                        .append(",'").append(pod.getDiagnosisName()).append("'")
                        .append(",'").append(CheckTime).append("'")
                        .append(")");
                query.update(patient.toString());
                patient.setLength(0);
                //sql[i] = patient.toString();
            }
            /* 审查医嘱诊断表 CHECK_PAT_ORDER_DIAGNOSIS  */
//            if(sql.length > 0)
//              query.batchUpdate(sql);
//            sql = new String[po.getPatOrderDrugSensitives().length];
            for(int i = 0 ; i< po.getPatOrderDrugSensitives().length; i++)
            {
                TPatOrderDrugSensitive pods = po.getPatOrderDrugSensitives()[i];
                patient = new StringBuffer();
                /* 审查医嘱过敏表 */
                patient.append("insert into CHECK_PAT_ORDER_DRUG_SENSITIVE(DRUG_ALLERGEN_ID,NGROUPNUM,checkDate)")
                        .append(" values(")
                        .append("'").append(pods.getPatOrderDrugSensitiveID()).append("'")
                        .append(",'").append(this.ngroupnum).append("'")
                        .append(",'").append(CheckTime).append("'")
                        .append(")");
                query.update(patient.toString());
                patient.setLength(0);
//                sql[i] = patient.toString();
            }
            /* 审查医嘱过敏表 CHECK_PAT_ORDER_DRUG_SENSITIVE*/
//            if(sql.length > 0)
//              query.batchUpdate(sql);
            /* 体征信息保存  */
            if(po.getPatSigns().length > 0)
            {
                sqlList.clear();
                for(int i = 0 ; i < po.getPatSigns().length ; i++)
                {
                    TPatSigns ps = po.getPatSigns()[i];
                    patient = new StringBuffer();
                    if((ps.getTWOK() + ps.getTWValue() + ps.getXXOK() + ps.getCValue()).length() > 0)
                    {
                        patient.append("insert into check_pat_signs(ngroupnum,twdate, twvalue, twok, xxdate, xxvalue, cvalue, xxok) values (")
                                .append("'").append(this.ngroupnum).append("'")
                                .append(",'").append(ps.getTWDate()).append("'")
                                .append(",'").append(ps.getTWValue()).append("'")
                                .append(",'").append(ps.getTWOK()).append("'")
                                .append(",'").append(ps.getXXDate()).append("'")
                                .append(",'").append(ps.getXXValue()).append("'")
                                .append(",'").append(ps.getCValue()).append("'")
                                .append(",'").append(ps.getXXOK()).append("'")
                                .append(")");
                        query.update(patient.toString());
                        patient.setLength(0);
//                      sqlList.add(patient.toString());
                    }
                }
//              query.batchUpdate(sqlList.toArray(new String[0]));
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]-------保存审查的基本信息-------" + e.getMessage() + e.toString());
            
        }
    }

    @Override
    public void saveDrugInteractionCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
                return ;
            if(dsr.getCheckResults() == null)
                return ;
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugInteractionRslt[] dir = tcr.getDrugInteractionRslt();
                if(dir == null || dir.length == 0)
                {
                    continue;
                }
                for(int j = 0 ;j<dir.length;j++)
                {
                    TDrugInteractionInfo[] dia = dir[j].getDrugInteractionInfo();
                    for(TDrugInteractionInfo t : dia)
                    {
                        StringBuffer sql = new StringBuffer();
                        sql.append("insert into DRUG_INTERACTION_CHECKRSLT(DRUG_INTERACTION_ID,DRUG_ID1,REC_MAIN_NO1,REC_SUB_NO1,DRUG_ID2,REC_MAIN_NO2,REC_SUB_NO2,NGROUPNUM,ALERT_HINT,ALERT_LEVEL,ALERT_CAUSE,checkDate) ")
                            .append("values(")
                            /* 互动信息审查库中的id号 */
                            .append("'").append(t.getDRUG_INTERACTION_INFO_ID()).append("'")
                            .append(",'").append(dir[j].getDrugA().getDRUG_NO_LOCAL()).append("'")
                            .append(",'").append(dir[j].getRecMainNo()).append("'")
                            .append(",'").append(dir[j].getRecSubNo()).append("'")
                            .append(",'").append(dir[j].getDrugB().getDRUG_NO_LOCAL()).append("'")
                            .append(",'").append(dir[j].getRecMainNo2()).append("'")
                            .append(",'").append(dir[j].getRecSubNo2()).append("'")
                            .append(",'").append(this.ngroupnum).append("'")
                            .append(",'").append("").append("'")
                            .append(",'").append(dir[j].getAlertLevel()).append("'")
                            .append(",'").append("").append("'")
                            .append(",'").append(CheckTime).append("'")
                            .append(")");
                        query.update(sql.toString());
                        sql.setLength(0);
                    }
                }
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]------互动信息----" + e.getMessage() + e.toString());
        }
    }

    @Override
    public void saveDrugIvEffectCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
                return ;
            if(dsr.getCheckResults() == null)
                return ;
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugIvEffectRslt[] die = tcr.getDrugIvEffectRslt();
                if(die == null || die.length == 0 )
                {
                    continue;
                }
                for(int j = 0 ;j<die.length;j++)
                {
                    TDrugIvEffect[] diet = die[j].getTDrugIvEffect();
                    for(TDrugIvEffect t : diet)
                    {
                        StringBuffer sql = new StringBuffer();
                        sql.append("insert into DRUG_IV_EFFECT_CHECKRSLT(EFFECT_ID,DRUG_ID1,REC_MAIN_NO1,REC_SUB_NO1,DRUG_ID2,REC_MAIN_NO2,REC_SUB_NO2,NGROUPNUM,ALERT_HINT,ALERT_LEVEL,ALERT_CAUSE,checkDate)")
                            .append("values(")
                            .append("'").append(t.getEFFECT_ID()).append("'")
                            .append(",'").append(die[j].getPatOrderDrug1().getDrugID()).append("'")
                            .append(",'").append(die[j].getRecMainNo()).append("'")
                            .append(",'").append(die[j].getRecSubNo()).append("'")
                            .append(",'").append(die[j].getPatOrderDrug2().getDrugID()).append("'")
                            .append(",'").append(die[j].getRecMainNo2()).append("'")
                            .append(",'").append(die[j].getRecSubNo2()).append("'")
                            .append(",'").append(this.ngroupnum).append("'")
                            .append(",'").append("").append("'")
                            .append(",'").append(die[j].getAlertLevel()).append("'")
                            .append(",'").append("").append("'")
                            .append(",'").append(CheckTime).append("'")
                            .append(")");
                        query.update(sql.toString());
                        sql.setLength(0);
                    }
                }
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]-------配伍信息------" + e.getMessage() + e.getLocalizedMessage());
        }
    }

    @Override
    public void saveDrugDiagCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
                return ;
            if(dsr.getCheckResults() == null)
                return ;
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugDiagRslt[] ddr = tcr.getDrugDiagRslt();
                if(ddr == null || ddr.length == 0)
                {
                    continue;
                }
                for(int j = 0 ;j<ddr.length ;j++)
                {
                    StringBuffer sql = new StringBuffer();
                    sql.append("insert into DRUG_DIAGINFO_CHECKRSLT(DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,checkDate)")
                        .append("values(")
                        .append("'").append(ddr[j].getDrug().getDRUG_NO_LOCAL()).append("'")
                        .append(",'").append(ddr[j].getRecMainNo()).append("'")
                        .append(",'").append(ddr[j].getRecSubNo()).append("'")
                        .append(",'").append(this.ngroupnum).append("'")
                        .append(",'").append(ddr[j].getAlertLevel()).append("'")
                        .append(",'").append(ddr[j].getAlertHint()).append("'")
                        .append(",'").append(ddr[j].getAlertCause()).append("'")
                        .append(",'").append(CheckTime).append("'")
                        .append(")");
                    query.update(sql.toString());
                    sql.setLength(0);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]--------禁忌症审查-------" + e.getMessage() + e.getLocalizedMessage());
        }
    }

    @Override
    public void saveDrugSpecPeopleCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
                return ;
            if(dsr.getCheckResults() == null)
                return ;
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugSpecPeopleRslt[] dsp = tcr.getDrugSpecPeopleRslt();
                if(dsp == null || dsp.length == 0)
                {
                    continue;
                }
                for(int j = 0  ;j<dsp.length ;j++)
                {
                    String sql = "";
                    /* 儿童 */
                    if(!dsp[j].getChildLevel().equals("0"))
                    {
                        sql = "insert into DRUG_SPECPEOPLE_CHECKRSLT(DRUG_USE_DETAIL_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,CHECK_ITEM,checkDate)";
                        sql += "values (";
                        sql += "'" + dsp[j].getDrugchild().getDRUG_USE_DETAIL_ID() + "'";
                        sql += ",'" + dsp[j].getDrug().getDRUG_NO_LOCAL() + "'";
                        sql += ",'" + dsp[j].getRecMainNo() + "'";
                        sql += ",'" + dsp[j].getRecSubNo() + "'";
                        sql += ",'" + this.ngroupnum + "'";
                        sql += ",'" + dsp[j].getChildLevel() + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'1'";
                        sql += ",'" + CheckTime + "'";
                        sql += ")";
                        query.update(sql.toString());
                        
                    }
                    /* 老人 */
                    if(!dsp[j].getOldLevel().equals("0"))
                    {
                        sql = "insert into DRUG_SPECPEOPLE_CHECKRSLT(DRUG_USE_DETAIL_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,CHECK_ITEM,checkDate)";
                        sql += "values (";
                        sql += "'" + dsp[j].getDrugold().getDRUG_USE_DETAIL_ID() + "'";
                        sql += ",'" + dsp[j].getDrug().getDRUG_NO_LOCAL() + "'";
                        sql += ",'" + dsp[j].getRecMainNo() + "'";
                        sql += ",'" + dsp[j].getRecSubNo() + "'";
                        sql += ",'" + this.ngroupnum + "'";
                        sql += ",'" + dsp[j].getOldLevel() + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'2'";
                        sql += ",'" + CheckTime + "'";
                        sql += ")";
                        query.update(sql.toString());
                    }
                    /* 孕妇*/
                    if(!dsp[j].getPregnantLevel().equals("0"))
                    {
                        sql = "insert into DRUG_SPECPEOPLE_CHECKRSLT(DRUG_USE_DETAIL_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,CHECK_ITEM,checkDate)";
                        sql += "values (";
                        sql += "'" + dsp[j].getDrugpregnant().getDRUG_USE_DETAIL_ID() + "'";
                        sql += ",'" + dsp[j].getDrug().getDRUG_NO_LOCAL() + "'";
                        sql += ",'" + dsp[j].getRecMainNo() + "'";
                        sql += ",'" + dsp[j].getRecSubNo() + "'";
                        sql += ",'" + this.ngroupnum + "'";
                        sql += ",'" + dsp[j].getPregnantLevel() + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'3'";
                        sql += ",'" + CheckTime + "'";
                        sql += ")";
                        query.update(sql.toString());
                    }
    //              private TDrugUseDetail druglact ;
    //              private TDrugUseDetail drughepatical ;
    //              private TDrugUseDetail drugrenal ;
                    /* 哺乳期 */
                    if(!dsp[j].getLactLevel().equals("0"))
                    {
                        sql = "insert into DRUG_SPECPEOPLE_CHECKRSLT(DRUG_USE_DETAIL_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,CHECK_ITEM,checkDate)";
                        sql += "values (";
                        sql += "'" + dsp[j].getDruglact().getDRUG_USE_DETAIL_ID() + "'";
                        sql += ",'" + dsp[j].getDrug().getDRUG_NO_LOCAL() + "'";
                        sql += ",'" + dsp[j].getRecMainNo() + "'";
                        sql += ",'" + dsp[j].getRecSubNo() + "'";
                        sql += ",'" + this.ngroupnum + "'";
                        sql += ",'" + dsp[j].getLactLevel() + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'" + "" + "'";
                        sql += ",'4'";
                        sql += ",'" + CheckTime + "'";
                        sql += ")";
                        query.update(sql.toString());
                    }
                    /* 肝功不全 */
                    if(!dsp[j].getHepaticalLevel().equals("0"))
                    {
                        sql = "insert into DRUG_SPECPEOPLE_CHECKRSLT(DRUG_USE_DETAIL_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,CHECK_ITEM,checkDate)";
                        sql += "values (";
                        sql += "'" + dsp[j].getDrughepatical().getDRUG_USE_DETAIL_ID() + "'";
                        sql += ",'" + dsp[j].getDrug().getDRUG_NO_LOCAL() + "'";
                        sql += ",'" + dsp[j].getRecMainNo() + "'";
                        sql += ",'" + dsp[j].getRecSubNo() + "'";
                        sql += ",'" + this.ngroupnum + "'";
                        sql += ",'" + dsp[j].getHepaticalLevel() + "'";
                        sql += ",''";
                        sql += ",''";
                        sql += ",'5'";
                        sql += ",'" + CheckTime + "'";
                        sql += ")";
                        query.update(sql.toString());
                    }
                    /* 肾功不全  */
                    if(!dsp[j].getRenalLevel().equals("0"))
                    {
                        sql = "insert into DRUG_SPECPEOPLE_CHECKRSLT(DRUG_USE_DETAIL_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,CHECK_ITEM,checkDate)";
                        sql += "values (";
                        sql += "'" + dsp[j].getDrugrenal().getDRUG_USE_DETAIL_ID() + "'";
                        sql += ",'" + dsp[j].getDrug().getDRUG_NO_LOCAL() + "'";
                        sql += ",'" + dsp[j].getRecMainNo() + "'";
                        sql += ",'" + dsp[j].getRecSubNo() + "'";
                        sql += ",'" + this.ngroupnum + "'";
                        sql += ",'" + dsp[j].getRenalLevel() + "'";
                        sql += ",''";
                        sql += ",''";
                        sql += ",'6'";
                        sql += ",'" + CheckTime + "'";
                        sql += ")";
                        query.update(sql.toString());
                    }
                }
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]-----------特殊人群----------" + e.getMessage() + e.toString());
        }
    }

    @Override
    public void saveDrugIngredientCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            setQueryCode("HisSysManager");
            if(dsr == null)
            {
                return ;
            }
            if(dsr.getCheckResults() == null)
            {
                return ;
            }
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugIngredientRslt[] did = tcr.getDrugIngredientRslt();
                if(did == null || did.length == 0)
                {
                    continue;
                }
                for(int j = 0 ;j< did.length ; j++)
                {
                    for(int x = 0 ; x<did[j].getDrugs().length;x++)
                    {
                        TDrug drug = did[j].getDrugs()[x];
                        StringBuffer sql = new StringBuffer();
                        sql.append("insert into DRUG_INGREDIEN_CHECKRSLT(DRUG_ID1,REC_MAIN_NO1,REC_SUB_NO1,DRUG_ID2,REC_MAIN_NO2,REC_SUB_NO2,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,checkDate) ")
                            .append("values(")
                            .append("'").append(did[j].getDrug().getDRUG_NO_LOCAL()).append("'")
                            .append(",'").append(did[j].getRecMainNo()).append("'")
                            .append(",'").append(did[j].getRecSubNo()).append("'")
                            .append(",'").append(drug.getDRUG_NO_LOCAL()).append("'")
                            .append(",'").append(drug.getRecMainNo()).append("'")
                            .append(",'").append(drug.getRecSubNo()).append("'")
                            .append(",'").append(this.ngroupnum).append("'")
                            .append(",'").append(did[j].getAlertLevel()).append("'")
                            .append(",'").append(did[j].getAlertHint()).append("'")
                            .append(",'").append(did[j].getAlertCause()).append("'")
                            .append(",'").append(CheckTime).append("'")
                            .append(")");
                        query.update(sql.toString());
                        sql.setLength(0);
                    }
                }
            }
        }
        catch(Exception e)
        {
            log.warn("[" + this.ngroupnum + "]--------重复成份审------" + e.getMessage() + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void saveDrugAdministrationCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
            {
                return ;
            }
            if(dsr.getCheckResults() == null)
            {
                return ;
            }
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TAdministrationRslt[] admin = tcr.getAdministrationRslt();
                if(admin == null || admin.length == 0)
                {
                    continue;
                }
                for(int j = 0 ; j<admin.length ; j++)
                {
                    TDrugUseDetail dud =  admin[j].getDrugUseDetail();
                    StringBuffer sql = new StringBuffer();
                    sql.append("insert into ADMINISTRATOR_CHECKRSLT(DRUG_USE_DETAIL_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,ALERT_CAUSE,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,checkDate)")
                        .append("values(")
                        .append("'").append(admin[j].getDrugUseDetail().getDRUG_USE_DETAIL_ID()).append("'")
                        .append(",'").append(admin[j].getDrug().getDRUG_NO_LOCAL()).append("'")
                        .append(",'").append(admin[j].getRecMainNo()).append("'")
                        .append(",'").append(admin[j].getRecSubNo()).append("'")
                        .append(",'").append(dud.getFORBID_CAUSE() + "," + dud.getADVERT_CAUSE() + "," + dud.getINADVIS_CAUSE()).append("'")
                        .append(",'").append(this.ngroupnum).append("'")
                        .append(",'").append(admin[j].getAlertLevel()).append("'")
                        .append(",'").append("").append("'")
                        .append(",'").append(CheckTime).append("'")
                        .append(")");
                    query.update(sql.toString());
                    sql.setLength(0);
                }
            }
        }
        catch(Exception e)
        {
            log.warn("[" + this.ngroupnum + "]------用药途径审查-------" + e.getMessage() +  e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void saveDrugAllergenCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
            {
                return ;
            }
            if(dsr.getCheckResults() == null)
            {
                return ;
            }
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugAllergenRslt[] dag = tcr.getDrugAllergenRslt();
                if(dag == null || dag.length == 0)
                {
                    continue;
                }
                for(int j = 0 ; j < dag.length ; j++)
                {
                    TAllergIngrDrug[] dagBean = dag[j].getDrugAllergen();
                    for(int x = 0 ;x<dagBean.length;x++)
                    {
                        StringBuffer sql = new StringBuffer();
                        sql.append("insert into DRUG_ALLERGEN_CHECKRSLT(DRUG_ALLERGEN_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,checkDate)")
                            .append(" values(")
                            .append("'").append(dagBean[x].getDRUG_ALLERGEN_ID()).append("'")
                            .append(",'").append(dag[j].getDrug().getDRUG_NO_LOCAL()).append("'")
                            .append(",'").append(dag[j].getRecMainNo()).append("'")
                            .append(",'").append(dag[j].getRecSubNo()).append("'")
                            .append(",'").append(this.ngroupnum).append("'")
                            .append(",'").append(dag[j].getAlertLevel()).append("'")
                            .append(",'").append("").append("'")
                            .append(",'").append("").append("'")
                            .append(",'").append(CheckTime).append("'")
                            .append(")");
                        query.update(sql.toString());
                        sql.setLength(0);
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDrugDosageCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
            {
                return ;
            }
            if(dsr.getCheckResults() == null)
            {
                return ;
            }
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugDosageRslt[] ddr = tcr.getDrugDosageRslt();
                if(ddr == null || ddr.length == 0)
                {
                    continue;
                }
                for(int j = 0 ;j < ddr.length ;j++)
                {
                    TDrugDosageRslt t = ddr[j];
                    StringBuffer values = new StringBuffer();
                    StringBuffer    sql = new StringBuffer();
                    for(String value :t.getPerformInfo())
                    {
                        values.append(value).append(";");
                    }
                    sql.append("insert into DRUG_DOSAGE_CHECKRSLT(DRUG_ID,REC_MAIN_NO,REC_SUB_NO,NGROUPNUM,ALERT_LEVEL,ALERT_HINT,ALERT_CAUSE,checkDate)")
                        .append(" values(")
                        .append("'").append(t.getPods().getDRUG_NO_LOCAL()).append("'")
                        .append(",'").append(t.getRecMainNo()).append("'")
                        .append(",'").append(t.getRecSubNo()).append("'")
                        .append(",'").append(this.ngroupnum).append("'")
                        .append(",'").append(t.getAlertLevel()).append("'")
                        .append(",'").append("").append("'")
                        .append(",'").append(values.toString()).append("'")
                        .append(",'").append(CheckTime).append("'")
                        .append(")");
                    query.update(sql.toString());
                    sql.setLength(0);
                }
            }
        }
        catch(Exception e)
        {
            log.warn("[" + this.ngroupnum + "]------药物剂量审查------" + e.getMessage() +  e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void saveDrugSideCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
            {
                return ;
            }
            if(dsr.getCheckResults() == null)
            {
                return ;
            }
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            for(int i = 0 ; i<tcrs.length ;i++)
            {
                TCheckResult tcr = tcrs[i];
                TDrugHarmfulRslt[] dhf = tcr.getSideRslt();
                if(dhf == null || dhf.length == 0)
                {
                    continue;
                }
                for(int j = 0 ; j < dhf.length ; j++)
                {
                    StringBuffer sql = new StringBuffer();
                    sql.append("insert into SIDE_CHECKRSLT(NGROUPNUM,SIDE_ID,DRUG_ID,REC_MAIN_NO,REC_SUB_NO,ALERT_HINT,ALERT_LEVEL,ALERT_CAUSE,checkDate)")
                        .append("values(")
                        .append("'").append(this.ngroupnum).append("'")
                        .append(",'").append(dhf[j].getDrugSide().getSIDE_ID()).append("'")
                        .append(",'").append(dhf[j].getDrug().getDRUG_NO_LOCAL()).append("'")
                        .append(",'").append(dhf[j].getRecMainNo()).append("'")
                        .append(",'").append(dhf[j].getRecSubNo()).append("'")
                        .append(",'").append("").append("'")
                        .append(",'").append(dhf[j].getAlertLevel()).append("'")
                        .append(",'").append(dhf[j].getDrugSide().getDIAGNOSIS_DESC()).append("'")
                        .append(",'").append(CheckTime).append("'")
                        .append(")");
                    query.update(sql.toString());
                    sql.setLength(0);
                    }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]----异常信号审查-----" + e.getMessage() + e.toString());
        }
    }

    @Override
    public void saveDrugSecurityCheckInfo(TPatientOrder po,TDrugSecurityRslt dsr)
    {
        try
        {
            setQueryCode("HisSysManager");
            TCheckResult[] tcrs = dsr.getCheckResults();
            int admRedCount     = 0, admYellowCount = 0;            //给药途径审查结果                  前面是红灯  后面是黄灯
            int dagRedCount     = 0, dagYellowCount = 0;            //过敏审查结果    
            int ddiRedCount     = 0, ddiYellowCount = 0;            //禁忌审查结果   
            int ddgRedCount     = 0, ddgYellowCount = 0;            //剂量审查结果 
            int didRedCount     = 0, didYellowCount = 0;            //重复成份审查结果                  
            int diaRedCount     = 0, diaYellowCount = 0;            //相互作用审查结果                  
            int dieRedCount     = 0, dieYellowCount = 0;            //配伍审查结果                        
            //int dhfRedCount   = 0, dhfYellowCount = 0;            //不良反应审查结果                  
            int NOLDRED         = 0, NOLDYELLOW = 0;                //老人红色,老人黄色 
            int NKIDRED         = 0, NKIDYELLOW = 0;                //小孩红色,小孩黄色
            int NPREGNANTRED    = 0, NPREGNANTYELLOW = 0;           //孕妇红色,孕妇黄色
            int NLACTATIONRED   = 0, NLACTATIONYELLOW = 0;          //哺乳红色,哺乳黄色
            int NRENALRED       = 0, NRENALYELLOW = 0;              //肾红色,肾黄色
            int NHEPATICALRED   = 0, NHEPATICALYELLOW = 0;          //肝红色,肝黄色
            int i =0;
            for (TCheckResult tcr:tcrs) 
            {
                admRedCount      = admRedCount + tcr.getAdmRedCount();
                admYellowCount   = admYellowCount + tcr.getAdmRedCount();
                dagRedCount      = dagRedCount + tcr.getDagRedCount();
                dagYellowCount   = dagYellowCount + tcr.getDagYellowCount();
                ddiRedCount      = ddiRedCount + tcr.getDdiRedCount();
                ddiYellowCount   = ddiYellowCount + tcr.getDdiYellowCount();
                ddgRedCount      = ddgRedCount + tcr.getDdgRedCount();
                ddgYellowCount   = ddgYellowCount + tcr.getDdgYellowCount();
                didRedCount      = didRedCount + tcr.getDidRedCount();
                didYellowCount   = didYellowCount + tcr.getDidYellowCount();
                diaRedCount      = diaRedCount + tcr.getDiaRedCount();
                diaYellowCount   = diaYellowCount + tcr.getDiaYellowCount();
                dieRedCount      = dieRedCount + tcr.getDieRedCount();
                dieYellowCount   = dieYellowCount + tcr.getDieYellowCount();
                
                TDrugSpecPeopleRslt[] dsp = tcr.getDrugSpecPeopleRslt();
                if(dsp != null && dsp.length != 0)
                {
                    for(int j = 0 ; j < dsp.length ; j++)
                    {
                        /* 儿童 */
                        if(!dsp[j].getChildLevel().equals("0"))
                        {
                            if("R".equals(dsp[j].getChildLevel()))
                                NKIDRED ++;
                            else
                                NKIDYELLOW ++;
                        }
                        /* 老人 */
                        else if(!dsp[j].getOldLevel().equals("0"))
                        {
                            if("R".equals(dsp[j].getChildLevel()))
                                NOLDRED ++;
                            else
                                NOLDYELLOW ++;
                        }
                        /* 孕妇*/
                        else if(!dsp[j].getPregnantLevel().equals("0"))
                        {
                            if("R".equals(dsp[j].getChildLevel()))
                                NPREGNANTRED++;
                            else
                                NPREGNANTYELLOW++;  
                        }
                        /* 哺乳期 */
                        else if(!dsp[j].getLactLevel().equals("0"))
                        {
                            if("R".equals(dsp[j].getChildLevel()))
                                NLACTATIONRED++;
                            else
                                NLACTATIONYELLOW++; 
                        }
                        /* 肝功不全 */
                        if(!dsp[j].getHepaticalLevel().equals("0"))
                        {
                            if("R".equals(dsp[j].getChildLevel()))
                                NHEPATICALRED++;
                            else
                                NHEPATICALYELLOW++;
                        }
                        /* 肾功不全  */
                        if(!dsp[j].getRenalLevel().equals("0"))
                        {
                            if("R".equals(dsp[j].getChildLevel()))
                                NRENALRED++; 
                            else
                                NRENALYELLOW ++;        
                        }
                    }
                }
                //dhfRedCount    = dhfRedCount + tcr.getDhfRedCount();
                //dhfYellowCount = dhfYellowCount + tcr.getDhfYellowCount();
                i++;
            }
            int redCount     =  dagRedCount + dieRedCount + ddiRedCount + diaRedCount + admRedCount + ddgRedCount + 
                                didRedCount + NOLDRED + NKIDRED + NPREGNANTRED + NLACTATIONRED + NRENALRED + NHEPATICALRED;
            int yellowCount  =  dagYellowCount + dieYellowCount + ddiYellowCount + diaYellowCount + admYellowCount + ddgYellowCount +
                                didYellowCount + NOLDYELLOW + NKIDYELLOW + NPREGNANTYELLOW + NLACTATIONYELLOW + NRENALYELLOW + NHEPATICALYELLOW;    
            StringBuffer sql = new StringBuffer();
            sql.append(" insert into DRUG_CHECKINFO_COLLECT(NGROUPNUM,NADVICENUM,NREDADVICEQUESSUM,NYELLOWADVICEQUESSUM");//批次号、医嘱数、红色问题医嘱数、黄色问题医嘱数
            sql.append(",VDOCTORCODE,VDOCTORNAME");                                         //医生编码、医生姓名
            sql.append(",VORGCODE,VORGNAME");                                               //组织编码、组织名称
            sql.append(",NALLERGENRED,NALLERGENYELLOW");                                    //过敏红色、过敏黄色
            sql.append(",NIVEFFECTRED,NIVEFFECTYELLOW");                                    //配伍红色、配伍黄色
            sql.append(",NDIAGINFORED,NDIAGINFOYELLOW");                                    //禁忌红色、禁忌黄色
            sql.append(",NINTERACTIONRED,NINTERACTIONYELLOW");                              //相互作用红色、相互作用黄色
            sql.append(",NADMINISTRATIONRED,NADMINISTRATIONYELLOW");                        //用药途径红色，用药途径黄色
            sql.append(",NDOSAGECHECKRED,NDOSAGECHECKYELLOW");                              //剂量红色,剂量黄色
            sql.append(",NINGREDIENTRED,NINGREDIENTYELLOW");                                //重复成份红色,重复成分黄色
            sql.append(",NOLDRED,NOLDYELLOW");                                              //老人红色,老人黄色 
            sql.append(",NKIDRED,NKIDYELLOW");                                              //小孩红色,小孩黄色
            sql.append(",NPREGNANTRED,NPREGNANTYELLOW");                                    //孕妇红色,孕妇黄色
            sql.append(",NLACTATIONRED,NLACTATIONYELLOW");                                  //哺乳红色,哺乳黄色
            sql.append(",NRENALRED,NRENALYELLOW");                                          //肾红色,肾黄色
            sql.append(",NHEPATICALRED,NHEPATICALYELLOW");                                  //肝红色,肝黄色
            //sql.append(",NSIDERED,NSIDEYELLOW");                                          //异常信号红色,异常信号黄色
            
            sql.append(",CHECKDATE");
            sql.append(" ) values ( ");
            sql.append("'").append(this.ngroupnum).append("',");
            sql.append(i).append(",");
            sql.append(redCount).append(",").append(yellowCount).append(",");
            sql.append("'").append(po.getDoctorID()).append("',").append("'").append(po.getDoctorName()).append("',");
            sql.append("'").append(po.getDoctorDeptID()).append("',").append("'").append(po.getDoctorDeptName()).append("',");
            
            sql.append(dagRedCount).append(",").append(dagYellowCount).append(",");
            sql.append(dieRedCount).append(",").append(dieYellowCount).append(",");
            sql.append(ddiRedCount).append(",").append(ddiYellowCount).append(",");
            sql.append(diaRedCount).append(",").append(diaYellowCount).append(",");
            sql.append(admRedCount).append(",").append(admYellowCount).append(",");
            sql.append(ddgRedCount).append(",").append(ddgYellowCount).append(",");
            sql.append(didRedCount).append(",").append(didYellowCount).append(",");
            
            sql.append(NOLDRED).append(",").append(NOLDYELLOW).append(",");     
            sql.append(NKIDRED).append(",").append(NKIDYELLOW).append(",");
            sql.append(NPREGNANTRED).append(",").append(NPREGNANTYELLOW).append(",");
            sql.append(NLACTATIONRED).append(",").append(NLACTATIONYELLOW).append(",");
            sql.append(NRENALRED).append(",").append(NRENALYELLOW).append(",");
            sql.append(NHEPATICALRED).append(",").append(NHEPATICALYELLOW).append(","); 
            
            //sql.append(diaRedCount).append(",");
            //sql.append(diaYellowCount).append(",");
            
            sql.append("to_date('").append(CheckTime).append("','yyyy-mm-dd hh24:mi:ss')").append(")");
            query.execute(sql.toString());
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]----保存审查总体结构错误 ----" + e.getMessage() + e.toString());
        }
    }
    
    @Override
    public void saveAntiDrugSecutity(TPatientOrder po,TAntiDrugSecurityResult[] adsr)
    {
        setQueryCode("HisSysManager");
        try
        {
            for(TAntiDrugSecurityResult ad : adsr)
            {
                TAntiDrugSecurityCheckResult[] ascrs = ad.getTAntiDrugSecurity();
                for(TAntiDrugSecurityCheckResult ascr :ascrs)
                {
                    for(TAntiDrugResult a :ascr.getAntiDrugResult())
                    {
                        StringBuffer sql = new StringBuffer();
                        sql.append("insert into ANTIDRUG_CHECKRSLT(drug_id,rec_main_no,rec_sub_no ,ngroupnum,checkdate,checktype,memo,dept_code,dept_name,doctor_code,doctor_name,result")
                               .append(" ) values (")
                               .append(" '").append(ascr.getDrug_ID()).append("'")
                               .append(",'").append(ascr.getOrder_No()).append("'")
                               .append(",'").append(ascr.getOrder_Sub_No()).append("'")
                               .append(",'").append(this.ngroupnum).append("'")
                               .append(",'").append(CheckTime).append("'")
                               .append(",'").append(a.getCheckType()).append("'")
                               .append(",'").append(a.getMemo()).append("'")                          
                               .append(",'").append(po.getDoctorDeptID()).append("'")
                               .append(",'").append(po.getDoctorDeptName()).append("'")
                               .append(",'").append(po.getDoctorID()).append("'")
                               .append(",'").append(po.getDoctorName()).append("'")
                               .append(",'").append( a.isResult()).append("'")
                               .append(")");
                        query.update(sql.toString());
                        sql.setLength(0);
                    }
                }
                
                TDrugSecurityRslt dsr = new TDrugSecurityRslt();
                TDrug drug = new TDrug();
                drug.setDRUG_NO_LOCAL(ad.getDrug_ID());
                drug.setRecMainNo(ad.getOrder_No());
                drug.setRecSubNo(ad.getOrder_Sub_No());  
                /*特殊人群*/
                if(ad.getDrugSpecPeopleRslt()!= null && ad.getDrugSpecPeopleRslt().length != 0 && ad.getDrugSpecPeopleRslt()[0] != null)
                {
                    dsr.regDrugSpecPeopleCheckResult(drug, ad.getDrugSpecPeopleRslt()[0]);
                }
                if(ad.getAllergenRslt() != null && ad.getAllergenRslt().length != 0 && ad.getAllergenRslt()[0] != null)
                {
                    /* 药物过敏审查结果对象 */
                    dsr.regAllergenCheckResult(drug, ad.getAllergenRslt()[0]);
                }
                if(ad.getDosageRslt() != null && ad.getDosageRslt().length != 0 && ad.getDosageRslt()[0] != null)
                {
                    /* 剂量 */
                    dsr.regDosageCheckResult(drug, ad.getDosageRslt()[0]);
                }
                this.saveDrugSpecPeopleCheckInfo(dsr);
                this.saveDrugAllergenCheckInfo(dsr);
                this.saveDrugDosageCheckInfo(dsr);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]----抗菌药物审查结果保存 ----" + e.getMessage() + e.toString());
        }
    }
    
    @Override
    public void savePreveUseDrug(TPatientOrder po)
    {
        setQueryCode("IAS");
        try
        {
            if(po.getPreveUseDrug() == null || po.getPreveUseDrug().length == 0 ) return ;
            for(TPreveUseDrug p : po.getPreveUseDrug())
            {
                String sql = " insert into ANTI_DRUG_USE_REC_YF(yf_id, patient_id, visit_id, dept_code, dept_name, doctor_name, name, sex, age, weight, drug_code, drug_name, dosage, dosage_units, administration, opertor_date, opertor_name, opertor_type, opertor_use_time, drug_use_time, gms, btgms, wxys, kndzbj, kljlanjl, yf_use_drug_yj, REC_DATE,tsyq) values (" +
                "'" + this.ngroupnum          + "'," +                        
                "'" + p.getPATIENT_ID()       + "'," +
                "'" + p.getVISIT_ID()         + "'," +
                "'" + p.getDEPT_CODE()        + "'," +
                "'" + p.getDEPT_NAME()        + "'," +
                "'" + p.getDOCTOR_NAME()      + "'," +
                "'" + p.getNAME()             + "'," +
                "'" + p.getSEX()              + "'," +
                "'" + p.getAGE()              + "'," +
                "'" + p.getWEIGHT()           + "'," +
                "'" + p.getDRUG_CODE()        + "'," +
                "'" + p.getDRUG_NAME()        + "'," +
                "'" + p.getDOSAGE()           + "'," +
                "'" + p.getDOSAGE_UNITS()     + "'," +
                "'" + p.getADMINISTRATION()   + "'," +
                " to_date('" + p.getOPERTOR_DATE() + "','yyyy-mm-dd hh24:mi:ss') ,"  +
                "'" + p.getOPERTOR_NAME()     + "',"  +
                "'" + p.getOPERTOR_TYPE()     + "',"  +
                "'" + p.getOPERTOR_USE_TIME() + "',"  +
                "'" + p.getDRUG_USE_TIME()    + "',"  +
                "'" + p.getGMS()              + "',"  +
                "'" + p.getBTGMS()            + "',"  +
                "'" + p.getWXYS()             + "',"  +
                "'" + p.getKNDZBJ()           + "',"  +
                "'" + p.getKLJLANJL()         + "',"  +
                "'" + p.getYF_USE_DRUG_YJ()   + "',"  +
                "'" + p.getREC_DATE()         + "',"  +
                "'" + p.getTSYQ()             + "'"  +
                ")";
                try
                {
                    query.update(sql);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    log.warn("[" + this.ngroupnum + "] -----预防用药信息保存----[" + sql + "]");
                }
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "] -----预防用药信息保存---- ");
        }
    }
    
    @Override
    public void saveTreatUseDrug(TPatientOrder po)
    {
        setQueryCode("IAS");
        try
        {
            if(po.getTreatUseDrug() == null || po.getTreatUseDrug().length == 0 ) return ;
            for(TTreatUseDrug p : po.getTreatUseDrug())
            {
                String sql = " insert into ANTI_DRUG_USE_REC_ZL(zl_id, patient_id, visit_id, dept_code, dept_name, doctor_name, name, sex, age, weight, yyly, zlfl, grbw, zdyj, kndzbj, drug_code, drug_name, dosage, dosage_units, administration, rec_date, twdate, twvalue, twok, xxdate, xxvalue, cvalue, xxok) values (" +
                "'" + this.ngroupnum        + "'," +
                "'" + p.getPATIENT_ID()     + "'," +
                "'" + p.getVISIT_ID()       + "'," +
                "'" + p.getDEPT_CODE()      + "'," +
                "'" + p.getDEPT_NAME()      + "'," +
                "'" + p.getDOCTOR_NAME()    + "'," +
                "'" + p.getNAME()           + "'," +
                "'" + p.getSEX()            + "'," +
                "'" + p.getAGE()            + "'," +
                "'" + p.getWEIGHT()         + "'," +
                "'" + p.getYYLY()           + "'," +
                "'" + p.getZLFL()           + "'," +
                "'" + p.getGRBW()           + "'," +
                "'" + p.getZDYJ()           + "'," +
                "'" + p.getKNDZBJ()         + "'," +
                "'" + p.getDRUG_CODE()      + "'," +
                "'" + p.getDRUG_NAME()      + "'," +
                "'" + p.getDOSAGE()         + "'," +
                "'" + p.getDOSAGE_UNITS()   + "'," +
                "'" + p.getADMINISTRATION() + "'," +
                "'" + p.getREC_DATE()       + "'," +
                "'" + p.getTWDATE()         + "'," +
                "'" + p.getTWVALUE()        + "'," +
                "'" + p.getTWOK()           + "'," +
                "'" + p.getXXDATE()         + "'," +
                "'" + p.getXXVALUE()        + "'," +
                "'" + p.getCVALUE()         + "'," +
                "'" + p.getXXOK()           + "'" +
                ") ";
                try
                {
                   query.update(sql);
                }
                catch(Exception e )
                {
                    e.printStackTrace();
                    log.warn("[" + this.ngroupnum + "] -----治疗用药信息保存----[" + sql + "]");
                }
            }
        }
        catch(Exception e )
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "] -----治疗用药信息保存---- ");
        }
    }
}
