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

package org.vpac.ndg.query.iteration;

import java.util.Iterator;

import org.vpac.ndg.query.QueryConfigurationException;

/**
 * Iterates over two iterables in unison.
 * @author Alex Fraser
 *
 * @param <T> The type contained in the first iterable.
 * @param <U> The type contained in the second iterable.
 */
public class Zip<T, U> implements Iterable<Pair<T, U>> {

	Iterable<T> a;
	Iterable<U> b;
	ZipIterator iter;
	Pair<T, U> pair;

	public Zip(Iterable<T> a, Iterable<U> b) throws QueryConfigurationException {
		this.a = a;
		this.b = b;
		iter = new ZipIterator();
	}

	@Override
	public Iterator<Pair<T, U>> iterator() {
		iter.ai = a.iterator();
		iter.bi = b.iterator();
		return iter;
	}

	final class ZipIterator implements Iterator<Pair<T, U>> {

		Iterator<T> ai;
		Iterator<U> bi;

		@Override
		public boolean hasNext() {
			return ai.hasNext() && bi.hasNext();
		}

		@Override
		public Pair<T, U> next() {
			pair.a = ai.next();
			pair.b = bi.next();
			return pair;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"Can't remove value from a zip.");
		}
	}
}
