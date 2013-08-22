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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.gdal.gdal.gdal;
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
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storagemanager.GraphicsFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class GraphicsFileCreatorTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory
			.getLogger(GraphicsFileCreatorTest.class);

	private TaskPipeline taskPipeline;
	private TileBandCreator tilebandCreator;
	private TileTransformer tileTransformer;
	private Committer committer;
	private GraphicsFileCreator gfCreator;

	private GraphicsFile transformImage;
	private List<TileBand> tilebands;

	private NestedGrid nng;

	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TileManager tileManager;

	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();

		tilebandCreator = new TileBandCreator();
		// TASK: Use the transformer task to re-project and cut several tiles
		tileTransformer = new TileTransformer();
		// TASK: Commit the new tiles into storagepool
		committer = new Committer();
		// TASK: Creating GraphicsFile images out of the tilebands
		gfCreator = new GraphicsFileCreator("Creating graphics file");

		nng = tileManager.getNngGrid();
	}

	protected void tearDown() throws Exception {
		taskPipeline = null;
		tilebandCreator = null;
		tileTransformer = null;
		committer = null;
		gfCreator = null;
	}

	@Test
	public void testGraphicsFileCreator() throws TaskException,
			TaskInitialisationException, IOException {
		final String datasetName = "testDs";
		String srcImageLocation = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";
		Path srcImage = Paths.get(srcImageLocation);

		// Set up source image
		Path tempDir = FileUtils.createTmpLocation();
		Path uploadedFile = tempDir.resolve(srcImage.getFileName());

		try {
			FileUtils.copy(srcImage, uploadedFile);
		} catch (IOException e) {
			throw new TaskException(String.format(
					Constant.ERR_COPY_FILE_FAILED, srcImageLocation,
					uploadedFile.toString()));
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

			Band band = new Band("Band1", true, false);
			band.setNodata(transformImage.getFileInformation().getNoData());
			band.setType(transformImage.getFileInformation().getDataType());

			datasetDao.addBand(dataset.getId(), band);
		}

		// Get the timeslice for this upload
		TimeSlice timeSlice = datasetDao
				.findTimeSlice(dataset.getId(), created);
		if (timeSlice == null) {
			timeSlice = datasetDao.addTimeSlice(dataset.getId(), new TimeSlice(
					created));
		}

		FileInformation fi = transformImage.getFileInformation();
		Point<Integer> firstTile = nng.mapToTile(fi.getBbox().getUlCorner(),
				transformImage.getResolution());
		Point<Integer> lastTile = nng.mapToTile(fi.getBbox().getLrCorner(),
				transformImage.getResolution());

		int expectedNumOfTiles = (lastTile.getX() - firstTile.getX() + 1)
				* (lastTile.getY() - firstTile.getY() + 1);

		// Create an empty container to capture all tilebands
		tilebands = new ArrayList<TileBand>();

		tilebandCreator.setSource(transformImage.getBounds());
		tilebandCreator.setTarget(tilebands);
		tilebandCreator.setTimeSlice(timeSlice);
		// FIXME: Get the actual target band from somewhere - e.g. add it as a
		// column to the Upload table.
		tilebandCreator.setBand(datasetDao.getBands(dataset.getId()).get(0));

		List<GraphicsFile> sourceList = new ArrayList<GraphicsFile>();
		sourceList.add(transformImage);

		tileTransformer.setSource(sourceList);
		tileTransformer.setTarget(tilebands);
		tileTransformer.setEpsgId(transformImage.getEpsgId());
		// For testing not need to delete source and target
		tileTransformer.setCleanupSource(false);
		tileTransformer.setCleanupTarget(false);

		committer.setSource(tilebands);
		committer.setBounds(transformImage.getBounds());
		committer.setTarget(timeSlice);

		taskPipeline = new TaskPipeline();
		taskPipeline.addTask(tilebandCreator);
		taskPipeline.addTask(tileTransformer);
		taskPipeline.addTask(committer);

		taskPipeline.initialise();

		try {
			// Run tasks as a transaction
			taskPipeline.run();
		} catch (TaskException e) {
			log.error("TaskException {}", e);
			// Roll back tasks as a transaction
			taskPipeline.rollback();
			throw e;
		} catch (RuntimeException e) {
			log.error("RuntimeException {}", e);
			// Roll back tasks as a transaction
			taskPipeline.rollback();
		}

		// Cleaning up required for each task
		taskPipeline.finalise();

		assertEquals(expectedNumOfTiles, tilebands.size());

		List<GraphicsFile> images = new ArrayList<GraphicsFile>();

		gfCreator.setSource(tilebands);
		gfCreator.setTarget(images);

		taskPipeline = new TaskPipeline();
		taskPipeline.addTask(gfCreator);

		taskPipeline.initialise();
		try {
			// Run tasks as a transaction
			taskPipeline.run();
		} catch (TaskException e) {
			log.error("TaskException {}", e);
			// Roll back tasks as a transaction
			taskPipeline.rollback();
		} catch (RuntimeException e) {
			log.error("RuntimeException {}", e);
			// Roll back tasks as a transaction
			taskPipeline.rollback();
		}

		// Cleaning up required for each task
		taskPipeline.finalise();

		// Remove test data in temporary upload location
		try {
			FileUtils.removeDirectory(tempDir);
		} catch (IOException e) {
			log.error("Could not delete temporary directory: {}", e);
		}
	}
}
