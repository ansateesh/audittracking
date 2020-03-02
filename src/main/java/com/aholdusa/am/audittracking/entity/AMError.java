/**
 * 
 */
package com.aholdusa.am.audittracking.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nvrxm24
 *
 */
public class AMError {
	Logger logger = LoggerFactory.getLogger(this.getClass()); 
	private String errorMessage; 
	private Integer errorCode; 
	private String objectDescription; 
	/**
	 * 
	 */
	public AMError(Integer errorCode, String errorMessage, String objectDesc) {
		// TODO Auto-generated constructor stub
		this.setErrorCode(errorCode);
		this.setErrorMessage(errorMessage);
		this.setObjectDescription(objectDesc);
		logger.info(errorCode + " " + errorMessage + " " + objectDesc);
	}
	
	public AMError() {
		// TODO Auto-generated constructor stub
	}

	public String getErrorMessage(){
		return this.errorMessage; 
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage; 
	}
	
	public Integer getErrorCode(){
		return this.errorCode; 
	}
	
	public void setErrorCode(Integer errorCode){
		this.errorCode = errorCode; 
	}
	
	public String getObjectDescription() {
		return this.objectDescription; 
	}
	
	public void setObjectDescription(String desc) {
		this.objectDescription = desc; 
	}
	
	public String toString(){
		return "{\"errorCode\":\"" + this.getErrorCode() + 
				"\", \"errorMessage\":\"" + this.getErrorMessage() + 
				"\", \"object\":" + this.objectDescription + "}"; 
	}
}
