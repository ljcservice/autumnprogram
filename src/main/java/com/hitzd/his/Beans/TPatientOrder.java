package com.hitzd.his.Beans;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.BeansException;

import com.hitzd.springBeanManager.SpringBeanUtil;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.service.pdss.pdss.Cache.PdssCache;

/**
 * 医嘱对象
 * 
 * @author Administrator
 * 
 */
public class TPatientOrder implements java.io.Serializable
{

    private static final long        serialVersionUID        = 1L;
    /* 病人ID */
    private String                   patientID               = null;
    /* 门诊 住院标示  指明审核数据的来源，1，门诊事实 2，门诊处方点评，3，临床医嘱事实 4，临床医嘱点评  */
    private String                   patType                 = null;
    /* 病人主要信息 */
    private TPatient                 patient                 = null;
    /* 病人就诊扩展信息 */
    private TPatOrderInfoExt         patInfoExt              = null;
    /* 病人住院信息 */
    private TPatOrderVisitInfo       patVisitInfo            = null;
    /* 病人用药记录 */
    private TPatOrderDrug[]          patOrderDrugs           = null;
    /* 病人体征信息 */
    private TPatSigns[]              patSigns                = null;
    /* 医嘱诊断信息 */
    private TPatOrderDiagnosis[]     patOrderDiagnosiss      = null;
    /* 医嘱药物过敏信息 */
    private TPatOrderDrugSensitive[] patOrderDrugSensitives  = null;
    /* 开具医嘱医生ID */
    private String                   doctorID                = null;
    /* 开具医嘱医生 */
    private String                   doctorName              = null;
    /* 开具医嘱科室 */
    private String                   doctorDeptID            = null;
    /* 开具医嘱科室 */
    private String                   doctorDeptName          = null;
    /* 开具医嘱职称 */
    private String                   doctorTitleID           = null;
    /* 开具医嘱职称 */
    private String                   doctorTitleName         = null;
    /* 红色问题医嘱变动数 */
    private String                   redQuestionChangeNum    = null;
    /* 黄色问题医嘱变动数 */
    private String                   yellowQuestionChangeNum = null;
    /* 手术记录 */
    private TPatOperation[]          patOperation           = null;
    /* 预防用药信息  */
    private TPreveUseDrug[]          preveUseDrug           = null;
    /* 治疗用药信息  */
    private TTreatUseDrug[]          treatUseDrug           = null;
    
    private Map<String, TDrug>       drugMap                = null;
    
    
    public String getPatType()
    {
        return patType;
    }

    public TDrug getDrugMap(String drugCode){
        if(drugMap.containsKey(drugCode))
        {
            return new TDrug(drugMap.get(drugCode));
        }
        return null;
    }
    
    public Map<String, TDrug> DrugMap(){
        return drugMap;
    }
    
    public void setPatType(String patType)
    {
        this.patType = patType;
    }

    public TPatOperation[] getPatOperation()
    {
		return patOperation;
	}

	public void setPatOperation(TPatOperation[] patOperation)
	{
		this.patOperation = patOperation;
	}

	public String getPatientID()
    {
        return patientID;
    }

    public void setPatientID(String patientID)
    {
        this.patientID = patientID;
    }

    public TPatient getPatient()
    {
        return patient;
    }

    public void setPatient(TPatient patient)
    {
        this.patient = patient;
    }

    public TPatOrderInfoExt getPatInfoExt()
    {
        return patInfoExt;
    }

    public void setPatInfoExt(TPatOrderInfoExt patInfoExt)
    {
        this.patInfoExt = patInfoExt;
    }

    public TPatOrderVisitInfo getPatVisitInfo()
    {
        return patVisitInfo;
    }

    public void setPatVisitInfo(TPatOrderVisitInfo patVisitInfo)
    {
        this.patVisitInfo = patVisitInfo;
    }

    public TPatSigns[] getPatSigns()
    {
        return patSigns;
    }

    public void setPatSigns(TPatSigns[] patSigns)
    {
        this.patSigns = patSigns;
    }

    public TPatOrderDrug[] getPatOrderDrugs()
    {
        
        return patOrderDrugs;
    }

