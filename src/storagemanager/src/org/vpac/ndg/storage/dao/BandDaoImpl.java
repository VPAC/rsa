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

import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class BandDaoImpl extends CustomHibernateDaoSupport implements BandDao {
	public BandDaoImpl() {
	}

	@Transactional
	@Override
	public Band create(Band b) {
		throw new NotImplementedException("Use DatasetDao");
	}

	@Transactional
	@Override
	public void update(Band b) {
		getHibernateTemplate().update(b);
	}

	@Transactional
	@Override
	public void delete(Band b) {
		getHibernateTemplate().delete(b);
	}

	@Transactional
	@Override
	public Band retrieve(String id) {
		return getHibernateTemplate().get(Band.class, id);
	}

	@Transactional
	@Override
	public Band find(String datasetId, String bandName) {
		@SuppressWarnings("unchecked")
		List<Band> list = getHibernateTemplate().find(
				"SELECT b FROM Dataset d join d.bands b WHERE d.id=? AND b.name=?",
				datasetId, bandName);
		if(list.size() <= 0)
			return null;
		return list.get(0);
	}

	@Transactional
	@Override
	public Dataset getParentDataset(String bandId) {
		@SuppressWarnings("unchecked")
		List<Dataset> list = getHibernateTemplate().find("SELECT d FROM Dataset d join d.bands b WHERE b.id=?", bandId);
		if(list.size() <= 0)
			return null;
		return list.get(0);
	}

}