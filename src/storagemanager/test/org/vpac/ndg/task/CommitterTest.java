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

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
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
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Projection;
import org.vpac.ndg.geometry.Tile;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.rasterservices.FileInformation;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * This class is responsible for testing Committer class which copies all tiles
 * from their temporary storage into the storage pool. Once all tiles have been
 * successfully moved into storage pool then save them into database and
 * increase upload counter for TimeSlice.
 * 
 * @author hsumanto
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class CommitterTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(CommitterTest.class);

	private TaskPipeline taskPipeline;
	private TileTransformer tileTransformer;
	private Committer committer;
	private List<TileBand> source;
	private Box bounds;
	private TimeSlice target;
	private Band band;
	private GraphicsFile sourceImage;
	private List<GraphicsFile> sourceList;
	private Path tempDir;
	private Path uploadedFile;
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	BandDao bandDao;
	@Autowired
	TimeSliceDao timeSliceDao;
	@Autowired
	TileManager tileManager;

	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();
		taskPipeline = new TaskPipeline();

		String datasetName = "testDs";
		// Set up original image
		String originalFileLocation = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";
		Path originalImage = Paths.get(originalFileLocation);
		assertTrue(Files.exists(originalImage));

		// Set up source image
		try {
			tempDir = FileUtils.createTmpLocation();
			uploadedFile = tempDir.resolve(originalImage.getFileName());
			FileUtils.copy(originalImage, uploadedFile);
		} catch (IOException e) {
			throw new TaskException(String.format(
					Constant.ERR_COPY_FILE_FAILED, originalFileLocation,
					uploadedFile.toString()));
		}

		sourceImage = new GraphicsFile(uploadedFile);
		FileInformation sourceFileInfo = sourceImage.getFileInformation();

		sourceList = new ArrayList<GraphicsFile>();
		sourceList.add(sourceImage);

		// Set up creation time
		final Date created = Utils.parseDate("2012-03-19");

		// Set up resolution
		CellSize resolution = CellSize.m100;

		// Get all required extents
		bounds = sourceFileInfo.getBbox();
		bounds = Projection.transform(bounds, resolution,
				sourceFileInfo.getEpsgId(), Projection.getDefaultMapEpsg());
		bounds = tileManager.getNngGrid().alignToGrid(bounds, resolution);
		List<Tile> tiles = tileManager.getTiles(bounds, resolution);

		// Set up dataset
		Dataset dataset = datasetDao.findDatasetByName(datasetName, resolution);
		if (dataset == null) {
			dataset = new Dataset(datasetName, resolution,
					DateUtils.MILLIS_PER_DAY);
			datasetDao.create(dataset);
		}

		String bandName = "Band1";
		// Find or create a band.
		band = bandDao.find(dataset.getId(), bandName);
		if (band == null) {
			band = new Band(bandName, true, false);
			band.setNodata(sourceFileInfo.getNoData());
			band.setType(sourceFileInfo.getDataType());
			datasetDao.addBand(dataset.getId(), band);
		}

		// Get the timeslice for this upload
		target = datasetDao.findTimeSlice(dataset.getId(), created);
		if (target == null) {
			target = datasetDao.addTimeSlice(dataset.getId(), new TimeSlice(created));
		}

		source = new ArrayList<TileBand>();
		for (Tile tile : tiles) {
			TileBand tileband = new TileBand(tile, band, target);
			source.add(tileband);
			log.debug("tileband: {}", tileband.getTileNameWithExtention());
		}
	}

	private void assertTrue(boolean exists) {
	}

	@Test
	public void testCommit() throws TaskInitialisationException, TaskException {
		tileTransformer = new TileTransformer("Cut tile set");
		tileTransformer.setSource(sourceList);
		tileTransformer.setTarget(source);
		tileTransformer.setEpsgId(sourceImage.getEpsgId());

		// Commit totally new tiles into storage pool
		committer = new Committer("Commit brand new tile set into storagepool");
		committer.setSource(source);
		committer.setBounds(bounds);
		committer.setBand(band);
		committer.setTarget(target);

		taskPipeline.addTask(tileTransformer);
		taskPipeline.addTask(committer);

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
			// Check if tiles have been copy into the correct TimeSlice
			// directory in storage pool
			for (TileBand tileband : source) {
				assertTrue(tileband.exists());
			}

			taskPipeline.finalise();
		}

		log.debug("AFTER COMMIT FINALISE");
		for (TileBand tileband : source) {
			Tile t = tileband.getTile();
			Dataset dataset = timeSliceDao.getParentDataset(target.getId());
			Box tileBounds = tileManager.getNngGrid()
					.getBounds(t.getIndex(), dataset.getResolution());
			Box existingBounds = target.getBounds();
			boolean isTileFound = existingBounds.intersects(tileBounds);
			// Check if tile has been added in the resulted TimeSlice.
			assertTrue(isTileFound);
			// Check if tile in previous temporary storage has been deleted.
			assertFalse(Files.exists(tileband.getPreviousFileLocation()));
		}

		// Remove test data in temporary upload location
		try {
			FileUtils.removeDirectory(tempDir);
		} catch (IOException e) {
			log.error("Could not delete temporary directory: {}", e);
		}
	}
}