    public void setPatOrderDrugs(TPatOrderDrug[] patOrderDrugs)
    {
        this.patOrderDrugs = patOrderDrugs;
        try
        {
            drugMap = ((PdssCache)SpringBeanUtil.getBean("pdssCache")).queryDrugMap(this.patOrderDrugs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public TPatOrderDiagnosis[] getPatOrderDiagnosiss()
    {
        return patOrderDiagnosiss;
    }

    public void setPatOrderDiagnosiss(TPatOrderDiagnosis[] patOrderDiagnosiss)
    {
        this.patOrderDiagnosiss = patOrderDiagnosiss;
    }

    public TPatOrderDrugSensitive[] getPatOrderDrugSensitives()
    {
        return patOrderDrugSensitives;
    }

    public void setPatOrderDrugSensitives(
            TPatOrderDrugSensitive[] patOrderDrugSensitives)
    {
        this.patOrderDrugSensitives = patOrderDrugSensitives;
    }

    public String getDoctorID()
    {
        return doctorID;
    }

    public void setDoctorID(String doctorID)
    {
        this.doctorID = doctorID;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getDoctorDeptID()
    {
        return doctorDeptID;
    }

    public void setDoctorDeptID(String doctorDeptID)
    {
        this.doctorDeptID = doctorDeptID;
    }

    public String getDoctorDeptName()
    {
        return doctorDeptName;
    }

    public void setDoctorDeptName(String doctorDeptName)
    {
        this.doctorDeptName = doctorDeptName;
    }

    public String getDoctorTitleID()
    {
        return doctorTitleID;
    }

    public void setDoctorTitleID(String doctorTitleID)
    {
        this.doctorTitleID = doctorTitleID;
    }

    public String getDoctorTitleName()
    {
        return doctorTitleName;
    }

    public void setDoctorTitleName(String doctorTitleName)
    {
        this.doctorTitleName = doctorTitleName;
    }

    public String getRedQuestionChangeNum()
    {
        return redQuestionChangeNum;
    }

    public void setRedQuestionChangeNum(String redQuestionChangeNum)
    {
        this.redQuestionChangeNum = redQuestionChangeNum;
    }

    public String getYellowQuestionChangeNum()
    {
        return yellowQuestionChangeNum;
    }

    public void setYellowQuestionChangeNum(String yellowQuestionChangeNum)
    {
        this.yellowQuestionChangeNum = yellowQuestionChangeNum;
    }

    public TPreveUseDrug[] getPreveUseDrug()
    {
        return preveUseDrug;
    }

    public void setPreveUseDrug(TPreveUseDrug[] preveUseDrug)
    {
        this.preveUseDrug = preveUseDrug;
    }

    public TTreatUseDrug[] getTreatUseDrug()
    {
        return treatUseDrug;
    }

    public void setTreatUseDrug(TTreatUseDrug[] treatUseDrug)
    {
        this.treatUseDrug = treatUseDrug;
    }

    @Override
    public String toString()
    {
        
        String poDrug = "";
        if(patOrderDrugs != null )
        {
            for( TPatOrderDrug podrug : patOrderDrugs)
            {
                poDrug += "[" + podrug.toString()+ "],";
            }
        }
        String poSigns = "";
        if(patSigns != null)
        {
            for(TPatSigns ps : patSigns)
            {
                poSigns += "[" + ps.toString() + "],";
            }
        }
        
        String  poDiagnosiss = "";
        if(patOrderDiagnosiss != null)
        {
            for(TPatOrderDiagnosis diag : patOrderDiagnosiss)
            {
                poDiagnosiss += "[" + diag.toString() + "],";
            }
        }
        if(patient == null ) patient = new TPatient();
        if(patInfoExt == null) patInfoExt =new TPatOrderInfoExt();
        
        
        
        return "TPatientOrder [patientID=" + patientID + ", patType=" + patType
                + ", patient=" + patient.toString() + ", patInfoExt=" + patInfoExt.toString()
                + ", patVisitInfo=" + patVisitInfo.toString() + ", patOrderDrugs="
                + poDrug + ", patSigns="
                + poSigns + ", patOrderDiagnosiss="
                + poDiagnosiss
                + ", patOrderDrugSensitives="
                + Arrays.toString(patOrderDrugSensitives) + ", doctorID="
                + doctorID + ", doctorName=" + doctorName + ", doctorDeptID="
                + doctorDeptID + ", doctorDeptName=" + doctorDeptName
                + ", doctorTitleID=" + doctorTitleID + ", doctorTitleName="
                + doctorTitleName + ", redQuestionChangeNum="
                + redQuestionChangeNum + ", yellowQuestionChangeNum="
                + yellowQuestionChangeNum + ", patOperation="
                + Arrays.toString(patOperation) + ", preveUseDrug="
                + Arrays.toString(preveUseDrug) + ", treatUseDrug="
                + Arrays.toString(treatUseDrug) + ", drugMap=" + drugMap + "]";
    }
    
}
