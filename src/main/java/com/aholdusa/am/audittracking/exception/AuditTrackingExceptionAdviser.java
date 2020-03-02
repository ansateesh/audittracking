package com.aholdusa.am.audittracking.exception;


import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.AuditException;
import com.aholdusa.am.audittracking.entity.ErrorMessages;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.MessageService;

@ControllerAdvice
public class AuditTrackingExceptionAdviser  extends ResponseEntityExceptionHandler{

	@Autowired
	private MessageService messageService;
	 
	@Autowired
	private ActivityLogService activityLogsService; 
	
	@Autowired
	ErrorMessages errorMessages;
	
	@Autowired
	Environment env;
	//java.sql.BatchUpdateException
	 @ExceptionHandler({SQLException.class , DataAccessException.class})
	  protected ResponseEntity<Object>handleSQLException(RuntimeException e, WebRequest request){
	        logger.info("SQLException Occured:: URL="+e.getMessage());
	        //SQLEXCEPTION_OCURRED
	        AuditException error = new AuditException(NextManagerConstants.SQLEXCEPTION_OCURRED_ERROR.getValue(),env.getProperty(NextManagerConstants.SQLEXCEPTION_OCURRED_ERROR.getValue()));
	    	HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
	    	return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	    }
	     
	 
	 
	 
	   

	@ExceptionHandler({ InvalidRequestException.class })
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		@SuppressWarnings("unused")
		InvalidRequestException ire = (InvalidRequestException) e;
		String errorDetails=ire.getMessage();

		// === >> CREATE AN ACT.LOG RECORD : RUNTIME_EXCEPTION DETAILS  <<===== //
		ActivityLogs activityLogForRunTimeExection =new ActivityLogs();
		activityLogForRunTimeExection.setActivityType(NextManagerConstants.RUN_TIME_EXCEPTION.getValue());
		activityLogForRunTimeExection.setEmpOpNum(3301);
		if(ire!=null && ire.getStoreNumber()!=null && ire.getStoreNumber()>0){
			activityLogForRunTimeExection.setStoreNumber( ire.getStoreNumber().intValue());
		}
		activityLogForRunTimeExection.setCreateDate( new Timestamp(System.currentTimeMillis())); 
		activityLogForRunTimeExection.setCreatedBy("ADMIN");
		activityLogForRunTimeExection.setMgrReason("RUNTIME EXCPTION ERROR");
		activityLogForRunTimeExection.setComments(e.getMessage());
		activityLogsService.insert(activityLogForRunTimeExection);


		//	AuditException error = new AuditException(messages.getMsgName(),messages.getMessage());
		AuditException error = new AuditException(errorDetails,env.getProperty(errorDetails));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

}
