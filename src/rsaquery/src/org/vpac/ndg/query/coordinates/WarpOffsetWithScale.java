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

package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A translation and scale. Use this to align datasets that share a coordinate
 * system, but which may not share the same grid.
 * 
 * <p>
 * <em>f(x) = (s<sub>a</sub>x + o<sub>a</sub> - o<sub>b</sub>) / s<sub>b</sub></em>
 * </p>
 * 
 * <p>
 * Where <em>s<sub>a</sub></em> and <em>s<sub>b</sub></em> are the cell sizes
 * (scale) of the input and output datasets, and <em>o<sub>a</sub></em> and
 * <em>o<sub>b</sub></em> are the offsets of the two grids from the origin of
 * the coordinate system.
 * </p>
 * 
 * @author Alex Fraser
 */
public class WarpOffsetWithScale implements Warp {

	VectorReal offset;
	VectorReal scaleIn;
	VectorReal scaleOut;

	public WarpOffsetWithScale(VectorReal offsetIn, VectorReal offsetOut,
			VectorReal scaleIn, VectorReal scaleOut) {

		int size = offsetIn.size();
		if (offsetOut.size() != size || scaleIn.size() != size
				|| scaleOut.size() != size) {
			throw new IllegalArgumentException(
					"Warp specified with incompatible vector dimensionality.");
		}

		offset = offsetIn.subNew(offsetOut);
		this.scaleIn = scaleIn.copy();
		// Invert output scale so multiply can be used during actual warp.
		this.scaleOut = VectorReal.createEmpty(scaleOut.size(), 1.0);
		this.scaleOut.div(scaleOut);
	}

	@Override
	public void warp(VectorReal co) {
		try {
			co.mul(scaleIn);
			co.add(offset);
			co.mul(scaleOut);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(
					"Warp with incompatible vector dimensionality.");
		}
	}

	@Override
	public void warp(BoxReal box) {
		// No need to warp all four corners: offset remains axis-aligned.
		warp(box.getMin());
		warp(box.getMax());
	}

	@Override
	public String toString() {
		return String.format("Offset((%sx + %s) * %s)", scaleIn, offset,
				scaleOut);
	}
}
