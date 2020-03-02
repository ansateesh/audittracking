package com.aholdusa.am.audittracking.entity;

import com.google.gson.annotations.Expose;

/**
 * Entity for Lane. Extends AbstractEntity which provides common attributes like
 * id,createdBy for use in this class.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.entity.AbstractEntity.java
 */
public class Messages extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	@Expose
	@AmKey
	private String msgName;
	@Expose
	@AmKey
	private String message;
	@Expose
	@AmKey
	private String msgLanguage;

	@Expose
	@AmKey
	private String messageType;
	// private Set<LaneHistory> laneHistory = new HashSet<LaneHistory>();

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Messages() {
		super();
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsgLanguage() {
		return msgLanguage;
	}

	public void setMsgLanguage(String msgLanguage) {
		this.msgLanguage = msgLanguage;
	}

}