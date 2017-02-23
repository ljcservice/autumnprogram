package com.ts.service.pdss.peaas.impl;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;
import com.ts.entity.pdss.pdss.RSBeans.TDrugSecurityRslt;
import com.ts.entity.pdss.peaas.Beans.TPrescDetailBean;
import com.ts.entity.pdss.peaas.Beans.TPrescMasterBean;
import com.ts.entity.pdss.peaas.Beans.TPrescPatMasterBean;
import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;
import com.ts.service.pdss.pdss.Utils.CommonUtils;
import com.ts.service.pdss.pdss.manager.IDrugSecurityChecker;
import com.ts.service.pdss.pdss.manager.IPatientSaveCheckResult;
import com.ts.service.pdss.peaas.manager.IPrescCheckerService;
import com.ts.service.pdss.peaas.manager.IPrescDoctorChecker;

@Service
@Transactional
public class PrescDoctorCheckerBean extends Persistent4DB implements IPrescDoctorChecker
{
    private static List<String> units = new LinkedList<String>();
    static
    {
       units.add("g");units.add("mg");units.add("μg");units.add("n");units.add("l");units.add("ml");units.add("iu");units.add("u");
       units.add("片");units.add("丸");units.add("粒");units.add("袋");units.add("支");units.add("瓶");units.add("盒");
    }
    @Override
    public TPrescCheckRslt PrescCheck(String[] PatMaster,String[] PrescInfo, String[][] PrescDetail, String[][] PrescDiag)
    {
//        TPrescCheckRslt PCRS = new TPrescCheckRslt();
//        TPrescPatMasterBean ppmb = CommonUtilsPeaas.getPrescPatMasterBean(PatMaster, PrescInfo, PrescDetail, PrescDiag);
        return null;
    }
    
    /**
     * 第十七条 有下列情况之一的，应当判定为不规范处方： 　　
     * （一）    处方的前记、正文、后记内容缺项，书写不规范或者字迹难以辨认的； 　　
     * （二）    医师签名、签章不规范或者与签名、签章的留样不一致的； 　　
     * （三）     药师未对处方进行适宜性审核的（处方后记的审核、调配、核对、发药栏目无审核调配药师及核对发药药师签名，或者单人值班调剂未执行双签名规定）； 　　
     * （四）     新生儿、婴幼儿处方未写明日、月龄的； 　　
     * （五）     西药、中成药与中药饮片未分别开具处方的； 　　
     * （六）     未使用药品规范名称开具处方的； 　　
     * （七）     药品的剂量、规格、数量、单位等书写不规范或不清楚的； 　　
     * （八）     用法、用量使用“遵医嘱”、“自用”等含糊不清字句的； 　　
     * （九）     处方修改未签名并注明修改日期，或药品超剂量使用未注明原因和再次签名的； 　　
     * （十）     开具处方未写临床诊断或临床诊断书写不全的； 　　
     * （十一）单张门急诊处方超过五种药品的； 　　
     * （十二）无特殊情况下，门诊处方超过7日用量，急诊处方超过3日用量，慢性病、老年病或特殊情况下需要适当延长处方用量未注明理由的； 　　
     * （十三）开具麻醉药品、精神药品、医疗用毒性药品、放射性药品等特殊管理药品处方未执行国家有关规定的； 　　
     * （十四）医师未按照抗菌药物临床应用管理规定开具抗菌药物处方的; 　　
     * （十五）中药饮片处方药物未按照“君、臣、佐、使”的顺序排列，或未按要求标注药物调剂、煎煮等特殊要求的。
    */
    private TCommonRecord PrescNoNormCheck(TCommonRecord p , List<TCommonRecord> presc)
    {
        TCommonRecord tCom = new TCommonRecord();
        /* 记录处方药品数 */
        int prescDrugCount = 0 ; 
        /* 1-7 用法有误 */
        boolean dosageUnit = true;
        /* 1-12 无特殊情况下 */
        List<TCommonRecord> listTcom1_12 = new ArrayList<TCommonRecord>();
        String[] IC = new String[3];
        for(TCommonRecord t : presc)
        {
            /* 1-5 */
            String itemclass = t.get("item_class");
            if("A".equals(itemclass))
            {
                IC[0] = "A";
            }
            else if ("B".equals(itemclass))
            {
                IC[1] = "B";
            }
            else
            {
                IC[1] = "C";
            }
            /* 1-7 */
            if("1".equals(t.get("ORDER_TYPE")))
            {
                prescDrugCount++;
            }
            /* 急诊科室标示 */
//            DrugUseDate dud = new DrugUseDate();
//            int Days        = dud.getPrescUseDate(tCom);
//            if((Config.getParamValue("EmergencyPRESC") + ",").indexOf(t.get("org_code") + ",") != -1)
//            {
//                if(Days > 3) listTcom1_12.add(tCom);
//            }
//            else // 门诊科室 
//            {
//                if(Days > 7 ) listTcom1_12.add(tCom);
//            }
            /* 1-11 */
//            if(dosageUnit && "A".equals(itemclass))
//            {
//                dosageUnit = getDosageUnits(t.get("DOSAGE_UNITS"));
//            }
        }
        /*（五）西药、中成药与中药饮片未分别开具处方的; */
        if("A".equals(IC[0]) && "C".equals(IC[2]) || "C".equals(IC[2]) && "B".equals(IC[1]))
        {
            tCom.set("1-5", "1-5");
        }
        /* （七）     药品的剂量、规格、数量、单位等书写不规范或不清楚的;*/
        if(!dosageUnit)
        {
            tCom.set("1-7", "1-7");
        }
        /*（十一）单张门急诊处方超过五种药品的;*/
        if(prescDrugCount > 5 )
        {
            tCom.set("1-11", "1-11");
        }
        return tCom;
    }
    
