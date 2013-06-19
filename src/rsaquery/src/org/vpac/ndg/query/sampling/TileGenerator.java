package org.vpac.ndg.query.sampling;

import java.util.Iterator;

import org.vpac.ndg.query.math.VectorInt;

/**
 * Allows iteration over a tile grid.
 * @author Alex Fraser
 */
public class TileGenerator implements Iterable<VectorInt> {

	private VectorInt tileGridShape;
	private VectorInt currentTile;

	public TileGenerator(VectorInt tileGridShape) {
		this.tileGridShape = tileGridShape;
		currentTile = VectorInt.createEmpty(tileGridShape.size());
		currentTile.setX(-1);
	}

	@Override
	public Iterator<VectorInt> iterator() {
		return new TileIterator();
	}

	private class TileIterator implements Iterator<VectorInt> {

		@Override
		public boolean hasNext() {
			for (int i = 0; i < currentTile.size(); i++) {
				if (currentTile.get(i) < (tileGridShape.get(i) - 1))
					return true;
			}
			return false;
		}

		@Override
		public VectorInt next() {
			currentTile.incr(tileGridShape);
			return currentTile;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Can't remove tiles.");
		}

	}

}
