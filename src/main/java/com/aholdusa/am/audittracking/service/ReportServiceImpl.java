package com.aholdusa.am.audittracking.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.ActivityLogDAO;
import com.aholdusa.am.audittracking.dao.AuditDAO;
import com.aholdusa.am.audittracking.dto.ReportDTO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.ReportActivityLogs;
import com.aholdusa.am.audittracking.entity.ReportConstants;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.util.AMUtils;



@Service("reportService")
public class ReportServiceImpl implements ReportService{

	@Autowired
	StoreService storeService;	
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	ActivityLogService activityLogService;	
	
	@Override
	public List<Integer> getStoreNumber() {
		
		List <Integer> storeNumbers= new ArrayList<Integer>();
		Store store =new Store();
		store.setIsDeleted(false);
		List<Store> stores=storeService.findByObject(store);

		for(Store storeTmp:stores){
			storeNumbers.add(storeTmp.getNumber());
		}
		
		return storeNumbers;
	}

	@Override
	public List<Audit> getAuditReport(ReportDTO reportDTO) {
		Audit audit=new Audit();
		List<Audit> audits=null;
		java.sql.Date beginDateValue =null;
		java.sql.Date endDateValue =null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			java.util.Date beginDate = sdf.parse(reportDTO.getBeginDate());
			beginDateValue =new java.sql.Date(beginDate.getTime());

			java.util.Date endDate = sdf.parse(reportDTO.getEndDate());
			endDateValue =new java.sql.Date(endDate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(reportDTO.getStoreNumber()!=null){
			audit.setStoreNumber(reportDTO.getStoreNumber());
			if(reportDTO.getOperatorNumber()!=null){
				Employee emp=new Employee();
				emp.setOperatorNumber(reportDTO.getOperatorNumber());
				audit.setEmployee(emp);
				audits=auditService.findAuditsByStoreNumberOperatorNumberBeginEndDate(audit, beginDateValue , endDateValue);
			}
			else{
				audits=auditService.findAuditsByStoreNumberBeginEndDate(audit, beginDateValue , endDateValue);
			}
		}

		return audits;
	}

	@Override
	public List<ReportActivityLogs> getActivityLogReport(ReportDTO reportDTO) {
		// TODO Auto-generated method stub
	 
		java.sql.Date beginDateValue =null;
		java.sql.Date endDateValue =null;
		ActivityLogs activity =new ActivityLogs();
		activity.setObjectType(ReportConstants.REPORT_ACTIVITY_OBJECT_TYPE_EMP.getValue());
		activity.setStoreNumber(reportDTO.getStoreNumber());
		activity.setActivityType(ReportConstants.REPORT_ACTIVITY_TYPE_OVERRIDE.getValue());
		
		if(reportDTO.getOperatorNumber()!=null){
		  activity.setEmpOpNum(reportDTO.getOperatorNumber());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			java.util.Date beginDate = sdf.parse(reportDTO.getBeginDate());
			beginDateValue =new java.sql.Date(beginDate.getTime());

			java.util.Date endDate = sdf.parse(reportDTO.getEndDate());
			endDateValue =new java.sql.Date(endDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	 
		return activityLogService.findActivityByBeginEndDate(activity,beginDateValue,endDateValue);
	}

	@Override
	public List<ReportActivityLogs> getActivityLogForSearchCriteria(ReportDTO reportDTO) {
		// TODO Auto-generated method stub
		
		return activityLogService.getActivityLogsBySearchCriteria(reportDTO);
	}
	
   

}
