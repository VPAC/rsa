package org.vpac.ndg.storage;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.UploadDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.Upload;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class UploadTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	UploadDao uploadDao;  
	@Autowired
	DatasetDao datasetDao;  
	
	@Test
	public void testUpload() {
		String timeSliceId = getFirstDatasetTimeSlice();
		uploadDao.create(new Upload(timeSliceId));
	}

	private String getFirstDatasetTimeSlice() {
		List<Dataset> dslist = datasetDao.getAll();
		List<TimeSlice> tsList = datasetDao.getTimeSlices(dslist.get(0).getId());
		return tsList.get(0).getId();
	}
}
