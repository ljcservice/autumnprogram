package com.ts.entity.pdss.CustomAudit.RSBeans;

import java.util.HashMap;
import java.util.Map;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TBaseResult;

/**
 *  顾客自定义审核
 * @author Administrator
 *
 */
public class TDrugCustomSecurityRslt extends TBaseResult
{
    private static final long serialVersionUID = 1L;

    /* 医嘱对象 */
    private TPatientOrder po ;
    
    public TPatientOrder getPo()
    {
        return po;
    }

    public void setPo(TPatientOrder po)
    {
        this.po = po;
    }

    /* 药品问题集合  key 为(药品码,主医嘱,子医嘱) value 药品审核内容  */
    private Map<String, TCustomCheckRslt> chkRslt = new HashMap<String, TCustomCheckRslt>();
    
    public void addDrugCustomCheckRslt(String drugInfo , TCustomCheckRslt ccrslt)
    {
        chkRslt.put(drugInfo,ccrslt);
    }
    
    public String[] getKey()
    {
        return this.chkRslt.keySet().toArray(new String[0]);
    }
    
    public TCustomCheckRslt getCheckRslt(String DrugKey)
    {
        return chkRslt.get(DrugKey);
    }

    public TCustomCheckRslt[] getCheckResults()
    {
        String[] keys = getKey();
        TCustomCheckRslt[] rslt  = new TCustomCheckRslt[keys.length];

        for(int i = 0 ; i < keys.length ; i++)
        {
            rslt[i] = getCheckRslt(keys[i]);
        }
        return rslt;
    }
}
