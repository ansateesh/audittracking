package com.aholdusa.am.audittracking.entity;

import com.google.gson.annotations.Expose;

/**
 * Entity for employees. Extends AbstractEntity which provides common attributes
 * like id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class EmployeeDetail extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	@Expose
	@AmKey
	private Long employeeId;
	@Expose
	private Boolean requiresAudit;
	@Expose
	@AmKey
	private Boolean active;
	@Expose
	@AmKey
	private Integer storeNumber;
	

	// private Integer recordStatus;
	private String comments;

	public EmployeeDetail() {
		super();
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Boolean getRequiresAudit() {
		return requiresAudit;
	}

	public void setRequiresAudit(Boolean requiresAudit) {
		this.requiresAudit = requiresAudit;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
}