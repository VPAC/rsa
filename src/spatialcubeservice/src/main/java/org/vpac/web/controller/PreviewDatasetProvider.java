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

package org.vpac.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.datamodel.AggregationOpener;
import org.vpac.ndg.datamodel.RsaAggregationFactory;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.lock.TimeSliceDbReadWriteLock;
import org.vpac.ndg.query.DatasetUtils;
import org.vpac.ndg.query.GridUtils;
import org.vpac.ndg.query.PassThrough;
import org.vpac.ndg.query.Query;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryDefinition;
import org.vpac.ndg.query.QueryDefinition.DatasetInputDefinition;
import org.vpac.ndg.query.QueryDefinition.DatasetOutputDefinition;
import org.vpac.ndg.query.QueryDefinition.FilterDefinition;
import org.vpac.ndg.query.QueryDefinition.GridDefinition;
import org.vpac.ndg.query.QueryDefinition.SamplerDefinition;
import org.vpac.ndg.query.QueryDefinition.VariableDefinition;
import org.vpac.ndg.query.coordinates.GridProjected;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.TimeAxis;
import org.vpac.ndg.query.io.DatasetMetadata;
import org.vpac.ndg.query.io.DatasetProvider;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.JobProgressDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.BandUtil;
import org.vpac.ndg.storage.util.DatasetUtil;
import org.vpac.ndg.storage.util.TimeSliceUtil;

import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Gives access to datasets that are stored in an RSA.
 * @author Alex Fraser
 */
public class PreviewDatasetProvider implements DatasetProvider {

	final Logger log = LoggerFactory.getLogger(RsaDatasetProvider.class);
	final private int PREVIEW_SIZE = 128;

	RsaAggregationFactory factory;
	AggregationOpener opener;
	DatasetUtils datasetUtils;
	GridUtils gridUtils;

	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TimeSliceDao timeSliceDao;
	@Autowired
	TimeSliceUtil timeSliceUtil;
	@Autowired
	BandDao bandDao;
	@Autowired
	JobProgressDao jobProgressDao;
	@Autowired
	BandUtil bandUtil;
	@Autowired
	NdgConfigManager ndgConfigManager;
	@Autowired
	DatasetUtil datasetUtil;

	public PreviewDatasetProvider() {
		factory = new RsaAggregationFactory();
		opener = new AggregationOpener();
		datasetUtils = new DatasetUtils();
		gridUtils = new GridUtils();
	}

	@Override
	public boolean canOpen(String uri) {
		// Just check whether the URI makes sense for this provider - not
		// whether the dataset exists.
		URI parsedUri;
		try {
			parsedUri = new URI(uri);
		} catch (URISyntaxException e) {
			return false;
		}

		String scheme = parsedUri.getScheme();
		if (scheme == null)
			return false;
		if (!scheme.toLowerCase().equals("preview"))
			return false;

		return true;
	}

	ReadWriteLock getLock(Dataset dataset) {
		List<TimeSlice> tss = datasetDao.getTimeSlices(dataset.getId());
		// Get locks for all timeslices
		TimeSliceDbReadWriteLock lock = new TimeSliceDbReadWriteLock(TaskType.Query.toString());
		for (TimeSlice ts : tss)
			lock.getTimeSliceIds().add(ts.getId());
		return lock;
	}

	// Dataset identifier looks like "rsa:name/resolution"
	private static final Pattern DATASET_PATTERN = Pattern.compile("^([^/]+)/(\\w+)");
	// TODO: Allow datasets to be selected by ID too.


	@Override
	public NetcdfDataset open(String uri, String referential,
			BoxReal boundsHint, DateTime timeMin, DateTime timeMax,
			List<String> bands) throws IOException {

		// TODO: Should provide a way to specify where (spatially) the preview
		// should be generated from.
//		VectorReal min = boundsHint.getMin();
//		VectorReal max = boundsHint.getMax();
//		Box bounds = new Box(min.getX(), min.getY(), max.getX(), max.getY());

		Dataset dataset = findDataset(uri, referential);
		if (dataset == null) {
			throw new IOException(String.format(
					"Could not find dataset %s", uri));
		}

		return getPreviewDataset(dataset, uri);
	}

	private NetcdfDataset getPreviewDataset(Dataset ds, String uri) throws IOException {
		Path previewFile = getPreviewFile(ds);
		if (!Files.exists(previewFile)) {
			ReadWriteLock lock = getLock(ds);
			if (lock.readLock().tryLock()) {
				try {
					runPreviewQuery(ds);
				} catch (QueryConfigurationException e) {
					throw new IOException("Failed to create preview file.", e);
				} finally {
					try {
						lock.readLock().unlock();
					} catch (Exception e) {
						log.error("Could not release lock. {}", e);
					}
				}
			} else {
				throw new IOException(String.format("Could not lock dataset %s",
						uri));
			}
		}
		return NetcdfDataset.openDataset(previewFile.toString());
	}

