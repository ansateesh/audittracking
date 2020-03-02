package com.aholdusa.am.audittracking.entity;

public class LaneType {

	private Integer laneNumber;
	private String  type;
	private Integer storeNumber;
	private String  storeDivision;
	private String  maker;
	private String  paymentType;
	private String  session;

	public Integer getLaneNumber() {
		return laneNumber;
	}
	public void setLaneNumber(Integer laneNumber) {
		this.laneNumber = laneNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getMaker() {
		return maker;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	public String getStoreDivision() {
		return storeDivision;
	}
	public void setStoreDivision(String storeDivision) {
		this.storeDivision = storeDivision;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}

}
