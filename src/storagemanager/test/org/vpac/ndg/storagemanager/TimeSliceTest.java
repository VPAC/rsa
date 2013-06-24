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

package org.vpac.ndg.storagemanager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class TimeSliceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	DatasetDao datasetDao;

	@Autowired
	TimeSliceDao timeSliceDao;

	@Test
	public void testSetResolution() {
		long precision = Utils.parseTemporalPrecision("1 hour");
		Dataset dataset = new Dataset("JUnitTestDataset",
				"Test data for JUNIT", CellSize.km1, precision);

		assertNotNull(dataset);
		datasetDao.create(dataset);
		String datasetAcquisitionTime = "2012-04-23 20:00";
		Date acquisitionDate = Utils.parseDate(datasetAcquisitionTime);
		TimeSlice timeSlice = datasetDao.addTimeSlice(dataset.getId(),
				new TimeSlice(acquisitionDate));

		assertTrue(timeSliceDao.getParentDataset(timeSlice.getId())
				.getResolution() == CellSize.km1);
		assertTrue(Utils
				.isSameDatetime(acquisitionDate, timeSlice.getCreated()));

		datasetDao.delete(dataset);
	}
}
