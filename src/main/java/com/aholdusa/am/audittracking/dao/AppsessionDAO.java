package com.aholdusa.am.audittracking.dao;

import java.util.List;

import com.aholdusa.am.audittracking.entity.Appsession;
//import com.aholdusa.core.base.dao.DBDAO;

/**
 * Data access layer interface for Store. Extends DBDAO which defines standard CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface AppsessionDAO extends DAO<Appsession, Long> {
	public List<Appsession> findByKey(String key);
}