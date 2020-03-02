package com.aholdusa.am.audittracking.controller;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//import com.mongodb.util.JSON;

import com.aholdusa.am.audittracking.entity.AmConstants;
import com.aholdusa.am.audittracking.entity.Employee;
import com.google.gson.Gson;

@RestController

public class GenericController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public void handlePost(final HttpServletRequest request,
			@RequestBody final Object entity, HttpServletResponse response) {
		logger.info("doAdmin entered");
		String operation = request.getParameter("operation");

		LinkedHashMap<String, LinkedHashMap> objList = (LinkedHashMap<String, LinkedHashMap>) entity;
		for (Entry<String, LinkedHashMap> s : objList.entrySet()) {
			Gson gson = new Gson();
			switch (AmConstants.valueOf(s.getKey())) {
			case Employee:
				Employee e = gson.fromJson(gson.toJson(s.getValue()), Employee.class); 
			}
		}
	}
	
}