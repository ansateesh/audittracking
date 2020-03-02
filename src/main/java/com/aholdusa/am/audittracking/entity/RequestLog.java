package com.aholdusa.am.audittracking.entity;

import java.sql.Timestamp;

public class RequestLog extends AbstractEntity{
	 
    
    private String 	  requestPayLoad;
    private String 	  responsePayLoad;
    private Timestamp requestDate;
     
    private String    uri;
    
    public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public RequestLog(){}
    
	public RequestLog(String requestPayLoad, String responsePayLoad,
			Timestamp requestDate, boolean isDeleted) {
		super();
		this.requestPayLoad = requestPayLoad;
		this.responsePayLoad = responsePayLoad;
		this.requestDate = requestDate;
		//isDeleted = isDeleted;
	}
	
	public String getRequestPayLoad() {
		return requestPayLoad;
	}
	public void setRequestPayLoad(String requestPayLoad) {
		this.requestPayLoad = requestPayLoad;
	}
	public String getResponsePayLoad() {
		return responsePayLoad;
	}
	public void setResponsePayLoad(String responsePayLoad) {
		this.responsePayLoad = responsePayLoad;
	}
	public Timestamp getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}
	 
    
    
    
}
