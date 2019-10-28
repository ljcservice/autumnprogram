package com.ts.FetcherHander.OutPatient.Check.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderInfoExt;
import com.hitzd.his.Beans.TPatOrderVisitInfo;
import com.hitzd.his.Beans.TPatient;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.Utils.DrugUtils;
import com.ts.FetcherHander.InHospital.Check.Impl.InHospitalCheckBean;
import com.ts.FetcherHander.OutPatient.Check.IOutPatientCheck;
import com.ts.dao.DAO;
import com.ts.entity.pdss.Saver.QueueBean;
import com.ts.entity.pdss.Saver.SaveBeanRS;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResult;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.interceptor.webservice.ApplicationProperties;
import com.ts.service.pdss.pdss.manager.IDrugSecurityChecker;
import com.ts.util.PageData;

import net.sf.json.JSONObject;


/**
 * 门诊处方点评审核
 * @author autumn
 */
@Service
@Transactional
public class OutPatientCheckBean implements IOutPatientCheck
{
    @Resource(name="drugSecurityChecker")
    IDrugSecurityChecker drugSecuity;
    @Resource(name="daoSupportPH")
    DAO dao;
    
    private static final Logger logger = Logger.getLogger(OutPatientCheckBean.class);

    @SuppressWarnings ("unchecked")
    @Override
    public Object OutPatientCheck(Object... value)
    {
        String dateTime =  value[0].toString();
        //数据检索
//        DAO dao = (DaoSupportPH)SpringBeanUtil.getBean("daoSupportPH");
        //药品审查 引擎
//        IDrugSecurityChecker drugSecuity = (IDrugSecurityChecker)SpringBeanUtil.getBean("drugSecurityChecker");
        // 普通处方
        CheckOutPresc(dateTime);
        // 抗菌药物处方
        CheckOutPrescAnti(dateTime);
        return null;
    }

