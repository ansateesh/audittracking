package com.aholdusa.am.audittracking.dao;

import com.aholdusa.am.audittracking.entity.Messages;

/**
 * Data access layer interface for Lane. Extends DBDAO which defines standard CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface MessageDAO extends DAO<Messages, Long> {
  // Define non-standard CRUD methods.
}