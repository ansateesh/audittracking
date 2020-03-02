package com.aholdusa.am.audittracking.entity;

public enum AudittrackingConstants {
 
	STORE_NUMBER_COL_NAME("storeNumber"), 
	LANE_NUMBER_COL_NAME("laneNumber");

	private String value;
 
	private AudittrackingConstants(String value) {
		this.value = value;
	}
 
	public String getValue() {
		return value;
	}


}
