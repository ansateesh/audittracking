package com.aholdusa.test;

import com.aholdusa.am.audittracking.entity.ToltFailover;




public class ToltFailoverDrools {
	
	
	public static final void main(String[] args) {
		
		RuleRunner runner = new RuleRunner();
		 
		ToltFailover tf = new ToltFailover();
		Long diff = (long) 11000;
		tf.setRecievedDifference(diff);
		String[] rules = { "NextManager.drl" };
        Object[] facts = { tf };
        runner.runRules(rules,facts);

        System.out.println("************Tolt Failover*************** "+tf.getFailover());

	}
	
	
}