package com.aholdusa.am.audittracking.service;

import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.entity.LaneType;
import com.aholdusa.am.audittracking.entity.NextManagerConstants;
import com.aholdusa.am.audittracking.util.RuleRunner;

@Service("laneTypeService")
public class LaneTypeServiceImpl implements LaneTypeService{

	@Override
	public LaneType getLaneType(LaneType laneType) {
		RuleRunner runner = new RuleRunner();
		String[] rules = { NextManagerConstants.LANE_TYPE_RULES.getValue() };
		Object[] facts = { laneType };
		runner.runRules(rules,facts);

		if(laneType.getType()==null){
			laneType.setType(NextManagerConstants.UNASSIGNED_LANE.getValue());
		}
		return laneType;
	}

}
