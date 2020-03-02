package com.aholdusa.am.audittracking.service;

import java.sql.Timestamp;
import java.util.List;

import com.aholdusa.am.audittracking.dto.OverShortReportDTO;
import com.aholdusa.am.audittracking.entity.CarryForwardNotificationTolt;
import com.aholdusa.am.audittracking.entity.OverShortReport;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;

public interface OverShortReportService  extends AMService<OverShortReport>{

	 void createOrUpdateOverShortReport(Store store, 
			                            CarryForwardNotificationTolt carryForwardNotificationTolt,
			                            Boolean failedOver,
                                        Timestamp reportedDate) 
	 throws AuditTrackingException;
	 
	 List<OverShortReportDTO> findOverShortReportByStoreNumber(OverShortReport overShortReport) throws AuditTrackingException;
	 
	 List<OverShortReport> findOverShortReportByStoreNumberLaneId(Long storeNumber, Long laneId, Timestamp reporteDate) throws AuditTrackingException;
	 
	 List<OverShortReport> findOverShortReportByStoreNumberReportDate(Long storeNumber, Timestamp reportDate) throws AuditTrackingException;
	 
	 OverShortReport  findOverShortReportById(Long id) throws AuditTrackingException;
	 
	 void deleteReport(OverShortReport overShortReport) throws AuditTrackingException;
	 
}
