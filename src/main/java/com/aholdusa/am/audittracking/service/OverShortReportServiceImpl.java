package com.aholdusa.am.audittracking.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.ActivityLogDAO;
import com.aholdusa.am.audittracking.dao.LaneDAO;
import com.aholdusa.am.audittracking.dao.OverShortReportDAO;
import com.aholdusa.am.audittracking.dto.OverShortReportDTO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.CarryForwardNotificationTolt;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.OverShortReport;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.entity.ToltFailover;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;
import com.aholdusa.am.audittracking.util.AMUtils;
import com.aholdusa.am.audittracking.util.RuleRunner;

@Service("overShortReportService")
public class OverShortReportServiceImpl    extends AMServiceImpl<OverShortReport> implements OverShortReportService {



	@Autowired
	private LaneDAO laneDao;

	@Autowired
	private ActivityLogDAO activityLogDao;

	@Autowired
	private OverShortReportDAO overShortReportDao;

	@Override
	public List<OverShortReport> findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createOrUpdateOverShortReport(Store store,
			CarryForwardNotificationTolt carryForwardNotificationTolt,
			Boolean failedOver,
			Timestamp reportedDate)
					throws AuditTrackingException {

		/*
		 * Create an Activity type=  OverShort_Received 
		 * 	
		 */
		ActivityLogs activityLogOverShortReceived =new ActivityLogs();
		activityLogOverShortReceived.setActivityType(NextManagerConstants.OVERSHORT_ACTIVITY_RECEIVED.getValue());
		activityLogOverShortReceived.setEmpOpNum(9999);
		activityLogOverShortReceived.setStoreNumber(store.getNumber());
		activityLogOverShortReceived.setCreateDate( new Timestamp(System.currentTimeMillis())); 
		activityLogOverShortReceived.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
		activityLogOverShortReceived.setMgrReason(NextManagerConstants.OVERSHORT_ACTIVITY_RECEIVED.getValue());
		activityLogDao.insert(activityLogOverShortReceived);

		for (Map.Entry<String, String> entry : carryForwardNotificationTolt.getTerminals().entrySet())

		{
			Integer terminalNumber=0; 
			if(entry.getKey()!=null){
				terminalNumber=new Integer(entry.getKey());
			}



			Lane lane= laneDao.findByLaneNumber(store.getNumber(), terminalNumber);

			//< ===== > Create Lane Logic to be included 

			if(lane!=null){

				//Formatting OverShortAMT
				String overShortAmt=entry.getValue().toString();
				overShortAmt=overShortAmt.replace("$", "").trim();
				//FIXES FOR NUM. FORMAT ','
				overShortAmt=overShortAmt.replace(",", "");


				//===== UPDATING OVER SHORT AMOUNT BY LANE NUMBER - DATA BASE LANES -> OVER_SHORT_AMOUNT 
				// Fixes for :Reverse signs for over/short 
				Double overShortAmtFixed=null;
				if(!overShortAmt.equalsIgnoreCase("")){
					overShortAmtFixed=new Double (overShortAmt);
					overShortAmtFixed*=-1;
				}else{
					overShortAmtFixed=new Double (0);
				}

				
				//over_short_amount < 25 and over_short_amount > -25 then softDeleted the record
				boolean softDeleted=false;
				if(overShortAmtFixed < 25 && overShortAmtFixed > -25){
					softDeleted=true;
				}
				
				lane.setOverShortAmount(new Double(overShortAmtFixed));

				//========= Updating Lane with OverShortAmt Received from Tolt ===========//
				laneDao.update(lane);


				//OVER SHORT REPORT NEW RECORD 
				//
				OverShortReport overShortReport =new OverShortReport();
				overShortReport.setCreateDate(new Timestamp(System.currentTimeMillis()));
				overShortReport.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
				overShortReport.setFailover(failedOver);
				overShortReport.setOverShortAmount( overShortAmtFixed);
				overShortReport=getOverShortDelete(overShortReport);
			    //overShortReport.setIsDeleted(overShortReport.getOverShortAmountDeleted());
				
				overShortReport.setReportDate(getOverShortReportedDate(reportedDate)); 
				overShortReport.setReportDeleted(softDeleted);
				//Fix for OverShort Is Deleted
				overShortReport.setIsDeleted(softDeleted);
				overShortReport.setStoreNumber(store.getNumber().longValue());
				overShortReport.setLaneId(lane.getId());
				overShortReport.setVersion(new Long(1));
				overShortReportDao.save(overShortReport);

				//CREATE A ACTIVITY LOG BY LANENUMBER / OVERSHRTAMT
				ActivityLogs activityLogs =new ActivityLogs();
				activityLogs.setActivityType(NextManagerConstants.OVERSHORT_ACTIVITY_TYPE.getValue());
				activityLogs.setEmpOpNum(9999);
				activityLogs.setStoreNumber(store.getNumber());
				activityLogs.setCreateDate( new Timestamp(System.currentTimeMillis())); 
				activityLogs.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
				activityLogs.setMgrReason("LaneNumber:"+terminalNumber+"|OverShortAmount:"+overShortAmtFixed);
				activityLogDao.insert(activityLogs);

			}

		}




	}

	
	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.service.OverShortReportService#findOverShortReportByStoreNumber(com.aholdusa.am.audittracking.entity.OverShortReport)
	 */
	
