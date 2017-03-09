package com.siweidg.comm.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import util.MapUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.siweidg.comm.page.Page;
import com.siweidg.comm.page.PageInfo;




public final class JsonfUtil {
	
	public static void toJSON(HttpServletResponse response,
			Map<String, Object> map) throws IOException {
		//这句话的意思，是让浏览器用utf8来解析返回的数据  
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(toJSONString(map));
	}

	public static void toJSON(HttpServletResponse response, Object obj) throws IOException {
		//这句话的意思，是让浏览器用utf8来解析返回的数据  
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(toJSONString(obj, SerializerFeature.WriteDateUseDateFormat));
	}
	

	public static void toJSON(HttpServletResponse response, String name,
			Object value) throws IOException {
		JSONObject object = new JSONObject();
		object.put(name, value);
		String jsonString = JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat);
		try {
			//这句话的意思，是让浏览器用utf8来解析返回的数据  
			response.setHeader("Content-type", "text/html;charset=UTF-8");  
			//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String toJSONString(Object object,
			SerializerFeature... features) {
		SerializeWriter out = new SerializeWriter();
		String s;
		JSONSerializer serializer = new JSONSerializer(out);
		SerializerFeature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++) {
			SerializerFeature feature = arr$[i$];
			serializer.config(feature, true);
		}
		serializer.getValueFilters().add(new ValueFilter() {
			public Object process(Object obj, String s, Object value) {
				if (null != value) {
					/*if (value instanceof java.util.Date) {
						return String.format("%1$tF %1tT", value);
					}*/
					if (value instanceof com.vividsolutions.jts.geom.Polygon) {
//						return value.toString();
						return value.toString().replaceAll("POLYGON ", "POLYGON").replaceAll(", ", ",");
					}
					if (value instanceof com.vividsolutions.jts.geom.Point) {
//						return value.toString();
						return value.toString().replaceAll("POINT ", "POINT").replaceAll(", ", ",");
					}
					return value;
				} else {
					return "";
				}
			}
		});
		serializer.write(object);
		s = out.toString();
		out.close();
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public  static <T> void toPageJson(HttpServletResponse response ,PageInfo<T> pageInfo) throws Exception {
		
		List<T> list = pageInfo.getRows();
		Page<T> page = new Page<T>();
		if(list!=null) {
			List<T> list2 = DeptLimitUtils.convertCollection(list, 0); //根据深度转换集合   默认的2层(能查到关联的对象)深度
			pageInfo.setRows(list2);
		}
		BeanUtils.copyProperties(pageInfo, page);
		///String jsonString = toJSONString(page, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		JSONObject jo = new JSONObject();
		String jsonString = jo.toJSONString(page, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(jsonString);
	}
	
}