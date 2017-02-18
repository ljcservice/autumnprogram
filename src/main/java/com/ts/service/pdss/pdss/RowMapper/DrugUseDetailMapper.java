package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrugUseDetail;
import com.ts.service.pdss.pdss.Cache.BeanCache;
/**
 *  药品用药信息 
 * @author liujc
 *
 */
public class DrugUseDetailMapper extends MapperBase implements RowMapper
{

	private Map<String, TDrugUseDetail> map = null;
	
	public DrugUseDetailMapper(Map<String, TDrugUseDetail> aMap)
	{
		map = aMap;
	}
	
    public DrugUseDetailMapper()
    {
    }
    @SuppressWarnings ("rawtypes")
    public DrugUseDetailMapper(List alist)
    {
        super(alist);
    }
    @Override
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrugUseDetail dul = new TDrugUseDetail();
        dul.setDRUG_USE_DETAIL_ID(rs.getString("DRUG_USE_DETAIL_ID"));
        dul.setDRUG_CLASS_ID(rs.getString("DRUG_CLASS_ID"));
        dul.setPREGNANT_INDI(rs.getString("PREGNANT_INDI"));
        dul.setPREGNANT_INFO(rs.getString("PREGNANT_INFO"));
        dul.setPREGNANT_INFO_REF(rs.getString("PREGNANT_INFO_REF"));
        dul.setLACT_INDI(rs.getString("LACT_INDI"));
        dul.setLACT_INFO(rs.getString("LACT_INFO"));
        dul.setLACT_INFO_REF(rs.getString("LACT_INFO_REF"));
        dul.setKID_INDI(rs.getString("KID_INDI"));
        dul.setKID_LOW(rs.getString("KID_LOW"));
        dul.setKID_HIGH(rs.getString("KID_HIGH"));
        dul.setKID_INFO(rs.getString("KID_INFO"));
        dul.setKID_INFO_REF(rs.getString("KID_INFO"));
        dul.setOLD_INDI(rs.getString("OLD_INDI"));
        dul.setOLD_INFO(rs.getString("OLD_INFO"));
        dul.setOLD_INFO_REF(rs.getString("OLD_INFO_REF"));
        dul.setHEPATICAL_INDI(rs.getString("HEPATICAL_INDI"));
        dul.setRENAL_INDI(rs.getString("RENAL_INDI"));
        dul.setFORBID_RUID(rs.getString("FORBID_RUID"));
        dul.setFORBID_CAUSE(rs.getString("FORBID_CAUSE"));
        dul.setINADVIS_RTID(rs.getString("INADVIS_RTID"));
        dul.setINADVIS_CAUSE(rs.getString("INADVIS_CAUSE"));
        dul.setADVERT_RTID(rs.getString("ADVERT_RTID"));
        dul.setADVERT_CAUSE(rs.getString("ADVERT_CAUSE"));
        dul.setLAST_DATE_TIME(rs.getString("LAST_DATE_TIME"));
        BeanCache.setDud(dul.getDRUG_CLASS_ID(),dul);
        CacheIt(dul);
        if (map != null)
        	map.put(dul.getDRUG_CLASS_ID(), dul);
        return dul;
    }

}
