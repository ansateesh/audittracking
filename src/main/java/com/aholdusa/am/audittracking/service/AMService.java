package com.aholdusa.am.audittracking.service; 

import java.util.HashMap;
import java.util.List;

public interface AMService<T> {
  // This is a marker interface
	
	public List<T> findByJsonObject(String jsonString);
	public List<T> findByObject(T object); 
	public T findById(long id) ;
	public List<T> findAll(); 
	public Class getClazz();
	public void insert(T object);
	public void update(T object);
	public void delete(T object);
}