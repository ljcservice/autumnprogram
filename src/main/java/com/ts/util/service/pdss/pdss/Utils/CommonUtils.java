package com.ts.service.pdss.pdss.Utils;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.hitzd.his.Beans.TPatOperation;
import com.hitzd.his.Beans.TPatOrderDiagnosis;
import com.hitzd.his.Beans.TPatOrderDrug;
import com.hitzd.his.Beans.TPatOrderDrugSensitive;
import com.hitzd.his.Beans.TPatOrderInfoExt;
import com.hitzd.his.Beans.TPatOrderVisitInfo;
import com.hitzd.his.Beans.TPatSigns;
import com.hitzd.his.Beans.TPatient;
import com.hitzd.his.Beans.TPatientOrder;
import com.hitzd.his.Beans.TPreveUseDrug;
import com.hitzd.his.Beans.TTreatUseDrug;
import com.hitzd.his.Utils.DateUtils;
import com.ts.entity.pdss.pdss.Beans.TAdministration;
import com.ts.entity.pdss.pdss.Beans.TDrug;

/**
 * 基本常用方法
 * 
 * @author liujc
 * 
 */
public class CommonUtils
{
    /**
     * 组装sql in( ? ) 问号部分 number 类型
     * 
     * @param values
     * @return
     */
    public static String makeWheres(String[] values)
    {
        return makeWheres(values, false);
    }

