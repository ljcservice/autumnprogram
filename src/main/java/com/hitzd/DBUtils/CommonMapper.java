package com.hitzd.DBUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Õ®”√mapper 
 * @author Administrator
 *
 */
public class CommonMapper implements RowMapper
{	
    public Object mapRow(ResultSet rs, int arg1) throws SQLException
    {
    	TCommonRecord cr = new TCommonRecord();
    	ResultSetMetaData rsmd = rs.getMetaData();  
    	// System.out.println(rsmd.getColumnCount());
    	for (int i = 1; i <=rsmd.getColumnCount(); i++)
    	{
    		String ColumnName = rsmd.getColumnName(i);
    		String Value = "";
    		if (rsmd.getColumnTypeName(i).equalsIgnoreCase("CLOB"))
    		{
				Reader reader = rs.getCharacterStream(i);
				if (reader != null)
				{
					BufferedReader bfrr = new BufferedReader(reader);
					StringBuffer sb = new StringBuffer();
					char[] c = new char[1024];
					int len = 0;
					try
					{
						while ( (len = bfrr.read(c, 0, 1024)) >0)
							sb.append(new String(c,0,len));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					Value = sb.toString();
				}
				else
				{
					Value = "";
				}  			
    		}
    		else
    		{
    			Value = rs.getString(i);
    		}
    		cr.set(ColumnName, Value);
    	}
        return cr;
    }
}
