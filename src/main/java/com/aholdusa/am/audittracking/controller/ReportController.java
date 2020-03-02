package com.aholdusa.am.audittracking.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JsonExporter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.AmConstants;
import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.ReportActivityLogs;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.AuditService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.service.LaneHistoryService;
import com.aholdusa.am.audittracking.service.LaneService;

@Controller
@RequestMapping("reports")
public class ReportController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	LaneHistoryService laneHistoryService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private LaneService laneService;
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	ActivityLogService activityLogService;


	@ResponseBody
	@RequestMapping(value = "/runreport", method = RequestMethod.GET)
	public void find(final HttpServletRequest request,
			HttpServletResponse response) {

		// Fixes for AT 311
		String responseType = request.getParameter("outputFormat");
		String reportName = request.getParameter("reportName");
		String storeNumber = request.getParameter("storeNumber");
		String operatorNumber = request.getParameter("operatorNumber");
		String laneNumber = request.getParameter("laneNumber");
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");

		Employee employee = null;
		Lane lane = null;
		Date beginDate = null;
		Date endDate = null;

		InputStream inputStream = null;
		OutputStream outputStream = null;
		FileInputStream finputStream = null;
		inputStream = getClass().getClassLoader().getResourceAsStream(
				"jrxml/" + reportName + ".jrxml");
		String outFileName = null;
		File outFile = null;
		try {

			HashMap hm = new HashMap();
			JRExporter exporter = null;
			Audit auditObject = new Audit();

			if (storeNumber != null) {

				// Set Employee
				if (operatorNumber != null) {
					employee = new Employee();
					employee.setOperatorNumber(new Integer(operatorNumber));
					auditObject.setEmployee(employee);
				}

				// Set Lane
				if (laneNumber != null) {
					lane = new Lane();
					lane.setNumber(new Integer(laneNumber));
					auditObject.setLane(lane);
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date dateParsed = null;
				// Set Start Time
				if (startDateStr != null) {
					try {
						dateParsed = dateFormat.parse(startDateStr);
						beginDate = new java.sql.Date(dateParsed.getTime());
					} catch (ParseException e) {
						throw new AuditTrackingException(
								"Invalid BeginDate Format /  Audit Report");
					}

				} else {
					throw new AuditTrackingException(
							"Invalid Begin Date /  Audit Report");
				}

				// Set End Time
				if (endDateStr != null) {
					try {
						dateParsed = dateFormat.parse(endDateStr);
						endDate = new java.sql.Date(dateParsed.getTime());
					} catch (ParseException e) {
						throw new AuditTrackingException(
								"Invalid EndDate Format /  Audit Report");
					}
				} else {
					// if end date is null then set current date
					endDate = new java.sql.Date(new java.util.Date().getTime());
				}
			}

			Enumeration<String> parameters = request.getParameterNames();
			try {
				while (parameters.hasMoreElements()) {
					String name = parameters.nextElement();
					logger.info("Setting bean property : " + name + " to "
							+ request.getParameter(name));
					BeanUtils.setProperty(auditObject, name,
							request.getParameter(name));
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// List<Audit> resultList = auditService.findByObject(auditObject);
			List<Audit> resultList = auditService.findByEmployeeLaneAndDate(
					auditObject, beginDate, endDate);

			logger.info("setting jrd");
			JasperReport jasperReport = JasperCompileManager
					.compileReport(inputStream);
			JRDataSource jrd = new JRBeanCollectionDataSource(resultList);
			hm.put("STORE_PARAM", storeNumber);
			hm.put("OPNUM_PARAM", operatorNumber);
			hm.put("LANENUMBER_PARAM", laneNumber);
			hm.put("START_DATE", startDateStr);
			hm.put("END_DATE", endDateStr);
			hm.put("DS1", jrd);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, hm,
					jrd);
			switch (AmConstants.valueOf(responseType)) {
			case XML:
				outFileName = reportName + ".xml";
				outFile = new File(outFileName);
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				exporter = new JRXmlExporter();
				response.setContentType("application/xml");
				break;
			case XLS:
				outFileName = reportName + ".xls";
				outFile = new File(outFileName);
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				exporter = new JRXlsExporter();
				response.setContentType("application/vnd.ms-excel");
				break;
			case JSON:
				outFileName = reportName + ".json";
				outFile = new File(outFileName);
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				exporter = new JsonExporter();
				response.setContentType("text/html");
				break;
			case PDF:
			default:
				outFileName = "Audit.pdf";
				outFile = new File(outFileName);
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				exporter = new JRPdfExporter();
				response.setContentType("application/pdf");
			}
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					outFileName);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.exportReport();
			logger.info("Exporter:" + exporter.toString());

			finputStream = new FileInputStream(outFile);
			outputStream = response.getOutputStream();
			IOUtils.copy(finputStream, outputStream);

		} catch (IOException ioe) {
			logger.error(ioe.getStackTrace().toString());
			ioe.printStackTrace();
		} catch (JRException jre) {
			// TODO Auto-generated catch block
			logger.error(jre.getStackTrace().toString());
			jre.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(finputStream);

		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getActivityLogReport", method = RequestMethod.GET)
	public void getActivityLogReport(final HttpServletRequest request,
			HttpServletResponse response) {
 
		
		String responseType = request.getParameter("outputFormat");
		String reportName = request.getParameter("reportName");
		String storeNumber = request.getParameter("storeNumber");
		String managerOperatorNumber = request.getParameter("managerOperatorNumber");
		String activityType = request.getParameter("activityType");
		String objectType= request.getParameter("objectType");
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		Date beginDate = null;
		Date endDate = null;
		List <ReportActivityLogs> activitiesReportList=null;
		 ActivityLogs activityLog=null;
		 try{
			 
			 InputStream inputStream = null;
			 OutputStream outputStream = null;
			 FileInputStream finputStream = null;
			 inputStream = getClass().getClassLoader().getResourceAsStream(
					 "jrxml/" + reportName + ".jrxml");
			 String outFileName = null;
			 File outFile = null; 
			 HashMap hm = new HashMap();
			 JRExporter exporter = null;
				
			 if (storeNumber != null && !storeNumber.equals("")) {
              activityLog=new ActivityLogs();
              activityLog.setStoreNumber(new Integer(storeNumber));
              
					//Activity Type
					if (activityType != null && !activityType.equals("")) {
						activityLog.setActivityType(activityType);
					}
					
					//Object Type
					if (objectType != null && !objectType.equals("")) {
						activityLog.setObjectType(objectType);
					}
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date dateParsed = null;
					// Set Start Time
					if (startDateStr != null) {
						try {
							dateParsed = dateFormat.parse(startDateStr);
							beginDate = new java.sql.Date(dateParsed.getTime());
						} catch (ParseException e) {
							throw new AuditTrackingException(
									"Invalid BeginDate Format /  Audit Report");
						}

					} else {
						throw new AuditTrackingException(
								"Invalid Begin Date /  Audit Report");
					}

					// Set End Time
					if (endDateStr != null) {
						try {
							dateParsed = dateFormat.parse(endDateStr);
							endDate = new java.sql.Date(dateParsed.getTime());
						} catch (ParseException e) {
							throw new AuditTrackingException(
									"Invalid EndDate Format /  Audit Report");
						}
					} else {
						// if end date is null then set current date
						endDate = new java.sql.Date(new java.util.Date().getTime());
					}
					// Set Manager Op Number
					if (managerOperatorNumber != null && !managerOperatorNumber.equals("")) {
						 
						 activityLog.setMgrOpNum(new Integer(managerOperatorNumber));
						 activitiesReportList=activityLogService.findActivityByManagerBeginEndDate(activityLog, beginDate, endDate);
						 
					}else{
						 activitiesReportList=activityLogService.findActivityByBeginEndDate(activityLog, beginDate, endDate);
					}
					
				}//End Params Validations
			 
			 Enumeration<String> parameters = request.getParameterNames();
				try {
					while (parameters.hasMoreElements()) {
						String name = parameters.nextElement();
						logger.info("Setting bean property : " + name + " to "
								+ request.getParameter(name));
						BeanUtils.setProperty(activityLog, name,
								request.getParameter(name));
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			 
		// Set Data To Report
				
				logger.info("setting jrd");
				JasperReport jasperReport = JasperCompileManager
						.compileReport(inputStream);
				JRDataSource jrd = new JRBeanCollectionDataSource(activitiesReportList);
				hm.put("DS1", jrd);

				JasperPrint print = JasperFillManager.fillReport(jasperReport, hm,jrd);
				switch (AmConstants.valueOf(responseType)) {
				case XML:
					outFileName = reportName + ".xml";
					outFile = new File(outFileName);
					if (!outFile.exists()) {
						outFile.createNewFile();
					}
					exporter = new JRXmlExporter();
					response.setContentType("application/xml");
					break;
				case XLS:
					outFileName = reportName + ".xls";
					outFile = new File(outFileName);
					if (!outFile.exists()) {
						outFile.createNewFile();
					}
					exporter = new JRXlsExporter();
					response.setContentType("application/vnd.ms-excel");
					break;
				case JSON:
					outFileName = reportName + ".json";
					outFile = new File(outFileName);
					if (!outFile.exists()) {
						outFile.createNewFile();
					}
					exporter = new JsonExporter();
					response.setContentType("text/html");
					break;
				case PDF:
				default:
					outFileName = "Audit.pdf";
					outFile = new File(outFileName);
					if (!outFile.exists()) {
						outFile.createNewFile();
					}
					exporter = new JRPdfExporter();
					response.setContentType("application/pdf");
				}
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
						outFileName);
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				exporter.exportReport();
				logger.info("Exporter:" + exporter.toString());

				finputStream = new FileInputStream(outFile);
				outputStream = response.getOutputStream();
				IOUtils.copy(finputStream, outputStream);
				
			

		} catch ( Exception ioe) {
			logger.error(ioe.getStackTrace().toString());
			ioe.printStackTrace();
		}   finally {
			 

		}
	}
}