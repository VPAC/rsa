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

package org.vpac.ndg.cli.smadaptor.local;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.cli.smadaptor.BandConnector;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.util.BandUtil;

public class LocalBandConnector implements BandConnector {
	@Autowired
	DatasetDao datasetDao;
	
	@Autowired
	BandDao bandDao;
	
	@Autowired
	BandUtil bandUtil;
	
	@Override
	public List<Band> list(String datasetId) {
		return datasetDao.getBands(datasetId);
	}

	@Override
	public Band createBand(String dsid, String name, 
			String datatype,
			String nodata,
			String isContinuous,
			String isMetadata) {

		final boolean continuous = Boolean.parseBoolean(isContinuous);
		final boolean meta = Boolean.parseBoolean(isMetadata);

		Band band = new Band(name, continuous, meta);
		if (datatype != null) {
			RasterDetails type = RasterDetails.valueOf(datatype);
			band.setType(type);
		}
		if (nodata != null && !nodata.isEmpty()) {
			band.setNodata(nodata);
		}
		datasetDao.addBand(dsid, band);

		return band;
	}

	@Override
	public void delete(String id) throws IOException {
		Band band = null;
		band = bandDao.retrieve(id);
		if (band == null) {
			throw new IllegalArgumentException("Band not found.");
		}
		bandUtil.delete(band);
	}

	@Override
	public void update(Band newBand) throws IOException {
		bandUtil.update(newBand);
	}

	@Override
	public Band retrieve(String bandId) {
		return bandDao.retrieve(bandId);
	}

	@Override
	public void updateInfo(Band newBand) {
		bandDao.update(newBand);
	}
}
