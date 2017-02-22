package com.hitzd.his.Beans;

import java.io.Serializable;

public class TPatOrderInfoExt implements Serializable {

    private static final long serialVersionUID = 1L;
    /* 是否哺乳期 */
	private String isLact;
	
	/* 是否孕妇  */
	private String isPregnant;
	
	/* 医保类别  */
	private String insureanceType;
	
	/* 医疗保险号  */
	private String insuranceNo;
	
	/* 肝功能不完全标志  */
	private String isLiverWhole;
	
	/* 肾功能不完全标志  */
	private String isKidneyWhole;
	
	/* 在职标志  */
	//private String isWorking;
	
	/* 身高  */
	private String height;
	
	/* 体重  */
	private String weight;

	/**
	 * 返回是否为肾功不全
	 * @return
	 */
	public boolean TheIsKidneyWhole()
	{
	    if(this.isKidneyWhole != null)
        {
            if("是".equals(this.isKidneyWhole)||"Y".equals(this.isKidneyWhole.toUpperCase())
                    ||"YES".equals(this.isKidneyWhole.toUpperCase())||"TRUE".equals(this.isKidneyWhole.toUpperCase())
                    ||"1".equals(this.isKidneyWhole))
            {
                return true;
            }
        }
	    return false;
	}
	
	/**
	 * 返回是否为肝功不全
	 * @return
	 */
	public boolean TheIsLiverWhole()
	{
	    if(this.isLiverWhole != null)
        {
            if("是".equals(this.isLiverWhole)||"Y".equals(this.isLiverWhole.toUpperCase())
                    ||"YES".equals(this.isLiverWhole.toUpperCase())||"TRUE".equals(this.isLiverWhole.toUpperCase())
                    ||"1".equals(this.isLiverWhole))
            {
                return true;
            }
        }
	    return false;
	}
	
	/**
	 * 返回是否为哺乳期
	 */
	public boolean TheIsLact()
	{
	    if(this.isLact != null)
	    {
    	    if("是".equals(this.isLact)||"Y".equals(this.isLact.toUpperCase())
    	            ||"YES".equals(this.isLact.toUpperCase())||"TRUE".equals(this.isLact.toUpperCase())
    	            ||"1".equals(this.isLact))
    	    {
    	        return true;
    	    }
	    }
	    return false;
	}
	
	
	/**
	 * 返回是否为孕妇
	 * @return
	 */
	public boolean TheIsPregnant()
	{
	    if(this.isPregnant != null)
        {
            if("是".equals(this.isPregnant)||"Y".equals(this.isPregnant.toUpperCase())
                    ||"YES".equals(this.isPregnant.toUpperCase())||"TRUE".equals(this.isPregnant.toUpperCase())
                    ||"1".equals(this.isPregnant))
            {
                return true;
            }
        }
	    return false;
	}
	
	public String getIsLact() {
		return isLact;
	}

	public void setIsLact(String isLact) {
		this.isLact = isLact;
	}

	public String getIsPregnant() {
		return isPregnant;
	}

	public void setIsPregnant(String isPregnant) {
		this.isPregnant = isPregnant;
	}

	public String getInsureanceType() {
		return insureanceType;
	}

	public void setInsureanceType(String insureanceType) {
		this.insureanceType = insureanceType;
	}

	public String getInsuranceNo() {
		return insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public String getIsLiverWhole() {
		return isLiverWhole;
	}

	public void setIsLiverWhole(String isLiverWhole) {
		this.isLiverWhole = isLiverWhole;
	}

	public String getIsKidneyWhole() {
		return isKidneyWhole;
	}

	public void setIsKidneyWhole(String isKidneyWhole) {
		this.isKidneyWhole = isKidneyWhole;
	}

//	public String getIsWorking() {
//		return isWorking;
//	}
//
//	public void setIsWorking(String isWorking) {
//		this.isWorking = isWorking;
//	}

	public String getHeight() 
	{
		if(this.height == null || "".equals(this.height.trim()))
			this.height = "0";
		return height;
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

	public String getWeight() 
	{
		if(weight == null || "".equals(weight.trim()))
			this.weight = "0";
		return weight;
	}

	public void setWeight(String weight)
	{
		this.weight = weight;
	}


	
  
}
