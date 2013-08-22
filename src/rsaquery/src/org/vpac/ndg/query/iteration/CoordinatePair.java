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

package org.vpac.ndg.query.iteration;

import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Combines an image index with more abstract coordinates.
 * @author Alex Fraser
 */
public class CoordinatePair {
	public VectorInt imageIndex;
	public VectorReal coordinates;

	public CoordinatePair(int ndimensions) {
		imageIndex = VectorInt.createEmpty(ndimensions);
		coordinates = VectorReal.createEmpty(ndimensions);
	}

	public CoordinatePair copy() {
		CoordinatePair other = new CoordinatePair(imageIndex.size());
		other.coordinates.set(coordinates);
		other.imageIndex.set(imageIndex);
		return other;
	}
}
