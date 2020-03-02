package com.aholdusa.am.audittracking.controller;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aholdusa.am.audittracking.dto.OverShortReportDTO;
import com.aholdusa.am.audittracking.entity.OverShortReport;
import com.aholdusa.am.audittracking.service.OverShortReportService;

@RestController
@RequestMapping("overShortReport")
public class OverShortReportController {


	@Autowired
	OverShortReportService overShortReportService;

//
//	@RequestMapping(value = "/getReportByStoreNumberLaneId/{storeNumber}/{laneId}/{reportDate}", method = RequestMethod.GET, produces = "application/json")
//	public List<OverShortReport> getOverShortReportByStoreNumberLaneId(@PathVariable Long storeNumber,
//			@PathVariable Long laneId,
//			@PathVariable Timestamp reportDate) {
//
//		return overShortReportService.findOverShortReportByStoreNumberLaneId(storeNumber, laneId, reportDate);
//
//	}



//	@RequestMapping(value = "/getReportByStoreNumberReportDate/{storeNumber}/{reportDate}", method = RequestMethod.GET, produces = "application/json")
//	public List<OverShortReport>  getOverShortReportByStoreNumberReportDate(@PathVariable Long storeNumber,
//			@PathVariable Timestamp reportDate  ) {
//
//		OverShortReport overShortReport = new OverShortReport();
//		overShortReport.setStoreNumber(storeNumber);
//		overShortReport.setReportDate(reportDate);
//		List<OverShortReport> overShortReports=overShortReportService.findOverShortReportByStoreNumber(overShortReport);
//
//
//		return overShortReports;
//	}

   /*
    * Get OverShort Report By Store Number 
    *.../audittracking/overShortReport/getReportByStoreNumber/6979
    * 
    */
	@RequestMapping(value = "/getReportByStoreNumber/{storeNumber}", method = RequestMethod.GET, produces = "application/json")
	public List<OverShortReportDTO>  getOverShortReportByStoreNumber(@PathVariable Long storeNumber
			) {
		OverShortReport overShortReport = new OverShortReport();
		overShortReport.setStoreNumber(storeNumber);
		return overShortReportService.findOverShortReportByStoreNumber(overShortReport);
	}

	 /*
	    * Get OverShort Report By Report ID 
	    *.../audittracking/getOverShortReportByReportId/1
	    * 
	    */
	@RequestMapping(value = "/getReportByStoreNumberReportDate/{id}", method = RequestMethod.GET, produces = "application/json")
	public  OverShortReport   getOverShortReportByReportId(@PathVariable Long id) {

		return overShortReportService.findOverShortReportById(id);
	}

	/*
	 * Soft Delete OverShortReport
	 * 
	 * {"id":920}
	 * 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/deleteOverShortReport/", produces = "application/json")
	@ResponseBody
	public String deleteActivityLog(@RequestBody final LinkedHashMap<String, Object> entity) {
		Long id=new Long(0);
		for (Entry<String, Object> entry : entity.entrySet()) {
			//activityId
			if (entry.getKey().equalsIgnoreCase("id")) {
				  id= new Long(entry.getValue().toString());
			}  
		}
		
		OverShortReport overShortReport=overShortReportService.findOverShortReportById(id);
		overShortReport.setIsDeleted(true);
		overShortReport.setId(id); 
		overShortReportService.deleteReport(overShortReport);
		return id.toString();
	}




}
