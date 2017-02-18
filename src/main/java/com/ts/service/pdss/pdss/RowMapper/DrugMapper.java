package com.ts.service.pdss.pdss.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.ts.entity.pdss.pdss.Beans.TDrug;
import com.ts.service.pdss.pdss.Cache.BeanCache;

/**
 * 对象映射类，此处进行加解密处理
 * 
 * @author Administrator
 * 
 */
public class DrugMapper extends MapperBase implements RowMapper
{
	
	private Map<String, TDrug> map = null;
	
    public DrugMapper(List<TDrug> list)
    {
        super(list);
    }
    
    public DrugMapper(Map<String, TDrug> aMap)
    {
    	map = aMap;
    }

    public DrugMapper()
    {

    }

    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
        TDrug drug = new TDrug();
        drug.setDRUG_ID(rs.getString("DRUG_ID"));
        drug.setDRUG_NAME(rs.getString("DRUG_NAME"));
        drug.setDRUG_CLASS_ID(rs.getString("DRUG_CLASS_ID"));
        drug.setDOSE_CLASS_ID(rs.getString("DOSE_CLASS_ID"));
        // 以下字段经过加密存储，此处进行解密
        //drug.setDRUG_SPEC(SecUtils.Decode(rs.getString("DRUG_SPEC")));
        //drug.setFIRM_ID(rs.getString("FIRM_ID"));
        drug.setUNITS(rs.getString("UNITS"));
        drug.setDRUG_FORM(rs.getString("DRUG_FORM"));
        drug.setTOXI_PROPERTY(rs.getString("TOXI_PROPERTY"));
        drug.setDOSE_PER_UNIT(rs.getString("DOSE_PER_UNIT"));
        drug.setDOSE_UNITS(rs.getString("DOSE_UNITS"));
        drug.setINGR_CLASS_IDS(rs.getString("INGR_CLASS_IDS"));
        drug.setIV_CLASS_CODE(rs.getString("IV_CLASS_CODE"));
        drug.setDRUG_INDICATOR(rs.getString("DRUG_INDICATOR"));
        drug.setINPUT_CODE(rs.getString("INPUT_CODE"));
        /* 本地ID */
        drug.setDRUG_NO_LOCAL(rs.getString("DRUG_NO_LOCAL"));
        /* 本地药品名称 */
        drug.setDRUG_NAME_LOCAL(rs.getString("DRUG_NAME_LOCAL"));
        /* 操作人 */
        drug.setOPER_USER(rs.getString("OPER_USER"));
        /* 操作时间*/
        drug.setOPER_TIME(rs.getString("OPER_DATE"));
        /* 操作方式 */
        drug.setOPER_TYPE(rs.getString("OPER_TYPE"));
        /* 药敏代码 */
        drug.setSENSIT_CODE(rs.getString("SENSIT_CODE"));
        /*使用类型 */
        drug.setUseType(rs.getString("usetype"));
        drug.setDirect_no(rs.getString("direct_no"));
        BeanCache.putDrug(new TDrug(drug));
        CacheIt(drug);
        if (map != null)
        	map.put(drug.getDRUG_NO_LOCAL(), drug);
        return drug;
    }
}
