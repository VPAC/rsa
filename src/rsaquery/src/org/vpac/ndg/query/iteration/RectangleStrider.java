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

import java.util.Iterator;

import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Iterates over coordinates within a rectangular window of pixels.
 * @author Alex Fraser
 */
public class RectangleStrider implements Iterable<CoordinatePair> {

	VectorInt current;
	VectorReal origin;
	VectorInt shape;
	VectorInt radius;
	int i;
	long n;
	int stride;
	int offset;
	SelfIterator iter;
	CoordinatePair coords;

	public RectangleStrider(VectorInt shape, int stride, int offset) {
		this.shape = shape;
		this.stride = stride;
		this.offset = offset;
		n = shape.volume();
		radius = shape.divNew(2);
		iter = new SelfIterator();
		current = VectorInt.createEmpty(shape.size());
		origin = VectorReal.createEmpty(shape.size());
		coords = new CoordinatePair(shape.size());
	}

	public RectangleStrider setOrigin(VectorReal coordinates) {
		origin.set(coordinates);
		return this;
	}

	public RectangleStrider setCentre(VectorReal coordinates) {
		origin.subOf(coordinates, radius);
		return this;
	}

	@Override
	public Iterator<CoordinatePair> iterator() {
		i = offset;
		current.set(0);
		current.setX(offset);
		current.wrapInPlace(shape);
		return iter;
	}

	private final class SelfIterator implements Iterator<CoordinatePair> {

		@Override
		public boolean hasNext() {
			return i < n;
		}

		@Override
		public CoordinatePair next() {
			coords.imageIndex.set(current);
			coords.coordinates.addOf(origin, current);

			current.setX(current.getX() + stride);
			current.wrapInPlace(shape);
			i += stride;
			return coords;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"Can't remove value from a rectangle.");
		}
	}

}
