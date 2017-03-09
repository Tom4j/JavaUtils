package com.siweidg.comm.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;


import com.alibaba.fastjson.JSON;
import com.siweidg.comm.dao.CommonDao;
import com.siweidg.comm.init.Constants;
import com.siweidg.comm.page.PageInfo;
import com.siweidg.comm.page.PageRequest;
import com.siweidg.comm.page.PageRequest.Sort;
import com.siweidg.comm.utils.AssertUtils;
import com.siweidg.comm.utils.TUtil;


public class CommonDaoImpl<T> implements CommonDao<T>{
	
	/** 属性比较类型. */
	public enum MatchType {
		EQ, LIKE, SLIKE, ELIKE, LT, GT, LE, GE,IN,NE,NI,NULL,NOTNULL,NLIKE,ALIAS;
		
	}
	
	
	protected SessionFactory sessionFactory;
	
	Class entityClass = TUtil.getActualType(this.getClass());

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void save(T t) {
		// TODO Auto-generated method stub
		getCurrentSession().saveOrUpdate(t);
	}

	@Override
	public T getById(Serializable id) {
		// TODO Auto-generated method stub
		return (T) getCurrentSession().get(entityClass, id);
	}
	
	@Override
	public T loadById(Serializable id) {
		return (T) getCurrentSession().load(entityClass, id);
	}
	
	@Override
	public void deleteById(Serializable id) {
		// TODO Auto-generated method stub
		T t = (T) getCurrentSession().get(entityClass,id);
		getCurrentSession().delete(t);
	}

	@Override
	public void update(T t) {
		// TODO Auto-generated method stub
		getCurrentSession().saveOrUpdate(t);
	}
	
	@Override
	public void executeHQL(String hql, Object... values) {
		// TODO Auto-generated method stub
		createQuery(hql, values).executeUpdate();
	}
	
	@Override
	public void executeHQL(String hql, Map<String, ?> values) {
		// TODO Auto-generated method stub
		createQuery(hql, values).executeUpdate();
	}
	
