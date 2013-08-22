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

package org.vpac.ndg.query.coordinates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.DatasetInput;
import org.vpac.ndg.query.DatasetMeta;
import org.vpac.ndg.query.DatasetStore;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryDefinition.GridDefinition;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorReal;

import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateUnit;
import ucar.unidata.geoloc.ProjectionImpl;

public class CoordinateUtils {

	final Logger log = LoggerFactory.getLogger(CoordinateUtils.class);

	DatasetStore datasetStore;
	WarpFactory warpFactory;

	public CoordinateUtils(DatasetStore datasetStore) {
		this.datasetStore = datasetStore;
		warpFactory = new WarpFactory();
	}

	/**
	 * Determine the <em>spatial</em> coordinate system, bounds and resolution.
	 * The bounds as specified by the user will be used, except for dimensions
	 * that were not given - for which the automatically determined bounds will
	 * be used. Note that the rank of the generated grid will be at least as
	 * great as the ranks of the requested and automatic bounds.
	 * <p>
	 * This coordinate system is global: it encompasses all input and output
	 * datasets for the current query.
	 * </p>
	 */
	public GridProjected initialiseGrid(GridDefinition gd)
			throws QueryConfigurationException {

		if (gd == null) {
			throw new QueryConfigurationException("Output dataset lacks a " +
					"grid definition.");
		}

		if (gd.ref == null) {
			throw new QueryConfigurationException("Grid definition lacks a " +
					"reference to a coordinate system. Defintion of new " +
					"coordinate systems is not implemented.");
		}

		DatasetMeta sd = datasetStore.findDataset(gd.ref);
		CoordinateSystem srs = sd.getGrid().getSrs();

		// Iterate over inputs to gather automatic bounds.
		// Always find the automatic bounds. Even if bounds are explicitly
		// defined, the user may not have specified all the required dimensions.
		if (gd.autobounds == null)
			gd.autobounds = "union";
		boolean union = gd.autobounds.equals("union");
		BoxReal autobounds = collateBounds(
				datasetStore.getInputDatasets(), srs, union);
		VectorReal autoResolution = sd.getGrid().getResolution();

		BoxReal requestedBounds;
		if (gd.bounds == null)
			requestedBounds = autobounds;
		else
			requestedBounds = new BoxReal(gd.bounds);

		log.debug("Requested bounds: {}", requestedBounds);

		// The user may have requested bounds that have fewer dimensions than
		// the input. Or, they may have requested more! Enlarge as necessary.
		log.info("Resizing requested bounding box from {}D to {}D.",
				requestedBounds.getRank(), autobounds.getRank());

		// Take some axes from the explicit box, and the rest from the
		// automatic one.
		int rank = Math.max(requestedBounds.getRank(), autobounds.getRank());
		BoxReal bounds = new BoxReal(rank);
		VectorReal resolution = VectorReal.createEmpty(rank);

		Swizzle resize = SwizzleFactory.resize(
				requestedBounds.getRank(), autobounds.getRank());
		resize.swizzle(requestedBounds, bounds);

		// Auto bounds (inherit)
		Swizzle resizeAuto = SwizzleFactory.resize(autobounds.getRank(), rank);
		resizeAuto.swizzle(autobounds, bounds);

		// Auto resolution (inherit)
		Swizzle resizeAutoRes = SwizzleFactory.resize(autobounds.getRank(),
				rank, '1');
		resizeAutoRes.swizzle(autoResolution, resolution);

		// Explicit bounds (override)
		Swizzle resizeReq = SwizzleFactory.resize(requestedBounds.getRank(), rank);
		resizeReq.swizzle(requestedBounds, bounds);

		GridProjected grid = new GridProjected(bounds, resolution, srs);
		log.debug("Target grid: {}", grid);
		return grid;
	}

