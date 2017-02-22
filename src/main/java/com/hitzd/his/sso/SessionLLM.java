package com.hitzd.his.sso;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionLLM implements HttpSessionListener
{
    private static int counter = 0;
    @Override
    public void sessionCreated(HttpSessionEvent arg0)
    {
        System.out.println("构建的了session");
        // TODO Auto-generated method stub
        counter++;

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0)
    {
        System.out.println("销毁了的session");
        // TODO Auto-generated method stub
        counter--;

    }
 
    public static int getCounter()
    {
        return counter;
    }
}
