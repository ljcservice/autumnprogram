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
import com.ts.util.PageData;


@Controller
@RequestMapping(value="/InitConfig")
public class InitConfig extends BaseController {

	
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
	
}
