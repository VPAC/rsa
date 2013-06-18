package org.vpac.ndg.task;

import java.io.File;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

/**
 * Test if netcdf could be converted into other format.
 * @author hsumanto
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class ConverterTest extends AbstractJUnit4SpringContextTests {

	@After
	public void tearDown() {
		FileUtils.deleteIfExists(Paths.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.png"));
		FileUtils.deleteIfExists(Paths.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.png.aux.xml"));
	}

	@Test
	public void test() throws TaskInitialisationException, TaskException {
		File srcFile = new File(
				"../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		File dstFile = new File(
				"../../data/small_landsat/LS7_ETM_095_082_20100116_B30.png");
		ImageTranslator converter = new ImageTranslator();
		// mandatory
		converter.setFormat(GdalFormat.PNG);
		converter.setLayerIndex(1);
		converter.setSrcFile(srcFile.toPath());
		converter.setDstFile(dstFile.toPath());
		converter.initialise();
		converter.execute();
	}
}
