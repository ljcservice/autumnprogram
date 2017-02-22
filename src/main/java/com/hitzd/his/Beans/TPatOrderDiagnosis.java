package com.hitzd.his.Beans;

import com.hitzd.his.Utils.Config;
import com.hitzd.his.Utils.DictCache;

/**
 * @description 医嘱诊断信息类：PatOrderDiagnosis 对应数据库表：医嘱诊断信息(Pat_Order_Diagnosis)
 * @author
 */
public class TPatOrderDiagnosis
{

    /* 诊断ID*/
    private String diagnosisDictID;

    /* 诊断名称 */
    private String diagnosisName;
    // 诊断代码*/

    // 急慢性标志 1急性3慢性2中间
    // private String acuteFlag;
    // // 是否新增
    // private String isNew;

    // public String getAcuteFlag() {
    // return acuteFlag;
    // }
    // public void setAcuteFlag(String acuteFlag) {
    // this.acuteFlag = acuteFlag;
    // }
    // public String getIsNew() {
    // return isNew;
    // }
    // public void setIsNew(String isNew) {
    // this.isNew = isNew;
    // }
    //
    public String getDiagnosisDictID()
    {
        return diagnosisDictID;
    }

    public void setDiagnosisDictID(String diagnosisDictID)
    {
    	String diagId = diagnosisDictID;
    	this.diagnosisDictID = diagId; 
    	if(Config.getParamValue("diag_icd9_conv_flag").equals("1") || Config.getParamValue("diag_icd10_conv_flag").equals("1") )
    	{
    	    this.diagnosisDictID = diagnosisDictID ;
    	}
    	else
    	if(Config.getParamValue("diagnosis_conv_flag").equals("1"))
        {
    		diagId = DictCache.getNewInstance().getDiagnosisByName(diagId).get("diagnosis_code");
    		this.setDiagnosisName(diagnosisDictID);
        }
    	if(!"".equals(diagId)&& diagId != null) this.diagnosisDictID = diagId;
    }

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
   
    
}
