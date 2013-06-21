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
 * Performs two warps, one after the other.
 * @author Alex Fraser
 */
public class WarpDual implements Warp {

	Warp warpA;
	Warp warpB;

	public WarpDual(Warp warpA, Warp warpB) {
		this.warpA = warpA;
		this.warpB = warpB;
	}

	@Override
	public void warp(VectorReal co) {
		warpA.warp(co);
		warpB.warp(co);
	}

	@Override
	public void warp(BoxReal box) {
		warpA.warp(box);
		warpB.warp(box);
	}

	@Override
	public String toString() {
		return String.format("WarpDual(%s, %s)", warpA, warpB);
	}
}
