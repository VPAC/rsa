package org.vpac.ndg.task;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.gdal.gdal.Driver;
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
import org.vpac.ndg.rasterservices.FileInformation;

/**
 * Test to make sure GetFileInformation can open various dataset.
 * 
 * @author hsumanto
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class GetFileInformationTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory
			.getLogger(GetFileInformationTest.class);

	private FileInformation fi;

	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();

		log.debug("Version: {}", gdal.VersionInfo());
		log.debug("Driver count: {}", gdal.GetDriverCount());
		Driver driver = gdal.GetDriverByName("netCDF");
		log.debug("Is netCDF Driver Available: {}", (driver != null));

		// get initial file and find out what it is
		fi = new FileInformation();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test to get information from netcdf file.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetInfoNc() {
		// Find out what it is
		fi.setInputDataset(Paths
				.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc"));

		try {
			fi.runInline();
		} catch (Exception e) {
			log.error("Error: {}", e.getMessage());
		}

		// Check result
		assertEquals(2700, fi.getSizeX());
		assertEquals(1000, fi.getSizeY());
		assertEquals(true, fi.isProjected());
		assertEquals(3577, fi.getEpsgId());

		// Check if exit successfully
		assertEquals(0, fi.getExitValue());
	}
}
