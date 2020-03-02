package com.aholdusa.am.audittracking.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.google.gson.Gson;

/**
 * Utility class for Service class methods. Include all methods that perform
 * operation to support business logic in the service layer.
 * 
 * @author nusxr42
 *
 */
public class ServiceUtil {

	private static Logger logger = LoggerFactory.getLogger(ServiceUtil.class);

	// @Autowired
	// static private AppsessionService appsessionService;

	public static HashMap<String, HashMap<String, String>> parseRequest(
			AppsessionService appsessionService,
			LinkedHashMap<String, Object> entity) {
		
		 
		HashMap<String, HashMap<String, String>> retMap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> hm = new HashMap<String, String>();
		HashMap<String, String> objectMap = new HashMap<String, String>();
		String objectType = "";
		Gson gson = new Gson();
		String returnString = "";
		Integer storeNumber = null;
		String appkey = null;
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (entry.getValue() == null) {
				logger.error("entry null: " + entry.getKey());
				continue;
			}
			String entryType = entry.getValue().getClass().getCanonicalName();
			logger.info("parsing: " + entryType);
			switch (entryType) {
			case "java.util.LinkedHashMap": {
				logger.info("LinkedHashMap");
				objectType = entry.getKey();
				for (Object o : ((LinkedHashMap<String, String>) entry
						.getValue()).entrySet().toArray()) {
					// logger.info("hashmap content .. " + o.toString());
					String arr[] = o.toString().split("=");
                  
					 
					    hm.put(arr[0], arr[1]);
                   
				}
				objectMap.put(objectType, gson.toJson(entry.getValue()));
			}
				break;
			case "java.lang.String": {
				logger.info("string");
				if (entry.getKey().equalsIgnoreCase("UUID")) {
					appkey = (String) entry.getValue();
					Appsession appsession = null;
					List<Appsession> sessionList = appsessionService
							.findByKey(appkey);
					if (sessionList != null && sessionList.size() > 0)
						appsession = appsessionService.findByKey(appkey).get(0);
					if (appsession == null
							|| appsession.getStoreNumber() == null
							|| appsession.getStoreNumber() <= 0)
						returnString = "{\"errorMessage\":\"key mismatch\"}";
					storeNumber = appsession.getStoreNumber();
				}
			}
			case "java.lang.Boolean":
				logger.info("boolean");
			default:
				logger.info("default");
				hm.put(entry.getKey(), entry.getValue().toString());
				break;
			}
			logger.info("entry: " + entry.getKey() + " entry type: "
					+ entry.getValue().toString());

		}
		hm.put("returnString", returnString);
		logger.info("storeNumber:" + storeNumber);
		logger.info("returnString:" + returnString);
		if (storeNumber != null) {
			hm.put("storeNumber", storeNumber.toString());
		}
		if (appkey != null) {
			hm.put("appkey", appkey);
		}
		retMap.put("hm", hm);
		retMap.put("objectMap", objectMap);
		logger.info("parsed map:" + retMap.toString());

		return retMap;
	}

}
