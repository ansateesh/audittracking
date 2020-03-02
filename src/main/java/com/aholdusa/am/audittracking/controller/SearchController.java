package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Audit;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.entity.Messages;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.Till;
import com.aholdusa.am.audittracking.exception.InvalidRequestException;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.AuditService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.service.LaneHistoryService;
import com.aholdusa.am.audittracking.service.LaneService;
import com.aholdusa.am.audittracking.service.MessageService;
import com.aholdusa.am.audittracking.service.TillService;
import com.aholdusa.am.audittracking.util.AMUtils;
import com.aholdusa.am.audittracking.util.ServiceUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Controller
@RequestMapping("search")
public class SearchController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AppsessionService appsessionService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private LaneService laneService;
	@Autowired
	private LaneHistoryService laneHistoryService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private TillService tillService;
	@Autowired
	private AuditService auditService;

	// @ResponseBody
	// @RequestMapping(value = "/getCleanLanes/", method = RequestMethod.POST)
	public void getCleanLanes(final HttpServletRequest request, @RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response) {
		String returnString = null;
		String appkey = null;
		String objectType = null;
		Object object = null;
		Integer storeNumber = null;
		Boolean greedy = Boolean.parseBoolean(request.getParameter("greedy"));
		if (greedy == null)
			greedy = false;
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (entry.getKey().equalsIgnoreCase("UUID")) {
				appkey = (String) entry.getValue();
				Appsession appsession = null;
				List<Appsession> sessionList = this.appsessionService.findByKey(appkey);
				if (sessionList != null && sessionList.size() > 0)
					appsession = this.appsessionService.findByKey(appkey).get(0);
				if (appsession == null || appsession.getStoreNumber() == null || appsession.getStoreNumber() <= 0)
					returnString = "{\"errorMessage\":\"key mismatch\"}";
				storeNumber = appsession.getStoreNumber();
			}
			// this.laneHistoryService.findAllByDateAndLane(lh)
		}
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void search(final HttpServletRequest request,
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response,
			BindingResult bindingResult) {
		Boolean greedy = Boolean.parseBoolean(request.getParameter("greedy"));
		if (greedy == null)
			greedy = false;

		String returnString = "{\"errorMessage\":\"success\"}";

		Object object = null;
		HashMap<String, HashMap<String, String>> map = ServiceUtil
				.parseRequest(this.appsessionService, entity);
		logger.info("parsed map:" + map.toString());
		HashMap<String, String> objectMap = map.get("objectMap");
		HashMap<String, String> hm = map.get("hm");
		logger.info("storeNumber:" + hm.get("storeNumber"));
		Integer storeNumber = null;
		if (hm.get("storeNumber") != null)
			storeNumber = Integer.parseInt(hm.get("storeNumber"));
		logger.info("storeNumber int:" + storeNumber);
		returnString = hm.get("returnString");
		String appkey = hm.get("appkey");
		String objectType = null;
		logger.info("storeNumber:" + storeNumber);
		logger.info("objectMap:" + objectMap.toString());

		/* 
		 * getClean Lanes is retrieving info using the uuid "UUID":"audittrackingclient<storeNumber>"
		 * if the Store Number is invalid :"audittrackingclientnull" request object is null for StoreNumber
		 * for that scenario the correct MSG will be invalid storeNumber  or invalid network name
		 * 
		 */
		if(appkey.contains("null") || appkey.contains("NULL")){
			throw new InvalidRequestException(NextManagerConstants.INVALID_NETWORK_NAME_ERROR.getValue(), bindingResult);
		}
		
		GsonBuilder builderForCleanLanes= new  GsonBuilder();

		//FIXES FOR STARTTIME AND CREATED DATE
		builderForCleanLanes.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() { 
			@Override  
			public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				String timeToString =json.getAsJsonPrimitive().toString();

				return	AMUtils.getTimestampFromString(timeToString);

			}
		});

		
		
		//FIXES FOR 2 DECIMALS
		builderForCleanLanes.registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {
	        @Override
	        public JsonElement serialize(final Double src, final Type typeOfSrc, final JsonSerializationContext context) {
	            BigDecimal value = BigDecimal.valueOf(src);
	            String amtFormattedValue=NumberFormat.getCurrencyInstance().format(value);
	            amtFormattedValue=amtFormattedValue.replaceAll("[( ),$]+", "");
	            if(value.doubleValue()<0){
	            	amtFormattedValue="-"+amtFormattedValue;
	            }
	            return new JsonPrimitive(amtFormattedValue);
	        }
	    }); 

		Gson gson =  builderForCleanLanes.create();			
				
		if (appkey == null || storeNumber == null) {

			returnString = "{}";
		} else {
			for (Entry<String, String> entry : objectMap.entrySet()) {
				logger.info("processing object:" + entry.getKey());
				switch (entry.getKey()) {
				case "Employee": {
					Employee employee = (Employee) gson.fromJson(
							entry.getValue(), Employee.class);
					employee.setStoreNumber(storeNumber);
					returnString = gson.toJson(this.employeeService
							.findByObject(employee, greedy));
				}
					break;
				case "Lane": {
					Lane lane = (Lane) gson.fromJson(entry.getValue(),
							Lane.class);
					lane.setStoreNumber(storeNumber);
					if (hm.get("cleanLane") != null
							&& hm.get("cleanLane").equalsIgnoreCase("true")) {
						logger.info("getting clean lanes");
						
						returnString = gson.toJson(this.laneService
								.getCleanLanes(storeNumber));
						logger.info("clean lanes: " + returnString);
						 
						
						break;
					}
					returnString = gson.toJson(this.laneService
							.findByObject(lane));
				}
					break;
				case "Messages": {
					Messages messages = (Messages) gson.fromJson(
							entry.getValue(), Messages.class);
					logger.info(messages.toString());
					returnString = gson.toJson(this.messageService
							.findByObject(messages));
				}
					break;
				case "Till": {
					java.util.Date date=null;
					Calendar cal = Calendar.getInstance();
					if(hm.get("date")!=null && !hm.get("date").equalsIgnoreCase("")){
						date=Date.valueOf(hm.get("date"));
					}else{
						  date=cal.getTime();
					}
					List<Till> resultList =this.tillService.getDrawer(storeNumber, date);
					System.out.println("till search result:" + resultList);
					gson = new Gson();
					returnString = gson.toJson(resultList);
				}
					break;
				case "Audit": {
					GsonBuilder builder= new  GsonBuilder();
				     
				     builder.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() { 
				    	@Override  
				    	 public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				    		String timeToString =json.getAsJsonPrimitive().toString();
				    		
				    		return	AMUtils.getTimestampFromString(timeToString);
				    		 
				    	   }
				    	});
				     
				    // gson =  builder.setDateFormat("yyyy-MM-dd").create();
				     
				     gson =  builder.create();
					//yyyy-MM-dd HH:mm:ss.SSS
					//gson = new GsonBuilder() .create();
					boolean byDate = false, byEmployee = false, byLane = false;

					Audit audit = (Audit) gson.fromJson(entry.getValue(),Audit.class);
					 
					
					audit.setStoreNumber(storeNumber);
					if (hm.get("date") != null) {
						byDate = true;
						logger.info("date: " + hm.get("date"));

						audit.setDate(AMUtils.getTimestampFromString(hm.get("date")));
					}
					if (hm.get("startTime") != null) {
						// byDate = true;
						logger.info("date: " + hm.get("date"));

						//audit.setStartTime(AMUtils.getTimestampFromString(hm.get("startTime")));
						audit.setStartTime(AMUtils.getTimestampFromString(hm.get("startTime")));

					}
					if (hm.get("operatorNumber") != null) {
						byEmployee = true;
						logger.info("operatorNumber: "
								+ hm.get("operatorNumber"));
						Employee employee = new Employee();
						employee.setOperatorNumber(Integer.parseInt(hm
								.get("operatorNumber")));
						audit.setEmployee(employee);
					}
					if (hm.get("laneNumber") != null) {
						byLane = true;
						logger.info("laneNumber: " + hm.get("laneNumber"));
						Lane lane = new Lane();
						lane.setNumber(Integer.parseInt(hm.get("laneNumber")));
						audit.setLane(lane);
					}

					if (byEmployee && !byDate) {
						logger.info("finding audits by employee");
						returnString = gson.toJson(this.auditService
								.findAllByEmployee(audit));

					} else if (byDate && !byEmployee && !byLane) {
						logger.info("finding audits by date");

						returnString = gson.toJson(this.auditService
								.findAllByDate(audit));
					} else if (byDate & byEmployee) {
						logger.info("finding audits by employee and date");

						returnString = gson.toJson(this.auditService
								.findAllByEmployeeAndDate(audit));
					} else if (byDate & byLane) {
						
						List<Audit>audits= this.auditService.findAllByLaneAndDate(audit);
						
						returnString = gson.toJson(this.auditService
								.findAllByLaneAndDate(audit));
						
						
					}
				}
					break;
				case "LaneHistory": {
					logger.info("searching for lane history");

					logger.info("hm:" + hm.toString());
					LaneHistory lh = new LaneHistory();
					lh.setStoreNumber(storeNumber);
					
				     DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				     
				     
				
					//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 		
							
							
					
					Calendar cal  = Calendar.getInstance();
					
					try {
						cal.setTime(df.parse(hm.get("date")));
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
					lh.setLaneAssignmentDate(new Timestamp(cal.getTimeInMillis()));
					
				//	lh.setLaneAssignmentDate(AMUtils.getTimestampFromString(hm.get("date")));
					Lane lane = new Lane();
					lane.setStoreNumber(storeNumber);
					boolean specificLane = false;
					if (hm.get("laneNumber") != null
							&& Integer.parseInt(hm.get("laneNumber")) > 0) {
						specificLane = true;
						lane.setNumber(Integer.parseInt(hm.get("laneNumber")));
					}
					List<Lane> laneList = this.laneService.findByObject(lane);
					logger.info("lanelist size:" + laneList.size());
					List<LaneHistory> lhList = new ArrayList();
					for (Lane l : laneList) {
						logger.info("lane : " + l.getNumber());
						LaneHistory laneHistory = null;
						lh.setLane(l);
						List<LaneHistory> lhList0;
						
						/*
						 * FIXES FOR CLEAN LANE LOGIC 
						 * endTime Validation is no needed anymore
						 * Eventually else statement will be removed 
						 * but for the moment let it be this "DEAD CODE" 
						 */
						if (!specificLane) {
							
							lhList0 = this.laneHistoryService
									.findAllByDateAndLane(lh);
									//.findLastLaneHistoryByDateAndLane(lh);
							logger.error("lane history list: "
									+ lhList0.toString());
						} else {
							lhList0 = this.laneHistoryService
									.findAllByDateAndLane(lh);
							logger.error("lane history list: "
									+ lhList0.toString());

						}
						if (lhList0 != null && lhList0.size() > 0) {

							/*
							 * Get the latest record only
							 * 
							 */
							
							if (hm.get("fullHistory") == null
									|| hm.get("fullHistory")
											.equalsIgnoreCase("false")){
								/*
								 * find all Lanes is in ASC order
								 * Latest record size()-1
								 */
								LaneHistory laneHistory0 = lhList0.get(lhList0.size()-1);
								laneHistory = laneHistory0;
								Audit tmpAudit = new Audit();
								tmpAudit.setLane(l);
								tmpAudit.setDate(AMUtils.getTimestampFromString(hm.get("date")));
								tmpAudit.setStoreNumber(storeNumber);
								List<Audit> auditList = this.auditService
										.findAllByLaneAndDate(tmpAudit);
								logger.info("audit list : " + auditList.toString());
								if (auditList != null && auditList.size() > 0) {
									Audit audit = auditList.get(0);
									laneHistory.setPosition(audit.getPosition());
									Double cashOverUnder = audit.getCashOverUnder();
									Double checkOverUnder = audit
											.getCheckOverUnder();
									Double overUnder = (cashOverUnder == null ? 0
											: cashOverUnder)
											+ (checkOverUnder == null ? 0
													: checkOverUnder);
									laneHistory.setOverUnder(overUnder);
									laneHistory.setLane(l);
									lhList.add(laneHistory);

									} else {
										laneHistory.setOverUnder(null);
										laneHistory.setLane(l);
										lhList.add(laneHistory);
									}
							}//end Lane Hist
							else{
								for (LaneHistory laneHistory0 : lhList0 )
								{

									laneHistory = laneHistory0;
									Audit tmpAudit = new Audit();
									tmpAudit.setLane(l);
									tmpAudit.setDate(AMUtils.getTimestampFromString(hm.get("date")));
									tmpAudit.setStoreNumber(storeNumber);
									List<Audit> auditList = this.auditService
											.findAllByLaneAndDate(tmpAudit);
									System.out.println(auditList);
									logger.info("audit list : " + auditList.toString());
									if (auditList != null && auditList.size() > 0) {
										Audit audit = auditList.get(0);
										laneHistory.setPosition(audit.getPosition());
										Double cashOverUnder = audit.getCashOverUnder();
										Double checkOverUnder = audit
												.getCheckOverUnder();
										Double overUnder = (cashOverUnder == null ? 0
												: cashOverUnder)
												+ (checkOverUnder == null ? 0
														: checkOverUnder);
										laneHistory.setOverUnder(overUnder);
										laneHistory.setLane(l);
										lhList.add(laneHistory);

									} else {
										laneHistory.setOverUnder(null);
										laneHistory.setLane(l);
										lhList.add(laneHistory);
									}
								}//end for
							}//else
							
						} else if (hm.get("suppressIfNoHistory") == null
								|| hm.get("suppressIfNoHistory")
										.equalsIgnoreCase("false")) {
							laneHistory = new LaneHistory();
							Employee employee = new Employee();
							employee.setFirstName("Open");
							laneHistory.setEmployee(employee);
							laneHistory.setLane(l);
							lhList.add(laneHistory);

						}
					}

					returnString = gson.toJson(lhList);
					logger.info("returnString:" + returnString);
				}
					break;
				default:
					logger.error("Do not know what to do with "
							+ entry.getKey());
					break;
				}

			}
		}
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			writer.print(returnString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}