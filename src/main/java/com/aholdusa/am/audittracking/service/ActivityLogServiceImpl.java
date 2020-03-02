package com.aholdusa.am.audittracking.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.ActivityLogDAO;
import com.aholdusa.am.audittracking.dao.EmployeeDAO;
import com.aholdusa.am.audittracking.dto.ReportDTO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Notification;
import com.aholdusa.am.audittracking.entity.ReportActivityLogs;
import com.aholdusa.am.audittracking.entity.ReportConstants;

@Service("activityLogService")
public class ActivityLogServiceImpl extends AMServiceImpl<ActivityLogs>
		implements ActivityLogService {

	
	@Autowired
	private ActivityLogDAO activityLogDao;
	
	
	@Autowired
	private EmployeeDAO employeeDao;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public void insert(ActivityLogs entity) {
		activityLogDao.insert(entity);

	}

	@Override
	public List<ActivityLogs> findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<ReportActivityLogs> findActivityByManagerBeginEndDate(
			ActivityLogs activityLog, Date beginDate, Date endDate) {
		List<ReportActivityLogs>reportActivityLogList=null; 
		List <ActivityLogs>activityLogList=activityLogDao.findActivityByManagerBeginEndDate(activityLog, beginDate, endDate);
	   
		if(activityLogList!=null && activityLogList.size() >0){
			reportActivityLogList=new ArrayList<ReportActivityLogs>();
		  for(ActivityLogs activity:activityLogList){
			  ReportActivityLogs reportActivity= new ReportActivityLogs();
			  reportActivity.setActivityLog(activity);
			  reportActivityLogList.add(reportActivity);
			  //Set Manager
			  List <Employee> managerList=  employeeDao.findByOperatorNumber(activity.getStoreNumber(), activity.getMgrOpNum());
			   reportActivity.setManager(managerList!=null?managerList.get(0):null);
			  
			  //Set Employee
			   List <Employee> employeeList = employeeDao.findByOperatorNumber(activity.getStoreNumber(), activity.getEmpOpNum());
			   reportActivity.setEmployee(employeeList!=null?employeeList.get(0):null);
		  }
		}
		
			
			return reportActivityLogList;
	}

	@Override
	public List<ReportActivityLogs> findActivityByBeginEndDate(
			ActivityLogs activityLog, Date beginDate, Date endDate) {
		List<ReportActivityLogs>reportActivityLogList=null; 
		List <ActivityLogs>activityLogList=null;
		if(activityLog.getEmpOpNum()!=null){
			activityLogList=activityLogDao.findActivityOperatorNumberByBeginEndDate(activityLog, beginDate, endDate);
		}else{
			activityLogList=activityLogDao.findActivityByBeginEndDate(activityLog, beginDate, endDate);
		}
		if(activityLogList!=null && activityLogList.size() >0){
			reportActivityLogList=new ArrayList<ReportActivityLogs>();
		  for(ActivityLogs activity:activityLogList){
			  ReportActivityLogs reportActivity= new ReportActivityLogs();
			  reportActivity.setActivityLog(activity);
			  reportActivityLogList.add(reportActivity);
			  //Set Manager
			  List <Employee> managerList=  employeeDao.findByOperatorNumber(activity.getStoreNumber(), activity.getMgrOpNum());
			   if(managerList!=null && managerList.size()>0){
			     reportActivity.setManager( managerList.get(0) );
			   }
			  //Set Employee
			   List <Employee> employeeList = employeeDao.findByOperatorNumber(activity.getStoreNumber(), activity.getEmpOpNum());
			   if(employeeList!=null && employeeList.size()>0){ 
			     reportActivity.setEmployee( employeeList.get(0) );
			   }
		  }
		}
		
			
			return reportActivityLogList;
	}

	@Override
	public List<ActivityLogs> findActivityByActivityType(
			ActivityLogs activityLog, Date currentDate) {
		List <ActivityLogs>activityLogList = activityLogDao.findActivityByActivityType(activityLog,currentDate);
		return activityLogList;
	}

	@Override
	public List<ActivityLogs> findActivityTillContentsByOpManagerNumber(
			ActivityLogs activityLog, Date currentDate) {
		return activityLogDao.findActivityTillContentsByOpManagerNumber(activityLog, currentDate);
	}

	@Override
	public ActivityLogs findActivityById(ActivityLogs activityLog) {
		// TODO Auto-generated method stub
		return activityLogDao.findActivityById(activityLog);
	}

	@Override
	public Notification findNotificationById(Notification notification) {
		// TODO Auto-generated method stub
		 ActivityLogs activity = new ActivityLogs();
		 String completeMessage;
		 String filteredReason = "";
		 activity.setId(notification.getActivityId());
		 activity= activityLogDao.findActivityById(activity);
		 
		 
		 if(activity != null){
			 // Mapping Activity vs. Notification
			 notification.setActivityType(activity.getActivityType());
			 notification.setStoreNumber(activity.getStoreNumber().longValue());
			 completeMessage = activity.getMgrReason()!=null?activity.getMgrReason():"";
			 
			 if(completeMessage.contains("Reason")) filteredReason = completeMessage.split("Reason:")[1];
			 notification.setMessage(filteredReason);

			 /*
			  * Extracting TerminalId
			  */
					 
			 boolean prevTokenIsTerminal=false;
			 String reason=activity.getMgrReason()!=null?activity.getMgrReason():"";
			 String terminalNumber="0";
			 StringTokenizer strTokenizer=new StringTokenizer(reason,"|");
			 while(strTokenizer.hasMoreTokens() && !prevTokenIsTerminal){
				 String element=strTokenizer.nextToken();
				 StringTokenizer strTokenizerInner=new StringTokenizer(element,":");
				 String token="";  
				 while(strTokenizerInner.hasMoreTokens() && !prevTokenIsTerminal){

					 if(token.contains("TerminalId")){
						 prevTokenIsTerminal=true;
					 }

					 token=strTokenizerInner.nextToken();

					 if(prevTokenIsTerminal){
						 terminalNumber=token;  

					 }
				 }
			 }


			 notification.setTerminalId(new Long(terminalNumber));
			 notification.setOperatorNumber(activity.getEmpOpNum().longValue());
			 notification.setActivityType(activity.getActivityType());


		 }else{
			 notification=null;
		 }
		 
		 
		 
		return notification;
	}

	@Override
	public List<ActivityLogs> findActivityByActivityTypeAndOperatorNumber(
			ActivityLogs activityLog) {
		return  activityLogDao.findActivityByActivityTypeAndOperatorNumber(activityLog);
	}

	/**
	 * On SignOnDecline, query the ActivityLog Table for a SignOnDecline entry 
	 * with the same operator number, lane number (terminal id), store number, 
	 * and trunc(created_date) with is_deleted = 0. If any entries match these 
	 * parameters,
	 * create a new ActivityLog entry for the SignOnDecline but with is_deleted = 1.
	 * ActivityLogs al = new ActivityLogs();
	query.setParameter("storeNumber", activityLog.getStoreNumber()); 
	query.setParameter("activityType", activityLog.getActivityType()); 
	query.setParameter("empOpNum",activityLog.getEmpOpNum() );
	query.setParameter("createdDate", activityLog.getCreateDate()); 
	return  activityLogDao.findActivityByActivityTypeAndOperatorNumber(activityLog);
	 */
	@Override
	public boolean checkSignOnDeclineActivity(ActivityLogs activityLog, Integer laneNumber) {
		//return  activityLogDao.findActivityByActivityTypeAndOperatorNumber(activityLog);
		boolean activityExist = false;
		try{
			List<ActivityLogs> list = activityLogDao.findActivityByActivityTypeAndOperatorNumber(activityLog);
			
			if(list != null && list.size() > 1 ){
				activityExist = true;
			}
			
			
			
		}catch(Exception e){
			LOGGER.info("Exception: "+e);
			
		}
		
		return activityExist;
	}

	
	
	@Override
	public void deletePrevOverrideActivities(ActivityLogs activityLog) {
		List<ActivityLogs> activities= activityLogDao.findActivityByActivityTypeAndOperatorNumber(activityLog);
		if(activities!=null && activities.size()>0){
			for (ActivityLogs activity : activities){
				activity.setIsDeleted(true);
				activityLogDao.update(activity);
			}
		}
	}

	@Override
	public List<ReportActivityLogs> getActivityLogsBySearchCriteria(ReportDTO reportDTO) {
		// TODO Auto-generated method stub
		List<ReportActivityLogs> reportActivityLogList=new ArrayList<ReportActivityLogs>();; 
		List<ActivityLogs> activityLogs =  activityLogDao.getActivityLogsBySearchCriteria(reportDTO);
		for (ActivityLogs activityLogsFromDao : activityLogs) {
			ReportActivityLogs reportActivityLogObj = new ReportActivityLogs();
			if(activityLogsFromDao.getEmpOpNum()!=null){
				List<Employee> employeesList = employeeDao.findByOperatorNumber(activityLogsFromDao.getStoreNumber(), activityLogsFromDao.getEmpOpNum());
				if(CollectionUtils.isNotEmpty(employeesList)){
					reportActivityLogObj.setEmployee(employeesList.get(0));
				}
			}
			if(activityLogsFromDao.getMgrOpNum()!=null){
				List<Employee> employeesList = employeeDao.findByOperatorNumber(activityLogsFromDao.getStoreNumber(), activityLogsFromDao.getMgrOpNum());
				if(CollectionUtils.isNotEmpty(employeesList)){
					reportActivityLogObj.setManager(employeesList.get(0));
				}
			}
			activityLogsFromDao.setObjectType(ReportConstants.REPORT_ACTIVITY_OBJECT_TYPE_EMP.getValue());
			reportActivityLogObj.setActivityLog(activityLogsFromDao);
			reportActivityLogList.add(reportActivityLogObj);
		}
		return reportActivityLogList;
	}

}