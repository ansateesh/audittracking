package com.aholdusa.am.audittracking.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.StoreDAO;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.Store;
import com.google.gson.Gson;

/**
 * Service implementation class for Store. Business logic involved in handling
 * Stores should be implemented here.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.service.AbstractDBService.java
 * 
 * @see com.aholdusa.core.base.service.DBService.java
 *
 */
@Service("storeService")
public class StoreServiceImpl extends AMServiceImpl<Store> implements
		StoreService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private StoreDAO storeDao;

	public List<Store> findByJsonObject(String jsonString) {
		List<Store> storeList = null;
		Gson gson = new Gson();
		Store modelStore = gson.fromJson(gson.toJson(jsonString), Store.class);
		storeList = this.storeDao.findByObject(modelStore);
		return storeList;
	}

	private String getIspName(String ip) throws UnknownHostException {
		InetAddress iAddr = InetAddress.getByName(ip);
		String hostName = iAddr.getCanonicalHostName().split("\\.")[0];
		if (hostName.startsWith("g"))
			hostName = "s001" + hostName.substring(1, 5);
		String ispName = iAddr.getCanonicalHostName().replaceFirst("\\w+",
				hostName);
		return ispName;
	}

	public String getStoreNumber(String ip) throws UnknownHostException {
		String storeNumber = null;
		InetAddress iAddr = InetAddress.getByName(ip);
		String hostName = iAddr.getCanonicalHostName().split("\\.")[0];
 
		storeNumber = hostName.substring(1, 5);
		if (hostName.startsWith("g")){
			String firstValue="";
			storeNumber = hostName.substring(1, 5);
			firstValue = String.valueOf(storeNumber.charAt(0));
			
			if(firstValue.equalsIgnoreCase("0")){
				storeNumber=replaceCharAt(storeNumber,0,'6');
			}
			
		}else if(hostName.startsWith("n")){
			storeNumber = hostName.substring(1, 5);
		}else if(hostName.startsWith("a")) {
			storeNumber = hostName.substring(2, 6);
		}else{
			new  UnknownHostException();
		}
		return storeNumber;
	}

	private String replaceCharAt(String s, int pos, char c) {
	    return s.substring(0, pos) + c + s.substring(pos + 1);
	 }
	
	public String getNextManagerURL(String ip) throws UnknownHostException {
		String nextManagerURL = null;
		InetAddress iAddr = InetAddress.getByName(ip);
		String hostName = iAddr.getCanonicalHostName().split("\\.")[0];
        //==== INCLUDE 00 eg 0087   
		String storeNumberValue=this.getStoreNumber(ip);
		String s001="s001";
		String gcURL="giantpa.aholdusa.com:7200";
		String nongcUrl="sands.aholdusa.com:7200";
		if (hostName.startsWith("g")){
			nextManagerURL=s001+storeNumberValue+"."+gcURL;
		}else{
			nextManagerURL=s001+storeNumberValue+"."+nongcUrl;
		} 
		return nextManagerURL;
	}
	
	private static String replaceLast(String text, String regex,
			String replacement) {
		return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")",
				replacement);
	}

	private String getRouterIp(String ip) {
		// === REPLACE LAST IP ADDRESS DIGITS WITH 1 eg 10.123.45.67 become 10.123.45.1
		return replaceLast(ip, "\\.\\w+", ".1");
	}

	public Store getStoreFromIp(String storeIp) {
		logger.error("getting store # from ip: " + storeIp);
		Store store = null;
		boolean storeChanged = false;
		try {

			/// ======= NETWORK OR IP ADDRESS CORRECTED  AS  x.y.z.1 =======//
			String routerIp = this.getRouterIp(storeIp);

			String storeNumber = this.getStoreNumber(routerIp);
			logger.info("store number:" + storeNumber);
			if (storeNumber != null && !storeNumber.equals("")) {
			//===   VALIDATION IF STORE EXISTS
				Store s = this.storeDao.getStoreByNumber(Integer
						.parseInt(storeNumber));
				logger.error("store :" + s);
			//===   IF STORE DOESN'T EXISTS CREATE THE STORE
				if (s == null) {
					s = new Store();
					boolean ssne = false;
					if (this.getIspName(routerIp).contains("sands")) {
						ssne = true;
						s.setProtocol("TCP");
						s.setDivision("SSNE");
					} else {
						ssne = false;
						s.setProtocol("UDP");
						s.setDivision("GC");
					}
					s.setId(Long.parseLong(storeNumber));
					s.setNumber(Integer.parseInt(storeNumber));
					s.setNetwork(routerIp);
					s.setStartIp(2);
					s.setEndIp(999);
					s.setIspIp(this.getIspName(routerIp));
					s.setIspPort("4050");
					s.setNextManagerUrl(getNextManagerURL(routerIp));

					s.setVersion(1L);
					s.setCreatedBy(NextManagerConstants.CREATED_BY.getValue());
					store = s;
					logger.error("Creating new store: " + s);
					this.dao.insert(s);
				} else {
					logger.info("got store: " + s);

					if (s.getNetwork() == "0.0.0.0") {
						s.setNetwork(routerIp);
						storeChanged = true;
					}

					if (s.getProtocol() == "Unknown" || s.getDivision() == "Unknown") {
						if (this.getIspName(routerIp).contains("sands")) {
							s.setProtocol("TCP");
							s.setDivision("SSNE");
							storeChanged = true;
						} else {
							//ssne = false;
							s.setProtocol("UDP");
							s.setDivision("GC");
							storeChanged = true;
						}
					}

					if (storeChanged) {
						this.dao.update(s);
					}

					store = s;
				}
			}

		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		if (store == null) {
			store = this.storeDao.getStoreByIp(this.getRouterIp(storeIp));
		}

		return store;
	}

	@Override
	public Store getStoreByNumber(Integer storeNumber) {
		Store store = null;
		store = this.storeDao.getStoreByNumber(storeNumber);
		return store;
	}

	/*
	 * Use this method for extracting the Store Number from NEXTMGR<dddd>
	 * (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.service.StoreService#getStoreByUUID(java.lang.String)
	 */
	
	@Override
	public Store getStoreByUUID(String UUID) {
		  String stNumberValue = UUID.replaceAll("\\D+","");
		  
		  Store store=this.storeDao.getStoreByNumber(new Integer(stNumberValue));
		  //Try NonSix Number
		  /*
		  if(store==null){
			  String stNumberNonSixValue=stNumberValue.substring(1, stNumberValue.length());
			  store=this.storeDao.getStoreByNumber(new Integer(stNumberNonSixValue));
		  }*/
		  
		return store;
	}
	
	
	public void createStore(Integer storeNumber, String uuid){

		Store store = getStoreByUUID(uuid);
		storeNumber = store.getNumber();

		if(storeNumber == null || storeNumber.equals("")){
			//if store doesnt exist, create the store
			logger.info("********* if store doesnt exist, create the store ********");
			store.setNumber(storeNumber);
			store.setNetwork(NextManagerConstants.DEFAULT_NETWORK.getValue());
			store.setStartIp(2);
			store.setEndIp(999);
			store.setIspIp("s001" + storeNumber + "sands.aholdusa.com");
			store.setIspPort("4050");

			store.setProtocol("Unknown");
			store.setDivision("Unknown");

			store.setVersion(1L);
			store.setNextManagerUrl("s001" + storeNumber + "sands.aholdusa.com:7200");
			store.setCreatedBy(NextManagerConstants.CREATED_BY.getValue());
			logger.error("Creating new store: " + store);
			this.storeDao.insert(store);
		}else{
			storeNumber=store.getNumber();
		}
	}

	public void createStoreForToltRequest(Integer storeNumber, String uuid){
		
		storeNumber = Integer.parseInt(uuid.replaceAll("\\D+",""));
		Store newStore = new Store();
		
		newStore.setNumber(storeNumber);
		newStore.setNetwork(NextManagerConstants.DEFAULT_NETWORK.getValue());
		newStore.setStartIp(2);
		newStore.setEndIp(999);
		newStore.setIspIp("s001" + storeNumber + "sands.aholdusa.com");
		newStore.setIspPort("4050");
		newStore.setProtocol("Unknown");
		newStore.setDivision("Unknown");
		newStore.setVersion(1L);
		try {
			newStore.setNextManagerUrl(getNextManagerUrlForToltRequest(storeNumber));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		newStore.setCreatedBy(NextManagerConstants.CREATED_BY.getValue());
		logger.error("Creating new store: " + newStore);
		this.storeDao.insert(newStore);
	}

	public String getNextManagerUrlForToltRequest(Integer storeNumberValue) throws UnknownHostException{
		
		String nextManagerURL = null;
		String s001="s001";
		//String gcURL="giantpa.aholdusa.com:7200";
		String nongcUrl="sands.aholdusa.com:7200";
		nextManagerURL=s001+storeNumberValue+"."+nongcUrl;

		return nextManagerURL;
	}

}
