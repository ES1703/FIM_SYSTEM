package com.springmvc.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.modle.dao.BaseDaoImpl;
import com.springmvc.entity.ModMain;
import com.springmvc.entity.SenDht11;

/**
 * 溫濕度dht11感應資料的Dao實做
 * 
 * @author hrne
 *
 */
@Repository
public class SenDht11DaoImpl extends BaseDaoImpl<SenDht11> implements SenDht11Dao {

	@Autowired
	private SessionFactory sessionFactory;

	public SenDht11DaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<SenDht11> findDht11OrderData(ModMain modData) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from SenDht11 where modData.id =:modData_id order by updateDate desc");
		query.setParameter("modData_id", modData.getId());
		return query.list();
	}
}