	@Override
	public  List<OverShortReportDTO> findOverShortReportByStoreNumber(
			OverShortReport overShortReport) throws AuditTrackingException {
		List<OverShortReportDTO>  overShortReportDTOByStore=null;
		List<OverShortReport> result=overShortReportDao.findOverShortReportByStoreNumber(overShortReport);
		if(result!=null){
			overShortReportDTOByStore=new ArrayList<OverShortReportDTO>();
			for(OverShortReport overShortReportIt:result){
			
				OverShortReportDTO overShortReportDTO = new OverShortReportDTO();
				overShortReportDTO.setId(overShortReportIt.getId());
				overShortReportDTO.setFailover(overShortReportIt.getFailover());
				overShortReportDTO.setOverShortAmount(overShortReportIt.getOverShortAmount());
				overShortReportDTO.setReportDate( AMUtils.getStringFromDate(overShortReportIt.getReportDate()));
				overShortReportDTO.setStoreNumber(overShortReportIt.getStoreNumber());
				overShortReportDTO.setLane(laneDao.findByID(overShortReportIt.getLaneId()));
				overShortReportDTOByStore.add(overShortReportDTO);
			}
		}

		return overShortReportDTOByStore;
	}

	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.service.OverShortReportService#findOverShortReportByStoreNumberLaneId(java.lang.Long, java.lang.Long, java.sql.Timestamp)
	 */
	@Override
	public List<OverShortReport> findOverShortReportByStoreNumberLaneId(
			Long storeNumber, Long laneId, Timestamp reporteDate) throws AuditTrackingException {
		OverShortReport overShortReport=new OverShortReport();
		return overShortReportDao.findOverShortReportByStoreNumberLaneIdReportDate(overShortReport);
	}

	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.service.OverShortReportService#findOverShortReportByStoreNumberReportDate(java.lang.Long, java.sql.Timestamp)
	 */
	@Override
	public List<OverShortReport> findOverShortReportByStoreNumberReportDate(
			Long storeNumber, Timestamp reportDate)
					throws AuditTrackingException {
		OverShortReport overShortReport=new OverShortReport();
		overShortReport.setStoreNumber(storeNumber);
		overShortReport.setReportDate(reportDate);

		return overShortReportDao.findOverShortReportByStoreNumberLaneIdReportDate(overShortReport);
	}

	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.service.OverShortReportService#findOverShortReportById(java.lang.Long)
	 */
	@Override
	public  OverShortReport  findOverShortReportById(Long id)
			throws AuditTrackingException {
		OverShortReport overShortReport=new OverShortReport();
		overShortReport.setId(id);
		return overShortReportDao.findOverShortReportById(overShortReport);
	}

	 
	private OverShortReport getOverShortDelete(OverShortReport overShortReport) {
		RuleRunner runner = new RuleRunner();
		String[] rules = { NextManagerConstants.OVERSHORTREPORT_DELETION_RULES.getValue() };
		Object[] facts = { overShortReport };
		runner.runRules(rules,facts);
		return overShortReport;
		
	}

	@Override
	public void deleteReport(OverShortReport overShortReport)
			throws AuditTrackingException {
		 
		overShortReportDao.deleteReport(overShortReport);
	}


	/**
	 * @param reportedDate
	 * @return Pomeroy Reported Date minus One Day 
	 */
	private Timestamp getOverShortReportedDate(Timestamp reportedDate){

		Calendar calReportedDate = Calendar.getInstance();
		calReportedDate.setTime(new Date(reportedDate.getTime()));
		calReportedDate.add(Calendar.DATE, -1);
		calReportedDate.getTime();
		return new Timestamp(calReportedDate.getTimeInMillis());

	}
	
	
}
