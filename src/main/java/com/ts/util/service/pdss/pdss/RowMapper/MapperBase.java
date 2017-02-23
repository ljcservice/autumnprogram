package com.ts.service.pdss.pdss.RowMapper;

import java.util.List;

public class MapperBase
{
    protected List list = null;
	protected Class clazz = null;

    public MapperBase(List aList)
    {
        list = aList;
    }

    public MapperBase(List aList, Class aClass)
    {
    	list = aList;
    	clazz = aClass;
    }
    
    public MapperBase()
    {
    }

    @SuppressWarnings ("unchecked")
    public void CacheIt(Object obj)
    {
        if (list != null)
            list.add(obj);
    }

}
