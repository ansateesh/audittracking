package com.aholdusa.am.audittracking.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;

/**
 * Data access layer Implementation for Audit. Extends AbstractHibernateDBDAO
 * which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("auditDAO")
public class AuditDAOImpl extends DAOImpl<Audit, Long> implements AuditDAO {

	private static final Logger logger = LoggerFactory.getLogger(Audit.class);

	public List<Audit> findByDate(Audit entity) {
		Query query = this.getSession().getNamedQuery("findAllAuditsByDate");
		query.setParameter("storeNumber", entity.getStoreNumber());
		query.setParameter("date", entity.getDate());
		return query.list();
	}

	public List<Audit> findByEmployeeAndDate(Audit entity) {
		Query query = this.getSession().getNamedQuery(
				"findAllAuditsByEmployeeAndDate");
		query.setParameter("storeNumber", entity.getStoreNumber());
		query.setParameter("date", entity.getDate());
		query.setParameter("operatorNumber", entity.getEmployee()
				.getOperatorNumber());
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.dao.AuditDAO#findByLaneAndDate(com.aholdusa.am.audittracking.entity.Audit)
	 */
	public List<Audit> findByLane(Audit entity) {
		Query query = this.getSession()
				.getNamedQuery("findAuditsByLane");
		query.setParameter("storeNumber", entity.getStoreNumber());
		query.setParameter("date", entity.getDate());
		query.setParameter("laneNumber", entity.getLane().getNumber());
		query.setParameter("startTime", entity.getStartTime());
		return query.list();
	}
	
	public List<Audit> findByLaneAndDate(Audit entity) {
		Query query = this.getSession()
				.getNamedQuery("findAllAuditsByLaneAndDate");
		query.setParameter("storeNumber", entity.getStoreNumber());
		query.setParameter("date", entity.getDate());
		query.setParameter("laneNumber", entity.getLane().getNumber());
		return query.list();
	}

	public List<Audit> findByEmployee(Audit entity) {
		Query query = this.getSession()
				.getNamedQuery("findAllAuditsByEmployee");
		query.setParameter("storeNumber", entity.getStoreNumber());
		query.setParameter("operatorNumber", entity.getEmployee()
				.getOperatorNumber());
		return query.list();
	}
	
	/**
	 * reEnableLaneInAudits(): Un soft-Delete(is_deleted = false) all instances 
	 * 							  of a given lane from the Audits table. 
	 * @param Long laneId: Unique Id for lane
	 * @param Integer storeNumber: store Number
	 * 
	 * @return boolean: Always true
	 */
	public boolean reEnableLaneInAudits(Long laneId, Integer storeNumber) {
		Query query = this.getSession().getNamedQuery("reEnableLaneInAudits");
		query.setParameter("laneId", laneId);
		query.setParameter("storeNumber", storeNumber);
		query.executeUpdate();
		return true;
	}
	
	/**
	 * deleteLaneInAudits(): Soft-Delete(is_deleted = true) all instances 
	 * 							  of a given lane from the Audits table. 
	 * @param Long laneId: Unique Id for lane
	 * @param Integer storeNumber: store Number
	 * 
	 * @return boolean: Always true
	 */
	public boolean deleteLaneInAudits(Long laneId, Integer storeNumber) {
		Query query = this.getSession().getNamedQuery("deleteLaneInAudits");
		query.setParameter("laneId", laneId);
		query.setParameter("storeNumber", storeNumber);
		query.executeUpdate();
		return true;
	}

	@Override
	public List<Audit> findByEmployeeLaneAndDate(Audit entity, Date beginDate,
			Date endDate) {
		Query query = this.getSession()
				.getNamedQuery("findAllAuditsByEmployeeLaneBeginEndDate");
		       
				query.setParameter("storeNumber", entity.getStoreNumber());
				query.setParameter("operatorNumber", entity.getEmployee().getOperatorNumber());
				query.setParameter("laneNumber", entity.getLane().getNumber());
				query.setParameter("beginDate", beginDate);
				query.setParameter("endDate", endDate);
				
		return query.list();
	}
	
	
	/*
	 * Reporting Query By Store And Audit
	 * (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.dao.AuditDAO#findAuditsByStoreNumberBeginEndDate(com.aholdusa.am.audittracking.entity.Audit, java.sql.Date, java.sql.Date)
	 */
	@Override
	public List<Audit> findAuditsByStoreNumberBeginEndDate(Audit entity, Date beginDate,
			Date endDate) {
		Query query = this.getSession()
				.getNamedQuery("findAuditsByStoreNumberBeginEndDate");
		       
				query.setParameter("storeNumber", entity.getStoreNumber());
				//query.setParameter("operatorNumber", entity.getEmployee().getOperatorNumber());
				query.setParameter("beginDate", beginDate);
				query.setParameter("endDate", endDate);
				
		return query.list();
	}
	
	
	@Override
	public List<Audit> findAuditsByStoreNumberOperatorNumberBeginEndDate(
			Audit entity, Date beginDate, Date endDate) {
		Query query = this.getSession()
				.getNamedQuery("findAuditsByStoreNumberOperatorNumberBeginEndDate");
		       
				query.setParameter("storeNumber", entity.getStoreNumber());
				query.setParameter("operatorNumber", entity.getEmployee().getOperatorNumber());
				query.setParameter("beginDate", beginDate);
				query.setParameter("endDate", endDate);
				
		return query.list();
	}
	
	public List<Audit> findByObject(Audit entity) {

		try { 
			logger.info("finding audit by example:" + entity.toString());
			List <Audit> auditList = this.findByObjectExample(entity);
			logger.info("audits found: " + auditList.toString()); 
			return auditList; 
		}catch(Exception ex) {
			ex.printStackTrace();
			return null; 
		}
	}

 
	public Audit populateEmployeeAndLane(Audit entity) {
		// TODO Auto-generated method stub
		// entity.setIsDeleted(true);
		Employee employee =null;  
		Query empQuery = this.getSession().getNamedQuery("findEmployeeByOperatorNumber") ; 
		empQuery.setParameter("operatorNumber", entity.getEmployee().getOperatorNumber());
		empQuery.setParameter("storeNumber", entity.getStoreNumber()); 
		try {
			logger.info("finding employee");
			employee= (Employee) empQuery.list().get(0); 
			logger.info("employee found:" + employee.toString());
		}catch (Exception ex){
			ex.printStackTrace();
			return null; 
		}
		Lane lane =null;  
		Query laneQuery = this.getSession().getNamedQuery("findLaneByNumber") ; 
		laneQuery.setParameter("laneNumber", entity.getLane().getNumber());
		laneQuery.setParameter("storeNumber", entity.getStoreNumber()); 
		try {
			logger.info("finding lane");
			lane= (Lane) laneQuery.list().get(0); 
			logger.info("lane found:" + lane.toString());
		}catch (Exception ex){
			ex.printStackTrace();
			return null; 
		}
		entity.setEmployee(employee);
		entity.setLane(lane);
		return entity;
	}
	public void insert(Audit entity) {
		// TODO Auto-generated method stub
//		Employee employee =null;  
//		Query empQuery = this.getSession().getNamedQuery("findEmployeeByOperatorNumber") ; 
//		empQuery.setParameter("operatorNumber", entity.getEmployee().getId()); 
//		try {
//			employee= (Employee) empQuery.list().get(0); 
//		}catch (Exception ex){
//			ex.printStackTrace();
//			return; 
//		}
//		Lane lane =null;  
//		Query laneQuery = this.getSession().getNamedQuery("findLaneNumber") ; 
//		laneQuery.setParameter("laneNumber", entity.getLane().getId()); 
//		try {
//			lane= (Lane) laneQuery.list().get(0); 
//		}catch (Exception ex){
//			ex.printStackTrace();
//			return; 
//		}
//		entity.setEmployee(employee);
//		entity.setLane(lane);
		this.save(entity);
	}

	public void delete(Audit entity) {
		// TODO Auto-generated method stub

	}

	public void update(Audit entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

	

	public List<Long> getLanesInAudit(Audit entity){

		Query query = this.getSession()
				.getNamedQuery("findLaneInAudit");
		query.setParameter("storeNumber", entity.getStoreNumber());
		query.setParameter("date", entity.getDate());
		query.setParameter("laneNumber", entity.getLane().getNumber());
		query.setParameter("startTime", entity.getStartTime());
		return query.list();

	}


//	public Audit findByID(Long id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}