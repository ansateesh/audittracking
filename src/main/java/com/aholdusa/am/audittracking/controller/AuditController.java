package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.exception.InvalidRequestException;
import com.aholdusa.am.audittracking.service.AMService;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.AuditService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.util.AMUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Controller for Audits. Uses the abstract controller methods to handle the
 * CRUD/Fetch operations on audits and lane positions.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.controller.AbstractController.java
 */
@Controller
@RequestMapping("audit")
public class AuditController extends AMController {
	private Logger logger = LoggerFactory.getLogger(Audit.class);

	@Autowired
	private AppsessionService appsessionService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private ActivityLogService activityLogsService; 
	@Autowired 
	private EmployeeService employeeService; 

	public HashMap<String, HashMap<String, String>> parseRequest(
			LinkedHashMap<String, Object> entity) {
		HashMap<String, HashMap<String, String>> retMap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> hm = new HashMap<String, String>();
		HashMap<String, String> objectMap = new HashMap<String, String>();
		String objectType = "";
		Gson gson = new Gson();
		String returnString = "";
		Integer storeNumber = null;
		String appkey = null;
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (entry.getValue() == null) {
				logger.error("entry null: " + entry.getKey());
				continue;
			}
			String entryType = entry.getValue().getClass().getCanonicalName();
			logger.info("parsing: " + entryType);
			switch (entryType) {
			case "java.util.LinkedHashMap": {
				logger.info("LinkedHashMap");
				objectType = entry.getKey();
				for (Object o : ((LinkedHashMap<String, String>) entry
						.getValue()).entrySet().toArray()) {
					// logger.info("hashmap content .. " + o.toString());
					String arr[] = o.toString().split("=");

					hm.put(arr[0], arr[1]);
				}
				objectMap.put(objectType, gson.toJson(entry.getValue()));
			}
				break;
			case "java.lang.String": {
				logger.info("string");
				if (entry.getKey().equalsIgnoreCase("UUID")) {
					appkey = (String) entry.getValue();
					Appsession appsession = null;
					List<Appsession> sessionList = this.appsessionService
							.findByKey(appkey);
					if (sessionList != null && sessionList.size() > 0)
						appsession = this.appsessionService.findByKey(appkey)
								.get(0);
					if (appsession == null
							|| appsession.getStoreNumber() == null
							|| appsession.getStoreNumber() <= 0)
						returnString = "{\"errorMessage\":\"key mismatch\"}";
					storeNumber = appsession.getStoreNumber();
				}
			}
			case "java.lang.Boolean":
				logger.info("boolean");
			default:
				logger.info("default");
				hm.put(entry.getKey(), entry.getValue().toString());
				break;
			}
			logger.info("entry: " + entry.getKey() + " entry type: "
					+ entry.getValue().toString());

		}
		hm.put("returnString", returnString);
		logger.info("storeNumber:" + storeNumber);
		logger.info("returnString:" + returnString);
		if (storeNumber != null) {
			hm.put("storeNumber", storeNumber.toString());
		}
		if (appkey != null) {
			hm.put("appkey", appkey);
		}
		retMap.put("hm", hm);
		retMap.put("objectMap", objectMap);
		logger.info("parsed map:" + retMap.toString());

		return retMap;
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void handlePost(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response,
			BindingResult bindingResult) {
		String returnString = "{\"errorMessage\":\"success\"}";

		String appkey = null;
		String objectType = null;
		Object object = null;
		Integer storeNumber = null;
		// Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
		// .create();
		// Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create() ;
	 
		   GsonBuilder gBuilder = new  GsonBuilder();

		   // deserialize TimeStamp inside Json Object and set correct date format
		   gBuilder.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() { 
			   @Override  
			   public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException 
			   {
				   String timeToString =json.getAsJsonPrimitive().toString();
				   Timestamp timestampFormatted =null;
				   // Fixes for incorrect date format FE 
				   if(timeToString.length()<=12){
					   timeToString+=" 00:00:00.000";
					   timeToString=timeToString.replace("\"","");
					   timestampFormatted =AMUtils.getTimestampFromString(timeToString);
				   }else{
					   SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");
					   java.util.Date parsedDate;
					   try {
						   timeToString=timeToString.replace("\"","");
						   parsedDate = dateFormat.parse(timeToString);

						   timestampFormatted = new java.sql.Timestamp(parsedDate.getTime());
					   } catch (ParseException e) {
						   e.printStackTrace();
					   }
				   }
				   return	timestampFormatted;

			   }
		   });		 
			
			 
			Gson gson =  gBuilder.create();	
		 
