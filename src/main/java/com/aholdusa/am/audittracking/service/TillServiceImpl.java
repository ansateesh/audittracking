package com.aholdusa.am.audittracking.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.TillDAO;
import com.aholdusa.am.audittracking.entity.Till;

@Service("tillService")
public class TillServiceImpl extends AMServiceImpl<Till> implements TillService {

	@Autowired
	private TillDAO dao;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private TillDAO tillDao;

	public List findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Till> getDrawer(Integer storeNumber, Date date) {
		return dao.getDrawer(storeNumber, date);
	}
}