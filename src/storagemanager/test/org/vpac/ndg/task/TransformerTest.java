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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.gdal.gdal.gdal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.rasterservices.FileInformation;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * This is the test class for Transformer class.
 * It check whether: 
 * - the transformation can perform re-projection correctly
 * - the result of transformation has the correct resolution
 * 
 * @author hsumanto
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class TransformerTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(TransformerTest.class);

	private TaskPipeline taskPipeline;
	private Transformer reprojector;
	
	private GraphicsFile sourceImage;
	private List<GraphicsFile> source;
	private GraphicsFile target;	
	
	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();
		taskPipeline = new TaskPipeline();
		reprojector = new Transformer();	
	}

	@After
	public void tearDown() throws Exception {
		taskPipeline = null;
		reprojector = null;
	}
	
	/**
	 * Test if the test case can successfully perform re-projection correctly
	 * and if the result of re-projection has the correct resolution.
	 * @throws TaskException
	 * @throws TaskInitialisationException 
	 * @throws IOException 
	 */
	@Test
	public void testReprojectionTransformation() throws TaskException, TaskInitialisationException, IOException {
		// Set up original image
		String originalFileLocation = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";
		Path originalImage = Paths.get(originalFileLocation);
		assertTrue(Files.exists(originalImage));
		
		// Set up source image
		Path tempDir = FileUtils.createTmpLocation();
		Path uploadedFile = tempDir.resolve(originalImage.getFileName());
		
		try {
			FileUtils.copy(originalImage, uploadedFile);
		} catch (IOException e) {
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, originalFileLocation, uploadedFile.toString()));
		}
		
		sourceImage = new GraphicsFile(uploadedFile);
		source = new ArrayList<GraphicsFile>();
		source.add(sourceImage);
				
		// Set up transform image
		Path targetImageFile = tempDir.resolve("reprojected.nc");
		target = new GraphicsFile(targetImageFile);		
		target.setEpsgId(3112);
		target.setResolution(CellSize.m100);
		
		// TASK 1: Create transformation command to do re-projection into EPSG:3112 and m100 resolution
		reprojector.setSource(source);
		reprojector.setTarget(target);
		reprojector.setSrcnodata("-999.0");
		reprojector.setDstnodata("-999.0");
		
		taskPipeline.addTask(reprojector);
		
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
			// Check target before it is being deleted in finalise
			FileInformation fi = FileInformation.read(target.getFileLocation());			
			// Check if can get file information
			assertNotNull(fi);
			// Check if the target has EPSG:3112
			assertEquals(fi.getEpsgId(), target.getEpsgId());
			// Check if the target pixel size for x and y is 100.0  
			assertEquals(CellSize.m100.toDouble(), fi.getPixelSizeX(), 0.0);
			assertEquals(CellSize.m100.toDouble(), Math.abs(fi.getPixelSizeY()), 0.0);	
			
			if(sourceImage.getNoData() != null) {
				// Check if nodata is set correct in source image 
				assertEquals("-999.0", sourceImage.getNoData());	
				// Check if the output dataset has the correct nodata value.
				assertEquals("-999.0", fi.getNoData());				
			}
			// Cleaning up required for each task
			taskPipeline.finalise();
		}
		
		
		// Check if finalise method has been successful in deleting the source image in upload or temporary storage 
		for(GraphicsFile sourceImage: source) {
			assertFalse(sourceImage.exists());			
		}

		// Check if finalise method has been successful in deleting the target image
		assertFalse(target.exists());
		
		// Remove test data in temporary upload location
		try {
			FileUtils.removeDirectory(tempDir);
		} catch (IOException e) {
			log.error("Could not delete temporary directory. {}", e);	
		}
	}	
}
