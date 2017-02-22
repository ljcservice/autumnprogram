package com.hitzd.his.Web.Taglibs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.hitzd.DBUtils.CommonMapper;
import com.hitzd.DBUtils.JDBCQueryImpl;
import com.hitzd.DBUtils.TCommonRecord;
import com.hitzd.Factory.DBQueryFactory;
import com.hitzd.his.Utils.DictCache;
import com.hitzd.his.casehistory.helper.CaseHistoryFactory;
import com.hitzd.his.casehistory.helper.ICaseHistoryHelper;

/**
 * taglib 身份查询标签 
 * @author jingcong
 *
 */
public class TagIdentity extends TagSupport
{
    private static final long serialVersionUID = 1L;
    
    private String datafield;
    private String QueryFlag;
    private String IdenName ;
    private String OrderCode; 
    
    public String getDatafield()
    {
        return datafield;
    }

    public void setDatafield(String datafield)
    {
        this.datafield = datafield;
    }

    public String getQueryFlag()
    {
        return QueryFlag;
    }

    public void setQueryFlag(String queryFlag)
    {
        QueryFlag = queryFlag;
    }

    public String getIdenName()
    {
        return IdenName;
    }

    public void setIdenName(String idenName)
    {
        IdenName = idenName;
    }

    public String getOrderCode()
    {
        return OrderCode;
    }

    public void setOrderCode(String orderCode)
    {
        this.OrderCode = orderCode;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    @Override
    public int doEndTag() throws JspException
    {
        HttpServletRequest req = (HttpServletRequest)super.pageContext.getRequest();
        JspWriter jwer = super.pageContext.getOut();
        StringBuffer sbfr = new StringBuffer();
        try
        {
            List<TCommonRecord> identitys = null;
            if("1".equals(this.QueryFlag))
            {
                identitys = QueryDBRecord();
            }
            else
            {
                identitys = QueryCacheRecord();
            }
            sbfr.append("<div id=\"popover-identity-box\" class=\"popover-box\" data-field=\"field-identity\">");
            sbfr.append("<div class=\"close-container\">");
            sbfr.append("<a href=\"#\">【关闭】</a>");
            sbfr.append("</div>");
            sbfr.append("<input type=\"hidden\" class=\"JUST_FOR_FIREFOX_DONT_REMOVE\"/>");
            sbfr.append("<div class=\"popover-body\" style=\"width:250px;\">");
            sbfr.append("<label class=\"chk-all\" style=\"width:200px;\">");
            sbfr.append("<input type=\"checkbox\" class=\"chk-all\"/>");
            sbfr.append("  全部");
            sbfr.append("</label>");
            for(TCommonRecord tcr:identitys)
            {                           
                sbfr.append("<label style=\"width:100px;\">");
                sbfr.append("<input type=\"checkbox\" value='" + tcr.get("identity_name") + "'/>");
                sbfr.append(tcr.get("identity_name"));
                sbfr.append("</label>");
             } 
            sbfr.append("</div>");
            sbfr.append("</div>");
            
            jwer.append(sbfr);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            req = null;
        }
        return EVAL_PAGE;
    }
    
    private List<TCommonRecord> QueryDBRecord()
    {
        JDBCQueryImpl query = DBQueryFactory.getQuery("HIS");
        CommonMapper cmr = new CommonMapper();
        ICaseHistoryHelper chhr = CaseHistoryFactory.getCaseHistoryHelper();
        List<TCommonRecord> list = null;
        try
        {
            String strFields = "*";
            List<TCommonRecord> lsWheres = new ArrayList<TCommonRecord>();
            List<TCommonRecord> lsGroup  = new ArrayList<TCommonRecord>();
            List<TCommonRecord> lsOrder  = new ArrayList<TCommonRecord>();
            list = chhr.fetchIdentityDict2CR(strFields, lsWheres, lsGroup, lsOrder, query);
            
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            query = null;
        }
        
        return list;
    }
    
    private List<TCommonRecord> QueryCacheRecord()
    {
        return DictCache.getNewInstance().getIdentityAll();
    }
}
