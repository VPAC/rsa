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

package org.vpac.ndg.storage;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.hibernate.QueryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
@Transactional
public class DatasetDaoTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(DatasetDaoTest.class);

	private final String testDatasetName = "TestDataset";
	private final CellSize testDatasetResolution = CellSize.m500;

	@Autowired
	DatasetDao datasetDao;

	@Rollback(false)
	@Before
	public void setUp() {
		getSampleDataset();
	}

	@Test
	public void testGetAll() {
		List<Dataset> list = datasetDao.getAll();
		assertNotSame(list.size(), 0);
	}

	@Test
	public void searchDataset() {
		String name = null;
		int page = 0;
		int pageSize = 50;
		List<Dataset> list;
		list = datasetDao.search(name, page, pageSize);
		assertNotSame(list.size(), 0);
		name = testDatasetName;
		list = datasetDao.search(name, page, pageSize);
		assertNotSame(list.size(), 0);
	}

	@Test
	public void testFindById() {
		List<Dataset> list = datasetDao.getAll();
		assertNotSame(list.size(), 0);
		Dataset firstOne = list.get(0);
		Dataset ds = datasetDao.retrieve(firstOne.getId());
		assertEquals(ds.getName(), firstOne.getName());
	}

	@Test
	public void testFindByNameAndResolution() {
		Dataset firstOne = getSampleDataset();
		Dataset ds = datasetDao.findDatasetByName(firstOne.getName(),
				firstOne.getResolution());
		assertEquals(ds.getName(), firstOne.getName());
		assertEquals(ds.getResolution(), firstOne.getResolution());
		assertEquals(ds.getAbst(), firstOne.getAbst());
	}

	@Test
	public void testFindByNameNoResult() {
		Dataset ds = datasetDao
				.findDatasetByName("weirdName", CellSize.unknown);
		assertNull(ds);
	}

	@Test
	public void testDatasetCreation() {
		Dataset ds = getSampleDataset();
		assertEquals(testDatasetName, ds.getName());
	}

	@Test
	public void testGetTimeSlice() {
		Dataset ds = getSampleDataset();
		datasetDao.addTimeSlice(ds.getId(), new TimeSlice(new Date()));
		List<TimeSlice> tsList = datasetDao.getTimeSlices(ds.getId());
		assertNotSame(tsList.size(), 0);
	}

	@Test
	public void testCreationTimeSlice() {
		Dataset ds = getSampleDataset();
		TimeSlice ts = new TimeSlice(new Date());
		ts.setDataAbstract("dataAbstract");
		List<TimeSlice> tsList1 = datasetDao.getTimeSlices(ds.getId());
		int size1 = tsList1.size();
		datasetDao.addTimeSlice(ds.getId(), ts);
		List<TimeSlice> tsList2 = datasetDao.getTimeSlices(ds.getId());
		int size2 = tsList2.size();
		assertEquals(size1 + 1, size2);
	}

	@Test
	public void testFindTimeSliceBySearchDate() {
		Dataset ds = getSampleDataset();
		Date startDate = null;
		Date endDate = null;
		datasetDao.addTimeSlice(ds.getId(), new TimeSlice(new Date()));
		log.debug("ds: {}", ds.getId());
		List<TimeSlice> result;

		result = datasetDao.findTimeSlices(ds.getId(), startDate, endDate);
		log.debug("Result1: {}", result.size());
		assertNotSame(result.size(), 0);

		startDate = new Date();
		result = datasetDao.findTimeSlices(ds.getId(), startDate, endDate);
		log.debug("Result2: {}", result.size());
		assertSame(result.size(), 0);

		startDate = null;
		endDate = new Date();
		log.debug("Result3: {}", result.size());
		result = datasetDao.findTimeSlices(ds.getId(), startDate, endDate);
		assertNotSame(result.size(), 0);

		startDate = new Date();
		endDate = new Date();
		log.debug("Result4: {}", result.size());
		result = datasetDao.findTimeSlices(ds.getId(), startDate, endDate);
		assertSame(result.size(), 0);
	}

	@Test(expected = QueryException.class)
	public void testFindBandsByBandIdsCheckNull() {
		Dataset ds = getSampleDataset();
		log.debug("ds: {}", ds.getId());
		List<String> bandIds = null;
		List<Band> result;
		result = datasetDao.findBands(ds.getId(), bandIds);
		log.debug("Result1: {}", result.size());
		assertSame(result.size(), 0);
	}

	@Test
	public void testCreationBand() {
		Dataset dataset = getSampleDataset();
		Band band = new Band();
		band.setName("B10");
		band.setMetadata(false);
		band.setContinuous(false);
		int expectedBandCount = datasetDao.getBands(dataset.getId()).size();
		boolean alreadyExist = false;
		for (Band b : datasetDao.getBands(dataset.getId())) {
			if (b.getName().equalsIgnoreCase(band.getName())) {
				alreadyExist = true;
				break;
			}
		}
		if (!alreadyExist) {
			datasetDao.addBand(dataset.getId(), band);
			expectedBandCount++;
		}

		band = new Band();
		band.setName("B20");
		band.setMetadata(false);
		band.setContinuous(false);
		alreadyExist = false;
		for (Band b : datasetDao.getBands(dataset.getId())) {
			if (b.getName().equalsIgnoreCase(band.getName())) {
				alreadyExist = true;
				break;
			}
		}
		if (!alreadyExist) {
			datasetDao.addBand(dataset.getId(), band);
			expectedBandCount++;
		}
		assertEquals(expectedBandCount, datasetDao.getBands(dataset.getId())
				.size());
	}

	@Test
	public void testCreationBandWithNullDatasetId() {
		Band band = new Band();
		band.setName("B10");
		band.setMetadata(false);
		band.setContinuous(false);
		try {
			datasetDao.addBand(null, band);
			assertFalse(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCreationBandWithEmptyDatasetId() {
		Band band = new Band();
		band.setName("B10");
		band.setMetadata(false);
		band.setContinuous(false);
		try {
			datasetDao.addBand("", band);
			assertFalse(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCreationBandWithDatasetIdNotExist() {
		Band band = new Band();
		band.setName("B10");
		band.setMetadata(false);
		band.setContinuous(false);
		try {
			datasetDao.addBand("DummyDatasetId", band);
			assertFalse(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCreationBandWithNullBand() {
		Band band = null;
		try {
			datasetDao.addBand("DummyDatasetId", band);
			assertFalse(false);
		} catch (NullPointerException e) {
			assertTrue(true);
		}
	}

	private Dataset getSampleDataset() {
		Dataset dataset = datasetDao.findDatasetByName(testDatasetName,
				testDatasetResolution);
		if (dataset == null) {
			long precision = Utils.parseTemporalPrecision("1 hour");
			dataset = new Dataset(testDatasetName, testDatasetResolution,
					precision);
			datasetDao.create(dataset);
		}
		return dataset;
	}
}
