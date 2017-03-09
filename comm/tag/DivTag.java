package com.siweidg.comm.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.siweidg.sys.auth.service.DictionaryService;
import com.siweidg.sys.auth.service.DictionarytypeService;

public class DivTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String style;
	private String name;
	private String type;
	private String class1;
	private String class2;
	
	@Override
	public int doStartTag() throws JspException {
		
		 
		JspWriter out = this.pageContext.getOut();
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();  
		DictionaryService dictionaryService =  webApplicationContext.getBean(DictionaryService.class);
		DictionarytypeService dictionarytypeService =  webApplicationContext.getBean(DictionarytypeService.class);
		if(StringUtils.isBlank(type)){
			try {
				if(StringUtils.isNotBlank(id)) {
					 String a = "";
					  if(id.contains(",")) {
					  String[] ids = id.split(",");
					  for(String str :ids) {
						  a +=dictionaryService.getTextById(Long.parseLong(str.trim()))+"/";
						  }
					  } else {
						  a = dictionaryService.getTextById(Long.parseLong(id.trim()));
					  }
					  out.write("<div class='width100 left' style='"+style+"'><div class='"+class1+"'style='width:80px;text-align:left'>"+name+"：</div><p class='"+class2+"' style='width:80%'>"+a+"</p></div>");
					  out.flush();
				}else{
					out.write("");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else if("1".equals(type)){
			try {
				if(StringUtils.isNotBlank(id)) {
					String a = "";
					a = dictionarytypeService.getNameById(Long.parseLong(id.trim()));
					out.write("<div class='width100 left' style='"+style+"'><div class='"+class1+"'style='width:80px;text-align:left'>"+name+"：</div><p class='"+class2+"' style='width:80%'>"+a+"</p></div>");
					out.flush();
				}else{
					out.write("");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getClass1() {
		return class1;
	}

	public void setClass1(String class1) {
		this.class1 = class1;
	}

	public String getClass2() {
		return class2;
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}

	
	
 
}
