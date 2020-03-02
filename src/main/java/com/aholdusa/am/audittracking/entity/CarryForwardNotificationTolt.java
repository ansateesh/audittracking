package com.aholdusa.am.audittracking.entity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class CarryForwardNotificationTolt {

 
	private String 	  uuid;
	private String 	  messageType;
	private String    storeNumber;
	private String    dateTime;
	private String                 storeNetTotal;
	private Map<String,String> terminals;
	
	
	public Map<String, String> getTerminals() {
		return terminals;
	}
	public void setTerminals(Map<String, String> terminals) {
		this.terminals = terminals;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uudi) {
		this.uuid = uudi;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getStoreNetTotal() {
		return storeNetTotal;
	}
	public void setStoreNetTotal(String storeNetTotal) {
		this.storeNetTotal = storeNetTotal;
	}
	 



}
