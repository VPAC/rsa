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

package org.vpac.ndg.query;

import java.io.File;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.vpac.ndg.query.QueryDefinition.LiteralDefinition;
import org.vpac.ndg.query.QueryDefinition.SamplerDefinition;
import org.vpac.ndg.query.QueryDefinition.VariableDefinition;

import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.DuplicateFieldException;

/**
 * Test for reading and writing a QueryDefiniton XML file.
 * @author Alex Fraser
 * @see QueryDefinition
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class QueryDefinitionTest extends TestCase {

	private QueryDefinition qd;

	@Before
	public void setUp() throws FileNotFoundException {
		File configFile = new File("data/config/wettingextents.xml");
		qd = QueryDefinition.fromXML(configFile);
	}

	@Test
	public void testDataset() {
		assertNotNull(qd.inputs);
		assertEquals(qd.inputs.size(), 1);
		assertEquals(qd.inputs.get(0).href, "../input/abstract.nc");
	}

	@Test
	public void testFilter() {
		assertNotNull(qd.filters);
		assertEquals(qd.filters.size(), 1);
		assertEquals(qd.filters.get(0).classname, "org.vpac.ndg.query.WettingExtents");
	}

	@Test
	public void testLiteral() {
		LiteralDefinition u = qd.filters.get(0).literals.get(0);
		assertEquals(u.name, "dryThreshold");
		assertEquals(u.value, "64");
	}

	@Test
	public void testSampler() {
		SamplerDefinition u = qd.filters.get(0).samplers.get(0);
		assertEquals(u.name, "input");
		assertEquals(u.ref, "#infile/Band1");
	}

	@Test
	public void testVariable() {
		VariableDefinition v = qd.output.variables.get(0);
		assertEquals(v.name, "wet");
		assertEquals(v.ref, "#Wet/output");
	}

	/**
     * Tests that the parser rejects a duplicate <em>output</em> element.
     */
	@Test(expected=DuplicateFieldException.class)
	public void testInvalid() throws FileNotFoundException {
		// Note: doesn't use the file opened in setUp.
		File configFile = new File("data/config/invalid_output.xml");
		qd = QueryDefinition.fromXML(configFile);
	}

}
