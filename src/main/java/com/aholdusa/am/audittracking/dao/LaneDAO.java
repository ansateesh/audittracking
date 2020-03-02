package com.aholdusa.am.audittracking.dao;

import java.util.List;

import com.aholdusa.am.audittracking.entity.Lane;

/**
 * Data access layer interface for Lane. Extends DBDAO which defines standard
 * CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface LaneDAO extends DAO<Lane, Long> {
	// Define non-standard CRUD methods.

	public  List<Lane> getCleanLanes(Integer storeNumber);
	public  Lane findByLaneNumber (Integer storeNumber, Integer laneNumber);
	public  List<Lane> getLanesByStoreId(Long storeNumber);
	public  List<Lane> getLanesByStoreNumber(Integer storeNumber);
	public  List<Lane> findActiveLanes(Lane entity);
	public List<Lane> getAuditLanes(Integer storeNumber);
	public  Lane findLaneByNumberAndStoreNumber (Integer storeNumber, Integer laneNumber);
}