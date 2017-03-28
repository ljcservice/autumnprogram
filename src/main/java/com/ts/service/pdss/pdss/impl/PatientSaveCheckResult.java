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
import com.ts.util.UuidUtil;

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
            //StringBuffer patient = new StringBuffer();
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
                /* 审查病人住院信息表 CHECK_PAT_ORDER_VISITINFO */
                dao.save("InfoMapper.insertCheckPtaOrderVisi", pw);
                /* 审查病人扩展信息表 */
                /* 审查病人扩展信息表  CHECK_PAT_ORDER_INFO_EXT */
                PageData param = new PageData();
            	param.put("is_lact", po.getPatInfoExt().getIsLact());
            	param.put("is_pregnant", po.getPatInfoExt().getIsPregnant());
            	param.put("insureance_type", po.getPatInfoExt().getInsureanceType());
            	param.put("insurance_no", po.getPatInfoExt().getInsuranceNo());
            	param.put("is_liverwhole", po.getPatInfoExt().getIsLiverWhole());
            	param.put("is_kidneywhole", po.getPatInfoExt().getIsKidneyWhole());
            	param.put("height", po.getPatInfoExt().getHeight());
            	param.put("weight", po.getPatInfoExt().getWeight());
            	param.put("ngroupnum", this.ngroupnum);
            	param.put("checkdate", CheckTime);
            	param.put("patient_id", patient_id);
            	param.put("visit_id", visit_id);
                dao.save("InfoMapper.insertCheckPtaOrderInfoExt", pw);
            }
            /*保存医生信息*/
            PageData param = new PageData();
        	param.put("NGROUPNUM", this.ngroupnum);
        	param.put("DOCTOR_NAME", po.getDoctorName());
        	param.put("DOCTOR_CODE", po.getDoctorID());
        	param.put("DEPT_NAME", po.getDoctorDeptName());
        	param.put("DEPT_CODE", po.getDoctorDeptID());
        	param.put("DOCTOR_TITLE", po.getDoctorTitleName());
        	param.put("Doctor_Title_id", po.getDoctorTitleID());
        	param.put("patient_id", patient_id);
        	param.put("visit_id", visit_id);
            dao.save("InfoMapper.insertCheckDoctorInfo", pw);
            
            
            param = new PageData();
        	param.put("ngroupnum", this.ngroupnum);
        	param.put("check_date", DateUtil.fomatDate2(CheckTime));
        	param.put("patient_id", patient_id);
        	param.put("visit_id", visit_id);
            dao.save("InfoMapper.insertCheckSerialList", pw);
