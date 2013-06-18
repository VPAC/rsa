package org.vpac.ndg.query.iteration;

import java.util.Iterator;

import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Iterates over coordinates within a rectangular window of pixels.
 * @author Alex Fraser
 */
public class Rectangle implements Iterable<CoordinatePair> {

	VectorInt current;
	VectorReal origin;
	VectorInt shape;
	VectorInt radius;
	int i;
	long n;
	SelfIterator iter;
	CoordinatePair coords;

	public Rectangle(VectorInt shape) {
		this.shape = shape;
		n = shape.volume();
		radius = shape.divNew(2);
		iter = new SelfIterator();
		current = VectorInt.createEmpty(shape.size());
		origin = VectorReal.createEmpty(shape.size());
		coords = new CoordinatePair(shape.size());
	}

	public Rectangle setOrigin(VectorReal coordinates) {
		origin.set(coordinates);
		return this;
	}

	public Rectangle setCentre(VectorReal coordinates) {
		origin.subOf(coordinates, radius);
		return this;
	}

	@Override
	public Iterator<CoordinatePair> iterator() {
		i = 0;
		current.set(0);
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
			current.incr(shape);
			i++;
			return coords;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"Can't remove value from a rectangle.");
		}
	}

}
