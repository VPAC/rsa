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
