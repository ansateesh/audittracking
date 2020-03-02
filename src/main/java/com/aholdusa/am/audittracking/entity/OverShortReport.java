package com.aholdusa.am.audittracking.entity;
 
import java.sql.Timestamp;

public class OverShortReport  extends AbstractEntity{

	private static final long serialVersionUID = -6027051845729232725L;
	private Long laneId;
	private Long storeNumber;
	private Timestamp reportDate;
	private Double overShortAmount;
	private Boolean failover= false;
	private Boolean reportDeleted=false;


	public Boolean getReportDeleted() {
		return reportDeleted;
	}
	public void setReportDeleted(Boolean reportDeleted) {
		this.reportDeleted = reportDeleted;
	}
	public Long getLaneId() {
		return laneId;
	}
	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public Timestamp getReportDate() {
		return reportDate;
	}
	public void setReportDate(Timestamp reportDate) {
		this.reportDate = reportDate;
	}
 
	public Boolean getFailover() {
		return failover;
	}
	public void setFailover(Boolean failover) {
		this.failover = failover;
	}
    
	public Double getOverShortAmount() {
		return overShortAmount;
	}
	public void setOverShortAmount(Double overShortAmount) {
		this.overShortAmount = overShortAmount;
	}

}
