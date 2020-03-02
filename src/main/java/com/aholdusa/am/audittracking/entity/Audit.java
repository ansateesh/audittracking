package com.aholdusa.am.audittracking.entity;

import java.sql.Date;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

/**
 * Entity for audits and lane position. Extends AbstractEntity which provides
 * common attributes like id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class Audit extends AbstractEntity {
	private static final long serialVersionUID = 1L;
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
	private Timestamp date;

	@Expose
	@AmKey
	private Timestamp startTime;

	@Expose
	@AmKey
	private Double cashPosition;
	@Expose
	@AmKey
	private Double cashExpected;
	@Expose
	@AmKey
	private Double cashActual;
	@Expose
	@AmKey
	private Double cashDifference;
	@Expose
	@AmKey
	private Double cashOverUnder;
	@Expose
	@AmKey
	private Double checkPosition;
	@Expose
	@AmKey
	private Double checkExpected;
	@Expose
	@AmKey
	private Double checkActual;
	@Expose
	@AmKey
	private Double checkDifference;
	@Expose
	@AmKey
	private Double checkOverUnder;
	@Expose
	@AmKey
	private Double cashLoan;
	public Double getCashLoan() {
		return cashLoan;
	}

	public void setCashLoan(Double cashLoan) {
		this.cashLoan = cashLoan;
	}

	@Expose
	private String notes;
	@Expose
	private String slip;
	@Expose
	private String empSignature;
	@Expose
	private String mgrSignature;
	@Expose
	private Integer position;

	public Audit() {
		super();
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
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

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Double getCashPosition() {
		return cashPosition;
	}

	public void setCashPosition(Double cashPosition) {
		this.cashPosition = cashPosition;
	}

	public Double getCashExpected() {
		return cashExpected;
	}

	public void setCashExpected(Double cashExpected) {
		this.cashExpected = cashExpected;
	}

	public Double getCashActual() {
		return cashActual;
	}

	public void setCashActual(Double cashActual) {
		this.cashActual = cashActual;
	}

	public Double getCashDifference() {
		return cashDifference;
	}

	public void setCashDifference(Double cashDifference) {
		this.cashDifference = cashDifference;
	}

	public Double getCashOverUnder() {
		return cashOverUnder;
	}

	public void setCashOverUnder(Double cashOverUnder) {
		this.cashOverUnder = cashOverUnder;
	}

	public Double getCheckPosition() {
		return checkPosition;
	}

	public void setCheckPosition(Double checkPosition) {
		this.checkPosition = checkPosition;
	}

	public Double getCheckExpected() {
		return checkExpected;
	}

	public void setCheckExpected(Double checkExpected) {
		this.checkExpected = checkExpected;
	}

	public Double getCheckActual() {
		return checkActual;
	}

	public void setCheckActual(Double checkActual) {
		this.checkActual = checkActual;
	}

	public Double getCheckDifference() {
		return checkDifference;
	}

	public void setCheckDifference(Double checkDifference) {
		this.checkDifference = checkDifference;
	}

	public Double getCheckOverUnder() {
		return checkOverUnder;
	}

	public void setCheckOverUnder(Double checkOverUnder) {
		this.checkOverUnder = checkOverUnder;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSlip() {
		return slip;
	}

	public void setSlip(String slip) {
		this.slip = slip;
	}

	public String getEmpSignature() {
		return empSignature;
	}

	public void setEmpSignature(String empSignature) {
		this.empSignature = empSignature;
	}

	public String getMgrSignature() {
		return mgrSignature;
	}

	public void setMgrSignature(String mgrSignature) {
		this.mgrSignature = mgrSignature;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	// public <T> T getObjectFromJson(LinkedHashMap jsonMap) {
	// // TODO Auto-generated method stub
	// return null;
	// }

}