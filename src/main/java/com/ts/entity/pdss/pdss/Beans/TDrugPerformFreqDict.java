package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 * 医嘱执行频率字典
 * @author liujc
 *
 */
public class TDrugPerformFreqDict extends TBaseBean
{
	private static final long serialVersionUID = 1L;
	/* ID */
    private String PERFORM_FREQ_DICT_ID;
    /* 执行频率描述 */
    private String PERFORM_FREQ_DICT_NAME;
    /* 频率次数 */
    private String FREQ_COUNTER;
    /* 频率间隔 */
    private String FREQ_INTERVAL;
    /* 频率间隔单位 */
    private String FREQ_INTERVAL_UNITS;
    /* 执行频率字典本地编码  */
    private String PERFORM_FREQ_DICT_NO_LOCAL;
    /* 执行频率本地描述 */
    private String PERFORM_FREQ_DICT_NAME_LOCAL;
    /* 操作人 */
    private String OPER_USER;
    /* 操作时间 */
    private String OPER_TIME;
    
    public String getPERFORM_FREQ_DICT_ID()
    {
        return PERFORM_FREQ_DICT_ID;
    }
    public void setPERFORM_FREQ_DICT_ID(String pERFORM_FREQ_DICT_ID)
    {
        PERFORM_FREQ_DICT_ID = pERFORM_FREQ_DICT_ID;
    }
    public String getPERFORM_FREQ_DICT_NAME()
    {
        return PERFORM_FREQ_DICT_NAME;
    }
    public void setPERFORM_FREQ_DICT_NAME(String pERFORM_FREQ_DICT_NAME)
    {
        PERFORM_FREQ_DICT_NAME = pERFORM_FREQ_DICT_NAME;
    }
    public String getFREQ_COUNTER()
    {
        return FREQ_COUNTER;
    }
    public void setFREQ_COUNTER(String fREQ_COUNTER)
    {
        FREQ_COUNTER = fREQ_COUNTER;
    }
    public String getFREQ_INTERVAL()
    {
        return FREQ_INTERVAL;
    }
    public void setFREQ_INTERVAL(String fREQ_INTERVAL)
    {
        FREQ_INTERVAL = fREQ_INTERVAL;
    }
    public String getFREQ_INTERVAL_UNITS()
    {
        return FREQ_INTERVAL_UNITS;
    }
    public void setFREQ_INTERVAL_UNITS(String fREQ_INTERVAL_UNITS)
    {
        FREQ_INTERVAL_UNITS = fREQ_INTERVAL_UNITS;
    }
    public String getPERFORM_FREQ_DICT_NO_LOCAL()
    {
        return PERFORM_FREQ_DICT_NO_LOCAL;
    }
    public void setPERFORM_FREQ_DICT_NO_LOCAL(String pERFORM_FREQ_DICT_NO_LOCAL)
    {
        PERFORM_FREQ_DICT_NO_LOCAL = pERFORM_FREQ_DICT_NO_LOCAL;
    }
    public String getPERFORM_FREQ_DICT_NAME_LOCAL()
    {
        return PERFORM_FREQ_DICT_NAME_LOCAL;
    }
    public void setPERFORM_FREQ_DICT_NAME_LOCAL(String pERFORM_FREQ_DICT_NAME_LOCAL)
    {
        PERFORM_FREQ_DICT_NAME_LOCAL = pERFORM_FREQ_DICT_NAME_LOCAL;
    }
    public String getOPER_USER()
    {
        return OPER_USER;
    }
    public void setOPER_USER(String oPER_USER)
    {
        OPER_USER = oPER_USER;
    }
    public String getOPER_TIME()
    {
        return OPER_TIME;
    }
    public void setOPER_TIME(String oPER_TIME)
    {
        OPER_TIME = oPER_TIME;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((PERFORM_FREQ_DICT_NO_LOCAL == null) ? 0
                        : PERFORM_FREQ_DICT_NO_LOCAL.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TDrugPerformFreqDict other = (TDrugPerformFreqDict) obj;
        if (PERFORM_FREQ_DICT_NO_LOCAL == null)
        {
            if (other.PERFORM_FREQ_DICT_NO_LOCAL != null)
                return false;
        }
        else if (!PERFORM_FREQ_DICT_NO_LOCAL
                .equals(other.PERFORM_FREQ_DICT_NO_LOCAL))
            return false;
        return true;
    }
    
    
}