    /* 审查功能*/
    @Resource(name="drugSecurityChecker")
    private IDrugSecurityChecker drugSC;
    
    /* 保存审查结果 */
    @Resource(name="patientSaveCheckResult")
    private IPatientSaveCheckResult patSaveCRLT;
    
    @Resource(name="prescCheckerServiceBean")
    private IPrescCheckerService pcs;

    /**
     * 返回病人所有处方信息 
     * @param patient_id
     * @return
     */
    private TPrescPatMasterBean getPatientPrescAll(String patient_id)
    {
        try
        {
            return pcs.getUsePrescDetail(patient_id, 0, new String[]{});    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new TPrescPatMasterBean();
    }
    
    /**
     * 药物安全审查 
     * @param po
     * @return
     */
    private TDrugSecurityRslt DrugSecurityCheck(TPatientOrder po)
    {
        TDrugSecurityRslt dsr = drugSC.DrugSecurityCheck(po);
        return dsr;
    }
    
    @Override
    public TPrescCheckRslt PrescCheckRs(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo, String[][] patSigns, String[] patOper)
    {
        /* 门诊审查数据集 */
        TPrescCheckRslt  pcrs = new TPrescCheckRslt();
        /* 审核总接口 */
        TCheckResultCollection CheckRC = new TCheckResultCollection();
        TPatientOrder po = CommonUtils.getPrescPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOper);
        /* 病人所有处方信息  此处需要做一个开关 现在用于 涉及到的门诊流程改造中 门诊处方查询 (目前只针对军委一号) */
        TPrescPatMasterBean ppmb = new TPrescPatMasterBean();// getPatientPrescAll(po.getPatientID());
//        resetPatOrders(ppmb, po);
        setPatMasterBean(ppmb, po);
        pcrs.setPmb(ppmb);
        CheckRC.setDsr(DrugSecurityCheck(po));
        pcrs.setTCRC(CheckRC);
        return pcrs;
    }
    
