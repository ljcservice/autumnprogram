package com.ts.entity.pdss.ias.Beans;


/**
 * 药品信息 
 * @author Administrator
 *
 */
public class TAntiDrugInput 
{
    /* 药品代码*/
	private String DrugID;
	/* 药品名称*/
	private String DrugName;
	/* 医嘱序号*/
	private String RecMainNo;
	/* 子医嘱*/
	private String RecSubNo;
	/* 医嘱开始时间 */
	private String StartDateTime;
	/* 医嘱结束时间 */
	private String StopDateTime;
	/* 医生 */
	private TDoctor doctor;
	/* 手术 */
	private TOperator operator;
	/* 诊断 */
	private String[] diagnosis;
	/* 病人编号*/
	private String  patientId ;
	/* 住院号*/
	private String  visi_id ; 
	/* 使用类型 */
	private String useType;
	
	public String getUseType()
    {
        return useType;
    }

    public void setUseType(String useType)
    {
        this.useType = useType;
    }

    public String getPatientId()
	{
		return patientId;
	}
	
	public void setPatientId(String patientId)
	{
		this.patientId = patientId;
	}
	
	public String getVisi_id()
	{
		return visi_id;
	}
	
	public void setVisi_id(String visi_id)
	{
		this.visi_id = visi_id;
	}
	public String getDrugID() 
	{
		return DrugID;
	}
	public void setDrugID(String drugID) 
	{
		DrugID = drugID;
	}
	public String getDrugName() 
	{
		return DrugName;
	}
	public void setDrugName(String drugName) 
	{
		DrugName = drugName;
	}
	public String getRecMainNo() 
	{
		return RecMainNo;
	}
	public void setRecMainNo(String recMainNo) 
	{
		RecMainNo = recMainNo;
	}
	public String getRecSubNo() 
	{
		return RecSubNo;
	}
	public void setRecSubNo(String recSubNo) 
	{
		RecSubNo = recSubNo;
	}
	public String getStartDateTime() 
	{
		return StartDateTime;
	}
	public void setStartDateTime(String startDateTime) 
	{
		StartDateTime = startDateTime;
	}
	public String getStopDateTime()
	{
		return StopDateTime;
	}
	public void setStopDateTime(String stopDateTime)
	{
		StopDateTime = stopDateTime;
	}
	public TDoctor getDoctor() 
	{
		return doctor;
	}
	public void setDoctor(TDoctor doctor)
	{
		this.doctor = doctor;
	}
	public TOperator getOperator() 
	{
		return operator;
	}
	public void setOperator(TOperator operator)
	{
		this.operator = operator;
	}
	public String[] getDiagnosis()
	{
		return diagnosis;
	}
	public void setDiagnosis(String[] diagnosis)
	{
		this.diagnosis = diagnosis;
	}
}
