package com.ts.FetcherHander.InHospital.Check.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

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
import com.ts.FetcherHander.InHospital.Check.InHospitalCheck;
import com.ts.dao.DAO;
import com.ts.entity.pdss.SaveER.SaveOperaction;
import com.ts.entity.pdss.Saver.QueueBean;
import com.ts.entity.pdss.Saver.SaveBeanRS;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.interceptor.webservice.ApplicationProperties;
import com.ts.service.pdss.pdss.manager.IDrugSecurityChecker;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;

import net.sf.json.JSONObject;

/**
 * 住院
 * @author autumn
 *
 */
@Service
@Transactional
public class InHospitalCheckBean implements InHospitalCheck
{
    //数据检索
//    private DAO dao = (DaoSupportPH)SpringBeanUtil.getBean("daoSupportPH");
    @Resource(name="drugSecurityChecker")
    private IDrugSecurityChecker drugSecuity;
    @Resource(name="daoSupportPH")
    private DAO dao;
    private static final Logger logger = Logger.getLogger("InHospitalCheckBean");
    
    @SuppressWarnings ("unchecked")
    @Override
    public Object InHospitalCheck(Object... values)
    {
        String dateTime = values[0].toString();
        //医嘱抗菌药专项点评
        OrderAntiCheck(dateTime);
        //医嘱点评
        OrderCheck(dateTime);
        return true;
    }

