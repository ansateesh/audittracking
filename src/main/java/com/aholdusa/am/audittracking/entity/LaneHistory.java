package com.aholdusa.am.audittracking.entity;

import java.sql.Date;
import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

/**
 * Entity for lane history. Extends AbstractEntity which provides common
 * attributes like id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class LaneHistory extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	// private Integer laneNumber;
	// private Integer operatorNumber;
	@Expose
	@AmKey
	private Timestamp laneAssignmentDate;
	@Expose
	@AmKey
	private Timestamp startTime;
	@Expose
	@AmKey
	private Timestamp endTime;
	@Expose
	@AmKey
	private Integer position;
	@Expose
	@AmKey
	private Double overUnder;
	@Expose
	@AmKey
	private Lane lane;
	@Expose
	@AmKey
	private Employee employee;
	@Expose
	@AmKey
	private Integer storeNumber;

	@Expose
	@AmKey
	private Boolean retain = false;

	@Expose
	@AmKey
	private Boolean failover = false;
	

	public Boolean getFailover() {
		return failover;
	}

	public void setFailover(Boolean failover) {
		this.failover = failover;
	}

	public LaneHistory(Lane lane, Employee employee, LaneHistory laneHistory) {
		super();
		this.setId(laneHistory.getId());
		this.setLaneAssignmentDate(laneHistory.getLaneAssignmentDate());
		this.setStoreNumber(laneHistory.getStoreNumber());
		this.setStartTime(laneHistory.getStartTime());
		this.setEndTime(laneHistory.getEndTime());
		this.setVersion(laneHistory.getVersion());
		this.setCreatedBy(laneHistory.getCreatedBy());
		this.setCreateDate(laneHistory.getCreateDate());
		this.setLastModifiedBy(laneHistory.getLastModifiedBy());
		this.setLastModifiedDate(laneHistory.getLastModifiedDate());
		this.setIsDeleted(laneHistory.getIsDeleted());
		this.setLane(lane);
		this.setEmployee(employee);
	}

	public LaneHistory(Long id, Integer storeNumber, Timestamp date, Timestamp startTime, Timestamp endTime, Long version,
			String createdBy, Timestamp createdDate, String lastModifiedBy, Timestamp lastModifiedDate,
			Boolean isDeleted, Lane lane, Employee employee) {
		super();
		this.setId(id);
		this.setLaneAssignmentDate(date);
		this.setStoreNumber(storeNumber);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setVersion(version);
		this.setCreatedBy(createdBy);
		this.setCreateDate(createdDate);
		this.setLastModifiedBy(lastModifiedBy);
		this.setLastModifiedDate(lastModifiedDate);
		this.setIsDeleted(isDeleted);
		this.setLane(lane);
		this.setEmployee(employee);
	}

	public LaneHistory(Integer laneNumber, Integer operatorNumber, Long id, Integer storeNumber, Timestamp date,
			Timestamp startTime, Timestamp endTime, Long version, String createdBy, Timestamp createdDate, String lastModifiedBy,
			Timestamp lastModifiedDate, Boolean isDeleted, Lane lane, Employee employee) {
		super();
		Lane l = new Lane();
		l.setNumber(laneNumber);
		l.setStoreNumber(storeNumber);
		Employee e = new Employee();
		e.setOperatorNumber(operatorNumber);
		e.setStoreNumber(storeNumber);
		this.setId(id);
		this.setLaneAssignmentDate(date);
		this.setStoreNumber(storeNumber);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setVersion(version);
		this.setCreatedBy(createdBy);
		this.setCreateDate(createdDate);
		this.setLastModifiedBy(lastModifiedBy);
		this.setLastModifiedDate(lastModifiedDate);
		this.setIsDeleted(isDeleted);
		this.setLane(l);
		this.setEmployee(e);
	}

	public LaneHistory() {
		super();
	}

	public Timestamp getLaneAssignmentDate() {
		return laneAssignmentDate;
	}

	public void setLaneAssignmentDate(Timestamp laneAssignmentDate) {
		this.laneAssignmentDate = laneAssignmentDate;
	}

 

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Double getOverUnder() {
		return overUnder;
	}

	public void setOverUnder(Double overUnder) {
		this.overUnder = overUnder;
	}

	public Lane getLane() {
		return lane;
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer store) {
		this.storeNumber = store;
	}

	// public void setOperatorNumber(Integer operatorNumber){
	// this.operatorNumber = operatorNumber;
	// }
	// public void setLaneNumber(Integer laneNumber){
	// this.laneNumber = laneNumber;
	// }
	// public Integer getOperatorNumber(){
	// return this.operatorNumber;
	// }
	// public Integer getLaneNumber(){
	// return this.laneNumber;
	// }

	public String toString() {
		String objString = "";
		objString = "Lane:" + (this.getLane() == null ? "" : this.getLane().toString());
		// objString += ", laneNumber: " + this.getLaneNumber();
		// objString += ", operatorNumber: " + this.getOperatorNumber();
		objString += ", id:" + this.getId();
		objString += ", date:" + this.getLaneAssignmentDate();
		objString += ",startTime:" + this.getStartTime();
		objString += ",endTime:" + this.getEndTime();
		objString += ",position:" + this.getPosition();
		objString += ",overUnder" + this.getOverUnder();
		objString += ",employee:" + (this.getEmployee() == null ? "" : this.getEmployee().toString());
		objString += ",store:" + this.getStoreNumber();
		return objString;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Boolean getRetain() {
		return retain;
	}

	public void setRetain(Boolean retain) {
		this.retain = retain;
	}
}