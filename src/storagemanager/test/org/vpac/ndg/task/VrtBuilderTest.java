package org.vpac.ndg.task;

import static org.junit.Assert.assertFalse;
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
public class VrtBuilderTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(VrtBuilderTest.class);

	private TaskPipeline taskPipeline;
	private TileTransformer tileTransformer;
	private VrtBuilder vrtBuilder;
	private List<TileBand> source;	
	private GraphicsFile sourceImage;
	private List<GraphicsFile> sourceList;
	private TimeSlice timeSlice;
	private Path tempDir;
	private Path uploadedFile;
	private Band band;
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
		String originalFileLocation = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";
		Path originalImage = Paths.get(originalFileLocation);
		assertTrue(Files.exists(originalImage));

		// Set up source image
		tempDir = FileUtils.createTmpLocation();
		uploadedFile = tempDir.resolve(originalImage.getFileName());

		try {
			FileUtils.copy(originalImage, uploadedFile);
		} catch (IOException e) {
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, originalFileLocation, uploadedFile.toString()));
		}

		sourceImage = new GraphicsFile(uploadedFile);
		sourceList = new ArrayList<GraphicsFile>();
		sourceList.add(sourceImage);

		// Set up creation time
		final Date created = Utils.parseDate("2012-03-19");

		// Set up resolution
		CellSize resolution = CellSize.m100;

		// Set up dataset
		Dataset dataset = datasetDao.findDatasetByName(datasetName, resolution);
		if (dataset == null) {
			dataset = new Dataset(datasetName, resolution,
					DateUtils.MILLIS_PER_DAY);
			datasetDao.create(dataset);
		}

		// Find or create band
		FileInformation fi = sourceImage.getFileInformation();
		band = bandDao.find(dataset.getId(), "Band1");
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
		timeSlice = datasetDao.findTimeSlice(dataset.getId(), created);
		if (timeSlice == null) {
			timeSlice = new TimeSlice(created);
			datasetDao.addTimeSlice(dataset.getId(), timeSlice);
		}

		FileInformation sourceFileInfo = sourceImage.getFileInformation();
		// Get all required extents
		List<Tile> tiles = tileManager.getTiles(sourceFileInfo.getBbox(), resolution);

		source = new ArrayList<TileBand>();
		for(Tile tile: tiles) {	
			TileBand tileband = new TileBand(tile, band, timeSlice);
			source.add(tileband);	
			log.debug("tileband: {}", tileband.getTileNameWithExtention());
		}

	}

	@After
	public void tearDown() throws Exception {
		// Remove test data in temporary upload location
		try {
			FileUtils.removeDirectory(tempDir);
		} catch (IOException e) {
			log.error("Could not delete temporary directory. {}", e);	
		}
	}

	@Test
	public void testVrtBuilder() throws TaskInitialisationException, TaskException {
		tileTransformer = new TileTransformer();
		tileTransformer.setSource(sourceList);
		tileTransformer.setTarget(source);
		tileTransformer.setEpsgId(sourceImage.getEpsgId());

		Path vrtLoc = tempDir.resolve("test" + Constant.EXT_VRT);
		GraphicsFile vrtFile = new GraphicsFile(vrtLoc);

		vrtBuilder = new VrtBuilder();
		vrtBuilder.setSource(source);
		vrtBuilder.setTarget(vrtFile);
		vrtBuilder.setTimeSlice(timeSlice);
		vrtBuilder.setBand(band);
		
		taskPipeline.addTask(tileTransformer);
		taskPipeline.addTask(vrtBuilder);
		
		// Run each specific initialisation on each task in pipeline
		taskPipeline.initialise();
	
		try {
			// Run tasks as a transaction
			taskPipeline.run();
			// Check if tiles have been copy into the correct TimeSlice directory in storage pool			
			for(TileBand tileband: source) {
				assertTrue("file doesn't exist.", tileband.exists());
			}

			// Check if vrt exist in temporary storage
			assertTrue(vrtFile.exists());
		} catch (TaskException e) {
			// Roll back tasks as a transaction
			taskPipeline.rollback();
			throw e;
		} finally {
			taskPipeline.finalise();
		}
		
		// Check if vrt has been successfully moved into storage pool
		assertTrue(vrtFile.exists());
		
		// Check if vrt in temporary storage has been properly deleted in after finalise method
		for(Path tmpVrt: vrtBuilder.getTmpFileList()) {
			assertFalse(Files.exists(tmpVrt));
		}
	}
}
