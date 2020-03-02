package com.aholdusa.am.audittracking.dto;

 
import java.util.List;

//import org.hibernate.validator.constraints.NotEmpty;

import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.ReportActivityLogs;

public class ReportDTO {
	
	private List<Integer> storeNumbers;
	//@NotEmpty 
	private Integer       storeNumber;
	private Integer       operatorNumber;
	private String        division;
	//@NotEmpty  
	private String        beginDate;
	//@NotEmpty 
	private String        endDate;
	private List<Audit>   audits;
	private String        reportType;
	private List<ReportActivityLogs> activities;
	 

	 

	public List<ReportActivityLogs> getActivities() {
		return activities;
	}

	public void setActivities(List<ReportActivityLogs> activities) {
		this.activities = activities;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	
	public List<Audit> getAudits() {
		return audits;
	}

	public void setAudits(List<Audit> audits) {
		this.audits = audits;
	}

	public Integer getOperatorNumber() {
		return operatorNumber;
	}

	public void setOperatorNumber(Integer operatorNumber) {
		this.operatorNumber = operatorNumber;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	 



	public Integer getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Integer storeNumber) {
		this.storeNumber = storeNumber;
	}

	public List<Integer> getStoreNumbers() {
		return storeNumbers;
	}

	public void setStoreNumbers(List<Integer> storeNumbers) {
		this.storeNumbers = storeNumbers;
	}
	

}
