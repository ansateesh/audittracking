package com.aholdusa.am.audittracking.dao; 


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
 
public interface DAO<T, ID extends Serializable> {
 
    public void save(T entity);
 
    public void merge(T entity);
 
    public void delete(T entity);
 
    public List<T> findMany(Query query);
 
    public T findOne(Query query);
 
    public List findAll();
 
    public T findByID( Long id);
    
//    public T findByID(Long id); 
    
    public List<T> findByObject(T entity); 
    public List<T> findByExample(T entity); 
    
    public void insert(T entity); 
    
    public void update(T entity);
    
    public Class getClazz(); 
    
    public List<T> findByObjectExample(T entity); 
}