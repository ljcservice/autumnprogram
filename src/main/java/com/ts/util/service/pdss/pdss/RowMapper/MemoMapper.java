package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.mas.Beans.TMemo;
import com.ts.service.pdss.pdss.Cache.BeanCache;

/**
 * 适用症列表
 * @author liujc
 *
 */
public class MemoMapper extends MapperBase implements RowMapper
{
    public MemoMapper(List<TMemo> list)
    {
        super(list);
    }

    public MemoMapper()
    {
    }
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
    	TMemo mm = new TMemo();
        mm.setID(rs.getString("ID"));
        mm.setDRUG_ID(rs.getString("DRUG_ID"));
        mm.setCHECK_ITEM_NAME(rs.getString("CHECK_ITEM_NAME"));
        mm.setCHECK_ITEM_CODE(rs.getString("CHECK_ITEM_CODE"));
        mm.setCHECK_TYPE(rs.getString("CHECK_TYPE"));
        BeanCache.setCommonCache(mm);
        CacheIt(mm);
        return mm;
    }
}







