package com.aholdusa.am.audittracking.dto;

 

import com.aholdusa.am.audittracking.entity.Lane;

public class OverShortReportDTO {
	private Long    id;
	private Long    storeNumber;
	private String  reportDate;
	private Double  overShortAmount;
	private Boolean failover= false;
	private Boolean reportDeleted=false;
	private Lane    lane;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	 
	public Double getOverShortAmount() {
		return overShortAmount;
	}
	public void setOverShortAmount(Double overShortAmount) {
		this.overShortAmount = overShortAmount;
	}
	public Boolean getFailover() {
		return failover;
	}
	public void setFailover(Boolean failover) {
		this.failover = failover;
	}
	public Boolean getReportDeleted() {
		return reportDeleted;
	}
	public void setReportDeleted(Boolean reportDeleted) {
		this.reportDeleted = reportDeleted;
	}
	public Lane getLane() {
		return lane;
	}
	public void setLane(Lane lane) {
		this.lane = lane;
	}


}
