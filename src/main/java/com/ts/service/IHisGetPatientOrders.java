package com.ts.service;

import com.hitzd.his.Beans.TPatientOrder;

/**
 * 获得 医嘱信息 
 * @author Administrator
 */
public interface IHisGetPatientOrders
{
	public TPatientOrder getPatientOrder(String patient_Id , String visit_Id);
}

