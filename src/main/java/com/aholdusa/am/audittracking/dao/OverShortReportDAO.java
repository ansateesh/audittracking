package com.aholdusa.am.audittracking.dao;

import java.util.List;

import com.aholdusa.am.audittracking.entity.OverShortReport;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;

public interface OverShortReportDAO extends DAO<OverShortReport, Long>{
 
	List<OverShortReport> findOverShortReportByStoreNumber(OverShortReport overShortReport) throws AuditTrackingException;
	List<OverShortReport> findOverShortReportByStoreNumberLaneIdReportDate(OverShortReport overShortReport) throws AuditTrackingException;
	List<OverShortReport> findOverShortReportByStoreNumberReportDate(OverShortReport overShortReport) throws AuditTrackingException;
	OverShortReport       findOverShortReportById(OverShortReport overShortReport) throws AuditTrackingException;
	void deleteReport(OverShortReport overShortReport) throws AuditTrackingException;
	
	
}
