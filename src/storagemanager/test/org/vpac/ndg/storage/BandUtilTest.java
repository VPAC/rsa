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

package org.vpac.ndg.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.TestUtil;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.util.BandUtil;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class BandUtilTest {

	final Logger log = LoggerFactory.getLogger(BandUtilTest.class);

	@Autowired
	TestUtil testUtil;
	@Autowired
	BandUtil bandUtil;
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	BandDao bandDao;

	final String testDatasetName = "TestDataset";
	final CellSize testDatasetResolution = CellSize.m500;
	final String testBandName = "TestBandUtil";
	final String testBandNodata = "-123";

	private static final String SECTION = "0:9, 0:9";
	static final int[] BLANK_DATA = new int[] { -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, -123, -123, -123, -123,
			-123, -123, -123, -123, -123, -123, -123, };

	@Test
	public void createBlankTile() throws IOException, InvalidRangeException {
		Dataset ds = getSampleDataset();
		Band band = getSampleBand();
		Path path = bandUtil.getBlankTilePath(ds, band);

		log.info("Blank tile path is {}", path);
		Files.deleteIfExists(path);

		bandUtil.createBlankTile(ds, band);
		assertTrue(Files.exists(path));

		NetcdfFile dataset = NetcdfFile.open(path.toString());
		try {
			checkKnownSection(dataset);
		} finally {
			dataset.close();
		}
	}

	void checkKnownSection(NetcdfFile dataset) throws IOException,
			InvalidRangeException {
		// Read 100 values from the centre of the data, and check that it is as
		// expected.
		Variable var;
		Array arr;
		var = dataset.findVariable("Band1");
		assertNotNull("Couldn't find variable Band1", var);
		arr = var.read(SECTION);
		assertArray(BLANK_DATA, arr);
	}

	/**
	 * Confirm that the contents of two arrays are the same.
	 */
	void assertArray(int[] expected, Array actual) {
		assertEquals(expected.length, actual.getSize());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.getInt(i));
		}
	}

	Band getSampleBand() {
		Dataset ds = getSampleDataset();
		Band band = bandDao.find(ds.getId(), testBandName);
		if (band == null) {
			band = new Band();
			band.setName(testBandName);
			band.setNodata(testBandNodata);
			band.setType(RasterDetails.INT16);
			datasetDao.addBand(ds.getId(), band);
		}
		return band;
	}

	Dataset getSampleDataset() {
		Dataset dataset = datasetDao
				.findDatasetByName(testUtil.getExportDatasetName(),
						testUtil.getExportDatasetRes());
		if (dataset == null) {
			long precision = Utils.parseTemporalPrecision("1 hour");
			dataset = new Dataset(testDatasetName, testDatasetResolution,
					precision);
			datasetDao.create(dataset);
		}
		return dataset;
	}

}
