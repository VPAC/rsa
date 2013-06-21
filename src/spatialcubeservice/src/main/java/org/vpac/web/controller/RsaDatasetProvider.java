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

package org.vpac.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
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
import org.vpac.ndg.datamodel.AggregationDefinition;
import org.vpac.ndg.datamodel.AggregationOpener;
import org.vpac.ndg.datamodel.RsaAggregationFactory;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.lock.RsaNetcdfDataset;
import org.vpac.ndg.lock.TimeSliceDbReadWriteLock;
import org.vpac.ndg.query.coordinates.GridProjected;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.TimeAxis;
import org.vpac.ndg.query.io.DatasetMetadata;
import org.vpac.ndg.query.io.DatasetProvider;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.BandUtil;
import org.vpac.ndg.storage.util.TimeSliceUtil;

import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateUnit;

/**
 * Gives access to datasets that are stored in an RSA.
 * @author Alex Fraser
 */
public class RsaDatasetProvider implements DatasetProvider {

	final Logger log = LoggerFactory.getLogger(RsaDatasetProvider.class);

	RsaAggregationFactory factory;
	AggregationOpener opener;

	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TimeSliceDao timeSliceDao;
	@Autowired
	TimeSliceUtil timeSliceUtil;
	@Autowired
	BandDao bandDao;
	@Autowired
	BandUtil bandUtil;
	@Autowired
	NdgConfigManager ndgConfigManager;

	public RsaDatasetProvider() {
		factory = new RsaAggregationFactory();
		opener = new AggregationOpener();
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
		if (!scheme.toLowerCase().equals("rsa"))
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

		NetcdfDataset ds;
		VectorReal min = boundsHint.getMin();
		VectorReal max = boundsHint.getMax();
		Box bounds = new Box(min.getX(), min.getY(), max.getX(), max.getY());

		Dataset dataset = findDataset(uri, referential);
		if (dataset == null) {
			throw new IOException(String.format("Could not find dataset %s",
					uri));
		}

		if (ndgConfigManager.getConfig().isFilelockingOn()) {
			ReadWriteLock lock = getLock(dataset);

			// If filelockingOn, then ensure can get all required read locks
			if (lock.readLock().tryLock()) {
				try {
					ds = open(uri, dataset, bounds, timeMin, timeMax, bands);
					try {
						ds = new RsaNetcdfDataset(ds, lock);
					} catch (IOException | RuntimeException e) {
						if (ds != null)
							ds.close();
						throw e;
					}
				} catch (IOException | RuntimeException e) {
					lock.readLock().unlock();
					throw e;
				}
			} else {
				throw new IOException(String.format("Could not lock dataset %s",
						uri));
			}

		} else {
			ds = open(uri, dataset, bounds, timeMin, timeMax, bands);
		}
		return ds;
	}

	protected NetcdfDataset open(String uri, Dataset dataset, Box bounds,
			DateTime timeMin, DateTime timeMax, List<String> bands)
			throws IOException {

		log.trace("Opening {}", dataset);
		log.trace("Bounds: {}", bounds);

		Date tmin = null;
		Date tmax = null;
		if (timeMin != null)
			tmin = timeMin.toDate();
		if (timeMax != null)
			tmax = timeMax.toDate();
		log.trace("Time constrained between {} and {}", timeMin, timeMax);
		log.trace("Bands: {}", bands);

		AggregationDefinition def = factory.create(dataset, bounds, tmin, tmax,
				bands);
		return opener.open(def, uri);
	}

	@Override
	public DatasetMetadata queryMetadata(String uri, String referential)
			throws IOException {

		DatasetMetadata md = new DatasetMetadata();

		// Coordinate system
		Dataset dataset = findDataset(uri, referential);
		if (dataset == null) {
			throw new IOException(String.format("Could not find dataset %s",
					uri));
		}
		log.trace("Querying spatial axes of {}", dataset);
		List<TimeSlice> tss = datasetDao.getTimeSlices(dataset.getId());

		GridProjected grid = queryGrid(dataset, tss);
		TimeAxis timeAxis = queryTimeAxis(dataset, tss);
		md.setCsys(new QueryCoordinateSystem(grid, timeAxis));

		// Variable names
		log.trace("Querying variable names of {}", dataset);
		List<String> bandNames = new ArrayList<>();
		for (Band b : datasetDao.getBands(dataset.getId())) {
			bandNames.add(b.getName());
		}
		// In the current RSA, all datasets have coordinate axes (1D variables)
		// x, y and time
		bandNames.add("time");
		bandNames.add("y");
		bandNames.add("x");
		md.setVariables(bandNames);

		return md;
	}

	private GridProjected queryGrid(Dataset dataset, List<TimeSlice> tss)
			throws IOException {

		// Create a bounding box that encompasses all time slices.
		Box smbounds = timeSliceUtil.aggregateBounds(tss);
		if (smbounds == null)
			throw new IOException("Could not determine bounds: no time slices.");

		BoxReal bounds = new BoxReal(2);
		bounds.getMin().setX(smbounds.getXMin());
		bounds.getMin().setY(smbounds.getYMin());
		bounds.getMax().setX(smbounds.getXMax());
		bounds.getMax().setY(smbounds.getYMax());

		// Resolution.
		double cellSize = dataset.getResolution().toDouble();
		VectorReal resolution = VectorReal.createEmpty(2);
		resolution.setX(cellSize);
		resolution.setY(cellSize);

		// All files for a given dataset share a common coordinate system. So,
		// we can just open one of the blank files and take a peek at it.
		CoordinateSystem srs = null;
		List<Band> bands = datasetDao.getBands(dataset.getId());
		for (Band b : bands) {
			Path blankTilePath = bandUtil.getBlankTilePath(dataset, b);
			if (Files.notExists(blankTilePath))
				continue;

			NetcdfDataset ncd = NetcdfDataset.openDataset(blankTilePath.toString());
			try {
				ncd.enhance();
				srs = ncd.getCoordinateSystems().get(0);
				break;
			} finally {
				ncd.close();
			}
		}
		if (srs == null) {
			throw new IOException(String.format("Could not determine " +
					"coordinate system for dataset %s. There may be no " +
					"tiles, or the bounds in the database may not match the " +
					"tiles stored on disk.", dataset));
		}

		return new GridProjected(bounds, resolution, srs);
	}

	private TimeAxis queryTimeAxis(Dataset dataset, List<TimeSlice> tss) {

		List<CalendarDate> values = new ArrayList<CalendarDate>();
		CalendarDateUnit units = timeSliceUtil.computeTimeMapping(tss, values);

		TimeAxis timeAxis = new TimeAxis();
		timeAxis.setUnits(units);
		timeAxis.setValues(values);
		return timeAxis;
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
