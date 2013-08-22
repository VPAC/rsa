/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.storage.dao;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.Process;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class ProcessDaoImpl extends CustomHibernateDaoSupport implements ProcessDao {

	final Logger log = LoggerFactory.getLogger(ProcessDaoImpl.class);

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
		log.debug("Now:" + nowDate);
		Query query = session.createQuery("delete Process where latest < :deadline");
		query.setParameter("deadline", deadlineDate.toDate());
		int result = query.executeUpdate();
		log.debug("deleted {} stale processes", result);
		return result;
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
