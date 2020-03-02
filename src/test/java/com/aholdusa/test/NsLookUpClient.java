package com.aholdusa.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NsLookUpClient {
	 public static void main(String[] argv) throws Exception {
		    byte[] ipAddr = new byte[] { 127, 0, 0, 1 };
		   //10.112.67.1 --> n0020vl02-->20
		    
		    String ip=getRouterIp("10.114.107.1");
		    InetAddress addr = InetAddress.getByName(ip); 
		 // 10.101.119.1 GC
		    String hostName = addr.getCanonicalHostName().split("\\.")[0];
		    String hostNameRaw = addr.getHostName();
		    String storeNumber="";
		    System.out.println("<<<<< Host Canonical Name  >>>>>"+hostName);
		    System.out.println("<<<<< Host   Name  >>>>>"+hostNameRaw);
		    
		    storeNumber = hostName.substring(1, 5);
		    
		    storeNumber = hostName.substring(1, 5);
		    if (hostName.startsWith("g")){
				storeNumber = storeNumber.replaceFirst("0", "6");
			}else if(hostName.startsWith("n")){
				storeNumber = hostName.substring(1, 5);
			}else if(hostName.startsWith("a")) {
				storeNumber = hostName.substring(2, 6);
			}else{
				new  UnknownHostException();
			}
			
			String nextManagerURL="";
			String storeNumberValue=storeNumber;
			String s001="ss01";
			String gcURL="giantpa.aholdusa.com:7200";
			String nongcUrl="sands.aholdusa.com:7200";
			if (hostName.startsWith("g")){
				nextManagerURL=s001+storeNumberValue+"."+gcURL;
			}if(hostName.startsWith("n")){
				nextManagerURL=s001+storeNumberValue+"."+nongcUrl;
			} 
		System.out.println("<<<<< STORE NUMBER >>>>>"+new Long(storeNumber));
		System.out.println("<<<<< NEXT MANAGER URL >>>>>"+ nextManagerURL);
			 
		  }

	 private static String replaceLast(String text, String regex,
				String replacement) {
			return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")",
					replacement);
		}

		private static String getRouterIp(String ip) {
			// === REPLACE LAST IP ADDRESS DIGITS WITH 1 eg 10.123.45.67 become 10.123.45.1
			return replaceLast(ip, "\\.\\w+", ".1");
		}
	 
}
