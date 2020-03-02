package com.aholdusa.am.audittracking.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class SessionRequestContoller {
	@Autowired(required=true)
	private HttpServletRequest request;
	private   final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Pointcut("execution(* com.aholdusa.am.audittracking.controller.*.*(..))")
    public void controllerPointCut() { }

    //@Around("controllerPointCut()")
	@After("controllerPointCut()")
	// @Before("execution(* com.aholdusa.am.audittracking.controller.AdminController.*(..))")
	//@Before("execution(void set*(*))")
	public void beforeControllerExecute() throws Exception {
	    HttpServletRequest requestLocal = ((ServletRequestAttributes) RequestContextHolder
	    			.getRequestAttributes()).getRequest();;
		//logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  More URI >>>>>>>>>>>>>>>>>>>>>>>>>"+IOUtils.toString(requestLocal.getInputStream()));
	}

}
