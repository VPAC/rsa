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

package org.vpac.ndg.datamodel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.datamodel.AggregationDefinition.DimDef;
import org.vpac.ndg.datamodel.AggregationDefinition.VarDef;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.BoxInt;
import org.vpac.ndg.geometry.NestedGrid;
import org.vpac.ndg.geometry.Point;
import org.vpac.ndg.geometry.Tile;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.BandUtil;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storagemanager.IGraphicsFile;

import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateUnit;

/**
 * Creates NCML objects for RSA datasets.
 * @author Alex Fraser
 */
public class RsaAggregationFactory extends AggregationFactory {

	final Logger log = LoggerFactory.getLogger(RsaAggregationFactory.class);

	DatasetDao datasetDao;
	TimeSliceDao timeSliceDao;
	TimeSliceUtil timeSliceUtil;
	TileManager tileManager;
	NdgConfigManager ndgConfigManager;
	BandUtil bandUtil;

	public RsaAggregationFactory() {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		datasetDao = (DatasetDao) appContext.getBean("datasetDao");
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		tileManager = (TileManager) appContext.getBean("tileManager");
		ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		bandUtil = (BandUtil) appContext.getBean("bandUtil");
	}

	/**
	 * Create a data cube that references all the data in a dataset (i.e. all
	 * bands, times and extents).
	 * 
	 * @param dataset
	 *            The dataset to use as the source.
	 * @return A data cube that refers to the dataset's underlying files.
	 * @throws IOException 
	 */
	public AggregationDefinition create(Dataset dataset) throws IOException {
		List<TimeSlice> tss = datasetDao.getTimeSlices(dataset.getId());
		Collections.sort(tss);
		List<Band> bs = datasetDao.getBands(dataset.getId());
		return create(dataset, tss, bs);
	}

	public AggregationDefinition create(Dataset dataset, Box boundsHint,
			Date timeMin, Date timeMax, List<String> bands) throws IOException {
		List<TimeSlice> tss;
		try {
			tss = datasetDao.findTimeSlices(dataset.getId(),
				timeMin, timeMax);
		} catch (RuntimeException e) {
			throw new IOException(String.format("Could not collect time " +
					"slice list: %s", e.getMessage()));
		}
		Collections.sort(tss);
		List<Band> bs;
		if (bands != null)
			bs = datasetDao.findBandsByName(dataset.getId(), bands);
		else
			bs = datasetDao.getBands(dataset.getId());
		return create(dataset, tss, bs, boundsHint);
	}

	/**
	 * Create a data cube that references the specified time slices and bands.
	 * 
	 * @param dataset
	 *            The dataset to use as the source.
	 * @param tss
	 *            The time slices to access.
	 * @param bs
	 *            The bands to access.
	 * @return Returns a data cube that references the specified time slices 
	 * and bands.
	 * @throws IOException 
	 */
	public AggregationDefinition create(Dataset dataset, List<TimeSlice> tss,
			List<Band> bs) throws IOException {

		Box bounds = timeSliceUtil.aggregateBounds(tss);
		if (bounds == null) {
			// Create a dummy box. It's not going to be used anyway: no time
			// slices means no tiles!
			bounds = new Box(new Point<Double>(0.0, 0.0));
		}
		return create(dataset, tss, bs, bounds);
	}

