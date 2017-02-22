package com.hitzd.his.Web.Taglibs.Plugs;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.hitzd.DBUtils.TCommonRecord;

/**
 * 用于画双轴图
 * @author Administrator
 *
 */
public class TagBarLineGraph extends TagSupport 
{
    private static final long serialVersionUID = 1L;
    /* 标题 */
    private String title = "";
	private String barLabel;
	private String lineLabel;
	private String xLabel = "";
	private String yLabel = "";
	private String startDate = "";
	private String endDate = "";
	private List<String> x;
	private List<String> y1;
	private List<String> y2;
	private String width = "";
	private String height = "";
	
	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}
	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setBarLabel(String barLabel) {
		this.barLabel = barLabel;
	}
	public void setLineLabel(String lineLabel) {
		this.lineLabel = lineLabel;
	}
	public void setX(List<String> x) {
		this.x = x;
	}
	public void setY1(List<String> y1) {
		this.y1 = y1;
	}
	public void setY2(List<String> y2) {
		this.y2 = y2;
	}
	public void setWidth(String width)
	{
		this.width = width;
	}
	public void setHeight(String height) 
	{
		this.height = height;
	}
	@Override
	public int doEndTag() throws JspException 
	{
		//初始化width和height的值
		//默认情况下，高度为600, 宽度为：50*size+140
		init();
		JspWriter writer = this.pageContext.getOut();
		String serialId  = UUID.randomUUID().toString();
		String aid       = UUID.randomUUID().toString();
		//准备标题
		if("".equals(title)){title = xLabel + ": " + barLabel + "-" + lineLabel  ;}
		String dateString = "\";";
		if(!"".equals(startDate) && !"".equals(endDate))
			dateString = "<div style='height:10px;'>&nbsp;</div><span style='font-size:12px;padding-top:30px;'>" +
			"开始时间：" +startDate+ "&nbsp;&nbsp;结束时间"+endDate+"</span>\";";
		try 
		{
			writer.append("<div><div id='"+serialId+ "' style='height:"+height+"px;width:"+width+"px;' /></div>\n");
			writer.append("<a id="+aid+" href='javascript:void(0)' style='text-decoration: none;color: black;font-size: 12px;'>点击下载图片</a>\n");
			writer.append("<script type=\"text/javascript\">\n");
			writer.append("$(document).ready(function () {\n" +
		            "                    $.jqplot.config.enablePlugins = true;\n");
			writer.append("var title = \"<span>"+title+"</span><br/>" + dateString + "\n");
		    writer.append("              $.jqplot('"+serialId+"', ["+GraphTagUtil.get2ListValue(x,y1)+","+GraphTagUtil.get2ListValue(x,y2)+"], {\n" +
		            "                        title:title,\n" +                  
		            "                        animate: true,\n" +
		            "                        animateReplot: true,\n" +
		            "                        cursor: {\n" +
		            "                            show: true,\n" +
		            "                            zoom: true,\n" +
		            "                            looseZoom: true,\n" +
		            "                            showTooltip: false\n" +
		            "                        },\n" +
		            "                        series:[\n" +
		            "                            {\n" +
		            "                                label:'"+barLabel+"',\n" +
		            "                                pointLabels:{ show:true, location:'s',ypadding:-15 },\n" +
		            "                                renderer: $.jqplot.BarRenderer,\n" +
		            "                                showHighlight: false,\n" +
		            "                                rendererOptions: {\n" +
		            "                                    animation: {\n" +
		            "                                        speed: 2500\n" +
		            "                                    },\n" +
		            "                                    highlightMouseOver: false\n" +
		            "                                }\n" +
		            "                            },\n" +
		            "                            {\n" +
		            "                                label:'"+lineLabel+"',\n" +
		            "                                pointLabels:{ show:true, location:'s',ypadding:-10 },\n" +
		            "                                showHighlight: false,\n" +
		            "                                rendererOptions: {\n" +
		            "                                    animation: {\n" +
		            "                                        speed: 2500\n" +
		            "                                    }\n" +
		            "                                }\n" +
		            "                            }\n" +
		            "                        ],\n" +
		            "                        legend: {\n" +
		            "                            show: true,\n" +
		            "                            placement: 'outsideGrid'\n" +
		            "                        },\n" +
		            "                        axesDefaults: {\n" +
		            "                        },\n" +
		            "                        axes: {\n" +
		            "                            xaxis: {\n" +
		            "                                label:'" +xLabel+ "',\n" +
		            "                                renderer: $.jqplot.CategoryAxisRenderer,\n" +
		            "                                tickRenderer: $.jqplot.CanvasAxisTickRenderer,\n" +
		            "                                tickOptions: {\n" +
		            "                                    angle:30\n" +
		            "                                }\n" +
		            "                            },\n" +
		            "                            yaxis: {\n" +
		            "                                label:'" +yLabel+ "',\n" +
		            "                                labelRenderer: $.jqplot.CanvasAxisLabelRenderer," +
		            "                                rendererOptions: {\n" +
		            "                                    forceTickAt0: true,\n" +
		            "                                    min:0\n" +
		            "                                }\n" +
		            "                            }\n" +
		            "                        }\n" +
		            "                    });\n" +
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
	private void init() 
	{
		if("".equals(height))
		{
			setHeight("500");
		}
		if("".equals(width))
		{
			setWidth(((x.size()*55+140)>500?(x.size()*55+140):500)+"");
		}
		
	}
	private void reset()
	{
		setTitle("");
		setHeight("");
		setWidth("");
		setxLabel("");
		setyLabel("");
		setStartDate("");
		setEndDate("");
		setBarLabel("");
		setLineLabel("");
	}
}
