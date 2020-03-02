package com.aholdusa.am.audittracking.controller;
 

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.aholdusa.am.audittracking.dto.ReportDTO;
import com.aholdusa.am.audittracking.entity.ReportConstants;
import com.aholdusa.am.audittracking.service.ReportService;

/**
 * @author nvixa3
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminReportController {

	@Autowired
	ReportService reportService;	

	/**
	 * @param reportDTO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	//@RequestMapping(value = "/report", method = RequestMethod.GET)
	public ModelAndView setReport(){
		ReportDTO reportDTO =new ReportDTO();
		ModelAndView model = new ModelAndView("admin");
		model.addObject("storeNumberList", reportService.getStoreNumber());
		model.addObject("reportDTO", reportDTO);
		return model;
	}

	/**
	 * @param reportDTO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	//@RequestMapping(value = "/getReport", method = RequestMethod.POST)
	public ModelAndView getAuditReport(@ModelAttribute("reportDTO") ReportDTO reportDTO,HttpSession session ) throws Exception {
		ModelAndView model =null;
		if(reportDTO.getReportType().equalsIgnoreCase(ReportConstants.REPORT_TYPE_AUDIT.getValue())){
			reportDTO.setAudits(reportService.getAuditReport(reportDTO));
			model =new ModelAndView("auditReport");		
		}else if(reportDTO.getReportType().equalsIgnoreCase(ReportConstants.REPORT_TYPE_OVERRIDE.getValue())){
			reportDTO.setActivities(reportService.getActivityLogReport(reportDTO));
			model =new ModelAndView("activityReport");
		}else if(reportDTO.getReportType().equalsIgnoreCase(ReportConstants.REPORT_TYPE_MARK.getValue())){
			reportDTO.setActivities(reportService.getActivityLogForSearchCriteria(reportDTO));
			model =new ModelAndView("activityReport");
		}else if(reportDTO.getReportType().equalsIgnoreCase(ReportConstants.REPORT_TYPE_UNMARK.getValue())){
			reportDTO.setActivities(reportService.getActivityLogForSearchCriteria(reportDTO));
			model =new ModelAndView("activityReport");
		}else if(reportDTO.getReportType().equalsIgnoreCase(ReportConstants.REPORT_TYPE_DESELECT.getValue())){
			reportDTO.setActivities(reportService.getActivityLogForSearchCriteria(reportDTO));
			model =new ModelAndView("activityReport");
		}
		model.addObject("storeNumberList", reportService.getStoreNumber());
		model.addObject("reportDTO", reportDTO);
		return model;
	}



}
