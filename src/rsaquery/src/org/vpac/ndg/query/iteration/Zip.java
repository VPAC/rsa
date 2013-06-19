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
