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

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Iterates over coordinates and values within a window of pixels.
 * @author Alex Fraser
 */
public class Kernel<T> implements Iterable<KernelPair<T>> {

	VectorInt current;
	VectorReal origin;
	VectorInt shape;
	VectorInt radius;
	T[] image;
	int i;
	KernelIterator iter;
	KernelPair<T> pair;

	public Kernel(VectorInt shape, T[] image) throws QueryConfigurationException {
		this.shape = shape;
		if (image.length != shape.volume()) {
			throw new QueryConfigurationException(String.format("Kernel " +
					"shape %s does not match number of kernel elements %d",
					shape, image.length));
		}
		radius = shape.divNew(2);
		this.image = image;
		iter = new KernelIterator();
		pair = new KernelPair<T>(shape.size());
		current = VectorInt.createEmpty(shape.size());
		origin = VectorReal.createEmpty(shape.size());
	}

	public Iterable<KernelPair<T>> setCentre(VectorReal coordinates) {
		origin.subOf(coordinates, radius);
		return this;
	}

	@Override
	public Iterator<KernelPair<T>> iterator() {
		i = 0;
		current.set(0);
		return iter;
	}

	final class KernelIterator implements Iterator<KernelPair<T>> {

		@Override
		public boolean hasNext() {
			return i < image.length;
		}

		@Override
		public KernelPair<T> next() {
			pair.value = image[i];
			pair.coordinates.addOf(origin, current);

			current.incr(shape);
			i++;
			return pair;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"Can't remove value from a kernel window.");
		}
	}

}
