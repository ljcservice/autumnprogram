package com.ts.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitzd.his.Beans.TOperationDict;
import com.ts.service.pdss.ias.manager.IAntiDrugSecurityChecker;

/**
 * 用于所有关于his数据的操作接口
 * @author Administrator
 *
 */
@Service
public class HisServiceCenter implements IHisServiceCenter
{

    /* 抗菌药物审查  */
    @Resource(name="antiDrugSecurityCheckerBean")
    private IAntiDrugSecurityChecker antiDrugscr;
    
    /**
     * 获得手术列表
     * @return
     */
    public TOperationDict[] getOperationDict()
    {
        return antiDrugscr.getOperationDict();
    }
}
