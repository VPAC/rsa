package org.vpac.ndg.configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.ApplicationContextProvider;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class NdgConfigManagerTest extends AbstractJUnit4SpringContextTests {

	final private Logger log = LoggerFactory.getLogger(NdgConfigManagerTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		NdgConfigManager ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		log.debug(ndgConfigManager.getConfig().getDefaultPickupLocation());
	}
}
