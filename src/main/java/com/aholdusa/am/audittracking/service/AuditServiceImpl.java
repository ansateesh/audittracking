package com.aholdusa.am.audittracking.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.AuditDAO;
import com.aholdusa.am.audittracking.entity.Audit;

/**
 * Service implementation class for Audit. Business logic involved in handling
 * Audits should be implemented here.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 * 
 * @see com.aholdusa.core.base.service.DBService.java
 *
 */
@Service("auditService")
public class AuditServiceImpl extends AMServiceImpl<Audit> implements
		AuditService {

	@Autowired
	private AuditDAO auditDao;

	public Audit populateEmployeeAndLane(Audit entity) {
		return this.auditDao.populateEmployeeAndLane(entity);
	}

	public List findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Audit> findAll() {
		// logger.info("finding audits");
		return auditDao.findAll();
	}

	public List<Audit> findAllByDate(Audit audit) {
		return this.auditDao.findByDate(audit);
	}

	public List<Audit> findAllByEmployeeAndDate(Audit audit) {
		return this.auditDao.findByEmployeeAndDate(audit);
	}

	public List<Audit> findAllByLaneAndDate(Audit audit) {
		return this.auditDao.findByLaneAndDate(audit);
	}

	public List<Audit> findAllByEmployee(Audit audit) {
		return this.auditDao.findByEmployee(audit);
	}

	@Override
	public List<Audit> findByEmployeeLaneAndDate(Audit entity, Date beginDate,Date endDate) {
		// TODO Auto-generated method stub
		return this.auditDao.findByEmployeeLaneAndDate(entity, beginDate, endDate);
	}

	@Override
	public List<Audit> findAuditsByStoreNumberBeginEndDate(Audit entity,
			Date beginDate, Date endDate) {
		// TODO Auto-generated method stub
		return auditDao.findAuditsByStoreNumberBeginEndDate(entity, beginDate, endDate);
	}
	
	@Override
	public List<Audit> findAuditsByStoreNumberOperatorNumberBeginEndDate(Audit entity,
			Date beginDate, Date endDate) {
		// TODO Auto-generated method stub
		return auditDao.findAuditsByStoreNumberOperatorNumberBeginEndDate(entity, beginDate, endDate);
	}
}