	@Override
	public <X> X findUnique(String hql, Object... values) {
		// TODO Auto-generated method stub
		Query query = createQuery(hql, values);
		return (X) query.uniqueResult();
	}
	
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return (X) createQuery(hql, values).uniqueResult();
	}
	
	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数.
	 * @param hql hql语句.
	 * @param values 命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public PageInfo<T> findPage(final PageRequest pageRequest, String hql, final Map<String, ?> values) {
		AssertUtils.notNull(pageRequest, "page不能为空");

		PageInfo<T> page = new PageInfo<T>(pageRequest);

		if (pageRequest.isCountTotal()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotal(totalCount);
		}

		if (pageRequest.isOrderBySetted()) {
			hql = setOrderParameterToHql(hql, pageRequest);
		}

		Query q = createQuery(hql, values);
		q.setFirstResult(pageRequest.getOffset());
	    q.setMaxResults(pageRequest.getPageSize());

		List result = q.list();
		page.setRows(result);
		return page;
	}
	
	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	private Query createQuery(final String queryString, final Map<String, ?> values) {
		AssertUtils.hasText(queryString, "queryString不能为空");
		Query query = getCurrentSession().createQuery(queryString);
		if (values != null && values.size()>0) {
			query.setProperties(values);
		}
		return query;
	}
	
	private Query createQuery(String hql, Object... values) {
		Session session = getCurrentSession();
		Query query = session.createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	@Override
	public <X> List<X> find(String hql, Object... values) {
			return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public List<T> find(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).list();
	}
	
	@Override
	public PageInfo<T> findPage(PageRequest pageRequest, String hql,
			Object... values) {
		// TODO Auto-generated method stub
		PageInfo<T> page = new PageInfo<T>(pageRequest);
		hql = setOrderParameterToHql(hql,pageRequest);
		if (pageRequest.isCountTotal()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotal(totalCount);
		}
		Query query = createQuery(hql, values);
        query.setFirstResult(pageRequest.getOffset());
        query.setMaxResults(pageRequest.getPageSize());
        List result = query.list();
		page.setRows(result);
		return page;

	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult( String hql, Object... values) {
		String countHql = prepareCountHql(hql);

		//try {
		Long count =  findUnique(countHql, values);
		if(count==null) {
			count=0l;
		}
			return count;
		/*} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}*/
	}
	
	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}
	
	private String prepareCountHql(String orgHql) {
		String countHql = "select count (*) " + removeSelect(removeOrders(orgHql));
		System.out.println(countHql);
		return countHql;
	}
	
	protected String setOrderParameterToHql(final String hql, final PageRequest pageRequest) {
		StringBuilder builder = new StringBuilder(hql);
		if(pageRequest.getSort()!=null) {
			builder.append(" order by");
	
			for (com.siweidg.comm.page.PageRequest.Sort orderBy : pageRequest.getSort()) {
				builder.append(String.format(" %s %s,", orderBy.getProperty(), orderBy.getDir()));
			}
	
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}

	private static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}
	
	public void delete(T t) {
		getCurrentSession().delete(t);
	}

	
	private static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
    public void delete(String tableName, String fieldName, String fieldValues) throws Exception {
        StringBuffer _hql = new StringBuffer();
        _hql.append(" delete from ").append(tableName);
        _hql.append(" where ").append(fieldName).append(" in (").append(fieldValues).append(")");
        executeSQL(_hql.toString());
    }
    
    public void update(final String tableName, String updateFieldName, String updateFieldValue,
        String updateKeyName, String updateKeyValue) throws Exception {
        StringBuffer _hql = new StringBuffer();
        _hql.append(" update ").append(tableName);
        _hql.append(" set ").append(updateFieldName).append(" = ").append(updateFieldValue);
        _hql.append(" where ").append(updateKeyName).append(" in (").append(updateKeyValue).append(")");
        executeSQL(_hql.toString());
    }
    
	public void executeSQL(String sql) throws Exception {
		getCurrentSession().createSQLQuery(sql).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> listSQL(String sql) throws Exception{
    	Query query = getCurrentSession().createSQLQuery(sql);
//    	query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
    	return query.list();
    }

	@Override
	public List<T> listSQLToEntity(String sql, Class<T> clazz) throws Exception {
    	Query query = getCurrentSession().createSQLQuery(sql);
    	query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
    	List<T> list = JSON.parseArray(JSON.toJSONString(query.list()), clazz);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> uniqueResultSQL(String sql) throws Exception{
    	Query query = getCurrentSession().createSQLQuery(sql);
    	query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	Map<String, Object> map = (Map<String, Object>) query.uniqueResult();
    	return map;
    }
	
	@Override
	public void updateSQL(String sql) {
		Query query = getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	/*@Override
	public PageInfo<T> findPage(PageRequest pageRequest,
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		PageInfo<T> page = new PageInfo<T>(pageRequest);
		Criteria criteria = this.getCurrentSession().createCriteria(entityClass);  
		 if (params != null){  
            Set<String> keys = params.keySet();  
            for (String key : keys){  
            	criteria.add(buildCriterionByKeyValue(key,params.get(key)));
            }  
	     }  
		 
		 int rowCount = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();  
	     criteria.setProjection(null);  
	     criteria.setFirstResult((pageRequest.getPageNo() - 1) * pageRequest.getPageSize());  
	     criteria.setMaxResults(pageRequest.getPageSize());  
	     List result = criteria.list();  
	     page.setTotal(rowCount);
	     page.setRows(result);
	     return page;
	}*/

	/**
	 * 通过key中"-"字符串后的值来决定eq,lt,gt或者like操作
	 * @param key
	 * @param criteria 
	 * @return
	 */
	
	private void buildCriterionByKeyValue(String key,Object value, Criteria criteria) {
		try {
			if (StringUtils.contains(key, "-")) {
				String splitArray[] = StringUtils.split(key, "-");
				MatchType type = MatchType.valueOf(splitArray[1].toUpperCase());
				 buildCriterion(splitArray[0], value, type,criteria);
			} else {
				 buildCriterion(key, value, MatchType.EQ,criteria);
			}
		} catch (Exception e) {

			 buildCriterion(StringUtils.split(key, "-")[0], value, MatchType.EQ,criteria);
		}
	}
	
	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 * @param criteria 
	 */
	protected void buildCriterion(final String propertyName, final Object propertyValue, final MatchType matchType, Criteria criteria) {
		AssertUtils.hasText(propertyName, "propertyName不能为空");

			switch (matchType) {
				case EQ:
					criteria.add(Restrictions.eq(propertyName, propertyValue));
					break;
				case LIKE:
					criteria.add(Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE));
					break;
				case SLIKE:
					criteria.add(Restrictions.like(propertyName, (String) propertyValue, MatchMode.START));
					break;
				case ELIKE:
					criteria.add(Restrictions.like(propertyName, (String) propertyValue, MatchMode.END));
					break;
				case LE:
					criteria.add(Restrictions.le(propertyName, propertyValue));
					break;
				case LT:
					criteria.add(Restrictions.lt(propertyName, propertyValue));
					break;
				case GE:
					criteria.add(Restrictions.ge(propertyName, propertyValue));
					break;
				case GT:
					criteria.add(Restrictions.gt(propertyName, propertyValue));
					break;
				case IN:
					criteria.add(Restrictions.in(propertyName, (Collection) propertyValue));
					break;
				case NE:
					criteria.add(Restrictions.ne(propertyName, propertyValue));
					break;
				case NI:
					criteria.add(Restrictions.not(Restrictions.in(propertyName, (Collection) propertyValue)));
					break;
				case NULL:
					criteria.add(Restrictions.isNull(propertyName));
					break ;
				case NOTNULL:
					criteria.add(Restrictions.isNotNull(propertyName));
					break ;
				case NLIKE:
					criteria.add(Restrictions.not(Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE)));
					break ;
				case ALIAS:
					criteria.createAlias(propertyName, (String)propertyValue);
					break ;
			}
				//return criterion;
	}
	
	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page 分页参数.
	 * @param criterions 数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	public PageInfo<T> findPage(final PageRequest pageRequest, final Criterion... criterions) {
		AssertUtils.notNull(pageRequest, "page不能为空");

		PageInfo<T> page = new PageInfo<T>(pageRequest);

		Criteria c = createCriteria(criterions);

		if (pageRequest.isCountTotal()) {
			Long rowCount = (Long) c.setProjection(Projections.rowCount()).uniqueResult();  
		     c.setProjection(null);  
			page.setTotal(rowCount);
		}

		setPageRequestToCriteria(c, pageRequest,true);

		List result = c.list();
		page.setRows(result);
		return page;
	}
	
	/**
	 * 根据Criterion条件创建Criteria.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
	
	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageRequestToCriteria(final Criteria c, final PageRequest pageRequest,boolean limit) {
		AssertUtils.isTrue(pageRequest.getPageSize() > 0, "Page Size must larger than zero");

		c.setFirstResult(pageRequest.getOffset());
		if (limit)
			c.setMaxResults(pageRequest.getPageSize());

		if (pageRequest.isOrderBySetted()) {
			for (Sort sort : pageRequest.getSort()) {
				if (Sort.ASC.equals(sort.getDir())) {
					c.addOrder(Order.asc(sort.getProperty()));
				} else {
					c.addOrder(Order.desc(sort.getProperty()));
				}
			}
		}
		return c;
	}

	@Override
	public PageInfo<T> findPage(PageRequest pageRequest,
			Map<String, Object> params, Criterion... criterions) {
		// TODO Auto-generated method stub
		PageInfo<T> page = new PageInfo<T>(pageRequest);
		Criteria criteria = this.getCurrentSession().createCriteria(entityClass);  
		 if (params != null){  
            Set<String> keys = params.keySet();  
            for (String key : keys){  
            	buildCriterionByKeyValue(key,params.get(key),criteria);
            }  
	     } 
		 
		 for (Criterion c : criterions) {
				criteria.add(c);
		 }
		 
		 Long rowCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();  
	     criteria.setProjection(null);  
//	     criteria.setFirstResult((pageRequest.getPageNo() - 1) * pageRequest.getPageSize());  
//	     criteria.setMaxResults(pageRequest.getPageSize());  
		 setPageRequestToCriteria(criteria, pageRequest,true);
		 
	     List result = criteria.list();  
	     page.setTotal(rowCount);
	     page.setRows(result);
	     return page;
	}

	@Override
	public List<T> find(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return find(params, null, null);
	}

	@Override
	public List<T> find(Map<String, Object> params, String order, String dir) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
	
				Criteria criteria = this.getCurrentSession().createCriteria(entityClass);  
				 if (params != null){  
		           Set<String> keys = params.keySet();  
		           for (String key : keys){  
		           	buildCriterionByKeyValue(key,params.get(key),criteria);
		           }  
			     } 
				 if(order!=null&&dir!=null) {
					 if(dir.equals("asc")) {
						 criteria.addOrder(Order.asc(order));
					 } else if(dir.equals("desc")) {
						 criteria.addOrder(Order.desc(order));
					 }
				 }
				 return criteria.list();
	}

	@Override
	public List<T> find(Criterion... criterions) {
		// TODO Auto-generated method stub
		Criteria criteria = this.getCurrentSession().createCriteria(entityClass); 
		 for (Criterion c : criterions) {
				criteria.add(c);
		 }
		return criteria.list();
	}
	
	public PageInfo<Map> findPageMap(final PageRequest pageRequest, String sql) {
		AssertUtils.notNull(pageRequest, "page不能为空");
		PageInfo<Map> page = new PageInfo<Map>(pageRequest);
	
		String countSql = this.buildCountSql(sql);
		Session session = this.getCurrentSession();
		SQLQuery queryCount = session.createSQLQuery(countSql);
		Long count = new Long(queryCount.uniqueResult().toString());
		page.setTotal(count);
		
		StringBuilder builder = new StringBuilder(sql);
		if (!sql.contains("order by") && pageRequest.isOrderBySetted()) {			
			builder.append(" order by");
			for (Sort orderBy : pageRequest.getSort()) {
				builder.append(String.format(" %s %s,", orderBy.getProperty(),
						orderBy.getDir()));
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		SQLQuery query = session.createSQLQuery(builder.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setFirstResult(pageRequest.getOffset());
		query.setMaxResults(pageRequest.getPageSize());
//		query.setResultTransformer(Transformers.aliasToBean(entityClass));
		List list = query.list();
		page.setRows(list);
		return page;
	}
	
	public List<Map> findSql(String sql) {
		Session session = this.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	private String buildCountSql(String sql) {
		return "select count(*) from (" + sql+" ) A";
	}

}
