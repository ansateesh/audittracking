package com.aholdusa.am.audittracking.service;

import java.util.List;

import com.aholdusa.am.audittracking.entity.CarryForwardNotificationTolt;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;

/**
 * Service Layer interface for Lanes. Extends from DBService which provides
 * basic implementation for standard CRUD/search operations. Any non-standard
 * CRUD operations on Lanes should be defined here.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 *
 * @see com.aholdusa.core.base.service.DBService.java
 * 
 */
public interface LaneService extends AMService<Lane> {

	void update(Lane l);

	void delete(Lane tmp);

	void insert(Lane l);

	public String lockLaneForUpdate(Lane lane, String key);

	public String unlockLane(Lane lane);

	public List<Lane> getCleanLanes(Integer storeNumber);

	public Lane findByLaneNumber(Integer storeNumber, Integer laneNumber);

	public List<Lane> getLanesByStoreNumber(Integer storeNumber);
	
	public  List<Lane> findActiveLanes(Lane entity);
	
	public String getOverShortReport(CarryForwardNotificationTolt carryForwardNotification);
	
	List<Lane> getAuditLanes(Integer storeNumber);
	
	public Lane findLaneByNumberAndStoreNumber(Integer storeNumber, Integer laneNumber);
	
}