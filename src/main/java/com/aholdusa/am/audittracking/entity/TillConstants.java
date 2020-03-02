package com.aholdusa.am.audittracking.entity;

public enum TillConstants {

	CHANGE_DRAWER_UPDATED("ChangeDrawerAudited"),
	MANAGER_REASON("UpdatingDrawer"),
	ADMIN_USER("AdminUser");
	
	private String value;
	 
	private TillConstants(String value) {
		this.value = value;
	}
 
	public String getValue() {
		return value;
	}
}
