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

public class WarpTimeLut implements Warp {

	double pre;
	double post;
	double[] lut;

	public WarpTimeLut(double[] lut) {
		this.lut = lut;
		pre = -1;
		post = lut[lut.length - 1] + 1.0;
	}

	@Override
	public void warp(VectorReal co) {
		int i = (int)co.getT();
		if (i < 0) {
			co.setT(pre);
			return;
		} else if (i >= lut.length) {
			co.setT(post);
			return;
		}
		co.setT(lut[i]);
	}

	@Override
	public void warp(BoxReal box) {
		// No need to warp all four corners: assume time is uniform across space
		warp(box.getMin());
		warp(box.getMax());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TimeLUT(");
		boolean first = true;
		for (double value : lut) {
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(value);
		}
		sb.append(")");
		return sb.toString();
	}
}
