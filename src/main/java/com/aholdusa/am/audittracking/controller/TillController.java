package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.entity.Till;
import com.aholdusa.am.audittracking.entity.TillConstants;
import com.aholdusa.am.audittracking.service.AMService;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.LaneService;
import com.aholdusa.am.audittracking.service.StoreService;
import com.aholdusa.am.audittracking.service.TillService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Controller for Lanes. Uses the abstract controller methods to handle the
 * CRUD/Fetch operations on lanes.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.controller.AbstractController.java
 */
@Controller
@RequestMapping("till")
public class TillController extends AMController {
	@Autowired
	private AppsessionService appsessionService;
	@Autowired
	private TillService tillService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private LaneService laneService;
	@Autowired
	private ActivityLogService activityLogsService;

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void handlePost(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response) {
		String returnString = "{\"errorMessage\":\"success\"}";

		String appkey = null;
		String objectType = null;
		Object object = null;
		Integer storeNumber = null;
		// Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
		// .create();
		// JsonDeserializer currencyDeserializer = (JsonDeserializer) new
		// CurrencyDeserializer();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
		// .registerTypeAdapter(Currency.class, currencyDeserializer)
				.create();
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (entry.getKey().equalsIgnoreCase("UUID")) {
				appkey = (String) entry.getValue();
				Appsession appsession = null;
				List<Appsession> sessionList = this.appsessionService
						.findByKey(appkey);
				if (sessionList != null && sessionList.size() > 0)
					appsession = this.appsessionService.findByKey(appkey)
							.get(0);
				if (appsession == null || appsession.getStoreNumber() == null
						|| appsession.getStoreNumber() <= 0) {
					returnString = "{\"errorMessage\":\"no store associated with key\"}";
					break;
				}
				storeNumber = appsession.getStoreNumber();
			} else {
				Till tmpTill = (Till) gson.fromJson(
						gson.toJson(entry.getValue()), Till.class);
				tmpTill.setStoreNumber(storeNumber);
				this.tillService.insert(tmpTill);
			}
		}
		PrintWriter writer = null;
		
		ActivityLogs activityLogChangeDrawer =new ActivityLogs();
		activityLogChangeDrawer.setActivityType(TillConstants.CHANGE_DRAWER_UPDATED.getValue());
		activityLogChangeDrawer.setEmpOpNum(9999);
		activityLogChangeDrawer.setStoreNumber(storeNumber);
		activityLogChangeDrawer.setCreateDate( new Timestamp(System.currentTimeMillis())); 
		activityLogChangeDrawer.setCreatedBy(TillConstants.ADMIN_USER.getValue());
		activityLogChangeDrawer.setMgrReason(TillConstants.MANAGER_REASON.getValue());
		activityLogsService.insert(activityLogChangeDrawer);
		
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

