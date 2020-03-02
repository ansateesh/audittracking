package com.aholdusa.am.audittracking.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.NextmanagerEvents;
import com.aholdusa.am.audittracking.entity.Notification;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.service.NextmanagerEventService;

@RestController
@RequestMapping("activityLogs")
public class ActivityLogsController {
	
	@Autowired
	private ActivityLogService activityLogsService; 
	@Autowired
	EmployeeService employeeService;

	@Autowired
	private NextmanagerEventService nextmanagerEventService; 
	/*
	 * Soft Delete an ActivityLog By ActivityId
	 * 
	 * {"activityId":920}
	 * 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/deleteActivitiesById/", produces = "application/json")
	@ResponseBody
	public String deleteActivityLog(@RequestBody final LinkedHashMap<String, Object> entity) {
		Long activityId= new Long(0);
		String activityIdValue="0";

		for (Entry<String, Object> entry : entity.entrySet()) {
			//activityId
			if (entry.getKey().equalsIgnoreCase("activityId")) {
				activityIdValue= entry.getValue().toString();
			}  
		}
		activityId= new Long(activityIdValue);
		NextmanagerEvents nextmaganerEvent=new NextmanagerEvents();
		nextmaganerEvent.setId(activityId);
		nextmaganerEvent=nextmanagerEventService.findEventById(nextmaganerEvent);
		nextmaganerEvent.setId(activityId);
		nextmaganerEvent.setIsDeleted(true);
		nextmanagerEventService.update(nextmaganerEvent);
		return "Ok";
	}


	@RequestMapping(value = "/getActivitiesById/{activityId}", method = RequestMethod.GET, produces = "application/json")
	public Notification getActivityById(@PathVariable Long activityId) {
		Notification notification=new Notification();
		notification.setActivityId(activityId);
		notification=nextmanagerEventService.findNotificationById(notification);
		return notification;
	}
	
	@Deprecated
	@RequestMapping(value = "/getActivitiesByActivityType/{activityType}/{storeNumber}", method = RequestMethod.GET, produces = "application/json")
	 public List<Employee> getActivityByActivityType(@PathVariable String activityType, @PathVariable Integer storeNumber) {
		
		
		Calendar cal  = Calendar.getInstance();
		ActivityLogs activityLogs = new ActivityLogs();
		activityLogs.setActivityType(activityType);
		activityLogs.setStoreNumber(storeNumber);
		activityLogs.setCreateDate(new Timestamp(cal.getTimeInMillis()));

		List<ActivityLogs> allActivityLogs = this.activityLogsService.findActivityByActivityType(activityLogs, new Date(cal.getTimeInMillis()));
		List<Employee> allEmployees = new ArrayList<Employee>();
		
		for (ActivityLogs activity : allActivityLogs) {
			// if(activity.getEmpOpNum() == Integer.parseInt(EmployeeConstants.INVALID_EMPLOYEE.getValue())) continue; 	// don't consider test 9999 operator number
			Employee employees = this.employeeService.findByOperatorNumber(activity.getStoreNumber(),activity.getEmpOpNum()).get(0);
			
			allEmployees.add(employees);
		}
		
		return allEmployees;
	}

}
