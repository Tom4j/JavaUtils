package com.siweidg.comm.init;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.siweidg.comm.json.MDictionary;
import com.siweidg.comm.utils.BeanUtils;
import com.siweidg.sys.auth.entry.Dictionary;
import com.siweidg.sys.auth.entry.Dictionarytype;
import com.siweidg.sys.auth.service.DictionaryService;
import com.siweidg.sys.auth.service.DictionarytypeService;


public class InitServletContextListener implements ServletContextListener {


	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("---------------数据字典加载中！-----------------");
		ServletContext application = sce.getServletContext();

		// 得到Service的实例对象
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(application);
		DictionaryService dictionaryService = (DictionaryService) ac.getBean("dictionaryService");
		DictionarytypeService dictionarytypeService = (DictionarytypeService) ac.getBean("dictionarytypeService");
		
		List<Dictionary> list = dictionaryService.findAll();
		if(list!=null){
			Constants.dictionarys.clear();
			for(Dictionary d:list){
				Constants.dictionarys.put(d.getId(), d.getText());
			}
		}
		
		List<Dictionarytype> list2 = dictionarytypeService.findAll();
		if(list2!=null){
			Constants.dictionaryTypes.clear();
			for(Dictionarytype d:list2){
				List<Dictionary> list3 = dictionaryService.findByType(d.getId());
				List<MDictionary> mList = new ArrayList<MDictionary>();
				for(Dictionary dictionary :list3){
					MDictionary md = new MDictionary();
					BeanUtils.copyProperties(dictionary, md);
					mList.add(md);
				}
				Constants.dictionaryTypes.put(d.getId(), mList);
			}
		}
		
		System.out.println("---------------数据字典加载完毕！-----------------");
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
