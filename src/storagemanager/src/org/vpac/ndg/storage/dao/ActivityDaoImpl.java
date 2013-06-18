package org.vpac.ndg.storage.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.storage.model.ActivityInfo;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class ActivityDaoImpl extends CustomHibernateDaoSupport implements ActivityInfoDao {

	@Transactional
	@Override
	public void save(ActivityInfo activity) {
		getHibernateTemplate().saveOrUpdate(activity);
	}

	@Transactional
	@Override
	public ActivityInfo retrieve(String id) {
		return getHibernateTemplate().get(ActivityInfo.class, id);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityInfo> search(TaskState state, int page, int pageSize) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(ActivityInfo.class);

		if(state != null) 
			criteria.add(Restrictions.eq("state", state));
		
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult(page * pageSize);
		return criteria.list();
	}
}
