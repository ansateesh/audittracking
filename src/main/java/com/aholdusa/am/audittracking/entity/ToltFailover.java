package com.aholdusa.am.audittracking.entity;

public class ToltFailover {
	
	private Long recievedDifference;
	private boolean isFailover;
	
	public Long getRecievedDifference() {
		return recievedDifference;
	}
	public void setRecievedDifference(Long recievedDifference) {
		this.recievedDifference = recievedDifference;
	}
	public boolean getFailover() {
		return isFailover;
	}
	public void setFailover(boolean isFailover) {
		this.isFailover = isFailover;
	}
	

}
