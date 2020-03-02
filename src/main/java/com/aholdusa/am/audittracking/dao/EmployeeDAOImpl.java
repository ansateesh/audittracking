package com.aholdusa.am.audittracking.dao;

//import org.apache.log4j.Logger;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Employee;
import com.aholdusa.am.audittracking.entity.LaneHistory;

/**
 * Data access layer Implementation for Employee. Extends AbstractHibernateDBDAO
 * which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("employeeDAO")
public class EmployeeDAOImpl extends DAOImpl<Employee, Long> implements
		EmployeeDAO {
	
	private static final Logger logger = LoggerFactory
			.getLogger(Employee.class);

	public void insert(Employee employee) {
		this.save(employee);
	}

	public void update(Employee employee) {
		this.save(employee);
		
	}

	public List<Employee> findByObject(Employee entity) {
		// entity.setIsDeleted(true);
		try {
			return this.findByObjectExample(entity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.findByExample(entity);
	}

	public void delete(Employee employee) {
		deleteEmployeeByID(employee.getStoreNumber(),
				employee.getOperatorNumber());
	}

	public void deleteEmployeeByID(Integer storeNumber, Integer operatorNumber) {

		Query query = this.getSession().getNamedQuery("deleteEmployeeById");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("operatorNumber", operatorNumber);
		int result = query.executeUpdate();
		return;
	}

	public List<Employee> findByOperatorNumber(Integer storeNumber,
			Integer operatorNumber) {
		Query query = this.getSession().getNamedQuery("findEmployeeByOperatorNumber");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("operatorNumber", operatorNumber);
		return (List<Employee>) query.list();
	}

	public List<Employee> findByName(Integer storeNumber, String firstName,
			String lastName) {
		Query query = this.getSession().getNamedQuery("findEmployeeByName");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		return query.list();
	}

	public List<Employee> partialFindByName(Integer storeNumber, String name) {
		Query query = this.getSession().getNamedQuery(
				"partialFindEmployeeByName");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("firstName", name);
		query.setParameter("lastName", name);
		return query.list();
	}

	public List<Employee> partialFindByName(Integer storeNumber,
			String firstName, String lastName) {
		Query query = this.getSession().getNamedQuery(
				"partialFindEmployeeByName");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		return query.list();
	}

	public List<Employee> partialFindByOperatorNumber(Integer storeNumber,
			Integer operatorNumber) {
		Query query = this.getSession().getNamedQuery(
				"partialFindEmployeeByOperatorNumber");
		query.setParameter("storeNumber", storeNumber);
		query.setParameter("operatorNumber", operatorNumber);
		return query.list();
	}
	
}