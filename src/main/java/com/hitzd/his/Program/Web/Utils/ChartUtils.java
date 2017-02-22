package com.hitzd.his.Program.Web.Utils;

import java.util.LinkedHashMap;
import java.util.Map;
import com.hitzd.his.report.utils.IPage;

public class ChartUtils 
{

	/**
     * 柱形图组织数据 和图形
     * @param request
     * @param value  key ： 要显示的名字  ,value ：显示数值 
     */
    public static String ColumnChart(IPage page, LinkedHashMap<String, String> value)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<script  language=\"javascript\">");
        sb.append("$(document).ready(function()");
        sb.append("{");
        sb.append("var line1 = ").append(getMapValue(value)).append(";"); 
        sb.append("var plot1b = $.jqplot('chart1b', [line1], {");
        sb.append(" title: '柱状图',");
        sb.append(" series:[{renderer:$.jqplot.BarRenderer}],");
        sb.append(" axesDefaults: {");
        sb.append(" tickRenderer: $.jqplot.CanvasAxisTickRenderer ,");
        sb.append(" tickOptions: {");
        sb.append(" fontFamily: '宋体',");
        sb.append(" fontSize: '10pt',");
        sb.append(" textColor: 'blue',");
        sb.append(" angle: -30");
        sb.append(" }");
        sb.append(" },");
        sb.append(" axes: {");
        sb.append(" xaxis: {");
        sb.append(" renderer: $.jqplot.CategoryAxisRenderer");
        sb.append(" }");
        sb.append(" }");
        sb.append(" });");
        sb.append(" });");
        sb.append(" </script>");
        return sb.toString();
    }
    
    public static String ColumnChart(LinkedHashMap<String, String> value)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<script  language=\"javascript\">");
        sb.append("$(document).ready(function()");
        sb.append("{");
        sb.append("var line1 = ").append(getMapValue(value)).append(";"); 
        sb.append("var plot1b = $.jqplot('chart1b', [line1], {");
        sb.append(" title: '柱状图',");
        sb.append(" series:[{renderer:$.jqplot.BarRenderer}],");
        sb.append(" axesDefaults: {");
        sb.append(" tickRenderer: $.jqplot.CanvasAxisTickRenderer ,");
        sb.append(" tickOptions: {");
        sb.append(" fontFamily: '宋体',");
        sb.append(" fontSize: '10pt',");
        sb.append(" textColor: 'blue',");
        sb.append(" angle: -30");
        sb.append(" }");
        sb.append(" },");
        sb.append(" axes: {");
        sb.append(" xaxis: {");
        sb.append(" renderer: $.jqplot.CategoryAxisRenderer");
        sb.append(" }");
        sb.append(" }");
        sb.append(" });");
        sb.append(" });");
        sb.append(" </script>");
        return sb.toString();
    }
    
    /**
     * 饼图组织数据和图形
     * @param request
     * @param value key ： 要显示的名字  ,value ：显示数值
     */
    public static String PieChart(IPage page, LinkedHashMap<String, String> value)
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<script  type=\"text/javascript\">");
        sb.append("$(document).ready(function(){");
        sb.append("var data = ").append(getMapValue(value)).append(";");
        sb.append("var plot1 = jQuery.jqplot ('chart1b', [data],"); 
        sb.append(" { ");
        sb.append(" seriesDefaults: {");
                // Make this a pie chart.
        sb.append(" renderer: jQuery.jqplot.PieRenderer,"); 
        sb.append(" rendererOptions: {");
                  // Put data labels on the pie slices.
                  // By default, labels show the percentage of the slice.
        sb.append(" showDataLabels: true");
        sb.append(" }");
        sb.append(" }, ");
        sb.append(" legend: { show:true, location: 's', rendererOptions:"); 
        sb.append("{");
        sb.append(" numberRows:4");
        sb.append(" }}");
        sb.append(" }");
        sb.append(" );");
        sb.append(" });");
        sb.append(" </script>");
        return sb.toString();
    }
    
    public static String PieChart(LinkedHashMap<String, String> value)
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<script  type=\"text/javascript\">");
        sb.append("$(document).ready(function(){");
        sb.append("var data = ").append(getMapValue(value)).append(";");
        sb.append("var plot1 = jQuery.jqplot ('chart1b', [data],"); 
        sb.append(" { ");
        sb.append(" seriesDefaults: {");
                // Make this a pie chart.
        sb.append(" renderer: jQuery.jqplot.PieRenderer,"); 
        sb.append(" rendererOptions: {");
                  // Put data labels on the pie slices.
                  // By default, labels show the percentage of the slice.
        sb.append(" showDataLabels: true");
        sb.append(" }");
        sb.append(" }, ");
        sb.append(" legend: { show:true, location: 's', rendererOptions:"); 
        sb.append("{");
        sb.append(" numberRows:4");
        sb.append(" }}");
        sb.append(" }");
        sb.append(" );");
        sb.append(" });");
        sb.append(" </script>");
        return sb.toString();
    }

    /**
     * 折线图组织数据和图形
     * @param request
     * @param value key ： 要显示的名字  ,value ：显示数值
     */

    public static String LineChart(IPage page, LinkedHashMap<String, String> value)
    {
        StringBuffer sb = new StringBuffer();
        return sb.toString();
    }
    
    public static String LineChart(LinkedHashMap<String, String> value)
    {
        StringBuffer sb = new StringBuffer();
        return sb.toString();
    }
   
    private static String getMapValue(Map<String, String> value)
    {
        StringBuffer sb = new StringBuffer();
        if(value.size()==0) return ""; 
        sb.append("[");
        for(String key : value.keySet())
        {
            sb.append("['").append(key).append("[").append(value.get(key)).append("]',").append(value.get(key)).append("],");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}