    /** 抗菌专项点评
     * @param dateTime
     */
    private void OrderAntiCheck(String dateTime)
    {
        Calendar cal = DateUtils.getCalendar(dateTime);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime2 = sdf.format(cal.getTime());
        //药品审查 引擎
//        IDrugSecurityChecker drugSecuity = (IDrugSecurityChecker)SpringBeanUtil.getBean("drugSecurityChecker");
        //医嘱点评集合
        List<PageData> pdOut = new ArrayList<PageData>();
        PageData pdInp = new PageData();
        pdInp.put("beginDate", dateTime);
        pdInp.put("endDate", dateTime2);
        try
        {
            // 删除 审核结果 
            dao.delete("PatVisitAntiMapper.deleteDrugCheckRslt", pdInp);
            dao.delete("PatVisitAntiMapper.reSetCheckRslt", pdInp);
            List<PageData> pdPatVisits = (List<PageData>)dao.findForList("PatVisitAntiMapper.queryPatVisitByDate", pdInp);
            // 判断审核是否有问题 false为存在问题， true：不存在问题。 
            boolean checkFlag = false;
            //判断审核药品中是否存在 抗拒药物，  true 存在抗菌药， false不存在抗菌药
            boolean checkAntiFlag = false;
            for(PageData pd : pdPatVisits){
                checkFlag = true;
                checkAntiFlag =  true;
                String uuid = UUID.randomUUID().toString();
                //需要将病人每天进行药品审核，但是如果有重复的药品审核  初步想法是删除。
                List<PageData> listPd = (List<PageData>)dao.findForList("OrdersMapper.ordersListByDateAndOrderClass", pd);
                TPatientOrder po = new TPatientOrder();
                //4 ： 标示为医嘱点评  
                po.setPatType("4");
                //增加基本信息  TODO 目前没有增加手术部分，后续需要考虑 
                this.setPatientOrder(po, pd); 
                String date = null;
                String checkLvl = "";
                List<PageData> orderDrug = new ArrayList<PageData>();
                for(PageData pdOrder : listPd){
                    if(date != null  && !pdOrder.getString("datestr").equals(date)){
                        this.setOrderDrug(po, orderDrug);
                        TDrugSecurityRslt dsr = drugSecuity.DrugSecurityCheck(po);
                        orderDrug = new ArrayList<PageData>();
                        SaveBeanRS saveBeanRs = new SaveBeanRS(uuid);
                        TCheckResultCollection checkRC = new TCheckResultCollection();
                        if(dsr.getCheckResults().length == 0) continue;
                        // 检索是否存在抗菌药。
                        for(TDrug tdrug :dsr.getDrugs())
                        {
                            if(tdrug.isAnti()) {
                                //存在抗菌药物
                                checkAntiFlag = true;
                                break;
                            }
                            //不存在抗菌药物
                            checkAntiFlag = false;
                        }
                        // 不存在抗菌药 跳过。    
                        if(!checkAntiFlag) continue;
                        checkLvl = dsr.getResultLevelByAnti();
                        checkFlag = false;
                        checkRC.setDsr(dsr);
                        saveBeanRs.setCheckRC(checkRC);
                        saveBeanRs.setPo(po);
                        //加入列队中进行保存
                        QueueBean.setSaveBeanRS(saveBeanRs);
                        logger.debug("抗菌药医嘱日期:" + pdOrder.getString("datestr") + ",药疗医嘱数：" + orderDrug.size());
                    }
                    date = pdOrder.getString("datestr");
                    orderDrug.add(pdOrder);
                }
                if(checkFlag) continue;
                //审核组
                String groupNum  = uuid;
                PageData entity = new PageData();
                entity.put("NGROUPNUM", groupNum);
                entity.put("PATIENT_ID", pd.getString("PATIENT_ID"));
                entity.put("VISIT_ID", pd.getString("VISIT_ID"));
                //只有觉得出现问题单 R 级别  视为不合理， 否则有些是 慎用 也所谓不合理 显然是 不合适的。
                entity.put("ISCHECKTRUE", "R".equals(checkLvl)?"1":"2");
                pdOut.add(entity);
            }
            // 批量保存数据 
            dao.batchUpdate("PatVisitAntiMapper.UpdatePatVisitNgroupnum", pdOut);
            
            setDrugAntiCheckRS(pdInp);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 正常医嘱点评
     * @param dateTime
     */
    private void OrderCheck(String dateTime)
    {
        Calendar cal = DateUtils.getCalendar(dateTime);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime2 = sdf.format(cal.getTime());
        //药品审查 引擎
//        IDrugSecurityChecker drugSecuity = (IDrugSecurityChecker)SpringBeanUtil.getBean("drugSecurityChecker");
        //医嘱点评集合
        List<PageData> pdOut = new ArrayList<PageData>();
        PageData pdInp = new PageData();
        pdInp.put("beginDate", dateTime);
        pdInp.put("endDate", dateTime2);
        try
        {
            // 删除 审核结果 
            dao.delete("PatVisitMapper.deleteDrugCheckRslt", pdInp);
            dao.delete("PatVisitMapper.reSetCheckRslt", pdInp);
            List<PageData> pdPatVisits = (List<PageData>)dao.findForList("PatVisitMapper.queryPatVisitByDate", pdInp);
            // 判断审核是否有问题 false为存在问题， true：不存在问题。 
            boolean checkFlag = false;
            for(PageData pd : pdPatVisits){
                checkFlag = true;
                String uuid = UUID.randomUUID().toString();
                //需要将病人每天进行药品审核，但是如果有重复的药品审核  初步想法是删除。
                List<PageData> listPd = (List<PageData>)dao.findForList("OrdersMapper.ordersListByDateAndOrderClass", pd);
                TPatientOrder po = new TPatientOrder();
                //4 ： 标示为医嘱点评  
                po.setPatType("4");
                //增加基本信息  TODO 目前没有增加手术部分，后续需要考虑 
                this.setPatientOrder(po, pd); 
                String date = null;
                String checkLvl = "";
                List<PageData> orderDrug = new ArrayList<PageData>();
                for(PageData pdOrder : listPd){
                    if(date != null  && !pdOrder.getString("datestr").equals(date)){
                        this.setOrderDrug(po, orderDrug);
                        TDrugSecurityRslt dsr = drugSecuity.DrugSecurityCheck(po);
                        orderDrug = new ArrayList<PageData>();
                        SaveBeanRS saveBeanRs = new SaveBeanRS(uuid);
                        TCheckResultCollection checkRC = new TCheckResultCollection();
                        if(dsr.getCheckResults().length == 0) continue;
                        checkLvl = dsr.getResultLevel();
                        checkFlag = false;
                        checkRC.setDsr(dsr);
                        saveBeanRs.setCheckRC(checkRC);
                        saveBeanRs.setPo(po);
                        //加入列队中进行保存
                        QueueBean.setSaveBeanRS(saveBeanRs);
                        logger.debug("医嘱日期:" + pdOrder.getString("datestr") + ",药疗医嘱数：" + orderDrug.size());
                    }
                    date = pdOrder.getString("datestr");
                    orderDrug.add(pdOrder);
                }
                if(checkFlag) continue;
                //审核组
                String groupNum  = uuid;
                PageData entity = new PageData();
                entity.put("NGROUPNUM", groupNum);
                entity.put("PATIENT_ID", pd.getString("PATIENT_ID"));
                entity.put("VISIT_ID", pd.getString("VISIT_ID"));
                //只有觉得出现问题单 R 级别  视为不合理， 否则有些是 慎用 也所谓不合理 显然是 不合适的。
                entity.put("ISCHECKTRUE", "R".equals(checkLvl)?"1":"2");
                pdOut.add(entity);
            }
            // 批量保存数据 
            dao.batchUpdate("PatVisitMapper.UpdatePatVisitNgroupnum", pdOut);
            
            setDrugCheckRS(pdInp);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 抗菌药物
     * @param pdInp
     * @throws Exception
     */
    private void setDrugAntiCheckRS(PageData pdInp) throws Exception
    {
        while(true)
        {
            if(QueueBean.getSaveBeanRSSize() == 0)
            {
                try
                {
                    logger.debug("等待8秒，去除重复抗菌药品审核结果");
                    Thread.sleep(8000);    
                }
                catch(Exception e )
                {
                    e.printStackTrace();
                }
                
                // 审核结果去重处理 
                List<PageData> checkRs = (List<PageData>)dao.findForList("PatVisitAntiMapper.queryGroupDrugCheckRs", pdInp);
                // 删除 审核结果  为了后面去重复为主
                dao.delete("PatVisitAntiMapper.deleteDrugCheckRslt", pdInp);
                for(PageData pd : checkRs){
                    pd.put("RS_ID",UuidUtil.get32UUID());
                    pd.put("checkDate", DateUtils.formatDate(DateUtils.FORMAT_DATETIME));
                    dao.save("rsDrugCheckRsltMapper.saveCheckRS", pd);
                }    
                return ;
            }
            try
            {
                Thread.sleep(5000);    
            }
            catch(Exception e )
            {
                e.printStackTrace();
            }
            
        }
    }
    
    /**
     * 正常医嘱点评
     * @param pdInp
     * @throws Exception
     */
    private void setDrugCheckRS(PageData pdInp) throws Exception
    {
        while(true)
        {
            if(QueueBean.getSaveBeanRSSize() == 0)
            {
                try
                {
                    logger.debug("等待8秒，去除重复药品审核结果");
                    Thread.sleep(8000);    
                }
                catch(Exception e )
                {
                    e.printStackTrace();
                }
                
                // 审核结果去重处理 
                List<PageData> checkRs = (List<PageData>)dao.findForList("PatVisitMapper.queryGroupDrugCheckRs", pdInp);
                // 删除 审核结果  为了后面去重复为主
                dao.delete("PatVisitMapper.deleteDrugCheckRslt", pdInp);
                for(PageData pd : checkRs){
                    pd.put("RS_ID",UuidUtil.get32UUID());
                    pd.put("checkDate", DateUtils.formatDate(DateUtils.FORMAT_DATETIME));
                    dao.save("rsDrugCheckRsltMapper.saveCheckRS", pd);
                }    
                return ;
            }
            try
            {
                Thread.sleep(5000);    
            }
            catch(Exception e )
            {
                e.printStackTrace();
            }
            
        }
    }

    /**
     * 设置基本信息
     * @param po
     * @param pd
     */
    private  void setPatientOrder(TPatientOrder po ,PageData pd) throws Exception{
        
        po.setDoctorDeptID(pd.getString(""));
        po.setDoctorDeptName(pd.getString("DEPT_DISCHARGE_FROM"));
        po.setDoctorID(pd.getString(""));
        po.setDoctorName(pd.getString("ATTENDING_DOCTOR"));

        // 设置 医生职称。
        DictCache dictCache =  DictCache.getNewInstance();
        TCommonRecord doctor = dictCache.getDoctorInfo(po.getDoctorName());
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
        
        TPatient patient = new TPatient();
        patient.setName(pd.getString("NAME"));
        patient.setDateOfBirth(pd.get("DATE_OF_BIRTH").toString());
        patient.setSex(pd.getString("SEX"));
        po.setPatient(patient );
        po.setPatientID(pd.getString("PATIENT_ID"));
        TPatOrderInfoExt patInfoExt  = new TPatOrderInfoExt();
        // 是否哺乳期
        patInfoExt.setIsLact(pd.getString(pd.getString("")));
        // 是否孕妇 
        patInfoExt.setIsPregnant(pd.getString(pd.getString("")));;
        /* 肝功能不完全标志  */
        patInfoExt.setIsLiverWhole(pd.getString(pd.getString("")));;
        /* 肾功能不完全标志  */
        patInfoExt.setIsKidneyWhole(pd.getString(pd.getString("")));
        po.setPatInfoExt(patInfoExt  );
        List<PageData> diagPd = (List<PageData>)dao.findForList("PatVisitMapper.queryDiagnosisByPatVisist", pd);
        List<TPatOrderDiagnosis> patOrderDiagnosiss =  new ArrayList<TPatOrderDiagnosis>();
        for(PageData diag : diagPd){
            TPatOrderDiagnosis d = new TPatOrderDiagnosis();
            d.setDiagnosisDictID(diag.getString("DIAGNOSIS_DESC"));
            d.setDiagnosisName(diag.getString("DIAGNOSIS_DESC"));
            patOrderDiagnosiss.add(d);
        }
        po.setPatOrderDiagnosiss(patOrderDiagnosiss.toArray(new TPatOrderDiagnosis[0]));  
        TPatOrderVisitInfo patVisitInfo = new TPatOrderVisitInfo();
        patVisitInfo.setVisitID(pd.getObjectString("VISIT_ID"));
        patVisitInfo.setPatientID(pd.getString("PATIENT_ID"));
        patVisitInfo.setInDept(pd.getString("DEPT_ADMISSION_TO"));
        patVisitInfo.setInDate(pd.get("ADMISSION_DATE_TIME").toString());
        patVisitInfo.setOutDept(pd.getString("DEPT_DISCHARGE_FROM"));
        // 主治医生 
        patVisitInfo.setMainDoctor(pd.getString("ATTENDING_DOCTOR"));
        // 门诊医生 
        patVisitInfo.setConsultingDoctor(pd.getString("DOCTOR_IN_CHARGE"));
        //经治医生
        patVisitInfo.setOtherDoctor(pd.getString("DOCTOR_IN_CHARGE")); 
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
            try
            {
                TPatOrderDrug pod = new TPatOrderDrug();
                // 药品ID
                pod.setDrugID(pd.getString("order_code"));
                /* 药品名称 */
                pod.setDrugName(pd.getString("order_text"));
                // 医嘱序号
                pod.setRecMainNo(pd.getString("ORDER_NO").toString());
                // 医嘱子序号
                pod.setRecSubNo(pd.getString("ORDER_SUB_NO").toString());
                // 一次使用剂量
                pod.setDosage(pd.getString("DOSAGE").toString());
                // 剂量单位
                pod.setDoseUnits(pd.getString("DOSAGE_UNITS"));
                // 给药途径ID
                pod.setAdministrationID(pd.getString("ADMINISTRATION"));
                String frequency = pd.getString("FREQUENCY");
                if("".equals(pd.getString("FREQUENCY")))
                {
                    frequency = pd.getString("FREQ_COUNTER").toString() + "/" + pd.getString("FREQ_INTERVAL").toString() + pd.getString("FREQ_INTERVAL_UNIT");
                }
                // 执行频率ID
                pod.setPerformFreqDictID(frequency);
                // 执行执行频率文本(输入执行频率描述)
                pod.setPerformFreqDictText(frequency);
                // 开始日期时间
                pod.setStartDateTime(pd.getString("START_DATE_TIME").toString());
                // 结束日期时间
                pod.setStopDateTime(pd.getString("STOP_DATE_TIME").toString());
                // 开具医嘱科室
                pod.setDoctorDept(pd.getString("ORDERING_DEPT"));
                // 开具医嘱医生
                pod.setDoctor(pd.getString("DOCTOR"));
                // 是否组
//                pod.setIsGroup(pd.getString("PRESC_NO"));
                // 药品厂商ID
//                pod.setFirmID(pd.getString("FIRM_ID"));
                // 是否为新医嘱
//                pod.setIsNew(pd.getString("TOXIPROPERTY"));
                // 确认时间
//                pod.setDvaliddate(DateUtils.getDate());
                /* 给药途径名称 */
                pod.setAdminName(pd.getString("ADMINISTRATION"));
                patOrderDrugs.add(pod);
            }
            catch(Exception e )
            {
                e.printStackTrace();
                logger.debug(InHospitalCheck.class.getName() +"加载药品出现异常:" + e.getMessage());
            }
            
        }
        po.setPatOrderDrugs(patOrderDrugs.toArray(new TPatOrderDrug[0]));
    }
}
