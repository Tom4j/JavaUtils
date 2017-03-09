package com.siweidg.comm.init;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.siweidg.comm.json.MDictionary;
import com.siweidg.sys.auth.entry.Dictionary;

/**
 * 配置文件初始化
 * 
 *
 * @copyRight SIWEIDG 北京航天世景信息技术有限公司
 */
public final class Constants {
	
	public static final String SESSION_INFO = "sessionInfo";
	
	public static final String INITIAL_PASSWORD = "123456";
	
	
	public static final String COMPRESSED_FILES_ZIP = ".zip";
	public static final String COMPRESSED_FILES_RAR = ".rar";
	
	
	public static final String CENTER_WS_USERNAME = "siwei";
	public static final String CENTER_WS_PWD = "siwei123456";

	private static Configuration config;
	public static Map<String, Object> cacheDB = new LinkedHashMap<String, Object>();
	static {
		try {
			config = new PropertiesConfiguration("siweidg.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 文件存储路径
	 */
	public static final String AOI_FILE_PATH = config
			.getString("aoi_file_path");
	public static final String YXSY_FILE_PATH =config
			.getString("yxsy_file_path");
	public static final String TEMP_FILE_PATH =config
			.getString("temp_file_path");
	public static final String MAP_FILE_PATH =config
			.getString("map_file_path");
	
	//数据字典状态
		public  static  enum D_STATE {
			ENABLE("启用",0),DISABLE("废弃",1);
			public String name;
			public Integer code;
			
			D_STATE(String name,Integer code) {
				this.name =  name;
				this.code = code;
			}
				
			public static String getText(Integer code) {
					if(code==ENABLE.code) {
						return ENABLE.name;
					}
					if(code==DISABLE.code) {
						return DISABLE.name;
					}
				return "";
			}
		};
			
		public static enum CONTRACT_STATE{
			REJECT("驳回",-1),DRAFT("草稿",0),CHARGE("主管评审",1),FTSO("二级评审",2),LEADER("领导评审",3),EGIS("订单分发",4),STORAGEREG("存储登记",5),QTYCTL("质检管理",6),SENDGOODS("发货",7),FINSH("已完成",10);
			public String name;
			public long code;
			CONTRACT_STATE(String name,long code) {
				this.name =  name;
				this.code = code;
			}

		}
		
		public static enum CONTRACT_MODIFY_APPLY_STATE{
			REJECT("驳回",-1),ADOAT("通过",0),REVIEWING("审核中",1);
			public String name;
			public long code;
			CONTRACT_MODIFY_APPLY_STATE(String name,long code) {
				this.name =  name;
				this.code = code;
			}
		}
		
		/**
		 * 设备运行状态
		 * @ClassName DEVICE_STATE
		 * @author Run.li
		 * @date 2017-3-1 上午10:23:46
		 */
		public static enum DEVICE_RUN_STATE {
			RUN("运行",1), STOP("停运",0), WARNING("报警",-1), BUG("故障",-2);
			public String name;
			public long code;
			DEVICE_RUN_STATE(String name,long code) {
				this.name =  name;
				this.code = code;
			}
		}
		
		public static enum ORDER_STATE{
			REJECT("驳回",-1),DRAFT("草稿",0),REVIEW("评审中",1),EGIS("分发中",2),STORAGEREG("生产中",3),QTYCTL("质检中",4),SENDGOODS("发货中",5),FINSH("已完成",6);;
			public String name;
			public long code;
			ORDER_STATE(String name,long code) {
				this.name =  name;
				this.code = code;
			}

		}

		public static enum SUBORDERSTATUS{
			WAITING("等待中","WAITING"),PROCESSING("处理中","PROCESSING"),FINISHED("完成","FINISHED"),FAILED("失败","FAILED");
			public String name;
			public String code;
			SUBORDERSTATUS(String name,String code) {
				this.name =  name;
				this.code = code;
			}
			public String getTextByCode(String code) {
				if(code.equals("WAITING")) {
					return "等待中";
				}
				if(code.equals("PROCESSING")) {
					return "处理中";
				}
				if(code.equals("FINISHED")) {
					return "完成";
				}
				if(code.equals("FAILED")) {
					return "失败";
				}
				return "";
			}
		}
			
		public static enum ORDER_COMPLAIN_STATE{
			HANDING("处理中",0),ADOAT("接收",1),REFUSE("拒绝",2);
			public String name;
			public long code;
			ORDER_COMPLAIN_STATE(String name,long code) {
				this.name =  name;
				this.code = code;
			}

		}
		
		public static enum CONSTRACT_APPLY_TYPE {
			CHARGE("主管评审",1),
			FINANCE("财务评审",2),
			TECHNOLOGY("技术服务评审",3),
			SECRECY("保密办评审",4),
			ORDERDEPART("订单部评审",5),
			LEADER("领导评审",6);
			
			public String name;
			public long code;
			CONSTRACT_APPLY_TYPE(String name,long code) {
				this.name =  name;
				this.code = code;
			
			}
			
			public static String getName(long code) {
				for(CONSTRACT_APPLY_TYPE e : values()) {
					if(code == e.code) {
						return e.name;
					}
				}
				return "";
			}
			
		}
		
		public static enum CONTRACT_APPLY_STATE{
			NONCHECKED("未审核",0),ADOPT("通过",1),REJECT("驳回",2);
			public String name;
			public long code;
			CONTRACT_APPLY_STATE(String name,long code) {
				this.name =  name;
				this.code = code;
			}
		}	

		//性别
		public  static  enum SEX {
			MAN("男",0),WOMAN("女",1);
			public String name;
			public Integer code;
			
			SEX(String name,Integer code) {
				this.name =  name;
				this.code = code;
			}
			
			
			
		};
	
	/**
	 * 项目端口控制 (apache和socket端口)
	 */
	public static final int APACHE_PORT = config.getInt("apache_port");
	public static final int ATTA_SOCKET_PORT = config.getInt("atta_socket_port");
	public static final int MAP_SOCKET_PORT = config.getInt("map_socket_port");
	
	/**
	 * url路径
	 */
	public static final String DOMAIN = config.getString("domain");
	public static final String DOWNLOAD_FILE_URL = config.getString("download_file_url");
	
	public static enum LOG_TYPE{
		UPDATE("update"),INSERT("insert"),DELETE("delete"),SELECT("select");
		public String name;
		private LOG_TYPE(String name) {
			// TODO Auto-generated constructor stub
			this.name =  name;
		}
	}
	
	public Constants(ServletConfig servletConfig) {
	}
	
	public static Map <Long,String> dictionarys = new HashMap<Long,String>();
	public static Map <Long,List<MDictionary>> dictionaryTypes = new HashMap<Long,List<MDictionary>>();
	
}
