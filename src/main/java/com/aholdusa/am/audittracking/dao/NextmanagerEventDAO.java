package com.aholdusa.am.audittracking.dao;

import java.util.List;
import com.aholdusa.am.audittracking.entity.NextmanagerEvents;

public interface NextmanagerEventDAO  extends DAO<NextmanagerEvents, Long> {
	
	NextmanagerEvents  findEventById(NextmanagerEvents nextmanagerEvents);
	List<NextmanagerEvents> findEventByEventTypeAndOperatorNumber(NextmanagerEvents nextmanagerEvents);

}
