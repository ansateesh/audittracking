package com.aholdusa.test;

 
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.aholdusa.am.audittracking.controller.AdminReportController;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.NextmanagerEvents;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.NextmanagerEventService;
import com.aholdusa.am.audittracking.service.ReportService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-test.xml"})
public class JUnitMVC {
  
	@InjectMocks
	AdminReportController adminReportController;
	@Autowired
	ReportService reportService;
	@Autowired
	ActivityLogService activityLogService;
	@Autowired
	NextmanagerEventService nextmanagerEventService;
	private MockMvc mockMvc;
	 
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminReportController).build();
    }
	 
    
    
    @Test
    public void testGetAuditReport() throws Exception {
      
      /*  
        mockMvc.perform(post("/audittracking/admin.jsp"))
        .andExpect(status().isOk())
        .andExpect(view().name("todo/admin"))
        .andExpect(forwardedUrl("/WEB-INF/jsp/view/admin.jsp"))
        .andExpect(model().attribute("admin", hasProperty("storeNumber", nullValue())))
        .andExpect(model().attribute("admin", hasProperty("beginDate", isEmptyOrNullString())))
        .andExpect(model().attribute("admin", hasProperty("endDate", isEmptyOrNullString())));
        */
    }

    
    @Test
    public void testGetAcitivityReport() throws Exception {
    /* Call the activity service */
    	
    	ActivityLogs activity=new ActivityLogs();
    	
    	java.sql.Date beginDateValue =  java.sql.Date.valueOf("2016-03-01");
		java.sql.Date endDateValue = java.sql.Date.valueOf("2016-03-30");
		 
		activity.setObjectType("Employee");
		activity.setStoreNumber(979);
		activity.setActivityType("Override");
		 assertNotNull(activityLogService.findActivityByBeginEndDate(activity,beginDateValue,endDateValue));
		activity.setEmpOpNum(101);
		assertNotNull(activityLogService.findActivityByBeginEndDate(activity,beginDateValue,endDateValue));
		
		
    }
    
    
    @Test
    public void testGetNextManagerEvents() throws Exception {
    	 

    	NextmanagerEvents event=new NextmanagerEvents();
    	event.setId(new Long(300)); 
    	assertNotNull (nextmanagerEventService.findEventById(event));

    }

    @Test
    public void testSoftDeleteNextManagerEvents() throws Exception {
    	 

    	NextmanagerEvents event=new NextmanagerEvents();
    	event.setId(new Long(300)); 
    	event.setIsDeleted(true);
    	nextmanagerEventService.update(event);

    }
}
