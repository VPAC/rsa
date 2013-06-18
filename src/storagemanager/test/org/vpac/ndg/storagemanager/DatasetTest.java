package org.vpac.ndg.storagemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class DatasetTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TimeSliceDao timeSliceDao;

	@Test
	public void testCreateTimeSlice() {

		Dataset dataset = null;
		long precision = Utils.parseTemporalPrecision("1 hour");
		dataset = new Dataset("JUnitTestDataset3", "Test data for JUNIT",
				CellSize.m500, precision);
		datasetDao.create(dataset);

		String datasetAcquisitionTime = "2012-04-23 20:00";
		Date acquisitionDate = Utils.parseDate(datasetAcquisitionTime);
		TimeSlice timeSlice = datasetDao.addTimeSlice(dataset.getId(),
				new TimeSlice(acquisitionDate));
		TimeSlice foundTimeSlice = timeSliceDao.retrieve(timeSlice.getId());
		assertNotNull(foundTimeSlice);
		assertEquals(foundTimeSlice.getId(), timeSlice.getId());
		datasetDao.delete(dataset);
	}
}
