package com.siweidg.comm.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.siweidg.sys.auth.service.UserService;

public class UserTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String id;

	@Override
	public int doStartTag() throws JspException {
		if (StringUtils.isNotBlank(id)) {
			WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
			UserService userService = webApplicationContext.getBean(UserService.class);
			JspWriter out = this.pageContext.getOut();
			try {
				out.write(userService.getTextById(Long.parseLong(id)));
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
