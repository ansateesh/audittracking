package com.aholdusa.am.audittracking.service;

import java.util.List;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aholdusa.am.audittracking.dao.NextmanagerEventDAO;
import com.aholdusa.am.audittracking.entity.NextmanagerEvents;
import com.aholdusa.am.audittracking.entity.Notification;


@Service("nextmanagerEventService")
public class NextmanagerEventServiceImpl extends AMServiceImpl<NextmanagerEvents>
implements NextmanagerEventService{

	@Autowired
	private NextmanagerEventDAO nextmanagerEventDAO;

	@Override 
	public void insert(NextmanagerEvents entity) {
		nextmanagerEventDAO.insert(entity);
	}

	@Override
	public List<NextmanagerEvents> findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
	public void update(NextmanagerEvents entity) {
		nextmanagerEventDAO.insert(entity);

	}

	@Override
	public NextmanagerEvents findEventById(NextmanagerEvents nextmanagerEvents) {
		// TODO Auto-generated method stub
		return nextmanagerEventDAO.findEventById(nextmanagerEvents);
	}

	@Override
	public boolean checkSignOnDeclineActivity(
			NextmanagerEvents nextmanagerEvents) {
		boolean activityExist = false;

		List<NextmanagerEvents> list = nextmanagerEventDAO.findEventByEventTypeAndOperatorNumber(nextmanagerEvents);

		if(list != null && list.size() > 1 ){
			activityExist = true;
		}

		return activityExist;
	}

	@Override
	public Notification findNotificationById(Notification notification) {

		// TODO Auto-generated method stub
		NextmanagerEvents nextManagerEvent= new NextmanagerEvents();
		 String completeMessage;
		 String filteredReason = "";
		 nextManagerEvent.setId(notification.getActivityId());
		 nextManagerEvent=  nextmanagerEventDAO.findEventById(nextManagerEvent);
		 
		 
		 if(nextManagerEvent != null){
			 // Mapping Activity vs. Notification
			 notification.setActivityType(nextManagerEvent.getEventType());
			 notification.setStoreNumber(nextManagerEvent.getStoreNumber().longValue());
			 completeMessage = nextManagerEvent.getMgrReason()!=null?nextManagerEvent.getMgrReason():"";
			 
			 if(completeMessage.contains("Reason")) filteredReason = completeMessage.split("Reason:")[1];
			 notification.setMessage(filteredReason);

			 /*
			  * Extracting TerminalId
			  */
					 
			 boolean prevTokenIsTerminal=false;
			 String reason=nextManagerEvent.getMgrReason()!=null?nextManagerEvent.getMgrReason():"";
			 String terminalNumber="0";
			 StringTokenizer strTokenizer=new StringTokenizer(reason,"|");
			 while(strTokenizer.hasMoreTokens() && !prevTokenIsTerminal){
				 String element=strTokenizer.nextToken();
				 StringTokenizer strTokenizerInner=new StringTokenizer(element,":");
				 String token="";  
				 while(strTokenizerInner.hasMoreTokens() && !prevTokenIsTerminal){

					 if(token.contains("TerminalId")){
						 prevTokenIsTerminal=true;
					 }

					 token=strTokenizerInner.nextToken();

					 if(prevTokenIsTerminal){
						 terminalNumber=token;  

					 }
				 }
			 }


			 notification.setTerminalId(new Long(terminalNumber));
			 notification.setOperatorNumber(nextManagerEvent.getEmpOpNum().longValue());
			 notification.setActivityType(nextManagerEvent.getEventType());


		 }else{
			 notification=null;
		 }
		 
		 
		 
		return notification;
	
	}

}
