package com.siweidg.comm.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.siweidg.comm.page.PageInfo;
import com.siweidg.comm.page.PageRequest;

public interface CommonDao<T> {
	
	public void save(T t);
	
	public void delete(T t) ;
	
	public T getById(Serializable id);
	
	public T loadById(Serializable id);
	
	public void deleteById(Serializable id);
	
	public void update(T t);
	
	/**
	 * 执行带条件的更新或删除hql语句
	 * @Description: TODO  
	 * @return void
	 * @param hql
	 * @param values
	 * @author Run.li
	 * @date 2016-4-30 下午9:27:50
	 */
	public void executeHQL(String hql,Object... values);
	
	public void executeHQL(String hql, Map<String, ?> values);
	
	public <X> X findUnique(String hql,Object... values);
	
	public List<T> find(Criterion... criterions);
	
	public <X> X findUnique(String hql, Map<String, ?> values);
	
	public  <X> List<X>  find(String hql,Object... values);
	
	public List<T> find(final Map<String,Object> params);
	
	public List<T> find(final Map<String,Object> params,String order,String dir);
	
	public List<T> find(final String hql, final Map<String, ?> values);
	
	public PageInfo<T> findPage(PageRequest pageRequest, String hql,Object... values);
	
	public PageInfo<T> findPage(final PageRequest pageRequest, String hql, final Map<String, ?> values);

    /**
     * 批量更新实体
     * @param tableName 表名称
     * @param updateFieldName 列名称
     * @param updateFieldValue 新值
     * @param updateKeyName 条件 列名称
     * @param updateKeyValue 某值
     * @return 返回执行结果，-1 表示失败
     * @throws Exception 
     */
    public void update(final String tableName, String updateFieldName, String updateFieldValue, String updateKeyName, String updateKeyValue) throws Exception;
    
    /**
     * 删除
     * @param tableName 表名
     * @param fieldName 字段名
     * @param fieldValues 字段值 [支持批量删除]<br/>如：id=4或id="4,5"
     * @throws Exception 
     */
    public void delete(String tableName, String fieldName, String fieldValues) throws Exception;
    
    /**
     * 执行原生SQL语句
     * 【注意：不能执行Select查询语句】可执行插入、更新、删除语句
     * @param sql
     * @throws Exception 
     */
    public void executeSQL(String sql) throws Exception;
    
    /**
     * 执行本地SQL语句
     * 【注意：只能执行Select查询语句】
     * @param sql
     * @return List
     * @throws Exception 
     */
	@SuppressWarnings("rawtypes")
	public List listSQL(String sql) throws Exception;
	
	/**
	 * 更新语句 -- 执行原生SQL语句 
	 * @Description: TODO  
	 * @return void
	 * @param sql
	 * @author Run.li
	 * @date 2016-4-30 下午9:03:48
	 */
	public void updateSQL(String sql);
	
	/**
	 * 返回SQL查询结果的第一条记录
	 * @param sql
	 * @return 以MAP形式返回第一条结果
	 * @throws Exception
	 */
	public Map<String, Object> uniqueResultSQL(String sql) throws Exception;
	
	/**
	 * 执行本地sql转化成对应的实体类集合
	 * @Description: TODO  需要fastjson的支持
	 * @return List<T>
	 * @param sql 原生SQL语句
	 * @param clazz 要转换的实体类
	 * @return 实体集合
	 * @throws Exception
	 * @author Run.li
	 * @date 2016-4-29 下午10:50:56
	 */
	public List<T> listSQLToEntity(String sql, Class<T> clazz) throws Exception;
	
	/**
	 * paremas 查询条件  列入  params.put("name-LIKE","zxx"); 
	 * 默认为eq
	 */
	//public PageInfo<T> findPage(PageRequest pageRequest,Map<String,Object> params);
	
	/**
	 * 高级查询
	 * @param pageRequest
	 * @param criterions
	 * @return
	 */
	public PageInfo<T> findPage(final PageRequest pageRequest, final Criterion... criterions);
	
	/**
	 * 组合查询
	 */
	
	public PageInfo<T> findPage(PageRequest pageRequest,Map<String,Object> params, final Criterion... criterions );
	
	public PageInfo<Map> findPageMap(final PageRequest pageRequest, String sql);
	
	public List<Map> findSql(String sql) ;
}
