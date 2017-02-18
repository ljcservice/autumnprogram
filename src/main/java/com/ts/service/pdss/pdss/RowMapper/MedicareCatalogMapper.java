package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.mas.Beans.TMedicareCatalog;
import com.ts.service.pdss.pdss.Cache.BeanCache;

/**
 * 是否医保内用药
 * @author liujc
 *
 */
public class MedicareCatalogMapper extends MapperBase implements RowMapper
{
    public MedicareCatalogMapper(List<TMedicareCatalog> list)
    {
        super(list);
    }

    public MedicareCatalogMapper()
    {

    }

    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
    	TMedicareCatalog mc = new TMedicareCatalog();
        mc.setDRUG_ID(rs.getString("DRUG_ID"));
        mc.setDRUG_NAME(rs.getString("DRUG_NAME"));
        mc.setREMARK(rs.getString("REMARK"));
        mc.setAPPLY_TYPE(rs.getString("APPLY_TYPE"));
        mc.setFEE_TYPE(rs.getString("FEE_TYPE"));
        BeanCache.setCommonCache(mc);
        CacheIt(mc);
        return mc;
    }
}
