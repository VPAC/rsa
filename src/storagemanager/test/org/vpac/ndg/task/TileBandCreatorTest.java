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

package org.vpac.ndg.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.vpac.ndg.geometry.NestedGrid;
import org.vpac.ndg.geometry.Point;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.rasterservices.FileInformation;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * This is the test class for TileRasteriser class.
 * It check whether: 
 * - the tiles have been cut properly from the source image.
 * - the resulting tiles has the correct number of tiles. 
 * 
 * @author hsumanto
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class TileBandCreatorTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(TileBandCreatorTest.class);

	private TaskPipeline taskPipeline;
	private TileBandCreator tilebandCreator;
	
	private GraphicsFile transformImage;	
	private List<TileBand> tilebands;
	
	private NestedGrid nng;
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	BandDao bandDao;
	@Autowired
	TileManager tileManager;
	
	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();
		tilebandCreator = new TileBandCreator();
		
		nng = tileManager.getNngGrid();
	}

	@After
	public void tearDown() throws Exception {
		taskPipeline = null;
		tilebandCreator = null;
	}	
	
	@Test
	public void testTilebandCreator() throws TaskException, TaskInitialisationException, IOException {
		final String datasetName = "testDs";
		String srcImageLocation = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";		
		Path srcImage = Paths.get(srcImageLocation);			
		assertTrue(Files.exists(srcImage));

		// Set up source image
		Path tempDir = FileUtils.createTmpLocation();
		Path uploadedFile = tempDir.resolve(srcImage.getFileName());

		try {
			FileUtils.copy(srcImage, uploadedFile);
		} catch (IOException e) {
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, srcImageLocation, uploadedFile.toString()));
		}

		// Set up creation time
		final Date created = Utils.parseDate("2012-03-19");

		// Set up resolution
		CellSize resolution = CellSize.m100;

		transformImage = new GraphicsFile(uploadedFile);
		transformImage.setResolution(resolution);

		// Set up dataset
		Dataset dataset = datasetDao.findDatasetByName(datasetName, resolution);
		if (dataset == null) {
			dataset = new Dataset(datasetName, resolution,
					DateUtils.MILLIS_PER_DAY);
			datasetDao.create(dataset);
		}

		// Find or create band
		FileInformation fi = transformImage.getFileInformation();
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

		Point<Integer> firstTile = nng.mapToTile(fi.getBbox().getUlCorner(), transformImage.getResolution());
		Point<Integer> lastTile = nng.mapToTile(fi.getBbox().getLrCorner(), transformImage.getResolution());
				
		int expectedNumOfTiles = (lastTile.getX() - firstTile.getX() + 1) * (lastTile.getY() - firstTile.getY() + 1); 

		// Create an empty container to capture all tilebands
		tilebands = new ArrayList<TileBand>(); 		
		
		tilebandCreator.setSource(transformImage.getBounds());
		tilebandCreator.setTarget(tilebands);
		tilebandCreator.setTimeSlice(timeSlice);
		tilebandCreator.setBand(band);
		
		taskPipeline = new TaskPipeline();
		taskPipeline.addTask(tilebandCreator);

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
			// Cleaning up required for each task
			taskPipeline.finalise();
		}		
		
		// Check whether the execution is successful.
		assertEquals(expectedNumOfTiles, tilebands.size());
		
		// Remove test data in temporary upload location
		try {
			FileUtils.removeDirectory(tempDir);
		} catch (IOException e) {
			log.error("Could not delete temporary directory. {}", e);	
		}
	}
}
