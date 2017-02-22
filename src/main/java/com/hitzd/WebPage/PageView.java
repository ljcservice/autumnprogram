package com.hitzd.WebPage;

import java.util.List;

/**
 * 
 * @author liujc
 *
 * @param <T>
 */
public class PageView<T> {
    
    /** 分页数据 **/
    private List<T> records;
    /** 页码开始索引和结束索引 **/
    private PageIndex pageindex;
    /** 总页数 **/
    private long totalpage = 1;
    /** 每页显示记录数 **/
    private int maxresult = 12;
    /** 当前页 **/
    private int currentpage = 1;
    /** 总记录数 **/
    private long totalrecord;
    /** 页码数量 **/
    private int pagecode = 10;
    
    public PageView() {
    }

    /**
     * @param maxresult
     * 每页显示记录数
     * @param currentpage
     * 当前页
     */
    public PageView(int maxresult, int currentpage) {
        this.maxresult = maxresult;
        this.currentpage = currentpage;
    }
    /**
     * @param qr
     * 返回自定义的结果集
     */
    public void setQueryResult(QueryResult<T> qr){
        setTotalrecord(qr.getTotalrecord());
        setRecords(qr.getResultlist());
    }
    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public PageIndex getPageindex() {
        return pageindex;
    }

    protected void setPageindex(PageIndex pageindex) {
        this.pageindex = pageindex;
    }

    public long getTotalpage() {
        return totalpage;
    }

    protected void setTotalpage(long totalpage) {
        this.totalpage = totalpage;
        this.setPageindex(WebTool.getPageIndex(pagecode, currentpage,this.totalpage));
    }

    public int getMaxresult() {
        return maxresult;
    }

    protected void setMaxresult(int maxresult) {
        this.maxresult = maxresult;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public long getTotalrecord() {
        return totalrecord;
    }

    protected void setTotalrecord(long totalrecord) {
        this.totalrecord = totalrecord;
        setTotalpage(this.totalrecord%this.maxresult==0? this.totalrecord/this.maxresult : this.totalrecord/this.maxresult+1);
    }
    
    /** 页码数量 **/
    public int getPagecode() {
        return pagecode;
    }
    /** 页码数量 **/
    public void setPagecode(int pagecode) {
        this.pagecode = pagecode;
    }
    
}
