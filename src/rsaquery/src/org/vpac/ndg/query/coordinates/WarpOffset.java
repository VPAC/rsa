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

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A translation: output = input + constant. Use this to align datasets that
 * share a coordinate system.
 *
 * @author Alex Fraser
 */
public class WarpOffset implements Warp {

	VectorReal offset;

	public WarpOffset(VectorReal offsetIn, VectorReal offsetOut, VectorReal scale) {
		offset = offsetIn.subNew(offsetOut);
		offset.div(scale);
	}

	@Override
	public void warp(VectorReal co) {
		co.add(offset);
	}

	@Override
	public void warp(BoxReal box) {
		// No need to warp all four corners: offset remains axis-aligned.
		warp(box.getMin());
		warp(box.getMax());
	}

	@Override
	public String toString() {
		return String.format("Offset(%s)", offset);
	}
}
