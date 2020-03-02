package com.aholdusa.am.audittracking.entity;

import com.google.gson.annotations.Expose;

public class AuditException {
	@Expose
	private boolean error=true;
	@Expose
	private String code;
	@Expose
	private String message;

	public AuditException() { } 

	public AuditException(String code, String message) {
		this.code = code;
		this.message = message;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