	/**
	 * Reduce the dimensionality of a grid to match the specified dimensions.
	 *
	 * @param grid The grid to adapt.
	 * @param dimensions The dimensions to adapt it to. Note that non-grid
	 *            dimensions like time will be ignored.
	 * @return A grid with the same number of grid dimensions as are available
	 *         in <em>dimensions<em>.
	 * @throws QueryConfigurationException if there are more requested spatial
	 *             dimensions than are available in the grid.
	 */
	public GridProjected adaptGrid(GridProjected grid, String[] dimensions)
			throws QueryConfigurationException {

		int rank = 0;
		for (String dim : dimensions) {
			if (dim.equals("time"))
				continue;
			rank++;
		}

		if (grid.getRank() == rank) {
			return grid;
		} else if (grid.getRank() < rank) {
			throw new QueryConfigurationException(String.format(
					"Output requests more dimensions (%s) than were " +
					"specified by the grid (%s).", rank, grid.getRank()));
		}

		// Shrink
		Swizzle resize = SwizzleFactory.resize(grid.getRank(), rank);
		BoxReal bounds = new BoxReal(rank);
		VectorReal resolution = VectorReal.createEmpty(rank);
		resize.swizzle(grid.getBounds(), bounds);
		resize.swizzle(grid.getResolution(), resolution);
		return new GridProjected(bounds, resolution, grid.getSrs());
	}

	/**
	 * Find the combined bounds of a collection of objects.
	 *
	 * @param targetSrs The coordinate system of the output.
	 * @param union If true, the bounds will be added together. Otherwise, the
	 *        intersection (common area) will be returned.
	 * @return The combined bounds, in the target coordinate system.
	 */
	public BoxReal collateBounds(
			Collection<? extends HasGridProjected> constituents,
			CoordinateSystem targetSrs, boolean union) {

		BoxReal bounds = null;

		for (HasGridProjected child : constituents) {
			BoxReal currentBounds = transformBounds(child.getGrid(), targetSrs);

			if (bounds == null)
				bounds = currentBounds;
			else if (union)
				bounds.union(currentBounds);
			else
				bounds.intersect(currentBounds);
		}

		return bounds;
	}

	public BoxReal transformBounds(GridProjected grid,
			CoordinateSystem targetSrs) {

		CoordinateSystem sourceSrs = grid.getSrs();
		ProjectionImpl sourceProj = sourceSrs.getProjection();
		ProjectionImpl targetProj = targetSrs.getProjection();

		// Convert from source projection to target.
		Warp xform = warpFactory.createSpatialWarp(sourceProj, targetProj);
		BoxReal bounds = grid.getBounds().copy();
		xform.warp(bounds);
		return bounds;
	}

	/**
	 * @return A list of unique time coordinates across all input datasets.
	 */
	public TimeAxis collateTime(Collection<DatasetInput> ds)
			throws QueryConfigurationException {
		// Collect all elements into one long list.
		List<CalendarDate> combinedTc = new ArrayList<CalendarDate>();
		CalendarDateUnit units = null;
		for (DatasetInput dm : ds) {
			TimeAxis localTimeAxis = dm.getCoordinateSystem().getTimeAxis();
			if (localTimeAxis == null)
				continue;
			List<CalendarDate> tc = localTimeAxis.getValues();
			if (tc == null)
				continue;

			CalendarDateUnit localUnits = localTimeAxis.getUnits();
			if (units == null) {
				units = localUnits;
			} else if (!units.getTimeUnit().equals(localUnits.getTimeUnit())) {
				// Actually with a bit of extra effort, it may be possible to
				// combine different units. But it could also get quite messy,
				// e.g. what should be done when combining days with
				// milliseconds, and what would that imply about the precision?
				throw new QueryConfigurationException(String.format(
						"Can't combine datasets with different temporal units."));
			}

			log.debug("Dataset {} has times {}", dm, tc);
			combinedTc.addAll(tc);
		}

		// Sort it, and remove duplicates.
		Collections.sort(combinedTc);
		List<CalendarDate> collatedTc = new ArrayList<CalendarDate>();
		CalendarDate last = null;
		for (CalendarDate elem : combinedTc) {
			if (elem.equals(last))
				continue;
			collatedTc.add(elem);
			last = elem;
		}

		log.debug("Collated times: {}", collatedTc);
		log.debug("Temporal units: {}", units);

		TimeAxis timeAxis = new TimeAxis();
		timeAxis.setUnits(units);
		timeAxis.setValues(collatedTc);
		return timeAxis;
	}

}
