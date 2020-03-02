package com.aholdusa.test;

import com.aholdusa.am.audittracking.entity.LaneType;

 

 
public class JunitDrools {
	
	
	public static final void main(String[] args) {
		//   IBC SCO ->  Toshiba SCO
		RuleRunner runner = new RuleRunner();
		 
		LaneType laneType=new LaneType();
		laneType.setLaneNumber(430);
		laneType.setStoreNumber(6331);
		String[] rules = { "LaneType.drl" };
        Object[] facts = { laneType };
        runner.runRules(rules,facts);

        if(laneType.getType()!=null){
           System.out.println("************Lane Type Toshiba *************** "+laneType.getType());
        }else{
        	System.out.println("************Lane Type Unassigned*************** " );	
        }
	}
	
	
}
