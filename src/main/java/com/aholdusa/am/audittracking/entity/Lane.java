package com.aholdusa.am.audittracking.entity;

import java.math.BigDecimal;

import com.google.gson.annotations.Expose;

/**
 * Entity for Lane. Extends AbstractEntity which provides common attributes like
 * id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class Lane extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	@Expose
	@AmKey
	private Integer number;
	@Expose
	@AmKey
	private Boolean active = null;
	@Expose
	@AmKey
	private String laneType;
	@AmKey
	private Integer storeNumber;
    /*
	 *@author nvixa3
	 * add DEFAULT_TILL_AMOUNT column 
	 */
    private Double defaultTillAmount;
    
    private Long storeId;
    
    private Double overShortAmount;
    
    private Boolean reportDeleted;
    
	// private Set<LaneHistory> laneHistory = new HashSet<LaneHistory>();

    public void setReportDeleted(final boolean reportDeleted){
  	  this.reportDeleted = reportDeleted;  
    }
    
    
    public boolean getReportDeleted(){
  	  return (this.reportDeleted == null)? false:this.reportDeleted;
    }
    
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Double getDefaultTillAmount() {
		return defaultTillAmount;
	}

	public void setDefaultTillAmount(Double defaultTillAmount) {
		this.defaultTillAmount = defaultTillAmount;
	}

	public Lane() {
		super();
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer laneNumber) {
		this.number = laneNumber;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getType() {
		return laneType;
	}

	public void setType(String type) {
		this.laneType = type;
	}

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer store) {
		this.storeNumber = store;
	}

	public Double getOverShortAmount() {
		return overShortAmount;
	}

	public void setOverShortAmount(Double overShortAmount) {
		this.overShortAmount = overShortAmount;
	}

	// public Set<LaneHistory> getLaneHistory() {
	// return laneHistory;
	// }
	//
	// public void setLaneHistory(Set<LaneHistory> laneHistory) {
	// this.laneHistory = laneHistory;
	// }

}