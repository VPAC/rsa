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

import java.nio.file.Path;
import java.nio.file.Paths;

import junit.framework.Assert;

import org.gdal.gdal.gdal;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.TestUtil;
import org.vpac.ndg.Utils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.Default;
import org.vpac.ndg.common.datamodel.CellSize;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * This unit test class is for testing the Importer class.
 * 
 * @author hsumanto
 *
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
public class ImporterTest extends AbstractJUnit4SpringContextTests {

	@Rule
	public MethodRule benchmarkRun = new BenchmarkRule();
	
	@Autowired
	TestUtil testUtil;
	
	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();
	}

	@After
	public void tearDown() throws Exception {
	}
		
	/**
	 * Test that the importer would catch error when required parameters not specified.
	 */
	@Test
	public void testImporter_Capturing_Parameter_Not_Specified_Error() throws Exception {
		String datasetName = Constant.EMPTY;		
		Path datasetPath = Paths.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		String datasetAcquisitionTime = Constant.EMPTY;
		String bandName = Default.BAND1;
		boolean continuous = true;
		CellSize targetResolution = CellSize.m100;
		long precision = Utils.parseTemporalPrecision("1 hour");
		// Note that this import is expected to fail!

		try {
			testUtil.runImport(datasetName, datasetAcquisitionTime, bandName,
					datasetPath, continuous, targetResolution, precision);
		} catch (IllegalArgumentException e) {
			// Expected behaviour.
			Assert.assertTrue(true);
		}
	}

	/**
	 * Test importing VictoriaDem sample dataset into 100m.
	 */
	@Test
	public void testImporter_Dem100m_Upload() throws Exception {
		String datasetName = "Landsat100m";		
		Path datasetPath = Paths.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		String datasetAcquisitionTime = "2012-04-23 20:00";
		String bandName = Default.BAND1;
		boolean continuous = true;
		CellSize targetResolution = CellSize.m100;
		long precision = Utils.parseTemporalPrecision("1 hour");
		testUtil.runImport(datasetName, datasetAcquisitionTime, bandName,
				datasetPath, continuous, targetResolution, precision);
	}

	/**
	 * Test importing VictoriaDem sample dataset into 500m.
	 */
	@Test
	public void testImporter_Dem500m_Upload() throws Exception {
		String datasetName = "Landsat500m";		
		Path datasetPath = Paths.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		String datasetAcquisitionTime = "2012-04-23 20:00";
		String bandName = Default.BAND1;
		boolean continuous = true;
		CellSize targetResolution = CellSize.m500;
		long precision = Utils.parseTemporalPrecision("1 hour");
		testUtil.runImport(datasetName, datasetAcquisitionTime, bandName,
				datasetPath, continuous, targetResolution, precision);
	}	
	
	/** 
	 * Test importing GA LANDSAT dataset.
	 */
	@Test
	public void testImporter_GA_LANDSAT_Upload() throws Exception {	
		String datasetName = "Landsat25m";		
		Path datasetPath = Paths.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		String datasetAcquisitionTime = FileUtils.getAcquisitionTimeStr(datasetPath);
		String bandName = FileUtils.getBandNameStr(datasetPath);
		boolean continuous = true;
		CellSize targetResolution = CellSize.m25;
		long precision = Utils.parseTemporalPrecision("1 day");
		long[] benchmark = { 0,0,0 };
		String srcnodata = "-999";

		testUtil.runImportWithBenchmark(datasetName, datasetAcquisitionTime, bandName,
					datasetPath, continuous, targetResolution,
					precision, benchmark, srcnodata);
	}
}
