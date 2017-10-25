package com.ts.controller.system.init;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.ts.service.pdss.pdss.Cache.InitPdssCache;
import com.ts.util.Logger;
import com.ts.util.PageData;
import com.ts.util.UuidUtil;


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
}
