package com.aholdusa.am.audittracking.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.LaneConstants;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.service.ActivityLogService;

/**
 * Data access layer Implementation for Lane. Extends AbstractHibernateDBDAO
 * which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("laneDAO")
public class LaneDAOImpl extends DAOImpl<Lane, Long> implements LaneDAO {

	@Autowired
	private ActivityLogService activityLogsService;
	
	private static final Logger logger = LoggerFactory.getLogger(Lane.class);

	//
	// public Lane findById(long id) {
	// return (Lane) super.findByID(Lane.class, id) ;
	// }

	public void insert(Lane entity) {
		// TODO Auto-generated method stub
		this.save(entity);
		//createLaneActivity(entity);
	}

	public List<Lane> findByObject(Lane entity) {
		// TODO Auto-generated method stub
		// entity.setIsDeleted(true);
		return this.findByObjectExample(entity);
	}

	// Active lane fixes

	public List<Lane> findActiveLanes(Lane entity) {
		Query query = this.getSession().getNamedQuery("findActiveLaneByNumber");
		query.setParameter("storeNumber", entity.getStoreNumber());
		query.setParameter("laneNumber", entity.getNumber());
		logger.info("findByLaneNumber: " + query.toString() + " laneNumber:"
				+ entity.getNumber() + " " + " StoreId:"
				+ entity.getStoreNumber());
		return query.list();
	}

	/**
	 * delete(): Soft-Delete(is_deleted = true) a lane from
	 * 			 Lane Table. 
	 * 			 Soft-Delete all the instances of a given lane
	 * 			 from LaneAssignments and Audits tables.
	 * @param Lane entity: Lane information
	 * 
	 * @return None
	 */
	public void delete(Lane entity) {
		//(updating isDeleted = true)
		
		LaneHistory lh = new LaneHistory();
		lh.setLane(entity);
		
		// UPDATING LANE_ASSIGNMENTS
		LaneHistoryDAO lhd = new LaneHistoryDAOImpl();
		lhd.deleteLaneInLaneHistory(entity.getId(), entity.getStoreNumber());
		
		// UPDATING AUDITS
		AuditDAO ad = new AuditDAOImpl();
		ad.deleteLaneInAudits(entity.getId(), entity.getStoreNumber());
		
		// UPDATING LANE
		entity.setIsDeleted(true);
		this.save(entity);
	}

	public void update(Lane entity) {
		// TODO Auto-generated method stub
		this.save(entity);
		createLaneActivity(entity);
	}

	public List<Lane> getCleanLanes(Integer storeNumber) {
		Query query = this.getSession().getNamedQuery("getCleanLanes");
		Date date = new Date(System.currentTimeMillis());
		query.setParameter("storeNumber", storeNumber);
		// FIXES For E2E Test
		// query.setParameter("date", date);
		logger.info("getCleanLanes: " + query.toString() + " date:" + date);

		return query.list();
	}

	public Lane findByLaneNumber(Integer storeNumber, Integer laneNumber) {
		Query query = this.getSession().getNamedQuery("findLaneByNumber");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("laneNumber", laneNumber);
		logger.info("findByLaneNumber: " + query.toString() + " laneNumber:"
				+ laneNumber + " " + " StoreId:" + storeNumber);
		return (Lane) query.uniqueResult();
	}
	
	/**
	 * findLaneByNumberUsingReportDeleted(): Un soft-Delete(is_deleted = false) a lane from
	 * 			 			Lane Table. 
	 * 			 			Un soft-Delete all the instances of a given lane
	 * 			 			from LaneAssignments and Audits tables.
	 * @param Integer storeNumber: store Number
	 * @param Integer laneNumber: lane Number
	 * 
	 * @return Lane lane
	 */
	public Lane findLaneByNumberAndStoreNumber(Integer storeNumber, Integer laneNumber) {
		Query query = this.getSession().getNamedQuery("findLaneByNumberAndStoreNumber");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("laneNumber", laneNumber);
		logger.info("findLaneByNumberUsingReportDeleted: " + query.toString() + " laneNumber:"
				+ laneNumber + " " + " StoreId:" + storeNumber);
		
		Lane lane = (Lane) query.uniqueResult();
		if(lane.getIsDeleted()){
			//(setting isDeleted = true)
			
			// UPDATING LANE_ASSIGNMENTS 
			LaneHistoryDAO lhd = new LaneHistoryDAOImpl();
			lhd.reEnableLaneInLaneHistory(new Long(lane.getId()),storeNumber );
			
			// UPDATING AUDITS
			AuditDAO ad = new AuditDAOImpl();
			ad.reEnableLaneInAudits(new Long(lane.getId()),storeNumber);
			
			// UPDATING LANE
			lane.setIsDeleted(false);
			this.update(lane);
			return (Lane) query.uniqueResult();
		}		
		return lane;
	}


	@Override
	public List<Lane> getLanesByStoreId(Long storeNumber) {
		Query query = this.getSession().getNamedQuery("findLaneByStoreId");
		query.setParameter("storeId", storeNumber);
		logger.info("getCleanLanes: " + query.toString() + " StoreID:"
				+ storeNumber);
		return query.list();
	}

	@Override
	public List<Lane> getLanesByStoreNumber(Integer storeNumber) {
		Query query = this.getSession().getNamedQuery("findLaneByStoreNumber");
		query.setParameter("storeNumber", storeNumber);
		logger.info("getCleanLanes: " + query.toString() + " StoreNumber:"
				+ storeNumber);
		return query.list();
	}

	@Override
	public List<Lane> getAuditLanes(Integer storeNumber) {
		Query query = this.getSession().getNamedQuery("getAuditLanes");
		query.setParameter("storeNumber", storeNumber);
		logger.info("getAuditLanes: " + query.toString() + " storeNumber:" + storeNumber);
		return query.list();
	}
	
	 
	@Deprecated
	/* @deprecated As of release Yellow
	    * this activity is no longer used by push notification server   
	    */
	private void createLaneActivity(Lane entity) {
		ActivityLogs activityLogLaneActivity = new ActivityLogs();
		activityLogLaneActivity.setActivityType(LaneConstants.LANE_UPDATED.getValue());
		activityLogLaneActivity.setEmpOpNum(9999);
		activityLogLaneActivity.setStoreNumber(entity.getStoreNumber());
		activityLogLaneActivity.setCreateDate( new Timestamp(System.currentTimeMillis())); 
		activityLogLaneActivity.setCreatedBy(LaneConstants.ADMIN_USER.getValue());
		activityLogLaneActivity.setMgrReason(LaneConstants.LANE_UPDATED.getValue());
		activityLogsService.insert(activityLogLaneActivity);
	}

}