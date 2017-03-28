package com.ts.entity.pdss.pdss.Beans;

import com.hitzd.his.Beans.TBaseBean;


/**
 *  药品配伍表
 * @author liujc
 *
 */
public class TDrugIvEffect extends TBaseBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* 配伍码 */
    private String EFFECT_ID;
    /* 药品输液码1 */
    private String IV_CLASS_CODE1;
    /* 生产厂家1 */
    private String FIRM_ID1;
    /* 药品输液码2 */
    private String IV_CLASS_CODE2;
    /* 生产厂家2*/
    private String FIRM_ID2;
    /* 溶媒码 */
    private String IV_CLASS_CODE3;
    /* 配伍结果 */
    private String RESULT_ID;
    /* 用药参考 */
    private String REFER_INFO;
    /* 文献来源 */
    private String REF_SOURCE;
    
    public String getEFFECT_ID()
    {
        return EFFECT_ID;
    }
    public void setEFFECT_ID(String eFFECT_ID)
    {
        EFFECT_ID = eFFECT_ID;
    }
    public String getIV_CLASS_CODE1()
    {
        return IV_CLASS_CODE1;
    }
    public void setIV_CLASS_CODE1(String iV_CLASS_CODE1)
    {
        IV_CLASS_CODE1 = iV_CLASS_CODE1;
    }
    public String getFIRM_ID1()
    {
        return FIRM_ID1;
    }
    public void setFIRM_ID1(String fIRM_ID1)
    {
        FIRM_ID1 = fIRM_ID1;
    }
    public String getIV_CLASS_CODE2()
    {
        return IV_CLASS_CODE2;
    }
    public void setIV_CLASS_CODE2(String iV_CLASS_CODE2)
    {
        IV_CLASS_CODE2 = iV_CLASS_CODE2;
    }
    public String getFIRM_ID2()
    {
        return FIRM_ID2;
    }
    public void setFIRM_ID2(String fIRM_ID2)
    {
        FIRM_ID2 = fIRM_ID2;
    }
    public String getIV_CLASS_CODE3()
    {
        return IV_CLASS_CODE3;
    }
    public void setIV_CLASS_CODE3(String iV_CLASS_CODE3)
    {
        IV_CLASS_CODE3 = iV_CLASS_CODE3;
    }
    public String getRESULT_ID()
    {
        return RESULT_ID;
    }
    public void setRESULT_ID(String rESULT_ID)
    {
        RESULT_ID = rESULT_ID;
    }
    public String getREFER_INFO()
    {
        return REFER_INFO;
    }
    public void setREFER_INFO(String rEFER_INFO)
    {
        REFER_INFO = rEFER_INFO;
    }
    public String getREF_SOURCE()
    {
        return REF_SOURCE;
    }
    public void setREF_SOURCE(String rEF_SOURCE)
    {
        REF_SOURCE = rEF_SOURCE;
    }
    
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result
//                + ((IV_CLASS_CODE1 == null) ? 0 : IV_CLASS_CODE1.hashCode());
//        result = prime * result
//                + ((IV_CLASS_CODE2 == null) ? 0 : IV_CLASS_CODE2.hashCode());
//        
//        return result;
//    }
//    
//    public boolean ObjEquals(String obj1, String obj2)
//    {
//        if (obj1 == null)
//        {
//            if (obj2 != null)
//                return false;
//        }
//        else
//            return obj1.equals(obj2);
//        
//        return true;
//    }
//    
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        TDrugIvEffect other = (TDrugIvEffect) obj;
//        
//        // 修改此处以使得该对象的相等判断可以使平行判断，也可以是交叉判断。
//        
//        if (!ObjEquals(IV_CLASS_CODE1, other.IV_CLASS_CODE1))
//        {
//            if (ObjEquals(IV_CLASS_CODE1, other.IV_CLASS_CODE2))
//            {
//                if (!ObjEquals(IV_CLASS_CODE2, other.IV_CLASS_CODE1))
//                    return false;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        else
//        if (!ObjEquals(IV_CLASS_CODE2, other.IV_CLASS_CODE2))
//            return false;
//        return true;
//    }
}
