package com.aholdusa.am.audittracking.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.AppsessionDAO;
import com.aholdusa.am.audittracking.entity.Appsession;

@Service("appsessionService")
public class AppsessionServiceImpl extends AMServiceImpl<Appsession> implements
		AppsessionService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AppsessionDAO appsessionDao;

	public List<Appsession> findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Appsession> findByKey(String key) {
		return this.appsessionDao.findByKey(key);
	}
}
