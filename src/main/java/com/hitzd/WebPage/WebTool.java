package com.hitzd.WebPage;

/**
 * 
 * @author liujc
 *
 */
public class WebTool {
    
    /**
     * web页面方法
     * @param viewpagecount 显示页面个数
     * @param currenPage  当前页码
     * @param totalpage   页数
     * @return
     */
  public static PageIndex getPageIndex(long viewpagecount, int currenPage, long totalpage){
        long startpage = currenPage-(viewpagecount%2==0? viewpagecount/2-1 : viewpagecount/2);
        long endpage = currenPage+viewpagecount/2;
        if(startpage<1){
            startpage = 1;
            if(totalpage>=viewpagecount) endpage = viewpagecount;
            else endpage = totalpage;
        }
        if(endpage>totalpage){
            endpage = totalpage;
            if((endpage-viewpagecount)>0) startpage = endpage-viewpagecount+1;
            else startpage = 1;
        }
        return new PageIndex(startpage, endpage);
    }
}
