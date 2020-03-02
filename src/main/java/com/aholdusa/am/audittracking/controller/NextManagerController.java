package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.CarryForwardNotificationTolt;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.entity.LaneType;
import com.aholdusa.am.audittracking.entity.Messages;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.NextmanagerEvents;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.entity.ToltFailover;
import com.aholdusa.am.audittracking.exception.InvalidRequestException;
import com.aholdusa.am.audittracking.service.ActivityLogService;
import com.aholdusa.am.audittracking.service.AppsessionService;
import com.aholdusa.am.audittracking.service.EmployeeService;
import com.aholdusa.am.audittracking.service.LaneHistoryService;
import com.aholdusa.am.audittracking.service.LaneService;
import com.aholdusa.am.audittracking.service.LaneTypeService;
import com.aholdusa.am.audittracking.service.MessageService;
import com.aholdusa.am.audittracking.service.NextManagerService;
import com.aholdusa.am.audittracking.service.NextmanagerEventService;
import com.aholdusa.am.audittracking.service.OverShortReportService;
import com.aholdusa.am.audittracking.service.StoreService;
import com.aholdusa.am.audittracking.service.ToltFailoverService;
import com.aholdusa.am.audittracking.util.AMUtils;



/**
 * NextManager Tolt Systems. POS  
 * SingOn/Off NextManager Controller - 
 * 
 * @author nvixa3
 * 
 * Tolt Notification message would be of the following format :
	{
        "MessageType": "SignOn",
        "TerminalId": "001",
        "DateTime": "2015-05-13 09:48:18",
        "OperatorName": "OPERATOR 1",
        "OperatorId": "1"
    }

 * Based on the application and response received to the terminal , 
 * The the sign on process will allow or disallow the employee to log into the system.
    {
	 "Response":"APPROVE"              OR 	"Response":"DECLINE"
    }
 *
 */

