package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugInteractionInfo;
import com.ts.service.pdss.pdss.Cache.BeanCache;

/**
 * 互动信息mapper
 * @author Administrator
 */
public class DrugInteractionMapper extends MapperBase implements RowMapper 
{
    public DrugInteractionMapper(List<TDrugInteractionInfo> list)
    {
		super(list);
	}

    public DrugInteractionMapper() 
    {
	}
    
	public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugInteractionInfo druginfo = new TDrugInteractionInfo();
        druginfo.setDRUG_INTERACTION_INFO_ID(rs.getString("DRUG_INTERACTION_INFO_ID"));
        druginfo.setINGR_CLASS_CODE1(rs.getString("INGR_CLASS_ID1"));
        druginfo.setINGR_CLASS_CODE2(rs.getString("INGR_CLASS_ID2"));
        druginfo.setINTER_INDI(rs.getString("INTER_INDI"));
        druginfo.setWAR_INFO(rs.getString("WAR_INFO"));
        druginfo.setMEC_INFO(rs.getString("MEC_INFO"));
        druginfo.setMAN_INFO(rs.getString("MAN_INFO"));
        druginfo.setDIS_INFO(rs.getString("DIS_INFO"));
        druginfo.setREF_SOURCE(rs.getString("REF_SOURCE"));
       // if(list!=null) 2012-06-01 liujcע��  
        BeanCache.setDrugInteractionInfo(druginfo);
        CacheIt(druginfo);
        return druginfo;
    }
}
