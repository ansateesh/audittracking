package com.aholdusa.am.audittracking.dao;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aholdusa.am.audittracking.entity.AmKey;
import com.aholdusa.am.audittracking.util.AMUtils;
import com.aholdusa.am.audittracking.util.HibernateUtil;

public abstract class DAOImpl<T, ID extends Serializable> implements DAO<T, ID> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private Class clazz;

	Criteria criteria;

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Class getClazz() {
		Class clazz = null;
		try {
			clazz = (Class) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
			this.setClazz(clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.clazz;
	}

	protected Session getSession() {
		return HibernateUtil.getSession();
	}

	public void save(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.saveOrUpdate(entity);
	}

	public void merge(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.merge(entity);
	}

	// public void delete(T entity) {
	// Session hibernateSession = this.getSession();
	// hibernateSession.delete(entity);
	// }

	public List<T> findMany(Query query) {
		List<T> t;
		t = (List<T>) query.list();
		return t;
	}

	public List<T> findByExample(T entity) {
		List<T> t;
		// Session hibernateSession = this.getSession();
		// HibernateUtil.beginTransaction();
		// BeanUtils.

		t = (List<T>) HibernateUtil.getCriteriaWithExample(entity.getClass(),
				Example.create(entity)).list();
		// try{
		// System.out.println("entity for query : " + entity + " of class:" +
		// entity.getClass());
		// System.out.println("criteria for query: " +
		// HibernateUtil.getCriteriaWithExample(entity.getClass(),
		// Example.create(entity)));
		// System.out.println("results from query : " + t);
		// }catch(Exception ex){
		//
		// }
		// HibernateUtil.commitTransaction();
		return t;
	}

	public T findOne(Query query) {
		T t;
		t = (T) query.uniqueResult();
		return t;
	}

	public T findByID(Long id) {
		Session hibernateSession = this.getSession();
		T t = null;
		t = (T) hibernateSession.get(getClazz(), id);
		return t;
	}

	// public List<T> findByObjectExample2(T entity) {
	//
	// Session hibernateSession = this.getSession();
	// List<T> listT = null;
	// Class clazz = getClazz();
	// String queryString = "from " + getClazz().getName()
	// + " where isDeleted = 0 ";
	// logger.info("debug statement 1");
	// Query query = hibernateSession.createQuery(null);
	//
	// }
	public Long getIdFromObject(Object entity) throws IllegalArgumentException,
			IllegalAccessException {
		Long retVal = null;
		for (Field f : entity.getClass().getDeclaredFields()) {
			// entity.getClass().get
			logger.info("getting " + f.getName() + " for:"
					+ entity.getClass().getCanonicalName());
			if (f.getName().equalsIgnoreCase("id")) {
				boolean accessFlag = f.isAccessible();
				f.setAccessible(true);
				//f.get(obj)
				//retVal = f.getLong(entity);
				retVal = (Long) f.get(entity);  
				f.setAccessible(accessFlag);
			}
		}
		if (retVal == null) {
			for (Field f : entity.getClass().getSuperclass()
					.getDeclaredFields()) {
				// entity.getClass().get
				logger.info("getting " + f.getName() + " for superclass:"
						+ entity.getClass().getCanonicalName());
				if (f.getName().equalsIgnoreCase("id")) {
					boolean accessFlag = f.isAccessible();
					f.setAccessible(true);
					//retVal = f.getLong(entity);
					retVal = (Long) f.get(entity);
					f.setAccessible(accessFlag);
				}
			}
		}
		return retVal;
	}

	public List<T> findByObjectExample(T entity) {

		Session hibernateSession = this.getSession();
		List<T> listT = null;
		// Query query = hibernateSession.createQuery();
		Class clazz = getClazz();
		String queryString = "from " + getClazz().getName()
				+ " where isDeleted = '0' ";
		logger.info("debug statement 1");
		for (Field f : clazz.getDeclaredFields()) {
			logger.info("field f: " + f.getName());
			if (f.getAnnotation(AmKey.class) != null) {
				Boolean accessible = false;
				if (f.isAccessible())
					accessible = true;
				if (!accessible) {
					f.setAccessible(true);
				}
				logger.info("building query string: " + queryString);
				try {
					try {
						logger.info("value of " + f.getName() + " : "
								+ f.get(entity).toString());
					} catch (Exception exception) {

					}
					if (f.get(entity) != null) {
						if (f.getType().equals(String.class)){
							queryString += " and " + f.getName() + " = '"
									+ f.get(entity) + "' ";
					}
					else if ( f.getType().equals(java.sql.Timestamp.class)){
						 	 
							Timestamp ts =AMUtils.getTimestampFromString(f.get(entity).toString());

							//  "yyyy-MM-dd HH:mm:ss.SSS"
							//  queryString += " and " + f.getName() + " = '"+ ts + "' ";

							queryString += " and " + f.getName() + " = TO_TIMESTAMP('"+ ts + "', 'YYYY-MM-DD HH24:MI:SS.FF1') ";
						 
					}
						else if (f.getType().getName()
								.startsWith("com.aholdusa")) {
							queryString += " and " + f.getName() + ".id = "
									+ getIdFromObject(f.get(entity)) + " ";
						} else
							queryString += " and " + f.getName() + " = "
									+ f.get(entity) + " ";

					}
				} catch (IllegalAccessException iae) {
					logger.error(iae.getStackTrace().toString());
					iae.printStackTrace();
				}
				if (!accessible)
					f.setAccessible(false);
			}
		}
		Query query = this.getSession().createQuery(queryString);
		// query.setProperties" and " + f.getName() + (entity);
		logger.info("executing query string: " + queryString);
		// logger.info("executing query : " + query.toString());

		listT = query.list();
		logger.info("results from findObjectByExample:" + listT.size());
		return listT;
	}

	public List<T> findAll() {
		Session hibernateSession = this.getSession();
		List<T> listT = null;
		Query query = hibernateSession.createQuery("from "
				+ getClazz().getName() + " where isDeleted = '0'");
		listT = query.list();
		return listT;
	}
}