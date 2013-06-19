package org.vpac.ndg.query.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class SwizzleTest {

	@Test
	public void test_int() throws Exception {
		Swizzle sw = SwizzleFactory.compile("zxyx");
		VectorInt from = VectorInt.create(1, 2, 3);
		VectorInt to = VectorInt.createEmpty(4);
		VectorInt expected;

		sw.swizzle(from, to);
		expected = VectorInt.create(1, 3, 2, 3);
		assertEquals(expected, to);
	}

	@Test
	public void test_real() throws Exception {
		Swizzle sw = SwizzleFactory.compile("zxyx");
		VectorReal from = VectorReal.create(1, 2, 3);
		VectorReal to = VectorReal.createEmpty(4);
		VectorReal expected;

		sw.swizzle(from, to);
		expected = VectorReal.create(1, 3, 2, 3);
		assertEquals(expected, to);
	}

	@Test
	public void test_element() throws Exception {
		Swizzle sw = SwizzleFactory.compile("zxyx");
		VectorElement from = new VectorElement(
				new ElementFloat(1.5f),
				new ElementByte((byte)2),
				new ElementLong(10000000000000l)
				);
		// Note: type conversion to long
		VectorElement to = VectorElement.createInt(4, 0);

		VectorElement expected = new VectorElement(
				new ElementLong(1),
				new ElementLong(10000000000000l),
				new ElementLong(2),
				new ElementLong(10000000000000l)
				);

		sw.swizzle(from, to);
		assertEquals(expected, to);
	}

}