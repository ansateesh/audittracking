package com.aholdusa.am.audittracking.entity;

public enum ReportConstants {

	REPORT_TYPE_AUDIT("Audit"),
	REPORT_TYPE_OVERRIDE("override"),
	REPORT_TYPE_MARK("Mark"),
	REPORT_TYPE_UNMARK("Unmark"),
	REPORT_TYPE_DESELECT("Deselect"),
	REPORT_ACTIVITY_TYPE_OVERRIDE("Override"),
	REPORT_ACTIVITY_OBJECT_TYPE_EMP("Employee");
	
	private String value;
	 
	private ReportConstants(String value) {
		this.value = value;
	}
 
	public String getValue() {
		return value;
	}
}
