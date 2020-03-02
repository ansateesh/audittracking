package com.aholdusa.test;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aholdusa.am.audittracking.entity.CarryForwardNotificationTolt;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.service.LaneHistoryService;
import com.aholdusa.am.audittracking.service.LaneService;
import com.aholdusa.am.audittracking.service.OverShortReportService;
import com.aholdusa.am.audittracking.util.AMUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "Hibernate_local_context.xml")
public class BasicCRUD {

	
	@Autowired
	LaneHistoryService laneHistoryService;

	@Autowired
	private LaneService laneService;
 
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	OverShortReportService overShortReportService;
	
	
	// Generate Error update laneH with invalid values
	@Test
	public void testSetLaneHistory(){
		 Lane lane = laneService.findByLaneNumber(87, 1);
		 LaneHistory lh =new LaneHistory();
		 LaneHistory laneHistory = new LaneHistory();
		 String toltFormatDateTime="2017-06-08 22:48:18";
			laneHistory.setEmployee( employeeService.findByOperatorNumber(87,1).get(0));
			laneHistory.setLane(lane);
			laneHistory.setStartTime( AMUtils.getTimestampFromString(toltFormatDateTime));
			laneHistory.setLaneAssignmentDate (AMUtils.getTimestampFromString(toltFormatDateTime));
			laneHistory.setIsDeleted(false);
			laneHistory.setStoreNumber(87);
			// laneHistory.setVersion(1L); This is mandatory field remove to generate and 500 error
			laneHistoryService.insert(laneHistory);
	}
	
	@Test
	public void testGetLaneHistory(){
		 Lane lane = laneService.findByLaneNumber(87, 1);
		 LaneHistory lh =new LaneHistory();
		 LaneHistory laneHistory = new LaneHistory();
		 String toltFormatDateTime="2017-04-27 22:48:18";
			laneHistory.setEmployee( employeeService.findByOperatorNumber(87,1).get(0));
			laneHistory.setLane(lane);
			laneHistory.setStartTime( AMUtils.getTimestampFromString(toltFormatDateTime));
			laneHistory.setLaneAssignmentDate (AMUtils.getTimestampFromString(toltFormatDateTime));
			laneHistory.setIsDeleted(false);
			laneHistory.setStoreNumber(87);
			laneHistory.setVersion(1L);
			laneHistoryService.findLastLaneHistoryByStoreAndLane(laneHistory);
	}
	
	
	@Test
	public void testUpdateLaneHistory(){
		 Lane lane = laneService.findByLaneNumber(87, 1);
		 LaneHistory lh =new LaneHistory();
		 LaneHistory laneHistory = new LaneHistory();
		 String toltFormatDateTime="2016-03-22 22:48:18";
			laneHistory.setEmployee( employeeService.findByOperatorNumber(87,1).get(0));
			laneHistory.setLane(lane);
			laneHistory.setStartTime( AMUtils.getTimestampFromString(toltFormatDateTime));
			laneHistory.setLaneAssignmentDate (AMUtils.getTimestampFromString(toltFormatDateTime));
			laneHistory.setIsDeleted(false);
			laneHistory.setStoreNumber(87);
			laneHistory.setVersion(1L);
			laneHistoryService.insert(laneHistory);
	}
	
	
	
	@Test
	public void testOverShortReport(){
		
		Store store=new Store();
		store.setNumber(6962);
		Map <String,String> terminals= new HashMap<String,String>();
		terminals.put("1", "$10.00");
		terminals.put("2", "$100.00");//soft deleted
		terminals.put("3", "$25.00");
		CarryForwardNotificationTolt dummyOverShort= new CarryForwardNotificationTolt();
		dummyOverShort.setTerminals(terminals);
		Timestamp reportedDate=new Timestamp(System.currentTimeMillis());
		
		overShortReportService.createOrUpdateOverShortReport(store, dummyOverShort, false, reportedDate);
		
	}
	
	
	
}
