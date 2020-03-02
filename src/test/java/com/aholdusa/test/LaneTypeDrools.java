package com.aholdusa.test;

import com.aholdusa.am.audittracking.entity.LaneType;

 

 
public class LaneTypeDrools {
	
	
	public static final void main(String[] args) {
		
		RuleRunner runner = new RuleRunner();
		 
		LaneType laneType=new LaneType();
		laneType.setLaneNumber(320);
		laneType.setStoreNumber(6331);
		String[] rules = { "LaneType.drl" };
        Object[] facts = { laneType };
        runner.runRules(rules,facts);

        if(laneType.getType()!=null){
         System.out.println("************Lane Type*************** "+laneType.getType());
        }else{
        	System.out.println("************Lane Type Unassigned*************** " );	
        }
	}
	
	
}