//            query.update(" insert into CHECK_SERIAL_LIST (patient_id, visit_id, check_date, ngroupnum) values ("+
//                    "'"+patient_id+"',"+
//                    visit_id+","+
//                    "to_date('" + CheckTime + "','yyyy-mm-dd hh24:mi:ss'),"+
//                    "'" + this.ngroupnum + "'" + 
//                    ")");
            
            //String[] sql = new String[po.getPatOrderDrugs().length];
            
            List<String> sqlList = new ArrayList<String>();
            for(int i = 0 ; i<po.getPatOrderDrugs().length ; i++)
            {
                TPatOrderDrug pod = po.getPatOrderDrugs()[i];
                TDrug drug = new TDrug();
                drug.setDRUG_NO_LOCAL(pod.getDrugID());
                drug.setRecMainNo(pod.getRecMainNo());
                drug.setRecSubNo(pod.getRecSubNo());
                TCheckResult cres = dsr.getCheckResult(drug);
                if (cres != null)
                {
                    String checkResult = cres.getAlertLevel();
                    param = new PageData();
                	param.put("DOCTOR_DEPT", pod.getDoctorDept());
                	param.put("IS_GROUP", "0");
                	param.put("ADMINISTRATION_ID", pod.getAdministrationID());
                	param.put("PERFORM_FREQ_DICT_ID", pod.getPerformFreqDictID());
                	param.put("DRUG_ID", pod.getDrugID());
                	param.put("REC_MAIN_NO", pod.getRecMainNo());
                	param.put("REC_SUB_NO", pod.getRecSubNo());
                	param.put("PERFORM_FREQ_DICT_NAME", pod.getPerformFreqDictText());
                	param.put("DOSAGE", pod.getDosage());
                	param.put("DOSE_UNITS", pod.getDoseUnits());
                	param.put("DOCTOR", pod.getDoctorName());
                	param.put("NGROUPNUM", this.ngroupnum);
                	param.put("checkDate", CheckTime);
                	param.put("USETYPE", "");
                	param.put("USECAUSE", "");
                	param.put("DRUG_NAME", pod.getDrugName());
                	param.put("check_Result", checkResult);
             		String start = pod.getStartDateTime();
            		String stop  = pod.getStartDateTime();
                    if(start != null && !"".equals(start))
                    {
                    	if(start.length()>19){
                    		start = start.substring(0, 19);
                    		param.put("START_DATE_TIME",DateUtil.fomatDate2(start));
                    	}
                    	if(stop.length()>19)
                    		stop = stop.substring(0, 19);
                    	param.put("STOP_DATE_TIME",DateUtil.fomatDate2(stop));
                    }
                    /* 病人用药记录 */
                    dao.save("InfoMapper.insertCheckPatOrderDrug", pw);
                    
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
                param = new PageData();
            	param.put("DIAGNOSIS_DICT_ID", pod.getDiagnosisDictID());
            	param.put("NGROUPNUM", this.ngroupnum);
            	param.put("DIAGNOSIS_NAME", pod.getDiagnosisName());
            	param.put("checkDate", CheckTime);
                /*  审查医嘱诊断表 */
                dao.save("InfoMapper.insertCheckPatOrderDiagnosis", param);
            }
            
            /* 审查医嘱诊断表 CHECK_PAT_ORDER_DIAGNOSIS  */
//            if(sql.length > 0)
//              query.batchUpdate(sql);
//            sql = new String[po.getPatOrderDrugSensitives().length];
            for(int i = 0 ; i< po.getPatOrderDrugSensitives().length; i++)
            {
                TPatOrderDrugSensitive pods = po.getPatOrderDrugSensitives()[i];
                /* 审查医嘱过敏表 */
                param = new PageData();
            	param.put("DRUG_ALLERGEN_ID", pods.getPatOrderDrugSensitiveID());
            	param.put("NGROUPNUM", this.ngroupnum);
            	param.put("checkDate", CheckTime);
                /*  审查医嘱诊断表 */
                dao.save("InfoMapper.insertCheckPatOrderDrugSensitive", param);
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
                    if((ps.getTWOK() + ps.getTWValue() + ps.getXXOK() + ps.getCValue()).length() > 0)
                    {
                        param = new PageData();
                    	param.put("ngroupnum", this.ngroupnum);
                    	param.put("twdate", ps.getTWDate());
                    	param.put("twvalue", ps.getTWValue());
                    	param.put("twok", ps.getTWOK());
                    	param.put("xxdate", ps.getXXDate());
                    	param.put("xxvalue", ps.getXXValue());
                    	param.put("cvalue", ps.getCValue());
                    	param.put("xxok", ps.getXXOK());
                        dao.save("InfoMapper.insertCheckPatSigns", param);
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

    /**
     *  互动信息
     *  互动信息：类型为1
     */
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
                        PageData param = new PageData();
                    	param.put("ID", t.getDRUG_INTERACTION_INFO_ID());
                    	param.put("DRUG_ID1", dir[j].getDrugA().getDRUG_NO_LOCAL());
                    	param.put("REC_MAIN_NO1", dir[j].getRecMainNo());
                    	param.put("REC_SUB_NO1",dir[j].getRecSubNo());
                    	param.put("DRUG_ID2", dir[j].getDrugB().getDRUG_NO_LOCAL());
                    	param.put("REC_MAIN_NO2", dir[j].getRecMainNo2());
                    	param.put("REC_SUB_NO2", dir[j].getRecSubNo2());
                    	param.put("NGROUPNUM", this.ngroupnum);
                    	param.put("ALERT_HINT", "");
                    	param.put("ALERT_LEVEL", dir[j].getAlertLevel());
                    	param.put("ALERT_CAUSE", "");
                    	param.put("checkDate", CheckTime);
                    	param.put("type", 1);//互动信息的类型
                        dao.save("ResultMapper.saveDrugCheckInfo", param);
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

    /**
     *  配伍信息
     *  配伍信息：类型为2
     */
    @Override
    public void saveDrugIvEffectCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
                return ;
            if(dsr.getCheckResults() == null)
                return ;
            //setQueryCode("HisSysManager");
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
                        PageData param = new PageData();
                    	param.put("ID", t.getEFFECT_ID());
                    	param.put("DRUG_ID1", die[j].getPatOrderDrug1().getDrugID());
                    	param.put("REC_MAIN_NO1", die[j].getRecMainNo());
                    	param.put("REC_SUB_NO1",die[j].getRecSubNo());
                    	param.put("DRUG_ID2", die[j].getPatOrderDrug2().getDrugID());
                    	param.put("REC_MAIN_NO2",die[j].getRecMainNo2());
                    	param.put("REC_SUB_NO2", die[j].getRecSubNo2());
                    	param.put("NGROUPNUM", this.ngroupnum);
                    	param.put("ALERT_HINT", "");
                    	param.put("ALERT_LEVEL", die[j].getAlertLevel());
                    	param.put("ALERT_CAUSE", "");
                    	param.put("checkDate", CheckTime);
                    	param.put("type", 2);//配伍信息的类型
                        dao.save("ResultMapper.saveDrugCheckInfo", param);
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
    /**
     *  禁忌症审查
     *  禁忌症审查：类型为3
     */
    @Override
    public void saveDrugDiagCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
                return ;
            if(dsr.getCheckResults() == null)
                return ;
            //setQueryCode("HisSysManager");
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
                        PageData param = new PageData();
                    	param.put("ID", UuidUtil.get32UUID());
                    	param.put("DRUG_ID1", ddr[j].getDrug().getDRUG_NO_LOCAL());
                    	param.put("REC_MAIN_NO1", ddr[j].getRecMainNo());
                    	param.put("REC_SUB_NO1",ddr[j].getRecSubNo());
                    	param.put("DRUG_ID2", null);
                    	param.put("REC_MAIN_NO2",null);
                    	param.put("REC_SUB_NO2", null);
                    	param.put("NGROUPNUM", this.ngroupnum);
                    	param.put("ALERT_HINT", ddr[j].getAlertHint());
                    	param.put("ALERT_LEVEL", ddr[j].getAlertLevel());
                    	param.put("ALERT_CAUSE", ddr[j].getAlertCause());
                    	param.put("checkDate", CheckTime);
                    	param.put("type", 3);//禁忌症审查的类型
                        dao.save("ResultMapper.saveDrugCheckInfo", param);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]--------禁忌症审查-------" + e.getMessage() + e.getLocalizedMessage());
        }
    }

    /**
     *  人群：类型为4
     */
    @Override
    public void saveDrugSpecPeopleCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            if(dsr == null)
                return ;
            if(dsr.getCheckResults() == null)
                return ;
            //setQueryCode("HisSysManager");
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
                    PageData param = new PageData();
                    param.put("DRUG_ID1", dsp[j].getDrug().getDRUG_NO_LOCAL());
                    /* 儿童 */
                    if(!dsp[j].getChildLevel().equals("0"))
                    {
                    	param.put("ID", dsp[j].getDrugchild().getDRUG_USE_DETAIL_ID());
                        param.put("ALERT_LEVEL",  dsp[j].getOldLevel());
                        param.put("CHECK_ITEM", 1);
                        
                        
                    }
                    /* 老人 */
                    if(!dsp[j].getOldLevel().equals("0"))
                    {
                    	param.put("ID", dsp[j].getDrugold().getDRUG_USE_DETAIL_ID());
                        param.put("ALERT_LEVEL",  dsp[j].getOldLevel());
                        param.put("CHECK_ITEM", 2);
                    }
                    /* 孕妇*/
                    if(!dsp[j].getPregnantLevel().equals("0"))
                    {
                    	param.put("ID", dsp[j].getDrugpregnant().getDRUG_USE_DETAIL_ID());
                        param.put("ALERT_LEVEL",  dsp[j].getPregnantLevel());
                        param.put("CHECK_ITEM", 3);
                    }
    //              private TDrugUseDetail druglact ;
    //              private TDrugUseDetail drughepatical ;
    //              private TDrugUseDetail drugrenal ;
                    /* 哺乳期 */
                    if(!dsp[j].getLactLevel().equals("0"))
                    {
                    	param.put("ID", dsp[j].getDruglact().getDRUG_USE_DETAIL_ID());
                        param.put("ALERT_LEVEL",  dsp[j].getLactLevel());
                        param.put("CHECK_ITEM", 4);
                    }
                    /* 肝功不全 */
                    if(!dsp[j].getHepaticalLevel().equals("0"))
                    {
                    	param.put("ID", dsp[j].getDrughepatical().getDRUG_USE_DETAIL_ID());
                        param.put("ALERT_LEVEL",  dsp[j].getHepaticalLevel());
                        param.put("CHECK_ITEM", 5);
                    }
                    /* 肾功不全  */
                    if(!dsp[j].getRenalLevel().equals("0"))
                    {
                    	param.put("ID", dsp[j].getDrugrenal().getDRUG_USE_DETAIL_ID());
                        param.put("ALERT_LEVEL",  dsp[j].getRenalLevel());
                        param.put("CHECK_ITEM", 6);
                    }
                	
                	param.put("REC_MAIN_NO1", dsp[j].getRecMainNo());
                	param.put("REC_SUB_NO1",dsp[j].getRecSubNo());
                	param.put("DRUG_ID2", null);
                	param.put("REC_MAIN_NO2",null);
                	param.put("REC_SUB_NO2", null);
                	param.put("NGROUPNUM", this.ngroupnum);
                	param.put("ALERT_HINT", "");
                	param.put("ALERT_CAUSE", "");
                	param.put("checkDate", CheckTime);
                	param.put("type", 4);//人群信息的类型
                    dao.save("ResultMapper.saveDrugCheckInfo", param);
                }
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]-----------特殊人群----------" + e.getMessage() + e.toString());
        }
    }

    /**
     *  构成组成部分的，类型为5
     */
    @Override
    public void saveDrugIngredientCheckInfo(TDrugSecurityRslt dsr)
    {
        try
        {
            //setQueryCode("HisSysManager");
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
                        PageData param = new PageData();
                    	param.put("ID", UuidUtil.get32UUID());
                    	param.put("DRUG_ID1", did[j].getDrug().getDRUG_NO_LOCAL());
                    	param.put("REC_MAIN_NO1", did[j].getRecMainNo());
                    	param.put("REC_SUB_NO1",did[j].getRecSubNo());
                    	param.put("DRUG_ID2", drug.getDRUG_NO_LOCAL());
                    	param.put("REC_MAIN_NO2",drug.getRecMainNo());
                    	param.put("REC_SUB_NO2", drug.getRecSubNo());
                    	param.put("NGROUPNUM", this.ngroupnum);
                    	param.put("ALERT_HINT", did[j].getAlertHint());
                    	param.put("ALERT_LEVEL", did[j].getAlertLevel());
                    	param.put("ALERT_CAUSE", did[j].getAlertCause());
                    	param.put("checkDate", CheckTime);
                    	param.put("type", 5);//类型
                        dao.save("ResultMapper.saveDrugCheckInfo", param);
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
    
    /**
     *  Administration：类型为6
     */
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
            //setQueryCode("HisSysManager");
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
                    PageData param = new PageData();
                	param.put("ID", admin[j].getDrugUseDetail().getDRUG_USE_DETAIL_ID());
                	param.put("DRUG_ID1", admin[j].getDrug().getDRUG_NO_LOCAL());
                	param.put("REC_MAIN_NO1", admin[j].getRecMainNo());
                	param.put("REC_SUB_NO1",admin[j].getRecSubNo());
                	param.put("DRUG_ID2", null);
                	param.put("REC_MAIN_NO2",null);
                	param.put("REC_SUB_NO2", null);
                	param.put("NGROUPNUM", this.ngroupnum);
                	param.put("ALERT_HINT", null);
                	param.put("ALERT_LEVEL", admin[j].getAlertLevel());
                	param.put("ALERT_CAUSE", dud.getFORBID_CAUSE() + "," + dud.getADVERT_CAUSE() + "," + dud.getINADVIS_CAUSE());
                	param.put("checkDate", CheckTime);
                	param.put("type", 6);//类型
                    dao.save("ResultMapper.saveDrugCheckInfo", param);
                }
            }
        }
        catch(Exception e)
        {
            log.warn("[" + this.ngroupnum + "]------用药途径审查-------" + e.getMessage() +  e.toString());
            e.printStackTrace();
        }
    }

    /**
     *  过敏原：类型为7
     */
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
            //setQueryCode("HisSysManager");
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
                        PageData param = new PageData();
                    	param.put("ID", dagBean[x].getDRUG_ALLERGEN_ID());
                    	param.put("DRUG_ID1", dag[j].getDrug().getDRUG_NO_LOCAL());
                    	param.put("REC_MAIN_NO1", dag[j].getRecMainNo());
                    	param.put("REC_SUB_NO1",dag[j].getRecSubNo());
                    	param.put("DRUG_ID2", null);
                    	param.put("REC_MAIN_NO2",null);
                    	param.put("REC_SUB_NO2", null);
                    	param.put("NGROUPNUM", this.ngroupnum);
                    	param.put("ALERT_HINT", null);
                    	param.put("ALERT_LEVEL", dag[j].getAlertLevel());
                    	param.put("ALERT_CAUSE", null);
                    	param.put("checkDate", CheckTime);
                    	param.put("type", 7);//类型
                        dao.save("ResultMapper.saveDrugCheckInfo", param);
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 类型8
     * @param dsr
     */
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
            //setQueryCode("HisSysManager");
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
                    for(String value :t.getPerformInfo())
                    {
                        values.append(value).append(";");
                    }
                    PageData param = new PageData();
                	param.put("ID", UuidUtil.get32UUID());
                	param.put("DRUG_ID1", t.getPods().getDRUG_NO_LOCAL());
                	param.put("REC_MAIN_NO1", t.getRecMainNo());
                	param.put("REC_SUB_NO1",t.getRecSubNo());
                	param.put("DRUG_ID2", null);
                	param.put("REC_MAIN_NO2",null);
                	param.put("REC_SUB_NO2", null);
                	param.put("NGROUPNUM", this.ngroupnum);
                	param.put("ALERT_HINT", null);
                	param.put("ALERT_LEVEL", t.getAlertLevel());
                	param.put("ALERT_CAUSE", values.toString());
                	param.put("checkDate", CheckTime);
                	param.put("type", 8);//类型
                    dao.save("ResultMapper.saveDrugCheckInfo", param);
                }
            }
        }
        catch(Exception e)
        {
            log.warn("[" + this.ngroupnum + "]------药物剂量审查------" + e.getMessage() +  e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 类型 9
     * @param dsr
     */
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
                    PageData param = new PageData();
                	param.put("ID", dhf[j].getDrugSide().getSIDE_ID());
                	param.put("DRUG_ID1", dhf[j].getDrug().getDRUG_NO_LOCAL());
                	param.put("REC_MAIN_NO1", dhf[j].getRecMainNo());
                	param.put("REC_SUB_NO1",dhf[j].getRecSubNo());
                	param.put("DRUG_ID2", null);
                	param.put("REC_MAIN_NO2",null);
                	param.put("REC_SUB_NO2", null);
                	param.put("NGROUPNUM", this.ngroupnum);
                	param.put("ALERT_HINT", null);
                	param.put("ALERT_LEVEL", dhf[j].getAlertLevel());
                	param.put("ALERT_CAUSE", dhf[j].getDrugSide().getDIAGNOSIS_DESC());
                	param.put("checkDate", CheckTime);
                	param.put("type", 9);//类型
                    dao.save("ResultMapper.saveDrugCheckInfo", param);
                 }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            log.warn("[" + this.ngroupnum + "]----异常信号审查-----" + e.getMessage() + e.toString());
        }
    }

    /**
     * 类型
     * @param po
     * @param dsr
     */
    @Override
    public void saveDrugSecurityCheckInfo(TPatientOrder po,TDrugSecurityRslt dsr)
    {
        try
        {
            //setQueryCode("HisSysManager");
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
//            sql.append(" insert into DRUG_CHECKINFO_COLLECT(NGROUPNUM,NADVICENUM,NREDADVICEQUESSUM,NYELLOWADVICEQUESSUM");//批次号、医嘱数、红色问题医嘱数、黄色问题医嘱数
//            sql.append(",VDOCTORCODE,VDOCTORNAME");                                         //医生编码、医生姓名
//            sql.append(",VORGCODE,VORGNAME");                                               //组织编码、组织名称
//            sql.append(",NALLERGENRED,NALLERGENYELLOW");                                    //过敏红色、过敏黄色
//            sql.append(",NIVEFFECTRED,NIVEFFECTYELLOW");                                    //配伍红色、配伍黄色
//            sql.append(",NDIAGINFORED,NDIAGINFOYELLOW");                                    //禁忌红色、禁忌黄色
//            sql.append(",NINTERACTIONRED,NINTERACTIONYELLOW");                              //相互作用红色、相互作用黄色
//            sql.append(",NADMINISTRATIONRED,NADMINISTRATIONYELLOW");                        //用药途径红色，用药途径黄色
//            sql.append(",NDOSAGECHECKRED,NDOSAGECHECKYELLOW");                              //剂量红色,剂量黄色
//            sql.append(",NINGREDIENTRED,NINGREDIENTYELLOW");                                //重复成份红色,重复成分黄色
//            sql.append(",NOLDRED,NOLDYELLOW");                                              //老人红色,老人黄色 
//            sql.append(",NKIDRED,NKIDYELLOW");                                              //小孩红色,小孩黄色
//            sql.append(",NPREGNANTRED,NPREGNANTYELLOW");                                    //孕妇红色,孕妇黄色
//            sql.append(",NLACTATIONRED,NLACTATIONYELLOW");                                  //哺乳红色,哺乳黄色
//            sql.append(",NRENALRED,NRENALYELLOW");                                          //肾红色,肾黄色
//            sql.append(",NHEPATICALRED,NHEPATICALYELLOW");                                  //肝红色,肝黄色
            //sql.append(",NSIDERED,NSIDEYELLOW");                                          //异常信号红色,异常信号黄色
            
            PageData param = new PageData();
            param.put("NGROUPNUM", this.ngroupnum); 
            param.put("NADVICENUM", i); 
            param.put("NREDADVICEQUESSUM", redCount); 
            param.put("NYELLOWADVICEQUESSUM", yellowCount); 
            param.put("VDOCTORCODE", po.getDoctorID()); 
            param.put("VDOCTORNAME", po.getDoctorName()); 
            param.put("VORGCODE", po.getDoctorDeptID()); 
            param.put("VORGNAME", po.getDoctorDeptName()); 
            param.put("dagRedCount", dagRedCount); 
            param.put("dagYellowCount",dagYellowCount );
            param.put("dieRedCount",dieRedCount );
            param.put("dieYellowCount", dieYellowCount);
            param.put("ddiRedCount", ddiRedCount);
            param.put("ddiYellowCount", ddiYellowCount);
            param.put("diaRedCount", diaRedCount);
            param.put("diaYellowCount", diaYellowCount);
            param.put("admRedCount", admRedCount);
            param.put("admYellowCount", admYellowCount);
            param.put("ddgRedCount", ddgRedCount);
            param.put("ddgYellowCount", ddgYellowCount);
            param.put("didRedCount", didRedCount);
        	param.put("didYellowCount", didYellowCount);
        	param.put("NOLDRED", NOLDRED);
        	param.put("NOLDYELLOW", NOLDYELLOW);
        	param.put("NKIDRED",NKIDRED );
        	param.put("NKIDYELLOW",NKIDYELLOW );
        	param.put("NPREGNANTRED", NPREGNANTRED);
        	param.put("NPREGNANTYELLOW", NPREGNANTYELLOW);
        	param.put("NLACTATIONRED", NLACTATIONRED);
        	param.put("NLACTATIONYELLOW", NLACTATIONYELLOW);
        	param.put("NRENALRED", NRENALRED);
        	param.put("NRENALYELLOW", NRENALYELLOW);
        	param.put("NHEPATICALRED", NHEPATICALRED);
        	param.put("NHEPATICALYELLOW", NHEPATICALYELLOW);
        	param.put("CHECKDATE", DateUtil.fomatDate2(CheckTime));
            dao.save("ResultMapper.saveDrugCheckInfoCollection", param);
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
        //setQueryCode("HisSysManager");
        try
        {
            for(TAntiDrugSecurityResult ad : adsr)
            {
                TAntiDrugSecurityCheckResult[] ascrs = ad.getTAntiDrugSecurity();
                for(TAntiDrugSecurityCheckResult ascr :ascrs)
                {
                    for(TAntiDrugResult a :ascr.getAntiDrugResult())
                    {
//                        sql.append("insert into ANTIDRUG_CHECKRSLT(drug_id,rec_main_no,rec_sub_no ,ngroupnum,checkdate" +
//                        		",checktype,memo,dept_code,dept_name,doctor_code,doctor_name,result")
                        PageData param = new PageData();
                        param.put("drug_id", ascr.getDrug_ID()); 
                        param.put("rec_main_no", ascr.getOrder_No()); 
                        param.put("rec_sub_no", ascr.getOrder_Sub_No()); 
                        param.put("ngroupnum", this.ngroupnum); 
                        param.put("checkdate", CheckTime);
                        param.put("checktype", a.getCheckType());
                    	param.put("memo",a.getMemo() );
                    	param.put("dept_code", po.getDoctorDeptID());
                    	param.put("dept_name", po.getDoctorDeptName());
                    	param.put("doctor_code", po.getDoctorID());
                    	param.put("doctor_name", po.getDoctorName());
                    	param.put("result", a.isResult());
                        dao.save("ResultMapper.saveAntidrugCheckrslt", param);
                    }
                }
                
//                TDrugSecurityRslt dsr = new TDrugSecurityRslt();
//                TDrug drug = new TDrug();
//                drug.setDRUG_NO_LOCAL(ad.getDrug_ID());
//                drug.setRecMainNo(ad.getOrder_No());
//                drug.setRecSubNo(ad.getOrder_Sub_No());  
//                /*特殊人群*/
//                if(ad.getDrugSpecPeopleRslt()!= null && ad.getDrugSpecPeopleRslt().length != 0 && ad.getDrugSpecPeopleRslt()[0] != null)
//                {
//                    dsr.regDrugSpecPeopleCheckResult(drug, ad.getDrugSpecPeopleRslt()[0]);
//                }
//                if(ad.getAllergenRslt() != null && ad.getAllergenRslt().length != 0 && ad.getAllergenRslt()[0] != null)
//                {
//                    /* 药物过敏审查结果对象 */
//                    dsr.regAllergenCheckResult(drug, ad.getAllergenRslt()[0]);
//                }
//                if(ad.getDosageRslt() != null && ad.getDosageRslt().length != 0 && ad.getDosageRslt()[0] != null)
//                {
//                    /* 剂量 */
//                    dsr.regDosageCheckResult(drug, ad.getDosageRslt()[0]);
//                }
//                this.saveDrugSpecPeopleCheckInfo(dsr);
//                this.saveDrugAllergenCheckInfo(dsr);
//                this.saveDrugDosageCheckInfo(dsr);
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
        //setQueryCode("IAS");
        try
        {
            if(po.getPreveUseDrug() == null || po.getPreveUseDrug().length == 0 ) return ;
            for(TPreveUseDrug p : po.getPreveUseDrug())
            {
            	PageData param = new PageData();
                try
                {
                //String sql = " insert into ANTI_DRUG_USE_REC_YF(yf_id, patient_id, visit_id, dept_code, dept_name, doctor_name, name, sex, age, weight, drug_code, drug_name, dosage, dosage_units, administration, opertor_date, opertor_name, opertor_type, opertor_use_time, drug_use_time, gms, btgms, wxys, kndzbj, kljlanjl, yf_use_drug_yj, REC_DATE,tsyq) values (" +
                param.put("yf_id", this.ngroupnum); 
                param.put("patient_id", p.getPATIENT_ID()); 
                param.put("visit_id", p.getVISIT_ID()); 
                param.put("dept_code", p.getDEPT_CODE()); 
                param.put("dept_name", p.getDEPT_NAME());
                param.put("doctor_name", p.getDOCTOR_NAME());
            	param.put("name",p.getNAME());
            	param.put("sex", p.getSEX() );
            	param.put("age", p.getAGE());
            	param.put("weight", p.getWEIGHT() );
            	param.put("drug_code", p.getDRUG_CODE() );
            	param.put("drug_name", p.getDRUG_NAME() );
            	param.put("dosage", p.getDOSAGE());
            	param.put("dosage_units", p.getDOSAGE_UNITS());
            	param.put("administration", p.getADMINISTRATION());
            	param.put("opertor_date", DateUtil.fomatDate2(p.getOPERTOR_DATE()));
            	param.put("opertor_name", p.getOPERTOR_NAME());
            	param.put("opertor_type", p.getOPERTOR_TYPE());
            	param.put("opertor_use_time", p.getOPERTOR_USE_TIME());
            	param.put("drug_use_time", p.getDRUG_USE_TIME());
            	param.put("gms", p.getGMS()  );
            	param.put("btgms", p.getBTGMS());
            	param.put("wxys", p.getWXYS());
            	param.put("kndzbj", p.getKNDZBJ());
            	param.put("kljlanjl", p.getKLJLANJL() );
            	param.put("yf_use_drug_yj",p.getYF_USE_DRUG_YJ());
            	param.put("REC_DATE", p.getREC_DATE() );
            	param.put("tsyq", p.getTSYQ()  );
                dao.save("ResultMapper.saveAntiDrugUseRecYf", param);
                
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    log.warn("[" + this.ngroupnum + "] -----预防用药信息保存----[ResultMapper.saveAntiDrugUseRecYf]");
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
        //setQueryCode("IAS");
        try
        {
            if(po.getTreatUseDrug() == null || po.getTreatUseDrug().length == 0 ) return ;
            for(TTreatUseDrug p : po.getTreatUseDrug())
            {
            	PageData param = new PageData();
            	try{
            		//String sql = " insert into ANTI_DRUG_USE_REC_ZL(zl_id, patient_id, visit_id, dept_code, dept_name, doctor_name, name, sex, age, weight, yyly, zlfl, grbw, zdyj, kndzbj, drug_code, drug_name, dosage, dosage_units, administration, rec_date, twdate, twvalue, twok, xxdate, xxvalue, cvalue, xxok) values (" +
	                param.put("zl_id", this.ngroupnum  );
	                param.put("patient_id", p.getPATIENT_ID()  );
	                param.put("visit_id", p.getVISIT_ID()  );
	                param.put("dept_code", p.getDEPT_CODE() );
	                param.put("dept_name",  p.getDEPT_NAME()   );
	                param.put("doctor_name", p.getDOCTOR_NAME()  );
	                param.put("name", p.getNAME()   );
	                param.put("sex", p.getSEX()  );
	                param.put("age", p.getAGE()   );
	                param.put("weight", p.getWEIGHT() );
	                param.put("yyly", p.getYYLY()   );
	                param.put("zlfl", p.getZLFL()  );
	                param.put("grbw", p.getGRBW()  );
	                param.put("zdyj", p.getZDYJ()  );
	                param.put("kndzbj", p.getKNDZBJ()   );
	                param.put("drug_code", p.getDRUG_CODE() );
	                param.put("drug_name", p.getDRUG_NAME()   );
	                param.put("dosage", p.getDOSAGE() );
	                param.put("dosage_units", p.getDOSAGE_UNITS()  );
	                param.put("administration", p.getADMINISTRATION() );
	                param.put("rec_date", p.getREC_DATE() );
	                param.put("twdate", p.getTWDATE()   );
	                param.put("twvalue", p.getTWVALUE() );
	                param.put("twok", p.getTWOK()   );
	            	param.put("xxdate", p.getXXDATE()   );
	            	param.put("xxvalue", p.getXXVALUE()  );
	            	param.put("cvalue", p.getCVALUE()   );
	            	param.put("xxok", p.getXXOK()  );
	                dao.save("ResultMapper.saveAntiDrugUseRecZl", param);
                }
                catch(Exception e )
                {
                    e.printStackTrace();
                    log.warn("[" + this.ngroupnum + "] -----治疗用药信息保存----[ResultMapper.saveAntiDrugUseRecZl]");
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
