package com.hitzd.DBUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import com.hitzd.DBUtils.DBTypeBean.DBClobBean;

public class LobCreatingPSCallBeck 
{
    
    private lobCreater lobcreater =  null;
    public LobCreatingPSCallBeck(final Object... values)
    {
        final LobHandler lh = new DefaultLobHandler();
        lobcreater = new lobCreater(lh);
        lobcreater.setObjects(values);
    }
    
    public lobCreater LobCreatingPreparedSCallback()
    {
        return this.lobcreater;
    }
}

class  lobCreater extends AbstractLobCreatingPreparedStatementCallback
{

    private Object[] values;
    
    public void  setObjects(Object[] values)
    {
        this.values = values;
    }
    
    public lobCreater(LobHandler lobHandler)
    {
        super(lobHandler);
    }
    
    @Override
    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException
    {
        if(values == null) throw new RuntimeException("输入参数为空!");
        for(int i = 0 ;i < values.length; i++)
        {
            Object o = values[i];
            if(o instanceof DBClobBean)
            {
                lobCreator.setClobAsString(ps, i+1, ((DBClobBean) o).getValueClob());
            }
            else 
            {
                ps.setString(i+1, ((String)o));
            }
        }
    }
}
