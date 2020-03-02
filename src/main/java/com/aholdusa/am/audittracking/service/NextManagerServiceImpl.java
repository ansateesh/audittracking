package com.aholdusa.am.audittracking.service;

 
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aholdusa.am.audittracking.dao.ActivityLogDAO;
import com.aholdusa.am.audittracking.dao.AppsessionDAO;
import com.aholdusa.am.audittracking.dao.NextmanagerEventDAO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.LaneHistory;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.Store;
import com.aholdusa.am.audittracking.entity.TillContent;
import com.aholdusa.am.audittracking.entity.ToltFailover;
import com.aholdusa.am.audittracking.util.RuleRunner;


 
@Service("nextManagerService")
public class NextManagerServiceImpl implements NextManagerService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AppsessionDAO appsessionDao;
  
	@Autowired
	private LaneService laneDao;
	
	@Autowired
	private ActivityLogDAO activityLogDao;
	
	@Autowired
	private ActivityLogService activityLogService;

	
	@Autowired
	private LaneHistoryService laneHistDao;
	
	@Autowired
	EmployeeService employeeDao;
	
	@Autowired
	private NextmanagerEventDAO nextmanagerEventDAO;
	
	 
	
	@Override
	public TillContent getTillContents(Store store, Integer laneNumber) {
		// TODO Auto-generated method stub
		
 
       
		TillContent tillContent=null;
		String uri ="http://"+store.getNextManagerUrl();
        uri=uri+NextManagerConstants.TERMINAL_STATUS_SERVICE_PREFIX_URL.getValue()+laneNumber;
        RestTemplate restTemplate = new RestTemplate();
        
        
    	@SuppressWarnings("unchecked")
        Map<String, Object> nextManagerResponse= restTemplate.getForObject(uri,  Map.class);
    	Map<String, Object> terminalStatus=null;
    	Map<String, Object> tillContest=null;
      
    	//Terminal Status
        if(nextManagerResponse.containsKey("TerminalStatus")){
        	logger.debug("<<<<<< TerminalStatus = TRUE >>>>>>");
        	terminalStatus=(Map<String, Object> )nextManagerResponse.get("TerminalStatus");
        } 
        
        // Adding G+ and G- to Till Contents
        tillContent=new TillContent();
        
        //Till Contents
        if(terminalStatus!=null && terminalStatus.containsKey("TillContents")){
        	logger.debug("<<<<<< TillStatus = TRUE >>>>>>");
        	tillContest=(Map<String, Object> )terminalStatus.get("TillContents");
        	
        	tillContent.setAmtCash(new BigDecimal(tillContest.get("AmtCash").toString()));
        	tillContent.setAmtCheck(new BigDecimal(tillContest.get("AmtCheck").toString()));
        }
        
		return tillContent;
	}
	
	 
	
    //
	/*@iaguilar
	 * 
	 * Get the SSL CERT. Use this method before posting the request to the HTTPS/SSL server 
	 */
	private void getSslVerification() {
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
			};

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}


	@Deprecated
	@Override
	public 	ActivityLogs logTillContents(Store store, Integer laneNumber,Employee employee) {

		ActivityLogs activityLogs =new ActivityLogs();
		activityLogs.setStoreNumber(store.getNumber());
		activityLogs.setEmpOpNum(employee.getOperatorNumber());
		activityLogs.setMgrOpNum(employee.getOperatorNumber());	
		activityLogs.setCreateDate( new Timestamp(System.currentTimeMillis())); 
		activityLogs.setActivityType(NextManagerConstants.LOG_TILL_CONTENTS.getValue());
		activityLogs.setCreatedBy(NextManagerConstants.TOLT_USER.getValue());
		TillContent tillContent =getTillContents(store,laneNumber);
		String tillContentValue="";
		if(tillContent!=null){
			tillContentValue+="AmtCash:"+tillContent.getAmtCash()+"|";
			tillContentValue+="AmtCheck:"+tillContent.getAmtCheck();

		}else{
			tillContentValue+="AmtCash:0.0" +"|";
			tillContentValue+="AmtCheck:0.0";

		}
		activityLogs.setMgrReason(tillContentValue);
		return 	activityLogs;

	}

	@Override
	public boolean isTerminalStatusHasChanged(Store store, Integer laneNumber) {
		boolean terminalStatusChanged=false;
		Integer numberOfTrans=getNumberOfTransactions(  store, laneNumber);
		Integer transNumber=getTransactionNumber(  store, laneNumber); // Transaction ID

		/*
		 *  Left side:
		 *  To determine that it was just the till automatically opening, 
		 *  if NumTrans is greater than TransNum and if NumTrans is '2', no transactions were done
		 * 
		 */
		
		if(!((numberOfTrans>transNumber) && numberOfTrans==2) && numberOfTrans>1){
			terminalStatusChanged=true;
		}

		return terminalStatusChanged;
	}

	@Deprecated
	@Override
	public boolean isTerminalStatusHasChanged(Store store, Integer laneNumber,
			Employee employee) {
		boolean terminalStatusChanged=false;

		ActivityLogs activityLog=new ActivityLogs();
		activityLog.setStoreNumber(store.getNumber());
		activityLog.setActivityType(NextManagerConstants.LOG_TILL_CONTENTS.getValue());
		activityLog.setEmpOpNum(employee.getOperatorNumber());
		activityLog.setMgrOpNum(employee.getOperatorNumber());	
		Calendar cal = Calendar.getInstance();
		List<ActivityLogs> activityLogs=activityLogService.findActivityTillContentsByOpManagerNumber(activityLog,  new Date(cal.getTimeInMillis()));
 
		
		TillContent currentTillContent =getTillContents(store,laneNumber);
		Double prevCashAmt=0.0;
		Double prevCheckAmt=0.0;
		//Double prevGrossPositive=0.0;
		//Double prevGrossNegative=0.0;

		if(activityLogs!=null && activityLogs.size()>0 ){
			String tillAmtValue=activityLogs.get(0).getMgrReason();
			StringTokenizer stTillContent = new StringTokenizer(tillAmtValue,"|");
			List<String> tillContentsValues=new ArrayList<String>();
			while (stTillContent.hasMoreElements()) {
				tillContentsValues.add(stTillContent.nextElement().toString());
			}
			/*
			 * Order Convention
			 * cash  index=0
			 * check index=1
			 * g+    index=2
			 * g-    index=3
			 */
			int index=0;
			for(String tillValue:tillContentsValues){
				StringTokenizer stTillContentVal = new StringTokenizer(tillValue,":");
				while (stTillContentVal.hasMoreElements()) {
					String tk=stTillContentVal.nextElement().toString(); 
					if(!tk.toLowerCase().contains("amt") ){
						if(index==0){
							prevCashAmt=new Double(tk);
						}else if(index==1){
							prevCheckAmt=new Double(tk);
						}
						
					}
				}

				index++;
			}//end for
			
			/*
			 * Validation
			 * Prev Till Amt Vs Current Till Amt
			 * 
			 */
			if(currentTillContent!=null && 
				currentTillContent.getAmtCash()!=null && 
				  currentTillContent.getAmtCheck()!=null && 
					prevCashAmt!=null && prevCheckAmt!=null  
					//&& prevGrossPositive!=null && prevGrossNegative!=null
					){
				if(currentTillContent.getAmtCash().doubleValue()!=prevCashAmt.doubleValue() ||
				   currentTillContent.getAmtCheck().doubleValue()!=prevCheckAmt.doubleValue()){
					terminalStatusChanged=true;
				}
			}
			
		}

		return terminalStatusChanged;
	}



	@Override
	public boolean isPrevEmployeeReqAudit(Integer storeNumber,
			Integer laneNumber) {

		Lane lane = laneDao.findByLaneNumber(storeNumber, laneNumber);
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		LaneHistory prevHistoryLaneRecord = new LaneHistory();
		prevHistoryLaneRecord.setLane(lane);
		prevHistoryLaneRecord.setStoreNumber(storeNumber);
		prevHistoryLaneRecord.setLaneAssignmentDate(currentDate);
		prevHistoryLaneRecord.setIsDeleted(false);
		boolean employeeReqAudit=false;

		List<LaneHistory> lhlist2 = laneHistDao.findAllByDateAndLane(prevHistoryLaneRecord);
		if (lhlist2!=null && lhlist2.size() > 0) {
			/*
			 * Get the previous LaneHistory Record
			 * lhlist2 is in ASC Order latest record is size-1
			 *  
			 */
			prevHistoryLaneRecord = lhlist2.get(lhlist2.size()-1);
		}
		Employee employee=null;
		if(prevHistoryLaneRecord.getEmployee()!=null && prevHistoryLaneRecord.getEmployee().getOperatorNumber()>0){
			List<Employee> employeeList=employeeDao.findByOperatorNumber(storeNumber, prevHistoryLaneRecord.getEmployee().getOperatorNumber());
			if(employeeList!=null && employeeList.size()>0){
				employee=employeeList.get(0);
			}
		}

		if(employee!=null && employee.getRequiresAudit()){
			employeeReqAudit=true;
		}
		return employeeReqAudit;
	}

	@Override
	public ToltFailover getToltFailover(ToltFailover toltFailover) {
		RuleRunner runner = new RuleRunner();
		String[] rules = { NextManagerConstants.NEXT_MANAGER_RULES.getValue() };
		Object[] facts = { toltFailover };
		runner.runRules(rules,facts);
		return toltFailover;
		
	}



	
	@SuppressWarnings("unchecked")
	@Override
	public Integer getNumberOfTransactions(Store store, Integer laneNumber) {
		// TODO Auto-generated method stub
		//   "NumTrans": 5,
		String uri ="http://"+store.getNextManagerUrl();
		uri=uri+NextManagerConstants.TERMINAL_STATUS_SERVICE_PREFIX_URL.getValue()+laneNumber;
		RestTemplate restTemplate = new RestTemplate();


		@SuppressWarnings("unchecked")
		Map<String, Object> nextManagerResponse= restTemplate.getForObject(uri,  Map.class);
		Map<String, Object> terminalStatus=null;

		//=== No transaction 
		Integer numberOfTransaction=1;

		//GET Terminal Status By StoreNumber+LaneNumber 
		if(nextManagerResponse.containsKey(NextManagerConstants.TERMINAL_STATUS.getValue())){
			logger.debug("<<<<<< TerminalStatus Response not Null = TRUE >>>>>>");
			terminalStatus=(Map<String, Object> )nextManagerResponse.get((NextManagerConstants.TERMINAL_STATUS.getValue()));
		} 

		//GET Number Of TRANS By Store Number and Lane Number 
		if(terminalStatus!=null && terminalStatus.containsKey(NextManagerConstants.NUMBER_OF_TRANS.getValue())){
			numberOfTransaction= new Integer(terminalStatus.get(NextManagerConstants.NUMBER_OF_TRANS.getValue()).toString());
		}

		return numberOfTransaction;
	}



	public Integer getTransactionNumber(Store store, Integer laneNumber) {
		// TODO Auto-generated method stub
		//   "NumTrans": 5,
		String uri ="http://"+store.getNextManagerUrl();
		uri=uri+NextManagerConstants.TERMINAL_STATUS_SERVICE_PREFIX_URL.getValue()+laneNumber;
		RestTemplate restTemplate = new RestTemplate();


		@SuppressWarnings("unchecked")
		Map<String, Object> nextManagerResponse= restTemplate.getForObject(uri,  Map.class);
		Map<String, Object> terminalStatus=null;

		//=== No transaction 
		Integer transactionNumber=1;

		//GET Terminal Status By StoreNumber+LaneNumber 
		if(nextManagerResponse.containsKey(NextManagerConstants.TERMINAL_STATUS.getValue())){
			logger.debug("<<<<<< TerminalStatus Response not Null = TRUE >>>>>>");
			terminalStatus=(Map<String, Object> )nextManagerResponse.get((NextManagerConstants.TERMINAL_STATUS.getValue()));
		} 

		//GET Number Of TRANS By Store Number and Lane Number 
		if(terminalStatus!=null && terminalStatus.containsKey(NextManagerConstants.TRANS_NUMBER.getValue())){
			transactionNumber=new Integer(terminalStatus.get(NextManagerConstants.TRANS_NUMBER.getValue()).toString());
		}

		return transactionNumber;
	}

	
	 
}
