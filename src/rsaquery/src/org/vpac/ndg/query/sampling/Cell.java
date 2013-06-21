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

package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.Element;

/**
 * Stores a single pixel value. May be scalar or vector.
 *
 * <p><img src="doc-files/PixelSource_and_Cell_class.png" /></p>
 *
 * @see PixelSource
 * @author Alex Fraser
 */
public interface Cell extends HasPrototype {
	/**
	 * @return The value of the cell in the dataset.
	 */
	Element<?> get();

	/**
	 * Write into the dataset.
	 *
	 * @param value The value to write.
	 * @throws ClassCastException If the value is of the wrong type (scalar vs.
	 *         vector).
	 */
	void set(Element<?> value) throws ClassCastException;

	/**
	 * Set the cell to be blank, i.e. <em>NODATA</em>.
	 */
	void unset();

}
