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

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.coordinates.HasShape;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Reinterprets a 1D array as an nD image.
 * @author Alex Fraser
 */
public class Image<T> implements HasShape {

	VectorInt shape;
	T[] image;

	public Image(T[] image, VectorInt shape) throws QueryConfigurationException {
		this.shape = shape;
		this.image = image;
	}

	public T getPixel(int i) {
		return image[i];
	}

	/**
	 * @param coordinates The coordinates to sample from.
	 * @return The value at the specified coordinates.
	 */
	public T getPixel(VectorInt coordinates) {
		int idx = (int) coordinates.toPixelIndex(shape);
		return image[idx];
	}

	/**
	 * Get the value at the specified coordinates. The coordinates will be
	 * floored to the nearest whole pixel; no interpolation is performed.
	 * 
	 * @param coordinates The coordinates to sample from.
	 * @return The value at the specified coordinates.
	 */
	public T getPixel(VectorReal coordinates) {
		int idx = (int) coordinates.toPixelIndex(shape);
		return image[idx];
	}

	@Override
	public VectorInt getShape() {
		return shape;
	}

	@Override
	public int getRank() {
		return shape.size();
	}

}
