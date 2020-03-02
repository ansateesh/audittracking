package com.aholdusa.am.audittracking.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.OverShortReport;
import com.aholdusa.am.audittracking.exception.AuditTrackingException;

@Repository("overShortReportDAO")
public class OverShortReportDAOImpl extends DAOImpl<OverShortReport, Long> implements OverShortReportDAO {

	@Override
	public void delete(OverShortReport entity) {
		this.update(entity);
	}
	
	@Override
	public void deleteReport(OverShortReport entity){
		this.getSession()
		.createSQLQuery(
				"UPDATE   MOBL_OWNER.OVER_SHORT_REPORTS SET IS_DELETED=:paramDeleted WHERE OVER_SHORT_REPORTS_ID=:paramID"  )
                .setParameter("paramDeleted", "1")
				.setParameter("paramID", entity.getId()).executeUpdate();
					 
	}

	@Override
	public List<OverShortReport> findByObject(OverShortReport entity) {
		return (List<OverShortReport>) this.findByObjectExample(entity);
	}

	@Override
	public void insert(OverShortReport entity) {
		// TODO Auto-generated method stub
		this.save(entity);
	}

	@Override
	public void update(OverShortReport entity) {
		// TODO Auto-generated method stub
		 
		this.update(entity);
	}

	@Override
	public List<OverShortReport> findOverShortReportByStoreNumber(
			OverShortReport overShortReport) throws AuditTrackingException {

		Query query =   this.getSession().getNamedQuery("findOverShortReportByStoreNumber"); 
		query.setParameter("storeNumber", overShortReport.getStoreNumber() );

		return (List<OverShortReport>) query.list();
	}

	@Override
	public List<OverShortReport> findOverShortReportByStoreNumberLaneIdReportDate(
			OverShortReport overShortReport) throws AuditTrackingException {
		Query query =   this.getSession().getNamedQuery("findOverShortReportByStoreNumberLaneId"); 


		query.setParameter("storeNumber", overShortReport.getStoreNumber() );
		
		query.setParameter("laneId", overShortReport.getLaneId());

		if(overShortReport.getReportDate()!=null){
			query.setParameter("reportDate", overShortReport.getReportDate() );
		}
		else{
 
			query.setParameter("reportDate",  new Timestamp(System.currentTimeMillis()));
		}

		return (List<OverShortReport>) query.list();
	}

	@Override
	public List<OverShortReport> findOverShortReportByStoreNumberReportDate(
			OverShortReport overShortReport) throws AuditTrackingException {
		Query query =   this.getSession().getNamedQuery("findOverShortReportByStoreNumber"); 


		query.setParameter("storeNumber", overShortReport.getStoreNumber() );



		if(overShortReport.getReportDate()!=null){
			query.setParameter("reportDate", overShortReport.getReportDate() );
		}
		else{

			query.setParameter("reportDate",  new Timestamp(System.currentTimeMillis()));
		}

		return (List<OverShortReport>) query.list();
	}

	@Override
	public OverShortReport findOverShortReportById(
			OverShortReport overShortReport) throws AuditTrackingException {
		Query query =   this.getSession().getNamedQuery("findOverShortReportById"); 
		query.setParameter("id", overShortReport.getId() );
		// List<OverShortReport> overShort= (List<OverShortReport>)  query.list();
		return ( OverShortReport ) query.uniqueResult();
	}
	
}
