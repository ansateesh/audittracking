package com.aholdusa.am.audittracking.dao;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aholdusa.am.audittracking.dto.ReportDTO;
import com.aholdusa.am.audittracking.entity.ActivityLogs;

/**
 * Data access layer Implementation for Lane History. Extends AbstractHibernateDBDAO which provides implementation for standard CRUD operations.
 * 
 * @author x1udxk1
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */
@Repository("ActivityLogDAO")
public class ActivityLogDAOImpl extends DAOImpl<ActivityLogs, Long> implements ActivityLogDAO {
	private static final Logger logger = LoggerFactory.getLogger(ActivityLogs.class);

	public void logActivity(){

	}
	@Override
	public void delete(ActivityLogs entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ActivityLogs> findByObject(ActivityLogs entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(ActivityLogs entity) {
		this.save(entity);

	}

	@Override
	public void update(ActivityLogs entity) {
		this.save(entity);

	}


	@Override
	public List<ActivityLogs> findActivityByManagerBeginEndDate(ActivityLogs activityLog,  Date beginDate,
			Date endDate) {


		Query query = this.getSession().getNamedQuery("findActivityByManagerBeginEndDate"); 

		query.setParameter("storeNumber", activityLog.getStoreNumber()); 
		query.setParameter("managerOperatorNumber", activityLog.getMgrOpNum()); 
		query.setParameter("objectType", activityLog.getObjectType()); 
		query.setParameter("activityType", activityLog.getActivityType()); 
		query.setParameter("beginDate", beginDate); 
		query.setParameter("endDate", endDate); 

		logger.info("findActivityByManagerBeginEndDate: " + query.toString() + " date:" + beginDate); 
		return (List<ActivityLogs>)query.list(); 
	}


	@Override
	public List<ActivityLogs>  findActivityByBeginEndDate(ActivityLogs activityLog,  Date beginDate, Date endDate) {
		Query query = this.getSession().getNamedQuery("findActivityByBeginEndDate"); 
		query.setParameter("storeNumber", activityLog.getStoreNumber()); 
		query.setParameter("objectType", activityLog.getObjectType()); 
		query.setParameter("activityType", activityLog.getActivityType()); 
		query.setParameter("beginDate", beginDate); 
		query.setParameter("endDate", endDate); 


		logger.info("findActivityByManagerBeginEndDate: " + query.toString() + " date:" + beginDate); 

		return (List<ActivityLogs>)query.list(); 
	} 
	
	@Override
	public List<ActivityLogs>  findActivityOperatorNumberByBeginEndDate(ActivityLogs activityLog,  Date beginDate, Date endDate) {
		Query query = this.getSession().getNamedQuery("findActivityOperatorNumberByBeginEndDate"); 
		query.setParameter("storeNumber", activityLog.getStoreNumber()); 
		query.setParameter("objectType", activityLog.getObjectType()); 
		query.setParameter("operatorNumber", activityLog.getEmpOpNum()); 
		query.setParameter("activityType", activityLog.getActivityType()); 
		query.setParameter("beginDate", beginDate); 
		query.setParameter("endDate", endDate); 


		logger.info("findActivityByManagerBeginEndDate: " + query.toString() + " date:" + beginDate); 

		return (List<ActivityLogs>)query.list(); 
	} 

	@Override
	public void logActivity(Integer empOpNum, Integer storeNum,
			String activity_type) {
		Query query = this.getSession().getNamedQuery("logMarkForAudit"); 
		query.setParameter("operatorNumber", empOpNum); 
		query.setParameter("storeNumber", storeNum ); 
		query.setParameter("activity_type",  activity_type);	
	}
	@Override
	public List<ActivityLogs> findActivityByActivityType(ActivityLogs activityLog,
			Date currentDate) {
		Query query = this.getSession().getNamedQuery("findActivityByActivityType"); 
		query.setParameter("storeNumber", activityLog.getStoreNumber()); 
		query.setParameter("activityType", activityLog.getActivityType()); 
		query.setParameter("cDate", currentDate); 

		logger.info("findActivityByActivityType: " + query.toString() + " date:" + currentDate); 

		return (List<ActivityLogs>)query.list(); 
	}
	@Override
	public List<ActivityLogs> findActivityTillContentsByOpManagerNumber(
			ActivityLogs activityLog, Date currentDate) {
	  
		Query query = this.getSession().getNamedQuery("findActivityTillContentsByOpManager"); 
		query.setParameter("storeNumber", activityLog.getStoreNumber()); 
		query.setParameter("activityType", activityLog.getActivityType()); 
		query.setParameter("createDate", currentDate); 
		query.setParameter("mgrOpNum", activityLog.getMgrOpNum()); 

		logger.info("findActivityTillContentsByOpManager: " + query.toString() + " date:" + currentDate); 

		return (List<ActivityLogs>)query.list(); 
	} 

	
	//findActivityById
	@Override
	public  ActivityLogs  findActivityById(
			ActivityLogs activityLog) {
	  
		Query query = this.getSession().getNamedQuery("findActivityById"); 
		query.setParameter("activityId", activityLog.getId() ); 
		List<ActivityLogs> activities=(List<ActivityLogs>) query.list();
	    ActivityLogs activity=null; 
		if(activities!=null && activities.size()>0){
			activity=activities.get(0);
        }
		return activity; 
	}
	
	@Override
	public List<ActivityLogs> findActivityByActivityTypeAndOperatorNumber(
			ActivityLogs activityLog) {
		Query query = this.getSession().getNamedQuery("findByActivityTypeAndOperatorNumber"); 
		query.setParameter("storeNumber", activityLog.getStoreNumber()); 
		query.setParameter("activityType", activityLog.getActivityType()); 
		query.setParameter("empOpNum",activityLog.getEmpOpNum() );
		query.setParameter("createdDate", activityLog.getCreateDate()); 
		logger.info("findByActivityTypeAndOperatorNumber: " + query.toString() ); 
		return (List<ActivityLogs>)query.list(); 
	}
	
	@SuppressWarnings("unchecked")
	@Transactional 
	public List<ActivityLogs> getActivityLogsBySearchCriteria(ReportDTO reportDTO){
		
		List<ActivityLogs> storesList = new ArrayList<ActivityLogs>();
		Session  session  = this.getSession();
		
		Criteria searchCriteria = session.createCriteria(ActivityLogs.class);
		
		//ACtivity Type
		if(StringUtils.isNotBlank(reportDTO.getReportType())){
			searchCriteria.add(Restrictions.eq("activityType", reportDTO.getReportType()));
		}
		
		//Store Number
		if(reportDTO.getStoreNumber()!=null){
			searchCriteria.add(Restrictions.eq("storeNumber", reportDTO.getStoreNumber()));
		}
		
		//Operator Number
		if(reportDTO.getOperatorNumber()!=null){
			searchCriteria.add(Restrictions.eq("empOpNum", reportDTO.getOperatorNumber()));
		}
		//Start Date
		if(StringUtils.isNotBlank(reportDTO.getBeginDate())){
			String beginDate = getDateFromString(reportDTO.getBeginDate());
			if(beginDate!=null){
				searchCriteria.add(Restrictions.sqlRestriction("TRUNC(created_date) >= TO_DATE('" + beginDate + "','dd-MON-yy')"));
			}
			 
		}
		//End date
		if(StringUtils.isNotBlank(reportDTO.getEndDate())){
			String endDate = getDateFromString(reportDTO.getEndDate());
			if(endDate!=null){
				searchCriteria.add(Restrictions.sqlRestriction("TRUNC(created_date) >= TO_DATE('" + endDate + "','dd-MON-yy')"));
			}
		}
		
		//DEfault of DEleted clause
		searchCriteria.add(Restrictions.eq("isDeleted", Boolean.FALSE));
		
		storesList=searchCriteria.list();
		return storesList;
	}
	
	
	private String getDateFromString(String dateStr){
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		    SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
		    java.util.Date date = format1.parse(dateStr);
			return format2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}