package com.aholdusa.am.audittracking.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.ActivityLogDAO;
import com.aholdusa.am.audittracking.dao.AppsessionDAO;
import com.aholdusa.am.audittracking.dao.AuditDAO;
import com.aholdusa.am.audittracking.dao.LaneDAO;
import com.aholdusa.am.audittracking.dao.LaneHistoryDAO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.CarryForwardNotificationTolt;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.LaneHistory;

/**
 * Service implementation class for Lane. Business logic involved in handling
 * Lanes should be implemented here.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 * 
 * @see com.aholdusa.core.base.service.DBService.java
 *
 */
@Service("laneService")
public class LaneServiceImpl extends AMServiceImpl<Lane> implements LaneService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private LaneHistoryDAO laneHistoryDao;
	@Autowired
	private AuditDAO auditDao;
	@Autowired
	private LaneDAO laneDao;
	@Autowired
	private ActivityLogDAO activityLogDAO;
	
	

	@Autowired
	private AppsessionDAO appsessionDao;
	
	public List<Lane> getCleanLanes(Integer storeNumber) {
 
		List<Lane> storeLanes=laneDao.getLanesByStoreNumber(storeNumber);
		ListIterator<Lane>itr = storeLanes.listIterator();
		LaneHistory prevHistoryLaneRecord = new LaneHistory();
		List<Lane> cleanLanes=new ArrayList<Lane>();
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		while (itr.hasNext()){
			Lane lane=itr.next();
			prevHistoryLaneRecord.setLane(lane);
			prevHistoryLaneRecord.setStoreNumber(storeNumber);
			prevHistoryLaneRecord.setLaneAssignmentDate(currentDate);
			prevHistoryLaneRecord.setIsDeleted(false);
			List<LaneHistory> lhlist2 = laneHistoryDao.findAllByDateAndLane(prevHistoryLaneRecord);
			/*
			 * Validation
			 * LaneAssignment Record Exists
			 * Get Last Assigned Lane Record Size -1 
			 * ASC Order
			 */
			if (lhlist2.size() > 0) {
				prevHistoryLaneRecord = lhlist2.get(lhlist2.size()-1);
			//	prevHistoryLaneRecord = lhlist2.get(0);
				Audit audit=new Audit();
				audit.setLane(lane);
				audit.setDate(currentDate);
				audit.setStoreNumber(storeNumber);
				audit.setStartTime(prevHistoryLaneRecord.getStartTime());
				//List<Audit> audits= auditDao.findByLane(audit);
				/*
				 * Use only the AuditID don't select all from Audits table
				 * 
				 * Use cols. below only if necessary those are images fields 
				 *  audit0_.SLIP AS SLIP17_2_,
         			audit0_.EMP_SIGNATURE AS EMP_SIG18_2_,
         			audit0_.MGR_SIGNATURE AS MGR_SIG19_2_,
				 */
				 List<Long> audits= auditDao.getLanesInAudit(audit);
				
			
				
				if(audits!=null && audits.size()>0){
					cleanLanes.add(lane);
				} 
			}
			/*
			 * Lane is Clean
			 */
			else{
				cleanLanes.add(lane);
			}
		}
		 
		return cleanLanes;
		 
	}

	public void update(Lane l) {
		this.laneDao.update(l);

	}

	public void delete(Lane tmp) {
		this.laneDao.delete(tmp);
	}

	public void insert(Lane l) {
		this.laneDao.insert(l);
	}

	public List findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	public String lockLaneForUpdate(Lane lane, String key) {
		lane.setRecordStatus(2);
		lane.setLockedBy(key);
		laneDao.save(lane);
		return "{\"UUID\":\"" + key + "\",\"id\":" + lane.getId() + "}";
	}

	public String unlockLane(Lane lane) {
		lane.setRecordStatus(0);
		lane.setLockedBy(null);
		laneDao.save(lane);
		return "{}";
	}

	@Override
	public Lane findByLaneNumber(Integer storeNumber, Integer laneNumber) {
		return this.laneDao.findByLaneNumber(storeNumber, laneNumber);
	}
	
	@Override
	public Lane findLaneByNumberAndStoreNumber(Integer storeNumber, Integer laneNumber) {
		return this.laneDao.findLaneByNumberAndStoreNumber(storeNumber, laneNumber);
	}

	@Override
	public List<Lane> getLanesByStoreNumber(Integer storeNumber) {
		return this.laneDao.getLanesByStoreNumber(storeNumber);
	}
	
	@Override
	public List<Lane> findActiveLanes(Lane entity) {
		return this.laneDao.findActiveLanes(entity);
	}

	
	@Override
	public String getOverShortReport(CarryForwardNotificationTolt carryForwardNotification){
		Integer storeNumber = null;
		Appsession appsession = null;
		String response="Failed";
		
		List<Appsession> sessionList = this.appsessionDao.findByKey(carryForwardNotification.getUuid());
		if (sessionList != null && sessionList.size() > 0){
			appsession = this.appsessionDao.findByKey(carryForwardNotification.getUuid()).get(0);
			if (appsession != null && appsession.getStoreNumber() != null && appsession.getStoreNumber() > 0){
				storeNumber = appsession.getStoreNumber();
				for (Map.Entry<String, String> entry : carryForwardNotification.getTerminals().entrySet())
				{
					Integer laneNumber=0; 
					if(entry.getKey()!=null){
						laneNumber=new Integer(entry.getKey());
					}

					Lane lane= laneDao.findByLaneNumber(storeNumber, laneNumber);
					if(lane!=null){
						String overShortAmt=entry.getValue().toString();
						overShortAmt=overShortAmt.replace("$", "").trim();
						lane.setOverShortAmount(new Double(overShortAmt));
						ActivityLogs activityLogs =new ActivityLogs();
						activityLogs.setActivityType("OverShortReport");
						activityLogs.setStoreNumber(storeNumber);
						String prevAmount=lane.getOverShortAmount()!=null?lane.getOverShortAmount().toString():"0";
						activityLogs.setMgrReason("LaneNumber:"+laneNumber+"| Prev. OverShortAmount:"+prevAmount);
						activityLogDAO.insert(activityLogs);
						
						laneDao.save(lane);
						response="200";
					}
				}
			}
		}
		return response;
	}

	@Override
	public List<Lane> getAuditLanes(Integer storeNumber) {
		// TODO Auto-generated method stub
		return laneDao.getAuditLanes(storeNumber);
	}
	
}