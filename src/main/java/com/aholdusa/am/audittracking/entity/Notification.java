package com.aholdusa.am.audittracking.entity;

public class Notification {
	
	private Long activityId;
	private Long storeNumber;
	private Long terminalId;
	private String message;
	private Long operatorNumber;
	private String activityType;
	
	
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	public Long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getOperatorNumber() {
		return operatorNumber;
	}
	public void setOperatorNumber(Long operatorNumber) {
		this.operatorNumber = operatorNumber;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	
	
	

}
