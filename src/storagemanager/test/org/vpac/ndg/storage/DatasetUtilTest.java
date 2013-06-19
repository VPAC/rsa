package org.vpac.ndg.storage;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.util.DatasetUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class DatasetUtilTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	DatasetDao datasetDao;  
	@Autowired
	DatasetUtil dsUtil;  

	@Test
	public void testGetLocation() {
		String locationCheckingString = "locationChecking";
		Dataset ds = new Dataset();
		ds.setName(locationCheckingString);
		datasetDao.create(ds);
		assertTrue(dsUtil.getPath(ds).toString().contains(locationCheckingString));
	}
}
