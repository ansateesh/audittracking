package com.aholdusa.am.audittracking.util;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

public class HibernateUtil {

//	@Resource("sessionFactory")
	private static SessionFactory sessionFactory;

//	@Autowired
//	HibernateUtil(SessionFactory sessionFactory){
//		this.sessionFactory = sessionFactory;
//	}
	
	//@Autowired
//	public void setSessionFactory(SessionFactory sessionFactory){
//		this.sessionFactory = sessionFactory;
//	}
	static {
		try {
			sessionFactory = new AnnotationConfiguration().configure()
					.buildSessionFactory();
			System.out.println("sessionFactory:"+sessionFactory);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session beginTransaction() {
		Session hibernateSession = HibernateUtil.getSession();
		System.out.println("hibernateSession:"+hibernateSession);
		hibernateSession.beginTransaction();
		return hibernateSession;
	}

	public static void commitTransaction() {
		HibernateUtil.getSession().getTransaction().commit();
	}

	public static void rollbackTransaction() {
		HibernateUtil.getSession().getTransaction().rollback();
	}

	public static Criteria getCriteriaWithExample(Class clazz, Criterion criterion) {
		//beginTransaction(); 
		Criteria criteria = getSession().createCriteria(clazz).add(criterion); 
		//commitTransaction(); 
		return criteria; 
	}
	public static void closeSession() {
		HibernateUtil.getSession().close();
	}

	public static Session getSession() {
		Session hibernateSession = sessionFactory.getCurrentSession();
		return hibernateSession;
	}
}