	/**
	 * Generates a 4D datacube from a dataset. Tiles that don't exist will be
	 * substituted by a blank file; this is due to the constraints of the
	 * <em>tiled</em> aggregation type. Bands and time slices that are
	 * completely empty will be excluded.
	 * @throws IOException 
	 */
	public AggregationDefinition create(Dataset ds, List<TimeSlice> tss,
			List<Band> bs, Box boundsHint) throws IOException {

		log.debug("Aggregation requested with bands {} and timeslices {}", bs,
				tss);

		// Filter out empty bands and time slices. Band filtering is especially
		// important, because blank tiles can not be generated if the band is
		// completely empty due to the lack of a nodata value.
		bs = filterBands(bs);
		tss = filterTimeSlices(tss);

		if (boundsHint == null)
			boundsHint = timeSliceUtil.aggregateBounds(tss);
		if (boundsHint == null) {
			throw new IllegalArgumentException("Can't create aggregation: " +
					"could not determine spatial extents.");
		}

		if (bs.size() == 0) {
			throw new IllegalArgumentException("Can't create aggregation: " +
					"none of the bands have been initialised.");
		}
		if (tss.size() == 0) {
			throw new IllegalArgumentException("Can't create aggregation: " +
					"none of the time slices contain any data.");
		}

		// Get global list of times
		List<CalendarDate> all_coords = new ArrayList<>();
		CalendarDateUnit timeUnits = timeSliceUtil.computeTimeMapping(tss, all_coords);
		VarDef vd = VarDef.newDimension("time", "int", timeUnits.toString());

		// Get global list of tiles
		BoxInt globalTileBounds = tileManager.mapToTile(boundsHint,
				ds.getResolution());
		List<Tile> tiles = tileManager.getTiles(globalTileBounds);
		List<String> sections = collectTileSections(tiles, ds.getResolution());

		List<IGraphicsFile> blanks = new ArrayList<>();
		for (Band b : bs) {
			// Blank tile creation is usually done in the Committer task during
			// import. But if the file has been deleted, a new one may be
			// created. Not thread-safe, but unlikely to happen anyway...
			blanks.add(bandUtil.getBlankTile(ds, b));
		}

		// Aggregate by band and tile
		List<AggregationDefinition> children = new ArrayList<>();
		List<CalendarDate> coords = new ArrayList<>();
		int tsIndex = 0;
		for (TimeSlice ts : tss) {
			BoxInt tileBounds = tileManager.mapToTile(ts.getBounds(),
					ds.getResolution());
			tileBounds.intersect(globalTileBounds);
			AggregationDefinition aggDef = union(ds, ts, bs, blanks,
					tileBounds, tiles, sections);
			if (aggDef != null && aggDef.nValidChildren > 0) {
				children.add(aggDef);
				coords.add(all_coords.get(tsIndex));
			}
			tsIndex++;
		}

		// Aggregate by time
		List<String> bandNames = new ArrayList<>();
		for (Band b : bs) {
			bandNames.add(b.getName());
		}
		List<String> coordValues = timeSliceUtil.datesToCoordValues(
				timeUnits, coords);
		AggregationDefinition aggDef = joinNew(children, bandNames, vd,
				coordValues);

		// Explicitly define coordinate axes, because the insertion of blank
		// tiles may confuse the reader.
		NestedGrid nng = tileManager.getNngGrid();
		Box finalExtents = nng.alignToTileGrid(boundsHint, ds.getResolution());
		double res = ds.getResolution().toDouble();
		int width = (int)(finalExtents.getWidth() / res);
		int height = (int)(finalExtents.getHeight() / res);
		// Coordinates are at the centre of each cell. This does not change the
		// bounds.
		Point<Double> firstCellCentre = nng.alignToGridCentre(finalExtents.getMin(), ds.getResolution());
		aggDef.getDimensions().add(new DimDef("x", Integer.toString(width)));
		aggDef.getVariables().add(VarDef.regularCoordinateAxis("x",
				Double.toString(firstCellCentre.getX()),
				Double.toString(res)));
		aggDef.getDimensions().add(new DimDef("y", Integer.toString(height)));
		aggDef.getVariables().add(VarDef.regularCoordinateAxis("y",
				Double.toString(firstCellCentre.getY()),
				Double.toString(res)));

		return aggDef;
	}

	/**
	 * Filters out empty time slices
	 */
	protected List<TimeSlice> filterTimeSlices(List<TimeSlice> tss) {
		List<TimeSlice> tsfiltered = new ArrayList<>();
		for (TimeSlice ts : tss) {
			if (!timeSliceUtil.isEmpty(ts))
				tsfiltered.add(ts);
			else
				log.debug("Excluding empty timeslice {}", ts);
		}
		return tsfiltered;
	}

	/**
	 * Filters out empty bands
	 */
	protected List<Band> filterBands(List<Band> bs) {
		List<Band> bsfiltered = new ArrayList<>();
		for (Band b : bs) {
			if (!bandUtil.isEmpty(b))
				bsfiltered.add(b);
			else
				log.debug("Excluding empty band {}", b);
		}
		return bsfiltered;
	}

