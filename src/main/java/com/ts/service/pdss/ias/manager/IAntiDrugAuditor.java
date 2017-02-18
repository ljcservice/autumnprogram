package com.ts.service.pdss.ias.manager;

/**
 * 获取抗菌药审核条件接口
 * @author Administrator
 */
public interface IAntiDrugAuditor
{
	/**
	 * 获取审核和登记开关
	 * @param DrugDoctorInfo 药品代码、名称、剂型、规格、厂家、数量、给药途径、医生姓名、职称、科室代码
	 * @return 审核、登记开关
	 */
	public String[] getAntiDrugCheckRule(String[] DrugDoctorInfo);
}