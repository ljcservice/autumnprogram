package com.ts.entity.pdss.pdss.RSBeans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TCheckResult 
{
	private String enter = "#A#D";
	public String getCheckResult()
    {
		String checkResult  = "";
		checkResult += "给药途径审查结果    -红灯：" + admRedCount + ", 黄灯：" + admYellowCount + enter;
		checkResult += "过敏审查结果              -红灯：" + dagRedCount + ", 黄灯：" + dagYellowCount + enter;
		checkResult += "禁忌审查结果              -红灯：" + ddiRedCount + ", 黄灯：" + ddiYellowCount + enter;
		checkResult += "剂量审查结果              -红灯：" + ddgRedCount + ", 黄灯：" + ddgYellowCount + enter;
		checkResult += "重复成份审查结果    -红灯：" + didRedCount + ", 黄灯：" + didYellowCount + enter;
		checkResult += "相互作用审查结果    -红灯：" + diaRedCount + ", 黄灯：" + diaYellowCount + enter;
		checkResult += "配伍审查结果              -红灯：" + dieRedCount + ", 黄灯：" + dieYellowCount + enter;
		checkResult += "特殊人群审查结果    -红灯：" + dspRedCount + ", 黄灯：" + dspYellowCount + enter;
		checkResult += "不良反应审查结果    -红灯：" + dhfRedCount + ", 黄灯：" + dhfYellowCount + enter;
        return checkResult;
    }

	public String getAlertLevel()
	{
		if ((diaRedCount + dieRedCount + admRedCount + dagRedCount + ddgRedCount + 
			 ddiRedCount + dhfRedCount + didRedCount + dspRedCount) > 0)
			return "R";
		else
		if ((diaYellowCount + dieYellowCount + admYellowCount + dagYellowCount + ddgYellowCount + 
			 ddiYellowCount + dhfYellowCount + didYellowCount + dspYellowCount) > 0)
			return "Y";
		else
			return "-";
	}
	
	//药物给药途径审查结果对象
	private List<TAdministrationRslt>  admList = new ArrayList<TAdministrationRslt>();
	public void addAdminRslt(TAdministrationRslt adminr)
	{
		// TODO: 统计红黄灯数量
		admList.add(adminr);
		if("R".equals(adminr.alertLevel))
		    this.admRedCount++;
		else if("Y".equals(adminr.alertLevel))
		    this.admYellowCount++;
	}
	private int admRedCount = 0;
	private int admYellowCount = 0;
	public void CopyADMRsltTo(TCheckResult cr)
	{
		cr.admList = this.admList;
		cr.admRedCount = this.admRedCount;
		cr.admYellowCount = this.admYellowCount;
	}
	
	/**
	 * 药物给药途径审查结果对象
	 * @return
	 */
	@XmlElement(name="getAdministrationRslt")
	public TAdministrationRslt[] getAdministrationRslt()
	{
		return admList.toArray(new TAdministrationRslt[0]);
	}
	
	//药物过敏审查结果对象
	private List<TDrugAllergenRslt>    dagList = new ArrayList<TDrugAllergenRslt>();
	public void addDrugAllergenRslt(TDrugAllergenRslt dag)
	{
		// TODO: 统计红黄灯数量
		dagList.add(dag);
		if("R".equals(dag.alertLevel))
		    this.dagRedCount++;
		else if("Y".equals(dag.alertLevel))
		    this.dagYellowCount++;
		
	}
	private int dagRedCount    = 0;
	private int dagYellowCount = 0;
	public void CopyDAGRsltTo(TCheckResult cr)
	{
		cr.dagList     = this.dagList;
		cr.dagRedCount = this.dagRedCount;
		cr.dagYellowCount  =  this.dagYellowCount;
	}
	
	/**
	 * 药物过敏审查结果对象
	 * @return
	 */
	@XmlElement(name="getDrugAllergenRslt")
	public TDrugAllergenRslt[] getDrugAllergenRslt()
	{
		return dagList.toArray(new TDrugAllergenRslt[0]);
	}
	
	//药品禁忌审查结果对象
	private List<TDrugDiagRslt>        ddiList = new ArrayList<TDrugDiagRslt>();
	public void addDrugDiagRslt(TDrugDiagRslt ddi)
	{
		// TODO: 统计红黄灯数量
		ddiList.add(ddi);
		if("R".equals(ddi.alertLevel))
            this.ddiRedCount++;
        else if("Y".equals(ddi.alertLevel))
            this.ddiYellowCount++;
	}
	private int ddiRedCount = 0;
	private int ddiYellowCount = 0;
	public void CopyDDIRsltTo(TCheckResult cr)
	{
		cr.ddiList = this.ddiList;
		cr.ddiRedCount = this.ddiRedCount;
		cr.ddiYellowCount = this.ddiYellowCount;
	}
	
	/**
	 * 药品禁忌审查结果对象
	 * @return
	 */
	@XmlElement(name="getDrugDiagRslt")
	public TDrugDiagRslt[] getDrugDiagRslt()
	{
		return ddiList.toArray(new TDrugDiagRslt[0]);
	}
	
	//药物剂量审查结果对象
	private List<TDrugDosageRslt>      ddgList = new ArrayList<TDrugDosageRslt>();
	public void addDrugDosageRslt(TDrugDosageRslt ddg)
	{
		// TODO: 统计红黄灯数量
		ddgList.add(ddg);
		if("R".equals(ddg.alertLevel))
            this.ddgRedCount++;
        else if("Y".equals(ddg.alertLevel))
            this.ddgYellowCount++;
	}
	private int ddgRedCount = 0;
	private int ddgYellowCount = 0;
	public void CopyDDGRsltTo(TCheckResult cr)
	{
		cr.ddgList = this.ddgList;
		cr.ddgRedCount = this.ddgRedCount;
		cr.ddgYellowCount = this.ddgYellowCount ;
	}
	
	/**
	 * 药物剂量审查结果对象
	 * @return
	 */
	@XmlElement(name="getDrugDosageRslt")
	public TDrugDosageRslt[] getDrugDosageRslt()
	{
		return ddgList.toArray(new TDrugDosageRslt[0]);
	}
	
	//药物重复成份审查结果对象
	private List<TDrugIngredientRslt>  didList = new ArrayList<TDrugIngredientRslt>();
	public void addDrugIngredientRslt(TDrugIngredientRslt did)
	{
		// TODO: 统计红黄灯数量
		didList.add(did);
		if("R".equals(did.alertLevel))
            this.didRedCount++;
        else if("Y".equals(did.alertLevel))
            this.didYellowCount++;
	}
	private int didRedCount = 0;
	private int didYellowCount = 0;
	public void CopyDIDRsltTo(TCheckResult cr)
	{
		cr.didList = this.didList;
		cr.didRedCount = this.didRedCount;
		cr.didYellowCount = this.didYellowCount;
	}
	/**
	 * 药物重复成份审查结果对象
	 * @return
	 */
	@XmlElement(name="getDrugIngredientRslt")
	public TDrugIngredientRslt[] getDrugIngredientRslt()
	{
		return didList.toArray(new TDrugIngredientRslt[0]);
	}
	
	//药品相互作用审查结果对象
	private List<TDrugInteractionRslt> diaList = new ArrayList<TDrugInteractionRslt>();
	public void addInteractionRslt(TDrugInteractionRslt dir)
	{
		if (dir.getAlertLevel().equals("R"))
			diaRedCount++;
		else
		if (dir.getAlertLevel().equals("Y"))
			diaYellowCount++;
		diaList.add(dir);
	}
	public void CopyIARsltTo(TCheckResult cr)
	{
		cr.diaList = this.diaList;
		cr.diaRedCount = this.diaRedCount;
		cr.diaYellowCount = this.diaYellowCount;
	}
	/**
	 * 药品相互作用审查结果对象
	 * @return
	 */
	@XmlElement(name="getDrugInteractionRslt")
	public TDrugInteractionRslt[] getDrugInteractionRslt()
	{
		return diaList.toArray(new TDrugInteractionRslt[0]);
	}
	private int diaRedCount = 0;
	private int diaYellowCount = 0;
	public int getDiaRedCount() 
	{
		return diaRedCount;
	}
	public int getDiaYellowCount() 
	{
		return diaYellowCount;
	}
	//药品配伍审查结果对象
	private List<TDrugIvEffectRslt>    dieList = new ArrayList<TDrugIvEffectRslt>();
	public void addDrugIvEffectRslt(TDrugIvEffectRslt die)
	{
		// TODO: 统计红黄灯数量
		dieList.add(die);
		if("R".equals(die.alertLevel))
            this.dieRedCount++;
        else if("Y".equals(die.alertLevel))
            this.dieYellowCount++;
	}
	public void CopyIERsltTo(TCheckResult cr) 
	{
		cr.dieList = this.dieList;
		cr.dieRedCount = this.dieRedCount ;
		cr.dieYellowCount = this.dieYellowCount ;
	}
	private int dieRedCount = 0;
	private int dieYellowCount = 0;
	
	/**
	 * 药品配伍审查结果对象
	 * @return
	 */
	@XmlElement(name="getDrugIvEffectRslt")
	public TDrugIvEffectRslt[] getDrugIvEffectRslt()
	{
		return dieList.toArray(new TDrugIvEffectRslt[0]);
	}
	
	//特殊人群审查结果对象
	private List<TDrugSpecPeopleRslt>  dspList = new ArrayList<TDrugSpecPeopleRslt>();
	public void addDrugSpecPeopleRslt(TDrugSpecPeopleRslt dsp)
	{
		// TODO: 统计红黄灯数量
		dspList.add(dsp);
		if("R".equals(dsp.alertLevel))
            this.dspRedCount++;
        else if("Y".equals(dsp.alertLevel))
            this.dspYellowCount++;
	}
	private int dspRedCount = 0;
	private int dspYellowCount = 0;
	public void CopyDSPRsltTo(TCheckResult cr)
	{
		cr.dspList = this.dspList;
		cr.dspRedCount = this.dspRedCount ;
		cr.dspYellowCount = this.dspYellowCount ;
	}
	/**
	 * 特殊人群审查结果对象
	 * @return
	 */
	@XmlElement(name="getDrugSpecPeopleRslt")
	public TDrugSpecPeopleRslt[] getDrugSpecPeopleRslt()
	{
		return dspList.toArray(new TDrugSpecPeopleRslt[0]);
	}
	
	//不良反应审查结果对象
	private List<TDrugHarmfulRslt>     dhfList = new ArrayList<TDrugHarmfulRslt>();
	public void addDrugHarmfulRslt(TDrugHarmfulRslt dhf)
	{
		// TODO: 统计红黄灯数量
		dhfList.add(dhf);
		if("R".equals(dhf.alertLevel))
            this.dhfRedCount++;
        else if("Y".equals(dhf.alertLevel))
            this.dhfYellowCount++;
	}
	private int dhfRedCount = 0;
	private int dhfYellowCount = 0;
	public void CopyDHRsltTo(TCheckResult cr)
	{
		cr.dhfList = this.dhfList;
		cr.dhfRedCount = this.dhfRedCount;
		cr.dhfYellowCount = this.dhfYellowCount ;
	}
	
	/**
	 * 不良反应审查结果对象
	 * @return
	 */
	@XmlElement(name="getSideRslt")
	public TDrugHarmfulRslt[] getSideRslt()
	{
		return dhfList.toArray(new TDrugHarmfulRslt[0]);
	}

	//医保审查
	private List<TMedicareRslt> mcareList = new ArrayList<TMedicareRslt>();
	public void addMedicareRslt(TMedicareRslt mcare)
	{
	    mcareList.add(mcare);
	}
	public void CopyMCRsltTo(TCheckResult cr)
	{
	    cr.mcareList = this.mcareList;
	}
	
	/**
	 * 医保审查
	 * @return
	 */
	@XmlElement(name="getMedicareRslt")
	public TMedicareRslt[] getMedicareRslt()
	{
	    return (TMedicareRslt[])mcareList.toArray(new TMedicareRslt[0]);
	}
	
	@XmlElement(name="getAdmRedCount")
	public int getAdmRedCount() {
		return admRedCount;
	}
	
	@XmlElement(name="getAdmYellowCount")
	public int getAdmYellowCount() {
		return admYellowCount;
	}

	@XmlElement(name="getDagRedCount")
	public int getDagRedCount() {
		return dagRedCount;
	}

	@XmlElement(name="getDagYellowCount")
	public int getDagYellowCount() {
		return dagYellowCount;
	}
	
	@XmlElement(name="getDdiRedCount")
	public int getDdiRedCount() {
		return ddiRedCount;
	}

	@XmlElement(name="getDdiYellowCount")
	public int getDdiYellowCount() {
		return ddiYellowCount;
	}

	@XmlElement(name="getDdgRedCount")
	public int getDdgRedCount() {
		return ddgRedCount;
	}

	@XmlElement(name="getDdgYellowCount")
	public int getDdgYellowCount() {
		return ddgYellowCount;
	}

	@XmlElement(name="getDidRedCount")
	public int getDidRedCount() {
		return didRedCount;
	}

	@XmlElement(name="getDidYellowCount")
	public int getDidYellowCount() {
		return didYellowCount;
	}

	@XmlElement(name="getDieRedCount")
	public int getDieRedCount() {
		return dieRedCount;
	}

	@XmlElement(name="getDieYellowCount")
	public int getDieYellowCount() {
		return dieYellowCount;
	}

	@XmlElement(name="getDspRedCount")
	public int getDspRedCount() {
		return dspRedCount;
	}

	@XmlElement(name="getDspYellowCount")
	public int getDspYellowCount() {
		return dspYellowCount;
	}

	@XmlElement(name="getDhfRedCount")
	public int getDhfRedCount() {
		return dhfRedCount;
	}

	@XmlElement(name="getDhfYellowCount")
	public int getDhfYellowCount() {
		return dhfYellowCount;
	}
}
