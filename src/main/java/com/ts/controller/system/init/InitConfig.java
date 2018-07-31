package com.ts.controller.system.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.fabric.xmlrpc.base.Array;
import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.dao.DAO;
import com.ts.entity.Page;
import com.ts.interceptor.webservice.ApplicationProperties;
import com.ts.service.pdss.pdss.Cache.InitPdssCache;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;
import com.ts.util.HTTP.HTTPURLConnection;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="/InitConfig")  
public class InitConfig extends BaseController {

	Logger logger = Logger.getLogger("InitConfig");
    
	@Resource(name="initPdssCache")
	private InitPdssCache ipc ; 
	@Resource(name="daoSupportPdss")
	private DAO daoPdss; 
	
	
	/**
	 * 初始化信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/Init")
	@ResponseBody
	public Object listAllRole(Model model)throws Exception{
		Map<String, Object> map = new HashMap<String , Object>();
		ipc.bulidRedisCache();
		map.put("result", "ok");
		return  map;
	}
	
	@RequestMapping(value="/DrugRepeat")
	@ResponseBody
	public Object drugRepeat() throws Exception {
	    Map<String, Object> map = new HashMap<String , Object>();
	    Page page = new Page();
	    PageData pd= this.getPageData();
	    page.setPd(pd);
	    List<PageData> pds = (List<PageData>) daoPdss.findForList("DrugRepeatMapper.findDrugRepeat", null);
	    List<PageData> rspd = new ArrayList<PageData>();
	    int i = 0;
	    for(PageData rs :pds)
	    {
	        System.out.println("第：（" + (++i) + "）记录");
	        String drugName1 = rs.getString("drug_name_1");
	        String drugName2 = rs.getString("drug_name_2");
	        PageData drug = new PageData();
	        drug.put("drug_name", drugName1);
	        List<PageData> pddrugs1 = (List<PageData>) daoPdss.findForList("DrugRepeatMapper.findDrug", drug);
	        if(pddrugs1.size() > 0 )
	            rs.put("drug_class_1", pddrugs1.get(0).getObjectString("DRUG_CLASS_ID"));
	        else {
	            rs.put("drug_class_1", "");
	        }
	        drug.put("drug_name", drugName2);
	        List<PageData> pddrugs2 = (List<PageData>) daoPdss.findForList("DrugRepeatMapper.findDrug", drug);
            if(pddrugs2.size() > 0 )
                rs.put("drug_class_2", pddrugs2.get(0).getObjectString("DRUG_CLASS_ID"));
            else {
                rs.put("drug_class_2", "");
            }
	        rspd.add(rs);
	    }
	    map.put("result:", rspd.size());
	    daoPdss.batchUpdate("DrugRepeatMapper.updateDrugClass", rspd);
	    return  map;
	}
	
	
	@RequestMapping(value="/DrugIvEffectClass")
    @ResponseBody
    public Object drugIvEffectSUPP() throws Exception {
        Map<String, Object> map = new HashMap<String , Object>();
        Page page = new Page();
        PageData pd= this.getPageData();
        page.setPd(pd);
        List<PageData> pds = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrugEffect", null);
        List<PageData> rspd = new ArrayList<PageData>();
        List<PageData> rsSupp = new ArrayList<PageData>();
        int i = 0;
        String drugName = "";
        for(PageData rs :pds)
        {
            System.out.println("第：（" + (++i) + "）记录");
            String drugName1 = rs.getString("drug_name1");
            String drugName2 = rs.getString("drug_name2");
//            drugName = "".equals(drugName1) ?drugName :drugName1;
//            drugName1 =  drugName ;
//            rs.put("DRUG_NAME1", drugName1);
//            rs.put("DRUG_NAME2", drugName2);
//            rs.put("IV_CLASS_CODE1", "");
//            rs.put("IV_CLASS_CODE2", "");
//            rspd.add(rs);  
            
            PageData drug = new PageData();
//            drug.put("drug_name", drugName1);
            drug.put("drug_name", "%" + drugName1 + "%");
            List<PageData> pddrugs1 = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrug", drug);
            if(pddrugs1.size() == 0)
            {
                drug.put("drug_name", "%" + drugName1 + "%");
                pddrugs1 = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrug", drug);
            }
//            drug.put("drug_name", drugName2);
            drug.put("drug_name", "%" + drugName1 + "%");
            List<PageData> pddrugs2 = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrug", drug);
            if(pddrugs2.size() == 0)
            {
                drug.put("drug_name", "%" + drugName1 + "%");
                pddrugs1 = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrug", drug);
            }
            Integer ivClassid1 = null;
            Integer ivClassid2 = null;
            String  ivDrugName1 = drugName1;
            String  ivDrugName2 = drugName2;  
            try
            {
                for(int a = 0 ; a <pddrugs1.size() ; a++)
                {
                    for(int  b = 0 ; b < pddrugs2.size() ; b++)
                    {
                        if(pddrugs1.get(a) != null && pddrugs2.get(b) != null  &&  pddrugs1.get(a).getInt("iv_class_code") != null && pddrugs2.get(b).getInt("iv_class_code") != null)
                        {
                            if(pddrugs1.get(a).getInt("iv_class_code") < pddrugs2.get(b).getInt("iv_class_code"))
                            {
                                ivClassid1 = pddrugs1.get(a).getInt("iv_class_code");
                                ivClassid2 = pddrugs2.get(b).getInt("iv_class_code");
                                ivDrugName1 = drugName1 ;
                                ivDrugName2 = drugName2 ;
                                
                            }else
                            {
                                ivClassid1 = pddrugs2.get(b).getInt("iv_class_code");
                                ivClassid2 = pddrugs1.get(a).getInt("iv_class_code");
                                ivDrugName1 = drugName2;
                                ivDrugName2 = drugName1;
                            }    
                        }
                        rs.put("DRUG_NAME1", ivDrugName1);
                        rs.put("DRUG_NAME2", ivDrugName2);
                        rs.put("IV_CLASS_CODE1", ivClassid1);
                        rs.put("IV_CLASS_CODE2", ivClassid2);
                        
                        if(b==0)
                        {
                            rspd.add(rs);
                        }
                        else
                        { 
                            rs.put("id",UuidUtil.get32UUID());
                            rsSupp.add(rs); 
                        }
                    }
                }
            }   
            catch(Exception e )
            { 
                e.printStackTrace();
            }
        }
        map.put("result:", rspd.size() + rsSupp.size());
        daoPdss.batchUpdate("DrugCleanManagerMapper.updateIvClass", rspd);
        daoPdss.batchUpdate("DrugCleanManagerMapper.addIvClass", rsSupp);
        return  map;
    }
	
	@RequestMapping(value="/drugDoseClassIdClean")
    @ResponseBody
    public Object drugDoseClassIdClean() throws Exception {
	    Map<String, Object> map = new HashMap<String , Object>();
	    Page page = this.getPage();
	    int pageNum = 1;
        page.setShowCount(5000);
        page.setTotalPage(pageNum);
        List<PageData> rs = null;
        int total = 1;
        while( pageNum == 1 || pageNum <= page.getTotalPage()){
            rs = new ArrayList<>();
            page.setCurrentPage(pageNum);
            List<PageData> drugList =  (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrugByAllPage", page);
            for(PageData p : drugList)
            {
                String drug_name = p.getString("drug_name");
                List<PageData> list =  (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.finddrug_dosageBydrugName", drug_name);
                if(list != null && list.size() != 0  ) {
                    PageData  pd = new PageData();
                    pd.put("dose_class_id", list.get(0).getInt("dose_class_id"));
                    pd.put("drug_id", p.getInt("drug_id"));
                    rs.add(pd);
                    total++;
                }
            }
            daoPdss.batchUpdate("DrugCleanManagerMapper.updateDrugByDoseClassId", rs);
            pageNum = page.getCurrentPage() + 1;
            logger.info("drug_table--total:"+page.getTotalPage() +"/第" + pageNum +"页");
        }
        
	    logger.info("update count  :" +total);
	    return map ;
	}
	
	@RequestMapping(value="/drugDoseClassIdClean01")
    @ResponseBody
    public Object drugDoseClassIdClean01() throws Exception {
        Map<String, Object> map = new HashMap<String , Object>();
        Page page = this.getPage();
        int pageNum = 1;
        page.setShowCount(100);
        page.setTotalPage(pageNum);
        List<PageData> rs = null;
        int total = 1;
        while( pageNum == 1 || pageNum <= page.getTotalPage()){
            rs = new ArrayList<>();
            page.setCurrentPage(pageNum);
            List<PageData> drugList =  (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrugByAllPage", page);
            for(PageData p : drugList)
            {
                int dose_class_id = p.getInt("dose_class_id");
                List<PageData> list =  (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.finddrug_dosageByDoseClassId", dose_class_id);
                if(list != null && list.size() > 0  ) {
                    continue;
                }
                String drug_name=p.getString("drug_name");
                list =  (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.finddrug_dosageBydrugName", drug_name);
                PageData  pd = new PageData();
                pd.put("dose_class_id", "");
                if(list != null && list.size() > 0  ) {
                    pd.put("dose_class_id", list.get(0).getInt("dose_class_id"));
                }
                pd.put("drug_id", p.getInt("drug_id"));
                rs.add(pd);
                total++;
            }
            daoPdss.batchUpdate("DrugCleanManagerMapper.updateDrugByDoseClassId", rs);
            pageNum = page.getCurrentPage() + 1;
            logger.info("drug_table--total:"+page.getTotalPage() +"/第" + pageNum +"页");
        }
        
        logger.info("update count  :" +total);
        return map ;
    }
	

    @RequestMapping(value="/specialdrugClassIdClean")
    @ResponseBody
    public Object specialdrugClassIdClean() throws Exception {
        Map<String, Object> map = new HashMap<String , Object>();
        List<PageData> entitys = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findspecial", "");
        List<PageData> rs = new ArrayList<>()   ;
        for(PageData pd : entitys)
        {
            String drug_name = NLPDrugRslt(pd.getString("drug_name"));
            List<PageData> list = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findDrugByDrugClassId", drug_name);
            if(list == null || list.size() == 0 ) continue;
            PageData p = new PageData();
            p.put("drug_code", pd.getString("drug_code"));
            p.put("drug_class_id", list.get(0).getString("drug_class_id"));
            rs.add(p);  
        }
        daoPdss.batchUpdate("DrugCleanManagerMapper.updatespecialByDrugClassId", rs);
        return "okay";
    }
    @RequestMapping(value="/specialdrugClassIdClean01")
    @ResponseBody
    public Object specialdrugClassIdClean01() throws Exception {
        Map<String, Object> map = new HashMap<String , Object>();
        List<PageData> entitys = (List<PageData>) daoPdss.findForList("DrugCleanManagerMapper.findspecial", "");
//        List<PageData> rs = new ArrayList<>();
        daoPdss.batchUpdate("DrugCleanManagerMapper.updateDrugUseDetail", entitys);
        return "okay";
    }
    
    
    private String NLPDrugRslt(String param)
    {
        String drugName = param ;
        try
        {
            String url = ApplicationProperties.getPropertyValue("NLPUrl");
//            String url = "http://10.10.41.25:10011";
//            String rs = HttpRequest.sendPost(url, "s=" + param);
//            HttpClientUtil http = new HttpClientUtil();
//            String rs = http.doPost(url, "s=" + param, "utf-8");
            String rs =  HTTPURLConnection.readContentFromPost(url, param);
            System.out.println(rs);
            JSONObject j = JSONObject.fromObject(rs);
            System.out.println(j);
            Iterator iterator = j.keys();
            while (iterator.hasNext())
            {
                String key = (String) iterator.next();// 术语类型 疾病
                if (key.equals("entity"))
                {
                    // "entity" :
                    // "急性支气管炎【疾病】<br/>咳嗽【症状】<br/>气道高反应【症状】<br/>【转换】性障碍【症状】",
                    String entity = j.getString(key);
                    String[] nlpstrs = entity.split("<br/>");
                    for (int k = 0; k < nlpstrs.length; k++)
                    {

                        if (!(nlpstrs[k] == "" || "".equals(nlpstrs[k])))
                        {
                            String type  = nlpstrs[k].substring(nlpstrs[k].lastIndexOf("【"),nlpstrs[k].length() -1 );
                            if(type.indexOf("药品") != -1||type.indexOf("保健食品") != -1)
                            {
                                drugName = nlpstrs[k].substring(0,nlpstrs[k].lastIndexOf("【")); 
                                break;
                            }
                        }
                    }

                }
            }
        
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        return drugName;
    }
}