    public void  setPatMasterBean(TPrescPatMasterBean ppmb, TPatientOrder po)
    {
        if(ppmb.getPATIENT_ID() == null || "".equals(ppmb.getPATIENT_ID()))
        {
            ppmb.setPATIENT_ID(po.getPatientID());
            ppmb.setNAME(po.getPatient().getName());
            ppmb.setSEX(po.getPatient().getSex());
            ppmb.setDATE_OF_BIRTH(po.getPatient().getDateOfBirth());
            TPatOrderDrug[] pods = po.getPatOrderDrugs();
            TPrescMasterBean pmb = new TPrescMasterBean();
            if(pods != null && pods.length > 0 )
            {
                List<TPrescDetailBean>  pdbs = new ArrayList<TPrescDetailBean>();
                for(TPatOrderDrug pod : pods)
                {
                    TPrescDetailBean pdb = new TPrescDetailBean();
                    // 药品ID
//                    pod.setDrugID(pdb.getDRUG_CODE());
                    pdb.setDRUG_CODE(pod.getDrugID());
                    /* 药品名称 */
//                    pod.setDrugName(pdb.getDRUG_NAME());
                    pdb.setDRUG_NAME(pod.getDrugName());
//                    pod.setDoctorName(pmb.getDOCTOR());
                    pmb.setDOCTOR(pod.getDoctorName());
                    // 医嘱序号
//                    pod.setRecMainNo(String.valueOf(++mainOrder));
//                    pdb.setORDER_NO(String.valueOf(mainOrder));
                    pdb.setORDER_NO(pod.getRecMainNo());
                    // 医嘱子序号
//                    pod.setRecSubNo("1");
                    pdb.setORDER_SUB_NO(pod.getRecSubNo());
                    // 一次使用剂量
//                    pod.setDosage(pdb.getDOSAGE());
                    pdb.setDOSAGE(pod.getDosage());
                    // 剂量单位
//                    pod.setDoseUnits(pdb.getDOSAGE_UNITS());
                    pdb.setDOSAGE_UNITS(pod.getDoseUnits());
                    // 给药途径ID
//                    pod.setAdministrationID(pdb.getADMINISTRATION());
                    pdb.setADMINISTRATION(pod.getAdministrationID());
                    /* 给药途径名称 */
//                  pod.setAdminName(pdb.getADMINISTRATION());
                    pdb.setADMINISTRATION(pod.getAdminName());
                    // 执行频率ID
//                    pod.setPerformFreqDictID(pdb.getFREQUENCY());
                    pdb.setFREQUENCY(pod.getPerformFreqDictID());
                    /* 药品包装规格 注：传入时该值放进此处*/
                    pdb.setDRUG_SPEC(pod.getDoctorDept());
                    /* 数量 注：传入时该值放进此处*/
                    pdb.setAMOUNT(pod.getStartDateTime());
                    /* 金额 注：传入时该值放进此处*/
                    pdb.setCHARGES(pod.getStopDateTime());
                    pdb.setCOSTS(pod.getStopDateTime());
                    /* 厂家  注：传入时该值放进此处*/
                    pdb.setFIRM_ID(pod.getDoctor());
                    // 开具医嘱科室
                    pod.setDoctorDept(pmb.getOrdered_by_name());
                    pmb.setOrdered_by_name(pod.getDoctorDept());
                    // 开具医嘱医生
//                    pod.setDoctor(pmb.getDOCTOR());
                    pmb.setDOCTOR(pod.getDoctor());
                    
                    pdbs.add(pdb); 
                }
                pmb.setPdbs((TPrescDetailBean[]) pdbs.toArray(new TPrescDetailBean[0]));
            }
            ppmb.setPmbs(new TPrescMasterBean[]{pmb});
        }
    }
    
    /**
     * 将病人门诊信息组织到医嘱对象中 
     * @param ppmb
     * @param po
     */
    private void resetPatOrders(TPrescPatMasterBean ppmb , TPatientOrder po)
    {
        List<TPatOrderDrug> pods = new ArrayList<TPatOrderDrug>();
        int mainOrder = 0;
        for(TPatOrderDrug t : po.getPatOrderDrugs())
        {
            t.setRecMainNo(String.valueOf(++mainOrder));
            t.setRecSubNo("1");
            pods.add(t);
        }
        TPrescMasterBean[] pmbs = ppmb.getPmbs();
        if(pmbs == null || pmbs.length == 0) return ;
        for(TPrescMasterBean pmb : pmbs)
        {
            for(TPrescDetailBean pdb: pmb.getPdbs())
            {
                TPatOrderDrug pod = new TPatOrderDrug();
                // 药品ID
                pod.setDrugID(pdb.getDRUG_CODE());
                /* 药品名称 */
                pod.setDrugName(pdb.getDRUG_NAME());
                pod.setDoctorName(pmb.getDOCTOR());
                // 医嘱序号
                pod.setRecMainNo(String.valueOf(++mainOrder));
                pdb.setORDER_NO(String.valueOf(mainOrder));
                // 医嘱子序号
                pod.setRecSubNo("1");
                pdb.setORDER_SUB_NO("1");
                // 一次使用剂量
                pod.setDosage(pdb.getDOSAGE());
                // 剂量单位
                pod.setDoseUnits(pdb.getDOSAGE_UNITS());
                // 给药途径ID
                pod.setAdministrationID(pdb.getADMINISTRATION());
                // 执行频率ID
                pod.setPerformFreqDictID(pdb.getFREQUENCY());
                // 开具医嘱科室
                pod.setDoctorDept(pmb.getOrdered_by_name());
                // 开具医嘱医生
                pod.setDoctor(pmb.getDOCTOR());
                /* 给药途径名称 */
                pod.setAdminName(pdb.getADMINISTRATION());
                pods.add(pod);
            }
        }
        po.setPatOrderDrugs((TPatOrderDrug[])pods.toArray(new TPatOrderDrug[0]));
    }
    
    @Override
    public String[] DrugAuthorizationCheck(String[] PrescBeanInfo)
    {
        return new String[]{};
    }
}
