package com.siweidg.comm.codeGen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 代码生成器
 * @author sunqz
 * @date 2016/9/12
 * @version 1.0
 */
public class CodeUtils {
	
	public static Configuration cfg; 
	
	public static   Connection connection;
	
	public static  String className ="org.postgresql.Driver";
	
	public static  String url ="jdbc:postgresql://127.0.0.1:5432/jiaozhou_water";
	
	public static  String username = "postgres";
	
	public static  String password = "123456";
	
	public static  String templatePath ="/config/ftl";
	
	public static  String rootPath = "/src/";  //项目根目录  如果为maven项目则为 /src/main/java/
	
	public static  String daoImplPath = "com.siweidg.monitor.dao.impl";
	
	public static  String daoPath = "com.siweidg.monitor.dao";
	
	public static  String servicePath = "com.siweidg.monitor.service";
	
	public static  String serviceImplPath = "com.siweidg.monitor.service.impl";
	
	public static  String actionPath = "com.siweidg.monitor.action";
	
	public static  String entryPath = "com.siweidg.monitor.entry"; 
	
	public static  String hbmPath = "mapping";
	
	public static  Boolean cover = false;  //默认不让覆盖  防止丢失代码
	
	public static  String tableName = "device,devicehistory"; //为""则生成库中所有表 多个表时中间以逗号隔开
	
	public static  List<Entry> entrys = new ArrayList<Entry>();
	
