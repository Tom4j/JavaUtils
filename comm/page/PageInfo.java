package com.siweidg.comm.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.siweidg.comm.utils.ListUtils;

public class PageInfo<T> extends PageRequest implements Iterable<T> {
	
	public  static final int MAX_ROW = 5;

	protected List<T> rows = null;
	
	protected List<Map<String, Object>> footer;
	
	private String pageHtml = "";
	
	/**
	 * 总记录数
	 */
	protected long total = -1;
	
	/**
	 * 总页数
	 */
	protected Integer pageCount = -1;
	
	public PageInfo() {
	}

	public PageInfo(PageRequest request) {
		this.pageNo = request.pageNo;
		this.pageSize = request.pageSize;
		this.countTotal = request.countTotal;
		this.orderBy = request.orderBy;
		this.orderDir = request.orderDir;
	}
	
	/**
	 * 得到总页数
	 * @return
	 */
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * 设置页总数
	 * @param pageCount
	 */
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	
	/**
	 * 获得页内的记录列表.
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 设置页内的记录列表.
	 */
	public void setRows (final List<T> rows) {
		this.rows = rows;
	}

	/**
	 * 获得总记录数, 默认值为-1.
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 设置总记录数.
	 */
	public void setTotal(final long total) {
		this.total = total;
		this.pageCount = getTotalPages();
		pageHtml=getPageHtml();
	}

	/** 
	 * 实现Iterable接口, 可以for(Object item : page)遍历使用
	 */
	@Override
	public Iterator<T> iterator() {
		return rows.iterator();
	}

	/**
	 * 根据pageSize与totalItems计算总页数.
	 */
	
	public int getTotalPages() {
		return (int) Math.ceil((double) total / (double) getPageSize());

	}

	/**
	 * 是否还有下一页.
	 */
	
	public boolean hasNextPage() {
		return (getPageNo() + 1 <= getTotalPages());
	}

	/**
	 * 是否最后一页.
	 */
	
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */

	public int getNextPage() {
		if (hasNextPage()) {
			return getPageNo() + 1;
		} else {
			return getPageNo();
		}
	}

	/**
	 * 是否还有上一页.
	 */
	
	public boolean hasPrePage() {
		return (getPageNo() > 1);
	}

	/**
	 * 是否第一页.
	 */

	public boolean isFirstPage() {
		return !hasPrePage();
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */

	public int getPrePage() {
		if (hasPrePage()) {
			return getPageNo() - 1;
		} else {
			return getPageNo();
		}
	}

	/**
	 * 计算以当前页为中心的页面列表,如"首页,23,24,25,26,27,末页"
	 * @param count 需要计算的列表大小
	 * @return pageNo列表 
	 */

	public List<Integer> getSlider(int count) {
		int halfSize = count / 2;
		int totalPage = getTotalPages();

		int startPageNo = Math.max(getPageNo() - halfSize, 1);
		int endPageNo = Math.min(startPageNo + count - 1, totalPage);

		if (endPageNo - startPageNo < count) {
			startPageNo = Math.max(endPageNo - count, 1);
		}

		List<Integer> result = ListUtils.newArrayList();
		for (int i = startPageNo; i <= endPageNo; i++) {
			result.add(i);
		}
		return result;
	}
	
	/**
	 * 设置当页的起始点
	 * @param start
	 */
	public void setStartAndLimit(int start , int limit) {
		this.setPageSize(limit);
		int pageNo = start/getPageSize() + 1 ;
		this.setPageNo(pageNo);
	}
	
	public List<Map<String, Object>> getFooter() {
		return footer;
	}

	public void setFooter(List<Map<String, Object>> footer) {
		this.footer = footer;
	}

	public void addFooter(Map<String, Object> sumMap) {
		if (sumMap != null && sumMap.size() > 0) {
			if (footer == null) {
				footer = new ArrayList<Map<String, Object>>();
			}
			footer.add(sumMap);
		}
	}

	@Override
	public String toString() {
		return "Page [rows=" + rows + ", pageCount=" + pageCount + ", total=" + total + "]";
	}
	
	 public  String getPageHtml() {  
	        StringBuffer sb = new StringBuffer("<span style='font-size: 14px;color: #666;'>"+total+"条  共"+pageCount+"页&nbsp;</span>");  
	        
	        if(pageNo>1&&pageCount>1) {
	        	sb.append("<a href='#' onclick=\'loadDataGrid(\"id\",\"desc\",1,"+pageSize+");\'>首页 </a>");
	        	sb.append("<a href='#' onclick=\'loadDataGrid(\"id\",\"desc\","+(pageNo-1)+","+pageSize+");\'>上一页 </a>");
	        }
	        
	        if(pageCount<=MAX_ROW) {
	        	for(int i=1;i<=pageCount;i++) {
	        		if(i==pageNo){
	        			sb.append("<span style=\"-moz-border-radius: 3px;-webkit-border-radius: 3px;height: 24px;font-size:14px;background:#2a8cef;color: #fff;line-height: 24px;margin: 0 2px;padding: 4px 8px;\">"+i+"</span>");
	        		} else {
	        			sb.append("<a href='#' onclick=\'loadDataGrid(\"id\",\"desc\","+i+","+pageSize+");\'>"+i+"</a>");
	        		}
	        	}
	        } else {
	        	int startno = pageNo-(MAX_ROW/2);
	        	
	        	if(startno+MAX_ROW-1>pageCount) {
	        		startno= pageCount+1 -MAX_ROW;
	        	}
	        	
	        	if(startno<=0) {
	        		startno=1;
	        	}
	        	for(int i=startno;i<=startno+MAX_ROW-1&&i<=pageCount;i++) {
	        		if(i==pageNo){
	        			sb.append("<span style=\"-moz-border-radius: 3px;-webkit-border-radius: 3px;height: 24px;font-size:14px;background:#2a8cef;color: #fff;line-height: 24px;margin: 0 2px;padding: 4px 8px;\">"+i+"</span>");
	        		} else {
	        			sb.append("<a href='#' onclick=\'loadDataGrid(\"id\",\"desc\","+i+","+pageSize+");\'>"+i+"</a>");
	        		}
    		}
	        }
	      
	        if(pageNo!=pageCount) {     
	        	sb.append("<a href='#' onclick=\'loadDataGrid(\"id\",\"desc\","+(pageNo+1)+","+pageSize+");\'>下一页</a>");
	        	sb.append("<a href='#' onclick=\'loadDataGrid(\"id\",\"desc\","+(pageCount)+","+pageSize+");\'>尾页</a>");
	        }
	        return   sb.toString();	
	    }  
	
}
