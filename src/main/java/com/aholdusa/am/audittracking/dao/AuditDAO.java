package com.aholdusa.am.audittracking.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;

import com.aholdusa.am.audittracking.entity.Audit;

/**
 * Data access layer interface for Audit. Extends DBDAO which defines standard CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface AuditDAO extends DAO<Audit, Long> {
  //Define non-standard CRUD methods.
	
//	public List<Audit> findAll(); 
	public List<Audit> findByDate(Audit entity); 
	public List<Audit> findByEmployeeAndDate(Audit entity);
	public List<Audit> findByEmployee(Audit entity);
	public List<Audit> findByLaneAndDate(Audit entity); 
	public Audit populateEmployeeAndLane(Audit entity);
	public List<Audit> findByEmployeeLaneAndDate(Audit entity,Date beginDate, Date endDate);
	
	List<Audit> findByLane(Audit entity);
	List<Audit> findAuditsByStoreNumberBeginEndDate(Audit entity, Date beginDate,Date endDate);
	List<Audit> findAuditsByStoreNumberOperatorNumberBeginEndDate(Audit entity, Date beginDate,
			Date endDate);
	 List<Long> getLanesInAudit(Audit entity);
	 
	 boolean reEnableLaneInAudits(Long laneId, Integer storeNumber);
	 boolean deleteLaneInAudits(Long laneId, Integer storeNumber);
}