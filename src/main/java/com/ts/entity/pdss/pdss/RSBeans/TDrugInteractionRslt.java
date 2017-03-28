package com.ts.entity.pdss.pdss.RSBeans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;

/**
 * 互动信息
 * @author liujc
 *
 */
public class TDrugInteractionRslt extends TBaseResult 
{
	private static final long serialVersionUID = 1L;
	/* 本地药品A */
	private TDrug DrugA;
	/* 本地药品B */
	private TDrug DrugB;
	private List<TDrugInteractionInfo> drugInfos = new ArrayList<TDrugInteractionInfo>();
    /* 医嘱序号*/
    private String recMainNo2;
    /* 医嘱子序号*/
    private String recSubNo2;
    
    public TDrugInteractionRslt deepClone() throws IOException, ClassNotFoundException 
    {
    	//首先将对象写到流里
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo    = new ObjectOutputStream(bo);
        oo.writeObject(this);
        //然后将对象从流里读出来
        ByteArrayInputStream bi     = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi        = new ObjectInputStream(bi);
        TDrugInteractionRslt Result = (TDrugInteractionRslt)oi.readObject();
        return Result;
    }

    @XmlElement(name="getListDruginfo")
    public List<TDrugInteractionInfo> getListDruginfo()
    {
    	return drugInfos;
    }
    
    public TDrugInteractionRslt()
    {
        setVersion(2);
    }
    
    public void addDrugInfo(TDrug a, TDrug b, List<TDrugInteractionInfo> drugInfo)
	{
		DrugA = a;
		DrugB = b;
		drugInfos = drugInfo;
		setAlertLevel("");
		setAlertHint("");
        setAlertCause("");
		for (int i = 0; i < drugInfo.size(); i++)
		{
	        if (("1").equals(drugInfo.get(i).getINTER_INDI()) || ("2").equals(drugInfo.get(i).getINTER_INDI()))
	        {
	            setAlertLevel("R");
	            setAlertHint(drugInfo.get(i).getMEC_INFO());
	            setAlertCause(drugInfo.get(i).getWAR_INFO());
	        }
	        else if(("3").equals(drugInfo.get(i).getINTER_INDI()) || ("4").equals(drugInfo.get(i).getINTER_INDI()))
	        {
	            if(!"R".equals(this.getAlertLevel()))
	            {
	            	setAlertLevel("Y");
	            	setAlertHint(drugInfo.get(i).getMEC_INFO());
		            setAlertCause(drugInfo.get(i).getWAR_INFO());
	            }
	        }
		}
		if("Y".equals(this.getAlertLevel()))
		{
		    this.YellowCount++;
		}
		else if("R".equals(this.getAlertLevel()))
		{
		    this.RedCount++;
		}
		setAlertLevel(this.RedCount > 0 ? "R" : this.YellowCount > 0 ? "Y" : "");
	}
    
    public void setDrugA(TDrug value)
    {
    	DrugA = value;
    }
    
    public void setDrugB(TDrug value)
    {
    	DrugB = value;
    }
    
	public TDrug getDrugA()
	{
		return DrugA;
	}
	
	public TDrug getDrugB()
	{
		return DrugB;
	}
	
	/**
	 * @return
	 */
	public TDrugInteractionInfo[] getDrugInteractionInfo()
	{
	    return drugInfos.toArray(new TDrugInteractionInfo[0]);
	}
    public String getRecMainNo2()
    {
        return recMainNo2;
    }
    public void setRecMainNo2(String recMainNo2)
    {
        this.recMainNo2 = recMainNo2;
    }
    public String getRecSubNo2()
    {
        return recSubNo2;
    }
    public void setRecSubNo2(String recSubNo2)
    {
        this.recSubNo2 = recSubNo2;
    }
}