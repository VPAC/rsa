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

package org.vpac.ndg.datamodel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.datamodel.AggregationDefinition.VarAggDef;
import org.vpac.ndg.datamodel.AggregationDefinition.VarDef;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class AggregationTest {

	final private Logger log = LoggerFactory.getLogger(AggregationTest.class);

	private Path tempDir;
	private AggregationFactory factory;

	@Before
	public void setUp() throws IOException {
		tempDir = FileUtils.createTmpLocation();
		factory = new AggregationFactory();
	}

	@Test
	public void testUnion() throws IOException {
		List<AggregationDefinition> children = new ArrayList<>();

		Path datasetPath;
		AggregationDefinition child;
		datasetPath = Paths
				.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");

		child = factory.raw(datasetPath);
		child.getVariables().add(VarDef.remap("Band1", "Band10"));
		children.add(child);
		child = factory.raw(datasetPath);
		child.getVariables().add(VarDef.remap("Band1", "Band20"));
		children.add(child);

		AggregationDefinition agg = factory.union(children);

		Path output = tempDir.resolve("union.ncml");
		agg.serialise(output);
	}

	@Test
	public void testJoinNew() throws IOException {
		List<AggregationDefinition> children = new ArrayList<>();

		Path datasetPath;
		AggregationDefinition child;
		datasetPath = Paths
				.get("../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		child = factory.raw(datasetPath);
		child.setCoordValue("50");
		children.add(child);
		child = factory.raw(datasetPath);
		child.setCoordValue("100");
		children.add(child);

		List<VarAggDef> aggVars = new ArrayList<>();
		aggVars.add(new VarAggDef("Band1"));

		VarDef vd = VarDef.newDimension("time", "int", "Days since 2011-01-01");

		AggregationDefinition agg = factory.joinNew(aggVars, vd, children);

		Path output = tempDir.resolve("joinNew.ncml");
		agg.serialise(output);
	}

	@After
	public void tearDown() {
		try {
			FileUtils.removeDirectory(tempDir);
		} catch (IOException e) {
			log.error("Could not delete temporary directory.", e);
		}
	}

}
