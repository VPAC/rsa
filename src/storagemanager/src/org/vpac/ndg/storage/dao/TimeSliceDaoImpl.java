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

import org.apache.commons.lang.NotImplementedException;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class TimeSliceDaoImpl extends CustomHibernateDaoSupport implements TimeSliceDao {
	public TimeSliceDaoImpl() {
	}
	@Transactional
	@Override
	public TimeSlice create(TimeSlice ds) {
		throw new NotImplementedException("please use DatasetDao");
	}
	@Transactional
	@Override
	public void update(TimeSlice ts) {
		getHibernateTemplate().update(ts);
	}
	@Transactional
	@Override
	public void delete(TimeSlice ts) {
		getHibernateTemplate().delete(ts);
	}
	@Transactional
	@Override
	public TimeSlice retrieve(String id) {
		return getHibernateTemplate().get(TimeSlice.class, id);
	}
	@Transactional
	@Override
	public Dataset getParentDataset(String timeSliceId) {
		@SuppressWarnings("unchecked")
		List<Dataset> list = getHibernateTemplate().find("SELECT d FROM Dataset d join d.slices s WHERE s.id=?", timeSliceId);
		if(list.size() <= 0)
			return null;
		return list.get(0);
	}
}