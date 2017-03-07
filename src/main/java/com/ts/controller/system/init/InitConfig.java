package com.ts.controller.system.init;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.service.pdss.pdss.Cache.InitPdssCache;


@Controller
@RequestMapping(value="/InitConfig")
public class InitConfig extends BaseController {

	
	@Resource(name="initPdssCache")
	private InitPdssCache ipc ; 
	
	/**
	 * 初始化信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/Init")
	@ResponseBody
	public Object listAllRole(Model model,String ROLE_ID)throws Exception{
		Map<String, Object> map = new HashMap<String , Object>();
		ipc.bulidRedisCache();
		map.put("result", "ok");
		return  map;
	}
	
}