	/**
	 * @param entity
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/getDrawer", method = RequestMethod.POST)
	public void handleTillGetDrawerPost(
			@RequestBody final LinkedHashMap<String, String> entity,
			HttpServletResponse response) {
		String returnString = "{\"errorMessage\":\"Failed\"}";
		Store store = null;
		String appKey = null;
		Integer storeNumber = null;

		if (entity != null) {
			// Get Store Info
			if (entity.get("UUID") != null) {
				appKey = entity.get("UUID");
				Appsession appsession = null;
				List<Appsession> sessionList = this.appsessionService
						.findByKey(appKey);
				if (sessionList != null && sessionList.size() > 0)
					appsession = this.appsessionService.findByKey(appKey)
							.get(0);
				if (appsession == null || appsession.getStoreNumber() == null
						|| appsession.getStoreNumber() <= 0) {
					returnString = "{\"errorMessage\":\"key mismatch\"}";
				} else {
					storeNumber = appsession.getStoreNumber();
					store = this.storeService.getStoreByNumber(storeNumber);
				}
			}

			if (appKey != null && store != null) {
				logger.info("Store Number :" + store.getNumber()
						+ " Default Drawer:" + store.getDefaultDrawer());
				returnString = "{\"UUID\":\"" + appKey
						+ "\",\"DefaultDrawer\":\"" + store.getDefaultDrawer()
						+ "\"" + "}";

			} else {
				returnString = "{\"errorMessage\":\"key mismatch\"}";
			}
		}
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			// request.addHeader("content-type", "application/json");
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

	/**
	 * @param entity
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/setDrawer", method = RequestMethod.POST)
	public void handleTillSetDrawerPost(
			@RequestBody final LinkedHashMap<String, String> entity,
			HttpServletResponse response) {
		String returnString = "{\"errorMessage\":\"Failed\"}";
		Store store = null;
		String appKey = null;
		Integer storeNumber = null;
		Double defaultDrawerAmount = null;

		if (entity != null) {
			// Get Store Info
			if (entity.get("UUID") != null) {
				appKey = entity.get("UUID");
				Appsession appsession = null;
				List<Appsession> sessionList = this.appsessionService
						.findByKey(appKey);
				if (sessionList != null && sessionList.size() > 0)
					appsession = this.appsessionService.findByKey(appKey)
							.get(0);
				if (appsession == null || appsession.getStoreNumber() == null
						|| appsession.getStoreNumber() <= 0) {
					returnString = "{\"errorMessage\":\"key mismatch\"}";
				} else {
					storeNumber = appsession.getStoreNumber();
					store = this.storeService.getStoreByNumber(storeNumber);
				}
			}

			if (entity.get("defaultDrawerAmount") != null && store != null) {
				defaultDrawerAmount = new Double(
						entity.get("defaultDrawerAmount"));
				store.setDefaultDrawer(defaultDrawerAmount);
				storeService.update(store);
			}

			if (appKey != null && defaultDrawerAmount != null && store != null) {
				logger.info("Store Number :" + store.getNumber()
						+ " Default Drawer Amount:" + defaultDrawerAmount);
				returnString = "{\"UUID\":\"" + appKey
						+ "\",\"DefaultDrawerAmount\":\""
						+ store.getDefaultDrawer() + "\"" + "}";

			} else {
				returnString = "{\"errorMessage\":\"key mismatch\"}";
			}
		}
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			// request.addHeader("content-type", "application/json");
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

	/**
	 * @param entity
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/getDefaultTillAmount", method = RequestMethod.POST)
	public void handleTillGetDefaultAmountPost(
			@RequestBody final LinkedHashMap<String, String> entity,
			HttpServletResponse response) {
		String returnString = "Failed";
		String appKey = null;
		Integer storeNumber = null;
		Integer laneNumber = null;

		if (entity != null) {
			// Get Store Info
			if (entity.get("UUID") != null) {
				appKey = entity.get("UUID");
				Appsession appsession = null;
				List<Appsession> sessionList = this.appsessionService
						.findByKey(appKey);
				if (sessionList != null && sessionList.size() > 0)
					appsession = this.appsessionService.findByKey(appKey)
							.get(0);
				if (appsession == null || appsession.getStoreNumber() == null
						|| appsession.getStoreNumber() <= 0) {
					returnString = "{\"errorMessage\":\"key mismatch\"}";
				} else {
					storeNumber = appsession.getStoreNumber();
				}
			}

			// Get Lane Number
			if (entity.get("LaneNumber") != null) {
				laneNumber = new Integer(entity.get("LaneNumber"));
			}
			logger.info("LINE NUMBER To Be Updated :" + laneNumber);

			Lane lane = laneService.findByLaneNumber(storeNumber, laneNumber);

			if (appKey != null && lane != null) {
				logger.info("Store Number :" + storeNumber);
				returnString = "{\"UUID\":\"" + appKey
						+ "\",\"DefaultTillAmount\":\""
						+ lane.getDefaultTillAmount() + "\"" + "}";

			} else {
				returnString = "{\"errorMessage\":\"key mismatch\"}";
			}
		}
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			// request.addHeader("content-type", "application/json");
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

	/**
	 * @param entity
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/setDefaultTillAmount", method = RequestMethod.POST)
	public void handleTillSetDefaultAmountPost(
			@RequestBody final LinkedHashMap<String, String> entity,
			HttpServletResponse response) {
		String returnString = "Failed";
		String appKey = null;
		Integer storeNumber = null;
		Double tillAmount = null;

		if (entity != null) {
			// Get Store Info
			if (entity.get("UUID") != null) {
				appKey = entity.get("UUID");
				Appsession appsession = null;
				List<Appsession> sessionList = this.appsessionService
						.findByKey(appKey);
				if (sessionList != null && sessionList.size() > 0)
					appsession = this.appsessionService.findByKey(appKey)
							.get(0);
				if (appsession == null || appsession.getStoreNumber() == null
						|| appsession.getStoreNumber() <= 0) {
					returnString = "{\"errorMessage\":\"key mismatch\"}";
				} else {
					storeNumber = appsession.getStoreNumber();

				}
			}

			// Get Default Till Amount
			if (entity.get("defaultTillAmount") != null) {
				tillAmount = new Double(entity.get("defaultTillAmount"));
			}

			if (appKey != null && tillAmount != null) {
				logger.info("Store Number :" + storeNumber);
				List<Lane> lanes = laneService
						.getLanesByStoreNumber(storeNumber);
				if (lanes != null) {
					for (Lane lane : lanes) {
						lane.setDefaultTillAmount(tillAmount);
						laneService.update(lane);
					}
				}
				int updatedLanes = lanes != null ? lanes.size() : 0;
				returnString = "{\"UUID\":\"" + appKey + "\",\"Response\":\""
						+ "Success" + "\"" + ",\"Updated Lanes\":\""
						+ updatedLanes + "\"" + "}";

			} else {
				returnString = "{\"errorMessage\":\"key mismatch\"}";
			}
		}

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			// request.addHeader("content-type", "application/json");
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

	@Override
	public AMService getService() {
		// TODO Auto-generated method stub
		return this.tillService;
	}
}