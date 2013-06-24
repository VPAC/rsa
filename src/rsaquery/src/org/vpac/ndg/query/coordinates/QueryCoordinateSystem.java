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

package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorInt;

public class QueryCoordinateSystem implements HasShape, HasGridProjected {

	private GridProjected grid;
	private TimeAxis timeAxis;
	private VectorInt shape;

	public QueryCoordinateSystem(GridProjected grid, TimeAxis timeAxis) {
		this.grid = grid;
		this.timeAxis = timeAxis;

		// Expand shape to include time dimension if appropriate.
		if (timeAxis == null) {
			shape = grid.getShape();
		} else {
			shape = VectorInt.createEmpty(grid.getRank() + 1);
			Swizzle resize = SwizzleFactory.resize(grid.getRank(),
					shape.size());
			resize.swizzle(grid.getShape(), shape);
			shape.setT(timeAxis.getValues().size());
		}
	}

	@Override
	public GridProjected getGrid() {
		return grid;
	}

	@Override
	public VectorInt getShape() {
		return shape;
	}

	@Override
	public int getRank() {
		return shape.size();
	}

	public TimeAxis getTimeAxis() {
		return timeAxis;
	}

}
