package com.aholdusa.am.audittracking.service;

import java.util.List;

import com.aholdusa.am.audittracking.entity.Store;

/**
 * Service Layer interface for Store. Extends from DBService which provides basic implementation for standard CRUD/search operations. 
 * Any non-standard CRUD operations on Stores should be defined here.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 *
 * @see com.aholdusa.core.base.service.DBService.java
 * 
 */
public interface StoreService extends AMService<Store> {
  // Define non-standard CRUD methods.
	Store getStoreFromIp(String ip); 
	Store getStoreByNumber(Integer storeNumber);
	Store getStoreByUUID(String UUID);
	void createStore(Integer storeNumber, String uuid);
	void createStoreForToltRequest(Integer storeNumber, String uuid);
}
