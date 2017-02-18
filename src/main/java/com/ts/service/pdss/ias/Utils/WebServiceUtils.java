package com.ts.service.pdss.ias.Utils;

import com.ts.entity.pdss.ias.Beans.TAntiDrugInput;
import com.ts.entity.pdss.ias.Beans.TDoctor;
import com.ts.entity.pdss.ias.Beans.TOperator;


/**
 * 组合参数bean 
 * @author Administrator
 *
 */
public class WebServiceUtils
{
	public static TAntiDrugInput getDrugInfo(String[] doctorInfo,String[] diagnosis,String[] operInfo,String[] drugInfo,String[] patientInfo)
	{
		TAntiDrugInput drug = new TAntiDrugInput();
		if(drugInfo.length > 0 )
		{
			drug.setDrugID(drugInfo[0]);
			drug.setDrugName(drugInfo[1]); 
			drug.setRecMainNo(drugInfo[2]);
			drug.setRecSubNo(drugInfo[3]); 
			drug.setStartDateTime(drugInfo[4]); 
			drug.setStopDateTime(drugInfo[5]);
			drug.setUseType(drugInfo[6]);
			drug.setPatientId(patientInfo[0]);
			drug.setVisi_id(patientInfo[1]);
			/* 诊断信息 存放的 id */
			drug.setDiagnosis(diagnosis);
			if(doctorInfo != null && doctorInfo.length > 0)
			{
				TDoctor doctor = new TDoctor();
				doctor.setDoctorDeptID(doctorInfo[0]);
				doctor.setDoctorDeptName(doctorInfo[1]);
				doctor.setDoctorID(doctorInfo[2]);
				doctor.setDoctorName(doctorInfo[3]);
				doctor.setDoctorTitleID(doctorInfo[4]);
				doctor.setDoctorTitleName(doctorInfo[5]);
				drug.setDoctor(doctor);
			}
			/* 手术 信息 */
			if(operInfo != null && operInfo.length > 0)
			{
				TOperator operator = new TOperator();
				operator.setOperCode(operInfo[0]);
				operator.setOperName(operInfo[1]); 
				operator.setOperLevel(operInfo[2]); 
				operator.setOperStartTime(operInfo[3]); 
				operator.setOperEndTime(operInfo[4]);
				drug.setOperator(operator);
			}
		}
		return drug;
	}
}
