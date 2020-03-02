package com.aholdusa.am.audittracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.RequestLogDAO;
import com.aholdusa.am.audittracking.entity.RequestLog;

@Service("requestLogService")
public class RequestLogServiceImpl extends AMServiceImpl<RequestLog> implements RequestLogService{
	@Autowired
	private RequestLogDAO requestLogDao;
	//	
	@Override
	public List<RequestLog> findByJsonObject(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}
	//	
	public void insert(RequestLog rl) {
		this.requestLogDao.insert(rl);
	}

}
