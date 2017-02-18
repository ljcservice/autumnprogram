package com.ts.entity.pdss.pdss.RSBeans;

import java.io.Serializable;
import java.util.List;

import com.ts.entity.pdss.pdss.Beans.TAllergIngrDrug;
import com.ts.entity.pdss.pdss.Beans.TDrug;

/**
 * @description 药物过敏审查结果 返回值类
 *
 */
public class TDrugAllergenRslt extends TBaseResult implements Serializable {


	private static final long serialVersionUID = 1L;
	/*
	 *  药品
	 */
	private TDrug drus ;
	/*
	 * 过敏信息
	 */
	private List<TAllergIngrDrug>  drugAllergen ;
	
	public TDrugAllergenRslt()
    {
	    setVersion(2);
    }
	/**
	 * 获得药品所有对象数组
	 * @return
	 */
	public TDrug getDrug()
	{
	    return this.drus;
	}
	
	/**
	 * 添加药品对象
	 * @param drug
	 */
	public void addDrug(TDrug _drug,List<TAllergIngrDrug> listAllergen)
	{
	    this.drus         = _drug;
	    this.drugAllergen = listAllergen;
	    this.alertLevel   = "R";
	}
	
	/**
	 * 返回过敏信息
	 * @return
	 */
	public TAllergIngrDrug[] getDrugAllergen()
	{
	    return (TAllergIngrDrug[])this.drugAllergen.toArray(new TAllergIngrDrug[0]);
	}
	
}
