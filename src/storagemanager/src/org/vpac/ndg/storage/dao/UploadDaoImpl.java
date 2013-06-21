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

import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.Upload;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class UploadDaoImpl extends CustomHibernateDaoSupport implements UploadDao {

	@Transactional
	@Override
	public Upload create(Upload u) {
		// manual check cause no foreign key in database
		TimeSlice ts = getHibernateTemplate().get(TimeSlice.class, u.getTimeSliceId());
		if(ts == null)
			throw new IllegalArgumentException(String.format("TimeSlice \"%s\" not found.", u.getTimeSliceId()));

		getHibernateTemplate().save(u);
		return u;
	}

	@Transactional
	@Override
	public void update(Upload u) {
		getHibernateTemplate().update(u);
	}

	@Transactional
	@Override
	public void delete(Upload u) {
		getHibernateTemplate().delete(u);
	}

	@Transactional
	@Override
	public Upload retrieve(String id) {
		return getHibernateTemplate().get(Upload.class, id);
	}
}