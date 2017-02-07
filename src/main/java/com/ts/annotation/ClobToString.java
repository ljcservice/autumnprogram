package com.ts.annotation;

import java.io.*;

import java.sql.*;

import javax.servlet.jsp.*;

import javax.servlet.jsp.tagext.*;

/**
 * 
 * 处理clob大对象类型
 * 
 * @author lishibo
 * 
 * @version 1.0
 * 
 * @date
 */

public class ClobToString extends SimpleTagSupport
{
	private Object clob;
	private int n;
	private String addContent;
	
	public String getAddContent()
	{
		return addContent;
	}
	public void setAddContent(String addContent)
	{
		this.addContent = addContent;
	}
	public int getN()
	{
		return n;
	}
	public void setN(int n)
	{
		this.n = n;
	}
	public Object getClob()

	{
		return clob;
	}
	public void setClob(Object clob)
	{
		this.clob = clob;
	}

	@Override
	public void doTag() throws JspException, IOException
	{
		JspContext jc = super.getJspContext();
		JspWriter out = jc.getOut();
		if (clob == null)
		{
			out.print("");
		}
		StringBuffer clobString = new StringBuffer();
		if (clob instanceof Clob)
		{
			int y;
			char ac[] = new char[4096];
			Reader reader;
			try
			{
				reader = ((Clob) clob).getCharacterStream();
				while ((y = reader.read(ac, 0, 4096)) != -1)
				{
					clobString.append(new String(ac, 0, y));
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			clobString.append(clob.toString());
		}
		if (clobString.toString().length() > n && n > 0)
		{
			out.print(clobString.toString().substring(0, n) + addContent);
		}
		else
		{
			out.print(clobString.toString());
		}
	}

}