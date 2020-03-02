package com.aholdusa.am.audittracking.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;

/**
 * Data access layer interface for Lane History. Extends DBDAO which defines
 * standard CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface LaneHistoryDAO extends DAO<LaneHistory, Long> {
	// Define non-standard CRUD methods.

	 
	  List<LaneHistory> findByAssignment(LaneHistory lh);

	  List<LaneHistory> findAllByDate(LaneHistory lh);

	  List<LaneHistory> findAllByDateAndLane(LaneHistory lh);

	  boolean laneHistoryExistsForEmployee(Long employeeId);

	  boolean laneHistoryExistsForLane(Long laneId);

	  List<LaneHistory> findLastLaneHistoryByDateAndLane(LaneHistory lh);
	
	  LaneHistory findLastLaneHistoryByStoreAndLane(LaneHistory laneHistory) throws AuditTrackingException;
	  
	  void reEnableLaneInLaneHistory(Long laneId, Integer storeNumber);
	  
	  void deleteLaneInLaneHistory(Long laneId, Integer storenumber);

}