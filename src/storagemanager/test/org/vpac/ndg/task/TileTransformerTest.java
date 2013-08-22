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

package org.vpac.ndg.task;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.gdal.gdal.gdal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.Utils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Tile;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.rasterservices.FileInformation;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storagemanager.GraphicsFile;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class TileTransformerTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(TileTransformerTest.class);

	private TaskPipeline taskPipeline;
	private TileTransformer tileTransformer;
	private GraphicsFile sourceImage;
	private List<GraphicsFile> source;
	private List<TileBand> target;
	private Path tmpUploadLocation;
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	BandDao bandDao;
	@Autowired
	TileManager tileManager;
	
	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();

		taskPipeline = new TaskPipeline();

		final String datasetName = "testDs";
		// Set up original image
		Path originalImage = Paths.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		if(!Files.exists(originalImage)) {
			return;
		}

		// Set up source image
		tmpUploadLocation = FileUtils.createTmpLocation();
		Path uploadedFileLocation = tmpUploadLocation.resolve(originalImage.getFileName());

		try {
			FileUtils.copy(originalImage, uploadedFileLocation);
		} catch (IOException e) {
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, originalImage, uploadedFileLocation));
		}

		sourceImage = new GraphicsFile(uploadedFileLocation);
		source = new ArrayList<GraphicsFile>();
		source.add(sourceImage);

		// Set up creation time
		final Date created = Utils.parseDate("2012-03-19");

		// Set up resolution
		CellSize resolution = CellSize.m100;

		// Set up dataset
		// Set up dataset
		Dataset dataset = datasetDao.findDatasetByName(datasetName, resolution);
		if (dataset == null) {
			dataset = new Dataset(datasetName, resolution,
					DateUtils.MILLIS_PER_DAY);
			datasetDao.create(dataset);
		}

		// Find or create band
		FileInformation fi = sourceImage.getFileInformation();
		Band band = bandDao.find(dataset.getId(), "Band1");
		if (band == null) {
			band = new Band("Band1", false, false);
			band.setNodata(fi.getNoData());
			band.setType(fi.getDataType());
			datasetDao.addBand(dataset.getId(), band);
		} else {
			//assertEquals("Band nodata value does not match import", band.getNodata(), fi.getNoData());
			//assertEquals("Band data type does not match import", band.getType(), fi.getDataType());
		}

		// Get the timeslice for this upload
		TimeSlice timeSlice = datasetDao.findTimeSlice(dataset.getId(), created);
		if (timeSlice == null) {
			timeSlice = new TimeSlice(created);
			datasetDao.addTimeSlice(dataset.getId(), timeSlice);
		}

		FileInformation sourceFileInfo = sourceImage.getFileInformation();
		// Get all required extents
		List<Tile> tiles = tileManager.getTiles(sourceFileInfo.getBbox(), resolution);

		target = new ArrayList<TileBand>();
		for(Tile tile: tiles) {
			TileBand tileband = new TileBand(tile, band, timeSlice);
			target.add(tileband);
			log.debug("tileband: {}", tileband.getTileNameWithExtention());
		}
	}

	@After
	public void tearDown() throws Exception {
		// Remove test data in temporary upload location
		FileUtils.removeDirectory(tmpUploadLocation);
	}

	@Test
	public void testTiling() throws TaskInitialisationException, TaskException {
		// TASK 3: Use the transformer task to re-project and cut several tiles
		tileTransformer = new TileTransformer();
		tileTransformer.setSource(source);
		tileTransformer.setTarget(target);
		tileTransformer.setEpsgId(sourceImage.getEpsgId());
		tileTransformer.setTemporaryLocation(tmpUploadLocation);
		tileTransformer.setUseBilinearInterpolation(false);
		tileTransformer.setSrcnodata(sourceImage.getNoData());
		tileTransformer.setDstnodata(sourceImage.getNoData());
		tileTransformer.setDatatype(sourceImage.getFileInformation().getDataType());
		
		taskPipeline.addTask(tileTransformer);
		taskPipeline.initialise();
		
		try {
			// Run tasks as a transaction
			taskPipeline.run();
		} catch (TaskException e) {
			// Roll back tasks as a transaction
			taskPipeline.rollback();
			// Report error
			log.error("{}", e);
			throw e;
		} finally {	
			// Check extents of each resulting tile
			for(TileBand tileband: target) {
				FileInformation fi = FileInformation.read(tileband.getFileLocation());			
				// Check if the tile produced is correct size
				Box tileBounds = tileband.getBounds();			
				log.debug("Expected: {}", tileBounds.toString());
				log.debug("Actual:   {}", fi.getBbox().toString());
				assertTrue(tileBounds.getXMin() == fi.getBbox().getXMin());
				assertTrue(tileBounds.getYMin() == fi.getBbox().getYMin());
				assertTrue(tileBounds.getXMax() == fi.getBbox().getXMax());
				assertTrue(tileBounds.getYMax() == fi.getBbox().getYMax());
				
				// Check if tile resolution is correct
				log.debug("tileband.getResolution().toDouble():{}", tileband.getResolution().toDouble());
				log.debug("fi.getPixelSizeX():{}", fi.getPixelSizeX());
				assertEquals(tileband.getResolution().toDouble(), fi.getPixelSizeX(), .000);
				assertEquals(tileband.getResolution().toDouble(), Math.abs(fi.getPixelSizeY()), .000);
				
				// Check if projection is correct
				assertEquals(tileTransformer.getEpsgId(), fi.getEpsgId());
				
				// Check if nodata is correct
				assertEquals(sourceImage.getNoData(), fi.getNoData());
			}
			
			// Cleaning up required for each task
			taskPipeline.finalise();
		}		
		
		// Check whether the execution is successful.
	}
}
