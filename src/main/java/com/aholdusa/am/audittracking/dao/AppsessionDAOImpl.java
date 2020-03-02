package com.aholdusa.am.audittracking.dao;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.Appsession;



/**
 * Data access layer Implementation for Store. Extends AbstractHibernateDBDAO
 * which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */

@Repository("AppsessionDAO")
public class AppsessionDAOImpl extends DAOImpl<Appsession, Long> implements AppsessionDAO {

	private static final Logger logger = LoggerFactory.getLogger(Appsession.class);

	public void delete(Appsession entity) {
		// TODO Auto-generated method stub
		
	}

	public void insert(Appsession entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

	public void update(Appsession entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

	public List<Appsession> findByKey(String key){
		Query query = this.getSession().getNamedQuery("findByKey"); 
		query.setString("appKey", key); 
		return query.list(); 
	}

	public List<Appsession> findByObject(Appsession entity) {
		// TODO Auto-generated method stub
//		entity.setIsDeleted(true);
		return this.findByExample(entity);
}


}