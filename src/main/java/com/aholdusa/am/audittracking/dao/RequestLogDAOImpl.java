package com.aholdusa.am.audittracking.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.RequestLog;

@Repository("requestLogDAO")
public class RequestLogDAOImpl extends DAOImpl<RequestLog, Long> implements RequestLogDAO{

	@Override
	public void delete(RequestLog entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RequestLog> findByObject(RequestLog entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(RequestLog entity) {
		this.save(entity);
	}

	@Override
	public void update(RequestLog entity) {
		// TODO Auto-generated method stub

	}



}
