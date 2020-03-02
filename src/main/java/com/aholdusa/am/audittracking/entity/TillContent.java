package com.aholdusa.am.audittracking.entity;

import java.math.BigDecimal;

import com.google.gson.annotations.Expose;

public class TillContent {
	//{AmtCash=3.99, AmtCheck=0.0, AmtFoods=0.0, AmtMisc1=0.0, AmtMisc2=0.0, AmtMisc3=0.0, AmtManuf=0.0, AmtStore=0.0}
	@Expose
	@AmKey
	private BigDecimal amtCash;
	@Expose
	@AmKey
	private BigDecimal amtCheck;
	@Expose
	@AmKey
	private BigDecimal amtFoods;
	@Expose
	@AmKey
	private BigDecimal amtMisc1;
	@Expose
	@AmKey
	private BigDecimal amtMisc2;
	@Expose
	@AmKey
	private BigDecimal amtManuf;
	@Expose
	@AmKey
	private BigDecimal grossPositive;
	@Expose
	@AmKey
	private BigDecimal grossNegative;
	
	public BigDecimal getGrossPositive() {
		return grossPositive;
	}
	public void setGrossPositive(BigDecimal grossPositive) {
		this.grossPositive = grossPositive;
	}
	public BigDecimal getGrossNegative() {
		return grossNegative;
	}
	public void setGrossNegative(BigDecimal grossNegative) {
		this.grossNegative = grossNegative;
	}

	
	public BigDecimal getAmtCash() {
		return amtCash;
	}
	public void setAmtCash(BigDecimal amtCash) {
		this.amtCash = amtCash;
	}
	public BigDecimal getAmtCheck() {
		return amtCheck;
	}
	public void setAmtCheck(BigDecimal amtCheck) {
		this.amtCheck = amtCheck;
	}
	public BigDecimal getAmtFoods() {
		return amtFoods;
	}
	public void setAmtFoods(BigDecimal amtFoods) {
		this.amtFoods = amtFoods;
	}
	public BigDecimal getAmtMisc1() {
		return amtMisc1;
	}
	public void setAmtMisc1(BigDecimal amtMisc1) {
		this.amtMisc1 = amtMisc1;
	}
	public BigDecimal getAmtMisc2() {
		return amtMisc2;
	}
	public void setAmtMisc2(BigDecimal amtMisc2) {
		this.amtMisc2 = amtMisc2;
	}
	public BigDecimal getAmtManuf() {
		return amtManuf;
	}
	public void setAmtManuf(BigDecimal amtManuf) {
		this.amtManuf = amtManuf;
	}
	
	

}
