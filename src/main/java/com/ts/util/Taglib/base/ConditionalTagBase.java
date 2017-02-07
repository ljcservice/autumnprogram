package com.ts.util.Taglib.base;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 真假判断标基类
 * @author autumn
 *
 */
public abstract class ConditionalTagBase extends TagSupport
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public int doStartTag() throws JspException {
        
        if(condition()){
            return TagSupport.EVAL_BODY_INCLUDE;
        }else{
            return TagSupport.SKIP_BODY;
        }
    }
    
	@Override
    public int doEndTag() throws JspException {
    
        return TagSupport.EVAL_PAGE;
    }
    
    @Override
    public void release() {
        super.release();
    }
    /**
     * 
     * @return 
     * @throws JspException
     */
     protected abstract boolean condition()  throws JspException;
}
