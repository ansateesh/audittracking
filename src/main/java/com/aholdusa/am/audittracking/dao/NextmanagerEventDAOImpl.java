package com.aholdusa.am.audittracking.dao;

import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.aholdusa.am.audittracking.entity.ActivityLogs;
import com.aholdusa.am.audittracking.entity.NextmanagerEvents;

@Repository("NextmanagerEventsDAO")
public class NextmanagerEventDAOImpl    extends DAOImpl<NextmanagerEvents, Long> implements NextmanagerEventDAO{

	private static final Logger logger = LoggerFactory.getLogger(NextmanagerEventDAO.class);
	
	@Override
	public void delete(NextmanagerEvents entity) {
		this.delete(entity);

	}

	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.dao.DAO#findByObject(java.lang.Object)
	 */
	@Override
	public List<NextmanagerEvents> findByObject(NextmanagerEvents entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.aholdusa.am.audittracking.dao.DAO#insert(java.lang.Object)
	 */
	@Override
	public void insert(NextmanagerEvents entity) {
		this.save(entity);

	}

	@Override
	public void update(NextmanagerEvents entity) {
		this.update(entity);

	}

	@Override
	public NextmanagerEvents findEventById(
			NextmanagerEvents nextmanagerEvents) {
		Query query = this.getSession().getNamedQuery("findNextManagerEventById"); 
		query.setParameter("activityId", nextmanagerEvents.getId() ); 
		List<NextmanagerEvents> events=(List<NextmanagerEvents>) query.list();
		NextmanagerEvents event=null; 
		if(events!=null && events.size()>0){
			event=events.get(0);
		}

		return event;
	}

	@Override
	public List<NextmanagerEvents> findEventByEventTypeAndOperatorNumber(
			NextmanagerEvents nextmanagerEvents) {
		Query query = this.getSession().getNamedQuery("findByEventTypeAndOperatorNumber"); 
		query.setParameter("storeNumber", nextmanagerEvents.getStoreNumber()); 
		query.setParameter("activityType", nextmanagerEvents.getEventType()); 
		query.setParameter("empOpNum",nextmanagerEvents.getEmpOpNum() );
		query.setParameter("createdDate", nextmanagerEvents.getCreateDate()); 
		logger.info("findByEventTypeAndOperatorNumber: " + query.toString() ); 
		return (List<NextmanagerEvents>)query.list(); 
		 
	}

}
