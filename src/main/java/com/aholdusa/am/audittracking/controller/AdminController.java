package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtilsBean;
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
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.exception.InvalidRequestException;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.service.LaneService;
import com.aholdusa.am.audittracking.util.AMBeanUtils;
import com.aholdusa.am.audittracking.util.AMUtils;
import com.aholdusa.am.audittracking.util.ServiceUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("admintasks")
public class AdminController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	LaneService laneService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	AppsessionService appsessionService;
	@Autowired
	ActivityLogService activityLogService;

	@ResponseBody
	@RequestMapping(value = "/lock/", method = RequestMethod.POST)
	public void lockObject(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response) {
		String returnString = lockUnlockObject(entity, false);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			logger.info("locking object:" + returnString);
			writer.print(returnString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/logactivity/", method = RequestMethod.POST)
	public void logActivity(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response) {
	
		//logger.info("<<<<<<<<<<<<<<< LOGACTIVITY REQUEST ADMIN >>>>>>>>>>>>>>"+entity.toString());
		//logger.debug("<<<<<<<<<<<<<<< LOGACTIVITY REQUEST ADMIN >>>>>>>>>>>>>>"+entity.toString());
	
		HashMap<String, HashMap<String, String>> map = ServiceUtil
				.parseRequest(this.appsessionService, entity);
			Gson gson = new Gson();
		HashMap<String, String> hm = map.get("hm");
		HashMap<String, String> objectMap = map.get("objectMap");
		Integer storeNumber = Integer.parseInt(hm.get("storeNumber"));
		String overrideActivityType = "Override";
		ActivityLogs activityLogs = (ActivityLogs) gson.fromJson(
				objectMap.get("ActivityLogs"), ActivityLogs.class);
		if (activityLogs != null) {
			//
			// Employee tmpEmployee = (Employee) gson.fromJson(
			// objectMap.get("Employee"), Employee.class);
			// tmpEmployee.setStoreNumber(storeNumber);
			//FIXES FOR NEW TOLT EMP.
			List tmpEmployees=this.employeeService.findByOperatorNumber(
					storeNumber, activityLogs.getEmpOpNum());
			Employee tmpEmployee =null;
			if(tmpEmployees!=null && tmpEmployees.size()>0 ){
			  tmpEmployee = this.employeeService.findByOperatorNumber(
					storeNumber, activityLogs.getEmpOpNum()).get(0);
			}
			
			if (tmpEmployee != null) {
				
				
			//	List employees=this.employeeService.findByObject(tmpEmployee);
				
				//Get Employee by Store and Operator Number and Deleted=False
				List employees=employeeService.findByOperatorNumber(tmpEmployee.getStoreNumber().intValue(),tmpEmployee.getOperatorNumber().intValue());
				Employee employee =null;
				
				if(employees!=null && employees.size()>0){
					employee =(Employee) employees.get(0);
				}
				
				if (employee != null) {
					activityLogs.setEmpOpNum(employee.getOperatorNumber());

					// if this is an override log, set employee's overridden
					// state to true
					//Override
					if (activityLogs.getActivityType().toLowerCase().equalsIgnoreCase(overrideActivityType) ) {
						employee.setOverridden(true);
						employeeService.update(employee);
					}
				}
				activityLogs.setStoreNumber(storeNumber);
			}
			// {
			// Employee mgrEmployee =
			// this.employeeService.findByOperatorNumber(storeNumber,
			// activityLogs.getMgrOpNum()).get(0);
			// }
			Long prevActivityId= new Long(0);
			if(activityLogs.getId()!=null){
				prevActivityId=activityLogs.getId();
				activityLogs.setId(null);
			}
			//
			activityLogs.setCreateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
			this.activityLogService.insert(activityLogs);
			
			
			//Soft Delete The Push Notification Activity
			if(prevActivityId.longValue()>0){
				ActivityLogs activityLogPrev = new ActivityLogs ();
				activityLogPrev.setId(prevActivityId);
				activityLogPrev =this.activityLogService.findActivityById(activityLogPrev);
				if(activityLogPrev!=null){
					activityLogPrev.setIsDeleted(true);
					this.activityLogService.update(activityLogPrev);
				}
			}
			

		}
	}

	@ResponseBody
	@RequestMapping(value = "/unlock/", method = RequestMethod.POST)
	public void unlockObject(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response) {
		String returnString = lockUnlockObject(entity, true);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			logger.info("unlocking object:" + returnString);
			writer.print(returnString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * 
	 * @param entity
	 * @param unlock
	 * @return
	 */
	public String lockUnlockObject(final LinkedHashMap<String, Object> entity,
			boolean unlock) {
		// String responseString = null;
		Object object = null;
		// Integer storeNumber = null;
		HashMap<String, HashMap<String, String>> map = ServiceUtil
				.parseRequest(this.appsessionService, entity);
		logger.info("parsed map:" + map.toString());
		HashMap<String, String> objectMap = map.get("objectMap");
		HashMap<String, String> hm = map.get("hm");		
		logger.info("storeNumber:" + hm.get("storeNumber"));
		Integer storeNumber = Integer.parseInt(hm.get("storeNumber"));
		logger.info("storeNumber int:" + storeNumber);
		String responseString = hm.get("returnString");
		String appkey = hm.get("appkey");
		String objectType = null;
		if (storeNumber == null || appkey == null) {
			return responseString;
		}
		logger.info("storeNumber:" + storeNumber);
		logger.info("returnString:" + responseString);

		Gson gson = new GsonBuilder().create();

		for (Entry<String, String> entry : objectMap.entrySet()) {
			objectType = entry.getKey();
			logger.info("trying to lock object:" + objectType + " " + entry);
			switch ((objectType)) {
			case "Employee":
				Employee tmpEmp = (Employee) gson.fromJson(entry.getValue(),
						Employee.class);
				tmpEmp.setStoreNumber(storeNumber);
				List<Employee> empList = this.employeeService
						.findByObject(tmpEmp);
				if (empList.size() != 1) {
					responseString = "{\"errorMessage\":\"No unique employees found to lock/unlock\"}";
					break;
				}
				logger.info(empList.toString());
				Employee employee = empList.get(0);
				if (employee.getRecordStatus() >= 1) {
					if (employee.getLockedBy().equalsIgnoreCase(appkey)) {
						if (unlock) {
							responseString = this.employeeService
									.unlockEmployee(employee);
							// <<==>> Clean Override Activity Log for Current Date==>>
							// Fixes for AT-335
							ActivityLogs activity=new ActivityLogs();
							activity.setStoreNumber(employee.getStoreNumber());
							activity.setEmpOpNum(employee.getOperatorNumber());
							activity.setActivityType(NextManagerConstants.OVERRIDE.getValue());
							activity.setCreateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
							this.activityLogService.deletePrevOverrideActivities(activity);
					 

						} else {
							responseString = "{\"UUID\":\"" + appkey
									+ "\", \"id\":" + employee.getId() + "}";
						}
					} else {
						responseString = "{\"errorMessage\":\"Employee locked by another instance\"}";
					}
					break;
				}
				if (!unlock) {
					responseString = this.employeeService
							.lockEmployeeForUpdate(employee, appkey);
				}
				break;
			case "Lane":
				Lane tmpLane = (Lane) gson.fromJson(entry.getValue(),
						Lane.class);
				tmpLane.setStoreNumber(storeNumber);
				logger.info("trying to lock lane: " + tmpLane.toString());
				List<Lane> laneList = this.laneService.findByObject(tmpLane);
				if (laneList.size() != 1) {
					responseString = "{\"errorMessage\":\"No unique lanes found to lock/unlock\"}";
					break;
				}
				Lane lane = laneList.get(0);
				if (lane.getRecordStatus() >= 1) {
					if (lane.getLockedBy().equalsIgnoreCase(appkey)) {
						if (unlock) {
							responseString = this.laneService.unlockLane(lane);

						} else {
							responseString = "{\"UUID\":\"" + appkey
									+ "\",\"id\":" + lane.getId() + "}";
						}
					} else {
						responseString = "{\"errorMessage\":\"Lane locked by another instance\"}";
					}
					break;
				}
				if (!unlock) {
					responseString = this.laneService.lockLaneForUpdate(lane,
							appkey);
				}
				break;
			default:
				break;
			}

		}

		return responseString;
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void handlePost(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response,
			BindingResult bindingResult) {

		logger.info("doAdmin entered: " + entity.toString());

		String returnString = "{\"errorMessage\":\"success\"}";

		Object object = null;
		HashMap<String, HashMap<String, String>> map = ServiceUtil
				.parseRequest(this.appsessionService, entity);
		logger.info("parsed map:" + map.toString());
		HashMap<String, String> objectMap = map.get("objectMap");
		HashMap<String, String> hm = map.get("hm");
		logger.info("storeNumber:" + hm.get("storeNumber"));
		Integer storeNumber = Integer.parseInt(hm.get("storeNumber"));
		logger.info("storeNumber int:" + storeNumber);
		returnString = hm.get("returnString");
		String appkey = hm.get("appkey");
		String objectType = null;
		// if (storeNumber == null || appkey == null) {
		// return responseString;
		// }
		logger.info("storeNumber:" + storeNumber);
		logger.info("returnString:" + returnString);

		if (storeNumber != null && appkey != null) {
			for (Entry<String, String> entry : objectMap.entrySet()) {
				logger.info("processing object:" + entry.getKey());
				switch (entry.getKey()) {
				case "Employee": {
					logger.error("Dumping objectMap ........ ");
					logger.error(objectMap.toString());
					logger.error("done ........ ");

					try{
					returnString = processEmployee(entry, storeNumber, appkey,
							objectMap.get("ActivityLogs"));
					} catch(Exception e){
						logger.info(e.getMessage());
						throw new InvalidRequestException(NextManagerConstants.EMPLOYEE_TRANSACTIONAL_ERROR.getValue(), bindingResult);
					}
					// ActivityLogs activityLog = objectMap.get("ActivityLog");
					continue;
				}
				case "Lane": {
					try{
						returnString = processLane(entry, storeNumber, appkey);
					}catch(Exception e){
						logger.info(e.getMessage());
						throw new InvalidRequestException(NextManagerConstants.LANE_TRANSACTIONAL_ERROR.getValue(), bindingResult);
					}
					continue;
				}
				case "ActivityLogs": {
					continue;
				}
				default: {
					returnString = "{\"errorMessage\":\"Do not know what to do with this object. Stop!\"}";
					continue;
				}
				}
			}
		}

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			logger.info("admin returning: " + returnString);
			writer.print(returnString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * 
	 * @param entry
	 * @param storeNumber
	 * @param appkey
	 * @param activityLogString
	 * @return
	 */
	public String processEmployee(Entry<String, String> entry,
			Integer storeNumber, String appkey, String activityLogString) {

		boolean delete = false;
		String returnString = "{\"errorMessage\":\"success\"}";
		Gson gson = new Gson();

		logger.info("processing employee");
		Employee tmpEmp = (Employee) gson.fromJson(entry.getValue(),
				Employee.class);
		ActivityLogs activityLogs = (ActivityLogs) gson.fromJson(
				activityLogString, ActivityLogs.class);
		logger.error("tmpEmp:" + tmpEmp.toString() + " empty check: "
				+ AMUtils.isEmpty(tmpEmp));
		
	
	

		tmpEmp.setStoreNumber(storeNumber);
		List<Employee> employeeList = null;

		// edit / delete
		if (tmpEmp.getId() != null && tmpEmp.getId() > 0) {
		
				
			
			logger.info("find employee by id: " + tmpEmp.getId());
			Employee employee = this.employeeService.findById(tmpEmp.getId());

			// AT-531 Fix.
			// the problem was. tmpEmp.getRequiresAudit() is NULL
			// since we tried to use it by doing, if(tmpEmp.getRequiresAudit() &&) 
			// it will have a null pointer exception. we cannot see the error
			// since we didn't have a try catch method on it. 
			// - to fix this. we will add a != null ex.  if(tmpEmp.getRequiresAudit() != null)
			// 	 and moved it inside the edit. since, on edit. this will have a value, while
			//	 in delete. it will always be null.
			// - "{\"UUID\":\"audittrackingclient6979\",\"Employee\":{\"id\":1725}}" this is the delete data
			// 	we are receiving.
			// - i suggest that we moved the validation of audit requires inside the edit 
			// 	 since delete doesn't have any business with it.
			// - added try catch method to lessen the pain of debugging...
			// 
			//
			
		try{
			//Check if employee is being marked for audit with this edit
			/* Commented out by ben.part of AT-531 and moved inside edi.
			if(tmpEmp.getRequiresAudit() && !employee.getRequiresAudit()){
				tmpEmp.setMarkedForAuditTime(new Timestamp(System.currentTimeMillis()));
			}*/
			
			// edit
			if (tmpEmp.getOperatorNumber() != null
					&& tmpEmp.getOperatorNumber() > 0) {
				BeanUtilsBean bu = new AMBeanUtils();
				
				// FIX for AT-531
			    if((tmpEmp.getRequiresAudit() != null && tmpEmp.getRequiresAudit() ) && 
			    	( employee != null && !employee.getRequiresAudit())  
			    		){
					logger.info("Temp Employee here: " + tmpEmp.toString());
					logger.info("AUDIT REQUIRES: " + tmpEmp.getId());
					tmpEmp.setMarkedForAuditTime(new Timestamp(System.currentTimeMillis()));
				}
			    
				// check for duplicate employees
				employeeList = this.employeeService.findByOperatorNumber(
						storeNumber, tmpEmp.getOperatorNumber());

				if (employeeList.size() > 1) {
					returnString = "{\"errorMessage\":\"Duplicate Employee found. Cannot Add/Edit\"}";
					logger.error(returnString + employeeList.size());
					return returnString;
				}

				// copy tmpEmp properties into employee
				try {
					bu.copyProperties(employee, tmpEmp);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				// activity log
				try {
					if (activityLogs == null) {
						activityLogs = new ActivityLogs();

						activityLogs.setObjectType("Employee");
					}

					activityLogs.setEmpOpNum(tmpEmp.getOperatorNumber());
					activityLogs.setStoreNumber(storeNumber);
					activityLogs.setTimeStamp(new Timestamp(Calendar
							.getInstance().getTime().getTime()));

					if (tmpEmp.getActive() != null
							&& employee.getActive() != tmpEmp.getActive()) {
						if (tmpEmp.getActive()) {
							activityLogs.setActivityType("mark active");
						} else {
							activityLogs.setActivityType("mark inactive");
						}
						this.activityLogService.insert(activityLogs);
					}
					if (tmpEmp.getRequiresAudit() != null
							&& employee.getRequiresAudit() != tmpEmp
									.getRequiresAudit()) {
						if (tmpEmp.getRequiresAudit()) {
							activityLogs.setActivityType("mark for audit");
						} else {
							activityLogs.setActivityType("unmark for audit");
						}
						this.activityLogService.insert(activityLogs);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// check if locked
				if (employee.getLockedBy().equals(appkey)) {
					// CHECK IF THE LANE IS DUPLICATE FROM THE DATABASE
					logger.info("find employee by object: "
							+ employee.getOperatorNumber());
					employeeList = this.employeeService.findByOperatorNumber(
							storeNumber, tmpEmp.getOperatorNumber());
					if (employeeList.size() > 1) { // if duplicate for either
													// operator
													// number change or other
													// than
													// operator number change
						returnString = "{\"errorMessage\":\"Duplicate Employee found. Cannot Add/Edit\"}";
						logger.error(returnString + employeeList.size());
						return returnString;
					} else if (employeeList.size() == 1) { // if anything but
															// operator
															// number was
															// changed
						// check if same employee as edited employee
						if (employeeList.get(0).getId()
								.equals(employee.getId())) {
							// update employee
							logger.info("updating employee: "
									+ employee.toString());
							this.employeeService.update(employee);
							returnString = "{\"errorMessage\":\"updated successfully\"}";
						} else {
							returnString = "{\"errorMessage\":\"Duplicate Employee found. Cannot Add/Edit\"}";
							logger.error(returnString + employeeList.size());
							return returnString;
						}
					} else if (employeeList.size() == 0) { // if operator number
															// was
															// changed
						// update employee
						logger.info("updating employee: " + employee.toString());
						this.employeeService.update(employee);
						returnString = "{\"errorMessage\":\"updated successfully\"}";
					}

				} else {
					returnString = "{\"errorMessage\":\"locked by another process\"}";
					logger.error(returnString);
					return returnString;
				}
			}
			// delete
			else {
				if (employee.getLockedBy().equals(appkey)) {
					logger.info("deleting lane:" + tmpEmp.toString());
					this.employeeService.delete(employee);
				} else {
					returnString = "{\"errorMessage\":\"locked by another process\"}";
					logger.error(returnString);
					return returnString;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		}
		// add
		else {
			logger.info("find employee by object: "
					+ tmpEmp.getOperatorNumber());
			employeeList = this.employeeService.findByOperatorNumber(
					storeNumber, tmpEmp.getOperatorNumber());

			// if any employees are found, duplicate error
			if (employeeList.size() > 0) {
				returnString = "{\"errorMessage\":\"Duplicate Employee found. Cannot Add/Edit\"}";
				logger.error(returnString + employeeList.size());
				return returnString;
			} else if (employeeList.size() == 0) {
				// add employee
				tmpEmp.setStoreNumber(storeNumber);
				tmpEmp.setActive(true);
				tmpEmp.setRequiresAudit(false);
				tmpEmp.setVersion(1L);
				tmpEmp.setRecordStatus(0);
				tmpEmp.setIsDeleted(false);
				tmpEmp.setStoreNumber(storeNumber);
				logger.info("creating employee:" + tmpEmp.toString());
				 
				    this.employeeService.insert(tmpEmp);
				
			}
		}

		return returnString;

	}

	/**
	 * 
	 * @param entry
	 * @param storeNumber
	 * @param appkey
	 * @return
	 */
	public String processLane2(Entry<String, String> entry,
			Integer storeNumber, String appkey) {
		String returnString = "{\"errorMessage\":\"success\"}";
		logger.info("processLane:" + entry.toString());
		return returnString;
	}

	/**
	 * 
	 * @param entry
	 * @param storeNumber
	 * @param appkey
	 * @return
	 */
	public String processLane(Entry<String, String> entry, Integer storeNumber,
			String appkey) {
		String returnString = "{\"errorMessage\":\"success\"}";
		Gson gson = new Gson();

		Lane tmpLane = null;
		logger.info("processing lane");
		try {
			tmpLane = (Lane) gson.fromJson(entry.getValue(), Lane.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (tmpLane == null) {
			returnString = "{\"errorMessage\":\"Gson error\"}";
			return returnString;
		}
		tmpLane.setStoreNumber(storeNumber);
		List<Lane> laneList = null;

		// edit / delete
		if (tmpLane.getId() != null && tmpLane.getId() > 0) {
			logger.info("find lane by id: " + tmpLane.getId());
			Lane lane = this.laneService.findById(tmpLane.getId());
			
			// edit
			if (tmpLane.getNumber() != null && tmpLane.getNumber() > 0) {
				BeanUtilsBean bu = new AMBeanUtils();

				// check for duplicate lanes
				laneList = this.laneService.findActiveLanes(lane);

				if (laneList.size() > 1) {
					returnString = "{\"errorMessage\":\"Duplicate Lane found. Cannot Add/Edit\"}";
					logger.error(returnString + laneList.size());
					return returnString;
				}

				// copy tmpLane properties into lane
				try {
					 // bu.copyProperties(lane, tmpLane);
					
					  if(tmpLane.getOverShortAmount()!=null){	
						lane.setOverShortAmount(tmpLane.getOverShortAmount());
					}
				
				} 
				/*
				catch (IllegalAccessException e) {
					e.printStackTrace();
				} 
				*/
				
				catch (Exception e) {
					e.printStackTrace();
				}
				
				// check if locked
				if (lane.getLockedBy()!=null && lane.getLockedBy().equals(appkey)) {
					// CHECK IF THE LANE IS DUPLICATE FROM THE DATABASE
					logger.info("find lane by object: " + lane.getNumber());
					laneList = this.laneService.findActiveLanes(lane);
					if (laneList.size() > 1) { // if duplicate for either lane
												// number change or other than
												// lane number change
						returnString = "{\"errorMessage\":\"Duplicate Lane found. Cannot Add/Edit\"}";
						logger.error(returnString + laneList.size());
						return returnString;
					} else if (laneList.size() == 1) { // if anything but lane
														// number was changed
						// check if same lane as edited lane
						if (laneList.get(0).getId().equals(lane.getId())) {
							// update lane
							logger.info("updating lane: " + lane.toString());
							this.laneService.update(lane);
							returnString = "{\"errorMessage\":\"updated successfully\"}";
						} else {
							returnString = "{\"errorMessage\":\"Duplicate Lane found. Cannot Add/Edit\"}";
							logger.error(returnString + laneList.size());
							return returnString;
						}
					} else if (laneList.size() == 0) { // if lane number was
														// changed
						// update lane
						logger.info("updating lane: " + lane.toString());
						this.laneService.update(lane);
						returnString = "{\"errorMessage\":\"updated successfully\"}";
					}

				} else {
					returnString = "{\"errorMessage\":\"locked by another process\"}";
					logger.error(returnString);
					return returnString;
				}
			}
			// delete
			else {
				if (lane.getLockedBy().equals(appkey)) {
					logger.info("deleting lane:" + tmpLane.toString());
					this.laneService.delete(lane);
				} else {
					returnString = "{\"errorMessage\":\"locked by another process\"}";
					logger.error(returnString);
					return returnString;
				}
			}
		}
		// add
		else {
			logger.info("find lane by object: " + tmpLane.getNumber());
			laneList = this.laneService.findActiveLanes(tmpLane);

			// if any lanes are found, duplicate error
			if (laneList.size() > 0) {
				returnString = "{\"errorMessage\":\"Duplicate Lane found. Cannot Add/Edit\"}";
				logger.error(returnString + laneList.size());
				return returnString;
			} else if (laneList.size() == 0) {
				// add lane
				tmpLane.setStoreNumber(storeNumber);
				tmpLane.setActive(true);
				tmpLane.setVersion(1L);
				tmpLane.setRecordStatus(0);
				tmpLane.setIsDeleted(false);
				logger.info("creating lane:" + tmpLane.toString());
				this.laneService.insert(tmpLane);
			}
		}

		return returnString;
	}
}
