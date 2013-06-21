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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.TestUtil;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.datamodel.CellSize;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * This unit test class is for testing batch import process.
 * 
 * @author hsumanto
 * 
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
public class BatchImporterTest extends AbstractJUnit4SpringContextTests {
	@Rule
	public MethodRule benchmarkRun = new BenchmarkRule();

	@Autowired
	TestUtil testUtil;

	/**
	 * Perform batch import on small scale timeseries dataset.
	 * 
	 * @throws Exception
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	public void testBatchImport() throws Exception {
		String datasetName = "small_landsat";
		Path datasetSrcDir = Paths.get("../../data/" + datasetName);

		boolean continuous = true;
		CellSize targetResolution = CellSize.m25;
		long precision = Utils.parseTemporalPrecision("1 day");
		String srcnodata = "-999";

		testUtil.batchImport(datasetName, targetResolution, precision,
				continuous, datasetSrcDir, srcnodata);
	}
}