	public static  String  hversion = "hversion";  //默认hibernate version 字段名称
	
	
	public  static void main(String[] args) throws Exception {
		init();
		 printDataBasesType();
		run();    
	}
	
	
	/**
	 * 初始化参数
	 * @throws Exception
	 */
	public  static void   init() throws Exception {
		  Class.forName(className);
		  connection = DriverManager.getConnection(url
                  , username,
                  password);
		  cfg = new Configuration(); 
		  cfg.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir")+templatePath));
		  
		   entrys.add( new Entry("action.ftl",rootPath,"Action.java",actionPath));
		    
		    entrys.add( new Entry("service.ftl",rootPath,"Service.java",servicePath));
		    
		    entrys.add( new Entry("dao.ftl",rootPath,"Dao.java",daoPath));
		    
		    entrys.add( new Entry("daoImpl.ftl",rootPath,"DaoImpl.java",daoImplPath));
		    
		    entrys.add( new Entry("serviceImpl.ftl",rootPath,"ServiceImpl.java",serviceImplPath));
		    //生成hbm文件需要多一个参数entryPath指定pojo类所在包路径
		    entrys.add( new Entry("hbm.xml.ftl","/config/",".hbm.xml",hbmPath));
		    
		    entrys.add( new Entry("entry.ftl",rootPath,".java",entryPath));
	}
	
	 
	/**
	 * 打印字段数据类型
	 * @throws Exception
	 */
	public  static void   printDataBasesType() throws Exception {
		DatabaseMetaData   dbMetaData   =  connection.getMetaData();  
		String[]   types   =   {"TABLE"};    
		ResultSet   tabs   =  null;  
		if(!"".equals(tableName)) {
			String[] tables = tableName.split(",");
			for (String table : tables) {				
				tabs = dbMetaData.getTables(null,"public",table.trim(),types);
				while (tabs.next()) {
					String tName = tabs.getObject("TABLE_NAME").toString();
					System.out.println("TABLE_NAME = "+tName);
					ResultSet rs = dbMetaData.getColumns(null, null, tName, "%");
					
					while (rs.next()) {
						String columnName = rs.getString("COLUMN_NAME");
						System.out.println(columnName + "  "
								+ rs.getString("TYPE_NAME"));
					}
				}
			}
		} else {
			tabs = dbMetaData.getTables(null,"public",null,types);
			while (tabs.next()) {
				String tName = tabs.getObject("TABLE_NAME").toString();
				ResultSet rs = dbMetaData.getColumns(null, null, tName, "%");
				
				while (rs.next()) {
					String columnName = rs.getString("COLUMN_NAME");
					System.out.println(columnName + "  "
							+ rs.getString("TYPE_NAME"));
				}
			}
		}
	}
	
	
	/**
	 * 执行生成代码
	 * @throws SQLException
	 * @throws Exception
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void run() throws SQLException, Exception, IOException,
			TemplateException {
		DatabaseMetaData   dbMetaData   =  connection.getMetaData();  
		String[]   types   =   {"TABLE","VIEW"};    
		ResultSet   tabs   =  null;
		if(!"".equals(tableName)) {
			String[] tables = tableName.split(",");
			for (String table : tables) {				
				tabs = dbMetaData.getTables(null,"public",table.trim(),types);
				while(tabs.next()){    
					for(Entry entry : entrys) {
						String entryName = buildEntry(dbMetaData, tabs, entry);
						mkCodeFile(entry,entryName);
					}
				}
			}
		} else {
			tabs = dbMetaData.getTables(null,"public",null,types);
			while(tabs.next()){    
				for(Entry entry : entrys) {
					String entryName = buildEntry(dbMetaData, tabs, entry);
					mkCodeFile(entry,entryName);
				}
			}
		}
		 System.out.println("完成");
	}
	
	/**
	 * 构建entry实体
	 * @param dbMetaData
	 * @param tabs
	 * @param entry
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static String buildEntry(DatabaseMetaData dbMetaData, ResultSet tabs,
			Entry entry) throws SQLException, Exception {
		String tName = tabs.getObject("TABLE_NAME").toString();
		entry.setTableName(tName);
		String centryName = replaceUnderlineAndfirstToUpper(tName,"_","");    
		String entryName =   firstCharacterToUpper(centryName);
		entry.setClassName(entryName);
		entry.setClassInstanceName(centryName);
		entry.setActionPath(actionPath);
		entry.setServiceImplPath(serviceImplPath);
		entry.setServicePath(servicePath);
		entry.setDaoImplPath(daoImplPath);
		entry.setDaoPath(daoPath);
		entry.setEntryPath(entryPath);
		ResultSet set = dbMetaData.getPrimaryKeys(null, null, tName);
		entry.propList.clear();
		while(set.next()){ 
			entry.idColumn = set.getString("COLUMN_NAME");
			//System.out.println("id="+entry.idColumn);
		}
		set.close();
		ResultSet rs = dbMetaData.getColumns(null, null, tName, "%");

		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			if(StringUtils.isBlank(entry.getIdColumn())) {
				if(tName.endsWith("view")) {
					entry.idColumn = columnName;
				}else {
				
					throw new Exception("没有主键！");
				}
			}
			
			buildProperty(entry, rs, columnName);
		}
		
		rs.close();
		return entryName;
	}
	
	/**
	 * freemarker生成模板
	 * @param entry
	 * @param entryName
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void  mkCodeFile(Entry entry,  String entryName)
			throws IOException, TemplateException {
		Map data = new HashMap<String,Object>();
		data.put("entry",entry);
		String dir = System.getProperty("user.dir");
		Template t = cfg.getTemplate(entry.getTemplateName());
		//System.out.println(dir+entry.getRootPath()+entry.getPackagePath()+"/"+entryName+entry.getClassSuffix());
		File file = new File(dir+entry.getRootPath()+entry.getPackagePath().replace(".", "/")+"/"+entryName+entry.getClassSuffix());
		if (file.exists()) {
			if (!cover ) {
				return;
			}
		}
		
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		t.process(data, fw);
	}

	/**
	 * 构建property
	 * @param entry
	 * @param rs
	 * @param columnName
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void  buildProperty(Entry entry, ResultSet rs, String columnName)
			throws SQLException, Exception {
		if (hversion.equals(columnName)) {
			entry.hasHibernateVersion = true;
			return;
		}
		//System.out.println(columnName);
		if (columnName.equals(entry.idColumn)) {
			
				entry.idName = replaceUnderlineAndfirstToUpper(entry.idColumn,"_","");
				entry.idType = typeMap.get(rs.getString("TYPE_NAME"));
				entry.idGetMethod = buildGetMethod(entry.idName);
				entry.idSetMethod = buildSetMethod(entry.idName);
				entry.idSimpleType = simpleTypeMap.get(rs.getString("TYPE_NAME"));

		} else {
				Entry.Property prop = new Entry.Property();
				prop.column = columnName;
				prop.propName = replaceUnderlineAndfirstToUpper(prop.column,"_","");
				prop.propType = typeMap.get(rs.getString("TYPE_NAME"));
				prop.simpleType = simpleTypeMap.get(rs.getString("TYPE_NAME"));
				if (prop.propType == null) {
					throw new Exception("Unknow Database DataType: " + rs.getString("TYPE_NAME"));
				}
				if ("BigDecimal".equals(prop.simpleType)) {
					entry.hasDecimalType = true;
				} else if ("Date".equals(prop.simpleType)) {
					entry.hasDateType = true;
				}
				prop.notAllowNull = "NO".equals(rs.getString("IS_NULLABLE")) ? (rs.getString("COLUMN_DEF") == null ? "true"
						: "false")
						: "false";
				prop.note = rs.getString("REMARKS");
				prop.getMethod = buildGetMethod(prop.propName, "Boolean".equals(prop.simpleType));
				prop.setMethod = buildSetMethod(prop.propName);	
				entry.propList.add(prop);
		}
	}
	
	/**
	 * 首字母小写
	 */
	public static String fistCharacherToLower(String srcStr) {
		return srcStr.substring(0, 1).toLowerCase() + srcStr.substring(1); 
	}
	
