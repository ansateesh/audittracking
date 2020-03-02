package com.aholdusa.am.audittracking.entity;

import com.google.gson.annotations.Expose;

/**
 * @author israel
 *
 */
public class NextmanagerEvents extends AbstractEntity {


	private static final long serialVersionUID = 1L;

	@Expose
	@AmKey
	private String eventType;
	@Expose
	@AmKey
	private Integer description;
	@Expose
	@AmKey
	private Integer empOpNum;
	@Expose
	@AmKey
	private Integer storeNumber;
	@Expose
	@AmKey
	private String mgrReason;


	public String getMgrReason() {
		return mgrReason;
	}
	public void setMgrReason(String mgrReason) {
		this.mgrReason = mgrReason;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Integer getDescription() {
		return description;
	}
	public void setDescription(Integer description) {
		this.description = description;
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




}
