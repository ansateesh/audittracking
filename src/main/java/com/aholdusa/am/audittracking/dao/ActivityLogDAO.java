package com.aholdusa.am.audittracking.dao;

import java.sql.Date;
import java.util.List;

import com.aholdusa.am.audittracking.dto.ReportDTO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;

/**
 * Data access layer interface for Lane. Extends DBDAO which defines standard CRUD operations.
 * 
 * @author x1udxk1
 * 
 * @see com.aholdusa.core.base.dao.DBDAO.java
 */
public interface ActivityLogDAO extends DAO<ActivityLogs, Long> {
	  void logActivity(Integer empOpNum, Integer empStoreNum, String activity_type);
	  List<ActivityLogs>  findActivityByManagerBeginEndDate(ActivityLogs activityLog,  Date beginDate,Date endDate);
	  List<ActivityLogs>  findActivityByBeginEndDate(ActivityLogs activityLog,  Date beginDate,Date endDate);
      List<ActivityLogs>  findActivityByActivityType(ActivityLogs activityLog,  Date currentDate);
      List<ActivityLogs>  findActivityTillContentsByOpManagerNumber(ActivityLogs activityLog,  Date currentDate);
      ActivityLogs  findActivityById(ActivityLogs activityLog);
      List<ActivityLogs>  findActivityByActivityTypeAndOperatorNumber(ActivityLogs activityLog);
      List<ActivityLogs>  findActivityOperatorNumberByBeginEndDate(ActivityLogs activityLog,  Date beginDate, Date endDate);
      List<ActivityLogs> getActivityLogsBySearchCriteria(ReportDTO reportDTO);
}