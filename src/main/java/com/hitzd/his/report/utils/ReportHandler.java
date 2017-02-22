package com.hitzd.his.report.utils;

import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Program.Web.Utils.ChartUtils;
import com.hitzd.his.Program.Web.Utils.CommonUtils;
import com.hitzd.his.Program.Web.Utils.ICharStrategy;

/**
 * 报表 管理器
 * @author jingcong
 *
 */
public class ReportHandler 
{
	protected String        forward = "/index.jsp";
	
	public String getForword() 
	{
		return forward;
	}

	public void setForword(String forward) 
	{
		this.forward = forward;
	}

	public List<TCommonRecord> doReport(IPage page)
	{
		String o = page.getParameter("o", "");
		if (o != null && !"".equals(o))
		{
			List<TCommonRecord> list = null;
			if (o.equals("query"))
			{
				list = query(page);
			}
			else if (o.equals("excelPrint")) 
			{
				
				excelPrint(page);
			    getSessionObj(page);
			}
			else if (o.equals("Print"))
			{
				
				Print(page);
			    getSessionObj(page);
			}
			else if (o.equals("PDFPrint"))
			{
				PDFPrint(page);
			    getSessionObj(page);
			}
			else if(o.equals("Graph"))
			{
				Graph(page);
				getSessionObj(page);
			}
			else if(o.equals("GraphPanel"))
			{
				GraphPanel(page);
				getSessionObj(page);
			}
			else
			{
				list = option(page);
			}

			return list;
		}
		else
			return first(page);
	}
	/**
	 * 用于画图跳转
	 * @param page
	 */
	public List<TCommonRecord> GraphPanel(IPage page)
	{
		forward = forward + "_GraphPanel.jsp";
		return null;
	}
	/**
	 * 用于画图跳转
	 * @param page
	 */
	public List<TCommonRecord> Graph(IPage page)
	{
		forward = forward + "_Graph.jsp";
		return null;
	}

	public List<TCommonRecord> first(IPage page)
	{
		forward = forward + "_detail.jsp";
		return null;
	}
	
	public List<TCommonRecord> query(IPage page)
	{
		forward = forward + "_detail.jsp";
		return null;
	}
	public List<TCommonRecord> excelPrint(IPage page)
	{
		forward = forward + "_Excel.jsp";
		return null;
	}
	public List<TCommonRecord> Print(IPage page)
	{
		forward = forward + "_Print.jsp";
		return null;
	}
	public List<TCommonRecord> option(IPage page)
	{
		forward = forward + "_detail.jsp";
		return null;
	}
	/**
	 * 获得 数据信息 
	 * @param request
	 */
	private void getSessionObj(IPage page)
	{
	    List<Object>  objs = CommonUtils.getSessionObject((HttpServletRequest)page.getAttribute("Request"), getCurClassName());
        if(objs == null)
            //TODO  在session 找不到数据集时 需要一个错误页面 增项用户体验 。
            this.forward = "/index.jsp" ;
        else
        	((HttpServletRequest)page.getAttribute("Request")).setAttribute("sessionObject",objs);
        
	}
	
	/**
	 * 获得当前类名字 做为 session key 存放数据集
	 * @return
	 */
	protected String getCurClassName()
	{
	    return this.getClass().getSimpleName();
	}
	
	/**
	 * 存放数据集 用于打印 ，导出，pdf 避免重复重新 
	 * @param request
	 * @param objs
	 */
	protected void setSessionObject(IPage page, Object... objs)
	{
	    CommonUtils.setSessionObject((HttpServletRequest)page.getAttribute("Request"), getCurClassName(), objs);
	}
	
    /**
     * pdf导出 
     * @param request
     * @param response
     */
    protected void PDFPrint(IPage page){}
	
	
    /**
     * 显示图形的基本方法
     * 使用者实现这样的接口方法 charBaseData.setCharBaseData() 来组织好数据   
     * @param request
     * @return
     */
    protected String chartView(IPage page, ICharStrategy charBaseData)
    {
        String graphtype = page.getParameter("graphtype", "");  // 图形格式
        LinkedHashMap<String, String> chart  = charBaseData.setCharBaseData();
        String strChar = "";
        switch(Integer.parseInt(graphtype))
        {
            case 1:                     //柱形图
                strChar = ChartUtils.ColumnChart(page, chart);
                break;
            case 3:                     //折线图
                strChar = ChartUtils.LineChart(page, chart);
                break;
            case 6:                     //饼图
                strChar = ChartUtils.PieChart(page, chart);
                break;
        }
        page.setAttribute("ChartsPage", strChar);
        return strChar; 
    }
}
