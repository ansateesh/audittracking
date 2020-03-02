package com.aholdusa.am.audittracking.service;

 
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.entity.TillContent;
import com.aholdusa.am.audittracking.entity.ToltFailover;

public interface NextManagerService   {
	
	  TillContent getTillContents(Store store, Integer laneNumber);
	  ActivityLogs logTillContents(Store store, Integer laneNumber,Employee employee);
      boolean isTerminalStatusHasChanged(Store store, Integer laneNumber,Employee employee);
      boolean isTerminalStatusHasChanged(Store store, Integer laneNumber);
      boolean isPrevEmployeeReqAudit(Integer storeNumber,  Integer laneNumber);
      ToltFailover getToltFailover(ToltFailover toltFailover);
      Integer getNumberOfTransactions(Store store, Integer laneNumber);

}
