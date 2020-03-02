package com.aholdusa.am.audittracking.dao;

//import org.apache.log4j.Logger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.EmployeeDetail;

/**
 * Data access layer Implementation for Employee. Extends AbstractHibernateDBDAO which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("employeeDetailDAO")
public class EmployeeDetailDAOImpl extends DAOImpl<EmployeeDetail, Long> implements EmployeeDetailDAO {
private static final Logger logger = LoggerFactory.getLogger(EmployeeDetail.class);

@Override
public void delete(EmployeeDetail entity) {
	// TODO Auto-generated method stub
	
}

@Override
public List<EmployeeDetail> findByObject(EmployeeDetail entity) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void insert(EmployeeDetail entity) {
	// TODO Auto-generated method stub
	
}

@Override
public void update(EmployeeDetail entity) {
	// TODO Auto-generated method stub
	
} 
	
	

}