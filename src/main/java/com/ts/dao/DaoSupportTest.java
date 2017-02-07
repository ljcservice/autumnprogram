package com.ts.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service
public class DaoSupportTest extends DaoSupport implements DAO
{

    public DaoSupportTest()
    {
        // TODO Auto-generated constructor stub
    }
    public DaoSupportTest(String sqlSession)
    {
        super(sqlSession);
        // TODO Auto-generated constructor stub
    }
    
   
    
}
