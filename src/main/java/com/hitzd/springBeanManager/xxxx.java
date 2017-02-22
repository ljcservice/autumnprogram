package com.hitzd.springBeanManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class xxxx implements BeanFactory
{

    @Override
    public Object getBean(String name) throws BeansException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getBean(String name, Class requiredType)
            throws BeansException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getBean(String name, Object[] args) throws BeansException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsBean(String name)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSingleton(String name)
            throws NoSuchBeanDefinitionException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPrototype(String name)
            throws NoSuchBeanDefinitionException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, Class targetType)
            throws NoSuchBeanDefinitionException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Class getType(String name) throws NoSuchBeanDefinitionException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getAliases(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