		//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		HashMap<String, String> hm = new HashMap<String, String>();
		HashMap<String, String> objectMap = new HashMap<String, String>();
		HashMap<String, Object> object2ObjectMap = new HashMap<String, Object>();
		
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (entry.getValue() == null) {
				logger.error("entry null: " + entry.getKey());
				continue;
			}
			String entryType = entry.getValue().getClass().getCanonicalName();
			logger.info("parsing: " + entryType);
			switch (entryType) {
			case "java.util.LinkedHashMap": {
				logger.info("LinkedHashMap");
				objectType = entry.getKey();
				for (Object o : ((LinkedHashMap<String, String>) entry
						.getValue()).entrySet().toArray()) {
					// logger.info("hashmap content .. " + o.toString());
					String arr[] = o.toString().split("=");

					hm.put(arr[0], arr[1]);
				}
				objectMap.put(objectType, gson.toJson(entry.getValue()));
				object2ObjectMap.put(objectType, entry.getValue());
			}
				break;
			case "java.lang.String": {
				logger.info("string");
				if (entry.getKey().equalsIgnoreCase("UUID")) {
					appkey = (String) entry.getValue();
					Appsession appsession = null;
					List<Appsession> sessionList = this.appsessionService
							.findByKey(appkey);
					if (sessionList != null && sessionList.size() > 0)
						appsession = this.appsessionService.findByKey(appkey)
								.get(0);
					if (appsession == null
							|| appsession.getStoreNumber() == null
							|| appsession.getStoreNumber() <= 0)
						returnString = "{\"errorMessage\":\"key mismatch\"}";
					storeNumber = appsession.getStoreNumber();
				}
			}
			case "java.lang.Boolean":
				logger.info("boolean");
			default:
				logger.info("default");
				hm.put(entry.getKey(), entry.getValue().toString());
				break;
			}
			logger.info("entry: " + entry.getKey() + " entry type: "
					+ entry.getValue().toString());

		}
		
		for (Entry<String, String> entry : objectMap.entrySet()) {
		 
			Audit tmpAudit = (Audit) gson.fromJson(entry.getValue(),
					Audit.class);
			
		 // if(startTime!=null){
			 // tmpAudit.setStartTime(startTime);
		 // } 
			
			List<Audit> auditList = null;
			String notes = "";
			if (hm.get("id") != null) {
				// tmpAudit.setId(Long.parseLong(hm.get("id")));
				auditList = new ArrayList<Audit>();
				String idString = hm.get("id");
				Long id = Long.parseLong(idString);
				// logger.error("audit id :" + idString + " " + id);
				Audit audit = this.auditService.findById(id);
				auditList.add(audit);
				if (hm.get("notes") != null) {
					notes = hm.get("notes");
				}
			} else {
				tmpAudit.setStoreNumber(storeNumber);
				Employee employee = new Employee();
				if (hm.get("operatorNumber") != null) {
					employee.setOperatorNumber(Integer.parseInt(hm
							.get("operatorNumber")));
				}
				Lane lane = new Lane();
				if (hm.get("laneNumber") != null) {
					lane.setNumber(Integer.parseInt(hm.get("laneNumber")));
				}
				if (tmpAudit.getNotes() != null) {
					notes = tmpAudit.getNotes();
				}
				tmpAudit.setLane(lane);
				tmpAudit.setEmployee(employee);
				tmpAudit = this.auditService.populateEmployeeAndLane(tmpAudit);
				tmpAudit.setStoreNumber(storeNumber);
				tmpAudit.setVersion(1L);
				auditList = this.auditService.findByObject(tmpAudit);
			}
			if (auditList != null && auditList.size() > 1) {
				returnString = "{\"errorMessage\":\"no unique audits found to update\"}";
			} else if (auditList != null && auditList.size() == 1) {
				// logger.error("auditlist:" + auditList.toString());
				for (Audit audit : auditList) {
					// Audit audit = auditList.get(0);
					if (audit != null) {
						// logger.error("Audit found by id :" +
						// audit.toString());
						audit.setNotes(notes);
					try{
						this.auditService.update(audit);
					 }catch(Exception e){
							logger.info(e.getMessage());
							throw new InvalidRequestException(NextManagerConstants.AUDIT_TRANSACTIONAL_ERROR.getValue(), bindingResult);
					}
					} else {
						returnString = "{\"errorMessage\":\"Audit is null\"}";
					}
				}
			} else {
				Audit audit = this.auditService
						.populateEmployeeAndLane(tmpAudit);
				// String mgrSignature = audit.getMgrSignature();
				String activityType = "";
				audit.setStoreNumber(storeNumber);
				audit.getDate().toString();
				logger.info("inserting audit:" + audit.toString());
				
				 try{
				    this.auditService.insert(audit);
				 }catch(Exception e){
						logger.info(e.getMessage());
						throw new InvalidRequestException(NextManagerConstants.AUDIT_TRANSACTIONAL_ERROR.getValue(), bindingResult);
				}
				
				Employee employee = audit.getEmployee();
				/*
				 * Activities for tracking Audit Positioned Activity (During-After) 
				 */
				ActivityLogs activityLogs = new ActivityLogs();
				boolean isError = false;
				
				// sort between position during, position end, and audit
				if (audit.getPosition().intValue() == 1) {
					activityType = NextManagerConstants.LANE_POSITIONED_DURING.getValue();
				}
				else if (audit.getPosition().intValue() == 2) {
					activityType = NextManagerConstants.LANE_POSITIONED_AFTER.getValue();
				}
				else if (audit.getPosition().intValue() == 3) {
					activityType = NextManagerConstants.LANE_AUDITED.getValue();
				}
				else {
					returnString = "{\"errorMessage\":\"Audit is null\"}";
					isError = true;
				}
				
				if(!isError) {
					activityLogs.setActivityType(activityType);
					activityLogs.setEmpOpNum(employee.getOperatorNumber());
					// activityLogs.setObjectType("Employee");
					activityLogs.setStoreNumber(storeNumber);
					activityLogs.setMgrReason("Audit completed");
					// activityLogs.setMgrSignature(mgrSignature);
					//AT-538 create date - null value fixes
					activityLogs.setCreateDate(new Timestamp(System.currentTimeMillis()));
					activityLogsService.insert(activityLogs);
					// employee.setRequiresAudit(false);
					// employeeService.update(employee);
				}
				 
			}
		}

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			writer.print(returnString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@Override
	public AMService getService() {
		// TODO Auto-generated method stub
		return this.auditService;
	}

}