package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.aholdusa.am.audittracking.entity.AMError;
import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.exception.InvalidRequestException;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.service.LaneHistoryService;
import com.aholdusa.am.audittracking.service.LaneService;
import com.aholdusa.am.audittracking.util.AMUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("laneassignments")
public class LaneAssignmentsController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// AbstractDBService a;
	@Autowired
	LaneService laneService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	LaneHistoryService laneHistoryService;
	// StoreService storeService;
	@Autowired
	AppsessionService appsessionService;

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void handlePost(final HttpServletRequest request, @RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response,
			BindingResult bindingResult) {
		// LinkedHashMap<String, LinkedHashMap> objList = (LinkedHashMap<String,
		// LinkedHashMap>) entity;
		try {

			String returnString = null;
			String appkey = null;
			String objectType = null;
			Object object = null;
			Integer storeNumber = null;
			HashMap<String, String> hm = new HashMap<String, String>();
			HashMap<String, String> objectMap = new HashMap<String, String>();
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			for (Entry<String, Object> entry : entity.entrySet()) {
				String entryType = entry.getValue().getClass().getCanonicalName();
				switch (entryType) {
				case "java.util.LinkedHashMap": {
					objectType = entry.getKey();
					for (Object o : ((LinkedHashMap<String, String>) entry.getValue()).entrySet().toArray()) {
						// logger.info("hashmap content .. " + o.toString());
						String arr[] = o.toString().split("=");

						hm.put(arr[0], arr[1]);
					}
					// objectMap.put("objectType", objectType);
					objectMap.put(objectType, gson.toJson(entry.getValue()));
				}
					break;
				case "java.lang.String": {
					if (entry.getKey().equalsIgnoreCase("UUID")) {
						appkey = (String) entry.getValue();
						Appsession appsession = null;
						List<Appsession> sessionList = this.appsessionService.findByKey(appkey);
						if (sessionList != null && sessionList.size() > 0)
							appsession = this.appsessionService.findByKey(appkey).get(0);
						if (appsession == null || appsession.getStoreNumber() == null
								|| appsession.getStoreNumber() <= 0)
							returnString = "{\"errorMessage\":\"key mismatch\"}";
						storeNumber = appsession.getStoreNumber();
					}
				}
				case "java.lang.Boolean":

				default:
					hm.put(entry.getKey(), entry.getValue().toString());
					break;
				}
				logger.info(
						"entry: " + entry.getKey() + "entry type: " + entry.getValue().getClass().getCanonicalName());
			}
			logger.info("hashmap contents:" + hm.toString());
			logger.info("objectMap contents:" + objectMap.toString());

			for (Entry<String, String> obj : objectMap.entrySet()) {
				// Gson gson = new Gson();
				switch (obj.getKey()) {
				case "Add": {
					Employee e = null;
					Lane l = null;
					Integer laneNumber = Integer.parseInt(hm.get("laneNumber"));
					logger.info("get lane details for: " + laneNumber);

					if (laneNumber != null && !laneNumber.equals("")) {
						Lane tmpLane = new Lane();
						tmpLane.setNumber(laneNumber);
						tmpLane.setStoreNumber(storeNumber);
						List<Lane> tmpLaneList = laneService.findByObject(tmpLane);
						if (tmpLaneList.size() != 1) {
							// something is wrong;;
							response.getOutputStream()
									.print(new AMError(509, "No unique lanes found",
											"{\"lane\":\"" + laneNumber + "\",\"store\":\"" + storeNumber + "\"}")
													.toString());
							return;
						}
						l = tmpLaneList.get(0);
					}
					if (l == null)
						logger.info("no lanes found");
					else
						logger.info(l.toString());
					String operatorNumber = hm.get("operatorNumber");
					logger.info("get employee details for: " + operatorNumber);
					if (operatorNumber != null && !operatorNumber.equals("")) {
						Employee tmpEmployee = new Employee();
						tmpEmployee.setStoreNumber(storeNumber);
						tmpEmployee.setOperatorNumber(Integer.parseInt(operatorNumber));
						List<Employee> tmpEmpList = employeeService.findByObject(tmpEmployee);
						if (tmpEmpList.size() != 1) {
							// something is wrong;;
							response.getOutputStream().print(new AMError(509, "No unique employees found",
									"{\"employee\":\"" + operatorNumber + "\",\"store\":\"" + storeNumber + "\"}")
											.toString());
							return;
						}
						e = tmpEmpList.get(0);
					}
					if (e == null)
						logger.info("no employees found");
					else
						logger.info(e.toString());
					if (l != null && e != null) {
						LaneHistory lh = new LaneHistory();
						lh.setEmployee(e);
						lh.setLane(l);
						String dateString = hm.get("date");
						Long startTime = System.currentTimeMillis();
						Timestamp currentDate = new Timestamp(System.currentTimeMillis());
						lh.setStartTime((new Timestamp(System.currentTimeMillis())));
						if (dateString != null && !dateString.equals("")) {
							 
							 currentDate =AMUtils.getTimestampFromString(dateString);
							 
						}
						lh.setLaneAssignmentDate(currentDate);
						lh.setIsDeleted(false);
						lh.setStoreNumber(storeNumber);
						lh.setVersion(1L);

						LaneHistory lh2 = new LaneHistory();
						lh2.setLane(l);
						lh2.setStoreNumber(storeNumber);
						lh2.setLaneAssignmentDate(currentDate);
						lh2.setEndTime(null);
						lh2.setIsDeleted(false);
						List<LaneHistory> lhlist2 = this.laneHistoryService.findLastLaneHistoryByDateAndLane(lh2);
						if (lhlist2.size() > 0) {
							lh2 = lhlist2.get(0);
							lh2.setEndTime(new Timestamp(System.currentTimeMillis()));
							this.laneHistoryService.update(lh2);
						}
						try{
						  this.laneHistoryService.insert(lh);
						 }catch(Exception ex){
								logger.info(ex.getMessage());
								throw new InvalidRequestException(NextManagerConstants.LANE_HISTORY_TRANSACTIONAL_ERROR.getValue(), bindingResult);
						}

					}
				}

					break;
				case "Remove": {
					Employee e = null;
					Lane l = null;
					// Store s = null;

					Integer laneNumber = Integer.parseInt(hm.get("laneNumber"));
					logger.info("get lane details for: " + laneNumber);

					if (laneNumber != null && !laneNumber.equals("")) {
						Lane tmpLane = new Lane();
						tmpLane.setNumber(laneNumber);
						tmpLane.setStoreNumber(storeNumber);
						List<Lane> tmpLaneList = laneService.findByObject(tmpLane);
						if (tmpLaneList.size() != 1) {
							// something is wrong;;
							response.getOutputStream()
									.print(new AMError(509, "No unique lanes found",
											"{\"lane\":\"" + laneNumber + "\",\"store\":\"" + storeNumber + "\"}")
													.toString());
							return;
						}
						l = tmpLaneList.get(0);
					}
					if (l == null)
						logger.info("no lanes found");
					else
						logger.info(l.toString());
					String operatorNumber = hm.get("operatorNumber");
					logger.info("get employee details for: " + operatorNumber);
					if (operatorNumber != null && !operatorNumber.equals("")) {
						Employee tmpEmployee = new Employee();
						tmpEmployee.setStoreNumber(storeNumber);
						tmpEmployee.setOperatorNumber(Integer.parseInt(operatorNumber));
						List<Employee> tmpEmpList = employeeService.findByObject(tmpEmployee);
						if (tmpEmpList.size() != 1) {
							// something is wrong;;

							response.getOutputStream().print(new AMError(509, "No unique employees found",
									"{\"employee\":\"" + operatorNumber + "\",\"store\":\"" + storeNumber + "\"}")
											.toString());
							return;
						}
						e = tmpEmpList.get(0);
					}
					if (e == null)
						logger.info("no employees found");
					else
						logger.info(e.toString());
					if (l != null && e != null) {
						LaneHistory tmpLaneHistory = new LaneHistory();
						tmpLaneHistory.setLane(l);
						tmpLaneHistory.setEmployee(e);
						tmpLaneHistory.setStoreNumber(storeNumber);
						tmpLaneHistory.setIsDeleted(false);
						tmpLaneHistory.setLaneAssignmentDate(AMUtils.getTimestampFromString(hm.get("date")));
						tmpLaneHistory.setStartTime(AMUtils.getTimestampFromString(hm.get("startTime")));
						List<LaneHistory> lhList = laneHistoryService.findByAssignment(tmpLaneHistory);
						if (lhList.size() != 1) {
							logger.info(lhList.toString());
							response.getOutputStream()
									.print(new AMError(509, "Unique lane history not found: " + lhList.size(),
											"{\"employee\":\"" + e.toString() + "\", \"date\":\"" + hm.get("date")
													+ "\", \"lane\":\"" + l.toString() + "\",\"store\":\"" + storeNumber
													+ "\"}").toString());
							return;
						}
						LaneHistory lh = lhList.get(0);
						lhList.clear();
						LaneHistory tmpLaneHistory2 = new LaneHistory();
						// tmpLaneHistory2.setEndTime(lh.getStartTime());
						tmpLaneHistory2.setLane(l);
						tmpLaneHistory2.setStoreNumber(storeNumber);
						tmpLaneHistory2.setIsDeleted(false);
						tmpLaneHistory2.setLaneAssignmentDate(AMUtils.getTimestampFromString(hm.get("date")));
						lhList = laneHistoryService.findByObject(tmpLaneHistory2);
						logger.info("trying to find previous employee 2: " + tmpLaneHistory2 + " " + lhList.size());
						LaneHistory lh2 =null;
						for(LaneHistory laneHistEndDate : lhList){
						  if(laneHistEndDate.getEndTime()!=null){
							  lh2=laneHistEndDate;
						  }
						}
						if (lhList.size() >= 1 && lh2!=null) {
							//LaneHistory lh2 = lhList.get(0);
							if (lh2 != null) {
								lh2.setEndTime(null);
								lh2.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
								try{
								   laneHistoryService.update(lh2);
								 }catch(Exception ex){
										logger.info(ex.getMessage());
										throw new InvalidRequestException(NextManagerConstants.LANE_HISTORY_TRANSACTIONAL_ERROR.getValue(), bindingResult);
								}
  
							}
						}
						try{
						  laneHistoryService.delete(lh);
						 }catch(Exception ex){
								logger.info(ex.getMessage());
								throw new InvalidRequestException(NextManagerConstants.LANE_HISTORY_TRANSACTIONAL_ERROR.getValue(), bindingResult);
						}
					}
				}
					break;
				default:
					break;

				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {

		}
	}

}