	/**
	 * get方法
	 * @param field
	 * @param isBoolean
	 * @return
	 */
	public static String buildGetMethod(String field, boolean isBoolean) {
		return (isBoolean ? "is" : "get") + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	/**
	 * get方法
	 * @param field
	 * @param isBoolean
	 * @return
	 */
	public static String buildGetMethod(String field) {
		return buildGetMethod(field, false);
	}

	/**
	 * set方法
	 * @param field
	 * @param isBoolean
	 * @return
	 */
	public static String buildSetMethod(String field) {
		return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
	
	/** 
	* 首字母大写 
	*  
	* @param srcStr 
	* @return 
	*/  
	public static String firstCharacterToUpper(String srcStr) {  
	   return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);  
	}  
	/** 
	* 替换字符串并让它的下一个字母为大写 
	* @param srcStr 
	* @param org 
	* @param ob 
	* @return 
	*/  
	public static String replaceUnderlineAndfirstToUpper(String srcStr,String org,String ob)  
	{  
	   String newString = "";  
	   int first=0;  
	   while(srcStr.indexOf(org)!=-1)  
	   {  
	    first=srcStr.indexOf(org);  
	    if(first!=srcStr.length())  
	    {  
	     newString=newString+srcStr.substring(0,first)+ob;  
	     srcStr=srcStr.substring(first+org.length(),srcStr.length());  
	     srcStr=firstCharacterToUpper(srcStr);  
	    }  
	   }  
	   newString=newString+srcStr;  
	   return newString;  
	}  
	
	//hbm文件字段类型转换
	public static final Map<String, String> typeMap = new HashMap<String, String>();
	//Java实体字段类型转换
	public static final Map<String, String> simpleTypeMap = new HashMap<String, String>();

	static {
		//hbm文件实体转换
		typeMap.put("int4", "java.lang.Integer");
		typeMap.put("int2", "java.lang.Integer");
		typeMap.put("int8", "java.lang.Long");
		typeMap.put("int1", "java.lang.Integer");
		typeMap.put("integer", "java.lang.Integer");
		typeMap.put("serial", "java.lang.Integer");
		typeMap.put("bigserial", "java.lang.Long");
		typeMap.put("timestamp", "java.util.Date");
		typeMap.put("timestamptz", "java.util.Date");
		typeMap.put("date", "java.util.Date");
		typeMap.put("datetime", "java.util.Date");
		typeMap.put("text", "java.lang.String");
		typeMap.put("varchar", "java.lang.String");
		typeMap.put("decimal", "java.math.BigDecimal");
		typeMap.put("numeric", "java.math.BigDecimal");
		typeMap.put("bit", "java.lang.Integer");
		typeMap.put("timetz", "java.util.Date");
		typeMap.put("float", "java.lang.Float");
		typeMap.put("mediumtext", "java.lang.String");
		typeMap.put("char", "java.lang.String");
		typeMap.put("bpchar", "java.lang.String");
		typeMap.put("double", "java.lang.Double");
		typeMap.put("bool", "java.lang.Boolean");
		typeMap.put("float8", "java.lang.Double");
		typeMap.put("_float8", "java.lang.Double");
		
		
		//Java实体类型转换
		simpleTypeMap.put("int4", "Integer");
		simpleTypeMap.put("int2", "Integer");
		simpleTypeMap.put("int8", "Long");
		simpleTypeMap.put("int1", "Integer");
		simpleTypeMap.put("integer", "Integer");
		simpleTypeMap.put("serial", "Integer");
		simpleTypeMap.put("bigserial", "Long");
		simpleTypeMap.put("timestamp", "Date");
		simpleTypeMap.put("timestamptz", "Date");
		simpleTypeMap.put("date", "Date");
		simpleTypeMap.put("datetime", "Date");
		simpleTypeMap.put("text", "String");
		simpleTypeMap.put("varchar", "String");
		simpleTypeMap.put("decimal", "BigDecimal");
		simpleTypeMap.put("numeric", "BigDecimal");
		simpleTypeMap.put("bit", "Integer");
		simpleTypeMap.put("timetz", "Date");
		simpleTypeMap.put("float", "Float");
		simpleTypeMap.put("mediumtext", "String");
		simpleTypeMap.put("char", "String");
		simpleTypeMap.put("bpchar", "String");
		simpleTypeMap.put("double", "Double");
		simpleTypeMap.put("bool", "Boolean");
		simpleTypeMap.put("float8", "Double");
		simpleTypeMap.put("_float8", "Double");
	}
}
