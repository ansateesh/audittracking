package com.aholdusa.am.audittracking.service;


import com.aholdusa.am.audittracking.entity.NextmanagerEvents;
import com.aholdusa.am.audittracking.entity.Notification;

public interface NextmanagerEventService extends AMService<NextmanagerEvents>{
	
	 NextmanagerEvents findEventById(NextmanagerEvents nextmanagerEvents); 
	 boolean checkSignOnDeclineActivity(NextmanagerEvents nextmanagerEvents);
	 Notification findNotificationById(Notification notification);

}
