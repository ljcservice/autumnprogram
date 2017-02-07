package com.ts.util.Taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * 自定义标签，权限过滤
 * @author silong.xing
 * <ts:rights code="user/listUsers">有权限时候输出内容<ts:norights>无权限时候输出内容</ts:norights></ts:rights>
 */
public class NoRightsTag extends BodyTagSupport {
	
	private static final long serialVersionUID = -5254746698673612689L;

	 public void release() {
	        super.release();
	    }
	    
	    public int doEndTag() throws JspException {
	    	Tag tag = (RightsTag) getParent();
	    	if(tag == null || !(tag instanceof RightsTag)){
	    		throw new JspTagException("parent tag must inside if tag");
	    	}
	    	RightsTag parent = (RightsTag) tag;
	        if(parent.isFathertagSucceeded()){
	        	//父条件执行成功
	        	this.bodyContent.clearBody();
	        }else{
	        	parent.setBody(this.getBodyContent().getString().trim());
	        	parent.setSubtagSucceeded(true);
	        }
	        return EVAL_BODY_BUFFERED;
	    }
}