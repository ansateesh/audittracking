package com.aholdusa.am.audittracking.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.AudittrackingConstants;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;

/**
 * Data access layer Implementation for Lane History. Extends
 * AbstractHibernateDBDAO which provides implementation for standard CRUD
 * operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("laneHistoryDAO")
public class LaneHistoryDAOImpl extends DAOImpl<LaneHistory, Long>implements LaneHistoryDAO {
	private static final Logger logger = LoggerFactory.getLogger(LaneHistory.class);

	public void delete(LaneHistory entity) {
		// TODO Auto-generated method stub
		entity.setIsDeleted(true);
		this.save(entity);
	}

	public void insert(LaneHistory entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

	public void update(LaneHistory entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

	public List<LaneHistory> findByAssignment(LaneHistory lh) { 
		

		Query query = this.getSession().getNamedQuery("findLaneHistoryByAssignment");
		query.setParameter("laneId", lh.getLane().getId());
		query.setParameter("employeeId", lh.getEmployee().getId());
		query.setParameter("storeNumber", lh.getStoreNumber());
		query.setParameter("date", lh.getLaneAssignmentDate());
		// query.setParameter("startTime", lh.getStartTime());
		//get result List
		List<LaneHistory> resultList=query.list();
		List<LaneHistory> resutlListWithStartTimeCriteria=new ArrayList<LaneHistory>();
		
		if(resultList!=null){
			for(LaneHistory laneHistory:resultList){
				long seconds =Math.abs( (lh.getStartTime().getTime()-laneHistory.getStartTime().getTime())/1000);
				if(seconds<=1){
					resutlListWithStartTimeCriteria.add(laneHistory);
				}
			}
		}
		
		return resutlListWithStartTimeCriteria;
	
	}

	public List<LaneHistory> findAllByDate(LaneHistory lh) {
		logger.info("findAllByDate");
		Query query = this.getSession().getNamedQuery("findAllLaneHistoryByDate");
		query.setParameter("storeNumber", lh.getStoreNumber());
		query.setParameter("date", lh.getLaneAssignmentDate());
		return query.list();
	}

	public List<LaneHistory> findAllByDateAndLane(LaneHistory lh) {
		Query query = this.getSession().getNamedQuery("findAllLaneHistoryByDateAndLane");
		query.setParameter("storeNumber", lh.getStoreNumber());
		query.setParameter("date", lh.getLaneAssignmentDate());
		query.setParameter("laneNumber", lh.getLane().getNumber());
		return query.list();
	}

	public List<LaneHistory> findLastLaneHistoryByDateAndLane(LaneHistory lh) {
		Query query = this.getSession().getNamedQuery("findLastLaneHistoryByDateAndLane");
		query.setParameter("storeNumber", lh.getStoreNumber());
		query.setParameter("date", lh.getLaneAssignmentDate());
		query.setParameter("laneNumber", lh.getLane().getNumber());
		return query.list();
	}

	// findLastLaneHistoryByDateAndLane
	public boolean laneHistoryExistsForEmployee(Long employeeId) {
		Query query = this.getSession().getNamedQuery("laneHistoryExistsForEmployee");
		query.setParameter("employeeId", employeeId);
		return query.list().size() > 0 ? true : false;

	}

	public boolean laneHistoryExistsForLane(Long laneId) {
		Query query = this.getSession().getNamedQuery("laneHistoryExistsForLane");
		query.setParameter("laneId", laneId);
		return query.list().size() > 0 ? true : false;

	}

	/**
	 * deleteLaneInLaneHistory(): Soft-Delete(is_deleted = true) all instances 
	 * 							  of a given lane from the LaneAssignments table. 
	 * @param Long laneId: Unique Id for lane
	 * @param Integer storeNumber: store Number
	 * 
	 * @return None
	 */
	public void deleteLaneInLaneHistory(Long laneId, Integer storeNumber) {
		Query query = this.getSession().getNamedQuery("softDeleteLaneHistoryByLaneId");
		query.setParameter("laneId", laneId);
		query.setParameter("storeNumber", storeNumber);
		query.executeUpdate();
	}
	
	/**
	 * reEnableLaneInLaneHistory(): Un soft-Delete(is_deleted = false) all instances 
	 * 							  of a given lane from the LaneAssignments table. 
	 * @param Long laneId: Unique Id for lane
	 * @param Integer storeNumber: store Number
	 * 
	 * @return None
	 */
	public void reEnableLaneInLaneHistory(Long laneId, Integer storeNumber) {
		Query query = this.getSession().getNamedQuery("reEnableLaneInLaneHistory");
		query.setParameter("laneId", laneId);
		query.setParameter("storeNumber", storeNumber);
		query.executeUpdate();
	}

	public List<LaneHistory> findByObject(LaneHistory entity) {
		// TODO Auto-generated method stub
		// entity.setIsDeleted(true);
		return this.findByObjectExample(entity);
	}

	private class LhResults {
		private Integer laneNumber;
		private Integer operatorNumber;
		private LaneHistory laneHistory;

		public LhResults(Integer lane, Integer employee, LaneHistory laneHistory) {
			this.laneNumber = lane;
			this.operatorNumber = employee;
			this.laneHistory = laneHistory;
		}

		public Integer getLane() {
			return this.laneNumber;
		}

		public void setLane(Integer lane) {
			this.laneNumber = lane;
		}

		public Integer getEmployee() {
			return this.operatorNumber;
		}

		public void setEmployee(Integer employee) {
			this.operatorNumber = employee;
		}

		public LaneHistory getLaneHistory() {
			return this.laneHistory;
		}

		public void setLaneHistory(LaneHistory laneHistory) {
			this.laneHistory = laneHistory;
		}

		public String toString() {
			String returnString = "";
			if (this.getLane() != null)
				returnString += this.getLane().toString();
			if (this.getEmployee() != null)
				returnString += this.getEmployee().toString();
			if (this.getLaneHistory() != null)
				returnString += this.getLaneHistory().toString();
			return returnString;
		}

	}

 
	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.dao.LaneHistoryDAO#findLastLaneHistoryByStoreAndLane(com.aholdusa.am.audittracking.entity.LaneHistory)
	 */
	@Override
	public LaneHistory findLastLaneHistoryByStoreAndLane(LaneHistory lh)
			throws AuditTrackingException {
		
		Query query = this.getSession().getNamedQuery("findLastLaneHistoryByStoreAndLane");
		query.setParameter(AudittrackingConstants.STORE_NUMBER_COL_NAME.getValue(), lh.getStoreNumber());
		query.setParameter(AudittrackingConstants.LANE_NUMBER_COL_NAME.getValue(), lh.getLane().getNumber());
		query.setMaxResults(1);
		LaneHistory laneHistory=null;
		if(query.uniqueResult()!=null){
			laneHistory=(LaneHistory)query.uniqueResult();
		}
		return  laneHistory;
		 
	}

	 

}