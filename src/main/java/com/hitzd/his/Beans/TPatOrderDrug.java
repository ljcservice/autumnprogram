package com.hitzd.his.Beans;

import java.io.Serializable;
import java.util.Date;

//import org.codehaus.xfire.aegis.type.java5.IgnoreProperty;

import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DateUtils;
import com.hitzd.his.Utils.DictCache;

/**
 * @description 病人用药记录类：PatOrderDrug 对应数据库表：病人用药记录(Pat_Order_Drug)
 * @author
 */
public class TPatOrderDrug implements Serializable
{

    private static final long serialVersionUID = 1L;
    // 药品ID
    private String            drugID;
    // 药品标准ID
    private String            drugStandID;
    /* 药品名称 */
    private String            DrugName;
    private String            DoctorName;
    // 医嘱序号
    private String            recMainNo;
    // 医嘱子序号
    private String            recSubNo;
    // 一次使用剂量
    private String            dosage;
    // 剂量单位
    private String            doseUnits;
    // 给药途径ID
    private String            administrationID;
    // 给药途径标准ID
    private String            administrationStandID;
    // 执行频率ID
    private String            performFreqDictID;
    // 执行频率标准ID
    private String            performFreqDictStandID;
    // 执行执行频率文本(输入执行频率描述)
    private String            performFreqDictText;
    // 开始日期时间
    private String            startDateTime;
    // 结束日期时间
    private String            stopDateTime;
    // 开具医嘱科室
    private String            doctorDept;
    // 开具医嘱医生
    private String            doctor;
    // 是否组
    private String            isGroup;
    // 药品厂商ID
    private String            firmID;
    // 是否为新医嘱
    private String            isNew;
    // 确认时间
    private String            dvaliddate;
    /* 给药途径名称 */
    private String            adminName;
    /* "预防用"用1表示，“治疗用”用2表示 */
    private String            useType;
    /* 使用原因 */
    private String            useCause;
    
    
    /**
     * 返回使用药品的天数 
     * @return
     */
    public Long getUseDrugDay(){
        Date begin =  DateUtils.getDateFromString(startDateTime,DateUtils.FORMAT_DATETIME);//开始用药时间
        Date stop =  DateUtils.getDateFromString(stopDateTime,DateUtils.FORMAT_DATETIME);//结束用药时间
        if(begin == null || stop == null)
        {
            //TODO 此处计划处理 门诊用药天数的计算
            return null;
        }
        Long  UseDay =  (stop.getTime() - begin.getTime()) / 1000 / (24 * 60 * 60);
        return UseDay;
    }
    
    /* 门诊需要的信息bean Begin */
    /* 处方序号 */
    private String PRESC_NO;
    /* 项目序号 */
    private String ITEM_NO ;
    /* 数量 */
    private String AMOUNT; 
    /* 计价金额 */
    private String COSTS;
    /* 单位 */
    private String UNITS;
    /* 药品规格 */
    private String DRUG_SPEC;
    /* 流水号 */
    private String SERIAL_NO;
    /* 项目类别  */
    private String ITEM_CLASS;
    /* 门诊需要的信息bean End*/
    
    public String getUseCause()
    {
        return useCause;
    }

    public void setUseCause(String useCause)
    {
        this.useCause = useCause;
    }

    public String getUseType()
    {
        return useType;
    }

    public void setUseType(String useType)
    {
        this.useType = useType;
    }

    public String getAdminName()
    {
        return adminName;
    }

    public void setAdminName(String adminName)
    {
        this.adminName = adminName;
    }

    public String getDrugName()
    {
        return DrugName;
    }

    public void setDrugName(String drugName)
    {
        DrugName = drugName;
    }

    public String getDoctorName()
    {
        return DoctorName;
    }

    public void setDoctorName(String doctorName)
    {
        DoctorName = doctorName;
    }

    public String getDvaliddate()
    {
        return dvaliddate;
    }

    public void setDvaliddate(String dvaliddate)
    {
        this.dvaliddate = dvaliddate;
    }

    public String getAdministrationID()
    {
        return administrationID;
    }

    public void setAdministrationID(String administrationID)
    {
        String admin = administrationID;
        /* 过滤一下 传入的是否是 code 为 1 时 administrationID 则是名字 需要转换 
         * */
//        DictCache dc = DictCache.getNewInstance();
//        if (Config.getParamValue("admin_conv_flag").equals("1"))
//        {
//            admin = dc.getAdminByName(administrationID).get("administration_code");
//            this.setAdminName(administrationID);
//        }
        this.administrationID = admin;
    }

    public String getDoctor()
    {
        return doctor;
    }

    public void setDoctor(String doctor)
    {
        this.doctor = doctor;
    }

