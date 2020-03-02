package com.aholdusa.am.audittracking.service;

import java.util.List;

import com.aholdusa.am.audittracking.dto.ReportDTO;
import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.ReportActivityLogs;

public interface ReportService {
	List<Integer> getStoreNumber();
	List<Audit> getAuditReport(ReportDTO reportDTO);
    List<ReportActivityLogs> getActivityLogReport(ReportDTO reportDTO);
    List<ReportActivityLogs> getActivityLogForSearchCriteria(ReportDTO reportDTO);
}
