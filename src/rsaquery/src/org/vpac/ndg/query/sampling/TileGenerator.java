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
