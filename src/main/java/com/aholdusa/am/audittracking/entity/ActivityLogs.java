package com.aholdusa.am.audittracking.entity;

import java.sql.Date;

import com.google.gson.annotations.Expose;

/**
 * Entity for Lane. Extends AbstractEntity which provides common attributes like
 * id,createdBy for use in this class.
 * 
 * @author x1udxk1
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class ActivityLogs extends AbstractEntity {


	@Expose
	@AmKey
	private String activityType;
	@Expose
	@AmKey
	private String mgrSignature;
	@Expose
	@AmKey
	private String mgrReason;
	@Expose
	@AmKey
	private Integer mgrOpNum;
	@Expose
	@AmKey
	private Integer empOpNum;
	@Expose
	@AmKey
	private Integer storeNumber;
	@Expose
	@AmKey
	private java.sql.Timestamp timeStamp;
	@Expose
	@AmKey
	private String objectType;
	@Expose
	@AmKey
	private String comments;
 
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ActivityLogs() {
		super();
	}
	
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getMgrSignature() {
		return mgrSignature;
	}

	public void setMgrSignature(String mgrSignature) {
		this.mgrSignature = mgrSignature;
	}

	public String getMgrReason() {
		return mgrReason;
	}

	public void setMgrReason(String mgrReason) {
		this.mgrReason = mgrReason;
	}

	public Integer getMgrOpNum() {
		return mgrOpNum;
	}

	public void setMgrOpNum(Integer mgrOpNum) {
		this.mgrOpNum = mgrOpNum;
	}

	public Integer getEmpOpNum() {
		return empOpNum;
	}

	public void setEmpOpNum(Integer empOpNum) {
		this.empOpNum = empOpNum;
	}

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}

	public java.sql.Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(java.sql.Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}



	public String getObjectType() {
		return objectType;
	}



	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

 	
	
}