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

package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.VectorInt;

/**
 * Splits an image into long chunks, based on the natural ordering of NetCDF.
 * @author Alex Fraser
 */
public class TilingStrategyStride  extends TilingStrategyBase {

	private long volume;

	/**
	 * Create a new tiling strategy that splits the image into long chunks.
	 *
	 * @param minVolume The minimum number of pixels to have in a chunk. The
	 *        actual volume may differ, because the tile must be rectangular
	 *        (not ragged).
	 */
	public TilingStrategyStride(long minVolume) {
		this.volume = minVolume;
	}

	@Override
	public VectorInt getTileShape(VectorInt baseShape) {
		VectorInt tileShape = VectorInt.createEmpty(baseShape.size());

		// NOTE that the ordering is reversed, so that the X axis is longest,
		// followed by Y, etc.
		long remainder = volume;
		for (int i = tileShape.size() - 1; i >= 0; i--) {
			long current;
			if (baseShape.get(i) < remainder)
				current = baseShape.get(i);
			else
				current = remainder;
			tileShape.set(i, current);
			remainder = (remainder / baseShape.get(i)) + 1;
		}

		return tileShape;
	}

}
