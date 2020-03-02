package com.aholdusa.am.audittracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aholdusa.am.audittracking.dao.DAO;

public abstract class AMServiceImpl<T> implements AMService<T> {
	@Autowired
	DAO<T, Long> dao;

	public T findById(long id) {
		return (T) dao.findByID(id);
	}

	public List<T> findAll() {
		return (List<T>) dao.findAll();
	}

	public List<T> findByObject(T object) {
		return dao.findByObject(object);
	}

	public void insert(T object) {
		dao.save(object);
	}

	public void delete(T object) {
		dao.delete(object);
	}

	public void update(T object) {
		dao.update(object);
	}

	public Class getClazz() {
		return this.dao.getClazz();
	}
}