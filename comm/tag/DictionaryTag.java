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

public class DictionaryTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String type;
	
	@Override
	public int doStartTag() throws JspException {
		
		 
		if(StringUtils.isNotBlank(id)) {
			 WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();  
			 DictionaryService dictionaryService =  webApplicationContext.getBean(DictionaryService.class);
			 DictionarytypeService dictionarytypeService =  webApplicationContext.getBean(DictionarytypeService.class);
			 
			 JspWriter out = this.pageContext.getOut();
			 try {
				 if(StringUtils.isBlank(type)){
					  if(id.contains(",")) {
						  String[] ids = id.split(",");
						  String rst = "";
						  for(String str :ids) {
							  rst +=dictionaryService.getTextById(Long.parseLong(str.trim()))+"/";
						  }
						  out.write(rst.substring(0, rst.length()-1));
					  } else {
						  out.write(dictionaryService.getTextById(Long.parseLong(id.trim())));
					  }
				 }else if("1".equals(type)){
					 if(id.contains(",")) {
						  String[] ids = id.split(",");
						  String rst = "";
						  for(String str :ids) {
							  rst +=dictionarytypeService.getNameById(Long.parseLong(str.trim()))+"/";
						  }
						  out.write(rst.substring(0, rst.length()-1));
					  } else {
						  out.write(dictionarytypeService.getNameById(Long.parseLong(id.trim())));
					  }
					 //out.write(dictionarytypeService.getNameById(Long.parseLong(id.trim())));
				 }
				 out.flush();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
 
}