	private void runPreviewQuery(Dataset ds) throws IOException,
			QueryConfigurationException {

		QueryDefinition qd = new QueryDefinition();
		// Data Input Definition
		//	<input id="infile" href="rsa:GA_LS7_NBAR/25m" />
		DatasetInputDefinition dif = new DatasetInputDefinition();
		dif.id = "infile";
		dif.href = ds.getUri();
		qd.inputs = new ArrayList<DatasetInputDefinition>();
		qd.inputs.add(dif);

		// Grid Definition
		// <grid ref="#infile" bounds="935175.00 -3675500.0 1050975.0 -3582900.0" />
		GridDefinition grid = new GridDefinition();
		List<TimeSlice> tsList = datasetDao.getTimeSlices(ds.getId());

		Box bounds = timeSliceUtil.aggregateBounds(tsList);
		double minX = bounds.getXMin();
		double minY = bounds.getYMin();
		double maxX = bounds.getXMax();
		double maxY = bounds.getYMax();
		double centreX = (maxX - minX) / 2 + minX;
		double centreY = (maxY - minY) / 2 + minY;
		double rightX = centreX + ds.getResolution().toDouble() * PREVIEW_SIZE;
		double rightY = centreY + ds.getResolution().toDouble() * PREVIEW_SIZE;

 		grid.bounds = String.format("%.2f %.2f %.2f %.2f", centreX, centreY, rightX, rightY);
 		log.info("Bounds : {}", grid.bounds);
 		grid.ref = "#infile";

		// Data Output Definition
		// <output id="outfile">
		DatasetOutputDefinition dod = new DatasetOutputDefinition();
		dod.id = "outfile";
		dod.grid = grid;

		// TODO set Timeslice. This should be a restricted set of time slices,
		// not all of them (there could be 1000s).
//		if(startDate != null) {
//			qd.output.grid.timeMin = startDate;
//			qd.output.grid.timeMax = endDate;
//		}

		// List of band from database
		List<Band> bands = datasetDao.getBands(ds.getId());

		// Variable Definition. Create one filter for each one, too. While it
		// would be possible to have one noop filter copy data for all bands,
		// keeping them separate ensures the names of the output variables are
		// set correctly - more robust than leaving it up to the reference
		// expansion in the query definition preprocessor.
		// <variable ref="#infile/B10" />
		dod.variables = new ArrayList<VariableDefinition>();
		qd.filters = new ArrayList<FilterDefinition>();
		for(Band b : bands) {
			// 	<filter id="noop" cls="org.vpac.ndg.query.PassThrough">
			FilterDefinition fd = new FilterDefinition();
			fd.id = String.format("noop%s", b.getName());
			fd.classname = PassThrough.class.getCanonicalName();
			fd.samplers = new ArrayList<SamplerDefinition>();

			// Sampler Definition
			// 		<sampler name="input" ref="#infile/BNN">
			SamplerDefinition sd = new SamplerDefinition();
			sd.name = "input";
			sd.ref = String.format("#infile/%s", b.getName());
			fd.samplers.add(sd);

			VariableDefinition vd = new VariableDefinition();
			vd.name = b.getName();
			vd.ref = String.format("#%s/output", fd.id);

			dod.variables.add(vd);
			qd.filters.add(fd);
		}
		qd.output = dod;

		String xmlString = qd.toXML();
		log.info("QD XML : {}", xmlString);

		Version version = Version.netcdf4;

		Path outputFile = getPreviewFile(ds);
		executeQuery(qd, outputFile, version);
	}

	private void executeQuery(QueryDefinition qd, Path outputPath,
			Version netcdfVersion) throws IOException,
			QueryConfigurationException {
		NetcdfFileWriter outputDataset = NetcdfFileWriter.createNew(
				netcdfVersion, outputPath.toString());

		try {
			Query q = new Query(outputDataset);
			q.setMemento(qd, "preview:");
			try {
				q.run();
			} finally {
				q.close();
			}
		} finally {
			try {
				outputDataset.close();
			} catch (Exception e) {
				log.warn("Failed to close output file", e);
			}
		}
	}

	public Path getPreviewFile(Dataset ds) {
		return datasetUtil.getPath(ds).resolve("preview.nc");
	}

	@Override
	public DatasetMetadata queryMetadata(String uri, String referential)
			throws IOException {

		DatasetMetadata meta = new DatasetMetadata();

		// Coordinate system
		Dataset dataset = findDataset(uri, referential);
		if (dataset == null) {
			throw new IOException(String.format("Could not find dataset %s",
					uri));
		}

		NetcdfDataset ds = getPreviewDataset(dataset, uri);
		try {
			ds.enhance();
			if (ds.getCoordinateSystems().size() == 0) {
				throw new IOException(String.format(
						"Dataset %s has no coordinate system.", uri));
			}
			GridProjected grid = gridUtils.findBounds(ds);
			TimeAxis timeAxis = datasetUtils.findTimeCoordinates(ds);
			meta.setCsys(new QueryCoordinateSystem(grid, timeAxis));

			List<Variable> vars = ds.getVariables();
			List<String> varNames = new ArrayList<String>(vars.size());
			for (Variable var : vars) {
				varNames.add(var.getFullName());
			}
			meta.setVariables(varNames);

			return meta;

		} finally {
			ds.close();
		}
	}

	protected Dataset findDataset(String uri, String referential)
			throws IOException {

		URI parsedUri;
		try {
			parsedUri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new IOException("Could not open dataset", e);
		}

		String path = parsedUri.getSchemeSpecificPart();
		Matcher matcher = DATASET_PATTERN.matcher(path);
		if (!matcher.matches()) {
			throw new FileNotFoundException(
					String.format("Invalid dataset name %s", path));
		}
		String name = matcher.group(1);
		CellSize res = CellSize.fromHumanString(matcher.group(2));

		return datasetDao.findDatasetByName(name, res);
	}
}
