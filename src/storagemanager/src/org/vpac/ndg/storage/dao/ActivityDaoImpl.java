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