@Controller
@RequestMapping("nextmanager")
public class NextManagerController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NextManagerService nextManagerService; 

	@Autowired
	private LaneService laneService;

	@Autowired
	private ActivityLogService activityLogsService; 
	
	// activity logs : nextmanagerEvents
	@Autowired
	private NextmanagerEventService nextmanagerEventService; 

	@Autowired
	private AppsessionService appsessionService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	LaneHistoryService laneHistoryService;

	@Autowired
	StoreService storeService;

	@Autowired
	LaneTypeService laneTypeService;

	@Autowired
	private MessageService messageService;

	@Autowired
	ToltFailoverService toltFailoverService;

	@Autowired
	OverShortReportService  overShortReportService; 

	/**
	 * @param entity
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void handleSignOnPost(
			@RequestBody final LinkedHashMap<String, Object> entity,
			HttpServletResponse response,
			BindingResult bindingResult) {

		String uuid = null;// UUID
		String messageType = null;
		String terminalId=null;
		String requestDateTimeValue=null;
		String operatorName=null;
		String operatorId=null;
		String auditLog=null;
		String returnString =NextManagerConstants.UNKNOWN.getValue();
		String declineResponse = "";
		boolean isSignOnDeclined=true;

		returnString = "{\"Response\":\""+NextManagerConstants.ACCESS_DENIED.getValue()+"\"}";
		// boolean isFailover = false;
		Timestamp receivedTime = null;

		//CARRY FORWARD
		CarryForwardNotificationTolt carryForward=null;

		logger.info(entity.toString());

		if(entity!=null){

			for (Entry<String, Object> entry : entity.entrySet()) {

				// TO DO BLogic for Signing On needs to be added

				if (entry.getKey().equalsIgnoreCase(NextManagerConstants.UUID.getValue())) {
					uuid = (String) entry.getValue();
				}  

				if (entry.getKey().equalsIgnoreCase(NextManagerConstants.MESSAGE_TYPE.getValue())) {
					messageType = (String) entry.getValue();

				}

				if (entry.getKey().equalsIgnoreCase(NextManagerConstants.TERMINAL_ID.getValue())) {
					terminalId = (String) entry.getValue();
				}

				if (entry.getKey().equalsIgnoreCase(NextManagerConstants.CDATE_TIME.getValue())) {
					requestDateTimeValue = (String) entry.getValue();

				}

				if (entry.getKey().equalsIgnoreCase(NextManagerConstants.OPERATOR_NAME.getValue())) {
					operatorName = (String) entry.getValue();
				}

				if (entry.getKey().equalsIgnoreCase(NextManagerConstants.OPERATOR_ID.getValue())) {
					operatorId = (String) entry.getValue();
				}

				// <<<<===========>>> CARRY FORWARD
				if (entry.getKey().equalsIgnoreCase((NextManagerConstants.TERMINALS.getValue()))) {
					@SuppressWarnings("unchecked")
					Map<String, String> terminals=(Map<String, String>)entry.getValue(); 
					carryForward=new CarryForwardNotificationTolt();
					carryForward.setTerminals(terminals);
				}
			}

			Integer storeNumber = null;
			//Appsession appsession = null;
			Integer laneNumber=0;
			Integer operatorNumber=0;
			Employee employee =null;

			// ================= GET STORE NUMBER BY UUID ========================//  
			Store store=storeService.getStoreByUUID(uuid);

			if(store==null || store.getNumber()==null){

				// ================= CREATE STORE ON TOLT REQUEST ========================//
				//if store doesnt exist, create the store
				storeService.createStoreForToltRequest(storeNumber, uuid);
				/*
				 * Reload New Store  
				 */
				store=storeService.getStoreByUUID(uuid);

			}


			storeNumber=store.getNumber();


			// =================   TOLT DATETIME REQUEST FORMATTING ==============//
			Timestamp requestDateTime= new Timestamp(System.currentTimeMillis());
			if(requestDateTimeValue!=null && !requestDateTimeValue.equalsIgnoreCase("")){
				requestDateTime= AMUtils.getTimestampFromString(requestDateTimeValue);
			}
			//Current DATE - TIME SERVER SIDE
			receivedTime = new Timestamp(System.currentTimeMillis());


			//============ FAILOVER VALIDATION BEGINS
			//If difference between current time and received time > 10s (from NextManager.drl) then Failover = true
			Long timeDIff = receivedTime.getTime() - requestDateTime.getTime();
			ToltFailover toltFailover = new ToltFailover();
			toltFailover.setRecievedDifference(timeDIff);
			toltFailover = nextManagerService.getToltFailover(toltFailover);
			//=================FAILOVER VALIDATION ENDs


			//==============================  CARRY FORDWARD NOTIFICATION BEGINS =====================//
			if (carryForward!=null){

				overShortReportService.createOrUpdateOverShortReport(store, 
						carryForward, 
						toltFailover.getFailover(),  
						AMUtils.getTimestampFromString(requestDateTimeValue));
				returnString = "{\"Response\":\""+NextManagerConstants.ACCESS_OVER_SHORT_PROCESSED.getValue()+"\"}";
			}//==========================CARRY FORDWARD NOTICATION ENDS ======================//

			// ========================== SIGN ON / OFF SERVICE	===============================//
			else{



				// ================== GET TERMINAL TYPE ===============//
				LaneType laneType=new LaneType();
				boolean isSCOorVirtualTerminal=false;
				laneType.setStoreNumber(storeNumber);
				laneType.setLaneNumber(new Integer(terminalId));
				laneType.setSession("Lane");
				laneType=laneTypeService.getLaneType(laneType);
				
				if(laneType!=null && (laneType.getType().contains(NextManagerConstants.TERMINAL_SCO.getValue()) ||
						laneType.getType().contains(NextManagerConstants.TERMINAL_KIOSK.getValue()) ||
						laneType.getType().equalsIgnoreCase(NextManagerConstants.UNASSIGNED_LANE.getValue()) || 
						laneType.getSession().equalsIgnoreCase(NextManagerConstants.TERMINAL_VIRTUAL.getValue())
						)){
					

					isSCOorVirtualTerminal=true;
					

					// ================ DEPRECATED ACTIVITY LOGS BEGIN ===========================//
					ActivityLogs activityLogs =new ActivityLogs();
					NextmanagerEvents nextmanagerEvent=new NextmanagerEvents();
					
					
					if(messageType.equalsIgnoreCase(NextManagerConstants.SIGN_ON.getValue())){
						nextmanagerEvent.setEventType(NextManagerConstants.SIGNON_SCO_ACTIVITY.getValue());
						activityLogs.setActivityType(NextManagerConstants.SIGNON_SCO_ACTIVITY.getValue());
					}
					else{
						nextmanagerEvent.setEventType(NextManagerConstants.SIGNON_SCO_ACTIVITY.getValue());
						activityLogs.setActivityType(NextManagerConstants.SIGNOFF_SCO_ACTIVITY.getValue());
					}
						
						nextmanagerEvent.setEmpOpNum(new Integer(operatorId));
						nextmanagerEvent.setStoreNumber(storeNumber);
						nextmanagerEvent.setCreateDate( new Timestamp(System.currentTimeMillis())); 
						nextmanagerEvent.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
						nextmanagerEvent.setMgrReason("Lane Type :"+laneType.getType());
						nextmanagerEventService.insert(nextmanagerEvent);
						
	                /*
						activityLogs.setEmpOpNum(new Integer(operatorId));
						activityLogs.setStoreNumber(storeNumber);
						activityLogs.setCreateDate( new Timestamp(System.currentTimeMillis())); 
						activityLogs.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
						activityLogs.setMgrReason("Lane Type :"+laneType.getType());
						activityLogsService.insert(activityLogs);
	                */
					    // ================ DEPRECATED ACTIVITY END ===========================//
						
						returnString = "{\""+NextManagerConstants.UUID.getValue().toUpperCase()+"\":\""+uuid+"\",\""
								+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
					
				}

				if(!isSCOorVirtualTerminal){


					//==================== GET   LANE NUMBER  ============================//
					laneNumber=new Integer(terminalId);

					//CREATE A NEW : IF THE LANE DOES NOT EXISTS 
					Lane tmpLane = laneService.findLaneByNumberAndStoreNumber(storeNumber, laneNumber);
					if(tmpLane==null){
						try{	
							tmpLane= new Lane();
							tmpLane.setStoreNumber(storeNumber);
							tmpLane.setNumber(laneNumber);
							tmpLane.setActive(true);
							tmpLane.setVersion(1L);
							tmpLane.setRecordStatus(0);
							tmpLane.setIsDeleted(false);
							tmpLane.setType(laneType.getType());
							tmpLane.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
							tmpLane.setCreateDate( new Timestamp(System.currentTimeMillis()));
							this.laneService.insert(tmpLane);
						}catch(Exception e){
							logger.info(e.getMessage());
							throw new InvalidRequestException(NextManagerConstants.LANE_TRANSACTIONAL_ERROR.getValue(), bindingResult,storeNumber.longValue());
						}
					}

					// GET OPERATOR NUMBER
					if(operatorId!=null && !operatorId.equals("") ){
						operatorNumber=new Integer(operatorId);
					}

					
					
					// ================ DEPRECATED ACTIVITY LOGS BEGIN ===========================//
				 
					NextmanagerEvents nextmanagerPOSEvent=new NextmanagerEvents(); 
					nextmanagerPOSEvent.setStoreNumber(storeNumber);
					nextmanagerPOSEvent.setEmpOpNum(operatorNumber);
					nextmanagerPOSEvent.setCreateDate( new Timestamp(System.currentTimeMillis())); 
					nextmanagerPOSEvent.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
					
					// ========== Setting Up Activity Logs  =======//
					/*
					ActivityLogs activityLogsPomeroy =new ActivityLogs();
					activityLogsPomeroy.setStoreNumber(storeNumber);
					activityLogsPomeroy.setEmpOpNum(operatorNumber);
					activityLogsPomeroy.setCreateDate( new Timestamp(System.currentTimeMillis())); 
					activityLogsPomeroy.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
					*/
					//===========================================//
					// ================ DEPRECATED ACTIVITY LOGS END ===========================//
					//== require  OpNumber
					boolean operatorRequiresAudit=false;

					//===================== GET EMPLOYEE INFORMATION BY OPERATOR NUMBER 

					boolean isNewEmployee=false;
					List<Employee> employeeList=employeeService.findByOperatorNumber(storeNumber,operatorNumber);
					if(employeeList!=null && employeeList.size()>0){
						//GET EMPLOYEE
						employee =employeeList.get(0);	
						if(employeeList.get(0).getRequiresAudit()){
							//======= IS AUDIT NEEDED
							operatorRequiresAudit=true;
						} 
					}//CREATE OR UPDATE EMPLOYEE AND AUDIT FLAG

					//==================== CREATE OR UPDATE EMPLOYEE	

					if( (employeeList!=null && employeeList.size()>0 && messageType.equalsIgnoreCase(NextManagerConstants.SIGN_ON.getValue()))){
						//Update Operator NAME
						if(employee.getFirstName()!=null && !employee.getFirstName().equalsIgnoreCase(operatorName)){ 
							employee.setOperatorName(operatorName);
							employee.setFirstName(operatorName);
							employee.setLastName("*");
							try{		
								employeeService.update(employee);
							}catch(Exception e){
								logger.info(e.getMessage());
								throw new InvalidRequestException(NextManagerConstants.EMPLOYEE_TRANSACTIONAL_ERROR.getValue(), bindingResult,storeNumber.longValue());
							}
						}



					}else if( messageType.equalsIgnoreCase(NextManagerConstants.SIGN_ON.getValue())) {
						//IF EMPLOYEE DOESN'T EXIST CREATE IT
						employee=new Employee();
						employee.setOperatorNumber(operatorNumber);
						employee.setStoreNumber(storeNumber);
						//String operatorFirstName=employeeService.getEmployeeFirstName(operatorName);
						//String operatorLastName=employeeService.getEmployeeLastName(operatorName);
						employee.setFirstName(operatorName);
						employee.setLastName("*");
						employee.setOperatorName(operatorName);
						employee.setActive(true);
						employee.setIsDeleted(false);
						employee.setRequiresAudit(false);
						employee.setOverridden(false);
						//Fixes for Status
						employee.setRecordStatus(0);
						// ======================= Disable for Testing SignOn/Off Tolt UPDATING EMPLOYEE
						try{
							employeeService.insert(employee);
						}catch(Exception e){
							logger.info(e.getMessage());
							throw new InvalidRequestException(NextManagerConstants.EMPLOYEE_TRANSACTIONAL_ERROR.getValue(), bindingResult,storeNumber.longValue());
						}

						isNewEmployee=true;

					}



					//====== GET CLEAN LANES 
					//Is Lane Clean
					List<Lane> cleanLanes=laneService.getCleanLanes(storeNumber);
					boolean isLaneClean=false;
					if(terminalId!=null && !terminalId.equals("") && messageType.equalsIgnoreCase(NextManagerConstants.SIGN_ON.getValue())){
						if(cleanLanes!=null && cleanLanes.size()>0){
							for(Lane lane:cleanLanes){
								if(lane.getNumber().intValue()==laneNumber.intValue()){
									isLaneClean=true;
									break;
								}
							}
						}
					}


					/*
					 * Sign On Validations  
					 * 
					 */

					// ============================== SIGN ON PROCESS BEGINS =====================================//

					if(messageType!=null && messageType.equalsIgnoreCase(NextManagerConstants.SIGN_ON.getValue())){	

						//activityLogs.setActivityType(NextManagerConstants.SIGN_ON.getValue());
						
						nextmanagerPOSEvent.setEventType(NextManagerConstants.SIGN_ON_APPROVED.getValue());
						//activityLogsPomeroy.setActivityType(NextManagerConstants.SIGN_ON_APPROVED.getValue());
						Integer managerOperatorNumber=new Integer(NextManagerConstants.MANAGER_OPERATOR_NUMBER.getValue());

						/*
						 * Sign On Validation 000:
						 * Overridden = true or OperatorNumber >= 3301 or Failover = true
						 * Sign On = Approved
						 */	
						if( operatorNumber>=managerOperatorNumber||
								(employee!=null && employee.getOverridden()!=null && employee.getOverridden()) ||
								toltFailover.getFailover()
								){
							returnString = "{\""+NextManagerConstants.UUID.getValue().toUpperCase()+"\":\""+uuid+"\",\""
									+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
							nextmanagerPOSEvent.setEventType(NextManagerConstants.SIGN_ON_APPROVED.getValue());
						//	activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+" :  "+NextManagerConstants.ACCESS_APPROVED.getValue()+"LaneNumber:"+laneNumber);
							isSignOnDeclined=false;   
							// Override is a single use action, even if not necessary to put employee on lane, set overridden to false
							//RESET OVERRIDDEN TO FALSE ONLY FOR EXITING EMPLOYEE
							if(!isNewEmployee){
								employee.setOverridden(false);
								try{
									employeeService.update(employee);
								}catch(Exception e){
									logger.info(e.getMessage());
									throw new InvalidRequestException(NextManagerConstants.EMPLOYEE_TRANSACTIONAL_ERROR.getValue(), bindingResult,storeNumber.longValue());
								}
							}
						}

						/*
						 * Sign On Validation 001 :
						 * The Lane is Clean 
						 * Sign On = Approved
						 */

						else if(isLaneClean){

							returnString = "{\""+NextManagerConstants.UUID.getValue().toUpperCase()+"\":\""+uuid+"\",\""
									+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
							nextmanagerPOSEvent.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+":"+"LaneNumber:"+laneNumber);
							//activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+":"+"LaneNumber:"+laneNumber);	
							isSignOnDeclined=false;   

						}
						/*
						 * Sign On Validation 002:
						 * Employee.ReqAudit=False and PrevEmployee.ReqAudit=False
						 * Sign On = Approved
						 */

						else if(!operatorRequiresAudit && !nextManagerService.isPrevEmployeeReqAudit(storeNumber,laneNumber)){

							returnString = "{\""+NextManagerConstants.UUID.getValue().toUpperCase()+"\":\""+uuid+"\",\""
									+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
							nextmanagerPOSEvent.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"LaneNumber:"+laneNumber);
						//	activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"LaneNumber:"+laneNumber);
							isSignOnDeclined=false; 

						}

						/*
						 * Sign On Validation 004:
						 * Overridden =  False and CleanLane=False and  isAuditNeeded=True
						 * Sign On = DECLINE
						 */
						else if(isSignOnDeclined){


							logger.info("Processsing SIGNON - DECLINE --> "+ entity.toString());
							/*
							 * CREATE A SIGNON DECLINE ACTIVITY - PushNotification Service to create a SignOnDecline Notification 
							 * 
							 */
							nextmanagerPOSEvent.setEventType(NextManagerConstants.SIGN_ON_DECLINED.getValue());
//							activityLogsPomeroy.setActivityType(NextManagerConstants.SIGN_ON_DECLINED.getValue());


							//if( activityLogsService.checkSignOnDeclineActivity(activityLogsPomeroy, laneNumber) ){
								//activityLogsPomeroy.setIsDeleted(true);
							//}
							

							if( nextmanagerEventService.checkSignOnDeclineActivity(nextmanagerPOSEvent) ){
								nextmanagerPOSEvent.setIsDeleted(true);
							}


							returnString = "{\""+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_DENIED.getValue()+"\"}";
							String storeNumberVal="StoreNumber:"+storeNumber+"|";
							String declineReason="|Overridden:false|";
							Messages messages = new Messages();
							List<Messages> messageList;

							if(operatorRequiresAudit) {
								declineReason+="requiresAnAudit:"+operatorRequiresAudit+"|";
								messages.setMsgName(NextManagerConstants.DECLINE_CURRENT.getValue());
								messageList = this.messageService.findByObject(messages);

								if(messageList != null && messageList.size() > 0) {
									declineResponse = messageList.get(0).getMessage();
								}
							}
							else {
								messages.setMsgName(NextManagerConstants.DECLINE_PREVIOUS.getValue());
								messageList = this.messageService.findByObject(messages);

								if(messageList != null && messageList.size() > 0) {
									declineResponse = messageList.get(0).getMessage();
								}
							}

							if(!isLaneClean) {
								declineReason+="isLaneClean:"+isLaneClean;
							}

							nextmanagerPOSEvent.setMgrReason(storeNumberVal+NextManagerConstants.SIGN_ON_DECLINED.getValue()+":"
									+NextManagerConstants.ACCESS_DENIED.getValue()+"|"
									+NextManagerConstants.TERMINAL_ID.getValue()+":"
									+terminalId+declineReason+"|Reason:"
									+declineResponse); 
						/*	
							activityLogsPomeroy.setMgrReason(storeNumberVal+NextManagerConstants.SIGN_ON_DECLINED.getValue()+":"
									+NextManagerConstants.ACCESS_DENIED.getValue()+"|"
									+NextManagerConstants.TERMINAL_ID.getValue()+":"
									+terminalId+declineReason+"|Reason:"
								+declineResponse);
						 */
							/*
							 * VALIDATION 005
							 * No associated should be declined a sign on onto the gas station irrespective of whether 
							 * the associate requires an audit or not.
							 * In the cases when an associate would have been declined because of the current business validation, a 
							 */	
							if(laneType.getType().contains(NextManagerConstants.TERMINAL_FUEL.getValue())){
								/*
								 * VALIDATION 005.1
								 * FYI notification should be sent to the device indicating that a person 
								 * who was supposed to be declined has signed on to a gas station lane.
								 * Only if Operator Requires - Audit
								 */
								if(operatorRequiresAudit){
									nextmanagerPOSEvent.setEventType(NextManagerConstants.FUEL_LANE_AUDIT_REQUIRED.getValue());
									//activityLogsPomeroy.setActivityType(NextManagerConstants.FUEL_LANE_AUDIT_REQUIRED.getValue());
									
								}else{
									//Create a SignOn approved
									nextmanagerPOSEvent.setEventType(NextManagerConstants.SIGN_ON_APPROVED.getValue());
									nextmanagerPOSEvent.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+":LaneNumber:"+laneNumber);
									
								//	activityLogsPomeroy.setActivityType(NextManagerConstants.SIGN_ON_APPROVED.getValue());
								//	activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+":LaneNumber:"+laneNumber);
								}
								/*
								 * No associated should be declined a sign on onto the gas station
								 * Override Declined 
								 */
								returnString = "{\""+NextManagerConstants.UUID.getValue().toUpperCase()+"\":\""+uuid+"\",\""
										+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
								nextmanagerPOSEvent.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"LaneNumber:"+laneNumber);
							//	activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"LaneNumber:"+laneNumber);
								isSignOnDeclined=false;

							}
						}//END ELSE DECLINE



						/*
						 * Validation 006  
						 * SignOn/SignOff/SignOn Scenario
						 * CurrentEmployeeSigningIn != PrevSignedOnEmployee
						 * Then EndTime = CurrentTime and Create new Record
						 * Else if CurrentEmployeeSigningIn == PrevSignedOnEmployee Set EndTime=null
						 * Else EndTime = CurrentTime and Create new Record
						 */
						Lane lane = laneService.findByLaneNumber(storeNumber, laneNumber);
						//Timestamp currentDate = new Timestamp(System.currentTimeMillis());
						LaneHistory prevHistoryLaneRecord = new LaneHistory();
						prevHistoryLaneRecord.setLane(lane);
						prevHistoryLaneRecord.setStoreNumber(storeNumber);

						// FIXES FOR POS Terminal DateTime
						//prevHistoryLaneRecord.setLaneAssignmentDate(currentDate);
						prevHistoryLaneRecord.setLaneAssignmentDate(requestDateTime);

						prevHistoryLaneRecord.setIsDeleted(false);
						List<LaneHistory> lhlist2 = this.laneHistoryService.findAllByDateAndLane(prevHistoryLaneRecord);
						boolean isSameEmployeeSigningOn=false;
						if (lhlist2!=null && lhlist2.size() > 0) {
							/*
							 * Get the previous LaneHistory Record
							 * lhlist2 is in ASC Order latest record is size-1
							 */
							prevHistoryLaneRecord = lhlist2.get(lhlist2.size()-1);
							if((employee.getOperatorNumber().longValue()==prevHistoryLaneRecord.getEmployee().getOperatorNumber().longValue()) &&

									// It isn't a manager fixes for JIRA - AT-331 -- REMOVE MANGER FROM SAME OPERATOR LOGIC
									// ADD MANAGER TO SAME OPERATOR LOGIC AGAIN	- 02/29/16	
									// employee.getOperatorNumber().longValue()<managerOperatorNumber &&  
									!isLaneClean){
								prevHistoryLaneRecord.setEndTime(null);
								this.laneHistoryService.update(prevHistoryLaneRecord);
								isSameEmployeeSigningOn=true;
								returnString = "{\""+NextManagerConstants.UUID.getValue().toUpperCase()+"\":\""+uuid+"\",\""
										+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
								//SET MANAGER REASON FOR SIGN ON APPROVED + LANE NUMBER
								//SAME EMPLOYEE REQ. AN AUDIT SET ACTIVITY TYPE= APPROVED  
								nextmanagerPOSEvent.setEventType(NextManagerConstants.SIGN_ON_APPROVED.getValue());
								nextmanagerPOSEvent.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+":LaneNumber:"+laneNumber);
								
							//	activityLogsPomeroy.setActivityType(NextManagerConstants.SIGN_ON_APPROVED.getValue());
							//	activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_ON.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+":LaneNumber:"+laneNumber);
								isSignOnDeclined=false; 
							}//SAME EMPLOYEE LOGIC END
						}


						//===================== ASSIGNING OPERATOR TO LANE ======================//
						employeeList=employeeService.findByOperatorNumber(storeNumber,operatorNumber);
						Employee employeeToLane = null;
						if(employeeList!=null && employeeList.size()>0){
							employeeToLane = employeeList.get(0);
						}else
						{	
							employeeToLane=employee;
						}


						if(!isSignOnDeclined && !isSameEmployeeSigningOn){
							if (lane != null && employeeToLane != null) {
								LaneHistory laneHistory = new LaneHistory();
								laneHistory.setEmployee(employeeToLane);
								laneHistory.setLane(lane);
								//laneHistory.setStartTime((new Timestamp(System.currentTimeMillis())));
								//laneHistory.setLaneAssignmentDate(currentDate);
								// Fixes for POS DateTime
								laneHistory.setStartTime(requestDateTime);
								laneHistory.setLaneAssignmentDate(requestDateTime);


								laneHistory.setIsDeleted(false);
								laneHistory.setStoreNumber(storeNumber);
								laneHistory.setVersion(1L);

								/*
								 * During FailOver, set Retain to true
								 * 
								 */
								if (toltFailover.getFailover()) {
									laneHistory.setRetain(true);
									/*
									 * AT-470 Set Failover to true if entry added during failover
									 */
									laneHistory.setFailover(true);
								}

								/*
								 * There is a previous LaneHistory Record
								 * 
								 */
								if (lhlist2.size() > 0) {
									/*
									 * Get the previous LaneHistory Record
									 * lhlist2 is in ASC Order latest record is size-1
									 *  
									 */
									prevHistoryLaneRecord = lhlist2.get(lhlist2.size()-1);

									//if(employee.getOperatorNumber().longValue()!=prevHistoryLaneRecord.getEmployee().getOperatorNumber().longValue())	{  
									// prevHistoryLaneRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
									prevHistoryLaneRecord.setEndTime(requestDateTime);
									try{
										this.laneHistoryService.update(prevHistoryLaneRecord);
										this.laneHistoryService.insert(laneHistory);
									}catch(Exception e){
										logger.info(e.getMessage());
										throw new InvalidRequestException(NextManagerConstants.LANE_HISTORY_TRANSACTIONAL_ERROR.getValue(), bindingResult,storeNumber.longValue());
									}

									/*
							  }else if(employee.getOperatorNumber().longValue()==prevHistoryLaneRecord.getEmployee().getOperatorNumber().longValue()){
								    prevHistoryLaneRecord.setEndTime(null);
									this.laneHistoryService.update(prevHistoryLaneRecord);
							  }
									 */

								}
								/*
								 * There is not a previous LaneHistory Record
								 * Then Create a new Lane History Record
								 */
								else{
									try{
										this.laneHistoryService.insert(laneHistory);
									}catch(Exception e){
										logger.info(e.getMessage());
										throw new InvalidRequestException(NextManagerConstants.LANE_HISTORY_TRANSACTIONAL_ERROR.getValue(), bindingResult,storeNumber.longValue());
									} 
								}

							}

						}//End Assigning Employee to Lane   

						//======================== INSERT ACTIVITY SIGN ON LOG ==================//
					//	activityLogsService.insert(activityLogsPomeroy);	
						nextmanagerEventService.insert(nextmanagerPOSEvent);

					}
					//============================== END SIGN ON PROCESS==============================================//



					//================================ SIGN OFF PROCESS BEGINS=========================================//
					else if(messageType!=null && messageType.equalsIgnoreCase(NextManagerConstants.SIGN_OFF.getValue())){
						
						nextmanagerPOSEvent.setEventType(NextManagerConstants.SIGN_OFF.getValue());
						//activityLogsPomeroy.setActivityType(NextManagerConstants.SIGN_OFF.getValue());
						
						
						if(!operatorRequiresAudit){

							returnString = "{\""+NextManagerConstants.UUID.getValue().toUpperCase()+"\":\""+uuid+"\",\""
									+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
						
							nextmanagerPOSEvent.setMgrReason(NextManagerConstants.SIGN_OFF.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"|"+NextManagerConstants.AUDIT_NEEDED.getValue()+":FALSE"+":LaneNumber:"+laneNumber);

						//	activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_OFF.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"|"+NextManagerConstants.AUDIT_NEEDED.getValue()+":FALSE"+":LaneNumber:"+laneNumber);


						}else if(operatorRequiresAudit){
							//====  SEND A NOTIFICATION TO MANAGER +++ TO DO TASK 44 ' SIGN OFF APPROVED ON AUDIT NEEDED '

							returnString = "{\""+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_APPROVED.getValue()+"\"}";
							
							nextmanagerPOSEvent.setMgrReason(NextManagerConstants.SIGN_OFF.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"|"+NextManagerConstants.AUDIT_NEEDED.getValue()+":TRUE"+":LaneNumber:"+laneNumber);
							
							//activityLogsPomeroy.setMgrReason(NextManagerConstants.SIGN_OFF.getValue()+":"+NextManagerConstants.ACCESS_APPROVED.getValue()+"|"+NextManagerConstants.AUDIT_NEEDED.getValue()+":TRUE"+":LaneNumber:"+laneNumber);

						}







						//====== >>>> SIGN OFF EMPLOYEE FROM LANE BEGIN=======>>//

						Employee empToBeRemovedFromLane = null;
						empToBeRemovedFromLane=employee;
						// Invalid singOff Request POS System sending invalid request 
						try{

							Lane lane = laneService.findByLaneNumber(storeNumber, laneNumber);
							if (lane != null && empToBeRemovedFromLane != null) {
								Timestamp currentDate = new Timestamp(System.currentTimeMillis());
								// If there is Previous  a Lane History Record set EndTime 
								LaneHistory prevHistoryLaneRecord = new LaneHistory();
								prevHistoryLaneRecord.setLane(lane);
								prevHistoryLaneRecord.setStoreNumber(storeNumber);
								prevHistoryLaneRecord.setLaneAssignmentDate(currentDate);
								prevHistoryLaneRecord.setEndTime(null);
								prevHistoryLaneRecord.setIsDeleted(false);
								// List<LaneHistory> lastLaneHistory = this.laneHistoryService.findLastLaneHistoryByDateAndLane(prevHistoryLaneRecord);
								/*
								 * Managers that sign on late at night and sign off the next day should be signed off for the first day
								 * Removing the Created_date Validation from the Query 
								 * JIRA - AT-525
								 */
								LaneHistory lastLaneHistory = this.laneHistoryService.findLastLaneHistoryByStoreAndLane(prevHistoryLaneRecord);
								if (lastLaneHistory!=null) {

									prevHistoryLaneRecord = lastLaneHistory;



									/*
									 * SignOff Manager Operator From Lane
									 * if it is a fail-over request don't validate till Amt. on SignOff action
									 */

									//====================== Fixes - @Robert  04.27.16 ==============//

									//==================== NON MANAGER SIGN OFF  =======================//
									if(operatorNumber < new Integer(NextManagerConstants.MANAGER_OPERATOR_NUMBER.getValue())) {
										prevHistoryLaneRecord.setEndTime(requestDateTime);
										// ==================== MANAGER SIGN OFF  ==========================//
									} else {

										if (prevHistoryLaneRecord.getRetain()) {
											prevHistoryLaneRecord.setEndTime(requestDateTime);
										} else {

											if(!nextManagerService.isTerminalStatusHasChanged(store, laneNumber)){
												prevHistoryLaneRecord.setIsDeleted(true);

											} else{
												prevHistoryLaneRecord.setEndTime(requestDateTime);
												prevHistoryLaneRecord.setRetain(true);
											}
											/*
											 * Adding NumberOfTrans to SignOff Logger
											 */
											
											nextmanagerPOSEvent.setMgrReason(nextmanagerPOSEvent.getMgrReason()+"|"+NextManagerConstants.NUMBER_OF_TRANS.getValue()+":"+nextManagerService.getNumberOfTransactions(store, laneNumber));
											//activityLogsPomeroy.setMgrReason(activityLogsPomeroy.getMgrReason()+"|"+NextManagerConstants.NUMBER_OF_TRANS.getValue()+":"+nextManagerService.getNumberOfTransactions(store, laneNumber));
										}
									}
									try{
										this.laneHistoryService.update(prevHistoryLaneRecord);
									}catch(Exception e){
										logger.info(e.getMessage());
										throw new InvalidRequestException(NextManagerConstants.LANE_HISTORY_TRANSACTIONAL_ERROR.getValue(), bindingResult,storeNumber.longValue());
									}
								}else{
									/*
									 * Create an invalid Sign Off Request Activity and return 200 Http Status
									 */
									nextmanagerPOSEvent.setMgrReason(NextManagerConstants.INVALID_SIGNOFF_REQUEST_WITHOUT_SIGNON_ERROR.getValue()+"|"+"LANE:"+lane.getNumber()+"|"+"EMP:"+empToBeRemovedFromLane.getOperatorNumber()+"|"+"ReqDateTime:"+requestDateTime);
									//activityLogsPomeroy.setMgrReason(NextManagerConstants.INVALID_SIGNOFF_REQUEST_WITHOUT_SIGNON_ERROR.getValue()+"|"+"LANE:"+lane.getNumber()+"|"+"EMP:"+empToBeRemovedFromLane.getOperatorNumber()+"|"+"ReqDateTime:"+requestDateTime);
								}
							}
						

						
						//activityLogsService.insert(activityLogsPomeroy);
						nextmanagerEventService.insert(nextmanagerPOSEvent);	
							
						}catch(Exception e){
							logger.info(e.getMessage());
							throw new InvalidRequestException(NextManagerConstants.INVALID_SIGNOFF_REQUEST_POS_ERROR.getValue(), bindingResult,storeNumber.longValue());
						} finally{
							//* 200 Http Code 
							//* Response Invalid SignOff Request 200 http Code
							//* For any 500 error 
							returnString = "{\""+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.INVALID_SIGNOFF_REQUEST_POS_ERROR.getValue()+"\"}";
						
						}
						
						
						
						//====== >>>> SIGN OFF EMPLOYEE FROM LANE END =======>>//

					}//SIGN OFF END

					else{

						returnString = "{\""+NextManagerConstants.RESPONSE.getValue()+"\":\""+NextManagerConstants.ACCESS_DENIED.getValue()+"\"}";

						logger.info("Next Manager Service Invalid Request");
					}

					// NextManager request to String
					auditLog= uuid!=null?uuid+",":""+
							requestDateTimeValue!=null?requestDateTimeValue+",":""+
									operatorId!=null?operatorId+",":""+
											operatorName!=null?operatorName+",":""+
													messageType!=null?messageType+",":""+
															terminalId!=null?terminalId+",":"";
					logger.info(" Next Manager "+messageType+" Service Request "+auditLog);

				} // END NON SCO TERMINAL
			} //END SIGN ON / OFF SERVICE

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