package com.ts.entity.pdss.Saver;

import java.util.UUID;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.ias.RSBeans.TAntiDrugSecurityResult;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;

/**
 * 队列BeanRS
 * @author Administrator
 */
public class SaveBeanRS
{
    private String id = UUID.randomUUID().toString();
	/* 医嘱 */
	private TPatientOrder  po;
	/* 总体检查对象  */
	private TCheckResultCollection checkRC;
	/* 抗菌药审查结果  */
	private TAntiDrugSecurityResult[] adsr;  
	
    public String getID()
    {
        return id;
    }
	   
	public TPatientOrder getPo() 
	{
		return po;
	}
	
	public void setPo(TPatientOrder po)
	{
		this.po = po;
	}
	
	public TCheckResultCollection getCheckRC()
	{
		return checkRC;
	}
	
	public void setCheckRC(TCheckResultCollection checkRC) 
	{
		this.checkRC = checkRC;
	}

    public TAntiDrugSecurityResult[] getAdsr()
    {
        return adsr;
    }

    public void setAdsr(TAntiDrugSecurityResult[] adsr)
    {
        this.adsr = adsr;
    }
}
