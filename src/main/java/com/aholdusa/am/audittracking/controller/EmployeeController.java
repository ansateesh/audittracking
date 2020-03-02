package com.aholdusa.am.audittracking.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aholdusa.am.audittracking.entity.AmConstants;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.exception.InvalidRequestException;
import com.aholdusa.am.audittracking.service.AMService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Controller for Employees. Uses the abstract controller methods to handle the
 * CRUD/Fetch operations on employees.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.controller.AbstractController.java
 */

@Controller
@RequestMapping("employee")
public class EmployeeController extends AMController<Employee> {
	@Autowired
	EmployeeService employeeService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@ResponseBody
	@RequestMapping(value = "/findByObject", method = RequestMethod.GET)
	public List<Employee> findByObject(final HttpServletRequest request) {
		List<Employee> employeeList = null;
		String lastName = request.getParameter("lastName");
		logger.info("getting details for employee:" + lastName);
		Employee employee = new Employee();
		employee.setLastName(lastName);
		employeeList = this.employeeService.findByObject(employee);

		return employeeList;
	}

	@ResponseBody
	@RequestMapping(value = "/getEmployeesList", method = RequestMethod.POST)
	public String getEmployeesList(final HttpServletRequest request,
			@RequestBody final JsonObject entity) {
		logger.info("entity : " + entity.toString());
		Gson gson = new Gson();
		Employee employee = gson.fromJson(entity, Employee.class);
		return gson.toJson(employee);

	}

	@ResponseBody
	@RequestMapping(value = "/getEmployees", method = RequestMethod.POST)
	public String findByOperatorNumber(final HttpServletRequest request,
			@RequestBody final Object entity,
			BindingResult bindingResult) {
		List<Employee> employeeList = null;

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		LinkedHashMap<String, LinkedHashMap> objList = (LinkedHashMap<String, LinkedHashMap>) entity;
		try {
			for (Entry<String, LinkedHashMap> obj : objList.entrySet()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				for (Object o : obj.getValue().entrySet().toArray()) {
					String arr[] = o.toString().split("=");
					hm.put(arr[0], arr[1]);
				}
				Employee employee = new Employee();
				// employee.setStoreNumber(hm.get("storeNumber"));
				employeeList = this.employeeService.findByObject(employee);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
			throw new InvalidRequestException(NextManagerConstants.EMPLOYEE_NONTRANSACTIONAL_ERROR.getValue(), bindingResult);
		}
		return gson.toJson(employeeList);
	}

	@ResponseBody
	@RequestMapping(value = "/getEmployeeTest1", method = RequestMethod.POST)
	public List<Employee> getEmployeeTest1(final HttpServletRequest request,
			@RequestBody final Object entity, HttpServletResponse response) {
		List empList = new ArrayList();
		LinkedHashMap<String, LinkedHashMap> objList = (LinkedHashMap<String, LinkedHashMap>) entity;
		for (Entry<String, LinkedHashMap> s : objList.entrySet()) {
			logger.info("current line:"
					+ Thread.currentThread().getStackTrace()[0].getLineNumber());
			Gson gson = new Gson();
			switch (AmConstants.valueOf(s.getKey())) {
			case Employee:
				Employee tmpEmployee = gson.fromJson(gson.toJson(s.getValue()),
						Employee.class);
				empList.add(this.employeeService.findByObject(tmpEmployee));
				break;
			default:
				break;
			}
		}
		return empList;
	}

	@Override
	public AMService<Employee> getService() {
		// TODO Auto-generated method stub
		return this.employeeService;
	}

}