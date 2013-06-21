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

import org.vpac.ndg.query.FilterAdapter;
import org.vpac.ndg.query.math.BoxReal;

public class PixelSourceFactory {
	public PixelSource create(FilterAdapter filter, Cell cell, BoxReal bounds) {

		if (CellScalar.class.isAssignableFrom(cell.getClass())) {
			// Scalar
			FilteredPixelScalar fp = new FilteredPixelScalar(filter,
					(CellScalar)cell, bounds);
			return fp;

		}else if (CellVector.class.isAssignableFrom(cell.getClass())) {
			// Vector
			FilteredPixelVector fp = new FilteredPixelVector(filter,
					(CellVector)cell, bounds);
			return fp;

		} else {
			throw new UnsupportedOperationException(String.format(
					"Can't create PixelSource wrapper for type %s",
					cell.getClass().getName()));
		}
	}
}
