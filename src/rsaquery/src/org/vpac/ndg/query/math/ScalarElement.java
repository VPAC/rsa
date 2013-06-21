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

// THIS IS GENERATED CODE. Do not modify this file. See elementgen.py.

package org.vpac.ndg.query.math;

/**
 * A scalar numeric value. Scalar elements have the special property that they
 * are comparable.
 * @author Alex Fraser
 */
public interface ScalarElement extends Element<ScalarElement>, Comparable<ScalarElement> {

	/**
	 * The same as {@link Comparable#compareTo(Object)}, but specialised for
	 * integer data.
	 */
	int compareTo(long other);
	/**
	 * The same as {@link Comparable#compareTo(Object)}, but specialised for
	 * real data.
	 */
	int compareTo(double other);

	// CASTING

	/**
	 * @return The value of this element, boxed in its native Number subclass.
	 */
	Number getValue();
	/**
	 * @return The value of this element, cast to byte.
	 */
	byte byteValue();
	/**
	 * @return The value of this element, cast to short.
	 */
	short shortValue();
	/**
	 * @return The value of this element, cast to int.
	 */
	int intValue();
	/**
	 * @return The value of this element, cast to long.
	 */
	long longValue();
	/**
	 * @return The value of this element, cast to float.
	 */
	float floatValue();
	/**
	 * @return The value of this element, cast to double.
	 */
	double doubleValue();

}
