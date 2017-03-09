/**
 * 
 */
package com.siweidg.comm.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.org.apache.regexp.internal.recompile;

/**
 * @author wangwei
 * 
 * create At 2013-9-26
 * 
 */
public class DateUtil {
//	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	static final String formatPattern_File = "yyyyMMddHHmmss";
//	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	/**
	 * 获取当前时间的Timestamp对象
	 * @return
	 */
	public static Timestamp getTimestamp() {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		
		return timestamp;
	}
	
	public static Timestamp strTimeToTimestamp(String strDate) throws Exception {
		Timestamp timestamp = null;
		Date date = sdf.parse(strDate);
		timestamp = new Timestamp(date.getTime());
		
		return timestamp;
	}
	
	public static String timestampToString(Timestamp ts){
		
		return sdf.format(ts);   
	}
	
	/**
	 * 获取当前时间的Date对象
	 * @return
	 */
	public static Date getDate(){
		Date date = new Date(new Date().getTime());
		return date;
	}
	
	public static Date strTimeToDate(String strDate) throws Exception {
		Date date = sdf.parse(strDate);
		return date;
	}
	
	public static String dateToString(Date date){
		return sdf.format(date);   
	}
	
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern_File);        
		return format.format(new Date());
	}
}