    /**
     * 组装sql in( ? ) 问号部分 varchar 类型
     * 
     * @param values
     * @param isSTR
     * @return
     */
    public static String makeWheres(String[] values, boolean isSTR)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.length; i++)
        {
            if (isSTR)
            {
                sb.append("'");
            }
            sb.append(values[i]);
            if (isSTR)
            {
                sb.append("'");
            }
            sb.append(",");
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 根据医嘱中的用药信息找到 药品信息
     * @param drugs 药品集合对象 List
     * @param pods  
     * @return
     */
    public static TDrug getDrugInfoOne(List<TDrug> drugs, TPatOrderDrug pods)
    {
        if(drugs == null)
             return null;
        if(pods == null)
            return null;
        TDrug drug = null;
        for(int ii = 0 ;ii<drugs.size();ii++)
        {
            if(drugs.get(ii).getDRUG_NO_LOCAL().equals(pods.getDrugID()))
            {
                return  new TDrug(drugs.get(ii));  
            }
        }
        return drug;
    }
    
    /**
     * 根据医嘱中的用药信息找到 药品信息
     * @param drugs 药品集合对象 List
     * @param pods  
     * @return
     */
    public static TAdministration getAdministrationInfoOne(List<TAdministration> admini, TPatOrderDrug pods)
    {
        if(admini == null)
             return null;
        if(pods == null)
            return null;
        TAdministration result = null;
        for(int ii = 0 ;ii<admini.size();ii++)
        {
            if(admini.get(ii).getADMINISTRATION_NO_LOCAL().equals(pods.getAdministrationID()))
            {
                result = admini.get(ii);
                break;
            }
        }
        return result; 
    }
    
    /**
     * 获得医嘱对象中的所有药品id
     * 
     * @param po
     * @return
     */
    public static String[] getPoDrugIDs(TPatientOrder po)
    {
        /* 病人用药信息 */
        TPatOrderDrug[] podrugs = po.getPatOrderDrugs();
        String[] drugids = new String[podrugs.length];
        for (int i = 0; i < podrugs.length; i++)
        {
            drugids[i] = podrugs[i].getDrugID();
        }
        return drugids;
    }


    /**
     * 预防用药信息记录
     * @param po
     * @param antidrug
     * @return
     */
    private static TPreveUseDrug setPreveUseDrug(TPatientOrder po , String[] antidrug)
    {
        TPreveUseDrug pud = new TPreveUseDrug();
        /* 预防用药id */
        pud.setYF_ID(UUID.randomUUID().toString());
        /* 病人id号(住院号) */
        pud.setPATIENT_ID(po.getPatientID());
        /* 出院次数 */
        pud.setVISIT_ID(po.getPatVisitInfo().getVisitID());
        /* 科室代码 */
        pud.setDEPT_CODE(po.getDoctorDeptID());
        /* 科室名称 */
        pud.setDEPT_NAME(po.getDoctorDeptName());
        /* 医生名称 */
        pud.setDOCTOR_NAME(po.getDoctorName());
        /* 姓名 */
        pud.setNAME(po.getPatient().getName());
        /* 性别 */
        pud.setSEX(po.getPatient().getSex());
        /* 年龄 */
        pud.setAGE("");
        /* 体重 */
        pud.setWEIGHT(po.getPatInfoExt().getWeight());
        /* 本地药品码 */
        pud.setDRUG_CODE(antidrug[4]);
        /* 本地药品名称 */
        pud.setDRUG_NAME(antidrug[3]);
        /* 最小剂量 */
        pud.setDOSAGE("");
        /* 最小单位 */
        pud.setDOSAGE_UNITS("");
        /* 用药途径 */
        pud.setADMINISTRATION("");
        /* 手术时间 */
        pud.setOPERTOR_DATE("");
        /* 手术名称 */
        pud.setOPERTOR_NAME(antidrug[7]);
        /* 手术类型 */
        pud.setOPERTOR_TYPE(antidrug[13]);
        /* 手术时长 */
        pud.setOPERTOR_USE_TIME("");
        /* 使用时间点 */
        pud.setDRUG_USE_TIME("");
        /* 过敏史 */
        pud.setGMS(antidrug[9]);
        /* β过敏史 */
        pud.setBTGMS(antidrug[10]);
        /* 危险因素 */
        pud.setWXYS(antidrug[11]);
        /* 可能的治病菌 */
        pud.setKNDZBJ(antidrug[12]);
        /* 克林剂量氨南剂量 */
        pud.setKLJLANJL("");
        /* 预防使用抗菌药物的依据 */
        pud.setYF_USE_DRUG_YJ("");
        /* 特殊要求 */
        pud.setTSYQ(antidrug[8]);
        /* 记录时间 */
        pud.setREC_DATE(DateUtils.getStringFromDate(new Date(), "yyyy-MM-dd"));
        return pud;
    }
    
    /**
     * 治疗用药信息保存
     * @param po
     * @param antidrug
     * @return
     */
    private static TTreatUseDrug setTreatUseDrug(TPatientOrder po , String[] antidrug)
    {
        TTreatUseDrug tud =  new TTreatUseDrug();
        /* 治疗用药id */
        tud.setZL_ID(UUID.randomUUID().toString());
        /* 病人id号(住院号) */
        tud.setPATIENT_ID(po.getPatientID());
        /* 出院次数 */
        tud.setVISIT_ID(po.getPatVisitInfo().getVisitID());
        /* 科室代码 */
        tud.setDEPT_CODE(po.getDoctorDeptID());
        /* 科室名称 */
        tud.setDEPT_NAME(po.getDoctorDeptName());
        /* 医生名称 */
        tud.setDOCTOR_NAME(po.getDoctorName());
        /* 姓名 */
        tud.setNAME(po.getPatient().getName());
        /* 性别 */
        tud.setSEX(po.getPatient().getSex());
        /* 年龄 */
        tud.setAGE("");
        /* 体重 */
        tud.setWEIGHT(po.getPatInfoExt().getWeight());
        /* 用药理由 */
        tud.setYYLY(antidrug[12]);
        /* 治疗分类 */
        tud.setZLFL(antidrug[13]);
        /* 感染部位 */
        tud.setGRBW(antidrug[14]);
        /* 诊断依据 */
        tud.setZDYJ(antidrug[15]);
        /* 可能的致病菌 */
        tud.setKNDZBJ("");
        /* 本地药品码 */
        tud.setDRUG_CODE(antidrug[4]);
        /* 本地药品名称 */
        tud.setDRUG_NAME(antidrug[3]);
        /* 最小剂量 */
        tud.setDOSAGE("");
        /* 最小单位 */
        tud.setDOSAGE_UNITS("");
        /* 用药途径 */
        tud.setADMINISTRATION("");
        /* 记录时间 */
        tud.setREC_DATE(DateUtils.getStringFromDate(new Date(), "yyyy-MM-dd"));
        /* 体温日期 */
        tud.setTWDATE(antidrug[5]);
        /* 体温值  */
        tud.setTWVALUE(antidrug[6]);
        /* 体温是否正常 */
        tud.setTWOK(antidrug[7]);
        /* 血项日期 */
        tud.setXXDATE(antidrug[8]);
        /* 血项值 */
        tud.setXXVALUE(antidrug[9]);
        /* C蛋白值 */
        tud.setCVALUE(antidrug[10]);
        /*血项是否正常   */
        tud.setXXOK(antidrug[11]);
        return tud;
    }
    
    /**
     * 带抗菌药物保存 
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @param patSigns
     * @param patOperation
     * @param antidrug
     * @return
     */
    public static TPatientOrder getPatientOrder(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns, String[] patOperation,String[][] antidrug)
    {
        TPatientOrder po = getPatientOrder(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOperation);
        List<TPreveUseDrug> puds = new ArrayList<TPreveUseDrug>();
        List<TTreatUseDrug> tuds = new ArrayList<TTreatUseDrug>();
        if(antidrug != null)
        {
            for(int i = 0 ; i < antidrug.length ; i++)
            {
                String flag = antidrug[i][0];
                String[] antidrugInfo = antidrug[i];
                if("1".equals(flag))
                {
                    puds.add(setPreveUseDrug(po,antidrugInfo));
                }
                else
                {
                    tuds.add(setTreatUseDrug(po, antidrugInfo));
                }    
            }
        }
        po.setPreveUseDrug((TPreveUseDrug[])puds.toArray(new TPreveUseDrug[0]));
        po.setTreatUseDrug((TTreatUseDrug[])tuds.toArray(new TTreatUseDrug[0]));
        return po;
    }
    
    /**
     * 返回医嘱对象
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @return
     */
    public static TPatientOrder getPatientOrder(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns, String[] patOperation)
    {
        System.out.println("---------------住院参数个数-------------------------------------");
        PrintPrescBaseInfo(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOperation);
        TPatientOrder entity = new TPatientOrder();
        entity.setDoctorDeptID(doctorInfo[0]);
        entity.setDoctorDeptName(doctorInfo[1]);
        entity.setDoctorID(doctorInfo[2]);
        entity.setDoctorName(doctorInfo[3]);
        entity.setDoctorTitleID(doctorInfo[4]);
        entity.setDoctorTitleName(doctorInfo[5]);
        Map<String, Object> mapPatient = getPatient(patientInfo);
        entity.setPatient((TPatient)mapPatient.get("Patient"));
        entity.setPatInfoExt((TPatOrderInfoExt)mapPatient.get("PatOrderInfoExt"));
        entity.setPatVisitInfo((TPatOrderVisitInfo)mapPatient.get("PatOrderVisitInfo"));
        entity.setPatSigns(getPatSigns(patSigns));
        entity.setPatientID(entity.getPatVisitInfo().getPatientID());
        entity.setPatOrderDrugs(getPatOrderDrug(drugInfo));
        entity.setPatOrderDiagnosiss(getPatOrderDiagnosis(diagnosisInfo));
        entity.setPatOrderDrugSensitives(getPatOrderDrugSensitive(sensitiveInfo));
        entity.setPatOperation(getPatOperation(patOperation));
        return entity;
    }
    
    private static void PrintPrescBaseInfo(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns, String[] patOperation)
    {
        
        System.out.println("doctorInfo:"  + (doctorInfo!= null?doctorInfo.length:"null"));
        System.out.println("patientInfo:" + (patientInfo!= null?patientInfo.length:"null"));
        System.out.println("drugInfo:"    + (drugInfo!= null?drugInfo.length:"null"));
        System.out.println("diagnosisInfo:" + (diagnosisInfo!= null?diagnosisInfo.length:"null"));
        System.out.println("sensitiveInfo:" + (sensitiveInfo!= null?sensitiveInfo.length:"null"));
        System.out.println("patSigns:" + (patSigns!= null?patSigns.length:"null"));
        System.out.println("patOperation:" + (patOperation!= null?patOperation.length:"null"));
        System.out.println("---------------结束-------------------------------------");
    }
    
    /** 门诊用医嘱对象 
     * 返回医嘱对象
     * @param doctorInfo
     * @param patientInfo
     * @param drugInfo
     * @param diagnosisInfo
     * @param sensitiveInfo
     * @return
     */
    public static TPatientOrder getPrescPatientOrder(String[] doctorInfo, String[] patientInfo, String[][] drugInfo, String[][] diagnosisInfo, String[][] sensitiveInfo,String[][] patSigns, String[] patOperation)
    {
        System.out.println("---------------门诊参数个数-------------------------------------");
        PrintPrescBaseInfo(doctorInfo, patientInfo, drugInfo, diagnosisInfo, sensitiveInfo, patSigns, patOperation);
        TPatientOrder entity = new TPatientOrder();
        entity.setDoctorDeptID(doctorInfo[0]);
        entity.setDoctorDeptName(doctorInfo[1]);
        entity.setDoctorID(doctorInfo[2]);
        entity.setDoctorName(doctorInfo[3]);
        entity.setDoctorTitleID(doctorInfo[4]);
        entity.setDoctorTitleName(doctorInfo[5]);
        Map<String, Object> mapPatient = getPatient(patientInfo);
        entity.setPatient((TPatient)mapPatient.get("Patient"));
        entity.setPatInfoExt((TPatOrderInfoExt)mapPatient.get("PatOrderInfoExt"));
        entity.setPatVisitInfo((TPatOrderVisitInfo)mapPatient.get("PatOrderVisitInfo"));
        entity.setPatSigns(getPatSigns(patSigns));
        entity.setPatientID(entity.getPatVisitInfo().getPatientID());
        entity.setPatOrderDrugs(getPrescPatOrderDrug(drugInfo));
        entity.setPatOrderDiagnosiss(getPatOrderDiagnosis(diagnosisInfo));
        entity.setPatOrderDrugSensitives(getPatOrderDrugSensitive(sensitiveInfo));
        entity.setPatOperation(getPatOperation(patOperation));
        return entity;
    }
    
    /**
     * 手术信息 
     * @param pats
     * @return
     */
    public static TPatOperation[] getPatOperation(String[] pats)
    {
        TPatOperation patOper = new TPatOperation();
        patOper.setOperCode(pats[0]);
        patOper.setOperName(pats[1]); 
        patOper.setOperLevel(pats[2]);
        patOper.setOperStartTime(pats[3]);
        patOper.setOperEndTime(pats[4]);
        return new TPatOperation[]{patOper};
    }
    
    /**
     * 体征对象
     * @param patSigns
     * @return
     */
    public static TPatSigns[] getPatSigns(String[][] patSigns) 
    {
        List<TPatSigns> psigns = new ArrayList<TPatSigns>();
        for (int i = 0; i < patSigns.length; i++)
        {
            TPatSigns p = new TPatSigns();
            p.setTWDate(patSigns[i][0]);
            p.setTWOK(patSigns[i][1]);
            p.setTWValue(patSigns[i][2]);
            p.setXXDate(patSigns[i][3]);
            p.setXXOK(patSigns[i][4]);
            p.setXXValue(patSigns[i][5]);
            p.setCValue(patSigns[i][6]);
            psigns.add(p);
        }
        return (TPatSigns[]) psigns.toArray(new TPatSigns[0]);
    }
    
    public static Map<String, Object> getPatient(String[] patient)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        TPatient entity              = new TPatient();
        TPatOrderInfoExt infoExt     = new TPatOrderInfoExt();
        TPatOrderVisitInfo visitinfo = new TPatOrderVisitInfo();
        visitinfo.setPatientID(patient[0]);
        entity.setName(patient[1]);
        entity.setSex(patient[2]);
        entity.setDateOfBirth(patient[3]);
        entity.setBirthPlace(patient[4]);
        entity.setNation(patient[5]);
        infoExt.setIsLact(patient[6]);
        infoExt.setIsPregnant(patient[7]);
        infoExt.setInsureanceType(patient[8]);
        infoExt.setInsuranceNo(patient[9]);
        infoExt.setIsLiverWhole(patient[10]);
        infoExt.setIsKidneyWhole(patient[11]);
        // infoExt.setisWorking(patient[12]);
        infoExt.setHeight(patient[13]);
        infoExt.setWeight(patient[14]);
        visitinfo.setVisitID(patient[15]);
        visitinfo.setInDept(patient[16]);
        visitinfo.setInMode(patient[17]);
        visitinfo.setInDate(patient[18]);
        visitinfo.setOutDept(patient[19]);
        // visitinfo.setoutDate(patient[20]);
        // entity.setoutMode(patient[21]);
        visitinfo.setPatAdmCondition(patient[22]);
        result.put("Patient", entity);
        result.put("PatOrderInfoExt", infoExt);
        result.put("PatOrderVisitInfo", visitinfo);
        return result;
    }

    /**
     * 医嘱诊断信息
     * 
     * @param diagnosis
     * @return
     */
    public static TPatOrderDiagnosis[] getPatOrderDiagnosis(String[][] diagnosis)
    {
        List<TPatOrderDiagnosis> diagnosises = new ArrayList<TPatOrderDiagnosis>();
        for (int i = 0; i < diagnosis.length; i++)
        {
            TPatOrderDiagnosis entity = new TPatOrderDiagnosis();
            if(diagnosis[i] != null)
            {
                System.out.println("诊断：" + diagnosis[i][0]); 
                entity.setDiagnosisDictID(diagnosis[i][0]);    
            }
            
            diagnosises.add(entity);
        }
        return (TPatOrderDiagnosis[]) diagnosises.toArray(new TPatOrderDiagnosis[0]);
    }

    /**
     * 过敏信息对象数组
     * 
     * @param sensitive
     * @return
     */
    public static TPatOrderDrugSensitive[] getPatOrderDrugSensitive(
            String[][] sensitive)
    {
        List<TPatOrderDrugSensitive> sensitives = new ArrayList<TPatOrderDrugSensitive>();
		
			for (int i = 0; i < sensitive.length; i++) {
				TPatOrderDrugSensitive entity = new TPatOrderDrugSensitive();
				try {
					entity.setPatOrderDrugSensitiveID(sensitive[i][0]);
					entity.setDrugAllergenID(sensitive[i][1]);
				} catch (NullPointerException e) {
					entity.setPatOrderDrugSensitiveID("");
					entity.setDrugAllergenID("");
				}
				sensitives.add(entity);
		}
        return (TPatOrderDrugSensitive[]) sensitives
                .toArray(new TPatOrderDrugSensitive[0]);
    }

    public static void main(String[] args) {
		String[][] starBArray = new String[][]{null,null};
		System.out.println(null != starBArray);
		System.out.println(starBArray.length);
		System.out.println(starBArray[0][1] ==  null );
	}
    /**
     * 用药信息对象数组
     * 
     * @param druginfos
     * @return 
     */
    public static TPatOrderDrug[] getPatOrderDrug(String[][] druginfos)
    {
        List<TPatOrderDrug> drugs = new ArrayList<TPatOrderDrug>();
        if(druginfos == null) return (TPatOrderDrug[]) drugs.toArray(new TPatOrderDrug[0]);
        for (int i = 0; i < druginfos.length; i++)
        {
            TPatOrderDrug drug = new TPatOrderDrug();
            drug.setDrugID(druginfos[i][0]);
            drug.setDrugStandID(druginfos[i][1]);
            drug.setDrugName(druginfos[i][2]);
            drug.setRecMainNo(druginfos[i][3]);
            drug.setRecSubNo(druginfos[i][4]);
            drug.setDosage(druginfos[i][5]);
            drug.setDoseUnits(druginfos[i][6]);
            drug.setAdministrationID(druginfos[i][7]);
            drug.setAdministrationStandID(druginfos[i][8]);
            drug.setPerformFreqDictID(druginfos[i][9]);
            drug.setPerformFreqDictStandID(druginfos[i][10]);
            drug.setPerformFreqDictText(druginfos[i][11]);
            drug.setStartDateTime(druginfos[i][12]);
            drug.setStopDateTime(druginfos[i][13]);
            drug.setDoctorDept(druginfos[i][14]);
            drug.setDoctor(druginfos[i][15]);
            drug.setIsGroup(druginfos[i][16]);
            drug.setFirmID(druginfos[i][17]);
            drug.setIsNew(druginfos[i][18]);
            drug.setDvaliddate(druginfos[i][19]);
            drug.setUseType(druginfos[i][20]);
            drug.setUseCause(druginfos[i][21]);
            drugs.add(drug);
        }
        return (TPatOrderDrug[]) drugs.toArray(new TPatOrderDrug[0]);
    }
    
    /**
     * 门诊用药信息 
     * @param druginfos
     * @return
     */
    public static TPatOrderDrug[] getPrescPatOrderDrug(String[][] druginfos)
    {
        List<TPatOrderDrug> drugs = new ArrayList<TPatOrderDrug>();
        if(druginfos == null) return (TPatOrderDrug[]) drugs.toArray(new TPatOrderDrug[0]);
        for (int i = 0; i < druginfos.length; i++)
        {
            TPatOrderDrug drug = new TPatOrderDrug();
            drug.setDrugID(druginfos[i][0]);
            drug.setDrugStandID(druginfos[i][1]);
            drug.setDrugName(druginfos[i][2]);
            String recMainNo = druginfos[i][3];
            String recSubNo  = druginfos[i][4]; 
            System.out.println("主医嘱序号 : " +  recMainNo );
            System.out.println("子医嘱序号 : " +  recSubNo );
            if(recMainNo == null || "".equals(recMainNo))
            {
                recMainNo = "1";
                recSubNo  = String.valueOf( i + 1 );
            }
            else
            {
                if(recSubNo == null || "".equals(recSubNo))
                {
                    recSubNo = "1";
                }
            }
            drug.setRecMainNo(recMainNo);
            drug.setRecSubNo(recSubNo);
            drug.setDosage(druginfos[i][5]);
            drug.setDoseUnits(druginfos[i][6]);
            drug.setAdministrationID(druginfos[i][7]);
            drug.setAdministrationStandID(druginfos[i][8]);
            drug.setPerformFreqDictID(druginfos[i][9]);
            drug.setPerformFreqDictStandID(druginfos[i][10]);
            drug.setPerformFreqDictText(druginfos[i][11]);
            drug.setStartDateTime(druginfos[i][12]);
            drug.setStopDateTime(druginfos[i][13]);
            drug.setDoctorDept(druginfos[i][14]);
            drug.setDoctor(druginfos[i][15]);
            drug.setIsGroup(druginfos[i][16]);
            drug.setFirmID(druginfos[i][17]);
            drug.setIsNew(druginfos[i][18]);
            drug.setDvaliddate(druginfos[i][19]);
            drug.setUseType(druginfos[i][20]);
            drug.setUseCause(druginfos[i][21]);
            drugs.add(drug);
        }
        return (TPatOrderDrug[]) drugs.toArray(new TPatOrderDrug[0]);
    }
    
    /**
     * 返回预防记录bean
     * @param PreveUseDrug
     * @return
     */
    public static TPreveUseDrug[] getPreveUseDrug(String[][] PreveUseDrug)
    {
        List<TPreveUseDrug> puds = new ArrayList<TPreveUseDrug>();
        if(PreveUseDrug == null) return (TPreveUseDrug[]) puds.toArray(new TPreveUseDrug[0]);
        System.out.println("-------------预防记录bean---------------");
        getSystem(PreveUseDrug);
        for(int i = 0 ; i < PreveUseDrug.length ; i++)
        {
            TPreveUseDrug pud = new TPreveUseDrug();
            pud.setYF_ID(PreveUseDrug[i][0]);           
            pud.setPATIENT_ID(PreveUseDrug[i][1]);      
            pud.setVISIT_ID(PreveUseDrug[i][2]);        
            pud.setDEPT_CODE(PreveUseDrug[i][3]);       
            pud.setDEPT_NAME(PreveUseDrug[i][4]);       
            pud.setDOCTOR_NAME(PreveUseDrug[i][5]);     
            pud.setNAME(PreveUseDrug[i][6]);            
            pud.setSEX(PreveUseDrug[i][7]);             
            pud.setAGE(PreveUseDrug[i][8]);             
            pud.setWEIGHT(PreveUseDrug[i][9]);          
            pud.setDRUG_CODE(PreveUseDrug[i][10]);       
            pud.setDRUG_NAME(PreveUseDrug[i][11]);       
            pud.setDOSAGE(PreveUseDrug[i][12]);          
            pud.setDOSAGE_UNITS(PreveUseDrug[i][13]);    
            pud.setADMINISTRATION(PreveUseDrug[i][14]);  
            pud.setOPERTOR_DATE(PreveUseDrug[i][15]);    
            pud.setOPERTOR_NAME(PreveUseDrug[i][16]);    
            pud.setOPERTOR_TYPE(PreveUseDrug[i][17]);    
            pud.setOPERTOR_USE_TIME(PreveUseDrug[i][18]);
            pud.setDRUG_USE_TIME(PreveUseDrug[i][19]);   
            pud.setGMS(PreveUseDrug[i][20]);             
            pud.setBTGMS(PreveUseDrug[i][21]);           
            pud.setWXYS(PreveUseDrug[i][22]);            
            pud.setKNDZBJ(PreveUseDrug[i][23]);          
            pud.setKLJLANJL(PreveUseDrug[i][24]);
            pud.setYF_USE_DRUG_YJ(PreveUseDrug[i][25]);  
            pud.setTSYQ(PreveUseDrug[i][26]);    
            pud.setREC_DATE(DateUtils.getStringFromDate(new Date(), "yyyy-MM-dd"));
            puds.add(pud);
        }
        return (TPreveUseDrug[]) puds.toArray(new TPreveUseDrug[0]);
    }
    
    /**
     * 返回治疗记录bean
     * @param PreveUseDrug
     * @return
     */
    public static TTreatUseDrug[] getTreatUseDrug(String[][] TreatUseDrug)
    {
        List<TTreatUseDrug> tuds = new ArrayList<TTreatUseDrug>();
        if(TreatUseDrug == null ) return (TTreatUseDrug[]) tuds.toArray(new TTreatUseDrug[0]);
        System.out.println("-------------治疗记录bean---------------");
        getSystem(TreatUseDrug);
        for(int i = 0 ; i < TreatUseDrug.length ; i++)
        {
            TTreatUseDrug tud = new TTreatUseDrug();
            tud.setZL_ID(TreatUseDrug[i][0]);         
            tud.setPATIENT_ID(TreatUseDrug[i][1]);    
            tud.setVISIT_ID(TreatUseDrug[i][2]);      
            tud.setDEPT_CODE(TreatUseDrug[i][3]);     
            tud.setDEPT_NAME(TreatUseDrug[i][4]);     
            tud.setDOCTOR_NAME(TreatUseDrug[i][5]);   
            tud.setNAME(TreatUseDrug[i][6]);          
            tud.setSEX(TreatUseDrug[i][7]);           
            tud.setAGE(TreatUseDrug[i][8]);           
            tud.setWEIGHT(TreatUseDrug[i][9]);        
            tud.setYYLY(TreatUseDrug[i][10]);          
            tud.setZLFL(TreatUseDrug[i][11]);          
            tud.setGRBW(TreatUseDrug[i][12]);          
            tud.setZDYJ(TreatUseDrug[i][13]);          
            tud.setKNDZBJ(TreatUseDrug[i][14]);        
            tud.setDRUG_CODE(TreatUseDrug[i][15]);     
            tud.setDRUG_NAME(TreatUseDrug[i][16]);     
            tud.setDOSAGE(TreatUseDrug[i][17]);        
            tud.setDOSAGE_UNITS(TreatUseDrug[i][18]);  
            tud.setADMINISTRATION(TreatUseDrug[i][19]);
            tud.setREC_DATE(DateUtils.getStringFromDate(new Date(), "yyyy-MM-dd"));     
            tud.setTWDATE(TreatUseDrug[i][21]);        
            tud.setTWVALUE(TreatUseDrug[i][22]);       
            tud.setTWOK(TreatUseDrug[i][23]);          
            tud.setXXDATE(TreatUseDrug[i][24]);        
            tud.setXXVALUE(TreatUseDrug[i][25]);       
            tud.setCVALUE(TreatUseDrug[i][26]);        
            tud.setXXOK(TreatUseDrug[i][27]);
            tuds.add(tud);
        }
        return (TTreatUseDrug[]) tuds.toArray(new TTreatUseDrug[0]);
    }
    
    private static void getSystem(String[][] values)
    {
        for(int i = 0 ; i < values.length ; i++)
        {
            for(int j = 0 ; j < values[i].length ; j++)
            {
                System.out.println("数组  [" + i + "][" + j + "] = " + values[i][j]);
            }
        }
    }
}
