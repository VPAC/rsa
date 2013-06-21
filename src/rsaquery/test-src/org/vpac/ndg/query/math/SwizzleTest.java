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