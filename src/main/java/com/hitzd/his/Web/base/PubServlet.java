package com.hitzd.his.Web.base;

import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.his.Program.Web.Utils.ChartUtils;
import com.hitzd.his.Program.Web.Utils.CommonUtils;
import com.hitzd.his.Program.Web.Utils.ICharStrategy;

/**
 * servlet的父类，根据页面参数o的值执行相应的方法
 * @author tyl
 *
 */
public  class PubServlet extends HttpServlet 
{

	private static final long serialVersionUID = 1L;
	/* 默认转向页 */
	protected String        forword = "/index.jsp";
	/**/
	protected String childClassName = "";
	
	/* 用于判断是否为 异步提交 */
    protected boolean AjaxFlag = false;
    
	public PubServlet(){}
	
	/**
	 *  通用处理 
	 */
	@SuppressWarnings("rawtypes")
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
		String o = CommonUtils.getRequestParameter(request, "o", "query");
		o = o.length() == 0 ? "query" : o;
		if (o != null && !"".equals(o))
		{
			List list = null;
			if (o.equals("insert"))
			{
				list = insert(request,response);
			} 
			else if (o.equals("update")) 
			{
				list = update(request,response);
			} 
			else if (o.equals("delete")) 
			{
				list = delete(request,response);
			}
			else if (o.equals("query"))
			{
				list = query(request,response);
			}
			else if (o.equals("modify"))
			{
				list = modify(request,response);
			}
			else if (o.equals("addNew")) 
			{
				list = addNew(request,response);
			}
			else if (o.equals("excelPrint")) 
			{
			    excelPrint(request, response);
			    getSessionObj(request);
			}
			else if (o.equals("Print"))
			{
			    Print(request, response);
			    getSessionObj(request);
			}
			else if(o.equals("Graph"))
			{
				graph(request,response);
				getSessionObj(request);
			}
			else if(o.equals("GraphPanel"))
			{
				graphPanel(request,response);
				getSessionObj(request);
			}
			else if (o.equals("PDFPrint"))
			{
			    PDFPrint(request, response);
			    getSessionObj(request);
			}
			else
			{
				list = option(o, request, response);
			}
			if(list != null)
			    request.setAttribute("List",list);
			/* 判断 是否是异步提交  */
            if(this.AjaxFlag)
            {
                this.forword = "";
            }
            else
            {
                request.getRequestDispatcher(forword).forward(request, response);
            }
			return;
		}
		response.sendRedirect(request.getContextPath() + forword);
	}

	/**
	 * 获得 数据信息 
	 * @param request
	 */
	private void getSessionObj(HttpServletRequest request)
	{
	    List<Object>  objs = CommonUtils.getSessionObject(request, getCurClassName());
        if(objs == null)
        {
            //TODO  在session 找不到数据集时 需要一个错误页面 增项用户体验 。
            if("".equals(this.forword)) this.forword = "/index.jsp" ;
        }
        else
        {
            request.setAttribute("sessionObject",objs);
        }
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
	protected void setSessionObject(HttpServletRequest request, Object... objs)
	{
	    CommonUtils.setSessionObject(request,getCurClassName(),objs);
	}
	
	
	/**
	 * 绘图版  
	 * @param request
	 * @param response
	 */
	protected void graphPanel(HttpServletRequest request,HttpServletResponse response)
	{
	    String graphPanel = CommonUtils.getRequestParameter(request, "graphPanel", "");
	    if(graphPanel.equals(""))
	    {
	            System.out.println("导出地址不能为空!");
	    }
	    this.forword = graphPanel;
	}
	/**
	 * 图形  
	 * @param request
	 * @param response
	 */
	protected void graph(HttpServletRequest request,HttpServletResponse response)
	{
	    String graph = CommonUtils.getRequestParameter(request, "graph", "");
	    if(graph.equals(""))
	    {
	            System.out.println("导出地址不能为空!");
	    }
	    this.forword = graph ;
	}
	/**
	 * 导出excel  
	 * @param request
	 * @param response
	 */
	protected void excelPrint(HttpServletRequest request,HttpServletResponse response)
	{
	    String excelPrint = CommonUtils.getRequestParameter(request, "excelPrint", "");
	    if(excelPrint.equals(""))
	    {
	            System.out.println("导出地址不能为空!");
	    }
	    this.forword = excelPrint;
	}
	
	 /**
     * 打印 
     * @param request
     * @param response
     */
    protected void Print(HttpServletRequest request,HttpServletResponse response)
    {
        String Print = CommonUtils.getRequestParameter(request, "Print", "");
        if(Print.equals(""))
        {
                System.out.println("打印地址不能为空!");
        }
        this.forword = Print;
    }
    
    /**
     * pdf导出 
     * @param request
     * @param response
     */
    protected void PDFPrint(HttpServletRequest request,HttpServletResponse response){}
	
	
    /**
     * 显示图形的基本方法
     * 使用者实现这样的接口方法 charBaseData.setCharBaseData() 来组织好数据   
     * @param request
     * @return
     */
    protected String chartView(HttpServletRequest request,ICharStrategy charBaseData)
    {
        String graphtype = CommonUtils.getRequestParameter(request,"graphtype","");  // 图形格式
        LinkedHashMap<String, String> chart  = charBaseData.setCharBaseData();
        String strChar = "";
        switch(Integer.parseInt(graphtype))
        {
            case 1:                     //柱形图
                strChar = ChartUtils.ColumnChart(chart);
                break;
            case 3:                     //折线图
                strChar = ChartUtils.LineChart(chart);
                break;
            case 6:                     //饼图
                strChar = ChartUtils.PieChart(chart);
                break;
        }
        request.setAttribute("ChartsPage", strChar);
        return strChar; 
    }
    
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
		this.doGet(request, response);
	}
	/**
	 * 执行插入数据
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<TCommonRecord> insert(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	/**
	 * 执行查看或者转向修改页面
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<TCommonRecord> modify(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	/**
	 * 执行更新
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<TCommonRecord> update(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	/**
	 * 执行删除
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<TCommonRecord> delete(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	/**
	 * 执行转向添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<TCommonRecord> addNew(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	
	/**
	 * 执行查询
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<TCommonRecord> query(HttpServletRequest request,HttpServletResponse response) {
		return null;
	}
	
	/**
	 * 用户自定义方法
	 * @param o
	 * @param request
	 * @param response
	 * @return
	 */
	protected List<TCommonRecord> option(String o,HttpServletRequest request,HttpServletResponse response){
		return null;
	}
}
