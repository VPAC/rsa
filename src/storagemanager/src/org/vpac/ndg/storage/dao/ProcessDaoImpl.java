package org.vpac.ndg.storage.dao;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.Process;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class ProcessDaoImpl extends CustomHibernateDaoSupport implements ProcessDao {

	@Transactional
	@Override
	public Process create(Process pm) {
		getHibernateTemplate().save(pm);
		return pm;
	}

	@Transactional
	@Override
	public void update(Process pm) {
		getHibernateTemplate().update(pm);
	}

	@Transactional
	@Override
	public void delete(Process pm) {
		getHibernateTemplate().delete(pm);
	}

	@Transactional
	@Override
	public Process retrieve(String id) {
		return getHibernateTemplate().get(Process.class, id);
	}

	@Transactional
	@Override
	public List<Process> list() {
		@SuppressWarnings("unchecked")
		List<Process> list = getHibernateTemplate().find(
				"FROM Process");

		return list;
	}

	@Transactional
	@Override
	public void update(String id) {
		Process process = retrieve(id);
		update(process);
	}

	@Transactional
	@Override
	public void delete(String id) {
		Process process = retrieve(id);
		delete(process);
	}

	@Transactional
	@Override
	public int deleteStale() {
		Session session = getSession();
		Query queryDate = session.createQuery("select current_timestamp() from Process p");
		Date nowDate = (Date) queryDate.list().get(0);
		DateTime deadlineDate = new DateTime(nowDate).minusMinutes(3);
		System.err.println("Now:" + nowDate);
		Query query = session.createQuery("delete Process where latest < :deadline");
		query.setParameter("deadline", deadlineDate.toDate());
		int result = query.executeUpdate();
		System.err.println("delete stale process !!" + result);
		return result;
//		return 0;
	}

	@Transactional
	@Override
	public int deleteOthers(String id) {
		Session session = getSession();
		Query query = session.createQuery("delete Process where id <> :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		System.err.println("delete others process !!" + result);

		return result;
	}

}
