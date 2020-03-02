package com.aholdusa.am.audittracking.dao;

import java.util.Date;
import java.util.List;

import com.aholdusa.am.audittracking.entity.Till;

/**
 * Data access layer interface for Lane. Extends DBDAO which defines standard CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface TillDAO extends DAO<Till, Long> {
  // Define non-standard CRUD methods.
	List<Till> getDrawer(Integer storeNumber,Date date);
}