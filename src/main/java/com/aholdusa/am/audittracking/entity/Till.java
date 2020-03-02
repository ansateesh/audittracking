package com.aholdusa.am.audittracking.entity;

import java.math.BigDecimal;
import java.sql.Date;

import com.google.gson.annotations.Expose;

/**
 * Entity for Lane. Extends AbstractEntity which provides common attributes like
 * id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class Till extends AbstractEntity {

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}

	 

	public BigDecimal getPennies() {
		return pennies;
	}

	public void setPennies(BigDecimal pennies) {
		this.pennies = pennies;
	}

	public BigDecimal getNickels() {
		return nickels;
	}

	public void setNickels(BigDecimal nickels) {
		this.nickels = nickels;
	}

	public BigDecimal getDimes() {
		return dimes;
	}

	public void setDimes(BigDecimal dimes) {
		this.dimes = dimes;
	}

	public BigDecimal getQuarters() {
		return quarters;
	}

	public void setQuarters(BigDecimal quarters) {
		this.quarters = quarters;
	}

	public BigDecimal getOnes() {
		return ones;
	}

	public void setOnes(BigDecimal ones) {
		this.ones = ones;
	}

	public BigDecimal getFives() {
		return fives;
	}

	public void setFives(BigDecimal fives) {
		this.fives = fives;
	}

	public BigDecimal getTens() {
		return tens;
	}

	public void setTens(BigDecimal tens) {
		this.tens = tens;
	}

	public BigDecimal getTwenties() {
		return twenties;
	}

	public void setTwenties(BigDecimal twenties) {
		this.twenties = twenties;
	}

	public BigDecimal getFifties() {
		return fifties;
	}

	public void setFifties(BigDecimal fifties) {
		this.fifties = fifties;
	}

	public BigDecimal getHundreds() {
		return hundreds;
	}

	public void setHundreds(BigDecimal hundreds) {
		this.hundreds = hundreds;
	}
	
	public BigDecimal getLoans() {
		return loans;
	}

	public void setLoans(BigDecimal loans) {
		this.loans = loans;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getStamps() {
		return stamps;
	}

	public void setStamps(BigDecimal stamps) {
		this.stamps = stamps;
	}

	public String getMgrSignature() {
		return mgrSignature;
	}

	public void setMgrSignature(String mgrSignature) {
		this.mgrSignature = mgrSignature;
	}

	public java.sql.Timestamp getDate() {
		return date;
	}

	public void setDate(java.sql.Timestamp date) {
		this.date = date;
	}
	
	private static final long serialVersionUID = 1L;

	@Expose
	@AmKey
	private Integer storeNumber;
	
	@Expose
	@AmKey
	private java.sql.Timestamp date;
	 
	@Expose
	@AmKey
	private BigDecimal pennies;
	@Expose
	@AmKey
	private BigDecimal nickels;
	@Expose
	@AmKey
	private BigDecimal dimes;
	@Expose
	@AmKey
	private BigDecimal quarters;
	@Expose
	@AmKey
	private BigDecimal ones;
	@Expose
	@AmKey
	private BigDecimal fives;
	@Expose
	@AmKey
	private BigDecimal tens;
	@Expose
	@AmKey
	private BigDecimal twenties;
	@Expose
	@AmKey
	private BigDecimal fifties;
	@Expose
	@AmKey
	private BigDecimal hundreds;
	@Expose
	@AmKey
	private BigDecimal loans;
	@Expose
	@AmKey
	private BigDecimal total;
	@Expose
	private BigDecimal stamps;
	@Expose
	private String mgrSignature;

	// private Set<LaneHistory> laneHistory = new HashSet<LaneHistory>();

	public Till() {
		super();
	}

	

}