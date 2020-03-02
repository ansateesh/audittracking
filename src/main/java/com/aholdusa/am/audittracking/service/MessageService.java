package com.aholdusa.am.audittracking.service;

import com.aholdusa.am.audittracking.entity.Messages;

/**
 * Service Layer interface for Lanes. Extends from DBService which provides basic implementation for standard CRUD/search operations. 
 * Any non-standard CRUD operations on Lanes should be defined here.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 *
 * @see com.aholdusa.core.base.service.DBService.java
 * 
 */
public interface MessageService extends AMService<Messages> {
  //Define non-standard CRUD methods.
}