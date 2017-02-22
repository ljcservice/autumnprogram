package com.hitzd.his.Beans;

/**
 * 检验项目
 * @author Administrator
 *
 */
public class TLabTestItems extends TBaseBean
{
    private String TEST_NO;
    private String ITEM_NO;  
    private String ITEM_NAME;
    private String ITEM_CODE;
    
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
    public String getITEM_NAME()
    {
        return ITEM_NAME;
    }
    public void setITEM_NAME(String iTEM_NAME)
    {
        ITEM_NAME = iTEM_NAME;
    }
    public String getITEM_CODE()
    {
        return ITEM_CODE;
    }
    public void setITEM_CODE(String iTEM_CODE)
    {
        ITEM_CODE = iTEM_CODE;
    }
}
