package com.aholdusa.am.audittracking.service;

import java.util.List;

import com.aholdusa.am.audittracking.entity.AMError;
import com.aholdusa.am.audittracking.entity.Employee;

/**
 * Service Layer interface for Employees. Extends from DBService which provides
 * basic implementation for standard CRUD/search operations. Any non-standard
 * CRUD/search on Employees operations should be defined here.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 *
 * @see com.aholdusa.core.base.service.DBService.java
 * 
 */
public interface EmployeeService extends AMService<Employee> {

	void delete(Employee tmp);

	void update(Employee e);

	void insert(Employee e);

	public AMError processEmployee(Employee employee);

	public List<Employee> findByOperatorNumber(Integer storeNumber,
			Integer operatorNumber);

	public List<Employee> findByName(Integer storeNumber, String name);

	public String lockEmployeeForUpdate(Employee employee, String key);

	public String unlockEmployee(Employee employee);

	public List<Employee> findByObject(Employee employee, Boolean greedy);
	
	public String getEmployeeFirstName(String nameOperator);
	
	public String getEmployeeLastName(String nameOperator);
	
	
}