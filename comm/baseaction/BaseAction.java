package com.siweidg.comm.baseaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.siweidg.comm.page.PageRequest;


public class BaseAction extends ActionSupport  implements ServletRequestAware,ServletResponseAware {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	protected int page = 1;
	
	protected int rows = 8;
	
	protected String sort ="id";
	
	protected String order ="desc";
	
	protected HttpServletResponse response;
	
	protected HttpServletRequest request;
	
	protected Map<String, Object> params = new LinkedHashMap<String, Object>();
	
	protected PageRequest pageRequest = new PageRequest(page, rows,sort,order);
	
	protected static Gson gson = new GsonBuilder().setPrettyPrinting()
			.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}
	
	public void setPage(int page) {
		this.page = page;
		pageRequest.setPageNo(page);
	}

	public void setRows(int rows) {
		this.rows = rows;
		pageRequest.setPageSize(rows);
	}

	public void setSort(String sort) {
		this.sort = sort;
		if(StringUtils.isNotBlank(sort)) {
			pageRequest.setOrderBy(sort);
		}
	}

	public void setOrder(String order) {
		this.order = order;
		if(StringUtils.isNotBlank(order)) {
			pageRequest.setOrderDir(order);
		}
	}
	
	public void outPrint(HttpServletResponse response , String message) throws IOException {
//		this.response.setHeader("Access-Control-Allow-Origin", "*");
		//解决中文乱码
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(message);
	}

}