    /**
     * 抗菌药物专项点评审核
     * @param dateTime
     */
    private void CheckOutPrescAnti(String dateTime) 
    {
    	
    	List<PageData> pdOut = new ArrayList<PageData>();
        PageData pdInp = new PageData();
        pdInp.put("ORDER_DATE", dateTime);
        try
        {
            dao.delete("PrescAntiMapper.deleteDrugCheckRslt", pdInp);
            dao.delete("PrescAntiMapper.reSetCheckRslt", pdInp);
            List<PageData> pds = (List<PageData>)dao.findForList("PrescAntiMapper.findByDate", pdInp);
            String checkLvl ="";
            for(PageData pd : pds)
            {
                TPatientOrder po = new TPatientOrder();
                //患者类型
                po.setPatType("5");
                String id = pd.getString("id");
                List<PageData>  details = (List<PageData>)dao.findForList("PrescAntiMapper.findPrescDetailById", id);
                this.setPatientOrder(po, pd);
                this.setOrderDrug(po, details);
                TDrugSecurityRslt dsr = drugSecuity.DrugSecurityCheck(po);
                SaveBeanRS saveRs = new SaveBeanRS();
                TCheckResultCollection checkRC = new TCheckResultCollection();
                if(dsr.getCheckResults().length == 0) continue;
                // 判断是否存在抗菌药物专项不合理情况。
                TDrug[] drugs = dsr.getDrugs();
                boolean antiFlag = false;
                for(TDrug entity : drugs)
                {
                	if(DrugUtils.isKJDrug(entity.getDRUG_NO_LOCAL()))
                	{
                		antiFlag = true;
                		break;
                	}
                }
                // 如果不存在抗菌药物 则跳过本次保存
                if(!antiFlag) continue;
                checkLvl = dsr.getResultLevel();
                checkRC.setDsr(dsr);
                saveRs.setCheckRC(checkRC);
                saveRs.setPo(po);
                //加入列队中，进行保存
                QueueBean.setSaveBeanRS(saveRs);
                //审核组
                String groupNum  = saveRs.getID();
                PageData entity = new PageData();
                entity.put("NGROUPNUM", groupNum);
                entity.put("ID", id);
                //只有觉得出现问题单 R 级别  视为不合理， 否则有些是 慎用 也所谓不合理 显然是 不合适的。
                entity.put("ISCHECKTRUE", "R".equals(checkLvl)?"1":"2");
                pdOut.add(entity);
            }
            // 批量保存数据
            if(pdOut.size() > 0 ) dao.batchUpdate("PrescAntiMapper.updatePrescNgroupnum", pdOut);
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 普通全处方审核
     * @param dateTime
     */
	private void CheckOutPresc(String dateTime) {
		List<PageData> pdOut = new ArrayList<PageData>();
        PageData pdInp = new PageData();
        pdInp.put("ORDER_DATE", dateTime);
        try
        {
            dao.delete("PrescMapper.deleteDrugCheckRslt", pdInp);
            dao.delete("PrescMapper.reSetCheckRslt", pdInp);
            List<PageData> pds = (List<PageData>)dao.findForList("PrescMapper.findByDate", pdInp);
            String checkLvl ="";
            for(PageData pd : pds)
            {
                TPatientOrder po = new TPatientOrder();
                //患者类型
                po.setPatType("2");
                String id = pd.getString("id");
                List<PageData>  details = (List<PageData>)dao.findForList("PrescMapper.findPrescDetailById", id);
                this.setPatientOrder(po, pd);
                this.setOrderDrug(po, details);
                TDrugSecurityRslt dsr = drugSecuity.DrugSecurityCheck(po);
                SaveBeanRS saveRs = new SaveBeanRS();
                TCheckResultCollection checkRC = new TCheckResultCollection();
                if(dsr.getCheckResults().length == 0) continue;
                checkLvl = dsr.getResultLevel();
                checkRC.setDsr(dsr);
                saveRs.setCheckRC(checkRC);
                saveRs.setPo(po);
                //加入列队中，进行保存
                QueueBean.setSaveBeanRS(saveRs);
                //审核组
                String groupNum  = saveRs.getID();
                PageData entity = new PageData();
                entity.put("NGROUPNUM", groupNum);
                entity.put("ID", id);
                //只有觉得出现问题单 R 级别  视为不合理， 否则有些是 慎用 也所谓不合理 显然是 不合适的。
                entity.put("ISCHECKTRUE", "R".equals(checkLvl)?"1":"2");
                pdOut.add(entity);
            }
            // 批量保存数据
            if(pdOut.size() > 0 ) dao.batchUpdate("PrescMapper.updatePrescNgroupnum", pdOut);
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
	}

    /**
     * 设置基本信息
     * @param po
     * @param pd
     */
    private  void setPatientOrder(TPatientOrder po ,PageData pd){
        po.setDoctorDeptName(pd.getString("ORG_CODE"));
        po.setDoctorDeptID(pd.getString("ORG_NAME"));
        po.setDoctorName(pd.getString("DOCTOR_NAME"));
        // 设置 医生职称。
        DictCache dictCache =  DictCache.getNewInstance();
        TCommonRecord doctor = dictCache.getDoctorInfo(pd.getString("DOCTOR_NAME"));
        String title = doctor.get("title");
        String json = ApplicationProperties.getPropertyValue("doctorTile");
        JSONObject  jsonObject = JSONObject.fromObject(json);

        if(jsonObject.getString("1").indexOf(title.trim()) != -1)
        {
            po.setDoctorTitleID("1");
            po.setDoctorTitleName(title);
        }else if(jsonObject.getString("2").indexOf(title.trim()) != -1)
        {
            po.setDoctorTitleID("2");
            po.setDoctorTitleName(title);
        }else
        {
            po.setDoctorTitleID("3");
            po.setDoctorTitleName(title);
        }
        po.setPatientID(pd.getString("PATIENT_ID"));
        TPatient patient = new TPatient();
        patient.setName(pd.getString("PATIENT_NAME"));
        patient.setSex(pd.getString("PATIENT_SEX"));
        patient.setDateOfBirth(pd.getString("PATIENT_BIRTH").toString());
        po.setPatient(patient );
        TPatOrderInfoExt patInfoExt = new TPatOrderInfoExt();
        // 是否哺乳期
        patInfoExt.setIsLact(pd.getString(""));
        // 是否孕妇 
        patInfoExt.setIsPregnant(pd.getString(""));;
        /* 肝功能不完全标志  */
        patInfoExt.setIsLiverWhole(pd.getString(""));;
        /* 肾功能不完全标志  */
        patInfoExt.setIsKidneyWhole(pd.getString(""));
        po.setPatInfoExt(patInfoExt );
        // 诊断信息
        //String[] diagCode = pd.getString("DIAGNOSIS_CODES").split(";");
        String[] diagName = pd.getString("DIAGNOSIS_NAMES").split(";");
        List<TPatOrderDiagnosis> patOrderDiagnosiss = new ArrayList<TPatOrderDiagnosis>();
        for(int i = 0 ; i < diagName.length ; i++)
        {
            TPatOrderDiagnosis diag = new TPatOrderDiagnosis();
            //diag.setDiagnosisDictID(diagCode[i]);
            diag.setDiagnosisName(diagName[i]);
            patOrderDiagnosiss.add(diag);
        }
        po.setPatOrderDiagnosiss(patOrderDiagnosiss.toArray(new TPatOrderDiagnosis[0]));
        
        TPatOrderVisitInfo patVisitInfo = new TPatOrderVisitInfo();
        patVisitInfo.setVisitID(pd.get("VISIT_NO").toString());
        patVisitInfo.setPatientID(pd.getString("PATIENT_ID"));
        patVisitInfo.setInDept(pd.getString("ORG_CODE"));
        patVisitInfo.setInDate(pd.getString("ORDER_DATE"));
        // 主治医生 
        patVisitInfo.setMainDoctor(pd.getString("DOCTOR_NAME"));
        // 门诊医生 
        patVisitInfo.setConsultingDoctor(pd.getString("DOCTOR_NAME"));
        //经治医生
        patVisitInfo.setOtherDoctor(pd.getString("DOCTOR_NAME"));
        po.setPatVisitInfo(patVisitInfo );
    }
    
    /**
     * 设置药品信息 
     * @param po
     * @param pd
     */
    private void setOrderDrug(TPatientOrder po , List<PageData> pds)
    {
        List<TPatOrderDrug> patOrderDrugs = new ArrayList<>();
        for(PageData pd : pds)
        {
            TPatOrderDrug pod = new TPatOrderDrug();
            // 药品ID
            pod.setDrugID(pd.getString("DRUG_CODE"));
            /* 药品名称 */
            pod.setDrugName(pd.getString("DRUG_NAME"));
            // 医嘱序号
            pod.setRecMainNo(pd.getString("ORDER_NO"));
            // 医嘱子序号
            pod.setRecSubNo(pd.getString("ORDER_SUB_NO"));
            // 一次使用剂量
            pod.setDosage(pd.getString("DOSAGE"));
            // 剂量单位
            pod.setDoseUnits(pd.getString("DOSAGE_UNITS"));
            // 给药途径ID
            pod.setAdministrationID(pd.getString("ADMINISTRATION"));
            // 执行频率ID
            pod.setPerformFreqDictID(pd.getString("FREQUENCY"));
            // 执行执行频率文本(输入执行频率描述)
            pod.setPerformFreqDictText(pd.getString("FREQUENCY"));
            // 开始日期时间
            pod.setStartDateTime(pd.getString("AMOUNT"));
            // 结束日期时间
            pod.setStopDateTime(pd.getString("COSTS"));
            // 开具医嘱科室
            pod.setDoctorDept(pd.getString("DRUG_SPEC"));
            // 开具医嘱医生
            pod.setDoctor(pd.getString("UNITS"));
            // 是否组
            pod.setIsGroup(pd.getString("PRESC_NO"));
            // 药品厂商ID
            pod.setFirmID(pd.getString("FIRM_ID"));
            // 是否为新医嘱
            pod.setIsNew(pd.getString("TOXIPROPERTY"));
            // 确认时间
            pod.setDvaliddate(DateUtils.getDate());
            /* 给药途径名称 */
            pod.setAdminName(pd.getString("ADMINISTRATION"));
            patOrderDrugs.add(pod);
        }
        po.setPatOrderDrugs(patOrderDrugs.toArray(new TPatOrderDrug[0]));
    }
}
