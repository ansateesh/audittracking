package com.aholdusa.am.audittracking.entity;

import java.sql.Timestamp;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * Entity for employees. Extends AbstractEntity which provides common attributes
 * like id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class Employee extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	@Expose
	@AmKey
	private Integer operatorNumber;
	@Expose
	@AmKey
	private String firstName;
	@Expose
	@AmKey
	private String lastName;
	@Expose
	private Boolean requiresAudit;
	@Expose
	@AmKey
	private Boolean active;
	@Expose
	@AmKey
	private Integer storeNumber;
	
	@Expose
	private String operatorName;
	@Expose
	private Timestamp markedForAuditTime;

	public Timestamp getMarkedForAuditTime() {
		return markedForAuditTime;
	}

	public void setMarkedForAuditTime(Timestamp markedForAuditTime) {
		this.markedForAuditTime = markedForAuditTime;
	}

	@Expose
	@AmKey
	private Boolean overridden;

	public Boolean getOverridden() {
		return overridden;
	}

	public void setOverridden(Boolean overridden) {
		this.overridden = overridden;
	}

	// @Expose
	// @AmKey
	private List<EmployeeDetail> employeeDetailList;

	// private Integer recordStatus;
	// private String lockedBy;

	public Employee() {
		super();
	}

	public Integer getOperatorNumber() {
		return operatorNumber;
	}

	public void setOperatorNumber(Integer operatorNumber) {
		this.operatorNumber = operatorNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getRequiresAudit() {
		return requiresAudit;
	}

	public void setRequiresAudit(Boolean audited) {
		this.requiresAudit = audited;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getOverriden() {
		return overridden;
	}

	public void setOverriden(Boolean overriden) {
		this.overridden = overriden;
	}

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}

	public boolean isEmpty() {
		if ((this.operatorNumber == null || this.operatorNumber == 0)
				&& (this.firstName == null || this.firstName.equals(""))
				&& (this.lastName == null || this.lastName.equals("")))
			return true;
		return false;
	}

	public List<EmployeeDetail> getEmployeeDetail() {
		return employeeDetailList;
	}

	public void setEmployeeDetail(List<EmployeeDetail> employeeDetailList) {
		this.employeeDetailList = employeeDetailList;
	}
	
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}	
	

}