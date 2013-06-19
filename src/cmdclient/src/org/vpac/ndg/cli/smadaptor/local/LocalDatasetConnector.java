package org.vpac.ndg.cli.smadaptor.local;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.Utils;
import org.vpac.ndg.cli.smadaptor.DatasetConnector;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.datamodel.AggregationDefinition;
import org.vpac.ndg.datamodel.AggregationOpener;
import org.vpac.ndg.datamodel.RsaAggregationFactory;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.util.DatasetUtil;

import ucar.nc2.dataset.NetcdfDataset;

public class LocalDatasetConnector implements DatasetConnector {
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	DatasetUtil datasetUtil;

	@Override
	public List<Dataset> list() {
		return datasetDao.getAll();
	}

	@Override
	public String create(String name, String abs, String resolution,
			String precision) {

		CellSize res;
		long prec;
		// Resolution
		res = CellSize.fromHumanString(resolution);
		// Precision
		prec = Utils.parseTemporalPrecision(precision);

		Dataset ds = new Dataset(name, abs, res, prec);
		datasetDao.create(ds);
		return ds.getId();
	}

	@Override
	public void delete(String identifier) throws IOException {
		Dataset ds = getDataset(identifier);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}
		datasetUtil.deleteDataset(ds);
	}

	@Override
	public List<Dataset> searchDataset(String name, CellSize resolution) {
		List<Dataset> list = datasetDao.search(name, resolution);
		return list;
	}

	@Override
	public Dataset getDataset(String identifier) {
		int splitLoc = identifier.lastIndexOf('/');
		if (splitLoc != -1) {
			String name = identifier.substring(0, splitLoc);
			String res = identifier.substring(splitLoc + 1);
			CellSize resolution = CellSize.fromHumanString(res);
			return datasetDao.findDatasetByName(name, resolution);
		} else {
			return datasetDao.retrieve(identifier);
		}
	}

	@Override
	public String getNcml(String identifier, Box extents, Date startDate,
			Date endDate) throws IOException {
		RsaAggregationFactory factory = new RsaAggregationFactory();
		Dataset ds = getDataset(identifier);
		AggregationDefinition aggregation = factory.create(ds, extents,
				startDate, endDate, null);

		StringWriter writer = new StringWriter();
		aggregation.serialise(writer);
		return writer.toString();
	}

	@Override
	public String getCdl(String identifier, Box extents, Date startDate,
			Date endDate) throws IOException {
		RsaAggregationFactory factory = new RsaAggregationFactory();
		AggregationOpener opener = new AggregationOpener();

		Dataset ds = getDataset(identifier);
		AggregationDefinition aggregation = factory.create(ds, extents,
				startDate, endDate, null);
		NetcdfDataset ncfile = null;
		try {
			ncfile = opener.open(aggregation, identifier);
			return ncfile.toString();
		} finally {
			if (ncfile != null)
				ncfile.close();
		}
	}

	@Override
	public String getLocation(String identifier) {
		Dataset ds = getDataset(identifier);
		return datasetUtil.getPath(ds).toString();
	}

	@Override
	public Dataset get(String datasetId) {
		return getDataset(datasetId);
	}

	@Override
	public void updateInfo(Dataset newDataset) {
		datasetDao.update(newDataset);
	}

	@Override
	public void update(Dataset newDataset) throws IOException {
		datasetUtil.update(newDataset);
	}
}
