package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import com.hitzd.his.Beans.TPatOrderDrug;
import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;

/**
 * @description 药品给药途径审查结果
 */
public class TAdministrationRslt extends TBaseResult implements Serializable
{

    private static final long serialVersionUID = 1L;
    /* 病人用药记录类 */
    private TPatOrderDrug     pod;
    /* 药品字典表          */
    private TDrug             drug;
    /* 药品用药信息     */
    private TDrugUseDetail    dud;
    
    public TAdministrationRslt()
    {
        setVersion(2);
    }
    
    public void addRslt(TPatOrderDrug _pod, TDrug _drug, TDrugUseDetail _dud, String _infoLevle)
    {
        this.pod        = _pod;
        this.recMainNo  = _pod.getRecMainNo();
        this.recSubNo   = _pod.getRecSubNo();
        this.drug       = _drug;
        this.dud        = _dud;
        this.alertLevel = _infoLevle;
    }

    /**
     * 病人用药记录类 里面含有 医嘱序号 .等等信息
     * 
     * @return
     */
    @XmlElement(name="getPatOrderDrug")
    public TPatOrderDrug getPatOrderDrug()
    {
        return this.pod;
    }

    /**
     * 返回药品
     * 
     * @return
     */
    @XmlElement(name="getDrug")
    public TDrug getDrug()
    {
        return this.drug;
    }

    /**
     * 药品用药信息
     * 
     * @return
     */
    @XmlElement(name="getDrugUseDetail")
    public TDrugUseDetail getDrugUseDetail()
    {
        return this.dud;
    }
}