	/**
	 * Generates a 3D banded and tiled aggregation for a single time slice.
	 */
	protected AggregationDefinition union(Dataset ds, TimeSlice ts,
			List<Band> bs, List<IGraphicsFile> blanks, BoxInt tileBounds,
			List<Tile> tiles, List<String> sections) {

		List<AggregationDefinition> children = new ArrayList<>();
		List<String> bandNames = new ArrayList<>();
		int nPopulated = 0;
		for (int i = 0; i < bs.size(); i++) {
			Band b = bs.get(i);
			IGraphicsFile blank = blanks.get(i);
			AggregationDefinition aggDef = tile(ds, ts, b, blank, tileBounds,
					tiles, sections);
			children.add(aggDef);
			bandNames.add(b.getName());
			nPopulated += aggDef.nValidChildren;
		}

		AggregationDefinition ad = union(children, bandNames);
		ad.nValidChildren = nPopulated;
		return ad;
	}

	/**
	 * Generates a 2D tiled aggregation for a single band and time slice pair.
	 */
	protected AggregationDefinition tile(Dataset ds, TimeSlice ts, Band band,
			IGraphicsFile blankTile, BoxInt tileBounds, List<Tile> tiles,
			List<String> sections) {

		List<AggregationDefinition> children = new ArrayList<>();
		List<String> filteredSections = new ArrayList<>();
		int nPopulatedTiles = 0;
		for (int i = 0; i < tiles.size(); i++) {

			// Use a blank file if the tile does not exist.
			AggregationDefinition aggDef;
			if (!tileBounds.contains(tiles.get(i).getIndex())) {
				// Tile is outside bounds of time slice.
				aggDef = raw(blankTile, false);

			} else {
				TileBand tb = new TileBand(tiles.get(i), band, ts);
				if (tb.existsInStoragepool()) {
					aggDef = raw(tb, false);
					nPopulatedTiles++;
				} else {
					aggDef = raw(blankTile, false);
				}
			}

			children.add(aggDef);
			filteredSections.add(sections.get(i));
		}

		// If there were no valid children, just return null so this aggregation
		// can be excluded from the NCML.
		AggregationDefinition ad = tile(children, "y x", filteredSections);
		ad.nValidChildren = nPopulatedTiles;
		return ad;
	}

	/**
	 * Computes the sections for all tiles in a list, relative to the first
	 * tile.
	 */
	protected List<String> collectTileSections(List<Tile> tiles,
			CellSize resolution) {

		int tileSize = tileManager.getResolutionList().get(resolution);
		Point<Integer> size = new Point<Integer>(tileSize, tileSize);
		// TODO: Don't assume tile zero is the first in the rasterisation. Add
		// a new method, like TileManager.getFirst.
		Point<Integer> origin = tiles.get(0).getIndex();

		boolean invertYOrder = ndgConfigManager.getConfig().isUpPositive();
		if (invertYOrder) {
			size.setY(0 - size.getY());
			origin.setY(origin.getY() + 1);
		}

		List<String> sections = new ArrayList<>();
		for (Tile t : tiles) {
			Point<Integer> offset = Point.subi(t.getIndex(), origin);
			Point<Integer> start = Point.muli(offset, size);
			Point<Integer> end = Point.addi(start, size);
			String section;
			if (invertYOrder) {
				section = String.format("%d:%d,%d:%d",
						end.getY(), start.getY() - 1,
						start.getX(), end.getX() - 1);
			} else {
				section = String.format("%d:%d,%d:%d",
						start.getY(), end.getY() - 1,
						start.getX(), end.getX() - 1);
			}
			sections.add(section);
		}
		return sections;
	}

	/**
	 * Generate a single Ncml element that references a graphics file.
	 * 
	 * @param child The file to reference. This method does <em>not</em> check
	 *        that the file exists; that is the responsibility of the caller.
	 * @param relative If true, only the file name (not path) will appear in the
	 *        element.
	 */
	public AggregationDefinition raw(IGraphicsFile child, boolean relative) {
		Path path;
		if (relative)
			path = child.getFileLocation().getFileName();
		else
			path = child.getFileLocation();

		return new AggregationDefinition(path);
	}
}
