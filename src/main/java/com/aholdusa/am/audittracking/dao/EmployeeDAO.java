package com.aholdusa.am.audittracking.dao;

import java.util.List;

import org.hibernate.Query;

import com.aholdusa.am.audittracking.entity.Employee;

/**
 * Data access layer interface for Employee. Extends DBDAO which defines standard CRUD operations.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface EmployeeDAO extends DAO<Employee, Long> {
	public List<Employee> findByOperatorNumber(Integer storeNumber, Integer operatorNumber) ;
	
	public List<Employee> findByName(Integer storeNumber, String firstName, String lastName) ;
	
	public List<Employee> partialFindByName(Integer storeNumber, String name) ;

	public List<Employee> partialFindByOperatorNumber(Integer storeNumber, Integer operatorNumber) ;
	
	public List<Employee> partialFindByName(Integer storeNumber, String firstName, String lastName) ;

}