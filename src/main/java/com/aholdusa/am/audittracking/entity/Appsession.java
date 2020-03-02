package com.aholdusa.am.audittracking.entity;

import com.google.gson.annotations.Expose;

/**
 * Entity for store. Extends AbstractEntity which provides common attributes
 * like id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class Appsession extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	@Expose
	@AmKey
	private Integer storeNumber;
	@Expose
	@AmKey
	private String appName;
	@Expose
	@AmKey
	private String appKey;

	public Appsession() {
		super();
	}

	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

}