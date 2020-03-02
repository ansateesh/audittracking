package com.aholdusa.am.audittracking.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Messages;

/**
 * Data access layer Implementation for Lane History. Extends AbstractHibernateDBDAO which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("messageDAO")
public class MessageDAOImpl extends DAOImpl<Messages, Long> implements MessageDAO {
private static final Logger logger = LoggerFactory.getLogger(Messages.class); 
	
//	public Messages findById(long id) {
//		return (Messages) super.findByID(Messages.class, id) ;
//	}

	public void delete(Messages entity) {
		// TODO Auto-generated method stub
		entity.setIsDeleted(true);
		this.save(entity);
	}

	public List<Messages> findByObject(Messages entity) {
		// TODO Auto-generated method stub
//		entity.setIsDeleted(true);
		return this.findByObjectExample(entity);
	}
	public void insert(Messages entity) {
		// TODO Auto-generated method stub
		this.insert(entity);
	}

	public void update(Messages entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

//	public Messages findByID(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}