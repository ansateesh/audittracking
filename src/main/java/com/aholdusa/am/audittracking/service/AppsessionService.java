package com.aholdusa.am.audittracking.service;

import java.util.List;

import com.aholdusa.am.audittracking.entity.Appsession;

public interface AppsessionService extends AMService<Appsession> {
	public List<Appsession> findByKey(String key);
}
