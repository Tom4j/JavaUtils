package com.siweidg.comm.codeGen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


/**
 * 实体属性
 * @author work
 *
 */
public class Entry {
	

	
	public String templateName; //模板文件
	
	public String  rootPath ;     //根路径
	
	public String packagePath;
	
	public String  entryPath;   //实体路径
	
	public   String daoImplPath;  //daoImpl路径
	
	public   String daoPath ;
	
	public   String servicePath ;
	
	public   String serviceImplPath ;
	
	public   String actionPath ;
	
	public String  classSuffix;   //类后缀
	
	public String  className;	 //类名
	
	public String  classInstanceName; //类实体名  （首字母小写）
	
	public String  tableName;         //表名
	
	public String  idName;			 //主键字段名
	
	public String  idColumn;			  //主键列名
	
	public String  idType;			  //主键类型
	
	public String  idGetMethod;
	
	public String  idSetMethod;
	
	public String idSimpleType;
	
	public List<Property> propList = new ArrayList<Property>();

	public boolean hasHibernateVersion;

	public boolean hasDecimalType;

	public boolean hasDateType;
	
	public Entry(){}
	
	public Entry( String templateName, String rootPath,
			 String classSuffix,String packagePath) {
		super();
	
		this.templateName = templateName;
		this.rootPath = rootPath;
		this.classSuffix = classSuffix;
		this.packagePath = packagePath;
	}
	
	
	public boolean isHasDecimalType() {
		return hasDecimalType;
	}

	public void setHasDecimalType(boolean hasDecimalType) {
		this.hasDecimalType = hasDecimalType;
	}

	public boolean isHasDateType() {
		return hasDateType;
	}

	public void setHasDateType(boolean hasDateType) {
		this.hasDateType = hasDateType;
	}

	public String getDaoImplPath() {
		return daoImplPath;
	}

	public void setDaoImplPath(String daoImplPath) {
		this.daoImplPath = daoImplPath;
	}

	public String getDaoPath() {
		return daoPath;
	}

	public void setDaoPath(String daoPath) {
		this.daoPath = daoPath;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getServiceImplPath() {
		return serviceImplPath;
	}

	public void setServiceImplPath(String serviceImplPath) {
		this.serviceImplPath = serviceImplPath;
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	public boolean isHasHibernateVersion() {
		return hasHibernateVersion;
	}

	public void setHasHibernateVersion(boolean hasHibernateVersion) {
		this.hasHibernateVersion = hasHibernateVersion;
	}


	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getClassSuffix() {
		return classSuffix;
	}
	public void setClassSuffix(String classSuffix) {
		this.classSuffix = classSuffix;
	}
	
	public String getEntryPath() {
		return entryPath;
	}

	public void setEntryPath(String entryPath) {
		this.entryPath = entryPath;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassInstanceName() {
		return classInstanceName;
	}

	public void setClassInstanceName(String classInstanceName) {
		this.classInstanceName = classInstanceName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getIdColumn() {
		return idColumn;
	}

	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdGetMethod() {
		return idGetMethod;
	}

	public void setIdGetMethod(String idGetMethod) {
		this.idGetMethod = idGetMethod;
	}

	public String getIdSetMethod() {
		return idSetMethod;
	}

	public void setIdSetMethod(String idSetMethod) {
		this.idSetMethod = idSetMethod;
	}

	public List<Property> getPropList() {
		return propList;
	}

	public void setPropList(List<Property> propList) {
		this.propList = propList;
	}
	
	

	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public String getIdSimpleType() {
		return idSimpleType;
	}

	public void setIdSimpleType(String idSimpleType) {
		this.idSimpleType = idSimpleType;
	}

	public static class Property {
		
		public String propName;
		
		public String column;
		
		public String propType;
		
		public String notAllowNull;
		
		public String note;
		
		public String simpleType;
		
		public String getMethod;
		
		public String setMethod;
		
		public String getPropName() {
			return propName;
		}
		public void setPropName(String propName) {
			this.propName = propName;
		}
		public String getColumn() {
			return column;
		}
		public void setColumn(String column) {
			this.column = column;
		}
		public String getPropType() {
			return propType;
		}
		public void setPropType(String propType) {
			this.propType = propType;
		}
		public String getNotAllowNull() {
			return notAllowNull;
		}
		public void setNotAllowNull(String notAllowNull) {
			this.notAllowNull = notAllowNull;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getSimpleType() {
			return simpleType;
		}
		public void setSimpleType(String simpleType) {
			this.simpleType = simpleType;
		}
		public String getGetMethod() {
			return getMethod;
		}
		public void setGetMethod(String getMethod) {
			this.getMethod = getMethod;
		}
		public String getSetMethod() {
			return setMethod;
		}
		public void setSetMethod(String setMethod) {
			this.setMethod = setMethod;
		}

	}
}
