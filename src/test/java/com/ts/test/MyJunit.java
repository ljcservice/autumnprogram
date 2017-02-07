package com.ts.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ts.dao.DAO;
import com.ts.util.PageData;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:spring/ApplicationContext*.xml"
//    })
public class MyJunit
{
    public static ApplicationContext app ;
    
    @Before
    public void setSpring()   {
        app = new ClassPathXmlApplicationContext(
                "classpath:spring/ApplicationContext-main.xml",
                "classpath:spring/ApplicationContext-dataSource.xml",
                "classpath:spring/ApplicationContext-shiro.xml",
                "classpath:spring/ApplicationContext-redis.xml"
                );
    }
    
    @Test
    public void exc(){
          DAO  dao = (DAO)app.getBean("daoSupportTest");
          try
          {
              PageData pd = new PageData();pd.put("dt_id", "11233");
              List<PageData> list = (List<PageData>) dao.findForList("AIMapper.dictList", pd);
              System.out.println(list.size());
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }
          
    }
}
