package com.aholdusa.am.audittracking.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.AuditDAO;
import com.aholdusa.am.audittracking.dao.LaneDAO;
import com.aholdusa.am.audittracking.dao.LaneHistoryDAO;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;

/**
 * Service implementation class for Lane History. Business logic involved in
 * handling Lane History should be implemented here.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 * 
 * @see com.aholdusa.core.base.service.DBService.java
 *
 */
@Service("laneHistoryService")
public class LaneHistoryServiceImpl extends AMServiceImpl<LaneHistory>
		implements LaneHistoryService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private LaneDAO laneDao;
	@Autowired
	private AuditDAO auditDao;
	@Autowired
	private LaneHistoryDAO laneHistoryDao;

	public List<LaneHistory> findByAssignment(LaneHistory lh) {
		return this.laneHistoryDao.findByAssignment(lh);
	}

	public List<LaneHistory> findAllByDate(LaneHistory lh) {
		return this.laneHistoryDao.findAllByDate(lh);
	}

	public List<LaneHistory> findAllByDateAndLane(LaneHistory lh) {
		return this.laneHistoryDao.findAllByDateAndLane(lh);
	}

	public List<LaneHistory> findLastLaneHistoryByDateAndLane(LaneHistory lh) {
		return this.laneHistoryDao.findLastLaneHistoryByDateAndLane(lh);
	}

	public void insert(LaneHistory lh) {
		dao.save(lh);
	}

	public List findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.service.LaneHistoryService#findLastLaneHistoryByStoreAndLane(com.aholdusa.am.audittracking.entity.LaneHistory)
	 */
	@Override
	public LaneHistory findLastLaneHistoryByStoreAndLane(LaneHistory lh)
			throws AuditTrackingException {
		// TODO Auto-generated method stub
		return laneHistoryDao.findLastLaneHistoryByStoreAndLane(lh);
	}

}