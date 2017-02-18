package com.ts.entity.pdss.pdss.RSBeans;

import java.util.ArrayList;
import java.util.List;

import com.ts.entity.pdss.mas.Beans.TMedicareCatalog;
import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.entity.pdss.pdss.Beans.TDrug;

public class TMedicareRslt extends TBaseResult 
{
    /* 药品 */
    private TDrug  drug;
	/* */
	private TMedicareCatalog medicareCatalog;
	/* 适应症 */
	private List<TMemo> memo = new ArrayList<TMemo>();
	/* 是否医保药品 */
	private boolean flag = false;
	/* 是否医保药品提示 */
	private String AlertInfo;
	/* */
    private String RemarkInfo;
	
    
    public boolean isFlag()
    {
        return flag;
    }
    
    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }
    
    public void setMemo(List<TMemo> value)
    {
        setFlag(true);
        this.memo = value;
    }
    public TMemo[] getMemo()
    {
        return (TMemo[])this.memo.toArray(new TMemo[0]);
    }
    
    public List<TMemo> getListTMemo()
    {
        return this.memo;
    }
	public TMedicareCatalog getMedicareCatalog()
    {
        return medicareCatalog;
    }
    public void setMedicareCatalog(TMedicareCatalog medicareCatalog)
    {
        this.medicareCatalog = medicareCatalog;
    }
    public TMedicareRslt()
    {
	    setVersion(3);
    }
	public String getRemarkInfo() {
		return RemarkInfo;
	}

	public void setRemarkInfo(String remarkInfo) {
		RemarkInfo = remarkInfo;
	}

	public String getAlertInfo() {
		return AlertInfo;
	}

	public void setAlertInfo(String alertInfo) {
		AlertInfo = alertInfo;
	}
	
    public TDrug getDrug()
    {
        return drug;
    }
    public void setDrug(TDrug drug)
    {
        this.drug = drug;
    }
}
