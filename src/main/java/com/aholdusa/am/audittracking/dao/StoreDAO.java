package com.aholdusa.am.audittracking.dao;

import java.util.List;

import com.aholdusa.am.audittracking.entity.Store;
//import com.aholdusa.core.base.dao.DBDAO;

/**
 * Data access layer interface for Store. Extends DBDAO which defines standard CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface StoreDAO extends DAO<Store, Long> {
  //Define non-standard CRUD methods.
//	public Store findById(long id);
	public Store getStoreByIp(String storeIp) ;
	public Store getStoreByNumber(Integer storeNumber); 
	
}