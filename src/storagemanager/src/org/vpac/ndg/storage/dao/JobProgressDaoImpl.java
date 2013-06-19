package org.vpac.ndg.storage.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.JobProgress;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class JobProgressDaoImpl extends CustomHibernateDaoSupport implements JobProgressDao {
	public JobProgressDaoImpl() {
	}

	@Transactional
	@Override
	public void save(JobProgress job) {
		getHibernateTemplate().saveOrUpdate(job);
	}

	@Transactional
	@Override
	public JobProgress retrieve(String id) {
		return getHibernateTemplate().get(JobProgress.class, id);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<JobProgress> search(TaskType type, TaskState state, int page, int pageSize) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(JobProgress.class);

		if(type != null)
			criteria.add(Restrictions.eq("taskType", type));
		if(state != null) 
			criteria.add(Restrictions.eq("state", state));
		
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult(page * pageSize);
		return criteria.list();
	}

}