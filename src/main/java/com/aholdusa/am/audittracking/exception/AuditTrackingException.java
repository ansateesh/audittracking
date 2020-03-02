package com.aholdusa.am.audittracking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Internal Server Error") 
public class AuditTrackingException extends RuntimeException {
	
	public AuditTrackingException(Exception ex) {
		
	}
	
	public AuditTrackingException(String message){
	     super(message);
	  }
	
}
