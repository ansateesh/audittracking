package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
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
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Controller
@RequestMapping("notifications")
public class NoticationController   {

	@Autowired
	private ActivityLogService activityLogsService; 

	@Autowired
	private AppsessionService appsessionService;


	@ResponseBody
	@RequestMapping(value = "/toltNotifications", method = RequestMethod.POST)
	public void getNotifications(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response) {
		ActivityLogs activityLogs=new ActivityLogs();
		String appkey=null;//UUID

		if(entity!=null){

			for (Entry<String, Object> entry : entity.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(NextManagerConstants.UUID.getValue())) {
					appkey = (String) entry.getValue();
				} 
			}
		}


		Integer storeNumber = 0;
		Appsession appsession = null;


		// ========= GET STORE NUMBER
		List<Appsession> sessionList = this.appsessionService.findByKey(appkey);
		if (sessionList != null && sessionList.size() > 0){
			appsession = this.appsessionService.findByKey(appkey).get(0);
			if (appsession != null && appsession.getStoreNumber() != null && appsession.getStoreNumber() > 0){
				storeNumber = appsession.getStoreNumber();
			}
		}

		//======= Set Activity Type
		Calendar cal  = Calendar.getInstance();
		activityLogs.setCreateDate(new Timestamp(cal.getTimeInMillis()));
		activityLogs.setStoreNumber(storeNumber);
		activityLogs.setActivityType(NextManagerConstants.SIGN_ON.getValue());

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.setDateFormat("yyyy-MM-dd").create();

		String returnString = gson.toJson(this.activityLogsService.findActivityByActivityType(activityLogs, new Date(cal.getTimeInMillis())));


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





