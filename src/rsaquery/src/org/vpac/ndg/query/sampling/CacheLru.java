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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a least-recently-used, limited size map.
 * @author Alex Fraser
 */
class CacheLru<K, V> implements Map<K, V> {

	Logger log = LoggerFactory.getLogger(CacheLru.class);

	private LinkedHashMap<K, V> store;
	private int capacity;

	public CacheLru(final int capacity) {
		// Create an LRU cache.
		store = new LinkedHashMap<K, V>(capacity + 1, 0.75f, true) {
			private static final long serialVersionUID = 5053321447670749987L;

			@Override
			protected boolean removeEldestEntry(Entry<K, V> eldest) {
				log.trace("Releasing {}", eldest.getKey());
				return size() > capacity;
			}
		};
		this.capacity = capacity;
	}

	@Override
	public void clear() {
		store.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return store.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return store.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return store.entrySet();
	}

	@Override
	public V get(Object key) {
		return store.get(key);
	}

	@Override
	public boolean isEmpty() {
		return store.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return store.keySet();
	}

	@Override
	public V put(K key, V value) {
		return store.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		store.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return store.remove(key);
	}

	@Override
	public int size() {
		return store.size();
	}

	@Override
	public Collection<V> values() {
		return store.values();
	}

	public int getCapacity() {
		return capacity;
	}

}
