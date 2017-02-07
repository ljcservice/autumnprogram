package com.ts.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.ts.entity.ai.Total;
import com.ts.entity.system.Menu;
/**
 * 项目名称：
 * @author:
 * 修改日期：2015/11/2
*/
public class Const {
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";		//验证码
	public static final String SESSION_USER = "sessionUser";					//session用的用户
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";		//用户功能权限
	public static final String SESSION_USER_MAX_ROLE = "userMaxRole";			//用户最大角色类型
	public static final String SESSION_USERNAME = "userName";					//用户名
	public static final String SESSION_ALL_MENU_LIST = "allMenuList";			//全部菜单
	public static final String SESSION_CURRENT_MENU_LIST = "currentMenuList";	//当前菜单
	public static final String SESSION_userpds = "userpds";
	
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String LOGIN = "/login_toLogin.do";				//登录地址
	public static final String NO_RIGHTS = "/staticrights.jsp";			//登录地址
	public static final String SYSNAME = "admin/config/SYSNAME.txt";	//系统名称路径
	public static final String PAGE	= "admin/config/PAGE.txt";			//分页条数配置路径
	public static final String EMAIL = "admin/config/EMAIL.txt";		//邮箱服务器配置路径
	public static final String SMS1 = "admin/config/SMS1.txt";			//短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt";			//短信账户配置路径2
	public static final String FWATERM = "admin/config/FWATERM.txt";	//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";	//图片水印配置路径
	public static final String WEIXIN	= "admin/config/WEIXIN.txt";	//微信配置路径
	public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";//WEBSOCKET配置路径
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";	//图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/";		//文件上传路径
	public static final String EXCEL = "excel/";						//excel模板路径
	public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/"; //二维码存放路径
	public static final String NO_INTERCEPTOR_PATH = ".*/((plugins)|(login)|(logout)|(code)|(app)|(weixin)|(static)|(main)|(websocket)).*";	//不对匹配该值的访问路径拦截（正则）
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	
	//菜单功能的标志
	public static final String MENU_FUN_FLAG = "function";
	//菜单、角色树跟节点ID
	public static final String TREE_ROOT_ID = "0";
	//菜单缓存父节点关键字
	public static final String SYS_MENU_FATHER_ID = "sysMenuFatherId";
	//菜单缓存
	public static final Map<String,List<Menu>> SYS_MENU_MAP = new HashMap<String,List<Menu>>(200);
	//MTS校验，用户与批次号
	public static final Map<String,String> QUESTION_MTS_CHECK = new HashMap<String,String>();
	//MTS校验，批次号与进度
	public static final Map<String,Total> QUESTION_MTS_BATCH_NUMBER= new HashMap<String,Total>();
	
	
	/*
	 * app业务使用
	 * 
	 */
	//app注册接口_请求协议参数)
	public static final String[] APP_REGISTERED_PARAM_ARRAY = new String[]{"countries","uname","passwd","title","full_name","company_name","countries_code","area_code","telephone","mobile"};
	public static final String[] APP_REGISTERED_VALUE_ARRAY = new String[]{"国籍","邮箱帐号","密码","称谓","名称","公司名称","国家编号","区号","电话","手机号"};
	
	//app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};


}
