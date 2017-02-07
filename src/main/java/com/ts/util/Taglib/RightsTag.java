package com.ts.util.Taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.shiro.session.Session;
import com.ts.entity.system.User;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.Tools;

/**
 * 自定义标签，权限过滤
 * @author silong.xing
 * <ts:rights code="user/listUsers">有权限时候输出内容<ts:norights>无权限时候输出内容</ts:norights></ts:rights>
 */
public class RightsTag extends BodyTagSupport {
	
	//有权限为true，无权限为flalse
	private boolean fathertagSucceeded = false;// 父标签条件

	protected boolean subtagSucceeded = false; //  子标签条件
	

	private static final long serialVersionUID = -1259437149706706783L;
	
	private String code = "";// 权限的编码
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean isSubtagSucceeded() {
		return subtagSucceeded;
	}

	public void setSubtagSucceeded(boolean subtagSucceeded) {
		this.subtagSucceeded = subtagSucceeded;
	}

	public boolean isFathertagSucceeded() {
		return fathertagSucceeded;
	}

	public void setFathertagSucceeded(boolean fathertagSucceeded) {
		this.fathertagSucceeded = fathertagSucceeded;
	}

	protected boolean condition() throws JspException
    {	
        if(Tools.isEmpty(code)){
            return true;
        }
        boolean flag = false; //访问权限校验
        try {
            Session session = Jurisdiction.getSession();
            User user = (User)session.getAttribute(Const.SESSION_USER);
            Integer roleMax = (Integer)session.getAttribute(user.getUSERNAME()+Const.SESSION_USER_MAX_ROLE);                //读取session中的用户信息(含角色信息)
            if(null == roleMax){roleMax = 2;}
            
            if(roleMax==0){
                flag = true ;//超级管理员角色则有全部权限
            }else{
                flag = Jurisdiction.hasJurisdiction(user.getUSERNAME(), code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
	
	public RightsTag() {
        super();
        init();
    }

    @Override
    public void release() {
        super.release();
        init();
    }
    
    @Override
    public int doStartTag() throws JspException {
    	init();
    	fathertagSucceeded = condition();
//    	Object s = getBodyContent();
//    	this.pageContext;
//        String body = getBody();
//        if(body!=null && body.contains("ts:norights")){
//        	this.includeSub = true;
//            if(subtagSucceeded){
//                return TagSupport.EVAL_BODY_INCLUDE;
//            }else{
//                return TagSupport.SKIP_BODY;
//            }
//        }
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
    	if(fathertagSucceeded){
    		try {
    			pageContext.getOut().write(bodyContent.getString().trim());
    		} catch (IOException e) {
    			throw new JspException("IOError while writing the body: " + e.getMessage(), e);
    		}
    	}else{
    		if(body!=null){
        		try {
        			pageContext.getOut().write(body);
        		} catch (IOException e) {
        			throw new JspException("IOError while writing the body: " + e.getMessage(), e);
        		}
    		}
    	}

    	return super.doEndTag();
    }
    private String body = null;        // 子标签控制显示的内容
    
    public void setBody(String body){
    	this.body = body;
    }
    
    private void init() {
        subtagSucceeded = false;
        fathertagSucceeded = false;
        body = null;
    }
    
}