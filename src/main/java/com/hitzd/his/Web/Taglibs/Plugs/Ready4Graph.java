package com.hitzd.his.Web.Taglibs.Plugs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class Ready4Graph extends TagSupport 
{
	/**
	 * 
	 * 准备画图
	 * 
	 * 	 */
	@Override
	public int doEndTag() throws JspException 
	{
		JspWriter writer = this.pageContext.getOut();
		try 
		{
			writer.append("<!--[if lt IE 9]>\n" +
			        "<script language=\"javascript\" type=\"text/javascript\" src=\"/COMMON/jqplot/excanvas.js\"></script>\n" +
			        "<![endif]-->\n" +
			        "<script language=\"javascript\" type=\"text/javascript\" src=\"/COMMON/jquery/jquery.min.js\"></script>\n" +
			        "<script language=\"javascript\" type=\"text/javascript\" src=\"/COMMON/jqplot/jquery.jqplot.min.js\"></script>\n" +
			        "<link rel=\"stylesheet\" type=\"text/css\" href=\"/COMMON/jqplot/jquery.jqplot.min.css\"/>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.categoryAxisRenderer.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.barRenderer.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.pointLabels.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.pieRenderer.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.dateAxisRenderer.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.dateAxisRenderer.min.js\"></script>\n" +
			        "<script type=\"text/javascript\" src=\"/COMMON/jqplot/plugins/jqplot.canvasTextRenderer.min.js\"></script>\n"
			        );
			writer.flush();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	
	

}
