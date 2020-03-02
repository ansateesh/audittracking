package com.aholdusa.am.audittracking.entity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:AudittrackingMessagesEN.properties")
public class ErrorMessages {

	@Value("${LANE_TRANSACTIONAL_ERROR}")
	private String laneTransError;
	@Value("${EMPLOYEE_TRANSACTIONAL_ERROR}")
	private String employeeTransError;
	@Value("${EMPLOYEE_NONTRANSACTIONAL_ERROR}")
	private String employeeNonTransError;
	@Value("${AUDIT_TRANSACTIONAL_ERROR}")
	private String auditTransError;
	@Value("${INVALID_NETWORK_NAME_ERROR}")
	private String networkOrDnsError;
	@Value("${NOT_RESOLVABLE_POS_URL_ERROR}")
	private String urlIspPosError;
	
	public String getLaneTransError() {
		return laneTransError;
	}
	public void setLaneTransError(String laneTransError) {
		this.laneTransError = laneTransError;
	}
	public String getEmployeeTransError() {
		return employeeTransError;
	}
	public void setEmployeeTransError(String employeeTransError) {
		this.employeeTransError = employeeTransError;
	}
	public String getEmployeeNonTransError() {
		return employeeNonTransError;
	}
	public void setEmployeeNonTransError(String employeeNonTransError) {
		this.employeeNonTransError = employeeNonTransError;
	}
	public String getAuditTransError() {
		return auditTransError;
	}
	public void setAuditTransError(String auditTransError) {
		this.auditTransError = auditTransError;
	}
	public String getNetworkOrDnsError() {
		return networkOrDnsError;
	}
	public void setNetworkOrDnsError(String networkOrDnsError) {
		this.networkOrDnsError = networkOrDnsError;
	}
	public String getUrlIspPosError() {
		return urlIspPosError;
	}
	public void setUrlIspPosError(String urlIspPosError) {
		this.urlIspPosError = urlIspPosError;
	}
	
}
