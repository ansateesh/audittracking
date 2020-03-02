package com.aholdusa.am.audittracking.service;

import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.entity.ToltFailover;
import com.aholdusa.am.audittracking.util.RuleRunner;

@Service("toltFailoverService")
public class ToltFailoverServiceImpl implements ToltFailoverService{

	/*
	 * (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.service.NextmagerService#getToltFailover(com.aholdusa.am.audittracking.entity.ToltFailover)
	 */
	
	@Deprecated 
	public ToltFailover getToltFailover(ToltFailover toltFailover) {
		RuleRunner runner = new RuleRunner();
		String[] rules = { NextManagerConstants.NEXT_MANAGER_RULES.getValue() };
		Object[] facts = { toltFailover };
		runner.runRules(rules,facts);
		return toltFailover;
		
	}

}
