package com.hitzd.his.Web.Taglibs.Plugs;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TagConditionGraph extends TagSupport
{
	private String title = "图形绘制";
	private String url;
	private List<String> shapes;
	private List<String> x;
	private List<String> y1;
	private List<String> y2;
	
	private int shapeDefault = 1;
	private int xDefault = 0;
	private int y1Default = 0;
	private int y2Default = 0;
	
	public void setUrl(String url) 
	{
		this.url = url;
	}
	
	public void setShapeDefault(int shapeDefault) {
		this.shapeDefault = shapeDefault;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public void setShapes(List<String> shapes) 
	{
		this.shapes = shapes;
	}
	public void setX(List<String> x) 
	{
		this.x = x;
	}
	public void setY1(List<String> y1) 
	{
		this.y1 = y1;
	}
	public void setY2(List<String> y2) 
	{
		this.y2 = y2;
	}
	public void setxDefault(int xDefault) 
	{
		this.xDefault = xDefault;
	}
	public void setY1Default(int y1Default) 
	{
		this.y1Default = y1Default;
	}
	public void setY2Default(int y2Default) 
	{
		this.y2Default = y2Default;
	}
	private void reset(){
		setTitle("图形绘制");
		setUrl("");
		setShapeDefault(1);
		setxDefault(0);
		setY1Default(0);
		setY2Default(0);
	}
	@Override
	public int doEndTag() throws JspException 
	{
		JspWriter writer = this.pageContext.getOut();
		if(x == null || y1 == null || y2 == null)
		{
			throw new JspException("参数不能为空");
		}
		try 
		{
			writer.write("<html>\n" +
			        "<head>\n" +
			        "    <title>"+title+"</title>\n" +
			        "</head>\n" +
			        "<script language=\"javascript\" type=\"text/javascript\" src=\"/COMMON/jquery/jquery.min.js\"></script>\n" +
			        "<body>\n" +
			        "<script type=\"text/javascript\">\n" +
			        "    window.onload = (function () {\n" +
			        "        try {\n" +
			        "            $(\".shapeType\").change(function () {\n" +
			        "                //xy\n" +
			        "                if (shapes.isXY($(this).val())) {\n" +
			        "                    $('.y2').hide();\n" +
			        "                    $('.y2').find(\"option[text='--请选择--']\").attr(\"selected\", true);\n" +
			        "                    $('.y2Label').hide();\n" +
			        "                    $('.y1Label').text('Y轴:');\n" +
			        "                }\n" +
			        "                //xy2\n" +
			        "                else if (shapes.isXY2($(this).val())) {\n" +
			        "                    $('.y2').show();\n" +
			        "                    $('.y2Label').show();\n" +
			        "                    $('.y1Label').text('Y轴1:');\n" +
			        "                }\n" +
			        "            })\n" +
			        "                    .change();\n" +
			        "        } catch (e) {\n" +
			        "        }\n" +
			        "        document.graphForm.submit(); "+
			        "    });\n" +
			        "    var shapes = {\n" +
			        "        xy: [\"饼图\", \"曲线图\", \"柱状图\",, \"折线图\"],\n" +
			        "        xy2: [\"双轴图\"],\n" +
			        "        isXY: function (shape) {\n" +
			        "            for (var i = 0; i < this.xy.length; i++) {\n" +
			        "                if (this.xy[i] == shape) {\n" +
			        "                    return true;\n" +
			        "                }\n" +
			        "            }\n" +
			        "            return false;\n" +
			        "        },\n" +
			        "        isXY2: function (shape) {\n" +
			        "            for (var i = 0; i < this.xy2.length; i++) {\n" +
			        "                if (this.xy2[i] == shape) {\n" +
			        "                    return true;\n" +
			        "                }\n" +
			        "            }\n" +
			        "            return false;\n" +
			        "        }\n" +
			        "    };\n" +
			        "</script>\n" +
			        "<div class>\n" +
			        "    <form name='graphForm' action=\""+url+"\" method=\"post\" target=\"detail\">\n" +
			        "        <input type=\"hidden\" name=\"o\" value=\"GraphPanel\"/>\n" +
			        "        <label>图形类型:</label>\n" +
			        "        <select name=\"graphType\" class=\"shapeType\">\n" );
			
			for (int i = 0; i < shapes.size(); i++) 
			{
				writer.write("<option value='"+shapes.get(i)+"' " +(i==shapeDefault?"selected":"") + ">"+shapes.get(i)+"</option>");
			}
			writer.write("</select>\n" +
		            "    <label>X轴:</label>\n" +
		            "    <select name=\"x\">\n");
			for (int i = 0; i < x.size(); i++) 
			{
				writer.write("<option value="+x.get(i)+" " +(i==xDefault?"selected":"") + ">"+x.get(i)+"</option>");
			}
			writer.write("</select>\n" +
		            "        <label class=\"y1Label\">Y轴1:</label>\n" +
		            "    <select name=\"y1\" class=\"y1\">\n");
			for (int i = 0; i < y1.size(); i++) 
			{
				writer.write("<option value="+y1.get(i)+" " +(i==y1Default?"selected":"") + ">"+y1.get(i)+"</option>");
			}
			writer.write("</select>\n" +
		            "        <label class=\"y2Label\">Y轴2:</label>\n" +
		            "    <select name=\"y2\" class=\"y2\">\n");
			for (int i = 0; i < y2.size(); i++) 
			{
				writer.write("<option value="+y2.get(i)+" " +(i==y2Default?"selected":"") + ">"+y2.get(i)+"</option>");
			}
			writer.write("</select>\n" +
		            "        <input type=\"submit\" value=\"生成图形\"/>\n" +
		            "        <hr/>\n" +
		            "    </form>\n" +
		            "</div>\n" +
		            "<div>\n" +
		            "    <iframe id=\"detail\" name=\"detail\" scrolling=\"auto\" src='' " +
		            "            frameborder=\"0\" width='100%' height='550'></iframe>\n" +
		            "</div><hr/><center>@苏州鼎丞大通医疗科技有限公司</center>\n" +
		            "</body>\n" +
		            "</html>");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		reset();
		return super.doEndTag();
	}
	
	
	
	
	
	
	
	
}
 