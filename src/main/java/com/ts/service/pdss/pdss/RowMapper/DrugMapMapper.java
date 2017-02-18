package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.entity.pdss.pdss.Beans.TDrugMap;
import com.ts.service.pdss.pdss.Cache.BeanCache;

public class DrugMapMapper extends MapperBase implements RowMapper
{

    @SuppressWarnings ("unused")
    private Map<String, TDrug> map = null;
    
    public DrugMapMapper(List<TDrug> list)
    {
        super(list);
    }
    
    public DrugMapMapper(Map<String, TDrug> aMap)
    {
        map = aMap;
    }

    public DrugMapMapper()
    {

    }
    
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        TDrugMap  dm = new TDrugMap();
        
        dm.setDRUG_NO_LOCAL(rs.getString("DRUG_NO_LOCAL"));
        dm.setDRUG_NAME_LOCAL(rs.getString("DRUG_NAME_LOCAL"));
        dm.setDRUG_ID(rs.getString("DRUG_ID"));        
        dm.setOPER_USER(rs.getString("OPER_USER"));      
        dm.setOPER_DATE(rs.getString("OPER_DATE"));      
        dm.setOPER_TYPE(rs.getString("OPER_TYPE"));      
        dm.setIS_ANTI(rs.getString("IS_ANTI"));        
        dm.setIS_MEDCARE(rs.getString("IS_MEDCARE"));     
        dm.setIS_BASEDRUG(rs.getString("IS_BASEDRUG"));    
        dm.setTOXI_PROPERTY(rs.getString("TOXI_PROPERTY"));  
        dm.setDDD_VALUE(rs.getString("DDD_VALUE"));
        dm.setDDD_UNIT(rs.getString("DDD_UNIT"));       
        dm.setIS_EXHILARANT(rs.getString("IS_EXHILARANT"));  
        dm.setIS_INJECTION(rs.getString("IS_INJECTION"));   
        dm.setIS_ORAL(rs.getString("IS_ORAL"));        
        dm.setANTI_LEVEL(rs.getString("ANTI_LEVEL"));     
        dm.setIS_IMPREGNANT(rs.getString("IS_IMPREGNANT"));  
        dm.setPHARM_CATALOG(rs.getString("PHARM_CATALOG"));  
        dm.setDRUG_CATALOG(rs.getString("DRUG_CATALOG"));   
        dm.setIS_PREDRUG(rs.getString("IS_PREDRUG"));     
        dm.setDRUG_SPEC(rs.getString("DRUG_SPEC"));      
        dm.setUSE_DEPT(rs.getString("USE_DEPT"));       
        dm.setDIRECT_ID(rs.getString("DIRECT_ID"));      
        BeanCache.setDrugMap(dm);
        CacheIt(dm);
        return dm;
    }

}
