package org.vpac.ndg.query.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class ElementTest {

	@Test
	public void test_ternary_coercion() throws Exception {
		ElementByte opA = new ElementByte((byte) 1);
		ElementByte opB = new ElementByte((byte) 2);
		ElementFloat result = new ElementFloat();
		result.divOf(opA, opB);
		assertEquals(0.5, result.floatValue(), 0.01);
	}

}
