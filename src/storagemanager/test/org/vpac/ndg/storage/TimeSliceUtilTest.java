package org.vpac.ndg.storage;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.TimeSliceUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class TimeSliceUtilTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	DatasetDao datasetDao;  
	@Autowired
	TimeSliceUtil timeSliceUtil;  

	
	@Test
	public void testGetLocation() {
		String testTimeSliceUtilString = "testTimeSliceUtil";
		Dataset ds = new Dataset();
		ds.setName(testTimeSliceUtilString);
		datasetDao.create(ds);
		TimeSlice ts = new TimeSlice(new Date());
		datasetDao.addTimeSlice(ds.getId(), ts);
		assertTrue(timeSliceUtil.getFileLocation(ts).toString().contains(testTimeSliceUtilString));
	}
}
