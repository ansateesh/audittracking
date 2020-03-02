 package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class LoggingFilter extends OncePerRequestFilter {

    protected static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_PREFIX = "Request: ";
    private static final String RESPONSE_PREFIX = "Response: ";
    private AtomicLong id = new AtomicLong(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if(logger.isDebugEnabled() || logger.isInfoEnabled()){
            long requestId = id.incrementAndGet();
            request = new RequestWrapper(requestId, request);
            response = new ResponseWrapper(requestId, response);
        }
        try {
            filterChain.doFilter(request, response);
//            response.flushBuffer();
        }
        finally {
            if(logger.isDebugEnabled() || logger.isInfoEnabled()){
                logRequest(request);
                logResponse((ResponseWrapper)response);
            }
        }

    }

    private void logRequest(final HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append(REQUEST_PREFIX);
        if(request instanceof RequestWrapper){
            msg.append("request id=").append(((RequestWrapper)request).getId()).append("; ");
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            msg.append("session id=").append(session.getId()).append("; ");
        }
        if(request.getContentType() != null) {
            msg.append("content type=").append(request.getContentType()).append("; ");
        }
        msg.append("uri=").append(request.getRequestURI());
        if(request.getQueryString() != null) {
            msg.append('?').append(request.getQueryString());
        }

        if(request instanceof RequestWrapper && !isMultipart(request)){
            RequestWrapper requestWrapper = (RequestWrapper) request;
            try {
                String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() :
                        "UTF-8";
                msg.append("; payload=").append(new String(requestWrapper.toByteArray(), charEncoding));
            } catch (UnsupportedEncodingException e) {
                logger.info("Failed to parse request payload", e);
            }

        }
        setJsonRequest(msg);
        logger.info(" <<<<<<< REQUEST LOGGER FOR REDIRECTING TO ORACLE >>>>>>"+msg.toString());
    }

    private boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType()!=null && request.getContentType().startsWith("multipart/form-data");
    }

    private void logResponse(final ResponseWrapper response) {
        StringBuilder msg = new StringBuilder();
        msg.append(RESPONSE_PREFIX);
        msg.append("request id=").append((response.getId()));
        try {
            msg.append("; payload=").append(new String(response.toByteArray(), response.getCharacterEncoding()));
        } catch (UnsupportedEncodingException e) {
            logger.warn("Failed to parse response payload", e);
        }
        logger.info(msg.toString());
    }

    private void setJsonRequest(StringBuilder msg){
    	String strRequest=msg.toString();
    	String jsonRequest="{}";
    	
    	if(strRequest!=null && !strRequest.equals("")){
    		System.out.println("StringTokenizer Example: \n");
    		StringTokenizer st = new StringTokenizer(strRequest, ";");
    		while (st.hasMoreElements()) {
    			 String strRequestElement=st.nextElement().toString();
    			 if(strRequestElement.contains("payload=")){
    				 jsonRequest=strRequestElement.substring(strRequestElement.indexOf("{"),strRequestElement.length()) ;
    			 }
    		}
    	}
    	 logger.info("<<<<<<<<<<<<<<JSON REQUEST >>>>>>>>>>>>"+jsonRequest);
    }
}
