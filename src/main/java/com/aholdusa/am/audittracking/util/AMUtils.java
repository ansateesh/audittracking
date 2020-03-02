package com.aholdusa.am.audittracking.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aholdusa.am.audittracking.entity.AmKey;
import com.aholdusa.am.audittracking.entity.Appsession;
import com.google.gson.Gson;

/**
 * Utility class that provides implementation for utility methods required to format/modify dates. 
 * 
 * @author nusxr42
 *
 */
public class AMUtils {
  public static String[] PARSE_PATTERNS = { "yyyy-MM-dd", "MM/dd/yyyy","yyyy-MM-dd HH:mm:ss.SSS","MMM dd, yyyy hh:mm:ss aa","yyyy-MM-dd HH:mm:ss" }; 
  private static Logger logger = LoggerFactory.getLogger(AMUtils.class); 

  public static boolean isEmpty(Object obj){
	  Class clazz=  obj.getClass();  
	  for (Field f : clazz.getDeclaredFields()) {
			logger.info("field f: " + f.getName());
			if (f.getAnnotation(AmKey.class) != null) {
				Boolean accessible = false;
				if (f.isAccessible())
					accessible = true;
				if (!accessible) {
					f.setAccessible(true);
				}
				try {
					if (f.getName().equals("id")) 
						continue; 
					if (f.get(obj) != null) {
						return false; 
					}
				} catch (IllegalAccessException iae) {
					logger.error(iae.getStackTrace().toString());
					iae.printStackTrace();
				}
				if (!accessible)
					f.setAccessible(false);
			}
		}
	  return true; 
  }
  
  
  
  public static Timestamp getTimestampFromString(String date) {
    Timestamp sqlDate = null;
    try {
      sqlDate = new Timestamp(DateUtils.parseDate(date, PARSE_PATTERNS).getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return sqlDate;
  }
  
  
  public static String getStringFromTimestamp(Timestamp date) {
	  String dateToString = null;
	    try {
	    	 dateToString =new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);	
	       
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return dateToString;
	  }
  
  
  public static String getStringFromDate(Timestamp date) {
	  String dateToString = null;
	    try {
	    	 dateToString =new SimpleDateFormat("MM/dd/yy").format(date);	
	       
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return dateToString;
	  }
  /*
  public HashMap<String, HashMap<String, String>> parseRequest(LinkedHashMap<String, Object> entity) {
		HashMap<String, HashMap<String, String>> retMap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> hm = new HashMap<String, String>();
		HashMap<String, String> objectMap = new HashMap<String, String>();
		String objectType = "";
		Gson gson = new Gson();
		String returnString = "";
		Integer storeNumber = null;
		String appkey = null;
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (entry.getValue() == null){
			logger.error("entry null: " + entry.getKey());
				continue;
			}
			String entryType = entry.getValue().getClass().getCanonicalName();
			logger.info("parsing: " + entryType ); 
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
					List<Appsession> sessionList = this.appsessionService
							.findByKey(appkey);
					if (sessionList != null && sessionList.size() > 0)
						appsession = this.appsessionService.findByKey(appkey)
								.get(0);
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
	*/
}
