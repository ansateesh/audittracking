package com.aholdusa.am.audittracking.entity;

public enum EmployeeConstants {

	EMPLOYEE_UPDATED("EmployeeUpdated"),
	ADMIN_USER("AdminUser"),
	INVALID_EMPLOYEE("9999");
	
	private String value;
	 
	private EmployeeConstants(String value) {
		this.value = value;
	}
 
	public String getValue() {
		return value;
	}
	
}
