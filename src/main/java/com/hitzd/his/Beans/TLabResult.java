package com.hitzd.his.Beans;

/**
 * 检验结果
 * @author Administrator
 *
 */
public class TLabResult extends TBaseBean
{
    private static final long serialVersionUID = 1L;
    
    private String TEST_NO;           
    private String ITEM_NO;           
    private String PRINT_ORDER;       
    private String REPORT_ITEM_NAME;  
    private String REPORT_ITEM_CODE;  
    private String RESULT;            
    private String UNITS;             
    private String ABNORMAL_INDICATOR;
    private String INSTRUMENT_ID;     
    private String RESULT_DATE_TIME;  
    private String PRINT_CONTEXT;

    public String getTEST_NO()
    {
        return TEST_NO;
    }
    public void setTEST_NO(String tEST_NO)
    {
        TEST_NO = tEST_NO;
    }
    public String getITEM_NO()
    {
        return ITEM_NO;
    }
    public void setITEM_NO(String iTEM_NO)
    {
        ITEM_NO = iTEM_NO;
    }
    public String getPRINT_ORDER()
    {
        return PRINT_ORDER;
    }
    public void setPRINT_ORDER(String pRINT_ORDER)
    {
        PRINT_ORDER = pRINT_ORDER;
    }
    public String getREPORT_ITEM_NAME()
    {
        return REPORT_ITEM_NAME;
    }
    public void setREPORT_ITEM_NAME(String rEPORT_ITEM_NAME)
    {
        REPORT_ITEM_NAME = rEPORT_ITEM_NAME;
    }
    public String getREPORT_ITEM_CODE()
    {
        return REPORT_ITEM_CODE;
    }
    public void setREPORT_ITEM_CODE(String rEPORT_ITEM_CODE)
    {
        REPORT_ITEM_CODE = rEPORT_ITEM_CODE;
    }
    public String getRESULT()
    {
        return RESULT;
    }
    public void setRESULT(String rESULT)
    {
        RESULT = rESULT;
    }
    public String getUNITS()
    {
        return UNITS;
    }
    public void setUNITS(String uNITS)
    {
        UNITS = uNITS;
    }
    public String getABNORMAL_INDICATOR()
    {
        return ABNORMAL_INDICATOR;
    }
    public void setABNORMAL_INDICATOR(String aBNORMAL_INDICATOR)
    {
        ABNORMAL_INDICATOR = aBNORMAL_INDICATOR;
    }
    public String getINSTRUMENT_ID()
    {
        return INSTRUMENT_ID;
    }
    public void setINSTRUMENT_ID(String iNSTRUMENT_ID)
    {
        INSTRUMENT_ID = iNSTRUMENT_ID;
    }
    public String getRESULT_DATE_TIME()
    {
        return RESULT_DATE_TIME;
    }
    public void setRESULT_DATE_TIME(String rESULT_DATE_TIME)
    {
        RESULT_DATE_TIME = rESULT_DATE_TIME;
    }
    public String getPRINT_CONTEXT()
    {
        return PRINT_CONTEXT;
    }
    public void setPRINT_CONTEXT(String pRINT_CONTEXT)
    {
        PRINT_CONTEXT = pRINT_CONTEXT;
    }
}
