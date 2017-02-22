package com.hitzd.his.Beans;

/**
 * 手术字典信息 
 * @author Administrator
 *
 */
public class TOperationDict extends TBaseBean
{
    private static final long serialVersionUID = -1166457621702576200L;
    
    /* 手术代码 */
    private String OperationCode ;
    /* 手术名字 */
    private String OperationName ;
    /* 输入码  */
    private String inputCode;
    public String getOperationCode()
    {
        return OperationCode;
    }
    public void setOperationCode(String operationCode)
    {
        OperationCode = operationCode;
    }
    public String getOperationName()
    {
        return OperationName;
    }
    public void setOperationName(String operationName)
    {
        OperationName = operationName;
    }
    public String getInputCode()
    {
        return inputCode;
    }
    public void setInputCode(String inputCode)
    {
        this.inputCode = inputCode;
    }
}
