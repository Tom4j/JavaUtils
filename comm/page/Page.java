package com.siweidg.comm.page;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	protected List<T> rows = new ArrayList<T>();
	protected long total = -1;
	protected int pageCount = -1;
	
	
	public Page() {
	}

	public Page(List<T> rows, long total, int pageCount) {
		this.rows = rows;
		this.total = total;
		this.pageCount = pageCount;
	}
	
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}
