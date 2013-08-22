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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class DatasetDaoImpl extends CustomHibernateDaoSupport implements DatasetDao {
	public DatasetDaoImpl() {
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Dataset create(Dataset ds){
		getHibernateTemplate().save(ds);
		return ds;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(Dataset ds){
		getHibernateTemplate().update(ds);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Dataset ds){
		getHibernateTemplate().delete(ds);
	}
	
	@Transactional
	public Dataset retrieve(String id){
		return getHibernateTemplate().get(Dataset.class, id);
	}

	@Transactional
	public List<Dataset> getAll() {
		@SuppressWarnings("unchecked")
		List<Dataset> list = getHibernateTemplate().find("FROM Dataset");
		return list;
	}

	@Transactional
	public List<TimeSlice> getTimeSlices(String datasetId) {
		@SuppressWarnings("unchecked")
		List<TimeSlice> list = getHibernateTemplate().find(
				"SELECT s FROM Dataset d join d.slices s " +
				"WHERE d.id=? ORDER BY s.created DESC",
				datasetId);
		return list;
	}

	@Transactional
	public List<Band> getBands(String datasetId) {
		@SuppressWarnings("unchecked")
		List<Band> list = getHibernateTemplate().find(
				"SELECT b FROM Dataset d JOIN d.bands b " +
				"WHERE d.id=? ORDER BY b.name",
				datasetId);
		return list;
	}

	@Transactional
	public TimeSlice findTimeSlice(String datasetId, Date creationDate) {
		@SuppressWarnings("unchecked")
		List<TimeSlice> list = getHibernateTemplate().find(
				"SELECT s FROM Dataset d JOIN d.slices s " +
				"WHERE d.id=? AND s.created=? ORDER BY s.created DESC",
				datasetId, creationDate);
		if(list.size() == 0)
			return null;
		return (TimeSlice) list.get(0);
	}

	@Transactional
	public List<TimeSlice> findTimeSlices(String datasetId, Date startDate, Date endDate) {
		Session session = getSession();
		String queryString = "SELECT s FROM Dataset d JOIN d.slices s WHERE d.id=:id";
		if (startDate != null && startDate.equals(endDate)) {
			queryString += " AND s.created = :startDate";
		} else {
			if(startDate != null)
				queryString += " AND s.created >= :startDate";
			if(endDate != null)
				queryString += " AND s.created < :endDate";
		}

		queryString += " ORDER BY s.created DESC";
		Query query = session.createQuery(queryString);
		query.setParameter("id", datasetId);

		if (startDate != null && startDate.equals(endDate)) {
			query.setParameter("startDate", startDate);
		} else {
			if(startDate != null)
				query.setParameter("startDate", startDate);
			if(endDate != null)
				query.setParameter("endDate", endDate);
		}

		@SuppressWarnings("unchecked")
		List<TimeSlice> list = query.list(); 
		return list;
	}

	@Transactional
	public Dataset findDatasetByName(String name, CellSize resolution) {
		@SuppressWarnings("unchecked")
		List<Dataset> list = getHibernateTemplate().find(
				"FROM Dataset WHERE name=? AND resolution=?", name, resolution);
		if(list.size() == 0)
			return null;
		return (Dataset)list.get(0);
	}

	@Transactional
	public List<Dataset> search(String name, int page, int pageSize) {
		Session session = getSession();
		String queryString = "FROM Dataset";
		if(name != null)
			queryString += " WHERE lower(name) like :name";

		queryString += " ORDER BY created DESC";
		Query query = session.createQuery(queryString);
		if(name != null)
			query.setString("name", "%" + name.toLowerCase() + "%");
		
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);

		@SuppressWarnings("unchecked")
		List<Dataset> list = query.list();
		return list;
	}

	@Transactional
	public List<Dataset> search(String name, CellSize resolution) {
		Session session = getSession();
		String queryString = "FROM Dataset";
		if(name != null && resolution != null) {
			queryString += " WHERE name like :name and resolution = :resolution";
		}
		else if(name != null && resolution == null) {
			queryString += " WHERE lower(name) like :name";
		}
		else if(name == null && resolution != null) {
			queryString += " WHERE resolution = :resolution";
		}

		queryString += " ORDER BY created DESC";

		Query query = session.createQuery(queryString);
		if(name != null) {
			query.setString("name", "%" + name.toLowerCase() + "%");
		}

		if(resolution != null) {
			query.setParameter("resolution", resolution);
		}

		@SuppressWarnings("unchecked")
		List<Dataset> list = query.list();
		return list;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addBand(String datasetId, Band band) {
		Session session = getSession();
		Dataset ds = (Dataset) session.get(Dataset.class, datasetId);
		if(ds == null) {
			// Capture if dataset not exist
			String msg = String.format("Dataset with ID = %s not found.", datasetId);
			throw new IllegalArgumentException(msg);
		}

		if(ds.getBands() == null)
			ds.setBands(new ArrayList<Band>());
		ds.getBands().add(band);
		session.saveOrUpdate(band);
		session.saveOrUpdate(ds);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TimeSlice addTimeSlice(String datasetId, TimeSlice ts) {
		Session session = getSession();
		Dataset ds = (Dataset) session.get(Dataset.class, datasetId);
		if(ds == null) {
			// Capture if dataset not exist
			String msg = String.format("Dataset with ID = %s not found.", datasetId);
			throw new IllegalArgumentException(msg);
		}

		if(ds.getSlices() == null)
			ds.setSlices(new ArrayList<TimeSlice>());
		ds.getSlices().add(ts);
		session.saveOrUpdate(ds);
		session.saveOrUpdate(ts);
		return ts;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addAllBands(String datasetId, List<Band> bands) {
		Dataset ds = retrieve(datasetId);
		if(ds == null) {
			// Capture if dataset not exist
			String msg = String.format("Dataset with ID = %s not found.", datasetId);
			throw new IllegalArgumentException(msg);
		}

		for(Band band : bands) {
			// Only add new band
			if(!ds.hasBand(band)) {
				addBand(datasetId, band);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addAllSlices(String datasetId, List<TimeSlice> slices) {
		Dataset ds = retrieve(datasetId);
		if(ds == null) {
			// Capture if dataset not exist
			String msg = String.format("Dataset with ID = %s not found.", datasetId);
			throw new IllegalArgumentException(msg);
		}

		for(TimeSlice ts : slices) {
			// Only add new timeslice
			if(findTimeSlice(datasetId, ts.getCreated()) == null) {
				addTimeSlice(datasetId, ts);
			}
		}
	}

	@Transactional
	@Override
	public List<Band> findBands(String datasetId, List<String> bandIds) {
		Session session = getSession();
		String queryString = "SELECT b FROM Dataset d JOIN d.bands b WHERE d.id=:id AND b.id IN (:bandIds) ORDER BY b.name";
		Query query = session.createQuery(queryString);
		query.setParameter("id", datasetId);
		query.setParameterList("bandIds", bandIds);

		@SuppressWarnings("unchecked")
		List<Band> list = query.list();
		return list;
	}

	@Transactional
	@Override
	public List<Band> findBandsByName(String datasetId, List<String> bandNames) {
		Session session = getSession();
		String queryString = "SELECT b FROM Dataset d JOIN d.bands b WHERE d.id=:id AND b.name IN (:bandNames) ORDER BY b.name";
		Query query = session.createQuery(queryString);
		query.setParameter("id", datasetId);
		query.setParameterList("bandNames", bandNames);

		@SuppressWarnings("unchecked")
		List<Band> list = query.list();
		return list;
	}
}