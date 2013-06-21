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

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.TestUtil;
import org.vpac.ndg.Utils;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Point;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.TimeSliceUtil;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
public class ExporterTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(ExporterTest.class);

	@Rule
	public MethodRule benchmarkRun = new BenchmarkRule();

	@Autowired
	TestUtil testUtil;
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TimeSliceUtil timeSliceUtil;
	@Autowired
	TileManager tileManager;

	@Before
	public void setUp() throws Exception {
	}

	// Exports without any extents set; this should return the natural extents
	// of the dataset.
	@Test
	public void testExport() throws Exception {
		testUtil.initialiseDataForExport();
		Date st = Utils.now();
		Dataset dataset = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());
		
		Exporter exporter = new Exporter();
		exporter.setDatasetId(dataset.getId());
		exporter.configure();
		exporter.call();

		Date et = Utils.now();
		log.debug("EXPORT TIME = {}ms", et.getTime() - st.getTime());
		
		Path outputPath = Exporter.findOutputPath(exporter.getTaskId());
		assertTrue(Files.exists(outputPath));
		assertTrue(Files.isRegularFile(outputPath));
	}

	@Test
	public void testExportDataCube() throws Exception {
		testUtil.initialiseDataForExport();
		Dataset dataset = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());

		Box extents = new Box(
				new Point<Double>(810000.00, -3625000.00),
				new Point<Double>(935000.00, -3500000.00));

		Exporter exporter = new Exporter();
		exporter.setDatasetId(dataset.getId());
		exporter.setExtents(extents);
		exporter.configure();
		exporter.call();

		Path outputPath = Exporter.findOutputPath(exporter.getTaskId());
		assertTrue(Files.exists(outputPath));
		assertTrue(Files.isRegularFile(outputPath));
	}

	@Test
	public void testExportWithMissingTiles() throws Exception {
		String dsName = "missing";
		Dataset ds = testUtil.initialiseDataForExport(dsName, testUtil.getExportDatasetRes(), testUtil.getExportDatasetPath());
		// Make sure one tile is absent.
		Box extents = testUtil.removeOneTile(ds);

		// // Set time range
		// Date start = Utils.parseDate("2010-12-13T00-00-00.000");
		// Date end = Utils.parseDate("2010-12-13T00-00-00.000");
		// log.info("Temporal extents: {} / {}", start, end);

		List<String> bandNamesFilter = new ArrayList<>();
		bandNamesFilter.add("B30");

		Exporter exporter = new Exporter();
		exporter.setDatasetId(ds.getId());
		exporter.setExtents(extents);
		// exporter.setStart(start);
		// exporter.setEnd(end);
		exporter.setBandNamesFilter(bandNamesFilter);

		exporter.configure();
		exporter.call();

		Path outputPath = Exporter.findOutputPath(exporter.getTaskId());
		assertTrue(Files.exists(outputPath));
		assertTrue(Files.isRegularFile(outputPath));
	}

	@Test(expected = TaskException.class)
	public void testExportWithNoTiles() throws Exception {
		String dsName = "missing";
		Dataset ds = testUtil.initialiseDataForExport(dsName, testUtil.getExportDatasetRes(), testUtil.getExportDatasetPath());

		// Create a new box that is just outside of the dataset, so that no
		// tiles can be found during export. This will force a TaskException.
		List<TimeSlice> tss = datasetDao.getTimeSlices(ds.getId());
		Box dsExtents = timeSliceUtil.aggregateBounds(tss);
		Box tileExtents = tileManager.getNngGrid().alignToTileGrid(dsExtents, testUtil.getExportDatasetRes());
		Box extents = new Box(
				Point.sub(tileExtents.getMin(), 100.0 * testUtil.getExportDatasetRes().toDouble()),
				tileExtents.getMin());
		log.info("Dataset extents: {}", dsExtents);
		log.info("Tile extents: {}", tileExtents);
		log.info("Shifted extents: {}", extents);

		List<String> bandNamesFilter = new ArrayList<>();
		bandNamesFilter.add("B30");

		Exporter exporter = new Exporter();
		exporter.setDatasetId(ds.getId());
		exporter.setExtents(extents);
		exporter.setBandNamesFilter(bandNamesFilter);

		exporter.configure();
		exporter.call();
	}

	@Test
	public void testExportDataCubeWithTargetProjection() throws Exception {
		Dataset ds = testUtil.initialiseDataForExport();

		Box extents = new Box(
				new Point<Double>(810000.00, -3625000.00),
				new Point<Double>(935000.00, -3500000.00));

		Exporter exporter = new Exporter();
		exporter.setDatasetId(ds.getId());
		exporter.setExtents(extents);
		exporter.setTargetProjection("EPSG:3112");
		exporter.configure();
		exporter.call();

		Path outputPath = Exporter.findOutputPath(exporter.getTaskId());
		assertTrue(Files.exists(outputPath));
		assertTrue(Files.isRegularFile(outputPath));
	}
}
