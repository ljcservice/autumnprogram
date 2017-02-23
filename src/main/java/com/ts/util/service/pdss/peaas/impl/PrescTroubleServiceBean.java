package com.ts.service.pdss.peaas.impl;

import org.springframework.stereotype.Service;

import com.hitzd.persistent.Persistent4DB;
import com.ts.entity.pdss.peaas.RSBeans.TPrescCheckRslt;
import com.ts.service.pdss.peaas.manager.IPrescTroubleService;

@Service
public class PrescTroubleServiceBean extends Persistent4DB implements IPrescTroubleService
{

    @Override
    public TPrescCheckRslt[] QueryPrescTrouble(String dept, String doctor, String beginDate, String endDate, String[] bak)
    {
        
        return null;
    }
    
    

}