    public String getDoctorDept()
    {
        return doctorDept;
    }

    public void setDoctorDept(String doctorDept)
    {
        this.doctorDept = doctorDept;
    }

    public String getDoseUnits()
    {
        return doseUnits;
    }

    public void setDoseUnits(String doseUnits)
    {
        this.doseUnits = doseUnits;
    }

    public String getDrugID()
    {
        return drugID;
    }

    public void setDrugID(String drugID)
    {
        this.drugID = drugID;
    }

    public String getPerformFreqDictID()
    {
        return performFreqDictID;
    }

    public void setPerformFreqDictID(String performFreqDictID)
    {
    	 String performFreq = performFreqDictID;
         /* 过滤一下 传入的是否是 code 为 1 时 performFreqDictID 则是名字 需要转换 
          * 1为 频次的名字  
          * */
//         DictCache dc = DictCache.getNewInstance();
//         if (Config.getParamValue("perform_conv_flag").equals("1"))
//         {
//        	 performFreq = dc.getPerformMapByName(performFreqDictID).get("serial_no");
//             this.setPerformFreqDictText(performFreqDictID);
//         }
         this.performFreqDictID = performFreq;
    }

    public String getRecMainNo()
    {
        return recMainNo;
    }

    public void setRecMainNo(String recMainNo)
    {
        this.recMainNo = recMainNo;
    }

    public String getRecSubNo()
    {
        return recSubNo;
    }

    public void setRecSubNo(String recSubNo)
    {
        this.recSubNo = recSubNo;
    }

    public String getDosage()
    {
        return dosage;
    }

    public void setDosage(String dosage)
    {
        this.dosage = dosage;
    }

    public String getStartDateTime()
    {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime)
    {
        this.startDateTime = startDateTime;
    }

    public String getStopDateTime()
    {
        return stopDateTime;
    }

    public void setStopDateTime(String stopDateTime)
    {
        this.stopDateTime = stopDateTime;
    }

    public String getIsGroup()
    {
        return isGroup;
    }

    public void setIsGroup(String isGroup)
    {
        this.isGroup = isGroup;
    }

    public String getIsNew()
    {
        return isNew;
    }

    public void setIsNew(String isNew)
    {
        this.isNew = isNew;
    }

    public String getPerformFreqDictText()
    {
        return performFreqDictText;
    }

    public void setPerformFreqDictText(String performFreqDictText)
    {
        this.performFreqDictText = performFreqDictText;
    }

    // @IgnoreProperty
    public String getPerformFreqDictStandID()
    {
        return performFreqDictStandID;
    }

    public void setPerformFreqDictStandID(String performFreqDictStandID)
    {
        this.performFreqDictStandID = performFreqDictStandID;
    }

    // @IgnoreProperty
    public String getAdministrationStandID()
    {
        return administrationStandID;
    }

    public void setAdministrationStandID(String administrationStandID)
    {
        this.administrationStandID = administrationStandID;
    }

    // @IgnoreProperty
    public String getDrugStandID()
    {
        return drugStandID;
    }

    public void setDrugStandID(String drugStandID)
    {
        this.drugStandID = drugStandID;
    }

    public String getFirmID()
    {
        return firmID;
    }

    public void setFirmID(String firmID)
    {
        this.firmID = firmID;
    }

    @Override
    public String toString()
    {
        return "TPatOrderDrug [drugID=" + drugID + ", drugStandID="
                + drugStandID + ", DrugName=" + DrugName + ", DoctorName="
                + DoctorName + ", recMainNo=" + recMainNo + ", recSubNo="
                + recSubNo + ", dosage=" + dosage + ", doseUnits=" + doseUnits
                + ", administrationID=" + administrationID
                + ", administrationStandID=" + administrationStandID
                + ", performFreqDictID=" + performFreqDictID
                + ", performFreqDictStandID=" + performFreqDictStandID
                + ", performFreqDictText=" + performFreqDictText
                + ", startDateTime=" + startDateTime + ", stopDateTime="
                + stopDateTime + ", doctorDept=" + doctorDept + ", doctor="
                + doctor + ", isGroup=" + isGroup + ", firmID=" + firmID
                + ", isNew=" + isNew + ", dvaliddate=" + dvaliddate
                + ", adminName=" + adminName + ", useType=" + useType
                + ", useCause=" + useCause + ", PRESC_NO=" + PRESC_NO
                + ", ITEM_NO=" + ITEM_NO + ", AMOUNT=" + AMOUNT + ", COSTS="
                + COSTS + ", UNITS=" + UNITS + ", DRUG_SPEC=" + DRUG_SPEC
                + ", SERIAL_NO=" + SERIAL_NO + ", ITEM_CLASS=" + ITEM_CLASS
                + "]";
    }
    
    

}
