package com.hitzd.his.Web.Taglibs.Plugs;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * 
 * @author hershel
 * 用于画饼图.
 *
 */

public class TagPieGraph extends TagSupport 
{
	/*标题，可选*/	
	private String title = "";
	/*X轴*/
	private List<String> x;
	/*Y轴*/
	private List<String> y;
	
	private String xLabel = "";
	
	private String yLabel = "";
	
	private String height = "500";
	private String width = "500";
	
	private String startDate = "";
	private String endDate = "";
	
	/*前多少个*/
	private int number = 7;
	
	
	public void setxLabel(String xLabel) 
	{
		this.xLabel = xLabel;
	}
	public void setyLabel(String yLabel) 
	{
		this.yLabel = yLabel;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	public void setX(List<String> x) 
	{
		this.x = x;
	}
	public void setY(List<String> y) 
	{
		this.y = y;
	}
	public void setHeight(String height) 
	{
		this.height = height;
	}
	public void setWidth(String width) 
	{
		this.width = width;
	}
	
	public void setStartDate(String startDate) 
	{
		this.startDate = startDate;
	}
	public void setNumber(int number)
	{
		this.number = number;
		
	}
	public void setEndDate(String endDate) 
	{
		this.endDate = endDate;
	}
	public void init()
	{
		if("".equals(height)){setHeight(((x.size()*26)>500?(x.size()*35):500) + "");}
		if("".equals(width) ){setWidth("500");}//自定义宽度
	}
	private void reset()
	{
		setTitle("");
		setxLabel("");
		setyLabel("");
		setHeight("500");
		setWidth("500");
		setStartDate("");
		setEndDate("");
	}
	@Override
	public int doEndTag() throws JspException 
	{
		JspWriter writer = this.pageContext.getOut();
		String serialId = UUID.randomUUID().toString();
		String aid = UUID.randomUUID().toString();
		
		//处理前多少个
		x = x.subList(0, number);
		y = y.subList(0, number);
		//准备标题
		if("".equals(title)){title = xLabel + "-" + yLabel ;}
		String dateString = "\";";
		if(!"".equals(startDate) && !"".equals(endDate))
			dateString = "<div style='height:10px;'>&nbsp;</div><span style='font-size:12px;padding-top:30px;'>" +
			"开始时间：" +startDate+ "&nbsp;&nbsp;结束时间"+endDate+"</span>\";";
		try 
		{
			writer.append("<div><div id='"+serialId+ "' style='height:"+height+"px;width:"+width+"px;' /></div>\n");
			writer.append("<a id="+aid+" href='javascript:void(0)' style='text-decoration: none;color: black;font-size: 12px;' >点击下载图片</a>\n");
			writer.append("<script type=\"text/javascript\">\n");
			writer.append("$(document).ready(function () {\n" +
	"                    $.jqplot.config.enablePlugins = true;\n");
			writer.append("var title = \"<span>"+title+"</span><br/>" + dateString + "\n");
			writer.append("$.jqplot('" + serialId +"', [" + GraphTagUtil.get2ListValue(x,y)+ "], {\n" +
	"                        title: title,\n" +
	"                        grid: {\n" +
	"                            drawBorder: false,\n" +
	"                            drawGridlines: false,\n" +
	"                            background: '#ffffff',\n" +
	"                            shadow:false\n" +
	"                        },\n" +
	"                        seriesDefaults:{\n" +
	"                            renderer: $.jqplot.PieRenderer,\n" +
	"                            rendererOptions: {\n" +
	"                                showDataLabels: true\n" +
	"                            }\n" +
	"                        },\n" +
	"                        legend:{\n" +
	"                            show:true," +
	"							 rendererOptions: {numberRows:22}\n" +
	"                        }\n" +
	"                    });"+
	"                    var chart = $('#"+serialId+"');\n" +
	"                    try{\n" +
	"                        if(!!document.createElement('canvas').getContext){\n" +
	"                            document.getElementById('"+aid+"').onclick=function (){\n" +
	"                                chart.jqplotSaveImage();\n" +
	"                            };\n" +
	"                        }else{\n" +
	"                            $('#" +aid+ "').text('对不起，您的浏览器不支持图片导出,请升级到支持HTML5浏览器');\n"+
	"                        }      \n"+
	"                    }catch(e){\n" +
	"                         $('#" +aid+ "').hide();\n"+
	"                    };"+
	"                });\n");
				writer.append("</script>\n");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		reset();
		return super.doEndTag();
	}
	
}
