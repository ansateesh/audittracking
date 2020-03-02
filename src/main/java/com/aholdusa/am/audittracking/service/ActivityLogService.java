package com.aholdusa.am.audittracking.service;

import java.sql.Date;
import java.util.List;

import com.aholdusa.am.audittracking.dto.ReportDTO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Notification;
import com.aholdusa.am.audittracking.entity.ReportActivityLogs;


public interface ActivityLogService extends AMService<ActivityLogs> {
	public List<ReportActivityLogs>  findActivityByManagerBeginEndDate(ActivityLogs activityLog,  Date beginDate,Date endDate);
	public List<ReportActivityLogs>  findActivityByBeginEndDate(ActivityLogs activityLog,  Date beginDate,Date endDate);
	public List<ActivityLogs> findActivityByActivityType(ActivityLogs activityLog,Date currentDate);
	List<ActivityLogs>  findActivityTillContentsByOpManagerNumber(ActivityLogs activityLog,  Date currentDate);
	ActivityLogs  findActivityById(ActivityLogs activityLog);
	Notification findNotificationById(Notification notification);
	List<ActivityLogs> findActivityByActivityTypeAndOperatorNumber(ActivityLogs activityLog);
	void deletePrevOverrideActivities(ActivityLogs activityLog);
	public boolean checkSignOnDeclineActivity(ActivityLogs activityLog, Integer laneNumber);
	public List<ReportActivityLogs> getActivityLogsBySearchCriteria(ReportDTO reportDTO);
}