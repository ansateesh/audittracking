package com.aholdusa.am.audittracking.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.aholdusa.am.audittracking.entity.Till;

/**
 * Data access layer Implementation for Lane History. Extends AbstractHibernateDBDAO which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("tillDAO")
public class TillDAOImpl extends DAOImpl<Till, Long> implements TillDAO {
private static final Logger logger = LoggerFactory.getLogger(Till.class); 
	
//	public Messages findById(long id) {
//		return (Messages) super.findByID(Messages.class, id) ;
//	}

	public void delete(Till entity) {
		// TODO Auto-generated method stub
//		entity.setIsDeleted(true);
		this.save(entity);
	}
	
	public List<Till> findByObject(Till entity) {
		// TODO Auto-generated method stub
//		entity.setIsDeleted(true);
		return this.findByObjectExample(entity);
}

	public void insert(Till entity) {
		// TODO Auto-generated method stub
		this.insert(entity);
	}

	public void update(Till entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

	@Override
	public List<Till> getDrawer(Integer storeNumber, Date date) {
	 
		Query query = this.getSession().getNamedQuery("getDrawer"); 
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("date", date);
		List<Till> tillList = query.list(); 
		return tillList;
	}

//	public Messages findByID(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}