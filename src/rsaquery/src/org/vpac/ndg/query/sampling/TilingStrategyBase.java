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

public abstract class TilingStrategyBase implements TilingStrategy {

	@Override
	public VectorInt getGridShape(VectorInt baseShape) {

		VectorInt tileShape = getTileShape(baseShape);

		VectorInt tileGridShape = baseShape.divNew(tileShape);
		VectorInt tileGridRemainder = baseShape.modNew(tileShape);
 		for (int i = 0; i < tileShape.size(); i++) {
			if (tileGridRemainder.get(i) > 0)
				tileGridShape.set(i, tileGridShape.get(i) + 1);
 		}
 
 		return tileGridShape;
	}

}
