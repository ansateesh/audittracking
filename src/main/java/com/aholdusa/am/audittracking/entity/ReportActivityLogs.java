package com.aholdusa.am.audittracking.entity;

public class ReportActivityLogs {
	
  static final long serialVersionUID = 1L;
	
     private ActivityLogs activityLog;
     private Employee     manager;
     private Employee     employee;
    
     
	public ActivityLogs getActivityLog() {
		return activityLog;
	}
	public void setActivityLog(ActivityLogs activityLog) {
		this.activityLog = activityLog;
	}
	public Employee getManager() {
		return manager;
	}
	public void setManager(Employee manager) {
		this.manager = manager;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	

}
