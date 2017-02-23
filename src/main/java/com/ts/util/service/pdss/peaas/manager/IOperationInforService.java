package com.ts.service.pdss.peaas.manager;

import com.ts.entity.pdss.peaas.Beans.TOperationDrug;
import com.ts.entity.pdss.peaas.Beans.TOperationType;

public interface IOperationInforService
{

    /**
     * 返回 手术类型 
     * @param name
     * @return
     */
    public TOperationType[] getOperationTypes(String name);
    
    /**
     * 返回手术类型下的药品 
     * @param operationID
     * @return
     */
    public TOperationDrug[] getOperationDrugs(String operationID);
}
