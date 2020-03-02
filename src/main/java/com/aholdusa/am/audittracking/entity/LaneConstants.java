package com.aholdusa.am.audittracking.entity;

public enum LaneConstants {

	ADMIN_USER("AdminUser"),
	LANE_UPDATED("LaneUpdated");
	
	
	private String value;
	 
	private LaneConstants(String value) {
		this.value = value;
	}
 
	public String getValue() {
		return value;
	}
	
}
