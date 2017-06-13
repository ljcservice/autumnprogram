package com.ts.controller.pdss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.pdss.DirectionImage;
import com.ts.entity.system.User;
import com.ts.service.pdss.ShowService;
import com.ts.util.PageData;
import com.ts.util.Tools;

@Controller
@RequestMapping(value="/show")
public class ShowController extends BaseController{

	@Autowired
	private ShowService showService;
	
	/**
	 * 主页
	 */
	@RequestMapping(value="/index")
	public ModelAndView index(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			logger.info("into index page.");
			User user = getCurrentUser();
			if(user!=null){
				pd.put("SYSNAME", user.getUSERNAME());
			}
			mv.setViewName("show/index");
			mv.addObject("pd", pd);
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	@RequestMapping(value="/all")
	public ModelAndView all()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			User user = getCurrentUser();
			//查询树
			
			mv.setViewName("show/all");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	@RequestMapping(value="/tree")
	public void tree(HttpServletResponse response)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> treeList = null;
		String id = pd.getString("ID");
		if(Tools.isEmpty(id)){
			pd.put("ID", 0);//默认顶级菜单
		}
		String type = pd.getString("ONTO_TYPE");
		treeList = showService.treeRootList(pd);
		//默认都显示 + 符号
		if(treeList!=null){
			for(PageData p:treeList){
				if("0".equals(type)){//药品说明书
					if(pd.get("IS_LEAF")!=null&&"1".equals(pd.get("IS_LEAF"))){
						//根节点，增加跳转的iframe标识
						p.put("target", "treeFrame"); 
						p.put("url", new StringBuffer("show/list.do?ONTO_TYPE=").append(type)
								.append("&id=").append(p.get("ID").toString()).toString() 
								);
//								.append("&name=dasdsa")
//								.append(p.getString("name")).toString() 
						p.put("isParent", false);
					}else{
						p.put("isParent", true);
					}
				}else if ("1".equals(type)){//个性化给药
					p.put("target", "treeFrame"); 
					p.put("url", new StringBuffer("show/list?ONTO_TYPE=").append(type)
							.append("&id=").append(p.get("ID").toString()).toString());
					p.put("isParent", false);
				}else if ("2".equals(type)||"3".equals(type)||"4".equals(type)||"5".equals(type)||"6".equals(type)){
					if("1".equals(p.get("IS_LEAF").toString())){
						p.put("target", "treeFrame"); 
						p.put("url", new StringBuffer("show/list?ONTO_TYPE=").append(type)
								.append("&id=").append(p.get("ID").toString()).toString());
						p.put("isParent", false);
					}else {
						p.put("isParent", true);
					}
				}
				p.put("ONTO_TYPE", type);
			}
		}
		JSONArray arr = JSONArray.fromObject(treeList);
		// 替换为Ztree使用的参数TREE_ID
		String json = arr.toString();
		json = json.replaceAll("ID", "id").replaceAll("TARGET", "target").replaceAll("URL", "url").replaceAll("NAME", "name").replaceAll("ISPARENT", "isParent");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
	}
	@RequestMapping(value="/list")
	public ModelAndView list(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
		    //liujc  增加 
		    page.setCurrentjztsFlag(1);
			User user = getCurrentUser();
			page.setPd(pd);
			getRequest().getHeaderNames();
			List<PageData> list;
			String type = pd.getString("ONTO_TYPE");
			if("0".equals(type)){//药品说明书
				list = showService.queryList(page);
//				for(PageData p:list){
//					p.put("INGREDIENT", ConvertCharacter.specilConvertCharacter( p.getString("INGREDIENT")));
//					p.put("INDICATION", ConvertCharacter.specilConvertCharacter(p.getString("INDICATION")));
//				}
				mv.addObject("resultList",list);
				mv.setViewName("show/list");
			}else if ("1".equals(type)){//个性化给药
				pd.put("ID", Integer.valueOf(pd.getString("ID")));
				List<PageData> list1 = showService.queryIndividualItem(pd);
				List<PageData> list2 = showService.queryCollectDescDict(pd);
				List<PageData> list3 = showService.queryConsistencyRange(pd);
				mv.addObject("list1",list1);
				mv.addObject("list2",list2);
				mv.addObject("list3",list3);
				mv.setViewName("show/individualDictList");
			}else if ("2".equals(type)){//抗菌药物临床应用指导原则
				pd.put("CODE", 11);
				list = showService.queryDrugRelrefDirection(pd);
				for(PageData pp:list){
					java.sql.Clob clob = (java.sql.Clob) pp.get("CONTENT");
					if(clob!=null){
						java.io.Reader reder = clob.getCharacterStream();
						BufferedReader br = new BufferedReader(reder);
						StringBuffer sb = new StringBuffer();
						 for (String s= br.readLine();s!=null;) {
							sb.append(s);
							s= br.readLine();
						 }
						pp.put("content", sb.toString());
					}
				}
				mv.addObject("resultList",list);
				mv.setViewName("show/drugRelrefDirection");
			}else if ("3".equals(type)){//医学常用计算公式
				pd.put("CODE", 12);
				list = showService.queryDrugRelrefDirection(pd);
				for(PageData pp:list){
					java.sql.Clob clob = (java.sql.Clob) pp.get("CONTENT");
					if(clob!=null){
						java.io.Reader reder = clob.getCharacterStream();
						BufferedReader br = new BufferedReader(reder);
						StringBuffer sb = new StringBuffer();
						 for (String s= br.readLine();s!=null;) {
							sb.append(s);
							s= br.readLine();
						 }
						pp.put("content", sb.toString());
					}
				}
				mv.addObject("resultList",list);
				mv.setViewName("show/drugRelrefDirection");
			}else if ("4".equals(type)){//医药法规
				pd.put("CODE", 13);
				list = showService.queryDrugRelrefDirection(pd);
				for(PageData pp:list){
					java.sql.Clob clob = (java.sql.Clob) pp.get("CONTENT");
					if(clob!=null){
						java.io.Reader reder = clob.getCharacterStream();
						BufferedReader br = new BufferedReader(reder);
						StringBuffer sb = new StringBuffer();
						 for (String s= br.readLine();s!=null;) {
							sb.append(s);
							s= br.readLine();
						 }
						pp.put("content", sb.toString());
					}
				}
				mv.addObject("resultList",list);
				mv.setViewName("show/drugRelrefDirection");
			}else if ("5".equals(type)){//临床检验正常值及意义
				pd.put("CODE", 100);
				list = showService.queryDrugRelrefDirection(pd);
				for(PageData pp:list){
					java.sql.Clob clob = (java.sql.Clob) pp.get("CONTENT");
					if(clob!=null){
						java.io.Reader reder = clob.getCharacterStream();
						BufferedReader br = new BufferedReader(reder);
						StringBuffer sb = new StringBuffer();
						 for (String s= br.readLine();s!=null;) {
							sb.append(s);
							s= br.readLine();
						 }
						pp.put("content", sb.toString());
					}
				}
				mv.addObject("resultList",list);
				mv.setViewName("show/drugRelrefDirection");
			}else if ("6".equals(type)){//临床路径
				list = showService.queryClinicalPathwayInfo(pd);
				for(PageData pp:list){
					java.sql.Clob clob = (java.sql.Clob) pp.get("CONTENT");
					if(clob!=null){
						java.io.Reader reder = clob.getCharacterStream();
						BufferedReader br = new BufferedReader(reder);
						StringBuffer sb = new StringBuffer();
						 for (String s= br.readLine();s!=null;) {
							sb.append(s);
							s= br.readLine();
						 }
						pp.put("content", sb.toString());
					}
				}
				mv.addObject("resultList",list);
				mv.setViewName("show/clinicalPathwayInfo");
			}
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping(value="/detail")
	public ModelAndView detail( )throws Exception{
		ModelAndView mv = this.getModelAndView();
		User user = getCurrentUser();
		PageData pd = this.getPageData();
		try{
			List<PageData> list = showService.drugDirectionDetail(pd);
			Reader reder = null;
			BufferedReader br  = null;
			for(PageData pp:list){
				java.sql.Clob clob = (java.sql.Clob) pp.get("TCONTENT");
				if(clob!=null){
					reder = clob.getCharacterStream();
					br = new BufferedReader(reder);
					StringBuffer sb = new StringBuffer();
					for (String s= br.readLine();s!=null;) {
						sb.append(s);
						s= br.readLine();
					}
					//show/image?DIRECTION_NO=${item.DIRECTION_NO}&ITEM_NAME=
					String TCONTENT = sb.toString();
					if(TCONTENT.contains("src=\"")){
						TCONTENT = TCONTENT.replace("src=\"", "src=\"show/image?DIRECTION_NO="+pp.get("DIRECTION_NO").toString()+"&ITEM_NAME=");
					}
					if(TCONTENT.contains("src=\'")){
						TCONTENT = TCONTENT.replaceAll("src=\'", "src=\'show/image?DIRECTION_NO="+pp.get("DIRECTION_NO").toString()+"&ITEM_NAME=");
					}
					pp.put("TCONTENT",TCONTENT);
					br.close();
					reder.close();
				}
			}
			mv.addObject("resultList",list);
			mv.setViewName("show/detail");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	@RequestMapping(value="/image")
	public void image(HttpServletRequest request,HttpServletResponse response)throws Exception{
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream(); 
			response.setContentType("image/jpeg");
			PageData pd = this.getPageData();
			String ITEM_NAME = pd.getString("ITEM_NAME");
			if(!Tools.isEmpty(ITEM_NAME)){
				DirectionImage img = showService.drugDirectionImage(pd);
				if(img!=null&&img.getIMAGE().length>1){
					out.write(img.getIMAGE()); 
					out.flush(); 
				}
			}
			out.close();
		} catch (Exception e) {
		} finally{
			if(out!=null){
				out.close();
			}
		}
	}
	
	
}