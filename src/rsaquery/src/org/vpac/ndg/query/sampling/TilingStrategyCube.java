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
 * Splits an image into cubes. If any dimension of the cube is larger than the
 * corresponding dimension in the image, it will be truncated (i.e. volume is
 * not guaranteed).
 * @author Alex Fraser
 */
public class TilingStrategyCube extends TilingStrategyBase {

	private int edgeLen;

	/**
	 * Create a new tiling strategy that tries to make cubic tiles.
	 * @param edgeLen The maximum length of any tile dimension.
	 */
	public TilingStrategyCube(int edgeLen) {
		this.edgeLen = edgeLen;
	}

	@Override
	public VectorInt getTileShape(VectorInt baseShape) {
		VectorInt tileShape = VectorInt.createEmpty(baseShape.size());
		for (int i = 0; i < tileShape.size(); i++) {
			if (baseShape.get(i) < edgeLen)
				tileShape.set(i, baseShape.get(i));
			else
				tileShape.set(i, edgeLen);
		}
		return tileShape;
	}

}
