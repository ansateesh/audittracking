package com.aholdusa.am.audittracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aholdusa.am.audittracking.service.AMService;
import com.aholdusa.am.audittracking.service.LaneHistoryService;

/**
 * Controller for Lane History. Uses the abstract controller methods to handle
 * the CRUD/Fetch operations on lane history.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.controller.AbstractController.java
 */
@Controller
@RequestMapping("lanehistory")
public class LaneHistoryController extends AMController {
	@Autowired
	private LaneHistoryService laneHistoryService;

	@Override
	public AMService getService() {
		// TODO Auto-generated method stub
		return this.laneHistoryService;
	}
}