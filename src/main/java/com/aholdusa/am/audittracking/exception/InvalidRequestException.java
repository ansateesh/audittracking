package com.aholdusa.am.audittracking.exception;

import org.springframework.validation.Errors;
@SuppressWarnings("serial")
public class InvalidRequestException extends RuntimeException{
	private Errors errors;
	private Long   storeNumber;

	

	public InvalidRequestException(String message, Errors errors) {
		super(message);
		this.errors = errors;

	}
	
	/**
	 * @param message
	 * @param errors
	 * @param storeNumber
	 */
	public InvalidRequestException(String message, Errors errors,Long storeNumber) {
		super(message);
		this.errors = errors;
		this.storeNumber=storeNumber;

	}

	public Errors getErrors() { return errors; }
	public Long getStoreNumber() {return storeNumber;}
}
