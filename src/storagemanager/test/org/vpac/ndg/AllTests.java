/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

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
