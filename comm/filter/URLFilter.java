package com.siweidg.comm.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.siweidg.comm.init.Constants;
import com.siweidg.comm.init.SessionInfo;

public class URLFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse, FilterChain filterchain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) httpservletrequest;
		HttpServletResponse res = (HttpServletResponse) httpservletresponse;
		StringBuffer url = httpservletrequest.getRequestURL();
		System.out.println("URL ===== " + url);
		SessionInfo info = (SessionInfo) req.getSession().getAttribute(Constants.SESSION_INFO);
//		System.out.println(info);
//		if(req.getSession().getAttribute(Constants.SESSION_INFO) != null ) {
//			filterchain.doFilter(httpservletrequest, httpservletresponse);
//			//addLog(req);
//		} else {
////			request.setAttribute("msg", "登陆超时，请重新登陆！");
////			req.getRequestDispatcher("/index.jsp").forward(request, response);
//			res.setContentType("text/html;charset=UTF-8");// 解决中文乱码 
//			res.setHeader("Pragma","No-cache");          
//			res.setHeader("Cache-Control","no-cache");   
//			res.setHeader("Cache-Control", "no-store");   
//			res.setDateHeader("Expires",0);
//			
//			res.sendRedirect(req.getContextPath() + "/login.html");
//		}
		filterchain.doFilter(httpservletrequest, httpservletresponse);
	}

}
