package com.aholdusa.am.audittracking.service;

import java.util.List;

import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;

/**
 * Service Layer interface for Lane History. Extends from DBService which
 * provides basic implementation for standard CRUD/search operations. Any
 * non-standard CRUD operations on Lane History should be defined here.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 *
 * @see com.aholdusa.core.base.service.DBService.java
 * 
 */
public interface LaneHistoryService extends AMService<LaneHistory> {
	  List<LaneHistory> findByAssignment(LaneHistory lh);
	  List<LaneHistory> findAllByDate(LaneHistory lh);
	  List<LaneHistory> findAllByDateAndLane(LaneHistory lh);
	  List<LaneHistory> findLastLaneHistoryByDateAndLane(LaneHistory lh);
	  LaneHistory findLastLaneHistoryByStoreAndLane(LaneHistory laneHistory)
				throws AuditTrackingException;
}