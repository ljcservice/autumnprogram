package com.ts.entity.pdss.Saver;

import com.hitzd.his.Beans.TPatientOrder;
import com.ts.entity.pdss.pdss.RSBeans.TCheckResultCollection;
import com.ts.service.pdss.pdss.impl.PatientSaveCheckResult;

/**
 * 队列保存保存信息 
 * @author Administrator
 *
 */
public class SaveThread implements Runnable 
{

	public SaveThread() {}
	
	private static PatientSaveCheckResult patientSaveBean = new PatientSaveCheckResult();
	
	@Override
	public void run() 
	{
		while(true)
		{
			//System.out.println("雷达开始扫描......");
			if(QueueBean.getSaveBeanRSSize()>0)
			{ 
				//System.out.println("雷达捕捉到猎物进行处理 ");
				SaveBeanRS bean            = QueueBean.getQueueSaveBeanRS();
				TPatientOrder po           = bean.getPo();
				if(po == null)
					continue;
				TCheckResultCollection crc = bean.getCheckRC();
				if(crc != null)
				{
					try
					{
						patientSaveBean.setNgroupnum(bean.getID());
						/* 保存基本信息 */
						patientSaveBean.savePatientCheckInfo(po,crc.getDsr());
				        /* 相互作用检查 */
				        patientSaveBean.saveDrugInteractionCheckInfo(crc.getDsr());
				        /* 配伍审查 */
				        patientSaveBean.saveDrugIvEffectCheckInfo(crc.getDsr());
				        /* 禁忌症审查 */
				        patientSaveBean.saveDrugDiagCheckInfo(crc.getDsr());
				        /* 特殊人群审查 */
				        patientSaveBean.saveDrugSpecPeopleCheckInfo(crc.getDsr());
				        /* 重复成份审查 */
				        patientSaveBean.saveDrugIngredientCheckInfo(crc.getDsr());
				        /* 用药途径审查 */
				        patientSaveBean.saveDrugAdministrationCheckInfo(crc.getDsr());
				        /* 过敏药物审查 */
				        patientSaveBean.saveDrugAllergenCheckInfo(crc.getDsr());
				        /* 药物剂量审查 */
				        patientSaveBean.saveDrugDosageCheckInfo(crc.getDsr());
				        /* 异常信号审查 */
				        patientSaveBean.saveDrugSideCheckInfo(crc.getDsr());
				        /* 保存审查总体结构 */
				        patientSaveBean.saveDrugSecurityCheckInfo(po, crc.getDsr());
				        /* 抗菌药物 审查  结果保存 */
				        patientSaveBean.saveAntiDrugSecutity(po,crc.getAdsr());
				        /* 预防用药信息保存  */
				        patientSaveBean.savePreveUseDrug(po);
				        /* 治疗用药信息保存  */
				        patientSaveBean.saveTreatUseDrug(po);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				System.out.println("处理完毕队列信息：医生名称 :" + po.getDoctorName() + ";部门名称:" + po.getDoctorDeptName() + ";病人id:" + po.getPatVisitInfo().getPatientID() + ";住院号:" + po.getPatVisitInfo().getVisitID());
			}
			else
			{
				//System.out.println("扫描没有猎物等待5秒后继续 扫描 ");
				try
				{
					Thread.sleep(5000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
