package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

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
import com.aholdusa.am.audittracking.service.NextManagerService;
import com.aholdusa.am.audittracking.service.StoreService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("tillcontents")
public class TillContentsController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private StoreService storeService;
	private NextManagerService nextManagerService;
	private AppsessionService appsessionService;
	
	@Autowired
	public TillContentsController( 
		final StoreService storeService,
		final NextManagerService nextManagerSystem,
		final AppsessionService appsessionService) {
		
		super();
		this.storeService = storeService;
		this.nextManagerService =nextManagerSystem;
	    this.appsessionService=appsessionService;
	}
	
	/**
	 * @param entity
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void handleTillContentsPost(
			@RequestBody final LinkedHashMap<String, String> entity,
			HttpServletResponse response,
			BindingResult bindingResult) {

		String laneNumber = null; 
		String returnString =NextManagerConstants.UNKNOWN.getValue();
		Store store = null;
		String appKey=null;


		if(entity!=null){
			//Get Store Info	
			if(entity.get(NextManagerConstants.UUID.getValue())!=null){
				appKey = entity.get(NextManagerConstants.UUID.getValue());
				Appsession appsession = null;
				List<Appsession> sessionList = this.appsessionService
						.findByKey(appKey);
				if (sessionList != null && sessionList.size() > 0)
					appsession = this.appsessionService.findByKey(appKey)
					.get(0);
				if (appsession == null
						|| appsession.getStoreNumber() == null
						|| appsession.getStoreNumber() <= 0){
					returnString = "{\"errorMessage\":\"key mismatch\"}";
				}
				else{
					store = this.storeService.getStoreByNumber(appsession.getStoreNumber());
				}
			}

			//Get Lane Number 	
			if (entity.get(NextManagerConstants.LANE_NUMBER.getValue())!=null) {
				laneNumber = (String) entity.get(NextManagerConstants.LANE_NUMBER.getValue());
			}  

			
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
			
			//GET Till Contents from NextManager Service
			if(store!=null){
				try{
				   returnString = gson.toJson(nextManagerService.getTillContents(store, new Integer(laneNumber)));
				}catch(Exception e){
					 logger.info(e.getMessage());
					throw new InvalidRequestException(NextManagerConstants.NOT_RESOLVABLE_POS_URL_ERROR.getValue(), bindingResult);
				}
			}
			else{
				returnString = "{\"errorMessage\":\"key mismatch\"}";
			}
		} 


		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			//request.addHeader("content-type", "application/json");
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
