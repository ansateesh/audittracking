package com.aholdusa.am.audittracking.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aholdusa.am.audittracking.entity.Appsession;
import com.aholdusa.am.audittracking.entity.Lane;
import com.aholdusa.am.audittracking.entity.Store;

/**
 * Data access layer Implementation for Store. Extends AbstractHibernateDBDAO
 * which provides implementation for standard CRUD operations.
 * 
 * @author nusxr42
 *
 * @see com.aholdusa.core.base.dao.AbstractHibernateDBDAO.java
 */

@Repository("storeDAO")
public class StoreDAOImpl extends DAOImpl<Store, Long> implements StoreDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(StoreDAO.class);

	//
	// public Store findById(long id) {
	// return (Store) super.findByID( id) ;
	// }
	public List<Store> findByObject(Store entity) {
		// TODO Auto-generated method stub
		// entity.setIsDeleted(true);
		return this.findByObjectExample(entity);
	}

	public Store getStoreByNumber(Integer storeNumber) {
		List<Store> stores = null;
		Store store = new Store();
		try {
			Query query = this.getSession().getNamedQuery("getStoreByNumber");
			query.setParameter("storeNumber", storeNumber);
			List<Store> storeList = query.list();
			if (storeList.size() <= 0) {
				logger.error("store not found .. are you in Weigmans?");
				store = null;
			} else if (storeList.size() != 1) {
				logger.error("Fix data: unique store not found:"
						+ storeList.size());
				logger.debug("stores found: " + storeList.toString());
				store = storeList.get(0);
			} else {
				store = storeList.get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return store;
	}

	public Store getStoreByIp(String storeIp) {
		List<Store> stores = null;
		Store store = new Store();
		try {
			Query query = this.getSession().getNamedQuery("getStoreByIp");

			query.setParameter("network", storeIp);
			List<Store> storeList = query.list();
			if (storeList.size() != 1) {
				logger.error("unique store not found:" + storeList.size());
				logger.debug("stores found: " + storeList.toString());
			}
			store = storeList.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return store;
	}

	public void insert(Store entity) {

		this.getSession()
				.createSQLQuery(
						"INSERT INTO  MOBL_OWNER.STORES " + "( "
								+ "STORE_NUMBER, " + "NETWORK, " + "START_IP, "
								+ "END_IP, " + "ISP_IP, " + "ISP_PORT, "
								+ "DIVISION, " + "PROTOCOL, " + "VERSION, "
								+ "CREATED_BY, " + "CREATED_DATE, " + "NEXTMANAGER_URL,"
								+ "IS_DELETED " + " ) " + "VALUES ("
								+ ":param00, " + ":param01, " + ":param02, "
								+ ":param03, " + ":param04, " + ":param05, "
								+ ":param06, " + ":param07, " + ":param08, "
								+ ":param09, " + ":param10, " + ":param11,"
								+ ":param12 " + ")")

				.setParameter("param00", entity.getNumber())
				.setParameter("param01", entity.getNetwork())
				.setParameter("param02", entity.getStartIp())
				.setParameter("param03", entity.getEndIp())
				.setParameter("param04", entity.getIspIp())
				.setParameter("param05", entity.getIspPort())
				.setParameter("param06", entity.getDivision())
				.setParameter("param07", entity.getProtocol())
				.setParameter("param08", new Integer(1))
				.setParameter("param09", entity.getCreatedBy())
				.setParameter("param10",
						new Timestamp(System.currentTimeMillis()))
				.setParameter("param11", entity.getNextManagerUrl())
				.setParameter("param12", entity.getIsDeleted()).executeUpdate();

	}

	public void delete(Store entity) {
		entity.setIsDeleted(true);
		this.save(entity);
	}

	public void update(Store entity) {
		this.save(entity);

	}

	// public Store findById(long id) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	// public Store findByID(Long id) {
	// // TODO Auto-generated method stub
	// return null;
	// }

}