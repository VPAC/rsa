package org.vpac.ndg;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;
import org.vpac.ndg.datamodel.AggregateViewTest;
import org.vpac.ndg.datamodel.AggregationTest;
import org.vpac.ndg.lock.TimeSliceLockTest;
import org.vpac.ndg.metadata.GaMetadataTest;
import org.vpac.ndg.storage.DatasetDaoTest;
import org.vpac.ndg.storagemanager.DatasetTest;
import org.vpac.ndg.storagemanager.TimeSliceTest;
import org.vpac.ndg.task.BatchImporterTest;
import org.vpac.ndg.task.CommitterTest;
import org.vpac.ndg.task.ExporterTest;
import org.vpac.ndg.task.GraphicsFileCreatorTest;
import org.vpac.ndg.task.ImporterTest;
import org.vpac.ndg.task.TileAggregatorTest;
import org.vpac.ndg.task.TileBandCreatorTest;
import org.vpac.ndg.task.TileTransformerTest;
import org.vpac.ndg.task.TransformerTest;
import org.vpac.ndg.task.VrtBuilderTest;

/**
 * This test suite should compile all JUnit test cases for all newly design
 * raster tasks.
 * 
 * @author hsumanto
 * 
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
@RunWith(Suite.class)
@SuiteClasses({
	SimpleDateFormatTest.class,
	DatasetTest.class,
	TimeSliceTest.class,
	UtilsTest.class,
	DatasetDaoTest.class,
	GaMetadataTest.class,
	CommitterTest.class,
	GraphicsFileCreatorTest.class,
	TileAggregatorTest.class,
	TileBandCreatorTest.class,
	TileTransformerTest.class,
	TransformerTest.class,
	VrtBuilderTest.class,
	ImporterTest.class,
	BatchImporterTest.class,
	ExporterTest.class,
	AggregationTest.class,
	AggregateViewTest.class,
	TimeSliceLockTest.class
})
public class AllTests {
}
