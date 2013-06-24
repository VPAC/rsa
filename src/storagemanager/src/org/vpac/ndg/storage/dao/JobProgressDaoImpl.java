/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

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