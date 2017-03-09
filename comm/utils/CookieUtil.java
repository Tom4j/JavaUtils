package com.siweidg.comm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.siweidg.comm.init.Constants;

/**
 * 
 * @author 王伟
 *
 * createAt 2013-8-9
 */
public class CookieUtil {

	private static final Logger logger = Logger
			.getLogger(CookieUtil.class);

	/**
	 * 该方法能读取当前路径及"直接父路径"的所有Cookie对象，如果没有所有Cookie的话，则返回null 
	 * @param request
	 * @param cookieName  cookie的key
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String cookieName) {
		if (null == cookieName || "".equals(cookieName))
			return null;
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					try {
//						logger.info("获取cookie：" + cookieName + " 值为："
//								+ cookie.getValue());
						return java.net.URLDecoder.decode(cookie.getValue(),
								"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	public static void addCookie(HttpServletResponse response,String cookieName,String cookieValue) throws UnsupportedEncodingException{
		addCookie(response, cookieName, cookieValue, -1);
	}

	public static void addCookie(
			HttpServletResponse response,
			String cookieName,
			String cookieValue,
			int maxAge) throws UnsupportedEncodingException {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		String domain = request.getRemoteHost();		
		addCookie(response, cookieName, cookieValue, "/", Constants.DOMAIN, maxAge);
	}

	public static void addCookie(
			HttpServletResponse response,
			String cookieName,
			String cookieValue,
			String path,
			String domain,
			int maxAge) throws UnsupportedEncodingException {

		logger.info("保存cookie：" + cookieName + " 值为："
				+ cookieValue + " path: " + path + " domain: " + domain
				+ " maxAge: " + maxAge);

		Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, "utf-8"));
		cookie.setMaxAge(maxAge);

		if (StringUtils.isNotBlank(path) && StringUtils.isNotEmpty(path)) {
			cookie.setPath(path);
		}
		if (StringUtils.isNotBlank(domain) && StringUtils.isNotEmpty(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	public static void removeCookies(
			HttpServletResponse response,
			String cookieName) throws UnsupportedEncodingException {
		String domain = "";
		removeCookies(response, domain, cookieName);
	}

	public static void removeCookies(
			HttpServletResponse response,
			String domain,
			String cookieName) throws UnsupportedEncodingException {
		String path = "/";
		short validTime = 0;
		addCookie(response, cookieName, null, path, domain, validTime);
	}

}
