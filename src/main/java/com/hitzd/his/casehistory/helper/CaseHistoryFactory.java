package com.hitzd.his.casehistory.helper;

import java.util.HashMap;
import java.util.Map;

import com.hitzd.his.Beans.Middle.TTableConfig;
import com.hitzd.his.Utils.Config;
import com.hitzd.his.report.utils.JillClassLoader;

public class CaseHistoryFactory 
{
	public static synchronized ICaseHistoryHelper getCaseHistoryHelper()
	{
		try 
		{
			String chhName = Config.getParamValue("CaseHistoryHelper");
			chhName = chhName.length() == 0 ? "com.hitzd.his.casehistory.helper.CaseHistoryJWYH" : chhName;
			return (ICaseHistoryHelper) JillClassLoader.loadClass(chhName).newInstance();
		} 
		catch (Exception e) 
		{
			System.out.print(e);
		}
		return null;
	}
	
	public static synchronized Map<String, TTableConfig> getTableMap()
	{
		Map<String, TTableConfig> tables = new HashMap<String, TTableConfig>();
		try
		{
			tables = Config.getTableMap();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return tables;
	}
	
    private CaseHistoryFactory()
    {
        // TODO Auto-generated constructor stub
    }
 /*   
    private static ICaseHistoryHelper chhr ;
    private static String CaseHistoryHelperConfig = "" ;
    
    private static synchronized ICaseHistoryHelper BuildObj() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException 
    {
        if(!CaseHistoryHelperConfig.equals(Config.getParamValue("CaseHistoryHelper")))
        {
            CaseHistoryHelperConfig = Config.getParamValue("CaseHistoryHelper");
            chhr = null;
        }
        CaseHistoryHelperConfig = CaseHistoryHelperConfig.length() == 0 ? "com.hitzd.his.casehistory.helper.CaseHistoryJWYH" : CaseHistoryHelperConfig;
        if(chhr == null)
        {
            chhr = (ICaseHistoryHelper) JillClassLoader.loadClass(CaseHistoryHelperConfig).getConstructor().newInstance();
        } 
        return chhr;
    }
    
    public static synchronized ICaseHistoryHelper getCaseHistoryHelper()
    {
        try 
        {
            return BuildObj();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    */
}
