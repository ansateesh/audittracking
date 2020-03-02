package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

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
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.exception.InvalidRequestException;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.StoreService;

@Controller
@RequestMapping("appregister")
public class AppRegisterController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AppsessionService appsessionService;
	@Autowired
	private StoreService storeService;

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void registerApp(
			@RequestBody final LinkedHashMap<String, String> entity,
			HttpServletResponse response,
			BindingResult bindingResult) {
		String returnString = null;
		String ip = null;

		
		Store store = null;
		/*
		 * getStoreFromIp Method will also create the Store if the Store Doesn't Exist 
		 * if the IP ADD is an invalid one, nslookUp operation is throwing
		 * a RunTimeException that exception will wrapped up by InvalidRequestException 
		 * with the correct information to the F.E. 
		 * in the MSG DB table under INVALID_NETWORK_NAME_ERROR id
		 */
		try{
		    store = this.storeService.getStoreFromIp(entity.get("ip"));
		   
		    if(store.getNetwork().equals(NextManagerConstants.DEFAULT_NETWORK.getValue())){
		    	store.setNetwork(entity.get("ip"));
		    	this.storeService.update(store);
		    }

		}catch(Exception e){
			throw new InvalidRequestException(NextManagerConstants.INVALID_NETWORK_NAME_ERROR.getValue(), bindingResult);
		}
		Integer storeNumber = null;
		Appsession appsession = new Appsession();
		String appName = entity.get("appName");
		String appKey = null;
		if (store != null) {
			storeNumber = store.getNumber();
			appKey = appName + storeNumber;
		}
		appsession.setAppKey(appKey);
		appsession.setStoreNumber(storeNumber);
		appsession.setAppName(appName);
		appsession.setCreateDate(new Timestamp(new java.util.Date().getTime()));
		this.appsessionService.insert(appsession);
		if (appKey != null) {
			returnString = "{\"UUID\":\"" + appKey + "\"}";
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
		// return returnString;
	}

}