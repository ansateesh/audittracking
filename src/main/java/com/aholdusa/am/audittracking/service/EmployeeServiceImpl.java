package com.aholdusa.am.audittracking.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.EmployeeDAO;
import com.aholdusa.am.audittracking.entity.AMError;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.EmployeeConstants;

/**
 * Service implementation class for Employee. Business logic involved in
 * handling Employees should be implemented here.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 * 
 * @see com.aholdusa.core.base.service.DBService.java
 *
 */

@Service("employeeService")
public class EmployeeServiceImpl extends AMServiceImpl<Employee> implements
		EmployeeService {

	@Autowired
	private EmployeeDAO employeeDao;
	
	@Autowired
	private ActivityLogService activityLogsService;

	public List<Employee> findByName(Integer storeNumber, String name) {
		return this.employeeDao.findByName(storeNumber, name, name);
	}

	public List<Employee> findByOperatorNumber(Integer storeNumber,
			Integer operatorNumber) {
		return this.employeeDao.findByOperatorNumber(storeNumber,
				operatorNumber);
	}

	public AMError processEmployee(Employee employee) {
		AMError amerror = new AMError();
		Employee tmp;
		if (employee.getId() != null && employee.getId() > 0L) {
			tmp = employeeDao.findByID(employee.getId());
		}
		return amerror;
	}

	public void delete(Employee tmp) {
		employeeDao.delete(tmp);
		//createEmployeeActivity(tmp);
	}

	public void update(Employee e) {

		employeeDao.save(e);
		//createEmployeeActivity(e);
	}

	public void insert(Employee e) {
		employeeDao.save(e);
		//createEmployeeActivity(e);
	}

	public List findByJsonObject(String jsonString) {
		return null;
	}

	public String lockEmployeeForUpdate(Employee employee, String key) {
		employee.setRecordStatus(2);
		employee.setLockedBy(key);
		employeeDao.save(employee);
		return "{\"UUID\":\"" + key + "\",\"id\":" + employee.getId() + "}";
	}

	public String unlockEmployee(Employee employee) {
		employee.setRecordStatus(0);
		employee.setLockedBy(null);
		employeeDao.save(employee);
		return "{}";
	}

	public List<Employee> findByObject(Employee employee, Boolean greedy) {
		if (!greedy) {
			return super.findByObject(employee);
		} else {
			if (employee.getOperatorNumber() != null
					&& employee.getOperatorNumber() > 0) {
				return employeeDao
						.partialFindByOperatorNumber(employee.getStoreNumber(),
								employee.getOperatorNumber());
			} else if (employee.getFirstName() != null
					&& !employee.getFirstName().equals("")) {
				return employeeDao.partialFindByName(employee.getStoreNumber(),
						employee.getFirstName());
			} else if (employee.getLastName() != null
					&& !employee.getLastName().equals("")) {
				return employeeDao.partialFindByName(employee.getStoreNumber(),
						employee.getLastName());
			} else
				return super.findByObject(employee);
		}
	}

	@Override
	public String getEmployeeFirstName(String nameOperator) {
		String delims = " ";
		String[] operatorNames = nameOperator.split(delims);
		String firstName = "";
		if (operatorNames.length > 0) {
			firstName = operatorNames[0];
		}
		return firstName;
	}

	@Override
	public String getEmployeeLastName(String nameOperator) {
		String delims = " ";
		String[] operatorNames = nameOperator.split(delims);
		String LastName = "";
		if (operatorNames.length == 2) {
			LastName = operatorNames[1];
		} else if (operatorNames.length == 3) {
			LastName = operatorNames[2];
		}
		return LastName;
	}
	
	
	/**
	 * @param employee
	 */
	@Deprecated
	/* @deprecated As of release Yellow
	    * this activity is no longer used by push notification server   
	    */
	private void createEmployeeActivity(Employee employee) {
		ActivityLogs activityLogEmployee =new ActivityLogs();
		activityLogEmployee.setActivityType(EmployeeConstants.EMPLOYEE_UPDATED.getValue());
		activityLogEmployee.setEmpOpNum(9999);
		activityLogEmployee.setStoreNumber(employee.getStoreNumber());
		activityLogEmployee.setCreateDate( new Timestamp(System.currentTimeMillis())); 
		activityLogEmployee.setCreatedBy(EmployeeConstants.ADMIN_USER.getValue());
		activityLogEmployee.setMgrReason(EmployeeConstants.EMPLOYEE_UPDATED.getValue());
		activityLogsService.insert(activityLogEmployee);
	}

}