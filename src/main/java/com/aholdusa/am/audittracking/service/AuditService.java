package com.aholdusa.am.audittracking.service;

import java.sql.Date;
import java.util.List;

import com.aholdusa.am.audittracking.entity.Audit;

/**
 * Service Layer interface for Audits. Extends from DBService which provides
 * basic implementation for standard CRUD/search operations. Any non-standard
 * CRUD/search on Audits operations should be defined here.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 *
 * @see com.aholdusa.core.base.service.DBService.java
 * 
 */
public interface AuditService extends AMService<Audit> {
	// Define non-standard CRUD methods.

	public List<Audit> findAll();

	public List<Audit> findAllByDate(Audit audit);

	public List<Audit> findAllByEmployeeAndDate(Audit audit);

	public List<Audit> findAllByLaneAndDate(Audit audit);

	public List<Audit> findAllByEmployee(Audit audit);
	public Audit populateEmployeeAndLane(Audit entity);
	public List<Audit> findByEmployeeLaneAndDate(Audit entity, Date beginDate,
			Date endDate);
	List<Audit> findAuditsByStoreNumberBeginEndDate(Audit entity, Date beginDate,Date endDate);
	
	List<Audit> findAuditsByStoreNumberOperatorNumberBeginEndDate(
				Audit entity, Date beginDate, Date endDate) ;
}