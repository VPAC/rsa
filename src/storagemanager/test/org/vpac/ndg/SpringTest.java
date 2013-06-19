package org.vpac.ndg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.storage.dao.DatasetDao;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class SpringTest extends AbstractJUnit4SpringContextTests {

	public SpringTest() {
	}
	
	@Test
	public void testGetSameBeanFromContext() {
		DatasetDao datasetDao = (DatasetDao) ApplicationContextProvider.getApplicationContext().getBean("datasetDao");
		DatasetDao datasetDao2 = (DatasetDao) ApplicationContextProvider.getApplicationContext().getBean("datasetDao");
		
		assertEquals(datasetDao, datasetDao2);
	}
}
