package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagInfo;
import com.ts.entity.pdss.pdss.Beans.TDrugDiagRel;

/**
 * 
 * @author liujc 药品禁忌审查结果
 * 
 */
public class TDrugDiagRslt extends TBaseResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    /* 药物字典 */
    private TDrug         drug ;
    /* 药物禁忌症对应 */
    private TDrugDiagRel  drugDiaRel;
    /* 药物禁忌症信息 */
    private TDrugDiagInfo drugDiaInfo ;
    
    /* 诊断名称 */
    private String   diagName ;

    public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public TDrugDiagRslt()
    {
        setVersion(2);
    }
    public void addDrugDiag(TDrug _drug , TDrugDiagRel _ddr , TDrugDiagInfo _ddi , String diagname  )
    {
        this.drug = _drug;
        this.drugDiaRel = _ddr;
        this.drugDiaInfo = _ddi;
        this.diagName = diagname;
        if("1".equals(_ddi.getINTER_INDI())||"2".equals(_ddi.getINTER_INDI()))
        {
            this.alertLevel = "R";
            this.alertHint  = _ddi.getDIAG_DESC();
            this.alertCause = _ddi.getDiagnosis_dict_name() + ":" + _ddi.getDIAG_DESC(); 
        }
        else
        if("3".equals(_ddi.getINTER_INDI())||"4".equals(_ddi.getINTER_INDI()))
        {
            this.alertLevel = "Y";
            this.alertHint  = _ddi.getDIAG_DESC();
            this.alertCause = _ddi.getDiagnosis_dict_name() + ":" + _ddi.getDIAG_DESC(); 
        }
    }
    
    /**
     * 获得药品字段数组
     * @return
     */
    @XmlElement(name="getDrug")
    public TDrug getDrug()
    {
        return this.drug;
    }
    
    /**
     * 获得药品禁忌症对应
     * @return
     */
    public TDrugDiagRel getDrugDiagRel()
    {
        return this.drugDiaRel;
    }
    
    /**
     *  获得药品禁忌信息
     * @return
     */
    @XmlElement(name="getDrugDiagInfo")
    public TDrugDiagInfo getDrugDiagInfo()
    {
        return this.drugDiaInfo;
    }
}
