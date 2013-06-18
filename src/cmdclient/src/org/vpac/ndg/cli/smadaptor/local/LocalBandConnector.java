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
